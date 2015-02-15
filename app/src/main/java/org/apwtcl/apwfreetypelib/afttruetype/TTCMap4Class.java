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
  /*    TTCMap4Class                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCMapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTUtil;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTCMap4Class extends TTCMapClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMap4Class";

  private final static int HEADER_SIZE = 6;   /* fields format, length, language */

  private int language = 0;
  private int num_segs = 0; /* 2 * segCount */
  private int search_range = 0; /* 2 * (2**FLOOR(log2(segCount))) */
  private int entry_selector = 0; /* log2(searchRange/2) */
  private int range_shift = 0; /* (2 * segCount) - searchRange */
  private byte[] end_code = null ; /* [segCount] 	Ending character code for each segment, last = 0xFFFF. */
  private int reserved_pad = 0; /* 	This value should be zero */
  private byte[] start_code = null; /* [segCount] 	Starting character code for each segment */
  private byte[] id_delta = null; /* [segCount] 	Delta for all character codes in segment */
  private byte[] id_range_offset = null; /* [segCount] 	Offset in bytes to glyph indexArray, or 0 */
  private byte[] glyph_index_array = null; /* [variable] 	Glyph index array */


  /* ==================== TTCMap4Class ================================== */
  public TTCMap4Class() {
    super();
    oid++;
    id = oid;

    format = TTTags.CMapFormat.CMap4;
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
    String super_str = super.toDebugString();
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("..length: "+length+'\n');
    str.append("..lanugage: "+language+'\n');
    str.append("..num_segs: "+ num_segs +'\n');
    str.append("..search_range: "+search_range+'\n');
    str.append("..entry_selector: "+entry_selector+'\n');
    str.append("..range_shift: "+range_shift+'\n');
//    str.append("..end_code: "+end_code+'\n');
    str.append("..reserved_pad: "+reserved_pad+'\n');
//    str.append("..start_code: "+start_code+'\n');
//    str.append("..id_delta: "+id_delta+'\n');
//    str.append("..id_range_offset: "+id_range_offset+'\n');
//    str.append("..glyph_index_array: "+glyph_index_array+'\n');
    return super_str + str.toString();
  }


  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    length = stream.readShort();
    language = stream.readShort();
    num_segs = stream.readShort();
        /* check that we have an even value here */
    if ((num_segs & 1) != 0) {
      return FTError.ErrorTag.LOAD_INVALID_DATA;
    }
    num_segs /= 2;
    search_range  = stream.readShort();
    entry_selector  = stream.readShort();
    range_shift = stream.readShort();
    end_code = new byte[num_segs * 2];
    stream.readByteArray(end_code, (num_segs * 2));
    reserved_pad = stream.readShort();
    start_code = new byte[num_segs * 2];
    stream.readByteArray(start_code, (num_segs * 2));
    id_delta = new byte[num_segs * 2];
    stream.readByteArray(id_delta, (num_segs * 2));
    id_range_offset = new byte[num_segs * 2];
    stream.readByteArray(id_range_offset, (num_segs * 2));
    glyph_index_array = new byte[num_segs * 2];
    stream.readByteArray(glyph_index_array, (num_segs * 2));
    Debug(0, DebugTag.DBG_CMAP, TAG, "Cmap4Class: "+toDebugString());
    return error;
  }

  /* =====================================================================
   *    tt_cmap4_validate
   *
   * =====================================================================
   */
  private FTError.ErrorTag tt_cmap4_validate(TTValidatorRec valid) {

    if (length < 16) {
      valid.setError(FTError.ErrorTag.LOAD_INVALID_TABLE);
      return FTError.ErrorTag.LOAD_INVALID_TABLE;
    }
      /* in certain fonts, the `length' field is invalid and goes */
      /* out of bound.  We try to correct this here...            */
    if (valid.getLevel().getVal() >= FTTags.Validate.TIGHT.getVal()) {
      valid.setError(FTError.ErrorTag.LOAD_INVALID_TABLE);
      return FTError.ErrorTag.LOAD_INVALID_TABLE;
    }
    if (length < 16 + (this.num_segs * 2) * 4) {
      valid.setError(FTError.ErrorTag.LOAD_INVALID_TABLE);
      return FTError.ErrorTag.LOAD_INVALID_TABLE;
    }
      /* check the search parameters - even though we never use them */
      /*                                                             */
    if (valid.getLevel().getVal() >= FTTags.Validate.PARANOID.getVal()) {
        /* check the values of `searchRange', `entrySelector', `rangeShift' */
      if (((search_range | range_shift) & 1) != 0) { /* must be even values */
        return FTError.ErrorTag.LOAD_INVALID_DATA;
      }
      search_range /= 2;
      range_shift  /= 2;
        /* `search range' is the greatest power of 2 that is <= num_segs */
      if (search_range > num_segs ||
          search_range * 2 < num_segs ||
          search_range + range_shift != num_segs ||
          search_range != (1 << entry_selector)) {
        valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
        return FTError.ErrorTag.LOAD_INVALID_DATA;
      }
    }
      /* check last segment; its end count value must be 0xFFFF */
    if (valid.getLevel().getVal() >= FTTags.Validate.PARANOID.getVal()) {
      int end_idx = (num_segs - 1) * 2;
      if (end_code[end_idx] != 0xFF || end_code[end_idx+1] != 0xFF) {
        valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
        return FTError.ErrorTag.LOAD_INVALID_DATA;
      }
    }
    {
      int n;
/* NEEDED !!! ??
      int start;
      int end;
      int offset;
      int last_start = 0;
      int last_end = 0;
      int delta;
      int p_start   = starts;
      int p_end     = ends;
      int p_delta   = deltas;
      int p_offset  = offsets;
 NEEDED !!! ?? */


      for (n = 0; n < num_segs; n++) {
/* NEEDED !!! ??
        table_idx = p_offset;
        offset_ref.Set(p_start);
        start = FTUtil.FT_NEXT_USHORT(table, offset_ref, length);
        p_start = offset_ref.Get();
        offset_ref.Set(p_end);
        end = FTUtil.FT_NEXT_USHORT(table, offset_ref, length);
        p_end = offset_ref.Get();
        offset_ref.Set(p_delta);
        delta = FTUtil.FT_NEXT_SHORT(table, offset_ref, length);
        p_delta = offset_ref.Get();
        offset_ref.Set(p_offset);
        offset = FTUtil.FT_NEXT_USHORT(table, offset_ref, length);
        p_offset = offset_ref.Get();
//System.out.println(String.format("start: %d, end: %d, delta: %d, offset: %d", start, end, delta, offset));
//System.out.println(String.format("pstart: %d, pend: %d, pdelta: %d, poffset: %d", p_start-valid.base_idx, p_end-valid.base_idx, p_delta-valid.base_idx, p_offset-valid.base_idx));
        if (start > end) {
          valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
          return FTError.ErrorTag.LOAD_INVALID_DATA;
        }
  NEEDED !!! ?? */
          /* this test should be performed at default validation level; */
          /* unfortunately, some popular Asian fonts have overlapping   */
          /* ranges in their charmaps                                   */
          /*                                                            */
/* NEEDED !!! ??
        if (start <= last_end && n > 0) {
          if (valid.getLevel().getVal() >= FTTags.Validate.TIGHT.getVal()) {
            valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
            return FTError.ErrorTagLOAD_INVALID_DATA;
          } else {
              /* allow overlapping segments, provided their start points */
              /* and end points, respectively, are in ascending order    */
              /*                                                         */
/* NEEDED !!! ??
            if (last_start > start || last_end > end) {
              error |= TTCMapRec.TT_CMAP_FLAG_UNSORTED;
            } else {
              error |= TTCMapRec.TT_CMAP_FLAG_OVERLAPPING;
            }
          }
        }
    NEEDED !!! ?? */
/* NEEDED !!! ??
        if (offset != 0 && offset != 0xFFFF) {
          table_idx += offset;  /* start of glyph ID array */
            /* check that we point within the glyph IDs table only */
/* NEEDED !!! ??
          if (valid.getLevel().getVal() >= FTTags.Validate.TIGHT.getVal()) {
            if (table_idx < glyph_ids || table_idx + (end - start + 1) * 2 > length) {
              valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
              return FTError.ErrorTag.LOAD_INVALID_DATA;
            }
            /* Some fonts handle the last segment incorrectly.  In */
            /* theory, 0xFFFF might point to an ordinary glyph --  */
            /* a cmap 4 is versatile and could be used for any     */
            /* encoding, not only Unicode.  However, reality shows */
            /* that far too many fonts are sloppy and incorrectly  */
            /* set all fields but `start' and `end' for the last   */
            /* segment if it contains only a single character.     */
            /*                                                     */
            /* We thus omit the test here, delaying it to the      */
            /* routines which actually access the cmap.            */
/* NEEDED !!! ??
          } else {
            if (n != num_segs - 1 || !(start == 0xFFFF && end == 0xFFFF)) {
              if (table_idx < glyph_ids || table_idx + (end - start + 1) * 2 > valid.limit) {
                valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
                return FTError.ErrorTag.LOAD_INVALID_DATA;
              }
            }
          }
 NEEDED !!! ?? */
            /* check glyph indices within the segment range */
/* NEEDED !!! ??
          if (valid.getLevel().getVal() >= FTTags.Validate.TIGHT.getVal()) {
            int i;
            int idx;

            for (i = start; i < end; i++) {
              offset_ref.Set(table_idx);
              idx = FTUtil.FT_NEXT_USHORT(table, offset_ref, length);
              table_idx = offset_ref.Get();
              if (idx != 0) {
                idx = (int)(idx + delta) & 0xFFFF;
                if (idx >= ((TTValidatorRec)valid).getNumGlyphs()) {
                  valid.setError(FTError.ErrorTag.LOAD_INVALID_GLYPH_ID);
                  return FTError.ErrorTag.LOAD_INVALID_GLYPH_ID;
                }
              }
            }
          } else {
            if (offset == 0xFFFF) {
                /* some fonts (erroneously?) use a range offset of 0xFFFF */
                /* to mean missing glyph in cmap table                    */
                /*                                                        */
/* NEEDED !!! ??
              if (valid.getLevel().getVal() >= FTTags.Validate.PARANOID.getVal() || n != num_segs - 1 ||
                  !(start == 0xFFFF && end == 0xFFFF)) {
                valid.setError(FTError.ErrorTag.LOAD_INVALID_DATA);
                return FTError.ErrorTag.LOAD_INVALID_DATA;
              }
            }
          }
          last_start = start;
          last_end = end;
        }
 NEEDED !!! ?? */
      }
    }
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   *    tt_cmap4_init
   *
   * =====================================================================
   */
  private FTError.ErrorTag tt_cmap4_init(TTCMap4Rec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    cmap.setCur_charcode(0xFFFFFFFF);
    cmap.setCur_gindex(0);
    cmap.setNum_ranges(num_segs);
    return error;
  }

  /* =====================================================================
   *    tt_cmap4_set_range
   *
   * =====================================================================
   */
  private int tt_cmap4_set_range(TTCMap4Rec cmap, int range_index) {
    int num_ranges = cmap.getNum_ranges();

    while (range_index < num_ranges) {
      int offset = 0;

        /* some fonts have an incorrect last segment; */
        /* we have to catch it                        */
      if (range_index >= num_ranges - 1 && cmap.getCur_start() == 0xFFFF &&
          cmap.getCur_end() == 0xFFFF) {
        TTFaceRec face  = (TTFaceRec)cmap.getFace();

        cmap.setCur_values(glyph_index_array);
        cmap.setCur_values_idx(0);
        cmap.setCur_range(range_index);
        return 0;
      }
      /* we skip empty segments */
      range_index++;
    }
    return -1;
  }

  /* =====================================================================
   *    tt_cmap4_next
   *
   * =====================================================================
   */
  private void tt_cmap4_next(TTCMap4Rec cmap) {
    int charcode;

    if (cmap.getCur_charcode() >= 0xFFFF) {
      cmap.setCur_charcode(0xFFFFFFFF);
      cmap.setCur_gindex(0);
      return;
    }
    charcode = cmap.getCur_charcode() + 1;

    if (charcode < cmap.getCur_start()) {
      charcode = cmap.getCur_start();
    }
    for ( ;; )
    {
      byte[] values = cmap.getCur_values();
      int end = cmap.getCur_end();
      int delta  = cmap.getCur_delta();

      if (charcode <= end) {
        if (values != null) {
          int table_idx = 2 * (charcode - cmap.getCur_start());

          do {
            int gindex = cmap.getCur_values()[table_idx];

            table_idx += 2;
            if (gindex != 0) {
              gindex = (int)((gindex + delta) & 0xFFFF);
              if (gindex != 0) {
                cmap.setCur_charcode(charcode);
                cmap.setCur_gindex(gindex);
                return;
              }
            }
          } while (++charcode <= end);
        } else {
          do {
            int gindex = ((charcode + delta) & 0xFFFF);

            if (gindex != 0) {
              cmap.setCur_charcode(charcode);
              cmap.setCur_gindex(gindex);
              return;
            }
          } while (++charcode <= end);
        }
      }
        /* we need to find another range */
      if (tt_cmap4_set_range(cmap, cmap.getCur_range() + 1) < 0) {
        break;
      }
      if (charcode < cmap.getCur_start()) {
        charcode = cmap.getCur_start();
      }
    }
    cmap.setCur_charcode(0xFFFFFFFF);
    cmap.setCur_gindex(0);
  }

  /* =====================================================================
   *    tt_cmap4_char_map_linear
   *
   * =====================================================================
   */
  public int tt_cmap4_char_map_linear(TTCMap4Rec cmap, FTReference<Integer> charcode_ref, boolean next) {
    int num_segs2;
    int start;
    int end;
    int offset;
    int delta;
    int i;
    int num_segs;
    int charcode = charcode_ref.Get();;
    int gindex = 0;
    int tableIdx = 0;

    num_segs2 = (int) FTCalc.FT_PAD_FLOOR(FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx), 2);
    num_segs = num_segs2 >> 1;
    if (num_segs == 0)  {
      return 0;
    }
    if (next) {
      charcode++;
    }
      /* linear search */
    for (; charcode <= 0xFFFF; charcode++) {
      int tableIdx2;


      tableIdx = 8;               /* ends table   */
      tableIdx2 = 10 + num_segs2;   /* starts table */
      for (i = 0; i < num_segs; i++) {
        end = FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx);
        tableIdx += 2;
        start = FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx2);
        tableIdx2 += 2;
        if (charcode >= start && charcode <= end) {
          tableIdx = tableIdx2 - 2 + num_segs2;
          delta = FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx);
          tableIdx += num_segs2;
          offset = FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx);
            /* some fonts have an incorrect last segment; */
            /* we have to catch it                        */
/*
          if (i >= num_segs - 1 && start == 0xFFFF && end == 0xFFFF) {
            TTFaceRec face = (TTFaceRec)cmap.face;
            int limit = face.cmap_size;

            if (offset != 0 && tableIdx + offset + 2 > limit) {
              delta  = 1;
              offset = 0;
            }
          }
*/
          if (offset == 0xFFFF) {
            continue;
          }
          if (offset != 0) {
            tableIdx += offset + (charcode - start) * 2;
            gindex = FTUtil.FT_PEEK_USHORT(cmap.getCur_values(), tableIdx);
            if (gindex != 0) {
              gindex = (int)(gindex + delta) & 0xFFFF;
            }
          } else {
            gindex = (int)(charcode + delta) & 0xFFFF;
          }
          break;
        }
      }
      if (next || gindex == 0) {
        break;
      }
    }
    if (next && gindex != 0) {
      charcode_ref.Set(charcode);
    }
    return gindex;
  }

  /* =====================================================================
   *    tt_cmap4_char_map_binary
   *
   * =====================================================================
   */
  private int tt_cmap4_char_map_binary(TTCMap4Rec cmap, FTReference<Integer> charcode_ref, boolean next) {
    int num_segs2;
    int start;
    int end;
    int offset;
    int delta;
    int max;
    int min;
    int mid;
    int num_segs;
    int charcode = charcode_ref.Get();
    int gindex   = 0;

    num_segs2 = this.num_segs * 2;
    if (num_segs2 == 0) {
      return 0;
    }
    num_segs = this.num_segs;
    if (next) {
      charcode++;
    }
    min = 0;
    max = num_segs;
      /* binary search */
    while (min < max) {
      mid = (min + max) >> 1;
      end = FTUtil.FT_PEEK_USHORT(end_code, mid * 2);
      start = FTUtil.FT_PEEK_USHORT(start_code, mid * 2);
      if (charcode < start) {
        max = mid;
      } else {
        if (charcode > end) {
          min = mid + 1;
        } else {
          delta = FTUtil.FT_PEEK_USHORT(id_delta, mid * 2);
          offset = FTUtil.FT_PEEK_USHORT(id_range_offset, mid * 2);
            /* some fonts have an incorrect last segment; */
            /* we have to catch it                        */
          if (mid >= num_segs - 1 && start == 0xFFFF && end == 0xFFFF) {
/*
            TTFaceRec face  = (TTFaceRec)cmap.face;
            int limit = (int)face.cmap_size;

            if (offset != 0 && (table_idx + offset + 2) > limit) {
              delta  = 1;
              offset = 0;
            }
*/
          }
            /* search the first segment containing `charcode' */
          if ((cmap.flags & TTTags.CMap.OVERLAPPING.getVal()) != 0) {
            int i;

              /* call the current segment `max' */
            max = mid;
            if (offset == 0xFFFF) {
              mid = max + 1;
            }
              /* search in segments before the current segment */
            for (i = max ; i > 0; i--) {
              int prev_end;

              prev_end = FTUtil.FT_PEEK_USHORT(end_code, (i - 1) * 2);
              if (charcode > prev_end) {
                break;
              }
              end = prev_end;
              start = FTUtil.FT_PEEK_USHORT(start_code, (i - 1) * 2);
              delta = FTUtil.FT_PEEK_USHORT(id_delta, (i - 1) * 2);
              offset = FTUtil.FT_PEEK_USHORT(id_range_offset, (i - 1) * 2);
              if (offset != 0xFFFF) {
                mid = i - 1;
              }
            }
              /* no luck */
            if (mid == max + 1) {
              if (i != max) {
                end = FTUtil.FT_PEEK_USHORT(end_code, max * 2);
                start = FTUtil.FT_PEEK_USHORT(start_code, max * 2);
                delta = FTUtil.FT_PEEK_USHORT(id_delta, max * 2);
                offset = FTUtil.FT_PEEK_USHORT(id_range_offset, max * 2);
              }
              mid = max;
                /* search in segments after the current segment */
              for (i = max + 1; i < num_segs; i++) {
                int next_end;
                int next_start;

                next_end = FTUtil.FT_PEEK_USHORT(end_code, i * 2);
                next_start = FTUtil.FT_PEEK_USHORT(start_code, i * 2);
                if (charcode < next_start) {
                  break;
                }
                end = next_end;
                start = next_start;
                delta  = FTUtil.FT_PEEK_USHORT(id_delta, i * 2);
                offset = FTUtil.FT_PEEK_USHORT(id_range_offset, i * 2);
                if (offset != 0xFFFF) {
                  mid = i;
                }
              }
              i--;
                /* still no luck */
              if (mid == max) {
                mid = i;
                break;
              }
            }
              /* end, start, delta, and offset are for the i'th segment */
            if (mid != i) {
              end = FTUtil.FT_PEEK_USHORT(end_code, mid * 2);
              start = FTUtil.FT_PEEK_USHORT(start_code, mid * 2);
              delta = FTUtil.FT_PEEK_USHORT(id_delta, mid * 2);
              offset = FTUtil.FT_PEEK_USHORT(id_range_offset, mid * 2);
            }
          } else {
            if (offset == 0xFFFF) {
              break;
            }
          }
          if (offset != 0) {
            gindex = FTUtil.FT_PEEK_USHORT(glyph_index_array, (charcode - start) * 2);
            if (gindex != 0) {
              gindex = (gindex + delta) & 0xFFFF;
            }
          } else {
            gindex = (charcode + delta) & 0xFFFF;
          }
          break;
        }
      }
      if (next) {
        TTCMap4Rec cmap4 = (TTCMap4Rec)cmap;
          /* if `charcode' is not in any segment, then `mid' is */
          /* the segment nearest to `charcode'                  */
          /*                                                    */
        if (charcode > end) {
          mid++;
          if (mid == num_segs) {
            return 0;
          }
        }
        if (tt_cmap4_set_range(cmap4, mid) != 0) {
          if (gindex != 0) {
            charcode_ref.Set(charcode);
          }
        } else {
          cmap4.setCur_charcode(charcode);
          if (gindex != 0) {
            cmap4.setCur_gindex(gindex);
          } else {
            cmap4.setCur_charcode (charcode);
            tt_cmap4_next(cmap4);
            gindex = cmap4.getCur_gindex();
          }
          if (gindex != 0) {
            charcode_ref.Set(cmap4.getCur_charcode());
          }
        }
      }
    }
    return gindex;
  }

  /* =====================================================================
   *    tt_cmap4_char_index
   *
   * =====================================================================
   */
  private int tt_cmap4_char_index(TTCMap4Rec cmap, int charcode) {
    FTReference<Integer> charcode_ref = new FTReference<Integer>();
    if (charcode >= 0x10000) {
      return 0;
    }
    charcode_ref.Set(charcode);
    if ((cmap.flags & TTTags.CMap.UNSORTED.getVal()) != 0) {
      return tt_cmap4_char_map_linear(cmap, charcode_ref, false);
    } else {
      return tt_cmap4_char_map_binary(cmap, charcode_ref, false);
    }
  }

  /* =====================================================================
   *    tt_cmap4_char_next
   *
   * =====================================================================
   */
  public static int tt_cmap4_char_next(Object ... args) {
    int error = 0;

    Log.e(TAG, "FTCMapFuncs  tt_cmap4_char_next: not yet implemented");
    return error;
  }

  /* ==================== initCMap ===================================== */
  @Override
  public FTError.ErrorTag initCMap(FTCMapRec cmap) {
    return tt_cmap4_init((TTCMap4Rec)cmap);
  }

  /* ==================== doneCMap ===================================== */
  @Override
  public FTError.ErrorTag doneCMap(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "doneCMap not yet implemented");
    return error;
  }

  /* =====================================================================
   *    tt_cmap4_get_info
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_cmap4_get_info(FTCMapRec cmap) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "FTCMapFuncs  tt_cmap4_get_info: not yet implemented");
    return error;
  }

  /* ==================== validate ===================================== */
  @Override
  public FTError.ErrorTag validate(TTValidatorRec valid) {
    return tt_cmap4_validate(valid);
  }

  /* ==================== getCmapInfo ===================================== */
  @Override
  public FTError.ErrorTag getCmapInfo(FTCMapRec cmap) {
    return tt_cmap4_get_info(cmap);
  }

  /* ==================== charIndex ===================================== */
  @Override
  public int charIndex(FTCMapRec cmap, int char_code) {
    return tt_cmap4_char_index((TTCMap4Rec)cmap, char_code);
  }

  /* ==================== charNext ===================================== */
  @Override
  public int charNext(FTCMapRec cmap, FTReference<Integer> char_code_ref) {
    Log.e(TAG, "charNext not yet implemented");
    return -1;
  }

}
