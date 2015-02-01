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
  /*    TTMoveFunc                                                         */
  /*    set of Move functions for interpreter                              */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;

public class TTMoveFunc extends TTMoveFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTMoveFunc";

  /* ==================== TTMoveFunc ================================== */
  public TTMoveFunc()
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
   * DirectMove
   *
   * <Description>
   *    Moves a point by a given distance along the freedom vector.  The
   *    point will be `touched'.
   *
   * <Input>
   *    point    :: The index of the point to move.
   *
   *    distance :: The distance to apply.
   *
   * <InOut>
   *    zone     :: The affected glyph zone.
   *
   * =====================================================================
   */
  /* ==================== move ===================================== */
  @Override
  public void move(TTGlyphZoneRec zone, int point, int distance) {
    int v;
    Debug(0, DebugTag.DBG_INTERP, TAG, "DirectMove");
    v = cur.graphics_state.freeVector.getX();
    if (v != 0) {
      zone.setCurPoint_x(point,  zone.getCurPoint_x(point) + FTCalc.FT_MulDiv(distance, v, cur.F_dot_P));
      zone.addTag(point, Flags.Curve.TOUCH_X);
    }
    v = cur.graphics_state.freeVector.getY();
    if (v != 0) {
      zone.setCurPoint_y(point, zone.getCurPoint_y(point) + FTCalc.FT_MulDiv(distance, v, cur.F_dot_P));
      zone.addTag(point, Flags.Curve.TOUCH_Y);
    }
  }

  /* =====================================================================
   * DirectMoveOrig
   *
   * =====================================================================
   */
  /* ==================== moveOrig ===================================== */
  @Override
  public void moveOrig(TTGlyphZoneRec zone, int point, int distance) {
    Log.w(TAG, "DirectMoveOrig not yet implemented!!");
  }

}