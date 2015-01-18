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
  /*    FTCBasicSCacheClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCBasicSCacheClass extends FTCGCacheClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicSCacheClass";

  /* ==================== FTCBasicSCacheClass ================================== */
  public FTCBasicSCacheClass() {
    oid++;
    id = oid;
    this.node_type = FTCTags.NodeType.SNODE;
    this.cache_class_type = FTCTags.ClassType.FTCBasicSCacheClass;
    this.family_class = new FTCBasicSFamilyClass();
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
    str.append("..node_type: "+node_type+"\n");
    str.append("..cache_class_type: "+cache_class_type+"\n");
    return str.toString();
  }

  /* ==================== ftc_sbit_copy_bitmap ===================================== */
  public FTError.ErrorTag ftc_sbit_copy_bitmap (FTReference<FTCSBitRec> sbit_ref, FTBitmapRec bitmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int pitch = bitmap.getPitch();
    int size;
    FTCSBitRec sbit = sbit_ref.Get();

    if (pitch < 0) {
      pitch = -pitch;
    }
    size = ( pitch * bitmap.getRows());
//    sbit.setBuffer(new byte[size]);
    sbit.setBuffer(java.util.Arrays.copyOf(bitmap.getBuffer(), size));
    sbit_ref.Set(sbit);
    return error;
  }

  /* ==================== nodeNew ===================================== */
  @Override
  public FTError.ErrorTag nodeNew(FTReference<FTCNodeRec> node_ref, Object query) {
    Log.e(TAG, "nodeNew not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//   return ftc_snode_new(node_ref, query, cache)
  }

  /* ==================== nodeWeight ===================================== */
  @Override
  public int nodeWeight(FTCMruNodeRec node) {
    Log.e(TAG, "nodeWeight not yet implemented");
    return 0;
//   return ftc_snode_weight(node)
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
//   return ftc_snode_compare(node, query, flag_ref)
  }

  /* ==================== nodeRemoveFaceid ===================================== */
  @Override
  public FTError.ErrorTag nodeRemoveFaceid(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeRemoveFaceid not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//   return ftc_basic_gnode_compare_faceid(node, query, flag_ref)
  }

  /* ==================== nodeFree ===================================== */
  @Override
  public FTError.ErrorTag nodeFree(FTCNodeRec node) {
    Log.e(TAG, "nodeFree not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//   return ftc_snode_free(node)
  }

  /* ==================== cacheInit ===================================== */
  @Override
  public FTError.ErrorTag cacheInit() {
   return ftc_gcache_init();
  }

  /* ==================== cacheDone ===================================== */
  @Override
  public FTError.ErrorTag cacheDone() {
   return ftc_gcache_done();
  }

}