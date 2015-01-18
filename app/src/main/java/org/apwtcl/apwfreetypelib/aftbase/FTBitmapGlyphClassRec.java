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

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

  /* ===================================================================== */
  /*    FTBitmapGlyphClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTBitmapGlyphClassRec extends FTGlyphClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTBitmapGlyphClassRec";

  /* ==================== FTBitmapGlyphClassRec ================================== */
  public FTBitmapGlyphClassRec() {
    oid++;
    id = oid;
    String class_name = "org.apwtcl.gles20.truetype.FTBitmapGlyphFuncs";
    this.glyph_size = 0;
    this.glyph_format = FTTags.GlyphFormat.BITMAP;
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
   *    ft_bitmap_glyph_init
   * =====================================================================
   */
  public FTError.ErrorTag ft_bitmap_glyph_init(Object ... args) {
    Log.w(TAG, "ft_bitmap_glyph_init not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_bitmap_glyph_done
   * =====================================================================
   */
  public FTError.ErrorTag ft_bitmap_glyph_done(Object ... args) {
    Log.w(TAG, "ft_bitmap_glyph_done not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_bitmap_glyph_transform
   * =====================================================================
   */
  public FTError.ErrorTag ft_bitmap_glyph_transform(Object ... args) {
    Log.w(TAG, "ft_bitmap_glyph_done not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_bitmap_glyph_copy
   * =====================================================================
   */
  public FTError.ErrorTag ft_bitmap_glyph_copy(Object ... args) {
    Log.w(TAG, "ft_bitmap_glyph_copy not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ft_bitmap_glyph_bbox
   * =====================================================================
   */
  public FTError.ErrorTag ft_bitmap_glyph_bbox(Object ... args) {
    Log.w(TAG, "ft_bitmap_glyph_init bbox yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* ==================== glyphInit ===================================== */
  public FTError.ErrorTag glyphInit() {
    return ft_bitmap_glyph_init();
  }

  /* ==================== glyphDone ===================================== */
  public FTError.ErrorTag glyphDone() {
    return ft_bitmap_glyph_done();
  }

  /* ==================== glyphCopy ===================================== */
  @Override
  public FTError.ErrorTag glyphCopy() {
    return ft_bitmap_glyph_copy();
  }

  /* ==================== glyphTransform ===================================== */
  @Override
  public FTError.ErrorTag glyphTransform() {
    return ft_bitmap_glyph_transform();
  }

  /* ==================== glyphBBox ===================================== */
  @Override
  public FTError.ErrorTag glyphBBox() {
    return ft_bitmap_glyph_bbox();
  }

  /* ==================== glyphPrepare ===================================== */
  @Override
  public FTError.ErrorTag glyphPrepare(FTGlyphRec glyph, FTReference<FTGlyphSlotRec> slot_ref) {
    Log.e(TAG, "glyphPrepare");
    return FTError.ErrorTag.ERR_OK;
  }

}