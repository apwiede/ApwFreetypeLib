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
  /*    FTCCMapQueryRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCCMapQueryRec extends FTCGQueryRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCCMapQueryRec";

  public final static int FTC_CMAP_INDICES_MAX = 128;

  private Object face_id = null;
  private int cmap_index = 0;
  private int char_code = 0;

  /* ==================== FTCCMapQueryRec ================================== */
  public FTCCMapQueryRec() {
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
    str.append("..cmap_index: "+cmap_index+'\n');
    str.append("..char_code: "+char_code+'\n');
    return str.toString();
  }

  /* ==================== getFace_id ================================== */
  public Object getFace_id() {
    return face_id;
  }

  /* ==================== setFace_id ================================== */
  public void setFace_id(Object face_id) {
    this.face_id = face_id;
  }

  /* ==================== getCmap_index ================================== */
  public int getCmap_index() {
    return cmap_index;
  }

  /* ==================== setCmap_index ================================== */
  public void setCmap_index(int cmap_index) {
    this.cmap_index = cmap_index;
  }

  /* ==================== getChar_code ================================== */
  public int getChar_code() {
    return char_code;
  }

  /* ==================== setChar_code ================================== */
  public void setChar_code(int char_code) {
    this.char_code = char_code;
  }

}