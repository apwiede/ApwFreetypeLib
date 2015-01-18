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
  /*    TTNameEntryRec                                                     */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure modeling TrueType name records.  Name records are used */
  /*    to store important strings like family name, style name,           */
  /*    copyright, etc. in _localized_ versions (i.e., language, encoding, */
  /*    etc).                                                              */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    platformID   :: The ID of the name's encoding platform.            */
  /*                                                                       */
  /*    encodingID   :: The platform-specific ID for the name's encoding.  */
  /*                                                                       */
  /*    languageID   :: The platform-specific ID for the name's language.  */
  /*                                                                       */
  /*    nameID       :: The ID specifying what kind of name this is.       */
  /*                                                                       */
  /*    stringLength :: The length of the string in bytes.                 */
  /*                                                                       */
  /*    stringOffset :: The offset to the string in the `name' table.      */
  /*                                                                       */
  /*    string       :: A pointer to the string's bytes.  Note that these  */
  /*                    are usually UTF-16 encoded characters.             */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTNameEntryRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTNameEntryRec";

  public final static int NAME_ENTRY_SIZE = 12;

  private TTTags.Platform platform_id = TTTags.Platform.APPLE_UNICODE;
  private int encoding_id = 0;
  private int language_id = 0;
  private int name_id = 0;
  private int string_length = 0;
  private long string_offset = 0;
  /* this last field is not defined in the spec */
  /* but used by the FreeType engine            */
  private byte[] string_array = null;
  private String string = null;

  /* ==================== TTNameEntryRec ================================== */
  public TTNameEntryRec() {
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
    str.append("..platform_id: "+platform_id+'\n');
    str.append("..encoding_id: "+encoding_id+'\n');
    str.append("..language_id: "+language_id+'\n');
    str.append("..name_id: "+name_id+'\n');
    str.append("..string_length: "+string_length+'\n');
    str.append("..string_offset: "+string_offset+'\n');
    return str.toString();
  }
  /* ==================== Load ===================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    platform_id = TTTags.Platform.getTableTag(stream.readShort());
    encoding_id = stream.readShort();
    language_id = stream.readShort();
    name_id = stream.readShort();
    string_length = stream.readShort();
    string_offset = stream.readShort();
    return error;
  }

  /* ==================== getPlatformId ===================================== */
  public TTTags.Platform getPlatformId() {
    return platform_id;
  }

  /* ==================== getEncodingId ===================================== */
  public int getEncodingId() {
    return encoding_id;
  }

  /* ==================== getLanguageId ===================================== */
  public int getLanguageId() {
    return language_id;
  }

  /* ==================== getNameId ===================================== */
  public int getNameId() {
    return name_id;
  }

  /* ==================== getStringLength ===================================== */
  public int getStringLength() {
    return string_length;
  }

  /* ==================== setStringLength ===================================== */
  public void setStringLength(int length) {
    this.string_length = length;
  }

  /* ==================== getStringOffset ===================================== */
  public long getStringOffset() {
    return string_offset;
  }

  /* ==================== setStringOffset ===================================== */
  public void setStringOffset(long offset) {
    this.string_offset = offset;
  }

  /* ==================== getStringArray ===================================== */
  public byte[] getStringArray() {
    return this.string_array;
  }

  /* ==================== setStringArray ===================================== */
  public void setStringArray(byte[] string_array) {
    this.string_array = string_array;
  }

  /* ==================== getString ===================================== */
  public String getString() {
    return this.string;
  }

  /* ==================== setStringArray ===================================== */
  public void setString(String string) {
    this.string = string;
  }

}