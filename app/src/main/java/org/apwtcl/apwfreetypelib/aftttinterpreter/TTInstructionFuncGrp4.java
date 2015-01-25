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
      if (cur.graphics_state.projVector.getY() == 0) {
        cur.tt_metrics.setRatio(cur.tt_metrics.getX_ratio());
      } else {
        if (cur.graphics_state.projVector.getX() == 0) {
          cur.tt_metrics.setRatio(cur.tt_metrics.getY_ratio());
        } else {
          int x;
          int y;

          x = TTUtil.TTMulFix14(cur.tt_metrics.getX_ratio(), cur.graphics_state.projVector.getX() << 2);
          y = TTUtil.TTMulFix14(cur.tt_metrics.getY_ratio(), cur.graphics_state.projVector.getY() << 2);
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
   * NPUSHB[]:     PUSH N Bytes
   * Opcode range: 0x40
   * Stack:        --> uint32...
   * =====================================================================
   */
  public void NPUSHB() {
    int length;
    int idx;

    length = cur.code[cur.IP + 1].getVal() & 0xFF;
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("NPUSHB: L: %d, cur.IP: %d", length, cur.IP));
    if (TTUtil.BOUNDS(length, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    for (idx = 0; idx < length; idx++) {
      cur.stack[cur.stack_idx + idx] = cur.code[cur.IP + idx + 2].getVal() & 0xFF;
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("pushb: %d 0x%02x, new_top: %d ", idx, cur.stack[cur.stack_idx + idx], cur.new_top)+cur.code[cur.IP + idx]);
      cur.new_top++;
    }
  }

  /* =====================================================================
   * NPUSHW[]:     PUSH N Words
   * Opcode range: 0x41
   * Stack:        --> int32...
   * =====================================================================
   */
  public void NPUSHW() {
    int length;
    int idx;

    length = cur.code[cur.IP + 1].getVal() & 0xFF;
    if (TTUtil.BOUNDS(length, cur.stackSize + 1 - cur.top)) {
      cur.error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
      return;
    }
    cur.IP += 2;
    for (idx = 0; idx < length; idx++) {
      cur.stack[cur.stack_idx + idx] = GetShortIns(cur);
      cur.new_top++;
    }
    cur.step_ins = false;
  }

  /* =====================================================================
   * WS[]:         Write Store
   * Opcode range: 0x42
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void WS() {
    int store_idx = cur.stack[cur.stack_idx];

    if (TTUtil.BOUNDSL(store_idx, cur.storeSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      }
    } else {
      cur.storage[store_idx] = cur.stack[cur.stack_idx + 1];
    }
  }

  /* =====================================================================
   * RS[]:         Read Store
   * Opcode range: 0x43
   * Stack:        uint32 --> uint32
   * =====================================================================
   */
  public void RS() {
    int store_idx = cur.stack[cur.stack_idx];

    if (TTUtil.BOUNDSL(store_idx, cur.storeSize)) {
      if (cur.pedantic_hinting ) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      } else {
        cur.stack[cur.stack_idx] = 0;
      }
    } else {
      cur.stack[cur.stack_idx] = cur.storage[store_idx];
    }
  }

  /* =====================================================================
   * WCVTP[]:      Write CVT in Pixel units
   * Opcode range: 0x44
   * Stack:        f26.6 uint32 -->
   * =====================================================================
   */
  public void WCVTP() {
    int cvt_idx = cur.stack[cur.stack_idx + 0];

    if (TTUtil.BOUNDSL(cvt_idx, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      }
    } else {
      cur.render_funcs.curr_cvt_func.writeCvt(cvt_idx, cur.stack[cur.stack_idx + 1]);
    }
  }

  /* =====================================================================
   * RCVT[]:       Read CVT
   * Opcode range: 0x45
   * Stack:        uint32 --> f26.6
   * =====================================================================
   */
  public void RCVT() {
    int cvt_idx = (int)cur.stack[cur.stack_idx + 0];

    if (TTUtil.BOUNDSL(cvt_idx, cur.cvtSize)) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_ARRAY_BOUND_ERROR;
      } else {
        cur.stack[cur.stack_idx + 0] = 0;
      }
    } else {
      int val = cur.render_funcs.curr_cvt_func.readCvt(cvt_idx) & 0xFFFF;
      cur.stack[cur.stack_idx + 0] = val;
    }
  }

  /* =====================================================================
   * GC[a]:        Get Coordinate projected onto
   * Opcode range: 0x46-0x47
   * Stack:        uint32 --> f26.6
   *
   * XXX: UNDOCUMENTED: Measures from the original glyph must be taken
   *      along the dual projection vector!
   * =====================================================================
   */
  public void GC() {
    int point_idx;
    int result;

    point_idx = cur.stack[cur.stack_idx];
    if (TTUtil.BOUNDSL(point_idx, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      result = 0;
    } else {
      if ((cur.opcode.getVal() & 1) != 0) {
        result = cur.funcDualproj(cur.zp2.getOrgPoint_x(point_idx), cur.zp2.getOrgPoint_y(point_idx));
      } else {
        result = cur.funcProject(cur.zp2.getCurPoint_x(point_idx), cur.zp2.getCurPoint_y(point_idx));
      }
    }
    cur.stack[cur.stack_idx] = result;
  }

  /* =====================================================================
   * SCFS[]:       Set Coordinate From Stack
   * Opcode range: 0x48
   * Stack:        f26.6 uint32 -->
   *
   * Formula:
   *
   *   OA := OA + ( value - OA.p )/( f.p ) * f
   * =====================================================================
   */
  public void SCFS() {
    int result;
    int point_idx;

    point_idx = cur.stack[cur.stack_idx];
    if (TTUtil.BOUNDS(point_idx, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
    result = cur.funcProject(cur.zp2.getCurPoint_x(point_idx), cur.zp2.getCurPoint_y(point_idx));
    cur.funcMove(cur.zp2, point_idx, (cur.stack[cur.stack_idx + 1] - result));
      /* UNDOCUMENTED!  The MS rasterizer does that with */
      /* twilight points (confirmed by Greg Hitchcock)   */
    if (cur.graphics_state.gep2 == 0) {
      cur.zp2.setOrgPoint(point_idx, cur.zp2.getCurPoint(point_idx));
    }
  }

  /* =====================================================================
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
   * =====================================================================
   */
  public void MD() {
    int point2_idx;
    int point1_idx;
    int result;

    point2_idx = cur.stack[cur.stack_idx + 1];
    point1_idx = cur.stack[cur.stack_idx + 0];
    if (TTUtil.BOUNDS(point1_idx, cur.zp0.getN_points()) || TTUtil.BOUNDS(point2_idx, cur.zp1.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      result = 0;
    } else {
      if ((cur.opcode.getVal() & 1) != 0) {
        result = cur.funcProject(cur.zp0.getCurPoint_x(point1_idx) - cur.zp1.getCurPoint_x(point2_idx),
            cur.zp0.getCurPoint_y(point1_idx) - cur.zp1.getCurPoint_y(point2_idx));
      } else {
          /* XXX: UNDOCUMENTED: twilight zone special case */
        if (cur.graphics_state.gep0 == 0 || cur.graphics_state.gep1 == 0) {
          FTVectorRec vec1 = cur.zp0.getOrgPoint(point1_idx);
          FTVectorRec vec2 = cur.zp1.getOrgPoint(point2_idx);

          result = cur.render_funcs.curr_project_func.dualproject(vec1.getX(), vec2.getY());
        } else {
          FTVectorRec vec1 = cur.zp0.getOrusPoint(point1_idx);
          FTVectorRec vec2 = cur.zp1.getOrusPoint(point2_idx);

          if (cur.metrics.getX_scale() == cur.metrics.getY_scale()) {
              /* this should be faster */
            result = cur.render_funcs.curr_project_func.dualproject(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
            result = TTUtil.FTMulFix(result, cur.metrics.getX_scale());
          } else {
            FTVectorRec vec = new FTVectorRec();

            vec.setX(TTUtil.FTMulFix(vec1.getX() - vec2.getX(), cur.metrics.getX_scale()));
            vec.setY(TTUtil.FTMulFix(vec1.getY() - vec2.getY(), cur.metrics.getY_scale()));
            result = cur.render_funcs.curr_project_func.dualproject(vec.getX(), vec.getY());
          }
        }
      }
    }
    cur.stack[cur.stack_idx] = result;
  }

  /* =====================================================================
   * MPPEM[]:      Measure Pixel Per EM
   * Opcode range: 0x4B
   * Stack:        --> Euint16
   * =====================================================================
   */
  public void MPPEM() {
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("MPPEM: %d", CurrentPpem(cur)));
    cur.stack[cur.stack_idx] = CurrentPpem(cur);
  }

  // Note: The pointSize should be irrelevant in a given font program;
  //       we thus decide to return only the ppem.

  /* =====================================================================
   * MPS[]:        Measure Point Size
   * Opcode range: 0x4C
   * Stack:        --> Euint16
   * =====================================================================
   */
  public void MPS() {
    cur.stack[cur.stack_idx] = CurrentPpem(cur);
  }

  /* =====================================================================
   * FLIPON[]:     Set auto-FLIP to ON
   * Opcode range: 0x4D
   * Stack:        -->
   * =====================================================================
   */
  public void FLIPON() {
    cur.graphics_state.auto_flip = true;
  }

  /* =====================================================================
   * FLIPOFF[]:    Set auto-FLIP to OFF
   * Opcode range: 0x4E
   * Stack: -->
   * =====================================================================
   */
  public void FLIPOFF() {
    cur.graphics_state.auto_flip = false;
  }

  /* =====================================================================
   * DEBUG[]:      DEBUG.  Unsupported.
   * Opcode range: 0x4F
   * Stack:        uint32 -->
   * =====================================================================
   */

   public void DEBUG() {
    cur.error = FTError.ErrorTag.INTERP_DEBUG_OPCODE;
  }

}