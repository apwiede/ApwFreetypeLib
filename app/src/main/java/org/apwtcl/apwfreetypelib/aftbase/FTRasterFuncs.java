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
  /*    FTRasterFuncs                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftraster.FTRasterRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTRasterFuncs extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTRasterFuncs";

    public FTTags.GlyphFormat glyph_format = FTTags.GlyphFormat.NONE;

    /* ==================== FTRasterFuncs ================================== */
    public FTRasterFuncs() {
      oid++;
      id = oid;
      
    }
    
    /* ==================== mySelf ================================== */
    public String mySelf() {
      String str = TAG+"!"+id+"!";
      return str;
    } 
        
    /* ==================== toString ===================================== */
    public String toString() {
      StringBuffer str = new StringBuffer(mySelf()+"!");
      return str.toString();
    }

    /* ==================== toDebugString ===================================== */
    public String toDebugString() {
      StringBuffer str = new StringBuffer(mySelf()+"\n");
      return str.toString();
    }

  /* ==================== rasterNew ===================================== */
  public FTError.ErrorTag rasterNew(FTReference<FTRasterRec> raster_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterNew not yet implemented");
    return error;
  }

  /* ==================== rasterReset ===================================== */
  public FTError.ErrorTag rasterReset(FTRasterRec raster, byte[] raster_pool, int raster_pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterRest not yet implemented");
    return error;
  }

  /* ==================== rasterSetMode ===================================== */
  public FTError.ErrorTag rasterSetMode() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterSetMode not yet implemented");
    return error;
  }

  /* ==================== rasterRender ===================================== */
  public FTError.ErrorTag rasterRender() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterRender not yet implemented");
    return error;
  }

  /* ==================== rasterDone ===================================== */
  public FTError.ErrorTag rasterDone(FTRasterRec raster) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterDone not yet implemented");
    return error;
  }

}
