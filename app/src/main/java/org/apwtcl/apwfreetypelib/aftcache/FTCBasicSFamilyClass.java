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

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCBasicSFamilyClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCBasicSFamilyClass extends FTCSFamilyClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicSFamilyClass";

  /* ==================== FTCBasicSFamilyClass ================================== */
  public FTCBasicSFamilyClass() {
    oid++;
    id = oid;
    String class_name = "org.apwtcl.gles20.truetype.FTCCacheFuncs";
    this.node_type = FTCTags.NodeType.BASIC_FAMILY;
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
  @Override
  public FTError.ErrorTag familyGetCount() {
    Log.e(TAG, "familyGetCount not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ftc_basic_family_get_count();
  }

  /* ==================== familyLoadGlyp ================================== */
  @Override
  public FTError.ErrorTag familyLoadGlyph() {
    Log.e(TAG, "familyGetCount not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ftc_basic_family_load_bitmap();
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCMruNodeRec node, Object data) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
//    return ftc_basic_family_compare(node, data);
  }

  /* ==================== nodeInit ===================================== */
  @Override
  public FTError.ErrorTag nodeInit(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    Log.e(TAG, "nodeInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return ftc_basic_family_init(obj_ref, key, data);
  }

  /* ==================== nodeReset ===================================== */
  @Override
  public FTError.ErrorTag nodeReset(FTCMruNodeRec node, Object key, Object data) {
    // nothing to do
    Log.i(TAG, "nodeReset");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeDone ===================================== */
  public FTError.ErrorTag nodeDone(FTCMruNodeRec node, Object data) {
    // nothing to do
    Log.i(TAG, "nodeDone");
    return FTError.ErrorTag.ERR_OK;
  }

}