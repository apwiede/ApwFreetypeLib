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
  /*    TTLocaRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType loca table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTUtil;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTLocaRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTLoca";

  private byte[] glyph_locations = null;

  /* the following fields are not in the file */
  private int num_locations = 0;
  private int glyf_len = 0;
  private long glyf_table_offset = 0L;
  private boolean short_form = true;
  private long[] glyph_offsets = null;
  private int[] glyph_lens = null;

  /* ==================== TTLocaRec ================================== */
  public TTLocaRec() {
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
    str.append("..num_locations: "+num_locations+'\n');
    str.append("..glyf_len: "+glyf_len+'\n');
    str.append("..glyf_table_offset: "+glyf_table_offset+'\n');
    return str.toString();
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: loca offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();
    int shift = 0;

      /* we need the size of the `glyf' table for malformed `loca' tables */
    length_ref.Set(glyf_len);
    error = ttface.gotoTable(TTTags.Table.glyf, stream, length_ref);
    glyf_len = length_ref.Get();
      /* it is possible that a font doesn't have a glyf table at all */
      /* or its size is zero                                         */
    if (error != FTError.ErrorTag.ERR_OK && error != FTError.ErrorTag.GLYPH_TABLE_MISSING) {
      glyf_len = 0;
    } else {
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
    }

    glyf_table_offset = stream.pos();
    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.loca, stream, length_ref);
    length = length_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }

    if (ttface.getHeader().getIndexToLocFormat() != 0) {
      short_form = false;
      shift = 2; /* long format uint32*/
      if (length >= 0x40000) {
        Log.e(TAG, "table too large");
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        return error;
      }
      num_locations = length >> shift;
    } else {
      short_form = true;
      shift = 1; /* short format uint16 */
      if (length >= 0x20000) {
        Log.e(TAG, "table too large");
        error = FTError.ErrorTag.INTERP_INVALID_TABLE;
        return error;
      }
      num_locations = length >> shift;
    }
    if (num_locations != ttface.getNum_glyphs() + 1) {
      FTTrace.Trace(7, TAG, String.format("glyph count mismatch!  loca: %d, maxp: %d",
          num_locations - 1, ttface.getNum_glyphs()));
        /* we only handle the case where `maxp' gives a larger value */
      if (num_locations <= ttface.getNum_glyphs()) {
        int new_loca_len = (ttface.getNum_glyphs() + 1) << shift;

        TTTableRec entry;
        int i = 0;
        int limit = ttface.getNum_tables();
        long pos  = stream.pos();
        int dist = 0x7FFFFFFF;

          /* compute the distance to next table in font file */
        for (i = 0; i < limit; i++) {
          entry = ttface.getDir_tables()[i];
          int diff = (int)(entry.getOffset() - pos);

          if (diff > 0 && diff < dist) {
            dist = diff;
          }
        }
        if (i == limit) {
            /* `loca' is the last table */
          dist = (int)(stream.getSize() - pos);
        }
        if (new_loca_len <= dist) {
          num_locations = ttface.getNum_glyphs() + 1;
          length = new_loca_len;
          FTTrace.Trace(7, TAG, String.format("adjusting num_locations to %d", num_locations));
        }
      }
    }
      /*
       * Extract the frame.  We don't need to decompress it since
       * we are able to parse it directly.
       */
    if (glyph_locations == null) {
      glyph_locations = new byte[length];
    }
    if (stream.readByteArray(glyph_locations, length) < 0) {
      error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
      return error;
    }
    Integer offset = 0;
    FTReference<Integer> offset_ref = new FTReference<>();
    offset_ref.Set(offset);
    int limit = glyf_len;
    int val1;
    int val2;
    glyph_offsets = new long[num_locations];
    glyph_lens = new int[num_locations];
    Integer last_offset = 0;
    for(int i = 0; i < num_locations - 1; i++) {
      if(short_form) {
        val1 = FTUtil.FT_NEXT_SHORT(glyph_locations, offset_ref, limit);
        val2 = val1;
        last_offset = offset_ref.Get();
        val2 = FTUtil.FT_NEXT_SHORT(glyph_locations, offset_ref, limit);
        offset_ref.Set(last_offset);
        val1 <<= 1;
        val2 <<= 1;
      } else {
        val1 = (int)FTUtil.FT_NEXT_ULONG(glyph_locations, offset_ref, limit);
        val2 = val1;
        last_offset = offset_ref.Get();
        val2 = (int)FTUtil.FT_NEXT_ULONG(glyph_locations, offset_ref, limit);
        offset_ref.Set(last_offset);
      }
      glyph_offsets[i] = val1 + glyf_table_offset;
      glyph_lens[i] = (int)(val2 - val1);
    }
    FTTrace.Trace(7, TAG, "loaded");
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, toDebugString());
    return error;
  }

  /* ==================== getGlyf_table_offset ================================== */
  public long getGlyf_table_offset() {
    return glyf_table_offset;
  }

  /* ==================== setGlyf_table_offset ================================== */
  public void setGlyf_table_offset(long glyf_table_offset) {
    this.glyf_table_offset = glyf_table_offset;
  }

  /* ==================== isShort_form ================================== */
  public boolean isShort_form() {
    return short_form;
  }

  /* ==================== setShort_form ================================== */
  public void setShort_form(boolean short_form) {
    this.short_form = short_form;
  }

  /* ==================== getGlyf_len ================================== */
  public int getGlyf_len() {
    return glyf_len;
  }

  /* ==================== setGlyf_len ================================== */
  public void setGlyf_len(int glyf_len) {
    this.glyf_len = glyf_len;
  }

  /* ==================== getNum_locations ================================== */
  public int getNum_locations() {
    return num_locations;
  }

  /* ==================== setNum_locations ================================== */
  public void setNum_locations(int num_locations) {
    this.num_locations = num_locations;
  }

  /* ==================== getGlyph_locations ================================== */
  public byte[] getGlyph_locations() {
    return glyph_locations;
  }

  /* ==================== setGlyph_locations ================================== */
  public void setGlyph_locations(byte[] glyph_locations) {
    this.glyph_locations = glyph_locations;
  }

  /* ==================== getGlyph_offsets ================================== */
  public long[] getGlyph_offsets() {
    return glyph_offsets;
  }

  /* ==================== setGlyph_offsets ================================== */
  public void setGlyph_offsets(long[] glyph_offsets) {
    this.glyph_offsets = glyph_offsets;
  }

  /* ==================== getGlyph_lens ================================== */
  public int[] getGlyph_lens() {
    return glyph_lens;
  }

  /* ==================== setGlyph_offsets ================================== */
  public void setGlyph_lens(int[] glyph_lens) {
    this.glyph_lens = glyph_lens;
  }

}