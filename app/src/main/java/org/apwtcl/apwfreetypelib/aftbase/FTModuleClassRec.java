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
  /*    FTModuleClassRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class FTModuleClassRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTModuleClassRec";

  public int module_flags;
  public FTTags.ModuleType module_type;
  public String module_name;
  public int module_version;
  public int module_requires;
  public FTModuleInterface module_interface;

  /* ==================== FTModuleClassRec ================================== */
  public FTModuleClassRec() {
    oid++;
    id = oid;

Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FTModuleClassRec constructor called!!");
    module_flags = 0;
    module_type = FTTags.ModuleType.UNKNOWN;
    module_name = null;
    module_version = 1;
    module_requires = 0;
    module_interface = null;
  }
    
  /* ==================== moduleInit ================================== */
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.e(TAG, "moduleInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== moduleDone ================================== */
  public void moduleDone() {
    Log.e(TAG, "moduleDone not yet implemented");
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
    str.append("..module_flags: "+Integer.toHexString(module_flags)+'\n');
    str.append("..module_type: "+module_type.getDescription()+'\n');
    str.append("..module_name: "+module_name+'\n');
    str.append("..module_version: "+module_version+'\n');
    str.append("..module_requires: "+module_requires+'\n');
    str.append("..module_interface: "+module_interface);
    return str.toString();
  }
 
}