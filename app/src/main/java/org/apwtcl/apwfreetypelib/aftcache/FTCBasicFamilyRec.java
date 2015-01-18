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
  /*    FTCBasicFamilyRec                                                  */
  /*                                                                       */
  /* ===================================================================== */

public class FTCBasicFamilyRec extends FTCFamilyRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicFamilyRec";

  public FTCBasicAttrRec attrs = null;

  /* ==================== FTCBasicFamilyRec ================================== */
  public FTCBasicFamilyRec() {
    oid++;
    id = oid;
  }
    
    /* ==================== FTCBasicFamilyRec ================================== */
    public void XFTCBasicFamilyRec(FTCMruNodeRec mru_node) {
//      super(mru_node);
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

  /* ==================== getAttrs ================================== */
  public FTCBasicAttrRec getAttrs() {
    return attrs;
  }

  /* ==================== setAttrs ================================== */
  public void setAttrs(FTCBasicAttrRec attrs) {
    this.attrs = attrs;
  }

}