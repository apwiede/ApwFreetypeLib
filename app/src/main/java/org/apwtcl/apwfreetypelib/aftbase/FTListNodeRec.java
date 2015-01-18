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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTListNodeRec                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to hold a single list element.                    */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    prev :: The previous element in the list.  null if first.          */
  /*                                                                       */
  /*    next :: The next element in the list.  null if last.               */
  /*                                                                       */
  /*    data :: A typeless pointer to the listed object.                   */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTListNodeRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTListNodeRec";

    public FTListNodeRec next = null;
    public FTListNodeRec prev = null;
    public Object data = null;


    /* ==================== FTListNodeRec ================================== */
    public FTListNodeRec() {
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
      str.append("..next: "+next+'\n');
      str.append("..prev: "+prev+'\n');
      str.append("..data: "+data+'\n');
      return str.toString();
    }
 
    /* =====================================================================
     * FTListAdd 
     * =====================================================================
     */
    public void FTListAdd(FTListRec list) {
      FTListNodeRec before = list.tail;

      next = null;
      prev = before;
      if (before != null) {
        before.next = this;
      } else {
        list.head = this;
      }
      list.tail = this;
    }

}