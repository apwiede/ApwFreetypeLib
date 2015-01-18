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
  /*    TTGaspRangeRec                                                     */
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

public class TTGaspRangeRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTGaspRangeRec";

    public int maxPPEM = 0;
    public int gaspFlag = 0;

    /* ==================== TTGaspRangeRec ================================== */
    public TTGaspRangeRec() {
      oid++;
      id = oid;
    }
    
    /* ==================== Load ================================== */
    public FTError.ErrorTag Load(FTStreamRec stream) {
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTGaspRangeRec offset: "+String.format("0x%08x", stream.pos()));
      maxPPEM = stream.readShort();
      gaspFlag = stream.readShort();
FTTrace.Trace(7, TAG, String.format("gaspRange rangeMaxPPEM %5d, rangeGaspBehavior 0x%04x",
    maxPPEM, gaspFlag));
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
 
}