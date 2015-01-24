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
  /*    TTInstructionFuncGrp6                                              */
  /*    instructions functions group 6  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTInstructionFuncGrp6 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp6";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp6 ================================== */
  public TTInstructionFuncGrp6(TTExecContextRec exec)
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
   * ADD[]:        ADD
   * Opcode range: 0x60
   * Stack:        f26.6 f26.6 --> f26.6
   * =====================================================================
   */
  public void ADD() {
    cur.stack[cur.numArgs + 0] += cur.stack[cur.numArgs + 1];
  }

  /* =====================================================================
   * SUB[]:        SUBtract
   * Opcode range: 0x61
   * Stack:        f26.6 f26.6 --> f26.6
   * =====================================================================
   */
  public void SUB() {
    cur.stack[cur.numArgs + 0] -= cur.stack[cur.numArgs + 1];
  }

  /* =====================================================================
   * DIV[]:        DIVide
   * Opcode range: 0x62
   * Stack:        f26.6 f26.6 --> f26.6
   * =====================================================================
   */
  public void DIV() {
    if (cur.stack[cur.numArgs + 1] == 0) {
      cur.error = FTError.ErrorTag.INTERP_DIVIDE_BY_ZERO;
    } else {
      cur.stack[cur.numArgs + 0] = FTCalc.FT_MulDivNoRound(cur.stack[cur.numArgs + 0], 64, cur.stack[cur.numArgs + 1]);
    }
  }

  /* =====================================================================
   * MUL[]:        MULtiply
   * Opcode range: 0x63
   * Stack:        f26.6 f26.6 --> f26.6
   * =====================================================================
   */
  public void MUL() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_MulDiv(cur.stack[cur.numArgs + 0], cur.stack[cur.numArgs + 1], 64);
  }

  /* =====================================================================
   * ABS[]:        ABSolute value
   * Opcode range: 0x64
   * Stack:        f26.6 --> f26.6
   * =====================================================================
   */
  public void ABS() {
    cur.stack[cur.numArgs + 0] = (int)(FTCalc.FT_ABS(cur.stack[cur.numArgs + 0]) & 0xFFFF);
  }

  /* =====================================================================
   * NEG[]:        NEGate
   * Opcode range: 0x65
   * Stack: f26.6 --> f26.6
   * =====================================================================
   */
  public void NEG() {
    cur.stack[cur.numArgs + 0] = -cur.stack[cur.numArgs + 0];
  }

  /* =====================================================================
   * FLOOR[]:      FLOOR
   * Opcode range: 0x66
   * Stack:        f26.6 --> f26.6
   * =====================================================================
   */
  public void FLOOR() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_PIX_FLOOR(cur.stack[cur.numArgs + 0]);
  }

  /* =====================================================================
   * CEILING[]:    CEILING
   * Opcode range: 0x67
   * Stack:        f26.6 --> f26.6
   * =====================================================================
   */
  public void CEILING() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_PIX_CEIL(cur.stack[cur.numArgs + 0]);
  }

  /* =====================================================================
   * ROUND[ab]:    ROUND value
   * Opcode range: 0x68-0x6B
   * Stack:        f26.6 --> f26.6
   * =====================================================================
   */
  public void ROUND() {
    cur.stack[cur.numArgs + 0] = cur.render_funcs.curr_round_func.round(cur.stack[cur.numArgs + 0], cur.tt_metrics.getCompensations()[cur.opcode.getVal() - 0x68]);
  }

  /* =====================================================================
   * NROUND[ab]:   No ROUNDing of value
   * Opcode range: 0x6C-0x6F
   * Stack:        f26.6 --> f26.6
   * =====================================================================
   */
  public void NROUND() {
    cur.stack[cur.numArgs + 0] = cur.render_funcs.round_none.round(cur.stack[cur.numArgs + 0], cur.tt_metrics.getCompensations()[cur.opcode.getVal() - 0x6C]);
  }

}