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
  /*    FTValidatorRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

public class FTValidatorRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTValidatorRec";

  private FTTags.Validate level = FTTags.Validate.DEFAULT;
  private FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

  /* ==================== FTValidatorRec ================================== */
  public FTValidatorRec() {
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
    str.append("..level: "+level+'\n');
    str.append("..error: "+error+'\n');
    return str.toString();
  }

  /* ==================== init ============================== */
  public void init(FTTags.Validate level) {
    this.level = level;
    error = FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getLevel ============================== */
  public FTTags.Validate getLevel() {
    return level;
  }

  /* ==================== getError ============================== */
  public FTError.ErrorTag getError() {
    return error;
  }

  /* ==================== setError ============================== */
  public void setError(FTError.ErrorTag errror) {
    this.error = error;
  }

}