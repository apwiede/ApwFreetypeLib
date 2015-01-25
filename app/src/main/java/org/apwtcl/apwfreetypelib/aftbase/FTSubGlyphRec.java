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
  /*    FTSubGlyphRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;

public class FTSubGlyphRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTSubGlyphRec";

  private int index = 0;
  private Flags.SubGlyph flags = Flags.SubGlyph.UNKNOWN;
  private int arg1 = 0;
  private int arg2 = 0;
  private FTMatrixRec transform = null;

  /* ==================== FTSubGlyphRec ================================== */
  public FTSubGlyphRec() {
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
    str.append("...index: " + index + '\n');
    str.append("...flags: "+flags+'\n');
    str.append("...arg1: "+arg1+'\n');
    str.append("...arg2: "+arg2+'\n');
    return str.toString();
  }

  /* ==================== getIndex ================================== */
  public int getIndex() {
    return index;
  }

  /* ==================== setIndex ================================== */
  public void setIndex(int index) {
    this.index = index;
  }

  /* ==================== getFlags ================================== */
  public Flags.SubGlyph getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(Flags.SubGlyph flags) {
    this.flags = flags;
  }

  /* ==================== getArg1 ================================== */
  public int getArg1() {
    return arg1;
  }

  /* ==================== setArg1 ================================== */
  public void setArg1(int arg1) {
    this.arg1 = arg1;
  }

  /* ==================== getArg2 ================================== */
  public int getArg2() {
    return arg2;
  }

  /* ==================== setArg2 ================================== */
  public void setArg2(int arg2) {
    this.arg2 = arg2;
  }

  /* ==================== getTransform ================================== */
  public FTMatrixRec getTransform() {
    return transform;
  }

  /* ==================== setTransform ================================== */
  public void setTransform(FTMatrixRec transform) {
    this.transform = transform;
  }

}