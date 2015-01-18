package org.apwtcl.apwfreetypelib.aftbase;

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

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTFaceRequester extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTFaceRequester";

  /* ==================== FTCSBitRec ================================== */
  public FTFaceRequester() {
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

  /* ==================== requestFace ================================== */
  public FTError.ErrorTag requestFace(Object face_id, FTLibraryRec library, Object request_data, FTReference<FTFaceRec> face_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

}
