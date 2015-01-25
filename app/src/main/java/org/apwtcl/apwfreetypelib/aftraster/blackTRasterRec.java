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
  /*    blackTRasterRec                                                    */
  /*                                                                       */
  /* ===================================================================== */

public class blackTRasterRec extends FTRasterRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "blackTRasterRec";

  private blackTWorkerRec worker = null;
  private byte[] grays = null;
  private int gray_width = 0;

  /* ==================== blackTRasterRec ================================== */
  public blackTRasterRec() {
    oid++;
    id = oid;

    grays = new byte[5];
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
    str.append("...gray_width: "+gray_width+'\n');
    return str.toString();
  }

  /* ==================== getWorker ================================== */
  public blackTWorkerRec getWorker() {
    return worker;
  }

  /* ==================== setWorker ================================== */
  public void setWorker(blackTWorkerRec worker) {
    this.worker = worker;
  }

  /* ==================== getGrays ================================== */
  public byte[] getGrays() {
    return grays;
  }

  /* ==================== setGrays ================================== */
  public void setGrays(byte[] grays) {
    this.grays = grays;
  }

  /* ==================== getGray_width ================================== */
  public int getGray_width() {
    return gray_width;
  }

  /* ==================== setGray_width ================================== */
  public void setGray_width(int gray_width) {
    this.gray_width = gray_width;
  }

}