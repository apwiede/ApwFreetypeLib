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
  /*    blackTBandRec                                                      */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class blackTBandRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "blackTBandRec";

  private int y_min = 0;   /* band's minimum */
  private int y_max = 0;   /* band's maximum */

  /* ==================== blackTBandRec ================================== */
  public blackTBandRec() {
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
    str.append("...y_min: "+y_min+'\n');
    str.append("...y_max: "+y_max+'\n');
    return str.toString();
  }

  /* ==================== getY_min ================================== */
  public int getY_min() {
    return y_min;
  }

  /* ==================== setY_min ================================== */
  public void setY_min(int y_min) {
    this.y_min = y_min;
  }

  /* ==================== getY_max ================================== */
  public int getY_max() {
    return y_max;
  }

  /* ==================== setY_max ================================== */
  public void setY_max(int y_max) {
    this.y_max = y_max;
  }

}