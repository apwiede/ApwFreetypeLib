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
  /*    TTPostscript                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType PostScript table.  All fields */
  /*    comply to the TrueType specification.  This structure does not     */
  /*    reference the PostScript glyph names, which can be nevertheless    */
  /*    accessed with the `ttpost' module.                                 */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTPostscriptRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTPostscript";

  private int format_type = 0;
  private int italic_angle = 0;
  private int underline_position = 0;
  private int underline_thickness = 0;
  private int is_fixed_pitch = 0;
  private int min_mem_type42 = 0;
  private int max_mem_type42 = 0;
  private int min_mem_type1 = 0;
  private int max_mem_type1 = 0;
  /* Glyph names follow in the file, but we don't   */
  /* load them by default.  See the ttpost.c file.  */

  /* ==================== TTPostscript ================================== */
  public TTPostscriptRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: post offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.post, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    format_type = stream.readInt();
    italic_angle = stream.readInt();
    underline_position = stream.readShort();
    underline_thickness = stream.readShort();
    is_fixed_pitch = stream.readInt();
    min_mem_type42 = stream.readInt();
    max_mem_type42 = stream.readInt();
    min_mem_type1 = stream.readInt();
    max_mem_type1 = stream.readInt();
      /* we don't load the glyph names, we do that in another */
      /* module (ttpost). */

    FTTrace.Trace(7, TAG, String.format("FormatType: 0x%08x", format_type));
    FTTrace.Trace(7, TAG, String.format("isFixedPitch:   %s", is_fixed_pitch != 0L ? "  yes" : "    no"));
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
    return str.toString();
  }

  /* ==================== getFormatType ===================================== */
  public int getFormatType() {
    return format_type;
  }

  /* ==================== getItalicAngle ===================================== */
  public int getItalicAngle() {
    return italic_angle;
  }

  /* ==================== getUnderlinePosition ===================================== */
  public int getUnderlinePosition() {
    return underline_position;
  }

  /* ==================== getUnderlineThickness ===================================== */
  public int getUnderlineThickness() {
    return underline_thickness;
  }

  /* ==================== getIsFixedPitch ===================================== */
  public int getIsFixedPitch() {
    return is_fixed_pitch;
  }

  /* ==================== getMinMemType42 ===================================== */
  public int getMinMemType42() {
    return min_mem_type42;
  }

  /* ==================== getMaxMemType42 ===================================== */
  public int getMaxMemType42() {
    return max_mem_type42;
  }

  /* ==================== getMinMemType1 ===================================== */
  public int getMinMemType1() {
    return min_mem_type1;
  }

  /* ==================== getMaxMemType1 ===================================== */
  public int getMaxMemType1() {
    return max_mem_type1;
  }

}