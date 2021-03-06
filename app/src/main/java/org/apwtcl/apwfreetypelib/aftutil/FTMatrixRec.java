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

  /*    FTMatrixRec                                                           */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A simple structure used to store a 2x2 matrix.  Coefficients are   */
  /*    in 16.16 fixed-point format.  The computation performed is:        */
  /*                                                                       */
  /*       {                                                               */
  /*          x' = x*xx + y*xy                                             */
  /*          y' = x*yx + y*yy                                             */
  /*       }                                                               */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    xx :: Matrix coefficient.                                          */
  /*                                                                       */
  /*    xy :: Matrix coefficient.                                          */
  /*                                                                       */
  /*    yx :: Matrix coefficient.                                          */
  /*                                                                       */
  /*    yy :: Matrix coefficient.                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTMatrixRec extends Object {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTMatrixRec";

  private int xx = 0;
  private int xy = 0;
  private int yx = 0;
  private int yy = 0;

  /* ==================== FTMatricRec ================================== */
  public FTMatrixRec() {
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
    str.append("...xx: "+xx+'\n');
    str.append("...xy: "+xy+'\n');
    str.append("...yx: "+yx+'\n');
    str.append("...yy: "+yy+'\n');
    return str.toString();
  }

  /* ==================== getXx ================================== */
  public int getXx() {
    return xx;
  }

  /* ==================== setXx ================================== */
  public void setXx(int xx) {
    this.xx = xx;
  }

  /* ==================== getXy ================================== */
  public int getXy() {
    return xy;
  }

  /* ==================== setXy ================================== */
  public void setXy(int xy) {
    this.xy = xy;
  }

  /* ==================== getYx ================================== */
  public int getYx() {
    return yx;
  }

  /* ==================== setYx ================================== */
  public void setYx(int yx) {
    this.yx = yx;
  }

  /* ==================== getYy ================================== */
  public int getYy() {
    return yy;
  }

  /* ==================== setYy ================================== */
  public void setYy(int yy) {
    this.yy = yy;
  }

}