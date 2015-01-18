package org.apwtcl.apwfreetypelib.aftutil;

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

public class FTUtilFlags {

  public enum StreamOpen {
    MEMORY(0x1, "FT_OPEN_MEMORY"),
    STREAM(0x2, "FT_OPEN_STREAM"),
    PATHNAME(0x4, "FT_OPEN_PATHNAME"),
    DRIVER(0x8, "FT_OPEN_DRIVER"),
    PARAMS(0x10, "FT_OPEN_PARAMS");
    private int val;
    private String str;
    private static SparseArray<StreamOpen> tagToStreamOpenMapping;
    public static StreamOpen getTableTag(int i) {
      if (tagToStreamOpenMapping == null) {
        initMapping();
      }
      return tagToStreamOpenMapping.get(i);
    }
    private static void initMapping() {
      tagToStreamOpenMapping = new SparseArray<StreamOpen>();
      for (StreamOpen t : values()) {
        tagToStreamOpenMapping.put(t.val, t);
      }
    }
    private StreamOpen(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
    public String getDescription() {
      return str;
    }

    public static boolean isMemory(int flags) {
      return (flags &  FTUtilFlags.StreamOpen.MEMORY.getVal()) != 0;
    }
    public static boolean isStream(int flags) {
      return (flags &  FTUtilFlags.StreamOpen.STREAM.getVal()) != 0;
    }
    public static boolean isPathname(int flags) {
      return (flags &  FTUtilFlags.StreamOpen.PATHNAME.getVal()) != 0;
    }
    public static boolean isDriver(int flags) {
      return (flags &  FTUtilFlags.StreamOpen.DRIVER.getVal()) != 0;
    }
    public static boolean isParams(int flags) {
      return (flags &  FTUtilFlags.StreamOpen.PARAMS.getVal()) != 0;
    }

  }

  public enum ArrayType {
    FT_VECTOR(0x1, "FT_ARRAY_TYPE_FT_VECTOR"),
    BYTE(0x2, "FT_ARRAY_TYPE_BYTE"),
    SHORT(0x4, "FT_ARRAY_TYPE_SHORT"),
    INT(0x8, "FT_ARRAY_TYPE_INT"),
    LONG(0x10, "FT_ARRAY_TYPE_LONG"),
    CHARMAPS(0x20, "FT_ARRAY_TYPE_CHARMAPS"),
    CURVE(0x40, "FT_ARRAY_TYPE_CURVE");
    private int val;
    private String str;
    private static SparseArray<ArrayType> tagToArrayTypeMapping;
    public static ArrayType getTableTag(int i) {
      if (tagToArrayTypeMapping == null) {
        initMapping();
      }
      return tagToArrayTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToArrayTypeMapping = new SparseArray<ArrayType>();
      for (ArrayType t : values()) {
        tagToArrayTypeMapping.put(t.val, t);
      }
    }
    private ArrayType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
    public String getDescription() {
      return str;
    }

  }

}