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
  /*    TTCMap2Class                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCMapRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class TTCMap2Class extends TTCMapClassRec {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTCMap2Class";

    /* ==================== TTCMap2Class ================================== */
    public TTCMap2Class() {
      super();
      oid++;
      id = oid;

      format = TTTags.CMapFormat.CMap2;
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

  /* ==================== validate ===================================== */
  public FTError.ErrorTag validate() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "validate not yet implemented");
//    return tt_cmap0_validate();
    return error;
  }

  /* ==================== getCmapInfo ===================================== */
  @Override
  public FTError.ErrorTag getCmapInfo(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "validate not yet implemented");
//    return tt_cmap0_get_info();
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

}
