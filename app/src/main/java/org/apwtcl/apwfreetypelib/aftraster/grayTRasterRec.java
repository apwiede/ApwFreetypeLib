/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann (dot) pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftraster;

  /* ===================================================================== */
  /*    grayTRasterRec                                                     */
  /*                                                                       */
  /* ===================================================================== */

public class grayTRasterRec extends FTRasterRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTRasterRec";

  private int band_size = 0;
  private grayTWorkerRec worker = null;

  /* ==================== grayTRasterRec ================================== */
  public grayTRasterRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== grayTRaster ================================== */
  public grayTRasterRec(FTRasterRec raster_rec) {
    oid++;
    id = oid;
    this.buffer = raster_rec.buffer;
    this.buffer_size = raster_rec.buffer_size;
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
    str.append("...band_size: "+band_size+'\n');
    return str.toString();
  }

  /* ==================== getBand_size ================================== */
  public int getBand_size() {
    return band_size;
  }

  /* ==================== setBand_size ================================== */
  public void setBand_size(int band_size) {
    this.band_size = band_size;
  }

  /* ==================== getWorker ================================== */
  public grayTWorkerRec getWorker() {
    return worker;
  }

  /* ==================== setWorker ================================== */
  public void setWorker(grayTWorkerRec worker) {
    this.worker = worker;
  }

}