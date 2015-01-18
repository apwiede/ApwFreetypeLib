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

import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

  /* ===================================================================== */
  /*    FTCMruListRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCMruListRec extends FTCMruListClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCMruListRec";

  private int num_nodes = 0;
  protected int max_nodes = 0;
  private FTCMruNodeRec nodes = null;
  protected Object data = null;

  /* ==================== FTCMruListRec ================================== */
  public FTCMruListRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== FTCMruListRec ================================== */
  public void XFTCMruListRec(FTCMruListClassRec clazz, String initStr) {
    oid++;
    id = oid;
  }
    
  /* ==================== mySelf ================================== */
  public String mySelf() {
    String str = TAG+"!"+id+"!";
    return str;
  }
        
  /* ==================== toString ===================================== */
  public String toString() {
    StringBuffer str = new StringBuffer(mySelf()+"!");
    return str.toString();
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("..num_nodes: "+num_nodes+'\n');
    str.append("..max_nodes: "+max_nodes+'\n');
    return str.toString();
  }

  /* ==================== getNum_nodes ===================================== */
  public int getNum_nodes() {
    return num_nodes;
  }

  /* ==================== setNum_nodes ===================================== */
  public void setNum_nodes(int num_nodes) {
    this.num_nodes = num_nodes;
  }

  /* ==================== getMax_nodes ===================================== */
  public int getMax_nodes() {
    return max_nodes;
  }

  /* ==================== setMax_nodes ===================================== */
  public void setMax_nodes(int max_nodes) {
    this.max_nodes = max_nodes;
  }

  /* ==================== getNodes ===================================== */
  public FTCMruNodeRec getNodes() {
    return nodes;
  }

  /* ==================== setNodes ===================================== */
  public void setNodes(FTCMruNodeRec nodes) {
    this.nodes = nodes;
  }

  /* ==================== getData ===================================== */
  public Object getData() {
    return data;
  }

  /* ==================== getData ===================================== */
  public void setData(Object data) {
    this.data = data;
  }

  /* =====================================================================
   * FTCMruListInit
   * =====================================================================
   */
  public FTError.ErrorTag FTCMruListInit(FTCMruListClassRec mru, int max_nodes, Object data) {
Log.i(TAG, "FTCMruListInit: max_nodes: "+max_nodes+"!");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    this.num_nodes = 0;
    this.max_nodes = max_nodes;
    this.nodes = null;
    this.node_type = mru.getNodeType();
    this.data = data;
    return error;
  }

  /* =====================================================================
   * FTCMruListLookup
   * =====================================================================
   */
  public FTError.ErrorTag FTCMruListLookup(Object key, FTReference<FTCMruNodeRec> node_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCMruNodeRec node;

    node = FTCMruListFind(key);
    if (node == null) {
      return FTCMruListNew(key, node_ref);
    }
    node_ref.Set(node);
    return error;
  }

  /* =====================================================================
   * FTCMruListFind
   * =====================================================================
   */
  public FTCMruNodeRec FTCMruListFind(Object key) {
    FTCMruNodeRec node;
    FTCMruNodeRec first;

Debug(0, DebugTag.DBG_CACHE, TAG, "FTCMruListFind");
    first = nodes;
    node = null;
    if (first != null) {
      node = nodes;
      do {
        if (nodeCompare(node, key)) {
          if (first != node) {
Debug(0, DebugTag.DBG_CACHE, TAG, "first: "+first+"!"+node+"!");
            nodes.FTCMruNodeUp(node);
          }
Debug(0, DebugTag.DBG_CACHE, TAG, "FTCMruListFind found: "+node+"!");
          return node;
        }
        node = nodes.getNext();
      } while (node != first);
    }
    return null;
  }

  /* =====================================================================
   * FTCMruListNew
   * =====================================================================
   */
  public FTError.ErrorTag FTCMruListNew(Object key, FTReference<FTCMruNodeRec> node_ref) {
    FTCMruNodeRec node = null;
    Object node_obj = node;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_CACHE, TAG, String.format("FTCMruListNew: num_nodes: %d max_nodes: %d", num_nodes, max_nodes));
    if (num_nodes >= max_nodes && max_nodes > 0) {
      node = nodes.getPrev();
//        FT_ASSERT( node );
      node_ref.Set(nodes);
      nodes.FTCMruNodeUp(node);
      nodes = (FTCMruNodeRec)node_ref.Get();
      error = nodeReset(node, key, data);
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      node_ref.Set(nodes);
      nodes.FTCMruNodeRemove(node_ref, node);
      nodes = (FTCMruNodeRec)node_ref.Get();
      num_nodes--;
      nodeDone(node, data);
    } else {
      switch(node_type) {
      case FACE_NODE:
         node = new FTCFaceNodeRec();
         break;
      case SIZE_NODE:
        node = new FTCSizeNodeRec();
        break;
      case GNODE:
        node = new FTCGNodeRec();
        break;
      case INODE:
        node = new FTCINodeRec();
        break;
      case SNODE:
        node = new FTCSNodeRec();
        break;
      case BASIC_FAMILY:
        node = new FTCBasicFamilyRec();
        break;
      default:
        Log.e(TAG, "bad node_type "+node_type+" in FTCMruListNew");
        break;
      }
      if (node == null) {
        node_ref.Set(node);
        return error;
      }
    }
    FTReference<FTCMruNodeRec> obj_ref = new FTReference<>();
    obj_ref.Set(node);
Debug(0, DebugTag.DBG_CACHE, TAG, "node_init: node: "+node+"!");
Debug(0, DebugTag.DBG_CACHE, TAG, "node_init2: key: "+key+"!data: "+data+"!");
    error = nodeInit(obj_ref, key, data);
    node_obj = obj_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      nodeDone((FTCMruNodeRec) node_obj, data);
//      FT_FREE( node );
      node_ref.Set(null);
      return error;
    }
Debug(0, DebugTag.DBG_CACHE, TAG, "list.nodes. " + nodes);
    obj_ref.Set(nodes);
    FTCMruNodeRec.FTCMruNodePrepend(obj_ref, (FTCMruNodeRec)node_obj);
    num_nodes++;
    node_ref.Set((FTCMruNodeRec)node_obj);
    return error;
  }

  /* =====================================================================
   * FTCMruListLookupCmp
   * =====================================================================
   */
  public void FTCMruListLookupCmp(FTReference<FTCMruListRec> list_ref, Object key, Object /*FTInstanceMethod*/ compare, FTCMruNodeRec node) {
    FTCMruListRec list = list_ref.Get();
    FTCMruNodeRec first = list.nodes;
    if (first == null);
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCMruNodeRec node, Object data) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
  }

  /* ==================== nodeInit ===================================== */
  @Override
  public FTError.ErrorTag nodeInit(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    Log.e(TAG, "nodeInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeReset ===================================== */
  @Override
  public FTError.ErrorTag nodeReset(FTCMruNodeRec node, Object key, Object data) {
    Log.e(TAG, "nodeReset not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeDone ===================================== */
  @Override
  public FTError.ErrorTag nodeDone(FTCMruNodeRec node, Object data) {
    Log.e(TAG, "nodeDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}