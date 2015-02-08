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
  /*    grayTWorkerRec                                                     */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBBoxRec;
import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.RasterUtil;

public class grayTWorkerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTWorkerRec";
  private final static int MaxBezier = 32;

  private int ex;
  private int ey;
  private int min_ex;
  private int max_ex;
  private int min_ey;
  private int max_ey;
  private int count_ex;
  private int count_ey;
  private int area;
  private int cover;
  private boolean invalid;
  private TCellRec[] cells = null;
  private int max_cells;
  private int num_cells;
  private int cx;
  private int cy;
  private int x;
  private int y;
  private int last_ey;
  private FTVectorRec[] bez_stack = new FTVectorRec[MaxBezier * 3 + 1];
  private int[] lev_stack = new int[32];
  private FTOutlineRec outline;
  private FTBitmapRec target;
  private FTBBoxRec clip_box;
  private FTSpanRec[] gray_spans = new FTSpanRec[FTSpanRec.FT_MAX_GRAY_SPANS];
  private int num_gray_spans;
  private Object render_span_data;
  private int span_y;
  private int band_size;
  private int band_shoot;
  private byte[] buffer;
  private int buffer_size;
  private TCellRec[] ycells;
  private int ycount;
  private FTGrayOutlineFuncsClass outline_funcs = null;

  /* ==================== grayTWorker ================================== */
  public grayTWorkerRec() {
    int i;

    oid++;
    id = oid;
    target = new FTBitmapRec();
    outline = new FTOutlineRec();
    clip_box = new FTBBoxRec();
    for (i = 0; i < FTSpanRec.FT_MAX_GRAY_SPANS; i++) {
      gray_spans[i] = new FTSpanRec();
    }
    for (i = 0; i < MaxBezier * 3 + 1; i++) {
      bez_stack[i] = new FTVectorRec();
    }
    outline_funcs = new FTGrayOutlineFuncsClass();
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
    str.append("...ex: "+ex+'\n');
    str.append("...ey: "+ey+'\n');
    str.append("...min_ex: "+min_ex+'\n');
    str.append("...max_ex: "+max_ex+'\n');
    str.append("...min_ey: "+min_ey+'\n');
    str.append("...max_ey: "+max_ey+'\n');
    str.append("...count_ex: "+count_ex+'\n');
    str.append("...count_ey: "+count_ey+'\n');
    str.append("...area: "+area+'\n');
    str.append("...cover: "+cover+'\n');
    str.append("...invalid: "+invalid+'\n');
    str.append("...max_cells: "+max_cells+'\n');
    str.append("...num_cells: "+num_cells+'\n');
    str.append("...cx: "+cx+'\n');
    str.append("...cy: "+cy+'\n');
    str.append("...x: "+x+'\n');
    str.append("...y: "+y+'\n');
    str.append("...last_ey: "+last_ey+'\n');
    str.append("...num_gray_spans: "+num_gray_spans+'\n');
    str.append("...span_y: "+span_y+'\n');
    str.append("...band_size: "+band_size+'\n');
    str.append("...band_shoot: "+band_shoot+'\n');
    str.append("...buffer_size: "+buffer_size+'\n');
    str.append("...ycount: "+ycount+'\n');
    return str.toString();
  }

  /* =====================================================================
   *    gray_raster_reset
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_raster_reset(grayTRasterRec raster, byte[] pool_base, int pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_reset");
    if (pool_base != null) {
      buffer = pool_base;
      buffer_size = pool_size;
//        raster.band_size = (int)raster.buffer_size / sizeof(TCell) * 8;
      band_size = buffer_size / 20;  // FIXME!!!
//      grayTWorkerRec worker =  new grayTWorkerRec();
//      raster.setWorker(worker);
    } else {
      buffer = null;
      buffer_size = 0;
//      raster.setWorker(null);
    }
    return error;
  }

  /* =====================================================================
   *    gray_raster_render
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_raster_render(grayTRasterRec raster, FTRasterParamsRec params) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_render");
    FTOutlineRec outline = (FTOutlineRec)params.getSource();
    FTBitmapRec target_map = params.getTarget();

    if (buffer == null || buffer_size == 0) {
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
      clip_box.setxMin(0);
      clip_box.setyMin(0);
      clip_box.setxMax(target_map.getWidth());
      clip_box.setyMax(target_map.getRows());
    } else {
      if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_CLIP) != 0) {
        clip_box = params.getClip_box();
      } else {
        clip_box.setxMin(-32768);
        clip_box.setyMin(-32768);
        clip_box.setxMax(32767);
        clip_box.setyMax(32767);
      }
    }
    gray_init_cells(buffer, buffer_size);
    this.outline = outline;
    num_cells = 0;
    invalid = true;
    band_size = raster.getBand_size();
    num_gray_spans = 0;
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_DIRECT) != 0) {
//FIXME!!      worker.render_span(params.gray_spans);
      render_span_data = params.getUser_data();
    } else {
      target = target_map;
      render_span_data = this;
    }
    return gray_convert_glyph();
  }

  /* =====================================================================
   *    gray_raster_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_raster_done() {
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
  public void gray_render_span(int y, int count, FTSpanRec[] spans) {
    int spansIdx = 0;
    int pIdx;
    int qIdx;
    FTBitmapRec map = target;
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
            map.getBuffer()[i] = coverage;
          }
//            FT_MEM_SET(p + spans[spansIdx].x, (char)coverage, spans[spansIdx].len);
        } else {
//            char*  q = p + spans.x;
          qIdx = pIdx + spans[spansIdx].getX();

          switch (spans[spansIdx].getLen()) {
            case 7: map.getBuffer()[qIdx++] = coverage;
            case 6: map.getBuffer()[qIdx++] = coverage;
            case 5: map.getBuffer()[qIdx++] = coverage;
            case 4: map.getBuffer()[qIdx++] = coverage;
            case 3: map.getBuffer()[qIdx++] = coverage;
            case 2: map.getBuffer()[qIdx++] = coverage;
            case 1: map.getBuffer()[qIdx] = coverage;
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
  public void gray_init_cells(byte[] buffer, int byte_size) {
Debug(0, DebugTag.DBG_RENDER, TAG, "+++ gray_init_cells");

    this.buffer = buffer;
    buffer_size = byte_size;
    ycells = new TCellRec[byte_size / 10];
    cells = null;
    max_cells = 0;
    num_cells = 0;
    area = 0;
    cover = 0;
    invalid = true;
  }

  /* =====================================================================
   *    gray_convert_glyph_inner
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_convert_glyph_inner() {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_convert_glyph_inner");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

//      if (ft_setjmp(ras.jump_buffer) == 0) {
    error = outline.FTOutlineDecompose(outline_funcs, this);
    gray_record_cell();
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
  public void gray_hline(int x, int y, int area, int acount) {
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
    if ((outline.getFlags() & Flags.Outline.EVEN_ODD_FILL.getVal()) != 0) {
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
    y += min_ey;
    x += min_ex;
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
      count = num_gray_spans;
      spanIdx = count - 1;
      if (count > 0) {
        span = gray_spans[count - 1];
      } else {
      }
      if (count > 0 && span_y == y && span.getX() + span.getLen() == x &&
          (span.getCoverage() & 0xFF) == coverage) {
        span.setLen(span.getLen() + acount);
        gray_spans[count - 1] = span;
        return;
      }
      if (span_y != y || count >= FTSpanRec.FT_MAX_GRAY_SPANS) {
        if (count > 0) {
          gray_render_span(span_y, count, gray_spans);
        }
        if (count > 0) {
          int  n;
          StringBuffer str = new StringBuffer();

          str.append(String.format("y = %3d ", span_y));
          for (n = 0; n < count; n++) {
            str.append(String.format("  [%d..%d]:0x%02x ",
                gray_spans[n].getX(), gray_spans[n].getX() + gray_spans[n].getLen() - 1, gray_spans[n].getCoverage()));
          }
          FTTrace.Trace(7, TAG, str.toString());
        }
        num_gray_spans = 0;
        span_y = y;
        count = 0;
        spanIdx = 0;
      } else {
        spanIdx++;
      }
        /* add a gray span to the current list */
      gray_spans[spanIdx].setX(x);
      gray_spans[spanIdx].setLen(acount);
      gray_spans[spanIdx].setCoverage((byte)coverage);
      num_gray_spans = num_gray_spans + 1;
    }
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_hline END 2");
  }

  /* =====================================================================
   *    gray_sweep
   *
   * =====================================================================
   */
  public void gray_sweep(FTBitmapRec target) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_sweep");
    int  yindex;

    if (num_cells == 0) {
      return;
    }
    num_gray_spans = 0;
    FTTrace.Trace(7, TAG, "gray_sweep: start "+ycount);
    for (yindex = 0; yindex < ycount; yindex++) {
      TCellRec cell = ycells[yindex];
      int cover = 0;
      int x = 0;

      for ( ; cell != null; cell = cell.getNext()) {
        int area;

//System.out.println(String.format("cell->next: %d", cell.next == null ? -1 : cell.next.self_idx));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_sweep 1: cell.x: %d x: %d,  yindex: %d, cover: %x", cell.getX(), x, yindex, cover));
        if (cell.getX() > x && cover != 0) {
          gray_hline(x, yindex, cover * (RasterUtil.ONE_PIXEL() * 2), cell.getX() - x );
        }
        cover += cell.getCover();
        area = cover * (RasterUtil.ONE_PIXEL() * 2) - cell.getArea();
        if (area != 0 && cell.getX() >= 0) {
          gray_hline(cell.getX(), yindex, area, 1 );
        }
        x = cell.getX() + 1;
      }
      if (cover != 0) {
        gray_hline(x, yindex, cover * (RasterUtil.ONE_PIXEL() * 2), count_ex - x);
      }
    }
    if (num_gray_spans > 0) {
      gray_render_span(span_y, num_gray_spans, gray_spans);
    }
    if (num_gray_spans > 0) {
      FTSpanRec span;
      int spanIdx = 0;
      int n;
      StringBuffer str = new StringBuffer("");

      str.append(String.format("y = %3d ", span_y));
      for (n = 0; n < num_gray_spans; n++, spanIdx++) {
        span = gray_spans[spanIdx];
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
  public FTError.ErrorTag gray_convert_glyph() {
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
    gray_compute_cbox();
      /* clip to target bitmap, exit if nothing to do */
    clip = clip_box;
    if (max_ex <= clip.getxMin() || min_ex >= clip.getxMax() ||
        max_ey <= clip.getyMin() || min_ey >= clip.getyMax()) {
      return error;
    }
    if (min_ex < clip.getxMin()) {
      min_ex = clip.getxMin();
    }
    if (min_ey < clip.getyMin()) {
      min_ey = clip.getyMin();
    }
    if (max_ex > clip.getxMax()) {
      max_ex = clip.getxMax();
    }
    if (max_ey > clip.getyMax()) {
      max_ey = clip.getyMax();
    }
    count_ex = max_ex - min_ex;
    count_ey = max_ey - min_ey;
      /* set up vertical bands */
    num_bands = (max_ey - min_ey / band_size);
    if (num_bands == 0) {
      num_bands = 1;
    }
    if (num_bands >= 39) {
      num_bands = 39;
    }
    band_shoot = 0;
    min = min_ey;
    max_y = max_ey;
    for (n = 0; n < num_bands; n++, min = max) {
      max = min + band_size;
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
          ycount = bands[bandIdx].getMax() - bands[bandIdx].getMin();
          ycells = new TCellRec[ycount];
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
          cells = new TCellRec[cells_max];
          for (int j = 0; j < cells_max; j++) {
            cells[j] = new TCellRec();
          }
          if (cellsIdx >= cells_max) {
            reduceBands = true;
          }
          if (!reduceBands) {
            max_cells = cells_max - cellsIdx;
            if (max_cells < 2) {
              reduceBands = true;
            }
          }
          if (!reduceBands) {
            for (yindex = 0; yindex < ycount; yindex++) {
              ycells[yindex] = null;
            }
          }
        }
        if (!reduceBands) {
          num_cells = 0;
          invalid = true;
          min_ey = bands[bandIdx].getMin();
          max_ey = bands[bandIdx].getMax();
          count_ey = bands[bandIdx].getMax() - bands[bandIdx].getMin();
          error = gray_convert_glyph_inner();
          if (error == FTError.ErrorTag.ERR_OK) {
            gray_sweep(target);
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
        if (bottom - top >= band_size) {
          band_shoot = band_shoot + 1;
        }
        bands[bandIdx + 1].setMin(bottom);
        bands[bandIdx + 1].setMax(middle);
        bands[bandIdx + 0].setMin(middle);
        bands[bandIdx + 0].setMax(top);
        bandIdx++;
      }
    }
    if (band_shoot > 8 && band_size > 16) {
      band_size = band_size / 2;
    }
    return error;
  }

  /* =====================================================================
   *    gray_move_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_move_to(FTVectorRec to) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int x;
    int y;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_move_to: to.x: %d, to.y: %d", to.getX(), to.getY()));

      /* record current cell, if any */
    gray_record_cell();
      /* start to a new position */
    x = RasterUtil.UPSCALE(to.getX());
    y = RasterUtil.UPSCALE(to.getY());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_move_to2: x(UPSCALE): %d, y(UPSCALE): %d", x, y));
    gray_start_cell(RasterUtil.TRUNC(x), RasterUtil.TRUNC(y));
    this.x = x;
    this.y = y;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_move_to end: worker.x: %d(%d)(%f), worker.y: %d(%d)(%f)", this.x, this.x/4, this.x/256.0, this.y, this.y/4, this.y/256.0));
    return error;
  }

  /* =====================================================================
   *    gray_line_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_line_to(FTVectorRec to) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_line_to: to.x: %d, to.y: %d", to.getX(), to.getY()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_line_to call gray_render_line");
    gray_render_line(RasterUtil.UPSCALE(to.getX()), RasterUtil.UPSCALE(to.getY()));
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_line_to after call gray_render_line");
    return error;
  }

  /* =====================================================================
   *    gray_conic_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_conic_to(FTVectorRec control, FTVectorRec to) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_conic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_conic(control, to);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_conic_to end");
    return error;
  }

  /* =====================================================================
   *    gray_cubic_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_cubic_to(FTVectorRec control1, FTVectorRec control2, FTVectorRec to) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_cubic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_cubic(control1, control2, to);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_cubic_to end");
    return error;
  }

  /* =====================================================================
   *    gray_find_cell
   *
   * =====================================================================
   */
  private TCellRec gray_find_cell() {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("  gray_find_cell: ras.ex: %d, ras.ey: %d, ras.count_ex: %d, ras.num_cells: %d", this.ex, this.ey, count_ex, num_cells));
    int pcellIdx;
    int cellIdx;
    TCellRec cell = null;
    TCellRec pcell = null;
    int x = ex;
    boolean useYcells = true;
    boolean isYcell = true;

    if (x > count_ex) {
      x = count_ex;
    }
    for(int j = 0; j < ycount; j++) {
      int a = ycells[j] == null ? -1 : ycells[j].getArea();
      int c = ycells[j] == null ? -1 : ycells[j].getCover();
      StringBuffer str = new StringBuffer("");
      str.append(String.format("ycells j: %2d\n\t\tidx: %2d, x: %2d, a: %06x, c: %06x", j,
          ycells[j] == null ? -1 : ycells[j].getSelf_idx(),
          ycells[j] == null ? -1 : ycells[j].getX(),
          a & 0xFFFFFF, c & 0xFFFFFF));
      cell = ycells[j];
      while( cell != null) {
        cell = cell.getNext();
        if (cell != null) {
          str.append(String.format("\n\t\tidx: %2d, x: %2d, a: %06x, c: %06x",
              cell.getSelf_idx(), cell.getX(), cell.getArea() & 0xFFFFFF, cell.getCover() & 0xFFFFFF));
        }
      }
      Debug(0, DebugTag.DBG_RENDER, TAG, str.toString());
    }

    pcellIdx = ey;
    cellIdx = -1;
    for (;;) {
      if (cellIdx == -1) {
        cell = ycells[pcellIdx];
      } else {
        cell = cell.getNext();
      }
      if (cell == null || cell.getX() > x) {
        break;
      }
      if (cell.getX() == x) {
        return cell;
      }
      if (!useYcells) {
        isYcell = false;
      }
      useYcells = false;
      cellIdx = cell.getSelf_idx();
    }
    if (num_cells >= max_cells) {
//FIXME!!        ft_longjmp( worker.jump_buffer, 1 );
      Log.e(TAG, "out of worker.cells: " + num_cells + "!" + max_cells);
    }
    cell = cells[num_cells];
    num_cells = num_cells + 1;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("new cell: x: %d, worker.ex: %d, worker.ey: %d", x, ex, ey));
    cell.setSelf_idx(num_cells - 1);
    cell.setX(x);
    cell.setArea(0);
    cell.setCover(0);
    if (useYcells) {
      cell.setNext(ycells[pcellIdx]);
//System.out.println(String.format("pcellIdx: %d",  pcellIdx));
      ycells[pcellIdx] = cell;
    } else {
      if (isYcell) {
        cell.setNext(ycells[pcellIdx].getNext());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        ycells[pcellIdx].setNext(cell);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("isYcell: cell idx: %d cell next idx: %d ", cell.getSelf_idx(), cell.getNext() == null ? -1 : cell.getNext().getSelf_idx()));
      } else {
        cell.setNext(cells[cellIdx].getNext());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        cells[cellIdx].setNext(cell);
      }
    }
    return cell;
  }

  /* =====================================================================
   *    gray_record_cell
   *
   * =====================================================================
   */
  public void gray_record_cell() {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_record_cell: ras.invalid: %b, ras.area: %x, ras.cover: %x\n", invalid, area, cover));

    if (!invalid && (area | cover) != 0) {
      TCellRec cell = gray_find_cell();

      cell.setArea(cell.getArea() + area);
      cell.setCover(cell.getCover() + cover);
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_record_cell2: cell.area: %x, cell.cover: %x worker.area: %x, worker.cover: %x\n", cell.getArea(), cell.getCover(), area, cover));
    }
  }

  /* =====================================================================
   *    gray_set_cell
   *
   * Move the cell pointer to a new position.  We set the `invalid'
   * flag to indicate that the cell isn't part of those we're interested
   * in during the render phase.  This means that:
   *
   * . the new vertical position must be within min_ey..max_ey-1.
   * . the new horizontal position must be strictly less than max_ex
   *
   * Note that if a cell is to the left of the clipping region, it is
   * actually set to the (min_ex-1) horizontal position.
   *
   * =====================================================================
   */
  private void gray_set_cell(int ex, int ey) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell: ex: %d, ey: %d, invalid: %b, worker.ex: %d, worker.ey: %d", ex, ey, invalid, this.ex, this.ey));
      /* All cells that are on the left of the clipping region go to the */
      /* min_ex - 1 horizontal position.                                 */
    ey -= min_ey;
    if (ex > max_ex) {
      ex = max_ex;
    }
    ex -= min_ex;
    if (ex < 0) {
      ex = -1;
    }
      /* are we moving to a different cell ? */
    if (ex != this.ex || ey != this.ey) {
        /* record the current one if it is valid */
      if (!invalid) {
        gray_record_cell();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell after call gray_record_cell"));
      }
      area = 0;
      cover = 0;
    }
    this.ex = ex;
    this.ey = ey;
    invalid = (ey >= count_ey || ex >= count_ex);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell2: invalid: %b, worker.area: %d, worker.cover: %d, worker.ex: %d, worker.ey: %d, worker.x: %d, worker.y: %d", invalid, area, cover, this.ex, this.ey, this.x, this.y));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell end"));
  }

  /* =====================================================================
   *    gray_start_cell
   *
   * =====================================================================
   */
  private void gray_start_cell(int ex, int ey) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_start_cell: ex: %d, ey: %d, worker.max_ex: %d, worker.min_ex: %d", ex, ey, max_ex, min_ex));

    if (ex > max_ex) {
      ex = max_ex;
    }
    if (ex < min_ex) {
      ex = min_ex - 1;
    }
    area = 0;
    cover = 0;
    this.ex = ex - min_ex;
    this.ey = ey - min_ey;
    last_ey = RasterUtil.SUBPIXELS(ey);
    invalid = false;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_start_cell2: worker.x: %d, worker.y: %d, worker.ex: %d, worker.ex: %d, worker.getlast_ey: %d", this.x, this.y, this.ex, this.ey, last_ey));
    gray_set_cell(ex, ey);
  }


  /* =====================================================================
   *    gray_render_scanline
   *
   * =====================================================================
   */
  private void gray_render_scanline(int ey, int x1, int y1, int x2, int y2) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_scanline0 x1: %d(%d)(%.2f) y1: %d(%d)(%.2f), x2: %d(%d)(%.2f), y2: %d(%d)(%.2f)", x1, x1/4, x1/256.0, y1, y1/4, y1/256.0, x2, x2/4, x2/256.0, y2, y2/4, y2/256.0));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_scanline1 worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
    int ex1;
    int ex2;
    int fx1;
    int fx2;
    int delta;
    int mod;
    int p;
    int first;
    int dx;
    int incr;

    dx = x2 - x1;
    ex1 = RasterUtil.TRUNC(x1);
    ex2 = RasterUtil.TRUNC(x2);
    fx1 = x1 - RasterUtil.SUBPIXELS(ex1);
    fx2 = x2 - RasterUtil.SUBPIXELS(ex2);
      /* trivial case.  Happens often */
    if (y1 == y2) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("call gray_set_cell ex2: %d, ey: %d", ex2, ey));
      gray_set_cell(ex2, ey);
      return;
    }
      /* everything is located in a single cell.  That is easy! */
      /*                                                        */
    if (ex1 == ex2) {
      delta = y2 - y1;
      area = area + ((fx1 + fx2) * delta);
      cover = cover + delta;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_scanline return2: worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_scanline return2 ex1 == ex2: %d", ex2));
      return;
    }
      /* ok, we'll have to render a run of adjacent cells on the same */
      /* scanline...                                                  */
      /*                                                              */
    p = (RasterUtil.ONE_PIXEL() - fx1) * (y2 - y1);
    first = RasterUtil.ONE_PIXEL();
    incr = 1;
    if (dx < 0) {
      p = fx1 * (y2 - y1);
      first = 0;
      incr = -1;
      dx = -dx;
    }
    delta = (p / dx);
    mod   = (p % dx);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("delta: 0x%08x, p: 0x%08x, dx: 0x%08x, mod: 0x%08x", delta, p, dx, mod));
    if (mod < 0) {
      delta--;
      mod += dx;
    }
    area = area + ((fx1 + first) * delta);
    cover = cover + delta;
    ex1 += incr;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("call gray_set_cell2 ex1: %d, ey: %d", ex1, ey));
    gray_set_cell(ex1, ey);
    y1  += delta;
    if (ex1 != ex2) {
      int lift;
      int rem;

      p = RasterUtil.ONE_PIXEL() * (y2 - y1 + delta);
      lift = (p / dx);
      rem  = (p % dx);
      if (rem < 0) {
        lift--;
        rem += dx;
      }
      mod -= (int)dx;
      while (ex1 != ex2) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ex1: %d ex2: %d incr: %d", ex1, ex2, incr));
        delta = lift;
        mod  += rem;
        if (mod >= 0) {
          mod -= dx;
          delta++;
        }
        area = area + (RasterUtil.ONE_PIXEL() * delta);
        cover = cover + delta;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_scanline3: worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
        y1 += delta;
        ex1 += incr;
        gray_set_cell(ex1, ey);
      }
    }
    delta = y2 - y1;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("return end delta: %d", delta));
    area = area  + ((fx2 + RasterUtil.ONE_PIXEL() - first) * delta);
    cover = cover + delta;
  }

  /* =====================================================================
   *    gray_render_line
   *
   * =====================================================================
   */
  private void gray_render_line(int to_x, int to_y) {
    int ey1;
    int ey2;
    int fy1;
    int fy2;
    int mod;
    int dx;
    int dy;
    int x;
    int x2;
    int p;
    int first;
    int delta;
    int rem;
    int lift;
    int incr;

Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_line: to_x: %d, to_y: %d", to_x, to_y));
    ey1 = RasterUtil.TRUNC(last_ey);
    ey2 = RasterUtil.TRUNC(to_y);     /* if (ey2 >= ras.max_ey) ey2 = ras.max_ey-1; */
    fy1 = (this.y - last_ey);
    fy2 = (to_y - RasterUtil.SUBPIXELS(ey2));
    dx = to_x - this.x;
    dy = to_y - this.y;
      /* XXX: we should do something about the trivial case where dx == 0, */
      /*      as it happens very often!                                    */

      /* perform vertical clipping */
    {
      int min;
      int max;

      min = ey1;
      max = ey2;
      if (ey1 > ey2) {
        min = ey2;
        max = ey1;
      }
      if (min >= max_ey || max < min_ey) {
        this.x = to_x;
        this.y = to_y;
        last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line return 1");
        return;
      }
    }
      /* everything is on a single scanline */
    if (ey1 == ey2) {
      gray_render_scanline(ey1, this.x, fy1, to_x, fy2);
      this.x = to_x;
      this.y = to_y;
      last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line return 2");
      return;
    }
      /* vertical line - avoid calling gray_render_scanline */
    incr = 1;
    if (dx == 0) {
      int ex = RasterUtil.TRUNC(this.x);
      int two_fx = ((this.x - RasterUtil.SUBPIXELS(ex)) << 1);
      int area;

      first = RasterUtil.ONE_PIXEL();
      if (dy < 0) {
        first = 0;
        incr  = -1;
      }
      delta = first - fy1;
      this.area = this.area  + two_fx * delta;
      this.cover = this.cover + delta;
      ey1 += incr;
      gray_set_cell(ex, ey1);
      delta = (first + first - RasterUtil.ONE_PIXEL());
      area = two_fx * delta;
      while (ey1 != ey2) {
        this.area = this.area + area;
        this.cover = this.cover + delta;
        ey1 += incr;
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_set_cell while");
        gray_set_cell(ex, ey1);
      }
      delta = (fy2 - RasterUtil.ONE_PIXEL() + first);
      this.area = this.area + two_fx * delta;
      this.cover = this.cover + delta;
      this.x = to_x;
      this.y = to_y;
      last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line return 3");
      return;
    }
      /* ok, we have to render several scanlines */
    p = (RasterUtil.ONE_PIXEL() - fy1) * dx;
    first = RasterUtil.ONE_PIXEL();
    incr = 1;
    if (dy < 0) {
      p = fy1 * dx;
      first = 0;
      incr = -1;
      dy = -dy;
    }
    delta = p / dy;
    mod = p % dy;
    if (mod < 0) {
      delta--;
      mod += dy;
    }
    x = this.x + delta;
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_render_scanline 1");
    gray_render_scanline(ey1, this.x, fy1, x, first);
    ey1 += incr;
    gray_set_cell(RasterUtil.TRUNC(x), ey1);
    if (ey1 != ey2) {
      p = RasterUtil.ONE_PIXEL() * dx;
      lift = (p / dy);
      rem = (p % dy);
      if (rem < 0) {
        lift--;
        rem += dy;
      }
      mod -= dy;
      while (ey1 != ey2) {
        delta = lift;
        mod += rem;
        if (mod >= 0) {
          mod -= dy;
          delta++;
        }
        x2 = x + delta;
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_render_scanline 2");
        gray_render_scanline(ey1, x, RasterUtil.ONE_PIXEL() - first, x2, first);
        x = x2;
        ey1 += incr;
        gray_set_cell(RasterUtil.TRUNC(x), ey1);
      }
    }
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_render_scanline 3");
    gray_render_scanline(ey1, x, RasterUtil.ONE_PIXEL() - first, to_x, fy2);
    this.x = to_x;
    this.y = to_y;
    last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line end");
  }

  /* =====================================================================
   *    gray_split_conic
   *
   * =====================================================================
   */
  private void gray_split_conic(FTVectorRec[] base, int arcIdx) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_split_conic");
    int a;
    int b;

    base[arcIdx + 4].setX(base[arcIdx + 2].getX());
    b = base[arcIdx + 1].getX();
    base[arcIdx + 3].setX((base[arcIdx + 2].getX() + b) / 2);
    a = base[arcIdx + 3].getX();
    base[arcIdx + 1].setX((base[arcIdx + 0].getX() + b) / 2);
    b = base[arcIdx + 1].getX();
    base[arcIdx + 2].setX((a + b) / 2);
    base[arcIdx + 4].setY(base[arcIdx + 2].getY());
    b = base[arcIdx + 1].getY();
    base[arcIdx + 3].setY((base[arcIdx + 2].getY() + b) / 2);
    a = base[arcIdx + 3].getY();
    base[arcIdx + 1].setY((base[arcIdx + 0].getY() + b) / 2);
    b = base[arcIdx + 1].getY();
    base[arcIdx + 2].setY((a + b) / 2);
  }

  /* =====================================================================
   *    gray_render_conic
   *
   * =====================================================================
   */
  private void gray_render_conic(FTVectorRec control, FTVectorRec to) {
    int dx;
    int dy;
    int min;
    int max;
    int y;
    int top;
    int level;
    int[] levels;
    FTVectorRec[] arc;
    int arcIdx = 0;
    boolean doDraw = false;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_conic");
    levels = lev_stack;
    arc = bez_stack;
    arc[arcIdx + 0].setX(RasterUtil.UPSCALE(to.getX()));
    arc[arcIdx + 0].setY(RasterUtil.UPSCALE(to.getY()));
    arc[arcIdx + 1].setX(RasterUtil.UPSCALE(control.getX()));
    arc[arcIdx + 1].setY(RasterUtil.UPSCALE(control.getY()));
    arc[arcIdx + 2].setX(this.x);
    arc[arcIdx + 2].setY(this.y);
    top = 0;
    dx = FTCalc.FT_ABS(arc[arcIdx + 2].getX() + arc[arcIdx].getX() - 2 * arc[arcIdx + 1].getX());
    dy = FTCalc.FT_ABS(arc[arcIdx + 2].getY() + arc[arcIdx].getY() - 2 * arc[arcIdx + 1].getY());
    if (dx < dy) {
      dx = dy;
    }
    if (dx < RasterUtil.ONE_PIXEL() / 4) {
      doDraw = true;
    }
    if (!doDraw) {
        /* short-cut the arc that crosses the current band */
      min = max = arc[arcIdx + 0].getY();
      y = arc[arcIdx + 1].getY();
      if (y < min) {
        min = y;
      }
      if (y > max) {
        max = y;
      }
      y = arc[arcIdx + 2].getY();
      if (y < min) {
        min = y;
      }
      if (y > max) {
        max = y;
      }
      if (RasterUtil.TRUNC(min) >= max_ey || RasterUtil.TRUNC(max) < min_ey) {
        doDraw = true;
      }
    }
    if (!doDraw) {
      level = 0;
      do {
        dx >>= 2;
        level++;
      } while (dx > RasterUtil.ONE_PIXEL() / 4);
      levels[0] = level;
    }
    do {
      if (!doDraw) {
        level = levels[top];
        if (level > 0) {
          gray_split_conic(arc, arcIdx);
          arcIdx += 2;
          top++;
          levels[top] = levels[top - 1] = level - 1;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic !doDraw continue level: %d", level));
          continue;
        }
      }
      doDraw = false;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic call gray_render_line top: %d", top));

      gray_render_line(arc[arcIdx + 0].getX(), arc[arcIdx + 0].getY());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic call after gray_render_line top: %d", top));
      top--;
      arcIdx -= 2;
    } while ( top >= 0 );
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic end top: %d", top));
  }

  /* =====================================================================
   *    gray_split_cubic
   *
   * =====================================================================
   */
  private void gray_split_cubic(FTVectorRec[] base, int arcIdx) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_split_cubic");
    int a;
    int b;
    int c;
    int d;

    base[arcIdx + 6].setX(base[arcIdx + 3].getX());
    c = base[arcIdx + 1].getX();
    d = base[arcIdx + 2].getX();
    a = (base[arcIdx + 0].getX() + c) / 2;
    base[arcIdx + 1].setX(a);
    b = (base[arcIdx + 3].getX() + d) / 2;
    base[arcIdx + 5].setX(b);
    c = (c + d) / 2;
    a = (a + c) / 2;
    base[arcIdx + 2].setX(a);
    b = (b + c) / 2;
    base[arcIdx + 4].setX(b);
    base[arcIdx + 3].setX((a + b) / 2);
    base[arcIdx + 6].setY(base[arcIdx + 3].getY());
    c = base[arcIdx + 1].getY();
    d = base[arcIdx + 2].getY();
    a = (base[arcIdx + 0].getY() + c) / 2;
    base[arcIdx + 1].setY(a);
    b = (base[arcIdx + 3].getY() + d) / 2;
    base[arcIdx + 5].setY(b);
    c = (c + d) / 2;
    a = (a + c) / 2;
    base[arcIdx + 2].setY(a);
    b = (b + c) / 2;
    base[arcIdx + 4].setY(b);
    base[arcIdx + 3].setY((a + b) / 2);
  }

  /* =====================================================================
   *    gray_render_cubic
   *
   * =====================================================================
   */
  private void gray_render_cubic(FTVectorRec control1, FTVectorRec control2, FTVectorRec to) {
    FTVectorRec[] arc;
    int arcIdx = 0;
    int min;
    int max;
    int y;
    boolean noSplit = false;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_cubic");
    arc = bez_stack;
    arc[arcIdx + 0].setX(RasterUtil.UPSCALE(to.getX()));
    arc[arcIdx + 0].setY(RasterUtil.UPSCALE(to.getY()));
    arc[arcIdx + 1].setX(RasterUtil.UPSCALE(control2.getX()));
    arc[arcIdx + 1].setY(RasterUtil.UPSCALE(control2.getY()));
    arc[arcIdx + 2].setX(RasterUtil.UPSCALE(control1.getX()));
    arc[arcIdx + 2].setY(RasterUtil.UPSCALE(control1.getY()));
    arc[arcIdx + 3].setX(this.x);
    arc[arcIdx + 3].setY(this.y);
      /* Short-cut the arc that crosses the current band. */
    min = max = arc[arcIdx + 0].getY();
    y = arc[arcIdx + 1].getY();
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    y = arc[arcIdx + 2].getY();
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    y = arc[arcIdx + 3].getY();
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    if (RasterUtil.TRUNC(min) >= max_ey || RasterUtil.TRUNC(max) < min_ey) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_cubic call gray_render_line"));
      gray_render_line(arc[arcIdx].getX(), arc[arcIdx].getY());
      if (arc == bez_stack) {
        return;
      }
      arcIdx -= 3;
    }
    for (;;) {
        /* Decide whether to split or draw. See `Rapid Termination          */
        /* Evaluation for Recursive Subdivision of Bezier Curves' by Thomas */
        /* F. Hain, at                                                      */
        /* http://www.cis.southalabama.edu/~hain/general/Publications/Bezier/Camera-ready%20CISST02%202.pdf */

      noSplit = false;
      {
        int dx;
        int dy;
        int dx_;
        int dy_;
        int dx1;
        int dy1;
        int dx2;
        int dy2;
        int L;
        int s;
        int s_limit;

          /* dx and dy are x and y components of the P0-P3 chord vector. */
        dx = arc[arcIdx + 3].getX() - arc[arcIdx].getX();
        dy = arc[arcIdx + 3].getY() - arc[arcIdx].getY();
          /* L is an (under)estimate of the Euclidean distance P0-P3.       */
          /*                                                                */
          /* If dx >= dy, then r = sqrt(dx^2 + dy^2) can be overestimated   */
          /* with least maximum error by                                    */
          /*                                                                */
          /*   r_upperbound = dx + (sqrt(2) - 1) * dy  ,                    */
          /*                                                                */
          /* where sqrt(2) - 1 can be (over)estimated by 107/256, giving an */
          /* error of no more than 8.4%.                                    */
          /*                                                                */
          /* Similarly, some elementary calculus shows that r can be        */
          /* underestimated with least maximum error by                     */
          /*                                                                */
          /*   r_lowerbound = sqrt(2 + sqrt(2)) / 2 * dx                    */
          /*                  + sqrt(2 - sqrt(2)) / 2 * dy  .               */
          /*                                                                */
          /* 236/256 and 97/256 are (under)estimates of the two algebraic   */
          /* numbers, giving an error of no more than 8.1%.                 */
        dx_ = FTCalc.FT_ABS(dx);
        dy_ = FTCalc.FT_ABS(dy);
          /* This is the same as                     */
          /*                                         */
          /*   L = ( 236 * FT_MAX( dx_, dy_ )        */
          /*       + 97 * FT_MIN( dx_, dy_ ) ) >> 8; */
        L = (dx_ > dy_ ? 236 * dx_ +  97 * dy_ :  97 * dx_ + 236 * dy_) >> 8;
          /* Avoid possible arithmetic overflow below by splitting. */
        if (L > 32767) {
          gray_split_cubic(arc, arcIdx);
          arcIdx += 3;
          continue;
        }
          /* Max deviation may be as much as (s/L) * 3/4 (if Hain's v = 1). */
        s_limit = L * (RasterUtil.ONE_PIXEL() / 6);
          /* s is L * the perpendicular distance from P1 to the line P0-P3. */
        dx1 = arc[arcIdx + 1].getX() - arc[arcIdx].getX();
        dy1 = arc[arcIdx + 1].getY() - arc[arcIdx].getY();
        s = FTCalc.FT_ABS(dy * dx1 - dx * dy1);
        if (s > s_limit) {
          gray_split_cubic(arc, arcIdx);
          arcIdx += 3;
          continue;
        }
          /* s is L * the perpendicular distance from P2 to the line P0-P3. */
        dx2 = arc[arcIdx + 2].getX() - arc[arcIdx].getX();
        dy2 = arc[arcIdx + 2].getY() - arc[arcIdx].getY();
        s = FTCalc.FT_ABS(dy * dx2 - dx * dy2);
        if (s > s_limit) {
          gray_split_cubic(arc, arcIdx);
          arcIdx += 3;
          continue;
        }
          /* Split super curvy segments where the off points are so far
             from the chord that the angles P0-P1-P3 or P0-P2-P3 become
             acute as detected by appropriate dot products. */
        if (dx1 * (dx1 - dx) + dy1 * (dy1 - dy) > 0 ||
            dx2 * (dx2 - dx) + dy2 * (dy2 - dy) > 0) {
          gray_split_cubic(arc, arcIdx);
          arcIdx += 3;
          continue;
        }
          /* No reason to split. */
        noSplit = true;
      }
      if (! noSplit) {
        gray_split_cubic(arc, arcIdx);
        arcIdx += 3;
        continue;
      }
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_cubic 2 call gray_render_line"));
      gray_render_line(arc[arcIdx + 0].getX(), arc[arcIdx + 0].getY());
      if (arc == bez_stack) {
        return;
      }
      arcIdx -= 3;
      bez_stack = arc;
    }
  }

  /* =====================================================================
   *    gray_compute_cbox
   *
   * =====================================================================
   */
  public void gray_compute_cbox() {
    FTVectorRec vec;
    int vecIdx = 0;
    int limit = outline.getN_points();

    Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_compute_cbox"));
    if (outline.getN_points() <= 0) {
      min_ex = 0;
      max_ex = 0;
      min_ey = 0;
      max_ey = 0;
      return;
    }
    min_ex = outline.getPoints()[vecIdx].getX();
    max_ex = outline.getPoints()[vecIdx].getX();
    min_ey = outline.getPoints()[vecIdx].getY();
    max_ey = outline.getPoints()[vecIdx].getY();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox01: %d %d %d %d", min_ex, min_ey, max_ex, max_ey));
    vecIdx++;
    for ( ; vecIdx < limit; vecIdx++) {
      vec = outline.getPoints()[vecIdx];
      int x = vec.getX();
      int y = vec.getY();

      if (x < min_ex) {
        min_ex = x;
      }
      if (x > max_ex) {
        max_ex = x;
      }
      if (y < min_ey) {
        min_ey = y;
      }
      if (y > max_ey) {
        max_ey = y;
      }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox11: %d %d", x, y));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox12: %d %d %d %d", min_ex, min_ey, max_ex, max_ey));
    }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox2: %d %d %d %d", min_ex, min_ey, max_ex, max_ey));
      /* truncate the bounding box to integer pixels */
    min_ex = min_ex >> 6;
    min_ey = min_ey >> 6;
    max_ex = (max_ex + 63) >> 6;
    max_ey = (max_ey + 63) >> 6;
    Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_compute_cbox end: min_ex: %d min_ey: %d max_ex: %d max_ey: %d", min_ex, min_ey, max_ex, max_ey));
  }

  /* ==================== getEx ================================== */
  public int getEx() {
    return ex;
  }

  /* ==================== setEx ================================== */
  public void setEx(int ex) {
    this.ex = ex;
  }

  /* ==================== getEy ================================== */
  public int getEy() {
    return ey;
  }

  /* ==================== setEy ================================== */
  public void setEy(int ey) {
    this.ey = ey;
  }

  /* ==================== getMin_ex ================================== */
  public int getMin_ex() {
    return min_ex;
  }

  /* ==================== setMin_ex ================================== */
  public void setMin_ex(int min_ex) {
    this.min_ex = min_ex;
  }

  /* ==================== getMax_ex ================================== */
  public int getMax_ex() {
    return max_ex;
  }

  /* ==================== setMax_ex ================================== */
  public void setMax_ex(int max_ex) {
    this.max_ex = max_ex;
  }

  /* ==================== getMin_ey ================================== */
  public int getMin_ey() {
    return min_ey;
  }

  /* ==================== setMin_ey ================================== */
  public void setMin_ey(int min_ey) {
    this.min_ey = min_ey;
  }

  /* ==================== getMax_ey ================================== */
  public int getMax_ey() {
    return max_ey;
  }

  /* ==================== setMax_ey ================================== */
  public void setMax_ey(int max_ey) {
    this.max_ey = max_ey;
  }

  /* ==================== getCount_ex ================================== */
  public int getCount_ex() {
    return count_ex;
  }

  /* ==================== setCount_ex ================================== */
  public void setCount_ex(int count_ex) {
    this.count_ex = count_ex;
  }

  /* ==================== getCount_ey ================================== */
  public int getCount_ey() {
    return count_ey;
  }

  /* ==================== setCount_ey ================================== */
  public void setCount_ey(int count_ey) {
    this.count_ey = count_ey;
  }

  /* ==================== getArea ================================== */
  public int getArea() {
    return area;
  }

  /* ==================== setArea ================================== */
  public void setArea(int area) {
    this.area = area;
  }

  /* ==================== getCover ================================== */
  public int getCover() {
    return cover;
  }

  /* ==================== setCover ================================== */
  public void setCover(int cover) {
    this.cover = cover;
  }

  /* ==================== isInvalid ================================== */
  public boolean isInvalid() {
    return invalid;
  }

  /* ==================== setInvalid ================================== */
  public void setInvalid(boolean invalid) {
    this.invalid = invalid;
  }

  /* ==================== getCells ================================== */
  public TCellRec[] getCells() {
    return cells;
  }

  /* ==================== getCell ================================== */
  public TCellRec getCell(int idx) {
    return cells[idx];
  }

  /* ==================== setCells ================================== */
  public void setCells(TCellRec[] cells) {
    this.cells = cells;
  }

  /* ==================== setCell ================================== */
  public void setCell(int idx, TCellRec cell) {
    this.cells[idx] = cell;
  }

  /* ==================== getMax_cells ================================== */
  public int getMax_cells() {
    return max_cells;
  }

  /* ==================== setMax_cells ================================== */
  public void setMax_cells(int max_cells) {
    this.max_cells = max_cells;
  }

  /* ==================== getNum_cells ================================== */
  public int getNum_cells() {
    return num_cells;
  }

  /* ==================== setNum_cells ================================== */
  public void setNum_cells(int num_cells) {
    this.num_cells = num_cells;
  }

  /* ==================== getCx ================================== */
  public int getCx() {
    return cx;
  }

  /* ==================== setCx ================================== */
  public void setCx(int cx) {
    this.cx = cx;
  }

  /* ==================== getCy ================================== */
  public int getCy() {
    return cy;
  }

  /* ==================== setCy ================================== */
  public void setCy(int cy) {
    this.cy = cy;
  }

  /* ==================== getX ================================== */
  public int getX() {
    return x;
  }

  /* ==================== setX ================================== */
  public void setX(int x) {
    this.x = x;
  }

  /* ==================== getY ================================== */
  public int getY() {
    return y;
  }

  /* ==================== setY ================================== */
  public void setY(int y) {
    this.y = y;
  }

  /* ==================== getLast_ey ================================== */
  public int getLast_ey() {
    return last_ey;
  }

  /* ==================== setLast_ey ================================== */
  public void setLast_ey(int last_ey) {
    this.last_ey = last_ey;
  }

  /* ==================== getBez_stack ================================== */
  public FTVectorRec[] getBez_stack() {
    return bez_stack;
  }

  /* ==================== setBez_stack ================================== */
  public void setBez_stack(FTVectorRec[] bez_stack) {
    this.bez_stack = bez_stack;
  }

  /* ==================== getLev_stack ================================== */
  public int[] getLev_stack() {
    return lev_stack;
  }

  /* ==================== setLev_stack ================================== */
  public void setLev_stack(int[] lev_stack) {
    this.lev_stack = lev_stack;
  }

  /* ==================== getOutline ================================== */
  public FTOutlineRec getOutline() {
    return outline;
  }

  /* ==================== setOutline ================================== */
  public void setOutline(FTOutlineRec outline) {
    this.outline = outline;
  }

  /* ==================== getTarget ================================== */
  public FTBitmapRec getTarget() {
    return target;
  }

  /* ==================== setTarget ================================== */
  public void setTarget(FTBitmapRec target) {
    this.target = target;
  }

  /* ==================== getClip_box ================================== */
  public FTBBoxRec getClip_box() {
    return clip_box;
  }

  /* ==================== setClip_box ================================== */
  public void setClip_box(FTBBoxRec clip_box) {
    this.clip_box = clip_box;
  }

  /* ==================== getGray_spans ================================== */
  public FTSpanRec[] getGray_spans() {
    return gray_spans;
  }

  /* ==================== getGray_span ================================== */
  public FTSpanRec getGray_span(int idx) {
    return gray_spans[idx];
  }

  /* ==================== setGray_spans ================================== */
  public void setGray_spans(FTSpanRec[] gray_spans) {
    this.gray_spans = gray_spans;
  }

  /* ==================== setGray_span ================================== */
  public void setGray_span(int idx, FTSpanRec gray_span) {
    this.gray_spans[idx] = gray_span;
  }

  /* ==================== getNum_gray_spans ================================== */
  public int getNum_gray_spans() {
    return num_gray_spans;
  }

  /* ==================== setNum_gray_spans ================================== */
  public void setNum_gray_spans(int num_gray_spans) {
    this.num_gray_spans = num_gray_spans;
  }

  /* ==================== getRender_span_data ================================== */
  public Object getRender_span_data() {
    return render_span_data;
  }

  /* ==================== setRender_span_data ================================== */
  public void setRender_span_data(Object render_span_data) {
    this.render_span_data = render_span_data;
  }

  /* ==================== getSpan_y ================================== */
  public int getSpan_y() {
    return span_y;
  }

  /* ==================== setSpan_y ================================== */
  public void setSpan_y(int span_y) {
    this.span_y = span_y;
  }

  /* ==================== getBand_size ================================== */
  public int getBand_size() {
    return band_size;
  }

  /* ==================== setBand_size ================================== */
  public void setBand_size(int band_size) {
    this.band_size = band_size;
  }

  /* ==================== getBand_shoot ================================== */
  public int getBand_shoot() {
    return band_shoot;
  }

  /* ==================== setBand_shoot ================================== */
  public void setBand_shoot(int band_shoot) {
    this.band_shoot = band_shoot;
  }

  /* ==================== getBuffer ================================== */
  public Object getBuffer() {
    return buffer;
  }

  /* ==================== setBuffer ================================== */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  /* ==================== getBuffer_size ================================== */
  public int getBuffer_size() {
    return buffer_size;
  }

  /* ==================== setBuffer_size ================================== */
  public void setBuffer_size(int buffer_size) {
    this.buffer_size = buffer_size;
  }

  /* ==================== getYcells ================================== */
  public TCellRec[] getYcells() {
    return ycells;
  }

  /* ==================== getYcell ================================== */
  public TCellRec getYcell(int cell_idx) {
    if (cell_idx < 0) {
      Log.e(TAG, "bad cell_idx: "+cell_idx);
    }
    return ycells[cell_idx];
  }

  /* ==================== setYcells ================================== */
  public void setYcells(TCellRec[] ycells) {
    this.ycells = ycells;
  }

  /* ==================== setYcell ================================== */
  public void setYcell(int idx, TCellRec ycell) {
    this.ycells[idx] = ycell;
  }

  /* ==================== getYcount ================================== */
  public int getYcount() {
    return ycount;
  }

  /* ==================== setYcount ================================== */
  public void setYcount(int ycount) {
    this.ycount = ycount;
  }

}