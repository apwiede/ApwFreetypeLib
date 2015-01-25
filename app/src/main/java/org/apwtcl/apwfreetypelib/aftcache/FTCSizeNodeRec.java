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
  /*    FTCSizeNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;

public class FTCSizeNodeRec extends FTCMruNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCSizeNodeRec";

  private FTSizeRec size = null;
  private FTCScalerRec scaler = null;

  /* ==================== FTCSizeNodeRec ================================== */
  public FTCSizeNodeRec() {
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

  /* ==================== getSize ================================== */
  public FTSizeRec getSize() {
    return size;
  }

  /* ==================== setSize ================================== */
  public void setSize(FTSizeRec size) {
    this.size = size;
  }

  /* ==================== getScaler ================================== */
  public FTCScalerRec getScaler() {
    return scaler;
  }

  /* ==================== setScale ================================== */
  public void setScaler(FTCScalerRec scaler) {
    this.scaler = scaler;
  }

}