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

package org.apwtcl.apwfreetypelib.aftsfnt;

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.*;
import org.apwtcl.apwfreetypelib.aftutil.*;

/* ===================================================================== */
/*    TTLoad                                                             */
/*                                                                       */
/* ===================================================================== */

public class TTLoad extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTLoad";

  /* ==================== TTLoad ================================== */
  public TTLoad() {
    oid++;
    id = oid;
  }

  /* ==================== mySelf ================================== */
  public String mySelf() {
      return TAG + "!" + id + "!";
    }

  /* ==================== toString ===================================== */
  public String toString() {
      return mySelf() + "!";
    }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf() + "\n");
    return str.toString();
  }

  /*
   * =====================================================================
   * tt_face_load_any
   * 
   * <Description> Loads any font table into client memory.
   * 
   * <Input> face :: The face object to look for.
   * 
   * tag :: The tag of table to load. Use the value 0 if you want to access
   * the whole font file, else set this parameter to a valid TrueType table
   * tag that you can forge with the MAKE_TT_TAG macro.
   * 
   * offset :: The starting offset in the table (or the file if tag == 0).
   * 
   * length :: The address of the decision variable:
   * 
   * If length == NULL: Loads the whole table. Returns an error if `offset' ==
   * 0!
   * 
   * If *length == 0: Exits immediately; returning the length of the given
   * table or of the font file, depending on the value of `tag'.
   * 
   * If *length != 0: Loads the next `length' bytes of table or font, starting
   * at offset `offset' (in table or font too).
   * 
   * <Output> buffer :: The address of target buffer.
   * 
   * <Return> FreeType error code. 0 means success.
   * 
   * =====================================================================
   */
  public static FTError.ErrorTag tt_face_load_any(Object... args) {
    TTFaceRec face = (TTFaceRec) args[0];
    TTTags.Table tag = (TTTags.Table) args[1];
    long offset = (long) args[2];
    byte[] buffer = (byte[]) args[3];
    FTReference<Long> length_ref = (FTReference<Long>) args[4];
    long length = length_ref.Get();

    FTError.ErrorTag error;
    FTStreamRec stream;
    TTTableRec table;
    Long size;

    if (tag != TTTags.Table.unkn) {
      /* look for tag in font directory */
      table = face.lookupTable(tag);
      if (table == null) {
        error = FTError.ErrorTag.LOAD_TABLE_MISSING;
        return error;
      }
      offset += table.getOffset();
      size = (long) table.getLength();
    } else {
      /* tag == 0 -- the user wants to access the font file directly */
  	  size = face.getStream().getSize();
    }
    if (length_ref != null && length == 0) {
      length = size;
      length_ref.Set(length);
      return FTError.ErrorTag.ERR_OK;
    }
    if (length_ref != null) {
      size = length;
    }
    stream = face.getStream();
    /* the `if' is syntactic sugar for picky compilers */
    stream.seek(offset);
    int ret = stream.readByteArray(buffer, size.shortValue());
    if (ret > 0) {
      error = FTError.ErrorTag.ERR_OK;
    } else {
      error = FTError.ErrorTag.LOAD_STREAM_READ_ERROR;
    }
    return error;
  }

  /*
   * =====================================================================
   * tt_face_free_name
   * 
   * <Description> Frees the name records.
   * 
   * <Input> face :: A handle to the target face object.
   * 
   * =====================================================================
   */
  public static void tt_face_free_name(Object... args) {
    FTReference<TTFaceRec> face_ref = (FTReference<TTFaceRec>) args[0];

    TTFaceRec face = face_ref.Get();
    TTNameTableRec table = face.getName_table();
    TTNameEntryRec entry = null;
    int count = table.getNumNameRecords();

    if (table.getNames() != null) {
      for (int i = 0; i < count; i++) {
        // FT_FREE( entry.string );
        entry = table.getNames()[i];
        entry.setStringLength(0);
      }
      /* free strings table */
      // FT_FREE(table.names);
    }
    table.reset();
    face.setName_table(table);
    face_ref.Set(face);
  }

  /*
   * =====================================================================
   * load_sbit_image
   * =====================================================================
   */
  public static int load_sbit_image(Object... arg1) {
    Log.e(TAG, "load_sbit_image not yet implemented");
    return 1;
  }

  /*
   * =====================================================================
   * tt_face_load_sbit_image
   * =====================================================================
   */
  public static int tt_face_load_sbit_image(Object... arg1) {
    Log.e(TAG, "tt_face_load_sbit_image not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_get_ps_name
   * =====================================================================
   */
  public static int tt_face_get_psname(Object... arg1) {
    Log.e(TAG, "tt_face_get_psname not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_free_ps_name
   * =====================================================================
   */
  public static int tt_face_free_ps_names(Object... arg1) {
    Log.e(TAG, "tt_face_free_ps_names not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_get_kerning
   * =====================================================================
   */
  public static int tt_face_get_kerning(Object... arg1) {
    Log.e(TAG, "tt_face_get_kerning not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_load_eblc
   * =====================================================================
   */
  public static int tt_face_load_eblc(Object... arg1) {
    Log.e(TAG, "tt_face_load_eblc not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_free_eblc
   * =====================================================================
   */
  public static int tt_face_free_eblc(Object... arg1) {
    Log.e(TAG, "tt_face_free_eblc not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_set_bit_strike
   * =====================================================================
   */
  public static int tt_face_set_bit_strike(Object... arg1) {
    Log.e(TAG, "tt_face_set_bit_strike not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_load_strike_metrics
   * =====================================================================
   */
  public static int tt_face_load_strike_metrics(Object... arg1) {
    Log.e(TAG, "tt_face_load_strike_metrics not yet implemented");
    return 1;
  }

  /* =====================================================================
   * tt_face_get_metrics
   *
   * <Description>
   *    Returns the horizontal or vertical metrics in font units for a
   *    given glyph.  The metrics are the left side bearing (resp. top
   *    side bearing) and advance width (resp. advance height).
   *
   * <Input>
   *    header  :: A pointer to either the horizontal or vertical metrics
   *               structure.
   *
   *    idx     :: The glyph index.
   *
   * <Output>
   *    bearing :: The bearing, either left side or top side.
   *
   *    advance :: The advance width resp. advance height.
   * 
   * =====================================================================
   */
  public static FTError.ErrorTag tt_face_get_metrics(Object ... args) {
    TTFaceRec face = (TTFaceRec)args[0];
    boolean vertical = (boolean)args[1];
    int gindex = (int)args[2];
    FTReference<Short> bearing_ref = (FTReference<Short>)args[3];
    FTReference<Short> advance_ref = (FTReference<Short>)args[4];
    long table_pos;
    int table_size;
    long table_end;
    long pos;
    int advance;
    int bearing;
    int k;

    if (vertical) {
      table_pos = face.getVert_metrics_header().getMetricsOffset();
      table_size = face.getVert_metrics_header().getMetricsSize();
      k = face.getVertical().getNumberOfVMetrics();
    } else {
      table_pos = face.getHori_metrics_header().getMetricsOffset();
      table_size = face.getHori_metrics_header().getMetricsSize();
      k = face.getHorizontal().getNumberOfHMetrics();
    }
    table_end = table_pos + table_size;
    if (k > 0) {
      if (gindex < (int)k) {
        table_pos += 4 * gindex;
        if (table_pos + 4 > table_end) {
          bearing_ref.Set((short)0);
          advance_ref.Set((short)0);
          return FTError.ErrorTag.ERR_OK;
        }
        pos = face.getStream().seek(table_pos);
        advance = face.getStream().readShort();
        bearing = face.getStream().readShort();
        if (pos < 0 /* ||
             FT_READ_USHORT(*aadvance) ||
             FT_READ_SHORT(*abearing) */ ) {
          bearing_ref.Set((short)0);
          advance_ref.Set((short)0);
          return FTError.ErrorTag.ERR_OK;
        }
        advance_ref.Set((short)advance);
        bearing_ref.Set((short)bearing);
      } else {
        table_pos += 4 * (k - 1);
        if (table_pos + 4 > table_end) {
          bearing_ref.Set((short)0);
          advance_ref.Set((short)0);
          return FTError.ErrorTag.ERR_OK;
        }
        pos = face.getStream().seek(table_pos);
        advance = face.getStream().readShort();
        if (pos < 0 /* ||
             FT_READ_USHORT(*aadvance) */ ) {
          bearing_ref.Set((short)0);
          advance_ref.Set((short)0);
          return FTError.ErrorTag.ERR_OK;
        }
        advance_ref.Set((short)advance);
        table_pos += 4 + 2 * (gindex - k);
        if (table_pos + 2 > table_end) {
          bearing_ref.Set((short)0);
        } else {
          pos = face.getStream().seek(table_pos);
          bearing = face.getStream().readShort();
          bearing_ref.Set((short)bearing);
        }
      }
    } else {
      bearing_ref.Set((short)0);
      advance_ref.Set((short)0);
    }
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * tt_face_get_device_metrics
   *
   * Return the advance width table for a given pixel size if it is found
   * in the font's `hdmx' table (if any).
   *
   * =====================================================================
   */
  public static int tt_face_get_device_metrics(TTFaceRec face, int ppem, int gindex) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_face_device_metrics");
    int nn;
    int result = 0;
    long record_size = face.getHdmx_table().getRecordSize();
    int recordIdx = 8;
    // face.hdmx_table

    for (nn = 0; nn < face.getHdmx_table().getRecordCount(); nn++) {
      if (face.getHdmx_table().getRecordValue(nn) == ppem) {
        gindex += 2;
        if (gindex < record_size) {
          result = (int)(recordIdx + nn * record_size + gindex);
        }
        break;
      }
    }
    return result;
  }

  /* =====================================================================
   * tt_face_get_location
   *
   * =====================================================================
   */
  public static long tt_face_get_location(TTFaceRec face, int gindex, FTReference<Integer> size_ref) {
    long pos1;
    long pos2;
    int pIdx;
    int pLimit;
    FTReference<Integer> idx_ref = new FTReference<Integer>();
//      p -> face.glyph_locations

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_face_get_location: num_location: "+face.getLoca_table().getNum_locations()+"!");
    pos1 = 0;
    pos2 = 0;
    if (gindex < face.getLoca_table().getNum_locations()) {
      if (face.getHeader().getIndexToLocFormat() != 0) {
        pIdx = gindex * 4;
        pLimit = (face.getLoca_table().getNum_locations() * 4);
        idx_ref.Set(pIdx);
        pos1 = FTUtil.FT_NEXT_ULONG(face.getGlyph_locations(), idx_ref, pLimit);
        pIdx = idx_ref.Get();
        pos2 = pos1;
        if (pIdx + 4 <= pLimit) {
          idx_ref.Set(pIdx);
          pos2 = FTUtil.FT_NEXT_ULONG(face.getGlyph_locations(), idx_ref, pLimit);
          pIdx = idx_ref.Get();
        }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("posa: pIdx: %d pLimit: %d pos1: %d pos2: %d", pIdx, pLimit, pos1, pos2));
      } else {
        pIdx = gindex * 2;
        pLimit = (face.getLoca_table().getNum_locations() * 2);
        idx_ref.Set(pIdx);
        pos1 = FTUtil.FT_NEXT_USHORT(face.getLoca_table().getGlyph_locations(), idx_ref, pLimit);
        pIdx = idx_ref.Get();
        pos2 = pos1;
        if (pIdx + 2 <= pLimit) {
          idx_ref.Set(pIdx);
          pos2 = FTUtil.FT_NEXT_USHORT(face.getLoca_table().getGlyph_locations(), idx_ref, pLimit);
          pIdx = idx_ref.Get();
        }
        pos1 <<= 1;
        pos2 <<= 1;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("posb: pIdx: %d pLimit: %d %d %d", pIdx, pLimit, pos1, pos2));
      }
    }
    /* Check broken location data */
    if (pos1 > face.getLoca_table().getGlyf_len()) {
      FTTrace.Trace(7, TAG, String.format("tt_face_get_location: too large offset=0x%08lx "+
             "found for gid=0x%04x, exceeding the end of glyf table (0x%08x)",
             pos1, gindex, face.getLoca_table().getGlyf_len()));
      size_ref.Set(0);
      return 0;
    }
    if (pos2 > face.getLoca_table().getGlyf_len()) {
      FTTrace.Trace(7, TAG, String.format("tt_face_get_location:"+
                  " too large offset=0x%08x found for gid=0x%04x,"+
                  " truncate at the end of glyf table (0x%08x)",
                  pos2, gindex + 1, face.getLoca_table().getGlyf_len()));
      pos2 = face.getLoca_table().getGlyf_len();
    }
    /* The `loca' table must be ordered; it refers to the length of */
    /* an entry as the difference between the current and the next  */
    /* position.  However, there do exist (malformed) fonts which   */
    /* don't obey this rule, so we are only able to provide an      */
    /* upper bound for the size.                                    */
    /*                                                              */
    /* We get (intentionally) a wrong, non-zero result in case the  */
    /* `glyf' table is missing.                                     */
    if (pos2 >= pos1) {
      size_ref.Set((int)(pos2 - pos1));
    } else {
      size_ref.Set((int)(face.getLoca_table().getGlyf_len() - pos1));
    }
    return pos1;
  }

}
