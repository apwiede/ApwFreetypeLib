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
  /*    FTCBasicICacheClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCBasicICacheClass extends FTCGCacheClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCBasicICacheClass";

  /* ==================== FTCBasicICacheClass ================================== */
  public FTCBasicICacheClass() {
    oid++;
    id = oid;
    this.node_type = FTCTags.NodeType.INODE;
    this.cache_class_type = FTCTags.ClassType.FTCBasicICacheClass;
    this.family_class = new FTCBasicIFamilyClass();
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

  /* ==================== nodeNew ===================================== */
  @Override
  public FTError.ErrorTag nodeNew(FTReference<FTCNodeRec> node_ref, Object query) {
    Log.e(TAG, "nodeNew not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//   return ftc_inode_new(node_ref, query, cache)
  }

  /* ==================== nodeWeight ===================================== */
  @Override
  public int nodeWeight(FTCMruNodeRec node) {
    Log.e(TAG, "nodeWeight not yet implemented");
    return 0;
//   return ftc_inode_weight(node)
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
//   return ftc_gnode_compare(node, query, flag_ref)
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
//   return ftc_inode_free(node)
  }

  /* ==================== cacheInit ===================================== */
  @Override
  public FTError.ErrorTag cacheInit() {
   return ftc_gcache_init();
  }

  /* ==================== cacheDone ===================================== */
  @Override
  public FTError.ErrorTag cacheDone() {
    Log.e(TAG, "cacheDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//   return ftc_gcache_done(cache)
  }

}