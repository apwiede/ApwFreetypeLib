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
  /*    RasterUtil.                                                         */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTOutlineFuncs;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.RasterUtil;

public class FTGrayOutlineFuncsClass extends FTOutlineFuncs {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTGrayOutlineFuncsClass";

  public static int PIXEL_BITS = 8;

  /* ==================== FTGrayOutlineFuncsClass ================================== */
  public FTGrayOutlineFuncsClass() {
    oid++;
    id = oid;
    shift = 0;
    delta = 0;
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
   *    gray_move_to
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_move_to(FTVectorRec to, grayTWorkerRec worker) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int x;
    int y;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_move_to: to.x: %d, to.y: %d", to.x, to.y));

      /* record current cell, if any */
    gray_record_cell(worker);
      /* start to a new position */
    x = RasterUtil.UPSCALE(to.x);
    y = RasterUtil.UPSCALE(to.y);
    gray_start_cell(worker, RasterUtil.TRUNC(x), RasterUtil.TRUNC(y));
    worker.setX(x);
    worker.setY(y);
    return error;
  }

  /* =====================================================================
   *    gray_line_to
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_line_to(FTVectorRec to, grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_line_to: to.x: %d, to.y: %d", to.x, to.y));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_line_to call gray_render_line");
    gray_render_line(worker, RasterUtil.UPSCALE(to.x), RasterUtil.UPSCALE(to.y));
    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_line_to after call gray_render_line");
    return error;
  }

  /* =====================================================================
   *    gray_conic_to
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_conic_to(FTVectorRec control, FTVectorRec to, grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_conic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_conic(control, to, worker);
    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_conic_to end");
    return error;
  }

  /* =====================================================================
   *    gray_cubic_to
   *
   * =====================================================================
   */
  private FTError.ErrorTag gray_cubic_to(FTVectorRec control1, FTVectorRec control2, FTVectorRec to, grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_cubic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_cubic(control1, control2, to, worker);
    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_cubic_to end");
    return error;
  }

  /* =====================================================================
   *    gray_find_cell
   *
   * =====================================================================
   */
  private TCellRec gray_find_cell(grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_find_cell: ras.ex: %d, ras.ey: %d, ras.count_ex: %d, ras.num_cells: %d", worker.getEx(), worker.getEy(), worker.getCount_ex(), worker.getNum_cells()));
    int pcellIdx;
    int cellIdx;
    TCellRec cell = null;
    TCellRec pcell = null;
    int x = worker.getEx();
    boolean useYcells = true;
    boolean isYcell = true;

    if (x > worker.getCount_ex()) {
      x = worker.getCount_ex();
    }
    for(int j = 0; j < worker.getYcount(); j++) {
      int a = worker.getYcell(j) == null ? -1 : worker.getYcell(j).area;
      int c = worker.getYcell(j) == null ? -1 : worker.getYcell(j).cover;
      StringBuffer str = new StringBuffer("");
      str.append(String.format("ycells j: %2d, idx: %2d, x: %4d, a: %6x, c: %6x", j,
          worker.getYcell(j) == null ? -1 : worker.getYcell(j).self_idx,
          worker.getYcell(j) == null ? -1 : worker.getYcell(j).x,
          a & 0xFFFFFF, c & 0xFFFFFF));
      cell = worker.getYcell(j);
      while( cell != null) {
        cell = cell.next;
        if (cell != null) {
          str.append(String.format("\n              idx: %2d, x: %4d, a: %6x, c: %6x",
              cell.self_idx, cell.x, cell.area & 0xFFFFFF, cell.cover & 0xFFFFFF));
        }
      }
Debug(0, DebugTag.DBG_RENDER, TAG, str.toString());
    }

    pcellIdx = worker.getEy();
    cellIdx = -1;
    for (;;) {
      if (cellIdx == -1) {
        cell = worker.getYcell(pcellIdx);
      } else {
        cell = cell.next;
      }
      if (cell == null || cell.x > x) {
        break;
      }
      if (cell.x == x) {
        return cell;
      }
      if (!useYcells) {
        isYcell = false;
      }
      useYcells = false;
      cellIdx = cell.self_idx;
    }
    if (worker.getNum_cells() >= worker.getMax_cells()) {
//FIXME!!        ft_longjmp( worker.jump_buffer, 1 );
      Log.e(TAG, "out of worker.cells: " + worker.getNum_cells() + "!" + worker.getMax_cells());
    }
    cell = worker.getCell(worker.getNum_cells());
    worker.setNum_cells(worker.getNum_cells() + 1);
    cell.self_idx = worker.getNum_cells() - 1;
    cell.x = x;
    cell.area = 0;
    cell.cover = 0;
    if (useYcells) {
      cell.next = worker.getYcell(pcellIdx);
//System.out.println(String.format("pcellIdx: %d",  pcellIdx));
      worker.setYcell(pcellIdx, cell);
    } else {
      if (isYcell) {
        cell.next = worker.getYcell(pcellIdx).next;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        worker.getYcell(pcellIdx).next = cell;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("isYcell: cell idx: %d cell next idx: %d ", cell.self_idx, cell.next == null ? -1 : cell.next.self_idx));
      } else {
        cell.next = worker.getCell(cellIdx).next;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        worker.getCell(cellIdx).next = cell;
      }
    }
    return cell;
  }

  /* =====================================================================
   *    gray_record_cell
   *
   * =====================================================================
   */
  public void gray_record_cell(grayTWorkerRec worker) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_record_cell: ras.invalid: %b, ras.area: %x, ras.cover: %x\n", worker.isInvalid(), worker.getArea(), worker.getCover()));

    if (!worker.isInvalid() && (worker.getArea() | worker.getCover()) != 0) {
      TCellRec cell = gray_find_cell(worker);

      cell.area  += worker.getArea();
      cell.cover += worker.getCover();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_record_cell2: cell.area: %x, cell.cover: %x worker.area: %x, worker.cover: %x\n", cell.area, cell.cover, worker.getArea(), worker.getCover()));
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
  private void gray_set_cell(grayTWorkerRec worker, int ex, int ey) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell: ex: %d, ey: %d, invalid: %b, ras.ex: %d, ras.ey: %d", ex, ey, worker.isInvalid(), worker.getEx(), worker.getEy()));
      /* All cells that are on the left of the clipping region go to the */
      /* min_ex - 1 horizontal position.                                 */
    ey -= worker.getMin_ey();
    if (ex > worker.getMax_ex()) {
      ex = worker.getMax_ex();
    }
    ex -= worker.getMin_ex();
    if (ex < 0) {
      ex = -1;
    }
      /* are we moving to a different cell ? */
    if (ex != worker.getEx() || ey != worker.getEy()) {
        /* record the current one if it is valid */
      if (!worker.isInvalid()) {
        gray_record_cell(worker);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell after call gray_record_cell"));
      }
      worker.setArea(0);
      worker.setCover(0);
    }
    worker.setEx(ex);
    worker.setEy(ey);
    worker.setInvalid((ey >= worker.getCount_ey()) || ex >= worker.getCount_ex());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_set_cell end"));
  }

  /* =====================================================================
   *    gray_start_cell
   *
   * =====================================================================
   */
  private void gray_start_cell(grayTWorkerRec worker, int ex, int ey) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_start_cell: ex: %d, ey: %d", ex, ey));

    if (ex > worker.getMax_ex()) {
      ex = worker.getMax_ex();
    }
    if (ex < worker.getMin_ex()) {
      ex = worker.getMin_ex() - 1;
    }
    worker.setArea(0);
    worker.setCover(0);
    worker.setEx(ex - worker.getMin_ex());
    worker.setEy(ey - worker.getMin_ey());
    worker.setLast_ey(RasterUtil.SUBPIXELS(ey));
    worker.setInvalid(false);
    gray_set_cell(worker, ex, ey);
  }


  /* =====================================================================
   *    gray_render_scanline
   *
   * =====================================================================
   */
  private void gray_render_scanline(grayTWorkerRec worker, int ey, int x1, int y1, int x2, int y2) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_scanline");
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
      gray_set_cell(worker, ex2, ey);
      return;
    }
      /* everything is located in a single cell.  That is easy! */
      /*                                                        */
    if (ex1 == ex2) {
      delta = y2 - y1;
      worker.setArea(worker.getArea() + ((fx1 + fx2) * delta));
      worker.setCover(worker.getCover() + delta);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("return ex1 == ex2: %d", ex2));
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
    if (mod < 0) {
      delta--;
      mod += dx;
    }
    worker.setArea(worker.getArea() + ((fx1 + first) * delta));
    worker.setCover(worker.getCover() + delta);
    ex1 += incr;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("call gray_set_cell2 ex1: %d, ey: %d", ex1, ey));
    gray_set_cell(worker, ex1, ey);
    y1  += delta;
    if (ex1 != ex2) {
      int lift;
      int rem;

Debug(0, DebugTag.DBG_RENDER, TAG, String.format("return 2 ex1 == ex2: %d", ex2));
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
        worker.setArea(worker.getArea() + (RasterUtil.ONE_PIXEL() * delta));
        worker.setCover(worker.getCover() + delta);
        y1 += delta;
        ex1 += incr;
        gray_set_cell(worker, ex1, ey);
      }
    }
    delta = y2 - y1;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("return end delta: %d", delta));
    worker.setArea(worker.getArea()  + ((fx2 + RasterUtil.ONE_PIXEL() - first) * delta));
    worker.setCover(worker.getCover() + delta);
  }

  /* =====================================================================
   *    gray_render_line
   *
   * =====================================================================
   */
  private void gray_render_line(grayTWorkerRec worker, int to_x, int to_y) {
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
    ey1 = RasterUtil.TRUNC(worker.getLast_ey());
    ey2 = RasterUtil.TRUNC(to_y);     /* if (ey2 >= ras.max_ey) ey2 = ras.max_ey-1; */
    fy1 = (worker.getY() - worker.getLast_ey());
    fy2 = (to_y - RasterUtil.SUBPIXELS(ey2));
    dx = to_x - worker.getX();
    dy = to_y - worker.getY();
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
      if (min >= worker.getMax_ey() || max < worker.getMin_ey()) {
        worker.setX(to_x);
        worker.setY(to_y);
        worker.setLast_ey(RasterUtil.SUBPIXELS(ey2));
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line return 1");
        return;
      }
    }
      /* everything is on a single scanline */
    if (ey1 == ey2) {
      gray_render_scanline(worker, ey1, worker.getX(), fy1, to_x, fy2);
      worker.setX(to_x);
      worker.setY(to_y);
      worker.setLast_ey(RasterUtil.SUBPIXELS(ey2));
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line return 2");
      return;
    }
      /* vertical line - avoid calling gray_render_scanline */
    incr = 1;
    if (dx == 0) {
      int ex = RasterUtil.TRUNC(worker.getX());
      int two_fx = ((worker.getX() - RasterUtil.SUBPIXELS(ex)) << 1);
      int area;

      first = RasterUtil.ONE_PIXEL();
      if (dy < 0) {
        first = 0;
        incr  = -1;
      }
      delta = (int)(first - fy1);
      worker.setArea(worker.getArea()  + two_fx * delta);
      worker.setCover(worker.getCover() + delta);
      ey1 += incr;
      gray_set_cell(worker, ex, ey1);
      delta = (first + first - RasterUtil.ONE_PIXEL());
      area = two_fx * delta;
      while (ey1 != ey2) {
        worker.setArea(worker.getArea() + area);
        worker.setCover(worker.getCover() + delta);
        ey1 += incr;
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_set_cell while");
        gray_set_cell(worker, ex, ey1);
      }
      delta = (fy2 - RasterUtil.ONE_PIXEL() + first);
      worker.setArea(worker.getArea() + two_fx * delta);
      worker.setCover(worker.getCover() + delta);
      worker.setX(to_x);
      worker.setY(to_y);
      worker.setLast_ey(RasterUtil.SUBPIXELS(ey2));
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
    delta = (int)(p / dy);
    mod = (int)(p % dy);
    if (mod < 0) {
      delta--;
      mod += dy;
    }
    x = worker.getX() + delta;
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_render_scanline 1");
    gray_render_scanline(worker, ey1, worker.getX(), fy1, x, first);
    ey1 += incr;
    gray_set_cell(worker, RasterUtil.TRUNC(x), ey1);
    if (ey1 != ey2) {
      p = RasterUtil.ONE_PIXEL() * dx;
      lift  = (p / dy);
      rem   = (p % dy);
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
        gray_render_scanline(worker, ey1, x, RasterUtil.ONE_PIXEL() - first, x2, first);
        x = x2;
        ey1 += incr;
        gray_set_cell(worker, RasterUtil.TRUNC(x), ey1);
      }
    }
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line call gray_render_scanline 3");
    gray_render_scanline(worker, ey1, x, RasterUtil.ONE_PIXEL() - first, to_x, fy2);
    worker.setX(to_x);
    worker.setY(to_y);
    worker.setLast_ey(RasterUtil.SUBPIXELS(ey2));
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_line end");
  }

  /* =====================================================================
   *    gray_split_conic
   *
   * =====================================================================
   */
  private void gray_split_conic(FTReference<FTVectorRec[]> base_ref, int arcIdx) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_split_conic");
    FTVectorRec[] base = base_ref.Get();
    int a;
    int b;

    base[arcIdx + 4].x = base[arcIdx + 2].x;
    b = base[arcIdx + 1].x;
    a = base[arcIdx + 3].x = (base[arcIdx + 2].x + b) / 2;
    b = base[arcIdx + 1].x = (base[arcIdx + 0].x + b) / 2;
    base[arcIdx + 2].x = (a + b) / 2;
    base[arcIdx + 4].y = base[arcIdx + 2].y;
    b = base[arcIdx + 1].y;
    a = base[arcIdx + 3].y = (base[arcIdx + 2].y + b) / 2;
    b = base[arcIdx + 1].y = (base[arcIdx + 0].y + b) / 2;
    base[arcIdx + 2].y = (a + b) / 2;
    base_ref.Set(base);
  }

  /* =====================================================================
   *    gray_render_conic
   *
   * =====================================================================
   */
  private void gray_render_conic(FTVectorRec control, FTVectorRec to, grayTWorkerRec worker) {
    int dx;
    int dy;
    int min;
    int max;
    int y;
    int top;
    int level;
    int[] levels;
    FTVectorRec[] arc;
    FTReference<FTVectorRec[]> arc_ref = new FTReference<FTVectorRec[]>();
    int arcIdx = 0;
    boolean doDraw = false;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_conic");
    levels = worker.getLev_stack();
    arc = worker.getBez_stack();
    arc[arcIdx + 0].x = RasterUtil.UPSCALE(to.x);
    arc[arcIdx + 0].y = RasterUtil.UPSCALE(to.y);
    arc[arcIdx + 1].x = RasterUtil.UPSCALE(control.x);
    arc[arcIdx + 1].y = RasterUtil.UPSCALE(control.y);
    arc[arcIdx + 2].x = worker.getX();
    arc[arcIdx + 2].y = worker.getY();
    top = 0;
    dx = FTCalc.FT_ABS(arc[arcIdx + 2].x + arc[arcIdx + 0].x - 2 * arc[arcIdx + 1].x);
    dy = FTCalc.FT_ABS(arc[arcIdx + 2].y + arc[arcIdx + 0].y - 2 * arc[arcIdx + 1].y);
    if (dx < dy) {
      dx = dy;
    }
    if (dx < RasterUtil.ONE_PIXEL() / 4) {
      doDraw = true;
    }
    if (!doDraw) {
        /* short-cut the arc that crosses the current band */
      min = max = arc[arcIdx + 0].y;
      y = arc[arcIdx + 1].y;
      if (y < min) {
        min = y;
      }
      if (y > max) {
        max = y;
      }
      y = arc[arcIdx + 2].y;
      if (y < min) {
        min = y;
      }
      if (y > max) {
        max = y;
      }
      if (RasterUtil.TRUNC(min) >= worker.getMax_ey() || RasterUtil.TRUNC(max) < worker.getMin_ey()) {
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
          arc_ref.Set(arc);
          gray_split_conic(arc_ref, arcIdx);
          arc = arc_ref.Get();
          arcIdx += 2;
          top++;
          levels[top] = levels[top - 1] = level - 1;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic !doDraw continue level: %d", level));
          continue;
        }
      }
      doDraw = false;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_conic call gray_render_line top: %d", top));

      gray_render_line(worker, arc[arcIdx + 0].x, arc[arcIdx + 0].y);
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
  private void gray_split_cubic(FTReference<FTVectorRec[]> base_ref, int arcIdx) {
Debug(0, DebugTag.DBG_RENDER, TAG, "gray_split_cubic");
    FTVectorRec[] base = base_ref.Get();
    int a;
    int b;
    int c;
    int d;

    base[arcIdx + 6].x = base[arcIdx + 3].x;
    c = base[arcIdx + 1].x;
    d = base[arcIdx + 2].x;
    base[arcIdx + 1].x = a = (base[arcIdx + 0].x + c) / 2;
    base[arcIdx + 5].x = b = (base[arcIdx + 3].x + d) / 2;
    c = (c + d) / 2;
    base[arcIdx + 2].x = a = (a + c) / 2;
    base[arcIdx + 4].x = b = (b + c) / 2;
    base[arcIdx + 3].x = (a + b) / 2;
    base[arcIdx + 6].y = base[arcIdx + 3].y;
    c = base[arcIdx + 1].y;
    d = base[arcIdx + 2].y;
    base[arcIdx + 1].y = a = (base[arcIdx + 0].y + c) / 2;
    base[arcIdx + 5].y = b = (base[arcIdx + 3].y + d) / 2;
    c = (c + d) / 2;
    base[arcIdx + 2].y = a = (a + c) / 2;
    base[arcIdx + 4].y = b = (b + c) / 2;
    base[arcIdx + 3].y = (a + b) / 2;
    base_ref.Set(base);
  }

  /* =====================================================================
   *    gray_render_cubic
   *
   * =====================================================================
   */
  private void gray_render_cubic(FTVectorRec control1, FTVectorRec control2, FTVectorRec to, grayTWorkerRec worker) {
    FTVectorRec[] arc;
    FTReference<FTVectorRec[]> arc_ref = new FTReference<FTVectorRec[]>();
    int arcIdx = 0;
    int min;
    int max;
    int y;
    boolean noSplit = false;

Debug(0, DebugTag.DBG_RENDER, TAG, "gray_render_cubic");
    arc = worker.getBez_stack();
    arc[arcIdx + 0].x = RasterUtil.UPSCALE(to.x);
    arc[arcIdx + 0].y = RasterUtil.UPSCALE(to.y);
    arc[arcIdx + 1].x = RasterUtil.UPSCALE(control2.x);
    arc[arcIdx + 1].y = RasterUtil.UPSCALE(control2.y);
    arc[arcIdx + 2].x = RasterUtil.UPSCALE(control1.x);
    arc[arcIdx + 2].y = RasterUtil.UPSCALE(control1.y);
    arc[arcIdx + 3].x = worker.getX();
    arc[arcIdx + 3].y = worker.getY();
      /* Short-cut the arc that crosses the current band. */
    min = max = arc[arcIdx + 0].y;
    y = arc[arcIdx + 1].y;
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    y = arc[arcIdx + 2].y;
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    y = arc[arcIdx + 3].y;
    if (y < min) {
      min = y;
    }
    if (y > max) {
      max = y;
    }
    if (RasterUtil.TRUNC(min) >= worker.getMax_ey() || RasterUtil.TRUNC(max) < worker.getMin_ey()) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_cubic call gray_render_line"));
      gray_render_line(worker, arc[arcIdx + 0].x, arc[arcIdx + 0].y);
      if (arc == worker.getBez_stack()) {
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
        dx = arc[arcIdx + 3].x - arc[arcIdx + 0].x;
        dy = arc[arcIdx + 3].y - arc[arcIdx + 0].y;
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
          arc_ref.Set(arc);
          gray_split_cubic(arc_ref, arcIdx);
          arc = arc_ref.Get();
          arcIdx += 3;
          continue;
        }
          /* Max deviation may be as much as (s/L) * 3/4 (if Hain's v = 1). */
        s_limit = L * (RasterUtil.ONE_PIXEL() / 6);
          /* s is L * the perpendicular distance from P1 to the line P0-P3. */
        dx1 = arc[arcIdx + 1].x - arc[arcIdx + 0].x;
        dy1 = arc[arcIdx + 1].y - arc[arcIdx + 0].y;
        s = FTCalc.FT_ABS(dy * dx1 - dx * dy1);
        if (s > s_limit) {
          arc_ref.Set(arc);
          gray_split_cubic(arc_ref, arcIdx);
          arc = arc_ref.Get();
          arcIdx += 3;
          continue;
        }
          /* s is L * the perpendicular distance from P2 to the line P0-P3. */
        dx2 = arc[arcIdx + 2].x - arc[arcIdx + 0].x;
        dy2 = arc[arcIdx + 2].y - arc[arcIdx + 0].y;
        s = FTCalc.FT_ABS(dy * dx2 - dx * dy2);
        if (s > s_limit) {
          arc_ref.Set(arc);
          gray_split_cubic(arc_ref, arcIdx);
          arc = arc_ref.Get();
          arcIdx += 3;
          continue;
        }
          /* Split super curvy segments where the off points are so far
             from the chord that the angles P0-P1-P3 or P0-P2-P3 become
             acute as detected by appropriate dot products. */
        if (dx1 * (dx1 - dx) + dy1 * (dy1 - dy) > 0 ||
            dx2 * (dx2 - dx) + dy2 * (dy2 - dy) > 0) {
          arc_ref.Set(arc);
          gray_split_cubic(arc_ref, arcIdx);
          arc = arc_ref.Get();
          arcIdx += 3;
          continue;
        }
          /* No reason to split. */
        noSplit = true;
      }
      if (! noSplit) {
        arc_ref.Set(arc);
        gray_split_cubic(arc_ref, arcIdx);
        arc = arc_ref.Get();
        arcIdx += 3;
        continue;
      }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_render_cubic 2 call gray_render_line"));
      gray_render_line(worker, arc[arcIdx + 0].x, arc[arcIdx + 0].y);
      if (arc == worker.getBez_stack()) {
        return;
      }
      arcIdx -= 3;
      worker.setBez_stack(arc);
    }
  }

  /* =====================================================================
   *    gray_compute_cbox
   *
   * =====================================================================
   */
  public void gray_compute_cbox(grayTWorkerRec worker) {
    FTOutlineRec outline = worker.getOutline();
    FTVectorRec vec;
    int vecIdx = 0;
    int limit = outline.getN_points();

Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_compute_cbox"));
    if (outline.getN_points() <= 0) {
      worker.setMin_ex(0);
      worker.setMax_ex(0);
      worker.setMin_ey(0);
      worker.setMax_ey(0);
      return;
    }
    worker.setMin_ex(outline.getPoints()[vecIdx].x);
    worker.setMax_ex(outline.getPoints()[vecIdx].x);
    worker.setMin_ey(outline.getPoints()[vecIdx].y);
    worker.setMax_ey(outline.getPoints()[vecIdx].y);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox01: %d %d %d %d", worker.getMin_ex(), worker.getMin_ey(), worker.getMax_ex(), worker.getMax_ey()));
    vecIdx++;
    for ( ; vecIdx < limit; vecIdx++) {
      vec = outline.getPoints()[vecIdx];
      int x = vec.x;
      int y = vec.y;

      if (x < worker.getMin_ex()) {
        worker.setMin_ex(x);
      }
      if (x > worker.getMax_ex()) {
        worker.setMax_ex(x);
      }
      if (y < worker.getMin_ey()) {
        worker.setMin_ey(y);
      }
      if (y > worker.getMax_ey()) {
        worker.setMax_ey(y);
      }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox11: %d %d", x, y));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox12: %d %d %d %d", worker.getMin_ex(), worker.getMin_ey(), worker.getMax_ex(), worker.getMax_ey()));
    }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("cbox2: %d %d %d %d", worker.getMin_ex(), worker.getMin_ey(), worker.getMax_ex(), worker.getMax_ey()));
      /* truncate the bounding box to integer pixels */
    worker.setMin_ex(worker.getMin_ex() >> 6);
    worker.setMin_ey(worker.getMin_ey() >> 6);
    worker.setMax_ex((worker.getMax_ex() + 63) >> 6);
    worker.setMax_ey((worker.getMax_ey() + 63) >> 6);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_comput_cbox end: %d %d %d %d", worker.getMin_ex(), worker.getMin_ey(), worker.getMax_ex(), worker.getMax_ey()));
    worker.setOutline(outline);
  }


  /* ==================== moveTo ===================================== */
  @Override
  public FTError.ErrorTag moveTo(FTVectorRec point, Object user) {
    return gray_move_to(point, (grayTWorkerRec)user);
  }

  /* ==================== lineTo ===================================== */
  @Override
  public FTError.ErrorTag lineTo(FTVectorRec point, Object user) {
    return gray_line_to(point, (grayTWorkerRec)user);
  }

  /* ==================== conicTo ===================================== */
  @Override
  public FTError.ErrorTag conicTo(FTVectorRec control, FTVectorRec point, Object user) {
    return gray_conic_to(control, point, (grayTWorkerRec)user);
  }

  /* ==================== cubicTo ===================================== */
  @Override
  public FTError.ErrorTag cubicTo(FTVectorRec control1, FTVectorRec control2, FTVectorRec point, Object user) {
    return gray_cubic_to(control1, control2, point, (grayTWorkerRec)user);
  }

}
