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
  /*    TTCodeRange                                                        */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TTCodeRange extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTCodeRange";

    public int size = 0;
    public TTOpCode.OpCode[] base = null;
    public short[] short_base = null;

    /* ==================== TTCodeRange ================================== */
    public TTCodeRange() {
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
 
}