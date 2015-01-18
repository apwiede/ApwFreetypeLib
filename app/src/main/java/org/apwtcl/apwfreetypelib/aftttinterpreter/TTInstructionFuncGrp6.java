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

import android.util.Log;

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


  /* ==================== ADD ===================================== */
  public void ADD() {
    cur.stack[cur.numArgs + 0] += cur.stack[cur.numArgs + 1];
  }

  /* ==================== SUB ===================================== */
  public void SUB() {
    cur.stack[cur.numArgs + 0] -= cur.stack[cur.numArgs + 1];
  }

  /* ==================== DO ===================================== */
  public void DIV() {
    if (cur.stack[cur.numArgs + 1] == 0) {
      cur.error = FTError.ErrorTag.INTERP_DIVIDE_BY_ZERO;
    } else {
      cur.stack[cur.numArgs + 0] = FTCalc.FT_MulDivNoRound(cur.stack[cur.numArgs + 0], 64, cur.stack[cur.numArgs + 1]);
    }
  }

  /* ==================== MUL ===================================== */
  public void MUL() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_MulDiv(cur.stack[cur.numArgs + 0], cur.stack[cur.numArgs + 1], 64);
  }

  /* ==================== ABS ===================================== */
  public void ABS() {
    cur.stack[cur.numArgs + 0] = (int)(FTCalc.FT_ABS(cur.stack[cur.numArgs + 0]) & 0xFFFF);
  }

  /* ==================== NEG ===================================== */
  public void NEG() {
    cur.stack[cur.numArgs + 0] = -cur.stack[cur.numArgs + 0];
  }

  /* ==================== FLOOR ===================================== */
  public void FLOOR() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_PIX_FLOOR(cur.stack[cur.numArgs + 0]);
  }

  /* ==================== CEILING ===================================== */
  public void CEILING() {
    cur.stack[cur.numArgs + 0] = FTCalc.FT_PIX_CEIL(cur.stack[cur.numArgs + 0]);
  }

  /* ==================== ROUND ===================================== */
  public void ROUND() {
    cur.stack[cur.numArgs + 0] = cur.render_funcs.curr_round_func.round(cur.stack[cur.numArgs + 0], cur.tt_metrics.getCompensations()[cur.opcode.getVal() - 0x68]);
  }

  /* ==================== NROUND ===================================== */
  public void NROUND() {
    cur.stack[cur.numArgs + 0] = cur.render_funcs.round_none.round(cur.stack[cur.numArgs + 0], cur.tt_metrics.getCompensations()[cur.opcode.getVal() - 0x6C]);
  }

}