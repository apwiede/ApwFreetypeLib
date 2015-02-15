/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2015
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri (dot) de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftraster;

import org.apwtcl.apwfreetypelib.aftbase.FTBBoxRec;
import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.RasterUtil;

public class grayTWorkerRec extends grayTWorkerSweepRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTWorkerRec";

  /* ==================== grayTWorkerRec ================================== */
  public grayTWorkerRec() {
  }

  /* ==================== mySelf ================================== */
  public String mySelf() {
    return TAG + "!" + id + "!";
  }

  /* ==================== toString ===================================== */
  public String toString() {
    return mySelf() + "!";
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf() + "\n");
    return super.toDebugString()+str.toString();
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
    error = outline.FTOutlineDecompose(this);
    gray_record_cell();
//      } else {
//        error = FT_THROW( Memory_Overflow );
//      }
    return error;
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
    num_bands = ((max_ey - min_ey) / band_size);
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
Debug(0, DebugTag.DBG_RENDER, TAG, "num_bands: n: "+n+" "+num_bands);
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

}