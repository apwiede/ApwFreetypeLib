/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf at wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCGNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCGNodeRec extends FTCNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCGNodeRec";

  private FTCFamilyRec family = null;
  private int gindex = 0;

  /* ==================== FTCGNodeRec ================================== */
  public FTCGNodeRec() {
    oid++;
    id = oid;
      
    family = new FTCFamilyRec();
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
    str.append("..gindex: "+gindex+'\n');
    return str.toString();
  }

  /* ==================== getFamily ===================================== */
  public FTCFamilyRec getFamily() {
    return family;
  }

  /* ==================== setFamily ===================================== */
  public void setFamily(FTCFamilyRec family) {
    this.family = family;
  }

  /* ==================== getGindex ===================================== */
  public int getGindex() {
    return gindex;
  }

  /* ==================== getGindex ===================================== */
  public void setGindex(int gindex) {
    this.gindex = gindex;
  }

}