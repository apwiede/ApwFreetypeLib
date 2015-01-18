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
  /*    TTHdmxRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType hdmx table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTHdmxRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTHdmx";

  private int version = 0;
  private int record_count = 0;
  private int record_size = 0;
  private int[] records = null;

  /* ==================== TTHdmx ================================== */
  public TTHdmxRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: hdmx offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.hdmx, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }

    version = stream.readShort();
    record_count = stream.readShort();
    record_size = stream.readInt();
    records = new int[record_count];
    for(int i = 0; i < record_count; i++) {
      records[i] = stream.readInt();
    }
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

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== geCount ================================== */
  public int getRecordCount() {
    return record_count;
  }

  /* ==================== getRecordSize ================================== */
  public int getRecordSize() {
    return record_size;
  }

  /* ==================== getRecords ================================== */
  public int[] getRecords() {
    return records;
  }

  /* ==================== getRecordValue ================================== */
  public int getRecordValue(int idx) {
    return records[idx];
  }

}
