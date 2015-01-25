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
  /*    FTFaceInternalRec                                                  */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure contains the internal fields of each FT_Face        */
  /*    object.  These fields may change between different releases of     */
  /*    FreeType.                                                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    max_points ::                                                      */
  /*      The maximum number of points used to store the vectorial outline */
  /*      of any glyph in this face.  If this value cannot be known in     */
  /*      advance, or if the face isn't scalable, this should be set to 0. */
  /*      Only relevant for scalable formats.                              */
  /*                                                                       */
  /*    max_contours ::                                                    */
  /*      The maximum number of contours used to store the vectorial       */
  /*      outline of any glyph in this face.  If this value cannot be      */
  /*      known in advance, or if the face isn't scalable, this should be  */
  /*      set to 0.  Only relevant for scalable formats.                   */
  /*                                                                       */
  /*    transform_matrix ::                                                */
  /*      A 2x2 matrix of 16.16 coefficients used to transform glyph       */
  /*      outlines after they are loaded from the font.  Only used by the  */
  /*      convenience functions.                                           */
  /*                                                                       */
  /*    transform_delta ::                                                 */
  /*      A translation vector used to transform glyph outlines after they */
  /*      are loaded from the font.  Only used by the convenience          */
  /*      functions.                                                       */
  /*                                                                       */
  /*    transform_flags ::                                                 */
  /*      Some flags used to classify the transform.  Only used by the     */
  /*      convenience functions.                                           */
  /*                                                                       */
  /*    services ::                                                        */
  /*      A cache for frequently used services.  It should be only         */
  /*      accessed with the macro `FT_FACE_LOOKUP_SERVICE'.                */
  /*                                                                       */
  /*    incremental_interface ::                                           */
  /*      If non-null, the interface through which glyph data and metrics  */
  /*      are loaded incrementally for faces that do not provide all of    */
  /*      this data when first opened.  This field exists only if          */
  /*      @FT_CONFIG_OPTION_INCREMENTAL is defined.                        */
  /*                                                                       */
  /*    ignore_unpatented_hinter ::                                        */
  /*      This boolean flag instructs the glyph loader to ignore the       */
  /*      native font hinter, if one is found.  This is exclusively used   */
  /*      in the case when the unpatented hinter is compiled within the    */
  /*      library.                                                         */
  /*                                                                       */
  /*    refcount ::                                                        */
  /*      A counter initialized to~1 at the time an @FT_Face structure is  */
  /*      created.  @FT_Reference_Face increments this counter, and        */
  /*      @FT_Done_Face only destroys a face if the counter is~1,          */
  /*      otherwise it simply decrements it.                               */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftcache.FTServiceCacheRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTFaceInternalRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTFaceInternalRec";

  private FTMatrixRec transform_matrix = null;
  private FTVectorRec transform_delta = null;
  private int transform_flags = 0;
  FTServiceCacheRec services = null;
  private boolean ignore_unpatented_hinter = false;
  private int refcount;

  /* ==================== FTFaceInternalRec ================================== */
  public FTFaceInternalRec() {
    oid++;
    id = oid;

    transform_matrix = new FTMatrixRec();
    transform_delta = new FTVectorRec();
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
    str.append("...transform_flags: "+transform_flags+'\n');
    str.append("...ignore_unpatented_hinter: "+ignore_unpatented_hinter+'\n');
    str.append("...refcount: "+refcount+'\n');
    return str.toString();
  }

  /* ==================== getTransform_matrix ================================== */
  public FTMatrixRec getTransform_matrix() {
    return transform_matrix;
  }

  /* ==================== setTransform_matrix ================================== */
  public void setTransform_matrix(FTMatrixRec transform_matrix) {
    this.transform_matrix = transform_matrix;
  }

  /* ==================== getTransform_delta ================================== */
  public FTVectorRec getTransform_delta() {
    return transform_delta;
  }

  /* ==================== setTransform_delta ================================== */
  public void setTransform_delta(FTVectorRec transform_delta) {
    this.transform_delta = transform_delta;
  }

  /* ==================== getTransform_flags ================================== */
  public int getTransform_flags() {
    return transform_flags;
  }

  /* ==================== getTransform_flags ================================== */
  public void setTransform_flags(int transform_flags) {
    this.transform_flags = transform_flags;
  }

  /* ==================== getServices ================================== */
  public FTServiceCacheRec getServices() {
    return services;
  }

  /* ==================== setServices ================================== */
  public void setServices(FTServiceCacheRec services) {
    this.services = services;
  }

  /* ==================== isIgnore_unpatented_hinter ================================== */
  public boolean isIgnore_unpatented_hinter() {
    return ignore_unpatented_hinter;
  }

  /* ==================== serIgnore_unpatented_hinter ================================== */
  public void setIgnore_unpatented_hinter(boolean ignore_unpatented_hinter) {
    this.ignore_unpatented_hinter = ignore_unpatented_hinter;
  }

  /* ==================== getRefcount ================================== */
  public int getRefcount() {
    return refcount;
  }

  /* ==================== setRefcount ================================== */
  public void setRefcount(int refcount) {
    this.refcount = refcount;
  }

}