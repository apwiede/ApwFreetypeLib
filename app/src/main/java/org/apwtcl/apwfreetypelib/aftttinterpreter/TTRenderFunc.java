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
  /*    TTRenderFunc                                                       */
  /*    references to render functions for interpreter                     */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TTRenderFunc extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRenderFunc";

  public TTMoveFuncBase move = null;
  public TTMoveFuncBase move_x = null;
  public TTMoveFuncBase move_y = null;

  public TTProjectFuncBase project = null;
  public TTProjectFuncBase project_x = null;
  public TTProjectFuncBase project_y = null;

  public TTRoundFuncBase round_none = null;
  public TTRoundFuncBase round_to_grid = null;
  public TTRoundFuncBase round_up_to_grid = null;
  public TTRoundFuncBase round_down_to_grid = null;
  public TTRoundFuncBase round_to_half_grid = null;
  public TTRoundFuncBase round_to_double_grid = null;
  public TTRoundFuncBase round_super = null;
  public TTRoundFuncBase round_super45 = null;

  public TTCvtFuncBase cvt = null;
  public TTCvtFuncBase cvt_stretched = null;

  public TTMoveFuncBase curr_move_func = null;
  public TTProjectFuncBase curr_project_func = null;
  public TTRoundFuncBase curr_round_func = null;
  public TTCvtFuncBase curr_cvt_func = null;

  public static TTExecContextRec cur = null;

  /* ==================== TTRenderFunc ================================== */
  public TTRenderFunc()
  {
    oid++;
    id = oid;

    move = new TTMoveFunc();
    move.setCur(cur);
    move_x = new TTMoveXFunc();
    move_x.setCur(cur);
    move_y = new TTMoveYFunc();
    move_y.setCur(cur);

    project = new TTProjectFunc();
    project_x = new TTProjectXFunc();
    project_y = new TTProjectYFunc();

    round_none = new TTRoundNoneFunc();
    round_to_grid = new TTRoundToGridFunc();
    round_up_to_grid = new TTRoundUpToGridFunc();
    round_down_to_grid = new TTRoundDownToGridFunc();
    round_to_half_grid = new TTRoundToHalfGridFunc();
    round_to_double_grid = new TTRoundToDoubleGridFunc();
    round_super = new TTRoundSuperFunc();
    round_super45 = new TTRoundSuper45Func();

    cvt = new TTCvtFunc();
    cvt.setCur(cur);
    cvt_stretched = new TTCvtStretchedFunc();
    cvt_stretched.setCur(cur);
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
   *
   * <Function>
   *    ComputeRound
   *
   * <Description>
   *    Sets the rounding mode.
   *
   * <Input>
   *    round_mode :: The rounding mode to be used.
   *
   * =====================================================================
   */
  public void ComputeRound(TTInterpTags.Round round_mode) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "ComputeRound");
    switch (round_mode) {
      case Off:
        curr_round_func = round_none;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundNone");
        break;
      case To_Grid:
        curr_round_func = round_to_grid;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundToGrid");
        break;
      case Up_To_Grid:
        curr_round_func = round_up_to_grid;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundUpToGrid");
        break;
      case Down_To_Grid:
        curr_round_func = round_down_to_grid;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundDownToGrid");
        break;
      case To_Half_Grid:
        curr_round_func = round_to_half_grid;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundToHalfGrid");
        break;
      case To_Double_Grid:
        curr_round_func = round_to_double_grid;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundToDoubleGrid");
        break;
      case Super:
        curr_round_func = round_super;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundSuper");
        break;
      case Super_45:
        curr_round_func = round_super45;
        Debug(0, DebugTag.DBG_INTERP, TAG, "RoundSuper45");
        break;
    }
  }

  /* =====================================================================
   *
   * <Function>
   *    computeFuncs
   *
   * <Description>
   *    Computes the projection and movement function pointers according
   *    to the current graphics state.
   *
   * =====================================================================
   */
  public void ComputeFuncs(TTExecContextRec exec) {
    cur = exec;
    if (exec.graphics_state.getFreeVector().getX() == 0x4000) {
      exec.F_dot_P = exec.graphics_state.getProjVector().getX();
    } else {
      if (exec.graphics_state.getFreeVector().getY() == 0x4000) {
        exec.F_dot_P = exec.graphics_state.getProjVector().getY();
      } else {
        exec.F_dot_P = (exec.graphics_state.getProjVector().getX() * exec.graphics_state.getFreeVector().getX() +
            exec.graphics_state.getProjVector().getY() * exec.graphics_state.getFreeVector().getY()) >> 14;
      }
    }
    if (exec.graphics_state.getProjVector().getX() == 0x4000) {
      curr_project_func = project_x;
    } else {
      if (exec.graphics_state.getProjVector().getY() == 0x4000) {
        curr_project_func = project_y;
      } else {
        curr_project_func = project;
      }
    }
    curr_move_func = move;
    if (exec.F_dot_P == 0x4000) {
      if (exec.graphics_state.getFreeVector().getX() == 0x4000) {
        curr_move_func = move_x;
      } else {
        if (exec.graphics_state.getFreeVector().getY() == 0x4000) {
          curr_move_func = move_y;
        }
      }
    }
      /* at small sizes, F_dot_P can become too small, resulting   */
      /* in overflows and `spikes' in a number of glyphs like `w'. */
    if (FTCalc.FT_ABS(exec.F_dot_P) < 0x400L) {
      exec.F_dot_P = 0x4000;
    }
      /* Disable cached aspect ratio */
    exec.tt_metrics.setRatio(0);
  }
  /* =====================================================================
   *
   * <Function>
   *    setCvtFuncs
   *
   * <Description>
   *    Sets the rounding mode.
   *
   * <Input>
   *    round_mode :: The rounding mode to be used.
   *
   * =====================================================================
   */
  public void SetCvtFuncs(TTExecContextRec exec) {
    Debug(0, DebugTag.DBG_INTERP, TAG, "setCvtFuncs");
    exec.tt_metrics.setRatio(0);
    if (exec.metrics.getX_ppem() != exec.metrics.getY_ppem()) {
      /* non-square pixels, use the stretched routines */
      curr_cvt_func = cvt_stretched;
    } else {
      /* square pixels, use normal routines */
      curr_cvt_func = cvt;
    }
  }

}