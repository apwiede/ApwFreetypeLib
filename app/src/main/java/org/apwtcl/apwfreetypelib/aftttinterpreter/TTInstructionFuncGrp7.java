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
  /*    TTInstructionFuncGrp7                                              */
  /*    instructions functions group 7  for interpreter                    */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTInterpRun;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp7 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp7";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp7 ================================== */
  public TTInstructionFuncGrp7(TTExecContextRec exec)
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

  /* ==================== WCVTF ===================================== */
  public void WCVTF() {
    int I = (int)cur.stack[cur.numArgs + 0];

    if (TTUtil.BOUNDSL(I, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      }
    } else {
      cur.cvt[I] = FTCalc.FTMulFix(cur.stack[cur.numArgs + 1], cur.tt_metrics.getScale());
    }
  }

  /* =====================================================================
   *
   * DELTACn[]:    DELTA exceptions C1, C2, C3
   * Opcode range: 0x73,0x74,0x75
   * Stack:        uint32 (2 * uint32)... -->
   *
   * =====================================================================
   */
  public void DELTAC() {
    int nump;
    int k;
    int A;
    int C;
    int B;

    nump = cur.stack[cur.numArgs + 0];
    for (k = 1; k <= nump; k++) {
      if (cur.numArgs < 2) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_TOO_FEW_ARGUMENTS;
        }
        cur.numArgs = 0;
        cur.new_top = cur.numArgs;
        return;
      }
      cur.numArgs -= 2;
      A = cur.stack[cur.numArgs + 1];
      B = cur.stack[cur.numArgs];
      if (TTUtil.BOUNDSL(A, cur.cvtSize)) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
      } else {
        C = (B & 0xF0) >> 4;
        switch (cur.opcode) {
          case DeltaCn_0:
            break;
          case DeltaCn_1:
            C += 16;
            break;
          case DeltaCn_2:
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
          cur.render_funcs.curr_cvt_func.moveCvt(A, B);
        }
      }
    }
    cur.new_top = cur.numArgs;
  }

  /* ==================== SROUND ===================================== */
  public void SROUND() {
    cur.render_funcs.round_super.SetSuperRound(0x4000, cur.stack[cur.numArgs + 0]);
    cur.graphics_state.round_state = TTInterpTags.RoundState.Super;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_super;
  }

  /* ==================== S45ROUND ===================================== */
  public void S45ROUND() {
    cur.render_funcs.round_super.SetSuperRound(0x2D41, cur.stack[cur.numArgs + 0]);
    cur.graphics_state.round_state = TTInterpTags.RoundState.Super_45;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_super45;
  }

  /* ==================== JROT ===================================== */
  public void JROT() {
    if (cur.stack[cur.numArgs + 1] != 0) {
      if (cur.stack[cur.numArgs + 0] == 0 && cur.numArgs == 0) {
        cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
      }
      cur.IP += cur.stack[cur.numArgs + 0];
      if ( cur.IP < 0 ||
          (cur.callTop > 0 && cur.IP > cur.callStack[cur.callTop - 1].CurEnd)) {
        cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
      }
      cur.step_ins = false;
    }
  }

  /* ==================== JROF ===================================== */
  public void JROF() {
    if (cur.stack[cur.numArgs + 1] == 0) {
      if (cur.stack[cur.numArgs + 0] == 0 && cur.numArgs == 0) {
        cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
      }
      cur.IP += cur.stack[cur.numArgs + 0];
      if (cur.IP < 0  ||
          (cur.callTop > 0 && cur.IP > cur.callStack[cur.callTop - 1].CurEnd)) {
        cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
      }
      cur.step_ins = false;
    }
  }

  /* ==================== ROFF ===================================== */
  public void ROFF() {
    cur.graphics_state.round_state = TTInterpTags.RoundState.Off;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_none;
  }

  /* ==================== RUTG ===================================== */
  public void RUTG() {
    cur.graphics_state.round_state = TTInterpTags.RoundState.Up_To_Grid;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_up_to_grid;
  }

  /* ==================== RDTG ===================================== */
  public void RDTG() {
    cur.graphics_state.round_state = TTInterpTags.RoundState.Down_To_Grid;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_down_to_grid;
  }

}