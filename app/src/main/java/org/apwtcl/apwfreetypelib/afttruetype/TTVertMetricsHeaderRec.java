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

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTVertMetricsHeaderRec extends FTDebug {

  private static int oid = 0;

  private int id;
  private static String TAG = "TTVertMetricsHeader";

  private int metrics_size = 0;
  private long metrics_offset = 0;

  /* ==================== TTVertMetricsHeader ================================== */
  public TTVertMetricsHeaderRec() {
    oid++;
    id = oid;
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: vmtx offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.vmtx, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    metrics_size = length_ref.Get().intValue();
    metrics_offset = stream.pos();
    return FTError.ErrorTag.ERR_OK;
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

  /* ==================== getMetricsSize ===================================== */
  public int getMetricsSize() {
    return metrics_size;
  }

  /* ==================== getMetricsOffset ===================================== */
  public long getMetricsOffset() {
    return metrics_offset;
  }

}
