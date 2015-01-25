package org.apwtcl.apwfreetypelib.aftraster;

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

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TPointRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TPointRec";

  private int x = 0;
  private int y = 0;

  /* ==================== TPointRec ================================== */
  public TPointRec() {
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
    str.append("...y: "+y+'\n');
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

  /* ==================== getY ================================== */
  public int getY() {
    return y;
  }

  /* ==================== setYf ================================== */
  public void setY(int y) {
    this.y = y;
  }

}