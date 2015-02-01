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
  /*    TTInstructionFuncGrp9                                              */
  /*    instructions functions group 9  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp9 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp9";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp9 ================================== */
  public TTInstructionFuncGrp9(TTExecContextRec exec)
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
   * MIRP[abcde]:  Move Indirect Relative Point
   * Opcode range: 0xE0-0xFF
   * Stack:        int32? uint32 -->
   * =====================================================================
   */
  public void MIRP() {
    int point;
    int cvtEntry;
    int cvt_dist;
    int distance;
    int cur_dist;
    int org_dist;
    int control_value_cutin;
    int minimum_distance;

//cur.zp0.showLoaderZone("zp0", cur);
//cur.zp1.showLoaderZone("zp1", cur);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MIRP: cur.GS.rp0: %d, cur.GS.rp1: %d", cur.graphics_state.rp0, cur.graphics_state.rp1));
    minimum_distance = cur.graphics_state.minimum_distance;
    control_value_cutin = cur.graphics_state.control_value_cutin;
    point = cur.stack[cur.stack_idx];
    cvtEntry = cur.stack[cur.stack_idx + 1] + 1;
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MIRP: minimum_distance: %d, control_value_cutin: %d, point: %d, cvtEntry: %d", minimum_distance, control_value_cutin, point, cvtEntry));
      /* XXX: UNDOCUMENTED! cvt[-1] = 0 always */
    if (TTUtil.BOUNDS(point, cur.zp1.getN_points()) ||
        TTUtil.BOUNDSL(cvtEntry, cur.cvtSize + 1) ||
        TTUtil.BOUNDS(cur.graphics_state.rp0, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.rp1 = cur.graphics_state.rp0;
      if ((cur.opcode.getVal() & 16) != 0) {
        cur.graphics_state.rp0 = point;
      }
      cur.graphics_state.rp2 = point;
      return;
    }
    if (cvtEntry == 0) {
      cvt_dist = 0;
    } else {
      cvt_dist = cur.render_funcs.curr_cvt_func.readCvt(cvtEntry - 1);
    }
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("cvtEntry: %d, cvt_dist: %d", cvtEntry, cvt_dist));
      /* single width test */
    if (FTCalc.FT_ABS(cvt_dist - cur.graphics_state.single_width_value) < cur.graphics_state.single_width_cutin) {
      if (cvt_dist >= 0) {
        cvt_dist = cur.graphics_state.single_width_value;
      } else {
        cvt_dist = -cur.graphics_state.single_width_value;
      }
    }
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("cvt_dist: %d, cur.GS.gep0: %d, cur.GS.gep1: %d", cvt_dist, cur.graphics_state.gep0, cur.graphics_state.gep1));
      /* UNDOCUMENTED!  The MS rasterizer does that with */
      /* twilight points (confirmed by Greg Hitchcock)   */
    if (cur.graphics_state.gep1 == 0) {
      cur.zp1.setOrgPoint_x(point, (cur.zp0.getOrgPoint_x(cur.graphics_state.rp0) + TTUtil.TTMulFix14(cvt_dist, cur.graphics_state.freeVector.getX())));
      cur.zp1.setOrgPoint_y(point, (cur.zp0.getOrgPoint_y(cur.graphics_state.rp0) + TTUtil.TTMulFix14(cvt_dist, cur.graphics_state.freeVector.getY())));
      cur.zp1.setCurPoint(point, cur.zp1.getOrgPoint(point));
    }
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("dualproj: point: %d, GS.rp0: %d, zp1.org[point].x: %d, zp1.org[point].y: %d, zp0.org[GS.rp0].x: %d, zp0.org[GS.rp0].y: %d", point, cur.graphics_state.rp0, cur.zp1.getOrgPoint_x(point), cur.zp1.getOrgPoint_y(point), cur.zp0.getOrgPoint_x(cur.graphics_state.rp0), cur.zp0.getOrgPoint_y(cur.graphics_state.rp0)));
    org_dist = cur.funcDualproj(cur.zp1.getOrgPoint_x(point) - cur.zp0.getOrgPoint_x(cur.graphics_state.rp0),
        cur.zp1.getOrgPoint_y(point) - cur.zp0.getOrgPoint_y(cur.graphics_state.rp0));
    cur_dist = cur.funcProject(cur.zp1.getCurPoint_x(point) - cur.zp0.getCurPoint_x(cur.graphics_state.rp0),
        cur.zp1.getCurPoint_y(point) - cur.zp0.getCurPoint_y(cur.graphics_state.rp0));
      /* auto-flip test */
    if (cur.graphics_state.auto_flip) {
      if ((org_dist ^ cvt_dist) < 0) {
        cvt_dist = -cvt_dist;
      }
    }
      /* control value cut-in and round */
    if ((cur.opcode.getVal() & 4) != 0) {
        /* XXX: UNDOCUMENTED!  Only perform cut-in test when both points */
        /*      refer to the same zone.                                  */
      if (cur.graphics_state.gep0 == cur.graphics_state.gep1) {
          /* XXX: According to Greg Hitchcock, the following wording is */
          /*      the right one:                                        */
          /*                                                            */
          /*        When the absolute difference between the value in   */
          /*        the table [CVT] and the measurement directly from   */
          /*        the outline is _greater_ than the cut_in value, the */
          /*        outline measurement is used.                        */
          /*                                                            */
          /*      This is from `instgly.doc'.  The description in       */
          /*      `ttinst2.doc', version 1.66, is thus incorrect since  */
          /*      it implies `>=' instead of `>'.                       */
        if (FTCalc.FT_ABS(cvt_dist - org_dist) > control_value_cutin) {
          cvt_dist = org_dist;
        }
      }
      distance = cur.funcRound(cvt_dist, cur.tt_metrics.getCompensations()[cur.opcode.getVal() & 3]);
    } else {
      distance = cur.render_funcs.round_none.round(cvt_dist, cur.tt_metrics.getCompensations()[cur.opcode.getVal() & 3]);
    }
      /* minimum distance test */
    if ((cur.opcode.getVal() & 8) != 0) {
      if (org_dist >= 0) {
        if (distance < minimum_distance) {
          distance = minimum_distance;
        }
      } else {
        if (distance > -minimum_distance) {
          distance = -minimum_distance;
        }
      }
    }
    cur.funcMove(cur.zp1, point, distance - cur_dist);
    cur.graphics_state.rp1 = cur.graphics_state.rp0;
    if ((cur.opcode.getVal() & 16) != 0) {
      cur.graphics_state.rp0 = point;
    }
    cur.graphics_state.rp2 = point;
  }

  /* =====================================================================
   * MDRP[abcde]:  Move Direct Relative Point
   * Opcode range: 0xC0-0xDF
   * Stack:        uint32 -->
   * =====================================================================
   */
  public void MDRP() {
    short point;
    int org_dist;
    int distance;
    int minimum_distance;

Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP: cur.GS.gep0: %d, cur.GS.gep1: %d", cur.graphics_state.gep0, cur.graphics_state.gep1));
    minimum_distance = cur.graphics_state.minimum_distance;
    point = (short)cur.stack[cur.stack_idx + 0];
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP: minimum_distance: %d point: %d rp0: %d",  minimum_distance, point, cur.graphics_state.rp0));
    if (TTUtil.BOUNDS(point, cur.zp1.getN_points()) || TTUtil.BOUNDS(cur.graphics_state.rp0, cur.zp0.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      cur.graphics_state.rp1 = cur.graphics_state.rp0;
      cur.graphics_state.rp2 = point;
      if ((cur.opcode.getVal() & 16) != 0) {
        cur.graphics_state.rp0 = point;
      }
      return;
    }
      /* XXX: Is there some undocumented feature while in the */
      /*      twilight zone?                                  */
      /* XXX: UNDOCUMENTED: twilight zone special case */
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("cur.GS.gep0: %d, cur.GS.gep1: %d\n", cur.graphics_state.gep0, cur.graphics_state.gep1));
    if (cur.graphics_state.gep0 == 0 || cur.graphics_state.gep1 == 0) {
      FTVectorRec vec1 = cur.zp1.getOrgPoint(point);
      FTVectorRec vec2 = cur.zp0.getOrgPoint(cur.graphics_state.rp0);

      org_dist = cur.funcDualproj(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
    } else {
      FTVectorRec vec1 = cur.zp1.getOrus()[cur.zp1.getOrus_idx() + point];
      FTVectorRec vec2 = cur.zp0.getOrus()[cur.zp1.getOrus_idx() + cur.graphics_state.rp0];

Debug(0, DebugTag.DBG_INTERP, TAG, String.format("vec1: %d %d, vec2: %d %d\n", vec1.getX(), vec1.getY(), vec2.getX(), vec2.getY()));
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("x_scale: %d y_scale: %d", cur.metrics.getX_scale(), cur.metrics.getY_scale()));
      if (cur.metrics.getX_scale() == cur.metrics.getY_scale()) {
          /* this should be faster */
        org_dist = cur.render_funcs.curr_project_func.dualproject(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("d1: %d", org_dist));
        org_dist = TTUtil.FTMulFix(org_dist, cur.metrics.getX_scale());
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("d2: %d", org_dist));
      } else {
        FTVectorRec vec = new FTVectorRec();

        vec.setX(TTUtil.FTMulFix(vec1.getX() - vec2.getX(), cur.metrics.getX_scale()));
        vec.setY(TTUtil.FTMulFix(vec1.getY() - vec2.getY(), cur.metrics.getY_scale()));
        org_dist = cur.render_funcs.curr_project_func.dualproject(vec.getX(), vec.getY());
      }
    }
Debug(0, DebugTag.DBG_INTERP, TAG, "org_dist1: "+org_dist);
      /* single width cut-in test */
    if (FTCalc.FT_ABS(org_dist - cur.graphics_state.single_width_value) < cur.graphics_state.single_width_cutin) {
      if (org_dist >= 0) {
        org_dist = cur.graphics_state.single_width_value;
      } else {
        org_dist = -cur.graphics_state.single_width_value;
      }
    }
Debug(0, DebugTag.DBG_INTERP, TAG, "org_dist2: "+org_dist);
      /* round flag */
    if ((cur.opcode.getVal() & 4) != 0) {
      distance = cur.funcRound(org_dist, cur.tt_metrics.getCompensations()[cur.opcode.getVal() & 3]);
    } else {
      distance = cur.render_funcs.round_none.round(org_dist, cur.tt_metrics.getCompensations()[cur.opcode.getVal() & 3]);
    }
Debug(0, DebugTag.DBG_INTERP, TAG, "distance1: "+distance);
      /* minimum distance flag */
    if ((cur.opcode.getVal() & 8) != 0) {
      if (org_dist >= 0) {
        if (distance < minimum_distance) {
          distance = minimum_distance;
        }
      } else {
        if (distance > -minimum_distance) {
          distance = -minimum_distance;
        }
      }
    }
Debug(0, DebugTag.DBG_INTERP, TAG, "distance2: "+distance);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP 3: cur.GS.rp0: %d, cur.GS.rp1: %d, cur.GS.gep0: %d, cur.GS.gep1: %d", cur.graphics_state.rp0, cur.graphics_state.rp1, cur.graphics_state.gep0, cur.graphics_state.gep1));
      /* now move the point */
    org_dist = cur.funcProject(cur.zp1.getCurPoint_x(point) - cur.zp0.getCurPoint_x(cur.graphics_state.rp0),
        cur.zp1.getCurPoint_y(point) - cur.zp0.getCurPoint_y(cur.graphics_state.rp0));
Debug(0, DebugTag.DBG_INTERP, TAG, "org_dist3: "+org_dist);
    cur.render_funcs.curr_move_func.move(cur.zp1 , point,distance - org_dist);

Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP 5: cur.GS.rp0: %d, cur.GS.rp1: %d, cur.GS.rp2: %d", cur.graphics_state.rp0, cur.graphics_state.rp1, cur.graphics_state.rp2));
    cur.graphics_state.rp1 = cur.graphics_state.rp0;
    cur.graphics_state.rp2 = point;
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP 6: cur.GS.rp0: %d, cur.GS.rp1: %d, cur.GS.rp2: %d opcode: ", cur.graphics_state.rp0, cur.graphics_state.rp1, cur.graphics_state.rp2)+cur.opcode);
    if ((cur.opcode.getVal() & 16) != 0) {
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP 7: cur.GS.rp0: %d, cur.GS.rp1: %d, cur.GS.rp2: %d", cur.graphics_state.rp0, cur.graphics_state.rp1, cur.graphics_state.rp2));
      cur.graphics_state.rp0 = point;
    }
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MDRP end: cur.GS.rp0: %d, cur.GS.rp1: %d", cur.graphics_state.rp0, cur.graphics_state.rp1));
  }

  /* =====================================================================
   * PUSHW[abc]:   PUSH Words
   * Opcode range: 0xB8-0xBF
   * Stack:        --> int32...
   * =====================================================================
   */
  public void PUSHW() {
    int length;
    int idx;

Debug(0, DebugTag.DBG_INTERP, TAG, "PUSHW");
    length = (cur.opcode.getVal() - 0xB8 + 1);
    if (TTUtil.BOUNDS(length, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    cur.IP++;
    for (idx = 0; idx < length; idx++) {
      cur.stack[cur.stack_idx + idx] = TTInstructionFuncGrp4.GetShortIns(cur);
    }
    cur.step_ins = false;
  }

  /* =====================================================================
   * PUSHB[abc]:   PUSH Bytes
   * Opcode range: 0xB0-0xB7
   * Stack:        --> uint32...
   * =====================================================================
   */
  public void PUSHB() {
    int length;
    int idx;

    length = (cur.opcode.getVal() - TTOpCode.OpCode.PushB_0.getVal() + 1);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("PUSHB stack_idx: %d length: %d", cur.stack_idx, length));
    if (TTUtil.BOUNDS(length, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    for (idx = 0; idx< length; idx++) {
      cur.stack[cur.stack_idx + idx] = (cur.code[cur.IP + idx + 1].getVal() & 0xFF);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("idx: %d IP: %d %s", idx, cur.IP, cur.code[cur.IP + idx + 1].toString()));
    }
  }

}