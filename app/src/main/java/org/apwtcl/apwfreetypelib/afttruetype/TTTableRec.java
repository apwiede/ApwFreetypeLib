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
  /*    TTTableRec                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure describes a given table of a TrueType font.         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    Tag      :: A four-bytes tag describing the table.                 */
  /*                                                                       */
  /*    CheckSum :: The table checksum.  This value can be ignored.        */
  /*                                                                       */
  /*    Offset   :: The offset of the table from the start of the TrueType */
  /*                font in its resource.                                  */
  /*                                                                       */
  /*    Length   :: The table length (in bytes).                           */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTTableRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTTableRec";

  private TTTags.Table tag;
  private int checksum = 0;
  private int offset = 0;
  private int length = 0;

  /* ==================== TTTableRec ================================== */
  public TTTableRec() {
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
    str.append("..checksum: 0x"+Integer.toHexString(checksum)+'\n');
    str.append("..offset: "+offset+'\n');
    str.append("..length: "+length+'\n');
    return str.toString();
  }

  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    int tag = stream.readInt();
    this.tag = TTTags.Table.getTableTag(tag);
    this.checksum = stream.readInt();
    this.offset = stream.readInt();
    this.length = stream.readInt();

    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getTag ===================================== */
  public TTTags.Table getTag() {
    return tag;
  }

  /* ==================== getCheckSum ===================================== */
  public int getCheckSum() {
    return checksum;
  }

  /* ==================== getOffset ===================================== */
  public int getOffset() {
    return offset;
  }

  /* ==================== getLength ===================================== */
  public int getLength() {
    return length;
  }

}