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
  /*    grayTWorker                                                        */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBBoxRec;
import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class grayTWorker extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "grayTWorker";
    private final static int MaxBezier = 32;

    public int ex;
    public int ey;
    public int min_ex;
    public int max_ex;
    public int min_ey;
    public int max_ey;
    public int count_ex;
    public int count_ey;
    public int area;
    public int cover;
    public boolean invalid;
    public TCell[] cells = null;
    public int max_cells;
    public int num_cells;
    public int cx;
    public int cy;
    public int x;
    public int y;
    public int last_ey;
    public FTVectorRec[] bez_stack = new FTVectorRec[MaxBezier * 3 + 1];
    public int[] lev_stack = new int[32];
    public FTOutlineRec outline;
    public FTBitmapRec target;
    public FTBBoxRec clip_box;
    public FTSpan[] gray_spans = new FTSpan [FTSpan.FT_MAX_GRAY_SPANS];
    public int num_gray_spans;
    public Object render_span_data;
    public int span_y;
    public int band_size;
    public int band_shoot;
//    public ft_jmp_buf jump_buffer;
    public Object buffer;
    public int buffer_size;
    public TCell[] ycells;
    public int ycount;

    /* ==================== grayTWorker ================================== */
    public grayTWorker() {
      int i;

      oid++;
      id = oid;
      target = new FTBitmapRec();
      outline = new FTOutlineRec();
      clip_box = new FTBBoxRec();
      for (i = 0; i < FTSpan.FT_MAX_GRAY_SPANS; i++) {
        gray_spans[i] = new FTSpan();
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
      return str.toString();
    }

  public FTError.ErrorTag renderSpan(int y, int count, FTSpan[] spans, grayTWorker worker) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "renderSpan not yet implemented");
    return error;
  }

}