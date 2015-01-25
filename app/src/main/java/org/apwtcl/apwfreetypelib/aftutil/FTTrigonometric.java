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
  /*    FTTrigonometric                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTTrigonometric extends FTDebug {
  private static int oid = 0;
  private static int[] ft_trig_arctan_table = new int[]
      {
        1740967, 919879, 466945, 234379, 117304, 58666, 29335,
        14668, 7334, 3667, 1833, 917, 458, 229, 115,
        57, 29, 14, 7, 4, 2, 1
      };
  private final static int FT_ANGLE_PI = 180 << 16;
  private final static int FT_ANGLE_PI2 = FT_ANGLE_PI / 2;
  private final static int FT_TRIG_SAFE_MSB = 29;
  private final static int FT_TRIG_MAX_ITERS = 23;
  private final static int FT_TRIG_SCALE = 0xDBD95B16;

  private int id;
  private static String TAG = "FTTrigonometric";

  /* ==================== FTTrigonometric ================================== */
  public FTTrigonometric() {
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
 
  /* ==================== ft_trig_downscale ================================ */
  private static int ft_trig_downscale(int val) {
    int s;
    int v1;
    int v2;
    int k1;
    int k2;
    int hi;
    int lo1;
    int lo2;
    int lo3;

Debug(0, DebugTag.DBG_RENDER, TAG, "ft_trig_downscale");
    s   = val;
    val = FTCalc.FT_ABS(val);
    v1 = (val >> 16);
    v2 = (val & 0xFFFF);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ft_trig_downscale: s: 0x%x, val: 0x%x, v1: 0x%x, v2: 0x%x", s, val, v1, v2));
    k1 = FT_TRIG_SCALE >> 16;         /* constant */
    k2 = (FT_TRIG_SCALE & 0xFFFF);   /* constant */
    hi = k1 * v1;
    lo1 = k1 * v2 + k2 * v1;       /* can't overflow */
    lo2 = (k2 * v2) >> 16;
    lo3 = lo1 > lo2 ? lo1 : lo2;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ft_trig_downscale2: k1: 0x%x, k2: 0x%x, hi: 0x%x, lo1: 0x%x, lo2: 0x%x, lo3: 0x%x", k1, k2, hi, lo1, lo2, lo3));
    lo1 += lo2;
    hi  += lo1 >> 16;
    if (lo1 < lo3) {
      hi += 0x10000L;
    }
    val  = hi;
    return (s >= 0) ? val : -val;
  }

  /* ==================== ft_trig_prenorm ===================================== */
  private static int ft_trig_prenorm(FTReference<FTVectorRec> vec_ref) {
    int x;
    int y;
    FTVectorRec vec = vec_ref.Get();
    int  shift;

    x = vec.getX();
    y = vec.getY();
    shift = FTCalc.FT_MSB(FTCalc.FT_ABS(x) | FTCalc.FT_ABS(y));
    if (shift <= FT_TRIG_SAFE_MSB) {
      shift  = FT_TRIG_SAFE_MSB - shift;
      vec.setX(x << shift);
      vec.setY(y << shift);
    } else {
      shift -= FT_TRIG_SAFE_MSB;
      vec.setX(x >> shift);
      vec.setY(y >> shift);
      shift  = -shift;
    }
    vec_ref.Set( vec);
    return shift;
  }

  /* ==================== ft_trig_pseudo_polarize ========================= */
  private static void ft_trig_pseudo_polarize (FTReference<FTVectorRec> vec_ref) {
    FTVectorRec vec = vec_ref.Get();
    int theta;
    int i;
    int x;
    int y;
    int xtemp;
    int b;
    int arctanIdx;
    int arctan;

    x = vec.getX();
    y = vec.getY();
    /* Get the vector into [-PI/4,PI/4] sector */
    if (y > x) {
      if (y > -x) {
        theta = FT_ANGLE_PI2;
        xtemp =  y;
        y = -x;
        x =  xtemp;
      } else {
        theta =  y > 0 ? FT_ANGLE_PI : -FT_ANGLE_PI;
        x = -x;
        y = -y;
      }
    } else {
      if (y < -x) {
        theta = -FT_ANGLE_PI2;
        xtemp = -y;
        y =  x;
        x =  xtemp;
      } else {
        theta = 0;
      }
    }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("theta0: 0x%x, x: 5x%x, y: 0x%x", theta, x, y));
    arctanIdx = 0;
    arctan = ft_trig_arctan_table[arctanIdx];
    /* Pseudorotations, with right shifts */
    b = 1;
    for (i = 1; i < FT_TRIG_MAX_ITERS; b <<= 1, i++) {
      arctan = ft_trig_arctan_table[arctanIdx];
      if (y > 0) {
        xtemp = x + ((y + b) >> i);
        y = y - ((x + b) >> i);
        x = xtemp;
        theta += arctan;
      } else {
        xtemp = x - ((y + b) >> i);
        y = y + ((x + b) >> i);
        x = xtemp;
        theta -= arctan;
      }
      arctanIdx++;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("theta1: 0x%x, x: 5x%x, y: 0x%x", theta, x, y));
    }
    /* round theta */
    if (theta >= 0) {
      theta = FTCalc.FT_PAD_ROUND(theta, 32);
    } else {
      theta = -FTCalc.FT_PAD_ROUND(-theta, 32);
    }
    vec.setX(x);
    vec.setY(theta);
    vec_ref.Set(vec);
  }

  /* ==================== FTVectorLength ===================================== */
  public static int FTVectorLength(FTVectorRec vec) {
    int shift;
    FTVectorRec v;
    FTReference<FTVectorRec> v_ref = new FTReference<FTVectorRec>();

Debug(0, DebugTag.DBG_RENDER, TAG, "FTVectorLength");
    v = vec;
    /* handle trivial cases */
    if (v.getX() == 0) {
      return FTCalc.FT_ABS(v.getY());
    } else {
      if (v.getY() == 0) {
        return FTCalc.FT_ABS(v.getX());
      }
    }
    /* general case */
    v_ref.Set(v);
    shift = ft_trig_prenorm(v_ref);
    v = v_ref.Get();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("shift1: %d, x: 0x%x, y: 0x%x", shift, v.getX(), v.getY()));
    ft_trig_pseudo_polarize(v_ref);
    v = v_ref.Get();
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("shift2: %d, x: 0x%x, y: 0x%x", shift, v.getX(), v.getY()));
    v.setX(ft_trig_downscale(v.getX()));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("shift3: %d, x: 0x%x, y: 0x%x", shift, v.getX(), v.getY()));
    if (shift > 0) {
      return (v.getX() + (1 << (shift - 1))) >> shift;
    }
    return (v.getX() << -shift);
  }

}
