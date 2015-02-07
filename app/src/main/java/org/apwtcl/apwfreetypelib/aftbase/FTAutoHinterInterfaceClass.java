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
  /*    FTAutoHinterInterfaceClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.nfc.Tag;
import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;

import java.util.Set;

public class FTAutoHinterInterfaceClass  extends FTModuleInterface {
  private static int oid = 0;

  private int id;
  private final static String TAG = "FTAutoHinterInterfaceClass";

  /* ==================== FTAutoHinterInterfaceClass =========================== */
  public FTAutoHinterInterfaceClass() {
    oid++;
    id = oid;
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

  /* ==================== resetFace ================================== */
  public FTError.ErrorTag resetFace() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "resetFace not yet implemented");
    return error;
  }

  /* ==================== getGlobalHints ================================== */
  public FTError.ErrorTag getGlobalHints() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "getGlobalHints not yet implemented");
    return error;
  }

  /* ==================== doneGlobalHints ================================== */
  public FTError.ErrorTag doneGlobalHints() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "doneGlobalHints not yet implemented");
    return error;
  }

  /* ==================== loadGlyph ================================== */
  public FTError.ErrorTag loadGlyph(FTModuleRec hinter, FTGlyphSlotRec slot, FTSizeRec size, int glyph_index, Set<Flags.Load>
      load_flags) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "loadGlyph not yet implemented");
    return error;
  }

}