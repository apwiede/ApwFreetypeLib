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

import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.RasterUtil;

public class grayTWorkerSweepRec extends grayTWorkerDecomposeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTWorkerSweepRec";

  /* ==================== grayTWorkerSweepRec ================================== */
  public grayTWorkerSweepRec() {
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
   *    gray_hline
   *
   * =====================================================================
   */
  public void gray_hline(int x, int y, int area, int acount) {
    Debug(0, DebugTag.DBG_SWEEP, TAG, String.format("gray_hline: x: %d, y: %d, area: 0x%08x, acount: %d", x, y, area, acount));
    int coverage = 0;
    int spanIdx = 0;

      /* compute the coverage line's coverage, depending on the    */
      /* outline fill rule                                         */
      /*                                                           */
      /* the coverage percentage is area/(PIXEL_BITS*PIXEL_BITS*2) */
      /*                                                           */
    coverage = (area >> (FTGrayOutlineClass.PIXEL_BITS * 2 + 1 - 8));
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
    Debug(0, DebugTag.DBG_SWEEP, TAG, String.format("x: %d, y: %d, coverage: %x", x, y, coverage));
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
    Debug(0, DebugTag.DBG_SWEEP, TAG, "gray_hline END 2");
  }

  /* =====================================================================
   *    gray_sweep
   *
   * =====================================================================
   */
  public void gray_sweep(FTBitmapRec target) {
    Debug(0, DebugTag.DBG_SWEEP, TAG, "gray_sweep");
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

        Debug(0, DebugTag.DBG_SWEEP, TAG, String.format("cell->next: %d", cell.getNext() == null ? -1 : cell.getNext().getSelf_idx()));
        Debug(0, DebugTag.DBG_SWEEP, TAG, String.format("gray_sweep 1: cell.x: %d x: %d,  yindex: %d, cover: %x", cell.getX(), x, yindex, cover));
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

}