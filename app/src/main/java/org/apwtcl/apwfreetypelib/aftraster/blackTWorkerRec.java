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
  /*    blackTWorkerRec                                                    */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class blackTWorkerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "blackTWorkerRec";
  private final static int MaxBezier = 32;

  private int precision_bits = 0;     /* precision related variables         */
  private int precision = 0;
  private int precision_half = 0;
  private int precision_shift = 0;
  private int precision_step = 0;
  private int precision_jitter = 0;
  private int scale_shift = 0;        /* == precision_shift   for bitmaps    */
                                     /* == precision_shift+1 for pixmaps    */
  private int[] buff = null;          /* The profiles buffer                 */
  private int sizeBuff = 0;           /* Render pool size                    */
  private int maxBuff = 0;            /* Profiles buffer size                */
  private int top = 0;                /* Current cursor in buffer            */
  private int error = 0;
  private int numTurns = 0;           /* number of Y-turns in outline        */
  private TPointRec arc = null;          /* current Bezier arc pointer          */
  private int bWidth = 0;             /* target bitmap width                 */
  private byte[] bTarget = null;      /* target bitmap buffer                */
  private byte[] gTarget = null;      /* target pixmap buffer                */
  private int lastX = 0;
  private int lastY = 0;
  private int minY = 0;
  private int maxY = 0;
  private int num_Profs = 0;          /* current number of profiles          */
  private boolean fresh = false;      /* signals a fresh new profile which   */
                                     /* `start' field must be completed     */
  private boolean joint = false;      /* signals that the last arc ended     */
                                     /* exactly on a scanline.  Allows      */
                                     /* removal of doublets                 */
  private TProfileRec cProfile = null;   /* current profile                     */
  private TProfileRec fProfile = null;   /* head of linked list of profiles     */
  private TProfileRec gProfile = null;   /* contour's first profile in case     */
                                     /* of impact                           */
  private TStates state = null;       /* rendering state                     */
  private FTBitmapRec target = null;  /* description of target bit/pixmap    */
  private FTOutlineRec outline = null;
  private int traceOfs = 0;           /* current offset in target bitmap     */
  private int traceG = 0;             /* current offset in target pixmap     */
  private int traceIncr = 0;          /* sweep's increment in target bitmap  */
  private int gray_min_x = 0;         /* current min x during gray rendering */
  private int gray_max_x = 0;         /* current max x during gray rendering */
  private byte dropOutControl = 0;    /* current drop_out control method     */
  private boolean second_pass = false; /* indicates whether a horizontal pass */
                                     /* should be performed to control      */
                                     /* drop-out accurately when calling    */
                                     /* Render_Glyph.  Note that there is   */
                                     /* no horizontal pass during gray      */
                                     /* rendering.                          */
  private TPointRec[] arcs = new TPointRec[3 * MaxBezier + 1]; /* The Bezier stack  */
  private blackTBandRec[] band_stack = new blackTBandRec[16]; /* band stack used for sub-banding */
  private int band_top;               /* band stack top                      */

  /* ==================== blackTWorkerRec ================================== */
  public blackTWorkerRec() {
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