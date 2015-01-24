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
  /*    TTGlyfRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType glyf table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftsfnt.TTLoad;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTGlyfRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyf";


  /* the following fields are not in the file */
  /* TTGlyphRec is filled with the contents of a single glyph */
  private TTGlyphRec[] glyphs = null;
  private int glyphs_max_index = 0;
  private int num_glyphs = 0;
  private int glyf_len = 0;
  private long glyf_table_offset = 0L;

  /* ==================== TTGlyfRec ================================== */
  public TTGlyfRec() {
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
    str.append("..num_glyphs: "+num_glyphs+'\n');
    str.append("..glyf_len: "+glyf_len+'\n');
    str.append("..glyf_table_offset: "+glyf_table_offset+'\n');
    return str.toString();
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: glyf offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();
    int shift = 0;
    int num_locations = 0;

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.glyf, stream, length_ref);
    length = length_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK && error != FTError.ErrorTag.GLYPH_TABLE_MISSING) {
      glyf_len = 0;
    } else {
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      glyf_len = length;
    }

    glyf_table_offset = stream.pos();
    num_locations = ttface.getLoca_table().getNum_locations();
    if (num_locations != ttface.getNum_glyphs() + 1) {
      FTTrace.Trace(7, TAG, String.format("glyph count mismatch!  loca: %d, maxp: %d",
          num_locations - 1, ttface.getNum_glyphs()));
    }
    num_glyphs = ttface.getNum_glyphs();
    glyphs = new TTGlyphRec[num_glyphs];
    FTTrace.Trace(7, TAG, "loaded");
    return error;
  }

  /* ==================== LoadGlyph ================================== */
  FTError.ErrorTag LoadGlyph(FTStreamRec stream, TTFaceRec ttface, int gindex) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTGlyphRec glyph = null;

    if (gindex < 0 || gindex > num_glyphs) {
      error = FTError.ErrorTag.GLYPH_INVALID_GLYPH_INDEX;
      return error;
    }
    if (glyphs[gindex] == null) {
      long offset = ttface.getLoca_table().getGlyph_offsets()[gindex];
      int len = ttface.getLoca_table().getGlyph_lens()[gindex];
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "offset: " + offset + " len: " + len);
      stream.seek(offset);
      int num_contours = stream.readShort();
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "LoadGlyph num_contours: " + num_contours);
      stream.seek(offset);
      if (num_contours > 0) {
        glyph = new TTGlyphSimpleRec();
        glyph.setFormat(FTTags.GlyphFormat.OUTLINE);
        error = glyph.Load(stream, ttface, glyf_len);
      } else {
        Log.e(TAG, "compound glyphs not yet implemented!");
        error = FTError.ErrorTag.LOAD_INVALID_GLYPH_FORMAT;
//        glyph = new TTGlyphCompositeRec();
//        glyph.setFormat(FTTags.GlyphFormat.COMPOSITE);
//        error = glyph.Load(stream, ttface, glyf_len);
      }
      if (error == FTError.ErrorTag.ERR_OK) {
        glyphs[gindex] = glyph;
        glyphs_max_index++;
      }
    }
    return error;
  }

  /* ==================== getGlyphs ================================== */
  public TTGlyphRec[] getGlyphs() {
    return glyphs;
  }

  /* ==================== setGlyphs ================================== */
  public void setGlyphs(TTGlyphRec[] glyphs) {
    this.glyphs = glyphs;
  }

  /* ==================== getNum_glyphs ================================== */
  public int getNum_glyphs() {
    return num_glyphs;
  }

  /* ==================== setNum_glyphs ================================== */
  public void setNum_glyphs(int num_glyphs) {
    this.num_glyphs = num_glyphs;
  }

  /* ==================== getGlyphs_max_index ================================== */
  public int getGlyphs_max_index() {
    return glyphs_max_index;
  }

  /* ==================== setGlyphs_max_index ================================== */
  public void setGlyphs_max_index(int glyphs_max_index) {
    this.glyphs_max_index = glyphs_max_index;
  }

  /* ==================== getGlyf_len ================================== */
  public int getGlyf_len() {
    return glyf_len;
  }

  /* ==================== setGlyf_len ================================== */
  public void setGlyf_len(int glyf_len) {
    this.glyf_len = glyf_len;
  }

  /* ==================== getGlyf_table_offset ================================== */
  public long getGlyf_table_offset() {
    return glyf_table_offset;
  }

  /* ==================== setGlyf_table_offset ================================== */
  public void setGlyf_table_offset(long glyf_table_offset) {
    this.glyf_table_offset = glyf_table_offset;
  }

}
