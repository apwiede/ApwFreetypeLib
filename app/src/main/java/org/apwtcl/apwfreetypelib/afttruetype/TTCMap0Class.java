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
  /*    TTCMap0Class                                                       */
  /*                                                                       */
  /* ===================================================================== */
  /* =====================================================================
   * CMap0 TABLE OVERVIEW
   * --------------
   *
   *   NAME        OFFSET         TYPE          DESCRIPTION
   *
   *   format      0              USHORT        must be 0
   *   length      2              USHORT        table length in bytes
   *   language    4              USHORT        Mac language code
   *   glyph_ids   6              BYTE[256]     array of glyph indices
   *               262
   * =====================================================================
   */


import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCMapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTCMap0Class extends TTCMapClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMap0Class";

  private final static int HEADER_SIZE = 6;   /* fields format, length, language */

  private int language = 0;
  private byte[] glyph_indices = null;

  /* ==================== TTCMap0Class ================================== */
  public TTCMap0Class() {
    super();
    oid++;
    id = oid;

    format = TTTags.CMapFormat.CMap0;
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
    str.append("..length: "+length+'\n');
    str.append("..lanugage: "+language+'\n');
    return super_str + str.toString();
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    length = stream.readShort();
    language = stream.readShort();
    glyph_indices = new byte[256];
    stream.readByteArray(glyph_indices, (length - HEADER_SIZE));
Debug(0, DebugTag.DBG_CMAP, TAG, "Cmap0Class: "+toDebugString());
    return error;
  }

  /* =====================================================================
   *    tt_cmap0_validate
   *
   * =====================================================================
   */
  private FTError.ErrorTag cmap0Validate(TTValidatorRec valid) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int length = 0;

      /* check glyph indices whenever necessary */
    if (valid.getLevel().getVal() >= FTTags.Validate.TIGHT.getVal()) {
      int n;
      int idx;

      for (n = 0; n < 256; n++) {
        idx = glyph_indices[n];
        if (idx >= ((TTValidatorRec)valid).getNumGlyphs()) {
          return FTError.ErrorTag.VALID_INVALID_GLYPH_ID;
        }
      }
    }
    return error;
  }

  /* ==================== validate ===================================== */
  @Override
  public FTError.ErrorTag validate(TTValidatorRec valid) {
    return cmap0Validate(valid);
  }

  /* ==================== getCmapInfo ===================================== */
  @Override
  public FTError.ErrorTag getCmapInfo(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "getCmapInfo not yet implemented");
//    return tt_cmap0_get_info();
    return error;
  }

  /* ==================== charIndex ===================================== */
  @Override
  public int charIndex(FTCMapRec cmap, int char_code) {
    return char_code < 256 ? glyph_indices[char_code] : 0;
  }

  /* ==================== charNext ===================================== */
  @Override
  public int charNext(FTCMapRec cmap, FTReference<Integer> char_code_ref) {
    Log.e(TAG, "charNext not yet fully implemented");
    int char_code = char_code_ref.Get();
    int result = 0;
    int gindex = 0;


    /* 6  go to glyph IDs */
    while(++char_code < 256) {
//      gindex = cmap.data[6 +char_code];
      if (gindex != 0) {
        result = char_code;
        break;
      }
    }
    char_code_ref.Set(result);
    return gindex;
  }

}
