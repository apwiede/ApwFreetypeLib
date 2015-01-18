/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf (at) wiedemann-pri (dot) de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTInitApwFreeType                                                  */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTApwFreeType extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTInit";
    
  public FTModules ft_modules = null;
  public FTModuleRec root;

  /* ==================== FTInitApwFreeType ================================== */
  public FTApwFreeType() {
    ft_modules = new FTModules();
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

  /* =====================================================================
   * InitFreeType
   * =====================================================================
   */

  public FTError.ErrorTag InitFreeType(FTLibraryRec library) {
    FTError.ErrorTag error;

    error = FTError.ErrorTag.ERR_OK;
    FTTrace.Trace(7, TAG, "InitFreeType");
Debug(0, DebugTag.DBG_INIT, TAG, "InitFreeType call FTAddDefaultModules");
    ft_modules.FTAddDefaultModules(library);
Debug(0, DebugTag.DBG_INIT, TAG, "InitFreeType done");
    return error;
  }

}