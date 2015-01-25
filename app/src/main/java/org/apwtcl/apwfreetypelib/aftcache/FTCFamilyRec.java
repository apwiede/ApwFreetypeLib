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
  /*    FTCFamilyRec                                                       */
  /*                                                                       */
  /*  We can group glyphs into `families'.  Each family correspond to a    */
  /*  given face ID, character size, transform, etc.                       */
  /*                                                                       */ 
  /*  Families are implemented as MRU list nodes.  They are                */
  /*  reference-counted.                                                   */
  /* ===================================================================== */

public class FTCFamilyRec extends FTCMruNodeRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCFamilyRec";

  private int num_nodes = 0; /* current number of nodes in this family */
  private FTCCacheRec cache = null;
  private FTCMruListClassRec clazz = null;

  /* ==================== FTCFamilyRec ================================== */
  public FTCFamilyRec() {
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
    str.append("..num_nodes: "+num_nodes+'\n');
    return str.toString();
  }

  /* ==================== getNum_nodes ================================== */
  public int getNum_nodes() {
    return num_nodes;
  }

  /* ==================== setNum_nodes ================================== */
  public void setNum_nodes(int num_nodes) {
    this.num_nodes = num_nodes;
  }

  /* ==================== getCache ================================== */
  public FTCCacheRec getCache() {
    return cache;
  }

  /* ==================== setCache ================================== */
  public void setCache(FTCCacheRec cache) {
    this.cache = cache;
  }

  /* ==================== getClazz ================================== */
  public FTCMruListClassRec getClazz() {
    return clazz;
  }

  /* ==================== setClazz ================================== */
  public void setClazz(FTCMruListClassRec clazz) {
    this.clazz = clazz;
  }

}