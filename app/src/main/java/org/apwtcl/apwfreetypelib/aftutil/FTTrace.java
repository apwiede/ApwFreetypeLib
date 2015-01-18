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

package org.apwtcl.apwfreetypelib.aftutil;

  /* ===================================================================== */
  /*    FT_Trace                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTTrace extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FT_Trace";

  public static int trace_level = 6;

  /* ==================== FT_Trace ================================== */
  public FTTrace() {
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
 
  /* ==================== Trace ===================================== */
  public static void Trace(int level, String tag, String msg) {
    if (level >= trace_level) {
      Debug(0, DebugTag.DBG_BASE, TAG, tag+": "+msg+".");
    }
  }

}