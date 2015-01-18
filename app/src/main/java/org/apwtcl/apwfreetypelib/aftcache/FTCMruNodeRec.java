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

package org.apwtcl.apwfreetypelib.aftcache;

  /* ===================================================================== */
  /*    FTCMruNodeRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCMruNodeRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCMruNodeRec";

  private FTCMruNodeRec next = null;
  private FTCMruNodeRec prev = null;

  /* ==================== FTCMruNodeRec ================================== */
  public FTCMruNodeRec() {
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
    str.append("..prev: "+prev+'\n');
    str.append("..next: "+next+'\n');
    return str.toString();
  }

  /* ==================== getPrev ================================== */
  public FTCMruNodeRec getPrev() {
    return prev;
  }

  /* ==================== getNext ================================== */
  public FTCMruNodeRec getNext() {
    return next;
  }

  /* =====================================================================
   * FTCMruNodePrepend
   * =====================================================================
   */
  public static void FTCMruNodePrepend(FTReference<FTCMruNodeRec> nodes_ref, FTCMruNodeRec node) {
    FTCMruNodeRec first = nodes_ref.Get();
Debug(0, DebugTag.DBG_CACHE, TAG, "FTCMruNodePrepend"+first+"!"+node+"!");
    FTCMruNodeRec last = null;

    if (first != null) {
      if (first.prev != null) {
        last = first.prev;
      }
      first.prev = node;
      if (last != null) {
        last.next = node;
      }
      node.next = first;
      if (last != null) {
        node.prev = last;
      } else {
        node.prev = null;
      }
    } else {
      node.next = node;
      node.prev = node;
    }
    first = node;
    nodes_ref.Set(first);
  }

  /* =====================================================================
   * FTCMruNodeRemove
   * =====================================================================
   */
  public static void FTCMruNodeRemove(FTReference<FTCMruNodeRec> nodes_ref, FTCMruNodeRec node) {
    FTCMruNodeRec first = nodes_ref.Get();
    FTCMruNodeRec next;
    FTCMruNodeRec prev;

    prev = node.prev;
    next = node.next;
      
    prev.next = next;
    next.prev = prev;
      
    if (node == next) {
      node = null;
    } else {
      if (node == first) {
        first = node;
        nodes_ref.Set(first);
      }
    }
  }

  /* =====================================================================
   * FTCMruNodeUp
   * =====================================================================
   */
  public void FTCMruNodeUp(FTCMruNodeRec node) {
    FTCMruNodeRec first = this;
    FTCMruNodeRec last;
    FTCMruNodeRec prev;
    FTCMruNodeRec next;

    if (first != node) {
      prev = node.prev;
      next = node.next;
      prev.next = next;
      next.prev = prev;
      last = first.prev;
      last.next  = node;
      first.prev = node;
      node.next = first;
      node.prev = last;
    }
  }

}