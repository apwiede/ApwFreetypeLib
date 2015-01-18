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

package org.apwtcl.apwfreetypelib.aftsfnt;

  /* ===================================================================== */
  /*    SFNTHeaderRec                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    SFNT file format header.                                           */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    format_tag     :: The font format tag.                             */
  /*                                                                       */
  /*    num_tables     :: The number of tables in file.                    */
  /*                                                                       */
  /*    search_range   :: Must be `16 * (max power of 2 <= num_tables)'.   */
  /*                                                                       */
  /*    entry_selector :: Must be log2 of `search_range / 16'.             */
  /*                                                                       */
  /*    range_shift    :: Must be `num_tables * 16 - search_range'.        */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class SFNTHeaderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "SFNTHeaderRec";

  public int SFNT_HEADER_SIZE = 12;

  private TTTags.Table format_tag;
  private int num_tables;
  private int search_range;
  private int entry_selector;
  private int range_shift;

  private long offset = 0L; /* not in file */ /* FIXME: have to check where it should be set different to 0 !! */

  /* ==================== SFNTHeaderRec ================================== */
  public SFNTHeaderRec() {
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
    str.append("..format_tag: "+format_tag+" "+format_tag.getDescription()+'\n');
    str.append("..num_tables: "+num_tables+'\n');
    str.append("..search_range: "+search_range+'\n');
    str.append("..entry_selector: "+entry_selector+'\n');
    str.append("..range_shift: "+range_shift+'\n');
    return str.toString();
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    int tag = stream.readInt();
    this.format_tag = TTTags.Table.getTableTag(tag);
    this.num_tables = stream.readShort();
    this.search_range = stream.readShort();
    this.entry_selector = stream.readShort();
    this.range_shift = stream.readShort();
    return error;
  }

  /* ==================== getFormatTag ================================== */
  public TTTags.Table getFormatTag() {
    return format_tag;
  }

  /* ==================== getNumTables ================================== */
  public int getNumTables() {
    return num_tables;
  }

  /* ==================== getNumTables ================================== */
  public void setNumTables(int num_tables) {
    this.num_tables = num_tables;
  }

  /* ==================== getSearchRange ================================== */
  public int getSearchRange() {
    return search_range;
  }

  /* ==================== getEntrySelector ================================== */
  public int getEntrySelecto() {
    return entry_selector;
  }

  /* ==================== getRangeShift ================================== */
  public int getRangeShift() {
    return range_shift;
  }

  /* ==================== getOffset ================================== */
  public long getOffset() {
    return offset;
  }

}