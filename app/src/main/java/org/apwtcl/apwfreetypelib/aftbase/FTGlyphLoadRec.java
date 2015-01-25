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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTGlyphLoadRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTGlyphLoadRec extends FTOutlineRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGlyphLoadRec";

  private FTVectorRec[] extra_points = null;  /* extra points table        */
  private int extra_points_idx = 0;
  private FTVectorRec[] extra_points2 = null; /* second extra points table */
  private int extra_points2_idx = 0;
  private int num_subglyphs = 0;              /* number of subglyphs       */
  private FTSubGlyphRec[] subglyphs = null;   /* subglyphs                 */
  private int subglyphs_idx;

  /* ==================== FTGlyphLoadRec ================================== */
  public FTGlyphLoadRec() {
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
    str.append("..extra_points_idx: "+""+'\n');
    str.append("..extra_points2_idx: "+""+'\n');
    str.append("..num_subglyphs: "+""+'\n');
    str.append("..subglyphs_idx: "+""+'\n');
    return str.toString();
  }
 
  /* ==================== copy ===================================== */
  public int copy( FTGlyphLoadRec from ) {
//FIXME ned to copy the fields here !!!
//    outline.copy(from.outline);
    extra_points = from.extra_points;
    extra_points2 = from.extra_points2;
    num_subglyphs = from.num_subglyphs;
    subglyphs = from.subglyphs;
    subglyphs_idx = from.subglyphs_idx;
    extra_points_idx = from.extra_points_idx;
    extra_points2_idx = from.extra_points2_idx;
    return 0;
  }

  /* ==================== showGLoaderGlyph ================================== */
  public void showGloaderGlyph(String str) {
    int j;

    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, str);
    if (points != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderLoad.outline.points: "+points+"!"+points_idx);
    }
    if (extra_points != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderLoad.extra_points: "+extra_points+"!"+extra_points_idx);
    }
    if (extra_points2 != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderLoad.extra_points2: "+extra_points2+"!"+extra_points2_idx);
    }
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "Show loaderLoad");
    for (j = 0; j < n_points; j++) {
      if (points != null) {
        if (points_idx + j < points.length) {
          if (getPoint(j) != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format(" outl: %d %5d %5d", j, getPoint(j).getX(), getPoint(j).getY()));
          }
        }
      }
      if (extra_points != null) {
        if (j < extra_points.length) {
          if (getExtra_point(j) != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format(" ext1: %d %5d %5d", j, getExtra_point(j).getX(), getExtra_point(j).getY()));
          }
        }
      }
      if (extra_points2 != null) {
        if (extra_points2_idx + j < extra_points2.length) {
          if (getExtra_point2(j) != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format(" ext2: %d %5d %5d", j, getExtra_point2(j).getX(), getExtra_point2(j).getY()));
          }
        }
      }
    }
  }

  /* ==================== getExtra_point ===================================== */
  public FTVectorRec getExtra_point(int idx) {
    return extra_points[idx+extra_points_idx];
  }

  /* ==================== setExtra_point ===================================== */
  public void setExtra_point(int idx, FTVectorRec vec) {
    extra_points[idx+extra_points_idx] = vec;
  }

  /* ==================== getExtra_points ===================================== */
  public FTVectorRec[] getExtra_points() {
    return extra_points;
  }

  /* ==================== setExtra_points ===================================== */
  public void setExtra_points(FTVectorRec[] extra_points) {
    this.extra_points = extra_points;
  }

  /* ==================== getExtra_points_idx ===================================== */
  public int getExtra_points_idx() {
    return extra_points_idx;
  }

  /* ==================== setExtra_points_idx ===================================== */
  public void setExtra_points_idx(int extra_points_idx) {
    this.extra_points_idx = extra_points_idx;
  }

  /* ==================== getExtra_point2 ===================================== */
  public FTVectorRec getExtra_point2(int idx) {
    return extra_points2[idx+extra_points2_idx];
  }

  /* ==================== setExtra_point2 ===================================== */
  public void setExtra_point2(int idx, FTVectorRec vec) {
    extra_points2[idx+extra_points2_idx] = vec;
  }

  /* ==================== getExtra_points2 ===================================== */
  public FTVectorRec[] getExtra_points2() {
    return extra_points2;
  }

  /* ==================== setExtra_points2 ===================================== */
  public void setExtra_points2(FTVectorRec[] extra_points2) {
    this.extra_points2 = extra_points2;
  }

  /* ==================== getExtra_points2_idx ===================================== */
  public int getExtra_points2_idx() {
    return extra_points2_idx;
  }

  /* ==================== setExtra_points2_idx ===================================== */
  public void setExtra_points2_idx(int extra_points2_idx) {
    this.extra_points2_idx = extra_points2_idx;
  }

  /* ==================== getNum_subglyphs ===================================== */
  public int getNum_subglyphs() {
    return num_subglyphs;
  }

  /* ==================== setNum_subglyphs ===================================== */
  public void setNum_subglyphs(int num_subglyphs) {
    this.num_subglyphs = num_subglyphs;
  }

  /* ==================== getSubglyphs ===================================== */
  public FTSubGlyphRec[] getSubglyphs() {
    return subglyphs;
  }

  /* ==================== setSubglyphs ===================================== */
  public void setSubglyphs(FTSubGlyphRec[] subglyphs) {
    this.subglyphs = subglyphs;
  }

  /* ==================== getSubglyphs_idx ===================================== */
  public int getSubglyphs_idx() {
    return subglyphs_idx;
  }

  /* ==================== setSubglyphs_idx ===================================== */
  public void setSubglyphs_idx(int subglyphs_idx) {
    this.subglyphs_idx = subglyphs_idx;
  }

}