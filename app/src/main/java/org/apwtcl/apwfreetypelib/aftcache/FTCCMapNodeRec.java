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
  /*    FTCCMapNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCCMapNodeRec extends FTCNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCCMapNodeRec";

  public final static int FTC_CMAP_INDICES_MAX = 128;
  public final static int FTC_CMAP_UNKNOWN = ~0;

  private Object face_id = null;
  private int cmap_index = 0;
  private int first = 0;
  private int indices[] = null;

  /* ==================== FTCCMapNodeRec ================================== */
  public FTCCMapNodeRec() {
    oid++;
    id = oid;
      
    indices =  new int[FTC_CMAP_INDICES_MAX];
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
    str.append("..first: "+first+'\n');
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

  /* ==================== getFirst ================================== */
  public int getFirst() {
    return first;
  }

  /* ==================== setFirst ================================== */
  public void setFirst(int first) {
    this.first = first;
  }

  /* ==================== getIndices ================================== */
  public int[] getIndices() {
    return indices;
  }

  /* ==================== setIndices ================================== */
  public void setIndices(int[] indices) {
    this.indices = indices;
  }

}