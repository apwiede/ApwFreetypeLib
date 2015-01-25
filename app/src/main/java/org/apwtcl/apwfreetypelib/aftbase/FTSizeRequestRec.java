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

  private FTTags.SizeRequestType type = FTTags.SizeRequestType.NOMINAL;
  private int width = 0;
  private int height = 0;
  private int horiResolution = 0;
  private int vertResolution = 0;

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
    str.append("...type: "+type+'\n');
    str.append("...width: "+width+'\n');
    str.append("...height: "+height+'\n');
    str.append("...horiResolution: "+horiResolution+'\n');
    str.append("...vertResolution: "+vertResolution+'\n');
    return str.toString();
  }

  /* ==================== getType ================================== */
  public FTTags.SizeRequestType getType() {
    return type;
  }

  /* ==================== setType ================================== */
  public void setType(FTTags.SizeRequestType type) {
    this.type = type;
  }

  /* ==================== getWidth ================================== */
  public int getWidth() {
    return width;
  }

  /* ==================== setWidth ================================== */
  public void setWidth(int width) {
    this.width = width;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getHoriResolution ================================== */
  public int getHoriResolution() {
    return horiResolution;
  }

  /* ==================== setHoriResolution ================================== */
  public void setHoriResolution(int horiResolution) {
    this.horiResolution = horiResolution;
  }

  /* ==================== getVertResolution ================================== */
  public int getVertResolution() {
    return vertResolution;
  }

  /* ==================== setVertResolution ================================== */
  public void setVertResolution(int vertResolution) {
    this.vertResolution = vertResolution;
  }

}