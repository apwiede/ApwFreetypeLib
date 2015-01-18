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
  /*    TTCMapInfoRec                                                      */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftcache.FTCNodeRec;

public class TTCMapInfoRec extends FTCNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMapInfoRec";

  public final static int FTC_CMAP_INDICES_MAX = 128;
  public final static int FTC_CMAP_UNKNOWN = ~0;

  private int language = 0;
  private int format = 0;

  /* ==================== TTCMapInfoRec ================================== */
  public TTCMapInfoRec() {
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

  /* ==================== getLanguage ===================================== */
  public int getLanguage() {
    return language;
  }

  /* ==================== setLanguage ===================================== */
  public void setLanguage(int language) {
    this.language = language;
  }

  /* ==================== getFormat ===================================== */
  public int getFormat() {
    return format;
  }

  /* ==================== setFormat ===================================== */
  public void setFormat(int format) {
    this.format = format;
  }

}