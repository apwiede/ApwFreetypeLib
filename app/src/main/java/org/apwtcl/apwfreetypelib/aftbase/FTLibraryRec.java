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

    public int version_major = 0;
    public int version_minor = 1;
    public int version_patch = 0;
    public int num_modules = 0;
    public FTModuleRec[] modules = null;      /* module objects  */
    public FTListRec renderers = null;        /* list of renderers        */
    public FTRendererRec cur_renderer = null; /* current outline renderer */
    public FTModuleRec auto_hinter = null;
    public byte[] raster_pool = null;         /* scan-line conversion */
                                              /* render pool          */
    public int raster_pool_size = 0;          /* size of render pool in bytes */
    public int refcount = 0;

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
      return str.toString();
    }

}