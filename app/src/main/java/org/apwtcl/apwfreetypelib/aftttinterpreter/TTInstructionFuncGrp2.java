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
  /*    TTInstructionFuncGrp2                                              */
  /*    instructions functions group 2  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTCallRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCodeRange;
import org.apwtcl.apwfreetypelib.afttruetype.TTDefRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp2 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp2";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp2 ================================== */
  public TTInstructionFuncGrp2(TTExecContextRec exec)
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

  /* ==================== FT_StackMove ===================================== */
  private void FTStackMove(int offset1, int offset2, int length) {
    int idx1;
    int idx2;
    int i;

    if (offset1 < offset2) {
      idx1 = offset2 + length;
      idx2 = offset1 + length;
      for ( i = length - 1; i >= 0; i--) {
        cur.stack[idx2] = cur.stack[idx1];
        idx2--;
        idx1--;
      }
    } else {
      idx1 = offset1 + length;
      idx2 = offset2 + length;
      for ( i = length - 1; i >= 0; i--) {
        cur.stack[idx1] = cur.stack[idx2];
        idx2--;
        idx1--;
      }
    }
  }

  /* =====================================================================
   * DUP[]:        DUPlicate the top stack's element
   * Opcode range: 0x20
   * Stack:        StkElt --> StkElt StkElt
   * =====================================================================
   */
  public void DUP() {
    cur.stack[cur.numArgs + 1] = cur.stack[cur.numArgs + 0];
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("DO_DUP: %d %d", cur.stack[cur.numArgs + 1], cur.stack[cur.numArgs + 0]));
  }

  /* =====================================================================
   * CLEAR[]:      CLEAR the entire stack
   * Opcode range: 0x22
   * Stack:        StkElt... -->
   * =====================================================================
   */
  public void CLEAR() {
    cur.new_top = 0;
  }

  /* =====================================================================
   * SWAP[]:       SWAP the stack's top two elements
   * Opcode range: 0x23
   * Stack:        2 * StkElt --> 2 * StkElt
   * =====================================================================
   */
  public void SWAP() {
    int L;

    L = cur.stack[cur.numArgs + 0];
    cur.stack[cur.numArgs + 0] = cur.stack[cur.numArgs + 1];
    cur.stack[cur.numArgs + 1] = L;
  }

  /* =====================================================================
   * DEPTH[]:      return the stack DEPTH
   * Opcode range: 0x24
   * Stack:        --> uint32
   * =====================================================================
   */
  public void DEPTH() {
    cur.stack[cur.numArgs + 0] = cur.top;
  }

  /* =====================================================================
   * CINDEX[]:     Copy INDEXed element
   * Opcode range: 0x25
   * Stack:        int32 --> StkElt
   * =====================================================================
   */
  public void CINDEX() {
    int L;

    L = cur.stack[cur.numArgs + 0];
    if (L <= 0 || L > cur.numArgs) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.stack[cur.numArgs + 0] = 0;
    } else {
      cur.stack[cur.numArgs + 0] = (int)cur.stack[(cur.numArgs - L)];
    }
  }

  /* =====================================================================
   * MINDEX[]:     Move INDEXed element
   * Opcode range: 0x26
   * Stack:        int32? --> StkElt
   * =====================================================================
   */

  public void MINDEX() {
    int L;
    int K;

    L = cur.stack[cur.numArgs + 0];
    if (L <= 0 || L > cur.numArgs) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
    } else {
      K = cur.stack[cur.numArgs - L];
      FTStackMove(cur.numArgs - L, cur.numArgs - L + 1, (L - 1));
      cur.stack[cur.numArgs - 1] = K;
    }
  }

  /* =====================================================================
   * ALIGNPTS[]:   ALIGN PoinTS
   * Opcode range: 0x27
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void ALIGNPTS() {
    short p1;
    short p2;
    int distance;

    p1 = (short)cur.stack[cur.numArgs + 0];
    p2 = (short)cur.stack[cur.numArgs + 1];
    if (TTUtil.BOUNDS(p1, cur.zp1.getN_points()) || TTUtil.BOUNDS(p2, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    distance = cur.funcProject(cur.zp0.getCur()[cur.zp0.getCur_idx() + p2].x - cur.zp1.getCur()[cur.zp1.getCur_idx() + p1].x,
        cur.zp0.getCur()[cur.zp0.getCur_idx() + p2].y - cur.zp1.getCur()[cur.zp1.getCur_idx() + p1].y) / 2;
    cur.funcMove(cur.zp1, p1, distance);
    cur.funcMove(cur.zp0, p2, -distance);
  }

  /* ======================== UNKNOWN ============================== */
  public void UNKNOWN() {
    TTDefRec def;
    int limit = cur.numIDefs;
    int def_idx;

    for (def_idx = 0; def_idx < limit; def_idx++) {
      def = cur.IDefs[def_idx];
      if (def == null) {
        break;
      }
      if (def.getOpc() == cur.opcode.getVal() && def.isActive()) {
        TTCallRec call_rec;

        if (cur.callTop >= cur.callSize) {
          cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
          return;
        }
        call_rec = cur.callStack[cur.callTop++];
        call_rec.setCaller_range(cur.curRange.getVal());
        call_rec.setCaller_IP(cur.IP + 1);
        call_rec.setCur_count(1);
        call_rec.setCur_restart(def.getStart());
        call_rec.setCur_end(def.getEnd());
        cur.TTGotoCodeRange(TTInterpTags.CodeRange.getTableTag(def.getRange()), def.getStart());
        cur.step_ins = false;
        return;
      }
    }
    cur.error = FTError.ErrorTag.INTERP_INVALID_OPCODE;
  }

  /* =====================================================================
   * UTP[a]:       UnTouch Point
   * Opcode range: 0x29
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void UTP() {
    short point;
    byte mask;

    point = (short)cur.stack[cur.numArgs + 0];
    if (TTUtil.BOUNDS(point, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    mask = (byte)0xFF;
    if (cur.graphics_state.freeVector.x != 0) {
      mask &= ~Flags.Curve.TOUCH_X.getVal();
    }
    if (cur.graphics_state.freeVector.y != 0) {
      mask &= ~Flags.Curve.TOUCH_Y.getVal();
    }
    cur.zp0.getTags()[point] = Flags.Curve.getTableTag(cur.zp0.getTags()[point].getVal() & mask);
  }

  /* =====================================================================
   * LOOPCALL[]:   LOOP and CALL function
   * Opcode range: 0x2A
   * Stack:        uint32? Eint16? -->
   * =====================================================================
   */
  public void LOOPCALL() {
    int F;
    TTCallRec call_rec;
    TTDefRec def;
    int defIdx;

      /* first of all, check the index */
    F = cur.stack[cur.numArgs + 1];
    if (TTUtil.BOUNDSL(F, cur.maxFunc + 1)) {
      cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      return;
    }
      /* Except for some old Apple fonts, all functions in a TrueType */
      /* font are defined in increasing order, starting from 0.  This */
      /* means that we normally have                                  */
      /*                                                              */
      /*    cur.maxFunc+1 == cur.numFDefs                             */
      /*    cur.FDefs[n].opc == n for n in 0..cur.maxFunc             */
      /*                                                              */
      /* If this isn't true, we need to look up the function table.   */
    def = cur.FDefs[F];
    if ( cur.maxFunc + 1 != cur.numFDefs || def.getOpc() != F ) {
        /* look up the FDefs table */
      int limit;

      defIdx = 0;
      def = cur.FDefs[defIdx];
      limit = defIdx + cur.numFDefs;
      while (defIdx < limit && def.getOpc() != F) {
        defIdx++;
      }
      if (defIdx == limit) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        return;
      }
    }
      /* check that the function is active */
    if (!def.isActive()) {
      cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      return;
    }
      /* check stack */
    if (cur.callTop >= cur.callSize) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    if (cur.stack[cur.numArgs + 0] > 0) {
      call_rec = cur.callStack[cur.callTop];
      call_rec.setCaller_range(cur.curRange.getVal());
      call_rec.setCaller_IP(cur.IP + 1);
      call_rec.setCur_count(cur.stack[cur.numArgs + 0]);
      call_rec.setCur_restart(def.getStart());
      call_rec.setCur_end(def.getEnd());
      cur.callTop++;
      cur.TTGotoCodeRange(TTInterpTags.CodeRange.getTableTag(def.getRange()), def.getStart());
      cur.step_ins = false;
    }
    return;
  }

  /* =====================================================================
   * CALL[]:       CALL function
   * Opcode range: 0x2B
   * Stack:        uint32? -->
   * =====================================================================
   */
  public void CALL() {
    int F;
    TTCallRec call_rec;
    TTDefRec def;
    int defIdx = 0;

      /* first of all, check the index */
    F = (int)cur.stack[cur.numArgs];
    Debug(0, DebugTag.DBG_INTERP, TAG, "insCall: "+F+"!"+cur.maxFunc+"!numArgs: "+cur.numArgs);
    if (TTUtil.BOUNDSL(F, cur.maxFunc + 1)) {
      cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      return;
    }
      /* Except for some old Apple fonts, all functions in a TrueType */
      /* font are defined in increasing order, starting from 0.  This */
      /* means that we normally have                                  */
      /*                                                              */
      /*    cur.maxFunc+1 == cur.numFDefs                             */
      /*    cur.FDefs[n].opc == n for n in 0..cur.maxFunc             */
      /*                                                              */
      /* If this isn't true, we need to look up the function table.   */

    def = cur.FDefs[F];
    if (cur.maxFunc + 1 != cur.numFDefs || def.getOpc() != F) {
        /* look up the FDefs table */
      int limit;

      def = cur.FDefs[defIdx];
      limit = defIdx + cur.numFDefs;
      while (defIdx < limit && def.getOpc() != F) {
        defIdx++;
      }
      if (defIdx == limit) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        return;
      }
    }
      /* check that the function is active */
    if (!def.isActive()) {
      cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      return;
    }
      /* check the call stack */
    if (cur.callTop >= cur.callSize) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    call_rec = cur.callStack[cur.callTop];
    call_rec.setCaller_range(cur.curRange.getVal());
    call_rec.setCaller_IP(cur.IP + 1);
    call_rec.setCur_count(1);
    call_rec.setCur_restart(def.getStart());
    call_rec.setCur_end(def.getEnd());
    cur.callTop++;
Debug(0, DebugTag.DBG_INTERP, TAG, "GotoCodeRange: "+def.getRange()+"!"+def.getStart()+"!");
    cur.TTGotoCodeRange(TTInterpTags.CodeRange.getTableTag(def.getRange()), def.getStart());
    cur.step_ins = false;
    return;
  }

  /* =====================================================================
   * FDEF[]:       Function DEFinition
   * Opcode range: 0x2C
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void FDEF() {
    int n;
    int rec_idx;
    int limit;
    TTDefRec rec;

Debug(0, DebugTag.DBG_INTERP, TAG, "FDEF");
      /* some font programs are broken enough to redefine functions! */
      /* We will then parse the current table.                       */
    limit = cur.numFDefs;
    n = cur.stack[cur.numArgs];
Debug(0, DebugTag.DBG_INTERP, TAG, "n: "+n+" numArgs: "+cur.numArgs+" stack[numArgs]: "+cur.stack[cur.numArgs]+" stack[0]: "+cur.stack[0]+"!");
    for (rec_idx = 0; rec_idx < limit; rec_idx++) {
      if (cur.FDefs[rec_idx].getOpc() == n) {
        break;
      }
    }
    if (rec_idx == limit) {
        /* check that there is enough room for new functions */
      if (cur.numFDefs >= cur.maxFDefs) {
        cur.error = FTError.ErrorTag.INTERP_TOO_MANY_FUNCTION_DEFS;
        return;
      }
      cur.numFDefs++;
    }
Debug(0, DebugTag.DBG_INTERP, TAG, "rec_idx: "+rec_idx+"!");
      /* Although FDEF takes unsigned 32-bit integer,  */
      /* func # must be within unsigned 16-bit integer */
    if (n > 0xFFFF) {
      cur.error = FTError.ErrorTag.INTERP_TOO_MANY_FUNCTION_DEFS;
      return;
    }
    rec = cur.FDefs[rec_idx];
    rec.setRange(cur.curRange.getVal());
    rec.setOpc(n);
    rec.setStart((cur.IP + 1));
    rec.setActive(true);
    rec.setInline_delta(false);
    rec.setSph_fdef_flags(0x0000);
    for (int j = 0; j < cur.numArgs; j++) {
      Debug(0, DebugTag.DBG_INTERP, TAG, "stack: "+cur.stack[j]+"!");
    }
    if (n > cur.maxFunc) {
      cur.maxFunc = n;
    }
      /* Now skip the whole function definition. */
      /* We don't allow nested IDEFS & FDEFs.    */
    while (TTInstructionFuncGrp1.SkipCode(cur) == true) {
      switch (cur.opcode) {
        case IDEF:    /* IDEF */
        case FDEF:    /* FDEF */
          cur.error = FTError.ErrorTag.INTERP_NESTED_DEFS;
          return;
        case ENDF:   /* ENDF */
          rec.setEnd(cur.IP);
Debug(0, DebugTag.DBG_INTERP, TAG, "rec: "+rec.toDebugString());
          return;
      }
    }
  }

  /* =====================================================================
   * ENDF[]:       END Function definition
   * Opcode range: 0x2D
   * Stack:        -->
   * =====================================================================
   */
  public void ENDF() {
    TTCallRec pRec;

    if (cur.callTop <= 0) {   /* We encountered an ENDF without a call */
      cur.error = FTError.ErrorTag.INTERP_ENDF_IN_EXEC_STREAM;
      return;
    }
    cur.callTop--;
    pRec = cur.callStack[cur.callTop];
    pRec.setCur_count(pRec.getCur_count() - 1);
    cur.step_ins = false;
    if (pRec.getCur_count() > 0) {
      cur.callTop++;
      cur.IP = pRec.getCur_restart();
    } else {
        /* Loop through the current function */
      cur.TTGotoCodeRange(TTInterpTags.CodeRange.getTableTag(pRec.getCaller_range()), pRec.getCaller_IP());
    }
      /* Exit the current call frame.                      */
      /* NOTE: If the last instruction of a program is a   */
      /*       CALL or LOOPCALL, the return address is     */
      /*       always out of the code range.  This is a    */
      /*       valid address, and it is why we do not test */
      /*       the result of Ins_Goto_CodeRange() here!    */
  }

  /* =====================================================================
   * MDAP[a]:      Move Direct Absolute Point
   * Opcode range: 0x2E-0x2F
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void MDAP() {
    short  point;
    int cur_dist;
    int distance;

    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("insMDAP: cur.GS.gep0: %d, cur.GS.gep1: %d", cur.graphics_state.gep0, cur.graphics_state.gep1));
    point = (short)cur.stack[cur.numArgs + 0];
    if (TTUtil.BOUNDS(point, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    if ((cur.opcode.getVal() & 1) != 0) {
      cur_dist = (cur.funcProject(cur.zp0.getCur()[cur.zp0.getCur_idx() + point].x,
          cur.zp0.getCur()[cur.zp0.getCur_idx() + point].y));
      distance = (cur.funcRound(cur_dist, cur.tt_metrics.getCompensations()[0]) - cur_dist);
      Debug(0, DebugTag.DBG_INTERP, TAG, String.format("cur_dist: %d, distance: %d comp: %d \n", cur_dist, distance, cur.tt_metrics.getCompensations()[0]));
    } else {
      distance = 0;
    }
    cur.funcMove(cur.zp0, (short) point, distance);
    cur.graphics_state.rp0 = point;
    cur.graphics_state.rp1 = point;
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("insMDAP end: point: %d distance: %d, cur.GS.rp0: %d, cur.GS.rp1: %d\n", point, distance, cur.graphics_state.rp0, cur.graphics_state.rp1));
  }

}