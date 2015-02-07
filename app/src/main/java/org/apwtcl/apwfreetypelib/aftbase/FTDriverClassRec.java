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
  /*    FTDriverClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.*;

import java.util.Set;

public class FTDriverClassRec extends FTModuleClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTDriverClassRec";

  private FTTags.DriverRec face_object_type = FTTags.DriverRec.FACE;
  private FTTags.DriverRec size_object_type = FTTags.DriverRec.SIZE;
  private FTTags.DriverRec slot_object_type = FTTags.DriverRec.GLYPH_SLOT;

  /* ==================== FTDriverClassRec ================================== */
  public FTDriverClassRec() {
    super();
    oid++;
    id = oid;
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTDriverClassRec constructor called!!");
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

  /* ==================== initFace ===================================== */
  public FTError.ErrorTag initFace(FTStreamRec stream, FTFaceRec face, int face_index, int num_params,
        FTParameterRec[] params) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "initFace not yet implemented");
    return error;
  }

  /* ==================== doneFace ===================================== */
  public void doneFace(FTFaceRec face) {
    Log.e(TAG, "doneFace not yet implemented");
  }

  /* ==================== initSize ===================================== */
  public FTError.ErrorTag initSize(FTSizeRec size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "initSize not yet implemented");
    return error;
  }

  /* ==================== doneSize ===================================== */
  public FTError.ErrorTag doneSize(FTSizeRec size) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "doneSize not yet implemented");
    return error;
  }

  /* ==================== initSlot ===================================== */
  public FTError.ErrorTag initSlot(FTGlyphSlotRec slot) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "initSlot not yet implemented");
    return error;
  }

  /* ==================== doneSlot ===================================== */
  public FTError.ErrorTag doneSlot(FTGlyphSlotRec slot) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "doneSlot not yet implemented");
    return error;
  }

  /* ==================== loadGlyph ===================================== */
  public FTError.ErrorTag loadGlyph(FTGlyphSlotRec slot, FTSizeRec size, int glyph_index, Set<Flags.Load> load_flags) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "loadGlyph not yet implemented");
    return error;
  }

  /* ==================== getKerning ===================================== */
  public FTError.ErrorTag getKerning(FTFaceRec face, int left_glyph, int right_glyph, FTVectorRec kerning) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "getKerning not yet implemented");
    return error;
  }

  /* ==================== attachFile ===================================== */
  public FTError.ErrorTag attachFile() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "attachFile not yet implemented");
    return error;
  }

  /* ==================== getAdvances ===================================== */
  public FTError.ErrorTag getAdvances(FTFaceRec face, int start, int count, int flags, FTReference<Integer> advances_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "getAdvances not yet implemented");
    return error;
  }

  /* ==================== requestSize ===================================== */
  public FTError.ErrorTag requestSize(FTSizeRec size, FTSizeRequestRec req) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "requestSize not yet implemented");
    return error;
  }

  /* ==================== selectSize ===================================== */
  public FTError.ErrorTag selectSize(int strike_index) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "selectSize not yet implemented");
    return error;
  }

  /* ==================== getFaceObjectType ===================================== */
  public FTTags.DriverRec getFaceObjectType() {
    return face_object_type;
  }

  /* ==================== getSizeObjectType ===================================== */
  public FTTags.DriverRec getSizeObjectType() {
    return size_object_type;
  }

  /* ==================== getSlotObjectType ===================================== */
  public FTTags.DriverRec getSlotObjectType() {
    return slot_object_type;
  }

}