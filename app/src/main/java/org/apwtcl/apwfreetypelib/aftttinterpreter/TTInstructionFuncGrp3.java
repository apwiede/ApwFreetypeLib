package org.apwtcl.apwfreetypelib.aftttinterpreter;

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

  /* ===================================================================== */
  /*    TTInstructionFuncGrp3                                              */
  /*    instructions functions group 3  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.afttruetype.TTGlyphZoneRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTIUPWorkerRec;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp3 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp3";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp3 ================================== */
  public TTInstructionFuncGrp3(TTExecContextRec exec)
  {
    oid++;
    id = oid;
    this.cur = exec;
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
    return str.toString();
  }

  /* =====================================================================
   * _iup_worker_interpolate
   * =====================================================================
   */
  private void _iup_worker_interpolate(FTReference<TTIUPWorkerRec> worker_ref, int p1, int p2,
                                              int ref1, int ref2, boolean useX) {
    TTIUPWorkerRec worker = worker_ref.Get();
    int i;
    int orus1;
    int orus2;
    int org1;
    int org2;
    int delta1;
    int delta2;

    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("_iup_worker_interpolate: p1: %d, p2: %d, ref1: %d ref2: %d", p1, p2, ref1, ref2));
    if (p1 > p2) {
      return;
    }
    if (TTUtil.BOUNDS(ref1, worker.max_points) ||
        TTUtil.BOUNDS(ref2, worker.max_points)) {
      return;
    }
    if (useX) {
      orus1 = worker.orus[worker.orus_idx + ref1].x;
      orus2 = worker.orus[worker.orus_idx + ref2].x;
    } else {
      orus1 = worker.orus[worker.orus_idx + ref1].y;
      orus2 = worker.orus[worker.orus_idx + ref2].y;
    }
    if (orus1 > orus2) {
      int tmp_o;
      int tmp_r;

      tmp_o = orus1;
      orus1 = orus2;
      orus2 = tmp_o;
      tmp_r = ref1;
      ref1  = ref2;
      ref2  = tmp_r;
    }
    if (useX) {
      org1   = worker.orgs[worker.org_idx + ref1].x;
      org2   = worker.orgs[worker.org_idx + ref2].x;
      delta1 = worker.curs[worker.cur_idx + ref1].x - org1;
      delta2 = worker.curs[worker.org_idx + ref2].x - org2;
    } else {
      org1   = worker.orgs[worker.org_idx + ref1].y;
      org2   = worker.orgs[worker.org_idx + ref2].y;
      delta1 = worker.curs[worker.cur_idx + ref1].y - org1;
      delta2 = worker.curs[worker.org_idx + ref2].y - org2;
    }
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("org1: %d,  org2: %d, delta1: %d, delta2: %d, orus1. %d, orus2: %d", org1, org2, delta1, delta2, orus1, orus2));
    if (orus1 == orus2) {
        /* simple shift of untouched points */
      for (i = p1; i <= p2; i++) {
        int x;

        if (useX) {
          x = worker.orgs[worker.org_idx + i].x;
        } else {
          x = worker.orgs[worker.org_idx + i].y;
        }
        if (x <= org1) {
          x += delta1;
        } else {
          x += delta2;
        }
        Debug(0, DebugTag.DBG_INTERP, TAG, String.format("i: %d curs[i].x: %d x: %d",  i, useX ? worker.curs[worker.cur_idx + i].x : worker.curs[worker.cur_idx + i].y, x));
        if (useX) {
          worker.curs[worker.cur_idx + i].x = x;
        } else {
          worker.curs[worker.cur_idx + i].y = x;
        }
      }
    } else {
      int scale = 0;
      boolean scale_valid = false;

      Debug(0, DebugTag.DBG_INTERP, TAG, String.format("p1: %d, p2: %d", p1, p2));
        /* interpolation */
      for (i = p1; i <= p2; i++) {
        int x;

        if (useX) {
          x = worker.orgs[worker.org_idx + i].x;
        } else {
          x = worker.orgs[worker.org_idx + i].y;
        }
        Debug(0, DebugTag.DBG_INTERP, TAG, String.format("x: %d org1: %d, org2: %d, delta1: %d", x, org1, org2, delta1));
        if (x <= org1) {
          x += delta1;
        } else {
          if (x >= org2) {
            x += delta2;
          } else {
            if (!scale_valid) {
              scale_valid = true;
              scale = FTCalc.FTDivFix(org2 + delta2 - (org1 + delta1), orus2 - orus1);
            }
            Debug(0, DebugTag.DBG_INTERP, TAG, String.format("scale: %d, org1: %d, delta1: %d, worker.orus[i].x: %d, orus1: %d", scale, org1, delta1, useX ?  worker.orus[worker.orus_idx + i].x :  worker.orus[worker.orus_idx + i].y, orus1));
            if (useX) {
              x = (org1 + delta1) + TTUtil.FTMulFix(worker.orus[worker.orus_idx + i].x - orus1, scale);
            } else {
              x = (org1 + delta1) + TTUtil.FTMulFix(worker.orus[worker.orus_idx + i].y - orus1, scale);
            }
            Debug(0, DebugTag.DBG_INTERP, TAG, String.format("x: %d", x));
          }
        }
        if (useX) {
          worker.curs[worker.cur_idx + i].x = x;
        } else {
          worker.curs[worker.cur_idx + i].y = x;
        }
      }
    }
//FTGlyphLoaderRec._showLoaderZone("_iup_worker_interpolate end");
    worker_ref.Set(worker);
  }

  /* =====================================================================
   * _iup_worker_shift
   * =====================================================================
   */
  private void _iup_worker_shift(FTReference<TTIUPWorkerRec> worker_ref, Integer p1, Integer p2, Integer p, boolean useX) {
    int i;
    int dx;
    TTIUPWorkerRec worker = worker_ref.Get();

    if (useX) {
      dx = worker.curs[worker.cur_idx + p].x - worker.orgs[worker.org_idx + p].x;
    } else {
      dx = worker.curs[worker.cur_idx + p].y - worker.orgs[worker.org_idx + p].y;
    }
    if (dx != 0) {
      for (i = p1; i < p; i++) {
        if (useX) {
          worker.curs[worker.cur_idx + i].x = worker.curs[worker.cur_idx + i].x + dx;
        } else {
          worker.curs[worker.cur_idx + i].y = worker.curs[worker.cur_idx + i].y + dx;
        }
      }
      for (i = p + 1; i <= p2; i++) {
        if (useX) {
          worker.curs[worker.cur_idx + i].x = worker.curs[worker.cur_idx + i].x + dx;
        } else {
          worker.curs[worker.cur_idx + i].y = worker.curs[worker.cur_idx + i].y + dx;
        }
      }
    }
    worker_ref.Set(worker);
  }

  /* =====================================================================
   * ComputePointDisplacement
   * =====================================================================
   */
  private boolean ComputePointDisplacement(FTReference<Integer> x_ref, FTReference<Integer> y_ref, FTReference<TTGlyphZoneRec> zone_ref, FTReference<Integer> int_ref) {
    TTGlyphZoneRec zp;
    int p;
    int d;
    int x;
    int y;

    if ((cur.opcode.getVal() & 1) != 0) {
      zp = cur.zp0;
      p  = cur.graphics_state.rp1;
    } else {
      zp = cur.zp1;
      p  = cur.graphics_state.rp2;
    }
    if (TTUtil.BOUNDS(p, zp.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      int_ref.Set(0);
      return false;
    }
    zone_ref.Set(zp);
    int_ref.Set(p);
    d = cur.funcProject(zp.getCurPoint_x(p) - zp.getOrgPoint_x(p),
        zp.getCurPoint_y(p) - zp.getOrgPoint_y(p));
    x = FTCalc.FT_MulDiv(d, cur.graphics_state.freeVector.x, cur.F_dot_P);
    x_ref.Set(x);
    y = FTCalc.FT_MulDiv(d, cur.graphics_state.freeVector.y, cur.F_dot_P);
    y_ref.Set(y);
    return true;
  }

  /* =====================================================================
   * MoveZp2Point
   * =====================================================================
   */
  private void MoveZp2Point(int point, int dx, int dy, boolean touch) {
    if (cur.graphics_state.freeVector.x != 0) {
      cur.zp2.setCurPoint_x(point, (cur.zp2.getCurPoint_x(point) + dx));
      if (touch) {
        cur.zp2.getTags()[point] = Flags.Curve.getTableTag(cur.zp2.getTags()[point].getVal() | Flags.Curve.TOUCH_X.getVal());
      }
    }
    if (cur.graphics_state.freeVector.y != 0) {
      cur.zp2.setCurPoint_y(point, (cur.zp2.getCurPoint_y(point) + dy));
      if (touch) {
        cur.zp2.getTags()[point] = Flags.Curve.getTableTag(cur.zp2.getTags()[point].getVal() | Flags.Curve.TOUCH_Y.getVal());
      }
    }
  }

  /* =====================================================================
   * IUP[a]:       Interpolate Untouched Points
   * Opcode range: 0x30-0x31
   * Stack:        -->
   * =====================================================================
   */
  public void IUP() {
    TTIUPWorkerRec V = new TTIUPWorkerRec();
    FTReference<TTIUPWorkerRec> worker_ref;
    Flags.Curve mask;
    int first_point;   /* first point of contour        */
    int end_point;     /* end point (last+1) of contour */
    int first_touched; /* first touched point in contour   */
    int cur_touched;   /* current touched point in contour */
    int point;         /* current point   */
    int contour;       /* current contour */
    boolean useX;

    Debug(0, DebugTag.DBG_INTERP, TAG, "insIUP");
      /* ignore empty outlines */
    if (cur.pts.getN_contours() == 0) {
      return;
    }
//FTGlyphLoaderRec._showLoaderZone("insIUP");
    if ((cur.opcode.getVal() & 1) != 0) {
      useX = true;
      mask = Flags.Curve.TOUCH_X;
      V.orgs = cur.pts.getOrg();
      V.org_idx = cur.pts.getOrg_idx();
      V.curs = cur.pts.getCur();
      V.cur_idx = cur.pts.getCur_idx();
      V.orus = cur.pts.getOrus();
      V.orus_idx = cur.pts.getOrus_idx();
    } else {
      useX = false;
      mask = Flags.Curve.TOUCH_Y;
      V.orgs = cur.pts.getOrg();
//        V.org_idx = cur.pts.org_idx + 1;
      V.org_idx = cur.pts.getOrg_idx();
      V.curs = cur.pts.getCur();
//        V.cur_idx = cur.pts.cur_idx + 1;
      V.cur_idx = cur.pts.getCur_idx();
      V.orus = cur.pts.getOrus();
//        V.orus_idx = cur.pts.orus_idx + 1;
      V.orus_idx = cur.pts.getOrus_idx();
    }
    V.max_points = cur.pts.getN_points();
    contour = 0;
    point = 0;
    do {
      end_point = cur.pts.getContours()[contour] - cur.pts.getFirst_point();
      first_point = point;
      if (TTUtil.BOUNDS(end_point, cur.pts.getN_points())) {
        end_point = cur.pts.getN_points() - 1;
      }
      while (point <= end_point && (cur.pts.getTags()[point].getVal() & mask.getVal()) == 0) {
        point++;
      }
      worker_ref = new FTReference<>();
      worker_ref.Set(V);
      if (point <= end_point) {
        first_touched = point;
        cur_touched   = point;
        point++;
        while (point <= end_point) {
          if ((cur.pts.getTags()[point].getVal() & mask.getVal()) != 0) {
            _iup_worker_interpolate(worker_ref, cur_touched + 1, point - 1, cur_touched, point, useX);
            cur_touched = point;
          }
          point++;
        }
        if (cur_touched == first_touched) {
          _iup_worker_shift(worker_ref, first_point, end_point, cur_touched, useX);
        } else {
          _iup_worker_interpolate(worker_ref, (cur_touched + 1), end_point,
              cur_touched, first_touched, useX);
          if (first_touched > 0) {
            _iup_worker_interpolate(worker_ref, first_point, first_touched - 1,
                cur_touched, first_touched, useX);
          }
        }
        V = worker_ref.Get();
      }
      contour++;
    } while (contour < cur.pts.getN_contours());
//FTGlyphLoaderRec._showLoaderZone("insIUP END");
  }

  /* =====================================================================
   * SHP[a]:       SHift Point by the last point
   * Opcode range: 0x32-0x33
   * Stack:        uint32... -->
   * =====================================================================
   */
  public void SHP() {
    int dx;
    int dy;
    int point;
    FTReference<Integer> dx_ref = new FTReference<>();
    FTReference<Integer> dy_ref = new FTReference<>();
    FTReference<TTGlyphZoneRec> zp_ref = new FTReference<>();
    FTReference<Integer> int_ref = new FTReference<>();

    if (cur.top < cur.graphics_state.loop) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
    if (ComputePointDisplacement(dx_ref, dy_ref, zp_ref, int_ref)) {
      return;
    }
    dx = dx_ref.Get();
    dy = dy_ref.Get();
    while (cur.graphics_state.loop > 0) {
      cur.stack_idx--;
      point = cur.stack[cur.stack_idx];
      if (TTUtil.BOUNDS(point, cur.zp2.getN_points())) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
      } else {
        MoveZp2Point(point, dx, dy, true);
      }
      cur.graphics_state.loop--;
    }
    cur.graphics_state.loop = 1;
    cur.new_top = cur.stack_idx;
  }

  /* =====================================================================
   * SHC[a]:       SHift Contour
   * Opcode range: 0x34-35
   * Stack:        uint32 -->
   *
   * UNDOCUMENTED: According to Greg Hitchcock, there is one (virtual)
   *               contour in the twilight zone, namely contour number
   *               zero which includes all points of it.
   * =====================================================================
   */
  public void SHC() {
    TTGlyphZoneRec zp;
    int ref;
    int dx;
    int dy;
    int contour;
    int bounds;
    int start;
    int limit;
    int i;
    FTReference<Integer> dx_ref = new FTReference<>();
    FTReference<Integer> dy_ref = new FTReference<>();
    FTReference<TTGlyphZoneRec> zp_ref = new FTReference<>();
    FTReference<Integer> int_ref = new FTReference<>();

    contour = cur.stack[cur.stack_idx];
    bounds  = (cur.graphics_state.gep2 == 0) ? 1 : cur.zp2.getN_contours();
    if (TTUtil.BOUNDS(contour, bounds)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    if (ComputePointDisplacement(dx_ref, dy_ref, zp_ref, int_ref)) {
      return;
    }
    dx = dx_ref.Get();
    dy = dy_ref.Get();
    zp = zp_ref.Get();
    ref = int_ref.Get();
    if (contour == 0) {
      start = 0;
    } else {
      start = cur.zp2.getContours()[contour - 1] + 1 - cur.zp2.getFirst_point();
    }
      /* we use the number of points if in the twilight zone */
    if (cur.graphics_state.gep2 == 0) {
      limit = cur.zp2.getN_points();
    } else {
      limit = cur.zp2.getContours()[contour] - cur.zp2.getFirst_point() + 1;
    }
    for (i = start; i < limit; i++) {
      if (zp.getCur() != cur.zp2.getCur() || ref != i) {
        MoveZp2Point(i, dx, dy, true);
      }
    }
  }

  /* =====================================================================
   * SHZ[a]:       SHift Zone
   * Opcode range: 0x36-37
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void SHZ() {
    TTGlyphZoneRec zp;
    int ref;
    int dx;
    int dy;
    int limit;
    int i;
    FTReference<Integer> dx_ref = new FTReference<>();
    FTReference<Integer> dy_ref = new FTReference<>();
    FTReference<TTGlyphZoneRec> zp_ref = new FTReference<>();
    FTReference<Integer> int_ref = new FTReference<>();

    if (TTUtil.BOUNDS(cur.stack[cur.stack_idx], 2)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    if (ComputePointDisplacement(dx_ref, dy_ref, zp_ref, int_ref)) {
      return;
    }
    dx = dx_ref.Get();
    dy = dy_ref.Get();
    zp = zp_ref.Get();
    ref = int_ref.Get();
      /* XXX: UNDOCUMENTED! SHZ doesn't move the phantom points.     */
      /*      Twilight zone has no real contours, so use `n_points'. */
      /*      Normal zone's `n_points' includes phantoms, so must    */
      /*      use end of last contour.                               */
    if (cur.graphics_state.gep2 == 0) {
      limit = cur.zp2.getN_points();
    } else {
      if (cur.graphics_state.gep2 == 1 && cur.zp2.getN_contours() > 0) {
        limit = cur.zp2.getContours()[cur.zp2.getN_contours() - 1] + 1;
      } else {
        limit = 0;
      }
    }
      /* XXX: UNDOCUMENTED! SHZ doesn't touch the points */
    for (i = 0; i < limit; i++) {
      if (zp.getCur() != cur.zp2.getCur() || ref != i) {
        MoveZp2Point(i, dx, dy, false);
      }
    }
  }

  /* =====================================================================
   * SHPIX[]:      SHift points by a PIXel amount
   * Opcode range: 0x38
   * Stack:        f26.6 uint32... -->
   * =====================================================================
   */
  public void SHPIX() {
    int dx;
    int dy;
    int point;

    if (cur.top < cur.graphics_state.loop + 1) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
    dx = TTUtil.TTMulFix14(cur.stack[cur.stack_idx], cur.graphics_state.freeVector.x);
    dy = TTUtil.TTMulFix14(cur.stack[cur.stack_idx], cur.graphics_state.freeVector.y);
    while (cur.graphics_state.loop > 0) {
      cur.stack_idx--;
      point = cur.stack[cur.stack_idx];
      if (TTUtil.BOUNDS(point, cur.zp2.getN_points())) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
      } else {
        MoveZp2Point(point, dx, dy, true);
      }
      cur.graphics_state.loop--;
    }
    cur.graphics_state.loop = 1;
    cur.new_top = cur.stack_idx;
  }

  /* =====================================================================
   * IP[]:         Interpolate Point
   * Opcode range: 0x39
   * Stack:        uint32... -->
   *
   * SOMETIMES, DUMBER CODE IS BETTER CODE
   * =====================================================================
   */
  public void IP() {
    int old_range;
    int cur_range;
    FTVectorRec orus_base;
    FTVectorRec cur_base;
    boolean twilight;

    if (cur.top < cur.graphics_state.loop) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
      /*
       * We need to deal in a special way with the twilight zone.
       * Otherwise, by definition, the value of cur.twilight.orus[n] is (0,0),
       * for every n.
       */
    twilight = cur.graphics_state.gep0 == 0 || cur.graphics_state.gep1 == 0 || cur.graphics_state.gep2 == 0;

    if (TTUtil.BOUNDS(cur.graphics_state.rp1, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("twilight: %b, rp1: %d", twilight, cur.graphics_state.rp1));
    if (twilight) {
      orus_base = cur.zp0.getOrgPoint(cur.graphics_state.rp1);
    } else {
      orus_base = cur.zp0.getOrus()[cur.zp0.getOrus_idx() + cur.graphics_state.rp1];
      Debug(0, DebugTag.DBG_INTERP, TAG, String.format("orus.x: %d, orus.y: %d", orus_base.x,orus_base.y));
      Debug(0, DebugTag.DBG_INTERP, TAG, String.format("orus2.x: %d, orus2.y: %d", cur.zp0.getOrus()[cur.zp0.getOrus_idx() + 1].x, cur.zp0.getOrus()[cur.zp0.getOrus_idx() + 1].y));
    }
    cur_base = cur.zp0.getCurPoint(cur.graphics_state.rp1);
      /* XXX: There are some glyphs in some braindead but popular */
      /*      fonts out there (e.g. [aeu]grave in monotype.ttf)   */
      /*      calling IP[] with bad values of rp[12].             */
      /*      Do something sane when this odd thing happens.      */
    if (TTUtil.BOUNDS(cur.graphics_state.rp1, cur.zp0.getN_points()) || TTUtil.BOUNDS(cur.graphics_state.rp2, cur.zp1.getN_points())) {
      old_range = 0;
      cur_range = 0;
    } else {
      if (twilight) {
        old_range = cur.funcDualproj(cur.zp1.getOrgPoint_x(cur.graphics_state.rp2) - orus_base.x,
            cur.zp1.getOrgPoint_y(cur.graphics_state.rp2) - orus_base.y);
      } else {
        System.out.println(String.format("x_scale: %d,  y_scale:%d", cur.metrics.getX_scale(), cur.metrics.getY_scale()));
        if (cur.metrics.getX_scale() == cur.metrics.getY_scale()) {
          Debug(0, DebugTag.DBG_INTERP, TAG, String.format("rp2: %d, zp1.orus.x: %d, x: %d, zp1.orus.y: %d, y: %d", cur.graphics_state.rp2, cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].x, orus_base.x,
              cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].y, orus_base.y));
          old_range = cur.funcDualproj(cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].x - orus_base.x,
              cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].y - orus_base.y);
          Debug(0, DebugTag.DBG_INTERP, TAG, String.format("old_range: %d, rp2.x: %d, rp2.y: %d, orus.x: %d, orus.y: %d", old_range, cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].x, cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].y, orus_base.x, orus_base.y));
        } else {
          FTVectorRec vec = new FTVectorRec();

          vec.x = TTUtil.FTMulFix(cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].x - orus_base.x, cur.metrics.getX_scale());
          vec.y = TTUtil.FTMulFix(cur.zp1.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp2].y - orus_base.y, cur.metrics.getY_scale());
          old_range = cur.funcDualproj(vec.x, vec.y);
        }
      }
      cur_range = cur.funcProject(cur.zp1.getCurPoint_x(cur.graphics_state.rp2) - cur_base.x,
          cur.zp1.getCurPoint_y(cur.graphics_state.rp2) - cur_base.y);
    }
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("old_range:%d cur_range: %d", old_range, cur_range));
    for (; cur.graphics_state.loop > 0; --cur.graphics_state.loop) {
      int point = cur.stack[--cur.stack_idx];
      int org_dist;
      int cur_dist;
      int new_dist;

        /* check point bounds */
      if (TTUtil.BOUNDS(point, cur.zp2.getN_points())) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
        continue;
      }
      if (twilight) {
        org_dist = cur.funcDualproj(cur.zp2.getOrgPoint_x(point) - orus_base.x,
            cur.zp2.getOrgPoint_y(point) - orus_base.y);
      } else {
        if (cur.metrics.getX_scale() == cur.metrics.getY_scale()) {
          org_dist = cur.funcDualproj(cur.zp2.getOrus()[cur.zp2.getOrus_idx() + point].x - orus_base.x,
              cur.zp2.getOrus()[cur.zp2.getOrus_idx() + point].y - orus_base.y);
        } else {
          FTVectorRec vec = new FTVectorRec();

          vec.x = TTUtil.FTMulFix(cur.zp2.getOrus()[cur.zp2.getOrus_idx() + point].x - orus_base.x,
              cur.metrics.getX_scale());
          vec.y = TTUtil.FTMulFix(cur.zp2.getOrus()[cur.zp2.getOrus_idx() + point].y - orus_base.y,
              cur.metrics.getY_scale());
          org_dist = cur.funcDualproj(vec.x, vec.y);
        }
      }
      cur_dist = cur.funcProject(cur.zp2.getCurPoint_x(point) - cur_base.x,
          cur.zp2.getCurPoint_y(point) - cur_base.y);
      if (org_dist != 0) {
        if (old_range != 0) {
          new_dist = FTCalc.FT_MulDiv(org_dist, cur_range, old_range);
        } else {
            /* This is the same as what MS does for the invalid case:  */
            /*                                                         */
            /*   delta = (Original_Pt - Original_RP1) -                */
            /*           (Current_Pt - Current_RP1)                    */
            /*                                                         */
            /* In FreeType speak:                                      */
            /*                                                         */
            /*   new_dist = cur_dist -                                 */
            /*              org_dist - cur_dist;                       */
          new_dist = -org_dist;
        }
      } else {
        new_dist = 0;
      }
      cur.render_funcs.curr_move_func.move(cur.zp2, point, new_dist - cur_dist);
    }
    cur.graphics_state.loop = 1;
    cur.new_top = cur.stack_idx;
  }

  /* =====================================================================
   * MSIRP[a]:     Move Stack Indirect Relative Position
   * Opcode range: 0x3A-0x3B
   * Stack:        f26.6 uint32 -->
   * =====================================================================
   */
  public void MSIRP() {
    int point;
    int distance;

    point = cur.stack[cur.stack_idx];
    if (TTUtil.BOUNDS(point, cur.zp1.getN_points()) || TTUtil.BOUNDS(cur.graphics_state.rp0, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
      /* UNDOCUMENTED!  The MS rasterizer does that with */
      /* twilight points (confirmed by Greg Hitchcock)   */
    if (cur.graphics_state.gep1 == 0) {
      cur.zp1.setOrgPoint(point, cur.zp0.getOrgPoint(cur.graphics_state.rp0));
      cur.funcMoveOrig(cur.zp1, point, cur.stack[cur.stack_idx + 1]);
      cur.zp1.setCurPoint(point, cur.zp1.getOrgPoint(point));
    }
    distance = cur.funcProject(cur.zp1.getCurPoint_x(point) - cur.zp0.getCurPoint_x(cur.graphics_state.rp0),
        cur.zp1.getCurPoint_y(point) - cur.zp0.getCurPoint_y(cur.graphics_state.rp0));
    cur.funcMove(cur.zp1, point, cur.stack[cur.stack_idx + 1] - distance);
    cur.graphics_state.rp1 = cur.graphics_state.rp0;
    cur.graphics_state.rp2 = point;
    if ((cur.opcode.getVal() & 1) != 0) {
      cur.graphics_state.rp0 = point;
    }
  }

  /* =====================================================================
   * ALIGNRP[]:    ALIGN Relative Point
   * Opcode range: 0x3C
   * Stack:        uint32 uint32... -->
   * =====================================================================
   */
  public void ALIGNRP() {
    int point;
    int distance;

    if (cur.top < cur.graphics_state.loop || TTUtil.BOUNDS(cur.graphics_state.rp0, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.loop = 1;
      cur.new_top = cur.stack_idx;
      return;
    }
    while (cur.graphics_state.loop > 0) {
      cur.stack_idx--;
      point = cur.stack[cur.stack_idx];
      if (TTUtil.BOUNDS(point, cur.zp1.getN_points())) {
        if (cur.pedantic_hinting) {
          cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
          return;
        }
      } else {
        distance = cur.funcProject(cur.zp1.getCurPoint_x(point) - cur.zp0.getCurPoint_x(cur.graphics_state.rp0),
            cur.zp1.getCurPoint_y(point) - cur.zp0.getCurPoint_y(cur.graphics_state.rp0));
        Debug(0, DebugTag.DBG_INTERP, TAG, String.format("distance: %d, zp1.cur[point].x: %d, zp1.cur[point].x: %d, zp0.cur[rp0].x: %d, zp0.cur[rp0].y: %d", distance, cur.zp1.getCurPoint_x(point), cur.zp1.getCurPoint_y(point), cur.zp0.getCurPoint_x(cur.graphics_state.rp0), cur.zp0.getCurPoint_y(cur.graphics_state.rp0)));
        cur.funcMove(cur.zp1, point, -distance);
      }
      cur.graphics_state.loop--;
    }
    cur.graphics_state.loop = 1;
    cur.new_top = cur.stack_idx;
  }

  /* =====================================================================
   * RTDG[]:       Round To Double Grid
   * Opcode range: 0x3D
   * Stack:        -->
   *
   * =====================================================================
   */
  public void RTDG() {
    cur.graphics_state.round_state = TTInterpTags.Round.To_Double_Grid;
    cur.render_funcs.curr_round_func = cur.render_funcs.round_to_double_grid;
  }

  /* =====================================================================
   * MIAP[a]:      Move Indirect Absolute Point
   * Opcode range: 0x3E-0x3F
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void MIAP() {
    int cvtEntry;
    int point;
    int distance;
    int org_dist;
    int control_value_cutin;

    control_value_cutin = cur.graphics_state.control_value_cutin;
    cvtEntry = cur.stack[cur.stack_idx + 1];
    point = cur.stack[cur.stack_idx];
    if (TTUtil.BOUNDS(point, cur.zp0.getN_points()) || TTUtil.BOUNDSL(cvtEntry, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.rp0 = point;
      cur.graphics_state.rp1 = point;
      return;
    }
      /* UNDOCUMENTED!                                                      */
      /*                                                                    */
      /* The behaviour of an MIAP instruction is quite different when used  */
      /* in the twilight zone.                                              */
      /*                                                                    */
      /* First, no control value cut-in test is performed as it would fail  */
      /* anyway.  Second, the original point, i.e. (org_x,org_y) of         */
      /* zp0.point, is set to the absolute, unrounded distance found in the */
      /* CVT.                                                               */
      /*                                                                    */
      /* This is used in the CVT programs of the Microsoft fonts Arial,     */
      /* Times, etc., in order to re-adjust some key font heights.  It      */
      /* allows the use of the IP instruction in the twilight zone, which   */
      /* otherwise would be invalid according to the specification.         */
      /*                                                                    */
      /* We implement it with a special sequence for the twilight zone.     */
      /* This is a bad hack, but it seems to work.                          */
      /*                                                                    */
      /* Confirmed by Greg Hitchcock.                                       */
    distance = cur.funcReadCvt(0);
    if (cur.graphics_state.gep0 == 0) {  /* If in twilight zone */
      cur.zp0.setOrgPoint_x(point, (TTUtil.TTMulFix14(distance, cur.graphics_state.freeVector.x)));
      cur.zp0.setOrgPoint_y(point, (TTUtil.TTMulFix14(distance, cur.graphics_state.freeVector.y)));
      cur.zp0.setCurPoint(point, cur.zp0.getOrgPoint(point));
    }
    org_dist = cur.funcProject(cur.zp0.getCurPoint_x(point) - cur.zp0.getCurPoint_x(point),
        cur.zp0.getCurPoint_y(point) - cur.zp0.getCurPoint_y(point));
    if ((cur.opcode.getVal() & 1) != 0) {  /* rounding and control cut-in flag */
      if (FTCalc.FT_ABS(distance - org_dist) > control_value_cutin) {
        distance = org_dist;
      }
      distance = cur.funcRound(distance, cur.tt_metrics.getCompensations()[0]);
    }
    cur.funcMove(cur.zp0, point, distance - org_dist);
    cur.graphics_state.rp0 = point;
    cur.graphics_state.rp1 = point;
  }

}