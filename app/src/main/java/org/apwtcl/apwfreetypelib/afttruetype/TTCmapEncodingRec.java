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

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTCmapEncodingRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCmapEncodingRec";

  protected int platform_id = 0;
  protected int encoding_id = 0;
  protected int offset = 0;

  /* the following fields are not in the file */
  private FTTags.Encoding encoding = FTTags.Encoding.NONE;

  /* ==================== FTCharMapRec ================================== */
  public TTCmapEncodingRec() {
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
    str.append("..platform_id: "+platform_id+'\n');
    str.append("..encoding_id: "+encoding_id+'\n');
    str.append("..offset: "+offset+'\n');
    return str.toString();
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
    Debug(0, FTDebug.DebugTag.DBG_LOAD_FACE, TAG, "Load cmap encoding");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    platform_id = stream.readShort();
    encoding_id = stream.readShort();
    offset = stream.readInt();
    Debug(0, FTDebug.DebugTag.DBG_CMAP, TAG, "after Load cmap ft: " + this.toDebugString());
    return error;
  }

  /* ==================== getPlatformId ================================== */
  public int getPlatformId() {
    return platform_id;
  }

  /* ==================== getEncodingId ================================== */
  public int getEncodingId() {
    return encoding_id;
  }

  /* ==================== getOffset ================================== */
  public int getOffset() {
    return offset;
  }

  /* ==================== getEncoding ================================== */
  public FTTags.Encoding getEncoding() {
    return encoding;
  }

  /* ==================== getEncoding ================================== */
  public void setEncoding(FTTags.Encoding encoding) {
    this.encoding = encoding;
  }

}
