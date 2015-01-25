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
  /*    FTRaster1RendererClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.*;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class FTRaster1RendererClass extends FTRendererClassRec {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTRaster1RendererClass";

    /* ==================== FTRaster1RendererClass ================================== */
    public FTRaster1RendererClass() {
      super();
      oid++;
      id = oid;

      module_flags = Flags.Module.RENDERER.getVal();  /* a renderer */
      module_type = FTTags.ModuleType.FT_RENDERER;
      module_name = "raster1";     /* driver name */
      module_version = 0x10000;   /* driver version 1.0 */
      module_requires = 0x20000;  /* driver requires FreeType 2.0 or higher */
      module_interface = null;     /* module specific interface */
      glyph_format = FTTags.GlyphFormat.OUTLINE;
      raster_type = FTTags.RasterType.Black;

//      raster_class = new FTStandardRasterClass();
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTRaster1RendererClass constructor called!!");
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

  /* =====================================================================
   *    ft_black_init
   *
   * =====================================================================
   */
  private FTError.ErrorTag ft_black_init(blackTRasterRec raster) {
Debug(0, DebugTag.DBG_INIT, TAG, "ft_black_init");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    // nothing to do yet
    return error;
  }

  /* =====================================================================
   *    ft_black_new
   *
   * =====================================================================
   */
  public FTError.ErrorTag ft_black_new(FTReference<FTRasterRec> raster_ref) {
Debug(0, DebugTag.DBG_INIT, TAG, "ft_black_new");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    blackTRasterRec raster = null;

    raster_ref.Set(null);
    raster = new blackTRasterRec();
    if (raster != null) {
      ft_black_init(raster);
      raster_ref.Set(raster);
    }
    return error;
  }

  /* =====================================================================
   *    ft_black_reset
   *
   * =====================================================================
   */
  public FTError.ErrorTag ft_black_reset(FTRasterRec raster_param, byte[] pool_base, int pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    blackTRasterRec raster = (blackTRasterRec)raster_param;

    if (raster != null) {
      if (pool_base != null) {
        blackTWorkerRec worker =  new blackTWorkerRec();
        raster.buffer = pool_base;
        raster.buffer_size = pool_size;
        raster.setWorker(worker);
      } else {
        raster.buffer = null;
        raster.buffer_size = 0;
        raster.setWorker(null);
      }
    }
    return error;
  }

  /* =====================================================================
   *    ft_black_set_mode
   *
   * =====================================================================
   */
  public FTError.ErrorTag ft_black_set_mode() {
    Log.e(TAG, "ft_black_set_mode not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_black_render
   *
   * =====================================================================
   */
  public FTError.ErrorTag ft_black_render(FTRasterRec raster, FTRasterParamsRec params) {
    Log.e(TAG, "ft_black_render not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_black_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag ft_black_done() {
    Log.e(TAG, "ft_black_done not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   * ft_raster1_init
   * =====================================================================
   */
  private FTError.ErrorTag ft_raster1_init(FTModuleRec module) {
    FTLibraryRec library = module.library;
    FTRendererRec render = (FTRendererRec)module;
Debug(0, DebugTag.DBG_INIT, TAG, "ft_raster1_init");
    render.getClazz().rasterReset(render.getRaster(), library.getRaster_pool(), library.getRaster_pool_size());
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * ft_raster1_render
   * =====================================================================
   */
  private FTError.ErrorTag ft_raster1_render(FTRendererRec render, FTGlyphSlotRec slot, int mode, FTVectorRec origin) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTOutlineRec outline;
    FTBBoxRec cbox = new FTBBoxRec();
    Integer width;
    Integer height;
    Integer pitch;
    FTBitmapRec bitmap;
    FTRasterParamsRec params = new FTRasterParamsRec();
    FTReference<FTBBoxRec> cbox_ref = new FTReference<FTBBoxRec>();

      /* check glyph image format */
    if (slot.getFormat().getVal() != render.getGlyph_format().getVal()) {
      error = FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
      return error;
    }
      /* check rendering mode */
    if (mode != FTTags.RenderMode.MONO.getVal()) {
        /* raster1 is only capable of producing monochrome bitmaps */
      if (render.module_clazz.getModule_name() == "raster1") {
        return FTError.ErrorTag.GLYPH_CANNOT_RENDER_GLYPH;
      }
    } else {
        /* raster5 is only capable of producing 5-gray-levels bitmaps */
      if (render.module_clazz.getModule_name() == "raster5")
        return FTError.ErrorTag.GLYPH_CANNOT_RENDER_GLYPH;
    }
    outline = slot.getOutline();
      /* translate the outline to the new origin if needed */
    if (origin != null) {
      outline.OutlineTranslate(origin.x, origin.y);
    }
      /* compute the control box, and grid fit it */
    cbox_ref.Set(cbox);
    outline.FTOutlineGetCBox(cbox_ref);
    cbox = cbox_ref.Get();
      /* undocumented but confirmed: bbox values get rounded */
    cbox.setxMin(FTCalc.FT_PIX_ROUND(cbox.getxMin()));
    cbox.setyMin(FTCalc.FT_PIX_ROUND(cbox.getyMin()));
    cbox.setxMax(FTCalc.FT_PIX_ROUND(cbox.getxMax()));
    cbox.setyMax(FTCalc.FT_PIX_ROUND(cbox.getyMax()));
    width  = (int)((cbox.getxMax() - cbox.getxMin()) >> 6);
    height = (int)((cbox.getyMax() - cbox.getyMin()) >> 6);
    if (width > 0xFFFF || height > 0xFFFF) {
      error = FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
      return error;
    }
    bitmap = slot.getBitmap();
      /* release old bitmap buffer */
    if ((slot.getInternal().getFlags().getVal() & FTTags.GlyphFormat.OWN_BITMAP.getVal()) != 0) {
//        FT_FREE(bitmap.buffer);
      slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() & ~FTTags.GlyphFormat.OWN_BITMAP.getVal()));
    }
      /* allocate new one, depends on pixel format */
    if ((mode & FTTags.RenderMode.MONO.getVal()) == 0) {
        /* we pad to 32 bits, only for backwards compatibility with FT 1.x */
      pitch = FTCalc.FT_PAD_CEIL(width, 4);
      bitmap.setPixel_mode(FTTags.PixelMode.GRAY);
      bitmap.setNum_grays(256);
    } else {
      pitch = ((width + 15) >> 4) << 1;
      bitmap.setPixel_mode(FTTags.PixelMode.MONO);
    }
    bitmap.setWidth(width);
    bitmap.setRows(height);
    bitmap.setPitch(pitch);
    // realloc cout = pitch, item_size ) height
    bitmap.setBuffer(new byte[pitch * height]);
    slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() | FTTags.GlyphFormat.OWN_BITMAP.getVal()));
    Debug(0, DebugTag.DBG_INIT, TAG, String.format("cbox_xMin: %d %dn", -cbox.getxMin(), -cbox.getyMin()));
      /* translate outline to render it into the bitmap */
    outline.OutlineTranslate(-cbox.getxMin(), -cbox.getyMin());
      /* set up parameters */
    params.setTarget(bitmap);
    params.setSource(outline);
    params.setFlags(0);
    if (bitmap.getPixel_mode() == FTTags.PixelMode.GRAY) {
      params.setFlags(params.getFlags() | FTRasterParamsRec.FT_RASTER_FLAG_AA);
    }
      /* render outline into the bitmap */
    error = render.rasterRender(render.getRaster(), params);
    outline.OutlineTranslate(cbox.getxMin(), cbox.getyMin());
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    slot.setFormat(FTTags.GlyphFormat.BITMAP);
    slot.setBitmap_left(cbox.getxMin() >> 6);
    slot.setBitmap_top(cbox.getyMax() >> 6);
    return error;
  }

  /* =====================================================================
   * ft_raster1_transform
   * =====================================================================
   */
  private FTError.ErrorTag ft_raster1_transform(FTRendererRec renderer, FTGlyphSlotRec slot, FTReference<FTMatrixRec> matrix_ref, FTReference<FTVectorRec> vec_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "WARNING: ft_raster1_transform not yet implemented");
    return error;
  }

  /* =====================================================================
   * ft_raster1_get_cbox
   * =====================================================================
   */
  private FTError.ErrorTag ft_raster1_get_cbox() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "WARNING: ft_raster1_get_cbox not yet implemented");
    return error;
  }

  /* =====================================================================
   * ft_raster1_set_mode
   * =====================================================================
   */
  private FTError.ErrorTag ft_raster1_set_mode(Object ... args) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "WARNING: ft_raster1_set_mode not yet implemented");
    return error;
  }

  /* ==================== moduleInit ===================================== */
  @Override
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.i(TAG, "moduleInit");
    return ft_raster1_init(module);
  }

  /* ==================== moduleDone ===================================== */
  @Override
  public void moduleDone() {
    Log.i(TAG, "moduleDone");
  }

  /* ==================== getInterface ===================================== */
//  @Override
  public FTError.ErrorTag getInterface() {
    Log.i(TAG, "getInterface");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== renderGlyph ===================================== */
  @Override
  public FTError.ErrorTag renderGlyph(FTRendererRec render, FTGlyphSlotRec slot, int mode, FTVectorRec origin) {
    return ft_raster1_render(render, slot, mode, origin);
  }

  /* ==================== transformGlyph ===================================== */
  @Override
  public FTError.ErrorTag transformGlyph(FTRendererRec renderer, FTGlyphSlotRec slot, FTReference<FTMatrixRec> matrix_ref, FTReference<FTVectorRec> vec_ref) {
    return ft_raster1_transform(renderer, slot, matrix_ref, vec_ref);
  }

  /* ==================== getGlyphCBox ===================================== */
  @Override
  public FTError.ErrorTag getGlyphCBox() {
    return ft_raster1_get_cbox();
  }

  /* ==================== renderGlyph ===================================== */
  public FTError.ErrorTag setMode(FTRendererRec renderer, int tag, Object data) {
    return ft_raster1_set_mode();
  }

  /* ==================== rasterNew ===================================== */
  @Override
  public FTError.ErrorTag rasterNew(FTReference<FTRasterRec> raster_ref) {
    return ft_black_new(raster_ref);
  }

  /* ==================== rasterReset ===================================== */
  @Override
  public FTError.ErrorTag rasterReset(FTRasterRec raster, byte[] raster_pool, int raster_pool_size) {
    return ft_black_reset(raster, raster_pool, raster_pool_size);
  }

  /* ==================== rasterSetMode ===================================== */
  @Override
  public FTError.ErrorTag rasterSetMode() {
    return ft_black_set_mode();
  }

  /* ==================== rasterRender ===================================== */
  @Override
  public FTError.ErrorTag rasterRender(FTRasterRec raster, FTRasterParamsRec params) {
    return ft_black_render(raster, params);
  }

  /* ==================== rasterDone ===================================== */
  @Override
  public FTError.ErrorTag rasterDone(FTRasterRec raster) {
    return ft_black_done();
  }

}