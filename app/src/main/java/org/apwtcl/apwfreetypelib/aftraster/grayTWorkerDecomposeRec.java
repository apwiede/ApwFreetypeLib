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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.RasterUtil;

public class grayTWorkerDecomposeRec extends grayTWorkerBaseRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTWorkerDecomposeRec";

  /* ==================== grayTWorkerDecomposeRec ================================== */
  public grayTWorkerDecomposeRec() {
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
    return super.toDebugString()+str.toString();
  }

  /* =====================================================================
   *    gray_find_cell
   *
   * =====================================================================
   */
  private TCellRec gray_find_cell() {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("  gray_find_cell: ras.ex: %d, ras.ey: %d, ras.count_ex: %d, ras.num_cells: %d", this.ex, this.ey, count_ex, num_cells));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, str.toString());
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("new cell: x: %d, worker.ex: %d, worker.ey: %d", x, ex, ey));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        ycells[pcellIdx].setNext(cell);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("isYcell: cell idx: %d cell next idx: %d ", cell.getSelf_idx(), cell.getNext() == null ? -1 : cell.getNext().getSelf_idx()));
      } else {
        cell.setNext(cells[cellIdx].getNext());
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("cellIdx. %d pcellIdx: %d",  cellIdx, pcellIdx));
        cells[cellIdx].setNext(cell);
      }
    }
    return cell;
  }

  /* =====================================================================
   *    gray_start_cell
   *
   * =====================================================================
   */
  private void gray_start_cell(int ex, int ey) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_start_cell: ex: %d, ey: %d, worker.max_ex: %d, worker.min_ex: %d", ex, ey, max_ex, min_ex));

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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_start_cell2: worker.x: %d, worker.y: %d, worker.ex: %d, worker.ex: %d, worker.getlast_ey: %d", this.x, this.y, this.ex, this.ey, last_ey));
    gray_set_cell(ex, ey);
  }


  /* =====================================================================
   *    gray_record_cell
   *
   * =====================================================================
   */
  public void gray_record_cell() {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_record_cell: ras.invalid: %b, ras.area: %x, ras.cover: %x\n", invalid, area, cover));

    if (!invalid && (area | cover) != 0) {
      TCellRec cell = gray_find_cell();

      cell.setArea(cell.getArea() + area);
      cell.setCover(cell.getCover() + cover);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_record_cell2: cell.area: %x, cell.cover: %x worker.area: %x, worker.cover: %x\n", cell.getArea(), cell.getCover(), area, cover));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_set_cell: ex: %d, ey: %d, invalid: %b, worker.ex: %d, worker.ey: %d", ex, ey, invalid, this.ex, this.ey));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_set_cell after call gray_record_cell"));
      }
      area = 0;
      cover = 0;
    }
    this.ex = ex;
    this.ey = ey;
    invalid = (ey >= count_ey || ex >= count_ex);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_set_cell2: invalid: %b, worker.area: %d, worker.cover: %d, worker.ex: %d, worker.ey: %d, worker.x: %d, worker.y: %d", invalid, area, cover, this.ex, this.ey, this.x, this.y));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_set_cell end"));
  }

  /* =====================================================================
   *    gray_render_scanline
   *
   * =====================================================================
   */
  private void gray_render_scanline(int ey, int x1, int y1, int x2, int y2) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_scanline0 x1: %d(%d)(%.2f) y1: %d(%d)(%.2f), x2: %d(%d)(%.2f), y2: %d(%d)(%.2f), ey: %d", x1, x1/4, x1/256.0, y1, y1/4, y1/256.0, x2, x2/4, x2/256.0, y2, y2/4, y2/256.0, ey));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_scanline1 worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("call gray_set_cell ex2: %d, ey: %d", ex2, ey));
      gray_set_cell(ex2, ey);
      return;
    }
      /* everything is located in a single cell.  That is easy! */
      /*                                                        */
    if (ex1 == ex2) {
      delta = y2 - y1;
      area = area + ((fx1 + fx2) * delta);
      cover = cover + delta;
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_scanline return2: worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_scanline return2 ex1 == ex2: %d", ex2));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("delta: 0x%08x, p: 0x%08x, dx: 0x%08x, mod: 0x%08x", delta, p, dx, mod));
    if (mod < 0) {
      delta--;
      mod += dx;
    }
    area = area + ((fx1 + first) * delta);
    cover = cover + delta;
    ex1 += incr;
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("call gray_set_cell2 ex1: %d, ey: %d", ex1, ey));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("ex1: %d ex2: %d incr: %d", ex1, ex2, incr));
        delta = lift;
        mod  += rem;
        if (mod >= 0) {
          mod -= dx;
          delta++;
        }
        area = area + (RasterUtil.ONE_PIXEL() * delta);
        cover = cover + delta;
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_scanline3: worker.area: 0x%08x, worker.cover: 0x%08x", area, cover));
        y1 += delta;
        ex1 += incr;
        gray_set_cell(ex1, ey);
      }
    }
    delta = y2 - y1;
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("return end delta: %d", delta));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line return 1");
        return;
      }
    }
      /* everything is on a single scanline */
    if (ey1 == ey2) {
      gray_render_scanline(ey1, this.x, fy1, to_x, fy2);
      this.x = to_x;
      this.y = to_y;
      last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line return 2");
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line call gray_set_cell while");
        gray_set_cell(ex, ey1);
      }
      delta = (fy2 - RasterUtil.ONE_PIXEL() + first);
      this.area = this.area + two_fx * delta;
      this.cover = this.cover + delta;
      this.x = to_x;
      this.y = to_y;
      last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line return 3");
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line call gray_render_scanline 1");
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line call gray_render_scanline 2");
        gray_render_scanline(ey1, x, RasterUtil.ONE_PIXEL() - first, x2, first);
        x = x2;
        ey1 += incr;
        gray_set_cell(RasterUtil.TRUNC(x), ey1);
      }
    }
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line call gray_render_scanline 3");
    gray_render_scanline(ey1, x, RasterUtil.ONE_PIXEL() - first, to_x, fy2);
    this.x = to_x;
    this.y = to_y;
    last_ey = RasterUtil.SUBPIXELS(ey2);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_line end");
  }

  /* =====================================================================
   *    gray_split_conic
   *
   * =====================================================================
   */
  private void gray_split_conic(FTVectorRec[] base, int arcIdx) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("gray_split_conic arcIdx: %d", arcIdx));
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

Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic 1a to_x: %d(%d)(%.2f) to_y: %d(%d)(%.2f)",
    RasterUtil.UPSCALE(to.getX()), to.getX(), to.getX()/64.0, RasterUtil.UPSCALE(to.getX()), to.getY(), to.getY()/64.0));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic 1b control_x: %d(%d)(%.2f), control_y: %d(%d)(%.2f)",
    RasterUtil.UPSCALE(control.getX()), control.getX(), control.getX()/64.0, RasterUtil.UPSCALE(control.getY()), control.getY(), control.getY()/64.0));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic 1c x: %d(%d)(%.2f), y: %d(%d)(%.2f)",
    this.x, RasterUtil.DOWNSCALE(this.x), this.x/256.0, this.y, RasterUtil.DOWNSCALE(this.y), this.y/256.0));

    levels = lev_stack;
    arc = bez_stack;
    arc[arcIdx].setX(RasterUtil.UPSCALE(to.getX()));
    arc[arcIdx].setY(RasterUtil.UPSCALE(to.getY()));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic !doDraw continue level: %d", level));
          continue;
        }
      }
      doDraw = false;
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic call gray_render_line top: %d", top));

      gray_render_line(arc[arcIdx + 0].getX(), arc[arcIdx + 0].getY());
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic call after gray_render_line top: %d", top));
      top--;
      arcIdx -= 2;
    } while ( top >= 0 );
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_conic end top: %d", top));
  }

  /* =====================================================================
   *    gray_split_cubic
   *
   * =====================================================================
   */
  private void gray_split_cubic(FTVectorRec[] base, int arcIdx) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_split_cubic");
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

Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_render_cubic");
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_cubic call gray_render_line"));
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_render_cubic 2 call gray_render_line"));
      gray_render_line(arc[arcIdx + 0].getX(), arc[arcIdx + 0].getY());
      if (arc == bez_stack) {
        return;
      }
      arcIdx -= 3;
      bez_stack = arc;
    }
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
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_move_to: to.x: %d, to.y: %d", to.getX(), to.getY()));

      /* record current cell, if any */
    gray_record_cell();
      /* start to a new position */
    x = RasterUtil.UPSCALE(to.getX());
    y = RasterUtil.UPSCALE(to.getY());
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_move_to2: x(UPSCALE): %d, y(UPSCALE): %d", x, y));
    gray_start_cell(RasterUtil.TRUNC(x), RasterUtil.TRUNC(y));
    this.x = x;
    this.y = y;
    Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_move_to end: worker.x: %d(%d)(%f), worker.y: %d(%d)(%f)", this.x, this.x/4, this.x/256.0, this.y, this.y/4, this.y/256.0));
    return error;
  }

  /* =====================================================================
   *    gray_line_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_line_to(FTVectorRec to) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, String.format("gray_line_to: to.x: %d, to.y: %d", to.getX(), to.getY()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_line_to call gray_render_line");
    gray_render_line(RasterUtil.UPSCALE(to.getX()), RasterUtil.UPSCALE(to.getY()));
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_line_to after call gray_render_line");
    return error;
  }

  /* =====================================================================
   *    gray_conic_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_conic_to(FTVectorRec control, FTVectorRec to) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_conic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_conic(control, to);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_conic_to end");
    return error;
  }

  /* =====================================================================
   *    gray_cubic_to
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_cubic_to(FTVectorRec control1, FTVectorRec control2, FTVectorRec to) {
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_cubic_to");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    gray_render_cubic(control1, control2, to);
Debug(0, DebugTag.DBG_DECOMPOSE, TAG, "gray_cubic_to end");
    return error;
  }

}