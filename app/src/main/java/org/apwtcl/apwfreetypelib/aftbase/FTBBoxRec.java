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
  /*    FTBBoxRec                                                             */
  /* <Description>                                                         */
  /*    A structure used to hold an outline's bounding box, i.e., the      */
  /*    coordinates of its extrema in the horizontal and vertical          */
  /*    directions.                                                        */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    xMin :: The horizontal minimum (left-most).                        */
  /*                                                                       */
  /*    yMin :: The vertical minimum (bottom-most).                        */
  /*                                                                       */
  /*    xMax :: The horizontal maximum (right-most).                       */
  /*                                                                       */
  /*    yMax :: The vertical maximum (top-most).                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The bounding box is specified with the coordinates of the lower    */
  /*    left and the upper right corner.  In PostScript, those values are  */
  /*    often called (llx,lly) and (urx,ury), respectively.                */
  /*                                                                       */
  /*    If `yMin' is negative, this value gives the glyph's descender.     */
  /*    Otherwise, the glyph doesn't descend below the baseline.           */
  /*    Similarly, if `ymax' is positive, this value gives the glyph's     */
  /*    ascender.                                                          */
  /*                                                                       */
  /*    `xMin' gives the horizontal distance from the glyph's origin to    */
  /*    the left edge of the glyph's bounding box.  If `xMin' is negative, */
  /*    the glyph extends to the left of the origin.                       */
  /*                                                                       */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTBBoxRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTBBoxRec";

  private int xMin = 0;
  private int xMax = 0;
  private int yMin = 0;
  private int yMax = 0;

  /* ==================== FTBBoxRec ================================== */
  public FTBBoxRec() {
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

  /* ==================== getxMin ================================== */
  public int getxMin() {
    return xMin;
  }

  /* ==================== setxMin ================================== */
  public void setxMin(int xMin) {
    this.xMin = xMin;
  }

  /* ==================== getxMax ================================== */
  public int getxMax() {
    return xMax;
  }

  /* ==================== setxMax ================================== */
  public void setxMax(int xMax) {
    this.xMax = xMax;
  }

  /* ==================== getyMin ================================== */
  public int getyMin() {
    return yMin;
  }

  /* ==================== setyMin ================================== */
  public void setyMin(int yMin) {
    this.yMin = yMin;
  }

  /* ==================== getyMax ================================== */
  public int getyMax() {
    return yMax;
  }

  /* ==================== setyMax ================================== */
  public void setyMax(int yMax) {
    this.yMax = yMax;
  }

}