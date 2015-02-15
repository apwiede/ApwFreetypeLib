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
  /*    FTSizeRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    FreeType root size class structure.  A size object models a face   */
  /*    object at a given size.                                            */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    face    :: Handle to the parent face object.                       */
  /*                                                                       */
  /*    generic :: A typeless pointer, which is unused by the FreeType     */
  /*               library or any of its drivers.  It can be used by       */
  /*               client applications to link their own data to each size */
  /*               object.                                                 */
  /*                                                                       */
  /*    metrics :: Metrics for this size object.  This field is read-only. */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.afttruetype.TTSizeRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;


public class FTSizeRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSizeRec";

  protected FTFaceRec face = null;           /* parent face object              */
  protected FTSizeMetricsRec metrics = null; /* size metrics                    */

  /* ==================== FTSizeRec ================================== */
  public FTSizeRec() {
    oid++;
    id = oid;
    metrics = new FTSizeMetricsRec();
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
   * FTNewSize
   * =====================================================================
   */
  public static FTError.ErrorTag FTNewSize(FTFaceRec face, FTReference<FTSizeRec> size_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTDriverRec driver = null;
    FTDriverClassRec clazz;
    FTSizeRec size;
    FTListNodeRec node = null;

Debug(0, DebugTag.DBG_SIZE, TAG, "FTNewSize");
    if (face == null) {
      size_ref.Set(null);
      return FTError.ErrorTag.INTERP_INVALID_FACE_HANDLE;
    }
    if (face.getDriver() == null) {
      size_ref.Set(null);
      return FTError.ErrorTag.INTERP_INVALID_DRIVER_HANDLE;
    }
    driver = face.getDriver();
    clazz  = driver.getDriver_clazz();
    /* Allocate new size object and perform basic initialisation */
    switch (clazz.getSizeObjectType()) {
    case SIZE:
      size = new TTSizeRec();
      break;
    default:
      size_ref.Set(null);
      return FTError.ErrorTag.FACE_BAD_SIZE_OBJECT_TYPE;
    }
    node = new FTListNodeRec();
    size.face = face;
    error = clazz.initSize(size);
    /* in case of success, add to the face's list */
    if (error == FTError.ErrorTag.ERR_OK) {
      node.data = size;
      node.FTListAdd(face.getSizes_list());
    }
    if (error != FTError.ErrorTag.ERR_OK) {
//        FT_FREE( node );
//        FT_FREE( size );
    }
Debug(0, DebugTag.DBG_SIZE, TAG, "FTNewSize END: "+error+"!");
    size_ref.Set(size);
    return error;
  }

  /* =====================================================================
   * ActivateSize
   * =====================================================================
   */
  public FTError.ErrorTag ActivateSize() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTFaceRec face;

Debug(0, DebugTag.DBG_SIZE, TAG, "FTActivateSize");
    face = this.face;
    if (face == null || face.getDriver() == null) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    /* we don't need anything more complex than that; all size objects */
    /* are already listed by the face                                  */
    face.setSize(this);
Debug(0, DebugTag.DBG_SIZE, TAG, "FTActivateSize END: "+error+"!");
    return error;
  }

  /* =====================================================================
   * MatchSize
   * =====================================================================
   */
  public FTError.ErrorTag MatchSize(FTFaceRec face, FTSizeRequestRec req, boolean ignore_width, FTReference<Integer> size_index_ref) {
    Debug(0, DebugTag.DBG_SIZE, TAG, "FTMatchSize:");
    int i;
    int w;
    int h;

    if (!face.getFace_flags().contains(Flags.Face.FIXED_SIZES)) {
      return FTError.ErrorTag.GLYPH_INVALID_FACE_HANDLE;
    }
    /* FT_Bitmap_Size doesn't provide enough info... */
    if (req.getType() != FTTags.SizeRequestType.NOMINAL) {
      return FTError.ErrorTag.GLYPH_UNIMPLEMENTED_FEATURE;
    }
    w = req.getHoriResolution() != 0
        ? ((req.getWidth() * req.getHoriResolution() + 36) / 72)
        : req.getWidth();
    h = req.getVertResolution() != 0
        ? ((req.getHeight() * req.getVertResolution() + 36) / 72)
        : req.getHeight();
    if (req.getWidth() != 0 && req.getHeight() == 0) {
      h = w;
    } else {
      if (req.getWidth() == 0 && req.getHeight() != 0) {
        w = h;
      }
    }
    w = FTCalc.FT_PIX_ROUND(w);
    h = FTCalc.FT_PIX_ROUND(h);
    for (i = 0; i < face.getNum_fixed_sizes(); i++) {
      FTBitmapSize bsize = face.getAvailable_sizes()[i];

      if (h != FTCalc.FT_PIX_ROUND(bsize.y_ppem)) {
        continue;
      }
      if (w == FTCalc.FT_PIX_ROUND( bsize.x_ppem) || ignore_width) {
        FTTrace.Trace(7, TAG, String.format("FTMatchSize: bitmap strike %d matches", i));
        if (size_index_ref != null) {
          size_index_ref.Set(new Integer(i));
        }
        return FTError.ErrorTag.ERR_OK;
      }
    }
    return FTError.ErrorTag.GLYPH_INVALID_PIXEL_SIZE;
  }

  /* =====================================================================
   * SelectMetrics
   * =====================================================================
   */
  public FTError.ErrorTag SelectMetrics(int strike_index) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_SIZE, TAG, "FTSelectMetrics:");
    FTBitmapSize  bsize;

    bsize = face.getAvailable_sizes()[strike_index];
    metrics.setX_ppem(((bsize.x_ppem + 32) >> 6));
    metrics.setY_ppem(((bsize.y_ppem + 32) >> 6));
    if (face.getFace_flags().contains(Flags.Face.SCALABLE)) {
      metrics.setX_scale(FTCalc.FTDivFix(bsize.x_ppem, face.getUnits_per_EM()));
      metrics.setY_scale(FTCalc.FTDivFix(bsize.y_ppem, face.getUnits_per_EM()));
      metrics.ft_recompute_scaled_metrics(face);
    } else {
      metrics.setX_scale(1 << 16);
      metrics.setY_scale(1 << 16);
      metrics.setAscender(bsize.y_ppem);
      metrics.setDescender(0);
      metrics.setHeight(bsize.height << 6);
      metrics.setMax_advance(bsize.x_ppem);
    }
    FTTrace.Trace(7, TAG, "FT_Select_Metrics:");
    FTTrace.Trace(7, TAG, String.format("  x scale: %d (%f)",
        metrics.getX_scale(), metrics.getX_scale() / 65536.0));
    FTTrace.Trace(7, TAG, String.format("  y scale: %d (%f)",
        metrics.getY_scale(), metrics.getY_scale() / 65536.0));
    FTTrace.Trace(7, TAG, String.format("  ascender: %f",metrics.getAscender() / 64.0));
    FTTrace.Trace(7, TAG, String.format("  descender: %f", metrics.getDescender() / 64.0));
    FTTrace.Trace(7, TAG, String.format("  height: %f", metrics.getHeight() / 64.0));
    FTTrace.Trace(7, TAG, String.format("  max advance: %f", metrics.getMax_advance() / 64.0));
    FTTrace.Trace(7, TAG, String.format("  x ppem: %d", metrics.getX_ppem()));
    FTTrace.Trace(7, TAG, String.format("  y ppem: %d", metrics.getY_ppem()));
    return error;
  }

  /* =====================================================================
   * SelectSize
   * =====================================================================
   */
  public FTError.ErrorTag SelectSize(FTFaceRec face, int strike_index) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_SIZE, TAG, "FTSelectSize:");
    FTDriverClassRec clazz;

    if (face == null || !face.getFace_flags().contains(Flags.Face.FIXED_SIZES)) {
      return FTError.ErrorTag.GLYPH_INVALID_FACE_HANDLE;
    }
    if (strike_index < 0 || strike_index >= face.getNum_fixed_sizes()) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    clazz = face.getDriver().getDriver_clazz();
    error = clazz.selectSize(strike_index);
    if (error != FTError.ErrorTag.FACE_NO_SELECT_SIZE_FUNCTION) {
      FTSizeMetricsRec metrics = face.getSize().metrics;

      FTTrace.Trace(7, TAG, "FT_Select_Size (font driver's `select_size'):" );
      FTTrace.Trace(7, TAG, String.format("  x scale: %d (%f)",
          metrics.getX_scale(), metrics.getX_scale() / 65536.0));
      FTTrace.Trace(7, TAG, String.format("  y scale: %d (%f)",
          metrics.getY_scale(), metrics.getY_scale() / 65536.0));
      FTTrace.Trace(7, TAG, String.format("  ascender: %f", metrics.getAscender() / 64.0));
      FTTrace.Trace(7, TAG, String.format("  descender: %f", metrics.getDescender() / 64.0));
      FTTrace.Trace(7, TAG, String.format("  height: %f", metrics.getHeight() / 64.0));
      FTTrace.Trace(7, TAG, String.format("  max advance: %f", metrics.getMax_advance() / 64.0));
      FTTrace.Trace(7, TAG, String.format("  x ppem: %d", metrics.getX_ppem()));
      FTTrace.Trace(7, TAG, String.format("  y ppem: %d", metrics.getY_ppem()));
      return error;
    }
    SelectMetrics(strike_index);
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * RequestSize
   * =====================================================================
   */
  public FTError.ErrorTag RequestSize(FTFaceRec face, FTReference<FTSizeRequestRec> req_ref) {
    FTSizeRequestRec req = req_ref.Get();
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTDriverClassRec clazz;
    int strike_index;

Debug(0, DebugTag.DBG_SIZE, TAG, "FTRequestSize:");
    if (face == null) {
      return FTError.ErrorTag.GLYPH_INVALID_FACE_HANDLE;
    }
    if (req == null || req.getWidth() < 0 || req.getHeight() < 0 ||
        req.getType().getVal() >= FTTags.SizeRequestType.MAX.getVal()) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    clazz = face.getDriver().getDriver_clazz();
    FTSizeMetricsRec metrics = face.getSize().metrics;
Debug(0, DebugTag.DBG_SIZE, TAG, "ME1: "+face.getSize().metrics.getX_ppem()+"!"+face.getSize().metrics.getY_ppem()+"!");
    error = clazz.requestSize(face.getSize(), req);
Debug(0, DebugTag.DBG_SIZE, TAG, "ME2: "+face.getSize().metrics.getX_ppem()+"!"+face.getSize().metrics.getY_ppem()+"!");
    if (error != FTError.ErrorTag.FACE_NO_SIZE_REQUEST_FUNCTION) {
      FTTrace.Trace(9, TAG, "FT_Request_Size (font driver's `request_size'):");
      FTTrace.Trace(9, TAG, String.format("  x scale: %d (%f)",
          metrics.getX_scale(), metrics.getX_scale() / 65536.0));
      FTTrace.Trace(9, TAG, String.format("  y scale: %d (%f)",
          metrics.getY_scale(), metrics.getY_scale() / 65536.0));
      FTTrace.Trace(9, TAG, String.format("  ascender: %f", metrics.getAscender() / 64.0));
      FTTrace.Trace(9, TAG, String.format("  descender: %f", metrics.getDescender() / 64.0));
      FTTrace.Trace(9, TAG, String.format("  height: %f", metrics.getHeight() / 64.0));
Debug(0, DebugTag.DBG_SIZE, TAG, "ascender: "+metrics.getAscender()+" "+metrics.getDescender()+" "+metrics.getHeight());
      FTTrace.Trace(9, TAG, String.format("  max advance: %f", metrics.getMax_advance() / 64.0));
      FTTrace.Trace(9, TAG, String.format("  x ppem: %d", metrics.getX_ppem()));
      FTTrace.Trace(9, TAG, String.format("  y ppem: %d", metrics.getY_ppem()));
      return error;
    }
    /*
     * The reason that a driver doesn't have `request_size' defined is
     * either that the scaling here suffices or that the supported formats
     * are bitmap-only and size matching is not implemented.
     *
     * In the latter case, a simple size matching is done.
     */
    if (face.getFace_flags().contains(Flags.Face.SCALABLE) &&
        face.getFace_flags().contains(Flags.Face.FIXED_SIZES)) {
      FTReference<Integer> strike_index_ref = new FTReference<>();
//    	strike_index_Ref.Set(strike_index);
      error = MatchSize(face, req, false, strike_index_ref);
      strike_index = strike_index_ref.Get();
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      return SelectSize(face, (int)strike_index);
    }
    face.getSize().getMetrics().RequestMetrics(face, req);

Debug(0, DebugTag.DBG_SIZE, TAG, "FTRequestSize: done");
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * SetCharSize
   * =====================================================================
   */
  public FTError.ErrorTag SetCharSize(FTFaceRec face, int char_width, int char_height, int horz_resolution, int vert_resolution) {
    FTReference<FTSizeRequestRec> req_ref = new FTReference<>();
    FTSizeRequestRec req = new FTSizeRequestRec();

 Debug(0, DebugTag.DBG_SIZE, TAG, String.format("FTSetCharSize: cw: %d ch: %d hr: %d vr: %d", char_width, char_height, horz_resolution, vert_resolution));
    if (char_width == 0) {
      char_width = char_height;
    } else {
      if (char_height == 0) {
        char_height = char_width;
      }
    }
    if (horz_resolution == 0) {
      horz_resolution = vert_resolution;
    } else {
      if (vert_resolution == 0) {
        vert_resolution = horz_resolution;
      }
    }
    if (char_width < 1 * 64) {
      char_width = 1 * 64;
    }
    if (char_height < 1 * 64) {
      char_height = 1 * 64;
    }
    if (horz_resolution == 0) {
      horz_resolution = vert_resolution = 72;
    }
    req.setType(FTTags.SizeRequestType.NOMINAL);
    req.setWidth(char_width);
    req.setHeight(char_height);
    req.setHoriResolution(horz_resolution);
    req.setVertResolution(vert_resolution);
    req_ref.Set(req);
    return RequestSize(face, req_ref);
  }

  /* ==================== getFace ================================== */
  public FTFaceRec getFace() {
    return face;
  }

  /* ==================== setFace ================================== */
  public void setFace(FTFaceRec face) {
    this.face = face;
  }

  /* ==================== getMetrics ================================== */
  public FTSizeMetricsRec getMetrics() {
    return metrics;
  }

  /* ==================== setMetrics ================================== */
  public void setMetrics(FTSizeMetricsRec metrics) {
    this.metrics = metrics;
  }

}