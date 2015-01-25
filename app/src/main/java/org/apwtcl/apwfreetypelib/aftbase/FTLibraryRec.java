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
  /*    FTLibraryRec                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The FreeType library class.  This is the root of all FreeType      */
  /*    data.                                                              */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    version_major    :: The major version number of the library.       */
  /*                                                                       */
  /*    version_minor    :: The minor version number of the library.       */
  /*                                                                       */
  /*    version_patch    :: The current patch level of the library.        */
  /*                                                                       */
  /*    num_modules      :: The number of modules currently registered     */
  /*                        within this library.  This is set to 0 for new */
  /*                        libraries.  New modules are added through the  */
  /*                        FTAddModule() API function.                    */
  /*                                                                       */
  /*    modules          :: A table used to store handles to the currently */
  /*                        registered modules. Note that each font driver */
  /*                        contains a list of its opened faces.           */
  /*                                                                       */
  /*    renderers        :: The list of renderers currently registered     */
  /*                        within the library.                            */
  /*                                                                       */
  /*    cur_renderer     :: The current outline renderer.  This is a       */
  /*                        shortcut used to avoid parsing the list on     */
  /*                        each call to FT_Outline_Render().  It is a     */
  /*                        handle to the current renderer for the         */
  /*                        FT_GLYPH_FORMAT_OUTLINE format.                */
  /*                                                                       */
  /*    auto_hinter      :: XXX                                            */
  /*                                                                       */
  /*    raster_pool      :: The raster object's render pool.  This can     */
  /*                        ideally be changed dynamically at run-time.    */
  /*                                                                       */
  /*    raster_pool_size :: The size of the render pool in bytes.          */
  /*                                                                       */
  /*    refcount         :: A counter initialized to~1 at the time an      */
  /*                        @FTLibrary structure is created.               */
  /*                        @FTReferenceLibrary increments this counter,   */
  /*                        and @FTDoneLibrary only destroys a library     */
  /*                        if the counter is~1, otherwise it simply       */
  /*                        decrements it.                                 */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTLibraryRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTLibraryRec";

  private int version_major = 0;
  private int version_minor = 1;
  private int version_patch = 0;
  private int num_modules = 0;
  private FTModuleRec[] modules = null;      /* module objects  */
  private FTListRec renderers = null;        /* list of renderers        */
  private FTRendererRec cur_renderer = null; /* current outline renderer */
  private FTModuleRec auto_hinter = null;
  private byte[] raster_pool = null;         /* scan-line conversion */
                                            /* render pool          */
  private int raster_pool_size = 0;          /* size of render pool in bytes */
  private int refcount = 0;

  /* ==================== FTLibraryRec ================================== */
  public FTLibraryRec() {
    oid++;
    id = oid;

Debug(0, DebugTag.DBG_INIT, TAG, "new FTLibraryRec: "+this);
    num_modules = 0;
    modules = new FTModuleRec[FTModules.FT_MAX_MODULES];
    renderers = new FTListRec();
Debug(0, DebugTag.DBG_INIT, TAG, "FTLibraryRec: new renderers list: "+renderers);
    cur_renderer = null;
    auto_hinter = null;
    raster_pool_size = FTRendererClassRec.getRenderPoolSize();
    raster_pool = new byte[raster_pool_size];
    refcount = 1;
  }
    
  /* ==================== mySelf ================================== */
  public String mySelf() {
      return TAG+"!"+id+"!";
    }
        
  /* ==================== toString ===================================== */
  public String toString() {
      return mySelf()+'!';
    }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("...version_major: "+version_major+'\n');
    str.append("...version_minor: "+version_minor+'\n');
    str.append("...version_patch: "+version_patch+'\n');
    str.append("...num_modules: "+num_modules+'\n');
    str.append("...raster_pool_size: "+raster_pool_size+'\n');
    str.append("...refcount: "+refcount+'\n');
    return str.toString();
  }


  /* ==================== getVersion_major ================================== */
  public int getVersion_major() {
    return version_major;
  }

  /* ==================== setVersion_major ================================== */
  public void setVersion_major(int version_major) {
    this.version_major = version_major;
  }

  /* ==================== getVersion_minor ================================== */
  public int getVersion_minor() {
    return version_minor;
  }

  /* ==================== setVersion_minor ================================== */
  public void setVersion_minor(int version_minor) {
    this.version_minor = version_minor;
  }

  /* ==================== getVersion_patch ================================== */
  public int getVersion_patch() {
    return version_patch;
  }

  /* ==================== setVersion_patch ================================== */
  public void setVersion_patch(int version_patch) {
    this.version_patch = version_patch;
  }

  /* ==================== getNum_modules ================================== */
  public int getNum_modules() {
    return num_modules;
  }

  /* ==================== setNum_modules ================================== */
  public void setNum_modules(int num_modules) {
    this.num_modules = num_modules;
  }

  /* ==================== getModules ================================== */
  public FTModuleRec[] getModules() {
    return modules;
  }

  /* ==================== getModule ================================== */
  public FTModuleRec getModule(int module_idx) {
    return modules[module_idx];
  }

  /* ==================== setModules ================================== */
  public void setModules(FTModuleRec[] modules) {
    this.modules = modules;
  }

  /* ==================== setModule ================================== */
  public void setModule(int module_idx, FTModuleRec module) {
    modules[module_idx] = module;
  }

  /* ==================== getRenderers ================================== */
  public FTListRec getRenderers() {
    return renderers;
  }

  /* ==================== setRenderers ================================== */
  public void setRenderers(FTListRec renderers) {
    this.renderers = renderers;
  }

  /* ==================== getCur_renderer ================================== */
  public FTRendererRec getCur_renderer() {
    return cur_renderer;
  }

  /* ==================== setCur_renderer ================================== */
  public void setCur_renderer(FTRendererRec cur_renderer) {
    this.cur_renderer = cur_renderer;
  }

  /* ==================== getAuto_hinter ================================== */
  public FTModuleRec getAuto_hinter() {
    return auto_hinter;
  }

  /* ==================== setAuto_hinter ================================== */
  public void setAuto_hinter(FTModuleRec auto_hinter) {
    this.auto_hinter = auto_hinter;
  }

  /* ==================== getRaster_pool ================================== */
  public byte[] getRaster_pool() {
    return raster_pool;
  }

  /* ==================== setRaster_pool ================================== */
  public void setRaster_pool(byte[] raster_pool) {
    this.raster_pool = raster_pool;
  }

  /* ==================== getRaster_pool_size ================================== */
  public int getRaster_pool_size() {
    return raster_pool_size;
  }

  /* ==================== setRaster_pool_size ================================== */
  public void setRaster_pool_size(int raster_pool_size) {
    this.raster_pool_size = raster_pool_size;
  }

  /* ==================== getRefcount ================================== */
  public int getRefcount() {
    return refcount;
  }

  /* ==================== setRefcount ================================== */
  public void setRefcount(int refcount) {
    this.refcount = refcount;
  }

}