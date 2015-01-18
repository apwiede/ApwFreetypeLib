package org.apwtcl.apwfreetypelib.afttruetype;

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

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTTags {

  /* =========================================================================
     *
     * Possible values of the `name' identifier field in the name records of
     * the TTF `name' table.  These values are platform independent.
     *
     * =========================================================================
     */
  public enum NameId {
    COPYRIGHT(0, "TT_NAME_ID_COPYRIGHT"),
    FONT_FAMILY(1, "TT_NAME_ID_FONT_FAMILY"),
    FONT_SUBFAMILY(2, "TT_NAME_ID_FONT_SUBFAMILY"),
    UNIQUE_ID(3, "TT_NAME_ID_UNIQUE_ID"),
    FULL_NAME(4, "TT_NAME_ID_FULL_NAME"),
    VERSION_STRING(5, "TT_NAME_ID_VERSION_STRING"),
    PS_NAME(6, "TT_NAME_ID_PS_NAME"),
    TRADEMARK(7, "TT_NAME_ID_TRADEMARK"),
    /* the following values are from the OpenType spec */
    MANUFACTURER(8, "TT_NAME_ID_MANUFACTURER"),
    DESIGNER(9, "TT_NAME_ID_DESIGNER"),
    DESCRIPTION(10, "TT_NAME_ID_DESCRIPTION"),
    VENDOR_URL(11, "TT_NAME_ID_VENDOR_URL"),
    DESIGNER_URL(12, "TT_NAME_ID_DESIGNER_URL"),
    LICENSE(13, "TT_NAME_ID_LICENSE"),
    LICENSE_URL(14, "TT_NAME_ID_LICENSE_URL"),
    /* number 15 is reserved */
    PREFERRED_FAMILY(16, "TT_NAME_ID_PREFERRED_FAMILY"),
    PREFERRED_SUBFAMILY(17, "TT_NAME_ID_PREFERRED_SUBFAMILY"),
    MAC_FULL_NAME(18, "TT_NAME_ID_MAC_FULL_NAME"),
    /* The following code is new as of 2000-01-21 */
    SAMPLE_TEXT(19, "TT_NAME_ID_SAMPLE_TEXT"),
    /* This is new in OpenType 1.3 */
    CID_FINDFONT_NAME(20, "TT_NAME_ID_CID_FINDFONT_NAME"),
    /* This is new in OpenType 1.5 */
    WWS_FAMILY(21, "TT_NAME_ID_WWS_FAMILY"),
    WWS_SUBFAMILY(22, "TT_NAME_ID_WWS_SUBFAMILY");
    private int val;
    private String str;
    private static SparseArray<NameId> tagToNameIdMapping;
    public static NameId getTableTag(int i) {
      if (tagToNameIdMapping == null) {
        initMapping();
      }
      return tagToNameIdMapping.get(i);
    }
    private static void initMapping() {
      tagToNameIdMapping = new SparseArray<NameId>();
      for (NameId t : values()) {
        tagToNameIdMapping.put(t.val, t);
      }
    }
    private NameId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum Table {
    _00010000(0x00010000, "00010000"),
    _00020000(0x00020000, "00020000"),
    avar(TTUtil.TagToInt("avar"), "Axis variation data"),
    BASE(TTUtil.TagToInt("BASE"), "advanced typographic: Baseline data"),
    bdat(TTUtil.TagToInt("bdat"), "no description for: bdat"),
    BDF (TTUtil.TagToInt("BDF "), "no description for: BDF"),
    bhed(TTUtil.TagToInt("bhed"), "no description for: bhed"),
    bloc(TTUtil.TagToInt("bloc"), "no description for: bloc"),
    bsln(TTUtil.TagToInt("bsln"), "no description for: bsln"),
    CBDT(TTUtil.TagToInt("CBDT"), "no description for: CBDT"),
    CBLC(TTUtil.TagToInt("CBLC"), "no description for: CFF"),
    CFF (TTUtil.TagToInt("CFF "), "postscript outline: PostScript font program (compact font format) [PostScript]"),
    CID (TTUtil.TagToInt("CID "), "no description for: CID"),
    cmap(TTUtil.TagToInt("cmap"), "required tables: Character to glyph mapping"),
    cvar(TTUtil.TagToInt("cvar"), "no description for: cvar"),
    cvt (TTUtil.TagToInt("cvt "), "outline tables: Control Value Table"),
    DSIG(TTUtil.TagToInt("DSIG"), "other open type: Digital signature"),
    EBDT(TTUtil.TagToInt("EBDT"), "Embedded bitmap data"),
    EBLC(TTUtil.TagToInt("EBLC"), "Embedded bitmap location data"),
    EBSC(TTUtil.TagToInt("EBSC"), "no description for: EBSC"),
    feat(TTUtil.TagToInt("feat"), "no description for: feat"),
    FOND(TTUtil.TagToInt("FOND"), "no description for: FOND"),
    fpgm(TTUtil.TagToInt("fpgm"), "outline tables: Font program"),
    fvar(TTUtil.TagToInt("fvar"), "no description for: fvar"),
    gasp(TTUtil.TagToInt("gasp"), "other open type: Grid-fitting/Scan-conversion"),
    GDEF(TTUtil.TagToInt("GDEF"), "advanced typographic: Glyph definition data"),
    glyf(TTUtil.TagToInt("glyf"), "outline tables: Glyph data"),
    GPOS(TTUtil.TagToInt("GPOS"), "advanced typographic: Glyph positioning data"),
    GSUB(TTUtil.TagToInt("GSUB"), "advanced typographic: Glyph substitution data"),
    gvar(TTUtil.TagToInt("gvar"), "no description for: gvar"),
    hdmx(TTUtil.TagToInt("hdmx"), "other open type: Horizontal device metrics"),
    head(TTUtil.TagToInt("head"), "required tables: Font header"),
    hhea(TTUtil.TagToInt("hhea"), "required tables: Horizontal header"),
    hmtx(TTUtil.TagToInt("hmtx"), "required tables: Horizontal metrics"),
    JSTF(TTUtil.TagToInt("JSTF"), "no description for: JSTF"),
    just(TTUtil.TagToInt("just"), "advanced typographic: Justification data"),
    kern(TTUtil.TagToInt("kern"), "other open type: Kerning"),
    lcar(TTUtil.TagToInt("lcar"), "no description for: lcar"),
    loca(TTUtil.TagToInt("loca"), "outline tables: Index to location"),
    LTSH(TTUtil.TagToInt("LTSH"), "other open type: Linear threshold data"),
    LWFN(TTUtil.TagToInt("LWFN"), "no description for: LWFN"),
    MATH(TTUtil.TagToInt("MATH"), "no description for: MATH"),
    maxp(TTUtil.TagToInt("maxp"), "required tables: Maximum profile"),
    META(TTUtil.TagToInt("META"), "no description for: META"),
    MMFX(TTUtil.TagToInt("MMFX"), "no description for: MMFX"),
    MMSD(TTUtil.TagToInt("MMSD"), "no description for: MMSD"),
    mort(TTUtil.TagToInt("mort"), "no description for: mort"),
    morx(TTUtil.TagToInt("morx"), "no description for: morx"),
    name(TTUtil.TagToInt("name"), "required tables: Naming table"),
    opbd(TTUtil.TagToInt("opbd"), "no description for: opbd"),
    OS2 (TTUtil.TagToInt("OS/2"), "required tables: OS/2 and Windows specific metrics"),
    OTTO(TTUtil.TagToInt("OTTO"), "no description for: OTTO"),
    PCLT(TTUtil.TagToInt("PCLT"), "other open type: PCL 5 data"),
    POST(TTUtil.TagToInt("POST"), "no description for: POST"),
    post(TTUtil.TagToInt("post"), "required tables: PostScript information"),
    prep(TTUtil.TagToInt("prep"), "outline tables: CVT Program"),
    prop(TTUtil.TagToInt("prop"), "no description for: prop"),
    sfnt(TTUtil.TagToInt("sfnt"), "no description for: sfnt"),
    SING(TTUtil.TagToInt("SING"), "no description for: SING"),
    trak(TTUtil.TagToInt("trak"), "no description for: trak"),
    truE(TTUtil.TagToInt("true"), "no description for: true"),            // avoid keyword conflict!
    ttc (TTUtil.TagToInt("ttc "), "truetype: Truetype collection"),
    ttcf(TTUtil.TagToInt("ttcf"), "truetype: Truetype collection header"),
    TYP1(TTUtil.TagToInt("TYP1"), "no description for: TYP1"),
    typ1(TTUtil.TagToInt("typ1"), "no description for: typ1"),
    unkn(TTUtil.TagToInt("unkn"), "unknown Table entry or default"),
    VDMX(TTUtil.TagToInt("VDMX"), "other open type: Vertical device metrics"),
    VORG(TTUtil.TagToInt("VORG"), "postscript outline: Vertical Origin"),
    vhea(TTUtil.TagToInt("vhea"), "other open type: Vertical Metrics header"),
    vmtx(TTUtil.TagToInt("vmtx"), "other open type: Vertical Metrics");

    private int val;
    private String str;
    private static SparseArray<Table> tagToTableMapping;
    public static Table getTableTag(int i) {
      if (tagToTableMapping == null) {
        initMapping();
      }
      return tagToTableMapping.get(i);
    }
    private static void initMapping() {
      tagToTableMapping = new SparseArray<Table>();
      for (Table t : values()) {
        tagToTableMapping.put(t.val, t);
      }
    }
    private Table(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
    public String getDescription() {
      return str;
    }
  }

    /* =========================================================================
     *
     * @enum:
     *   TT_PLATFORM_XXX
     *
     * @description:
     *   A list of valid values for the `platform_id' identifier code in
     *   @FT_CharMapRec and @FT_SfntName structures.
     *
     * @values:
     *   TT_PLATFORM_APPLE_UNICODE ::
     *     Used by Apple to indicate a Unicode character map and/or name entry.
     *     See @TT_APPLE_ID_XXX for corresponding `encoding_id' values.  Note
     *     that name entries in this format are coded as big-endian UCS-2
     *     character codes _only_.
     *
     *   TT_PLATFORM_MACINTOSH ::
     *     Used by Apple to indicate a MacOS-specific charmap and/or name entry.
     *     See @TT_MAC_ID_XXX for corresponding `encoding_id' values.  Note that
     *     most TrueType fonts contain an Apple roman charmap to be usable on
     *     MacOS systems (even if they contain a Microsoft charmap as well).
     *
     *   TT_PLATFORM_ISO ::
     *     This value was used to specify ISO/IEC 10646 charmaps.  It is however
     *     now deprecated.  See @TT_ISO_ID_XXX for a list of corresponding
     *     `encoding_id' values.
     *
     *   TT_PLATFORM_MICROSOFT ::
     *     Used by Microsoft to indicate Windows-specific charmaps.  See
     *     @TT_MS_ID_XXX for a list of corresponding `encoding_id' values.
     *     Note that most fonts contain a Unicode charmap using
     *     (TT_PLATFORM_MICROSOFT, @TT_MS_ID_UNICODE_CS).
     *
     *   TT_PLATFORM_CUSTOM ::
     *     Used to indicate application-specific charmaps.
     *
     *   TT_PLATFORM_ADOBE ::
     *     This value isn't part of any font format specification, but is used
     *     by FreeType to report Adobe-specific charmaps in an @FT_CharMapRec
     *     structure.  See @TT_ADOBE_ID_XXX.
     * =========================================================================
     */

  public enum Platform {
    APPLE_UNICODE(0, "TT_PLATFORM_APPLE_UNICODE"),
    MACINTOSH(1, "TT_PLATFORM_MACINTOSH"),
    ISO(2, "TT_PLATFORM_ISO"), /* deprecated */
    MICROSOFT(3, "TT_PLATFORM_MICROSOFT"),
    CUSTOM(4, "TT_PLATFORM_CUSTOM"),
    ADOBE(7, "TT_PLATFORM_ADOBE"); /* artificial */
    private int val;
    private String str;
    private static SparseArray<Platform> tagToPlatformMapping;
    public static Platform getTableTag(int i) {
      if (tagToPlatformMapping == null) {
        initMapping();
      }
      return tagToPlatformMapping.get(i);
    }
    private static void initMapping() {
      tagToPlatformMapping = new SparseArray<Platform>();
      for (Platform t : values()) {
        tagToPlatformMapping.put(t.val, t);
      }
    }
    private Platform(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

    /* =========================================================================
     *
     * @enum:
     *   TT_APPLE_ID_XXX
     *
     * @description:
     *   A list of valid values for the `encoding_id' for
     *   @TT_PLATFORM_APPLE_UNICODE charmaps and name entries.
     *
     * @values:
     *   TT_APPLE_ID_DEFAULT ::
     *     Unicode version 1.0.
     *
     *   TT_APPLE_ID_UNICODE_1_1 ::
     *     Unicode 1.1; specifies Hangul characters starting at U+34xx.
     *
     *   TT_APPLE_ID_ISO_10646 ::
     *     Deprecated (identical to preceding).
     *
     *   TT_APPLE_ID_UNICODE_2_0 ::
     *     Unicode 2.0 and beyond (UTF-16 BMP only).
     *
     *   TT_APPLE_ID_UNICODE_32 ::
     *     Unicode 3.1 and beyond, using UTF-32.
     *
     *   TT_APPLE_ID_VARIANT_SELECTOR ::
     *     From Adobe, not Apple.  Not a normal cmap.  Specifies variations
     *     on a real cmap.
     * =========================================================================
     */

  public enum AppleId {
    DEFAULT(0, "TT_APPLE_ID_DEFAULT"), /* Unicode 1.0 */
    UNICODE_1_1(1, "TT_APPLE_ID_UNICODE_1_1"), /* specify Hangul at U+34xx */
    ISO_10646(2, "TT_APPLE_ID_ISO_10646"), /* deprecated */
    UNICODE_2_0(3, "TT_APPLE_ID_UNICODE_2_0"), /* or later */
    UNICODE_32(4, "TT_APPLE_ID_UNICODE_32"), /* 2.0 or later, full repertoire */
    VARIANT_SELECTOR(5, "TT_APPLE_ID_VARIANT_SELECTOR"); /* variation selector data */
    private int val;
    private String str;
    private static SparseArray<AppleId> tagToAppleIdMapping;
    public static AppleId getTableTag(int i) {
      if (tagToAppleIdMapping == null) {
        initMapping();
      }
      return tagToAppleIdMapping.get(i);
    }
    private static void initMapping() {
      tagToAppleIdMapping = new SparseArray<AppleId>();
      for (AppleId t : values()) {
        tagToAppleIdMapping.put(t.val, t);
      }
    }
    private AppleId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

    /* =========================================================================
     *
     * @enum:
     *   TT_MAC_ID_XXX
     *
     * @description:
     *   A list of valid values for the `encoding_id' for
     *   @TT_PLATFORM_MACINTOSH charmaps and name entries.
     *
     * @values:
     *   TT_MAC_ID_ROMAN ::
     *   TT_MAC_ID_JAPANESE ::
     *   TT_MAC_ID_TRADITIONAL_CHINESE ::
     *   TT_MAC_ID_KOREAN ::
     *   TT_MAC_ID_ARABIC ::
     *   TT_MAC_ID_HEBREW ::
     *   TT_MAC_ID_GREEK ::
     *   TT_MAC_ID_RUSSIAN ::
     *   TT_MAC_ID_RSYMBOL ::
     *   TT_MAC_ID_DEVANAGARI ::
     *   TT_MAC_ID_GURMUKHI ::
     *   TT_MAC_ID_GUJARATI ::
     *   TT_MAC_ID_ORIYA ::
     *   TT_MAC_ID_BENGALI ::
     *   TT_MAC_ID_TAMIL ::
     *   TT_MAC_ID_TELUGU ::
     *   TT_MAC_ID_KANNADA ::
     *   TT_MAC_ID_MALAYALAM ::
     *   TT_MAC_ID_SINHALESE ::
     *   TT_MAC_ID_BURMESE ::
     *   TT_MAC_ID_KHMER ::
     *   TT_MAC_ID_THAI ::
     *   TT_MAC_ID_LAOTIAN ::
     *   TT_MAC_ID_GEORGIAN ::
     *   TT_MAC_ID_ARMENIAN ::
     *   TT_MAC_ID_MALDIVIAN ::
     *   TT_MAC_ID_SIMPLIFIED_CHINESE ::
     *   TT_MAC_ID_TIBETAN ::
     *   TT_MAC_ID_MONGOLIAN ::
     *   TT_MAC_ID_GEEZ ::
     *   TT_MAC_ID_SLAVIC ::
     *   TT_MAC_ID_VIETNAMESE ::
     *   TT_MAC_ID_SINDHI ::
     *   TT_MAC_ID_UNINTERP ::
     * =========================================================================
     */

  public enum MacId {
    ROMAN(0, "TT_MAC_ID_ROMAN"),
    JAPANESE(1, "TT_MAC_ID_JAPANESE"),
    TRADITIONAL_CHINESE(2, "TT_MAC_ID_TRADITIONAL_CHINESE"),
    KOREAN(3, "TT_MAC_ID_KOREAN"),
    ARABIC(4, "TT_MAC_ID_ARABIC"),
    HEBREW(5, "TT_MAC_ID_HEBREW"),
    GREEK(6, "TT_MAC_ID_GREEK"),
    RUSSIAN(7, "TT_MAC_ID_RUSSIAN"),
    RSYMBOL(8, "TT_MAC_ID_RSYMBOL"),
    DEVANAGARI(9, "TT_MAC_ID_DEVANAGARI"),
    GURMUKHI(10, "TT_MAC_ID_GURMUKHI"),
    GUJARATI(11, "TT_MAC_ID_GUJARATI"),
    ORIYA(12, "TT_MAC_ID_ORIYA"),
    BENGALI(13, "TT_MAC_ID_BENGALI"),
    TAMIL(14, "TT_MAC_ID_TAMIL"),
    TELUGU(15, "TT_MAC_ID_TELUGU"),
    KANNADA(16, "TT_MAC_ID_KANNADA"),
    MALAYALAM(17, "TT_MAC_ID_MALAYALAM"),
    SINHALESE(18, "TT_MAC_ID_SINHALESE"),
    BURMESE(19, "TT_MAC_ID_BURMESE"),
    KHMER(20, "TT_MAC_ID_KHMER"),
    THAI(21, "TT_MAC_ID_THAI"),
    LAOTIAN(22, "TT_MAC_ID_LAOTIAN"),
    GEORGIAN(23, "TT_MAC_ID_GEORGIAN"),
    ARMENIAN(24, "TT_MAC_ID_ARMENIAN"),
    MALDIVIAN(25, "TT_MAC_ID_MALDIVIAN"),
    SIMPLIFIED_CHINESE(25, "TT_MAC_ID_SIMPLIFIED_CHINESE"),
    TIBETAN(26, "TT_MAC_ID_TIBETAN"),
    MONGOLIAN(27, "TT_MAC_ID_MONGOLIAN"),
    GEEZ(28, "TT_MAC_ID_GEEZ"),
    SLAVIC(29, "TT_MAC_ID_SLAVIC"),
    VIETNAMESE(30, "TT_MAC_ID_VIETNAMESE"),
    SINDHI(31, "TT_MAC_ID_SINDHI"),
    UNINTERP(32, "TT_MAC_ID_UNINTERP");
    private int val;
    private String str;
    private static SparseArray<MacId> tagToMacIdMapping;
    public static MacId getTableTag(int i) {
      if (tagToMacIdMapping == null) {
        initMapping();
      }
      return tagToMacIdMapping.get(i);
    }
    private static void initMapping() {
      tagToMacIdMapping = new SparseArray<MacId>();
      for (MacId t : values()) {
        tagToMacIdMapping.put(t.val, t);
      }
    }
    private MacId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* =========================================================================
   *
   * @enum:
   *   TT_ISO_ID_XXX
   *
   * @description:
   *   A list of valid values for the `encoding_id' for
   *   @TT_PLATFORM_ISO charmaps and name entries.
   *
   *   Their use is now deprecated.
   *
   * @values:
   *   TT_ISO_ID_7BIT_ASCII ::
   *     ASCII.
   *   TT_ISO_ID_10646 ::
   *     ISO/10646.
   *   TT_ISO_ID_8859_1 ::
   *     Also known as Latin-1.
   * =========================================================================
   */
  public enum Iso {
    ID_7BIT_ASCII(0, "TT_ISO_ID_7BIT_ASCII"),
    ID_10646(1, "TT_ISO_ID_10646"),
    ID_8859_1(2, "TT_ISO_ID_8859_1");
    private int val;
    private String str;
    private static SparseArray<Iso> tagToIsoMapping;
    public static Iso getTableTag(int i) {
      if (tagToIsoMapping == null) {
        initMapping();
      }
      return tagToIsoMapping.get(i);
    }
    private static void initMapping() {
      tagToIsoMapping = new SparseArray<Iso>();
      for (Iso t : values()) {
        tagToIsoMapping.put(t.val, t);
      }
    }
    private Iso(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* =========================================================================
   *
   * @enum:
   *   TT_MS_ID_XXX
   *
   * @description:
   *   A list of valid values for the `encoding_id' for
   *   @TT_PLATFORM_MICROSOFT charmaps and name entries.
   *
   * @values:
   *   TT_MS_ID_SYMBOL_CS ::
   *     Corresponds to Microsoft symbol encoding. See
   *     @FT_ENCODING_MS_SYMBOL.
   *
   *   TT_MS_ID_UNICODE_CS ::
   *     Corresponds to a Microsoft WGL4 charmap, matching Unicode.  See
   *     @FT_ENCODING_UNICODE.
   *
   *   TT_MS_ID_SJIS ::
   *     Corresponds to SJIS Japanese encoding.  See @FT_ENCODING_SJIS.
   *
   *   TT_MS_ID_GB2312 ::
   *     Corresponds to Simplified Chinese as used in Mainland China.  See
   *     @FT_ENCODING_GB2312.
   *
   *   TT_MS_ID_BIG_5 ::
   *     Corresponds to Traditional Chinese as used in Taiwan and Hong Kong.
   *     See @FT_ENCODING_BIG5.
   *
   *   TT_MS_ID_WANSUNG ::
   *     Corresponds to Korean Wansung encoding.  See @FT_ENCODING_WANSUNG.
   *
   *   TT_MS_ID_JOHAB ::
   *     Corresponds to Johab encoding.  See @FT_ENCODING_JOHAB.
   *
   *   TT_MS_ID_UCS_4 ::
   *     Corresponds to UCS-4 or UTF-32 charmaps.  This has been added to
   *     the OpenType specification version 1.4 (mid-2001.)
   * =========================================================================
   */
  public enum MsId {
    SYMBOL_CS(0, "TT_MS_ID_SYMBOL_CS"),
    UNICODE_CS(1, "TT_MS_ID_UNICODE_CS"),
    SJIS(2, "TT_MS_ID_SJIS"),
    GB2312(3, "TT_MS_ID_GB2312"),
    BIG_5(4, "TT_MS_ID_BIG_5"),
    WANSUNG(5, "TT_MS_ID_WANSUNG"),
    JOHAB(6, "TT_MS_ID_JOHAB"),
    UCS_4(10, "TT_MS_ID_UCS_4");
    private int val;
    private String str;
    private static SparseArray<MsId> tagToMsIdMapping;
    public static MsId getTableTag(int i) {
      if (tagToMsIdMapping == null) {
        initMapping();
      }
      return tagToMsIdMapping.get(i);
    }
    private static void initMapping() {
      tagToMsIdMapping = new SparseArray<MsId>();
      for (MsId t : values()) {
        tagToMsIdMapping.put(t.val, t);
      }
    }
    private MsId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* =========================================================================
     *
     * @enum:
     *   TT_ADOBE_ID_XXX
     *
     * @description:
     *   A list of valid values for the `encoding_id' for
     *   @TT_PLATFORM_ADOBE charmaps.  This is a FreeType-specific extension!
     *
     * @values:
     *   TT_ADOBE_ID_STANDARD ::
     *     Adobe standard encoding.
     *   TT_ADOBE_ID_EXPERT ::
     *     Adobe expert encoding.
     *   TT_ADOBE_ID_CUSTOM ::
     *     Adobe custom encoding.
     *   TT_ADOBE_ID_LATIN_1 ::
     *     Adobe Latin~1 encoding.
     * =========================================================================
     */
  public enum AdobeId {
    STANDARD(0, "TT_ADOBE_ID_STANDARD"),
    EXPERT(1, "TT_ADOBE_ID_EXPERT"),
    CUSTOM(2, "TT_ADOBE_ID_CUSTOM"),
    LATIN_1(3, "TT_ADOBE_ID_LATIN_1");
    private int val;
    private String str;
    private static SparseArray<AdobeId> tagToAdobeIdMapping;
    public static AdobeId getTableTag(int i) {
      if (tagToAdobeIdMapping == null) {
        initMapping();
      }
      return tagToAdobeIdMapping.get(i);
    }
    private static void initMapping() {
      tagToAdobeIdMapping = new SparseArray<AdobeId>();
      for (AdobeId t : values()) {
        tagToAdobeIdMapping.put(t.val, t);
      }
    }
    private AdobeId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* =========================================================================
     *
     * Possible values of the language identifier field in the name records
     * of the TTF `name' table if the `platform' identifier code is
     * TT_PLATFORM_MACINTOSH.  These values are also used as return values
     * for function @FT_Get_CMap_Language_ID.
     *
     * The canonical source for the Apple assigned Language ID's is at
     *
     *   https://developer.apple.com/fonts/TTRefMan/RM06/Chap6name.html
     *
     * =========================================================================
     */
  public enum MacLangId {
    ENGLISH(0, "TT_MAC_LANGID_ENGLISH"),
    FRENCH(1, "TT_MAC_LANGID_FRENCH"),
    GERMAN(2, "TT_MAC_LANGID_GERMAN"),
    ITALIAN(3, "TT_MAC_LANGID_ITALIAN"),
    DUTCH(4, "TT_MAC_LANGID_DUTCH"),
    SWEDISH(5, "TT_MAC_LANGID_SWEDISH"),
    SPANISH(6, "TT_MAC_LANGID_SPANISH"),
    DANISH(7, "TT_MAC_LANGID_DANISH"),
    PORTUGUESE(8, "TT_MAC_LANGID_PORTUGUESE"),
    NORWEGIAN(9, "TT_MAC_LANGID_NORWEGIAN"),
    HEBREW(10, "TT_MAC_LANGID_HEBREW"),
    JAPANESE(11, "TT_MAC_LANGID_JAPANESE"),
    ARABIC(12, "TT_MAC_LANGID_ARABIC"),
    FINNISH(13, "TT_MAC_LANGID_FINNISH"),
    GREEK(14, "TT_MAC_LANGID_GREEK"),
    ICELANDIC(15, "TT_MAC_LANGID_ICELANDIC"),
    MALTESE(16, "TT_MAC_LANGID_MALTESE"),
    TURKISH(17, "TT_MAC_LANGID_TURKISH"),
    CROATIAN(18, "TT_MAC_LANGID_CROATIAN"),
    CHINESE_TRADITIONAL(19, "TT_MAC_LANGID_CHINESE_TRADITIONAL"),
    URDU(20, "TT_MAC_LANGID_URDU"),
    HINDI(21, "TT_MAC_LANGID_HINDI"),
    THAI(22, "TT_MAC_LANGID_THAI"),
    KOREAN(23, "TT_MAC_LANGID_KOREAN"),
    LITHUANIAN(24, "TT_MAC_LANGID_LITHUANIAN"),
    POLISH(25, "TT_MAC_LANGID_POLISH"),
    HUNGARIAN(26, "TT_MAC_LANGID_HUNGARIAN"),
    ESTONIAN(27, "TT_MAC_LANGID_ESTONIAN"),
    LETTISH(28, "TT_MAC_LANGID_LETTISH"),
    SAAMISK(29, "TT_MAC_LANGID_SAAMISK"),
    FAEROESE(30, "TT_MAC_LANGID_FAEROESE"),
    FARSI(31, "TT_MAC_LANGID_FARSI"),
    RUSSIAN(32, "TT_MAC_LANGID_RUSSIAN"),
    CHINESE_SIMPLIFIED(33, "TT_MAC_LANGID_CHINESE_SIMPLIFIED"),
    FLEMISH(34, "TT_MAC_LANGID_FLEMISH"),
    IRISH(35, "TT_MAC_LANGID_IRISH"),
    ALBANIAN(36, "TT_MAC_LANGID_ALBANIAN"),
    ROMANIAN(37, "TT_MAC_LANGID_ROMANIAN"),
    CZECH(38, "TT_MAC_LANGID_CZECH"),
    SLOVAK(39, "TT_MAC_LANGID_SLOVAK"),
    SLOVENIAN(40, "TT_MAC_LANGID_SLOVENIAN"),
    YIDDISH(41, "TT_MAC_LANGID_YIDDISH"),
    SERBIAN(42, "TT_MAC_LANGID_SERBIAN"),
    MACEDONIAN(43, "TT_MAC_LANGID_MACEDONIAN"),
    BULGARIAN(44, "TT_MAC_LANGID_BULGARIAN"),
    UKRAINIAN(45, "TT_MAC_LANGID_UKRAINIAN"),
    BYELORUSSIAN(46, "TT_MAC_LANGID_BYELORUSSIAN"),
    UZBEK(47, "TT_MAC_LANGID_UZBEK"),
    KAZAKH(48, "TT_MAC_LANGID_KAZAKH"),
    AZERBAIJANI(49, "TT_MAC_LANGID_AZERBAIJANI"),
    AZERBAIJANI_CYRILLIC_SCRIPT(49, "TT_MAC_LANGID_AZERBAIJANI_CYRILLIC_SCRIPT"),
    AZERBAIJANI_ARABIC_SCRIPT(50, "TT_MAC_LANGID_AZERBAIJANI_ARABIC_SCRIPT"),
    ARMENIAN(51, "TT_MAC_LANGID_ARMENIAN"),
    GEORGIAN(52, "TT_MAC_LANGID_GEORGIAN"),
    MOLDAVIAN(53, "TT_MAC_LANGID_MOLDAVIAN"),
    KIRGHIZ(54, "TT_MAC_LANGID_KIRGHIZ"),
    TAJIKI(55, "TT_MAC_LANGID_TAJIKI"),
    TURKMEN(56, "TT_MAC_LANGID_TURKMEN"),
    MONGOLIAN(57, "TT_MAC_LANGID_MONGOLIAN"),
    MONGOLIAN_MONGOLIAN_SCRIPT(57, "TT_MAC_LANGID_MONGOLIAN_MONGOLIAN_SCRIPT"),
    MONGOLIAN_CYRILLIC_SCRIPT(58, "TT_MAC_LANGID_MONGOLIAN_CYRILLIC_SCRIPT"),
    PASHTO(59, "TT_MAC_LANGID_PASHTO"),
    KURDISH(60, "TT_MAC_LANGID_KURDISH"),
    KASHMIRI(61, "TT_MAC_LANGID_KASHMIRI"),
    SINDHI(62, "TT_MAC_LANGID_SINDHI"),
    TIBETAN(63, "TT_MAC_LANGID_TIBETAN"),
    NEPALI(64, "TT_MAC_LANGID_NEPALI"),
    SANSKRIT(65, "TT_MAC_LANGID_SANSKRIT"),
    MARATHI(66, "TT_MAC_LANGID_MARATHI"),
    BENGALI(67, "TT_MAC_LANGID_BENGALI"),
    ASSAMESE(68, "TT_MAC_LANGID_ASSAMESE"),
    GUJARATI(69, "TT_MAC_LANGID_GUJARATI"),
    PUNJABI(70, "TT_MAC_LANGID_PUNJABI"),
    ORIYA(71, "TT_MAC_LANGID_ORIYA"),
    MALAYALAM(72, "TT_MAC_LANGID_MALAYALAM"),
    KANNADA(73, "TT_MAC_LANGID_KANNADA"),
    TAMIL(74, "TT_MAC_LANGID_TAMIL"),
    TELUGU(75, "TT_MAC_LANGID_TELUGU"),
    SINHALESE(76, "TT_MAC_LANGID_SINHALESE"),
    BURMESE(77, "TT_MAC_LANGID_BURMESE"),
    KHMER(78, "TT_MAC_LANGID_KHMER"),
    LAO(79, "TT_MAC_LANGID_LAO"),
    VIETNAMESE(80, "TT_MAC_LANGID_VIETNAMESE"),
    INDONESIAN(81, "TT_MAC_LANGID_INDONESIAN"),
    TAGALOG(82, "TT_MAC_LANGID_TAGALOG"),
    MALAY_ROMAN_SCRIPT(83, "TT_MAC_LANGID_MALAY_ROMAN_SCRIPT"),
    MALAY_ARABIC_SCRIPT(84, "TT_MAC_LANGID_MALAY_ARABIC_SCRIPT"),
    AMHARIC(85, "TT_MAC_LANGID_AMHARIC"),
    TIGRINYA(86, "TT_MAC_LANGID_TIGRINYA"),
    GALLA(87, "TT_MAC_LANGID_GALLA"),
    SOMALI(88, "TT_MAC_LANGID_SOMALI"),
    SWAHILI(89, "TT_MAC_LANGID_SWAHILI"),
    RUANDA(90, "TT_MAC_LANGID_RUANDA"),
    RUNDI(91, "TT_MAC_LANGID_RUNDI"),
    CHEWA(92, "TT_MAC_LANGID_CHEWA"),
    MALAGASY(93, "TT_MAC_LANGID_MALAGASY"),
    ESPERANTO(94, "TT_MAC_LANGID_ESPERANTO"),
    WELSH(128, "TT_MAC_LANGID_WELSH"),
    BASQUE(129, "TT_MAC_LANGID_BASQUE"),
    CATALAN(130, "TT_MAC_LANGID_CATALAN"),
    LATIN(131, "TT_MAC_LANGID_LATIN"),
    QUECHUA(132, "TT_MAC_LANGID_QUECHUA"),
    GUARANI(133, "TT_MAC_LANGID_GUARANI"),
    AYMARA(134, "TT_MAC_LANGID_AYMARA"),
    TATAR(135, "TT_MAC_LANGID_TATAR"),
    UIGHUR(136, "TT_MAC_LANGID_UIGHUR"),
    DZONGKHA(137, "TT_MAC_LANGID_DZONGKHA"),
    JAVANESE(138, "TT_MAC_LANGID_JAVANESE"),
    SUNDANESE(139, "TT_MAC_LANGID_SUNDANESE"),
    /* The Following codes are new as of 2000-03-10 */
    GALICIAN(140, "TT_MAC_LANGID_GALICIAN"),
    AFRIKAANS(141, "TT_MAC_LANGID_AFRIKAANS"),
    BRETON(142, "TT_MAC_LANGID_BRETON"),
    INUKTITUT(143, "TT_MAC_LANGID_INUKTITUT"),
    SCOTTISH_GAELIC(144, "TT_MAC_LANGID_SCOTTISH_GAELIC"),
    MANX_GAELIC(145, "TT_MAC_LANGID_MANX_GAELIC"),
    IRISH_GAELIC(146, "TT_MAC_LANGID_IRISH_GAELIC"),
    TONGAN(147, "TT_MAC_LANGID_TONGAN"),
    GREEK_POLYTONIC(148, "TT_MAC_LANGID_GREEK_POLYTONIC"),
    GREELANDIC(149, "TT_MAC_LANGID_GREELANDIC"),
    AZERBAIJANI_ROMAN_SCRIPT(150, "TT_MAC_LANGID_AZERBAIJANI_ROMAN_SCRIPT");
    private int val;
    private String str;
    private static SparseArray<MacLangId> tagToMacLangIdMapping;
    public static MacLangId getTableTag(int i) {
      if (tagToMacLangIdMapping == null) {
        initMapping();
      }
      return tagToMacLangIdMapping.get(i);
    }
    private static void initMapping() {
      tagToMacLangIdMapping = new SparseArray<MacLangId>();
      for (MacLangId t : values()) {
        tagToMacLangIdMapping.put(t.val, t);
      }
    }
    private MacLangId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }


  /* =========================================================================
     *
     * Possible values of the language identifier field in the name records
     * of the TTF `name' table if the `platform' identifier code is
     * TT_PLATFORM_MICROSOFT.
     *
     * The canonical source for the MS assigned LCIDs is
     *
     *   http://www.microsoft.com/globaldev/reference/lcid-all.mspx
     *
     * =========================================================================
     */
  public enum MsLangId {
    ARABIC_GENERAL(0x0001, "TT_MS_LANGID_ARABIC_GENERAL"),
    ARABIC_SAUDI_ARABIA(0x0401, "TT_MS_LANGID_ARABIC_SAUDI_ARABIA"),
    ARABIC_IRAQ(0x0801, "TT_MS_LANGID_ARABIC_IRAQ"),
    ARABIC_EGYPT(0x0c01, "TT_MS_LANGID_ARABIC_EGYPT"),
    ARABIC_LIBYA(0x1001, "TT_MS_LANGID_ARABIC_LIBYA"),
    ARABIC_ALGERIA(0x1401, "TT_MS_LANGID_ARABIC_ALGERIA"),
    ARABIC_MOROCCO(0x1801, "TT_MS_LANGID_ARABIC_MOROCCO"),
    ARABIC_TUNISIA(0x1c01, "TT_MS_LANGID_ARABIC_TUNISIA"),
    ARABIC_OMAN(0x2001, "TT_MS_LANGID_ARABIC_OMAN"),
    ARABIC_YEMEN(0x2401, "TT_MS_LANGID_ARABIC_YEMEN"),
    ARABIC_SYRIA(0x2801, "TT_MS_LANGID_ARABIC_SYRIA"),
    ARABIC_JORDAN(0x2c01, "TT_MS_LANGID_ARABIC_JORDAN"),
    ARABIC_LEBANON(0x3001, "TT_MS_LANGID_ARABIC_LEBANON"),
    ARABIC_KUWAIT(0x3401, "TT_MS_LANGID_ARABIC_KUWAIT"),
    ARABIC_UAE(0x3801, "TT_MS_LANGID_ARABIC_UAE"),
    ARABIC_BAHRAIN(0x3c01, "TT_MS_LANGID_ARABIC_BAHRAIN"),
    ARABIC_QATAR(0x4001, "TT_MS_LANGID_ARABIC_QATAR"),
    BULGARIAN_BULGARIA(0x0402, "TT_MS_LANGID_BULGARIAN_BULGARIA"),
    CATALAN_SPAIN(0x0403, "TT_MS_LANGID_CATALAN_SPAIN"),
    CHINESE_GENERAL(0x0004, "TT_MS_LANGID_CHINESE_GENERAL"),
    CHINESE_TAIWAN(0x0404, "TT_MS_LANGID_CHINESE_TAIWAN"),
    CHINESE_PRC(0x0804, "TT_MS_LANGID_CHINESE_PRC"),
    CHINESE_HONG_KONG(0x0c04, "TT_MS_LANGID_CHINESE_HONG_KONG"),
    CHINESE_SINGAPORE(0x1004, "TT_MS_LANGID_CHINESE_SINGAPORE"),
    CHINESE_MACAU(0x1404, "TT_MS_LANGID_CHINESE_MACAU"),
    CZECH_CZECH_REPUBLIC(0x0405, "TT_MS_LANGID_CZECH_CZECH_REPUBLIC"),
    DANISH_DENMARK(0x0406, "TT_MS_LANGID_DANISH_DENMARK"),
    GERMAN_GERMANY(0x0407, "TT_MS_LANGID_GERMAN_GERMANY"),
    GERMAN_SWITZERLAND(0x0807, "TT_MS_LANGID_GERMAN_SWITZERLAND"),
    GERMAN_AUSTRIA(0x0c07, "TT_MS_LANGID_GERMAN_AUSTRIA"),
    GERMAN_LUXEMBOURG(0x1007, "TT_MS_LANGID_GERMAN_LUXEMBOURG"),
    GERMAN_LIECHTENSTEI(0x1407, "TT_MS_LANGID_GERMAN_LIECHTENSTEI"),
    GREEK_GREECE(0x0408, "TT_MS_LANGID_GREEK_GREECE"),
    ENGLISH_GENERAL(0x0009, "TT_MS_LANGID_ENGLISH_GENERAL"),
    ENGLISH_UNITED_STATES(0x0409, "TT_MS_LANGID_ENGLISH_UNITED_STATES"),
    ENGLISH_UNITED_KINGDOM(0x0809, "TT_MS_LANGID_ENGLISH_UNITED_KINGDOM"),
    ENGLISH_AUSTRALIA(0x0c09, "TT_MS_LANGID_ENGLISH_AUSTRALIA"),
    ENGLISH_CANADA(0x1009, "TT_MS_LANGID_ENGLISH_CANADA"),
    ENGLISH_NEW_ZEALAND(0x1409, "TT_MS_LANGID_ENGLISH_NEW_ZEALAND"),
    ENGLISH_IRELAND(0x1809, "TT_MS_LANGID_ENGLISH_IRELAND"),
    ENGLISH_SOUTH_AFRICA(0x1c09, "TT_MS_LANGID_ENGLISH_SOUTH_AFRICA"),
    ENGLISH_JAMAICA(0x2009, "TT_MS_LANGID_ENGLISH_JAMAICA"),
    ENGLISH_CARIBBEAN(0x2409, "TT_MS_LANGID_ENGLISH_CARIBBEAN"),
    ENGLISH_BELIZE(0x2809, "TT_MS_LANGID_ENGLISH_BELIZE"),
    ENGLISH_TRINIDAD(0x2c09, "TT_MS_LANGID_ENGLISH_TRINIDAD"),
    ENGLISH_ZIMBABWE(0x3009, "TT_MS_LANGID_ENGLISH_ZIMBABWE"),
    ENGLISH_PHILIPPINES(0x3409, "TT_MS_LANGID_ENGLISH_PHILIPPINES"),
    ENGLISH_INDONESIA(0x3809, "TT_MS_LANGID_ENGLISH_INDONESIA"),
    ENGLISH_HONG_KONG(0x3c09, "TT_MS_LANGID_ENGLISH_HONG_KONG"),
    ENGLISH_INDIA(0x4009, "TT_MS_LANGID_ENGLISH_INDIA"),
    ENGLISH_MALAYSIA(0x4409, "TT_MS_LANGID_ENGLISH_MALAYSIA"),
    ENGLISH_SINGAPORE(0x4809, "TT_MS_LANGID_ENGLISH_SINGAPORE"),
    SPANISH_SPAIN_TRADITIONAL_SORT(0x040a, "TT_MS_LANGID_SPANISH_SPAIN_TRADITIONAL_SORT"),
    SPANISH_MEXICO(0x080a, "TT_MS_LANGID_SPANISH_MEXICO"),
    SPANISH_SPAIN_INTERNATIONAL_SORT(0x0c0a, "TT_MS_LANGID_SPANISH_SPAIN_INTERNATIONAL_SORT"),
    SPANISH_GUATEMALA(0x100a, "TT_MS_LANGID_SPANISH_GUATEMALA"),
    SPANISH_COSTA_RICA(0x140a, "TT_MS_LANGID_SPANISH_COSTA_RICA"),
    SPANISH_PANAMA(0x180a, "TT_MS_LANGID_SPANISH_PANAMA"),
    SPANISH_DOMINICAN_REPUBLIC(0x1c0a, "TT_MS_LANGID_SPANISH_DOMINICAN_REPUBLIC"),
    SPANISH_VENEZUELA(0x200a, "TT_MS_LANGID_SPANISH_VENEZUELA"),
    SPANISH_COLOMBIA(0x240a, "TT_MS_LANGID_SPANISH_COLOMBIA"),
    SPANISH_PERU(0x280a, "TT_MS_LANGID_SPANISH_PERU"),
    SPANISH_ARGENTINA(0x2c0a, "TT_MS_LANGID_SPANISH_ARGENTINA"),
    SPANISH_ECUADOR(0x300a, "TT_MS_LANGID_SPANISH_ECUADOR"),
    SPANISH_CHILE(0x340a, "TT_MS_LANGID_SPANISH_CHILE"),
    SPANISH_URUGUAY(0x380a, "TT_MS_LANGID_SPANISH_URUGUAY"),
    SPANISH_PARAGUAY(0x3c0a, "TT_MS_LANGID_SPANISH_PARAGUAY"),
    SPANISH_BOLIVIA(0x400a, "TT_MS_LANGID_SPANISH_BOLIVIA"),
    SPANISH_EL_SALVADOR(0x440a, "TT_MS_LANGID_SPANISH_EL_SALVADOR"),
    SPANISH_HONDURAS(0x480a, "TT_MS_LANGID_SPANISH_HONDURAS"),
    SPANISH_NICARAGUA(0x4c0a, "TT_MS_LANGID_SPANISH_NICARAGUA"),
    SPANISH_PUERTO_RICO(0x500a, "TT_MS_LANGID_SPANISH_PUERTO_RICO"),
    SPANISH_UNITED_STATES(0x540a, "TT_MS_LANGID_SPANISH_UNITED_STATES"),
    /* The following ID blatantly violate MS specs by using a */
      /* sublanguage > 0x1F.                                    */
    SPANISH_LATIN_AMERICA(0xE40a, "TT_MS_LANGID_SPANISH_LATIN_AMERICA"),
    FINNISH_FINLAND(0x040b, "TT_MS_LANGID_FINNISH_FINLAND"),
    FRENCH_FRANCE(0x040c, "TT_MS_LANGID_FRENCH_FRANCE"),
    FRENCH_BELGIUM(0x080c, "TT_MS_LANGID_FRENCH_BELGIUM"),
    FRENCH_CANADA(0x0c0c, "TT_MS_LANGID_FRENCH_CANADA"),
    FRENCH_SWITZERLAND(0x100c, "TT_MS_LANGID_FRENCH_SWITZERLAND"),
    FRENCH_LUXEMBOURG(0x140c, "TT_MS_LANGID_FRENCH_LUXEMBOURG"),
    FRENCH_MONACO(0x180c, "TT_MS_LANGID_FRENCH_MONACO"),
    FRENCH_WEST_INDIES(0x1c0c, "TT_MS_LANGID_FRENCH_WEST_INDIES"),
    FRENCH_REUNION(0x200c, "TT_MS_LANGID_FRENCH_REUNION"),
    FRENCH_CONGO(0x240c, "TT_MS_LANGID_FRENCH_CONGO"),
    /* which was formerly: */
    FRENCH_ZAIRE(0x240c, "TT_MS_LANGID_FRENCH_ZAIRE"),
    FRENCH_SENEGAL(0x280c, "TT_MS_LANGID_FRENCH_SENEGAL"),
    FRENCH_CAMEROON(0x2c0c, "TT_MS_LANGID_FRENCH_CAMEROON"),
    FRENCH_COTE_D_IVOIRE(0x300c, "TT_MS_LANGID_FRENCH_COTE_D_IVOIRE"),
    FRENCH_MALI(0x340c, "TT_MS_LANGID_FRENCH_MALI"),
    FRENCH_MOROCCO(0x380c, "TT_MS_LANGID_FRENCH_MOROCCO"),
    FRENCH_HAITI(0x3c0c, "TT_MS_LANGID_FRENCH_HAITI"),
    /* and another violation of the spec (see 0xE40aU) */
    FRENCH_NORTH_AFRICA(0xE40c, "TT_MS_LANGID_FRENCH_NORTH_AFRICA"),
    HEBREW_ISRAEL(0x040d, "TT_MS_LANGID_HEBREW_ISRAEL"),
    HUNGARIAN_HUNGARY(0x040e, "TT_MS_LANGID_HUNGARIAN_HUNGARY"),
    ICELANDIC_ICELAND(0x040f, "TT_MS_LANGID_ICELANDIC_ICELAND"),
    ITALIAN_ITALY(0x0410, "TT_MS_LANGID_ITALIAN_ITALY"),
    ITALIAN_SWITZERLAND(0x0810, "TT_MS_LANGID_ITALIAN_SWITZERLAND"),
    JAPANESE_JAPAN(0x0411, "TT_MS_LANGID_JAPANESE_JAPAN"),
    KOREAN_EXTENDED_WANSUNG_KOREA(0x0412, "TT_MS_LANGID_KOREAN_EXTENDED_WANSUNG_KOREA"),
    KOREAN_JOHAB_KOREA(0x0812, "TT_MS_LANGID_KOREAN_JOHAB_KOREA"),
    DUTCH_NETHERLANDS(0x0413, "TT_MS_LANGID_DUTCH_NETHERLANDS"),
    DUTCH_BELGIUM(0x0813, "TT_MS_LANGID_DUTCH_BELGIUM"),
    NORWEGIAN_NORWAY_BOKMAL(0x0414, "TT_MS_LANGID_NORWEGIAN_NORWAY_BOKMAL"),
    NORWEGIAN_NORWAY_NYNORSK(0x0814, "TT_MS_LANGID_NORWEGIAN_NORWAY_NYNORSK"),
    POLISH_POLAND(0x0415, "TT_MS_LANGID_POLISH_POLAND"),
    PORTUGUESE_BRAZIL(0x0416, "TT_MS_LANGID_PORTUGUESE_BRAZIL"),
    PORTUGUESE_PORTUGAL(0x0816, "TT_MS_LANGID_PORTUGUESE_PORTUGAL"),
    RHAETO_ROMANIC_SWITZERLAND(0x0417, "TT_MS_LANGID_RHAETO_ROMANIC_SWITZERLAND"),
    ROMANIAN_ROMANIA(0x0418, "TT_MS_LANGID_ROMANIAN_ROMANIA"),
    MOLDAVIAN_MOLDAVIA(0x0818, "TT_MS_LANGID_MOLDAVIAN_MOLDAVIA"),
    RUSSIAN_RUSSIA(0x0419, "TT_MS_LANGID_RUSSIAN_RUSSIA"),
    RUSSIAN_MOLDAVIA(0x0819, "TT_MS_LANGID_RUSSIAN_MOLDAVIA"),
    CROATIAN_CROATIA(0x041a, "TT_MS_LANGID_CROATIAN_CROATIA"),
    SERBIAN_SERBIA_LATIN(0x081a, "TT_MS_LANGID_SERBIAN_SERBIA_LATIN"),
    SERBIAN_SERBIA_CYRILLIC(0x0c1a, "TT_MS_LANGID_SERBIAN_SERBIA_CYRILLIC"),
    CROATIAN_BOSNIA_HERZEGOVINA(0x101a, "TT_MS_LANGID_CROATIAN_BOSNIA_HERZEGOVINA"),
    BOSNIAN_BOSNIA_HERZEGOVINA(0x141a, "TT_MS_LANGID_BOSNIAN_BOSNIA_HERZEGOVINA"),
    /* and XPsp2 Platform SDK added (2004-07-26) */
      /* Names are shortened to be significant within 40 chars. */
    SERBIAN_BOSNIA_HERZ_LATIN(0x181a, "TT_MS_LANGID_SERBIAN_BOSNIA_HERZ_LATIN"),
    SERBIAN_BOSNIA_HERZ_CYRILLIC(0x181a, "TT_MS_LANGID_SERBIAN_BOSNIA_HERZ_CYRILLIC"),
    SLOVAK_SLOVAKIA(0x041b, "TT_MS_LANGID_SLOVAK_SLOVAKIA"),
    ALBANIAN_ALBANIA(0x041c, "TT_MS_LANGID_ALBANIAN_ALBANIA"),
    SWEDISH_SWEDEN(0x041d, "TT_MS_LANGID_SWEDISH_SWEDEN"),
    SWEDISH_FINLAND(0x081d, "TT_MS_LANGID_SWEDISH_FINLAND"),
    THAI_THAILAND(0x041e, "TT_MS_LANGID_THAI_THAILAND"),
    TURKISH_TURKEY(0x041f, "TT_MS_LANGID_TURKISH_TURKEY"),
    URDU_PAKISTAN(0x0420, "TT_MS_LANGID_URDU_PAKISTAN"),
    URDU_INDIA(0x0820, "TT_MS_LANGID_URDU_INDIA"),
    INDONESIAN_INDONESIA(0x0421, "TT_MS_LANGID_INDONESIAN_INDONESIA"),
    UKRAINIAN_UKRAINE(0x0422, "TT_MS_LANGID_UKRAINIAN_UKRAINE"),
    BELARUSIAN_BELARUS(0x0423, "TT_MS_LANGID_BELARUSIAN_BELARUS"),
    SLOVENE_SLOVENIA(0x0424, "TT_MS_LANGID_SLOVENE_SLOVENIA"),
    ESTONIAN_ESTONIA(0x0425, "TT_MS_LANGID_ESTONIAN_ESTONIA"),
    LATVIAN_LATVIA(0x0426, "TT_MS_LANGID_LATVIAN_LATVIA"),
    LITHUANIAN_LITHUANIA(0x0427, "TT_MS_LANGID_LITHUANIAN_LITHUANIA"),
    CLASSIC_LITHUANIAN_LITHUANIA(0x0827, "TT_MS_LANGID_CLASSIC_LITHUANIAN_LITHUANIA"),
    TAJIK_TAJIKISTAN(0x0428, "TT_MS_LANGID_TAJIK_TAJIKISTAN"),
    FARSI_IRAN(0x0429, "TT_MS_LANGID_FARSI_IRAN"),
    VIETNAMESE_VIET_NAM(0x042a, "TT_MS_LANGID_VIETNAMESE_VIET_NAM"),
    ARMENIAN_ARMENIA(0x042b, "TT_MS_LANGID_ARMENIAN_ARMENIA"),
    AZERI_AZERBAIJAN_LATIN(0x042c, "TT_MS_LANGID_AZERI_AZERBAIJAN_LATIN"),
    AZERI_AZERBAIJAN_CYRILLIC(0x082c, "TT_MS_LANGID_AZERI_AZERBAIJAN_CYRILLIC"),
    BASQUE_SPAIN(0x042d, "TT_MS_LANGID_BASQUE_SPAIN"),
    SORBIAN_GERMANY(0x042e, "TT_MS_LANGID_SORBIAN_GERMANY"),
    MACEDONIAN_MACEDONIA(0x042f, "TT_MS_LANGID_MACEDONIAN_MACEDONIA"),
    SUTU_SOUTH_AFRICA(0x0430, "TT_MS_LANGID_SUTU_SOUTH_AFRICA"),
    TSONGA_SOUTH_AFRICA(0x0431, "TT_MS_LANGID_TSONGA_SOUTH_AFRICA"),
    TSWANA_SOUTH_AFRICA(0x0432, "TT_MS_LANGID_TSWANA_SOUTH_AFRICA"),
    VENDA_SOUTH_AFRICA(0x0433, "TT_MS_LANGID_VENDA_SOUTH_AFRICA"),
    XHOSA_SOUTH_AFRICA(0x0434, "TT_MS_LANGID_XHOSA_SOUTH_AFRICA"),
    ZULU_SOUTH_AFRICA(0x0435, "TT_MS_LANGID_ZULU_SOUTH_AFRICA"),
    AFRIKAANS_SOUTH_AFRICA(0x0436, "TT_MS_LANGID_AFRIKAANS_SOUTH_AFRICA"),
    GEORGIAN_GEORGIA(0x0437, "TT_MS_LANGID_GEORGIAN_GEORGIA"),
    FAEROESE_FAEROE_ISLANDS(0x0438, "TT_MS_LANGID_FAEROESE_FAEROE_ISLANDS"),
    HINDI_INDIA(0x0439, "TT_MS_LANGID_HINDI_INDIA"),
    MALTESE_MALTA(0x043a, "TT_MS_LANGID_MALTESE_MALTA"),
    /* Added by XPsp2 Platform SDK (2004-07-26) */
    SAMI_NORTHERN_NORWAY(0x043b, "TT_MS_LANGID_SAMI_NORTHERN_NORWAY"),
    SAMI_NORTHERN_SWEDEN(0x083b, "TT_MS_LANGID_SAMI_NORTHERN_SWEDEN"),
    SAMI_NORTHERN_FINLAND(0x0C3b, "TT_MS_LANGID_SAMI_NORTHERN_FINLAND"),
    SAMI_LULE_NORWAY(0x103b, "TT_MS_LANGID_SAMI_LULE_NORWAY"),
    SAMI_LULE_SWEDEN(0x143b, "TT_MS_LANGID_SAMI_LULE_SWEDEN"),
    SAMI_SOUTHERN_NORWAY(0x183b, "TT_MS_LANGID_SAMI_SOUTHERN_NORWAY"),
    SAMI_SOUTHERN_SWEDEN(0x1C3b, "TT_MS_LANGID_SAMI_SOUTHERN_SWEDEN"),
    SAMI_SKOLT_FINLAND(0x203b, "TT_MS_LANGID_SAMI_SKOLT_FINLAND"),
    SAMI_INARI_FINLAND(0x243b, "TT_MS_LANGID_SAMI_INARI_FINLAND"),
    /* ... and we also keep our old identifier... */
    SAAMI_LAPONIA(0x043b, "TT_MS_LANGID_SAAMI_LAPONIA"),
    SCOTTISH_GAELIC_UNITED_KINGDOM(0x083c, "TT_MS_LANGID_SCOTTISH_GAELIC_UNITED_KINGDOM"),
    IRISH_GAELIC_IRELAND(0x043c, "TT_MS_LANGID_IRISH_GAELIC_IRELAND"),
    YIDDISH_GERMANY(0x043d, "TT_MS_LANGID_YIDDISH_GERMANY"),
    MALAY_MALAYSIA(0x043e, "TT_MS_LANGID_MALAY_MALAYSIA"),
    MALAY_BRUNEI_DARUSSALAM(0x083e, "TT_MS_LANGID_MALAY_BRUNEI_DARUSSALAM"),
    KAZAK_KAZAKSTAN(0x043f, "TT_MS_LANGID_KAZAK_KAZAKSTAN"),
    KIRGHIZ_KIRGHIZSTAN(0x0440, "TT_MS_LANGID_KIRGHIZ_KIRGHIZSTAN"), /* Cyrillic*/
    /* alias declared in Windows 2000 */
    KIRGHIZ_KIRGHIZ_REPUBLIC(0x0440, "TT_MS_LANGID_KIRGHIZ_KIRGHIZ_REPUBLIC"),
    SWAHILI_KENYA(0x0441, "TT_MS_LANGID_SWAHILI_KENYA"),
    TURKMEN_TURKMENISTAN(0x0442, "TT_MS_LANGID_TURKMEN_TURKMENISTAN"),
    UZBEK_UZBEKISTAN_LATIN(0x0443, "TT_MS_LANGID_UZBEK_UZBEKISTAN_LATIN"),
    UZBEK_UZBEKISTAN_CYRILLIC(0x0843, "TT_MS_LANGID_UZBEK_UZBEKISTAN_CYRILLIC"),
    TATAR_TATARSTAN(0x0444, "TT_MS_LANGID_TATAR_TATARSTAN"),
    BENGALI_INDIA(0x0445, "TT_MS_LANGID_BENGALI_INDIA"),
    BENGALI_BANGLADESH(0x0845, "TT_MS_LANGID_BENGALI_BANGLADESH"),
    PUNJABI_INDIA(0x0446, "TT_MS_LANGID_PUNJABI_INDIA"),
    PUNJABI_ARABIC_PAKISTAN(0x0846, "TT_MS_LANGID_PUNJABI_ARABIC_PAKISTAN"),
    GUJARATI_INDIA(0x0447, "TT_MS_LANGID_GUJARATI_INDIA"),
    ORIYA_INDIA(0x0448, "TT_MS_LANGID_ORIYA_INDIA"),
    TAMIL_INDIA(0x0449, "TT_MS_LANGID_TAMIL_INDIA"),
    TELUGU_INDIA(0x044a, "TT_MS_LANGID_TELUGU_INDIA"),
    KANNADA_INDIA(0x044b, "TT_MS_LANGID_KANNADA_INDIA"),
    MALAYALAM_INDIA(0x044c, "TT_MS_LANGID_MALAYALAM_INDIA"),
    ASSAMESE_INDIA(0x044d, "TT_MS_LANGID_ASSAMESE_INDIA"),
    MARATHI_INDIA(0x044e, "TT_MS_LANGID_MARATHI_INDIA"),
    SANSKRIT_INDIA(0x044f, "TT_MS_LANGID_SANSKRIT_INDIA"),
    MONGOLIAN_MONGOLIA(0x0450, "TT_MS_LANGID_MONGOLIAN_MONGOLIA"),  /* Cyrillic */
    MONGOLIAN_MONGOLIA_MONGOLIAN(0x0850, "TT_MS_LANGID_MONGOLIAN_MONGOLIA_MONGOLIAN"),
    TIBETAN_CHINA(0x0451, "TT_MS_LANGID_TIBETAN_CHINA"),
    /* Don't use the next constant!  It has            */
      /*   (1) the wrong spelling (Dzonghka)             */
      /*   (2) Microsoft doesn't officially define it -- */
      /*       at least it is not in the List of Local   */
      /*       ID Values.                                */
      /*   (3) Dzongkha is not the same language as      */
      /*       Tibetan, so merging it is wrong anyway.   */
      /*                                                 */
      /* TT_MS_LANGID_TIBETAN_BHUTAN is correct, BTW.    */
    DZONGHKA_BHUTAN(0x0851, "TT_MS_LANGID_DZONGHKA_BHUTAN"),
    /* So we will continue to #define it, but with the correct value */
    TIBETAN_BHUTAN(0x0851, "TT_MS_LANGID_TIBETAN_BHUTAN"),
    WELSH_WALES(0x0452, "TT_MS_LANGID_WELSH_WALES"),
    KHMER_CAMBODIA(0x0453, "TT_MS_LANGID_KHMER_CAMBODIA"),
    LAO_LAOS(0x0454, "TT_MS_LANGID_LAO_LAOS"),
    BURMESE_MYANMAR(0x0455, "TT_MS_LANGID_BURMESE_MYANMAR"),
    GALICIAN_SPAIN(0x0456, "TT_MS_LANGID_GALICIAN_SPAIN"),
    KONKANI_INDIA(0x0457, "TT_MS_LANGID_KONKANI_INDIA"),
    MANIPURI_INDIA(0x0458, "TT_MS_LANGID_MANIPURI_INDIA"),    /* Bengali */
    SINDHI_INDIA(0x0459, "TT_MS_LANGID_SINDHI_INDIA"),    /* Arabic */
    SINDHI_PAKISTAN(0x0859, "TT_MS_LANGID_SINDHI_PAKISTAN"),
    /* Missing a LCID for Sindhi in Devanagari script */
    SYRIAC_SYRIA(0x045a, "TT_MS_LANGID_SYRIAC_SYRIA"),
    SINHALESE_SRI_LANKA(0x045b, "TT_MS_LANGID_SINHALESE_SRI_LANKA"),
    CHEROKEE_UNITED_STATES(0x045c, "TT_MS_LANGID_CHEROKEE_UNITED_STATES"),
    INUKTITUT_CANADA(0x045d, "TT_MS_LANGID_INUKTITUT_CANADA"),
    AMHARIC_ETHIOPIA(0x045e, "TT_MS_LANGID_AMHARIC_ETHIOPIA"),
    TAMAZIGHT_MOROCCO(0x045f, "TT_MS_LANGID_TAMAZIGHT_MOROCCO"),    /* Arabic */
    TAMAZIGHT_MOROCCO_LATIN(0x085f, "TT_MS_LANGID_TAMAZIGHT_MOROCCO_LATIN"),
    /* Missing a LCID for Tifinagh script */
    KASHMIRI_PAKISTAN(0x0460, "TT_MS_LANGID_KASHMIRI_PAKISTAN"),    /* Arabic */
    /* Spelled this way by XPsp2 Platform SDK (2004-07-26) */
      /* script is yet unclear... might be Arabic, Nagari or Sharada */
    KASHMIRI_SASIA(0x0860, "TT_MS_LANGID_KASHMIRI_SASIA"),
    /* ... and aliased (by MS) for compatibility reasons. */
    KASHMIRI_INDIA(0x0860, "TT_MS_LANGID_KASHMIRI_INDIA"),
    NEPALI_NEPAL(0x0461, "TT_MS_LANGID_NEPALI_NEPAL"),
    NEPALI_INDIA(0x0861, "TT_MS_LANGID_NEPALI_INDIA"),
    FRISIAN_NETHERLANDS(0x0462, "TT_MS_LANGID_FRISIAN_NETHERLANDS"),
    PASHTO_AFGHANISTAN(0x0463, "TT_MS_LANGID_PASHTO_AFGHANISTAN"),
    FILIPINO_PHILIPPINES(0x0464, "TT_MS_LANGID_FILIPINO_PHILIPPINES"),
    DHIVEHI_MALDIVES(0x0465, "TT_MS_LANGID_DHIVEHI_MALDIVES"),
    /* alias declared in Windows 2000 */
    DIVEHI_MALDIVES(0x0465, "TT_MS_LANGID_DIVEHI_MALDIVES"),
    EDO_NIGERIA(0x0466, "TT_MS_LANGID_EDO_NIGERIA"),
    FULFULDE_NIGERIA(0x0467, "TT_MS_LANGID_FULFULDE_NIGERIA"),
    HAUSA_NIGERIA(0x0468, "TT_MS_LANGID_HAUSA_NIGERIA"),
    IBIBIO_NIGERIA(0x0469, "TT_MS_LANGID_IBIBIO_NIGERIA"),
    YORUBA_NIGERIA(0x046a, "TT_MS_LANGID_YORUBA_NIGERIA"),
    QUECHUA_BOLIVIA(0x046b, "TT_MS_LANGID_QUECHUA_BOLIVIA"),
    QUECHUA_ECUADOR(0x086b, "TT_MS_LANGID_QUECHUA_ECUADOR"),
    QUECHUA_PERU(0x0c6b, "TT_MS_LANGID_QUECHUA_PERU"),
    SEPEDI_SOUTH_AFRICA(0x046c, "TT_MS_LANGID_SEPEDI_SOUTH_AFRICA"),
    /* Also spelled by XPsp2 Platform SDK (2004-07-26) */
    SOTHO_SOUTHERN_SOUTH_AFRICA(0x046c, "TT_MS_LANGID_SOTHO_SOUTHERN_SOUTH_AFRICA"),
    /* language codes 0x046d, 0x046e and 0x046f are (still) unknown. */
    IGBO_NIGERIA(0x0470, "TT_MS_LANGID_IGBO_NIGERIA"),
    KANURI_NIGERIA(0x0471, "TT_MS_LANGID_KANURI_NIGERIA"),
    OROMO_ETHIOPIA(0x0472, "TT_MS_LANGID_OROMO_ETHIOPIA"),
    TIGRIGNA_ETHIOPIA(0x0473, "TT_MS_LANGID_TIGRIGNA_ETHIOPIA"),
    TIGRIGNA_ERYTHREA(0x0873, "TT_MS_LANGID_TIGRIGNA_ERYTHREA"),
    /* also spelled in the `Passport SDK' list as: */
    TIGRIGNA_ERYTREA(0x0873, "TT_MS_LANGID_TIGRIGNA_ERYTREA"),
    GUARANI_PARAGUAY(0x0474, "TT_MS_LANGID_GUARANI_PARAGUAY"),
    HAWAIIAN_UNITED_STATES(0x0475, "TT_MS_LANGID_HAWAIIAN_UNITED_STATES"),
    LATIN(0x0476, "TT_MS_LANGID_LATIN"),
    SOMALI_SOMALIA(0x0477, "TT_MS_LANGID_SOMALI_SOMALIA"),
    /* Note: Yi does not have a (proper) ISO 639-2 code, since it is mostly */
      /*       not written (but OTOH the peculiar writing system is worth     */
      /*       studying).                                                     */
    YI_CHINA(0x0478, "TT_MS_LANGID_YI_CHINA"),
    PAPIAMENTU_NETHERLANDS_ANTILLES(0x0479, "TT_MS_LANGID_PAPIAMENTU_NETHERLANDS_ANTILLES"),
    /* language codes from 0x047a to 0x047f are (still) unknown. */
    UIGHUR_CHINA(0x0480, "TT_MS_LANGID_UIGHUR_CHINA"),
    MAORI_NEW_ZEALAND(0x0481, "TT_MS_LANGID_MAORI_NEW_ZEALAND");
    private int val;
    private String str;
    private static SparseArray<MsLangId> tagToMsLangIdMapping;
    public static MsLangId getTableTag(int i) {
      if (tagToMsLangIdMapping == null) {
        initMapping();
      }
      return tagToMsLangIdMapping.get(i);
    }
    private static void initMapping() {
      tagToMsLangIdMapping = new SparseArray<MsLangId>();
      for (MsLangId t : values()) {
        tagToMsLangIdMapping.put(t.val, t);
      }
    }
    private MsLangId(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum CMap {
    UNSORTED(1, "TT_CMAP_FLAG_UNSORTED"),
    OVERLAPPING(2, "TT_CMAP_FLAG_OVERLAPPING");
    private int val;
    private String str;
    private static SparseArray<CMap> tagToCMapMapping;
    public static CMap getTableTag(int i) {
      if (tagToCMapMapping == null) {
        initMapping();
      }
      return tagToCMapMapping.get(i);
    }
    private static void initMapping() {
      tagToCMapMapping = new SparseArray<CMap>();
      for (CMap t : values()) {
        tagToCMapMapping.put(t.val, t);
      }
    }
    private CMap(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum CMapFormat {
    CMapUnknown(-1, "TT_CMAP_UNKNOWN_FORMAT"),
    CMap0(0, "TT_CMAP0_FORMAT"),
    CMap2(2, "TT_CMAP2_FORMAT"),
    CMap4(4, "TT_CMAP4_FORMAT"),
    CMap6(6, "TT_CMAP6_FORMAT"),
    CMap8(8, "TT_CMAP8_FORMAT"),
    CMap10(10, "TT_CMAP10_FORMAT"),
    CMap12(12, "TT_CMAP12_FORMAT"),
    CMap13(13, "TT_CMAP13_FORMAT"),
    CMap14(14, "TT_CMAP14_FORMAT");
    private int val;
    private String str;
    private static SparseArray<CMapFormat> tagToCMapFormatMapping;
    public static CMapFormat getTableTag(int i) {
      if (tagToCMapFormatMapping == null) {
        initMapping();
      }
      return tagToCMapFormatMapping.get(i);
    }
    private static void initMapping() {
      tagToCMapFormatMapping = new SparseArray<CMapFormat>();
      for (CMapFormat t : values()) {
        tagToCMapFormatMapping.put(t.val, t);
      }
    }
    private CMapFormat(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum GlyphFlags {
    Unknown(0x0, "TT_CMAP_UNKNOWN_FORMAT"),
    ON_CURVE(0x1, "TT_GLYPH_FLAGS_ON_CURVE"),
    X_SHORT(0x2, "TT_GLYPH_FLAGS_X_SHORT"),
    Y_SHORT(0x4, "TT_GLYPH_FLAGS_Y_SHORT"),
    REPEAT(0x8, "TT_GLYPH_FLAGS_REPEAT"),
    X_SAME(0x10, "TT_GLYPH_FLAGS_X_SAME"),
    Y_SAME(0x20, "TT_GLYPH_FLAGS_Y_SAME"),
    RESERVED1(0x40, "TT_GLYPH_FLAGS_RESERVED1"),
    RESERVED2(0x80, "TT_GLYPH_FLAGS_RESERVED2");
    private int val;
    private String str;
    private static SparseArray<GlyphFlags> tagToGlyphFlagsMapping;
    public static GlyphFlags getTableTag(int i) {
      if (tagToGlyphFlagsMapping == null) {
        initMapping();
      }
      return tagToGlyphFlagsMapping.get(i);
    }
    private static void initMapping() {
      tagToGlyphFlagsMapping = new SparseArray<GlyphFlags>();
      for (GlyphFlags t : values()) {
        tagToGlyphFlagsMapping.put(t.val, t);
      }
    }
    public static String getFlags(int value) {
      String str = "";
      String sep = "";
      if (tagToGlyphFlagsMapping == null) {
        initMapping();
      }
      for (GlyphFlags t : values()) {
        if ((value & t.getVal()) != 0) {
          str = str+sep+tagToGlyphFlagsMapping.get(t.val, t);
          sep = ", ";
        }
      }
      return str;
    }
    private GlyphFlags(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

}
