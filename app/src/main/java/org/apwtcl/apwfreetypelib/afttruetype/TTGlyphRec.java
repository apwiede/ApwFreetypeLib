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
  /*    TTGlyphRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType glyf table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTGlyphRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyph";

  protected int num_contours = 0;
  protected int x_min = 0;
  protected int y_min = 0;
  protected int x_max = 0;
  protected int y_max = 0;

  /* the following fields are not in the file */
  protected long glyf_offset = 0L;
  protected FTTags.GlyphFormat format = FTTags.GlyphFormat.NONE;

  /* ==================== TTGlyphRec ================================== */
  public TTGlyphRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface, int limit) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: glyph header: offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    /* the caller has already positioned to the correct offset in the glyf table! */
    glyf_offset = stream.pos();
    /* load the glyph header */
    num_contours = stream.readShort();
    x_min = (short)stream.readShort();
    y_min = (short)stream.readShort();
    x_max = (short)stream.readShort();
    y_max = (short)stream.readShort();
    FTTrace.Trace(7, TAG, "header loaded");
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
    str.append("..num_contours: "+num_contours+'\n');
    str.append("..x_min: "+x_min+'\n');
    str.append("..y_min: "+y_min+'\n');
    str.append("..x_max: "+x_max+'\n');
    str.append("..y_max: "+y_max+'\n');
    str.append("..glyf_offset: "+glyf_offset+'\n');
    return str.toString();
  }

  /* ==================== getNum_contours ================================== */
  public int getNum_contours() {
    return num_contours;
  }

  /* ==================== setNum_contours ================================== */
  public void setNum_contours(int num_contours) {
    this.num_contours = num_contours;
  }

  /* ==================== getX_min ================================== */
  public int getX_min() {
    return x_min;
  }

  /* ==================== setX_min ================================== */
  public void setX_min(int x_min) {
    this.x_min = x_min;
  }

  /* ==================== getY_min ================================== */
  public int getY_min() {
    return y_min;
  }

  /* ==================== setY_min ================================== */
  public void setY_min(int y_min) {
    this.y_min = y_min;
  }

  /* ==================== getX_max ================================== */
  public int getX_max() {
    return x_max;
  }

  /* ==================== setX_max ================================== */
  public void setX_max(int x_max) {
    this.x_max = x_max;
  }

  /* ==================== getY_max ================================== */
  public int getY_max() {
    return y_max;
  }

  /* ==================== setY_max ================================== */
  public void setY_max(int y_max) {
    this.y_max = y_max;
  }

  /* ==================== getGlyf_offset ================================== */
  public long getGlyf_offset() {
    return glyf_offset;
  }

  /* ==================== setGlyf_offset ================================== */
  public void setGlyf_offset(long glyf_offset) {
    this.glyf_offset = glyf_offset;
  }


  /* ==================== getFormat ================================== */
  public FTTags.GlyphFormat getFormat() {
    return format;
  }

  /* ==================== setFormat ================================== */
  public void setFormat(FTTags.GlyphFormat format) {
    this.format = format;
  }

}