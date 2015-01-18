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
  /*    TTMoveXFunc                                                        */
  /*    set of Move functions for interpreter                              */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTInterpBase;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTMoveXFunc extends TTMoveFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTMoveXFunc";

  /* ==================== TTMoveXFunc ================================== */
  public TTMoveXFunc()
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
   * DirectMoveX
   *
   * =====================================================================
   */
  /* ==================== move ===================================== */
  @Override
  public void move(TTGlyphZoneRec zone, int point, int distance) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "DirectMoveX");
//    FTGlyphLoaderRec._showLoaderZone("DirectMoveX");
    Debug(0, DebugTag.DBG_INTERP, TAG, "zone: "+zone.toDebugString()+"!");
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("Direct_Move_X: point: %d x: %d distance: %d\n", point, zone.getCur()[zone.getCur_idx() + point].x, distance));
    zone.getCur()[zone.getCur_idx() + point].x += distance;
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("Direct_Move_X2: cur_idx: %d point: %d x: %d distance: %d\n",
        zone.getCur_idx(), point, zone.getCur()[zone.getCur_idx() + point].x, distance));
    zone.getTags()[point] = Flags.Curve.getTableTag(zone.getTags()[point].getVal() | Flags.Curve.TOUCH_X.getVal());
  }

  /* =====================================================================
   * DirectMoveOrigX
   *
   * =====================================================================
   */
  /* ==================== moveOrig ===================================== */
  @Override
  public void moveOrig(TTGlyphZoneRec zone,  int point, int distance) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "DirectMoveOrigX");
    zone.getOrg()[zone.getOrg_idx() + point].x += distance;
  }

}
