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
  /*    TTValidatorRec                                                          */
  /*                                                                       */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftbase.FTValidatorRec;

public class TTValidatorRec extends FTValidatorRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTValidatorRec";

  private int num_glyphs = 0;

  /* ==================== TTValidatorRec ================================== */
  public TTValidatorRec() {
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
    String super_str = super.toDebugString();
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("..num_glyphs: "+num_glyphs+'\n');
    return super_str + str.toString();
  }

  /* ==================== getNumGlyphs ===================================== */
  public int getNumGlyphs() {
    return num_glyphs;
  }

  /* ==================== getNumGlyphs ===================================== */
  public void setNumGlyphs(int num_glyphs) {
    this.num_glyphs = num_glyphs;
  }

}