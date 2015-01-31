/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri (dot) de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftttinterpreter;

  /* ===================================================================== */
  /*    TTExecContextRec                                                          */
  /*                                                                       */
  /* ===================================================================== */


import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTSizeMetricsRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCallRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCodeRange;
import org.apwtcl.apwfreetypelib.afttruetype.TTDefRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTDefaultGraphicsStateClass;
import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTMaxProfileRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTSizeMetricsRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTSizeRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTExecContextRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTExecContextRec";
  private final static int MAX_STACK_SIZE = 32;
  private final static int MAX_CALL_SIZE = 32;
  private final static int MAX_ARGS = 4096;

  public TTFaceRec face = null;
  protected TTSizeRec size = null;
    /* instructions state */
  protected FTError.ErrorTag error = FTError.ErrorTag.ERR_OK; /* last execution error */
  public int top = 0;                         /* top of exec. stack   */
  protected int stackSize = MAX_STACK_SIZE;   /* size of exec. stack  */
  protected int[] stack = null;               /* current exec. stack  */
  protected int stack_idx = 0;
  protected int[] args = null;
  protected int new_top = 0;                  /* new top after exec.  */
  public TTGlyphZoneRec zp0 = null;           /* zone records */
  public TTGlyphZoneRec zp1 = null;
  public TTGlyphZoneRec zp2 = null;
  public TTGlyphZoneRec pts = null;
  protected TTGlyphZoneRec twilight = null;
  public FTSizeMetricsRec metrics = null;
  public TTSizeMetricsRec tt_metrics = null;  /* size metrics */
  public org.apwtcl.apwfreetypelib.aftttinterpreter.TTGraphicsStateRec graphics_state = null;       /* current graphics state */
  public TTInterpTags.CodeRange curRange = TTInterpTags.CodeRange.NONE;             /* current code range number   */
  public TTOpCode.OpCode[] code = null;       /* current code range          */
  public short[] cvt_code = null;             /* current code range          */
  public int IP = 0;                          /* current instruction pointer */
  public int codeSize = 0;                    /* size of current range       */
  protected TTOpCode.OpCode opcode;           /* current opcode              */
  protected int length = 0;                   /* length of current opcode    */
  protected boolean step_ins = false;         /* true if the interpreter must */
                                                /* increment IP after ins. exec */
  protected int cvtSize = 0;
  public int[] cvt = null;
  public int glyphSize = 0;                   /* glyph instructions buffer size */
  public TTOpCode.OpCode[] glyphIns = null;   /* glyph instructions buffer */
  public int numFDefs = 0;                    /* number of function defs         */
  public int maxFDefs = 0;                    /* maximum number of function defs */
  public TTDefRec[] FDefs = null;             /* table of FDefs entries          */
  public int numIDefs = 0;                    /* number of instruction defs */
  public int maxIDefs = 0;                    /* maximum number of ins defs */
  public TTDefRec[] IDefs = null;             /* table of IDefs entries     */
  public int maxFunc = 0;                     /* maximum function index     */
  public int maxIns = 0;                      /* maximum instruction index  */
  public int callTop = 0;                     /* top of call stack during execution */
  protected int callSize = MAX_CALL_SIZE;     /* size of call stack */
  protected TTCallRec[] callStack = null;     /* call stack */
  protected short maxPoints = 0;              /* capacity of this context's `pts' */
  protected short maxContours = 0;            /* record, expressed in points and  */
                                              /* contours.                        */
  public TTCodeRange[] codeRangeTable = null; /* table of valid code ranges */
                                              /* useful for the debugger   */
  protected int storeSize = 0;                /* size of current storage */
  protected int[] storage = null;             /* storage area            */
  public int period = 0;                      /* values used for the */
  public int phase = 0;                       /* `SuperRounding'     */
  public int threshold = 0;
  public boolean instruction_trap = false;    /* If `True', the interpreter will */
                                              /* exit after each instruction     */
  protected org.apwtcl.apwfreetypelib.aftttinterpreter.TTGraphicsStateRec default_GS = null; /* graphics state resulting from   */
                                              /* the prep program                */
  public boolean is_composite = false;        /* true if the glyph is composite  */
  public boolean pedantic_hinting = false;    /* true if pedantic interpretation */
  /* latest interpreter additions */
  public int F_dot_P = 0;                     /* dot product of freedom and projection vectors */
  public boolean grayscale = false;           /* are we hinting for grayscale? */

  public TTRenderFunc render_funcs = null;

  /* ==================== TTExecContextRec ================================== */
  public TTExecContextRec() {
    oid++;
    id = oid;

    stack = new int[stackSize];
    args = new int[MAX_ARGS];
    zp0 = new TTGlyphZoneRec();
    zp1 = new TTGlyphZoneRec();
    zp2 = new TTGlyphZoneRec();
    pts = new TTGlyphZoneRec();
    twilight = new TTGlyphZoneRec();
    metrics = new FTSizeMetricsRec();
    tt_metrics = new TTSizeMetricsRec();
    graphics_state = new org.apwtcl.apwfreetypelib.aftttinterpreter.TTGraphicsStateRec();
    cvt = new int[1024];
    glyphIns = new TTOpCode.OpCode[1];
    callStack = new TTCallRec[callSize];
    codeRangeTable = new TTCodeRange[TTSizeRec.TT_MAX_CODE_RANGES];
    for (int i = 0; i < TTSizeRec.TT_MAX_CODE_RANGES; i++) {
      codeRangeTable[i] = new TTCodeRange();
    }
    storage = new int[1024];
    default_GS = new TTDefaultGraphicsStateClass();
    /* latest interpreter additions */
    render_funcs = new TTRenderFunc();
  }
    
  /* ==================== mySelf ================================== */
  public String mySelf() {
    String str = TAG+"!"+id+"!";
    return str;
  }
        
  /* ==================== toString ===================================== */
  public String toString() {
    StringBuffer str = new StringBuffer(mySelf()+"!");
    return str.toString();
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    return str.toString();
  }

  /* ==================== SkipCode ========================= */
  public boolean SkipCode() {
    IP += length;
    if (IP < codeSize) {
      opcode = TTOpCode.OpCode.getTableTag(code[IP].getVal() & 0xFF);
      length = opcode.getOpCodeLength();
Debug(0, DebugTag.DBG_INTERP, TAG, "SkipCode: " + opcode);
      if (length < 0) {
        if (IP + 1 >= codeSize) {
          error = FTError.ErrorTag.INTERP_CODE_OVERFLOW;
          return false;
        }
        length = 2 - length * (code[IP + 1].getVal() & 0xFF);
      }
      if (IP + length <= codeSize) {
        return true;
      }
    }
    return false;
  }

  /* =====================================================================
   * <Function>
   *    InitContext
   *
   * <Description>
   *    Initializes a context object.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag InitContext() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    callSize = 32;
    callStack = new TTCallRec[callSize];
    for (int i = 0; i < callSize; i++) {
      callStack[i] = new TTCallRec();
    }
      /* all values in the context are set to 0 already, but this is */
      /* here as a remainder                                         */
    maxPoints = 0;
    maxContours = 0;
    stackSize = 0;
    glyphSize = 0;
    stack = null;
    glyphIns = null;
    face = null;
    size = null;
    return error;
  }

  /* =====================================================================
   * TTLoadContext
   *
   * <Description>
   *    Prepare an execution context for glyph hinting.
   *
   * <Input>
   *    face :: A handle to the source face object.
   *
   *    size :: A handle to the source size object.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * <Note>
   *    Only the glyph loader and debugger should call this function.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTLoadContext(TTFaceRec face, TTSizeRec size) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTLoadContext");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int i;
    TTMaxProfileRec maxp ;

    this.face = face;
    maxp = face.getMax_profile();
    this.size = size;
    if (size != null) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "num_function_defs: "+size.getNum_function_defs()+"!"+numFDefs+"!");
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "max_function_defs: "+size.getMax_function_defs()+"!"+maxFDefs+"!");
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "function_defs: "+size.getFunction_defs()+"=="+FDefs+"!");
      numFDefs = size.getNum_function_defs();
      maxFDefs = size.getMax_function_defs();
      numIDefs = size.getNum_instruction_defs();
      maxIDefs = size.getMax_instruction_defs();
      FDefs = size.getFunction_defs();
      IDefs = size.getInstruction_defs();
      tt_metrics = size.getTtmetrics();
      metrics  = size.getMetrics();
      maxFunc = size.getMax_func();
      maxIns = size.getMax_ins();
      for (i = 0; i < TTSizeRec.TT_MAX_CODE_RANGES; i++) {
        codeRangeTable[i] = size.getCodeRangeTable()[i];
      }
      /* set graphics state */
      graphics_state = size.getGraphics_state();
      cvtSize = (int)size.getCvt_size();
      cvt = size.getCvt();
      storeSize = size.getStorage_size();
      storage = size.getStorage();
      twilight = size.getTwilight();
      /* In case of multi-threading it can happen that the old size object */
      /* no longer exists, thus we must clear all glyph zone references.   */
      // ft_memset( &exec.zp0, 0, sizeof ( exec.zp0 ) );
      // exec.zp1 = exec.zp0;
      // exec.zp2 = exec.zp0;
      // ATTENTION!! have to call reset otherwise the references are wrong in
      // for example DirectMoveX!!
      zp0.reset();
      zp1.reset();
      zp2.reset();
    }
    /* XXX: We reserve a little more elements on the stack to deal safely */
    /*      with broken fonts like arialbs, courbs, timesbs, etc.         */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "face.id: "+face+"!"+face.getMax_profile()+"!");
    stackSize = face.getMax_profile().getMaxStackElements() + 32;
      // tmp = exec.stackSize;
//      error = UpdateMax(exec.memory, &tmp, sizeof ( FT_F26Dot6 ), (void*)&exec.stack, maxp.maxStackElements + 32 );
    int[] tmp1 = null;
    if (stack != null) {
      tmp1 = new int[stackSize];
      tmp1 = java.util.Arrays.copyOf(stack, stackSize);
    }
    stack = new int[maxp.getMaxStackElements() + 32];
    if (tmp1 != null) {
      stack = java.util.Arrays.copyOf(tmp1, stackSize);
    }
    for (int k = stackSize; k < maxp.getMaxStackElements() + 32; k++) {
      stack[k] = 0;
    }
    stackSize = maxp.getMaxStackElements() + 32;
    if (stackSize > stack.length) {
      Log.e(TAG, "exec.stackSize > exec.stack!!");
      error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
      return error;
    }
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    glyphSize = face.getMax_profile().getMaxSizeOfInstructions();
//      tmp = exec.glyphSize;
//      error = Update_Max(exec.memory, &tmp, sizeof ( FT_Byte ), (void*)&exec.glyphIns, maxp.maxSizeOfInstructions );
    TTOpCode.OpCode[] tmp2 = null;
    if (glyphIns != null) {
      tmp2 = new TTOpCode.OpCode[glyphSize];
      tmp2 = java.util.Arrays.copyOf(glyphIns, glyphSize);
    }
    glyphIns = new TTOpCode.OpCode[maxp.getMaxSizeOfInstructions()];
    if (tmp2 != null) {
      glyphIns = java.util.Arrays.copyOf(tmp2, glyphSize);
    }
    for (int j = glyphSize; j < maxp.getMaxSizeOfInstructions(); j++) {
      glyphIns[j] = TTOpCode.OpCode.SVTCA_y;
    }
    glyphSize = maxp.getMaxSizeOfInstructions();
    if (glyphSize > glyphIns.length) {
      Log.e(TAG, "exec.glyphSize > exec.glyphIns!!");
      error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
      return error;
    }
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    // exec.zp1 = exec.pts;
    // exec.zp2 = exec.pts;
    // exec.zp0 = exec.pts;
    zp1.copy(pts);
    zp2.copy(pts);
    zp0.copy(pts);
    pts.setN_points(0);
    pts.setN_contours(0);

    instruction_trap = false;
    return error;
  }

  /* =====================================================================
   * TTRunContext
   * =====================================================================
   */

  public FTError.ErrorTag TTRunContext(boolean debug) {
    FTError.ErrorTag error;
Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, "TT_Run_Context" + TTInterpTags.CodeRange.GLYPH+" zp0.cur: "+zp0.getCur()+"\n zp1.cur: "+zp1.getCur());

    if ((error = TTGotoCodeRange(TTInterpTags.CodeRange.GLYPH, 0)) != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    zp0 = pts;
    zp1 = pts;
    zp2 = pts;
Debug(0, DebugTag.DBG_INTERP, TAG, "TT_Run_Context2 zp0.cur: "+zp0.getCur()+" \nzp1.cur: "+zp1.getCur());
    for( int i = 0; i < 6; i++) {
//  Debug(0, DebugTag.DBG_INTERP, TAG, String.format("zp1.org: %d x: %d, y: %d\n", i, cur.zp1.org[i].x, cur.zp1.org[i].y));
//  Debug(0, DebugTag.DBG_INTERP, TAG, String.format("zp0.org: %d x: %d, y: %d\n", i, cur.zp0.org[i].x, cur.zp0.org[i].y));
    }
    graphics_state.setGep0(1);
    graphics_state.setGep1(1);
    graphics_state.setGep2(1);
    graphics_state.getProjVector().setX(0x4000);
    graphics_state.getProjVector().setY(0x0000);
    graphics_state.getFreeVector().setX(graphics_state.getProjVector().getX());
    graphics_state.getFreeVector().setY(graphics_state.getProjVector().getY());
    graphics_state.getDualVector().setX(graphics_state.getProjVector().getX());
    graphics_state.getDualVector().setY(graphics_state.getProjVector().getY());
    graphics_state.setRound_state(TTInterpTags.Round.To_Grid);
    graphics_state.setLoop(1);
    /* some glyphs leave something on the stack. so we clean it */
    /* before a new execution.                                  */
    top = 0;
    callTop = 0;
DebugTag.DBG_INTERP.setLevel(0);
    return face.Interpreter(this);
  }

  /* =====================================================================
   * TTSetCodeRange
   *
   * <Description>
   *    Sets a code range.
   *
   * <Input>
   *    range  :: The code range index.
   *
   *    base   :: The new code base.
   *
   *    length :: The range size in bytes.
   *
   * <InOut>
   *    exec   :: The target execution context.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTSetCodeRange(TTInterpTags.CodeRange range, TTOpCode.OpCode[] byte_base, short[] short_base, int length) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTSetCodeRange: "+range+"!"+short_base+"!"+byte_base+"!"+length+"!");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    if (range.getVal() < 1 || range.getVal() > 3) {
      error = FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      return error;
    }
    codeRangeTable[range.getVal() - 1].short_base = short_base;
    codeRangeTable[range.getVal() - 1].base = byte_base;
    codeRangeTable[range.getVal() - 1].size = length;
    return error;
  }

  /* =====================================================================
   * TTClearCodeRange
   *
   * <Description>
   *    Clears a code range.
   *
   * <Input>
   *    range :: The code range index.
   *
   * <InOut>
   *    exec  :: The target execution context.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * <Note>
   *    Does not set the Error variable.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTClearCodeRange(TTInterpTags.CodeRange range) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTClearCodeRange");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    if (range.getVal() < 1 || range.getVal() > 3) {
      error = FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      return error;
    }
    codeRangeTable[range.getVal() - 1].base = null;
    codeRangeTable[range.getVal() - 1].size = 0;
    return error;
  }

  /* =====================================================================
   * TTGotoCodeRange
   *
   * <Description>
   *    Switches to a new code range (updates the code related elements in
   *    `exec', and `IP').
   *
   * <Input>
   *    range :: The new execution code range.
   *
   *    IP    :: The new IP in the new code range.
   *
   * <InOut>
   *    exec  :: The target execution context.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTGotoCodeRange(TTInterpTags.CodeRange range, int IP) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTGotoCodeRange: "+range);
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    if (range.getVal() < TTInterpTags.CodeRange.FONT.getVal() || range.getVal() > TTInterpTags.CodeRange.GLYPH.getVal()) {
      error = FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      return error;
    }
    if (codeRangeTable[range.getVal() - 1].base == null) {
      error = FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      return error;
    }
      /* NOTE: Because the last instruction of a program may be a CALL */
      /*       which will return to the first byte *after* the code    */
      /*       range, we test for IP <= Size instead of IP < Size.     */
      /*                                                               */
    if (IP > codeRangeTable[range.getVal() - 1].size) {
      error = FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      return error;
    }
    code = codeRangeTable[range.getVal() - 1].base;
    cvt_code = codeRangeTable[range.getVal() - 1].short_base;
    codeSize = codeRangeTable[range.getVal() - 1].size;
Debug(2, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTGotoCodeRange: size: %d", codeSize));
    this.IP = IP;
    curRange = range;
    return error;
  }

  /* ==================== funcRound ===================================== */
  protected int funcRound(int distance, int compensation) {     /* current rounding function */
    Log.e(TAG, "funcRound not yet implemented");
    return render_funcs.curr_round_func.round(distance, compensation);
  }

  /* ==================== funcProject ===================================== */
  protected int funcProject (int dx, int dy) {   /* current projection function */
    Log.e(TAG, "funcProject not yet fully implemented");
    return render_funcs.curr_project_func.project(dx, dy);
  }

  /* ==================== funcDualproj ===================================== */
  protected int funcDualproj(int dx, int dy) {  /* current dual proj. function */
    Log.e(TAG, "funcDualproj not yet fully implemented");
    return render_funcs.curr_project_func.dualproject(dx, dy);
  }

  /* ==================== funcFreeProj ===================================== */
  protected FTError.ErrorTag funcFreeProj() {  /* current freedom proj. func  */
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "funcFreeProj not yet implemented");
    return error;
  }

  /* ==================== funcMove ===================================== */
  protected void funcMove(TTGlyphZoneRec zone, int point, int distance) {      /* current point move function */
    Log.e(TAG, "funcMove not yet implemented");
    render_funcs.curr_move_func.move(zone, point, distance);
  }

  /* ==================== funcMoveOrig ===================================== */
  protected void funcMoveOrig(TTGlyphZoneRec zone, int point, int distance) { /* move original position function */
    Log.e(TAG, "funcMoveOrig not yet fully implemented");
    render_funcs.curr_move_func.moveOrig(zone, point, distance);
  }

  /* ==================== funcReadCvt ===================================== */
  protected int funcReadCvt(int idx) {   /* read a cvt entry              */
    Log.e(TAG, "funcReadCvt not yet fully implemented");
    return render_funcs.curr_cvt_func.readCvt(idx);
  }

  /* ==================== funcWriteCvt ===================================== */
  protected void funcWriteCvt(int idx, int value) { /* write a cvt entry (in pixels) */
    Log.e(TAG, "funcWriteCvt not yet fully implemented");
    render_funcs.curr_cvt_func.writeCvt(idx, value);
  }

  /* ==================== funcMoveCvt ===================================== */
  protected FTError.ErrorTag funcMoveCvt(int val1, int val2) {  /* incr a cvt entry (in pixels)  */
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "funcMoveCvt not yet implemented");
    return render_funcs.curr_cvt_func.moveCvt(val1, val2);
  }

  public FTError.ErrorTag RunInstructions() {
    Log.e(TAG, "RunInstructions may not be called directly");
    return FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
  }

}