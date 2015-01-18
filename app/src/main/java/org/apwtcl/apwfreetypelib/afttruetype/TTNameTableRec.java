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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTUtil;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;


  /* ===================================================================== */
  /*    TTNameTableRec                                                     */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure modeling the TrueType name table.                      */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    format         :: The format of the name table.                    */
  /*                                                                       */
  /*    numNameRecords :: The number of names in table.                    */
  /*                                                                       */
  /*    storageOffset  :: The offset of the name table in the `name'       */
  /*                      TrueType table.                                  */
  /*                                                                       */
  /*    names          :: An array of name records.                        */
  /*                                                                       */
  /*    stream         :: the file's input stream.                         */
  /*                                                                       */
  /* ===================================================================== */

public class TTNameTableRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTNameTableRec";

  public final static int NAME_TABLE_HEADER_SIZE = 6;

  // header
  private int format = 0;
  private int num_name_records = 0;
  private int storage_offset = 0;

  //entries
  private TTNameEntryRec[] names = null;

  /* ==================== TTNameTableRec ================================== */
  public TTNameTableRec() {
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
    return str.toString();
  }


  /* =====================================================================
   * convert_ascii_from_utf16
   * =====================================================================
   */
  /* convert a UTF-16 name entry to ASCII */
  private String convert_ascii_from_utf16(TTNameEntryRec entry) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "convert_ascii_from_utf16");

    int len;
    int code;
    int n;
    byte[] str;
    Integer offset = 0;
    FTReference<Integer> offset_ref = new FTReference<>();

    len = entry.getStringLength() / 2;
    str = new byte[len];
    for (n = 0; n < len; n++) {
      offset_ref.Set(offset);
      code = FTUtil.FT_NEXT_USHORT(entry.getStringArray(), offset_ref, offset + 2);
      offset += 2;
      if (code == 0) {
        break;
      }
      if (code < 32 || code > 127) {
        code = (int)'?';
      }
      str[n] = (byte)code;
    }
    entry.setString(new String(str));
    return entry.getString();
  }

  /* =====================================================================
   * convert_ascii_from_other
   * =====================================================================
   */
  /* convert an Apple Roman or symbol name entry to ASCII */
  public static String convert_ascii_from_other(TTNameEntryRec entry) {
    String string = null;
    Log.e(TAG, "convert_ascii_from_other not yet implemented");
/*
    Integer len;
    Integer code;
    Integer n;
    Byte[] read = (Byte)entry.string;
    int error;

    len = entry.stringLength;
    if (FT_NEW_ARRAY(string, len + 1)) {
      return null;
    }
    for (n = 0; n < len; n++) {
      code = *read++;
      if (code == 0) {
        break;
      }
      if (code < 32 || code > 127) {
        code = '?';
      }
      string[n] = (char)code;
    }
    string[n] = 0;
*/
    return string;
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: name offset: " + stream.pos() + "!");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int count;
    int i;
    long table_pos = 0;
    long storage_start = 0;
    long storage_limit = 0;
    int table_length = 0;
    FTReference<Integer> length_ref = new FTReference<Integer>();

    error = ttface.gotoTable(TTTags.Table.name, stream, length_ref);
    table_length = length_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    table_pos = stream.pos();

    /* read name table header */
    format = stream.readShort();
    num_name_records = stream.readShort();
    storage_offset = stream.readShort();

    /* Some popular Asian fonts have an invalid `storageOffset' value */
    /* (it should be at least "6 + 12*num_names"). However, the string */
    /* offsets, computed as "storageOffset + entry.stringOffset", are */
    /* valid pointers within the name table... */
    /*                                    */
    /* We thus can't check `storageOffset' right now. */
    /*                                    */
    storage_start = table_pos + NAME_TABLE_HEADER_SIZE + TTNameEntryRec.NAME_ENTRY_SIZE * num_name_records;
    storage_limit = table_pos + table_length;
    if (storage_start > storage_limit) {
      FTTrace.Trace(7, TAG, "tt_face_load_name: invalid `name' table");
      error = FTError.ErrorTag.LOAD_NAME_TABLE_MISSING;
      return error;
    }
      /* Allocate the array of name records. */
    count = num_name_records;
    num_name_records = 0;
    names = new TTNameEntryRec[count];
    if (names == null) {
      error = FTError.ErrorTag.LOAD_UNKNOWN_FILE_FORMAT;
      return error;
    }
      /* Load the name records and determine how much storage is needed */
      /* to hold the strings themselves. */
    {
      TTNameEntryRec entry;

      for (i = 0; i < count; i++) {
        names[i] = new TTNameEntryRec();
        entry = names[i];
        entry.Load(stream);
        if (entry == null) {
          continue;
        }
          /* check that the name is not empty */
        if (entry.getStringLength() == 0) {
          continue;
        }
          /* check that the name string is within the table */
        entry.setStringOffset(entry.getStringOffset() + table_pos + storage_offset);
        if (entry.getStringOffset() < storage_start
            || entry.getStringOffset() + entry.getStringLength() > storage_limit) {
              /* invalid entry - ignore it */
          entry.setStringOffset(0);
          entry.setStringLength(0);
          continue;
        }
      }
      num_name_records = i;
    }
      /* everything went well, update face->num_names */
    ttface.setNum_names(num_name_records);
    return error;
  }

  /* ==================== getFormat ===================================== */
  public int getFormat() {
    return format;
  }

  /* ==================== getNumNameRecords ===================================== */
  public int getNumNameRecords() {
    return num_name_records;
  }

  /* ==================== getStorageOffset ===================================== */
  public int getStorageOffset() {
    return storage_offset;
  }

  /* ==================== getNames ===================================== */
  public TTNameEntryRec[] getNames() {
    return names;
  }

  /* ==================== getNameRec ===================================== */
  public TTNameEntryRec getNameRec(int idx) {
    return names[idx];
  }

  /* ==================== reset ===================================== */
  public void reset() {
    num_name_records = 0;
    format = 0;
    storage_offset = 0;
  }

  /*
   * =====================================================================
   * getName
   *
   * <Description> Returns a given ENGLISH name record in ASCII.
   * <Input> face :: A handle to the source face object.
   * nameid :: The name id of the name record to return.
   * <InOut> name :: The address of a string pointer. NULL if no name is
   * present.
   * <Return> FreeType error code. 0 means success.
   * =====================================================================
   */
  public FTError.ErrorTag getName(TTFaceRec face, int nameid, FTReference<String> name_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    String result = null;
    int n;
    TTNameEntryRec rec = null;
    int found_apple = -1;
    int found_apple_roman = -1;
    int found_apple_english = -1;
    int found_win = -1;
    int found_unicode = -1;
    boolean is_english = false;
    String convert = null;

    for (n = 0; n < num_name_records; n++) {
      rec = names[n];
        /* According to the OpenType 1.3 specification, only Microsoft or */
        /* Apple platform IDs might be used in the `name' table. The */
        /* `Unicode' platform is reserved for the `cmap' table, and the */
        /* `ISO' one is deprecated. */
        /*                                   */
        /* However, the Apple TrueType specification doesn't say the same */
        /* thing and goes to suggest that all Unicode `name' table entries */
        /* should be coded in UTF-16 (in big-endian format I suppose). */
        /*                                   */
      if (rec.getNameId() == nameid && rec.getStringLength() > 0) {
        switch (rec.getPlatformId()) {
          case APPLE_UNICODE:
          case ISO:
            /* there is `languageID' to check there. We should use this */
            /* field only as a last solution when nothing else is */
            /* available. */
            /*                                 */
            found_unicode = n;
            break;
          case MACINTOSH:
            /* This is a bit special because some fonts will use either */
            /*
             * an English language id, or a Roman encoding id, to
             * indicate
             */
            /* the English version of its font name. */
            /*                                   */
            if (rec.getLanguageId() == TTTags.MacLangId.ENGLISH.getVal()) {
              found_apple_english = n;
            } else {
              if (rec.getEncodingId() == TTTags.MacId.ROMAN.getVal()) {
                found_apple_roman = n;
              }
            }
            break;
          case MICROSOFT:
            /* we only take a non-English name when there is nothing */
            /* else available in the font */
            /*                             */
            if (found_win == -1 || (rec.getLanguageId() & 0x3FF) == 0x009) {
              switch (TTTags.MsId.getTableTag(rec.getEncodingId())) {
                case SYMBOL_CS:
                case UNICODE_CS:
                case UCS_4:
                  is_english = ((rec.getLanguageId() & 0x3FF) == 0x009);
                  found_win = n;
                  break;
                default:
                  ;
              }
            }
            break;
          default:
            ;
        }
      }
    }
    found_apple = found_apple_roman;
    if (found_apple_english >= 0) {
      found_apple = found_apple_english;
    }
      /* some fonts contain invalid Unicode or Macintosh formatted entries; */
      /* we will thus favor names encoded in Windows formats if available */
      /* (provided it is an English name) */
      /*                                      */
    convert = null;
    if (found_win >= 0 && !(found_apple >= 0 && !is_english)) {
      rec = face.getName_table().getNames()[found_win];
      switch (TTTags.MsId.getTableTag(rec.getEncodingId())) {
        /* all Unicode strings are encoded using UTF-16BE */
        case UNICODE_CS:
        case SYMBOL_CS:
          convert = "convert_ascii_from_utf16";
          break;
        case UCS_4:
          /*
           * Apparently, if this value is found in a name table entry, it
           * is
           */
          /* documented as `full Unicode repertoire'. Experience with the */
          /*
           * MsGothic font shipped with Windows Vista shows that this
           * really
           */
          /* means UTF-16 encoded names (UCS-4 values are only used within */
          /* charmaps). */
          convert = "convert_ascii_from_utf16";
          break;
        default:
          ;
      }
    } else {
      if (found_apple >= 0) {
        rec = face.getName_table().getNames()[found_apple];
        convert = "convert_ascii_from_other";
      } else {
        if (found_unicode >= 0) {
          rec = face.getName_table().getNames()[found_unicode];
          convert = "convert_ascii_from_utf16";
        }
      }
    }
    if (rec != null && convert != null) {
      if (rec.getStringArray() == null) {
        rec.setStringArray(new byte[rec.getStringLength()]);
        if (face.getStream().seek(rec.getStringOffset()) < 0
            || face.getStream().readByteArray(rec.getStringArray(), rec.getStringLength()) < 0) {
          // FT_FREE( rec->string );
          rec.setStringLength(0);
          name_ref.Set(null);
          return error;
        }
      }
      if (convert.equals("convert_ascii_from_utf16")) {
        result = convert_ascii_from_utf16(rec);
      } else {
        if (convert.equals("convert_ascii_from_other")) {
          result = convert_ascii_from_other(rec);
        }
      }
    }
    name_ref.Set(result);
    return error;
  }

}
