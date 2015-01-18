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

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCSNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCSNodeRec extends FTCGNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCSNodeRec";

  public final static int FTC_SBIT_ITEMS_PER_NODE = 16;

  private int count = 0;
  private FTCSBitRec[] sbits = new FTCSBitRec[FTC_SBIT_ITEMS_PER_NODE];

  /* ==================== FTCSNodeRec ================================== */
  public FTCSNodeRec() {
    oid++;
    id = oid;
    for (int i = 0; i < FTC_SBIT_ITEMS_PER_NODE; i++) {
      sbits[i] = new FTCSBitRec();
    }
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
    str.append("..count: "+count+'\n');
    return str.toString();
  }

  /* ==================== getCount ================================== */
  public int getCount() {
    return count;
  }

  /* ==================== setCount ================================== */
  public void setCount(int count) {
    this.count = count;
  }

  /* ==================== getSbits ================================== */
  public FTCSBitRec[] getSbits() {
    return sbits;
  }

  /* ==================== setSbits ================================== */
  public void setSbits(FTCSBitRec[] sbits) {
    this.sbits = sbits;
  }

}