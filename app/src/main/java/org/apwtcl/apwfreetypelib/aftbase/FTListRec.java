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
  /*    FTListRec                                                          */
  /* <Description>                                                         */
  /*    A structure used to hold a simple doubly-linked list.  These are   */
  /*    used in many parts of FreeType.                                    */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    head :: The head (first element) of doubly-linked list.            */
  /*                                                                       */
  /*    tail :: The tail (last element) of doubly-linked list.             */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTListRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTListRec";

  protected FTListNodeRec head = null;
  protected FTListNodeRec tail = null;

  /* ==================== FTListRec ================================== */
  public FTListRec() {
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
    str.append("...head: "+head+'\n');
    str.append("...tail: "+tail+'\n');
    return str.toString();
  }
    
  /* =====================================================================
   * FTListUp
   * =====================================================================
   */
  public static void FTListUp(FTListRec list, FTListNodeRec node) {
Debug(0, DebugTag.DBG_BASE, TAG, "FTListUp");
    FTListNodeRec before;
    FTListNodeRec after;

    before = node.prev;
    after  = node.next;
    /* check whether we are already on top of the list */
    if (before == null) {
      return;
    }
    before.next = after;
    if (after != null) {
      after.prev = before;
    } else {
      list.tail = before;
    }
    node.prev = null;
    node.next = list.head;
    list.head.prev = node;
    list.head = node;
  }
    
  /* =====================================================================
   * FTListFind
   * =====================================================================
   */
  public static FTListNodeRec FTListFind(FTListRec list, Object data) {
Debug(0, DebugTag.DBG_BASE, TAG, "FTListFind");
    FTListNodeRec cur;

    cur = list.head;
    while (cur != null) {
      if (cur.data == data) {
        return cur;
      }
      cur = cur.next;
    }
    return null;
  }

  /* ==================== getHead ================================== */
  public FTListNodeRec getHead() {
    return head;
  }

  /* ==================== getHead ================================== */
  public void setHead(FTListNodeRec head) {
    this.head = head;
  }

  /* ==================== getTail ================================== */
  public FTListNodeRec getTail() {
    return tail;
  }

  /* ==================== setTail ================================== */
  public void setTail(FTListNodeRec tail) {
    this.tail = tail;
  }

}