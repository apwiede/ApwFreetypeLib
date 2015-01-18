package org.apwtcl.apwfreetypelib.aftttinterpreter;

/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Round (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri (dot) de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

  /* ===================================================================== */
  /*    TTRoundFuncBase                                                    */
  /*    base for set of Round functions for interpreter                    */
  /* ===================================================================== */

import android.util.Log;
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTRoundFuncBase extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRoundFuncBase";


  TTExecContextRec cur = null;

  /* ==================== TTRoundFuncBase ================================== */
  public TTRoundFuncBase()
  {
    oid++;
    id = oid;
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


  public void setCur (TTExecContextRec cur) {
    this.cur = cur;
  }

  /* ====================================================================
   * <Function>
   *    SetSuperRound
   *
   * <Description>
   *    Sets Super Round parameters.
   *
   * <Input>
   *    GridPeriod :: The grid period.
   *
   *    selector   :: The SROUND opcode.
   * ====================================================================
   */
  protected void SetSuperRound(int grid_period, int selector) {
    switch (selector & 0xC0) {
      case 0:
        cur.period = grid_period / 2;
        break;
      case 0x40:
        cur.period = grid_period;
        break;
      case 0x80:
        cur.period = grid_period * 2;
        break;
      /* This opcode is reserved, but... */
      case 0xC0:
        cur.period = grid_period;
        break;
    }
    switch (selector & 0x30) {
      case 0:
        cur.phase = 0;
        break;
      case 0x10:
        cur.phase = cur.period / 4;
        break;
      case 0x20:
        cur.phase = cur.period / 2;
        break;
      case 0x30:
        cur.phase = cur.period * 3 / 4;
        break;
    }
    if ((selector & 0x0F) == 0) {
      cur.threshold = cur.period - 1;
    } else {
      cur.threshold = ((selector & 0x0F) - 4) * cur.period / 8;
    }
    cur.period    /= 256;
    cur.phase     /= 256;
    cur.threshold /= 256;
  }

  /* ==================== round ===================================== */
  public int round(int distance, int compensation) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "round not yet implemented");
    return -1;
  }

}
