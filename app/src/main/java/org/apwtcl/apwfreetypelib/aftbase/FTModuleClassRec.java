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

  protected int module_flags;
  protected FTTags.ModuleType module_type;
  protected String module_name;
  protected int module_version;
  protected int module_requires;
  protected FTModuleInterface module_interface;

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

  /* ==================== moduleInit ================================== */
  public FTError.ErrorTag moduleInit(FTModuleRec module) {
    Log.e(TAG, "moduleInit not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== moduleDone ================================== */
  public void moduleDone() {
    Log.e(TAG, "moduleDone not yet implemented");
  }

  /* ==================== getModule_flags ================================== */
  public int getModule_flags() {
    return module_flags;
  }

  /* ==================== setModule_flags ================================== */
  public void setModule_flags(int module_flags) {
    this.module_flags = module_flags;
  }

  /* ==================== getModule_type ================================== */
  public FTTags.ModuleType getModule_type() {
    return module_type;
  }

  /* ==================== setModule_type ================================== */
  public void setModule_type(FTTags.ModuleType module_type) {
    this.module_type = module_type;
  }

  /* ==================== getModule_name ================================== */
  public String getModule_name() {
    return module_name;
  }

  /* ==================== setModule_name ================================== */
  public void setModule_name(String module_name) {
    this.module_name = module_name;
  }

  /* ==================== getModule_version ================================== */
  public int getModule_version() {
    return module_version;
  }

  /* ==================== setModule_version ================================== */
  public void setModule_version(int module_version) {
    this.module_version = module_version;
  }

  /* ==================== getModule_requires ================================== */
  public int getModule_requires() {
    return module_requires;
  }

  /* ==================== setModule_requires ================================== */
  public void setModule_requires(int module_requires) {
    this.module_requires = module_requires;
  }

  /* ==================== getModule_interface ================================== */
  public FTModuleInterface getModule_interface() {
    return module_interface;
  }

  /* ==================== setModule_interface ================================== */
  public void setModule_interface(FTModuleInterface module_interface) {
    this.module_interface = module_interface;
  }

}