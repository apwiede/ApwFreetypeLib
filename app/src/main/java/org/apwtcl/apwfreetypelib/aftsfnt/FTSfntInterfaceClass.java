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

package org.apwtcl.apwfreetypelib.aftsfnt;

  /* ===================================================================== */
  /*    FTSfntInterfaceClass                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure holds pointers to the functions used to load and    */
  /*    free the basic tables that are required in a `sfnt' font file.     */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    Check the various xxx_Func() descriptions for details.             */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.*;
import org.apwtcl.apwfreetypelib.afttruetype.TTCMapRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class FTSfntInterfaceClass extends FTModuleInterface {
    private static int oid = 0;

    private int id;
    private final static String TAG = "FTSfntInterfaceClass";

    /* ==================== FTSfntInterfaceClass =========================== */
    public FTSfntInterfaceClass() {
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
   * sfnt_init_face
   * =====================================================================
   */
  public FTError.ErrorTag sfnt_init_face(FTStreamRec stream, TTFaceRec face, int face_index,
                                         int num_params, FTParameter[] params) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTLibraryRec library = face.getDriver().library;
    FTSfntInterfaceClass sfnt;

    if (num_params != 0) ; // unused
    if (params == null) ;  // unused
    Debug(0, DebugTag.DBG_INIT, TAG, "sfnt_init_face called: "+face_index+"!"+face.getSfnt()+"!");
    sfnt = (FTSfntInterfaceClass)face.getSfnt();
    if (sfnt == null) {
      sfnt = (FTSfntInterfaceClass) FTModuleRec.FTGetModuleInterface(library, "sfnt");
      if (sfnt == null) {
        FTTrace.Trace(7, TAG, "sfnt_init_face: cannot access `sfnt' module");
        return FTError.ErrorTag.INTERP_MISSING_MODULE;
      }
      face.setSfnt(sfnt);
//      FT_FACE_FIND_GLOBAL_SERVICE(face, face.psnames, POSTSCRIPT_CMAPS);
    }
    FTTrace.Trace(7, TAG, "SFNT driver" );
    error = sfnt_open_font(stream, face);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    FTTrace.Trace(7, TAG, "sfnt_init_face: "+face+"!"+face_index);
    if (face_index < 0) {
      face_index = 0;
    }
    if (face_index >= face.getTtc_header().GetCount()) {
      return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "offsets: "+face_index+"!"+face.getTtc_header().GetCount()+"!");
    error = face.getTtc_header().GotoFaceIndex(stream, face_index);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
      /* check that we have a valid TrueType file */
    error = loadFontDir(face, stream);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    face.setNum_faces(face.getTtc_header().GetCount());
    face.setFace_index(face_index);
    Debug(0, DebugTag.DBG_INIT, TAG, "sfnt_init_face done");
    return error;
  }

  /* =====================================================================
   * sfnt_load_face
   * =====================================================================
   */
  public static FTError.ErrorTag sfnt_load_face(FTStreamRec stream, TTFaceRec ttface, int face_index, int num_params, FTParameter[] params) {
    FTError.ErrorTag  error = FTError.ErrorTag.ERR_OK;
    boolean has_outline = false;
    boolean is_apple_sbit = false;
    boolean ignore_preferred_family  = false;
    boolean ignore_preferred_subfamily  = false;

    if (face_index == 0) ; // unused
    FTSfntInterfaceClass sfnt = (FTSfntInterfaceClass)ttface.getSfnt();
      /* Check parameters */
    {
      int i;

      for (i = 0; i < num_params; i++) {
        if (params[i].tag == FTParameter.ParamTag.FT_PARAM_TAG_IGNORE_PREFERRED_FAMILY) {
          ignore_preferred_family = true;
        } else {
          if (params[i].tag == FTParameter.ParamTag.FT_PARAM_TAG_IGNORE_PREFERRED_SUBFAMILY) {
            ignore_preferred_subfamily = true;
          }
        }
      }
    }
      /* Load tables */
      /* We now support two SFNT-based bitmapped font formats.  They */
      /* are recognized easily as they do not include a `glyf'       */
      /* table.                                                      */
      /*                                                             */
      /* The first format comes from Apple, and uses a table named   */
      /* `bhed' instead of `head' to store the font header (using    */
      /* the same format).  It also doesn't include horizontal and   */
      /* vertical metrics tables (i.e. `hhea' and `vhea' tables are  */
      /* missing).                                                   */
      /*                                                             */
      /* The other format comes from Microsoft, and is used with     */
      /* WinCE/PocketPC.  It looks like a standard TTF, except that  */
      /* it doesn't contain outlines.                                */
      /*                                                             */
    FTTrace.Trace(7, TAG, String.format("sfnt_load_face: %s", ttface));
      /* do we have outlines in there? */
    has_outline = ((ttface.lookupTable(TTTags.Table.glyf) != null) ||
        (ttface.lookupTable(TTTags.Table.CFF) != null));
    is_apple_sbit = false;
      /* if this font doesn't contain outlines, we try to load */
      /* a `bhed' table                                        */
    if (!has_outline) {
      FTTrace.Trace(7, TAG, "`bhed' -->");
      error = sfnt.loadBhed(ttface, stream);
      FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
          error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      is_apple_sbit = (error != FTError.ErrorTag.ERR_OK);
    }
      /* load the font header (`head' table) if this isn't an Apple */
      /* sbit font file                                             */
    if (!is_apple_sbit) {
      FTTrace.Trace(7, TAG, "`head' -->");
      error = sfnt.loadHead(ttface, stream);
      FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
          error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, "sfnt_load_face: done1");
        return error;
      }
    }
    if (ttface.getHeader().getUnitsPerEM() == 0) {
      error = FTError.ErrorTag.INTERP_INVALID_TABLE;
      FTTrace.Trace(7, TAG, "sfnt_load_face: done2");
      return error;
    }
      /* the following tables are often not present in embedded TrueType */
      /* fonts within PDF documents, so don't check for them.            */
    FTTrace.Trace(7, TAG, "`maxp' -->");
    error = sfnt.loadMaxp(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
    FTTrace.Trace(7, TAG, "`cmap' -->");
    error = sfnt.loadCmap(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      /* the following tables are optional in PCL fonts -- */
      /* don't check for errors                            */
    FTTrace.Trace(7, TAG, "`name' -->");
    error = sfnt.loadName(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));

    FTTrace.Trace(7, TAG, "`post' -->");
    error = sfnt.loadPost(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      /* do not load the metrics headers and tables if this is an Apple */
      /* sbit font file                                                 */
    if (!is_apple_sbit) {
        /* load the `hhea' and `hmtx' tables */
      FTTrace.Trace(7, TAG, "`hhea' -->");
      error = sfnt.loadHhea(ttface, stream);
      FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
          error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      if (error == FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, "`hmtx' -->");
        error = sfnt.loadHmtx(ttface, stream);
        FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
            error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
        if (error == FTError.ErrorTag.LOAD_TABLE_MISSING) {
          error = FTError.ErrorTag.LOAD_HMTX_TABLE_MISSING;
        }
      } else {
        if (error == FTError.ErrorTag.LOAD_TABLE_MISSING) {
          /* No `hhea' table necessary for SFNT Mac fonts. */
          if (ttface.getFormat_tag() == TTTags.Table.truE) {
            FTTrace.Trace(7, TAG, "This is an SFNT Mac font.");
            has_outline = false;
            error = FTError.ErrorTag.ERR_OK;
          } else {
            error = FTError.ErrorTag.LOAD_HORIZ_HEADER_MISSING;
          }
        }
      }
      if (error != FTError.ErrorTag.ERR_OK) {
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        FTTrace.Trace(7, TAG, "sfnt_load_face: done3");
      }
        /* try to load the `vhea' and `vmtx' tables */
      FTTrace.Trace(7, TAG, "`vhea' -->");
      error = sfnt.loadVhea(ttface, stream);
      FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
          error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      if (error == FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, "`vmtx' -->");
        error = sfnt.loadVmtx(ttface, stream);
        FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
            error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
        if (error == FTError.ErrorTag.ERR_OK) {
          ttface.setVertical_info(true);
        }
      }
      if (error != FTError.ErrorTag.ERR_OK && error != FTError.ErrorTag.LOAD_TABLE_MISSING) {
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        FTTrace.Trace(7, TAG, "sfnt_load_face: done4");
      }
      FTTrace.Trace(7, TAG, "`os2' -->");
      error = sfnt.loadOs2(ttface, stream);
      FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
          error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
      if (error != FTError.ErrorTag.ERR_OK) {
          /* we treat the table as missing if there are any errors */
        ttface.getOs2().setVersion(0xFFFF);
      }
    }
      /* the optional tables */
      /* embedded bitmap support */
    FTTrace.Trace(7, TAG, "`eblc' -->");
    error = sfnt.loadEblc(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
    if (error != FTError.ErrorTag.ERR_OK) {
          /* a font which contains neither bitmaps nor outlines is */
          /* still valid (although rather useless in most cases);  */
          /* however, you can find such stripped fonts in PDFs     */
      if (error == FTError.ErrorTag.LOAD_TABLE_MISSING) {
        error = FTError.ErrorTag.ERR_OK;
      } else {
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        FTTrace.Trace(7, TAG, "sfnt_load_face: done5");
      }
    }
    FTTrace.Trace(7, TAG, "`pclt' -->");
    error = sfnt.loadPclt(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
    if (error != FTError.ErrorTag.ERR_OK) {
      if (error != FTError.ErrorTag.LOAD_TABLE_MISSING) {
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        FTTrace.Trace(7, TAG, "sfnt_load_face: done6");
      }
      ttface.getPclt().setVersion(0);
    }
      /* consider the kerning and gasp tables as optional */
    FTTrace.Trace(7, TAG, "`gasp' -->");
    error = sfnt.loadGasp(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));

    FTTrace.Trace(7, TAG, "`kern' -->");
    error = sfnt.loadKern(ttface, stream);
    FTTrace.Trace(7, TAG, String.format("%s", (error == FTError.ErrorTag.ERR_OK) ? "loaded" :
        error == FTError.ErrorTag.LOAD_TABLE_MISSING ? "missing" : "failed to load"));
    ttface.setNum_glyphs(ttface.getMax_profile().getNumGlyphs());
      /* Bit 8 of the `fsSelection' field in the `OS/2' table denotes  */
      /* a WWS-only font face.  `WWS' stands for `weight', width', and */
      /* `slope', a term used by Microsoft's Windows Presentation      */
      /* Foundation (WPF).  This flag has been introduced in version   */
      /* 1.5 of the OpenType specification (May 2008).                 */
    ttface.setFamily_name(null);
    ttface.setStyle_name(null);
    FTReference<String> str_ref = new FTReference<String>();
    str_ref.Set(null);
    if (ttface.getOs2().getVersion() != 0xFFFF && (ttface.getOs2().getFsSelection() & 256) != 0) {
      if (!ignore_preferred_family) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.PREFERRED_FAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_PREFERRED_FAMILY");
          return error;
        }
        ttface.setFamily_name(str_ref.Get());
      }
      if (ttface.getFamily_name() != null) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.FONT_FAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_FONT_FAMILY");
          return error;
        }
        ttface.setFamily_name(str_ref.Get());
      }
      if (!ignore_preferred_subfamily) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.PREFERRED_SUBFAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_PREFERRED_SUB_FAMILY");
          return error;
        }
        ttface.setStyle_name(str_ref.Get());
      }
      if (ttface.getStyle_name() == null) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.FONT_SUBFAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_FONT_SUBFAMILY");
          return error;
        }
        ttface.setStyle_name(str_ref.Get());
      }
    } else {

      error = ttface.getName_table().getName(ttface, TTTags.NameId.WWS_FAMILY.getVal(), str_ref);
      if (error != FTError.ErrorTag.ERR_OK)  {
        FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_WWS_FAMILY");
        return error;
      }
      ttface.setFamily_name(str_ref.Get());
      if (ttface.getFamily_name() == null && !ignore_preferred_family) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.PREFERRED_FAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_PREFERRED_FAMILY");
          return error;
        }
        ttface.setFamily_name(str_ref.Get());
      }
      if (ttface.getFamily_name() == null) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.FONT_FAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_FONT_FAMILY");
          return error;
        }
        ttface.setFamily_name(str_ref.Get());
      }
      error = ttface.getName_table().getName(ttface, TTTags.NameId.WWS_SUBFAMILY.getVal(), str_ref);
      if (error != FTError.ErrorTag.ERR_OK)  {
        FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_WAS_SUBFAMILY");
        return error;
      }
      ttface.setStyle_name(str_ref.Get());
      if (ttface.getStyle_name() != null && !ignore_preferred_subfamily) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.PREFERRED_SUBFAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_PREFERRED_SUBFAMILY");
          return error;
        }
        ttface.setStyle_name(str_ref.Get());
      }
      if (ttface.getStyle_name() == null) {
        error = ttface.getName_table().getName(ttface, TTTags.NameId.FONT_SUBFAMILY.getVal(), str_ref);
        if (error != FTError.ErrorTag.ERR_OK)  {
          FTTrace.Trace(7, TAG, "sfnt_load_face: error TT_NAME_ID_FONT_SUBFAMILY");
          return error;
        }
        ttface.setStyle_name(str_ref.Get());
      }
    }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "family_name: "+ttface.getFamily_name()+" style_name: "+ttface.getStyle_name());
      /* now set up root fields */
    {
      FTFaceRec root = ttface;
      int flags = Flags.Face.getTableTag(root.getFace_flags()).getVal();

        /* =================================================================
         * Compute face flags.
         * =================================================================
         */
      if (has_outline == true) {
        flags = flags | Flags.Face.SCALABLE.getVal();   /* scalable outlines */
      }
        /* The sfnt driver only supports bitmap fonts natively, thus we */
        /* don't set FT_FACE_FLAG_HINTER.                               */
      flags = flags | Flags.Face.SFNT.getVal() | /* SFNT file format  */
          Flags.Face.HORIZONTAL.getVal(); /* horizontal data   */
        /* fixed width font? */
      if (ttface.getPostscript().getIsFixedPitch() != 0) {
        flags = flags | Flags.Face.FIXED_WIDTH.getVal();
      }
        /* vertical information? */
      if (ttface.isVertical_info()) {
        flags = flags | Flags.Face.VERTICAL.getVal();
      }
        /* kerning available ? */
      if ((ttface.getFace_flags() & Flags.Face.KERNING.getVal()) != 0) {
        flags = flags | Flags.Face.KERNING.getVal();
      }
      root.setFace_flags(flags);
        /* =================================================================
         * Compute style flags.
         * =================================================================
         */
      int style_flags = Flags.FontStyle.NONE.getVal();
      if (has_outline == true && ttface.getOs2().getVersion() != 0xFFFF) {
          /* We have an OS/2 table; use the `fsSelection' field.  Bit 9 */
          /* indicates an oblique font face.  This flag has been        */
          /* introduced in version 1.5 of the OpenType specification.   */
        if ((ttface.getOs2().getFsSelection() & 512) != 0) {      /* bit 9 */
          style_flags = flags | Flags.FontStyle.ITALIC.getVal();
        } else {
          if ((ttface.getOs2().getFsSelection() & 1) != 0) {   /* bit 0 */
            style_flags = flags | Flags.FontStyle.ITALIC.getVal();
          }
        }
        if ((ttface.getOs2().getFsSelection() & 32) != 0) {    /* bit 5 */
          style_flags = flags | Flags.FontStyle.BOLD.getVal();
        }
      } else {
          /* this is an old Mac font, use the header field */
        if ((ttface.getHeader().getMacStyle() & 1) != 0) {
          style_flags = flags | Flags.FontStyle.BOLD.getVal();
        }
        if ((ttface.getHeader().getMacStyle() & 2) != 0) {
          style_flags = flags | Flags.FontStyle.ITALIC.getVal();
        }
      }
      root.setStyle_flags(style_flags);
        /* =================================================================
         * Polish the charmaps.
         *
         *   Try to set the charmap encoding according to the platform &
         *   encoding ID of each charmap.
         *
         * =================================================================
         */
      TTCMapRec.buildCMaps(ttface);  /* ignore errors */
        /* set the encoding fields */
      {
        int  m;

        for (m = 0; m < root.getNum_charmaps(); m++) {
          FTCharMapRec charmap = root.charmaps[m];

          if (charmap == null) {
Log.e(TAG, "root.charmaps["+m+"] is null!!");
          } else {
            charmap.setEncoding(FTEncoding.sfnt_find_encoding(TTTags.Platform.getTableTag(charmap.getPlatformId()), charmap.getEncodingId()));
          }
        }
      }
        /* a font with no bitmaps and no outlines is scalable; */
        /* it has only empty glyphs then                       */
      if ((root.getFace_flags() & Flags.Face.FIXED_SIZES.getVal()) != 0 &&
          (root.getFace_flags() & Flags.Face.SCALABLE.getVal()) == 0) {
        root.setFace_flags(root.getFace_flags() | Flags.Face.SCALABLE.getVal());
      }
        /* =================================================================
         *  Set up metrics.
         * =================================================================
         */
      if ((root.getFace_flags() & Flags.Face.SCALABLE.getVal()) != 0) {
          /* XXX What about if outline header is missing */
          /*     (e.g. sfnt wrapped bitmap)?             */
        root.getBbox().setxMin(ttface.getHeader().getXMin());
        root.getBbox().setyMin(ttface.getHeader().getYMin());
        root.getBbox().setxMax(ttface.getHeader().getXMax());
        root.getBbox().setyMax(ttface.getHeader().getYMax());
        root.setUnits_per_EM(ttface.getHeader().getUnitsPerEM());
          /* XXX: Computing the ascender/descender/height is very different */
          /*      from what the specification tells you.  Apparently, we    */
          /*      must be careful because                                   */
          /*                                                                */
          /*      - not all fonts have an OS/2 table; in this case, we take */
          /*        the values in the horizontal header.  However, these    */
          /*        values very often are not reliable.                     */
          /*                                                                */
          /*      - otherwise, the correct typographic values are in the    */
          /*        sTypoAscender, sTypoDescender & sTypoLineGap fields.    */
          /*                                                                */
          /*        However, certain fonts have these fields set to 0.      */
          /*        Rather, they have usWinAscent & usWinDescent correctly  */
          /*        set (but with different values).                        */
          /*                                                                */
          /*      As an example, Arial Narrow is implemented through four   */
          /*      files ARIALN.TTF, ARIALNI.TTF, ARIALNB.TTF & ARIALNBI.TTF */
          /*                                                                */
          /*      Strangely, all fonts have the same values in their        */
          /*      sTypoXXX fields, except ARIALNB which sets them to 0.     */
          /*                                                                */
          /*      On the other hand, they all have different                */
          /*      usWinAscent/Descent values -- as a conclusion, the OS/2   */
          /*      table cannot be used to compute the text height reliably! */
          /*                                                                */
          /* The ascender and descender are taken from the `hhea' table. */
          /* If zero, they are taken from the `OS/2' table.              */
        root.setAscender(ttface.getHorizontal().getAscender());
        root.setDescender(ttface.getHorizontal().getDescender());
        root.setHeight((root.getAscender()) - root.getDescender() + ttface.getHorizontal().getLineGap());
        if (!(root.getAscender() != 0 || root.getDescender() != 0)) {
          if (ttface.getOs2().getVersion() != 0xFFFF) {
            if (ttface.getOs2().getSTypoAscender() != 0 || ttface.getOs2().getSTypoDescender() != 0) {
              root.setAscender(ttface.getOs2().getSTypoAscender());
              root.setDescender(ttface.getOs2().getSTypoDescender());
              root.setHeight((root.getAscender()) - root.getDescender() + ttface.getOs2().getSTypoLineGap());
            } else {
              root.setAscender(ttface.getOs2().getUsWinAscent());
              root.setDescender(-ttface.getOs2().getUsWinDescent());
              root.setHeight((root.getAscender() - root.getDescender()));
            }
          }
        }
        root.setMax_advance_width(ttface.getHorizontal().getAdvanceWidthMax());
        root.setMax_advance_height((ttface.isVertical_info())
            ? ttface.getVertical().getAdvanceHeightMax()
            : root.getHeight());
          /* See http://www.microsoft.com/OpenType/OTSpec/post.htm -- */
          /* Adjust underline position from top edge to centre of     */
          /* stroke to convert TrueType meaning to FreeType meaning.  */
        root.setUnderline_position((ttface.getPostscript().getUnderlinePosition() -
            ttface.getPostscript().getUnderlineThickness() / 2));
        root.setUnderline_thickness(ttface.getPostscript().getUnderlineThickness());
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "root(ttface): "+root.toDebugString());
      }
    }
    FTTrace.Trace(7, TAG, "sfnt_load_face: done7 OK");
    return error;
  }

  /* =====================================================================
   * sfnt_done_face
   * =====================================================================
   */
  public static int sfnt_done_face(Object ... arg1) {
    Log.w(TAG, "WARNING: sfnt_done_face not yet implemented");
    Debug(0, DebugTag.DBG_INIT, TAG, "WARNING! sfnt_done_face not yet implemented");
    return 1;
  }

  /* =====================================================================
   * sfnt_get_interface
   * =====================================================================
   */
  public static int sfnt_get_interface(Object ... arg) {
    Log.w(TAG, "WARNING: sfnt_get_interface not yet implemented");
    Debug(0, DebugTag.DBG_INIT, TAG, "WARNING! sfnt_get_interface not yet implemented");
    return 1;
  }

  /* =====================================================================
   * sfnt_open_font
   * =====================================================================
   */
  public static FTError.ErrorTag sfnt_open_font(FTStreamRec stream, TTFaceRec face) {
    return face.getTtc_header().Load(stream);
  }



  /* ==================== initFace ===================================== */
  public FTError.ErrorTag initFace(FTStreamRec stream, FTFaceRec face, int face_index, int num_params, FTParameter[] params) {
    return sfnt_init_face(stream, (TTFaceRec)face, face_index, num_params, params);
  }

  /* ==================== loadFace ===================================== */
  public FTError.ErrorTag loadFace(FTStreamRec stream, FTFaceRec face, int face_index, int num_params, FTParameter[] params) {
    return sfnt_load_face(stream, (TTFaceRec)face, face_index, num_params, params);
  }

  /* ==================== gotoTable ===================================== */
  public FTError.ErrorTag gotoTable() {
    Log.e(TAG, "gotoTable not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return tt_face_goto_table();
  }

  /* ==================== loadFontDir ===================================== */
  public FTError.ErrorTag loadFontDir(TTFaceRec face, FTStreamRec stream) {
    return face.getFont_directory().Load(face, stream);
  }

  /* ==================== loadBhed ===================================== */
  public FTError.ErrorTag loadBhed(TTFaceRec face, FTStreamRec stream) {
    return face.loadBhed(stream);
  }

  /* ==================== loadSbitImage ===================================== */
  public FTError.ErrorTag loadSbitImage() {
    Log.e(TAG, "loadSBitImage not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return tt_face_load_sbit_image();
  }

  /* ==================== loadHead ===================================== */
  public FTError.ErrorTag loadHead(TTFaceRec face, FTStreamRec stream) {
    return face.loadHead(stream);
  }

  /* ==================== loadMaxp ===================================== */
  public FTError.ErrorTag loadMaxp(TTFaceRec face, FTStreamRec stream) {
    return face.loadMaxp(stream);
  }

  /* ==================== loadCmap ===================================== */
  public FTError.ErrorTag loadCmap(TTFaceRec face, FTStreamRec stream) {
    return face.loadCmap(stream);
  }

  /* ==================== loadName ===================================== */
  public FTError.ErrorTag loadName(TTFaceRec face, FTStreamRec stream) {
    return face.loadName(stream);
  }

  /* ==================== loadPost ===================================== */
  public FTError.ErrorTag loadPost(TTFaceRec face, FTStreamRec stream) {
    return face.loadPost(stream);
  }

  /* ==================== loadOs2 ===================================== */
  public FTError.ErrorTag loadOs2(TTFaceRec face, FTStreamRec stream) {
    return face.loadOs2(stream);
  }

  /* ==================== loadPclt ===================================== */
  public FTError.ErrorTag loadPclt(TTFaceRec face, FTStreamRec stream) {
    return face.loadPclt(stream);
  }

  /* ==================== loadGasp ===================================== */
  public FTError.ErrorTag loadGasp(TTFaceRec face, FTStreamRec stream) {
    return face.loadGasp(stream);
  }

  /* ==================== loadKern ===================================== */
  public FTError.ErrorTag loadKern(TTFaceRec face, FTStreamRec stream) {
    return face.loadKern(stream);
  }

  /* ==================== getMetrics ===================================== */
  public FTError.ErrorTag getMetrics(FTFaceRec face, boolean flag, int idx, FTReference<Integer> bearing_ref, FTReference<Integer> advance_ref ) {
    return ((TTFaceRec)face).tt_face_get_metrics(flag, idx, bearing_ref, advance_ref);
  }

  /* ==================== loadHhea ===================================== */
  public FTError.ErrorTag loadHhea(TTFaceRec face, FTStreamRec stream) {
    return face.loadHhea(stream);
  }

  /* ==================== loadHmtx ===================================== */
  public FTError.ErrorTag loadHmtx(TTFaceRec face, FTStreamRec stream) {
    return face.loadHmtx(stream);
  }

  /* ==================== loadVhea ===================================== */
  public FTError.ErrorTag loadVhea(TTFaceRec face, FTStreamRec stream) {
    return face.loadVhea(stream);
  }

  /* ==================== loadVmtx ===================================== */
  public FTError.ErrorTag loadVmtx(TTFaceRec face, FTStreamRec stream) {
    return face.loadVmtx(stream);
  }

  /* ==================== loadEblc ===================================== */
  public FTError.ErrorTag loadEblc(TTFaceRec face, FTStreamRec stream) {
    Log.e(TAG, "loadEblc not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return tt_face_load_eblc();
  }


}