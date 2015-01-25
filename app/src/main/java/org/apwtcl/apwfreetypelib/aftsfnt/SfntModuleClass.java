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

package org.apwtcl.apwfreetypelib.aftsfnt;

  /* ===================================================================== */
  /*    SfntModuleClass                                                    */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTModuleClassRec;
import org.apwtcl.apwfreetypelib.aftbase.FTModuleRec;
import org.apwtcl.apwfreetypelib.aftbase.FTModules;
import org.apwtcl.apwfreetypelib.aftbase.FTRendererRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class SfntModuleClass extends FTModuleClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "SfntModuleClass";

  /* ==================== SfntModuleClass ================================== */
  public SfntModuleClass() {
    super();
    oid++;
    id = oid;

    module_flags = 0;  /* not a font driver or renderer */
    module_type = FTTags.ModuleType.FT_MODULE;
    module_name = "sfnt";        /* driver name */
    module_version = 0x10000;   /* driver version 1.0 */
    module_requires = 0x20000;  /* driver requires FreeType 2.0 or higher */
    module_interface = new FTSfntInterfaceClass(); /* "Sfnt_Interface"; /* module specific interface */
Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "SfntModuleClass constructor called!!");
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

  /* ==================== moduleInit ===================================== */
  @Override
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.i(TAG, "moduleInit");
    // nothing to do
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== moduleDone ===================================== */
  @Override
  public void moduleDone() {
    //bothig to do
    Log.i(TAG, "moduleDone");
  }

  /* ==================== getInterface ===================================== */
//  @Override
  public FTError.ErrorTag getInterface() {
    Log.e(TAG, "getInterface not yet implemented");
    return FTError.ErrorTag.ERR_OK;
//    return sfnt_get_interface();
  }

}