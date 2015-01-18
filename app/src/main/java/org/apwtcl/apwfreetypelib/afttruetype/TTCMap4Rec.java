/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTCMap4Rec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class TTCMap4Rec extends TTCMapRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMap4Rec";

  private int cur_charcode = 0;   /* current charcode */
  private int cur_gindex = 0;     /* current glyph index */
  private int num_ranges = 0;
  private int cur_range = 0;
  private int cur_start = 0;
  private int cur_end = 0;
  private int cur_delta = 0;
  private byte[] cur_values = null;
  private int cur_values_idx = 0;

  /* ==================== TTCMap4Rec ================================== */
  public TTCMap4Rec() {
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

  /* ==================== getCur_charcode ================================== */
  public int getCur_charcode() {
    return cur_charcode;
  }

  /* ==================== setCur_charcode ================================== */
  public void setCur_charcode(int cur_charcode) {
    this.cur_charcode = cur_charcode;
  }

  /* ==================== getCur ================================== */
  public int getCur_gindex() {
    return cur_gindex;
  }

  /* ==================== setCur ================================== */
  public void setCur_gindex(int cur_gindex) {
    this.cur_gindex = cur_gindex;
  }

  /* ==================== getNum_ranges ================================== */
  public int getNum_ranges() {
    return num_ranges;
  }

  /* ==================== setNum_ranges ================================== */
  public void setNum_ranges(int num_ranges) {
    this.num_ranges = num_ranges;
  }

  /* ==================== getCur_range ================================== */
  public int getCur_range() {
    return cur_range;
  }

  /* ==================== setCur_range ================================== */
  public void setCur_range(int cur_range) {
    this.cur_range = cur_range;
  }

  /* ==================== getCur_start ================================== */
  public int getCur_start() {
    return cur_start;
  }

  /* ==================== setCur_start ================================== */
  public void setCur_start(int cur_start) {
    this.cur_start = cur_start;
  }

  /* ==================== getCur_end ================================== */
  public int getCur_end() {
    return cur_end;
  }

  /* ==================== setCur_end ================================== */
  public void setCur_end(int cur_end) {
    this.cur_end = cur_end;
  }

  /* ==================== getCur_delta ================================== */
  public int getCur_delta() {
    return cur_delta;
  }

  /* ==================== setCur_delta ================================== */
  public void setCur_delta(int cur_delta) {
    this.cur_delta = cur_delta;
  }

  /* ==================== getCur_values ================================== */
  public byte[] getCur_values() {
    return cur_values;
  }

  /* ==================== setCur_values ================================== */
  public void setCur_values(byte[] cur_values) {
    this.cur_values = cur_values;
  }

  /* ==================== getCur_values_idx ================================== */
  public int getCur_values_idx() {
    return cur_values_idx;
  }

  /* ==================== setCur_values_idx ================================== */
  public void setCur_values_idx(int cur_values_idx) {
    this.cur_values_idx = cur_values_idx;
  }

}