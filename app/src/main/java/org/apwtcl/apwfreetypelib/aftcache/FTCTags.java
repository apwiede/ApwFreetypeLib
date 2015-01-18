package org.apwtcl.apwfreetypelib.aftcache;

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

public class FTCTags {
  public enum NodeType {
    UNKNOWN(0, "FTC_NODE_TYPE_UNKNOWN"),
    FACE_NODE(1, "FTC_NODE_TYPE_FACE_NODE"),
    SIZE_NODE(2, "FTC_NODE_TYPE_SIZE_NODE"),
    GNODE(3, "FTC_NODE_TYPE_GNODE"),
    INODE(4, "FTC_NODE_TYPE_INODE"),
    SNODE(5, "FTC_NODE_TYPE_SNODE"),
    BASIC_FAMILY(6, "FTC_NODE_TYPE_BASIC_FAMILY");

    private int val;
    private String str;
    private static SparseArray<NodeType> tagToNodeTypeMapping;
    public static NodeType getTableTag(int i) {
      if (tagToNodeTypeMapping == null) {
        initMapping();
      }
      return tagToNodeTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToNodeTypeMapping = new SparseArray<NodeType>();
      for (NodeType t : values()) {
        tagToNodeTypeMapping.put(t.val, t);
      }
    }
    private NodeType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

  public enum ClassType {
    UNKNOWN(0, "FTC_CACHE_CLASS_TYPE_UNKNOWN"),
    FTCBasicICacheClass(1, "FTC_CACHE_CLASS_TYPE_FTCBasicICacheClass"),
    FTCBasicSCacheClass(2, "FTC_CACHE_CLASS_TYPE_FTCBasicSCacheClass"),
    FTCCMapCacheClass(3, "FTC_CACHE_CLASS_TYPE_FTCCMapCacheClass");

    private int val;
    private String str;
    private static SparseArray<ClassType> tagToClassTypeMapping;
    public static ClassType getTableTag(int i) {
      if (tagToClassTypeMapping == null) {
        initMapping();
      }
      return tagToClassTypeMapping.get(i);
    }
    private static void initMapping() {
      tagToClassTypeMapping = new SparseArray<ClassType>();
      for (ClassType t : values()) {
        tagToClassTypeMapping.put(t.val, t);
      }
    }
    private ClassType(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

}
