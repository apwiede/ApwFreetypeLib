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
  /*    FTSizeRequestRec                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a size request.                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    type           :: See @FT_Size_Request_Type.                       */
  /*                                                                       */
  /*    width          :: The desired width.                               */
  /*                                                                       */
  /*    height         :: The desired height.                              */
  /*                                                                       */
  /*    horiResolution :: The horizontal resolution.  If set to zero,      */
  /*                      `width' is treated as a 26.6 fractional pixel    */
  /*                      value.                                           */
  /*                                                                       */
  /*    vertResolution :: The vertical resolution.  If set to zero,        */
  /*                      `height' is treated as a 26.6 fractional pixel   */
  /*                      value.                                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    If `width' is zero, then the horizontal scaling value is set equal */
  /*    to the vertical scaling value, and vice versa.                     */
  /*                                                                       */
  /* ===================================================================== */

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTSizeRequestRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTSizeRequestRec";

  public FTTags.SizeRequestType type = FTTags.SizeRequestType.NOMINAL;
  public int width = 0;
  public int height = 0;
  public int horiResolution = 0;
  public int vertResolution = 0;

  /* ==================== FTSizeRequestRec ================================== */
    public FTSizeRequestRec() {
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
 
}