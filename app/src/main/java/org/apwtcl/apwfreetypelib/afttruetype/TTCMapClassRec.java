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
  /*    TTCMapClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCMapClassRec;
import org.apwtcl.apwfreetypelib.aftbase.FTCMapRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class TTCMapClassRec extends FTCMapClassRec {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTCMapClassRec";

    /* ==================== TTCMapClassRec ================================== */
    public TTCMapClassRec() {
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
      String super_str = super.toDebugString();
      StringBuffer str = new StringBuffer(mySelf()+"\n");
      return super_str + str.toString();
    }

  /* ==================== validate ===================================== */
  public FTError.ErrorTag validate(TTValidatorRec valid) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "validate not yet implemented");
    return error;
  }

  /* ==================== getCmapInfo ===================================== */
  public FTError.ErrorTag getCmapInfo(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "getCMapInfo not yet implemented");
    return error;
  }

  /* ==================== initCMap ===================================== */
  @Override
  public FTError.ErrorTag initCMap(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "initCMap not yet implemented");
//    cmap.data = table;
    return error;
  }

  /* ==================== doneCMap ===================================== */
  @Override
  public FTError.ErrorTag doneCMap(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "doneCMap not yet implemented");
    return error;
  }

  /* ==================== charIndex ===================================== */
  @Override
  public int charIndex(FTCMapRec cmap, int char_code) {
    Log.e(TAG, "charIndex not yet implemented");
    return -1;
  }

  /* ==================== charNext ===================================== */
  @Override
  public int charNext(FTCMapRec cmap, FTReference<Integer> char_code_ref) {
    Log.e(TAG, "charNext not yet implemented");
    return -1;
  }

  /* ==================== charVarIndex ===================================== */
  @Override
  public FTError.ErrorTag charVarIndex(FTCMapRec cmap, FTCMapRec ucmap, int char_code, int variant_selector) {
    Log.e(TAG, "charVarIndex not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== charVarDefault ===================================== */
  @Override
  public FTError.ErrorTag charVarDefault(FTCMapRec cmap, int char_code, int variant_selector) {
    Log.e(TAG, "charVarDefault not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== variantList ===================================== */
  @Override
  public int[] variantList(FTCMapRec cmap) {
    Log.e(TAG, "variantList not yet implemented");
    return null;
  }

  /* ==================== charVariantList ===================================== */
  @Override
  public int[] charVariantList(FTCMapRec cmap, int char_code) {
    Log.e(TAG, "charVariantList not yet implemented");
    return null;
  }

  /* ==================== variantCharList ===================================== */
  @Override
  public int[] variantCharList(FTCMapRec cmap, int variant_selector) {
    Log.e(TAG, "variantCharList not yet implemented");
    return null;
  }

}