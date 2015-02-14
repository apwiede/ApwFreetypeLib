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

package org.apwtcl.apwfreetypelib.aftraster;

  /* ===================================================================== */
  /*    grayTRasterRec                                                     */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class grayTRasterRec extends FTRasterRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "grayTRasterRec";

  private int band_size = 0;
  private grayTWorkerRec worker = null;

  /* ==================== grayTRasterRec ================================== */
  public grayTRasterRec() {
    oid++;
    id = oid;
    worker = new grayTWorkerRec();
  }
    
  /* ==================== grayTRasterRec ================================== */
  public grayTRasterRec(FTRasterRec raster_rec) {
    oid++;
    id = oid;
    this.buffer = raster_rec.buffer;
    this.buffer_size = raster_rec.buffer_size;
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
    str.append("...band_size: "+band_size+'\n');
    return str.toString();
  }

  /* =====================================================================
 *    gray_raster_reset
 *
 * =====================================================================
 */
  public FTError.ErrorTag gray_raster_reset(grayTWorkerRec worker, byte[] pool_base, int pool_size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Debug(0, DebugTag.DBG_RENDER, TAG, "gray_raster_reset");
    if (pool_base != null) {
      buffer = pool_base;
      buffer_size = pool_size;
//      band_size = (int)buffer_size / sizeof(TCell) * 8;
      band_size = buffer_size / 20;  // FIXME!!!
//      grayTWorkerRec worker =  new grayTWorkerRec();
      setWorker(worker);
      worker.setBand_size(band_size);
    } else {
      buffer = null;
      buffer_size = 0;
      setWorker(null);
    }
    return error;
  }

  /* =====================================================================
   *    gray_raster_render
   *
   * =====================================================================
   */
  public FTError.ErrorTag gray_raster_render(FTRasterParamsRec params) {
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
    worker.gray_init_cells(buffer, buffer_size);
    worker.setOutline(outline);
    worker.setNum_cells(0);
    worker.setInvalid(true);
    worker.setNum_gray_spans(0);
    if ((params.getFlags() & FTRasterParamsRec.FT_RASTER_FLAG_DIRECT) != 0) {
//FIXME!!      worker.render_span(params.gray_spans);
      worker.setRender_span_data(params.getUser_data());
    } else {
      worker.setTarget(target_map);
      worker.setRender_span_data(worker);
    }
    return worker.gray_convert_glyph();
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



  /* ==================== getBand_size ================================== */
  public int getBand_size() {
    return band_size;
  }

  /* ==================== setBand_size ================================== */
  public void setBand_size(int band_size) {
    this.band_size = band_size;
  }

  /* ==================== getWorker ================================== */
  public grayTWorkerRec getWorker() {
    return worker;
  }

  /* ==================== setWorker ================================== */
  public void setWorker(grayTWorkerRec worker) {
    this.worker = worker;
  }

}