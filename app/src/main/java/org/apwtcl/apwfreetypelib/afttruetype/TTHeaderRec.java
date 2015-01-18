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
  /*    TTHeaderRec                                                           */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType font header table.  All       */
  /*    fields follow the TrueType specification.                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTHeaderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTHeaderRec";

  private int table_version = 0;
  private int font_revision = 0;
  private int checksum_adjust = 0;
  private int magic_number = 0;
  private int flags = 0;
  private int units_per_em = 0;
  private int[] created = new int[2];
  private int[] modified= new int[2];
  private int x_min = 0;
  private int y_min = 0;
  private int x_max = 0;
  private int y_max = 0;
  private int mac_style = 0;
  private int lowest_rec_ppem = 0;
  private int font_direction = 0;
  private int index_to_loc_format = 0;
  private int glyph_data_format = 0;

  /* ==================== TTHeaderRec ================================== */
  public TTHeaderRec() {
    oid++;
    id = oid;
  }

  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface, TTTags.Table tag) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "TTHeaderRec offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTReference<Integer> length_ref = new FTReference<Integer>();

    Log.i(TAG, "gotoTable");
    error = ttface.gotoTable(tag, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    table_version = stream.readInt();
    font_revision = stream.readInt();
    checksum_adjust = stream.readInt();
    magic_number = stream.readInt();
    flags = stream.readShort();
    units_per_em = stream.readShort();
    created[0] = stream.readInt();
    created[1] = stream.readInt();
    modified[0] = stream.readInt();
    modified[1] = stream.readInt();
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "HEADER: "+String.format("table_version: 0x%08x, font_revision: 0x%08x, checksum_adjust; 0x%08x, magic_number0x%08x",
		table_version, font_revision, checksum_adjust, magic_number));
    x_min = stream.readShort();
    y_min = stream.readShort();
    x_max = stream.readShort();
    y_max = stream.readShort();
    mac_style = stream.readShort();
    lowest_rec_ppem = stream.readShort();
    font_direction = stream.readShort();
    index_to_loc_format = stream.readShort();
    glyph_data_format = stream.readShort();
    FTTrace.Trace(7, TAG, String.format("Units per EM: %4d", units_per_em));
    FTTrace.Trace(7, TAG, String.format("IndexToLoc:   %4d", index_to_loc_format));
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

  /* ==================== getTableVersion ===================================== */
  public int getTableVersion() {
    return table_version;
  }


  /* ==================== getFontRevision ===================================== */
  public int getFontRevision() {
    return font_revision;
  }

  /* ==================== getCheckSumAdjust ===================================== */
  public int getCheckSumAdjust() {
    return checksum_adjust;
  }

  /* ==================== getMagicNumber ===================================== */
  public int getMagicNumber() {
    return magic_number;
  }

  /* ==================== getFlags ===================================== */
  public int getFlags() {
    return flags;
  }

  /* ==================== getUnitsPerEM ===================================== */
  public int getUnitsPerEM() {
    return units_per_em;
  }

  /* ==================== getCreated ===================================== */
  public int[] getCreated() {
    return created;
  }

  /* ==================== getModified ===================================== */
  public int[] getModified() {
    return modified;
  }

  /* ==================== getXMin ===================================== */
  public int getXMin() {
    return x_min;
  }

  /* ==================== getXMax ===================================== */
  public int getXMax() {
    return x_max;
  }

  /* ==================== getYMin===================================== */
  public int getYMin() {
    return y_min;
  }

  /* ==================== getYMax===================================== */
  public int getYMax() {
    return y_max;
  }


  /* ==================== getMacStyle ===================================== */
  public int getMacStyle() {
    return mac_style;
  }

  /* ==================== getLowestRecPPEM ===================================== */
  public int getLowestRecPPEM() {
    return lowest_rec_ppem;
  }


  /* ==================== getFontDirection ===================================== */
  public int getFontDirection() {
    return font_direction;
  }


  /* ==================== getIndexToLocFormat ===================================== */
  public int getIndexToLocFormat() {
    return index_to_loc_format;
  }


  /* ==================== getGlyphDataFormat ===================================== */
  public int getGlyphDataFormat() {
    return glyph_data_format;
  }

}