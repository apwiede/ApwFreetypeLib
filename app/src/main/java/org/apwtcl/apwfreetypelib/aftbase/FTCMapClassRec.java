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
  /*    FTCMapClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTCMapRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCMapClassRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCMapClassRec";

  /* common fields to all cmap class formats */
  protected TTTags.CMapFormat format = TTTags.CMapFormat.CMapUnknown;
  protected int length = 0;

  /* ==================== FTCMapClassRec ================================== */
  public FTCMapClassRec() {
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
    str.append("..format: "+format+'\n');
    str.append("..length: "+length+'\n');
    return str.toString();
  }

  /* ==================== getFormat ================================== */
  public TTTags.CMapFormat getFormat() {
    return format;
  }

  /* ==================== getLength ================================== */
  public int getLength() {
    return length;
  }

  /* ==================== initCMap ===================================== */
  public FTError.ErrorTag initCMap(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "initCMap not yet implemented");
    return error;
  }

  /* ==================== doneCMap ===================================== */
  public FTError.ErrorTag doneCMap(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "doneCMap not yet implemented");
    return error;
  }

  /* ==================== charIndex ===================================== */
  public int charIndex(FTCMapRec cmap, int ch_code) {
    Log.e(TAG, "charIndex not yet implemented");
    return -1;
  }

  /* ==================== charNext ===================================== */
  public int charNext(FTCMapRec cmap, FTReference<Integer> char_code_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "charNext not yet implemented");
    return -1;
  }


  /* ==================== charVarIndex ===================================== */
  public FTError.ErrorTag charVarIndex(FTCMapRec cmap, FTCMapRec ucmap, int char_code, int variant_selector) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "charVarIndex not yet implemented");
    return error;
  }

  /* ==================== charVarDefault ===================================== */
  public FTError.ErrorTag charVarDefault(FTCMapRec cmap, int char_code, int variant_selector) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "charVarDefault not yet implemented");
    return error;
  }

  /* ==================== variantList ===================================== */
  public int[] variantList(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "variantList not yet implemented");
    return null;
  }

  /* ==================== charVariantList ===================================== */
  public int[] charVariantList(FTCMapRec cmap, int char_code) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "charvariantList not yet implemented");
    return null;
  }

  /* ==================== variantCharList ===================================== */
  public int[] variantCharList(FTCMapRec cmap, int variant_selector) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "variantcahrList not yet implemented");
    return null;
  }

}