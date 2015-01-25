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
  /*    IUPWorkerRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTIUPWorkerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "IUPWorkerRec";

  private FTVectorRec[] org = null; /* original and current coordinate arrays */
  private int org_idx= 0;
  private FTVectorRec[] cur = null;
  private int cur_idx = 0;
  private FTVectorRec[] orus = null;
  private int orus_idx = 0;
  private int max_points;

  /* ==================== IUPWorkerRec ================================== */
  public TTIUPWorkerRec() {
    oid++;
    id = oid;

    max_points = 0;
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
    str.append("...org_idx: "+org_idx+'\n');
    str.append("...cur_idx: "+cur_idx+'\n');
    str.append("...orus_idx: "+orus_idx+'\n');
    str.append("...max_points: "+max_points+'\n');
    return str.toString();
  }

  /* ==================== getOrg ================================== */
  public FTVectorRec[] getOrg() {
    return org;
  }

  /* ==================== getOrgPoint ================================== */
  public FTVectorRec getOrgPoint(int point_idx) {
    return org[org_idx + point_idx];
  }

  /* ==================== getOrgPoint_x ================================== */
  public int getOrgPoint_x(int point_idx) {
    return org[org_idx + point_idx].getX();
  }

  /* ==================== getOrgPoint_y ================================== */
  public int getOrgPoint_y(int point_idx) {
    return org[org_idx + point_idx].getY();
  }

  /* ==================== setOrg ================================== */
  public void setOrg(FTVectorRec[] org) {
    this.org = org;
  }

  /* ==================== setOrgPoint ================================== */
  public void setOrgPoint(int point_idx, FTVectorRec vec) {
    org[org_idx + point_idx].setX(vec.getX());
    org[org_idx + point_idx].setY(vec.getY());
  }

  /* ==================== setOrgPoint_x ================================== */
  public void setOrgPoint_x(int point_idx, int value) {
    org[org_idx + point_idx].setX(value);
  }

  /* ==================== setOrgPoint_y ================================== */
  public void setOrgPoint_y(int point_idx, int value) {
    org[org_idx + point_idx].setY(value);
  }

  /* ==================== resetOrgPoint ================================== */
  public void resetOrgPoint(int point_idx) {
    org[org_idx + point_idx].setX(0);
    org[org_idx + point_idx].setY(0);
  }

  /* ==================== getOrg_idx ================================== */
  public int getOrg_idx() {
    return org_idx;
  }

  /* ==================== setOrg_idx ================================== */
  public void setOrg_idx(int org_idx) {
    this.org_idx = org_idx;
  }

  /* ==================== getCur ================================== */
  public FTVectorRec[] getCur() {
    return cur;
  }

  /* ==================== getCurPoint ================================== */
  public FTVectorRec getCurPoint(int point_idx) {
    return cur[cur_idx + point_idx];
  }

  /* ==================== getCurPoint_x ================================== */
  public int getCurPoint_x(int point_idx) {
    return cur[cur_idx + point_idx].getX();
  }

  /* ==================== getCurPoint_y ================================== */
  public int getCurPoint_y(int point_idx) {
    return cur[cur_idx + point_idx].getY();
  }

  /* ==================== setCur ================================== */
  public void setCur(FTVectorRec[] cur) {
    this.cur = cur;
  }

  /* ==================== setCurPoint ================================== */
  public void setCurPoint(int point_idx, FTVectorRec vec) {
    cur[cur_idx + point_idx].setX(vec.getX());
    cur[cur_idx + point_idx].setY(vec.getY());
  }

  /* ==================== setCurPoint_x ================================== */
  public void setCurPoint_x(int point_idx, int value) {
    cur[cur_idx + point_idx].setX(value);
  }

  /* ==================== setCurPoint_y ================================== */
  public void setCurPoint_y(int point_idx, int value) {
    cur[cur_idx + point_idx].setY(value);
  }

  /* ==================== resetCurPoint ================================== */
  public void resetCurPoint(int point_idx) {
    cur[cur_idx + point_idx].setX(0);
    cur[cur_idx + point_idx].setY(0);
  }

  /* ==================== getCur_idx ================================== */
  public int getCur_idx() {
    return cur_idx;
  }

  /* ==================== setCur_idx ================================== */
  public void setCur_idx(int cur_idx) {
    this.cur_idx = cur_idx;
  }

  /* ==================== getOrus ================================== */
  public FTVectorRec[] getOrus() {
    return orus;
  }

  /* ==================== getOrusPoint ================================== */
  public FTVectorRec getOrusPoint(int point_idx) {
    return orus[orus_idx + point_idx];
  }

  /* ==================== getOrusPoint_x ================================== */
  public int getOrusPoint_x(int point_idx) {
    return orus[orus_idx + point_idx].getX();
  }

  /* ==================== getOrusPoint_y ================================== */
  public int getOrusPoint_y(int point_idx) {
    return orus[orus_idx + point_idx].getY();
  }

  /* ==================== setOrus ================================== */
  public void setOrus(FTVectorRec[] orus) {
    this.orus = orus;
  }

  /* ==================== setOrusPoint ================================== */
  public void setOrusPoint(int point_idx, FTVectorRec vec) {
    orus[orus_idx + point_idx].setX(vec.getX());
    orus[orus_idx + point_idx].setY(vec.getY());
  }

  /* ==================== setOrusPoint_x ================================== */
  public void setOrusPoint_x(int point_idx, int value) {
    orus[orus_idx + point_idx].setX(value);
  }

  /* ==================== setOrusPoint_y ================================== */
  public void setOrusPoint_y(int point_idx, int value) {
    orus[orus_idx + point_idx].setY(value);
  }

  /* ==================== resetOrusPoint ================================== */
  public void resetOrusPoint(int point_idx) {
    orus[orus_idx + point_idx].setX(0);
    orus[orus_idx + point_idx].setY(0);
  }

  /* ==================== getOrus_idx ================================== */
  public int getOrus_idx() {
    return orus_idx;
  }

  /* ==================== setOrus_idx ================================== */
  public void setOrus_idx(int orus_idx) {
    this.orus_idx = orus_idx;
  }

  /* ==================== getMax_points ================================== */
  public int getMax_points() {
    return max_points;
  }

  /* ==================== getMax_points ================================== */
  public void setMax_points(int max_points) {
    this.max_points = max_points;
  }

}