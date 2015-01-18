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
  /*    TTCallRec                                                          */
  /*                                                                       */
  /* This class defines a call record, used to manage function calls.      */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TTCallRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTCallRec";

    public int CallerRange;
    public int CallerIP;
    public int CurCount;
    public int CurRestart;
    public int CurEnd;

    /* ==================== TTCallRec ================================== */
    public TTCallRec() {
      oid++;
      id = oid;
      
      CallerRange = 0;
      CallerIP = 0;
      CurCount = 0;
      CurRestart = 0;
      CurEnd = 0;
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
      str.append("  CallerRange: "+CallerRange+"\n");
      str.append("  CallerIP: "+CallerIP+"\n");
      str.append("  CurCount: "+CurCount+"\n");
      str.append("  CurRestart: "+CurRestart+"\n");
      str.append("  CurEnd: "+CurEnd+"\n");
      return str.toString();
    }
 
    /* ==================== setCallerRange ===================================== */
    public void setCallerRange(int val) {
        this.CallerRange = val;
    }

    /* ==================== setCallerIP ===================================== */
    public void setCallerIP(int val) {
        this.CallerIP = val;
    }

    /* ==================== setCurCount ===================================== */
    public void setCurCount(int val) {
        this.CurCount = val;
    }

    /* ==================== setCurRestart ===================================== */
    public void setCurRestart(int val) {
        this.CurRestart = val;
    }

    /* ==================== setCurEnd ===================================== */
    public void setCurEnd(int val) {
        this.CurEnd = val;
    }

    /* ==================== getCallerRange ===================================== */
    public int getCallerRange(int val) {
        return this.CallerRange;
    }

    /* ==================== getCallerIP ===================================== */
    public int getCallerIP(int val) {
        return this.CallerIP;
    }

    /* ==================== getCurCount ===================================== */
    public int getCurCount(int val) {
        return this.CurCount;
    }

    /* ==================== getCurRestart ===================================== */
    public int getCurRestart(int val) {
        return this.CurRestart;
    }

    /* ==================== getCurEnd ===================================== */
    public int getCurEnd(int val) {
        return this.CurEnd;
    }

}