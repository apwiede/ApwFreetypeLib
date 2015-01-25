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

  FTGrayOutlineFuncsClass func_interface = null;

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
      raster_type = FTTags.RasterType.Black;
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
      slot.getOutline().OutlineTranslate(origin.x, origin.y);
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
    Debug(0, DebugTag.DBG_INIT, TAG, String.format("ft_smooth_render_generic: pitch: %d, width: %d, height: %d", pitch, width, height));
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
        slot.getOutline().OutlineTranslate(-origin.x, -origin.y);
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
      slot.getOutline().OutlineTranslate(-origin.x, -origin.y);
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


  /* =====================================================================
   *    gray_raster_new
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_raster_new(FTReference<FTRasterRec> raster_ref) {
Debug(0, DebugTag.DBG_INIT, TAG, "gray_raster_new");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    grayTRasterRec raster = null;
    raster_ref.Set(null);
    raster = new grayTRasterRec();
    if (raster != null) {
      raster_ref.Set(raster);
    }
    return error;
  }

  /* =====================================================================
   *    gray_raster_reset
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_raster_reset(FTRasterRec raster_param, byte[] pool_base, int pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    grayTRasterRec raster = (grayTRasterRec)raster_param;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_reset");
    if (raster != null) {
      if (pool_base != null) {
        grayTWorkerRec worker =  new grayTWorkerRec();
        raster.buffer = pool_base;
        raster.buffer_size = pool_size;
//          raster.band_size = (int)raster.buffer_size / sizeof(TCell) * 8;
        raster.setBand_size(raster.buffer_size / 20);  // FIXME!!!
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
   *    gray_raster_render
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_raster_render(FTRasterRec raster_param, FTRasterParamsRec params) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_render");
    grayTRasterRec raster = (grayTRasterRec)raster_param;
    FTOutlineRec outline = (FTOutlineRec)params.getSource();
    FTBitmapRec target_map = params.getTarget();
    grayTWorkerRec worker;

    if (raster == null || raster.buffer == null || raster.buffer_size == 0) {
      return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
    }
    if (outline == null) {
      return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
    }
      /* return immediately if the outline is empty */
    if (outline.getN_points() == 0 || outline.getN_contours() <= 0) {
      return FTError.ErrorTag.ERR_OK;
    }
    if (outline.getContours() == null || outline.getPoints()== null) {
      return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
    }
    if (outline.getN_points() != outline.getContours()[outline.getN_contours() - 1] + 1) {
      return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
    }
    worker = raster.getWorker();
      /* if direct mode is not set, we must have a target bitmap */
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_DIRECT) == 0) {
      if (target_map == null) {
        return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
      }
        /* nothing to do */
      if (target_map.getWidth() == 0|| target_map.getRows() == 0) {
        return FTError.ErrorTag.ERR_OK;
      }
      if (target_map.getBuffer() == null) {
        return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
      }
    }
      /* this version does not support monochrome rendering */
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_AA) == 0) {
      return FTError.ErrorTag.RENDER_INVALID_MODE;
    }
      /* compute clipping box */
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_DIRECT) == 0) {
        /* compute clip box from target pixmap */
      worker.getClip_box().setxMin(0);
      worker.getClip_box().setyMin(0);
      worker.getClip_box().setxMax(target_map.getWidth());
      worker.getClip_box().setyMax(target_map.getRows());
    } else {
      if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_CLIP) != 0) {
        worker.setClip_box(params.getClip_box());
      } else {
        worker.getClip_box().setxMin(-32768);
        worker.getClip_box().setyMin(-32768);
        worker.getClip_box().setxMax(32767);
        worker.getClip_box().setyMax(32767);
      }
    }
    gray_init_cells(worker, raster.buffer, raster.buffer_size);
    worker.setOutline(outline);
    worker.setNum_cells(0);
    worker.setInvalid(true);
    worker.setBand_size(raster.getBand_size());
    worker.setNum_gray_spans(0);
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_DIRECT) != 0) {
//FIXME!!      worker.render_span(params.gray_spans);
      worker.setRender_span_data(params.getUser_data());
    } else {
      worker.setTarget(target_map);
      worker.setRender_span_data(worker);
    }
    return gray_convert_glyph(worker);
  }

  /* =====================================================================
   *    gray_raster_done
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_raster_done() {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_done");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    // nothing to do
    return error;
  }

  /* =====================================================================
   *    gray_render_span
   *
   * =====================================================================
   */
  private void gray_render_span(int y, int count, FTSpanRec[] spans, grayTWorkerRec worker) {
    int spansIdx = 0;
    int pIdx;
    int qIdx;
    FTBitmapRec map = worker.getTarget();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_span y: %d, count: %d", y, count));

    /* first of all, compute the scanline offset */
    pIdx = 0 - y * map.getPitch();
    if (map.getPitch() >= 0) {
      pIdx += ((map.getRows() - 1) * map.getPitch());
    }
    for (; count > 0; count--, spansIdx++) {
      byte coverage = spans[spansIdx].getCoverage();

      if (coverage != 0) {
          /* For small-spans it is faster to do it by ourselves than
           * calling `memset'.  This is mainly due to the cost of the
           * function call.
           */
        if (spans[spansIdx].getLen() >= 8) {
          int lgth = spans[spansIdx].getLen();
          int i;
          for (i = spans[spansIdx].getX(); i < spans[spansIdx].getX() + lgth; i++) {
            map.getBuffer()[i] = (byte)coverage;
          }
//            FT_MEM_SET(p + spans[spansIdx].x, (char)coverage, spans[spansIdx].len);
        } else {
//            char*  q = p + spans.x;
          qIdx = pIdx + spans[spansIdx].getX();

          switch (spans[spansIdx].getLen()) {
            case 7: map.getBuffer()[qIdx++] = (byte)coverage;
            case 6: map.getBuffer()[qIdx++] = (byte)coverage;
            case 5: map.getBuffer()[qIdx++] = (byte)coverage;
            case 4: map.getBuffer()[qIdx++] = (byte)coverage;
            case 3: map.getBuffer()[qIdx++] = (byte)coverage;
            case 2: map.getBuffer()[qIdx++] = (byte)coverage;
            case 1: map.getBuffer()[qIdx] = (byte)coverage;
            default:
              ;
          }
        }
      }
    }
  }

  /* =====================================================================
   *    gray_init_cells
   *
   * =====================================================================
   */
  private void gray_init_cells(grayTWorkerRec worker, byte[] buffer, int byte_size) {
    Debug(0, DebugTag.DBG_RENDER, TAG, "+++ gray_init_cells");

    worker.setBuffer(buffer);
    worker.setBuffer_size(byte_size);
    worker.setYcells(new TCellRec[byte_size / 10]);
    worker.setCells(null);
    worker.setMax_cells(0);
    worker.setNum_cells(0);
    worker.setArea(0);
    worker.setCover(0);
    worker.setInvalid(true);
  }

  /* =====================================================================
   *    gray_convert_glyph_inner
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_convert_glyph_inner(grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_convert_glyph_inner");
    func_interface = new FTGrayOutlineFuncsClass();
    FTReference<FTOutlineRec> outline_ref = new FTReference<FTOutlineRec>();
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

//      if (ft_setjmp(ras.jump_buffer) == 0) {
    error = worker.getOutline().FTOutlineDecompose((FTOutlineFuncs)func_interface, worker);
    func_interface.gray_record_cell(worker);
//      } else {
//        error = FT_THROW( Memory_Overflow );
//      }
    return error;
  }

  /* =====================================================================
   *    gray_hline
   *
   * =====================================================================
   */
  private void gray_hline(grayTWorkerRec worker, int x, int y, int area, int acount) {
    Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_hline: x: %d, y: %d, area: 0x%08x, acount: %d", x, y, area, acount));
    int coverage = 0;
    int spanIdx = 0;

      /* compute the coverage line's coverage, depending on the    */
      /* outline fill rule                                         */
      /*                                                           */
      /* the coverage percentage is area/(PIXEL_BITS*PIXEL_BITS*2) */
      /*                                                           */
    coverage = (area >> (FTGrayOutlineFuncsClass.PIXEL_BITS * 2 + 1 - 8));
                                                      /* use range 0..256 */
    if (coverage < 0) {
      coverage = -coverage;
    }
    if ((worker.getOutline().getFlags() & Flags.Outline.EVEN_ODD_FILL.getVal()) != 0) {
      coverage &= 511;
      if (coverage > 256) {
        coverage = 512 - coverage;
      } else {
        if (coverage == 256) {
          coverage = 255;
        }
      }
    } else {
        /* normal non-zero winding rule */
      if (coverage >= 256) {
        coverage = 255;
      }
    }
    y += worker.getMin_ey();
    x += worker.getMin_ex();
      /* FT_Span.x is a 16-bit short, so limit our coordinates appropriately */
    if (x >= 32767) {
      x = 32767;
    }
      /* FT_Span.y is an integer, so limit our coordinates appropriately */
    if (y >= 0xFFFF) {
      y = 0xFFFF;
    }
    Debug(0, DebugTag.DBG_RENDER, TAG, String.format("x: %d, y: %d, coverage: %x", x, y, coverage));
    if (coverage != 0) {
      int count;
      FTSpanRec span = null;

        /* see whether we can add this span to the current list */
      count = worker.getNum_gray_spans();
      spanIdx = count - 1;
      if (count > 0) {
        span = worker.getGray_span(count - 1);
      } else {
      }
      if (count > 0 && worker.getSpan_y() == y && span.getX() + span.getLen() == (int)x &&
          (span.getCoverage() & 0xFF) == coverage) {
        span.setLen(span.getLen() + acount);
        worker.setGray_span(count - 1, span);
        return;
      }
      if (worker.getSpan_y() != y || count >= FTSpanRec.FT_MAX_GRAY_SPANS) {
        if (count > 0) {
          worker.renderSpan(worker.getSpan_y(), count, worker.getGray_spans(),
              (grayTWorkerRec)worker.getRender_span_data());
        }
        if (count > 0) {
          int  n;
          StringBuffer str = new StringBuffer();

          str.append(String.format("y = %3d ", worker.getSpan_y()));
          for (n = 0; n < count; n++) {
            str.append(String.format("  [%d..%d]:0x%02x ",
                worker.getGray_span(n).getX(), worker.getGray_span(n).getX() + worker.getGray_span(n).getLen() - 1, worker.getGray_span(n).getCoverage()));
          }
          FTTrace.Trace(7, TAG, str.toString());
        }
        worker.setNum_gray_spans(0);
        worker.setSpan_y(y);
        count = 0;
        spanIdx = 0;
      } else {
        spanIdx++;
      }
        /* add a gray span to the current list */
      worker.getGray_span(spanIdx).setX(x);
      worker.getGray_span(spanIdx).setLen(acount);
      worker.getGray_span(spanIdx).setCoverage((byte)coverage);
      worker.setNum_gray_spans(worker.getNum_gray_spans() + 1);
    }
    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_hline END 2");
  }

  /* =====================================================================
   *    gray_sweep
   *
   * =====================================================================
   */
  public void gray_sweep(grayTWorkerRec worker, FTBitmapRec target) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_sweep");
    int  yindex;

    if (worker.getNum_cells() == 0) {
      return;
    }
    worker.setNum_gray_spans(0);
    FTTrace.Trace(7, TAG, "gray_sweep: start "+worker.getYcount());
    for (yindex = 0; yindex < worker.getYcount(); yindex++) {
      TCellRec cell = worker.getYcell(yindex);
      int cover = 0;
      int x = 0;

      for ( ; cell != null; cell = cell.getNext()) {
        int area;

//System.out.println(String.format("cell->next: %d", cell.next == null ? -1 : cell.next.self_idx));
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_sweep 1: cell.x: %d x: %d,  yindex: %d, cover: %x", cell.getX(), x, yindex, cover));
        if (cell.getX() > x && cover != 0) {
          gray_hline(worker, x, yindex, cover * (RasterUtil.ONE_PIXEL() * 2), cell.getX() - x );
        }
        cover += cell.getCover();
        area = cover * (RasterUtil.ONE_PIXEL() * 2) - cell.getArea();
        if (area != 0 && cell.getX() >= 0) {
          gray_hline(worker, cell.getX(), yindex, area, 1 );
        }
        x = cell.getX() + 1;
      }
      if (cover != 0) {
        gray_hline(worker, x, yindex, cover * (RasterUtil.ONE_PIXEL() * 2), worker.getCount_ex() - x);
      }
    }
    if (worker.getNum_gray_spans() > 0) {
      worker.renderSpan(worker.getSpan_y(), worker.getNum_gray_spans(),
          worker.getGray_spans(), (grayTWorkerRec)worker.getRender_span_data());
    }
    if (worker.getNum_gray_spans() > 0) {
      FTSpanRec span;
      int spanIdx = 0;
      int n;
      StringBuffer str = new StringBuffer("");

      str.append(String.format("y = %3d ", worker.getSpan_y()));
      for (n = 0; n < worker.getNum_gray_spans(); n++, spanIdx++) {
        span = worker.getGray_span(spanIdx);
        str.append(String.format("  [%d..%d]:0x%02x ",
            span.getX(), span.getX() + span.getLen() - 1, span.getCoverage() & 0xFF));
      }
      FTTrace.Trace(7, TAG, str.toString());
    }
    FTTrace.Trace(7, TAG, "gray_sweep: end");
  }

  /* =====================================================================
   *    gray_convert_glyph
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_convert_glyph(grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_convert_glyph");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    grayTBandRec[] bands = new grayTBandRec[40];
    int bandIdx = 0;
    int n;
    int i;
    int num_bands;
    int min;
    int max;
    int max_y;
    FTBBoxRec clip;
    boolean reduceBands = false;

    for (i = 0; i < 40; i++) {
      bands[i] = new grayTBandRec();
    }

      /* Set up state in the raster object */
    func_interface.gray_compute_cbox(worker);
      /* clip to target bitmap, exit if nothing to do */
    clip = worker.getClip_box();
    if (worker.getMax_ex() <= clip.getxMin() || worker.getMin_ex() >= clip.getxMax() ||
        worker.getMax_ey() <= clip.getyMin() || worker.getMin_ey() >= clip.getyMax()) {
      return error;
    }
    if (worker.getMin_ex() < clip.getxMin()) {
      worker.setMin_ex(clip.getxMin());
    }
    if (worker.getMin_ey() < clip.getyMin()) {
      worker.setMin_ey(clip.getyMin());
    }
    if (worker.getMax_ex() > clip.getxMax()) {
      worker.setMax_ex(clip.getxMax());
    }
    if (worker.getMax_ey() > clip.getyMax()) {
      worker.setMax_ey(clip.getyMax());
    }
    worker.setCount_ex(worker.getMax_ex() - worker.getMin_ex());
    worker.setCount_ey(worker.getMax_ey() - worker.getMin_ey());
      /* set up vertical bands */
    num_bands = (worker.getMax_ey() - worker.getMin_ey() / worker.getBand_size());
    if (num_bands == 0) {
      num_bands = 1;
    }
    if (num_bands >= 39) {
      num_bands = 39;
    }
    worker.setBand_shoot(0);
    min = worker.getMin_ey();
    max_y = worker.getMax_ey();
    for (n = 0; n < num_bands; n++, min = max) {
      max = min + worker.getBand_size();
      if (n == num_bands - 1 || max > max_y) {
        max = max_y;
      }
      bands[0].setMin(min);
      bands[0].setMax(max);
      bandIdx = 0;
      while (bandIdx >= 0) {
        int bottom;
        int top;
        int middle;
        {
          int cells_max;
          int yindex;
          int cell_start;
          int cell_end;
          int cellsIdx = 0;

//            worker.ycells = (TCell[])worker.buffer;
          worker.setYcount(bands[bandIdx].getMax() - bands[bandIdx].getMin());
          worker.setYcells(new TCellRec[worker.getYcount()]);
          cell_start = 0;
          cell_end = 1024;
//            cell_start = TCellSize * worker.ycount;
//            cell_mod = cell_start % TCellSize;
//            if (cell_mod > 0) {
//              cell_start += TCellSize - cell_mod;
//            }
//            cell_end = worker.buffer_size;
//            cell_end -= cell_end % TCellSize;
          cells_max = cell_end;
          cellsIdx = cell_start;
          cellsIdx = 0;
          worker.setCells(new TCellRec[cells_max]);
          for (int j = 0; j < cells_max; j++) {
            worker.setCell(j, new TCellRec());
          }
          if (cellsIdx >= cells_max) {
            reduceBands = true;
          }
          if (!reduceBands) {
            worker.setMax_cells(cells_max - cellsIdx);
            if (worker.getMax_cells() < 2) {
              reduceBands = true;
            }
          }
          if (!reduceBands) {
            for (yindex = 0; yindex < worker.getYcount(); yindex++) {
              worker.setYcell(yindex, null);
            }
          }
        }
        if (!reduceBands) {
          worker.setNum_cells(0);
          worker.setInvalid(true);
          worker.setMin_ey(bands[bandIdx].getMin());
          worker.setMax_ey(bands[bandIdx].getMax());
          worker.setCount_ey(bands[bandIdx].getMax() - bands[bandIdx].getMin());
          error = gray_convert_glyph_inner(worker);
          if (error == FTError.ErrorTag.ERR_OK) {
            gray_sweep(worker, worker.getTarget());
            bandIdx--;
            continue;
          } else {
            if (error != FTError.ErrorTag.RASTER_MEMORY_OVERFLOW) {
              return error;
            }
          }
        }
        reduceBands = false;
          /* render pool overflow; we will reduce the render band by half */
        bottom = bands[bandIdx].getMin();
        top = bands[bandIdx].getMax();
        middle = bottom + ((top - bottom) >> 1);
          /* This is too complex for a single scanline; there must */
          /* be some problems.                                     */
        if (middle == bottom) {
          FTTrace.Trace(7, TAG, "gray_convert_glyph: rotten glyph");
          return FTError.ErrorTag.GLYPH_ROTTEN_GLYPH;
        }
        if (bottom-top >= worker.getBand_size()) {
          worker.setBand_shoot(worker.getBand_shoot() + 1);
        }
        bands[bandIdx + 1].setMin(bottom);
        bands[bandIdx + 1].setMax(middle);
        bands[bandIdx + 0].setMin(middle);
        bands[bandIdx + 0].setMax(top);
        bandIdx++;
      }
    }
    if (worker.getBand_shoot() > 8 && worker.getBand_size() > 16) {
      worker.setBand_size(worker.getBand_size() / 2);
    }
    return error;
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
  public FTError.ErrorTag setMode(FTRendererRec renderer, FTParameter.ParamTag tag, Object data) {
    Log.e(TAG, "setMode not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_smooth_set_mode();
  }

  /* ==================== rasterNew ===================================== */
  @Override
  public FTError.ErrorTag rasterNew(FTReference<FTRasterRec> raster_ref) {
    return gray_raster_new(raster_ref);
  }


  /* ==================== rasterReset ===================================== */
  @Override
  public FTError.ErrorTag rasterReset(FTRasterRec raster, byte[] raster_pool, int raster_pool_size) {
    return gray_raster_reset(raster, raster_pool, raster_pool_size);
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
    return gray_raster_render(raster, params);
  }

  /* ==================== rasterDone ===================================== */
  @Override
  public FTError.ErrorTag rasterDone(FTRasterRec raster) {
    return gray_raster_done();
  }

  /* ==================== grayRenderSpan ===================================== */
  public void grayRenderSpan(int y, int count, FTSpanRec[] spans, grayTWorkerRec worker) {
    gray_render_span(y, count, spans, worker);
  }

}
