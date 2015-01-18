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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTStrokeBorderRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTStrokeBorderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTStrokeBorderRec";

  private int num_points = 0;
  private int max_points = 0;
  private FTVectorRec[] points = null;
  private byte[] tags = null;
  private boolean movable = false; /* TRUE for ends of lineto borders */
  private int start = -1;          /* index of current sub-path start point */
  private boolean valid = false;

  /* ==================== FTStrokeBorderRec ================================== */
  public FTStrokeBorderRec() {
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
    str.append("..num_points: "+num_points+'\n');
    str.append("..max_points: "+max_points+'\n');
    str.append("..movable: "+movable+'\n');
    str.append("..start: "+start+'\n');
    str.append("..valid: "+valid+'\n');
    return str.toString();
  }

  /* ==================== getNum_points ================================== */
  public int getNum_points() {
    return num_points;
  }

  /* ==================== setNum_points ================================== */
  public void setNum_points(int num_points) {
    this.num_points = num_points;
  }

  /* ==================== getMax_points ================================== */
  public int getMax_points() {
    return max_points;
  }

  /* ==================== setMax_points ================================== */
  public void setMax_points(int max_points) {
    this.max_points = max_points;
  }

  /* ==================== getPoints ================================== */
  public FTVectorRec[] getPoints() {
    return points;
  }

  /* ==================== setPoints ================================== */
  public void setPoints(FTVectorRec[] points) {
    this.points = points;
  }

  /* ==================== getTags ================================== */
  public byte[] getTags() {
    return tags;
  }

  /* ==================== setTags ================================== */
  public void setTags(byte[] tags) {
    this.tags = tags;
  }

  /* ==================== isMovable ================================== */
  public boolean isMovable() {
    return movable;
  }

  /* ==================== setMovable ================================== */
  public void setMovable(boolean movable) {
    this.movable = movable;
  }

  /* ==================== getStart ================================== */
  public int getStart() {
    return start;
  }

  /* ==================== setStart ================================== */
  public void setStart(int start) {
    this.start = start;
  }

  /* ==================== isValid ================================== */
  public boolean isValid() {
    return valid;
  }

  /* ==================== setValid ================================== */
  public void setValid(boolean valid) {
    this.valid = valid;
  }

}