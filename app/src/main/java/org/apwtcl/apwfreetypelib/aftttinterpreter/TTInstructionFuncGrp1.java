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
  /*    TTInstructionFuncGrp1                                              */
  /*    instructions functions group 1  for interpreter                    */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTInterpRun;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTInstructionFuncGrp1 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp1";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp1 ================================== */
  public TTInstructionFuncGrp1(TTExecContextRec exec)
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
   *
   * MANAGING THE FLOW OF CONTROL
   *
   *   Instructions appear in the specification's order.
   *
   * =====================================================================
   */
  public static boolean SkipCode(TTExecContextRec cur) {
    cur.IP += cur.length;
    if ( cur.IP < cur.codeSize ) {
      cur.opcode = TTOpCode.OpCode.getTableTag(cur.code[cur.IP].getVal() & 0xFF);
      cur.length = cur.opcode.getOpCodeLength();
      if (cur.length < 0) {
        if (cur.IP + 1 >= cur.codeSize) {
          cur.error = FTError.ErrorTag.INTERP_CODE_OVERFLOW;
          return false;
        }
        cur.length = 2 - cur.length * (cur.code[cur.IP + 1].getVal() & 0xFF);
      }
      if (cur.IP + cur.length <= cur.codeSize) {
        return true;
      }
    }
    return false;
  }

  /* =====================================================================
   *
   * SVTCA[a]:     Set (F and P) Vectors to Coordinate Axis
   * Opcode range: 0x00-0x01
   * Stack:        -->
   *
   * =====================================================================
   */
  public void SVTCA() {
    short A;
    short B;

    A = (short)((cur.opcode.getVal() & 1) << 14);
    B = (short)(A ^ (short)0x4000);
    cur.graphics_state.freeVector.x = A;
    cur.graphics_state.projVector.x = A;
    cur.graphics_state.dualVector.x = A;
    cur.graphics_state.freeVector.y = B;
    cur.graphics_state.projVector.y = B;
    cur.graphics_state.dualVector.y = B;
    cur.render_funcs.ComputeFuncs(cur);
  }

  /* =====================================================================
   *
   * SPVTCA[a]:    Set PVector to Coordinate Axis
   * Opcode range: 0x02-0x03
   * Stack:        -->
   *
   * =====================================================================
   */
  public void SPVTCA() {
    short A;
    short B;

    A = (short)((cur.opcode.getVal() & 1) << 14);
    B = (short)(A ^ (short)0x4000);
    cur.graphics_state.projVector.x = A;
    cur.graphics_state.dualVector.x = A;
    cur.graphics_state.projVector.y = B;
    cur.graphics_state.dualVector.y = B;
    cur.render_funcs.ComputeFuncs(cur);
  }

  /* =====================================================================
   *
   * SFVTCA[a]:    Set FVector to Coordinate Axis
   * Opcode range: 0x04-0x05
   * Stack:        -->
   *
   * =====================================================================
   */
  public void SFVTCA() {
    short A;
    short B;

    A = (short)((cur.opcode.getVal() & 1) << 14);
    B = (short)(A ^ (short)0x4000);
    cur.graphics_state.freeVector.x = A;
    cur.graphics_state.freeVector.y = B;
    cur.render_funcs.ComputeFuncs(cur);
  }

  /* =====================================================================
   *
   * SRP0[]:       Set Reference Point 0
   * Opcode range: 0x10
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SRP0() {
    cur.graphics_state.rp0 = (short)cur.stack[cur.numArgs + 0];
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("DO_SRP0: cur.GS.rp0: %d cur.numArgs: %d", cur.graphics_state.rp0, cur.numArgs));
  }


  /* =====================================================================
   *
   * SRP1[]:       Set Reference Point 1
   * Opcode range: 0x11
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SRP1() {
    cur.graphics_state.rp1 = (short)cur.stack[cur.numArgs + 0];
  }


  /* =====================================================================
   *
   * SRP2[]:       Set Reference Point 2
   * Opcode range: 0x12
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SRP2() {
    cur.graphics_state.rp2 = (short)cur.stack[cur.numArgs + 0];
  }


  /* =====================================================================
   *
   * SZP0[]:       Set Zone Pointer 0
   * Opcode range: 0x13
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SZP0() {
    switch (cur.stack[cur.numArgs + 0]) {
      case 0:
        cur.zp0 = cur.twilight;
        break;
      case 1:
        cur.zp0 = cur.pts;
        break;
      default:
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        }
        return;
    }
    cur.graphics_state.gep0 = (short)cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   *
   * SZP1[]:       Set Zone Pointer 1
   * Opcode range: 0x14
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SZP1() {
    switch ((int)cur.stack[cur.numArgs + 0]) {
      case 0:
        cur.zp1 = cur.twilight;
        break;
      case 1:
        cur.zp1 = cur.pts;
        break;
      default:
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        }
        return;
    }
    cur.graphics_state.gep1 = (short)cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   *
   * SZP2[]:       Set Zone Pointer 2
   * Opcode range: 0x15
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SZP2() {
    switch ((int)cur.stack[cur.numArgs + 0]) {
      case 0:
        cur.zp2 = cur.twilight;
        break;
      case 1:
        cur.zp2 = cur.pts;
        break;
      default:
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        }
        return;
    }
    cur.graphics_state.gep2 = (short)cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   *
   * SZPS[]:       Set Zone PointerS
   * Opcode range: 0x16
   * Stack:        uint32 -->
   *
   * =====================================================================
   */
  public void SZPS() {
    switch (cur.stack[cur.numArgs + 0]) {
      case 0:
        cur.zp0 = cur.twilight;
        break;
      case 1:
        cur.zp0 = cur.pts;
        break;
      default:
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
        }
        return;
    }
    cur.zp1 = cur.zp0;
    cur.zp2 = cur.zp0;
    cur.graphics_state.gep0 = (short)cur.stack[cur.numArgs + 0];
    cur.graphics_state.gep1 = (short)cur.stack[cur.numArgs + 0];
    cur.graphics_state.gep2 = (short)cur.stack[cur.numArgs + 0];
  }

  /* ==================== SLOOP ===================================== */
  public void SLOOP() {
    if (cur.stack[cur.numArgs + 0] < 0) {
      cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
    } else {
      cur.graphics_state.loop = cur.stack[cur.numArgs + 0];
    }
  }

  /* ==================== RTG ===================================== */
  public void RTG() {
    cur.graphics_state.round_state = TTInterpTags.RoundState.To_Grid;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_to_grid;
  }

  /* =====================================================================
   *
   * RTHG[]:       Round To Half Grid
   * Opcode range: 0x19
   * Stack:        -->
   *
   * =====================================================================
   */
  public void RTHG() {
    cur.graphics_state.round_state = TTInterpTags.RoundState.To_Half_Grid;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_to_half_grid;
  }

  /* ==================== SMD ===================================== */
  public void SMD() {
    cur.graphics_state.minimum_distance = cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   *
   * ELSE[]:       ELSE
   * Opcode range: 0x1B
   * Stack:        -->
   *
   * =====================================================================
   */
  public void ELSE() {
    int nIfs;

    nIfs = 1;
    do {
      if (SkipCode(cur) == false) {
        return;
      }
      switch (cur.opcode) {
        case IF:    /* IF */
          nIfs++;
          break;
        case EIF:    /* EIF */
          nIfs--;
          break;
      }
    } while (nIfs != 0);
  }

  /* ==================== JMPR ===================================== */
  public void JMPR() {
    if (cur.stack[cur.numArgs + 0] == 0 && cur.numArgs == 0) {
      cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
    }
    cur.IP += cur.stack[cur.numArgs + 0];
    if (cur.IP < 0 ||
        (cur.callTop > 0 && cur.IP > cur.callStack[cur.callTop - 1].CurEnd)) {
      cur.error = FTError.ErrorTag.INTERP_BAD_ARGUMENT;
    }
    cur.step_ins = false;
  }

  /* ==================== SCVTCI ===================================== */
  public void SCVTCI() {
    cur.graphics_state.control_value_cutin = (int)cur.stack[cur.numArgs + 0];
  }

  /* ==================== SSWCI ===================================== */
  public void SSWCI() {
    cur.graphics_state.single_width_cutin = (int)cur.stack[cur.numArgs + 0];
  }

  /* ==================== SSW ===================================== */
  public void SSW() {
    cur.graphics_state.single_width_value = FTCalc.FTMulFix(cur.stack[cur.numArgs + 0], cur.tt_metrics.getScale());
  }

}