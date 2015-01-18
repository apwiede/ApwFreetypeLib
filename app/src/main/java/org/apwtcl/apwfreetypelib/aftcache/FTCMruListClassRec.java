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
  /*    FTCMruListClassRec                                                 */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCMruListClassRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCMruListClassRec";

  protected FTCTags.NodeType node_type = FTCTags.NodeType.UNKNOWN;

  /* ==================== FTCMruListClassRec ================================== */
  public FTCMruListClassRec() {
    super();
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
    str.append("..node_type: "+node_type+'\n');
    return str.toString();
  }

  /* ==================== getNodeType ===================================== */
  public FTCTags.NodeType getNodeType() {
    return node_type;
  }

  /* ==================== setNodeType ===================================== */
  public void setNodeType(FTCTags.NodeType node_type) {
    this.node_type = node_type;
  }

  /* ==================== nodeCompare ===================================== */
  public boolean nodeCompare(FTCMruNodeRec node, Object data) {
    Log.e(TAG, "nodeCompare not yet implemented");
    return true;
  }

  /* ==================== nodeInit ===================================== */
  public FTError.ErrorTag nodeInit(FTReference<FTCMruNodeRec> obj_ref, Object key, Object data) {
    Log.e(TAG, "nodeInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeReset ===================================== */
  public FTError.ErrorTag nodeReset(FTCMruNodeRec node, Object key, Object data) {
    Log.e(TAG, "nodeReset not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== nodeDone ===================================== */
  public FTError.ErrorTag nodeDone(FTCMruNodeRec node, Object data) {
    Log.e(TAG, "nodeDone not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}