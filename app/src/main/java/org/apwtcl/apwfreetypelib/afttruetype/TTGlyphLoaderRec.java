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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

  /* ===================================================================== */
  /*    TTGlyphLoaderRec                                                   */
  /*                                                                       */
  /* ===================================================================== */

public class TTGlyphLoaderRec extends FTGlyphLoaderRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyphLoaderRec";

  /* ==================== TTGlyphLoaderRec ================================== */
  public TTGlyphLoaderRec() {
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
    String super_str = super.toDebugString();
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    return super_str + str.toString();
  }

  /* =====================================================================
   * tt_load_simple_glyph
   * =====================================================================
   */
  public FTError.ErrorTag tt_load_simple_glyph(TTLoaderRec loader) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_load_simple_glyph");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int n_contours = loader.getN_contours();
    TTFaceRec ttface = (TTFaceRec)loader.getFace();
    TTGlyphSimpleRec ttglyph;
    int contour_idx;
    int contour_limit;
    int n_ins;
    int n_points;
    int flag_idx;
    int flag_limit;
    int vec_idx;
    int vec_limit;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader.n_contours: %d ", loader.getN_contours()));

    ttglyph = (TTGlyphSimpleRec)ttface.getGlyf_table().getGlyphs()[loader.getGlyph_index()];

      /* check that we can add the contours to the glyph */
    error = FT_GLYPHLOADER_CHECK_POINTS(0, n_contours);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
      /* loading the contours' endpoints & number of points */
    contour_limit = n_contours;
      /* check space for contours array + instructions count */

    for (contour_idx = 0; contour_idx < contour_limit; contour_idx++) {
      current.getContours()[contour_idx] = ttglyph.getContours()[contour_idx];
    }
    n_points = 0;
    if (n_contours > 0) {
      n_points = current.getContours()[contour_idx - 1] + 1;
      if (n_points < 0) {
        error = FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
        return error;
      }
    }
    for(int i = 0; i < contour_limit; i++) {
Debug(9, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("i: %d contour: %d", i, current.getContours()[i]));
    }

      /* note that we will add four phantom points later */
    error = FT_GLYPHLOADER_CHECK_POINTS(n_points + 4, 0);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }

      /* loading the bytecode instructions */
    loader.getGlyph().setControl_len(0);
    loader.getGlyph().setControl_data(null);

    n_ins = ttglyph.getInstructions_length();
    FTTrace.Trace(7, TAG, String.format("  Instructions size: %d", n_ins));
    if (n_ins > ttface.getMax_profile().getMaxSizeOfInstructions()) {
      FTTrace.Trace(7, TAG, String.format("tt_load_simple_glyph: too many instructions (%d)", n_ins));
      error = FTError.ErrorTag.GLYPH_TOO_MANY_HINTS;
      return error;
    }
    if ((loader.getLoad_flags().getVal() & Flags.Load.NO_AUTOHINT.getVal()) == 0) {
      int i;

      loader.getGlyph().setControl_len(n_ins);
      loader.getGlyph().setControl_data(loader.getExec().glyphIns);
      for (i = 0; i < n_ins; i++) {
        loader.getExec().glyphIns[i] = TTOpCode.OpCode.getTableTag(ttglyph.getInstructions()[i] & 0xFF);
        if (loader.getExec().glyphIns[i] == null) {
          Log.e(TAG, String.format("loader.getExec().glyphIns[%d] is null!", i));
        } else {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("ins: %d 0x%02x: ", i, loader.getExec().glyphIns[i].getVal()) + loader.getExec().glyphIns[i]);
        }
      }
//        FT_MEM_COPY( loader.exec.glyphIns, p, (FT_Long)n_ins );
    }

      /* loading the point tags */
    flag_idx = 0;
    flag_limit = n_points;
    while (flag_idx < flag_limit) {
      current.getTags()[flag_idx++] = Flags.Curve.getTableTag(ttglyph.getTags()[flag_idx]);
      flag_idx++;
    }
for(int t = 0; t < flag_idx; t++) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("Tag: t: %d: 0x%02x: ", t, current.getTags()[t].getVal())+current.getTags()[t]);
}

      /* loading the X and y coordinates */
    vec_limit = n_points;
    for (vec_idx = 0; vec_idx < vec_limit; vec_idx++) {
      current.getPoints()[vec_idx] = ttglyph.getPoints()[vec_idx];
    }
    current.setN_points(n_points);
    current.setN_contours(n_contours);
for (int i = 0; i < n_points; i++) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("point i:%d  %d %d", i, current.getPoints()[i].x, current.getPoints()[i].y));
}
    return error;
  }

}
