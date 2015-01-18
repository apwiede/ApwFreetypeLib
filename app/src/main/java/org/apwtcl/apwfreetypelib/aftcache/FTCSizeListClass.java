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
  /*    FTCSizeListClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCSizeListClass extends FTCMruListRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCSizeListClass";

  /* ==================== FTCSizeListClass ================================== */
  public FTCSizeListClass(int max_nodes, Object data) {
    oid++;
    id = oid;

    this.max_nodes = max_nodes;
    this.node_type = FTCTags.NodeType.SIZE_NODE;
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
   *    ftc_size_node_compare
   *
   * =====================================================================
   */
  public boolean ftc_size_node_compare(FTCMruNodeRec node, Object data) {
Debug(0, DebugTag.DBG_CACHE, TAG, "ftc_size_node_compare");
    FTCScalerRec scaler = (FTCScalerRec)data;
    FTCScalerRec scaler0 = ((FTCSizeNodeRec)node).getScaler();

    if (scaler0.getFace_id() == scaler.getFace_id() &&
        scaler0.getWidth() == scaler.getWidth() &&
        scaler0.getHeight() == scaler.getHeight() &&
        (scaler0.getPixel() != 0) == (scaler.getPixel() != 0) &&
        (scaler0.getPixel() != 0 ||
            (scaler0.getX_res() == scaler.getX_res() && scaler0.getY_res() == scaler.getY_res()))) {
      ((FTCSizeNodeRec)node).getSize().ActivateSize();
      return true;
    }
    return false;
  }

  /* =====================================================================
   *    ftc_size_node_init
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_size_node_init(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    FTCSizeNodeRec size_node = (FTCSizeNodeRec)obj_ref.Get();
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_CACHE, TAG, "ftc_size_node_init");
    size_node.setScaler((FTCScalerRec)key);
    FTReference<FTSizeRec> size_ref = new FTReference<FTSizeRec>();
    error = ((FTCScalerRec)key).ftc_scaler_lookup_size((FTCManagerRec)data, (FTCScalerRec)key, size_ref);
    size_node.setSize(size_ref.Get());
    obj_ref.Set(size_node);
    return error;
  }

  /* =====================================================================
   *    ftc_size_node_reset
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_size_node_reset(FTCMruNodeRec node, Object key, Object data) {
    Log.w(TAG, "ftc_size_node_reset not yet implemented");

    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ftc_size_node_done
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_size_node_done(FTCMruNodeRec node, Object data) {
    Log.w(TAG, "ftc_size_node_done not yet implemented");

    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCMruNodeRec node, Object data) {
    return ftc_size_node_compare(node, data);
  }

  /* ==================== nodeInit ===================================== */
  @Override
  public FTError.ErrorTag nodeInit(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    return ftc_size_node_init(obj_ref, key, data);
  }

  /* ==================== nodeReset ===================================== */
  @Override
  public FTError.ErrorTag nodeReset(FTCMruNodeRec node, Object key, Object data) {
    return ftc_size_node_reset(node, key, data);
  }

  /* ==================== nodeDone ===================================== */
  @Override
  public FTError.ErrorTag nodeDone(FTCMruNodeRec node, Object data) {
    return ftc_size_node_done(node, data);
  }

}