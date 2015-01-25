package org.apwtcl.apwfreetypelib.aftttinterpreter;

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

  /* ===================================================================== */
  /*    TTInstructionFuncGrp8                                              */
  /*    instructions functions group 8  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTDefRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp8 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp8";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp8 ================================== */
  public TTInstructionFuncGrp8(TTExecContextRec exec)
  {
    oid++;
    id = oid;
    this.cur = exec;
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
    return str.toString();
  }

  /* =====================================================================
   * FLIPPT[]:     FLIP PoinT
   * Opcode range: 0x80
   * Stack:        uint32... -->
   * =====================================================================
   */
  public void FLIPPT() {
    short point;

    if (cur.top < cur.graphics_state.loop) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_TOO_FEW_ARGUMENTS;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
    while (cur.graphics_state.loop > 0) {
      cur.stack_idx--;
      point = (short)cur.stack[cur.stack_idx];
      if (TTUtil.BOUNDS(point, cur.pts.getN_points())) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
      } else {
        cur.pts.getTags()[point] = Flags.Curve.getTableTag(cur.pts.getTags()[point].getVal() ^ Flags.Curve.ON.getVal());
      }
      cur.graphics_state.loop--;
    }
    cur.graphics_state.loop = 1;
    cur.new_top = cur.stack_idx;
  }


  /* =====================================================================
   * FLIPRGON[]:   FLIP RanGe ON
   * Opcode range: 0x81
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void FLIPRGON() {
    short I;
    short K;
    short L;

    K = (short)cur.stack[cur.stack_idx + 1];
    L = (short)cur.stack[cur.stack_idx + 0];
    if (TTUtil.BOUNDS(K, cur.pts.getN_points()) || TTUtil.BOUNDS(L, cur.pts.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    for (I = L; I <= K; I++) {
      cur.pts.getTags()[I] = Flags.Curve.getTableTag(cur.pts.getTags()[I].getVal() | Flags.Curve.ON.getVal());
    }
  }

  /* =====================================================================
   * FLIPRGOFF:    FLIP RanGe OFF
   * Opcode range: 0x82
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void FLIPRGOFF() {
    short I;
    short K;
    short L;

    K = (short)cur.stack[cur.stack_idx + 1];
    L = (short)cur.stack[cur.stack_idx + 0];
    if (TTUtil.BOUNDS(K, cur.pts.getN_points()) || TTUtil.BOUNDS(L, cur.pts.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    for (I = L; I <= K; I++) {
      cur.pts.getTags()[I] = Flags.Curve.getTableTag(cur.pts.getTags()[I].getVal() & ~Flags.Curve.ON.getVal());
    }
  }

  /* =====================================================================
   * SCANCTRL[]:   SCAN ConTRoL
   * Opcode range: 0x85
   * Stack:        uint32? -->
   * =====================================================================
   */
  public void SCANCTRL() {
    int A;

      /* Get Threshold */
    A = (cur.stack[cur.stack_idx + 0] & 0xFF);
    if (A == 0xFF) {
      cur.graphics_state.scan_control = true;
      return;
    } else {
      if ( A == 0 ) {
        cur.graphics_state.scan_control = false;
        return;
      }
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x100) != 0 && cur.tt_metrics.getPpem() <= A) {
      cur.graphics_state.scan_control = true;
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x200) != 0 && cur.tt_metrics.isRotated()) {
      cur.graphics_state.scan_control = true;
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x400) != 0 && cur.tt_metrics.isStretched()) {
      cur.graphics_state.scan_control = true;
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x800) != 0 && cur.tt_metrics.getPpem() > A) {
      cur.graphics_state.scan_control = false;
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x1000) != 0 && cur.tt_metrics.isRotated()) {
      cur.graphics_state.scan_control = false;
    }
    if ((cur.stack[cur.stack_idx + 0] & 0x2000) != 0 && cur.tt_metrics.isStretched()) {
      cur.graphics_state.scan_control = false;
    }
  }

  /* =====================================================================
   * SDPVTL[a]:    Set Dual PVector to Line
   * Opcode range: 0x86-0x87
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void SDPVTL() {
    int A;
    int B;
    int C;
    short p1;    /* was FT_Int in pas type ERROR */
    short p2;    /* was FT_Int in pas type ERROR */
    TTOpCode.OpCode aOpc = cur.opcode;

    p1 = (short)cur.stack[cur.stack_idx + 1];
    p2 = (short)cur.stack[cur.stack_idx + 0];

    if (TTUtil.BOUNDS(p2, cur.zp1.getN_points()) || TTUtil.BOUNDS(p1, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    {
      FTVectorRec v1 = cur.zp1.getOrgPoint(p2);
      FTVectorRec v2 = cur.zp2.getOrgPoint(p1);

      A = v1.x - v2.x;
      B = v1.y - v2.y;
        /* If v1 == v2, SDPVTL behaves the same as */
        /* SVTCA[X], respectively.                 */
        /*                                         */
        /* Confirmed by Greg Hitchcock.            */
      if (A == 0 && B == 0) {
        A = 0x4000;
        aOpc = TTOpCode.OpCode.SVTCA_y;
      }
    }
    if ((aOpc.getVal() & 1) != 0) {
      C = B;   /* counter clockwise rotation */
      B = A;
      A = -C;
    }
    FTReference<FTVectorRec> dualVec_ref = new FTReference<FTVectorRec>();
    dualVec_ref.Set(cur.graphics_state.dualVector);
    TTUtil.Normalize(A, B, dualVec_ref);
    cur.graphics_state.dualVector = dualVec_ref.Get();
    {
      FTVectorRec v1 = cur.zp1.getCurPoint(p2);
      FTVectorRec v2 = cur.zp2.getCurPoint(p1);

      A = v1.x - v2.x;
      B = v1.y - v2.y;
      if (A == 0 && B == 0) {
        A = 0x4000;
        aOpc = TTOpCode.OpCode.SVTCA_y;
      }
    }
    if ((aOpc.getVal() & 1) != 0) {
      C = B;   /* counter clockwise rotation */
      B = A;
      A = -C;
    }
    dualVec_ref.Set(cur.graphics_state.projVector);
    TTUtil.Normalize(A, B, dualVec_ref);
    cur.graphics_state.projVector = dualVec_ref.Get();
//      GUESS_VECTOR( freeVector );
    cur.render_funcs.ComputeFuncs(cur);
  }

  /* =====================================================================
   * GETINFO[]:    GET INFOrmation
   * Opcode range: 0x88
   * Stack:        uint32 --> uint32
   * =====================================================================
   */
  public void GETINFO() {
    int K;

    K = 0;
    if ((cur.stack[cur.stack_idx + 0] & 1) != 0) {
      K = TTRunInstructions.TT_INTERPRETER_VERSION_35;
    }
    /********************************/
      /* GLYPH ROTATED                */
      /* Selector Bit:  1             */
      /* Return Bit(s): 8             */
      /*                              */
    if ((cur.stack[cur.stack_idx + 0] & 2) != 0 && cur.tt_metrics.isRotated()) {
      K |= 0x80;
    }
    /********************************/
      /* GLYPH STRETCHED              */
      /* Selector Bit:  2             */
      /* Return Bit(s): 9             */
      /*                              */
    if ((cur.stack[cur.stack_idx + 0] & 4) != 0 && cur.tt_metrics.isStretched()) {
      K |= 1 << 8;
    }
    /********************************/
      /* HINTING FOR GRAYSCALE        */
      /* Selector Bit:  5             */
      /* Return Bit(s): 12            */
      /*                              */
    if (((cur.stack[cur.stack_idx + 0] & 32) != 0) && cur.grayscale) {
      K |= 1 << 12;
    }
    cur.stack[cur.stack_idx + 0] = K;
  }

  /* =====================================================================
   * IDEF[]:       Instruction DEFinition
   * Opcode range: 0x89
   * Stack:        Eint8 -->
   * =====================================================================
   */
  public void IDEF() {
    TTDefRec def;
    int limit;
    int defIdx;

      /*  First of all, look for the same function in our table */
    defIdx = 0;
    def = cur.IDefs[defIdx];
    limit = defIdx + cur.numIDefs;
    for (; defIdx < limit; defIdx++) {
      if (def.getOpc() == cur.stack[cur.stack_idx + 0]) {
        break;
      }
    }
    if (defIdx == limit) {
        /* check that there is enough room for a new instruction */
      if (cur.numIDefs >= cur.maxIDefs) {
        cur.error = FTError.ErrorTag.INTERP_TOO_MANY_INSTRUCTION_DEFS;
        return;
      }
      cur.numIDefs++;
    }
      /* opcode must be unsigned 8-bit integer */
    if (0 > cur.stack[cur.stack_idx + 0] || cur.stack[cur.stack_idx + 0] > 0x00FF) {
      cur.error = FTError.ErrorTag.INTERP_TOO_MANY_INSTRUCTION_DEFS;
      return;
    }
    def.setOpc(cur.stack[cur.stack_idx + 0]);
    def.setStart(cur.IP + 1);
    def.setRange(cur.curRange.getVal());
    def.setActive(true);
    if (cur.stack[cur.stack_idx + 0] > cur.maxIns) {
      cur.maxIns = (byte)cur.stack[cur.stack_idx + 0];
    }
      /* Now skip the whole function definition. */
      /* We don't allow nested IDEFs & FDEFs.    */
    while (cur.SkipCode() == true) {
      switch (cur.opcode) {
        case IDEF:   /* IDEF */
        case FDEF:   /* FDEF */
          cur.error = FTError.ErrorTag.INTERP_NESTED_DEFS;
          return;
        case ENDF:   /* ENDF */
          return;
      }
    }
  }

  /* =====================================================================
   * ROLL[]:       ROLL top three elements
   * Opcode range: 0x8A
   * Stack:        3 * StkElt --> 3 * StkElt
   * =====================================================================
   */
  public void ROLL() {
    int A;
    int B;
    int C;

    A = cur.stack[cur.stack_idx + 2];
    B = cur.stack[cur.stack_idx + 1];
    C = cur.stack[cur.stack_idx + 0];
    cur.stack[cur.stack_idx + 2] = C;
    cur.stack[cur.stack_idx + 1] = A;
    cur.stack[cur.stack_idx + 0] = B;
  }

  /* =====================================================================
   * MAX[]:        MAXimum
   * Opcode range: 0x68
   * Stack:        int32? int32? --> int32
   * =====================================================================
   */
  public void MAX() {
    if (cur.stack[cur.stack_idx + 1] > cur.stack[cur.stack_idx + 0]) {
      cur.stack[cur.stack_idx + 0] = cur.stack[cur.stack_idx + 1];
    }
  }

  /* =====================================================================
   * MIN[]:        MINimum
   * Opcode range: 0x69
   * Stack:        int32? int32? --> int32
   * =====================================================================
   */
  public void MIN() {
    if (cur.stack[cur.stack_idx + 1] < cur.stack[cur.stack_idx + 0]) {
      cur.stack[cur.stack_idx + 0] = cur.stack[cur.stack_idx + 1];
    }
  }

  /* =====================================================================
   * SCANTYPE[]:   SCAN TYPE
   * Opcode range: 0x8D
   * Stack:        uint32? -->
   * =====================================================================
   */
  public void SCANTYPE() {
    if (cur.stack[cur.stack_idx + 0] >= 0) {
      cur.graphics_state.scan_type = (int)cur.stack[cur.stack_idx + 0];
    }
  }

  /* =====================================================================
   * INSTCTRL[]:   INSTruction ConTRoL
   * Opcode range: 0x8e
   * Stack:        int32 int32 -->
   * =====================================================================
   */
  public void INSTCTRL() {
    int K;
    int L;

    K = cur.stack[cur.stack_idx + 1];
    L = cur.stack[cur.stack_idx + 0];
    if (K < 1 || K > 2) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    if (L != 0) {
      L = K;
    }
    byte byte1 = (byte)(cur.graphics_state.instruct_control & ~(K & 0xFF));
    byte byte2 = (byte)(L & 0xFF);
    cur.graphics_state.instruct_control = (byte)(byte1 | byte2);
  }

}