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
  /*    TTInstructionFuncGrp4                                              */
  /*    instructions functions group 4  for interpreter                    */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp4 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp4";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp4 ================================== */
  public TTInstructionFuncGrp4(TTExecContextRec exec)
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

  /* ====================================================================
   * <Function>
   *    CurrentRatio
   *
   * <Description>
   *    Returns the current aspect ratio scaling factor depending on the
   *    projection vector's state and device resolutions.
   *
   * <Return>
   *    The aspect ratio in 16.16 format, always <= 1.0 .
   *
   * ====================================================================
   */
  protected static int CurrentRatio(TTExecContextRec cur) {
    if (cur.tt_metrics.getRatio() == 0) {
      if (cur.graphics_state.projVector.y == 0) {
        cur.tt_metrics.setRatio(cur.tt_metrics.getX_ratio());
      } else {
        if (cur.graphics_state.projVector.x == 0) {
          cur.tt_metrics.setRatio(cur.tt_metrics.getY_ratio());
        } else {
          int x;
          int y;

          x = TTUtil.TTMulFix14(cur.tt_metrics.getX_ratio(), cur.graphics_state.projVector.x << 2);
          y = TTUtil.TTMulFix14(cur.tt_metrics.getY_ratio(), cur.graphics_state.projVector.y << 2);
          cur.tt_metrics.setRatio(FTCalc.FTHypot(x, y));
        }
      }
    }
    return cur.tt_metrics.getRatio();
  }

  /* ==================== currentPpem ===================================== */
  public static int CurrentPpem(TTExecContextRec cur) {
    return FTCalc.FTMulFix(cur.tt_metrics.getPpem(), CurrentRatio(cur));
  }

    /* =====================================================================
     * <Function>
     *    GetShortIns
     *
     * <Description>
     *    Returns a short integer taken from the instruction stream at
     *    address IP.
     *
     * <Return>
     *    Short read at code[IP].
     *
     * <Note>
     *    This one could become a macro.
     * =====================================================================
     */

  public static int GetShortIns(TTExecContextRec cur) {
    short val;
    short short1;
    short short2;
      /* Reading a byte stream so there is no endianess (DaveP) */
    cur.IP += 2;
    short1 = (short)((cur.code[cur.IP - 2].getVal() & 0xFF) << 8);
    short2 = (short)(cur.code[cur.IP - 1].getVal() & 0xFF);
    val = (short)((short1 + short2) & 0xFFFF);
    return val;
//      return (short)(((cur.code[cur.IP - 2]) << 8) + cur.code[cur.IP - 1]);
  }

  /* =====================================================================
   *
   * NPUSHB[]:     PUSH N Bytes
   * Opcode range: 0x40
   * Stack:        --> uint32...
   *
   * =====================================================================
   */
  public void NPUSHB() {
    int L;
    int K;

    L = cur.code[cur.IP + 1].getVal() & 0xFF;
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("insNPUSHB: L: %d, cur.IP: %d", L, cur.IP));
    if (TTUtil.BOUNDS(L, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    for (K = 1; K <= L; K++) {
      cur.stack[cur.numArgs + K - 1] = cur.code[cur.IP + K + 1].getVal() & 0xFF;
      Debug(0, DebugTag.DBG_INTERP, TAG, String.format("pushb: %d 0x%02x, numArgs: %d ", K-1, cur.stack[cur.numArgs + K - 1], cur.numArgs)+cur.code[cur.IP + K + 1]);
    }
    cur.new_top += L;
  }

  /* =====================================================================
   *
   * NPUSHW[]:     PUSH N Words
   * Opcode range: 0x41
   * Stack:        --> int32...
   *
   * =====================================================================
   */
  public void NPUSHW() {
    int L;
    int K;

    L = cur.code[cur.IP + 1].getVal() & 0xFF;
    if (TTUtil.BOUNDS(L, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    cur.IP += 2;
    for (K = 0; K < L; K++) {
      cur.stack[cur.numArgs + K] = GetShortIns(cur);
    }
    cur.step_ins = false;
    cur.new_top += L;
  }

  /* ==================== WS ===================================== */
  public void WS() {
    int I = cur.stack[cur.numArgs + 0];

    if (TTUtil.BOUNDSL(I, cur.storeSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      }
    } else {
      cur.storage[I] = cur.stack[cur.numArgs + 1];
    }
  }

  /* ==================== RS ===================================== */
  public void RS() {
    int I = (int)cur.stack[cur.numArgs + 0];

    if (TTUtil.BOUNDSL(I, cur.storeSize)) {
      if (cur.pedantic_hinting ) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      } else {
        cur.stack[cur.numArgs + 0] = 0;
      }
    } else {
      cur.stack[cur.numArgs + 0] = cur.storage[I];
    }
  }

  /* ==================== WCVTP ===================================== */
  public void WCVTP() {
    int I = cur.stack[cur.numArgs + 0];


    if (TTUtil.BOUNDSL(I, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      }
    } else {
      cur.render_funcs.curr_cvt_func.writeCvt(I, cur.stack[cur.numArgs + 1]);
    }
  }

  /* ==================== RCVT ===================================== */
  public void RCVT() {
    int I = (int)cur.stack[cur.numArgs + 0];

    if (TTUtil.BOUNDSL(I, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      } else {
        cur.stack[cur.numArgs + 0] = 0;
      }
    } else {
      int val = cur.render_funcs.curr_cvt_func.readCvt(I) & 0xFFFF;
      cur.stack[cur.numArgs + 0] = val;
    }
  }

  /* =====================================================================
   *
   * GC[a]:        Get Coordinate projected onto
   * Opcode range: 0x46-0x47
   * Stack:        uint32 --> f26.6
   *
   * XXX: UNDOCUMENTED: Measures from the original glyph must be taken
   *      along the dual projection vector!
   *
   * =====================================================================
   */
  public void GC() {
    int L;
    int R;

    L = cur.stack[cur.numArgs + 0];
    if (TTUtil.BOUNDSL(L, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      R = 0;
    } else {
      if ((cur.opcode.getVal() & 1) != 0) {
        R = cur.funcDualproj(cur.zp2.getOrg()[cur.zp2.getOrg_idx() + L].x - cur.zp2.getOrg()[cur.zp2.getOrg_idx() + L].x,
            cur.zp2.getOrg()[cur.zp2.getOrg_idx() + L].y - cur.zp2.getOrg()[cur.zp2.getOrg_idx() + L].y);
      } else {
        R = cur.funcProject(cur.zp2.getCur()[cur.zp2.getCur_idx() + L].x - cur.zp2.getCur()[cur.zp2.getCur_idx() + L].x,
            cur.zp2.getCur()[cur.zp2.getCur_idx() + L].y - cur.zp2.getCur()[cur.zp2.getCur_idx() + L].y);
      }
    }
    cur.stack[cur.numArgs + 0] = R;
  }

  /* =====================================================================
   *
   * SCFS[]:       Set Coordinate From Stack
   * Opcode range: 0x48
   * Stack:        f26.6 uint32 -->
   *
   * Formula:
   *
   *   OA := OA + ( value - OA.p )/( f.p ) * f
   *
   * =====================================================================
   */
  public void SCFS() {
    int K;
    int L;

    L = cur.stack[cur.numArgs + 0];
    if (TTUtil.BOUNDS(L, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    K =cur.funcProject(cur.zp2.getCur()[cur.zp2.getCur_idx() + L].x - cur.zp2.getCur()[cur.zp2.getCur_idx() + L].x,
        cur.zp2.getCur()[cur.zp2.getCur_idx() + L].y - cur.zp2.getCur()[cur.zp2.getCur_idx() + L].y);
    cur.funcMove(cur.zp2, L, (int)(cur.stack[cur.numArgs + 1] - K));
      /* UNDOCUMENTED!  The MS rasterizer does that with */
      /* twilight points (confirmed by Greg Hitchcock)   */
    if (cur.graphics_state.gep2 == 0) {
      cur.zp2.getOrg()[cur.zp2.getOrg_idx() + L] = cur.zp2.getCur()[cur.zp2.getCur_idx() + L];
    }
  }

  /* =====================================================================
   *
   * MD[a]:        Measure Distance
   * Opcode range: 0x49-0x4A
   * Stack:        uint32 uint32 --> f26.6
   *
   * XXX: UNDOCUMENTED: Measure taken in the original glyph must be along
   *                    the dual projection vector.
   *
   * XXX: UNDOCUMENTED: Flag attributes are inverted!
   *                      0 => measure distance in original outline
   *                      1 => measure distance in grid-fitted outline
   *
   * XXX: UNDOCUMENTED: `zp0 - zp1', and not `zp2 - zp1!
   *
   * =====================================================================
   */
  public void MD() {
    short K;
    short L;
    int D;

    K = (short)cur.stack[cur.numArgs + 1];
    L = (short)cur.stack[cur.numArgs + 0];
    if (TTUtil.BOUNDS(L, cur.zp0.getN_points()) || TTUtil.BOUNDS(K, cur.zp1.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      D = 0;
    } else {
      if ((cur.opcode.getVal() & 1) != 0) {
        D = cur.funcProject(cur.zp0.getCur()[cur.zp0.getCur_idx() + L].x - cur.zp1.getCur()[cur.zp0.getCur_idx() + K].x,
            cur.zp0.getCur()[cur.zp0.getCur_idx() + L].y - cur.zp1.getCur()[cur.zp0.getCur_idx() + K].y);
      } else {
          /* XXX: UNDOCUMENTED: twilight zone special case */
        if (cur.graphics_state.gep0 == 0 || cur.graphics_state.gep1 == 0) {
          FTVectorRec vec1 = cur.zp0.getOrg()[cur.zp0.getOrg_idx() + L];
          FTVectorRec vec2 = cur.zp1.getOrg()[cur.zp1.getOrg_idx() + K];

          D = cur.render_funcs.curr_project_func.dualproject(vec1.x, vec2.y);
        } else {
          FTVectorRec vec1 = cur.zp0.getOrus()[cur.zp0.getOrus_idx() + L];
          FTVectorRec vec2 = cur.zp1.getOrus()[cur.zp1.getOrus_idx() + K];

          if (cur.metrics.getX_scale() == cur.metrics.getY_scale()) {
              /* this should be faster */
            D = cur.render_funcs.curr_project_func.dualproject(vec1.x - vec2.x, vec1.y - vec2.y);
            D = TTUtil.FTMulFix(D, cur.metrics.getX_scale());
          } else {
            FTVectorRec vec = new FTVectorRec();

            vec.x = TTUtil.FTMulFix(vec1.x - vec2.x, cur.metrics.getX_scale());
            vec.y = TTUtil.FTMulFix(vec1.y - vec2.y, cur.metrics.getY_scale());
            D = cur.render_funcs.curr_project_func.dualproject(vec.x, vec.y);
          }
        }
      }
    }
    cur.stack[cur.numArgs + 0] = D;
  }

  /* ==================== MPPEM ===================================== */
  public void MPPEM() {
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("DO_MPPEM: %d", CurrentPpem(cur)));
    cur.stack[cur.numArgs + 0] = CurrentPpem(cur);
  }

  // Note: The pointSize should be irrelevant in a given font program;
  //       we thus decide to return only the ppem.
  /* ==================== MPS ===================================== */
  public void MPS() {
    cur.stack[cur.numArgs + 0] = CurrentPpem(cur);
  }

  /* ==================== FLIPON ===================================== */
  public void FLIPON() {
    cur.graphics_state.auto_flip = true;
  }

  /* ==================== FLIPPOFF ===================================== */
  public void FLIPOFF() {
    cur.graphics_state.auto_flip = false;
  }

  /* ==================== DEBUG ===================================== */
  public void DEBUG() {
    cur.error = FTError.ErrorTag.INTERP_DEBUG_OPCODE;
  }

}