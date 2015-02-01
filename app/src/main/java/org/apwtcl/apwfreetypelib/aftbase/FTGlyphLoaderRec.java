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

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTLoaderRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTSizeRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.*;

  /* ===================================================================== */
  /*    FTGlyphLoaderRec                                                   */
  /*                                                                       */
  /* ===================================================================== */

public class FTGlyphLoaderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGlyphLoaderRec";

  protected int max_points = 0;
  protected int max_contours = 0;
  protected int max_subglyphs = 0;
  protected boolean use_extra = false;
  protected FTGlyphLoadRec base = null;
  protected FTGlyphLoadRec current = null;
  protected int base_idx;
  protected int current_idx;

  /* ==================== FTGlyphLoaderRec ================================== */
  public FTGlyphLoaderRec() {
    oid++;
    id = oid;
    base = new FTGlyphLoadRec();
    current = new FTGlyphLoadRec();
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
    str.append("..max_points: "+max_points+'\n');
    str.append("..max_contours: "+max_contours+'\n');
    str.append("..max_subglyphs: "+max_subglyphs+'\n');
    str.append("..use_extra: "+use_extra+'\n');
    str.append("..base_idx: "+base_idx+'\n');
    str.append("..current_idx: "+current_idx+'\n');
    return str.toString();
  }
 
  /* ==================== FTGlyphLoaderDone ===================================== */
  public void FTGlyphLoaderDone() {
    FTTrace.Trace(7, TAG, "WARNING: FTGlyphLoaderDone called");
  }

  /* ==================== FTGlyphLoaderNew ===================================== */
  public static FTError.ErrorTag FTGlyphLoaderNew(FTReference<FTGlyphLoaderRec> loader_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphLoaderNew");
    FTGlyphLoaderRec loader = new FTGlyphLoaderRec();
    loader_ref.Set(loader);
    return error;
  }

  /* =====================================================================
   * FTGlyphLoaderCreateExtra
   * =====================================================================
   */
  public FTError.ErrorTag FTGlyphLoaderCreateExtra() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FTGlyphLoaderCreateExtra: %d", max_points));
     if (max_points > 0) {
      base.setExtra_points(new FTVectorRec[2 * max_points]);
     }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphLoaderCreateExtra 2 extra: "+base.getExtra_points()+"!extra2: "+base.getExtra_points2());
    if (base.getExtra_points() == null) {
      use_extra = true;
      base.setExtra_points_idx(0);
      base.setExtra_points2(base.getExtra_points());
      base.setExtra_points2_idx(base.getExtra_points_idx() + max_points);
      GlyphLoaderAdjustPoints();
    }
    return error;
  }

  /* =====================================================================
   * FTDoneGlyph
   * =====================================================================
   */
  public FTError.ErrorTag FTDoneGlyph(FTGlyphRec glyph_lst) {
    Log.e(TAG, "FTDoneGlyph not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * FTVectorTransform
   * =====================================================================
   */
  public FTError.ErrorTag FTVectorTransform(FTReference<FTVectorRec> advance_ref,
          FTReference<FTMatrixRec> matrix_ref) {
    Log.e(TAG, "FTVectorTransform not yet implemented");Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTVectorTransform not yet implemented!!");
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * ft_lookup_glyph_renderer
   * =====================================================================
   */
  public FTRendererRec ft_lookup_glyph_renderer(FTGlyphSlotRec slot) {
    FTFaceRec face = slot.getFace();
    FTLibraryRec library = face.getDriver().library;
    FTRendererRec renderer = library.getCur_renderer();

    if (renderer == null || renderer.glyph_format != slot.getFormat()) {
      renderer = library.getCur_renderer().FTLookupRenderer(library, slot.getFormat(), null);
    }
    return renderer;
  }

  /* =====================================================================
   * FTGetGlyph
   * =====================================================================
   */
  public FTError.ErrorTag FTGetGlyph(FTReference<FTGlyphSlotRec> slot_ref,FTReference<FTGlyphRec> glyph_ref) {
    FTGlyphSlotRec slot = slot_ref.Get();
    FTGlyphRec aglyph = glyph_ref.Get();

    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTLibraryRec library;
    FTGlyphRec glyph = null;
    FTGlyphClassRec clazz = null;

    if (slot == null) {
      return FTError.ErrorTag.LOAD_INVALID_SLOT_HANDLE;
    }
    library = slot.getLibrary();
    if (aglyph == null) {
      error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
      return error;
    }
    /* if it is a bitmap, that's easy :-) */
    if (slot.getFormat() == FTTags.GlyphFormat.BITMAP) {
      FTBitmapGlyphClassRec glyph_class = new FTBitmapGlyphClassRec();
      clazz = glyph_class;
    } else {
      /* if it is an outline */
      if (slot.getFormat() == FTTags.GlyphFormat.OUTLINE) {
        FTOutlineGlyphClass glyph_class = new FTOutlineGlyphClass();
        clazz = glyph_class;
      } else {
        /* try to find a renderer that supports the glyph image format */
        FTRendererRec render = library.getCur_renderer().FTLookupRenderer(library, slot.getFormat(), null);

        if (render != null) {
          clazz = render.getGlyph_class();
        }
      }
    }
    if (clazz == null) {
      error = FTError.ErrorTag.LOAD_INVALID_GLYPH_FORMAT;
      return error;
    }
    /* create FT_Glyph object */
    glyph = new FTGlyphRec();
    glyph.initGlyph(library, clazz);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    /* copy advance while converting it to 16.16 format */
    glyph.advance.setX(slot.getAdvance().getX() << 10);
    glyph.advance.setY(slot.getAdvance().getY() << 10);
    /* now import the image from the glyph slot */
    error = clazz.glyphInit(glyph, slot);
    /* if an error occurred, destroy the glyph */
    if (error != FTError.ErrorTag.ERR_OK) {
      FTDoneGlyph(glyph);
    } else {
      glyph_ref.Set(glyph);
    }
    return error;
  }

  /* =====================================================================
   * GlyphLoaderAdd
   * =====================================================================
   */
  public void GlyphLoaderAdd() {
    int n_curr_contours;
    int n_base_points;
    int n;

    n_curr_contours = current.getN_contours();
    n_base_points = base.getN_points();
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FTGlyphLoaderAdd: n_base_points: %d, loader.current.outline.n_points: %d", n_base_points, current.getN_points()));
    base.setN_points(base.getN_points() + current.getN_points());
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FTGlyphLoaderAdd3: n_points: %d", base.getN_points()));
    base.setN_contours(base.getN_contours() + current.getN_contours());
    base.setNum_subglyphs(base.getNum_subglyphs() + current.getNum_subglyphs());
    /* adjust contours count in newest outline */
    for (n = 0; n < n_curr_contours; n++) {
      current.getContours()[n] = current.getContours()[n + n_base_points];
    }
    /* prepare for another new glyph image */
    GlyphLoaderPrepare();
  }

  /* =====================================================================
   * GlyphLoaderRewind
   * =====================================================================
   */
  public void GlyphLoaderRewind() {
    base.setN_points(0);
    base.n_contours = 0;
    base.setNum_subglyphs(0);
    current.copy(base);
  }

  /* =====================================================================
   * GlyphLoaderPrepare
   *
   * =====================================================================
   */
  public void GlyphLoaderPrepare() {
    current.setN_points(0);
    current.setN_contours(0);
    current.setNum_subglyphs(0);
    GlyphLoaderAdjustPoints();
    GlyphLoaderAdjustSubglyphs();
  }

  /* =====================================================================
   * translate_array
   *
   * =====================================================================
   */
  public static void translate_array(int n, FTVectorRec[] coords, int idx, int delta_x, int delta_y) {
    int k;

    if (delta_x != 0) {
      for (k = 0; k < n; k++) {
        coords[idx + k].setX(coords[idx + k].getX() + delta_x);
      }
    }
    if (delta_y != 0) {
      for (k = 0; k < n; k++) {
        coords[idx + k].setY(coords[idx + k].getY() + delta_y);
      }
    }
  }

  /* =====================================================================
   * GlyphLoaderAdjustPoints
   *
   * =====================================================================
   */
  public void GlyphLoaderAdjustPoints() {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphLoaderAdjustPoints: "+this+"!");
//showLoaderZone("FT_GlyphLoader_Adjust_Points 1");
    current.setPoints(base.getPoints());
    current.setPoints_idx(base.getN_points());
    current.setTags(base.getTags());
    current.setTags_idx(base.getN_points());
    current.setContours(base.getContours());
    current.setContours_idx(base.getN_contours());
//showLoaderZone("FT_GlyphLoader_Adjust_Points 2");
    /* handle extra points table - if any */
    if (use_extra) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphLoaderAdjustPoints 3: extra_points: "+base.getExtra_points()+"!"+base.getExtra_points_idx()+"!extra_points2: "+base.getExtra_points2()+"!"+base.getExtra_points2_idx()+"!");
      current.setExtra_points(base.getExtra_points());
      current.setExtra_points_idx(base.getExtra_points_idx() + base.getN_points());
      current.setExtra_points2(base.getExtra_points2());
      current.setExtra_points2_idx(base.getExtra_points2_idx() + base.getN_points());
    }
//showLoaderZone("FT_GlyphLoader_Adjust_Points 3");
  }

  /* =====================================================================
   * GlyphLoaderAdjustSubglyphs
   *
   * =====================================================================
   */
  public void GlyphLoaderAdjustSubglyphs() {
    current.setSubglyphs(base.getSubglyphs());
    current.setSubglyphs_idx(base.getNum_subglyphs());
  }

  /* =====================================================================
   * GlyphLoaderCheckP
   * =====================================================================
   */
  public boolean GlyphLoaderCheckP(int count) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("CheckP: count: %d, base n_points: %d, current n_points: %d, max_points: %d",
    count, base.getN_points(), current.getN_points(), max_points));
    return (count == 0 ||
          base.getN_points() +
          current.getN_points() + (long)count <=
          max_points);
  }

  /* =====================================================================
   * GlyphLoaderCheckC
   * =====================================================================
   */
  public boolean GlyphLoaderCheckC(int count) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("CheckC: count: %d, base n_contours: %d, current n_contours: %d, max_contours: %d",
    count, base.getN_contours(), current.getN_contours(), max_contours));
    return (count == 0 ||
          base.getN_contours() +
          current.getN_contours() + (long)count <=
          max_contours);
  }

  /* =====================================================================
   * GlyphLoaderCheckPoints
   *
   * Ensure that we can add `n_points' and `n_contours' to our glyph.
   * This function reallocates its outline tables if necessary.  Note that
   * it DOESN'T change the number of points within the loader!
   *
   * =====================================================================
   */
  public  FTError.ErrorTag GlyphLoaderCheckPoints(int n_points, int n_contours) {
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
      boolean adjust = false;
      int new_max;
      int old_max;
      int k;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphLoaderCheckPoints: base.outline.points: "+base.getPoints()+"!base.outline.tags: "+base.getTags()+"!");
      /* check points & tags */
      new_max = base.getN_points() + current.getN_points() + n_points;
      old_max = max_points;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("n_points: base.outline.n_points: %d current.outline.n_points: %d n_points: %d", base.getN_points(), current.getN_points(), n_points));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FTGlyphLoaderCheckPoints: renew: old: %d new: %d", old_max, new_max));
      if (new_max > old_max) {
        new_max = FTCalc.FT_PAD_CEIL(new_max, 8);
        if (new_max > Flags.Outline.POINTS_MAX.getVal()) {
          return FTError.ErrorTag.GLYPH_ARRAY_TOO_LARGE;
        }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FT_RENEW_ARRAY: base.outline.points: "+base.getPoints()+"!");
        base.setPoints((FTVectorRec[]) FTUtil.FT_RENEW_ARRAY(base.getPoints(), FTUtilFlags.ArrayType.FT_VECTOR, old_max, new_max));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "after FT_RENEW_ARRAY: base.outline.points: "+base.getPoints()+"!");
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FT_RENEW_ARRAY: base.outline.tags: "+base.getTags());
        base.setTags((Flags.Curve[])FTUtil.FT_RENEW_ARRAY(base.getTags(), FTUtilFlags.ArrayType.CURVE, old_max, new_max));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "after FT_RENEW_ARRAY: base.outline.tags: "+base.getTags());
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FT_RENEW_ARRAY base.extra_points: "+base.getExtra_points()+"!use_extra: "+use_extra+"!");
        if (use_extra) {
          base.setExtra_points((FTVectorRec[])FTUtil.FT_RENEW_ARRAY(base.getExtra_points(), FTUtilFlags.ArrayType.FT_VECTOR, old_max * 2, new_max * 2));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "after FT_RENEW_ARRAY base.extra_points: "+base.getExtra_points()+"!use_extra: "+use_extra+"!");
          for (k = 0; k < old_max; k++) {
            base.getExtra_points()[new_max + k].setX(base.getExtra_points()[old_max + k].getX());
            base.getExtra_points()[new_max + k].setY(base.getExtra_points()[old_max + k].getY());
          }
          base.setExtra_points2(new FTVectorRec[new_max]);
          for (k = old_max; k < old_max * 2; k++) {
            base.getExtra_points2()[k - old_max] = base.getExtra_point(k);
          }
          for (k = old_max; k < new_max; k++) {
            base.getExtra_points2()[k] = new FTVectorRec();
          }
          base.setExtra_points2_idx(0);
          
        }
        adjust = true;
        max_points = new_max;
      }
      /* check contours */
      old_max = max_contours;
      new_max = base.getN_contours() + current.getN_contours() + n_contours;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("base.outline.n_contours: %d, current.outline.n_contours: %d, n_contours: %d", base.getN_contours(), current.getN_contours(), n_contours));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FTGlyphLoaderCheckContours: renew: old: %d new: %d", old_max, new_max));
      if (new_max > old_max) {
        new_max = FTCalc.FT_PAD_CEIL(new_max, 4);
        if (new_max > Flags.Outline.CONTOURS_MAX.getVal()) {
          return FTError.ErrorTag.GLYPH_ARRAY_TOO_LARGE;
        }
        base.setContours((int[])FTUtil.FT_RENEW_ARRAY(base.getContours(), FTUtilFlags.ArrayType.INT, old_max, new_max));
        adjust = true;
        max_contours = new_max;
      }
      if (adjust) {
        GlyphLoaderAdjustPoints();
      }
      if (error != FTError.ErrorTag.ERR_OK) {
        GlyphLoaderReset();
      }
      return error;
    }

  /* =====================================================================
   * FT_GLYPHLOADER_CHECK_POINTS
   * =====================================================================
   */
  public FTError.ErrorTag FT_GLYPHLOADER_CHECK_POINTS(int points, int contours) {
    if (GlyphLoaderCheckP(points) && GlyphLoaderCheckC(contours)) {
      return FTError.ErrorTag.ERR_OK;
    } else {
      return GlyphLoaderCheckPoints(points, contours);
    }
  }

  /* =====================================================================
   * GlyphLoaderReset
   * =====================================================================
   */
  public void GlyphLoaderReset() {
    base.setExtra_points2(null);
//    max_points = 0;
    max_contours = 0;
    max_subglyphs = 0;
    FTGlyphLoaderRewind();
  }

  /* =====================================================================
   * FTGlyphLoaderRewind
   * =====================================================================
   */
  public void FTGlyphLoaderRewind() {
    base.setN_points(0);
    base.setN_contours(0);
    base.setNum_subglyphs(0);
    current.copy(base);
  }

  /* =====================================================================
   * FTGlyphLoaderCheckSubGlyphs
   * =====================================================================
   */
  public FTError.ErrorTag FTGlyphLoaderCheckSubGlyphs(int n_subs) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int new_max;
    int old_max;

    new_max = base.getNum_subglyphs() + current.getNum_subglyphs() + n_subs;
    old_max = max_subglyphs;
    if (new_max > old_max) {
      new_max = FTCalc.FT_PAD_CEIL(new_max, 2);
      if (old_max == 0) {
        base.setSubglyphs(new FTSubGlyphRec[new_max]);
      } else {
        FTSubGlyphRec[] tmp = new FTSubGlyphRec[old_max];
        tmp = java.util.Arrays.copyOf(base.getSubglyphs(), old_max);
        base.setSubglyphs(new FTSubGlyphRec[new_max]);
        base.setSubglyphs(java.util.Arrays.copyOf(tmp, old_max));
      }
//        if (FT_RENEW_ARRAY(base.subglyphs, old_max, new_max)) {
//          return error;
//        }
      max_subglyphs = new_max;
      GlyphLoaderAdjustSubglyphs();
    }
    return error;
  }
  /* ==================== getMax_points ================================== */
  public int getMax_points() {
    return max_points;
  }

  /* ==================== setMax_points ================================== */
  public void setMax_points(int max_points) {
    this.max_points = max_points;
  }

  /* ==================== getMax_contours ================================== */
  public int getMax_contours() {
    return max_contours;
  }

  /* ==================== setMax_contours ================================== */
  public void setMax_contours(int max_contours) {
    this.max_contours = max_contours;
  }

  /* ==================== getMax_subglyphs ================================== */
  public int getMax_subglyphs() {
    return max_subglyphs;
  }

  /* ==================== setMax_subglyphs ================================== */
  public void setMax_subglyphs(int max_subglyphs) {
    this.max_subglyphs = max_subglyphs;
  }

  /* ==================== isUse_extra ================================== */
  public boolean isUse_extra() {
    return use_extra;
  }

  /* ==================== setUse_extra ================================== */
  public void setUse_extra(boolean use_extra) {
    this.use_extra = use_extra;
  }

  /* ==================== getBase ================================== */
  public FTGlyphLoadRec getBase() {
    return base;
  }

  /* ==================== setBase ================================== */
  public void setBase(FTGlyphLoadRec base) {
    this.base = base;
  }

  /* ==================== getCurrent ================================== */
  public FTGlyphLoadRec getCurrent() {
    return current;
  }

  /* ==================== setCurrent ================================== */
  public void setCurrent(FTGlyphLoadRec current) {
    this.current = current;
  }

  /* ==================== getBase_idx ================================== */
  public int getBase_idx() {
    return base_idx;
  }

  /* ==================== setBase_idx ================================== */
  public void setBase_idx(int base_idx) {
    this.base_idx = base_idx;
  }

  /* ==================== getCurrent_idx ================================== */
  public int getCurrent_idx() {
    return current_idx;
  }

  /* ==================== setCurrent_idx ================================== */
  public void setCurrent_idx(int current_idx) {
    this.current_idx = current_idx;
  }

}