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
  /*    IUPWorkerRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTIUPWorkerRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "IUPWorkerRec";

    public FTVectorRec[] orgs; /* original and current coordinate */
    public int org_idx;
    public FTVectorRec[] curs; /* arrays */
    public int cur_idx;
    public FTVectorRec[] orus; /* arrays */
    public int orus_idx;
    public int max_points;

    /* ==================== IUPWorkerRec ================================== */
    public TTIUPWorkerRec() {
      oid++;
      id = oid;

      max_points = 0;
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