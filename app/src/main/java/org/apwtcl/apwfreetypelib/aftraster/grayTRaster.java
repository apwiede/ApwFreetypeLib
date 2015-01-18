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
  /*    grayTRaster                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class grayTRaster extends FTRasterRec {
    private static int oid = 0;

    private int id;
    private static String TAG = "grayTRaster";

    public int band_size = 0;
    public grayTWorker worker = null;

    /* ==================== grayTRaster ================================== */
    public grayTRaster() {
      oid++;
      id = oid;
    }
    
    /* ==================== grayTRaster ================================== */
    public grayTRaster(FTRasterRec raster_rec) {
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
      return str.toString();
    }
 
}
