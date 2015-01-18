package org.apwtcl.apwfreetypelib.aftttinterpreter;

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

  /* ===================================================================== */
  /*    TTCvtFuncStretched                                                 */
  /*   set of Cvt functions for interpreter                                */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class TTCvtStretchedFunc extends TTCvtFuncBase {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCvtFuncStretched";

  /* ==================== TTCvtFuncStretched ================================== */
  public TTCvtStretchedFunc()
  {
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

  /* =====================================================================
   * ReadCVTStretched
   *
   * =====================================================================
   */
  /* ==================== readCvt ===================================== */
  @Override
  public int readCvt(int idx) {
    Log.e(TAG, "readCvt not yet implemented");
    return -1;
  }

  /* =====================================================================
   * WriteCVTStretched
   *
   * =====================================================================
   */
  /* ==================== writeCvt ===================================== */
  @Override
  public void writeCvt(int idx, int value) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "writeCvt not yet implemented");
  }

  /* =====================================================================
   * MoveCVTStretched
   *
   * =====================================================================
   */
  /* ==================== moveCvt ===================================== */
  @Override
  public FTError.ErrorTag moveCvt(int val1, int val2) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Log.e(TAG, "moveCvt not yet implemented");
    return error;
  }

}