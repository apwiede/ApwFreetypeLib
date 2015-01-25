package org.apwtcl.apwfreetypelib.aftraster;

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

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TProfileRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "ftdemo.TProfile";

    public Long X = 0L;           /* current coordinate during sweep          */
    public int link = -1;         /* link to next profile (various purposes)  */
    public int offset = 0;        /* start of profile's data in render pool   */
    public int flags = 0;         /* Bit 0-2: drop-out mode                   */
                                  /* Bit 3: profile orientation (up/down)     */
                                  /* Bit 4: is top profile?                   */
                                  /* Bit 5: is bottom profile?                */
    public long height = 0L;      /* profile's height in scanlines            */
    public long start = 0L;       /* profile's starting scanline              */
    public Integer countL = 0;    /* number of lines to step before this      */
                                  /* profile becomes drawable                 */
    public int next = -1;         /* next profile in same contour, used       */
                                  /* during drop-out control                  */

    /* ==================== ftdemo.TProfile ================================== */
    public TProfileRec() {
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