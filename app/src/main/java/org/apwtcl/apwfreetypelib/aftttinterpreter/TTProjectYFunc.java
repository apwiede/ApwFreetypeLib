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
  /*    TTProjectYFunc                                                     */
  /*    set of Project functions for interpreter                           */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTProjectYFunc extends TTProjectFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTProjectYFunc";

  /* ==================== TTProjectYFunc ================================== */
  public TTProjectYFunc()
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
   * ProjectY
   *
   * <Description>
   *    Computes the projection of the vector given by (v2-v1) along the
   *    vertical axis.
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
  /* ==================== project ===================================== */
  @Override
  public int project(int dx, int dy) {
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("ProjectY: dx: %d, dy: %d", dx, dy));
    return dy;
  }

  /* ==================== dualproject ===================================== */
  @Override
  public int dualproject(int dx, int dy) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "DualProject");
    return project(dx, dy);
  }

}