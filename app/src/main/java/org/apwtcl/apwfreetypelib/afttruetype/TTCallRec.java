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

  private int caller_range;
  private int caller_IP;
  private int cur_count;
  private int cur_restart;
  private int cur_end;

  /* ==================== TTCallRec ================================== */
  public TTCallRec() {
    oid++;
    id = oid;
      
    caller_range = 0;
    caller_IP = 0;
    cur_count = 0;
    cur_restart = 0;
    cur_end = 0;
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
    str.append("  caller_range: "+ caller_range +"\n");
    str.append("  caller_IP: "+ caller_IP +"\n");
    str.append("  cur_count: "+ cur_count +"\n");
    str.append("  cur_restart: "+ cur_restart +"\n");
    str.append("  cur_end: "+ cur_end +"\n");
    return str.toString();
  }
 
  /* ==================== setCaller_range ===================================== */
  public void setCaller_range(int val) {
        this.caller_range = val;
    }

  /* ==================== setCaller_IP ===================================== */
  public void setCaller_IP(int val) {
        this.caller_IP = val;
    }

  /* ==================== setCur_count ===================================== */
  public void setCur_count(int val) {
        this.cur_count = val;
    }

  /* ==================== setCur_restart ===================================== */
  public void setCur_restart(int val) {
        this.cur_restart = val;
    }

  /* ==================== setCur_end ===================================== */
  public void setCur_end(int val) {
        this.cur_end = val;
    }

  /* ==================== getCaller_range ===================================== */
  public int getCaller_range() {
        return this.caller_range;
    }

  /* ==================== getCaller_IP ===================================== */
  public int getCaller_IP() {
        return this.caller_IP;
    }

  /* ==================== getCur_count ===================================== */
  public int getCur_count() {
        return this.cur_count;
    }

  /* ==================== getCur_restart ===================================== */
  public int getCur_restart() {
        return this.cur_restart;
    }

  /* ==================== getCur_end ===================================== */
  public int getCur_end() {
        return this.cur_end;
    }

}