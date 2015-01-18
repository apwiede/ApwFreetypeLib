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
  /*    TTKernRec                                                          */
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
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTKernRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTKernRec";

  private int version = 0;
  private int num_tables = 0;

  /* the next values are not in the file */
  private int table_size;
  private TTKernEntryRec[] entries = null;
  private int num_kern_tables = 0;
  private int avail_bits = 0;
  private int order_bits = 0;

  /* ==================== TTKernRec ================================== */
  public TTKernRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: kern offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();
    long limit;
    int byte_idx = 0;
    int nn = 0;
    int avail = 0;
    int ordered = 0;

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.kern, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    length = length_ref.Get();
    if (length < 4) { /* the case of a malformed table */
      FTTrace.Trace(7, TAG, "tt_face_load_kern:kerning table is too small - ignored");
      error = FTError.ErrorTag.LOAD_TABLE_MISSING;
      return error;
    }

    table_size = length;
    limit = stream.pos() + length;

    version = stream.readShort();
    num_tables = stream.readShort();
    byte_idx = 4;
    if (num_tables > 32) { /* we only support up to 32 sub-tables */
      num_tables = 32;
    }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "kern: table_size: " + table_size + "!num_tables: "+num_tables + "!");

    entries = new TTKernEntryRec[num_tables];
    for (nn = 0; nn < num_tables; nn++) {
      int num_pairs;
      long next_pos = stream.pos();
      TTKernEntryRec entry = new TTKernEntryRec();

      int mask = 1 << nn;

      if (stream.pos() + TTKernEntryRec.TT_KERN_ENTRY_SIZE > limit) {
        break;
      }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "nn: " + nn + " pos: " + stream.pos() + "!");
      entries[nn] = entry;
      entry.Load(stream);
      if (entry.getLength() <= TTKernEntryRec.TT_KERN_ENTRY_SIZE) {
        break;
      }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "nn: " + nn + " length: "
    + String.format("0x%08x", entry.getLength()) + "!coverage: "
    + String.format("0x%08x", entry.getCoverage()) + "!");
      if (stream.pos() + entry.getLength() > limit) { /* handle broken table */
        next_pos = limit;
      }
      /* only use horizontal kerning tables */
      if ((entry.getCoverage() & ~8) != 0x0001 || stream.pos() + 2 /* num_pairs_value */ + TTKernEntryRec.TT_KERN_ENTRY_SIZE > limit) {
        ; /* goto next table */
      } else {
        num_pairs = stream.readShort();
        stream.readShort();
        stream.readShort();
        stream.readShort();
        next_pos = stream.pos();
        if ((next_pos - stream.pos()) < TTKernEntryRec.TT_KERN_ENTRY_SIZE * num_pairs) { /* handle broken count */
          num_pairs = (int)((next_pos - stream.pos()) / TTKernEntryRec.TT_KERN_ENTRY_SIZE);
        }
        avail |= mask;
          /*
           * Now check whether the pairs in this table are ordered. We
           * then can use binary search.
           */
        if (num_pairs > 0) {
          int count;
          int old_pair;

          old_pair = stream.readInt();
          stream.readShort();
          next_pos = stream.pos();
          for (count = (num_pairs - 1); count > 0; count--) {
            int cur_pair;

            cur_pair = stream.readInt();
            stream.readShort();
            next_pos = stream.pos();
            if (cur_pair <= old_pair) {
              break;
            }
            old_pair = cur_pair;
          }
          if (count == 0) {
            ordered |= mask;
          }
        }
      }
      stream.seek(next_pos);
    }
    num_kern_tables = nn;
    avail_bits = avail;
    order_bits = ordered;
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

  /* ==================== getTableSize ================================== */
  public int getTableSize() {
    return table_size;
  }

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getNumKernTables ================================== */
  public int getNumKernTables() {
    return num_kern_tables;
  }

  /* ==================== getAvailBits ================================== */
  public int getAvailBits() {
    return avail_bits;
  }

  /* ==================== getOrderBits ================================== */
  public int getOrderBits() {
    return order_bits;
  }
}
