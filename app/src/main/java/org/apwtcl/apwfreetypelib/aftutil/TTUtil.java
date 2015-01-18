package org.apwtcl.apwfreetypelib.aftutil;

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

public class TTUtil extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTUtil";

  /* ==================== TTUtil ================================== */
  public TTUtil() {
    oid++;
    id = oid;
  }


  /* ==================== TagToInt ===================================== */
    public static int TagToInt(String str) {
        return str.charAt(0)<<24|str.charAt(1)<<16|str.charAt(2)<<8|str.charAt(3);
    }

  /* ==================== Pack ===================================== */
  public static byte Pack(int x, int y) {
    byte valx = (byte)x;
    byte valy = (byte)y;
    byte valx_nibble = (byte)(((valx & 0xF) << 4) & 0xFF);
    byte valy_nibble = (byte)((valy & 0xF));

    return (byte)((valx_nibble | valy_nibble) & 0xFF);
  }

  /* ==================== BOUNDS ===================================== */
  public static boolean BOUNDS(int x, int n) {
    return (x >= n);
  }

  /* ==================== BOUNDSL ===================================== */
  public static boolean BOUNDSL(int x, int n) {
    return (x  >= n);
  }

  /* ==================== Normalize =====================================
   *
   * <Function>
   *    Normalize
   *
   * <Description>
   *    Norms a vector.
   *
   * <Input>
   *    Vx :: The horizontal input vector coordinate.
   *    Vy :: The vertical input vector coordinate.
   *
   * <Output>
   *    R  :: The normed unit vector.
   *
   * <Return>
   *    Returns FAILURE if a vector parameter is zero.
   *
   * <Note>
   *    In case Vx and Vy are both zero, Normalize() returns SUCCESS, and
   *    R is undefined.
   * ====================================================================
   */
  public static boolean Normalize(int Vx, int Vy, FTReference<FTVectorRec> R_ref) {
    int  W;
    FTVectorRec R = R_ref.Get();

    Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("Normalize: Vx: %d, vy: %d", Vx, Vy));
    if (FTCalc.FT_ABS(Vx) < 0x4000L && FTCalc.FT_ABS(Vy) < 0x4000L) {
      if (Vx == 0 && Vy == 0) {
          /* XXX: UNDOCUMENTED! It seems that it is possible to try   */
          /*      to normalize the vector (0,0).  Return immediately. */
        return true;
      }
      Vx *= 0x4000;
      Vy *= 0x4000;
      Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("Normalize2: Vx: %d, vy: %d", Vx, Vy));
    }
    Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("Normalize2: Vx: %d, vy: %d", Vx, Vy));
    W = FTCalc.FTHypot(Vx, Vy);
    Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("W: %d 0x%x",  W, W));
    R.x = FTCalc.FTDivFix(Vx, W << 2);
    R.y = FTCalc.FTDivFix(Vy, W << 2);
    Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("R.x: %d, R.y: %d", R.x, R.y));
    R_ref.Set(R);
    return true;
  }

  /* ==================== FTMulFix ===================================== */
  public static int FTMulFix(int a, int b) {
    int s = 1;
    int c;

    if (a < 0) {
      a = -a;
      s = -1;
    }
    if (b < 0) {
      b = -b;
      s = -s;
    }
    c = ((a * b + 0x8000) >> 16);
    return (s > 0) ? c : -c;
  }

  /* ==================== TTMulFix14 ===================================== */
  public static int TTMulFix14(int a, int b) {
    int sign;
    int ah;
    int al;
    int mid;
    int lo;
    int hi;

    sign = (int)(a ^ b);
    if (a < 0) {
      a = -a;
    }
    if (b < 0) {
      b = -b;
    }
    ah = ((a >> 16) & 0xFFFF);
    al = (a & 0xFFFF);
    lo = al * b;
    mid = ah * b;
    hi = mid >> 16;
    mid = (mid << 16) + (1 << 13); /* rounding */
    lo += mid;
    if (lo < mid) {
      hi += 1;
    }
    mid = ((lo >> 14) | (hi << 18));
    return sign >= 0 ? mid : -mid;
  }

}
