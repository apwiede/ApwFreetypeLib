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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRequester;
import org.apwtcl.apwfreetypelib.aftbase.FTLibraryRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftdemo.TFont;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
  /* ===================================================================== */
  /*    FTCManagerRec                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This object corresponds to one instance of the cache-subsystem.    */
  /*    It is used to cache one or more @FT_Face objects, along with       */
  /*    corresponding @FT_Size objects.                                    */
  /*                                                                       */
  /*    The manager intentionally limits the total number of opened        */
  /*    @FT_Face and @FT_Size objects to control memory usage.  See the    */
  /*    `max_faces' and `max_sizes' parameters of @FTC_Manager_New.        */
  /*                                                                       */
  /*    The manager is also used to cache `nodes' of various types while   */
  /*    limiting their total memory usage.                                 */
  /*                                                                       */
  /*    All limitations are enforced by keeping lists of managed objects   */
  /*    in most-recently-used order, and flushing old nodes to make room   */
  /*    for new ones.                                                      */
  /*                                                                       */
  /* ===================================================================== */

public class FTCManagerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCManagerRec";

  private final static int FTC_MAX_FACES_DEFAULT = 2;
  private final static int FTC_MAX_SIZES_DEFAULT = 4;
  private final static int FTC_MAX_CACHES = 16;

  private FTLibraryRec library = null;
  private FTCNodeRec nodes_list = null;
  private int max_weight = 0;
  private int cur_weight = 0;
  private int num_nodes = 0;
  private FTCCacheRec[] caches = null;
  private int num_caches = 0;
  private FTCMruListRec faces = null;
  private FTCMruListRec sizes = null;
  private Object request_data = null;
  private FTFaceRequester request_face;

  /* ==================== FTCManagerRec ================================== */
  public FTCManagerRec(FTLibraryRec library, int max_faces, int max_sizes, FTFaceRequester requester, Object requester_data) {
    oid++;
    id = oid;
    if (library == null) {
      Log.e(TAG, "constructor: library == null!");
      return;
    }
    this.library = library;
    this.request_face = requester;
    this.request_data = requester_data;
Debug(0, DebugTag.DBG_CACHE, TAG, "call new FTCFaceListClass");
    if (max_faces == 0) {
      max_faces = FTC_MAX_FACES_DEFAULT;
    }
    faces = new FTCFaceListClass(max_faces, this);

Debug(0, DebugTag.DBG_CACHE, TAG, "call new FTCSizeListClass");
    if (max_sizes == 0) {
      max_sizes = FTC_MAX_SIZES_DEFAULT;
    }
    sizes = new FTCSizeListClass(max_sizes, this);

    caches = new FTCCacheRec[FTC_MAX_CACHES];
//    for (int i = 0; i < FTC_MAX_CACHES; i++) {
//      caches[i] = new FTCCacheRec();
//    }
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
    str.append("...max_weight: "+max_weight+'\n');
    str.append("...cur_weight: "+cur_weight+'\n');
    str.append("...num_nodes: "+num_nodes+'\n');
    str.append("...num_caches: "+num_caches+'\n');
    return str.toString();
  }
  /* =====================================================================
   * RegisterCache
   * =====================================================================
   */
  public FTError.ErrorTag RegisterCache(FTCGCacheClassRec cache) {
    FTError.ErrorTag error;

Debug(0, DebugTag.DBG_CACHE, TAG, "FTCManagerRegisterCache");
    if (num_caches >= FTC_MAX_CACHES) {
      error = FTError.ErrorTag.GLYPH_TOO_MANY_CACHES;
      Log.e(TAG, "RegisterCache: too many registered caches");
      return error;
    }
    cache.setManager(this);
    /* THIS IS VERY IMPORTANT!  IT WILL WRETCH THE MANAGER */
    /* IF IT IS NOT SET CORRECTLY                          */
    cache.setIndex(num_caches);
    error = cache.cacheInit();
    if (error != FTError.ErrorTag.ERR_OK) {
      cache.cacheDone();
      return error;
    }
    caches[num_caches++] = cache;
    return error;
  }

  /* =====================================================================
   * LookupSize
   * =====================================================================
   */
  public FTError.ErrorTag LookupSize(FTCScalerRec scaler, FTReference<FTSizeRec> size_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTReference<FTCMruNodeRec> mru_ref = new FTReference<>();
    FTCMruNodeRec mrunode = null;

Debug(0, DebugTag.DBG_CACHE, TAG, "LookupSize");
    if (size_ref == null) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    size_ref.Set(null);
Debug(0, DebugTag.DBG_CACHE, TAG, "LookupSize1b");
    error = sizes.FTCMruListLookup(scaler, mru_ref);
    mrunode = mru_ref.Get();
    if (error == FTError.ErrorTag.ERR_OK) {
      FTCSizeNodeRec size_node = (FTCSizeNodeRec)mrunode;
      size_node.setScaler(scaler);
      size_ref.Set(size_node.getSize());
    }
Debug(0, DebugTag.DBG_CACHE, TAG, "FTCManagerLookupSize2");
    return error;
  }

  /* =====================================================================
   * LookupFace
   * =====================================================================
   */
  public FTError.ErrorTag LookupFace(Object face_id, FTReference<FTFaceRec> face_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTReference<FTCMruNodeRec> mru_ref = new FTReference<>();

Debug(0, DebugTag.DBG_CACHE, TAG, "LookupFace");
    if (face_ref == null) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    face_ref.Set(null);
    error = faces.FTCMruListLookup(face_id, mru_ref);
    FTCFaceNodeRec face_node = (FTCFaceNodeRec)mru_ref.Get();
    if (error == FTError.ErrorTag.ERR_OK) {
Debug(0, DebugTag.DBG_CACHE, TAG, "face_node.face: "+face_node+"+"+face_node.getFace()+"+"+face_node.getFace().getDriver());
      face_ref.Set(face_node.getFace());
    }
Debug(0, DebugTag.DBG_CACHE, TAG, "LookupFace end "+face_node+"+"+face_node.getFace()+"+"+face_node.getFace().getDriver());
    return error;
  }

  /* =====================================================================
   * Compress
   * =====================================================================
   */
  public void Compress() {
    FTCMruNodeRec node;
    FTCNodeRec first;
    FTCMruNodeRec prev;

    first = nodes_list;
//      FTC_Manager_Check( manager );
    FTTrace.Trace(7, TAG, String.format("compressing, weight = %d, max = %d, nodes = %d",
        cur_weight, max_weight, num_nodes));
    if (cur_weight < max_weight || first == null) {
      return;
    }
    /* go to last node -- it's a circular list */
    node = first.getPrev();
    do {
      prev = ( node == first ) ? null : node.getPrev();
      if (((FTCNodeRec)node).ref_count <= 0) {
        ((FTCNodeRec)node).ftc_node_destroy(this);
      }
      node = prev;
    } while (node != null && cur_weight > max_weight);
  }


  /* ==================== getLibrary ================================== */
  public FTLibraryRec getLibrary() {
    return library;
  }

  /* ==================== setLibrary ================================== */
  public void setLibrary(FTLibraryRec library) {
    this.library = library;
  }

  /* ==================== getNodes_list ================================== */
  public FTCNodeRec getNodes_list() {
    return nodes_list;
  }

  /* ==================== setNodes_list ================================== */
  public void setNodes_list(FTCNodeRec nodes_list) {
    this.nodes_list = nodes_list;
  }

  /* ==================== getMax_weight ================================== */
  public int getMax_weight() {
    return max_weight;
  }

  /* ==================== setMax_weight ================================== */
  public void setMax_weight(int max_weight) {
    this.max_weight = max_weight;
  }

  /* ==================== getCur_weight ================================== */
  public int getCur_weight() {
    return cur_weight;
  }

  /* ==================== setCur_weight ================================== */
  public void setCur_weight(int cur_weight) {
    this.cur_weight = cur_weight;
  }

  /* ==================== getNum_nodes ================================== */
  public int getNum_nodes() {
    return num_nodes;
  }

  /* ==================== setNum_nodes ================================== */
  public void setNum_nodes(int num_nodes) {
    this.num_nodes = num_nodes;
  }

  /* ==================== getCaches ================================== */
  public FTCCacheRec[] getCaches() {
    return caches;
  }

  /* ==================== setCaches ================================== */
  public void setCaches(FTCCacheRec[] caches) {
    this.caches = caches;
  }

  /* ==================== getNum_caches ================================== */
  public int getNum_caches() {
    return num_caches;
  }

  /* ==================== setNum_caches ================================== */
  public void setNum_caches(int num_caches) {
    this.num_caches = num_caches;
  }

  /* ==================== getFaces ================================== */
  public FTCMruListRec getFaces() {
    return faces;
  }

  /* ==================== setFaces ================================== */
  public void setFaces(FTCMruListRec faces) {
    this.faces = faces;
  }

  /* ==================== getSizes ================================== */
  public FTCMruListRec getSizes() {
    return sizes;
  }

  /* ==================== setSizes ================================== */
  public void setSizes(FTCMruListRec sizes) {
    this.sizes = sizes;
  }

  /* ==================== getRequest_data ================================== */
  public Object getRequest_data() {
    return request_data;
  }

  /* ==================== setRequest_data ================================== */
  public void setRequest_data(Object request_data) {
    this.request_data = request_data;
  }

  /* ==================== getRequest_face ================================== */
  public FTFaceRequester getRequest_face() {
    return request_face;
  }

  /* ==================== setRequest_face ================================== */
  public void setRequest_face(FTFaceRequester request_face) {
    this.request_face = request_face;
  }

}