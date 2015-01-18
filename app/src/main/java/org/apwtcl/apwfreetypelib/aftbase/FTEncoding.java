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
  /*    FTEncoding                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    An enumeration used to specify character sets supported by         */
  /*    charmaps.  Used in the @FT_Select_Charmap API function.            */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Despite the name, this enumeration lists specific character        */
  /*    repertories (i.e., charsets), and not text encoding methods (e.g., */
  /*    UTF-8, UTF-16, etc.).                                              */
  /*                                                                       */
  /*    Other encodings might be defined in the future.                    */
  /*                                                                       */
  /* <Values>                                                              */
  /*    FT_ENCODING_NONE ::                                                */
  /*      The encoding value~0 is reserved.                                */
  /*                                                                       */
  /*    FT_ENCODING_UNICODE ::                                             */
  /*      Corresponds to the Unicode character set.  This value covers     */
  /*      all versions of the Unicode repertoire, including ASCII and      */
  /*      Latin-1.  Most fonts include a Unicode charmap, but not all      */
  /*      of them.                                                         */
  /*                                                                       */
  /*      For example, if you want to access Unicode value U+1F028 (and    */
  /*      the font contains it), use value 0x1F028 as the input value for  */
  /*      @FT_Get_Char_Index.                                              */
  /*                                                                       */
  /*    FT_ENCODING_MS_SYMBOL ::                                           */
  /*      Corresponds to the Microsoft Symbol encoding, used to encode     */
  /*      mathematical symbols in the 32..255 character code range.  For   */
  /*      more information, see `http://www.ceviz.net/symbol.htm'.         */
  /*                                                                       */
  /*    FT_ENCODING_SJIS ::                                                */
  /*      Corresponds to Japanese SJIS encoding.  More info at             */
  /*      at `http://langsupport.japanreference.com/encoding.shtml'.       */
  /*      See note on multi-byte encodings below.                          */
  /*                                                                       */
  /*    FT_ENCODING_GB2312 ::                                              */
  /*      Corresponds to an encoding system for Simplified Chinese as used */
  /*      used in mainland China.                                          */
  /*                                                                       */
  /*    FT_ENCODING_BIG5 ::                                                */
  /*      Corresponds to an encoding system for Traditional Chinese as     */
  /*      used in Taiwan and Hong Kong.                                    */
  /*                                                                       */
  /*    FT_ENCODING_WANSUNG ::                                             */
  /*      Corresponds to the Korean encoding system known as Wansung.      */
  /*      For more information see                                         */
  /*      `http://www.microsoft.com/typography/unicode/949.txt'.           */
  /*                                                                       */
  /*    FT_ENCODING_JOHAB ::                                               */
  /*      The Korean standard character set (KS~C 5601-1992), which        */
  /*      corresponds to MS Windows code page 1361.  This character set    */
  /*      includes all possible Hangeul character combinations.            */
  /*                                                                       */
  /*    FT_ENCODING_ADOBE_LATIN_1 ::                                       */
  /*      Corresponds to a Latin-1 encoding as defined in a Type~1         */
  /*      PostScript font.  It is limited to 256 character codes.          */
  /*                                                                       */
  /*    FT_ENCODING_ADOBE_STANDARD ::                                      */
  /*      Corresponds to the Adobe Standard encoding, as found in Type~1,  */
  /*      CFF, and OpenType/CFF fonts.  It is limited to 256 character     */
  /*      codes.                                                           */
  /*                                                                       */
  /*    FT_ENCODING_ADOBE_EXPERT ::                                        */
  /*      Corresponds to the Adobe Expert encoding, as found in Type~1,    */
  /*      CFF, and OpenType/CFF fonts.  It is limited to 256 character     */
  /*      codes.                                                           */
  /*                                                                       */
  /*    FT_ENCODING_ADOBE_CUSTOM ::                                        */
  /*      Corresponds to a custom encoding, as found in Type~1, CFF, and   */
  /*      OpenType/CFF fonts.  It is limited to 256 character codes.       */
  /*                                                                       */
  /*    FT_ENCODING_APPLE_ROMAN ::                                         */
  /*      Corresponds to the 8-bit Apple roman encoding.  Many TrueType    */
  /*      and OpenType fonts contain a charmap for this encoding, since    */
  /*      older versions of Mac OS are able to use it.                     */
  /*                                                                       */
  /*    FT_ENCODING_OLD_LATIN_2 ::                                         */
  /*      This value is deprecated and was never used nor reported by      */
  /*      FreeType.  Don't use or test for it.                             */
  /*                                                                       */
  /*    FT_ENCODING_MS_SJIS ::                                             */
  /*      Same as FT_ENCODING_SJIS.  Deprecated.                           */
  /*                                                                       */
  /*    FT_ENCODING_MS_GB2312 ::                                           */
  /*      Same as FT_ENCODING_GB2312.  Deprecated.                         */
  /*                                                                       */
  /*    FT_ENCODING_MS_BIG5 ::                                             */
  /*      Same as FT_ENCODING_BIG5.  Deprecated.                           */
  /*                                                                       */
  /*    FT_ENCODING_MS_WANSUNG ::                                          */
  /*      Same as FT_ENCODING_WANSUNG.  Deprecated.                        */
  /*                                                                       */
  /*    FT_ENCODING_MS_JOHAB ::                                            */
  /*      Same as FT_ENCODING_JOHAB.  Deprecated.                          */
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
  /*                                                                       */
  /* ===================================================================== */


import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTEncoding extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTEncoding";

    public static TEncoding[] tt_encodings = null;

    /* ==================== FTEncoding ================================== */
    public FTEncoding() {
      oid++;
      id = oid;
      tt_encodings = new TEncoding[12];
      tt_encodings[0] = new TEncoding(TTTags.Platform.ISO,
              -1, FTTags.Encoding.UNICODE);
      tt_encodings[1] = new TEncoding(TTTags.Platform.APPLE_UNICODE,
              -1, FTTags.Encoding.UNICODE);
      tt_encodings[2] = new TEncoding(TTTags.Platform.MACINTOSH,
              TTTags.MacId.ROMAN.getVal(), FTTags.Encoding.APPLE_ROMAN);
      tt_encodings[3] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.SYMBOL_CS.getVal(), FTTags.Encoding.MS_SYMBOL);
      tt_encodings[4] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.UCS_4.getVal(), FTTags.Encoding.UNICODE);
      tt_encodings[5] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.UNICODE_CS.getVal(), FTTags.Encoding.UNICODE);
      tt_encodings[6] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.SJIS.getVal(), FTTags.Encoding.SJIS);
      tt_encodings[7] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.GB2312.getVal(), FTTags.Encoding.GB2312);
      tt_encodings[8] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.BIG_5.getVal(), FTTags.Encoding.BIG5);
      tt_encodings[9] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.WANSUNG.getVal(), FTTags.Encoding.WANSUNG);
      tt_encodings[10] = new TEncoding(TTTags.Platform.MICROSOFT,
              TTTags.MsId.JOHAB.getVal(), FTTags.Encoding.JOHAB);
    }
    
    /* ==================== mySelf ================================== */
    public String mySelf() {
      String str = TAG+"!"+id+"!";
      return str;
    } 
        
    /* ==================== toString ===================================== */
    public String toString() {
      StringBuffer str = new StringBuffer(mySelf()+"!");
      return str.toString();
    }

    /* ==================== toDebugString ===================================== */
    public String toDebugString() {
      StringBuffer str = new StringBuffer(mySelf()+"\n");
      return str.toString();
    }
 
    /* =====================================================================
     * sfnt_find_encoding
     * =====================================================================
     */
    public static FTTags.Encoding sfnt_find_encoding(TTTags.Platform platform_id, int encoding_id) {
      TEncoding cur;
      int limit;

      limit = 12;
      for (int i = 0; i < limit; i++) {
        cur = tt_encodings[i];
        if (cur.platform_id == platform_id) {
          if (cur.encoding_id == encoding_id || cur.encoding_id == -1) {
            return cur.encoding;
          }
        }
      }
      return FTTags.Encoding.NONE;
    }
}
