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
  /*    TTTableOffsetRec                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftsfnt.FTSfntTableTag;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTTableOffsetRec extends FTDebug {
  private static int oid = 0;
    
  private int id;
  private static String TAG = "TTFontDirectoryRec";
    
  private TTTags.Table tag;
  private int num_tables = 0;
  private int search_range = 0;
  private int entry_selector = 0;
  private int range_shift = 0;

  /* ==================== TTFontDirectoryRec ================================== */
  public TTTableOffsetRec() {
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
    str.append("..num_tables: "+num_tables+'\n');
    str.append("..search_range: "+search_range+'\n');
    str.append("..entry_selector: "+entry_selector+'\n');
    str.append("..range_shift: "+range_shift+'\n');
    return str.toString();
  }

  /* ==================== GetNumTables ===================================== */
  public int GetNumTables() {
    return num_tables;
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    int tag = stream.readInt();
    this.tag = TTTags.Table.getTableTag((int)tag);
    this.num_tables = (int)(stream.readShort() & 0xFFFF);
    this.search_range = (int)(stream.readShort() & 0xFFFF);
    this.entry_selector = (int)(stream.readShort() & 0xFFFF);
    this.range_shift = (int)(stream.readShort() & 0xFFFF);
        
    return FTError.ErrorTag.ERR_OK;
  }

}
