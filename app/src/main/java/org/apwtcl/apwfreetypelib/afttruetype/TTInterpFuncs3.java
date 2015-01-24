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

import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

  /* ===================================================================== */
  /*    TTInterpFuncs3                                                        */
  /*                                                                       */
  /* ===================================================================== */

public class TTInterpFuncs3 extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTInterpFuncs3";
    
    /* ==================== TTInterpFuncs3 ================================== */
    public TTInterpFuncs3() {
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
 
    /* ==================== _showZp0Zp1OrgCur ================================ */
    public static void _showZp0Zp1OrgCur( String str, TTExecContextRec cur ) {
      Debug(0, DebugTag.DBG_RENDER, TAG, str);
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.org: "+(Object)cur.zp0.xgetOrg());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.org: "+(Object)cur.zp1.xgetOrg());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.org: "+(Object)cur.zp2.xgetOrg());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.cur: "+(Object)cur.zp0.xgetCur());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.cur: "+(Object)cur.zp1.xgetCur());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.cur: "+(Object)cur.zp2.xgetCur());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp0.orus: "+(Object)cur.zp0.getOrus());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp1.orus: "+(Object)cur.zp1.getOrus());
      Debug(0, DebugTag.DBG_RENDER, TAG, "zp2.orus: "+(Object)cur.zp2.getOrus());
      for( int i = 0; i < 6; i++) {
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp0.org: %d x: %d, y: %d\n", i, cur.zp0.getOrgPoint_x(i), cur.zp0.getOrgPoint_y(i)));
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp1.org: %d x: %d, y: %d\n", i, cur.zp1.getOrgPoint_x(i), cur.zp1.getOrgPoint_y(i)));
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp0.cur: %d x: %d, y: %d\n", i, cur.zp0.getCurPoint_x(i), cur.zp0.getCurPoint_y(i)));
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("zp1.cur: %d x: %d, y: %d\n", i, cur.zp1.getCurPoint_x(i), cur.zp1.getCurPoint_y(i)));
      }
    }



}