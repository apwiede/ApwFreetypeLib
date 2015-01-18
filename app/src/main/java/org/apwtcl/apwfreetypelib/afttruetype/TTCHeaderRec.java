/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf at wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTCHeaderRec                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    TrueType collection header.  This table contains the offsets of    */
  /*    the font headers of each distinct TrueType face in the file.       */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    tag     :: Must be `ttc ' to indicate a TrueType collection.       */
  /*                                                                       */
  /*    version :: The version number.                                     */
  /*                                                                       */
  /*    count   :: The number of faces in the collection.  The             */
  /*               specification says this should be an unsigned long, but */
  /*               we use a signed long since we need the value -1 for     */
  /*               specific purposes.                                      */
  /*                                                                       */
  /*    offsets :: The offsets of the font headers, one per face.          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftsfnt.FTSfntTableTag;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTCHeaderRec extends FTDebug {
  private static int oid = 0;
    
  private int id;
  private static String TAG = "TTCHeaderRec";
    
  private TTTags.Table tag;
  private int version = 0;
  private int count = 0;
  private long[] offsets = null;

  /* ==================== TTCHeaderRec ================================== */
  public TTCHeaderRec() {
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
    str.append("..tag: "+tag+" "+tag.getDescription()+'\n');
    str.append("..version: "+Long.toHexString(version)+'\n');
    str.append("..count: "+count+'\n');
    str.append("..offsets:\n");
    if (offsets != null) {
      for (int i = 0; i < offsets.length; i++) {
        str.append(String.format("....%2d: %d\n", i, offsets[i]));
      }
    }
    return str.toString();
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    long offset = stream.pos();
    int tag = stream.readInt();
    this.tag = TTTags.Table.getTableTag(tag);
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "TTCHeaderRec: Tag: " + String.format("0x%08x offset: 0x%08x", tag, offset));
    if (tag != 0x00010000 &&
        tag != TTTags.Table.ttcf.getVal()  &&
        tag != TTTags.Table.OTTO.getVal()  &&
        tag != TTTags.Table.truE.getVal()  &&
        tag != TTTags.Table.typ1.getVal()  &&
        tag != 0x00020000) {
      FTTrace.Trace(7, TAG, "  not a font using the SFNT container format");
      return FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT;
    }
    this.tag = TTTags.Table.ttcf;
    if (tag == TTTags.Table.ttcf.getVal()) {
      int  n;

      FTTrace.Trace(7, TAG, "sfnt_open_font: file is a collection");
      this.version = stream.readInt();
      this.count = stream.readInt();
        
      if (this.count == 0) {
        return FTError.ErrorTag.LOAD_INVALID_TABLE;
      }
      /* a rough size estimate: let's conservatively assume that there   */
      /* is just a single table info in each subfont header (12 + 16*1 = */
      /* 28 bytes), thus we have (at least) `12 + 4*count' bytes for the */
      /* size of the TTC header plus `28*count' bytes for all subfont    */
      /* headers                                                         */
      if (this.count > stream.getSize() / (28 + 4)) {
        return FTError.ErrorTag.LOAD_ARRAY_TOO_LARGE;
      }
      /* now read the offsets of each font in the file */
      this.offsets = new long[this.count];
      for (n = 0; n < this.count; n++) {
        this.offsets[n] = stream.readInt();
      }
    } else {
      FTTrace.Trace(7, TAG, "sfnt_open_font: synthesize TTC");
      this.version = (1 << 16);
      this.count = 1;
      this.offsets = new long[1];
      this.offsets[0] = offset;
    }
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== GotoFaceIndex ===================================== */
  public FTError.ErrorTag GotoFaceIndex(FTStreamRec stream, int face_index) {
    if (face_index < 0 || face_index >= count) {
      return FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
    }
    stream.seek(offsets[face_index]);
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== GetCount ===================================== */
  public int GetCount() {
    return count;
  }

  /* ==================== GetVersion ===================================== */
  public int GetVersion() {
    return version;
  }

}
