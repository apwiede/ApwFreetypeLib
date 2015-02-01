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
  /*    TTRoundToGridFunc                                                  */
  /*    set of Round functions for interpreter                             */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTRoundToGridFunc extends TTRoundFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRoundToGridFunc";

  /* ==================== TTRoundToGridFunc ================================== */
  public TTRoundToGridFunc()
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

  /* =====================================================================
   * RoundToGrid
   *
   * <Description>
   *    Rounds value to grid after adding engine compensation.
   *
   * <Input>
   *    distance     :: The distance to round.
   *
   *    compensation :: The engine compensation.
   *
   * <Return>
   *    Rounded distance.
   *
   * =====================================================================
   */
  /* ==================== round ===================================== */
  @Override
  public int round(int distance, int compensation) {
    int val;
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("RoundToGrid: distance: %d, compensation: %d", distance, compensation));

    if (distance >= 0) {
      val = distance + compensation + 32;
      if (distance != 0 && val > 0) {
        val &= ~63;
      } else {
        val = 0;
      }
    } else {
      val = -FTCalc.FT_PIX_ROUND(compensation - distance);
      if (val > 0) {
        val = 0;
      }
    }
    return  val;
  }

}
