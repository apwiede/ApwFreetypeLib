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
  /*    FTCBasicAttrRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTCBasicAttrRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicAttrRec";

  private FTCScalerRec scaler = null;
  private int load_flags = 0;

  /* ==================== FTCBasicAttrRec ================================== */
  public FTCBasicAttrRec() {
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
    str.append("..load_flags: "+load_flags+'\n');
    return str.toString();
  }

  /* ==================== getScaler ================================== */
  public FTCScalerRec getScaler() {
    return scaler;
  }

  /* ==================== setScaler ================================== */
  public void setScaler(FTCScalerRec scaler) {
    this.scaler = scaler;
  }

  /* ==================== getLoad_flags ================================== */
  public int getLoad_flags() {
    return load_flags;
  }

  /* ==================== setLoad_flags ================================== */
  public void setLoad_flags(int load_flags) {
    this.load_flags = load_flags;
  }

}