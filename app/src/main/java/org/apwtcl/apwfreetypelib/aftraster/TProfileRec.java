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
  private static String TAG = "TProfileRec";

  private int x = 0;           /* current coordinate during sweep          */
  private int link = -1;       /* link to next profile (various purposes)  */
  private int offset = 0;      /* start of profile's data in render pool   */
  private int flags = 0;       /* Bit 0-2: drop-out mode                   */
                               /* Bit 3: profile orientation (up/down)     */
                               /* Bit 4: is top profile?                   */
                               /* Bit 5: is bottom profile?                */
  private int height = 0;      /* profile's height in scanlines            */
  private int start = 0;       /* profile's starting scanline              */
  private int count_lines = 0; /* number of lines to step before this      */
                               /* profile becomes drawable                 */
  private int next = -1;       /* next profile in same contour, used       */
                               /* during drop-out control                  */

  /* ==================== TProfileRec ================================== */
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
    str.append("...x: "+x+'\n');
    str.append("...link: "+link+'\n');
    str.append("...offset: "+offset+'\n');
    str.append("...flags: "+flags+'\n');
    str.append("...height: "+height+'\n');
    str.append("...start: "+start+'\n');
    str.append("...count_lines: "+count_lines+'\n');
    str.append("...next: "+next+'\n');
    return str.toString();
  }

  /* ==================== getX ================================== */
  public int getX() {
    return x;
  }

  /* ==================== setX ================================== */
  public void setX(int x) {
    this.x = x;
  }

  /* ==================== getLink ================================== */
  public int getLink() {
    return link;
  }

  /* ==================== setLink ================================== */
  public void setLink(int link) {
    this.link = link;
  }

  /* ==================== getOffset ================================== */
  public int getOffset() {
    return offset;
  }

  /* ==================== setOffset ================================== */
  public void setOffset(int offset) {
    this.offset = offset;
  }

  /* ==================== getFlags ================================== */
  public int getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getStart ================================== */
  public int getStart() {
    return start;
  }

  /* ==================== setStart ================================== */
  public void setStart(int start) {
    this.start = start;
  }

  /* ==================== getCount_lines ================================== */
  public int getCount_lines() {
    return count_lines;
  }

  /* ==================== setCount_lines ================================== */
  public void setCount_lines(int count_lines) {
    this.count_lines = count_lines;
  }

  /* ==================== getNext ================================== */
  public int getNext() {
    return next;
  }

  /* ==================== setNext ================================== */
  public void setNext(int next) {
    this.next = next;
  }

}