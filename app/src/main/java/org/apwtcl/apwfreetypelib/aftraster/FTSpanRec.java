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
  /*    FTSpanRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a single span of gray (or black) pixels  */
  /*    when rendering a monochrome or anti-aliased bitmap.                */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    x        :: The span's horizontal start position.                  */
  /*                                                                       */
  /*    len      :: The span's length in pixels.                           */
  /*                                                                       */
  /*    coverage :: The span color/coverage, ranging from 0 (background)   */
  /*                to 255 (foreground).  Only used for anti-aliased       */
  /*                rendering.                                             */
  /*                                                                       */
  /* <Note>                                                                */
  /*    This structure is used by the span drawing callback type named     */
  /*    @FT_SpanFunc which takes the y~coordinate of the span as a         */
  /*    a parameter.                                                       */
  /*                                                                       */
  /*    The coverage value is always between 0 and 255.  If you want less  */
  /*    gray values, the callback function has to reduce them.             */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTSpanRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSpanRec";

  public static int FT_MAX_GRAY_SPANS = 32;

  private int x;
  private int len;
  private byte coverage;

  /* ==================== FTSpanRec ================================== */
  public FTSpanRec() {
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
    str.append("...x: "+x+'\n');
    str.append("...len: "+len+'\n');
    str.append("...coverage: "+coverage+'\n');
    return str.toString();
  }

  /* ==================== getX ================================== */
  public int getX() {
    return x;
  }

  /* ==================== setX ================================== */
  public void setX(int x) {
    this.x = x;
  }

  /* ==================== getLen ================================== */
  public int getLen() {
    return len;
  }

  /* ==================== setLen ================================== */
  public void setLen(int len) {
    this.len = len;
  }

  /* ==================== getCoverage ================================== */
  public byte getCoverage() {
    return coverage;
  }

  /* ==================== setCoverage ================================== */
  public void setCoverage(byte coverage) {
    this.coverage = coverage;
  }

}