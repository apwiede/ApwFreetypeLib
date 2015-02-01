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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTSizeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTSizeMetricsRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRequestRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTGraphicsStateRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTInterpTags;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTSizeRec extends FTSizeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTSizeRec";
    
  public final static int TT_MAX_CODE_RANGES = 3;

  /* we have our own copy of metrics so that we can modify */
  /* it without affecting auto-hinting (when used)         */
  private FTSizeMetricsRec local_metrics = null;
  private TTSizeMetricsRec ttmetrics = null;
  private int strike_index = 0;      /* 0xFFFFFFFF to indicate invalid */
  private int num_function_defs = 0; /* number of function definitions */
  private int max_function_defs = 0;
  private TTDefRec[] function_defs = null;     /* table of function definitions  */
  private int num_instruction_defs = 0;  /* number of ins. definitions */
  private int max_instruction_defs = 0;
  private TTDefRec[] instruction_defs = null;      /* table of ins. definitions  */
  private int max_func = 0;
  private int max_ins = 0;
  private TTCodeRange[] codeRangeTable = null;
  private TTGraphicsStateRec graphics_state = null;
  private int cvt_size = 0;      /* the scaled control value table */
  private int[] cvt = null;
  private int storage_size = 0; /* The storage area is now part of */
  private int[] storage = null;      /* the instance                    */
  private TTGlyphZoneRec twilight = null;     /* The instance's twilight zone    */
  /* debugging variables */
  /* When using the debugger, we must keep the */
  /* execution context tied to the instance    */
  /* object rather than asking it on demand.   */
  private boolean debug = false;
  private TTExecContextRec context = null;
  private boolean bytecode_ready = false;
  private boolean cvt_ready = false;

  /* ==================== TTSizeRec ================================== */
  public TTSizeRec() {
    oid++;
    id = oid;
    local_metrics = new FTSizeMetricsRec();
    ttmetrics = new TTSizeMetricsRec();
    function_defs = new TTDefRec[1];
    instruction_defs = new TTDefRec[1];
    codeRangeTable = new TTCodeRange[TT_MAX_CODE_RANGES];
    for (int i = 0; i < TT_MAX_CODE_RANGES; i++) {
      codeRangeTable[i] = new TTCodeRange();
    }
    graphics_state = new TTDefaultGraphicsStateClass();
    twilight = new TTGlyphZoneRec();
Debug(0, DebugTag.DBG_INIT, TAG, "exec1 ttsize: "+context+"!");
  }
    
  /* ==================== mySelf ================================== */
  public String mySelf() {
      return TAG+"!"+id+"!";
    }
        
  /* ==================== toString ===================================== */
  public String toString() {
      return mySelf()+"!";
    }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("..strike_index: "+strike_index+'\n');
    str.append("..num_function_defs: "+num_function_defs+'\n');
    str.append("..max_function_defs: "+max_function_defs+'\n');
    str.append("..num_instruction_defs: "+num_instruction_defs+'\n');
    str.append("..max_instruction_defs: "+max_instruction_defs+'\n');
    str.append("..max_func: "+max_func+'\n');
    str.append("..max_ins: "+max_ins+'\n');
    str.append("..cvt_size: "+cvt_size+'\n');
    str.append("..storage_size: "+storage_size+'\n');
    str.append("..debug: "+debug+'\n');
    str.append("..bytecode_ready: "+bytecode_ready+'\n');
    str.append("..cvt_ready: "+cvt_ready+'\n');
    return str.toString();
  }

  /* =====================================================================
  * <Function>
  *    tt_size_init
  *
  * <Description>
  *    Initialize a new TrueType size object.
  *
  * <InOut>
  *    size :: A handle to the size object.
  *
  * <Return>
  *    FreeType error code.  0 means success.
  *
  * =====================================================================
 */
  public FTError.ErrorTag tt_size_init() {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_init");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    bytecode_ready = false;
    cvt_ready = false;
    ttmetrics.setValid(false);
    strike_index = 0xFFFFFFFF;
    return error;
  }

  /* =====================================================================
   *    tt_size_done
   *
   * =====================================================================
   */
  public void tt_size_done() {   /* TTFaceRec */
    Log.e(TAG, "tt_size_done not yet implemented!!");

  }

  /* =====================================================================
   *    tt_size_done_bytecode
   *
   * =====================================================================
   */
  public void tt_size_done_bytecode() {   /* TT_Face */
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_done_bytecode");

    cvt = null;
    cvt_size = 0;
      /* twilight zone */
    twilight.tt_glyphzone_done();
    function_defs = null;
    instruction_defs = null;
    num_function_defs = 0;
    max_function_defs = 0;
    num_instruction_defs = 0;
    max_func = 0;
    max_ins  = 0;
    bytecode_ready = false;
    cvt_ready = false;
  }

  /* =====================================================================
   * tt_size_request
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_request(FTSizeRequestRec req) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_request");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    face.getSize().getMetrics().RequestMetrics(face, req);
    if ((face.getFace_flags() & Flags.Face.SCALABLE.getVal()) != 0) {
      error = tt_size_reset();
    }
    return error;
  }

  /* =====================================================================
   * tt_size_reset
   *
   * <Description>
   *    Reset a TrueType size when resolutions and character dimensions
   *    have been changed.
   *
   * <Input>
   *    size :: A handle to the target size object.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_reset() {
    TTFaceRec ttface = (TTFaceRec)face;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_reset");
    ttmetrics.setValid(false);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "facse ascender: "+ttface.getAscender()+" "+ttface.getDescender()+" "+ttface.getHeight());
      /* copy the result from base layer */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "RES: "+metrics.getX_ppem()+"!"+metrics.getY_ppem()+"!"+local_metrics.getX_ppem()+"!"+local_metrics.getY_ppem()+"!");
    local_metrics.copy(metrics);
    if (local_metrics.getX_ppem() < 1 || local_metrics.getY_ppem() < 1) {
      return FTError.ErrorTag.GLYPH_INVALID_PPEM;
    }
      /* This bit flag, if set, indicates that the ppems must be       */
      /* rounded to integers.  Nearly all TrueType fonts have this bit */
      /* set, as hinting won't work really well otherwise.             */
      /*                                                               */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "face.header.flags: "+(ttface.getHeader().getFlags() & 8)+"!");
    if ((ttface.getHeader().getFlags() & 8) != 0) {
      local_metrics.setX_scale(FTCalc.FTDivFix(local_metrics.getX_ppem() << 6, ttface.getUnits_per_EM()));
      local_metrics.setY_scale(FTCalc.FTDivFix(local_metrics.getY_ppem() << 6, ttface.getUnits_per_EM()));

      local_metrics.setAscender(FTCalc.FT_PIX_ROUND(FTCalc.FTMulFix(ttface.getAscender(), local_metrics.getY_scale())));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "ascender: "+local_metrics.getAscender()+" "+ttface.getAscender()+" "+local_metrics.getY_scale());
      local_metrics.setDescender(FTCalc.FT_PIX_ROUND(FTCalc.FTMulFix(ttface.getDescender(), local_metrics.getY_scale())));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "descender: "+local_metrics.getDescender()+" "+ttface.getDescender()+" "+local_metrics.getY_scale());
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "descender2: "+metrics.getDescender()+" "+ttface.getDescender()+" "+local_metrics.getY_scale());
      local_metrics.setHeight(FTCalc.FT_PIX_ROUND(FTCalc.FTMulFix(ttface.getHeight(), local_metrics.getY_scale())));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "height: "+local_metrics.getHeight()+" "+ttface.getHeight()+" "+local_metrics.getY_scale());
      local_metrics.setMax_advance(FTCalc.FT_PIX_ROUND(FTCalc.FTMulFix(ttface.getMax_advance_width(), local_metrics.getX_scale())));
    }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_reset: size: "+this+"!");
      /* compute new transformation */
    if (local_metrics.getX_ppem() >= local_metrics.getY_ppem()) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "x_scale: "+local_metrics.getX_scale()+"!"+ttmetrics.getScale()+"!");
      ttmetrics.setScale(local_metrics.getX_scale());
      ttmetrics.setPpem(local_metrics.getX_ppem());
      ttmetrics.setX_ratio(0x10000);
      ttmetrics.setY_ratio(FTCalc.FTDivFix(local_metrics.getY_ppem(), local_metrics.getX_ppem()));
    } else {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "y_scale: "+local_metrics.getX_scale()+"!"+ttmetrics.getScale()+"!");
      ttmetrics.setScale(local_metrics.getY_scale());
      ttmetrics.setPpem(local_metrics.getY_ppem());
      ttmetrics.setX_ratio(FTCalc.FTDivFix(local_metrics.getX_ppem(), local_metrics.getY_ppem()));
      ttmetrics.setY_ratio(0x10000);
    }
    cvt_ready = false;
    if (error == FTError.ErrorTag.ERR_OK) {
      ttmetrics.setValid(true);
    }
    return error;
  }

  /* =====================================================================
   * tt_size_ready_bytecode
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_ready_bytecode(boolean pedantic) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_ready_bytecode");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTFaceRec ttface = (TTFaceRec)face;

    if (!bytecode_ready) {
      error = tt_size_init_bytecode(pedantic);
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
    }
      /* rescale CVT when needed */
    if (!cvt_ready) {
      int i;

        /* Scale the cvt values to the new ppem.          */
        /* We use by default the y ppem to scale the CVT. */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_ready_bytecode: "+this+"!");
      for (i = 0; i < cvt_size; i++) {
        cvt[i] = TTUtil.FTMulFix(ttface.getCvt_table().getCvtValue(i), ttmetrics.getScale());
        Debug(-1, DebugTag.DBG_LOAD_GLYPH, TAG, "size->cvt: i: "+i+" cvt[i]: "+cvt[i]+" cvt_value: "+ttface.getCvt_table().getCvtValue(i)+" scale: "+ttmetrics.getScale()+"!");
      }
        /* all twilight points are originally zero */
      for (i = 0; i < twilight.getN_points(); i++) {
        twilight.resetOrgPoint(i);
        twilight.resetCurPoint(i);
      }
        /* clear storage area */
      for (i = 0; i < storage_size; i++) {
        storage[i] = 0;
      }
      graphics_state = new TTDefaultGraphicsStateClass();
      error = tt_size_run_prep(pedantic);
      if (error == FTError.ErrorTag.ERR_OK) {
        cvt_ready = true;
      }
    }
    return error;
  }

  /* =====================================================================
   * tt_size_run_prep
   *
   * <Description>
   *    Run the control value program.
   *
   * <Input>
   *    size     :: A handle to the size object.
   *
   *    pedantic :: Set if bytecode execution should be pedantic.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_run_prep(boolean pedantic) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_run_prep");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTExecContextRec exec;
    TTFaceRec ttface = (TTFaceRec)face;

      /* debugging instances have their own context */
    if (debug) {
      exec = context;
    } else {
      exec = ((TTDriverRec)ttface.getDriver()).context;
    }
    if (exec == null) {
      return FTError.ErrorTag.GLYPH_COULD_NOT_FIND_CONTEXT;
    }
    exec.TTLoadContext(ttface, this);
    exec.callTop = 0;
    exec.top     = 0;
    exec.instruction_trap = false;
    exec.pedantic_hinting = pedantic;
    exec.TTSetCodeRange(TTInterpTags.CodeRange.CVT, ttface.getPrep_table().getPrepProgram(), null, ttface.getPrep_table().getPrepProgramSize());
    exec.TTClearCodeRange(TTInterpTags.CodeRange.GLYPH);
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "cvt_program_size: "+ttface.getPrep_table().getPrepProgramSize()+"!");
    if (ttface.getPrep_table().getPrepProgramSize() > 0) {
      error = (exec.TTGotoCodeRange(TTInterpTags.CodeRange.CVT, 0) == FTError.ErrorTag.ERR_OK ? FTError.ErrorTag.ERR_OK : FTError.ErrorTag.UNEXPECTED_NULL_VALUE);
      if (error == FTError.ErrorTag.ERR_OK && !debug) {
        FTTrace.Trace(7, TAG, "Executing `prep' table.");
        if (exec.code == null) {
          return FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
        }
        error = ttface.Interpreter(exec);
      }
    } else {
      error = FTError.ErrorTag.ERR_OK;
    }

      /* UNDOCUMENTED!  The MS rasterizer doesn't allow the following */
      /* graphics state variables to be modified by the CVT program.  */
    exec.graphics_state.getDualVector().setX(0x4000);
    exec.graphics_state.getDualVector().setY(0);
    exec.graphics_state.getProjVector().setX(0x4000);
    exec.graphics_state.getProjVector().setY(0x0);
    exec.graphics_state.getFreeVector().setX(0x4000);
    exec.graphics_state.getFreeVector().setY(0x0);
    exec.graphics_state.setRp0(0);
    exec.graphics_state.setRp1(0);
    exec.graphics_state.setRp2(0);
    exec.graphics_state.setGep0(1);
    exec.graphics_state.setGep1(1);
    exec.graphics_state.setGep2(1);
    exec.graphics_state.setLoop(1);
      /* save as default graphics state */
    graphics_state = exec.graphics_state;
    TTSaveContext(exec);
    return error;
  }

  /* =====================================================================
   * tt_size_init_bytecode
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_init_bytecode(boolean pedantic) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_init_bytecode");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTFaceRec ttface = (TTFaceRec)face;
    int i;
    int n_twilight;
    TTMaxProfileRec maxp = ttface.getMax_profile();
    bytecode_ready = true;
    cvt_ready = false;
    max_function_defs = maxp.getMaxFunctionDefs();
    max_instruction_defs = maxp.getMaxInstructionDefs();
    num_function_defs = 0;
    num_instruction_defs = 0;
    max_func = 0;
    max_ins = 0;
    cvt_size = ttface.getCvt_table().getCvtSize();
    storage_size = maxp.getMaxStorage();
      /* Set default metrics */
    {
      ttmetrics.setRotated(false);
      ttmetrics.setStretched(false);
        /* set default compensation (all 0) */
      for (i = 0; i < 4; i++) {
        ttmetrics.getCompensations()[i] = 0;
      }
    }
      /* allocate function defs, instruction defs, cvt, and storage area */
    function_defs = new TTDefRec[max_function_defs];
    for (i = 0; i < max_function_defs; i++) {
      function_defs[i] = new TTDefRec();
    }
    instruction_defs = new TTDefRec[max_instruction_defs];
    for (i = 0; i < max_instruction_defs; i++) {
      instruction_defs[i] = new TTDefRec();
    }
    cvt = new int[cvt_size];
    storage = new int[storage_size];
      /* reserve twilight zone */
    n_twilight = maxp.getMaxTwilightPoints();
      /* there are 4 phantom points (do we need this?) */
    n_twilight += 4;
    FTReference<TTGlyphZoneRec> zone_ref = new FTReference<TTGlyphZoneRec>();
    zone_ref.Set(twilight);
    error = TTGlyphZoneRec.tt_glyphzone_new(n_twilight, 0, zone_ref);
    twilight = zone_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      tt_size_done_bytecode();
      return error;
    }
    twilight.setN_points(n_twilight);
    graphics_state = new TTDefaultGraphicsStateClass();
      /* set `ttface.interpreter' according to the debug hook present */
    {
//        FTLibraryRec library = ttface.driver.library;
//        ttface.interpreter = library.debug_hooks[FT_DEBUG_HOOK_TRUETYPE];
    }
      /* Fine, now run the font program! */
    error = tt_size_run_fpgm(pedantic);
    if (error != FTError.ErrorTag.ERR_OK) {
      tt_size_done_bytecode();
    }
    return error;
  }

  /* =====================================================================
   * tt_size_run_fpgm
   *
   * <Description>
   *    Run the font program.
   *
   * <Input>
   *    size     :: A handle to the size object.
   *
   *    pedantic :: Set if bytecode execution should be pedantic.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_size_run_fpgm(boolean pedantic) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_size_run_fpgm");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTFaceRec ttface = (TTFaceRec)face;
    TTDriverRec ttdriver = (TTDriverRec)ttface.getDriver();
    TTExecContextRec exec;

      /* debugging instances have their own context */
    if (debug) {
      exec = context;
    } else {
      exec = ttdriver.context;
    }
    if (exec == null) {
      error = FTError.ErrorTag.GLYPH_COULD_NOT_FIND_CONTEXT;
      return error;
    }
    exec.TTLoadContext(ttface, this);
    exec.callTop = 0;
    exec.top     = 0;
    exec.period    = 64;
    exec.phase     = 0;
    exec.threshold = 0;
    exec.instruction_trap = false;
    exec.F_dot_P = 0x4000;
    exec.pedantic_hinting = pedantic;
    {
//FIXME!!
/* need to figure out how to avoid commenting out the next lines!!
        exec.metrics.x_ppem = 0;
        exec.metrics.y_ppem = 0;
        exec.metrics.x_scale = 0;
        exec.metrics.y_scale = 0;
        exec.tt_metrics.ppem = 0;
        exec.tt_metrics.scale = 0;
        exec.tt_metrics.ratio = 0x10000L;
*/
    }
      /* allow font program execution */
    exec.TTSetCodeRange(TTInterpTags.CodeRange.FONT, ttface.getFpgm_table().getFontProgram(), null, ttface.getFpgm_table().getFontProgramSize());
      /* disable CVT and glyph programs coderange */
    exec.TTClearCodeRange(TTInterpTags.CodeRange.CVT);
    exec.TTClearCodeRange(TTInterpTags.CodeRange.GLYPH);
    if (ttface.getFpgm_table().getFontProgramSize() > 0) {
      error = exec.TTGotoCodeRange(TTInterpTags.CodeRange.FONT, 0 /* IP */);
      if (error == FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, "Executing `fpgm' table.");
        error = ttface.Interpreter(exec);
      }
    } else {
      error = FTError.ErrorTag.ERR_OK;
    }
    if (error == FTError.ErrorTag.ERR_OK) {
      TTSaveContext(exec);
    }
    return error;
  }


  /* =====================================================================
   * TTSaveContext
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTSaveContext(TTExecContextRec exec) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTSaveContext");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int i;

      /* XXX: Will probably disappear soon with all the code range */
      /*      management, which is now rather obsolete.            */
      /*                                                           */
    num_function_defs = exec.numFDefs;
    function_defs = exec.FDefs;
    num_instruction_defs = exec.numIDefs;
    instruction_defs = exec.IDefs;
    max_func = exec.maxFunc;
    max_ins  = exec.maxIns;
    ttmetrics = exec.tt_metrics;
    metrics = exec.metrics;
    for (i = 0; i < TTSizeRec.TT_MAX_CODE_RANGES; i++) {
      codeRangeTable[i] = exec.codeRangeTable[i];
    }
    return error;
  }

  /* ==================== getLocal_metrics ================================== */
  public FTSizeMetricsRec getLocal_metrics() {
    return local_metrics;
  }

  /* ==================== setLocal_metrics ================================== */
  public void setLocal_metrics(FTSizeMetricsRec local_metrics) {
    this.local_metrics = local_metrics;
  }

  /* ==================== getTtmetrics ================================== */
  public TTSizeMetricsRec getTtmetrics() {
    return ttmetrics;
  }

  /* ==================== getTtmetrics ================================== */
  public void setTtmetrics(TTSizeMetricsRec ttmetrics) {
    this.ttmetrics = ttmetrics;
  }

  /* ==================== getStrike_index ================================== */
  public int getStrike_index() {
    return strike_index;
  }

  /* ==================== setStrike_index ================================== */
  public void setStrike_index(int strike_index) {
    this.strike_index = strike_index;
  }

  /* ==================== getNum_function_defs ================================== */
  public int getNum_function_defs() {
    return num_function_defs;
  }

  /* ==================== setNum_function_defs ================================== */
  public void setNum_function_defs(int num_function_defs) {
    this.num_function_defs = num_function_defs;
  }

  /* ==================== getMax_function_defs ================================== */
  public int getMax_function_defs() {
    return max_function_defs;
  }

  /* ==================== setMax_function_defs ================================== */
  public void setMax_function_defs(int max_function_defs) {
    this.max_function_defs = max_function_defs;
  }

  /* ==================== getFunction_defs ================================== */
  public TTDefRec[] getFunction_defs() {
    return function_defs;
  }

  /* ==================== setFunction_defs ================================== */
  public void setFunction_defs(TTDefRec[] function_defs) {
    this.function_defs = function_defs;
  }

  /* ==================== getNum_instruction_defs ================================== */
  public int getNum_instruction_defs() {
    return num_instruction_defs;
  }

  /* ==================== setNum_instruction_defs ================================== */
  public void setNum_instruction_defs(int num_instruction_defs) {
    this.num_instruction_defs = num_instruction_defs;
  }

  /* ==================== getMax_instruction_defs ================================== */
  public int getMax_instruction_defs() {
    return max_instruction_defs;
  }

  /* ==================== setMax_instruction_defs ================================== */
  public void setMax_instruction_defs(int max_instruction_defs) {
    this.max_instruction_defs = max_instruction_defs;
  }

  /* ==================== getInstruction_defs ================================== */
  public TTDefRec[] getInstruction_defs() {
    return instruction_defs;
  }

  /* ==================== setInstruction_defs ================================== */
  public void setInstruction_defs(TTDefRec[] instruction_defs) {
    this.instruction_defs = instruction_defs;
  }

  /* ==================== getMax_func ================================== */
  public int getMax_func() {
    return max_func;
  }

  /* ==================== setMax_func ================================== */
  public void setMax_func(int max_func) {
    this.max_func = max_func;
  }

  /* ==================== getMax_ins ================================== */
  public int getMax_ins() {
    return max_ins;
  }

  /* ==================== setMax_ins ================================== */
  public void setMax_ins(int max_ins) {
    this.max_ins = max_ins;
  }

  /* ==================== getCodeRangeTable ================================== */
  public TTCodeRange[] getCodeRangeTable() {
    return codeRangeTable;
  }

  /* ==================== setCodeRangeTable ================================== */
  public void setCodeRangeTable(TTCodeRange[] codeRangeTable) {
    this.codeRangeTable = codeRangeTable;
  }

  /* ==================== getGraphics_state ================================== */
  public TTGraphicsStateRec getGraphics_state() {
    return graphics_state;
  }

  /* ==================== setGraphics_state ================================== */
  public void setGraphics_state(TTGraphicsStateRec graphics_state) {
    this.graphics_state = graphics_state;
  }

  /* ==================== getCvt_size ================================== */
  public int getCvt_size() {
    return cvt_size;
  }

  /* ==================== setCvt_size ================================== */
  public void setCvt_size(int cvt_size) {
    this.cvt_size = cvt_size;
  }

  /* ==================== getCvt ================================== */
  public int[] getCvt() {
    return cvt;
  }

  /* ==================== setCvt ================================== */
  public void setCvt(int[] cvt) {
    this.cvt = cvt;
  }

  /* ==================== getStorage_size ================================== */
  public int getStorage_size() {
    return storage_size;
  }

  /* ==================== getStorage_size ================================== */
  public void setStorage_size(int storage_size) {
    this.storage_size = storage_size;
  }

  /* ==================== getStorage ================================== */
  public int[] getStorage() {
    return storage;
  }

  /* ==================== setStorage ================================== */
  public void setStorage(int[] storage) {
    this.storage = storage;
  }

  /* ==================== getTwilight ================================== */
  public TTGlyphZoneRec getTwilight() {
    return twilight;
  }

  /* ==================== setTwilight ================================== */
  public void setTwilight(TTGlyphZoneRec twilight) {
    this.twilight = twilight;
  }

  /* ==================== isDebug ================================== */
  public boolean isDebug() {
    return debug;
  }

  /* ==================== setDebug ================================== */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /* ==================== getContext ================================== */
  public TTExecContextRec getContext() {
    return context;
  }

  /* ==================== setContext ================================== */
  public void setContext(TTExecContextRec context) {
    this.context = context;
  }

  /* ==================== isBytecode_ready ================================== */
  public boolean isBytecode_ready() {
    return bytecode_ready;
  }

  /* ==================== setBytecode_ready ================================== */
  public void setBytecode_ready(boolean bytecode_ready) {
    this.bytecode_ready = bytecode_ready;
  }

  /* ==================== isCvt_ready ================================== */
  public boolean isCvt_ready() {
    return cvt_ready;
  }

  /* ==================== setCvt_ready ================================== */
  public void setCvt_ready(boolean cvt_ready) {
    this.cvt_ready = cvt_ready;
  }

}