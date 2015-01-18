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
  /*    FTCGCacheClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCGCacheClassRec extends FTCCacheRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCGCacheClassRec";

  public FTCMruListRec families = null;
  public FTCMruListClassRec family_class = null;

  /* ==================== FTCGCacheClassRec ================================== */
  public FTCGCacheClassRec() {
    oid++;
    id = oid;

    families = new FTCMruListRec();
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

  /* ==================== getFamilies ===================================== */
  public FTCMruListRec getFamilies() {
    return families;
  }

  /* ==================== setFamilies ===================================== */
  public void setFamilies(FTCMruListRec families) {
    this.families = families;
  }

  /* =====================================================================
   *    ftc_gcache_init
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_gcache_init() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_CACHE, TAG, "ftc_gcache_init: ");
    error = FTCCacheInit();
    if (error == FTError.ErrorTag.ERR_OK) {
      families.setMax_nodes(0);  /* no maximum here! */
      families.setData(this);
      if (family_class == null) {
        /* that is for FTCCMapCacheClass etc. */
        families.setNodeType(FTCTags.NodeType.GNODE);
      } else {
        families.setNodeType(family_class.getNodeType());
      }
    }
    return error;
  }

  /* =====================================================================
   *    ftc_gcache_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_gcache_done() {
    Log.e(TAG, "ftc_gcache_done not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   * FTCGCacheNew
   * =====================================================================
   */
  public FTError.ErrorTag GCacheNew(FTCManagerRec manager, FTCGCacheClassRec clazz, FTCGCacheClassRec cache) {
    return manager.RegisterCache((FTCGCacheClassRec)this);
  }

  /* =====================================================================
   * FTCGCacheLookup
   * =====================================================================
   */
  public FTError.ErrorTag FTCGCacheLookup(int hash, int gindex,
                                          FTCGQueryRec query, FTReference<FTCNodeRec> node_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_CACHE, TAG, "FTCGCacheLookup");
    query.setGindex(gindex);
    FTReference<FTCMruNodeRec> family_ref = new FTReference<>();
Debug(0, DebugTag.DBG_CACHE, TAG, "FTCGCacheLookup2: "+families+"!"+families.getData());
    family_ref.Set(query.getFamily());
    error = families.FTCMruListLookup((Object)query, family_ref);
    query.setFamily((FTCFamilyRec)family_ref.Get());
    if (error == FTError.ErrorTag.ERR_OK) {
        /* prevent the family from being destroyed too early when an        */
        /* out-of-memory condition occurs during glyph node initialization. */
      query.getFamily().setNum_nodes(query.getFamily().getNum_nodes()+1);
      error = FTCCacheLookup(hash, query, node_ref);
      query.getFamily().setNum_nodes(query.getFamily().getNum_nodes()-1);
      if (query.getFamily().getNum_nodes() == 0) {
//          FTC_FAMILY_FREE( family, cache );
      }
    }
    return error;
  }

  /* ==================== nodeNew ===================================== */
  @Override
  public FTError.ErrorTag nodeNew(FTReference<FTCNodeRec> node_ref, Object query) {
    Log.e(TAG, "nodeNew not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeWeight ===================================== */
  @Override
  public int nodeWeight(FTCMruNodeRec node) {
    Log.e(TAG, "nodeWeight not yet implemented");
    return 0;
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
  }

  /* ==================== nodeRemoveFaceid ===================================== */
  @Override
  public FTError.ErrorTag nodeRemoveFaceid(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeRemoveFaceid not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeFree ===================================== */
  @Override
  public FTError.ErrorTag nodeFree(FTCNodeRec node) {
    Log.e(TAG, "nodeFree not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== cacheInit ===================================== */
  @Override
  public FTError.ErrorTag cacheInit() {
    Log.e(TAG, "cacheInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== cacheDone ===================================== */
  @Override
  public FTError.ErrorTag cacheDone() {
    Log.e(TAG, "cacheDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}