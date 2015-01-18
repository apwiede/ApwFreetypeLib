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
  /*    TTDriverClass                                                      */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.*;
import org.apwtcl.apwfreetypelib.aftsfnt.FTSfntInterfaceClass;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTRunInstructions;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTDriverClass extends FTDriverClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTDriverClass";

  /* ==================== TTDriverClass ================================== */
  public TTDriverClass() {
    super();
    oid++;
    id = oid;

Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "TTDriverClass constructor called!!");
    module_flags = (Flags.Module.FONT_DRIVER.getVal() |
         Flags.Module.DRIVER_SCALABLE.getVal() |
         Flags.Module.DRIVER_HAS_HINTER.getVal());  /* a font driver */
    module_type = FTTags.ModuleType.TT_DRIVER;
    module_name = "truetype";   /* driver name */
    module_version = 0x10000;   /* driver version 1.0 */
    module_requires = 0x20000;  /* driver requires FreeType 2.0 or higher */
    module_interface = null;    /* driver specific interface */
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

  /* ==================== tt_driver_init ===================================== */
  private FTError.ErrorTag tt_driver_init(FTModuleRec module) {   /* TT_Driver */
Debug(0, DebugTag.DBG_INIT, TAG, "tt_driver_init");

    TTDriverRec  driver = (TTDriverRec)module;
Debug(0, DebugTag.DBG_INIT, TAG, String.format("module_type: %s", driver.getDriver_clazz().module_type));
    if (driver.NewContext() == null) {
      return FTError.ErrorTag.INTERP_COULD_NOT_FIND_CONTEXT;
    }
    driver.interpreter_version = TTRunInstructions.TT_INTERPRETER_VERSION_35;
    return FTError.ErrorTag.ERR_OK;
  }


  /* =====================================================================
   * <Function>
   *    tt_face_init
   *
   * <Description>
   *    Initialize a given TrueType face object.
   *
   * <Input>
   *    stream     :: The source font stream.
   *
   *    face_index :: The index of the font face in the resource.
   *
   *    num_params :: Number of additional generic parameters.  Ignored.
   *
   *    params     :: Additional generic parameters.  Ignored.
   *
   * <InOut>
   *    face       :: The newly built face object.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   * =====================================================================
   */
  private FTError.ErrorTag tt_face_init(FTStreamRec stream, TTFaceRec ttface, int face_index, int num_params, FTParameter[] params) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTLibraryRec library;
    FTSfntInterfaceClass sfnt = null;

Debug(0, DebugTag.DBG_INIT, TAG, "tt_face_init");
    FTTrace.Trace(7, TAG, "TTF driver");
    library = ttface.getDriver().library;
    sfnt = (FTSfntInterfaceClass)FTModuleRec.FTGetModuleInterface(library, "sfnt");
    if (sfnt == null) {
      FTTrace.Trace(7, TAG, "tt_face_init: cannot access `sfnt' module");
      error = FTError.ErrorTag.INTERP_MISSING_MODULE;
      return error;
    }
      /* create input stream from resource */
    if (stream.seek(0) < 0) {
      return error;
    }
      /* check that we have a valid TrueType file */
    error = sfnt.initFace(stream, ttface, face_index, num_params, params);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
      /* We must also be able to accept Mac/GX fonts, as well as OT ones. */
      /* The 0x00020000 tag is completely undocumented; some fonts from   */
      /* Arphic made for Chinese Windows 3.1 have this.                   */
    if (ttface.getFormat_tag() != TTTags.Table._00010000 &&  /* MS fonts  */
        ttface.getFormat_tag() != TTTags.Table._00020000 && /* CJK fonts for Win 3.1 */
        ttface.getFormat_tag() != TTTags.Table.truE) {   /* Mac fonts */
      FTTrace.Trace(7, TAG, "  not a TTF font");
      error = FTError.ErrorTag.INTERP_UNKNOWN_FILE_FORMAT;
      return error;
    }
    ttface.setFace_flags((ttface.getFace_flags() | Flags.Face.HINTER.getVal()));
      /* If we are performing a simple font format check, exit immediately. */
    if (face_index < 0) {
      return FTError.ErrorTag.ERR_OK;
    }
      /* Load font directory */
    error = sfnt.loadFace(stream, ttface, face_index, num_params, params);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    Debug(0, DebugTag.DBG_INIT, TAG, "tt_face_init: after sfnt_load_face");
//      if (tt_check_trickyness(ttface)) {
//        ttface.face_flags |= FTFaceRec.FT_FACE_FLAG_TRICKY;
//      }
//      error = tt_face_load_hdmx(face, stream);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
// FIXME!!
    ttface.setFace_flags((ttface.getFace_flags()) | Flags.Face.SCALABLE.getVal());
    if ((ttface.getFace_flags() & Flags.Face.SCALABLE.getVal()) != 0) {
      if (error == FTError.ErrorTag.ERR_OK) {
        error = ttface.loadLoca(stream);
      }
// TEMPORARY!!!
if (error == FTError.ErrorTag.ERR_OK) {
  int gindex = 0;
  error = ttface.loadGlyf(stream, gindex);
}


      if (error == FTError.ErrorTag.ERR_OK) {
        error = ttface.loadCvt(stream);
      }
      if (error == FTError.ErrorTag.ERR_OK) {
        error = ttface.loadFpgm(stream);
      }
      if (error == FTError.ErrorTag.ERR_OK) {
        error = ttface.loadPrep(stream);
      }
        /* Check the scalable flag based on `loca'. */
/*
        if (ttface.num_fixed_sizes != 0 && ttface.glyph_locations != null &&
             tt_check_single_notdef(ttface)) {
          FTTrace.Trace(7, TAG, "tt_face_init: \n"+
                      " Only the `.notdef' glyph has an outline.\n"+
                      "             "+
                      " Resetting scalable flag to FALSE.");
          ttface.face_flags = ttface.face_flags & ~Flags.Face.SCALABLE.getVal();
        }
*/
    }
      /* initialize standard glyph loading routines */
    TTFaceRec.TTInitGlyphLoading(ttface);
    return error;
  }

  /* =====================================================================
   *
   * <Function>
   *    tt_face_done
   *
   * <Description>
   *    Finalize a given face object.
   *
   * <Input>
   *    face :: A pointer to the face object to destroy.
   * =====================================================================
   */
  private void tt_face_done(TTFaceRec face) {   /* TT_Face */
Debug(0, DebugTag.DBG_INIT, TAG, "tt_face_done");

    if (face == null) {
      return;
    }
//      stream = ttface.stream;
//      sfnt = (FTSfntInterface)face.sfnt;
      /* for `extended TrueType formats' (i.e. compressed versions) */
//      if (face.extra.finalizer) {
//        face.extra.finalizer(face.extra.data);
//      }
//      if (sfnt != null) {
//        sfnt.done_face(face);
//      }
      /* freeing the locations table */
//      tt_face_done_loca(face);
//      tt_face_free_hdmx(face);
      /* freeing the CVT */
//      FT_FREE(face.cvt);
//    face.cvt_size = 0;
      /* freeing the programs */
//      FT_FRAME_RELEASE( face->font_program );
//      FT_FRAME_RELEASE( face->cvt_program );
//    face.font_program_size = 0;
//    face.cvt_program_size  = 0;
  }


  /* =====================================================================
   * tt_slot_init
   *
   * <Description>
   *    Initialize a new slot object.
   *
   * <InOut>
   *    slot :: A handle to the slot object.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  private FTError.ErrorTag tt_slot_init(FTGlyphSlotRec slot) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_INIT, TAG, "tt_slot_init");
    error = slot.getInternal().getLoader().FTGlyphLoaderCreateExtra();
    return error;
  }

  /* =====================================================================
   * tt_glyph_load
   *
   * <Description>
   *    A driver method used to load a glyph within a given glyph slot.
   *
   * <Input>
   *    slot        :: A handle to the target slot object where the glyph
   *                   will be loaded.
   *
   *    size        :: A handle to the source face size at which the glyph
   *                   must be scaled, loaded, etc.
   *
   *    glyph_index :: The index of the glyph in the font file.
   *
   *    load_flags  :: A flag indicating what to load for this glyph.  The
   *                   FT_LOAD_XXX constants can be used to control the
   *                   glyph loading process (e.g., whether the outline
   *                   should be scaled, whether to load bitmaps or not,
   *                   whether to hint the outline, etc).
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_glyph_load(TTGlyphSlotRec ttslot, TTSizeRec ttsize, int glyph_index, int load_flags) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "tt_glyph_load");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    FTFaceRec face = ttslot.getFace();
    FTReference<TTSizeRec> size_ref = new FTReference<TTSizeRec>();

    if (ttsize == null) {
      error = FTError.ErrorTag.LOAD_INVALID_SIZE_HANDLE;
      return error;
    }
    if (face == null) {
      error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
      return error;
    }
    if (glyph_index >= face.getNum_glyphs()) {
      error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
      return error;
    }
    if ((load_flags & Flags.Load.NO_HINTING.getVal()) !=0) {
        /* both FT_LOAD_NO_HINTING and FT_LOAD_NO_AUTOHINT   */
        /* are necessary to disable hinting for tricky fonts */
      if ((face.getFace_flags() & Flags.Face.TRICKY.getVal()) != 0) {
        load_flags &= ~Flags.Load.NO_HINTING.getVal();
      }
      if ((load_flags & Flags.Load.NO_AUTOHINT.getVal()) != 0) {
        load_flags |= Flags.Load.NO_HINTING.getVal();
      }
    }
    if ((load_flags & (Flags.Load.NO_RECURSE.getVal() | Flags.Load.NO_SCALE.getVal())) != 0) {
      load_flags |= (Flags.Load.NO_BITMAP.getVal() | Flags.Load.NO_SCALE.getVal());
      if ((face.getFace_flags() & Flags.Face.TRICKY.getVal()) == 0) {
        load_flags |= Flags.Load.NO_HINTING.getVal();
      }
    }
      /* now load the glyph outline if necessary */
    error = ttslot.TTLoadGlyph(ttsize, glyph_index, load_flags);
      /* force drop-out mode to 2 - irrelevant now */
      /* slot->outline.dropout_mode = 2; */

    return error;
  }

  /* =====================================================================
   * tt_get_kerning
   *
   * <Description>
   *    A driver method used to return the kerning vector between two
   *    glyphs of the same face.
   *
   * <Input>
   *    face        :: A handle to the source face object.
   *
   *    left_glyph  :: The index of the left glyph in the kern pair.
   *
   *    right_glyph :: The index of the right glyph in the kern pair.
   *
   * <Output>
   *    kerning     :: The kerning vector.  This is in font units for
   *                   scalable formats, and in pixels for fixed-sizes
   *                   formats.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * <Note>
   *    Only horizontal layouts (left-to-right & right-to-left) are
   *    supported by this function.  Other layouts, or more sophisticated
   *    kernings, are out of scope of this method (the basic driver
   *    interface is meant to be simple).
   *
   *    They can be implemented by format-specific interfaces.
   *
   * =====================================================================
   */
  private FTError.ErrorTag tt_get_kerning(FTFaceRec face, int left_glyph, int right_glyph, FTVectorRec kerning) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "tt_get_kerning not yet implemented");
    return error;
  }

  /* =====================================================================
   * tt_get_advances
   * =====================================================================
   */
  private FTError.ErrorTag tt_get_advances(TTFaceRec face, int start, int count, int flags, FTReference<Integer> advances_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "tt_get_advances not yet implemented");
    return error;
  }

  /* ==================== moduleInit ===================================== */
 @Override
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.i(TAG, "moduleInit call tt_driver_init");
    return tt_driver_init(module);
  }

  /* ==================== moduleDone ===================================== */
  @Override
  public void moduleDone() {
    Log.e(TAG, "moduleDone not yet implemented");
//    return  tt_driver_done();
  }

  /* ==================== getInterface ===================================== */
//  @Override
  public FTError.ErrorTag getInterface() {
    Log.e(TAG, "getInterface not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return tt_get_interface();
  }

  /* ==================== loadGlyph ===================================== */
  @Override
  public FTError.ErrorTag loadGlyph(FTGlyphSlotRec slot, FTSizeRec size, int glyph_index, int load_flags) {
    return  tt_glyph_load((TTGlyphSlotRec)slot, (TTSizeRec)size, glyph_index, load_flags);
  }

  /* ==================== attachFile ===================================== */
  @Override
  public FTError.ErrorTag attachFile() {
    // nothing to do
    return FTError.ErrorTag.ERR_OK;
  }

  String face_class_name = "org.apwtcl.gles20.truetype.TTFaceFuncs";

  /* ==================== initFace ===================================== */
  @Override
  public FTError.ErrorTag initFace(FTStreamRec stream, FTFaceRec face, int face_index, int num_params,
                                   FTParameter[] params) {
    return tt_face_init(stream, (TTFaceRec)face, face_index, num_params, params);
  }

  /* ==================== doneFace ===================================== */
  @Override
  public void doneFace(FTFaceRec face) {
    tt_face_done((TTFaceRec)face);
  }

  /* ==================== initSlot ===================================== */
  @Override
  public FTError.ErrorTag initSlot(FTGlyphSlotRec slot) {
    return tt_slot_init(slot);
  }

  /* ==================== doneSlot ===================================== */
  @Override
  public FTError.ErrorTag doneSlot(FTGlyphSlotRec slot) {
    // nothing to do
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getAdvances ===================================== */
  @Override
  public FTError.ErrorTag getAdvances(FTFaceRec face, int start, int count, int flags, FTReference<Integer> advances_ref) {
    return tt_get_advances((TTFaceRec)face, start, count, flags, advances_ref);
  }

  /* ==================== getKerning ===================================== */
  @Override
  public FTError.ErrorTag getKerning(FTFaceRec face, int left_glyph, int right_glyph, FTVectorRec vec) {
    return tt_get_kerning((TTFaceRec)face, left_glyph, right_glyph, vec);
  }

  /* ==================== initSize ===================================== */
  @Override
  public FTError.ErrorTag initSize(FTSizeRec size) {
    return ((TTSizeRec)size).tt_size_init();
  }

  /* ==================== doneSize ===================================== */
  @Override
  public FTError.ErrorTag doneSize(FTSizeRec size) {
    ((TTSizeRec)size).tt_size_done();
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== requestSize ===================================== */
  @Override
  public FTError.ErrorTag requestSize(FTSizeRec size, FTSizeRequestRec req) {
    return ((TTSizeRec)size).tt_size_request(req);
  }

  /* ==================== selectSize ===================================== */
  @Override
  public FTError.ErrorTag  selectSize(int strike_index) {
    return FTError.ErrorTag.ERR_OK;
  }

}