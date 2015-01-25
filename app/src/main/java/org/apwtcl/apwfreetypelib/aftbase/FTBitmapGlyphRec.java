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
  /*    FTBitmapGlyphRec                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used for bitmap glyph images.  This really is a        */
  /*    `sub-class' of @FTGlyphRec.                                       */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    root   :: The root @FTGlyph fields.                               */
  /*                                                                       */
  /*    left   :: The left-side bearing, i.e., the horizontal distance     */
  /*              from the current pen position to the left border of the  */
  /*              glyph bitmap.                                            */
  /*                                                                       */
  /*    top    :: The top-side bearing, i.e., the vertical distance from   */
  /*              the current pen position to the top border of the glyph  */
  /*              bitmap.  This distance is positive for upwards~y!        */
  /*                                                                       */
  /*    bitmap :: A descriptor for the bitmap.                             */
  /*                                                                       */
  /* <Note>                                                                */
  /*    You can typecast an @FTGlyph to @FTBitmapGlyph if you have       */
  /*    `glyph->format == FTGLYPH_FORMAT_BITMAP'.  This lets you access   */
  /*    the bitmap's contents easily.                                      */
  /*                                                                       */
  /*    The corresponding pixel buffer is always owned by @FTBitmapGlyph  */
  /*    and is thus created and destroyed with it.                         */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.*;

public class FTBitmapGlyphRec extends FTGlyphRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTBitmapGlyphRec";

  private int left = 0;
  private int top = 0;
  private FTBitmapRec bitmap = null;
  private FTBitmapGlyphClassRec bitmap_clazz = null;

  /* ==================== FTBitmapGlyphRec ================================== */
  public FTBitmapGlyphRec() {
    oid++;
    id = oid;
    bitmap = new FTBitmapRec();
    bitmap_clazz = (FTBitmapGlyphClassRec)((FTGlyphClassRec)this);
  }
    
  /* ==================== FTBitmapGlyphRec ================================== */
  public FTBitmapGlyphRec(FTGlyphRec glyph) {
    oid++;
    id = oid;
    bitmap = new FTBitmapRec();
FTTrace.Trace(7, TAG, "new FTBitmapGlyphRec with glyph: " + library + "!");
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
    str.append("..left: "+left+'\n');
    str.append("..top: "+top+'\n');
    return str.toString();
  }
 
  /* =====================================================================
   * FTGlyphToBitmap
   * =====================================================================
   */
  public FTError.ErrorTag FTGlyphToBitmap(int render_mode, FTReference<FTVectorRec> origin_ref, boolean destroy) {
    FTVectorRec origin;
    FTGlyphSlotRec dummy = new FTGlyphSlotRec();
    FTReference<FTGlyphSlotRec> dummy_ref = new FTReference<FTGlyphSlotRec>();
    FTSlotInternalRec dummy_internal = null;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTGlyphRec b = null;
    FTBitmapGlyphRec bitmap = null;

    if (origin_ref.Get() != null) {
      origin = origin_ref.Get();
    } else {
      origin = null;
    }
    /* when called with a bitmap glyph, do nothing and return successfully */
//FIXME!!      if (clazz == FTBitmapGlyphRec.FT_BITMAP_GLYPH_CLASS_GET) {
//        error = FT_Error.GLYPH_INVALID_ARGUMENT;
//        if (error != 0 && bitmap != null) {
//        	FTGlyphLoaderRec.FTDoneGlyph(bitmap.root);
//        }
//        return error;
//      }
System.out.println("PREP: ");
    /* we render the glyph into a glyph bitmap using a `dummy' glyph slot */
    /* then calling FTRenderGlyphInternal()                            */
    dummy.setInternal(dummy_internal); // FIXME need address!!
    dummy.setLibrary(this.library);
    dummy.setFormat(glyph_format);
    /* create result bitmap glyph */

    b = new FTGlyphRec();
    error = b.initGlyph(this.library, (FTGlyphClassRec)(new FTBitmapGlyphClassRec()));
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    bitmap = new FTBitmapGlyphRec(b);
    /* if `origin' is set, translate the glyph image */
    if (origin != null) {
        GlyphTransform(this, 0, origin);
    }
    /* prepare dummy slot for rendering */
    dummy_ref.Set(dummy);
System.out.println("prep2: ");
    error = glyphPrepare(this, dummy_ref);
    dummy = dummy_ref.Get();
    if (error == FTError.ErrorTag.ERR_OK) {
      error = FTGlyphRenderFuncs.FTRenderGlyphInternal(this.library, dummy, render_mode);
    }
    if (destroy && origin != null) {
      FTVectorRec v = new FTVectorRec();
      v.x = -origin.x;
      v.y = -origin.y;
      GlyphTransform(this, 0, v);
    }
    if (error != FTError.ErrorTag.ERR_OK) {
      if (error != FTError.ErrorTag.ERR_OK && bitmap != null) {
// FIXME FTDoneGlyph
//      	FTGlyphLoaderRec.FTDoneGlyph((FTGlyphRec)bitmap);
      }
      return error;
    }
    /* in case of success, copy the bitmap to the glyph bitmap */
    error = bitmap_clazz.ft_bitmap_glyph_init(bitmap, null);
    if (error != FTError.ErrorTag.ERR_OK) {
      if (error != FTError.ErrorTag.ERR_OK && bitmap != null) {
// FIXME FTDoneGlyph
//        FTGlyphLoaderRec.FTDoneGlyph((FTGlyphRec)bitmap);
      }
      return error;
    }
    /* copy advance */
    bitmap.advance = this.advance;
    if (destroy) {
// FIXME FTDoneGlyph
//  	  FTGlyphLoaderRec.FTDoneGlyph(this);
    }
    if (error != FTError.ErrorTag.ERR_OK && bitmap != null) {
// FIXME FTDoneGlyph
//  	  FTGlyphLoaderRec.FTDoneGlyph((FTGlyphRec)bitmap);
    }
    return error;
  }

  /* ==================== glyphInit ================================== */
  @Override
  public FTError.ErrorTag glyphInit() {
    return bitmap_clazz.ft_bitmap_glyph_init();
  }

  /* ==================== glyphDone ================================== */
  @Override
  public FTError.ErrorTag glyphDone() {
    return bitmap_clazz.ft_bitmap_glyph_done();
  }

  /* ==================== glyphCopy ================================== */
  @Override
  public FTError.ErrorTag glyphCopy () {
    return bitmap_clazz.ft_bitmap_glyph_copy();
  }

  /* ==================== glyphTransform ================================== */
  @Override
  public FTError.ErrorTag glyphTransform() {
    return bitmap_clazz.ft_bitmap_glyph_transform();
  }

  /* ==================== glyphBBox ================================== */
  @Override
  public FTError.ErrorTag  glyphBBox() {
    return bitmap_clazz.ft_bitmap_glyph_bbox();
  }

  /* ==================== glyphPrepare ================================== */
  @Override
  public FTError.ErrorTag  glyphPrepare() {
    Log.e(TAG, "glyphPrepare not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getLeft ================================== */
  public int getLeft() {
    return left;
  }

  /* ==================== setLeft ================================== */
  public void setLeft(int left) {
    this.left = left;
  }

  /* ==================== getTop ================================== */
  public int getTop() {
    return top;
  }

  /* ==================== setTop ================================== */
  public void setTop(int top) {
    this.top = top;
  }

  /* ==================== getBitmap ================================== */
  public FTBitmapRec getBitmap() {
    return bitmap;
  }

  /* ==================== setBitmap ================================== */
  public void setBitmap(FTBitmapRec bitmap) {
    this.bitmap = bitmap;
  }

}