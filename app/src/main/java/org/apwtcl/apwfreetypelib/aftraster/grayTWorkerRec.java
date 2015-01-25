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
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

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
  private Object buffer;
  private int buffer_size;
  private TCellRec[] ycells;
  private int ycount;

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

  /* ==================== renderSpan ================================== */
  public FTError.ErrorTag renderSpan(int y, int count, FTSpanRec[] spans, grayTWorkerRec worker) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "renderSpan not yet implemented");
    return error;
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
  public void setBuffer(Object buffer) {
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