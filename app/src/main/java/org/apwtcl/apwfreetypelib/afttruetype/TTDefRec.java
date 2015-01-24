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

  private int range = 0;          /* in which code range is it located?     */
  private int start = 0;          /* where does it start?                   */
  private int end = 0;            /* where does it end?                     */
  private int opc = 0;            /* function #, or instruction code        */
  private boolean active = false;         /* is it active?                          */
  private boolean inline_delta = false;   /* is function that defines inline delta? */
  private int sph_fdef_flags = 0; /* flags to identify special functions    */

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
    str.append("...range: "+range+'\n');
    str.append("...start: "+start+'\n');
    str.append("...end: "+end+'\n');
    str.append("...opc: "+opc+'\n');
    str.append("...active: "+active+'\n');
    str.append("...inline_delta: "+inline_delta+'\n');
    str.append("...sph_fdef_flags: "+sph_fdef_flags+'\n');
    return str.toString();
  }

  /* ==================== getRange ================================== */
  public int getRange() {
    return range;
  }

  /* ==================== setRange ================================== */
  public void setRange(int range) {
    this.range = range;
  }

  /* ==================== getStart ================================== */
  public int getStart() {
    return start;
  }

  /* ==================== setStart ================================== */
  public void setStart(int start) {
    this.start = start;
  }

  /* ==================== getEnd ================================== */
  public int getEnd() {
    return end;
  }

  /* ==================== setEnd ================================== */
  public void setEnd(int end) {
    this.end = end;
  }

  /* ==================== getOpc ================================== */
  public int getOpc() {
    return opc;
  }

  /* ==================== setOpc ================================== */
  public void setOpc(int opc) {
    this.opc = opc;
  }

  /* ==================== isActive ================================== */
  public boolean isActive() {
    return active;
  }

  /* ==================== setActive ================================== */
  public void setActive(boolean active) {
    this.active = active;
  }

  /* ==================== isInline_delta ================================== */
  public boolean isInline_delta() {
    return inline_delta;
  }

  /* ==================== setInline_delta ================================== */
  public void setInline_delta(boolean inline_delta) {
    this.inline_delta = inline_delta;
  }

  /* ==================== getSph_fdef_flags ================================== */
  public int getSph_fdef_flags() {
    return sph_fdef_flags;
  }

  /* ==================== setSph_fdef_flags ================================== */
  public void setSph_fdef_flags(int sph_fdef_flags) {
    this.sph_fdef_flags = sph_fdef_flags;
  }

}