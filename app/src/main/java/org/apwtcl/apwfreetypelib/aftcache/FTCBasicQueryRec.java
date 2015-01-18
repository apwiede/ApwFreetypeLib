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
  /*    FTCBasicQueryRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCBasicQueryRec extends FTCGQueryRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicQueryRec";

  private FTCBasicAttrRec attrs = null;

  /* ==================== FTCBasicQueryRec ================================== */
  public FTCBasicQueryRec() {
    oid++;
    id = oid;
    attrs = new FTCBasicAttrRec();
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

  /* ==================== getAttrs ================================== */
  public FTCBasicAttrRec getAttrs() {
    return attrs;
  }

  /* ==================== setAttrs ================================== */
  public void setAttrs(FTCBasicAttrRec attrs) {
    this.attrs = attrs;
  }

}