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

package org.apwtcl.apwfreetypelib.aftttinterpreter;

  /* ===================================================================== */
  /*    TTGraphicsStateRec                                                    */
  /*                                                                       */
  /* <Struct>                                                              */
  /*    TTGraphicsStateRec                                                    */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The TrueType graphics state used during bytecode interpretation.   */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTGraphicsStateRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTGraphicsStateRec";

    public short rp0;
    public short rp1;
    public short rp2;
    public FTVectorRec dualVector;
    public FTVectorRec projVector;
    public FTVectorRec freeVector;
    public long loop;
    public int minimum_distance;
    public TTInterpTags.RoundState round_state;
    public boolean auto_flip;
    public int control_value_cutin;
    public int single_width_cutin;
    public int single_width_value;
    public short delta_base;
    public short delta_shift;
    public byte instruct_control;
    /* According to Greg Hitchcock from Microsoft, the `scan_control'     */
    /* variable as documented in the TrueType specification is a 32-bit   */
    /* integer; the high-word part holds the SCANTYPE value, the low-word */
    /* part the SCANCTRL value.  We separate it into two fields.          */
    public boolean scan_control;
    public int scan_type;
    public short gep0;
    public short gep1;
    public short gep2;

    /* ==================== TTGraphicsStateRec ================================== */
    public TTGraphicsStateRec() {
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