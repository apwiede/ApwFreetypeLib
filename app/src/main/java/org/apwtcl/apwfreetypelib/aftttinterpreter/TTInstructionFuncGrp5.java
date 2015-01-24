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
  /*    TTInstructionFuncGrp5                                              */
  /*    instructions functions group 5  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp5 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp5";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp5 ================================== */
  public TTInstructionFuncGrp5(TTExecContextRec exec)
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
   * LT[]:         Less Than
   * Opcode range: 0x50
   * Stack:        int32? int32? --> bool
   * =====================================================================
   */
  public void LT() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] < cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
   * LTEQ[]:       Less Than or EQual
   * Opcode range: 0x51
   * Stack:        int32? int32? --> bool
   * =====================================================================
   */
  public void LTEQ() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] <= cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
  /* GT[]:         Greater Than
  /* Opcode range: 0x52
  /* Stack:        int32? int32? --> bool
  * =====================================================================
      */
  public void GT() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] > cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
   * GTEQ[]:       Greater Than or EQual
   * Opcode range: 0x53
   * Stack:        int32? int32? --> bool
   * =====================================================================
   */
  public void GTEQ() {
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("DO_GTEQ: %d >= %d", cur.stack[cur.numArgs + 0], cur.stack[cur.numArgs + 1]));
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] >= cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
   * EQ[]:         EQual
   * Opcode range: 0x54
   * Stack:        StkElt StkElt --> bool
   * =====================================================================
   */
  public void EQ() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] == cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
   * NEQ[]:        Not EQual
   * Opcode range: 0x55
   * Stack:        StkElt StkElt --> bool
   * =====================================================================
   */
  public void NEQ() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] != cur.stack[cur.numArgs + 1]) ? 1 : 0;
  }

  /* =====================================================================
   * ODD[]:        Is ODD
   * Opcode range: 0x56
   * Stack:        f26.6 --> bool
   * =====================================================================
   */
  public void ODD() {
    cur.stack[cur.numArgs + 0] = (cur.render_funcs.curr_round_func.round(cur.stack[cur.numArgs + 0], 0) & 127) == 64 ? 1 : 0;
  }

  /* =====================================================================
   * EVEN[]:       Is EVEN
   * Opcode range: 0x57
   * Stack:        f26.6 --> bool
   * =====================================================================
   */
  public void EVEN() {
    cur.stack[cur.numArgs + 0] = (cur.render_funcs.curr_round_func.round(cur.stack[cur.numArgs + 0], 0) & 127) == 0 ? 1 : 0;
  }

  /* =====================================================================
   * IF[]:         IF test
   * Opcode range: 0x58
   * Stack:        StkElt -->
   * =====================================================================
   */
  public void IF() {
    int nIfs;
    boolean Out;

    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("insIF: %d\n", cur.stack[cur.numArgs + 0]));
    if (cur.stack[cur.numArgs + 0] != 0) {
      return;
    }
    nIfs = 1;
    Out = false;
    do {
      if (TTInstructionFuncGrp1.SkipCode(cur) == false) {
        return;
      }
      switch (cur.opcode) {
        case IF:      /* IF */
          nIfs++;
          break;
        case ELSE:      /* ELSE */
          Out = (nIfs == 1);
          break;
        case EIF:      /* EIF */
          nIfs--;
          Out = (nIfs == 0);
          break;
      }
    } while (Out == false);
  }

  /* =====================================================================
   * AND[]:        logical AND
   * Opcode range: 0x5A
   * Stack:        uint32 uint32 --> uint32
   * =====================================================================
   */
  public void AND() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] != 0 && cur.stack[cur.numArgs + 1] != 0) ? 1 : 0;
  }

  /* =====================================================================
   * OR[]:         logical OR
   * Opcode range: 0x5B
   * Stack:        uint32 uint32 --> uint32
   * =====================================================================
   */
  public void OR() {
    cur.stack[cur.numArgs + 0] = (cur.stack[cur.numArgs + 0] != 0 || cur.stack[cur.numArgs + 1] != 0) ? 1 : 0;
  }

  /* =====================================================================
   * NOT[]:        logical NOT
   * Opcode range: 0x5C
   * Stack:        StkElt --> uint32
   * =====================================================================
   */
  public void NOT() {
    cur.stack[cur.numArgs + 0] = cur.stack[cur.numArgs + 0] == 0  ? 1 : 0;
  }

  /* =====================================================================
   * DELTAPn[]:    DELTA exceptions P1, P2, P3
   * Opcode range: 0x5D,0x71,0x72
   * Stack:        uint32 (2 * uint32)... -->
   * =====================================================================
   */
  public void DELTAP() {
    int k;
    int nump;
    short A;
    int C;
    int B;

    Debug(0, DebugTag.DBG_INTERP, TAG, "insDELTAP");
    nump = cur.stack[cur.numArgs + 0];   /* some points theoretically may occur more
                                     than once, thus UShort isn't enough */
    for (k = 1; k <= nump; k++) {
      if (cur.numArgs < 2) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_TOO_FEW_ARGUMENTS;
        }
        cur.numArgs = 0;
        cur.new_top = cur.numArgs;
        Debug(0, DebugTag.DBG_INTERP, TAG, "insDELTAP end 0");
        return;
      }
      cur.numArgs -= 2;
      A = (short)cur.stack[cur.numArgs + 1];
      B = cur.stack[cur.numArgs];
        /* XXX: Because some popular fonts contain some invalid DeltaP */
        /*      instructions, we simply ignore them when the stacked   */
        /*      point reference is off limit, rather than returning an */
        /*      error.  As a delta instruction doesn't change a glyph  */
        /*      in great ways, this shouldn't be a problem.            */
      if (!TTUtil.BOUNDS(A, cur.zp0.getN_points())) {
        C = (B & 0xF0) >> 4;
        switch (cur.opcode) {
          case DeltaP1:
            break;
          case DeltaP2:
            C += 16;
            break;
          case DeltaP3:
            C += 32;
            break;
        }
        C += cur.graphics_state.delta_base;
        if (TTInstructionFuncGrp4.CurrentPpem(cur) == C) {
          B = (B & 0xF) - 8;
          if (B >= 0) {
            B++;
          }
          B = B * 64 / (1 << cur.graphics_state.delta_shift);
          cur.funcMove(cur.zp0, A, B);
        }
      } else {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        }
      }
    }
    Debug(0, DebugTag.DBG_INTERP, TAG, "DELTAP end 1");
    cur.new_top = cur.numArgs;
    Debug(0, DebugTag.DBG_INTERP, TAG, "DELTAP end 2");
  }

  /* =====================================================================
   * SDB[]:        Set Delta Base
   * Opcode range: 0x5E
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void SDB() {
    cur.graphics_state.delta_base = (short)cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   * SDS[]:        Set Delta Shift
   * Opcode range: 0x5F
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void SDS() {
    cur.graphics_state.delta_shift = (short)cur.stack[cur.numArgs + 0];
  }

}