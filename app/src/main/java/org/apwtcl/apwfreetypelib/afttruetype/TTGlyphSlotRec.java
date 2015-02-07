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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTGlyphSlotRec                                                      */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.*;
import org.apwtcl.apwfreetypelib.aftutil.*;

import java.util.Set;

public class TTGlyphSlotRec extends FTGlyphSlotRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyphSlotRec";

  /* ==================== TTGlyphSLotRec ================================== */
  public TTGlyphSlotRec() {
    super();
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

  /* =====================================================================
   *
   *    TT_Load_Glyph
   *
   * <Description>
   *    A function used to load a single glyph within a given glyph slot,
   *    for a given size.
   *
   * <Input>
   *    glyph       :: A handle to a target slot object where the glyph
   *                   will be loaded.
   *
   *    size        :: A handle to the source face size at which the glyph
   *                   must be scaled/loaded.
   *
   *    glyph_index :: The index of the glyph in the font file.
   *
   *    load_flags  :: A flag indicating what to load for this glyph.  The
   *                   FT_LOAD_XXX constants can be used to control the
   *                   glyph loading process (e.g., whether the outline
   *                   should be scaled, whether to load bitmaps or not,
   *                   whether to hint the outline, etc).
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTLoadGlyph(TTSizeRec ttsize, int glyph_index, Set<Flags.Load> load_flags) {
    FTError.ErrorTag error;
    TTLoaderRec loader;
    FTReference<FTOutlineRec> outline_ref = new FTReference<FTOutlineRec>();

    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TT_Load_Glyph: glyph_index: %d size: " + ttsize, glyph_index));
    error = FTError.ErrorTag.ERR_OK;
      /* if FT_LOAD_NO_SCALE is not set, `ttmetrics' must be valid */
    if (!load_flags.contains(Flags.Load.NO_SCALE) && ttsize.getTtmetrics().isValid() == false) {
      error = FTError.ErrorTag.LOAD_INVALID_SIZE_HANDLE;
      return error;
    }
    if (load_flags.contains(Flags.Load.SBITS_ONLY)) {
      error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
      return error;
    }
    loader = new TTLoaderRec();
    error = loader.tt_loader_init(ttsize, this, load_flags, false);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    format = FTTags.GlyphFormat.OUTLINE;
    num_subglyphs = 0;
    outline.setFlags(0);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "load_truetype_glyph");
      /* main loading loop */
    error = loader.load_truetype_glyph(glyph_index, 0, false);
    if (error == FTError.ErrorTag.ERR_OK) {
      if (format == FTTags.GlyphFormat.COMPOSITE) {
        num_subglyphs = loader.getGloader().getBase().getNum_subglyphs();
        subglyphs = loader.getGloader().getBase().getSubglyphs();
      } else {
        outline = loader.getGloader().getBase();
        outline.setFlags(outline.getFlags() & ~Flags.Outline.SINGLE_PASS.getVal());
          /* Translate array so that (0,0) is the glyph's origin.  Note  */
          /* that this behaviour is independent on the value of bit 1 of */
          /* the `flags' field in the `head' table -- at least major     */
          /* applications like Acroread indicate that.                   */
loader.getBase().showLoaderZone("TTLoadGlyph", null);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader.pp1.x: %d", loader.getPp1().getX()));
        int i;
        for (i = 0; i < loader.getGloader().getCurrent().getN_points() + 4; i++) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("PP1: i: %d ipoints_idx: %d x: %d y: %d\n", i, loader.getGloader().getCurrent().getPoints_idx(), loader.getGloader().getCurrent().getPoint(i).getX(),
              loader.getGloader().getCurrent().getPoint(i).getY()));
        }
        if (loader.getPp1().getX() != 0) {
          outline.OutlineTranslate(-loader.getPp1().getX(), 0);
        }
      }

      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "IS_HINTED: "+!load_flags.contains(Flags.Load.NO_HINTING));
      if (!load_flags.contains(Flags.Load.NO_HINTING)) {
        if (loader.getExec().graphics_state.isScan_control()) {
            /* convert scan conversion mode to FT_OUTLINE_XXX flags */
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("IS_HINTED2: %d", loader.getExec().graphics_state.getScan_type()));
          switch (loader.getExec().graphics_state.getScan_type()) {
            case 0: /* simple drop-outs including stubs */
              outline.setFlags(outline.getFlags() | Flags.Outline.INCLUDE_STUBS.getVal());
              break;
            case 1: /* simple drop-outs excluding stubs */
              /* nothing; it's the default rendering mode */
              break;
            case 4: /* smart drop-outs including stubs */
              outline.setFlags(outline.getFlags() | Flags.Outline.SMART_DROPOUTS.getVal() |
                  Flags.Outline.INCLUDE_STUBS.getVal());
              break;
            case 5: /* smart drop-outs excluding stubs  */
              outline.setFlags(outline.getFlags() | Flags.Outline.SMART_DROPOUTS.getVal());
              break;
            default: /* no drop-out control */
              outline.setFlags(outline.getFlags() | Flags.Outline.IGNORE_DROPOUTS.getVal());
              break;
          }
        } else {
          outline.setFlags(outline.getFlags() | Flags.Outline.IGNORE_DROPOUTS.getVal());
        }
      }
      loader.compute_glyph_metrics(glyph_index);
    }
      /* Set the `high precision' bit flag.                           */
      /* This is _critical_ to get correct output for monochrome      */
      /* TrueType glyphs at all sizes using the bytecode interpreter. */
      /*                                                              */
    if (!load_flags.contains(Flags.Load.NO_SCALE) && ttsize.getMetrics().getY_ppem() < 24) {
      outline.setFlags(outline.getFlags() | Flags.Outline.HIGH_PRECISION.getVal());
    }
    return error;
  }

}