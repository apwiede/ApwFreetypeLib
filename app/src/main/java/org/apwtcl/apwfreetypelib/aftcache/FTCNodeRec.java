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
  /* Each cache controls one or more cache nodes.  Each node is part of    */
  /* the global_lru list of the manager.  Its `data' field however is used */
  /* as a reference count for now.                                         */
  /*                                                                       */
  /* A node can be anything, depending on the type of information held by  */
  /* the cache.  It can be an individual glyph image, a set of bitmaps     */
  /* glyphs for a given size, some metrics, etc.                           */
  /*                                                                       */
  /* ===================================================================== */

  /* ===================================================================== */
  /*    FTCNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTCNodeRec extends FTCMruNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCNodeRec";

  private final static int MAX_NODE_RECS = 1024;

  protected FTCNodeRec link = null;   /* used for hashing                   */
  protected int hash = 0;             /* used for hashing too               */
  protected int cache_index = 0;    /* index of cache the node belongs to */
  protected int ref_count = 0;      /* reference count for this node      */

  /* ==================== FTCNodeRec ================================== */
  public FTCNodeRec() {
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
    str.append("..link: "+link+'\n');
    str.append("..hash: "+hash+'\n');
    str.append("..cache_index: "+cache_index+'\n');
    str.append("..ref_count: "+ref_count+'\n');
    return str.toString();
  }

  /* =====================================================================
   * ftc_node_hash_unlink
   * =====================================================================
   */
    /* remove a node from its cache's hash table */
  private void ftc_node_hash_unlink(FTCNodeRec node0, FTCCacheRec cache) {
    int node0Idx = cache.ftc_get_top_node_for_hash(node0.hash);
    FTCNodeRec pnode = cache.getBuckets()[node0Idx];
    FTCNodeRec linkNode = null;

    for (;;) {
      FTCNodeRec node;
      node = pnode;

      if (node == null) {
        FTTrace.Trace(7, TAG, "ftc_node_hash_unlink: unknown node");
        return;
      }
      if (node == node0) {
        break;
      }
      linkNode = pnode;
      pnode = pnode.link;
    }
    if (linkNode == null) {
      cache.getBuckets()[node0Idx] = cache.getBuckets()[node0Idx].link;
    } else {
      linkNode.link = cache.getBuckets()[node0Idx].link;
    }
    node0.link = null;
    cache.setSlack(cache.getSlack() +1);
    cache.ftc_cache_resize();
  }

  /* =====================================================================
   * ftc_node_destroy
   * =====================================================================
   */
  public void ftc_node_destroy(FTCManagerRec manager) {
    FTCCacheRec cache;

      /* find node's cache */
    if (cache_index >= manager.getNum_caches()) {
      FTTrace.Trace(7, TAG, "ftc_node_destroy: invalid node handle");
      return;
    }
    cache = manager.getCaches()[cache_index];
    if (cache == null) {
      FTTrace.Trace(7, TAG, "ftc_node_destroy: invalid node handle");
      return;
    }
    manager.setCur_weight(manager.getCur_weight() - cache.nodeWeight(this));
      /* remove node from mru list */
    FTReference<FTCMruNodeRec> nodes_ref = new FTReference<>();
    nodes_ref.Set(manager.getNodes_list());
    FTCMruNodeRemove(nodes_ref, this);
      /* remove node from cache's hash table */
    ftc_node_hash_unlink(this, cache);
      /* now finalize it */
    cache.nodeFree(this);
  }
}