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
  /*    grayTBandRec                                                       */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class grayTBandRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTBandRec";

  private int min = 0;   /* band's minimum */
  private int max = 0;   /* band's maximum */

  /* ==================== grayTBand ================================== */
  public grayTBandRec() {
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
    str.append("...min: "+min+'\n');
    str.append("...max: "+max+'\n');
    return str.toString();
  }

  /* ==================== getMin ================================== */
  public int getMin() {
    return min;
  }

  /* ==================== setMin ================================== */
  public void setMin(int min) {
    this.min = min;
  }

  /* ==================== getMax ================================== */
  public int getMax() {
    return max;
  }

  /* ==================== setMax ================================== */
  public void setMax(int max) {
    this.max = max;
  }

}