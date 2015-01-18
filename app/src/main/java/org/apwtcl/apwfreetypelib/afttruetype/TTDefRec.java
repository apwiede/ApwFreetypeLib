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
  /*    TTDefRec                                                           */
  /*                                                                       */
  /* Defines a function/instruction definition record.                     */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TTDefRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTDefRec";

    public int range = 0;          /* in which code range is it located?     */
    public int start = 0;          /* where does it start?                   */
    public int end = 0;            /* where does it end?                     */
    public int opc = 0;            /* function #, or instruction code        */
    public boolean active = false;         /* is it active?                          */
    public boolean inline_delta = false;   /* is function that defines inline delta? */
    public int sph_fdef_flags = 0; /* flags to identify special functions    */

    /* ==================== TTDefRec ================================== */
    public TTDefRec() {
      oid++;
      id = oid;
    }
    
    /* ==================== mySelf ================================== */
    public String mySelf() {
      return  TAG+"!"+id+"!";
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
