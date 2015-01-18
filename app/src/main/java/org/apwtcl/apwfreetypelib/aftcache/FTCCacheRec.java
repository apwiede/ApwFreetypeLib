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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
  /* ===================================================================== */
  /*    FTCCacheRec                                                        */
  /*                                                                       */
  /* each cache really implements a dynamic hash table to manage its nodes */
  /* ===================================================================== */

public class FTCCacheRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCCacheRec";

  public final static int FTC_HASH_INITIAL_SIZE = 8;
  public final static int FTC_HASH_MIN_LOAD = 1;
  public final static int FTC_HASH_MAX_LOAD = 2;
  public final static int FTC_HASH_SUB_LOAD = (FTC_HASH_MAX_LOAD - FTC_HASH_MIN_LOAD);

  private int p = 0;
  private int mask = 0;
  private long slack = 0L;
  private FTCNodeRec[] buckets = null;
//  private FTCCacheClassRec clazz = null;     /* local copy, for speed  */
  private FTCManagerRec manager = null;
  private int index = 0;                     /* in manager's table     */
//  private FTCCacheClassRec org_class = null; /* original class pointer */
  protected FTCTags.NodeType node_type = FTCTags.NodeType.GNODE;
  protected FTCTags.ClassType cache_class_type = FTCTags.ClassType.UNKNOWN;

  /* ==================== FTCCacheRec ================================== */
  public FTCCacheRec() {
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
    str.append(super.toString());
    str.append("    p: "+p+"\n");
    str.append("    mask: "+mask+"\n");
    str.append("    slack: "+slack+"\n");
//    str.append("    clazz: "+clazz.toDebugString()+"\n");
    str.append("    manager: "+manager.toDebugString()+"\n");
    str.append("    index: "+index+"\n");
//    str.append("    org_class: "+org_class.toDebugString()+"\n");
    return str.toString();
  }

  /* ==================== getP ================================== */
  public int getP() {
    return p;
  }

  /* ==================== setP ================================== */
  public void setP(int p) {
    this.p = p;
  }

  /* ==================== getMask ================================== */
  public int getMask() {
    return mask;
  }

  /* ==================== setMask ================================== */
  public void setMask(int mask) {
    this.mask = mask;
  }

  /* ==================== getSlack ================================== */
  public long getSlack() {
    return slack;
  }

  /* ==================== setSlack ================================== */
  public void setSlack(long slack) {
    this.slack = slack;
  }

  /* ==================== getBuckets ================================== */
  public FTCNodeRec[] getBuckets() {
    return buckets;
  }

  /* ==================== setBuckets ================================== */
  public void setBuckets(FTCNodeRec[] buckets) {
    this.buckets = buckets;
  }

//  /* ==================== getClazz ================================== */
//  public FTCCacheClassRec getClazz() {
//    return clazz;
//  }

  /* ==================== setClazz ================================== */
//  public void setClazz(FTCCacheClassRec clazz) {
//    this.clazz = clazz;
//  }

  /* ==================== getManager ================================== */
  public FTCManagerRec getManager() {
    return manager;
  }

  /* ==================== setManager ================================== */
  public void setManager(FTCManagerRec manager) {
    this.manager = manager;
  }

  /* ==================== getIndex ================================== */
  public int getIndex() {
    return index;
  }

  /* ==================== setIndex ================================== */
  public void setIndex(int index) {
    this.index = index;
  }

  /* ==================== getOrg_class ================================== */
//  public FTCCacheClassRec getOrg_class() {
//    return org_class;
//  }

  /* ==================== setOrg_class ================================== */
//  public void setOrg_class(FTCCacheClassRec org_class) {
//    this.org_class = org_class;
//  }

  /* =====================================================================
   * ftc_get_top_node_for_hash
   * =====================================================================
   */
  /* get a top bucket for specified hash from cache,
   * body for FTC_NODE__TOP_FOR_HASH( cache, hash )
   */
  public int ftc_get_top_node_for_hash(Object hash) {
    int idx;

    idx =((int)hash & mask);
    if (idx < p) {
      idx = ((int)hash & (2 * mask + 1));
    }
    return idx;
  }

  /* =====================================================================
   * ftc_cache_resize
   * =====================================================================
   */
  public void ftc_cache_resize() {
    for (;;) {
      FTCNodeRec node;
      FTCNodeRec pnode;
      FTCNodeRec linkNode = null;
      int pnodeIdx;
      int linkIdx;
      int count = mask + p + 1;    /* number of buckets */

        /* do we need to shrink the buckets array? */
      if (slack < 0) {
        FTCNodeRec new_list = null;

          /* try to expand the buckets array _before_ splitting
           * the bucket lists
           */
        if (p >= mask) {
            /* if we can't expand the array, leave immediately */
          Log.e(TAG, "FT_RENEW_ARRAY not yet implemented");
          return;
//            if (FT_RENEW_ARRAY(cache.buckets,
//                                 (mask + 1) * 2, (mask + 1) * 4)) {
//              break;
//            }
        }
          /* split a single bucket */
        pnode = buckets[p];
        linkIdx = p;
        for (;;) {
          node = pnode;
          if (node == null) {
            break;
          }
          if (((int)node.hash & (mask + 1)) != 0) {
            linkNode = pnode;
            pnode = node.link;
            node.link = new_list;
            new_list = node;
          } else {
            linkNode = pnode;
            pnode = node.link;
          }
        }
        buckets[p + mask + 1] = new_list;
        slack += FTC_HASH_MAX_LOAD;
        if (p >= mask) {
          mask = 2 * mask + 1;
          p    = 0;
        } else {
          p = p + 1;
        }
      } else {
          /* do we need to expand the buckets array? */
        if (slack > (long)count * FTC_HASH_SUB_LOAD) {
          int old_index = p + mask;
          FTCNodeRec pold;

          if (old_index + 1 <= FTC_HASH_INITIAL_SIZE) {
            break;
          }
          if (p == 0) {
            int error;

              /* if we can't shrink the array, leave immediately */
            Log.e(TAG, "FT_RENEW_ARRAY not yet implemented");
            return;
//              if (FT_RENEW_ARRAY(cache.buckets,
//                                 (mask + 1) * 2, mask + 1)) {
//                break;
//              }
//              cache.mask >>= 1;
//              p = cache.mask;
          } else {
            p--;
          }
          linkNode = null;
          linkIdx = p;
          pnode = buckets[p];
          while (pnode != null) {
            linkNode = pnode;
            pnode = pnode.link;
          }
          pold = buckets[old_index];
          if (linkNode != null) {
            linkNode.link = pold;
          } else {
            buckets[linkIdx] = pold;
          }
          pold = null;
          slack -= FTC_HASH_MAX_LOAD;
//          cache.p = p;
        } else {
            /* otherwise, the hash table is balanced */
          break;
        }
      }
    }
  }



  /* =====================================================================
   * FTCCacheInit
   * =====================================================================
   */
  public FTError.ErrorTag FTCCacheInit() {
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

      p = 0;
      mask = FTC_HASH_INITIAL_SIZE - 1;
      slack = FTC_HASH_INITIAL_SIZE * FTC_HASH_MAX_LOAD;
      buckets = new FTCNodeRec[FTC_HASH_INITIAL_SIZE * 2];
      return error;
  }

   /* =====================================================================
    * FTCCacheLookup
    * =====================================================================
    */
  public FTError.ErrorTag FTCCacheLookup(Object hash, Object query, FTReference<FTCNodeRec> node_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCNodeRec bucket;
    FTCNodeRec pnode;
    FTCNodeRec node = node_ref.Get();
    int pnodeIdx= -1;
    boolean list_changed = false;
    boolean isLinkNode = false;

Debug(0, DebugTag.DBG_CACHE, TAG, "FTCCacheLookup");
   if ( node_ref == null) {
     return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
   }
   /* Go to the `top' node of the list sharing same masked hash */
   pnodeIdx = ftc_get_top_node_for_hash(hash);
   pnode = buckets[pnodeIdx];
   bucket = pnode;
     
   /* Lookup a node with exactly same hash and queried properties.  */
   /* NOTE: _nodcomp() may change the linked list to reduce memory. */
   for (;;) {
     if (isLinkNode) {
       node = pnode.link;
     } else {
       node = pnode;
     }
     if (node == null) {
       return FTCCacheNewNode(hash, query, node_ref);
     }
     FTReference<Boolean> flag_ref = new FTReference<Boolean>();
     flag_ref.Set(list_changed);
     if (node.hash == (int)hash && nodeCompare(node, query, flag_ref)) {
       list_changed = flag_ref.Get();
       break;
     }
     isLinkNode = true;
     pnode = node;
   }
   if (list_changed) {
     /* Update bucket by modified linked list */
     pnodeIdx = ftc_get_top_node_for_hash(hash);
     pnode = buckets[pnodeIdx];
     bucket = pnode;
     isLinkNode = false;
     /* Update pnode by modified linked list */
     while (pnode != node) {
       if ((isLinkNode ? pnode.link : pnode) == null) {
         FTTrace.Trace(7, TAG, "FTC_Cache_Lookup: oops!!!  node missing");
         return FTCCacheNewNode(hash, query, node_ref);
       } else {
         isLinkNode = true;
         pnode = pnode.link;
       }
     }
   }
   /* Reorder the list to move the found node to the `top' */
   if (node != bucket) {
     isLinkNode = true;
     pnode = node;
     node.link = bucket;
     bucket = node;
   }
   /* move to head of MRU list */
   {
     if (node != manager.getNodes_list()) {
       manager.getNodes_list().FTCMruNodeUp(node);
     }
   }
   node_ref.Set(node);
   return error;
 }

 /* =====================================================================
  * FTCCacheNewNode
  * =====================================================================
  */
 public FTError.ErrorTag FTCCacheNewNode(Object hash, Object query, FTReference<FTCNodeRec> node_ref) {
   FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
   FTCNodeRec node;
Debug(0, DebugTag.DBG_CACHE, TAG, "FTCCacheNewNode");
   /*
    * We use the FTC_CACHE_TRYLOOP macros to support out-of-memory
    * errors (OOM) correctly, i.e., by flushing the cache progressively
    * in order to make more room.
    */

// FIXME to be implemented later !!
//      FTC_CACHE_TRYLOOP( cache )
   {
     FTReference<FTCCacheRec> cache_ref = new FTReference<FTCCacheRec>();
     cache_ref.Set(this);
     error = nodeNew(node_ref, query);
     node = node_ref.Get();
   }
//      FTC_CACHE_TRYLOOP_END( NULL );
   if (error != FTError.ErrorTag.ERR_OK) {
     node = null;
   } else {
    /* don't assume that the cache has the same number of buckets, since
     * our allocation request might have triggered global cache flushing
     */
     node_ref.Set(node);
     ftc_cache_add((Object)hash, node_ref);
   }
   node_ref.Set(node);
   return error;
 }

  /* =====================================================================
   *    ftc_cache_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_cache_done() {
    Log.w(TAG, "ftc_cache_done not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   * ftc_cache_add
   * =====================================================================
   */
  public FTError.ErrorTag ftc_cache_add(Object hash, FTReference<FTCNodeRec> node_ref) {
    FTCNodeRec node = node_ref.Get();
    node.hash = (Integer)hash;
    node.cache_index = index;
    node.ref_count = 0;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    ftc_node_hash_link(node);
    FTReference<FTCMruNodeRec> nodes_list_ref = new FTReference<FTCMruNodeRec>();
    nodes_list_ref.Set(manager.getNodes_list());
    FTCMruNodeRec.FTCMruNodePrepend(nodes_list_ref, node);
    manager.setNodes_list((FTCNodeRec)nodes_list_ref.Get());
    manager.setNum_nodes(manager.getNum_nodes() + 1);
    {
      int val = nodeWeight(node);
      manager.setCur_weight(manager.getCur_weight() + val);

      if (manager.getCur_weight() >= manager.getMax_weight()) {
        node.ref_count++;
        manager.Compress();
        node.ref_count--;
      }
    }
    return error;
  }

  /* =====================================================================
   * ftc_node_hash_link
   * =====================================================================
   */
  public void ftc_node_hash_link(FTCNodeRec node) {
    int idx = ftc_get_top_node_for_hash(node.hash);

    node.link = buckets[idx];
    buckets[idx] = node;
    slack--;
    ftc_cache_resize();
  }

  /* ==================== nodeNew ===================================== */
  public FTError.ErrorTag nodeNew(FTReference<FTCNodeRec> node_ref, Object query) {
    Log.e(TAG, "nodeNew not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeWeight ===================================== */
  public int nodeWeight(FTCMruNodeRec node) {
    Log.e(TAG, "nodeWeight not yet implemented");
    return 0;
  }

  /* ==================== nodeCompare ===================================== */
  public boolean nodeCompare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
  }

  /* ==================== nodeRemoveFaceid ===================================== */
  public FTError.ErrorTag nodeRemoveFaceid(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.e(TAG, "nodeRemoveFaceid not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeFree ===================================== */
  public FTError.ErrorTag nodeFree(FTCNodeRec node) {
    Log.e(TAG, "nodeFree not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== cacheInit ===================================== */
  public FTError.ErrorTag cacheInit() {
    Log.e(TAG, "cacheInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== cacheDone ===================================== */
  public FTError.ErrorTag cacheDone() {
    Log.e(TAG, "cacheDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}