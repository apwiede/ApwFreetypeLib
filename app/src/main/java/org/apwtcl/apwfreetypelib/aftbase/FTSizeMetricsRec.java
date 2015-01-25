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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTSizeMetricsRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTSizeMetricsRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSizeMetricsRec";

  private int x_ppem = 0;    /* horizontal pixels per EM */
  private int y_ppem = 0;    /* vertical pixels per EM */
  private int x_scale = 0;     /* scaling values used to convert font */
                                /* all in 26.6 fractional pixels */
  private int y_scale = 0;     /* units to */
  private int ascender = 0;    /* ascender in */
  private int descender = 0;   /* descender in */
  private int height = 0;      /* text height in */
  private int max_advance = 0; /* max horizontal advance in */

  /* ==================== FTSizeMetricsRec ================================== */
  public FTSizeMetricsRec() {
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
    str.append("..x_ppem: "+x_ppem+'\n');
    str.append("..y_ppem: "+x_ppem+'\n');
    str.append("..x_scale: "+x_ppem+'\n');
    str.append("..y_scale: "+x_ppem+'\n');
    str.append("..ascender: "+x_ppem+'\n');
    str.append("..descender: "+x_ppem+'\n');
    str.append("..height: "+x_ppem+'\n');
    str.append("..max_advance: "+x_ppem+'\n');
    return str.toString();
  }

  /* ==================== copy ================================== */
  public void copy(FTSizeMetricsRec from) {
    x_ppem = from.x_ppem;
    y_ppem = from.y_ppem;
    x_scale = from.x_scale;
    y_scale = from.y_scale;
    ascender = from.ascender;
    descender = from.descender;
    height = from.height;
    max_advance = from.max_advance;

  }

  /* ==================== getX_ppem ================================== */
  public int getX_ppem() {
    return x_ppem;
  }

  /* ==================== setX_ppem ================================== */
  public void setX_ppem(int x_ppem) {
    this.x_ppem = x_ppem;
  }

  /* ==================== getY_ppem ================================== */
  public int getY_ppem() {
    return y_ppem;
  }

  /* ==================== setY_ppem ================================== */
  public void setY_ppem(int y_ppem) {
    this.y_ppem = y_ppem;
  }

  /* ==================== getX_scale ================================== */
  public int getX_scale() {
    return x_scale;
  }

  /* ==================== setX_scale ================================== */
  public void setX_scale(int x_scale) {
    this.x_scale = x_scale;
  }

  /* ==================== getY_scale ================================== */
  public int getY_scale() {
    return y_scale;
  }

  /* ==================== setY_scale ================================== */
  public void setY_scale(int y_scale) {
    this.y_scale = y_scale;
  }

  /* ==================== getAscender ================================== */
  public int getAscender() {
    return ascender;
  }

  /* ==================== setAscender ================================== */
  public void setAscender(int ascender) {
    this.ascender = ascender;
  }

  /* ==================== getDescender ================================== */
  public int getDescender() {
    return descender;
  }

  /* ==================== setDescender ================================== */
  public void setDescender(int descender) {
    this.descender = descender;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getMax_advance ================================== */
  public int getMax_advance() {
    return max_advance;
  }

  /* ==================== setMax_advance ================================== */
  public void setMax_advance(int max_advance) {
    this.max_advance = max_advance;
  }

  /* =====================================================================
   * RequestMetrics
   * =====================================================================
   */
  public FTError.ErrorTag RequestMetrics(FTFaceRec face, FTSizeRequestRec req) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    boolean doall = true;

Debug(0, DebugTag.DBG_INIT, TAG, "FTRequestMetrics:");
    FTSizeMetricsRec metrics;

    metrics = face.getSize().metrics;
    if ((face.getFace_flags() & Flags.Face.SCALABLE.getVal()) != 0) {
      int w = 0;
      int h = 0;
      int scaled_w = 0;
      int scaled_h = 0;

      switch (req.getType()) {
      case NOMINAL:
        h = face.getUnits_per_EM();
        w = h;
Debug(0, DebugTag.DBG_INIT, TAG, "met1: "+w+"!"+h+"!");
        break;
      case REAL_DIM:
        h = face.getAscender() - face.getDescender();
        w = h;
Debug(0, DebugTag.DBG_INIT, TAG, "met2: "+w+"!"+h+"!");
        break;
      case BBOX:
        w = face.getBbox().getxMax() - face.getBbox().getxMin();
        h = face.getBbox().getyMax() - face.getBbox().getyMin();
Debug(0, DebugTag.DBG_INIT, TAG, "met3: "+w+"!"+h+"!");
        break;
      case CELL:
        w = face.getMax_advance_width();
        h = face.getAscender() - face.getDescender();
Debug(0, DebugTag.DBG_INIT, TAG, "met4: "+w+"!"+h+"!");
        break;
      case SCALES:
        metrics.x_scale = req.getWidth();
        metrics.y_scale = req.getHeight();
        if (metrics.x_scale == 0) {
          metrics.x_scale = metrics.y_scale;
        } else {
          if (metrics.y_scale == 0) {
            metrics.y_scale = metrics.x_scale;
          }
        }
        doall = false;
Debug(0, DebugTag.DBG_INIT, TAG, "met5: "+w+"!"+h+"!");
        break;
      case MAX:
        break;
      }
      if (doall) {
        /* to be on the safe side */
        if (w < 0) {
          w = -w;
        }
        if (h < 0) {
          h = -h;
        }
        scaled_w = req.getHoriResolution() != 0
            ? ((req.getWidth() * req.getHoriResolution() + 36) / 72)
            : req.getWidth();
        scaled_h = req.getVertResolution() != 0
            ? ((req.getHeight() * req.getVertResolution() + 36) / 72)
            : req.getHeight();
Debug(0, DebugTag.DBG_INIT, TAG, "scaled_w: "+scaled_w+"!"+scaled_h+"!"+req);
        /* determine scales */
        if (req.getWidth() != 0) {
          metrics.x_scale = FTCalc.FTDivFix(scaled_w, w);
          if (req.getHeight() != 0) {
            metrics.y_scale = FTCalc.FTDivFix(scaled_h, h);
            if (req.getType() == FTTags.SizeRequestType.CELL) {
              if (metrics.y_scale > metrics.x_scale) {
                metrics.y_scale = metrics.x_scale;
              } else {
                metrics.x_scale = metrics.y_scale;
              }
            }
          } else {
          metrics.y_scale = metrics.x_scale;
            scaled_h = FTCalc.FT_MulDiv(scaled_w, h, w);
          }
        } else {
          metrics.x_scale = metrics.y_scale = FTCalc.FTDivFix(scaled_h, h);
          scaled_w = FTCalc.FT_MulDiv(scaled_h, w, h);
        }
      }
      /* calculate the ppems */
      if (req.getType() != FTTags.SizeRequestType.NOMINAL) {
        scaled_w = FTCalc.FTMulFix(face.getUnits_per_EM(), metrics.x_scale);
        scaled_h = FTCalc.FTMulFix(face.getUnits_per_EM(), metrics.y_scale);
      }
      metrics.x_ppem = (short)((scaled_w + 32) >> 6);
      metrics.y_ppem = (short)((scaled_h + 32) >> 6);
Debug(0, DebugTag.DBG_INIT, TAG, String.format("scaled: %d %d %d %d", scaled_w, scaled_h, metrics.x_ppem, metrics.y_ppem));
      FTReference<FTSizeMetricsRec> metrics_ref = new FTReference<FTSizeMetricsRec>();
      metrics_ref.Set(metrics);
      ft_recompute_scaled_metrics(face, metrics_ref);
      metrics = metrics_ref.Get();
    } else {
//        FT_ZERO( metrics );
      metrics.x_scale = 1 << 16;
      metrics.y_scale = 1 << 16;
    }
    FTTrace.Trace(7, TAG, "FT_Request_Metrics:");
    FTTrace.Trace(7, TAG, String.format("  x scale: %d (%f)",
                metrics.x_scale, metrics.x_scale / 65536.0 ));
    FTTrace.Trace(7, TAG, String.format("  y scale: %d (%f)",
                metrics.y_scale, metrics.y_scale / 65536.0 ));
    FTTrace.Trace(7, TAG, String.format("  ascender: %f", metrics.ascender / 64.0));
    FTTrace.Trace(7, TAG, String.format("  descender: %f", metrics.descender / 64.0));
    FTTrace.Trace(7, TAG, String.format("  height: %f", metrics.height / 64.0));
    FTTrace.Trace(7, TAG, String.format("  max advance: %f", metrics.max_advance / 64.0));
    FTTrace.Trace(7, TAG, String.format("  x ppem: %d", metrics.x_ppem ));
    FTTrace.Trace(7, TAG, String.format("  y ppem: %d", metrics.y_ppem ));
    return error;
  }
 
  /* =====================================================================
   * ft_recompute_scaled_metrics
   * =====================================================================
   */
  public int ft_recompute_scaled_metrics(FTFaceRec face, FTReference<FTSizeMetricsRec> metrics_ref) {
    int error = 0;
    FTSizeMetricsRec metrics = metrics_ref.Get();

Debug(0, DebugTag.DBG_INIT, TAG, "ft_recompute_scaled_metrics:");
    /* Compute root ascender, descender, test height, and max_advance */

//    metrics->ascender    = FT_PIX_CEIL( FT_MulFix( face->ascender, metrics->y_scale ) ); 
//    metrics->descender   = FT_PIX_FLOOR( FT_MulFix( face->descender, metrics->y_scale ) );
//    metrics->height      = FT_PIX_ROUND( FT_MulFix( face->height, metrics->y_scale ) );
//    metrics->max_advance = FT_PIX_ROUND( FT_MulFix( face->max_advance_width, metrics->x_scale ) );
    metrics.ascender = FTCalc.FTMulFix(face.getAscender(), metrics.y_scale);
    metrics.descender = FTCalc.FTMulFix(face.getDescender(), metrics.y_scale);
    metrics.height = FTCalc.FTMulFix(face.getHeight(), metrics.y_scale);
    metrics.max_advance = FTCalc.FTMulFix(face.getMax_advance_width(), metrics.x_scale);
    metrics_ref.Set(metrics);
    return error;
  }

}