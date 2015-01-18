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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTCMap12Rec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class TTCMap12Rec extends TTCMapRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMap12Rec";

  private boolean valid = false;
  private long cur_charcode = 0;
  private int cur_gindex = 0;
  private long cur_group = 0;
  private long num_groups = 0;

  /* ==================== TTCMap12Rec ================================== */
  public TTCMap12Rec() {
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

}