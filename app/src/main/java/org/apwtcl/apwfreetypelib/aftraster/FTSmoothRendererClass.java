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
  /*    FTSmoothRendererClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.*;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class FTSmoothRendererClass extends FTRendererClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSmoothRendererClass";

  /* ==================== FTSmoothRendererClass ================================== */
  public FTSmoothRendererClass() {
    super();
    oid++;
    id = oid;

    module_flags = Flags.Module.RENDERER.getVal();  /* a renderer */
    module_type = FTTags.ModuleType.FT_RENDERER;
    module_name = "smooth";     /* driver name */
    module_version = 0x10000;   /* driver version 1.0 */
    module_requires = 0x20000;  /* driver requires FreeType 2.0 or higher */
    module_interface = null;    /* module specific interface */
    glyph_format = FTTags.GlyphFormat.OUTLINE;
    raster_type = FTTags.RasterType.GRAY;
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTMSmoothRendererClass constructor called!!");
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
   * ft_smooth_init
   * =====================================================================
   */
  private FTError.ErrorTag ft_smooth_init(FTModuleRec module) {
    FTLibraryRec library = module.library;
    FTRendererRec render = (FTRendererRec)module;
Debug(0, DebugTag.DBG_INIT, TAG, "ft_smooth_init");
    render.getClazz().rasterReset(render.getRaster(), library.getRaster_pool(), library.getRaster_pool_size());
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * ft_smooth_render_generic
   * =====================================================================
   */
  private FTError.ErrorTag ft_smooth_render_generic(FTRendererRec render, FTGlyphSlotRec slot, int  mode, FTVectorRec origin, int required_mode) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTBBoxRec cbox = new FTBBoxRec();
    int width;
    int height;
    int pitch;
    FTBitmapRec bitmap = slot.getBitmap();  // FIXME set slot after modifying bitmap!!
    boolean hmul = mode == FTTags.RenderMode.LCD.getVal();
    boolean vmul = mode == FTTags.RenderMode.LCD_V.getVal();
    int x_shift = 0;
    int y_shift = 0;
    int x_left = 0;
    int y_top = 0;
    int height_org;
    int width_org;
    FTRasterParamsRec params = new FTRasterParamsRec();
    boolean have_translated_origin = false;
    boolean have_outline_shifted = false;
    boolean have_buffer = false;
    FTReference<FTBBoxRec> cbox_ref = new FTReference<FTBBoxRec>();

      /* check glyph image format */
    if (slot.getFormat() != render.getGlyph_format()) {
      error = FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
      return error;
    }
      /* check rendering mode */
    if (mode != required_mode) {
      return FTError.ErrorTag.GLYPH_CANNOT_RENDER_GLYPH;
    }
      /* translate the outline to the new origin if needed */
    if (origin != null) {
      slot.getOutline().OutlineTranslate(origin.getX(), origin.getY());
      have_translated_origin = true;
    }
      /* compute the control box, and grid fit it */
    cbox_ref.Set(cbox);
    slot.getOutline().FTOutlineGetCBox(cbox_ref);
    cbox = cbox_ref.Get();
      /* undocumented but confirmed: bbox values get rounded */
    cbox.setxMin(FTCalc.FT_PIX_ROUND(cbox.getxMin()));
    cbox.setyMin(FTCalc.FT_PIX_ROUND(cbox.getyMin()));
    cbox.setxMax(FTCalc.FT_PIX_ROUND(cbox.getxMax()));
    cbox.setyMax(FTCalc.FT_PIX_ROUND(cbox.getyMax()));
    width  = (int)((cbox.getxMax() - cbox.getxMin()) >> 6);
    height = (int)((cbox.getyMax() - cbox.getyMin()) >> 6);
    if (width > 0xFFFF || height > 0xFFFF) {
      error = FTError.ErrorTag.RENDER_RASTER_OVERFLOW;
      return error;
    }
    bitmap = slot.getBitmap();
    width_org = width;
    height_org = height;
      /* release old bitmap buffer */
    if ((slot.getInternal().getFlags().getVal() & FTTags.GlyphFormat.OWN_BITMAP.getVal()) != 0) {
//        FT_FREE(bitmap.buffer);
      slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() & ~FTTags.GlyphFormat.OWN_BITMAP.getVal()));
    }
      /* allocate new one */
    pitch = width;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ft_smooth_render_generic: pitch: %d, width: %d, height: %d", pitch, width, height));
    if (hmul) {
      width = width * 3;
      pitch = FTCalc.FT_PAD_CEIL(width, 4);
    }
    if (vmul) {
      height *= 3;
    }
    x_shift = cbox.getxMin();
    y_shift = cbox.getyMin();
    x_left = (int)(cbox.getxMin() >> 6);
    y_top = (int)(cbox.getyMax() >> 6);
    bitmap.setPixel_mode(FTTags.PixelMode.GRAY);
    bitmap.setNum_grays(256);
    bitmap.setWidth(width);
    bitmap.setRows(height);
    bitmap.setPitch(pitch);
      /* translate outline to render it into the bitmap */
    slot.getOutline().OutlineTranslate(-x_shift, -y_shift);
    have_outline_shifted = true;
    bitmap.setBuffer(new byte[pitch * height]);

    have_buffer = true;
    slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() | FTTags.GlyphFormat.OWN_BITMAP.getVal()));
      /* set up parameters */
    params.setTarget(bitmap);
    params.setSource(slot.getOutline());
    params.setFlags(FTRasterParamsRec.FT_RASTER_FLAG_AA);
      /* render outline into bitmap */
    error = render.rasterRender(render.getRaster(), params);
    if (error != FTError.ErrorTag.ERR_OK) {
      if (have_outline_shifted) {
        slot.getOutline().OutlineTranslate(x_shift, y_shift);
      }
      if (have_translated_origin) {
        slot.getOutline().OutlineTranslate(-origin.getX(), -origin.getY());
      }
      if (have_buffer) {
//          FT_FREE( bitmap.buffer );
        slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() & ~FTTags.GlyphFormat.OWN_BITMAP.getVal()));
      }
      return error;
    }
      /* expand it horizontally */
    if (hmul) {
//        FT_Byte*  line = bitmap.buffer;
      int lineIdx = 0;
      int hh;

      for (hh = height_org; hh > 0; hh--, lineIdx += pitch) {
        int xx;
//          FT_Byte*  end = line + width;
        int endIdx = width;

        if (lineIdx == 0) ; // unused
        for (xx = width_org; xx > 0; xx--) {
          int pixel = bitmap.getBuffer()[xx-1];

          bitmap.getBuffer()[endIdx - 3] = (byte)pixel;
          bitmap.getBuffer()[endIdx - 2] = (byte)pixel;
          bitmap.getBuffer()[endIdx - 1] = (byte)pixel;
          endIdx -= 3;
        }
      }
    }
      /* expand it vertically */
    if (vmul) {
//        FT_Byte*  read  = bitmap.buffer + ( height - height_org ) * pitch;
      int readIdx = (height - height_org) * pitch;
//        FT_Byte*  write = bitmap.buffer;
      int writeIdx = 0;
      int   hh;

      for (hh = height_org; hh > 0; hh--) {
        System.arraycopy(bitmap.getBuffer(), writeIdx, bitmap.getBuffer(), readIdx, pitch);
//          ft_memcpy(write, read, pitch);
        writeIdx += pitch;
        System.arraycopy(bitmap.getBuffer(), writeIdx, bitmap.getBuffer(), readIdx, pitch);
//          ft_memcpy(write, read, pitch);
        writeIdx += pitch;
        System.arraycopy(bitmap.getBuffer(), writeIdx, bitmap.getBuffer(), readIdx, pitch);
//          ft_memcpy( write, read, pitch );
        writeIdx += pitch;
        readIdx += pitch;
      }
    }
      /*
       * XXX: on 16bit system, we return an error for huge bitmap
       * to prevent an overflow.
       */
    if (x_left > 0xFFFF || y_top > 0xFFFF) {
      error = FTError.ErrorTag.RENDER_INVALID_PIXEL_SIZE;
    } else {
      slot.setFormat(FTTags.GlyphFormat.BITMAP);
      slot.setBitmap_left(x_left);
      slot.setBitmap_top(y_top);
      slot.setBitmap(bitmap);
          /* everything is fine; don't deallocate buffer */
      have_buffer = false;
      error = FTError.ErrorTag.ERR_OK;
    }
    if (have_outline_shifted) {
      slot.getOutline().OutlineTranslate(x_shift, y_shift);
    }
    if (have_translated_origin) {
      slot.getOutline().OutlineTranslate(-origin.getX(), -origin.getY());
    }
    if (have_buffer) {
//        FT_FREE( bitmap.buffer );
      slot.getInternal().setFlags(FTTags.GlyphFormat.getTableTag(slot.getInternal().getFlags().getVal() & ~FTTags.GlyphFormat.OWN_BITMAP.getVal()));
    }
    return error;
  }

  /* =====================================================================
   * ft_smooth_render
   * =====================================================================
   */
  private FTError.ErrorTag ft_smooth_render(FTRendererRec render, FTGlyphSlotRec slot, int mode, FTVectorRec origin) {

    return ft_smooth_render_generic(render, slot, mode, origin, FTTags.RenderMode.NORMAL.getVal());
  }

  /* =====================================================================
   * ft_smooth_transform
   * =====================================================================
   */
  private FTError.ErrorTag ft_smooth_transform(FTRendererRec renderer, FTGlyphSlotRec slot, FTReference<FTMatrixRec> matrix_ref, FTReference<FTVectorRec> vec_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.w(TAG, "WARNING: ft_smooth_transform not yet implemented");
    System.out.println("ft_smooth_transform not yet implemented!!");
    return error;
  }

  /* =====================================================================
   * ft_smooth_get_cbox
   * =====================================================================
   */
  public static int ft_smooth_get_cbox(Object ... args) {
    Log.w(TAG, "WARNING: ft_smooth_get_cbox not yet implemented");
    System.out.println("ft_smooth_get_cbox not yet implemented!!");
    return 1;
  }

  /* =====================================================================
   * ft_smooth_set_mode
   * =====================================================================
   */
  public static int ft_smooth_set_mode(Object ... args) {
    Log.w(TAG, "WARNING: ft_smooth_set_mode not yet implemented");
    System.out.println("ft_smooth_set_mode not yet implemented!!");
    return 1;
  }


  /* ==================== moduleInit ===================================== */
  @Override
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.i(TAG, "moduleInit");
    return ft_smooth_init(module);
  }

  /* ==================== moduleDone ===================================== */
  @Override
  public void moduleDone() {
    // nothing to do
    Log.i(TAG, "moduleDone");
  }

  /* ==================== getInterface ===================================== */
//  @Override
  public FTError.ErrorTag getInterface() {
    // nothing to do
    Log.i(TAG, "getInterface");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== renderGlyph ===================================== */
  @Override
  public FTError.ErrorTag renderGlyph(FTRendererRec render, FTGlyphSlotRec slot, int mode, FTVectorRec origin) {
    return ft_smooth_render(render, slot, mode, origin);
  }

  /* ==================== transformGlyph ===================================== */
  @Override
  public FTError.ErrorTag transformGlyph(FTRendererRec renderer, FTGlyphSlotRec slot, FTReference<FTMatrixRec> matrix_ref, FTReference<FTVectorRec> vec_ref) {
    return ft_smooth_transform(renderer, slot, matrix_ref, vec_ref);
  }

  /* ==================== getGlyphCBox ===================================== */
  @Override
  public FTError.ErrorTag getGlyphCBox() {
    Log.e(TAG, "gteGlyphCBox not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_smooth_get_cbox();
  }

  /* ==================== setMode ===================================== */
  @Override
  public FTError.ErrorTag setMode(FTRendererRec renderer, FTParameterRec.ParamTag tag, Object data) {
    Log.e(TAG, "setMode not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_smooth_set_mode();
  }

  /* ==================== rasterNew ===================================== */
  @Override
  public FTError.ErrorTag rasterNew(FTReference<FTRasterRec> raster_ref) {
Debug(0, DebugTag.DBG_INIT, TAG, "gray_raster_new");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;;
    grayTRasterRec raster = new grayTRasterRec();
    raster_ref.Set(raster);
    return error;
  }


  /* ==================== rasterReset ===================================== */
  @Override
  public FTError.ErrorTag rasterReset(FTRasterRec raster, byte[] raster_pool, int raster_pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;;
    ((grayTRasterRec)raster).getWorker().gray_raster_reset((grayTRasterRec)raster, raster_pool, raster_pool_size);
    ((grayTRasterRec)raster).setBand_size(((grayTRasterRec)raster).getWorker().getBand_size());
    return error;
  }

  /* ==================== rasterSetMode ===================================== */
  @Override
  public FTError.ErrorTag rasterSetMode() {
    Log.e(TAG, "rasterSetMode");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== rasterRender ===================================== */
  @Override
  public FTError.ErrorTag rasterRender(FTRasterRec raster, FTRasterParamsRec params) {
    return ((grayTRasterRec)raster).getWorker().gray_raster_render(((grayTRasterRec)raster), params);
  }

  /* ==================== rasterDone ===================================== */
  @Override
  public FTError.ErrorTag rasterDone(FTRasterRec raster) {
    return ((grayTRasterRec)raster).getWorker().gray_raster_done();
  }

}
