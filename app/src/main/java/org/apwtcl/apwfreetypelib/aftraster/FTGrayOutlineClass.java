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

package org.apwtcl.apwfreetypelib.aftraster;

  /* ===================================================================== */
  /*    RasterUtil.                                                         */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTGrayOutlineClass extends FTOutlineRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTGrayOutlineClass";

  public static int PIXEL_BITS = 8;

  /* ==================== FTGrayOutlineClass ================================== */
  public FTGrayOutlineClass() {
    oid++;
    id = oid;
    shift = 0;
    delta = 0;
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
  @Override
  public FTError.ErrorTag moveTo(FTVectorRec point, Object user) {
    return ((grayTWorkerRec)user).gray_move_to(point);
  }

  /* ==================== lineTo ===================================== */
  @Override
  public FTError.ErrorTag lineTo(FTVectorRec point, Object user) {
    return ((grayTWorkerRec)user).gray_line_to(point);
  }

  /* ==================== conicTo ===================================== */
  @Override
  public FTError.ErrorTag conicTo(FTVectorRec control, FTVectorRec point, Object user) {
    return ((grayTWorkerRec)user).gray_conic_to(control, point);
  }

  /* ==================== cubicTo ===================================== */
  @Override
  public FTError.ErrorTag cubicTo(FTVectorRec control1, FTVectorRec control2, FTVectorRec point, Object user) {
    return ((grayTWorkerRec)user).gray_cubic_to(control1, control2, point);
  }

}