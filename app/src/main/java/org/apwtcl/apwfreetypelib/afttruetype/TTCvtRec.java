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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTCvtRec                                                           */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType cvt table.  All fields        */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTCvtRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCvt";

  private int cvt_size = 0;
  private short[] cvt = null;

  /* ==================== TTCvtRec ================================== */
  public TTCvtRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: cvt offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.cvt, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      FTTrace.Trace(7, TAG, "is missing");
      cvt_size = 0;
      cvt = null;
      error = FTError.ErrorTag.ERR_OK;
      return error;
    }
    length = length_ref.Get();
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, String.format("pos: %d length: %d", stream.pos(), length));

    cvt_size = length / 2;
    cvt = new short[cvt_size];
    for (int i = 0; i < cvt_size; i++) {
      cvt[i] = (short)stream.readShort();
    }
    FTTrace.Trace(7, TAG, "loaded");
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, toDebugString());
    return error;
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
    str.append("..cvt_size: "+cvt_size+'\n');
    return str.toString();
  }

  /* ==================== getCvtSize ================================== */
  public int getCvtSize() {
    return cvt_size;
  }

  /* ==================== getCvt ================================== */
  public short[] getCvt() {
    return cvt;
  }

  /* ==================== getCvtValue ================================== */
  public short getCvtValue(int idx) {
    return cvt[idx];
  }

}
