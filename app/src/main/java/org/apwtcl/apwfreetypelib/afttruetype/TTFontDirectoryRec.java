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
  /*    TTTableDirectoryRec                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftsfnt.FTSfntTableTag;
import org.apwtcl.apwfreetypelib.aftsfnt.SFNTHeaderRec;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTFontDirectoryRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTFontDirectoryRec";

  /* ==================== TTTableRec ================================== */
  public TTFontDirectoryRec() {
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
//    str.append("..tag: "+tag+" "+tag.getDescription()+'\n');
//    str.append("..checksum: 0x"+Integer.toHexString(checksum)+'\n');
//    str.append("..offset: "+offset+'\n');
//    str.append("..length: "+length+'\n');
    return str.toString();
  }

  /*
   * =====================================================================
   * check_table_dir
   *
   * Here, we
   *
   * - check that `num_tables' is valid (and adjust it if necessary)
   *
   * - look for a `head' table, check its size, and parse it to check whether
   * its `magic' field is correctly set
   *
   * - errors (except errors returned by stream handling)
   *
   * SFNT_Err_Unknown_File_Format: no table is defined in directory, it is not
   * sfnt-wrapped data SFNT_Err_Table_Missing: table directory is valid, but
   * essential tables (head/bhed/SING) are missing
   *
   * =====================================================================
   */
  private FTError.ErrorTag check_table_dir(SFNTHeaderRec sfnt, FTStreamRec stream) {
    FTError.ErrorTag error;
    int nn;
    int valid_entries = 0;
    int has_head = 0;
    int has_sing = 0;
    int has_meta = 0;
    long offset = sfnt.getOffset() + sfnt.SFNT_HEADER_SIZE;
    TTTableRec entry = new TTTableRec();

    if (stream.seek(offset) < 0) {
      error = FTError.ErrorTag.LOAD_STREAM_CANNOT_SEEK;
      return error;
    }
    for (nn = 0; nn < sfnt.getNumTables(); nn++) {
      error = entry.Load(stream) ;
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, String.format("check_table_dir: can read only %d table%s "
                + "in font (instead of %d)", nn, nn == 1 ? "" : "s", sfnt.getNumTables()));
        sfnt.setNumTables(nn);
        break;
      }
        /* we ignore invalid tables */
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, entry.toDebugString());
      if ((entry.getOffset() + entry.getLength()) > stream.getSize()) {
        FTTrace.Trace(7, TAG, String.format("check_table_dir: table entry %d invalid", nn));
        continue;
      } else {
        valid_entries++;
      }
      if (entry.getTag() == TTTags.Table.head
          || entry.getTag() == TTTags.Table.bhed) {
        Integer magic;
        if (entry.getTag() == TTTags.Table.head) {
          has_head = 1;
        }
          /*
           * The table length should be 0x36, but certain font tools make
           * it 0x38, so we will just check that it is greater.
           *
           * Note that according to the specification, the table must be
           * padded to 32-bit lengths, but this doesn't apply to the value
           * of its `Length' field!
           */
        if (entry.getLength() < 0x36) {
          FTTrace.Trace(7, TAG, "check_table_dir: `head' table too small");
          error = FTError.ErrorTag.LOAD_TABLE_MISSING;
          return error;
        }
        if (stream.seek(entry.getOffset() + 12) < 0) {
          return error;
        }
        magic = (int) stream.readInt();
        if (magic < 0) {
          return error;
        }
        if (magic != 0x5F0F3CF5L) {
          FTTrace.Trace(7, TAG,"check_table_dir: no magic number found in `head' table");
          error = FTError.ErrorTag.LOAD_TABLE_MISSING;
          return error;
        }
        if (stream.seek(offset + (nn + 1) * 16) < 0) {
          return error;
        }
      } else {
        if (entry.getTag() == TTTags.Table.SING) {
          has_sing = 1;
        } else {
          if (entry.getTag() == TTTags.Table.META) {
            has_meta = 1;
          }
        }
      }
    }
    sfnt.setNumTables(valid_entries);
    if (sfnt.getNumTables() == 0) {
      FTTrace.Trace(7, TAG, "check_table_dir: no tables found");
      error = FTError.ErrorTag.INTERP_UNKNOWN_FILE_FORMAT;
      return error;
    }
      /* if `sing' and `meta' tables are present, there is no `head' table */
    if (has_head != 0 || (has_sing != 0 && has_meta != 0)) {
      error = FTError.ErrorTag.ERR_OK;
      return error;
    } else {
      FTTrace.Trace(7, TAG,"check_table_dir: neither `head' nor `sing' table found");
      error = FTError.ErrorTag.LOAD_TABLE_MISSING;
    }
    return error;
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(TTFaceRec face, FTStreamRec stream) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load called");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTTableRec entry;
    SFNTHeaderRec sfnt = new SFNTHeaderRec();
      /* load the table directory */
    sfnt.Load(stream);
    FTTrace.Trace(7, TAG, String.format("-- Number of tables: %10d", sfnt.getNumTables()));
    FTTrace.Trace(7, TAG, String.format("-- Format version:   "+sfnt.getFormatTag()));
    if (sfnt.getFormatTag() != TTTags.Table.OTTO) {
        /* check first */
      error = check_table_dir(sfnt, stream);
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG,"Load: invalid table directory for TrueType");
        return error;
      }
    }
    face.setNum_tables(sfnt.getNumTables());
    face.setFormat_tag(sfnt.getFormatTag());
    face.setDir_tables(new TTTableRec[face.getNum_tables()]);
    stream.seek(sfnt.getOffset() + sfnt.SFNT_HEADER_SIZE);
    error = stream.FTStreamEnterFrame(face.getNum_tables() * 16L);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    FTTrace.Trace(7, TAG, "  tag     offset    length   checksum");
    FTTrace.Trace(7, TAG, "  ----------------------------------");
    for (int nn = 0; nn < sfnt.getNumTables(); nn++) {
      face.getDir_tables()[nn] = new TTTableRec();
      entry = face.getDir_tables()[nn];
      entry.Load(stream);
        /* ignore invalid tables */
      if (entry.getOffset() + entry.getLength() > stream.getSize()) {
        continue;
      } else {
        FTTrace.Trace(7, TAG, String.format("  %-4s  %08x  %08x  %08x",
            entry.getTag(), entry.getOffset(), entry.getLength(), entry.getCheckSum()));
      }
    }
    FTTrace.Trace(7, TAG, "font directory loaded\n");
    return error;

  }

}