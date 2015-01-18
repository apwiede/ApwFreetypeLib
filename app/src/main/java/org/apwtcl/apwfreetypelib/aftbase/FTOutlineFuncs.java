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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTOutlineFuncs                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTOutlineFuncs extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTOutlineFuncs";

    protected int shift = 0;
    protected int delta = 0;

    /* ==================== FTOutlineFuncs ================================== */
    public FTOutlineFuncs() {
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

  /* ==================== moveTo ===================================== */
  public FTError.ErrorTag moveTo(FTVectorRec point, Object user) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "moveTo not yet implemented");
    return error;
  }

  /* ==================== lineTo ===================================== */
  public FTError.ErrorTag lineTo(FTVectorRec point, Object user) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "lineTo not yet implemented");
    return error;
  }

  /* ==================== conicTo ===================================== */
  public FTError.ErrorTag conicTo(FTVectorRec control, FTVectorRec point, Object user) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "conicTo not yet implemented");
    return error;
  }

  /* ==================== cubicTo ===================================== */
  public FTError.ErrorTag cubicTo(FTVectorRec control1, FTVectorRec control2, FTVectorRec point, Object user) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "cubicTo not yet implemented");
    return error;
  }

  /* ==================== getShift ===================================== */
  public int getShift() {
    return shift;
  }

  /* ==================== getDelta ===================================== */
  public int getDelta() {
    return delta;
  }

}