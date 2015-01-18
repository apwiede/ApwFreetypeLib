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
  /*    TTOS2                                                              */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType OS/2 table. This is the long  */
  /*    table version.  All fields comply to the TrueType specification.   */
  /*                                                                       */
  /*    Note that we now support old Mac fonts which do not include an     */
  /*    OS/2 table.  In this case, the `version' field is always set to    */
  /*    0xFFFF.                                                            */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTOs2Rec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTOs2";

  private int version = 0;                /* 0x0001 - more or 0xFFFF */
  private int x_avg_char_width = 0;
  private int us_weight_class = 0;
  private int us_width_class = 0;
  private int fs_type = 0;
  private int y_subscript_x_size = 0;
  private int y_subscript_y_size = 0;
  private int y_subscript_x_offset = 0;
  private int y_subscript_y_offset = 0;
  private int y_superscript_x_size = 0;
  private int y_superscript_y_size = 0;
  private int y_superscript_x_offset = 0;
  private int y_superscript_y_offset = 0;
  private int y_strikeout_size = 0;
  private int y_strikeout_position = 0;
  private int s_family_class = 0;
  private byte[] panose = new byte[10];
  private int ul_unicode_range1 = 0;        /* Bits 0-31   */
  private int ul_unicode_range2 = 0;        /* Bits 32-63  */
  private int ul_unicode_range3 = 0;        /* Bits 64-95  */
  private int ul_unicode_range4 = 0;        /* Bits 96-127 */
  private char[] ach_vend_id = new char[4];
  private int fs_selection = 0;
  private int us_first_char_index = 0;
  private int us_last_char_index = 0;
  private int s_typo_ascender = 0;
  private int s_typo_descender = 0;
  private int s_typo_line_gap = 0;
  private int us_win_ascent = 0;
  private int us_win_descent = 0;
  /* only version 1 tables: */
  private int ul_code_page_range1 = 0;       /* Bits 0-31   */
  private int ul_code_page_range2 = 0;       /* Bits 32-63  */
  /* only version 2 tables: */
  private int sx_height = 0;
  private int s_cap_height = 0;
  private int us_default_char = 0;
  private int us_break_char = 0;
  private int us_max_context = 0;

  /* ==================== TTOS2 ================================== */
  public TTOs2Rec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: os2 offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.OS2, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }


    version = stream.readShort();
    x_avg_char_width = stream.readShort();
    us_weight_class = stream.readShort();
    us_width_class = stream.readShort();
    fs_type = stream.readShort();
    y_subscript_x_size = stream.readShort();
    y_subscript_y_size = stream.readShort();
    y_subscript_x_offset = stream.readShort();
    y_subscript_y_offset = stream.readShort();
    y_superscript_x_size = stream.readShort();
    y_superscript_y_size = stream.readShort();
    y_superscript_x_offset = stream.readShort();
    y_superscript_y_offset = stream.readShort();
    y_strikeout_size = stream.readShort();
    y_strikeout_position = stream.readShort();
    s_family_class = stream.readShort();
    for (int i = 0; i < 10; i++) {
      panose[i] = stream.readByte();
    }
    ul_unicode_range1 = stream.readInt();
    ul_unicode_range2 = stream.readInt();
    ul_unicode_range3 = stream.readInt();
    ul_unicode_range4 = stream.readInt();
    for (int i = 0; i < 4; i++) {
      ach_vend_id[i] = (char)stream.readByte();
    }
    fs_selection = stream.readShort();
    us_first_char_index = stream.readShort();
    us_last_char_index = stream.readShort();
    s_typo_ascender = stream.readShort();
    s_typo_descender = stream.readShort();
    s_typo_line_gap = stream.readShort();
    us_win_ascent = stream.readShort();
    us_win_descent = stream.readShort();

    if (version >= 0x0001) {
          /* only version 1 tables */
      ul_code_page_range1 = stream.readInt();       /* Bits 0-31   */
      ul_code_page_range2 = stream.readInt();       /* Bits 32-63  */
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      if (version >= 0x0002) {
            /* only version 2 tables */
        sx_height = stream.readShort();
        s_cap_height = stream.readShort();
        us_default_char = stream.readShort();
        us_break_char = stream.readShort();
        us_max_context = stream.readShort();
        if (error != FTError.ErrorTag.ERR_OK) {
          return error;
        }
      }
    }
    FTTrace.Trace(7, TAG, String.format("s_typo_ascender:  %4d", s_typo_ascender));
    FTTrace.Trace(7, TAG, String.format("s_typo_descender: %4d", s_typo_descender));
    FTTrace.Trace(7, TAG, String.format("us_win_ascent:    %4d", us_win_ascent));
    FTTrace.Trace(7, TAG, String.format("us_win_descent:   %4d", us_win_descent));
    FTTrace.Trace(7, TAG, String.format("fs_selection:    0x%02x", fs_selection));
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

  /* ==================== getVersion ===================================== */
  public int getVersion() {                /* 0x0001 - more or 0xFFFF */
    return version;
  }

  /* ==================== setVersion ===================================== */
  public void setVersion(int version) {                /* 0x0001 - more or 0xFFFF */
    this.version = version;
  }

  /* ==================== getXAvgCharWidth ===================================== */
  public int  getXAvgCharWidth() {
    return x_avg_char_width;
  }

  /* ==================== getUsWeightClass ===================================== */
  public int getUsWeightClass() {
    return us_weight_class;
  }

  /* ==================== getUsWidthClass ===================================== */
  public int getUsWidthClass() {
    return us_width_class;
  }

  /* ==================== getFsType ===================================== */
  public int getFsType() {
    return fs_type;
  }

  /* ==================== getYSubscriptXSize ===================================== */
  public int getYSubscriptXSize() {
    return y_subscript_x_size;
  }

  /* ==================== getYSubscriptYSize ===================================== */
  public int getYSubscriptYSize() {
    return y_subscript_y_size;
  }

  /* ==================== getYSubscriptXOffset ===================================== */
  public int getYSubscriptXOffset() {
    return y_subscript_x_offset;
  }

  /* ==================== getYSubscriptYOffset ===================================== */
  public int getYSubscriptYOffset() {
    return y_subscript_y_offset;
  }

  /* ==================== getYSuperscriptXSize ===================================== */
  public int getYSuperscriptXSize() {
    return y_superscript_x_size;
  }

  /* ==================== getYSuperscriptYSize ===================================== */
  public int getYSuperscriptYSize() {
    return y_superscript_y_size;
  }

  /* ==================== getYSuperscriptXOffset ===================================== */
  public int getYSuperscriptXOffset() {
    return y_superscript_x_offset;
  }

  /* ==================== getYSuperscriptYOffset ===================================== */
  public int getYSuperscriptYOffset() {
    return y_superscript_y_offset;
  }

  /* ==================== getYStrikeoutSize ===================================== */
  public int getYStrikeoutSize() {
    return y_strikeout_size;
  }

  /* ==================== getYStrikeoutPosition ===================================== */
  public int getYStrikeoutPosition() {
    return y_strikeout_position;
  }

  /* ==================== getSFamilyClass ===================================== */
  public int getSFamilyClass() {
    return s_family_class;
  }

  /* ==================== getPanose ===================================== */
  public byte[] getPanose() {
    return panose;
  }

  /* ==================== getUlUnicodeRange1 ===================================== */
  public int getUlUnicodeRange1() {        /* Bits 0-31   */
    return ul_unicode_range1;
  }

  /* ==================== getUlUnicodeRange2 ===================================== */
  public int getUlUnicodeRange2() {        /* Bits 32-63  */
    return ul_unicode_range2;
  }

  /* ==================== getUlUnicodeRange3 ===================================== */
  public int getUlUnicodeRange3() {        /* Bits 64-95  */
    return ul_unicode_range3;
  }

  /* ==================== getUlUnicodeRange4 ===================================== */
  public int getUlUnicodeRange4() {        /* Bits 96-127 */
    return ul_unicode_range4;
  }

  /* ==================== getAchVendID ===================================== */
  public char[] getAchVendID() {
    return ach_vend_id;
  }

  /* ==================== getFsSelection ===================================== */
  public int getFsSelection() {
    return fs_selection;
  }

  /* ==================== getUsFirstCharIndex ===================================== */
  public int getUsFirstCharIndex() {
    return us_first_char_index;
  }

  /* ==================== getUsLastCharIndex ===================================== */
  public int getUsLastCharIndex() {
    return us_last_char_index;
  }

  /* ==================== getSTypoAscender ===================================== */
  public int getSTypoAscender() {
    return s_typo_ascender;
  }

  /* ==================== getSTypoDescender ===================================== */
  public int getSTypoDescender() {
    return s_typo_descender;
  }

  /* ==================== getSTypoLineGap ===================================== */
  public int getSTypoLineGap() {
    return s_typo_line_gap;
  }

  /* ==================== getUsWinAscent ===================================== */
  public int getUsWinAscent() {
    return us_win_ascent;
  }

  /* ==================== getUsWinDescent ===================================== */
  public int getUsWinDescent() {
    return us_win_descent;
  }

  /* ==================== getUlCodePageRange1 ===================================== */
  public int getUlCodePageRange1() {       /* Bits 0-31   */
    return ul_code_page_range1;
  }

  /* ==================== getUlCodePageRange2 ===================================== */
  public int getUlCodePageRange2() {       /* Bits 32-63  */
    return ul_code_page_range2;
  }

  /* ==================== getSxHeight ===================================== */
  public int getSxHeight() {
    return sx_height;
  }

  /* ==================== getSCapHeight ===================================== */
  public int getSCapHeight() {
    return s_cap_height;
  }

  /* ==================== getUsDefaultChar ===================================== */
  public int getUsDefaultChar() {
    return us_default_char;
  }

  /* ==================== getUsBreakChar ===================================== */
  public int getUsBreakChar() {
    return us_break_char;
  }

  /* ==================== getUsMaxContext ===================================== */
  public int getUsMaxContext() {
    return us_max_context;
  }

}
