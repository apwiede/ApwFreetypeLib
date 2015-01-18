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
  /*    FTSlotInternalRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure contains the internal fields of each FT_GlyphSlot   */
  /*    object.  These fields may change between different releases of     */
  /*    FreeType.                                                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    loader            :: The glyph loader object used to load outlines */
  /*                         into the glyph slot.                          */
  /*                                                                       */
  /*    flags             :: Possible values are zero or                   */
  /*                         FT_GLYPH_OWN_BITMAP.  The latter indicates    */
  /*                         that the FT_GlyphSlot structure owns the      */
  /*                         bitmap buffer.                                */
  /*                                                                       */
  /*    glyph_transformed :: Boolean.  Set to TRUE when the loaded glyph   */
  /*                         must be transformed through a specific        */
  /*                         font transformation.  This is _not_ the same  */
  /*                         as the face transform set through             */
  /*                         FT_Set_Transform().                           */
  /*                                                                       */
  /*    glyph_matrix      :: The 2x2 matrix corresponding to the glyph     */
  /*                         transformation, if necessary.                 */
  /*                                                                       */
  /*    glyph_delta       :: The 2d translation vector corresponding to    */
  /*                         the glyph transformation, if necessary.       */
  /*                                                                       */
  /*    glyph_hints       :: Format-specific glyph hints management.       */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTSlotInternalRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSlotInternalRec";

  private FTGlyphLoaderRec loader = null;
  private FTTags.GlyphFormat flags = FTTags.GlyphFormat.NONE;
  private boolean glyph_transformed = false;
  private FTMatrixRec glyph_matrix = null;
  private FTVectorRec glyph_delta = null;
  private Object[] glyph_hints = null;

  /* ==================== FTSlotInternalRec ================================== */
  public FTSlotInternalRec() {
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
    str.append("..flags: "+flags+'\n');
    str.append("..glyph_transformed: "+glyph_transformed+'\n');
    return str.toString();
  }

  /* ==================== getLoader ================================== */
  public FTGlyphLoaderRec getLoader() {
    return loader;
  }

  /* ==================== setLoader ================================== */
  public void setLoader(FTGlyphLoaderRec loader) {
    this.loader = loader;
  }

  /* ==================== getFlags ================================== */
  public FTTags.GlyphFormat getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(FTTags.GlyphFormat flags) {
    this.flags = flags;
  }

  /* ==================== isGlyph_transformed ================================== */
  public boolean isGlyph_transformed() {
    return glyph_transformed;
  }

  /* ==================== setGlyph_transformed ================================== */
  public void setGlyph_transformed(boolean glyph_transformed) {
    this.glyph_transformed = glyph_transformed;
  }

  /* ==================== getGlyph_matrix ================================== */
  public FTMatrixRec getGlyph_matrix() {
    return glyph_matrix;
  }

  /* ==================== setGlyph_matrix ================================== */
  public void setGlyph_matrix(FTMatrixRec glyph_matrix) {
    this.glyph_matrix = glyph_matrix;
  }

  /* ==================== getGlyph_delta ================================== */
  public FTVectorRec getGlyph_delta() {
    return glyph_delta;
  }

  /* ==================== setGlyph_delta ================================== */
  public void setGlyph_delta(FTVectorRec glyph_delta) {
    this.glyph_delta = glyph_delta;
  }

  /* ==================== getGlyph_hints ================================== */
  public Object[] getGlyph_hints() {
    return glyph_hints;
  }

  /* ==================== setGlyph_hints ================================== */
  public void setGlyph_hints(Object[] glyph_hints) {
    this.glyph_hints = glyph_hints;
  }

}