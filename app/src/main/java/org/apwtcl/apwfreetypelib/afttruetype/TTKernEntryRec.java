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

  /* ===================================================================== */
  /*    TTKernEntryRec                                                     */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A tiny structure used to model a gasp range according to the       */
  /*    TrueType specification.                                            */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    maxPPEM  :: The maximum ppem value to which `gaspFlag' applies.    */
  /*                                                                       */
  /*    gaspFlag :: A flag describing the grid-fitting and anti-aliasing   */
  /*                modes to be used.                                      */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTKernEntryRec extends FTDebug {
  private static int oid = 0;

  public final static int TT_KERN_ENTRY_SIZE = 6;

  private int id;
  private static String TAG = "TTKernEntryRec";

  private int version = 0;
  private int length = 0;
  private int coverage = 0;

  /* ==================== TTKernEntryRec ================================== */
  public TTKernEntryRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(-1, DebugTag.DBG_LOAD_GLYPH, TAG, "Load kern entry offset: "+String.format("0x%08x", stream.pos()));
    version = stream.readShort();
    length = stream.readShort();
    coverage = stream.readShort();
    return error;
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

  /* ==================== getVersion ================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getLength ================================== */
  public int getLength() {
    return length;
  }

  /* ==================== getCoverage ================================== */
  public int getCoverage() {
    return coverage;
  }

}