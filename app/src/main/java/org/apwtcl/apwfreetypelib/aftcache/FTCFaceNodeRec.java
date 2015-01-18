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
  /*    FTCFaceNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;

public class FTCFaceNodeRec extends FTCMruNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCFaceNodeRec";

  private Object face_id = null;
  private FTFaceRec face = null;

  /* ==================== FTCFaceNodeRec ================================== */
  public FTCFaceNodeRec() {
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
    return str.toString();
  }

  /* ==================== getFace_id ================================== */
  public Object getFace_id() {
    return face_id;
  }

  /* ==================== setFace_id ================================== */
  public void setFace_id(Object face_id) {
    this.face_id = face_id;
  }

  /* ==================== getFace ================================== */
  public FTFaceRec getFace() {
    return face;
  }

  /* ==================== setFace ================================== */
  public void setFace(FTFaceRec face) {
    this.face = face;
  }

}