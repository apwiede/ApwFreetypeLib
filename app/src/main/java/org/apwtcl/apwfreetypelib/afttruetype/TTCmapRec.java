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
  /*    TTCmapRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType cmap table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTCmapRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCmap";


  public final static int NUM_CMAP_CLASSES = 10;

  private int version = 0;
  private int num_subtables = 0;

  /* the following fields are not in the file */
  long table_start = 0;
  private int table_size = 0;
  private TTCmapEncodingRec[] encodings = null;
  private TTCMapClassRec[] cmap_classes = null;
  private int[] formats = null;

  /* ==================== TTCmapRec ================================== */
  public TTCmapRec() {
    oid++;
    id = oid;
    cmap_classes = new TTCMapClassRec[NUM_CMAP_CLASSES];
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: cmap offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();
    long limit;

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.cmap, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      FTTrace.Trace(7, TAG, "is missing");
      table_start = 0;
      table_size = 0;
      error = FTError.ErrorTag.ERR_OK;
      return error;
    }
    length = length_ref.Get();
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, String.format("pos: %d length: %d", stream.pos(), length));
    table_size = length_ref.Get().intValue();
    table_start = stream.pos();
    version = stream.readShort();
      /* only recognize format 0 */
    if (version != 0) {
      Log.e(TAG, String.format("Load cmap: unsupported `cmap' table version = %d", version));
      return FTError.ErrorTag.LOAD_INVALID_TABLE;
    }
    num_subtables = stream.readShort();
    limit = stream.pos() + table_size;
    Debug(0, DebugTag.DBG_CMAP, TAG, toDebugString());
    encodings = new TTCmapEncodingRec[num_subtables];
    formats = new int[num_subtables];
    for (int n = 0; n < num_subtables && stream.pos() + 8 <= limit; n++) {
      encodings[n] = new TTCmapEncodingRec();
      encodings[n].Load(stream, ttface);
    }
    /* now read the charmaps depending on format */
    for (int n = 0; n < num_subtables && stream.pos() + 8 <= limit; n++) {
      TTCMapClassRec cmap_class;
      stream.seek(table_start + encodings[n].getOffset());
      /* read the format (every cmap format type stores its own format when called */
      int format = stream.readShort();
      formats[n] = format;
      switch (TTTags.CMapFormat.getTableTag(format)) {
        case CMap0:
          cmap_class = new TTCMap0Class();
          cmap_classes[TTTags.CMapFormat.CMap0.getVal()] = cmap_class;
          ((TTCMap0Class)cmap_class).Load(stream, ttface);
          break;
        case CMap2:
          cmap_class = new TTCMap2Class();
          cmap_classes[TTTags.CMapFormat.CMap2.getVal()] = cmap_class;
          break;
        case CMap4:
          cmap_class = new TTCMap4Class();
          cmap_classes[TTTags.CMapFormat.CMap4.getVal()] = cmap_class;
          ((TTCMap4Class)cmap_class).Load(stream, ttface);
          break;
        case CMap6:
          cmap_class = new TTCMap6Class();
          cmap_classes[TTTags.CMapFormat.CMap6.getVal()] = cmap_class;
          break;
        case CMap8:
          cmap_class = new TTCMap8Class();
          cmap_classes[TTTags.CMapFormat.CMap8.getVal()] = cmap_class;
          break;
        case CMap10:
          cmap_class = new TTCMap10Class();
          cmap_classes[TTTags.CMapFormat.CMap10.getVal()] = cmap_class;
          break;
        case CMap12:
          cmap_class = new TTCMap12Class();
          cmap_classes[TTTags.CMapFormat.CMap12.getVal()] = cmap_class;
          break;
        case CMap13:
          cmap_class = new TTCMap13Class();
          cmap_classes[TTTags.CMapFormat.CMap13.getVal()] = cmap_class;
          break;
        case CMap14:
          cmap_class = new TTCMap14Class();
          cmap_classes[TTTags.CMapFormat.CMap14.getVal()] = cmap_class;
          break;
      }
    }
    FTTrace.Trace(7, TAG, "loaded");
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
    str.append("..num_subtables: "+num_subtables+'\n');
    str.append("..table_start: "+table_start+'\n');
    str.append("..table_size: "+table_size+'\n');
    return str.toString();
  }

  /* ==================== getTableSize ================================== */
  public int getSize() {
    return table_size;
  }

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getNumSubtables ================================== */
  public int getNumSubtables() {
    return num_subtables;
  }

  /* ==================== getSubtableEncoding ================================== */
  public TTCmapEncodingRec getSubtableEncoding(int idx) {
    return encodings[idx];
  }

  /* ==================== getFormatEntry ================================== */
  public int getFormatEntry(int idx) {
    return formats[idx];
  }

  /* ==================== getCMapClassEntry ================================== */
  public TTCMapClassRec getCMapClassEntry(TTTags.CMapFormat idx) {
    return cmap_classes[idx.getVal()];
  }

}
