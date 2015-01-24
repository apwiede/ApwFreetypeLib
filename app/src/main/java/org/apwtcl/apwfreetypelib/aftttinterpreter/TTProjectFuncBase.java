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
  /*    TTProjectFuncBase                                                  */
  /*    base for set of Project functions for interpreter                  */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTInterpRun;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTProjectFuncBase extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTProjectFuncBase";

  /* ==================== TTProjectFuncBase ================================== */
  public TTProjectFuncBase()
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
   * TTDotFix14
   *
   * compute (ax*bx+ay*by)/2^14 with maximum accuracy and rounding
   * =====================================================================
   */
  protected int TTDotFix14(int ax, int ay, int bx, int by) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "TT_DotFix14");
    int m;
    int s;
    int hi1;
    int hi2;
    int hi;
    int l;
    int lo1;
    int lo2;
    int lo;

      /* compute ax*bx as 64-bit value */
    l = (int)((ax & 0xFFFF) * bx);
    m = (ax >> 16) * (int)bx;
    lo1 = l + ((int)m << 16);
    hi1 = (m >> 16) + ((int)l >> 31) + (lo1 < l ? 1 : 0);
      /* compute ay*by as 64-bit value */
    l = (int)((ay & 0xFFFF) * by);
    m = (ay >> 16) * (int)by;
    lo2 = l + ((int)m << 16);
    hi2 = (m >> 16) + ((int)l >> 31) + (lo2 < l ? 1 : 0);
      /* add them */
    lo = lo1 + lo2;
    hi = hi1 + hi2 + (lo < lo1 ? 1 : 0);
      /* divide the result by 2^14 with rounding */
    s = hi >> 31;
    l = lo + (int)s;
    hi += s + (l < lo ? 1 : 0);
    lo  = l;
    l = lo + 0x2000;
    hi += (l < lo ? 1 : 0);
    return (int)(((int)hi << 18) | (l >> 14));
  }

  /* ==================== project ===================================== */
  public int project(int dx, int dy) {
    Log.e(TAG, "project not yet implemented");
    return -1;
  }

  /* =====================================================================
   * DualProject
   *
   * <Description>
   *    Computes the projection of the vector given by (v2-v1) along the
   *    current dual vector.
   *
   * <Input>
   *    v1 :: First input vector.
   *    v2 :: Second input vector.
   *
   * <Return>
   *    The distance in F26dot6 format.
   *
   * =====================================================================
   */
  /* ==================== dualproject ===================================== */
  public int dualproject(int dx, int dy) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "DualProject");
    return TTDotFix14(dx, dy, TTRenderFunc.cur.graphics_state.getDualVector().x, TTRenderFunc.cur.graphics_state.getDualVector().y);
  }

}
