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
  /*    FTGlyphRec                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The root glyph structure contains a given glyph image plus its     */
  /*    advance width in 16.16 fixed-point format.                         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    library :: A handle to the FreeType library object.                */
  /*                                                                       */
  /*    clazz   :: A pointer to the glyph's class.  Private.               */
  /*                                                                       */
  /*    format  :: The format of the glyph's image.                        */
  /*                                                                       */
  /*    advance :: A 16.16 vector that gives the glyph's advance width.    */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTGlyphRec extends FTGlyphClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGlyphRec";

  protected FTLibraryRec library = null;
  protected FTTags.GlyphFormat format = FTTags.GlyphFormat.NONE;
  protected FTVectorRec advance = null;

  /* ==================== FTGlyphRec ================================== */
  public FTGlyphRec() {
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
    str.append("..format: "+format+'\n');
    return str.toString();
  }

  /* =====================================================================
   * initGlyph
   * =====================================================================
   */
  public FTError.ErrorTag initGlyph(FTLibraryRec library, FTGlyphClassRec clazz) {
    FTError.ErrorTag  error = FTError.ErrorTag.ERR_OK;
    FTGlyphRec glyph = null;

    this.library = library;
    format = clazz.glyph_format;
    return error;
  }

  /* =====================================================================
   * GlyphTransform
   * =====================================================================
   */
  public FTError.ErrorTag GlyphTransform(FTGlyphRec advance, int val, FTVectorRec vec) {
    Log.w(TAG, "WARNING: FTGlyphTransform not yet implemented");
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTGlyphTransform not yet implemented!!");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphInit ================================== */
  public FTError.ErrorTag glyphInit() {
    Log.e(TAG, "glyphInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphDone ================================== */
  public FTError.ErrorTag glyphDone() {
    Log.e(TAG, "glyphInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphCopy ================================== */
  public FTError.ErrorTag glyphCopy () {
    Log.e(TAG, "glyphInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphTransform ================================== */
  public FTError.ErrorTag glyphTransform() {
    Log.e(TAG, "glyphInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphBBox ================================== */
  public FTError.ErrorTag  glyphBBox() {
    Log.e(TAG, "glyphBBox not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== glyphPrepare ================================== */
  public FTError.ErrorTag  glyphPrepare() {
    Log.e(TAG, "glyphPrepare not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getLibrary ================================== */
  public FTLibraryRec getLibrary() {
    return library;
  }

  /* ==================== setLibrary ================================== */
  public void setLibrary(FTLibraryRec library) {
    this.library = library;
  }

  /* ==================== getClazz ================================== */
  public FTGlyphClassRec getClazz() {
    return (FTGlyphClassRec)this;
  }

  /* ==================== setClazz ================================== */
  public void setClazz(FTGlyphClassRec clazz) {
    Log.e(TAG, "setClazz not yet imlemented!");
  }

  /* ==================== getFormat ================================== */
  public FTTags.GlyphFormat getFormat() {
    return format;
  }

  /* ==================== setFormat ================================== */
  public void setFormat(FTTags.GlyphFormat format) {
    this.format = format;
  }

  /* ==================== getAdvance ================================== */
  public FTVectorRec getAdvance() {
    return advance;
  }

  /* ==================== setAdvance ================================== */
  public void setAdvance(FTVectorRec advance) {
    this.advance = advance;
  }

}