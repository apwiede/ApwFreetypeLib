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
  /*    FTCCMapCacheClass                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCharMapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCCMapCacheClass extends FTCGCacheClassRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCCMapCacheClass";

  /* ==================== FTCCMapCacheClass ================================== */
  public FTCCMapCacheClass() {
    oid++;
    id = oid;
    this.node_type = FTCTags.NodeType.GNODE;
    this.cache_class_type = FTCTags.ClassType.FTCCMapCacheClass;
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
    str.append("..node_type: "+node_type+"\n");
    str.append("..cache_class_type: "+cache_class_type+"\n");
    return str.toString();
  }

  /* =====================================================================
   *    ftc_cmap_node_new
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_cmap_node_new(FTReference<FTCNodeRec> node_ref, FTCCMapQueryRec query) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCCMapNodeRec node = null;
    int nn;

    node = new FTCCMapNodeRec();
    node.setFace_id(query.getFace_id());
    node.setCmap_index(query.getCmap_index());
    node.setFirst((query.getChar_code() / FTCCMapNodeRec.FTC_CMAP_INDICES_MAX) * FTCCMapNodeRec.FTC_CMAP_INDICES_MAX);
    for (nn = 0; nn < FTCCMapNodeRec.FTC_CMAP_INDICES_MAX; nn++) {
      node.getIndices()[nn] = FTCCMapNodeRec.FTC_CMAP_UNKNOWN;
    }
    node_ref.Set(node);
    return error;
  }

  /* =====================================================================
   *    ftc_cmap_node_weight
   *
   * =====================================================================
   */
  public int ftc_cmap_node_weight(FTCMruNodeRec node) {
    Log.w(TAG, "ftc_cmap_node_weight not fully implemented");
    return 0; /* should be some amount of memory for determining FTCCMapNodeRec */
  }

  /* =====================================================================
   *    ftc_cmap_node_compare
   *
   * =====================================================================
   */
  public boolean ftc_cmap_node_compare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.w(TAG, "ftc_cmap_node_compare not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return false;
  }

  /* =====================================================================
   *    ftc_cmap_node_remove_faceid
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_cmap_node_remove_faceid(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    Log.w(TAG, "ftc_cmap_node_remove_faceid not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    return error;
  }

  /* =====================================================================
   *    ftc_cmap_node_free
   *
   * =====================================================================
   */
  public FTError.ErrorTag ftc_cmap_node_free(FTCNodeRec node) {
    Log.w(TAG, "ftc_cmap_node_free not yet implemented");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    return error;
  }



  /* =====================================================================
   *    FTCCMapCacheLookup
   *
   * =====================================================================
   */
  public int FTCCMapCacheLookup(Object face_id, int cmap_index, int char_code) {
    FTCCMapQueryRec query = new FTCCMapQueryRec();
    FTCNodeRec node = null;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int gindex = 0;
    Object hash;
    int no_cmap_change = 0;


Debug(0, DebugTag.DBG_CACHE, TAG, String.format("FTC_CMapCache_Lookup: cmap_index: %d, char_code: %c 0x%02x", cmap_index, char_code, char_code));
    if (cmap_index < 0) {
        /* Treat a negative cmap index as a special value, meaning that you */
        /* don't want to change the FT_Face's character map through this    */
        /* call.  This can be useful if the face requester callback already */
        /* sets the face's charmap to the appropriate value.                */

      no_cmap_change = 1;
      cmap_index = 0;
    }
    query.setFace_id(face_id);
    query.setCmap_index(cmap_index);
    query.setChar_code(char_code);
    hash = FTCHashFuncs.FTC_CMAP_HASH(face_id, cmap_index, char_code);
    FTReference<FTCCMapQueryRec> query_ref = new FTReference<FTCCMapQueryRec>();
    FTReference<FTCNodeRec> node_ref = new FTReference<FTCNodeRec>();
    query_ref.Set(query);
    node_ref.Set(node);
    error = FTCCacheLookup(hash, (Object) query, node_ref);
    node = node_ref.Get();
    query = query_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      return gindex;
    }
//      FT_ASSERT( (int)(char_code - ((FTCCMapNodeRec)node).first ) <
//                  FTCCMapNodeRec.FTC_CMAP_INDICES_MAX);
      /* something rotten can happen with rogue clients */
    int val1 = char_code - ((FTCCMapNodeRec)node).getFirst();
    int val2 = FTCCMapNodeRec.FTC_CMAP_INDICES_MAX;
    if (val1 >= val2) {
//FIXME!!        return 0; /* XXX: should return appropriate error */
    }
    gindex = ((FTCCMapNodeRec)node).getIndices()[val1];
    if (gindex == FTCCMapNodeRec.FTC_CMAP_UNKNOWN) {
      FTFaceRec face;

      gindex = 0;
      FTReference<FTFaceRec> face_ref = new FTReference<FTFaceRec>();
      error = getManager().LookupFace(((FTCCMapNodeRec) node).getFace_id(), face_ref);
      face = face_ref.Get();
      if (error != FTError.ErrorTag.ERR_OK) {
        return gindex;
      }
      if ((int)cmap_index < (int)face.getNum_charmaps()) {
        FTCharMapRec old = null;
        FTCharMapRec cmap = null;

        old  = face.getCharmap();
        cmap = face.charmaps[cmap_index];
        if (old != cmap && (no_cmap_change == 0)) {
          cmap.SetCharmap(face);
        }
        gindex = cmap.FTGetCharIndex(face, char_code);
        if (old != cmap && (no_cmap_change == 0)) {
          old.SetCharmap(face);
        }
      }
      ((FTCCMapNodeRec)node).getIndices()[char_code - ((FTCCMapNodeRec)node).getFirst()] = gindex;
    }
    return gindex;
  }




  /* ==================== nodeNew ===================================== */
  @Override
  public FTError.ErrorTag nodeNew(FTReference<FTCNodeRec> node_ref, Object query) {
   return ftc_cmap_node_new(node_ref, (FTCCMapQueryRec)query);
  }

  /* ==================== nodeWeight ===================================== */
  @Override
  public int nodeWeight(FTCMruNodeRec node) {
    return ftc_cmap_node_weight(node);
  }

  /* ==================== nodeCompare ===================================== */
  @Override
  public boolean nodeCompare(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    return ftc_cmap_node_compare(node, query, flag_ref);
  }

  /* ==================== nodeRemoveFaceid ===================================== */
  @Override
  public FTError.ErrorTag nodeRemoveFaceid(FTCNodeRec node, Object query, FTReference<Boolean> flag_ref) {
    return ftc_cmap_node_remove_faceid(node, query, flag_ref);
  }

  /* ==================== nodeFree ===================================== */
  @Override
  public FTError.ErrorTag nodeFree(FTCNodeRec node) {
    return ftc_cmap_node_free(node);
  }

  /* ==================== cacheInit ===================================== */
  @Override
  public FTError.ErrorTag cacheInit() {
   return ftc_gcache_init();
  }

  /* ==================== cacheDone ===================================== */
  @Override
  public FTError.ErrorTag cacheDone() {
   return ftc_gcache_done();
  }

}