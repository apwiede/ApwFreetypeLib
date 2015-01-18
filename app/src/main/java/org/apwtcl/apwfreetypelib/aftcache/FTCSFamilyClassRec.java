/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri dot) de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCSFamilyClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class FTCSFamilyClassRec extends FTCMruListClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCSFamilyClassRec";

  /* ==================== FTCSFamilyClassRec ================================== */
  public FTCSFamilyClassRec() {
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

  /* ==================== familyGetCount ================================== */
  public FTError.ErrorTag familyGetCount() {
    Log.e(TAG, "familyGetCount not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== familyLoadGlyp ================================== */
  public FTError.ErrorTag familyLoadGlyph() {
    Log.e(TAG, "familyGetCount not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}