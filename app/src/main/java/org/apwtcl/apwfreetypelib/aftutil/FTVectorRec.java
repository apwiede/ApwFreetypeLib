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

package org.apwtcl.apwfreetypelib.aftutil;

  /* ===================================================================== */
  /*    FTVectorRec                                                           */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A simple structure used to store a 2D vector; coordinates are of   */
  /*    the FTPos type.                                                   */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    x :: The horizontal coordinate.                                    */
  /*    y :: The vertical coordinate.                                      */
  /* ===================================================================== */

public class FTVectorRec extends Object {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTVectorRec";

  private int x = 0;
  private int y = 0;

  /* ==================== FTVectorRec ================================== */
  public FTVectorRec() {
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

  /* ==================== setY ================================== */
  public void setY(int y) {
    this.y = y;
  }

}