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

package org.apwtcl.apwfreetypelib.aftutil;

  /* ===================================================================== */
  /*    FTCalc                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCalc extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTCalc";

    /* ==================== FTCalc ================================== */
    public FTCalc() {
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
      return str.toString();
    }
 
    /* ==================== FT_PAD_FLOOR ===================================== */
    public static int FT_PAD_FLOOR(int x, int n) {
        return (x & ~(n - 1));
    }

    /* ==================== FT_PAD_ROUND ===================================== */
    public static int FT_PAD_ROUND(int x, int n) {
      return FT_PAD_FLOOR(x + (n / 2), n);
    }

    /* ==================== FT_PAD_CEIL ===================================== */
    public static int FT_PAD_CEIL(int x, int n) {
        return FT_PAD_FLOOR(x + (n - 1), n);
    }

    /* ==================== FT_PIX_FLOOR ===================================== */
    public static int FT_PIX_FLOOR(int x) {
        return (x & ~63);
    }

    /* ==================== FT_PIX_ROUND ===================================== */
    public static int FT_PIX_ROUND(int x) {
        return FT_PIX_FLOOR(x + 32);
    }

    /* ==================== FT_PIX_CEIL ===================================== */
    public static int FT_PIX_CEIL(int x) {
        return FT_PIX_FLOOR(x + 63);
    }

    /* ==================== FT_ABS ===================================== */
    public static int FT_ABS(int a) {
        return (a < 0) ? -a : a;
    }

    /* ==================== FT_MulDivNoRound ===================================== */
    public static int FT_MulDivNoRound(int a, int b, int c) {
      int  s;
      int  d;

      s = 1;
      if (a < 0) {
        a = -a;
       	s = -1;
      }
      if (b < 0) {
        b = -b;
       	s = -s;
      }
      if (c < 0) {
        c = -c;
       	s = -s;
      }
      d = (c > 0 ? a * b / c : 0x7FFFFFFF);
      return (s > 0) ? d : -d;
    }

    /* ==================== FT_MulDiv ===================================== */
    public static int FT_MulDiv(int a, int b, int c) {
      int  s;
      int  d;


      s = 1;
      if (a < 0) {
        a = -a;
       	s = -1;
      }
      if (b < 0) {
        b = -b;
       	s = -s;
      }
      if (c < 0) {
        c = -c;
       	s = -s;
      }
      d = c > 0 ? (a * b + (c >> 1)) / c : 0x7FFFFFFF;
      return (s > 0) ? d : -d;
    }

    /* =====================================================================
     *    FTDivFix
     *
     * <Description>
     *    A very simple function used to perform the computation
     *    `(a*0x10000)/b' with maximum accuracy.  Most of the time, this is
     *    used to divide a given value by a 16.16 fixed-point factor.
     *
     * <Input>
     *    a :: The first multiplier.
     *    b :: The second multiplier.  Use a 16.16 factor here whenever
     *         possible (see note below).
     *
     * <Return>
     *    The result of `(a*0x10000)/b'.
     *
     * <Note>
     *    The optimization for FTDivFix() is simple: If (a~<<~16) fits in
     *    32~bits, then the division is computed directly.  Otherwise, we
     *    use a specialized version of @FT_MulDiv.
     * =====================================================================
     */

    public static int FTDivFix(int a, int b) {
      int s;
      int q;

      s = 1;
      if (a < 0) {
        a = -a;
        s = -1;
      }
      if (b < 0) {
        b = -b;
        s = -s;
      }
      if (b == 0) {
        /* check for division by 0 */
        q = 0x7FFFFFFF;
      } else {
        /* compute result directly */
        q = (int)((((a << 16) + (b >> 1)) / b));
      }
      return (s < 0 ? -q : q);
    }

    /* ==================== FTMulFix ===================================== */
    public static int FTMulFix(int a, int b) {
      int s = 1;
      int  c;

      if (a < 0) {
        a = -a;
        s = -1;
      }
      if (b < 0) {
        b = -b;
        s = -s;
      }
      c = (a * b + 0x8000) >> 16;
      return (s > 0) ? c : -c;
    }

    /* ==================== FT_MSB ===================================== */
    public static int FT_MSB(int z) {
      int shift = 0;

      /* determine msb bit index in `shift' */
      if (z >= (1 << 16)) {
        z >>= 16;
        shift += 16;
      }
      if (z >= (1 << 8)) {
        z >>= 8;
        shift += 8;
      }
      if (z >= (1 << 4)) {
        z >>= 4;
        shift += 4;
      }
      if (z >= (1 << 2)) {
        z >>= 2;
        shift += 2;
      }
      if (z >= (1 << 1)) {
        z >>= 1;
        shift += 1;
      }
      return shift;
    }

    /* ==================== FTHypot ===================================== */
    public static int FTHypot(int x, int y) {
      FTVectorRec v = new FTVectorRec();
      v.setX(x);
      v.setY(y);
      return FTTrigonometric.FTVectorLength(v);
    }

}
