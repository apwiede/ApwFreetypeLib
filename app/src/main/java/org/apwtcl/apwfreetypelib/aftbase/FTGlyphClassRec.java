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
  /*    FTGlyphClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class FTGlyphClassRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGlyphClassRec";

  protected int glyph_size = 0;
  protected FTTags.GlyphFormat glyph_format = FTTags.GlyphFormat.NONE;

  /* ==================== FTGlyphClassRec ================================== */
  public FTGlyphClassRec() {
    oid++;
    id = oid;
  }

  /* ==================== mySelf ================================== */
  public String mySelf() {
    String str = TAG + "!" + id + "!";
    return str;
  }

  /* ==================== toString ===================================== */
  public String toString() {
    StringBuffer str = new StringBuffer(mySelf() + "!");
    return str.toString();
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf() + "\n");
    str.append("..glyph_size: "+glyph_size+'\n');
    str.append("..glyph_format: "+glyph_format+'\n');
    return str.toString();
  }

  /* ==================== glyphInit ===================================== */
  public FTError.ErrorTag glyphInit(FTGlyphRec glyph, FTGlyphSlotRec slot) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
Log.e(TAG, "glyphInit not yet implemented");
    return error;
  }

  /* ==================== glyphDone ===================================== */
  public FTError.ErrorTag glyphDone() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "glyphDone not yet implemented");
    return error;
  }

  /* ==================== glyphCopy ===================================== */
  public FTError.ErrorTag glyphCopy() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "glyphCopy not yet implemented");
    return error;
  }

  /* ==================== glyphTransform ===================================== */
  public FTError.ErrorTag glyphTransform() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "glyphTransform not yet implemented");
    return error;
  }

  /* ==================== glyphBBox ===================================== */
  public FTError.ErrorTag glyphBBox() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "glyphBBox not yet implemented");
    return error;
  }

  /* ==================== glyphPrepare ===================================== */
  public FTError.ErrorTag glyphPrepare(FTGlyphRec glyph, FTReference<FTGlyphSlotRec> slot_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "glyphPrepare not yet implemented");
    return error;
  }

}