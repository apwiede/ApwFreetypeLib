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

  /* ===================================================================== */
  /*    FTOutlineGlyphClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTOutlineGlyphClass extends FTGlyphClassRec {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTOutlineGlyphClass";

    /* ==================== FTOutlineGlyphClass ================================== */
    public FTOutlineGlyphClass() {
      oid++;
      id = oid;
      String class_name = "org.apwtcl.gles20.truetype.FTOutlineGlyphFuncs";
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
     *    ft_outline_glyph_init
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_init(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_init not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    ft_outline_glyph_done
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_done(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_done not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    ft_outline_glyph_copy
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_copy(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_copy not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    ft_outline_glyph_transform
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_transform(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_transform not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    ft_outline_glyph_bbox
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_bbox(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_bbox not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    ft_outline_glyph_prepare
     *
     * =====================================================================
     */
    public static int ft_outline_glyph_prepare(Object ... args) {
      Log.w(TAG, "ft_outline_glyph_prepare not yet implemented");
      int error = 0;

      return error;
    }

    /* =====================================================================
     *    FTGlyphTransform
     *
     * =====================================================================
     */
    public static int FTGlyphTransform(Object ... args) {
      Log.w(TAG, "FTGlyphTransform not yet implemented");
      int error = 0;

      return error;
    }

  /* ==================== glyphInit ===================================== */
  public FTError.ErrorTag glyphInit() {
    Log.e(TAG, "glyphInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_init();
  }

  /* ==================== glyphDone ===================================== */
  public FTError.ErrorTag glyphDone() {
    Log.e(TAG, "glyphDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_done();
  }

  /* ==================== glyphCopy ===================================== */
  public FTError.ErrorTag glyphCopy() {
    Log.e(TAG, "glyphCopy not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_copy();
  }

  /* ==================== glyphTransform ===================================== */
  public FTError.ErrorTag glyphTransform() {
    Log.e(TAG, "glyphTransform not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_transform();
  }

  /* ==================== glyphBBox ===================================== */
  public FTError.ErrorTag glyphBBox() {
    Log.e(TAG, "glyphBBox not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_bbox();
  }

  /* ==================== glyphPrepare ===================================== */
  public FTError.ErrorTag glyphPrepare() {
    Log.e(TAG, "glyphPrepare not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ft_outline_glyph_prepare();
  }

}