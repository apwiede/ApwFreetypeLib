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
  /*    blackTWorker                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class blackTWorker extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "blackTWorker";
    private final static int MaxBezier = 32;

    public int precision_bits = 0;     /* precision related variables         */
    public int precision = 0;
    public int precision_half = 0;
    public int precision_shift = 0;
    public int precision_step = 0;
    public int precision_jitter = 0;
    public int scale_shift = 0;        /* == precision_shift   for bitmaps    */
                                       /* == precision_shift+1 for pixmaps    */
    public int[] buff = null;               /* The profiles buffer                 */
    public int sizeBuff = 0;           /* Render pool size                    */
    public int maxBuff = 0;            /* Profiles buffer size                */
    public int top = 0;                /* Current cursor in buffer            */
    public int error = 0;
    public int numTurns = 0;             /* number of Y-turns in outline        */
    public TPoint arc = null;            /* current Bezier arc pointer          */
    public Short bWidth = 0;             /* target bitmap width                 */
    public byte[] bTarget = null;        /* target bitmap buffer                */
    public byte[] gTarget = null;        /* target pixmap buffer                */
    public int lastX = 0;
    public int lastY = 0;
    public int minY = 0;
    public int maxY = 0;
    public Short num_Profs = 0;          /* current number of profiles          */
    public boolean fresh = false;        /* signals a fresh new profile which   */
                                  /* `start' field must be completed     */
    public boolean joint = false;        /* signals that the last arc ended     */
                                  /* exactly on a scanline.  Allows      */
                                  /* removal of doublets                 */
    public TProfile cProfile = null;     /* current profile                     */
    public TProfile fProfile = null;     /* head of linked list of profiles     */
    public TProfile gProfile = null;     /* contour's first profile in case     */
                                         /* of impact                           */
    public TStates state = null;         /* rendering state                     */
    public FTBitmapRec target = null;    /* description of target bit/pixmap    */
    public FTOutlineRec outline = null;
    public int traceOfs = 0;           /* current offset in target bitmap     */
    public int traceG = 0;             /* current offset in target pixmap     */
    public short traceIncr = 0;          /* sweep's increment in target bitmap  */
    public short gray_min_x = 0;         /* current min x during gray rendering */
    public short gray_max_x = 0;         /* current max x during gray rendering */
    public byte dropOutControl = 0;      /* current drop_out control method     */
    public boolean second_pass = false;  /* indicates whether a horizontal pass */
                                  /* should be performed to control      */
                                  /* drop-out accurately when calling    */
                                  /* Render_Glyph.  Note that there is   */
                                  /* no horizontal pass during gray      */
                                  /* rendering.                          */
    public TPoint[] arcs = new TPoint[3 * MaxBezier + 1]; /* The Bezier stack  */
    public blackTBand[] band_stack = new blackTBand[16]; /* band stack used for sub-banding */
    public int band_top;                /* band stack top                      */

    /* ==================== blackTWorker ================================== */
    public blackTWorker() {
      oid++;
      id = oid;
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

  /* ==================== SweepInit ===================================== */
  public FTError.ErrorTag SweepInit() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "SweepInit not yet implemented");
    return error;
  }

  /* ==================== SweepSpan ===================================== */
  public FTError.ErrorTag SweepSpan() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "SweepSpan not yet implemented");
    return error;
  }

  /* ==================== SweepDrop ===================================== */
  public FTError.ErrorTag SweepDrop() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "SweepDrop not yet implemented");
    return error;
  }

  /* ==================== SweepStep ===================================== */
  public FTError.ErrorTag SweepStep() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "SweepStep not yet implemented");
    return error;
  }

}