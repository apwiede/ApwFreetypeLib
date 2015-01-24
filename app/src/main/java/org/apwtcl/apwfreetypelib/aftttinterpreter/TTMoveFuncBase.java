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
  /*    TTMoveFuncBase                                                      */
  /*    base for set of Move functions for interpreter                      */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTMoveFuncBase extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTMoveFuncBase";

  protected TTExecContextRec cur = null;

  /* ==================== TTMoveFuncBase ================================== */
  public TTMoveFuncBase()
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

  /* ==================== setCur ================================== */
  public void setCur(TTExecContextRec cur) {
    this.cur = cur;
  }

  /* ==================== _showZp0Zp1OrgCur ================================ */
  protected static void _showZp0Zp1OrgCur( String str, TTExecContextRec cur ) {
    Debug(0, DebugTag.DBG_RENDER, TAG, str);
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.org: "+cur.zp0.xgetOrg());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.org: "+cur.zp1.xgetOrg());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.org: "+cur.zp2.xgetOrg());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.cur: "+cur.zp0.xgetCur());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.cur: "+cur.zp1.xgetCur());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.cur: "+cur.zp2.xgetCur());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.orus: "+cur.zp0.getOrus());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.orus: "+cur.zp1.getOrus());
    Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.orus: "+cur.zp2.getOrus());
    for( int i = 0; i < 6; i++) {
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp0.org: %d x: %d, y: %d\n", i, cur.zp0.getOrgPoint_x(i), cur.zp0.getOrgPoint_y(i)));
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp1.org: %d x: %d, y: %d\n", i, cur.zp1.getOrgPoint_x(i), cur.zp1.getOrgPoint_y(i)));
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp0.cur: %d x: %d, y: %d\n", i, cur.zp0.getCurPoint_x(i), cur.zp0.getCurPoint_y(i)));
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp1.cur: %d x: %d, y: %d\n", i, cur.zp1.getCurPoint_x(i), cur.zp1.getCurPoint_y(i)));
    }
  }

  /* ==================== move ===================================== */
  public void move(TTGlyphZoneRec zone, int point, int distance) {
    Log.e(TAG, "move not yet implemented");
  }

  /* ==================== moveOrig ===================================== */
  public void moveOrig(TTGlyphZoneRec zone, int point, int distance) {
    Log.e(TAG, "moveOrig not yet implemented");
  }

}