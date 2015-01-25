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
  /*    FTStrokerRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTStrokerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTStrokerRec";

  public final static int FT_STROKER_LINECAP_BUTT = 0;
  public final static int FT_STROKER_LINECAP_ROUND = 1;
  public final static int FT_STROKER_LINECAP_SQUARE = 2;
  public final static int FT_STROKER_LINEJOIN_ROUND = 0;
  public final static int FT_STROKER_LINEJOIN_BEVEL = 1;
  public final static int FT_STROKER_LINEJOIN_MITER_VARIABLE = 2;
  public final static int FT_STROKER_LINEJOIN_MITER = 2;
  public final static int FT_STROKER_LINEJOIN_MITER_FIXED = 3;

  private int angle_in = 0;                    /* direction into curr join */
  private int angle_out = 0;                   /* direction out of join  */
  private FTVectorRec center = null;           /* current position */
  private int line_length = 0;                 /* length of last lineto */
  private boolean first_point = false;         /* is this the start? */
  private boolean subpath_open = false;        /* is the subpath open? */
  private int subpath_angle = 0;               /* subpath start direction */
  private FTVectorRec subpath_start = null;    /* subpath start position */
  private int subpath_line_length = 0;         /* subpath start lineto len */
  private boolean handle_wide_strokes = false; /* use wide strokes logic? */
  private int line_cap = 0;
  private int line_join = 0;
  private int line_join_saved = 0;
  private int miter_limit = 0;
  private int radius = 0;
  private FTStrokeBorderRec[] borders = null;
  private FTLibraryRec library = null;

  /* ==================== FTStrokerRec ================================== */
  public FTStrokerRec(FTLibraryRec library) {
    oid++;
    id = oid;

    if (library == null) {
      Log.e(TAG, "constructor library == null!");
      return;
    }
    this.library = library;
    borders = new FTStrokeBorderRec[2];
    borders[0] = new FTStrokeBorderRec();
    borders[1] = new FTStrokeBorderRec();
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
      str.append("..angle_in: "+angle_in+'\n');
      str.append("..angle_out: "+angle_out+'\n');
      str.append("..center: "+center.x+" "+center.y+'\n');
      str.append("..line_length: "+line_length+'\n');
      str.append("..first_point: "+first_point+'\n');
      str.append("..subpath_open: "+subpath_open+'\n');
      str.append("..subpath_angle: "+subpath_angle+'\n');
      str.append("..subpath_start: "+subpath_start.x+" "+subpath_start.y+'\n');
      str.append("..subpath_line_length: "+subpath_line_length+'\n');
      str.append("..handle_wide_strokes: "+handle_wide_strokes+'\n');
      str.append("..line_cap: "+line_cap+'\n');
      str.append("..line_join: "+line_join+'\n');
      str.append("..line_join_saved: "+line_join_saved+'\n');
      str.append("..miter_limit: "+miter_limit+'\n');
      str.append("..radius: "+radius+'\n');
      return str.toString();
    }

  /* ==================== getAngle_in ================================== */
  public int getAngle_in() {
    return angle_in;
  }

  /* ==================== setAngle_in ================================== */
  public void setAngle_in(int angle_in) {
    this.angle_in = angle_in;
  }

  /* ==================== getAngle_out ================================== */
  public int getAngle_out() {
    return angle_out;
  }

  /* ==================== setAngle_out ================================== */
  public void setAngle_out(int angle_out) {
    this.angle_out = angle_out;
  }

  /* ==================== getCenter ================================== */
  public FTVectorRec getCenter() {
    return center;
  }

  /* ==================== setCenter ================================== */
  public void setCenter(FTVectorRec center) {
    this.center = center;
  }

  /* ==================== getLine_length ================================== */
  public int getLine_length() {
    return line_length;
  }

  /* ==================== setLine_length ================================== */
  public void setLine_length(int line_length) {
    this.line_length = line_length;
  }

  /* ==================== isFirst_point ================================== */
  public boolean isFirst_point() {
    return first_point;
  }

  /* ==================== setFirst_point ================================== */
  public void setFirst_point(boolean first_point) {
    this.first_point = first_point;
  }

  /* ==================== isSubpath_open ================================== */
  public boolean isSubpath_open() {
    return subpath_open;
  }

  /* ==================== setSubpath_open ================================== */
  public void setSubpath_open(boolean subpath_open) {
    this.subpath_open = subpath_open;
  }

  /* ==================== getSubpath_angle ================================== */
  public int getSubpath_angle() {
    return subpath_angle;
  }

  /* ==================== setSubpath_angle ================================== */
  public void setSubpath_angle(int subpath_angle) {
    this.subpath_angle = subpath_angle;
  }

  /* ==================== getSubpath_start ================================== */
  public FTVectorRec getSubpath_start() {
    return subpath_start;
  }

  /* ==================== setSubpath_start ================================== */
  public void setSubpath_start(FTVectorRec subpath_start) {
    this.subpath_start = subpath_start;
  }

  /* ==================== getSubpath_line_length ================================== */
  public int getSubpath_line_length() {
    return subpath_line_length;
  }

  /* ==================== setSubpath_line_length ================================== */
  public void setSubpath_line_length(int subpath_line_length) {
    this.subpath_line_length = subpath_line_length;
  }

  /* ==================== isHandle_wide_strokes ================================== */
  public boolean isHandle_wide_strokes() {
    return handle_wide_strokes;
  }

  /* ==================== setHandle_wide_strokes ================================== */
  public void setHandle_wide_strokes(boolean handle_wide_strokes) {
    this.handle_wide_strokes = handle_wide_strokes;
  }

  /* ==================== getLine_cap ================================== */
  public int getLine_cap() {
    return line_cap;
  }

  /* ==================== setLine_cap ================================== */
  public void setLine_cap(int line_cap) {
    this.line_cap = line_cap;
  }

  /* ==================== getLine_join ================================== */
  public int getLine_join() {
    return line_join;
  }

  /* ==================== setLine_join ================================== */
  public void setLine_join(int line_join) {
    this.line_join = line_join;
  }

  /* ==================== getLine_join_saved ================================== */
  public int getLine_join_saved() {
    return line_join_saved;
  }

  /* ==================== getLine_join_saved ================================== */
  public void setLine_join_saved(int line_join_saved) {
    this.line_join_saved = line_join_saved;
  }

  /* ==================== getMiter_limit ================================== */
  public int getMiter_limit() {
    return miter_limit;
  }

  /* ==================== setMiter_limit ================================== */
  public void setMiter_limit(int miter_limit) {
    this.miter_limit = miter_limit;
  }

  /* ==================== getRadius ================================== */
  public int getRadius() {
    return radius;
  }

  /* ==================== setRadius ================================== */
  public void setRadius(int radius) {
    this.radius = radius;
  }

  /* ==================== getBorders ================================== */
  public FTStrokeBorderRec[] getBorders() {
    return borders;
  }

  /* ==================== setBorders ================================== */
  public void setBorders(FTStrokeBorderRec[] borders) {
    this.borders = borders;
  }

  /* ==================== getLibrary ================================== */
  public FTLibraryRec getLibrary() {
    return library;
  }

  /* ==================== setLibrary ================================== */
  public void setLibrary(FTLibraryRec library) {
    this.library = library;
  }

}