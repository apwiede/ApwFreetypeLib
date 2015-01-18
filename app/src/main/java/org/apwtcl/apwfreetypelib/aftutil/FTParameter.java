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

  /* ===================================================================== */
  /*    FTParameter                                                        */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A simple structure used to pass more or less generic parameters to */
  /*    @FTOpenFace.                                                     */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    tag  :: A four-byte identification tag.                            */
  /*                                                                       */
  /*    data :: A pointer to the parameter data.                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The ID and function of parameters are driver-specific.  See the    */
  /*    various FT_PARAM_TAG_XXX flags for more information.               */
  /*                                                                       */
  /*                                                                       */
  /* ===================================================================== */

import android.util.SparseArray;

public class FTParameter extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTParameter";

  public enum ParamTag {
    FT_PARAM_TAG_IGNORE_PREFERRED_FAMILY(TTUtil.TagToInt("igpf"), "FT_PARAM_TAG_IGNORE_PREFERRED_FAMILY"),
    FT_PARAM_TAG_IGNORE_PREFERRED_SUBFAMILY(TTUtil.TagToInt("igps"), "FT_PARAM_TAG_IGNORE_PREFERRED_SUBFAMILY"),
    FT_PARAM_TAG_INCREMENTAL(TTUtil.TagToInt("incr"), "FT_PARAM_TAG_INCREMENTAL"),
    FT_PARAM_TAG_UNPATENTED_HINTING(TTUtil.TagToInt("unpa"), "FT_PARAM_TAG_UNPATENTED_HINTING");
    private int val;
    private String str;
    private static SparseArray<ParamTag> tagToParamTagMapping;
    public static ParamTag getTableTag(int i) {
      if (tagToParamTagMapping == null) {
        initMapping();
      }
      return tagToParamTagMapping.get(i);
    }
    private static void initMapping() {
      tagToParamTagMapping = new SparseArray<ParamTag>();
      for (ParamTag t : values()) {
        tagToParamTagMapping.put(t.val, t);
      }
    }
    private ParamTag(int val, String str) {
      this.val = val;
      this.str = str;
    }
    public int getVal() {
      return val;
    }
  }

    public ParamTag tag;
    public Object[] data;

    /* ==================== FTParameter ================================== */
    public FTParameter() {
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
      return str.toString();
    }

  /* ==================== getTag ===================================== */
  public ParamTag getTag() {
    return tag;
  }

  /* ==================== getData ===================================== */
  public Object getData() {
    return data;
  }

}