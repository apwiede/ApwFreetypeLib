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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTDriverRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTDriverRec extends FTModuleRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTDriverRec";

  protected FTDriverClassRec driver_clazz = null;
  protected FTListRec faces_list = null;
  protected FTGlyphLoaderRec glyph_loader = null;

  /* ==================== FTDriverRec ================================== */
  public FTDriverRec() {
    super();
    oid++;
    id = oid;

Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTDriverRec constructor called!!");
    faces_list = new FTListRec();
    FTTrace.Trace(7, TAG, "faces_list 1 new FTListRec: " + faces_list.mySelf());
  }
    
  /* ==================== FTDriverRec ================================== */
  public FTDriverRec(FTModuleRec module) {
    oid++;
    id = oid;
      
Debug(0, DebugTag.DBG_INIT, TAG, "FTDriverRec constructor 2 called module: "+module.library);
    this.module_clazz = module.module_clazz;
    this.library = module.library;
    driver_clazz = (FTDriverClassRec)module.module_clazz;
    faces_list = new FTListRec();
    FTTrace.Trace(7, TAG, "faces_list 2 new FTListRec: "+ faces_list.mySelf());
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

  /* ==================== getDriver_clazz ================================== */
  public FTDriverClassRec getDriver_clazz() {
    return driver_clazz;
  }

  /* ==================== setDriver_clazz ================================== */
  public void setDriver_clazz(FTDriverClassRec driver_clazz) {
    this.driver_clazz = driver_clazz;
  }

  /* ==================== getFaces_list ================================== */
  public FTListRec getFaces_list() {
    return faces_list;
  }

  /* ==================== setFaces_list ================================== */
  public void setFaces_list(FTListRec faces_list) {
    this.faces_list = faces_list;
  }

  /* ==================== getGlyph_loader ================================== */
  public FTGlyphLoaderRec getGlyph_loader() {
    return glyph_loader;
  }

  /* ==================== setGlyph_loader ================================== */
  public void setGlyph_loader(FTGlyphLoaderRec glyph_loader) {
    this.glyph_loader = glyph_loader;
  }

}