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
  /*    TTRoundNoneFunc                                                    */
  /*    set of Round functions for interpreter                             */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTRoundNoneFunc extends TTRoundFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRoundNoneFunc";

  /* ==================== TTRoundNoneFunc ================================== */
  public TTRoundNoneFunc()
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

  /* ====================================================================
   * <Function>
   *    RoundNone
   *
   * <Description>
   *    Does not round, but adds engine compensation.
   *
   * <Input>
   *    distance     :: The distance (not) to round.
   *
   *    compensation :: The engine compensation.
   *
   * <Return>
   *    The compensated distance.
   *
   * <Note>
   *    The TrueType specification says very few about the relationship
   *    between rounding and engine compensation.  However, it seems from
   *    the description of super round that we should add the compensation
   *    before rounding.
   * ====================================================================
   */
  /* ==================== round ===================================== */
  @Override
  public int round(int distance, int compensation) {
    int val;

    if (distance >= 0) {
      val = distance + compensation;
      if (distance != 0 && val < 0) {
        val = 0;
      }
    } else {
      val = distance - compensation;
      if (val > 0) {
        val = 0;
      }
    }
    return val;
  }

}