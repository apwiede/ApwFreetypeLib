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

package org.apwtcl.apwfreetypelib.afttruetype;

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTDriverClassRec;
import org.apwtcl.apwfreetypelib.aftbase.FTDriverRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTLibraryRec;
import org.apwtcl.apwfreetypelib.aftbase.FTModuleClassRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTRunInstructions;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;


  /* ===================================================================== */
  /*    TTDriverRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class TTDriverRec extends FTDriverRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTDriverRec";

  protected TTExecContextRec context; /* execution context        */
  protected TTGlyphZoneRec zone;      /* glyph loader points zone */
  protected int interpreter_version;

  /* ==================== TTDriverRec ================================== */
  public TTDriverRec() {
    super();
    oid++;
    id = oid;
Debug(0, DebugTag.DBG_INIT, TAG, "TTDriverRec constructor called id: "+id);

    context = null;
    zone = null;
    interpreter_version = 0;
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
    str.append("..interpreter_version: "+interpreter_version+'\n');
    return str.toString();
  }

  /* ==================== NewContext ===================================== */
  public FTError.ErrorTag NewContext() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTExecContextRec exec;

Debug(0, DebugTag.DBG_INIT, TAG, "NewContext");
    if (context == null) {
        /* allocate object */
      exec = new TTRunInstructions();
      Debug(0, DebugTag.DBG_INIT, TAG, "new TTExecContextRec: "+exec+"!");
        /* store it into the driver */
      error = exec.InitContext();
      if (error != FTError.ErrorTag.ERR_OK) {
        return null;
      }
      context = exec;
    }
    return error;
  }

  /* ==================== getContext ================================== */
  public TTExecContextRec getContext() {
    return context;
  }

  /* ==================== setContext ================================== */
  public void setContext(TTExecContextRec context) {
    this.context = context;
  }

  /* ==================== getZone ================================== */
  public TTGlyphZoneRec getZone() {
    return zone;
  }

  /* ==================== setZone ================================== */
  public void setZone(TTGlyphZoneRec zone) {
    this.zone = zone;
  }

  /* ==================== getInterpreter_version ================================== */
  public int getInterpreter_version() {
    return interpreter_version;
  }

  /* ==================== setInterpreter_version ================================== */
  public void setInterpreter_version(int interpreter_version) {
    this.interpreter_version = interpreter_version;
  }

}