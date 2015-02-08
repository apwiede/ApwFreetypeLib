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
  /*    FTGlyphSlotRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftraster.FTGrayOutlineClass;
import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphSlotRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

import java.util.Set;

public class FTGlyphSlotRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGlyphSlotRec";

  protected FTLibraryRec library = null;
  protected FTFaceRec face = null;
  protected FTGlyphSlotRec next;
  protected FTGlyphMetricsRec metrics = null;
  protected int linearHoriAdvance = 0;
  protected int linearVertAdvance = 0;
  protected FTVectorRec advance = null;
  protected FTTags.GlyphFormat format = FTTags.GlyphFormat.NONE;
  protected FTBitmapRec bitmap = null;
  protected int bitmap_left = 0;
  protected int bitmap_top = 0;
  protected FTOutlineRec outline = null;
  protected int num_subglyphs = 0;
  protected FTSubGlyphRec[] subglyphs = null;
  protected Object control_data;
  protected int control_len = 0;
  protected int lsb_delta = 0;
  protected int rsb_delta = 0;
  protected Object other = null;
  protected FTSlotInternalRec internal = null;

  protected FTTags.RasterType raster_type;

  /* ==================== FTGlyphSlotRec ================================== */
  public FTGlyphSlotRec() {
    oid++;
    id = oid;

    metrics = new FTGlyphMetricsRec();
    advance = new FTVectorRec();
    bitmap = new FTBitmapRec();
//    outline = new FTOutlineRec();
    subglyphs = new FTSubGlyphRec[5];
    internal = new FTSlotInternalRec();
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
    str.append("..linearHoriAdvance: "+linearHoriAdvance+'\n');
    str.append("..linearVertAdvance: "+linearVertAdvance+'\n');
    str.append("..advance: "+advance.getX()+" "+advance.getY()+'\n');
    str.append("..format: "+format+'\n');
    str.append("..bitmap_left: "+bitmap_left+'\n');
    str.append("..bitmap_top: "+bitmap_top+'\n');
    str.append("..num_subglyphs: "+num_subglyphs+'\n');
    str.append("..control_len: "+control_len+'\n');
    str.append("..lsb_delta: "+lsb_delta+'\n');
    str.append("..rsb_delta: "+rsb_delta+'\n');
    return str.toString();
  }

  /* =====================================================================
   * ft_glyphslot_init
   * =====================================================================
   */
  private FTError.ErrorTag ft_glyphslot_init() {
    FTDriverRec driver = face.getDriver();
    FTDriverClassRec clazz = driver.getDriver_clazz();
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    library = driver.library;
    internal = new FTSlotInternalRec();
    if (!driver.module_clazz.module_flags.contains(Flags.Module.DRIVER_NO_OUTLINES)) {
      FTReference<FTGlyphLoaderRec> loader_ref = new FTReference<FTGlyphLoaderRec>();
      loader_ref.Set(internal.getLoader());
      switch(face.getDriver().getDriver_clazz().module_type) {
        case TT_DRIVER:
          internal.setLoader(face.getDriver().getGlyph_loader());
          break;
        default:
          return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
      }
      if (internal.getLoader() == null) {
        error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
      }
    }
    if (error == FTError.ErrorTag.ERR_OK) {
      error = clazz.initSlot(this);
    }
    return error;
  }

  /* =====================================================================
   * ft_glyphslot_free_bitmap
   * =====================================================================
   */
  private void ft_glyphslot_free_bitmap() {
    if ((internal != null) &&
        (internal.getFlags().getVal() & FTTags.GlyphFormat.OWN_BITMAP.getVal()) != 0) {
//        FT_FREE( slot.bitmap.buffer );
      internal.setFlags(FTTags.GlyphFormat.getTableTag(internal.getFlags().getVal() & ~FTTags.GlyphFormat.OWN_BITMAP.getVal()));
    } else {
        /* assume that the bitmap buffer was stolen or not */
        /* allocated from the heap                         */
      bitmap.setBuffer(null);
    }
  }

  /* =====================================================================
   * ft_glyphslot_clear
   * =====================================================================
   */
  public void ft_glyphslot_clear() {
    FTGlyphSlotRec slot = null;

      /* free bitmap if needed */
    this.ft_glyphslot_free_bitmap();
      /* clear all public fields in the glyph slot */
    metrics.clear();
    outline.clear();
    bitmap.setWidth(0);
    bitmap.setRows(0);
    bitmap.setPitch(0);
    bitmap.setPixel_mode(FTTags.PixelMode.NONE);
      /* `slot->bitmap.buffer' has been handled by ft_glyphslot_free_bitmap */
    bitmap_left = 0;
    bitmap_top = 0;
    num_subglyphs =0;
    subglyphs = null;
    control_data = 0;
    control_len = 0;
    other = 0;
    format = FTTags.GlyphFormat.NONE;
    linearHoriAdvance = 0;
    linearVertAdvance = 0;
    lsb_delta = 0;
    rsb_delta = 0;
  }

  /* =====================================================================
   * ft_glyphslot_done
   * =====================================================================
   */
  public FTError.ErrorTag ft_glyphslot_done() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTDriverRec driver = face.getDriver();

    Debug(0, DebugTag.DBG_INIT, TAG, "ft_glyphslot_done");
    if (internal != null) {
      /* free glyph loader */
      if (!driver.getDriver_clazz().module_flags.contains(Flags.Module.DRIVER_NO_OUTLINES)) {
        internal.getLoader().FTGlyphLoaderDone();
        internal.setLoader(null);
      }
    }
    return error;
  }

  /* =====================================================================
   * FTNewGlyphSlot
   * =====================================================================
   */
  public static FTError.ErrorTag FTNewGlyphSlot(FTFaceRec face, FTReference<FTGlyphSlotRec> slot_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTGlyphSlotRec slot = null;

Debug(0, DebugTag.DBG_INIT, TAG, "FTNewGlyphSlot");
    if (face  == null|| face.getDriver() == null) {
Debug(0, DebugTag.DBG_INIT, TAG, "FTNewGlyphSlot inv arg");
      return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    FTTrace.Trace(7, TAG, "FTNewGlyphSlot: Creating new slot object");
    switch(face.getDriver().getDriver_clazz().module_type) {
      case TT_DRIVER:
        slot = new TTGlyphSlotRec();
        break;
      default:
        return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    slot.face = face;
    error = slot.ft_glyphslot_init();
    if (error != FTError.ErrorTag.ERR_OK) {
      slot.ft_glyphslot_done();
      slot_ref.Set(null);
//          FT_FREE( slot );
      FTTrace.Trace(7, TAG, String.format("FTNewGlyphSlot: Return %d", error));
      return error;
    }
    slot.next = face.getGlyph();
    face.setGlyph(slot);
    if (slot_ref != null) {
      slot_ref.Set(slot);
    }
    FTTrace.Trace(7, TAG, "FTNewGlyphSlot: Return "+error);
    return error;
  }

  /* =====================================================================
   *    FTLoadGlyph
   *
   * <Description>
   *    A function used to load a single glyph into the glyph slot of a
   *    face object.
   *
   * <InOut>
   *    face        :: A handle to the target face object where the glyph
   *                   is loaded.
   *
   * <Input>
   *    glyph_index :: The index of the glyph in the font file.  For
   *                   CID-keyed fonts (either in PS or in CFF format)
   *                   this argument specifies the CID value.
   *
   *    load_flags  :: A flag indicating what to load for this glyph.  The
   *                   @FT_LOAD_XXX constants can be used to control the
   *                   glyph loading process (e.g., whether the outline
   *                   should be scaled, whether to load bitmaps or not,
   *                   whether to hint the outline, etc).
   *
   * <Return>
   *    FreeType error code.  0~means success.
   *
   * <Note>
   *    The loaded glyph may be transformed.  See @FT_Set_Transform for
   *    the details.
   *
   *    For subsetted CID-keyed fonts, `FT_Err_Invalid_Argument' is
   *    returned for invalid CID values (this is, for CID values which
   *    don't have a corresponding glyph in the font).  See the discussion
   *    of the @FT_FACE_FLAG_CID_KEYED flag for more details.
   *
   * =====================================================================
   */
  public FTError.ErrorTag LoadGlyph(int glyph_index, Set<Flags.Load> load_flags) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTDriverRec driver = null;
    FTLibraryRec library;
    boolean autohint = false;
    FTModuleRec hinter;
    boolean Load_Ok = false;
    TTFaceRec ttface = (TTFaceRec)face;

    if (face == null || face.getSize() == null || face.getGlyph() == null) {
      return FTError.ErrorTag.LOAD_INVALID_FACE_HANDLE;
    }
    /* The validity test for `glyph_index' is performed by the */
    /* font drivers.                                           */
    ft_glyphslot_clear();
    driver  = face.getDriver();
    library = driver.library;
    hinter  = library.getAuto_hinter();
    /* resolve load flags dependencies */
    if (load_flags.contains(Flags.Load.NO_RECURSE)) {
      load_flags.add(Flags.Load.NO_SCALE);
      load_flags.add(Flags.Load.IGNORE_TRANSFORM);
    }
    if (load_flags.contains(Flags.Load.NO_SCALE)) {
      load_flags.add(Flags.Load.NO_HINTING);
      load_flags.add(Flags.Load.NO_BITMAP);
      load_flags.remove(Flags.Load.RENDER);
    }
    /*
     * Determine whether we need to auto-hint or not.
     * The general rules are:
     *
     * - Do only auto-hinting if we have a hinter module, a scalable font
     *   format dealing with outlines, and no transforms except simple
     *   slants and/or rotations by integer multiples of 90 degrees.
     *
     * - Then, auto-hint if FT_LOAD_FORCE_AUTOHINT is set or if we don't
     *   have a native font hinter.
     *
     * - Otherwise, auto-hint for LIGHT hinting mode or if there isn't
     *   any hinting bytecode in the TrueType/OpenType font.
     *
     * - Exception: The font is `tricky' and requires the native hinter to
     *   load properly.
     */
    if (hinter != null &&
        (!load_flags.contains(Flags.Load.NO_HINTING)) &&
        (!load_flags.contains(Flags.Load.NO_AUTOHINT)) &&
        (driver.getDriver_clazz().module_flags.contains(Flags.Module.DRIVER_SCALABLE)) &&
        (!driver.getDriver_clazz().module_flags.contains(Flags.Module.DRIVER_NO_OUTLINES)) &&
        (!face.getFace_flags().contains(Flags.Face.TRICKY)) &&
        ((load_flags.contains(Flags.Load.IGNORE_TRANSFORM)) ||
            (face.getInternal().getTransform_matrix().getYx() == 0 && face.getInternal().getTransform_matrix().getXx() != 0) ||
            (face.getInternal().getTransform_matrix().getXx() == 0 && face.getInternal().getTransform_matrix().getYx() != 0))) {
      if ((load_flags.contains(Flags.Load.FORCE_AUTOHINT)) ||
          (!driver.getDriver_clazz().module_flags.contains(Flags.Module.DRIVER_HAS_HINTER))) {
        autohint = true;
      } else {
        int mode = ((Flags.Load.LoadSetToInt(load_flags) >> 16 ) & 15);
        /* the check for `num_locations' assures that we actually    */
        /* test for instructions in a TTF and not in a CFF-based OTF */
        if ((mode == FTTags.RenderMode.LIGHT.getVal()) ||
            face.getInternal().isIgnore_unpatented_hinter() ||
            (face.getFace_flags().contains(Flags.Face.SFNT) &&
                ttface.getLoca_table().getNum_locations() != 0 &&
                ttface.getMax_profile().getMaxSizeOfInstructions() == 0)) {
          autohint = true;
        }
      }
    }
    if (autohint) {
        FTAutoHinterInterfaceClass hinting;

        /* try to load embedded bitmaps first if available            */
        /*                                                            */
        /* XXX: This is really a temporary hack that should disappear */
        /*      promptly with FreeType 2.1!                           */
        /*                                                            */
        if (face.getFace_flags().contains(Flags.Face.FIXED_SIZES) &&
            (!load_flags.contains(Flags.Load.NO_BITMAP))) {
          Set<Flags.Load> my_load_flags = load_flags;
          my_load_flags.add(Flags.Load.SBITS_ONLY);
          error = driver.driver_clazz.loadGlyph(this, face.getSize(),
                 glyph_index, my_load_flags);
          if ((error != FTError.ErrorTag.ERR_OK )&& format == FTTags.GlyphFormat.BITMAP) {
            Load_Ok = true;
          }
        }
        if (! Load_Ok) {
          FTFaceInternalRec internal = face.getInternal();
          int transform_flags = internal.getTransform_flags();

          /* since the auto-hinter calls FT_Load_Glyph by itself, */
          /* make sure that glyphs aren't transformed             */
          internal.setTransform_flags(0);
          /* load auto-hinted outline */
          hinting = (FTAutoHinterInterfaceClass)hinter.module_clazz.module_interface;
          error = hinting.loadGlyph(hinter, this, face.getSize(), glyph_index, load_flags);
          internal.setTransform_flags(transform_flags);
        }
    } else {
      error = driver.getDriver_clazz().loadGlyph(this, face.getSize(), glyph_index, load_flags);
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FT_Load_Glyph: slot->format is outline: "+(format == FTTags.GlyphFormat.OUTLINE));
      if (format == FTTags.GlyphFormat.OUTLINE) {
        /* check that the loaded outline is correct */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("CHKOUTLINE: %d", outline.n_points));
        error = outline.FTOutlineCheck();
        if (error != FTError.ErrorTag.ERR_OK) {
          return error;
        }
      }
    }
    /* compute the advance */
    if (load_flags.contains(Flags.Load.VERTICAL_LAYOUT)) {
      advance.setX(0);
      advance.setY(metrics.getVertAdvance());
    } else {
      advance.setX(metrics.getHoriAdvance());
      advance.setY(0);
    }
    /* compute the linear advance in 16.16 pixels */
    if ((!load_flags.contains(Flags.Load.LINEAR_DESIGN)) && (face.getFace_flags().contains(Flags.Face.SCALABLE))) {
      FTSizeMetricsRec metrics = face.getSize().metrics;

      /* it's tricky! */
      linearHoriAdvance = FTCalc.FT_MulDiv(linearHoriAdvance, metrics.getX_scale(), 64);
      linearVertAdvance = FTCalc.FT_MulDiv(linearVertAdvance, metrics.getY_scale(), 64);
    }

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("FT_LOAD_IGNORE_TRANSFORM: %b", load_flags.contains(Flags.Load.IGNORE_TRANSFORM)));
    if (!load_flags.contains(Flags.Load.IGNORE_TRANSFORM)) {
      FTFaceInternalRec internal = face.getInternal();
      FTReference<FTMatrixRec> matrix_ref = new FTReference<FTMatrixRec>();
      FTReference<FTVectorRec> delta_ref = new FTReference<FTVectorRec>();

      /* now, transform the glyph image if needed */
      if (internal.getTransform_flags() != 0) {
        /* get renderer */
        FTRendererRec renderer = driver.getGlyph_loader().ft_lookup_glyph_renderer(this);
        if (renderer != null) {
          matrix_ref.Set(internal.getTransform_matrix());
          delta_ref.Set(internal.getTransform_delta());
          error = renderer.clazz.transformGlyph(renderer, this, matrix_ref, delta_ref);
          internal.setTransform_matrix(matrix_ref.Get());
          internal.setTransform_delta(delta_ref.Get());
        } else {
          if (format == FTTags.GlyphFormat.OUTLINE) {
            /* apply `standard' transformation if no renderer is available */
            if ((internal.getTransform_flags() & 1) != 0) {
              Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "standard outline transform");
              outline.OutlineTransform(internal.getTransform_matrix());
            }
            if ((internal.getTransform_flags() & 2) != 0) {
              outline.OutlineTranslate(internal.getTransform_delta().getX(), internal.getTransform_delta().getY());
            }
          }
        }
        /* transform advance */
        outline.VectorTransform(advance, internal.getTransform_matrix());
        internal.setTransform_matrix(matrix_ref.Get());
      }
    }
    FTTrace.Trace(7, TAG, String.format("  x advance: %d", advance.getX()));
    FTTrace.Trace(7, TAG, String.format("  y advance: %d", advance.getY()));
    FTTrace.Trace(7, TAG, String.format("  linear x advance: %d", linearHoriAdvance));
    FTTrace.Trace(7, TAG, String.format("  linear y advance: %d", linearVertAdvance));
    /* do we need to render the image now? */
    if (error == FTError.ErrorTag.ERR_OK &&
        (format != FTTags.GlyphFormat.BITMAP) &&
        (format != FTTags.GlyphFormat.COMPOSITE) &&
        load_flags.contains(Flags.Load.RENDER)) {
      int mode = ((Flags.Load.LoadSetToInt(load_flags) >> 16) & 15);

      if ((mode == FTTags.RenderMode.NORMAL.getVal()) &&
          load_flags.contains(Flags.Load.MONOCHROME)) {
        mode = FTTags.RenderMode.MONO.getVal();
      }
      error = this.FTRenderGlyph(mode);
    }
    return error;
  }

  /* =====================================================================
   * DoneGlyphSlot
   * =====================================================================
   */
  public FTError.ErrorTag DoneGlyphSlot() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTGlyphSlotRec prev;
    FTGlyphSlotRec cur;

    /* Remove slot from its parent face's list */
    prev = null;
    cur = face.getGlyph();
    while (cur != null) {
      if (cur == this) {
        if (prev == null) {
          face.setGlyph(cur.next);
        } else {
          prev.next = cur.next;
        }
        ft_glyphslot_done();
        break;
      }
      prev = cur;
      cur = cur.next;
    }
    return error;
  }

  /* =====================================================================
   * FTRenderGlyphInternal
   * =====================================================================
   */
  public FTError.ErrorTag FTRenderGlyphInternal(FTLibraryRec library, int render_mode) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTRendererRec renderer;
    FTReference<FTListNodeRec> node_ref = new FTReference<FTListNodeRec>();

Debug(0, DebugTag.DBG_RENDER, TAG, "FTRenderGlyphInternal");
    /* if it is already a bitmap, no need to do anything */
    if (this.format == FTTags.GlyphFormat.BITMAP) {
      /* already a bitmap, don't do anything */
    } else {
      FTListNodeRec node = null;
      boolean update = false;

      /* small shortcut for the very common case */
      if (this.format == FTTags.GlyphFormat.OUTLINE) {
        renderer = library.getCur_renderer();
        node = library.getRenderers().head;
      } else {
        node_ref.Set(node);
        renderer = library.getCur_renderer().FTLookupRenderer(library, this.format, node_ref);
        Debug(0, DebugTag.DBG_RENDER, TAG, "renderer name: "+renderer.clazz.module_name);
        node = node_ref.Get();
      }
      error = FTError.ErrorTag.GLYPH_UNIMPLEMENTED_FEATURE;
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer 0: "+renderer+"!");
      while (renderer != null) {
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer 1: "+renderer+"!"+this.bitmap+"!");
        error = renderer.render(renderer, this, render_mode, null);
        if (error == FTError.ErrorTag.ERR_OK || error != FTError.ErrorTag.GLYPH_CANNOT_RENDER_GLYPH) {
          break;
        }
        /* FT_Err_Cannot_Render_Glyph is returned if the render mode   */
        /* is unsupported by the current renderer for this glyph image */
        /* format.                                                     */
        /* now, look for another renderer that supports the same */
        /* format.                                               */
        node_ref.Set(node);
        renderer = library.getCur_renderer().FTLookupRenderer(library, this.format, node_ref);
        if (renderer != null && renderer.clazz != null) {
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer name: "+renderer.clazz.module_name);
        }
        node = node_ref.Get();
        update = true;
      }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert0d: rows %d width %d pitch %d num_grays %d", this.bitmap.getRows(), this.bitmap.getWidth(), this.bitmap.getPitch(), this.bitmap.getNum_grays()));
      /* if we changed the current renderer for the glyph image format */
      /* we need to select it as the next current one                  */
      if (error == FTError.ErrorTag.ERR_OK && update && renderer != null) {
        FTRendererRec.FTSetRenderer(library, renderer, 0, null);
      }
    }
    /* we convert to a single bitmap format for computing the checksum */
    {
      FTBitmapRec bitmap = new FTBitmapRec();
      FTError.ErrorTag err;
      FTReference<FTBitmapRec> bitmap_ref = new FTReference<FTBitmapRec>();

      bitmap_ref.Set(bitmap);
      StringBuffer str = new StringBuffer("");
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert: rows %d width %d pitch %d num_grays %d", bitmap.getRows(), bitmap.getWidth(), bitmap.getPitch(), bitmap.getNum_grays()));
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert slot: rows %d width %d pitch %d num_grays %d", this.bitmap.getRows(), this.bitmap.getWidth(), this.bitmap.getPitch(), this.bitmap.getNum_grays()));
      err = this.bitmap.Convert(library, bitmap, 1);
      Debug(0, DebugTag.DBG_RENDER, TAG, String.format("after BITMAP convert: rows %d width %d pitch %d num_grays %d", bitmap.getRows(), bitmap.getWidth(), bitmap.getPitch(), bitmap.getNum_grays()));
      bitmap = bitmap_ref.Get();
      int r;
      int c;
//Debug(0, DebugTag.DBG_RENDER, TAG, String.format("++ Bitmapconv char: %c %d rows: %d width: %d",
//    FTDemoHandle.currCharacter, FTDemoHandle.currGIndex, bitmap.rows, bitmap.width));
      str = new StringBuffer("");
      for(r = 0; r < bitmap.getRows(); r++) {
        str.delete(0,  str.length());
        str.append(String.format("%02d: ", r));
        for(c = 0; c < bitmap.getWidth(); c++) {
          str.append(String.format("%02x", bitmap.getBuffer()[r+c]));
        }
        Debug(0, DebugTag.DBG_RENDER, TAG, str.toString());
      }
      if (err == FTError.ErrorTag.ERR_OK) {
        MD5CTX ctx = null;
        byte[] md5 = new byte[16];
        int i;

        ctx = new MD5CTX();
        ctx.MD5Init();
        FTReference<Object> buffer_ref = new FTReference<Object>();
        buffer_ref.Set(bitmap.getBuffer());
        ctx.MD5Update(buffer_ref, (long) (bitmap.getRows() * bitmap.getPitch()));
        bitmap.setBuffer((byte[]) buffer_ref.Get());
        ctx.MD5Final(md5);
        str = new StringBuffer(String.format("MD5 checksum for %dx%d bitmap:  ", bitmap.getRows(), bitmap.getPitch()));
        for (i = 0; i < 16; i++) {
          str.append(String.format("%02X", md5[i]));
        }
        FTTrace.Trace(7, TAG, str.toString());
      }
      bitmap.Done(library);
    }
    return error;
  }

  /* =====================================================================
   * FTRenderGlyph
   * =====================================================================
   */
  public FTError.ErrorTag FTRenderGlyph(int render_mode) {
    Debug(0, DebugTag.DBG_RENDER, TAG, "FTRenderGlyph");
    FTLibraryRec library;

    if (this.face == null) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    library = this.face.getDriver().library;
    return FTRenderGlyphInternal(library, render_mode);
  }

  /* ==================== getLibrary ================================== */
  public FTLibraryRec getLibrary() {
    return library;
  }

  /* ==================== setLibrary ================================== */
  public void setLibrary(FTLibraryRec library) {
    this.library = library;
  }

  /* ==================== getFace ================================== */
  public FTFaceRec getFace() {
    return face;
  }

  /* ==================== setFace ================================== */
  public void setFace(FTFaceRec face) {
    this.face = face;
  }

  /* ==================== getNext ================================== */
  public FTGlyphSlotRec getNext() {
    return next;
  }

  /* ==================== setNext ================================== */
  public void setNext(FTGlyphSlotRec next) {
    this.next = next;
  }

  /* ==================== getMetrics ================================== */
  public FTGlyphMetricsRec getMetrics() {
    return metrics;
  }

  /* ==================== setMetrics ================================== */
  public void setMetrics(FTGlyphMetricsRec metrics) {
    this.metrics = metrics;
  }

  /* ==================== getLinearHoriAdvance ================================== */
  public int getLinearHoriAdvance() {
    return linearHoriAdvance;
  }

  /* ==================== setLinearHoriAdvance ================================== */
  public void setLinearHoriAdvance(int linearHoriAdvance) {
    this.linearHoriAdvance = linearHoriAdvance;
  }

  /* ==================== getLinearVertAdvance ================================== */
  public int getLinearVertAdvance() {
    return linearVertAdvance;
  }

  /* ==================== setLinearVertAdvance ================================== */
  public void setLinearVertAdvance(int linearVertAdvance) {
    this.linearVertAdvance = linearVertAdvance;
  }

  /* ==================== getAdvance ================================== */
  public FTVectorRec getAdvance() {
    return advance;
  }

  /* ==================== setAdvance ================================== */
  public void setAdvance(FTVectorRec advance) {
    this.advance = advance;
  }

  /* ==================== getFormat ================================== */
  public FTTags.GlyphFormat getFormat() {
    return format;
  }

  /* ==================== setFormat ================================== */
  public void setFormat(FTTags.GlyphFormat format) {
    this.format = format;
  }

  /* ==================== getBitmap ================================== */
  public FTBitmapRec getBitmap() {
    return bitmap;
  }

  /* ==================== setBitmap ================================== */
  public void setBitmap(FTBitmapRec bitmap) {
    this.bitmap = bitmap;
  }

  /* ==================== getBitmap_left ================================== */
  public int getBitmap_left() {
    return bitmap_left;
  }

  /* ==================== setBitmap_left ================================== */
  public void setBitmap_left(int bitmap_left) {
    this.bitmap_left = bitmap_left;
  }

  /* ==================== getBitmap_top ================================== */
  public int getBitmap_top() {
    return bitmap_top;
  }

  /* ==================== setBitmap_top ================================== */
  public void setBitmap_top(int bitmap_top) {
    this.bitmap_top = bitmap_top;
  }

  /* ==================== getOutline ================================== */
  public FTOutlineRec getOutline() {
    return outline;
  }

  /* ==================== setOutline ================================== */
  public void setOutline(FTOutlineRec outline) {
    this.outline = outline;
  }

  /* ==================== getNum_subglyphs ================================== */
  public int getNum_subglyphs() {
    return num_subglyphs;
  }

  /* ==================== setNum_subglyphs ================================== */
  public void setNum_subglyphs(int num_subglyphs) {
    this.num_subglyphs = num_subglyphs;
  }

  /* ==================== getSubglyphs ================================== */
  public FTSubGlyphRec[] getSubglyphs() {
    return subglyphs;
  }

  /* ==================== setSubglyphs ================================== */
  public void setSubglyphs(FTSubGlyphRec[] subglyphs) {
    this.subglyphs = subglyphs;
  }

  /* ==================== getControl_data ================================== */
  public Object getControl_data() {
    return control_data;
  }

  /* ==================== setControl_data ================================== */
  public void setControl_data(Object control_data) {
    this.control_data = control_data;
  }

  /* ==================== getControl_len ================================== */
  public int getControl_len() {
    return control_len;
  }

  /* ==================== setControl_len ================================== */
  public void setControl_len(int control_len) {
    this.control_len = control_len;
  }

  /* ==================== getLsb_delta ================================== */
  public int getLsb_delta() {
    return lsb_delta;
  }

  /* ==================== setLsb_delta ================================== */
  public void setLsb_delta(int lsb_delta) {
    this.lsb_delta = lsb_delta;
  }

  /* ==================== getRsb_delta ================================== */
  public int getRsb_delta() {
    return rsb_delta;
  }

  /* ==================== setRsb_delta ================================== */
  public void setRsb_delta(int rsb_delta) {
    this.rsb_delta = rsb_delta;
  }

  /* ==================== getOther ================================== */
  public Object getOther() {
    return other;
  }

  /* ==================== setOther ================================== */
  public void setOther(Object other) {
    this.other = other;
  }

  /* ==================== getInternal ================================== */
  public FTSlotInternalRec getInternal() {
    return internal;
  }

  /* ==================== setInternal ================================== */
  public void setInternal(FTSlotInternalRec internal) {
    this.internal = internal;
  }

}