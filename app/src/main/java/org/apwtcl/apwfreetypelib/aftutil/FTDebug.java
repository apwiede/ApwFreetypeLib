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

package org.apwtcl.apwfreetypelib.aftutil;

import android.util.Log;
import android.util.SparseArray;

/* =====================================================================
 *    FTDebug debug utilities
 * =====================================================================
 */

public class FTDebug extends Object {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTDebug";

  public enum DebugTag {
    DBG_BASE(1 << 0, 0, true, "DBG_BASE"),
    DBG_SIZE(1 << 1, 0, true, "DBG_SIZE"),
    DBG_CACHE(1 << 2, 0, true, "DBG_CACHE"),
    DBG_CMAP(1 << 3, 0, true, "DBG_CMAP"),
    DBG_INIT(1 << 4, 2, true, "DBG_INIT"),
    DBG_INTERP(1 << 5, 2, true, "DBG_INTERP"),
    DBG_LIBRARY(1 << 6, 0, true, "DBG_LIBRARY"),
    DBG_LOAD_FACE(1 << 7, 0, true, "DBG_LOAD_FACE"),
    DBG_LOAD_GLYPH(1 << 8, 2, true, "DBG_LOAD_GLYPH"),
    DBG_RENDER(1 << 9, 0, true, "DBG_RENDER"),
    DBG_DECOMPOSE(1 << 10, 0, true, "DBG_DECOMPOSE"),
    DBG_SWEEP(1 << 11, 0, true, "DBG_SWEEP"),
    DBG_MD5(1 << 12, 2, true, "DBG_MD5");

    private int val;
    private int level;
    private boolean dbgIsOn;
    private String str;
    private static SparseArray<DebugTag> tagToDebugTagMapping;
    public static DebugTag getTableTag(int i) {
      if (tagToDebugTagMapping == null) {
        initMapping();
      }
      return tagToDebugTagMapping.get(i);
    }
    private static void initMapping() {
      tagToDebugTagMapping = new SparseArray<DebugTag>();
      for (DebugTag t : values()) {
        tagToDebugTagMapping.put(t.val, t);
      }
    }
    private DebugTag(int val, int level, boolean isOn, String str) {
      this.val = val;
      this.level = level;
      this.dbgIsOn = isOn;
      this.str = str;
    }
    public boolean isOn() { return dbgIsOn; }
    public int level() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getVal() {
        return val;
      }
    public String getDescription() {
        return str;
      }
  }
  private static final int DBG_MAX_TYPES = 32;

  /* ==================== constructor ================================== */
  public FTDebug() {
    oid++;
    id = oid;
            
  }

  /* ==================== mySelf ================================== */
  public String mySelf() {
    String str = TAG+"!"+id+"!";
    return str;
  } 
        
  /* ==================== toString ===================================== */
  public String toString() {
    StringBuffer str = new StringBuffer(mySelf()+"!");
    return str.toString();
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    return str.toString();
  }
 
  /* ==================== Debug ================================== */
  public static void Debug(int level, DebugTag type, String tag, String str) throws IndexOutOfBoundsException {
    // ATTENTION take care that 1 << <nn> in th next line fits to the maximum Debug type!!
    if (type.getVal() < 0 || type.getVal() > (1 << 12)) {
      throw new IndexOutOfBoundsException(String.format("bad type: %s (0x%08x) in Debug", type, type.getVal()));
    }
    if (type.isOn() && level >= type.level()) {
      Log.i(TAG, type.getDescription()+": "+tag+": "+str);
    }
  }
    
}
