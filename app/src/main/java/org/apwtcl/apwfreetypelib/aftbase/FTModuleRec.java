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
  /*    FTModuleRec                                                        */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A module object instance.                                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    clazz   :: A reference to the module's class.                      */
  /*                                                                       */
  /*    library :: A handle to the parent library object.                  */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTModuleRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTModuleRec";

    public FTModuleClassRec module_clazz = null;
    public FTLibraryRec library = null;

    /* ==================== FTModuleRec ================================== */
    public FTModuleRec() {
      oid++;
      id = oid;
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTModuleRec constructor called!!");
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
      str.append("..module_clazz: "+module_clazz+'\n');
      str.append("..library: "+library+'\n');
      return str.toString();
    }
 
    /* =====================================================================
     *   FTGetModule
     * =====================================================================
     */
    public static FTModuleRec FTGetModule(FTLibraryRec library, String mod_name) {
      FTModuleRec result = null;
      FTModuleRec cur;
      int limit;
      int module_idx;

      if (library == null || mod_name == null) {
        return result;
      }
      limit = library.num_modules;
      for (module_idx = 0; module_idx < limit; module_idx++) {
        cur = library.modules[module_idx];
        if (mod_name.equals(cur.module_clazz.module_name)) {
          result = cur;
          break;
        }
      }
      return result;
    }

    /* =====================================================================
     *   FTGetModuleInterface
     * =====================================================================
     */
    public static FTModuleInterface FTGetModuleInterface(FTLibraryRec library, String mod_name) {
      FTModuleRec module;

FTDebug.Debug(0, DebugTag.DBG_INIT, TAG, String.format("FT_Get_Module_Interface: %s", mod_name));
      /* test for valid `library' delayed to FT_Get_Module() */
      module = FTGetModule(library, mod_name);
      return module != null ? module.module_clazz.module_interface : null;
    }

}