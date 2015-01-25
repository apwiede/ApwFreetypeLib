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

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTServiceCacheRec                                                  */
  /*                                                                       */
  /*  This structure is used to store a cache for several frequently used  */
  /*  services.  It is the type of `face->services'.  You                  */
  /*  should only use FT_FACE_LOOKUP_SERVICE to access it.                 */
  /*                                                                       */
  /*  All fields should have the type FT_Pointer to relax compilation      */
  /*  dependencies.  We assume the developer isn't completely stupid.      */
  /*                                                                       */
  /*  Each field must be named `service_XXXX' where `XXX' corresponds to   */
  /*  the correct FT_SERVICE_ID_XXXX macro.  See the definition of         */
  /*  FT_FACE_LOOKUP_SERVICE below how this is implemented                 */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTServiceCacheRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTServiceCacheRec";

  protected Object service_POSTSCRIPT_FONT_NAME = null;
  protected Object service_MULTI_MASTERS = null;
  protected Object service_GLYPH_DICT = null;
  protected Object service_PFR_METRICS = null;
  protected Object service_WINFNT = null;

  /* ==================== FTServiceCacheRec ================================== */
  public FTServiceCacheRec() {
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

  /* ==================== getService_POSTSCRIPT_FONT_NAME ================================== */
  public Object getService_POSTSCRIPT_FONT_NAME() {
    return service_POSTSCRIPT_FONT_NAME;
  }

  /* ==================== setService_POSTSCRIPT_FONT_NAME ================================== */
  public void setService_POSTSCRIPT_FONT_NAME(Object service_POSTSCRIPT_FONT_NAME) {
    this.service_POSTSCRIPT_FONT_NAME = service_POSTSCRIPT_FONT_NAME;
  }

  /* ==================== getService_MULTI_MASTERS ================================== */
  public Object getService_MULTI_MASTERS() {
    return service_MULTI_MASTERS;
  }

  /* ==================== setService_MULTI_MASTERS ================================== */
  public void setService_MULTI_MASTERS(Object service_MULTI_MASTERS) {
    this.service_MULTI_MASTERS = service_MULTI_MASTERS;
  }

  /* ==================== getService_GLYPH_DICT ================================== */
  public Object getService_GLYPH_DICT() {
    return service_GLYPH_DICT;
  }

  /* ==================== setService_GLYPH_DICT ================================== */
  public void setService_GLYPH_DICT(Object service_GLYPH_DICT) {
    this.service_GLYPH_DICT = service_GLYPH_DICT;
  }

  /* ==================== getService_PFR_METRICS ================================== */
  public Object getService_PFR_METRICS() {
    return service_PFR_METRICS;
  }

  /* ==================== setService_PFR_METRICS ================================== */
  public void setService_PFR_METRICS(Object service_PFR_METRICS) {
    this.service_PFR_METRICS = service_PFR_METRICS;
  }

  /* ==================== getService_WINFNT ================================== */
  public Object getService_WINFNT() {
    return service_WINFNT;
  }

  /* ==================== setService_WINFNT ================================== */
  public void setService_WINFNT(Object service_WINFNT) {
    this.service_WINFNT = service_WINFNT;
  }

}