package org.apwtcl.apwfreetypelib.aftttinterpreter;

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

import android.util.SparseArray;

public class TTInterpTags {
  /* =====================================================================
   *
   * Rounding mode enum.
   *
   * =====================================================================
   */
  public enum Round {
    To_Half_Grid(0, "TT_Round_To_Half_Grid"),
    To_Grid(1, "TT_Round_To_Grid"),
    To_Double_Grid(2, "TT_Round_To_Double_Grid"),
    Down_To_Grid(3, "TT_Round_Down_To_Grid"),
    Up_To_Grid(4, "TT_Round_Up_To_Grid"),
    Off(5, "TT_Round_Off"),
    Super(6, "TT_Round_Super"),
    Super_45(7, "TT_Round_Super_45");
    private int val;
    private String str;
    private static SparseArray<Round> tagToRoundStateMapping;
    public static Round getTableTag(int i) {
      if (tagToRoundStateMapping == null) {
        initMapping();
      }
      return tagToRoundStateMapping.get(i);
    }
    private static void initMapping() {
      tagToRoundStateMapping = new SparseArray<Round>();
      for (Round t : values()) {
        tagToRoundStateMapping.put(t.val, t);
      }
    }
    private Round(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum CodeRange {
    NONE(0, "TT_CODERANGE_NONE"),
    FONT(1, "TT_CODERANGE_FONT"),
    CVT(2, "TT_CODERANGE_CVT"),
    GLYPH (3, "TT_CODERANGE_GLYPH");
    private int val;
    private String str;
    private static SparseArray<CodeRange> tagToCodeRangeMapping;
    public static CodeRange getTableTag(int i) {
      if (tagToCodeRangeMapping == null) {
        initMapping();
      }
      return tagToCodeRangeMapping.get(i);
    }
    private static void initMapping() {
      tagToCodeRangeMapping = new SparseArray<CodeRange>();
      for (CodeRange t : values()) {
        tagToCodeRangeMapping.put(t.val, t);
      }
    }
    private CodeRange(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

}