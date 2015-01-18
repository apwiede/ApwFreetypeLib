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
  /*    FTCFaceListClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCFaceListClass extends FTCMruListRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCFaceListClass";

  /* ==================== FTCFaceListClass ================================== */
  public FTCFaceListClass(int max_nodes, Object data) {
    oid++;
    id = oid;

    this.max_nodes = max_nodes;
    this.node_type = FTCTags.NodeType.FACE_NODE;
    this.data = data;
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

  /* =====================================================================
   *    ftc_face_node_compare
   *
   * =====================================================================
   */
  public boolean ftc_face_node_compare(FTCMruNodeRec node, Object face_id) {
Debug(0, DebugTag.DBG_CACHE, TAG, "ftc_face_node_compare");

    return ((FTCFaceNodeRec)node).getFace_id() == face_id;
  }

  /* =====================================================================
   *    ftc_face_node_init
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_face_node_init(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCFaceNodeRec node = (FTCFaceNodeRec)obj_ref.Get();
    FTCManagerRec manager = (FTCManagerRec)data;
    Object face_id = key;

    node.setFace_id(face_id);
    FTReference<FTFaceRec> face_ref = new FTReference<FTFaceRec>();
    face_ref.Set(node.getFace());
    error = manager.getRequest_face().requestFace(face_id, manager.getLibrary(), manager.getRequest_data(), face_ref);
    node.setFace(face_ref.Get());
    if (error != FTError.ErrorTag.ERR_OK) {
        /* destroy initial size object; it will be re-created later */
      if (node.getFace().getSize() != null) {
//          FTDoneSize(node.face.size);
      }
    }
    obj_ref.Set(node);
    return error;
  }

  /* =====================================================================
   *    ftc_face_node_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_face_node_done(FTCMruNodeRec node, Object data) {
    Log.w(TAG, "ftc_face_node_done not yet implemented");

    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }


  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCMruNodeRec node, Object data) {
  return ftc_face_node_compare(node, data);
  }

  /* ==================== nodeInit ===================================== */
  @Override
  public FTError.ErrorTag nodeInit(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    return ftc_face_node_init(obj_ref, key, data);
  }

  /* ==================== nodeReset ===================================== */
  @Override
  public FTError.ErrorTag nodeReset(FTCMruNodeRec node, Object key, Object data) {
    // nothing to do
    Log.e(TAG, "nodeReset");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeDone ===================================== */
  @Override
  public FTError.ErrorTag nodeDone(FTCMruNodeRec node, Object data) {
    return ftc_face_node_done(node, data);
  }

}