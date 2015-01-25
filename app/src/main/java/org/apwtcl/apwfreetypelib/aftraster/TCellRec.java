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

package org.apwtcl.apwfreetypelib.aftraster;

  /* ===================================================================== */
  /*    TCell                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TCellRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TCellRec";

  public static TCellRec[] tcells = new TCellRec[2048];
  public static int tcells_idx = 0;

  private int x;     /* same with grayTWorker.ex    */
  private int cover; /* same with grayTWorker.cover */
  private int area;
  private int next_idx;
  private TCellRec next;
  private int self_idx;

  /* ==================== TCellRec ================================== */
  public TCellRec() {
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
    str.append("...x: "+x+'\n');
    str.append("...cover: "+cover+'\n');
    str.append("...area: "+area+'\n');
    str.append("...next_idx: "+next_idx+'\n');
    str.append("...self_idx: "+self_idx+'\n');
    return str.toString();
  }

  /* ==================== getX ================================== */
  public int getX() {
    return x;
  }

  /* ==================== setX ================================== */
  public void setX(int x) {
    this.x = x;
  }

  /* ==================== getCover ================================== */
  public int getCover() {
    return cover;
  }

  /* ==================== setCover ================================== */
  public void setCover(int cover) {
    this.cover = cover;
  }

  /* ==================== getArea ================================== */
  public int getArea() {
    return area;
  }

  /* ==================== setArea ================================== */
  public void setArea(int area) {
    this.area = area;
  }

  /* ==================== getNext_idx ================================== */
  public int getNext_idx() {
    return next_idx;
  }

  /* ==================== setNext_idx ================================== */
  public void setNext_idx(int next_idx) {
    this.next_idx = next_idx;
  }

  /* ==================== getNext ================================== */
  public TCellRec getNext() {
    return next;
  }

  /* ==================== setNext ================================== */
  public void setNext(TCellRec next) {
    this.next = next;
  }

  /* ==================== getSelf_idx ================================== */
  public int getSelf_idx() {
    return self_idx;
  }

  /* ==================== setSelf_idx ================================== */
  public void setSelf_idx(int self_idx) {
    this.self_idx = self_idx;
  }

}