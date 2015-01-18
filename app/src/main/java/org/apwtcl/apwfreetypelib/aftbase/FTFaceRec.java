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

import org.apwtcl.apwfreetypelib.afttruetype.TTDriverRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTFaceRec;
import org.apwtcl.apwfreetypelib.aftutil.*;

import java.io.*;

  /* ===================================================================== */
  /*    FTFaceRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    FreeType root face class structure.  A face object models a        */
  /*    typeface in a font file.                                           */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    num_faces           :: The number of faces in the font file.  Some */
  /*                           font formats can have multiple faces in     */
  /*                           a font file.                                */
  /*                                                                       */
  /*    face_index          :: The index of the face in the font file.  It */
  /*                           is set to~0 if there is only one face in    */
  /*                           the font file.                              */
  /*                                                                       */
  /*    face_flags          :: A set of bit flags that give important      */
  /*                           information about the face; see             */
  /*                           @FT_FACE_FLAG_XXX for the details.          */
  /*                                                                       */
  /*    style_flags         :: A set of bit flags indicating the style of  */
  /*                           the face; see @FT_STYLE_FLAG_XXX for the    */
  /*                           details.                                    */
  /*                                                                       */
  /*    num_glyphs          :: The number of glyphs in the face.  If the   */
  /*                           face is scalable and has sbits (see         */
  /*                           `num_fixed_sizes'), it is set to the number */
  /*                           of outline glyphs.                          */
  /*                                                                       */
  /*                           For CID-keyed fonts, this value gives the   */
  /*                           highest CID used in the font.               */
  /*                                                                       */
  /*    family_name         :: The face's family name.  This is an ASCII   */
  /*                           string, usually in English, which describes */
  /*                           the typeface's family (like `Times New      */
  /*                           Roman', `Bodoni', `Garamond', etc).  This   */
  /*                           is a least common denominator used to list  */
  /*                           fonts.  Some formats (TrueType & OpenType)  */
  /*                           provide localized and Unicode versions of   */
  /*                           this string.  Applications should use the   */
  /*                           format specific interface to access them.   */
  /*                           Can be NULL (e.g., in fonts embedded in a   */
  /*                           PDF file).                                  */
  /*                                                                       */
  /*    style_name          :: The face's style name.  This is an ASCII    */
  /*                           string, usually in English, which describes */
  /*                           the typeface's style (like `Italic',        */
  /*                           `Bold', `Condensed', etc).  Not all font    */
  /*                           formats provide a style name, so this field */
  /*                           is optional, and can be set to NULL.  As    */
  /*                           for `family_name', some formats provide     */
  /*                           localized and Unicode versions of this      */
  /*                           string.  Applications should use the format */
  /*                           specific interface to access them.          */
  /*                                                                       */
  /*    num_fixed_sizes     :: The number of bitmap strikes in the face.   */
  /*                           Even if the face is scalable, there might   */
  /*                           still be bitmap strikes, which are called   */
  /*                           `sbits' in that case.                       */
  /*                                                                       */
  /*    available_sizes     :: An array of @FT_Bitmap_Size for all bitmap  */
  /*                           strikes in the face.  It is set to NULL if  */
  /*                           there is no bitmap strike.                  */
  /*                                                                       */
  /*    num_charmaps        :: The number of charmaps in the face.         */
  /*                                                                       */
  /*    charmaps            :: An array of the charmaps of the face.       */
  /*                                                                       */
  /*    generic             :: A field reserved for client uses.  See the  */
  /*                           @FT_Generic type description.               */
  /*                                                                       */
  /*    bbox                :: The font bounding box.  Coordinates are     */
  /*                           expressed in font units (see                */
  /*                           `units_per_EM').  The box is large enough   */
  /*                           to contain any glyph from the font.  Thus,  */
  /*                           `bbox.yMax' can be seen as the `maximum     */
  /*                           ascender', and `bbox.yMin' as the `minimum  */
  /*                           descender'.  Only relevant for scalable     */
  /*                           formats.                                    */
  /*                                                                       */
  /*                           Note that the bounding box might be off by  */
  /*                           (at least) one pixel for hinted fonts.  See */
  /*                           @FT_Size_Metrics for further discussion.    */
  /*                                                                       */
  /*    units_per_EM        :: The number of font units per EM square for  */
  /*                           this face.  This is typically 2048 for      */
  /*                           TrueType fonts, and 1000 for Type~1 fonts.  */
  /*                           Only relevant for scalable formats.         */
  /*                                                                       */
  /*    ascender            :: The typographic ascender of the face,       */
  /*                           expressed in font units.  For font formats  */
  /*                           not having this information, it is set to   */
  /*                           `bbox.yMax'.  Only relevant for scalable    */
  /*                           formats.                                    */
  /*                                                                       */
  /*    descender           :: The typographic descender of the face,      */
  /*                           expressed in font units.  For font formats  */
  /*                           not having this information, it is set to   */
  /*                           `bbox.yMin'.  Note that this field is       */
  /*                           usually negative.  Only relevant for        */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    height              :: This value is the vertical distance         */
  /*                           between two consecutive baselines,          */
  /*                           expressed in font units.  It is always      */
  /*                           positive.  Only relevant for scalable       */
  /*                           formats.                                    */
  /*                                                                       */
  /*                           If you want the global glyph height, use    */
  /*                           `ascender - descender'.                     */
  /*                                                                       */
  /*    max_advance_width   :: The maximum advance width, in font units,   */
  /*                           for all glyphs in this face.  This can be   */
  /*                           used to make word wrapping computations     */
  /*                           faster.  Only relevant for scalable         */
  /*                           formats.                                    */
  /*                                                                       */
  /*    max_advance_height  :: The maximum advance height, in font units,  */
  /*                           for all glyphs in this face.  This is only  */
  /*                           relevant for vertical layouts, and is set   */
  /*                           to `height' for fonts that do not provide   */
  /*                           vertical metrics.  Only relevant for        */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    underline_position  :: The position, in font units, of the         */
  /*                           underline line for this face.  It is the    */
  /*                           center of the underlining stem.  Only       */
  /*                           relevant for scalable formats.              */
  /*                                                                       */
  /*    underline_thickness :: The thickness, in font units, of the        */
  /*                           underline for this face.  Only relevant for */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    glyph               :: The face's associated glyph slot(s).        */
  /*                                                                       */
  /*    size                :: The current active size for this face.      */
  /*                                                                       */
  /*    charmap             :: The current active charmap for this face.   */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Fields may be changed after a call to @FT_Attach_File or           */
  /*    @FT_Attach_Stream.                                                 */
  /*                                                                       */
  /* ===================================================================== */

public class FTFaceRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTFaceRec";

  private int num_faces = 0;
  private int face_index = 0;
  private int face_flags = Flags.Face.UNKNOWN.getVal();
  private int style_flags = Flags.FontStyle.NONE.getVal();
  private int num_glyphs = 0;
  private String family_name = null;
  private String style_name = null;
  private int num_fixed_sizes = 0;
  private FTBitmapSize[] available_sizes = null;
  private int num_charmaps = 0;
  public FTCharMapRec[] charmaps = null;
  /*# The following member variables (down to `underline_thickness') */
  /*# are only relevant to scalable outlines; cf. @FTBitmapSize      */
  /*# for bitmap fonts.                                              */
  private FTBBoxRec bbox = null;
  private int units_per_EM = 0;
  private int ascender = 0;
  private int descender = 0;
  private int height = 0;
  private int max_advance_width = 0;
  private int max_advance_height = 0;
  private int underline_position = 0;
  private int underline_thickness = 0;
  private FTGlyphSlotRec glyph = null;
  private FTSizeRec size = null;
  private FTCharMapRec charmap = null;
  /*@private begin */
  private FTDriverRec driver = null;
  protected FTStreamRec stream = null;
  private FTListRec sizes_list = null;
  private FTFaceInternalRec internal = null;

  /* ==================== FTFaceRec ================================== */
  public FTFaceRec() {
    oid++;
    id = oid;
      
    bbox = new FTBBoxRec();
    sizes_list = new FTListRec();
    internal = new FTFaceInternalRec();
Debug(0, DebugTag.DBG_INIT, TAG, "FTFaceRec constructor: "+mySelf()+"!");
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
    str.append("..num_faces: "+num_faces+'\n');
    str.append("..face_index: "+face_index+'\n');
    str.append("..face_flags: "+face_flags+'\n');
    str.append("..style_flags: "+style_flags+'\n');
    str.append("..num_glyphs: "+num_glyphs+'\n');
    str.append("..family_name: "+family_name+'\n');
    str.append("..style_name: "+style_name+'\n');
    str.append("..num_fixed_sizes: "+num_fixed_sizes+'\n');
    str.append("..num_charmaps : "+num_charmaps+'\n');
    str.append("..units_per_EM: "+units_per_EM+'\n');
    str.append("..ascender: "+ascender+'\n');
    str.append("..descender: "+descender+'\n');
    str.append("..height: "+height+'\n');
    str.append("..max_advance_width: "+max_advance_width+'\n');
    str.append("..max_advance_height: "+max_advance_height+'\n');
    str.append("..underline_position: "+underline_position+'\n');
    str.append("..underline_thickness: "+underline_thickness+'\n');
    return str.toString();
  }

  /* =====================================================================
   * FTNewFace
   * =====================================================================
   */
  public static FTError.ErrorTag FTNewFace(FTLibraryRec library, String pathname, int face_index, FTReference<FTFaceRec> face_ref) {
    FTOpenArgs args = new FTOpenArgs();
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "FTNewFace: pathname: "+pathname+"!");
    /* test for valid `library' and `face_ref' delayed to FT_Open_Face() */
    if (pathname == null) {
      Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "FTNewFace1 inv arg");
      return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    args.setFlags(FTUtilFlags.StreamOpen.PATHNAME.getVal());
    args.setPathname(pathname);
    args.setStream(null);
    error = FTOpenFace(library, args, face_index, face_ref);
    return error;
  }

  /* =====================================================================
   * open_face
   * =====================================================================
   */
  public FTError.ErrorTag open_face(FTDriverRec driver, FTStreamRec stream, int face_index, int num_params,
                                           FTParameter[] params) {
    FTDriverClassRec clazz;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTError.ErrorTag error2 = FTError.ErrorTag.ERR_OK;

    FTTrace.Trace(7, TAG, "open_face");
    clazz  = driver.getDriver_clazz();
    this.driver = driver;
    this.stream = stream;
    error = clazz.initFace(stream, this, face_index, num_params, params);
    FTTrace.Trace(7, TAG, String.format("clazz init_face2: "+error));
    if (error != FTError.ErrorTag.ERR_OK) {
//      destroy_charmaps(face, memory);
      clazz.doneFace(this);
      return error;
    }
    /* select Unicode charmap by default */
    error2 = FTCharMapRec.find_unicode_charmap(this);
    /* if no Unicode charmap can be found, FT_Err_Invalid_CharMap_Handle */
    /* is returned.                                                      */
    /* no error should happen, but we want to play safe */
    if ((error2 != FTError.ErrorTag.ERR_OK) && (error2 != FTError.ErrorTag.LOAD_INVALID_CHARMAP_HANDLE)) {
      error = error2;
//        destroy_charmaps(face, memory);
      clazz.doneFace(this);
      return error;
    }
    return error;
  }

  /* =====================================================================
   * FTOpenFace
   * =====================================================================
   */
  public static FTError.ErrorTag FTOpenFace(FTLibraryRec library, FTOpenArgs args, int face_index, FTReference<FTFaceRec> face_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTDriverRec driver = null;
    FTReference<RandomAccessFile> stream_ref = new FTReference<RandomAccessFile>();
    FTFaceRec face = null;
    FTListNodeRec node = null;
    boolean is_external_stream;
    FTModuleRec cur;
    int module_idx = 0;
    int limit = 0;
    FTStreamRec stream;
    boolean is_success = false;

    FTTrace.Trace(7, TAG, "FT_Open_Face");
    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "FT_OPEN_FACE: args.pathname: "+args.getPathname()+"!");
    /* test for valid `library' delayed to */
    /* FTStreamNew()                     */
    if ((face_ref == null && face_index >= 0) || args == null) {
      Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "FTOpenFace1 inv arg");
      return FTError.ErrorTag.INTERP_INVALID_ARGUMENT;
    }
    is_external_stream = (FTUtilFlags.StreamOpen.isStream(args.getFlags()) && args.getStream() != null);
    if (is_external_stream) {
      stream = args.getStream();
    } else {
      stream = new FTStreamRec();
    }
    /* create input stream */
    error = stream.Open(args);
    if (error != FTError.ErrorTag.ERR_OK) {
      /* If we are on the mac, and we get an                          */
      /* FT_Err_Invalid_Stream_Operation it may be because we have an */
      /* empty data fork, so we need to check the resource fork.      */
      if (error != FTError.ErrorTag.LOAD_CANNOT_OPEN_STREAM &&
          error != FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT &&
          error != FTError.ErrorTag.LOAD_INVALID_STREAM_OPERATION) {
        FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
        return error;
      }
      /* no driver is able to handle this format */
      error = FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT;
      FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
      return error;
    }
    /* If the font driver is specified in the `args' structure, use */
    /* it.  Otherwise, we scan the list of registered drivers.      */
    if (FTUtilFlags.StreamOpen.isDriver(args.getFlags()) && args.getDriver() != null) {
      driver = (TTDriverRec)args.getDriver();
      /* not all modules are drivers, so check... */
      if ((driver.getDriver_clazz().module_flags & Flags.Module.FONT_DRIVER.getVal()) != 0) {
        int num_params = 0;
        FTParameter[] params =null ;

        if (FTUtilFlags.StreamOpen.isParams(args.getFlags())) {
          num_params = args.getNum_params();
          params = args.getParams();
        }
        /* allocate the face object and perform basic initialization */
        switch (driver.getDriver_clazz().getFaceObjectType()) {
          case FACE:
            face = new TTFaceRec();
            break;
          default:
            return FTError.ErrorTag.FACE_BAD_FACE_OBJECT_TYPE;
        }
        error = face.open_face(driver, stream, face_index, num_params, params);
        if (error == FTError.ErrorTag.ERR_OK) {
          FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
          return error;
        } else {
          error = FTError.ErrorTag.LOAD_INVALID_HANDLE;
        }
      }
//        FTStreamFree(stream, external_stream);
      FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
      return error;
    } else {
      error = FTError.ErrorTag.LOAD_MISSING_MODULE;
        /* check each font driver for an appropriate format */
      cur = library.modules[0];
      limit = library.num_modules;
      for (module_idx = 0; module_idx < limit; module_idx++) {
        cur = library.modules[module_idx];
          /* not all modules are font drivers, so check... */
        if ((cur.module_clazz.module_flags & Flags.Module.FONT_DRIVER.getVal()) != 0) {
          int num_params = 0;
          FTParameter params[] = null;

          TTDriverRec cur_obj = (TTDriverRec)library.modules[module_idx];
          driver = cur_obj;
          if (FTUtilFlags.StreamOpen.isParams(args.getFlags())) {
            num_params = args.getNum_params();
            params = args.getParams();
          }
          /* allocate the face object and perform basic initialization */
          switch (driver.getDriver_clazz().getFaceObjectType()) {
            case FACE:
              face = new TTFaceRec();
              break;
            default:
              return FTError.ErrorTag.FACE_BAD_FACE_OBJECT_TYPE;
          }
          error = face.open_face(driver, stream, face_index, num_params, params);
          if (error  == FTError.ErrorTag.ERR_OK) {
            is_success = true;
            break;
          }
          if (error !=  FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT) {
              /* If we are on the mac, and we get an                          */
              /* FT_Err_Invalid_Stream_Operation it may be because we have an */
              /* empty data fork, so we need to check the resource fork.      */
            if (error != FTError.ErrorTag.LOAD_CANNOT_OPEN_STREAM &&
                error != FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT &&
                error != FTError.ErrorTag.LOAD_INVALID_STREAM_OPERATION) {
              FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
              return error;
            }
              /* no driver is able to handle this format */
            error = FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT;
            FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
            return error;
          }
        }
      }
      if (! is_success) {
          /* If we are on the mac, and we get an                          */
          /* FT_Err_Invalid_Stream_Operation it may be because we have an */
          /* empty data fork, so we need to check the resource fork.      */
        if (error != FTError.ErrorTag.LOAD_CANNOT_OPEN_STREAM &&
            error != FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT &&
            error != FTError.ErrorTag.LOAD_INVALID_STREAM_OPERATION) {
          FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
          return error;
        }
          /* no driver is able to handle this format */
        error = FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT;
        FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
        return error;
      }
    }
    FTTrace.Trace(7, TAG, "FT_Open_Face: New face object, adding to list stream: "+stream+"!");
      /* set the FT_FACE_FLAG_EXTERNAL_STREAM bit for FT_Done_Face */
    if (is_external_stream) {
      face.face_flags = face.face_flags | Flags.Face.EXTERNAL_STREAM.getVal();
    }
      /* add the face object to its driver's list */
    node = new FTListNodeRec();
    face.stream = stream;
    node.data = face;
      /* don't assume driver is the same as face->driver, so use */
      /* face->driver instead.                                   */
    node.FTListAdd(face.driver.getFaces_list());
      /* now allocate a glyph slot object for the face */
    FTTrace.Trace(7, TAG, "FT_Open_Face: Creating glyph slot");
    if (face_index >= 0) {
      FTReference<FTGlyphSlotRec> slot_ref = new FTReference<FTGlyphSlotRec>();
      error = FTGlyphSlotRec.FTNewGlyphSlot(face, slot_ref);
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
        return error;
      }
        /* finally, allocate a size object for the face */
      {
        FTSizeRec size;

        FTTrace.Trace(7, TAG, "FT_Open_Face: Creating size object");
        FTReference<FTSizeRec> size_ref = new FTReference<>();
        error = FTSizeRec.FTNewSize(face, size_ref);
        if (error != FTError.ErrorTag.ERR_OK) {
          FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
          return error;
        }
        size = size_ref.Get();
        face.size = size;
      }
    }
      /* some checks */
    if ((face.face_flags & Flags.Face.SCALABLE.getVal()) != 0) {
      if (face.height < 0) {
        face.height = -face.height;
      }
      if ((face.face_flags & Flags.Face.VERTICAL.getVal()) != 0) {
        face.max_advance_height = face.height;
      }
    }
    if ((face.face_flags & Flags.Face.FIXED_SIZES.getVal()) != 0) {
      int i;

      for (i = 0; i < face.num_fixed_sizes; i++) {
        FTBitmapSize bsize = face.available_sizes[i];

        if (bsize.height < 0) {
          bsize.height = -bsize.height;
        }
        if (bsize.x_ppem < 0) {
          bsize.x_ppem = -bsize.x_ppem;
        }
        if (bsize.y_ppem < 0) {
          bsize.y_ppem = -bsize.y_ppem;
        }
      }
    }
      /* initialize internal face data */
    {
      face.internal.transform_matrix.xx = 0x10000;
      face.internal.transform_matrix.xy = 0;
      face.internal.transform_matrix.yx = 0;
      face.internal.transform_matrix.yy = 0x10000;
      face.internal.transform_delta.x = 0;
      face.internal.transform_delta.y = 0;
      face.internal.refcount = 1;
    }
    if (face_ref != null) {
      face_ref.Set(face);
    } else {
//        FTDoneFace(face);
    }
    FTTrace.Trace(7, TAG, "FT_Open_Face: Return "+error);
    return error;
  }

  /* ==================== getNum_faces ================================== */
  public int getNum_faces() {
    return num_faces;
  }

  /* ==================== setNum_faces ================================== */
  public void setNum_faces(int num_faces) {
    this.num_faces = num_faces;
  }

  /* ==================== getFace_index ================================== */
  public int getFace_index() {
    return face_index;
  }

  /* ==================== setFace_index ================================== */
  public void setFace_index(int face_index) {
    this.face_index = face_index;
  }

  /* ==================== getFace_flags ================================== */
  public int getFace_flags() {
    return face_flags;
  }

  /* ==================== setFace_flags ================================== */
  public void setFace_flags(int face_flags) {
    this.face_flags = face_flags;
  }

  /* ==================== getStyle_flags ================================== */
  public int getStyle_flags() {
    return style_flags;
  }

  /* ==================== setStyle_flags ================================== */
  public void setStyle_flags(int style_flags) {
    this.style_flags = style_flags;
  }

  /* ==================== getNum_glyphs ================================== */
  public int getNum_glyphs() {
    return num_glyphs;
  }

  /* ==================== setNum_glyphs ================================== */
  public void setNum_glyphs(int num_glyphs) {
    this.num_glyphs = num_glyphs;
  }

  /* ==================== getFamily_name ================================== */
  public String getFamily_name() {
    return family_name;
  }

  /* ==================== setFamily_name ================================== */
  public void setFamily_name(String family_name) {
    this.family_name = family_name;
  }

  /* ==================== getStyle_name ================================== */
  public String getStyle_name() {
    return style_name;
  }

  /* ==================== setStyle_name ================================== */
  public void setStyle_name(String style_name) {
    this.style_name = style_name;
  }

  /* ==================== getNum_fixed_sizes ================================== */
  public int getNum_fixed_sizes() {
    return num_fixed_sizes;
  }

  /* ==================== setNum_fixed_sizes ================================== */
  public void setNum_fixed_sizes(int num_fixed_sizes) {
    this.num_fixed_sizes = num_fixed_sizes;
  }

  /* ==================== getAvailable_sizes ================================== */
  public FTBitmapSize[] getAvailable_sizes() {
    return available_sizes;
  }

  /* ==================== setAvailable_sizes ================================== */
  public void setAvailable_sizes(FTBitmapSize[] available_sizes) {
    this.available_sizes = available_sizes;
  }

  /* ==================== getNum_charmaps ================================== */
  public int getNum_charmaps() {
    return num_charmaps;
  }

  /* ==================== setNum_charmaps ================================== */
  public void setNum_charmaps(int num_charmaps) {
    this.num_charmaps = num_charmaps;
  }

  /* ==================== getCharmaps ================================== */
  public FTCharMapRec[] getCharmaps() {
    return charmaps;
  }

  /* ==================== setCharmaps ================================== */
  public void setCharmaps(FTCharMapRec[] charmaps) {
    this.charmaps = charmaps;
  }

  /* ==================== getBbox ================================== */
  public FTBBoxRec getBbox() {
    return bbox;
  }

  /* ==================== setBbox ================================== */
  public void setBbox(FTBBoxRec bbox) {
    this.bbox = bbox;
  }

  /* ==================== getUnits_per_EM ================================== */
  public int getUnits_per_EM() {
    return units_per_EM;
  }

  /* ==================== setUnits_per_EM ================================== */
  public void setUnits_per_EM(int units_per_EM) {
    this.units_per_EM = units_per_EM;
  }

  /* ==================== getAscender ================================== */
  public int getAscender() {
    return ascender;
  }

  /* ==================== setAscender ================================== */
  public void setAscender(int ascender) {

    this.ascender = ascender;
  }

  /* ==================== getDescender ================================== */
  public int getDescender() {
    return descender;
  }

  /* ==================== setDescender ================================== */
  public void setDescender(int descender) {
    this.descender = descender;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getMax_advance_width ================================== */
  public int getMax_advance_width() {
    return max_advance_width;
  }

  /* ==================== setMax_advance_width ================================== */
  public void setMax_advance_width(int max_advance_width) {
    this.max_advance_width = max_advance_width;
  }

  /* ==================== getMax_advance_height ================================== */
  public int getMax_advance_height() {
    return max_advance_height;
  }

  /* ==================== setMax_advance_height ================================== */
  public void setMax_advance_height(int max_advance_height) {
    this.max_advance_height = max_advance_height;
  }

  /* ==================== getUnderline_position ================================== */
  public int getUnderline_position() {
    return underline_position;
  }

  /* ==================== setUnderline_position ================================== */
  public void setUnderline_position(int underline_position) {
    this.underline_position = underline_position;
  }

  /* ==================== getUnderline_thickness ================================== */
  public int getUnderline_thickness() {
    return underline_thickness;
  }

  /* ==================== setUnderline_thickness ================================== */
  public void setUnderline_thickness(int underline_thickness) {
    this.underline_thickness = underline_thickness;
  }

  /* ==================== getGlyph ================================== */
  public FTGlyphSlotRec getGlyph() {
    return glyph;
  }

  /* ==================== setGlyph ================================== */
  public void setGlyph(FTGlyphSlotRec glyph) {
    this.glyph = glyph;
  }

  /* ==================== getSize ================================== */
  public FTSizeRec getSize() {
    return size;
  }

  /* ==================== setSize ================================== */
  public void setSize(FTSizeRec size) {
    this.size = size;
  }

  /* ==================== getCharmap ================================== */
  public FTCharMapRec getCharmap() {
    return charmap;
  }

  /* ==================== setCharmap ================================== */
  public void setCharmap(FTCharMapRec charmap) {
    this.charmap = charmap;
  }

  /* ==================== getDriver ================================== */
  public FTDriverRec getDriver() {
    return driver;
  }

  /* ==================== setDriver ================================== */
  public void setDriver(FTDriverRec driver) {
    this.driver = driver;
  }

  /* ==================== getStream ================================== */
  public FTStreamRec getStream() {
    return stream;
  }

  /* ==================== setStream ================================== */
  public void setStream(FTStreamRec stream) {
    this.stream = stream;
  }

  /* ==================== getSizes_list ================================== */
  public FTListRec getSizes_list() {
    return sizes_list;
  }

  /* ==================== setSizes_list ================================== */
  public void setSizes_list(FTListRec sizes_list) {
    this.sizes_list = sizes_list;
  }

  /* ==================== getInternal ================================== */
  public FTFaceInternalRec getInternal() {
    return internal;
  }

  /* ==================== setInternal ================================== */
  public void setInternal(FTFaceInternalRec internal) {
    this.internal = internal;
  }

}