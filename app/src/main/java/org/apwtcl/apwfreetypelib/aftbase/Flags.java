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

import java.util.HashSet;
import java.util.Set;

public class Flags {

  public enum Module {
    FONT_DRIVER(1, "this module is a font driver"),
    RENDERER(2, "this module is a renderer"),
    HINTER(4, "this module is a glyph hinter"),
    DRIVER_STYLER(8, "this module is a styler"),
    DRIVER_SCALABLE(0x100, " the driver supports scalable fonts"),
    DRIVER_NO_OUTLINES(0x200, "the driver does not support vector outlines"),
    DRIVER_HAS_HINTER(0x400, "the driver provides its own hinter");

    private int val;
    private String str;
    private static SparseArray<Module> tagToModuleMapping;
    public static Module getTableTag(int i) {
      if (tagToModuleMapping == null) {
        initMapping();
      }
      return tagToModuleMapping.get(i);
    }
    private static void initMapping() {
      tagToModuleMapping = new SparseArray<Module>();
      for (Module t : values()) {
        tagToModuleMapping.put(t.val, t);
      }
    }
    private Module(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
    public String getDescription() {
      return str;
    }

    public static boolean isFontDriver(int flags) {
      return (flags &  Flags.Module.FONT_DRIVER.getVal()) != 0;
    }
    public static boolean isRenderer(int flags) {
      return (flags &  Flags.Module.RENDERER.getVal()) != 0;
    }
    public static boolean isHinter(int flags) {
      return (flags &  Flags.Module.HINTER.getVal()) != 0;
    }
    public static boolean isDriverStyler(int flags) {
      return (flags &  Flags.Module.DRIVER_STYLER.getVal()) != 0;
    }
    public static boolean isDriverScalable(int flags) {
      return (flags &  Flags.Module.DRIVER_SCALABLE.getVal()) != 0;
    }
    public static boolean isDriverNoOutlines(int flags) {
      return (flags &  Flags.Module.DRIVER_NO_OUTLINES.getVal()) != 0;
    }
    public static boolean driverHasHinter(int flags) {
      return (flags &  Flags.Module.DRIVER_HAS_HINTER.getVal()) != 0;
    }

  }

  public enum Face {
    UNKNOWN(0, "Flags Face UNKNOWN"),
    SCALABLE((1 <<  0), "FT_FACE_FLAG_SCALABLE"),
    FIXED_SIZES((1 <<  1), "FT_FACE_FLAG_FIXED_SIZES"),
    FIXED_WIDTH((1 <<  2), "FT_FACE_FLAG_FIXED_WIDTH"),
    SFNT((1 <<  3), "FT_FACE_FLAG_SFNT"),
    HORIZONTAL((1 <<  4), "FT_FACE_FLAG_HORIZONTAL"),
    VERTICAL((1 <<  5), "FT_FACE_FLAG_VERTICAL"),
    KERNING((1 <<  6), "FT_FACE_FLAG_KERNING"),
    FAST_GLYPHS((1 <<  7), "FT_FACE_FLAG_FAST_GLYPHS"),
    MULTIPLE_MASTERS((1 <<  8), "FT_FACE_FLAG_MULTIPLE_MASTERS"),
    GLYPH_NAMES((1 <<  9), "FT_FACE_FLAG_GLYPH_NAMES"),
    EXTERNAL_STREAM((1 << 10), "FT_FACE_FLAG_EXTERNAL_STREAM"),
    HINTER((1 << 11), "FT_FACE_FLAG_HINTE"),
    CID_KEYED((1 << 12), "FT_FACE_FLAG_CID_KEYE"),
    TRICKY((1 << 13), "FT_FACE_FLAG_TRICK");
    private int val;
    private String str;
    private static SparseArray<Face> tagToFaceMapping;
    public static Face getTableTag(int i) {
      if (tagToFaceMapping == null) {
        initMapping();
      }
      return tagToFaceMapping.get(i);
    }
    private static void initMapping() {
      tagToFaceMapping = new SparseArray<Face>();
      for (Face t : values()) {
        tagToFaceMapping.put(t.val, t);
      }
    }
    private Face(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* =========================================================================
     *
     * <Const>
     *    FT_STYLE_FLAG_XXX
     *
     * <Description>
     *    A list of bit-flags used to indicate the style of a given face.
     *    These are used in the `style_flags' field of @FT_FaceRec.
     *
     * <Values>
     *    FT_STYLE_FLAG_ITALIC ::
     *      Indicates that a given face style is italic or oblique.
     *
     *    FT_STYLE_FLAG_BOLD ::
     *      Indicates that a given face is bold.
     *
     * <Note>
     *    The style information as provided by FreeType is very basic.  More
     *    details are beyond the scope and should be done on a higher level
     *    (for example, by analyzing various fields of the `OS/2' table in
     *    SFNT based fonts).
     *
     * =========================================================================
     */
  public enum FontStyle {
    NONE(0, "FT_STYLE_FLAG_NONE"),
    ITALIC((1 << 0), "FT_STYLE_FLAG_ITALIC"),
    BOLD((1 << 1), "FT_STYLE_FLAG_BOLD");
    private int val;
    private String str;
    private static SparseArray<FontStyle> tagToFontStyleMapping;
    public static FontStyle getTableTag(int i) {
      if (tagToFontStyleMapping == null) {
        initMapping();
      }
      return tagToFontStyleMapping.get(i);
    }
    private static void initMapping() {
      tagToFontStyleMapping = new SparseArray<FontStyle>();
      for (FontStyle t : values()) {
        tagToFontStyleMapping.put(t.val, t);
      }
    }
    private FontStyle(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public String toString() {
      String str = "";
      for (FontStyle t : values()) {
        if ((this.val & t.getVal()) != 0) {
          str += this.str + " ";
        }
      }
      return str;
    }
    public void add(FontStyle t) {
      this.val |= t.getVal();
    }
    public void remove(FontStyle t) {
      this.val &= ~t.getVal();
    }
    public int getVal() {
      return val;
    }
  }

  /* =====================================================================
   *   FT_LOAD_XXX
   *
   * @description:
   *   A list of bit-field constants used with @FT_Load_Glyph to indicate
   *   what kind of operations to perform during glyph loading.
   *
   * @values:
   *   FT_LOAD_DEFAULT ::
   *     Corresponding to~0, this value is used as the default glyph load
   *     operation.  In this case, the following happens:
   *
   *     1. FreeType looks for a bitmap for the glyph corresponding to the
   *        face's current size.  If one is found, the function returns.
   *        The bitmap data can be accessed from the glyph slot (see note
   *        below).
   *
   *     2. If no embedded bitmap is searched or found, FreeType looks for a
   *        scalable outline.  If one is found, it is loaded from the font
   *        file, scaled to device pixels, then `hinted' to the pixel grid
   *        in order to optimize it.  The outline data can be accessed from
   *        the glyph slot (see note below).
   *
   *     Note that by default, the glyph loader doesn't render outlines into
   *     bitmaps.  The following flags are used to modify this default
   *     behaviour to more specific and useful cases.
   *
   *   FT_LOAD_NO_SCALE ::
   *     Don't scale the loaded outline glyph but keep it in font units.
   *
   *     This flag implies @FT_LOAD_NO_HINTING and @FT_LOAD_NO_BITMAP, and
   *     unsets @FT_LOAD_RENDER.
   *
   *     If the font is `tricky' (see @FT_FACE_FLAG_TRICKY for more), using
   *     FT_LOAD_NO_SCALE usually yields meaningless outlines because the
   *     subglyphs must be scaled and positioned with hinting instructions.
   *     This can be solved by loading the font without FT_LOAD_NO_SCALE and
   *     setting the character size to `font->units_per_EM'.
   *
   *   FT_LOAD_NO_HINTING ::
   *     Disable hinting.  This generally generates `blurrier' bitmap glyphs
   *     when the glyph are rendered in any of the anti-aliased modes.  See
   *     also the note below.
   *
   *     This flag is implied by @FT_LOAD_NO_SCALE.
   *
   *   FT_LOAD_RENDER ::
   *     Call @FT_Render_Glyph after the glyph is loaded.  By default, the
   *     glyph is rendered in @FT_RENDER_MODE_NORMAL mode.  This can be
   *     overridden by @FT_LOAD_TARGET_XXX or @FT_LOAD_MONOCHROME.
   *
   *     This flag is unset by @FT_LOAD_NO_SCALE.
   *
   *   FT_LOAD_NO_BITMAP ::
   *     Ignore bitmap strikes when loading.  Bitmap-only fonts ignore this
   *     flag.
   *
   *     @FT_LOAD_NO_SCALE always sets this flag.
   *
   *   FT_LOAD_VERTICAL_LAYOUT ::
   *     Load the glyph for vertical text layout.  In particular, the
   *     `advance' value in the @FT_GlyphSlotRec structure is set to the
   *     `vertAdvance' value of the `metrics' field.
   *
   *     In case @FT_HAS_VERTICAL doesn't return true, you shouldn't use
   *     this flag currently.  Reason is that in this case vertical metrics
   *     get synthesized, and those values are not always consistent across
   *     various font formats.
   *
   *   FT_LOAD_FORCE_AUTOHINT ::
   *     Indicates that the auto-hinter is preferred over the font's native
   *     hinter.  See also the note below.
   *
   *   FT_LOAD_CROP_BITMAP ::
   *     Indicates that the font driver should crop the loaded bitmap glyph
   *     (i.e., remove all space around its black bits).  Not all drivers
   *     implement this.
   *
   *   FT_LOAD_PEDANTIC ::
   *     Indicates that the font driver should perform pedantic verifications
   *     during glyph loading.  This is mostly used to detect broken glyphs
   *     in fonts.  By default, FreeType tries to handle broken fonts also.
   *
   *     In particular, errors from the TrueType bytecode engine are not
   *     passed to the application if this flag is not set; this might
   *     result in partially hinted or distorted glyphs in case a glyph's
   *     bytecode is buggy.
   *
   *   FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH ::
   *     Ignored.  Deprecated.
   *
   *   FT_LOAD_NO_RECURSE ::
   *     This flag is only used internally.  It merely indicates that the
   *     font driver should not load composite glyphs recursively.  Instead,
   *     it should set the `num_subglyph' and `subglyphs' values of the
   *     glyph slot accordingly, and set `glyph->format' to
   *     @FT_GLYPH_FORMAT_COMPOSITE.
   *
   *     The description of sub-glyphs is not available to client
   *     applications for now.
   *
   *     This flag implies @FT_LOAD_NO_SCALE and @FT_LOAD_IGNORE_TRANSFORM.
   *
   *   FT_LOAD_IGNORE_TRANSFORM ::
   *     Indicates that the transform matrix set by @FT_Set_Transform should
   *     be ignored.
   *
   *   FT_LOAD_MONOCHROME ::
   *     This flag is used with @FT_LOAD_RENDER to indicate that you want to
   *     render an outline glyph to a 1-bit monochrome bitmap glyph, with
   *     8~pixels packed into each byte of the bitmap data.
   *
   *     Note that this has no effect on the hinting algorithm used.  You
   *     should rather use @FT_LOAD_TARGET_MONO so that the
   *     monochrome-optimized hinting algorithm is used.
   *
   *   FT_LOAD_LINEAR_DESIGN ::
   *     Indicates that the `linearHoriAdvance' and `linearVertAdvance'
   *     fields of @FT_GlyphSlotRec should be kept in font units.  See
   *     @FT_GlyphSlotRec for details.
   *
   *   FT_LOAD_NO_AUTOHINT ::
   *     Disable auto-hinter.  See also the note below.
   *
   *   FT_LOAD_COLOR ::
   *     This flag is used to request loading of color embedded-bitmap
   *     images.  The resulting color bitmaps, if available, will have the
   *     @FT_PIXEL_MODE_BGRA format.  When the flag is not used and color
   *     bitmaps are found, they will be converted to 256-level gray
   *     bitmaps transparently.  Those bitmaps will be in the
   *     @FT_PIXEL_MODE_GRAY format.
   *
   * @note:
   *   By default, hinting is enabled and the font's native hinter (see
   *   @FT_FACE_FLAG_HINTER) is preferred over the auto-hinter.  You can
   *   disable hinting by setting @FT_LOAD_NO_HINTING or change the
   *   precedence by setting @FT_LOAD_FORCE_AUTOHINT.  You can also set
   *   @FT_LOAD_NO_AUTOHINT in case you don't want the auto-hinter to be
   *   used at all.
   *
   *   See the description of @FT_FACE_FLAG_TRICKY for a special exception
   *   (affecting only a handful of Asian fonts).
   *
   *   Besides deciding which hinter to use, you can also decide which
   *   hinting algorithm to use.  See @FT_LOAD_TARGET_XXX for details.
   *
   *   Note that the auto-hinter needs a valid Unicode cmap (either a native
   *   one or synthesized by FreeType) for producing correct results.  If a
   *   font provides an incorrect mapping (for example, assigning the
   *   character code U+005A, LATIN CAPITAL LETTER Z, to a glyph depicting a
   *   mathematical integral sign), the auto-hinter might produce useless
   *   results.
   *
   * Bits 16..19 are used by `FT_LOAD_TARGET_'
   * =====================================================================
   */

  public enum Load {
    DEFAULT(0x0, "FT_LOAD_DEFAULT"),
    NO_SCALE((1 << 0 ), "FT_LOAD_NO_SCALE"),
    NO_HINTING((1 << 1), "FT_LOAD_NO_HINTING"),
    RENDER((1 << 2), "FT_LOAD_RENDER"),
    NO_BITMAP((1 << 3), "FT_LOAD_NO_BITMAP"),
    VERTICAL_LAYOUT((1 << 4), "FT_LOAD_VERTICAL_LAYOUT"),
    FORCE_AUTOHINT((1 << 5), "FT_LOAD_FORCE_AUTOHINT"),
    CROP_BITMAP((1 << 6), "FT_LOAD_CROP_BITMAP"),
    PEDANTIC((1 << 7), "FT_LOAD_PEDANTIC"),
    ADVANCE_ONLY((1 << 8), "FT_LOAD_ADVANCE_ONLY"),
    IGNORE_GLOBAL_ADVANCE_WIDTH((1 << 9), "FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH"),
    NO_RECURSE((1 << 10), "FT_LOAD_NO_RECURSE"),
    IGNORE_TRANSFORM((1 << 11), "FT_LOAD_IGNORE_TRANSFORM"),
    MONOCHROME((1 << 12), "FT_LOAD_MONOCHROME"),
    LINEAR_DESIGN((1 << 13), "FT_LOAD_LINEAR_DESIGN"),
    SBITS_ONLY((1 << 14), "FT_LOAD_SBITS_ONLY"),
    NO_AUTOHINT((1 << 15), "FT_LOAD_NO_AUTOHINT"),
    COLOR((1 << 20), "FT_LOAD_COLOR");
    private int val;
    private String str;
    private static SparseArray<Load> tagToLoadMapping;
    public static Load getTableTag(int i) {
      if (tagToLoadMapping == null) {
        initMapping();
      }
      return tagToLoadMapping.get(i);
    }
    private static void initMapping() {
      tagToLoadMapping = new SparseArray<Load>();
      for (Load t : values()) {
        tagToLoadMapping.put(t.val, t);
      }
    }
    private Load(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public static Set<Load> makeTableTagSet(int value) {
      Set<Load> flags = new HashSet<>();
      for (Load t : values()) {
        if ((value & t.getVal()) != 0) {
          flags.add(t);
        }
      }
      return flags;
    }
    public static String CurveSetToString(Set<Curve> flags) {
      StringBuffer str = new StringBuffer();
      for (Load t : values()) {
        if (flags.contains(t)) {
          str.append(" "+t.toString());
        }
      }
      return str.toString();
    }
    public static int CurveSetToInt(Set<Curve> flags) {
      int val = 0;
      for (Load t : values()) {
        if (flags.contains(t)) {
          val += t.getVal();
        }
      }
      return val;
    }
    public int getVal() {
      return val;
    }
  }

  public enum LoadType {
    ARGS_ARE_WORDS(0x0001, "ARGS_ARE_WORDS"),
    ARGS_ARE_XY_VALUES(0x0002, "ARGS_ARE_XY_VALUES"),
    ROUND_XY_TO_GRID(0x0004, "ROUND_XY_TO_GRID"),
    WE_HAVE_A_SCALE(0x0008, "WE_HAVE_A_SCALE"),
    /* reserved      = 0x0010 */
    MORE_COMPONENTS(0x0020, "MORE_COMPONENTS"),
    WE_HAVE_AN_XY_SCALE(0x0040, "WE_HAVE_AN_XY_SCALE"),
    WE_HAVE_A_2X2(0x0080, "WE_HAVE_A_2X2"),
    WE_HAVE_INSTR(0x0100, "WE_HAVE_INSTR"),
    USE_MY_METRICS(0x0200, "USE_MY_METRICS"),
    OVERLAP_COMPOUND(0x0400, "OVERLAP_COMPOUND"),
    SCALED_COMPONENT_OFFSET(0x0800, "SCALED_COMPONENT_OFFSET"),
    UNSCALED_COMPONENT_OFFSET(0x1000, "UNSCALED_COMPONENT_OFFSET");
    private int val;
    private String str;
    private static SparseArray<LoadType> tagToLoadTypeMapping;
    public static LoadType getTableTag(int i) {
      if (tagToLoadTypeMapping == null) {
        initMapping();
      }
      return tagToLoadTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToLoadTypeMapping = new SparseArray<LoadType>();
      for (LoadType t : values()) {
        tagToLoadTypeMapping.put(t.val, t);
      }
    }
    private LoadType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  /* ===================================================================== */
  /*    FT_OUTLINE_FLAGS                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A list of bit-field constants use for the flags in an outline's    */
  /*    `flags' field.                                                     */
  /*                                                                       */
  /* <Values>                                                              */
  /*    FT_OUTLINE_NONE ::                                                 */
  /*      Value~0 is reserved.                                             */
  /*                                                                       */
  /*    FT_OUTLINE_OWNER ::                                                */
  /*      If set, this flag indicates that the outline's field arrays      */
  /*      (i.e., `points', `flags', and `contours') are `owned' by the     */
  /*      outline object, and should thus be freed when it is destroyed.   */
  /*                                                                       */
  /*    FT_OUTLINE_EVEN_ODD_FILL ::                                        */
  /*      By default, outlines are filled using the non-zero winding rule. */
  /*      If set to 1, the outline will be filled using the even-odd fill  */
  /*      rule (only works with the smooth rasterizer).                    */
  /*                                                                       */
  /*    FT_OUTLINE_REVERSE_FILL ::                                         */
  /*      By default, outside contours of an outline are oriented in       */
  /*      clock-wise direction, as defined in the TrueType specification.  */
  /*      This flag is set if the outline uses the opposite direction      */
  /*      (typically for Type~1 fonts).  This flag is ignored by the scan  */
  /*      converter.                                                       */
  /*                                                                       */
  /*    FT_OUTLINE_IGNORE_DROPOUTS ::                                      */
  /*      By default, the scan converter will try to detect drop-outs in   */
  /*      an outline and correct the glyph bitmap to ensure consistent     */
  /*      shape continuity.  If set, this flag hints the scan-line         */
  /*      converter to ignore such cases.  See below for more information. */
  /*                                                                       */
  /*    FT_OUTLINE_SMART_DROPOUTS ::                                       */
  /*      Select smart dropout control.  If unset, use simple dropout      */
  /*      control.  Ignored if @FT_OUTLINE_IGNORE_DROPOUTS is set.  See    */
  /*      below for more information.                                      */
  /*                                                                       */
  /*    FT_OUTLINE_INCLUDE_STUBS ::                                        */
  /*      If set, turn pixels on for `stubs', otherwise exclude them.      */
  /*      Ignored if @FT_OUTLINE_IGNORE_DROPOUTS is set.  See below for    */
  /*      more information.                                                */
  /*                                                                       */
  /*    FT_OUTLINE_HIGH_PRECISION ::                                       */
  /*      This flag indicates that the scan-line converter should try to   */
  /*      convert this outline to bitmaps with the highest possible        */
  /*      quality.  It is typically set for small character sizes.  Note   */
  /*      that this is only a hint that might be completely ignored by a   */
  /*      given scan-converter.                                            */
  /*                                                                       */
  /*    FT_OUTLINE_SINGLE_PASS ::                                          */
  /*      This flag is set to force a given scan-converter to only use a   */
  /*      single pass over the outline to render a bitmap glyph image.     */
  /*      Normally, it is set for very large character sizes.  It is only  */
  /*      a hint that might be completely ignored by a given               */
  /*      scan-converter.                                                  */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The flags @FT_OUTLINE_IGNORE_DROPOUTS, @FT_OUTLINE_SMART_DROPOUTS, */
  /*    and @FT_OUTLINE_INCLUDE_STUBS are ignored by the smooth            */
  /*    rasterizer.                                                        */
  /*                                                                       */
  /*    There exists a second mechanism to pass the drop-out mode to the   */
  /*    B/W rasterizer; see the `tags' field in @FT_Outline.               */
  /*                                                                       */
  /*    Please refer to the description of the `SCANTYPE' instruction in   */
  /*    the OpenType specification (in file `ttinst1.doc') how simple      */
  /*    drop-outs, smart drop-outs, and stubs are defined.                 */
  /*                                                                       */
  /* ===================================================================== */

  public enum Outline {
    NONE(0x0, "FT_OUTLINE_NONE"),
    OWNER(0x1, "FT_OUTLINE_OWNER"),
    EVEN_ODD_FILL(0x2, "FT_OUTLINE_EVEN_ODD_FILL"),
    REVERSE_FILL(0x4, "FT_OUTLINE_REVERSE_FILL"),
    IGNORE_DROPOUTS(0x8, "FT_OUTLINE_IGNORE_DROPOUTS"),
    SMART_DROPOUTS(0x10, "FT_OUTLINE_SMART_DROPOUTS"),
    INCLUDE_STUBS(0x20, "FT_OUTLINE_INCLUDE_STUBS"),
    HIGH_PRECISION(0x100, "FT_OUTLINE_HIGH_PRECISION"),
    SINGLE_PASS(0x200, "FT_OUTLINE_SINGLE_PASS"),
    CONTOURS_MAX(0xFFFF, "FT_OUTLINE_CONTOURS_MAX"),
    POINTS_MAX(0xFFFF, "FT_OUTLINE_POINTS_MAX");
    private int val;
    private String str;
    private static SparseArray<Outline> tagToOutlineMapping;
    public static Outline getTableTag(int i) {
      if (tagToOutlineMapping == null) {
        initMapping();
      }
      return tagToOutlineMapping.get(i);
    }
    private static void initMapping() {
      tagToOutlineMapping = new SparseArray<Outline>();
      for (Outline t : values()) {
        tagToOutlineMapping.put(t.val, t);
      }
    }
    private Outline(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum Curve {
    CONIC(0, "FT_CURVE_TAG_CONIC"),
    ON(1, "FT_CURVE_TAG_ON"),
    CUBIC(2, "FT_CURVE_TAG_CUBIC"),
    HAS_SCANMODE(4, "FT_CURVE_TAG_HAS_SCANMODE"),
    TOUCH_X(8, "FT_CURVE_TAG_TOUCH_X"), /* reserved for the TrueType hinter */
    TOUCH_Y(16, "FT_CURVE_TAG_TOUCH_Y"); /* reserved for the TrueType hinter */
//    TOUCH_BOTH((Curve.TOUCH_X.getVal() | Curve.TOUCH_Y.getVal()), "FT_CURVE_TAG_TOUCH_BOTH");
    private int val;
    private String str;
    private static SparseArray<Curve> tagToCurveMapping;

    public static Curve getTableTag(int i) {
      if (tagToCurveMapping == null) {
        initMapping();
      }
      return tagToCurveMapping.get(i);
    }
    public static Set<Curve> makeTableTagSet(int value) {
      Set<Curve> flags = new HashSet<>();
      for (Curve t : values()) {
        if ((value & t.getVal()) != 0) {
          flags.add(t);
        }
      }
      return flags;
    }
    private static void initMapping() {
      tagToCurveMapping = new SparseArray<Curve>();
      for (Curve t : values()) {
        tagToCurveMapping.put(t.val, t);
      }
    }
    private Curve(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public static String CurveSetToString(Set<Curve> flags) {
      StringBuffer str = new StringBuffer();
      for (Curve t : values()) {
        if (flags.contains(t)) {
          str.append(" "+t.toString());
        }
      }
      return str.toString();
    }
    public static int CurveSetToInt(Set<Curve> flags) {
      int val = 0;
      for (Curve t : values()) {
        if (flags.contains(t)) {
          val += t.getVal();
        }
      }
      return val;
    }
    public int getVal() {
      return val;
    }
  }

  public enum SubGlyph {
    UNKNOWN(0, "FT_SUBGLYPH_FLAG_UNKNOWN"),
    ARGS_ARE_WORDS(1, "FT_SUBGLYPH_FLAG_ARGS_ARE_WORDS"),
    ARGS_ARE_XY_VALUES(2, "FT_SUBGLYPH_FLAG_ARGS_ARE_XY_VALUES"),
    ROUND_XY_TO_GRID(4, "FT_SUBGLYPH_FLAG_ROUND_XY_TO_GRID"),
    SCALE(8, "FT_SUBGLYPH_FLAG_SCALE"),
    FLAG_XY_SCALE(0x40, "FT_SUBGLYPH_FLAG_XY_SCALE"),
    FLAG_2X2(0x80, "FT_SUBGLYPH_FLAG_2X2"),
    USE_MY_METRICS(0x200, "FT_SUBGLYPH_FLAG_USE_MY_METRICS");
    private int val;
    private String str;
    private static SparseArray<SubGlyph> tagToSubGlyphMapping;
    public static SubGlyph getTableTag(int i) {
      if (tagToSubGlyphMapping == null) {
        initMapping();
      }
      return tagToSubGlyphMapping.get(i);
    }
    private static void initMapping() {
      tagToSubGlyphMapping = new SparseArray<SubGlyph>();
      for (SubGlyph t : values()) {
        tagToSubGlyphMapping.put(t.val, t);
      }
    }
    private SubGlyph(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

}