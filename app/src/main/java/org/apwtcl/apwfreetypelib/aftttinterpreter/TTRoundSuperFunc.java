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
  /*    TTRoundSuperFunc                                                    */
  /*    set of Round functions for interpreter                             */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTRoundSuperFunc extends TTRoundFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRoundSuperFunc";

  /* ==================== TTRoundSuperFunc ================================== */
  public TTRoundSuperFunc()
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
   *
   * <Function>
   *    RoundSuper
   *
   * <Description>
   *    Super-rounds value to grid after adding engine compensation.
   *
   * <Input>
   *    distance     :: The distance to round.
   *
   *    compensation :: The engine compensation.
   *
   * <Return>
   *    Rounded distance.
   *
   * <Note>
   *    The TrueType specification says very few about the relationship
   *    between rounding and engine compensation.  However, it seems from
   *    the description of super round that we should add the compensation
   *    before rounding.
   *
   * =====================================================================
   */
  /* ==================== round ===================================== */
  @Override
  public int round(int distance, int compensation) {
    int val;

    if (distance >= 0) {
      val = (distance - cur.phase + cur.threshold + compensation) & -cur.period;
      if (distance != 0 && val < 0) {
        val = 0;
      }
      val += cur.phase;
    } else {
      val = -((cur.threshold - cur.phase - distance + compensation) & -cur.period );
      if (val > 0) {
        val = 0;
      }
      val -= cur.phase;
    }
    return val;
  }

}
