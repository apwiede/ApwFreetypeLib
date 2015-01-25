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
  /*    TTGlyphZoneRec                                                     */
 /*                                                                       */
  /*  <Description>                                                        */
  /*     A glyph zone is used to load, scale and hint glyph outline        */
  /*     coordinates.                                                      */
  /*                                                                       */
  /*  <Fields>                                                             */
  /*     max_points   :: The maximum size in points of the zone.           */
  /*                                                                       */
  /*     max_contours :: Max size in links contours of the zone.           */
  /*                                                                       */
  /*     n_points     :: The current number of points in the zone.         */
  /*                                                                       */
  /*     n_contours   :: The current number of contours in the zone.       */
  /*                                                                       */
  /*     org          :: The original glyph coordinates (font              */
  /*                     units/scaled).                                    */
  /*                                                                       */
  /*     cur          :: The current glyph coordinates (scaled/hinted).    */
  /*                                                                       */
  /*     tags         :: The point control tags.                           */
  /*                                                                       */
  /*     contours     :: The contours end points.                          */
  /*                                                                       */
  /*     first_point  :: Offset of the current subglyph's first point.     */
  /*                                                                       */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoadRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTGlyphZoneRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyphZoneRec";

  private int max_points = 0;
  private int max_contours = 0;
  private int n_points = 0;          /* number of points in zone    */
  private int n_contours = 0;        /* number of contours          */
  private FTVectorRec[] org = null;  /* original point coordinates  */
  private int org_idx = 0;
  private FTVectorRec[] cur = null;  /* current point coordinates   */
  private int cur_idx = 0;
  private FTVectorRec[] orus = null; /* original (unscaled) point coordinates */
  private int orus_idx = 0;
  private Flags.Curve[] tags = null; /* current touch flags         */
  private int tags_idx = 0;
  private int[] contours = null;     /* contour end points          */
  private int contours_idx = 0;
  private int first_point = 0;       /* offset of first (#0) point  */
  private TTExecContextRec exec = null;

  /* ==================== TTGlyphZoneRec ================================== */
  public TTGlyphZoneRec() {
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
    str.append("org: "+(Object)org+"\n");
    str.append("cur: "+(Object)cur+"\n");
    str.append("orus: "+(Object)orus);
    return str.toString();
  }
 
  /* =====================================================================
   * copy
   * =====================================================================
   */
  public int copy (TTGlyphZoneRec from) {
    int i;
      
    max_points = from.max_points;
    max_contours = from.max_contours;
    n_points = from.n_points;
    n_contours = from.n_contours;
    org = from.org;
    org_idx = from.org_idx;
    cur = from.cur;
    cur_idx = from.cur_idx;
    orus = from.orus;
    orus_idx = from.orus_idx;
    tags = from.tags;
    tags_idx = from.tags_idx;
    contours = from.contours;
    contours_idx = from.contours_idx;
    first_point = from.first_point;
    return 0;
  }

  /* ==================== showLoaderZone ================================== */
  public void showLoaderZone(String str, TTExecContextRec exec) {
    int j;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, str);
    if (exec != null) {
      if (exec.zp0 != null) {
        if (exec.zp0.cur != null) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp0.cur: "+exec.zp0.cur);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp0.org: "+exec.zp0.org);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp0.orus: "+exec.zp0.orus);
        }
      }
      if (exec.zp1 != null) {
        if (exec.zp1.cur != null) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp1.cur: "+exec.zp1.cur);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp1.org: "+exec.zp1.org);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp1.orus: "+exec.zp1.orus);
        }
      }
      if (exec.zp2 != null) {
        if (exec.zp2.cur != null) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp2.cur: "+exec.zp2.cur);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp2.org: "+exec.zp2.org);
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zp2.orus: "+exec.zp2.orus);
        }
      }
    }
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loaderZone: n_points: %d\n", n_points));
    if (cur != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderZone.cur: "+cur+"!"+cur_idx);
    }
    if (orus != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderZone.orus: "+orus+"!"+orus_idx);
    }
    if (org != null) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loaderZone.org: "+org+"!"+org_idx);
    }
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "Show loaderZone");
    for (j = 0; j < n_points; j++) {
      if (cur != null) {
        if (j < cur.length) {
          if (cur[cur_idx + j] != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  cur: %d %5d %5d", j, cur[cur_idx + j].x, cur[cur_idx + j].y));
          }
        }
      }
      if (orus != null) {
        if (j < orus.length) {
          if (orus[orus_idx + j] != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format(" orus: %d %5d %5d", j, orus[orus_idx + j].x, orus[orus_idx + j].y));
          }
        }
      }
      if (org != null) {
        if (j < org.length) {
          if (org[org_idx + j] != null) {
            Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  org: %d %5d %5d", j, org[org_idx + j].x, org[org_idx + j].y));
          }
        }
      }
    }
  }

  /* ==================== showLoaderZoneShort ================================== */
  public void showLoaderZoneShort(String str) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("%s: n_points: %d", str, n_points));
    int n_points = 5;
    for( int j = 0; j < n_points; j++) {
      if (cur != null) {
        if (j < cur.length) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  cur: %d %5d %5d", j, cur[cur_idx + j].x, cur[cur_idx + j].y));
        }
      }
      if (orus != null) {
        if (j < orus.length) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format(" orus: %d %5d %5d", j, orus[orus_idx + j].x, orus[orus_idx + j].y));
        }
      }
      if (org != null) {
        if (j < org.length) {
          Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  org: %d %5d %5d", j, org[org_idx + j].x, org[org_idx + j].y));
        }
      }
    }
  }

  /* =====================================================================
   * reset
   * =====================================================================
   */
  public int reset () {
    int i;
      
    max_points = 0;
    max_contours = 0;
    n_points = 0;
    n_contours = 0;

    if (org != null) {
      for (i = 0; i < org.length; i++) {
        org[i].x = 0;
        org[i].y = 0;
      }
    }
    org_idx = 0;

    if (cur != null) {
      for (i = 0; i < cur.length; i++) {
        cur[i].x = 0;
        cur[i].y = 0;
      }
    }
    cur_idx = 0;

    if (orus != null) {
      for (i = 0; i < orus.length; i++) {
        orus[i].x = 0;
        orus[i].y = 0;
      }
    }
    orus_idx = 0;

    if (tags != null) {
      for (i = 0; i < tags.length; i++) {
        tags[i] = Flags.Curve.CONIC;
      }
    }
    tags_idx = 0;

    if (contours != null) {
      for (i = 0; i < contours.length; i++) {
        contours[i] = 0;
      }
    }
    contours_idx = 0;

    first_point = 0;
    return 0;
  }

  /* =====================================================================
   *    tt_glyphzone_new
   *
   * <Description>
   *    Allocate a new glyph zone.
   *
   * <Input>
   *    maxPoints   :: The capacity of glyph zone in points.
   *
   *    maxContours :: The capacity of glyph zone in contours.
   *
   * <Output>
   *    zone        :: A pointer to the target glyph zone record.
   *
   * <Return>
   *    FreeType error code.  0 means success.
   *
   * =====================================================================
   */
  public static FTError.ErrorTag tt_glyphzone_new(int maxPoints, int maxContours, FTReference<TTGlyphZoneRec> zone_ref) {
    int i;
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTGlyphZoneRec zone = zone_ref.Get();

    zone.org = new FTVectorRec[maxPoints];
    for (i = 0; i < maxPoints; i++) {
      zone.org[i] = new FTVectorRec();
    }
    zone.cur = new FTVectorRec[maxPoints];
    for (i = 0; i < maxPoints; i++) {
      zone.cur[i] = new FTVectorRec();
    }
    zone.orus = new FTVectorRec[maxPoints];
    for (i = 0; i < maxPoints; i++) {
      zone.orus[i] = new FTVectorRec();
    }
    zone.tags = new Flags.Curve[maxPoints];
    zone.contours = new int[maxContours];
    zone.max_points = maxPoints;
    zone.max_contours = maxContours;
    zone_ref.Set(zone);
    return error;
  }

  /* =====================================================================
   * tt_prepare_zone
   *
   * =====================================================================
   */
  public void tt_prepare_zone(FTGlyphLoadRec load, int start_point, int start_contour) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_prepare_zone: "+load.getExtra_points()+"!"+load.getExtra_points_idx()+"!"+load.getExtra_points2()+"!"+load.getExtra_points2_idx()+"!");
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_prepare_zone1: extra_points length: "+(load.getExtra_points() == null ? 0 : load.getExtra_points().length)+" extra_points2 length: "+(load.getExtra_points2() == null ? 0 : load.getExtra_points2().length)+"!");
    n_points = (load.getN_points() - start_point);
    n_contours = (load.getN_contours() - start_contour);
    org = load.getExtra_points();
    org_idx = load.getExtra_points_idx() + start_point;
    cur = load.getPoints();
    cur_idx = load.getPoints_idx() + start_point;
    orus = load.getExtra_points2();
    orus_idx = load.getExtra_points2_idx() + start_point;
    tags = load.getTags();
    tags_idx = load.getTags_idx() + start_point;
    contours = load.getContours();
    contours_idx = load.getContours_idx() + start_contour;
    first_point = start_point;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "zone org: " + load.getExtra_points() + "!" + load.getExtra_points_idx() + "!" + load.getExtra_points2() + "!" + load.getExtra_points2_idx() + "!");
    showLoaderZone("tt_prepare_zone", exec);
  }

  /* =====================================================================
   * tt_glyphzone_done
   *
   * <Description>
   *    Deallocate a glyph zone.
   *
   * <Input>
   *    zone :: A pointer to the target glyph zone.
   *
   * =====================================================================
   */
  public FTError.ErrorTag tt_glyphzone_done() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
      
    contours = null;
    tags = null;
    cur = null;
    org = null;
    orus = null;

    max_points = 0;
    n_points = 0;
    max_contours = 0;
    n_contours = 0;
    return error;
  }

  /* ==================== getMax_points ================================== */
  public int getMax_points() {
    return max_points;
  }

  /* ==================== aetMax_points ================================== */
  public void setMax_points(int max_points) {
    this.max_points = max_points;
  }

  /* ==================== getMax_contours ================================== */
  public int getMax_contours() {
    return max_contours;
  }

  /* ==================== setMax_contours ================================== */
  public void setMax_contours(int max_contours) {
    this.max_contours = max_contours;
  }

  /* ==================== getN_points ================================== */
  public int getN_points() {
    return n_points;
  }

  /* ==================== setN_points ================================== */
  public void setN_points(int n_points) {
    this.n_points = n_points;
  }

  /* ==================== getN_contours ================================== */
  public int getN_contours() {
    return n_contours;
  }

  /* ==================== setN_contours ================================== */
  public void setN_contours(int n_contours) {
    this.n_contours = n_contours;
  }

  /* ==================== getOrg ================================== */
  public FTVectorRec[] getOrg() {
    return org;
  }

  /* ==================== getOrgPoint ================================== */
  public FTVectorRec getOrgPoint(int point_idx) {
    return org[org_idx + point_idx];
  }

  /* ==================== getOrgPoint_x ================================== */
  public int getOrgPoint_x(int point_idx) {
    return org[org_idx + point_idx].x;
  }

  /* ==================== getOrgPoint_y ================================== */
  public int getOrgPoint_y(int point_idx) {
    return org[org_idx + point_idx].y;
  }

  /* ==================== setOrg ================================== */
  public void xsetOrg(FTVectorRec[] org) {
    this.org = org;
  }

  /* ==================== setOrgPoint ================================== */
  public void setOrgPoint(int point_idx, FTVectorRec vec) {
    org[org_idx + point_idx].x = vec.x;
    org[org_idx + point_idx].y = vec.y;
  }

  /* ==================== setOrgPoint_x ================================== */
  public void setOrgPoint_x(int point_idx, int value) {
    org[org_idx + point_idx].x = value;
  }

  /* ==================== setOrgPoint_y ================================== */
  public void setOrgPoint_y(int point_idx, int value) {
    org[org_idx + point_idx].y = value;
  }

  /* ==================== resetOrgPoint ================================== */
  public void resetOrgPoint(int point_idx) {
    org[org_idx + point_idx].x = 0;
    org[org_idx + point_idx].y = 0;
  }

  /* ==================== getOrg_idx ================================== */
  public int getOrg_idx() {
    return org_idx;
  }

  /* ==================== setOrg_idx ================================== */
  public void setOrg_idx(int org_idx) {
    this.org_idx = org_idx;
  }

  /* ==================== getCur ================================== */
  public FTVectorRec[] getCur() {
    return cur;
  }

  /* ==================== getCurPoint ================================== */
  public FTVectorRec getCurPoint(int point_idx) {
    return cur[cur_idx + point_idx];
  }

  /* ==================== getCurPoint_x ================================== */
  public int getCurPoint_x(int point_idx) {
    return cur[cur_idx + point_idx].x;
  }

  /* ==================== getCurPoint_y ================================== */
  public int getCurPoint_y(int point_idx) {
    return cur[cur_idx + point_idx].y;
  }

  /* ==================== setCur ================================== */
  public void xsetCur(FTVectorRec[] cur) {
    this.cur = cur;
  }

  /* ==================== setCurPoint ================================== */
  public void setCurPoint(int point_idx, FTVectorRec vec) {
    cur[cur_idx + point_idx].x = vec.x;
    cur[cur_idx + point_idx].y = vec.y;
  }

  /* ==================== setCurPoint_x ================================== */
  public void setCurPoint_x(int point_idx, int value) {
    cur[cur_idx + point_idx].x = value;
  }

  /* ==================== setCurPoint_y ================================== */
  public void setCurPoint_y(int point_idx, int value) {
    cur[cur_idx + point_idx].y = value;
  }

  /* ==================== resetCurPoint ================================== */
  public void resetCurPoint(int point_idx) {
    cur[cur_idx + point_idx].x = 0;
    cur[cur_idx + point_idx].y = 0;
  }

  /* ==================== getCur_idx ================================== */
  public int getCur_idx() {
    return cur_idx;
  }

  /* ==================== setCur_idx ================================== */
  public void setCur_idx(int cur_idx) {
    this.cur_idx = cur_idx;
  }

  /* ==================== getOrus ================================== */
  public FTVectorRec[] getOrus() {
    return orus;
  }

  /* ==================== getOrusPoint ================================== */
  public FTVectorRec getOrusPoint(int point_idx) {
    return orus[orus_idx + point_idx];
  }

  /* ==================== getOrusPoint_x ================================== */
  public int getOrusPoint_x(int point_idx) {
    return orus[orus_idx + point_idx].x;
  }

  /* ==================== getOrusPoint_y ================================== */
  public int getOrusPoint_y(int point_idx) {
    return orus[orus_idx + point_idx].y;
  }

  /* ==================== setOrus ================================== */
  public void xsetOrus(FTVectorRec[] orus) {
    this.orus = orus;
  }

  /* ==================== setOrusPoint ================================== */
  public void setOrusPoint(int point_idx, FTVectorRec vec) {
    orus[orus_idx + point_idx].x = vec.x;
    orus[orus_idx + point_idx].y = vec.y;
  }

  /* ==================== setOrusPoint_x ================================== */
  public void setOrusPoint_x(int point_idx, int value) {
    orus[orus_idx + point_idx].x = value;
  }

  /* ==================== setOrusPoint_y ================================== */
  public void setOrusPoint_y(int point_idx, int value) {
    orus[orus_idx + point_idx].y = value;
  }

  /* ==================== resetOrusPoint ================================== */
  public void resetOrusPoint(int point_idx) {
    orus[orus_idx + point_idx].x = 0;
    orus[orus_idx + point_idx].y = 0;
  }

  /* ==================== getOrus_idx ================================== */
  public int getOrus_idx() {
    return orus_idx;
  }

  /* ==================== setOrus_idx ================================== */
  public void setOrus_idx(int orus_idx) {
    this.orus_idx = orus_idx;
  }

  /* ==================== getTags ================================== */
  public Flags.Curve[] getTags() {
    return tags;
  }

  /* ==================== getTag ================================== */
  public Flags.Curve getTag(int tag_idx) {
    return tags[tags_idx + tag_idx];
  }

  /* ==================== setTags ================================== */
  public void setTags(Flags.Curve[] tags) {
    this.tags = tags;
  }

  /* ==================== getTags_idx ================================== */
  public int getTags_idx() {
    return tags_idx;
  }

  /* ==================== setTags_idx ================================== */
  public void setTags_idx(int tags_idx) {
    this.tags_idx = tags_idx;
  }

  /* ==================== getContours ================================== */
  public int[] getContours() {
    return contours;
  }

  /* ==================== getContour ================================== */
  public int getContour(int contour_idx) {
    return contours[contours_idx + contour_idx];
  }

  /* ==================== setContours ================================== */
  public void setContours(int[] contours) {
    this.contours = contours;
  }

  /* ==================== getContours_idx ================================== */
  public int getContours_idx() {
    return contours_idx;
  }

  /* ==================== setContours_idx ================================== */
  public void setContours_idx(int contours_idx) {
    this.contours_idx = contours_idx;
  }

  /* ==================== getFirst_point ================================== */
  public int getFirst_point() {
    return first_point;
  }

  /* ==================== setFirst_point ================================== */
  public void setFirst_point(int first_point) {
    this.first_point = first_point;
  }


  public TTExecContextRec getExec() {
    return exec;
  }

  public void setExec(TTExecContextRec exec) {
    this.exec = exec;
  }

}