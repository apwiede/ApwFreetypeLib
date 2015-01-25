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

package org.apwtcl.apwfreetypelib.aftbase;


  /* ===================================================================== */
  /*    FTRendererClassRec                                                 */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The renderer module class descriptor.                              */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    root            :: The root @FTModuleClass fields.                 */
  /*                                                                       */
  /*    glyph_format    :: The glyph image format this renderer handles.   */
  /*                                                                       */
  /*    renderGlyph     :: A method used to render the image that is in a  */
  /*                       given glyph slot into a bitmap.                 */
  /*                                                                       */
  /*    transformGlyph  :: A method used to transform the image that is in */
  /*                       a given glyph slot.                             */
  /*                                                                       */
  /*    getGlyphCBox    :: A method used to access the glyph's cbox.       */
  /*                                                                       */
  /*    setMode         :: A method used to pass additional parameters.    */
  /*                                                                       */
  /*    raster_class    :: For @FT_GLYPH_FORMAT_OUTLINE renderers only.    */
  /*                       This is a pointer to its raster's class.        */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftraster.FTRasterParamsRec;
import org.apwtcl.apwfreetypelib.aftraster.FTRasterRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTParameter;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTRendererClassRec extends FTModuleClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTRendererClassRec";

  private static int FT_RENDER_POOL_SIZE = 16384;

  protected FTTags.GlyphFormat glyph_format;
  protected FTTags.RasterType raster_type;

    /* ==================== FTRendererClassRec ================================== */
    public FTRendererClassRec() {
      super();
      oid++;
      id = oid;
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTRendererClassRec constructor called!!");
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

  /* ==================== renderGlyph ===================================== */
  public FTError.ErrorTag renderGlyph(FTRendererRec render, FTGlyphSlotRec slot, int mode, FTVectorRec origin) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "renderGlyph not yet implemented");
    return error;
  }

  /* ==================== transformGlyph ===================================== */
  public FTError.ErrorTag transformGlyph(FTRendererRec renderer, FTGlyphSlotRec slot, FTReference<FTMatrixRec> matirx_ref, FTReference<FTVectorRec> vec_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "transformGlyph not yet implemented");
    return error;
  }

  /* ==================== getGlyphCBox ===================================== */
  public FTError.ErrorTag getGlyphCBox() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "getGlyphCBox not yet implemented");
    return error;
  }

  /* ==================== setMode ===================================== */
  public FTError.ErrorTag setMode(FTRendererRec renderer, FTParameter.ParamTag tag, Object data) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "SetMode not yet implemented");
    return error;
  }

  /* ==================== getGlyphFormat ===================================== */
  public FTTags.GlyphFormat getGlyphFormat() {
    return glyph_format;
  }

  /* ==================== getRenderPoolSize ===================================== */
  public static int getRenderPoolSize() {
    return FT_RENDER_POOL_SIZE;
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
  public FTError.ErrorTag rasterRender(FTRasterRec raster, FTRasterParamsRec params) {
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