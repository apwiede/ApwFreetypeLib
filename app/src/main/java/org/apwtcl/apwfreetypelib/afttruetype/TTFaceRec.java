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
  /*    TTFaceRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The TrueType face class.  These objects model the resolution and   */
  /*    point-size independent data found in a TrueType font file.         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    root                 :: The base FT_Face structure, managed by the */
  /*                            base layer.                                */
  /*                                                                       */
  /*    ttc_header           :: The TrueType collection header, used when  */
  /*                            the file is a `ttc' rather than a `ttf'.   */
  /*                            For ordinary font files, the field         */
  /*                            `ttc_header.count' is set to 0.            */
  /*                                                                       */
  /*    format_tag           :: The font format tag.                       */
  /*                                                                       */
  /*    num_tables           :: The number of TrueType tables in this font */
  /*                            file.                                      */
  /*                                                                       */
  /*    dir_tables           :: The directory of TrueType tables for this  */
  /*                            font file.                                 */
  /*                                                                       */
  /*    header               :: The font's font header (`head' table).     */
  /*                            Read on font opening.                      */
  /*                                                                       */
  /*    horizontal           :: The font's horizontal header (`hhea'       */
  /*                            table).  This field also contains the      */
  /*                            associated horizontal metrics table        */
  /*                            (`hmtx').                                  */
  /*                                                                       */
  /*    max_profile          :: The font's maximum profile table.  Read on */
  /*                            font opening.  Note that some maximum      */
  /*                            values cannot be taken directly from this  */
  /*                            table.  We thus define additional fields   */
  /*                            below to hold the computed maxima.         */
  /*                                                                       */
  /*    vertical_info        :: A boolean which is set when the font file  */
  /*                            contains vertical metrics.  If not, the    */
  /*                            value of the `vertical' field is           */
  /*                            undefined.                                 */
  /*                                                                       */
  /*    vertical             :: The font's vertical header (`vhea' table). */
  /*                            This field also contains the associated    */
  /*                            vertical metrics table (`vmtx'), if found. */
  /*                            IMPORTANT: The contents of this field is   */
  /*                            undefined if the `verticalInfo' field is   */
  /*                            unset.                                     */
  /*                                                                       */
  /*    num_names            :: The number of name records within this     */
  /*                            TrueType font.                             */
  /*                                                                       */
  /*    name_table           :: The table of name records (`name').        */
  /*                                                                       */
  /*    os2                  :: The font's OS/2 table (`OS/2').            */
  /*                                                                       */
  /*    postscript           :: The font's PostScript table (`post'        */
  /*                            table).  The PostScript glyph names are    */
  /*                            not loaded by the driver on face opening.  */
  /*                            See the `ttpost' module for more details.  */
  /*                                                                       */
  /*    cmap_table           :: Address of the face's `cmap' SFNT table    */
  /*                            in memory (it's an extracted frame).       */
  /*                                                                       */
  /*    cmap_size            :: The size in bytes of the `cmap_table'      */
  /*                            described above.                           */
  /*                                                                       */
  /*    goto_table           :: A function called by each TrueType table   */
  /*                            loader to position a stream's cursor to    */
  /*                            the start of a given table according to    */
  /*                            its tag.  It defaults to TT_Goto_Face but  */
  /*                            can be different for strange formats (e.g. */
  /*                            Type 42).                                  */
  /*                                                                       */
  /*    access_glyph_frame   :: A function used to access the frame of a   */
  /*                            given glyph within the face's font file.   */
  /*                                                                       */
  /*    forget_glyph_frame   :: A function used to forget the frame of a   */
  /*                            given glyph when all data has been loaded. */
  /*                                                                       */
  /*    read_glyph_header    :: A function used to read a glyph header.    */
  /*                            It must be called between an `access' and  */
  /*                            `forget'.                                  */
  /*                                                                       */
  /*    read_simple_glyph    :: A function used to read a simple glyph.    */
  /*                            It must be called after the header was     */
  /*                            read, and before the `forget'.             */
  /*                                                                       */
  /*    read_composite_glyph :: A function used to read a composite glyph. */
  /*                            It must be called after the header was     */
  /*                            read, and before the `forget'.             */
  /*                                                                       */
  /*    sfnt                 :: A pointer to the SFNT service.             */
  /*                                                                       */
  /*    psnames              :: A pointer to the PostScript names service. */
  /*                                                                       */
  /*    hdmx                 :: The face's horizontal device metrics       */
  /*                            (`hdmx' table).  This table is optional in */
  /*                            TrueType/OpenType fonts.                   */
  /*                                                                       */
  /*    gasp                 :: The grid-fitting and scaling properties    */
  /*                            table (`gasp').  This table is optional in */
  /*                            TrueType/OpenType fonts.                   */
  /*                                                                       */
  /*    pclt                 :: The `pclt' SFNT table.                     */
  /*                                                                       */
  /*    num_sbit_strikes     :: The number of sbit strikes, i.e., bitmap   */
  /*                            sizes, embedded in this font.              */
  /*                                                                       */
  /*    sbit_strikes         :: An array of sbit strikes embedded in this  */
  /*                            font.  This table is optional in a         */
  /*                            TrueType/OpenType font.                    */
  /*                                                                       */
  /*    num_sbit_scales      :: The number of sbit scales for this font.   */
  /*                                                                       */
  /*    sbit_scales          :: Array of sbit scales embedded in this      */
  /*                            font.  This table is optional in a         */
  /*                            TrueType/OpenType font.                    */
  /*                                                                       */
  /*    postscript_names     :: A table used to store the Postscript names */
  /*                            of  the glyphs for this font.  See the     */
  /*                            file  `ttconfig.h' for comments on the     */
  /*                            TT_CONFIG_OPTION_POSTSCRIPT_NAMES option.  */
  /*                                                                       */
  /*    num_locations        :: The number of glyph locations in this      */
  /*                            TrueType file.  This should be             */
  /*                            identical to the number of glyphs.         */
  /*                            Ignored for Type 2 fonts.                  */
  /*                                                                       */
  /*    glyph_locations      :: An array of longs.  These are offsets to   */
  /*                            glyph data within the `glyf' table.        */
  /*                            Ignored for Type 2 font faces.             */
  /*                                                                       */
  /*    glyf_len             :: The length of the `glyf' table.  Needed    */
  /*                            for malformed `loca' tables.               */
  /*                                                                       */
  /*    font_program_size    :: Size in bytecodes of the face's font       */
  /*                            program.  0 if none defined.  Ignored for  */
  /*                            Type 2 fonts.                              */
  /*                                                                       */
  /*    font_program         :: The face's font program (bytecode stream)  */
  /*                            executed at load time, also used during    */
  /*                            glyph rendering.  Comes from the `fpgm'    */
  /*                            table.  Ignored for Type 2 font fonts.     */
  /*                                                                       */
  /*    cvt_program_size     :: The size in bytecodes of the face's cvt    */
  /*                            program.  Ignored for Type 2 fonts.        */
  /*                                                                       */
  /*    cvt_program          :: The face's cvt program (bytecode stream)   */
  /*                            executed each time an instance/size is     */
  /*                            changed/reset.  Comes from the `prep'      */
  /*                            table.  Ignored for Type 2 fonts.          */
  /*                                                                       */
  /*    cvt_size             :: Size of the control value table (in        */
  /*                            entries).   Ignored for Type 2 fonts.      */
  /*                                                                       */
  /*    cvt                  :: The face's original control value table.   */
  /*                            Coordinates are expressed in unscaled font */
  /*                            units.  Comes from the `cvt ' table.       */
  /*                            Ignored for Type 2 fonts.                  */
  /*                                                                       */
  /*    num_kern_pairs       :: The number of kerning pairs present in the */
  /*                            font file.  The engine only loads the      */
  /*                            first horizontal format 0 kern table it    */
  /*                            finds in the font file.  Ignored for       */
  /*                            Type 2 fonts.                              */
  /*                                                                       */
  /*    kern_table_index     :: The index of the kerning table in the font */
  /*                            kerning directory.  Ignored for Type 2     */
  /*                            fonts.                                     */
  /*                                                                       */
  /*    interpreter          :: A pointer to the TrueType bytecode         */
  /*                            interpreters field is also used to hook    */
  /*                            the debugger in `ttdebug'.                 */
  /*                                                                       */
  /*    unpatented_hinting   :: If true, use only unpatented methods in    */
  /*                            the bytecode interpreter.                  */
  /*                                                                       */
  /*    doblend              :: A boolean which is set if the font should  */
  /*                            be blended (this is for GX var).           */
  /*                                                                       */
  /*    blend                :: Contains the data needed to control GX     */
  /*                            variation tables (rather like Multiple     */
  /*                            Master data).                              */
  /*                                                                       */
  /*    extra                :: Reserved for third-party font drivers.     */
  /*                                                                       */
  /*    postscript_name      :: The PS name of the font.  Used by the      */
  /*                            postscript name service.                   */
  /*                                                                       */
  /* ===================================================================== */


import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTModuleInterface;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTRunInstructions;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTFaceRec extends FTFaceRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTFaceRec";

  private TTCHeaderRec ttc_header = null;
  private TTTags.Table format_tag = TTTags.Table.unkn;
  private int num_tables = 0;
  private TTTableRec[] dir_tables = null;
  private TTFontDirectoryRec font_directory = null;
  private TTHeaderRec header = null;           /* TrueType header table          */
  private TTHoriHeaderRec horizontal = null;   /* TrueType horizontal header     */
  private TTMaxProfileRec max_profile = null;
  private boolean vertical_info = false;
  private TTVertHeaderRec vertical = null;     /* TT Vertical header, if present */
  private int num_names = 0;              /* number of name records  */
  private TTNameTableRec name_table = null; /* name table              */
  private TTOs2Rec os2 = null;                 /* TrueType OS/2 table            */
  private TTPostscriptRec postscript = null;   /* TrueType Postscript table      */
  private TTCmapRec cmap_table = null;   /* TrueType cmap table */
  /* a typeless pointer to the SFNT_Interface table used to load */
  /* the basic TrueType tables in the face object                */
  private FTModuleInterface sfnt = null;
  /* a typeless pointer to the FT_Service_PsCMapsRec table used to */
  /* handle glyph names <-> unicode & Mac values                   */
  private Object[] psnames = null;
  /* ===================================================================== */
  /*                                                                       */
  /* Optional TrueType/OpenType tables                                     */
  /*                                                                       */
  /* ===================================================================== */
  /* grid-fitting and scaling table */
  private TTGaspRec gasp = null;                 /* the `gasp' table */
  /* PCL 5 table */
  private TTPcltRec pclt = null;
  /* embedded bitmaps support */
  private int num_sbit_scales = 0;
  /* postscript names table */
  /* ===================================================================== */
  /*                                                                       */
  /* TrueType-specific fields (ignored by the OTF-Type2 driver)            */
  /*                                                                       */
  /* ===================================================================== */
  /* the font program, if any */
  private TTFpgmRec fpgm_table = null;
  /* the cvt program, if any */
  private TTCvtRec cvt_table = null;
  /* the original, unscaled, control value table */
  /* ===================================================================== */
  /*                                                                     */
  /* Other tables or fields. This is used by derivative formats like     */
  /* OpenType.                                                           */
  /*                                                                     */
  /* ===================================================================== */
  private String postscript_name = null;
//  public int glyf_len = 0;  -> loca table
  /* since version 2.2 */
  private TTHoriMetricsHeaderRec hori_metrics_header = null;
  private TTVertMetricsHeaderRec vert_metrics_header = null;
//  public int num_locations = 0; /* in broken TTF, gid > 0xFFFF */ -> loca table
  private byte[] glyph_locations = null;
  private TTHdmxRec hdmx_table = null;
  private byte[] sbit_table = null;
  private int sbit_table_size = 0;
  private int sbit_num_strikes = 0;
  private TTKernRec kern_table = null;
  private TTLocaRec loca_table = null;
  private TTGlyfRec glyf_table = null;
  private TTPrepRec prep_table = null;
  /* since 2.3.0 */

  /* ==================== TTFaceRec ================================== */
  public TTFaceRec() {
    oid++;
    id = oid; 
    ttc_header = new TTCHeaderRec();
    header = new TTHeaderRec();
    font_directory = new TTFontDirectoryRec();
Debug(0, DebugTag.DBG_INIT, TAG, "TTFaceRec constructor: "+mySelf()+"!");
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
    str.append("..format_tag: "+format_tag+'\n');
    str.append("..num_tables: "+num_tables+'\n');
    str.append("..vertical_info: "+vertical_info+'\n');
    str.append("..num_names: "+num_names+'\n');
    str.append("..num_sbit_scales: "+num_sbit_scales+'\n');
    str.append("..sbit_table_size: "+sbit_table_size+'\n');
    str.append("..sbit_num_strikes: "+sbit_num_strikes+'\n');
    return super_str+str.toString();
  }

  /*
   * =====================================================================
   * lookupTable
   *
   * <Description> Looks for a TrueType table by name.
   * <Input> tag :: The searched tag.
   * <Return> A pointer to the table directory entry. 0 if not found.
   * =====================================================================
   */
  public TTTableRec lookupTable(TTTags.Table tag) {
    TTTableRec entry;
    int limit;
    boolean zero_length = false;

    FTTrace.Trace(7, TAG, "lookupTable: " + tag);
    limit = num_tables;
    for (int i = 0; i < limit; i++) {
      entry = dir_tables[i];
        /* For compatibility with Windows, we consider */
        /* zero-length tables the same as missing tables. */
      if (entry.getTag() == tag) {
        if (entry.getLength() != 0) {
          FTTrace.Trace(7, TAG, "found table.");
          return entry;
        }
        zero_length = true;
      }
    }
    if (zero_length) {
      FTTrace.Trace(7, TAG, "ignoring empty table");
    } else {
      FTTrace.Trace(7, TAG, "could not find table");
    }
    return null;
  }

  /*
   * =====================================================================
   * gotoTable
   *
   * <Description> Looks for a TrueType table by name, then seek a stream to
   * it.
   *
   * <Input>  tag :: The searched tag.
   * stream :: The stream to seek when the table is found.
   * <Output> length :: The length of the table if found, undefined otherwise.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag gotoTable(TTTags.Table tag, FTStreamRec stream, FTReference<Integer> length_ref) {
    Integer length = 0;
    TTTableRec table;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    table = lookupTable(tag);
    if (table != null) {
      if (length_ref != null) {
        length = table.getLength();
        length_ref.Set(length);
      }
      if (stream.seek(table.getOffset()) < 0) {
        length_ref.Set(null);
        // FIXME!!
        error = FTError.ErrorTag.INTERP_UNKNOWN_FILE_FORMAT;
        return error;
      }
    } else {
      length_ref.Set(null);
      error = FTError.ErrorTag.LOAD_TABLE_MISSING;
    }
    return error;
  }

  /*
   * =====================================================================
   * loadGenericHeader
   *
   * <Description> Loads the TrueType table `head' or `bhed'.
   * <Input> face :: A handle to the target face object.
   * stream :: The input stream.
   * <Return> FreeType error code. 0 means success.   *
   * =====================================================================
   */
  private FTError.ErrorTag loadGenericHeader(FTStreamRec stream, TTTags.Table tag) {
    header = new TTHeaderRec();
    return header.Load(stream, this, tag);
  }

  /*
   * =====================================================================
   * loadHead
   * =====================================================================
   */
  public FTError.ErrorTag loadHead(FTStreamRec stream) {
    return loadGenericHeader(stream, TTTags.Table.head);
  }

  /*
   * =====================================================================
   * loadBhed
   * =====================================================================
   */
  public FTError.ErrorTag loadBhed(FTStreamRec stream) {
    return loadGenericHeader(stream, TTTags.Table.bhed);
  }

  /*
   * =====================================================================
   * loadMaxp
   *
   * <Description> Loads the maximum profile into a face object.
   * <Input> stream :: The input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadMaxp(FTStreamRec stream) {
    max_profile = new TTMaxProfileRec();
    return max_profile.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadName
   *
   * <Description> Loads the name records.
   * <Input> stream :: The input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadName(FTStreamRec stream) {
    name_table = new TTNameTableRec();
    return name_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadCmap
   *
   * <Description> Loads the cmap directory in a face object. The cmaps
   * themselves are loaded on demand in the `ttcmap.c' module.
   *
   * <Input> stream :: A handle to the input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadCmap(FTStreamRec stream) {
    cmap_table = new TTCmapRec();
    return cmap_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadPost
   *
   * <Description> Loads the Postscript table.
   * <Input> face : stream :: A handle to the input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadPost(FTStreamRec stream) {
    postscript = new TTPostscriptRec();
    return postscript.Load(stream, this);
  }

  /* =====================================================================
   * loadHhea
   *
   * <Description>
   *    Load the `hhea' table into a face object.
   * <Input> stream   :: The input stream.
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadHhea(FTStreamRec stream) {
    horizontal = new TTHoriHeaderRec();
    return horizontal.Load(stream, this);
  }

  /* =====================================================================
   * loadVhea
   *
   * <Description>
   *    Load the 'vhea' table into a face object.
   * <Input> stream   :: The input stream.
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadVhea(FTStreamRec stream) {
    vertical = new TTVertHeaderRec();
    return vertical.Load(stream, this);
  }

  /* =====================================================================
   * loadHmtx
   *
   * <Description>
   *    Load the `hmtx' table into a face object.
   * <Input> stream   :: The input stream.
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadHmtx(FTStreamRec stream) {
    hori_metrics_header = new TTHoriMetricsHeaderRec();
    return hori_metrics_header.Load(stream, this);
  }

  /* =====================================================================
   * loadVmtx
   *
   * <Description>
   *    Load the `vmtx' table into a face object.
   * <Input> stream   :: The input stream.
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadVmtx(FTStreamRec stream) {
    hori_metrics_header = new TTHoriMetricsHeaderRec();
    return hori_metrics_header.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadOs2
   *
   * <Description> Loads the OS2 table.
   * <Input> face :: stream :: A handle to the input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadOs2(FTStreamRec stream) {
    os2 = new TTOs2Rec();
    return os2.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadPclt
   *
   * <Description> Loads the PCL 5 Table.
   * <Input> stream :: A handle to the input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadPclt(FTStreamRec stream) {
    pclt = new TTPcltRec();
    return pclt.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadGasp
   *
   * <Description> Loads the `gasp' table into a face object.
   * <Input>  stream :: The input stream.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag loadGasp(FTStreamRec stream) {
    gasp = new TTGaspRec();
    return gasp.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadKern
   * =====================================================================
   */
  public FTError.ErrorTag loadKern(FTStreamRec stream) {
    kern_table = new TTKernRec();
    return kern_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadLoca
   * =====================================================================
   */
  public FTError.ErrorTag loadLoca(FTStreamRec stream) {
    loca_table = new TTLocaRec();
    return loca_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadGlyf
   * =====================================================================
   */
  public FTError.ErrorTag loadGlyf(FTStreamRec stream) {
    glyf_table = new TTGlyfRec();
    return glyf_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadCvt
   * =====================================================================
   */
  public FTError.ErrorTag loadCvt(FTStreamRec stream) {
    cvt_table = new TTCvtRec();
    return cvt_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadFpgm
   * =====================================================================
   */
  public FTError.ErrorTag loadFpgm(FTStreamRec stream) {
    fpgm_table = new TTFpgmRec();
    return fpgm_table.Load(stream, this);
  }

  /*
   * =====================================================================
   * loadPrep
   * =====================================================================
   */
  public FTError.ErrorTag loadPrep(FTStreamRec stream) {
    prep_table = new TTPrepRec();
    return prep_table.Load(stream, this);
  }


  /* =====================================================================
   * tt_face_get_metrics
   *
   * <Description>
   *    Returns the horizontal or vertical metrics in font units for a
   *    given glyph.  The metrics are the left side bearing (resp. top
   *    side bearing) and advance width (resp. advance height).
   *
   * <Input>
   *    header  :: A pointer to either the horizontal or vertical metrics
   *               structure.
   *
   *    idx     :: The glyph index.
   *
   * <Output>
   *    bearing :: The bearing, either left side or top side.
   *
   *    advance :: The advance width resp. advance height.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_face_get_metrics(boolean isVertical, int gindex, FTReference<Integer> bearing_ref, FTReference<Integer> advance_ref) {
    long table_pos;
    long table_size;
    long table_end;
    long pos;
    int advance;
    int bearing;
    int k;

    if (isVertical) {
      table_pos = vert_metrics_header.getMetricsOffset();
      table_size = vert_metrics_header.getMetricsSize();
      k = vertical.getNumberOfVMetrics();
    } else {
      table_pos = hori_metrics_header.getMetricsOffset();
      table_size = hori_metrics_header.getMetricsSize();
      k = horizontal.getNumberOfHMetrics();
    }
    table_end = table_pos + table_size;
    if (k > 0) {
      if (gindex < (int)k) {
        table_pos += 4 * gindex;
        if (table_pos + 4 > table_end) {
          bearing_ref.Set(0);
          advance_ref.Set(0);
          return FTError.ErrorTag.ERR_OK;
        }
        pos = getStream().seek(table_pos);
        advance = stream.readShort();
        bearing = stream.readShort();
        if (pos < 0 /* ||
               FT_READ_USHORT(*aadvance) ||
               FT_READ_SHORT(*abearing) */ ) {
          bearing_ref.Set(0);
          advance_ref.Set(0);
          return FTError.ErrorTag.ERR_OK;
        }
        advance_ref.Set(advance);
        bearing_ref.Set(bearing);
      } else {
        table_pos += 4 * (k - 1);
        if (table_pos + 4 > table_end) {
          bearing_ref.Set(0);
          advance_ref.Set(0);
          return FTError.ErrorTag.ERR_OK;
        }
        pos = stream.seek(table_pos);
        advance = stream.readShort();
        if (pos < 0 /* ||
               FT_READ_USHORT(*aadvance) */ ) {
          bearing_ref.Set(0);
          advance_ref.Set(0);
          return FTError.ErrorTag.ERR_OK;
        }
        advance_ref.Set(advance);
        table_pos += 4 + 2 * (gindex - k);
        if (table_pos + 2 > table_end) {
          bearing_ref.Set(0);
        } else {
          pos = stream.seek(table_pos);
          bearing = stream.readShort();
          bearing_ref.Set(bearing);
        }
      }
    } else {
      bearing_ref.Set(0);
      advance_ref.Set(0);
    }
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * tt_access_glyph_frame
   * =====================================================================
   */
  public FTError.ErrorTag tt_access_glyph_frame(TTLoaderRec loader, int glyph_index, long offset, int len) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_access_glyph_frame");

    FTTrace.Trace(7, TAG, String.format("Glyph %d", glyph_index));
      /* the following line sets the `error' variable through macros! */
/*
    stream.seek(offset);
*/
//      if (( offset ) || FT_FRAME_ENTER( byte_count ) )
//        return error;
//      }
/*
    loader.cursor = stream.cursor();
    loader.limit = stream.limit;
*/
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * tt_load_composite_glyph
   * =====================================================================
   */
  public FTError.ErrorTag tt_load_composite_glyph(TTLoaderRec loader) {
    Log.e(TAG, "tt_load_composite_glyph not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
/*
    long pIdx = loader.cursor;
    long limit = loader.limit;
    FTGlyphLoaderRec gloader = loader.gloader;
    FTStreamRec stream = loader.stream;
    int num_subglyphs;

    num_subglyphs = 0;
    do {
      long xx;
      long xy;
      long yy;
      long yx;
      int count;

        /* check that we can load a new subglyph */
/*
      error = FTGlyphLoaderRec.FTGlyphLoaderCheckSubGlyphs(gloader, num_subglyphs + 1);
      if (error != 0) {
        return error;
      }
        /* check space */
/*
      if (pIdx + 4 > limit) {
        error = FT_Error.GLYPH_INVALID_COMPOSITE;
        return error;
      }
      gloader.current.subglyphs[num_subglyphs].arg1 = 0;
      gloader.current.subglyphs[num_subglyphs].arg2 = 0;
      gloader.current.subglyphs[num_subglyphs].flags = stream.readShort(stream);
      pIdx += 2;
      gloader.current.subglyphs[num_subglyphs].index = stream.readShort(stream);
      pIdx += 2;
        /* check space */
/*
      count = 2;
      if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.ARGS_ARE_WORDS) != 0) {
        count += 2;
      }
      if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_A_SCALE) != 0) {
        count += 2;
      } else {
        if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_AN_XY_SCALE) != 0) {
          count += 4;
        } else {
          if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_A_2X2) != 0) {
            count += 8;
          }
        }
      }
      if (pIdx + count > limit) {
        error = FT_Error.GLYPH_INVALID_COMPOSITE;
        return error;
      }
        /* read arguments */
/*
      if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.ARGS_ARE_WORDS) != 0) {
        gloader.current.subglyphs[num_subglyphs].arg1 = stream.readShort(stream);
        pIdx += 2;
        gloader.current.subglyphs[num_subglyphs].arg2 = stream.readShort(stream);
        pIdx += 2;
      } else {
        gloader.current.subglyphs[num_subglyphs].arg1 = (short)stream.readByte(stream);
        pIdx += 1;
        gloader.current.subglyphs[num_subglyphs].arg2 = (short)stream.readByte(stream);
        pIdx += 1;
      }
        /* read transform */
/*
      xx = 0x10000L;
      yy = 0x10000L;
      xy = 0;
      yx = 0;
      if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_A_SCALE) != 0) {
        xx = (long)stream.readShort(stream) << 2;
        pIdx += 2;
        yy = xx;
      } else {
        if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_AN_XY_SCALE) != 0) {
          xx = (long)stream.readShort(stream) << 2;
          pIdx += 2;
          yy = (long)stream.readShort(stream) << 2;
          pIdx += 2;
        } else {
          if ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.WE_HAVE_A_2X2) != 0) {
            xx = (long)stream.readShort(stream) << 2;
            pIdx += 2;
            yx = (long)stream.readShort(stream) << 2;
            pIdx += 2;
            xy = (long)stream.readShort(stream) << 2;
            pIdx += 2;
            yy = (long)stream.readShort(stream) << 2;
            pIdx += 2;
          }
        }
      }
      gloader.current.subglyphs[num_subglyphs].transform.xx = xx;
      gloader.current.subglyphs[num_subglyphs].transform.xy = xy;
      gloader.current.subglyphs[num_subglyphs].transform.yx = yx;
      gloader.current.subglyphs[num_subglyphs].transform.yy = yy;
      num_subglyphs++;
    } while ((gloader.current.subglyphs[num_subglyphs].flags & FTGlyphLoaderFlags.MORE_COMPONENTS) != 0);
    gloader.current.num_subglyphs = num_subglyphs;
      /* we must undo the FT_FRAME_ENTER in order to point */
      /* to the composite instructions, if we find some.   */
      /* We will process them later.                       */
      /*                                                   */
/*
    loader.ins_pos = (long)(stream.pos() + pIdx - limit);
    loader.cursor = pIdx;
*/
    return error;
  }

  /* =====================================================================
   * tt_forget_glyph_frame
   * =====================================================================
   */
  public FTError.ErrorTag tt_forget_glyph_frame(TTLoaderRec loader) {
      System.out.println("tt_forget_glyph_frame");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    // nothing to do
    return error;
  }



  /* =====================================================================
   * TTInitGlyphLoading
   * =====================================================================
   */
  public static void TTInitGlyphLoading(TTFaceRec face) {
    String class_name = "org.apwtcl.gles20.truetype.TTGlyphLoadingFuncs";
    Log.i(TAG, "TTInitGlyphLoading nothing to do");
  }

  /* ==================== accessGlyphFrame ===================================== */
  public FTError.ErrorTag accessGlyphFrame(TTLoaderRec loader, int glyph_index, long offset, int len) {
    return tt_access_glyph_frame(loader, glyph_index, offset, len);
  }

  /* ==================== forgetGlyphFrame ===================================== */
  public FTError.ErrorTag forgetGlyphFrame(TTLoaderRec loader)  {
    return tt_forget_glyph_frame(loader);
  }

  /* ==================== readGlyphHeader ===================================== */
  public FTError.ErrorTag readGlyphHeader(TTLoaderRec loader) {
    Log.e(TAG, "readGlyphHeader called illegal");
    return FTError.ErrorTag.ERR_OK;
/*
//    return tt_load_glyph_header(loader);
*/
  }

  /* ==================== readSimpleGlyph ===================================== */
  public FTError.ErrorTag readSimpleGlyph(TTLoaderRec loader) {
    return ((TTGlyphLoaderRec)(loader).getGloader()).tt_load_simple_glyph(loader);
  }

  /* ==================== readCompositeGlpyh ===================================== */
  public FTError.ErrorTag readCompositeGlyph(TTLoaderRec loader) {
    return tt_load_composite_glyph(loader);
  }

  /* ==================== Interpreter ===================================== */
  /* A pointer to the bytecode interpreter to use.  This is also */
    /* used to hook the debugger for the `ttdebug' utility.        */
  public FTError.ErrorTag Interpreter(TTExecContextRec exec) {
    return exec.RunInstructions();
  }

  /* ==================== getTtc_header ================================== */
  public TTCHeaderRec getTtc_header() {
    return ttc_header;
  }

  /* ==================== setTtc_header ================================== */
  public void setTtc_header(TTCHeaderRec ttc_header) {
    this.ttc_header = ttc_header;
  }

  /* ==================== getFormat_tag ================================== */
  public TTTags.Table getFormat_tag() {
    return format_tag;
  }

  /* ==================== setFormat_tag ================================== */
  public void setFormat_tag(TTTags.Table format_tag) {
    this.format_tag = format_tag;
  }

  /* ==================== getNum_tables ================================== */
  public int getNum_tables() {
    return num_tables;
  }

  /* ==================== setNum_tables ================================== */
  public void setNum_tables(int num_tables) {
    this.num_tables = num_tables;
  }

  /* ==================== getDir_tables ================================== */
  public TTTableRec[] getDir_tables() {
    return dir_tables;
  }

  /* ==================== setDir_tables ================================== */
  public void setDir_tables(TTTableRec[] dir_tables) {
    this.dir_tables = dir_tables;
  }

  /* ==================== getFont_directory ================================== */
  public TTFontDirectoryRec getFont_directory() {
    return font_directory;
  }

  /* ==================== setFont_directory ================================== */
  public void setFont_directory(TTFontDirectoryRec font_directory) {
    this.font_directory = font_directory;
  }

  /* ==================== getHeader ================================== */
  public TTHeaderRec getHeader() {
    return header;
  }

  /* ==================== setHeader ================================== */
  public void setHeader(TTHeaderRec header) {
    this.header = header;
  }

  /* ==================== getHorizontal ================================== */
  public TTHoriHeaderRec getHorizontal() {
    return horizontal;
  }

  /* ==================== setHorizontal ================================== */
  public void setHorizontal(TTHoriHeaderRec horizontal) {
    this.horizontal = horizontal;
  }

  /* ==================== getMax_profile ================================== */
  public TTMaxProfileRec getMax_profile() {
    return max_profile;
  }

  /* ==================== setMax_profile ================================== */
  public void setMax_profile(TTMaxProfileRec max_profile) {
    this.max_profile = max_profile;
  }

  /* ==================== isVertical_info ================================== */
  public boolean isVertical_info() {
    return vertical_info;
  }

  /* ==================== setVertical_info ================================== */
  public void setVertical_info(boolean vertical_info) {
    this.vertical_info = vertical_info;
  }

  /* ==================== getVertical ================================== */
  public TTVertHeaderRec getVertical() {
    return vertical;
  }

  /* ==================== setVertical ================================== */
  public void setVertical(TTVertHeaderRec vertical) {
    this.vertical = vertical;
  }

  /* ==================== getNum_names ================================== */
  public int getNum_names() {
    return num_names;
  }

  /* ==================== setNum_names ================================== */
  public void setNum_names(int num_names) {
    this.num_names = num_names;
  }

  /* ==================== getName_table ================================== */
  public TTNameTableRec getName_table() {
    return name_table;
  }

  /* ==================== setName_table ================================== */
  public void setName_table(TTNameTableRec name_table) {
    this.name_table = name_table;
  }

  /* ==================== getOs2 ================================== */
  public TTOs2Rec getOs2() {
    return os2;
  }

  /* ==================== setOs2 ================================== */
  public void setOs2(TTOs2Rec os2) {
    this.os2 = os2;
  }

  /* ==================== getPostscript() ================================== */
  public TTPostscriptRec getPostscript() {
    return postscript;
  }

  /* ==================== setPostscript() ================================== */
  public void setPostscript(TTPostscriptRec postscript) {
    this.postscript = postscript;
  }

  /* ==================== getCmap_table ================================== */
  public TTCmapRec getCmap_table() {
    return cmap_table;
  }

  /* ==================== setCmap_table ================================== */
  public void setCmap_table(TTCmapRec cmap_table) {
    this.cmap_table = cmap_table;
  }

  /* ==================== getSfnt ================================== */
  public FTModuleInterface getSfnt() {
    return sfnt;
  }

  /* ==================== setSfnt ================================== */
  public void setSfnt(FTModuleInterface sfnt) {
    this.sfnt = sfnt;
  }

  /* ==================== getPsnames ================================== */
  public Object[] getPsnames() {
    return psnames;
  }

  /* ==================== setPsnames ================================== */
  public void setPsnames(Object[] psnames) {
    this.psnames = psnames;
  }

  /* ==================== getGasp ================================== */
  public TTGaspRec getGasp() {
    return gasp;
  }

  /* ==================== setGasp ================================== */
  public void setGasp(TTGaspRec gasp) {
    this.gasp = gasp;
  }

  /* ==================== getPclt ================================== */
  public TTPcltRec getPclt() {
    return pclt;
  }

  /* ==================== setPclt ================================== */
  public void setPclt(TTPcltRec pclt) {
    this.pclt = pclt;
  }

  /* ==================== getNum_sbit_scales ================================== */
  public int getNum_sbit_scales() {
    return num_sbit_scales;
  }

  /* ==================== setNum_sbit_scales ================================== */
  public void setNum_sbit_scales(int num_sbit_scales) {
    this.num_sbit_scales = num_sbit_scales;
  }

  /* ==================== getFpgm_table ================================== */
  public TTFpgmRec getFpgm_table() {
    return fpgm_table;
  }

  /* ==================== setFpgm_table ================================== */
  public void setFpgm_table(TTFpgmRec fpgm_table) {
    this.fpgm_table = fpgm_table;
  }

  /* ==================== getCvt_table ================================== */
  public TTCvtRec getCvt_table() {
    return cvt_table;
  }

  /* ==================== setCvt_table ================================== */
  public void setCvt_table(TTCvtRec cvt_table) {
    this.cvt_table = cvt_table;
  }

  /* ==================== getPostscript_name ================================== */
  public String getPostscript_name() {
    return postscript_name;
  }

  /* ==================== setPostscript_name ================================== */
  public void setPostscript_name(String postscript_name) {
    this.postscript_name = postscript_name;
  }

  /* ==================== getHori_metrics_header ================================== */
  public TTHoriMetricsHeaderRec getHori_metrics_header() {
    return hori_metrics_header;
  }

  /* ==================== setHori_metrics_header ================================== */
  public void setHori_metrics_header(TTHoriMetricsHeaderRec hori_metrics_header) {
    this.hori_metrics_header = hori_metrics_header;
  }

  /* ==================== getVert_metrics_header ================================== */
  public TTVertMetricsHeaderRec getVert_metrics_header() {
    return vert_metrics_header;
  }

  /* ==================== setVert_metrics_header ================================== */
  public void setVert_metrics_header(TTVertMetricsHeaderRec vert_metrics_header) {
    this.vert_metrics_header = vert_metrics_header;
  }

  /* ==================== getGlyph_locations ================================== */
  public byte[] getGlyph_locations() {
    return glyph_locations;
  }

  /* ==================== setGlyph_locations ================================== */
  public void setGlyph_locations(byte[] glyph_locations) {
    this.glyph_locations = glyph_locations;
  }

  /* ==================== getHdmx_table ================================== */
  public TTHdmxRec getHdmx_table() {
    return hdmx_table;
  }

  /* ==================== setHdmx_table ================================== */
  public void setHdmx_table(TTHdmxRec hdmx_table) {
    this.hdmx_table = hdmx_table;
  }

  /* ==================== getSbit_table ================================== */
  public byte[] getSbit_table() {
    return sbit_table;
  }

  /* ==================== setSbit_table ================================== */
  public void setSbit_table(byte[] sbit_table) {
    this.sbit_table = sbit_table;
  }

  /* ==================== getSbit_table_size ================================== */
  public int getSbit_table_size() {
    return sbit_table_size;
  }

  /* ==================== setSbit_table_size ================================== */
  public void setSbit_table_size(int sbit_table_size) {
    this.sbit_table_size = sbit_table_size;
  }

  /* ==================== getSbit_num_strikes ================================== */
  public int getSbit_num_strikes() {
    return sbit_num_strikes;
  }

  /* ==================== setSbit_num_strikes ================================== */
  public void setSbit_num_strikes(int sbit_num_strikes) {
    this.sbit_num_strikes = sbit_num_strikes;
  }

  /* ==================== getKern_table ================================== */
  public TTKernRec getKern_table() {
    return kern_table;
  }

  /* ==================== setKern_table ================================== */
  public void setKern_table(TTKernRec kern_table) {
    this.kern_table = kern_table;
  }

  /* ==================== getLoca_table ================================== */
  public TTLocaRec getLoca_table() {
    return loca_table;
  }

  /* ==================== setLoca_table ================================== */
  public void setLoca_table(TTLocaRec loca_table) {
    this.loca_table = loca_table;
  }

  /* ==================== getPrep_table ================================== */
  public TTPrepRec getPrep_table() {
    return prep_table;
  }

  /* ==================== getPrep_table ================================== */
  public void setPrep_table(TTPrepRec prep_table) {
    this.prep_table = prep_table;
  }


  /* ==================== getGlyf_table ================================== */
  public TTGlyfRec getGlyf_table() {
    return glyf_table;
  }

  /* ==================== setGlyf_table ================================== */
  public void setGlyf_table(TTGlyfRec glyf_table) {
    this.glyf_table = glyf_table;
  }

}