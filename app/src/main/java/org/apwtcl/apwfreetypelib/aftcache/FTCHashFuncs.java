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

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCHashFuncs                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTCHashFuncs extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCHashFuncs";

  public final static int FTC_CMAP_INDICES_MAX = 128;

  /* ==================== FTCHashFuncs ================================== */
  public FTCHashFuncs() {
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
   * _FTC_FACE_ID_HASH
   * =====================================================================
   */
  public static int _FTC_FACE_ID_HASH(Object obj_i) {
    int i = obj_i.hashCode();
    return ((i >> 3) ^ (i << 7));
  }

  /* =====================================================================
   * FTC_SCALER_HASH
   * =====================================================================
   */
  public static int FTC_SCALER_HASH(FTCScalerRec q) {
    int val1 = _FTC_FACE_ID_HASH(q);
    int val2 = q.getFace_id().hashCode();
    return val1 + val2 + q.getWidth() + q.getHeight() * 7 +
      (q.getPixel() != 0 ? 0 : q.getX_res() * 33 ^ q.getY_res() * 61);
  }

  /* =====================================================================
   * FTC_CMAP_HASH
   * =====================================================================
   */
  public static int FTC_CMAP_HASH(Object faceid, int index, int charcode) {
    return _FTC_FACE_ID_HASH(faceid) + 211 * index +
     (charcode / FTC_CMAP_INDICES_MAX);
  }

  /* =====================================================================
   * FTC_BASIC_ATTR_HASH
   * =====================================================================
   */
  public static int FTC_BASIC_ATTR_HASH(FTCBasicAttrRec a) {
    return FTC_SCALER_HASH(a.getScaler()) + 31*a.getLoad_flags();
  }
 
}