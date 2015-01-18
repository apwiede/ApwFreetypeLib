package org.apwtcl.apwfreetypelib.aftbase;

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

public class FTTags {

  public enum ModuleTag {
    AUTOFIT(0, "autofit_module_class"),
    TT_DRIVER(1, "tt_driver_class"),
    T1_DRIVER(2, "t1_driver_class"),
    CFF_DRIVER(3, "cff_driver_class"),
    T1CID_DRIVER(4, "t1cid_driver_class"),
    PFR_DRIVER(5, "pfr_driver_class"),
    T42_DRIVER(6, "t42_driver_class"),
    WINFT_DRIVER(7, "winfnt_driver_class"),
    PCF_DRIVER(8, "pcf_driver_class"),
    BDF_DRIVER(9, "bdf_driver_class"),
    PSAUX_MODULE(10, "psaux_module_class"),
    PSNAMES_MODULE(11, "psnames_module_class"),
    PSHINTER_MODULE(12, "pshinter_module_class"),
    FT_RASTER1_RENDERER(13, "ft_raster1_renderer_class"),
    SFNT_MODULE(14, "sfnt_module_class"),
    FT_SMOOTH_RENDERER(15, "ft_smooth_renderer_class"),
    FT_SMOOTH_LDC_RENDERER(16, "ft_smooth_lcd_renderer_class"),
    FT_SMOOTH_LCDV_RENDERER(17, "ft_smooth_lcdv_renderer_class");
    private int val;
    private String str;
    private static SparseArray<ModuleTag> tagToModuleTagMapping;
    public static ModuleTag getTableTag(int i) {
      if (tagToModuleTagMapping == null) {
        initMapping();
      }
      return tagToModuleTagMapping.get(i);
    }
    private static void initMapping() {
      tagToModuleTagMapping = new SparseArray<ModuleTag>();
      for (ModuleTag t : values()) {
        tagToModuleTagMapping.put(t.val, t);
      }
    }
    private ModuleTag(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum ModuleType {
    UNKNOWN(TTUtil.TagToInt("unkn"), "FT_MODULE_TYPE_UNKNOWN"),
    FT_MODULE(TTUtil.TagToInt("modu"), "FT_MODULE_TYPE_FT_MODULE"),
    FT_RENDERER(TTUtil.TagToInt("rend"), "FT_MODULE_TYPE_FT_RENDERER"),
    TT_DRIVER(TTUtil.TagToInt("driv"), "FT_MODULE_TYPE_TT_DRIVER");

    private int val;
    private String str;
    private static SparseArray<ModuleType> tagToModuleTypeMapping;
    public static ModuleType getTableTag(int i) {
      if (tagToModuleTypeMapping == null) {
        initMapping();
      }
      return tagToModuleTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToModuleTypeMapping = new SparseArray<ModuleType>();
      for (ModuleType t : values()) {
        tagToModuleTypeMapping.put(t.val, t);
      }
    }
    private ModuleType(int val, String str) {
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

  public enum GlyphFormat {
    NONE(0, "none"),
    OWN_BITMAP(1, "FT_GLYPH_OWN_BITMAP"),
    COMPOSITE(TTUtil.TagToInt("comp"), "FT_GLYPH_FORMAT_COMPOSITE"),
    BITMAP(TTUtil.TagToInt("bits"), "FT_GLYPH_FORMAT_BITMAP"),
    OUTLINE(TTUtil.TagToInt("outl"), "FT_GLYPH_FORMAT_OUTLINE"),
    PLOTTER(TTUtil.TagToInt("plot"), "FT_GLYPH_FORMAT_PLOTTER");
    private int val;
    private String str;
    private static SparseArray<GlyphFormat> tagToGlyphFormatMapping;
    public static GlyphFormat getTableTag(int i) {
      if (tagToGlyphFormatMapping == null) {
        initMapping();
      }
      return tagToGlyphFormatMapping.get(i);
    }
    private static void initMapping() {
      tagToGlyphFormatMapping = new SparseArray<GlyphFormat>();
      for (GlyphFormat t : values()) {
        tagToGlyphFormatMapping.put(t.val, t);
      }
    }
    private GlyphFormat(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum RenderMode {
    NORMAL(0, "FT_RENDER_MODE_NORMAL"),
    LIGHT(1, "FT_RENDER_MODE_LIGHT"),
    MONO(2, "FT_RENDER_MODE_MONO"),
    LCD(3, "FT_RENDER_MODE_LCD"),
    LCD_V(4, "FT_RENDER_MODE_LCD_V"),
    MAX(5, "FT_RENDER_MODE_MAX");
    private int val;
    private String str;
    private static SparseArray<RenderMode> tagToRenderModeMapping;
    public static RenderMode getTableTag(int i) {
      if (tagToRenderModeMapping == null) {
        initMapping();
      }
      return tagToRenderModeMapping.get(i);
    }
    private static void initMapping() {
      tagToRenderModeMapping = new SparseArray<RenderMode>();
      for (RenderMode t : values()) {
        tagToRenderModeMapping.put(t.val, t);
      }
    }
    private RenderMode(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum DriverRec {
    UNKNOWN(0, "REC_TYPE_UNKNOWN"),
    FACE(10, "FACE_REC_TYPE_TT"),
    SIZE(20, "SIZE_REC_TYPE_TT"),
    GLYPH_SLOT(30, "SLOT_REC_TYPE_FT_GLYPH");
    private int val;
    private String str;
    private static SparseArray<DriverRec> tagToDriverRecMapping;
    public static DriverRec getTableTag(int i) {
      if (tagToDriverRecMapping == null) {
        initMapping();
      }
      return tagToDriverRecMapping.get(i);
    }
    private static void initMapping() {
      tagToDriverRecMapping = new SparseArray<DriverRec>();
      for (DriverRec t : values()) {
        tagToDriverRecMapping.put(t.val, t);
      }
    }
    private DriverRec(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }


  public enum PixelMode {
    NONE(0, "FT_PIXEL_MODE_NONE"),
    MONO(1, "FT_PIXEL_MODE_MONO"),
    GRAY(2, "FT_PIXEL_MODE_GRAY"),
    GRAY2(3, "FT_PIXEL_MODE_GRAY2"),
    GRAY4(4, "FT_PIXEL_MODE_GRAY4"),
    LCD(5, "FT_PIXEL_MODE_LCD"),
    LCD_V(6, "FT_PIXEL_MODE_LCD_V"),
    BGRA(7, "FT_PIXEL_MODE_BGRA"),
    MAX(8, "FT_PIXEL_MODE_MAX"); /* do not remove */
    private int val;
    private String str;
    private static SparseArray<PixelMode> tagToPixelModeMapping;
    public static PixelMode getTableTag(int i) {
      if (tagToPixelModeMapping == null) {
        initMapping();
      }
      return tagToPixelModeMapping.get(i);
    }
    private static void initMapping() {
      tagToPixelModeMapping = new SparseArray<PixelMode>();
      for (PixelMode t : values()) {
        tagToPixelModeMapping.put(t.val, t);
      }
    }
    private PixelMode(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum SizeRequestType {
    NOMINAL(0, "FT_SIZE_REQUEST_TYPE_NOMINAL"),
    REAL_DIM(1, "FT_SIZE_REQUEST_TYPE_REAL_DIM"),
    BBOX(2, "FT_SIZE_REQUEST_TYPE_BBOX"),
    CELL(3, "FT_SIZE_REQUEST_TYPE_CELL"),
    SCALES(4, "FT_SIZE_REQUEST_TYPE_SCALES"),
    MAX(5, "FT_SIZE_REQUEST_TYPE_MAX");
    private int val;
    private String str;
    private static SparseArray<SizeRequestType> tagToSizeRequestTypeMapping;
    public static SizeRequestType getTableTag(int i) {
      if (tagToSizeRequestTypeMapping == null) {
        initMapping();
      }
      return tagToSizeRequestTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToSizeRequestTypeMapping = new SparseArray<SizeRequestType>();
      for (SizeRequestType t : values()) {
        tagToSizeRequestTypeMapping.put(t.val, t);
      }
    }
    private SizeRequestType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* ===================================================================== */
  /* <Values> Encoding                                                              */
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
  /* ===================================================================== */
  public enum Encoding {
    NONE(0, "FT_ENCODING_NONE"),
    MS_SYMBOL(TTUtil.TagToInt("symb"), "FT_ENCODING_MS_SYMBOL"),
    UNICODE(TTUtil.TagToInt("unic"), "FT_ENCODING_UNICODE"),
    SJIS(TTUtil.TagToInt("sjis"), "FT_ENCODING_SJIS"),
    GB2312(TTUtil.TagToInt("gb  "), "FT_ENCODING_GB2312"),
    BIG5(TTUtil.TagToInt("big5"), "FT_ENCODING_BIG5"),
    WANSUNG(TTUtil.TagToInt("wans"), "FT_ENCODING_WANSUNG"),
    JOHAB(TTUtil.TagToInt("joha"), "FT_ENCODING_JOHAB"),
    ADOBE_STANDARD(TTUtil.TagToInt("ADOB"), "FT_ENCODING_ADOBE_STANDARD"),
    ADOBE_EXPERT(TTUtil.TagToInt("ADBE"), "FT_ENCODING_ADOBE_EXPERT"),
    ADOBE_CUSTOM(TTUtil.TagToInt("'ADBC"), "FT_ENCODING_ADOBE_CUSTOM"),
    ADOBE_LATIN_1(TTUtil.TagToInt("lat1"), "FT_ENCODING_ADOBE_LATIN_1"),
    OLD_LATIN_2(TTUtil.TagToInt("lat2"), "FT_ENCODING_OLD_LATIN_2"),
    APPLE_ROMAN(TTUtil.TagToInt("armn"), "FT_ENCODING_APPLE_ROMAN");
    private int val;
    private String str;
    private static SparseArray<Encoding> tagToEncodingMapping;
    public static Encoding getTableTag(int i) {
      if (tagToEncodingMapping == null) {
        initMapping();
      }
      return tagToEncodingMapping.get(i);
    }
    private static void initMapping() {
      tagToEncodingMapping = new SparseArray<Encoding>();
      for (Encoding t : values()) {
        tagToEncodingMapping.put(t.val, t);
      }
    }
    private Encoding(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum RasterType {
    Black(0, "FT_RASTER_TYPE_TBLACK"),
    GRAY(1, "FT_RASTER_TYPE_TGRAY");
    private int val;
    private String str;
    private static SparseArray<RasterType> tagToRasterTypeMapping;
    public static RasterType getTableTag(int i) {
      if (tagToRasterTypeMapping == null) {
        initMapping();
      }
      return tagToRasterTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToRasterTypeMapping = new SparseArray<RasterType>();
      for (RasterType t : values()) {
        tagToRasterTypeMapping.put(t.val, t);
      }
    }
    private RasterType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum Validate {
    DEFAULT(0, "FT_VALIDATE_DEFAULT"),
    TIGHT(1, "FT_VALIDATE_TIGHT"),
    PARANOID(2, "FT_VALIDATE_PARANOID");
    private int val;
    private String str;
    private static SparseArray<Validate> tagToValidateMapping;
    public static Validate getTableTag(int i) {
      if (tagToValidateMapping == null) {
        initMapping();
      }
      return tagToValidateMapping.get(i);
    }
    private static void initMapping() {
      tagToValidateMapping = new SparseArray<Validate>();
      for (Validate t : values()) {
        tagToValidateMapping.put(t.val, t);
      }
    }
    private Validate(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

}