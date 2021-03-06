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
  /*    TTInstructionFuncGrp0                                              */
  /*    instructions functions group 0  for interpreter                    */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTInstructionFuncGrp0 extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTInstructionFuncGrp0";

  private TTExecContextRec cur = null;

  /* ==================== TTInstructionFuncGrp0 ================================== */
  public TTInstructionFuncGrp0(TTExecContextRec exec)
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

  /* ==================== SxVTL =====================================
   * Set xVector to Line ?
   * ================================================================
   */
  private boolean SxVTL(int aIdx1, int aIdx2, TTOpCode.OpCode aOpc, FTReference<FTVectorRec> vec_ref) {
    int A;
    int B;
    int C;
    FTVectorRec p1;
    FTVectorRec p2;

    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("insSxVTL: aIdx1: %d, aIdx2: %d, aOpc: 0x%04x", aIdx1, aIdx2, aOpc));
    if (((int)aIdx1 >= (int)cur.zp2.getN_points()) || ((int)aIdx2 >= (int)cur.zp1.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return false;
    }
    p1 = cur.zp1.getCurPoint(aIdx2);
    p2 = cur.zp2.getCurPoint(aIdx1);
    A = p1.getX() - p2.getX();
    B = p1.getY() - p2.getY();
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("1: A: %d B: %d", A, B));
      /* If p1 == p2, SPVTL and SFVTL behave the same as */
      /* SPVTCA[X] and SFVTCA[X], respectively.          */
      /*                                                 */
      /* Confirmed by Greg Hitchcock.                    */
    if (A == 0 && B == 0) {
      A = 0x4000;
      aOpc = TTOpCode.OpCode.SVTCA_y;
    }
    if ((aOpc.getVal() & 1) != 0) {
      C = B;   /* counter clockwise rotation */
      B = A;
      A = -C;
    }
    Debug(0, DebugTag.DBG_INTERP, TAG, String.format("2: A: %d B: %d", A, B));
    TTUtil.Normalize(A, B, vec_ref);
    FTVectorRec vec = vec_ref.Get();
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("vec: %d %d", vec.getX(), vec.getY()));
    return true;
  }

  /* =====================================================================
   * SPVTL[a]:     Set PVector To Line
   * Opcode range: 0x06-0x07
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void SPvTL() {
    FTReference<FTVectorRec> ft_unit_ref = new FTReference<FTVectorRec>();

    ft_unit_ref.Set(cur.graphics_state.projVector);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("SPvTL: %d, %d", cur.stack[cur.stack_idx + 1], cur.stack[cur.stack_idx + 0]));
    if (SxVTL(cur.stack[cur.stack_idx + 1], cur.stack[cur.stack_idx + 0], cur.opcode, ft_unit_ref)) {
      cur.graphics_state.projVector = ft_unit_ref.Get();
      cur.graphics_state.dualVector = ft_unit_ref.Get();
      cur.render_funcs.ComputeFuncs(cur);
    }
  }

  /* =====================================================================
   * SFVTL[a]:     Set FVector To Line
   * Opcode range: 0x08-0x09
   * Stack:        uint32 uint32 -->
   * =====================================================================
   */
  public void SFVTL() {
    FTReference<FTVectorRec> ft_unit_ref = new FTReference<FTVectorRec>();

    ft_unit_ref.Set(cur.graphics_state.freeVector);
    if (SxVTL((short)cur.stack[cur.stack_idx + 1], (short)cur.stack[cur.stack_idx + 0], cur.opcode, ft_unit_ref)) {
      cur.graphics_state.freeVector = ft_unit_ref.Get();
      cur.render_funcs.ComputeFuncs(cur);
    }
  }

  /* =====================================================================
   * SPVFS[]:      Set PVector From Stack
   * Opcode range: 0x0A
   * Stack:        f2.14 f2.14 -->
   * =====================================================================
   */
  public void SPVFS() {
    short S;
    int X;
    int Y;
    FTReference<FTVectorRec> ft_unit_ref = new FTReference<FTVectorRec>();

    ft_unit_ref.Set(cur.graphics_state.projVector);
    // Only use low 16bits, then sign extend
    S = (short)cur.stack[cur.stack_idx + 1];
    Y = S;
    S = (short)cur.stack[cur.stack_idx + 0];
    X = S;
    TTUtil.Normalize(X, Y, ft_unit_ref);
    cur.graphics_state.projVector = ft_unit_ref.Get();
    cur.graphics_state.dualVector = cur.graphics_state.projVector;
    cur.render_funcs.ComputeFuncs(cur);
  }

  /* =====================================================================
   * SFVFS[]:      Set FVector From Stack
   * Opcode range: 0x0B
   * Stack:        f2.14 f2.14 -->
   * =====================================================================
   */
  public void SFVFS() {
    short S;
    int X;
    int Y;
    FTReference<FTVectorRec> ft_unit_ref = new FTReference<FTVectorRec>();

    ft_unit_ref.Set(cur.graphics_state.freeVector);
    // Only use low 16bits, then sign extend
    S = (short)cur.stack[cur.stack_idx + 1];
    Y = (int)S;
    S = (short)cur.stack[cur.stack_idx + 0];
    X = S;
    TTUtil.Normalize(X, Y, ft_unit_ref);
    cur.graphics_state.freeVector = ft_unit_ref.Get();
    cur.render_funcs.ComputeFuncs(cur);
  }


  /* =====================================================================
   * GPV[]:        Get Projection Vector
   * Opcode range: 0x0C
   * Stack:        ef2.14 --> ef2.14
   * =====================================================================
   */
  public void GPV() {
    cur.stack[cur.stack_idx] = cur.graphics_state.projVector.getX() & 0xFFFF;
    cur.stack[cur.stack_idx + 1] = cur.graphics_state.projVector.getY() & 0xFFFF;
  }

  /* =====================================================================
   * GFV[]:        Get Freedom Vector
   * Opcode range: 0x0D
   * Stack:        ef2.14 --> ef2.14
   * =====================================================================
   */
  public void GFV() {
    cur.stack[cur.stack_idx] = cur.graphics_state.freeVector.getX() & 0xFFFF;
    cur.stack[cur.stack_idx + 1] = cur.graphics_state.freeVector.getY() & 0xFFFF;
  }

  /* =====================================================================
   * SFVTPV[]:     Set FVector To PVector
   * Opcode range: 0x0E
   * Stack:        -->
   * =====================================================================
   */
  public void SFVTPV() {
    cur.graphics_state.freeVector = cur.graphics_state.projVector;
    cur.render_funcs.ComputeFuncs(cur);
  }


  /* =====================================================================
   * ISECT[]:      moves point to InterSECTion
   * Opcode range: 0x0F
   * Stack:        5 * uint32 -->
   * =====================================================================
   */
  public void ISECT() {
    int point;
    int a0;
    int a1;
    int b0;
    int b1;
    int discriminant;
    int dotproduct;

    int dx;
    int dy;
    int dax;
    int day;
    int dbx;
    int dby;
    int val;
    FTVectorRec R = new FTVectorRec();

    point = (short)cur.stack[cur.stack_idx + 0];
    a0 = (short)cur.stack[cur.stack_idx + 1];
    a1 = (short)cur.stack[cur.stack_idx + 2];
    b0 = (short)cur.stack[cur.stack_idx + 3];
    b1 = (short)cur.stack[cur.stack_idx + 4];

    if (TTUtil.BOUNDS(b0, cur.zp0.getN_points()) ||
        TTUtil.BOUNDS(b1, cur.zp0.getN_points()) ||
        TTUtil.BOUNDS(a0, cur.zp1.getN_points()) ||
        TTUtil.BOUNDS(a1, cur.zp1.getN_points()) ||
        TTUtil.BOUNDS(point, cur.zp2.getN_points())) {
      if (cur.pedantic_hinting) {
        cur.error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
      }
      return;
    }
      /* Cramer's rule */
    dbx = cur.zp0.getCurPoint_x(b1) - cur.zp0.getCurPoint_x(b0);
    dby = cur.zp0.getCurPoint_y(b1) - cur.zp0.getCurPoint_y(b0);
    dax = cur.zp1.getCurPoint_x(a1) - cur.zp1.getCurPoint_x(a0);
    day = cur.zp1.getCurPoint_y(a1) - cur.zp1.getCurPoint_y(a0);
    dx = cur.zp0.getCurPoint_x(b0) - cur.zp1.getCurPoint_x(a0);
    dy = cur.zp0.getCurPoint_y(b0) - cur.zp1.getCurPoint_y(a0);
    cur.zp2.addTag(point, Flags.Curve.TOUCH_X);
    cur.zp2.addTag(point, Flags.Curve.TOUCH_Y);
    discriminant = FTCalc.FT_MulDiv(dax, -dby, 0x40) + FTCalc.FT_MulDiv(day, dbx, 0x40);
    dotproduct = FTCalc.FT_MulDiv(dax, dbx, 0x40) + FTCalc.FT_MulDiv(day, dby, 0x40);
      /* The discriminant above is actually a cross product of vectors     */
      /* da and db. Together with the dot product, they can be used as     */
      /* surrogates for sine and cosine of the angle between the vectors.  */
      /* Indeed,                                                           */
      /*       dotproduct   = |da||db|cos(angle)                           */
      /*       discriminant = |da||db|sin(angle)     .                     */
      /* We use these equations to reject grazing intersections by         */
      /* thresholding abs(tan(angle)) at 1/19, corresponding to 3 degrees. */
    if (19 * FTCalc.FT_ABS(discriminant) > FTCalc.FT_ABS(dotproduct)) {
      val = FTCalc.FT_MulDiv(dx, -dby, 0x40) + FTCalc.FT_MulDiv(dy, dbx, 0x40);
      R.setX(FTCalc.FT_MulDiv(val, dax, discriminant));
      R.setY(FTCalc.FT_MulDiv(val, day, discriminant));
      cur.zp2.setCurPoint_x(point, (cur.zp1.getCurPoint_x(a0) + R.getX()));
      cur.zp2.setCurPoint_y(point, (cur.zp1.getCurPoint_y(a0) + R.getY()));
    } else {
        /* else, take the middle of the middles of A and B */
      cur.zp2.setCurPoint_x(point, ((cur.zp1.getCurPoint_x(a0) + cur.zp1.getCurPoint_x(a1) +
          cur.zp0.getCurPoint_x(b0) + cur.zp0.getCurPoint_x(b1)) / 4));
      cur.zp2.setCurPoint_y(point, ((cur.zp1.getCurPoint_y(a0) + cur.zp1.getCurPoint_y(a1) +
          cur.zp0.getCurPoint_y(b0) + cur.zp0.getCurPoint_y(b1)) / 4));
    }
  }

}