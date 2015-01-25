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
  /*    TTMoveYFunc                                                        */
  /*    et of Move functions for interpreter                               */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;

public class TTMoveYFunc extends TTMoveFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTMoveYFunc";

  /* ==================== TTMoveYFunc ================================== */
  public TTMoveYFunc()
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
   * DirectMoveY
   *
   * =====================================================================
   */
  /* ==================== move ===================================== */
  @Override
  public void move(TTGlyphZoneRec zone, int point, int distance) {
Debug(0, DebugTag.DBG_INTERP, TAG, "DirectMoveY");
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("Direct_Move_Y: %d %d %d\n", point, zone.getCurPoint_y(point), distance));
    zone.setCurPoint_y(point, zone.getCurPoint_y(point) + distance);
    zone.getTags()[point] = Flags.Curve.getTableTag(zone.getTags()[point].getVal() | Flags.Curve.TOUCH_Y.getVal());
  }

  /* =====================================================================
   * DirectMoveOrigY
   *
   * =====================================================================
   */
  /* ==================== moveOrig ===================================== */
  @Override
  public void moveOrig(TTGlyphZoneRec zone, int point, int distance) {
Debug(0, DebugTag.DBG_INTERP, TAG, "DirectMoveOrigY");
    zone.setOrgPoint_y(point, zone.getOrgPoint_y(point) + distance);
  }

}