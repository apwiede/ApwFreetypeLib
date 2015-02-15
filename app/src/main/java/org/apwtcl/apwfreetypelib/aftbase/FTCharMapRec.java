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
  /*    FTCharMapRec                                                       */
  /* <Description>                                                         */
  /*    The base charmap structure.                                        */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    platform_id :: An ID number describing the platform for the        */
  /*                   following encoding ID.  This comes directly from    */
  /*                   the TrueType specification and should be emulated   */
  /*                   for other formats.                                  */
  /*                                                                       */
  /*    encoding_id :: A platform specific encoding number.  This also     */
  /*                   comes from the TrueType specification and should be */
  /*                   emulated similarly.                                 */
  /*                                                                       */
  /*    face        :: A handle to the parent face object.                 */
  /*                                                                       */
  /*    encoding    :: An @FT_Encoding tag identifying the charmap.  Use   */
  /*                   this with @FT_Select_Charmap.                       */
  /*                                                                       */
  /* <Note>                                                                */
  /*    By default, FreeType automatically synthesizes a Unicode charmap   */
  /*    for PostScript fonts, using their glyph names dictionaries.        */
  /*    However, it also reports the encodings defined explicitly in the   */
  /*    font file, for the cases when they are needed, with the Adobe      */
  /*    values as well.                                                    */
  /*                                                                       */
  /*    FT_ENCODING_NONE is set by the BDF and PCF drivers if the charmap  */
  /*    is neither Unicode nor ISO-8859-1 (otherwise it is set to          */
  /*    FT_ENCODING_UNICODE).  Use @FT_Get_BDF_Charset_ID to find out      */
  /*    which encoding is really present.  If, for example, the            */
  /*    `cs_registry' field is `KOI8' and the `cs_encoding' field is `R',  */
  /*    the font is encoded in KOI8-R.                                     */
  /*                                                                       */
  /*    FT_ENCODING_NONE is always set (with a single exception) by the    */
  /*    winfonts driver.  Use @FT_Get_WinFNT_Header and examine the        */
  /*    `charset' field of the @FT_WinFNT_HeaderRec structure to find out  */
  /*    which encoding is really present.  For example,                    */
  /*    @FT_WinFNT_ID_CP1251 (204) means Windows code page 1251 (for       */
  /*    Russian).                                                          */
  /*                                                                       */
  /*    FT_ENCODING_NONE is set if `platform_id' is @TT_PLATFORM_MACINTOSH */
  /*    and `encoding_id' is not @TT_MAC_ID_ROMAN (otherwise it is set to  */
  /*    FT_ENCODING_APPLE_ROMAN).                                          */
  /*                                                                       */
  /*    If `platform_id' is @TT_PLATFORM_MACINTOSH, use the function       */
  /*    @FT_Get_CMap_Language_ID  to query the Mac language ID which may   */
  /*    be needed to be able to distinguish Apple encoding variants.  See  */
  /*                                                                       */
  /*      http://www.unicode.org/Public/MAPPINGS/VENDORS/APPLE/README.TXT  */
  /*                                                                       */
  /*    to get an idea how to do that.  Basically, if the language ID      */
  /*    is~0, don't use it, otherwise subtract 1 from the language ID.     */
  /*    Then examine `encoding_id'.  If, for example, `encoding_id' is     */
  /*    @TT_MAC_ID_ROMAN and the language ID (minus~1) is                  */
  /*    `TT_MAC_LANGID_GREEK', it is the Greek encoding, not Roman.        */
  /*    @TT_MAC_ID_ARABIC with `TT_MAC_LANGID_FARSI' means the Farsi       */
  /*    variant the Arabic encoding.                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTCMapInfoRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTNameTableRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTCharMapRec extends FTEncoding {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCharMapRec";

  protected FTFaceRec face = null;
  protected FTTags.Encoding encoding = FTTags.Encoding.NONE;
  protected int platform_id = 0;
  protected int encoding_id = 0;


  /* ==================== FTCharMapRec ================================== */
  public FTCharMapRec() {
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
      str.append("..encoding: "+encoding+'\n');
      str.append("..platform_id: "+platform_id+'\n');
      str.append("..encoding_id: "+encoding_id+'\n');
      return str.toString();
    }

  /* =====================================================================
   *    GetCMapFormat
   *
   * =====================================================================
   */
  public int GetCMapFormat() {
    FTServiceTTCMapsRec service = null;
    TTCMapInfoRec cmap_info;

    if (face == null) {
      return -1;
    }
    FTModuleRec module = face.getDriver();
Log.e(TAG, "GetCMapFormat not yet fully implemented!!");
//    service = (FTServiceTTCMapsRec)module.module_clazz.  ..mgetInterface(module, "TT_CMAP");
//      FT_FACE_FIND_SERVICE( face, service, TT_CMAP );
    if (service == null) {
      return -1;
    }
    FTReference<TTCMapInfoRec> cmap_info_ref = new FTReference<TTCMapInfoRec>();
    if (service.getCmapInfo(this, cmap_info_ref) == FTError.ErrorTag.ERR_OK) {
      return -1;
    }
    cmap_info = cmap_info_ref.Get();
    return cmap_info.getFormat();
  }

  /* =====================================================================
   *    SetCharMap
   *
   * =====================================================================
   */
  public FTError.ErrorTag SetCharmap(FTFaceRec face) {
    FTCharMapRec cur[];
    int curIdx = 0;
    int limit;


    if (face == null) {
      return FTError.ErrorTag.GLYPH_INVALID_FACE_HANDLE;
    }
    cur = face.charmaps;
    if (cur == null) {
      return FTError.ErrorTag.CMAP_INVALID_CHARMAP_HANDLE;
    }
    if (GetCMapFormat() == 14)
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;

    limit = face.getNum_charmaps();

    for (curIdx = 0; curIdx < limit; curIdx++) {
      if (cur[curIdx] == this) {
        face.setCharmap(cur[curIdx]);
        return FTError.ErrorTag.ERR_OK;
      }
    }
    return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
  }

  /* =====================================================================
   * FTGetCharmapIndex
   * =====================================================================
   */
  public int  FTGetCharmapIndex() {
    int i;

    if (face == null) {
      return -1;
    }
    for (i = 0; i < face.getNum_charmaps(); i++) {
      if (face.charmaps[i] == this) {
        break;
      }
    }
    if (i >= face.getNum_charmaps()) {
      return -1;
    }
    return i;
  }

  /* =====================================================================
   * FTSelectCharmap
   * =====================================================================
   */
  public FTError.ErrorTag FTSelectCharmap(FTFaceRec face, FTTags.Encoding encoding) {
    FTCharMapRec cur;
    int limit;

    if (face == null) {
      return FTError.ErrorTag.INTERP_INVALID_FACE_HANDLE;
    }
    if (encoding == FTTags.Encoding.NONE) {
      return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    /* FT_ENCODING_UNICODE is special.  We try to find the `best' Unicode */
    /* charmap available, i.e., one with UCS-4 characters, if possible.   */
    /*                                                                    */
    /* This is done by find_unicode_charmap() above, to share code.       */
    if (encoding == FTTags.Encoding.UNICODE) {
//        return find_unicode_charmap(face);
      return FTError.ErrorTag.INTERP_NOT_YET_IMPLEMENTED;
    }
    cur = face.charmaps[0];
    if (cur == null) {
      return FTError.ErrorTag.INTERP_INVALID_CHARMAP_HANDLE;
    }
    limit = face.getNum_charmaps();
    for (int j = 0; j < limit; j++) {
      cur = face.charmaps[j];
      if (cur.encoding == encoding) {
        face.setCharmap(cur);
        return FTError.ErrorTag.ERR_OK;
      }
    }
    return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
  }

  /* =====================================================================
   * FTGetCharIndex
   * =====================================================================
   */
  public static int FTGetCharIndex(FTFaceRec face, int charcode) {
    int result = -1;
    int ch_code = charcode;
  
    if (face != null && face.getCharmap() != null) {
      FTCMapRec cmap = (FTCMapRec)(face.getCharmap());

      if (charcode > 0xFFFFFFFFL) {
        FTTrace.Trace(7, TAG, "FT_Get_Char_Index: too large charcode");
        FTTrace.Trace(7, TAG, String.format(" 0x%x is truncated", charcode));
      }
      result = cmap.getClazz().charIndex(cmap, ch_code);
    }
Debug(0, DebugTag.DBG_CACHE, TAG, String.format("FTGetCharIndex: 0x%04x %d", charcode, result));
    return result;
  }

  /* =====================================================================
   * find_unicode_charmap
   * =====================================================================
   */
  public static FTError.ErrorTag find_unicode_charmap(FTFaceRec face) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCharMapRec first;
    FTCharMapRec cur;
    int charmaps_idx = 0;
  
    /* caller should have already checked that `face' is valid */
 //     FT_ASSERT( face );
  
Debug(0, DebugTag.DBG_CACHE, TAG, "find_unicode_charmap:");
    first = face.charmaps[charmaps_idx];
    if (first == null) {
      return FTError.ErrorTag.LOAD_INVALID_CHARMAP_HANDLE;
    }
    /*
     *  The original TrueType specification(s) only specified charmap
     *  formats that are capable of mapping 8 or 16 bit character codes to
     *  glyph indices.
     *
     *  However, recent updates to the Apple and OpenType specifications
     *  introduced new formats that are capable of mapping 32-bit character
     *  codes as well.  And these are already used on some fonts, mainly to
     *  map non-BMP Asian ideographs as defined in Unicode.
     *
     *  For compatibility purposes, these fonts generally come with
     *  *several* Unicode charmaps:
     *
     *   - One of them in the "old" 16-bit format, that cannot access
     *     all glyphs in the font.
     *
     *   - Another one in the "new" 32-bit format, that can access all
     *     the glyphs.
     *
     *  This function has been written to always favor a 32-bit charmap
     *  when found.  Otherwise, a 16-bit one is returned when found.
     */
  
    /* Since the `interesting' table, with IDs (3,10), is normally the */
    /* last one, we loop backwards.  This loses with type1 fonts with  */
    /* non-BMP characters (<.0001%), this wins with .ttf with non-BMP  */
    /* chars (.01% ?), and this is the same about 99.99% of the time!  */
  
    charmaps_idx = face.getNum_charmaps() - 1;
    for ( ; charmaps_idx >= 0; charmaps_idx--) {
      cur = face.charmaps[charmaps_idx];
if (cur == null) {
  return FTError.ErrorTag.LOAD_INVALID_CHARMAP_HANDLE;
}
      if (cur.encoding == FTTags.Encoding.UNICODE) {
        /* XXX If some new encodings to represent UCS-4 are added, */
        /*     they should be added here.                          */
        if ((cur.platform_id == TTTags.Platform.MICROSOFT.getVal() && cur.encoding_id == TTTags.MsId.UCS_4.getVal())     ||
             (cur.platform_id == TTTags.Platform.APPLE_UNICODE.getVal() && cur.encoding_id == TTTags.AppleId.UNICODE_32.getVal())) {
          face.setCharmap(cur);
          return FTError.ErrorTag.ERR_OK;
        }
      }
    }
    /* We do not have any UCS-4 charmap.                */
    /* Do the loop again and search for UCS-2 charmaps. */
    charmaps_idx = face.getNum_charmaps() - 1;
    for ( ; charmaps_idx >= 0; charmaps_idx--) {
      cur = face.charmaps[charmaps_idx];
      if (cur.encoding == FTTags.Encoding.UNICODE) {
        face.setCharmap(cur);
        return FTError.ErrorTag.ERR_OK;
      }
    }
    return FTError.ErrorTag.LOAD_INVALID_CHARMAP_HANDLE;
  }

  /* ==================== getPlatformId ================================== */
  public int getPlatformId() {
    return platform_id;
  }

  /* ==================== getEncodingId ================================== */
  public int getEncodingId() {
    return encoding_id;
  }

  /* ==================== getEncoding ================================== */
  public FTTags.Encoding getEncoding() {
    return encoding;
  }

  /* ==================== setFace ================================== */
  public FTFaceRec getFace() {
    return face;
  }

  /* ==================== setPlatformId ================================== */
  public void setPlatformId(int platform_id) {
    this.platform_id = platform_id;
  }

  /* ==================== setEncodingId ================================== */
  public void setEncodingId(int encoding_id) {
    this.encoding_id = encoding_id;
  }

  /* ==================== setEncoding ================================== */
  public void setEncoding(FTTags.Encoding encoding) {
    this.encoding = encoding;
  }

  /* ==================== setFace ================================== */
  public void setFace(FTFaceRec face) {
    this.face = face;
  }

}