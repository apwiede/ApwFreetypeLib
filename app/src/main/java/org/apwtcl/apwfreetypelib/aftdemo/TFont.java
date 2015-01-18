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

package org.apwtcl.apwfreetypelib.aftdemo;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TFont extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TFont";

  private String filepathname = null;
  private int face_index = 0;
  private int cmap_index = 0;
  private int num_indices = 0;
  private Object file_address = null;  /* for preloaded files */
  private long file_size = 0L;

  /* ==================== TFont ================================== */
  public TFont() {
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

  /* ==================== getFilepathname ================================== */
  public String getFilepathname() {
    return filepathname;
  }

  /* ==================== setFilepathname ================================== */
  public void setFilepathname(String filepathname) {
    this.filepathname = filepathname;
  }

  /* ==================== getFace_index ================================== */
  public int getFace_index() {
    return face_index;
  }

  /* ==================== setFace_index ================================== */
  public void setFace_index(int face_index) {
    this.face_index = face_index;
  }

  /* ==================== getCmap_index ================================== */
  public int getCmap_index() {
    return cmap_index;
  }

  /* ==================== setCmap_index ================================== */
  public void setCmap_index(int cmap_index) {
    this.cmap_index = cmap_index;
  }

  /* ==================== getNum_indices ================================== */
  public int getNum_indices() {
    return num_indices;
  }

  /* ==================== setNum_indices ================================== */
  public void setNum_indices(int num_indices) {
    this.num_indices = num_indices;
  }

  /* ==================== getFile_address ================================== */
  public Object getFile_address() {
    return file_address;
  }

  /* ==================== setFile_address ================================== */
  public void setFile_address(Object file_address) {
    this.file_address = file_address;
  }

  /* ==================== getFile_size ================================== */
  public long getFile_size() {
    return file_size;
  }

  /* ==================== setFile_size ================================== */
  public void setFile_size(long file_size) {
    this.file_size = file_size;
  }

}