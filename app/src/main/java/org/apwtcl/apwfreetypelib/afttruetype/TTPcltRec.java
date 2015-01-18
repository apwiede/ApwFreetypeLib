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
  /*    TTPcltRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType PCLT table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;

public class TTPcltRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTPclt";

  private int version = 0;
  private int font_number = 0;
  private int pitch = 0;
  private int x_height = 0;
  private int style = 0;
  private int type_family = 0;
  private int cap_height = 0;
  private int symbol_set = 0;
  private char[] type_face = new char[16];
  private char[] character_complement = new char[8];
  private char[] file_name = new char[6];
  private char stroke_weight;
  private char width_type;
  private byte serif_style;

  /* ==================== TTPclt ================================== */
  public TTPcltRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: pclt offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.PCLT, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }

    version = stream.readInt();
    font_number = stream.readInt();
    pitch = stream.readShort();
    x_height = stream.readShort();
    style = stream.readShort();
    type_family = stream.readShort();
    cap_height = stream.readShort();
    symbol_set = stream.readShort();
    for (int i = 0; i < 16; i++) {
      type_face[i] = (char)stream.readByte();
    }
    for (int i = 0; i < 8; i++) {
      character_complement[i] = (char)stream.readByte();
    }
    for (int i = 0; i < 6; i++) {
      file_name[i] = (char)stream.readByte();
    }
    stroke_weight = (char)stream.readByte();
    width_type = (char)stream.readByte();
    serif_style = stream.readByte();
    return error;
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

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== setVersion ================================== */
  public void setVersion(int version) {
    this.version = version;
  }

  /* ==================== getFontNumber ================================== */
  public int getFontNumber() {
    return font_number;
  }

  /* ==================== getPitch ================================== */
  public int getPitch() {
    return pitch;
  }

  /* ==================== getXHeight ================================== */
  public int getXHeight() {
    return x_height;
  }

  /* ==================== getStyle ================================== */
  public int getStyle() {
    return style;
  }

  /* ==================== getTypeFamily ================================== */
  public int getTypeFamily() {
    return type_family;
  }

  /* ==================== getCapHeight ================================== */
  public int getCapHeight() {
    return cap_height;
  }

  /* ==================== getSymbolSet ================================== */
  public int getSymbolSet() {
    return symbol_set;
  }

  /* ==================== getTypeFace ================================== */
  public char[] getTypeFace() {
    return type_face;
  }

  /* ==================== getCharacterComplement ================================== */
  public char[] getCharacterComplement() {
    return character_complement;
  }

  /* ==================== getFileName ================================== */
  public char[] getFileName() {
    return file_name;
  }

  /* ==================== getStrokeWeight ================================== */
  public char getStrokeWeight() {
    return stroke_weight;
  }

  /* ==================== getWidthType ================================== */
  public char getWidthType() {
    return width_type;
  }

  /* ==================== getSerifStyle ================================== */
  public byte getSerifStyle() {
    return serif_style;
  }

}
