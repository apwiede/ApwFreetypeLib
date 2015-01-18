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
  /*    TTGaspRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure modeling the TrueType `gasp' table used to specify     */
  /*    grid-fitting and anti-aliasing behaviour.                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    version    :: The version number.                                  */
  /*                                                                       */
  /*    numRanges  :: The number of gasp ranges in table.                  */
  /*                                                                       */
  /*    gaspRanges :: An array of gasp ranges.                             */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTGaspRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGaspRec";

  private int version;
  private int num_ranges;
  private TTGaspRangeRec[] gasp_ranges = null;

  /* ==================== TTGaspRec ================================== */
  public TTGaspRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: gasp offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.gasp, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }

    version = stream.readShort();
    num_ranges = stream.readShort();
    gasp_ranges = new TTGaspRangeRec[num_ranges];
    /* only support versions 0 and 1 of the table */
    if (version >= 2) {
      num_ranges = 0;
      error = FTError.ErrorTag.INTERP_INVALID_TABLE;
      return error;
    }

    for (int i = 0; i < num_ranges; i++) {
      gasp_ranges[i] = new TTGaspRangeRec();
      gasp_ranges[i].Load(stream);
    }
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
    str.append("..version: "+version+'\n');
    str.append("..num_ranges: "+num_ranges+'\n');
    return str.toString();
  }

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getNumRanges ================================== */
  public int getNumRanges() {
    return num_ranges;
  }

  /* ==================== getGaspRanges ================================== */
  public TTGaspRangeRec[] getGaspRanges() {
    return gasp_ranges;
  }

}