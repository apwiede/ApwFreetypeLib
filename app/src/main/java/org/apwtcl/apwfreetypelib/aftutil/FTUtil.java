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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTCharMapRec;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

  /* ===================================================================== */
  /*    FTUtil                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTUtil extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTUtil";

    /* ==================== FTUtil ================================== */
    public FTUtil() {
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
 
    /* =====================================================================
     * FT_RENEW_ARRAY
     * =====================================================================
     */
    public static Object FT_RENEW_ARRAY(Object obj, FTUtilFlags.ArrayType type, int curCount, int newCount) {
      int i;
      int j;
      int k;

      if (curCount < 0 || newCount < 0) {
        Log.e(TAG, String.format("curCount < 0 || newCount < 0, curCount: %d, newCount: %d", curCount, newCount));
        return null;
      }
      if (curCount == 0) {
        if (obj != null) {
          Log.e(TAG, String.format("curCount == 0 obj != null"));
          return null;
        }
      }
      switch (type) {
        case FT_VECTOR:
          FTVectorRec[] vec_array = (FTVectorRec[]) obj;
          if (vec_array == null) {
            // easy just allocate
            vec_array = new FTVectorRec[newCount];
            for (i = 0; i < newCount; i++) {
              vec_array[i] = new FTVectorRec();
            }
            obj = vec_array;
          } else {
            if (vec_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                vec_array = new FTVectorRec[newCount];
                for (j = 0; j < newCount; j++) {
                  vec_array[j] = new FTVectorRec();
                }
                obj = vec_array;
              } else {
                FTVectorRec[] tmp1;
                tmp1 = java.util.Arrays.copyOf(vec_array, curCount);
                vec_array = new FTVectorRec[newCount];
                for (k = 0; k < curCount; k++) {
                  vec_array[k] = tmp1[k];
                }
                for (k = curCount; k < newCount; k++) {
                  vec_array[k] = new FTVectorRec();
                }
                obj = vec_array;
              }
            }
          }
          break;
        case BYTE:
          byte[] byte_array = (byte[]) obj;
          if (byte_array == null) {
            // easy just allocate
            byte_array = new byte[newCount];
            for (i = 0; i < newCount; i++) {
              byte_array[i] = 0;
            }
            obj = byte_array;
          } else {
            if (byte_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                byte_array = new byte[newCount];
                for (j = 0; j < newCount; j++) {
                  byte_array[j] = 0;
                }
                obj = byte_array;
              } else {
                byte[] tmp2;
                tmp2 = java.util.Arrays.copyOf(byte_array, curCount);
                byte_array = new byte[newCount];
                for (k = 0; k < curCount; k++) {
                  byte_array[k] = tmp2[k];
                }
                for (k = curCount; k < newCount; k++) {
                  byte_array[k] = 0;
                }
                obj = byte_array;
              }
            }
          }
          break;
        case CURVE:
          Flags.Curve[] curve_array = (Flags.Curve[]) obj;
          if (curve_array == null) {
            // easy just allocate
            curve_array = new Flags.Curve[newCount];
            for (i = 0; i < newCount; i++) {
              curve_array[i] = Flags.Curve.CONIC;
            }
            obj = curve_array;
          } else {
            if (curve_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                curve_array = new Flags.Curve[newCount];
                for (j = 0; j < newCount; j++) {
                  curve_array[j] = Flags.Curve.CONIC;
                }
                obj = curve_array;
              } else {
                Flags.Curve[] tmp2;
                tmp2 = java.util.Arrays.copyOf(curve_array, curCount);
                curve_array = new Flags.Curve[newCount];
                for (k = 0; k < curCount; k++) {
                  curve_array[k] = tmp2[k];
                }
                for (k = curCount; k < newCount; k++) {
                  curve_array[k] = Flags.Curve.CONIC;
                }
                obj = curve_array;
              }
            }
          }
          break;
        case SHORT:
          short[] short_array = (short[]) obj;
          if (short_array == null) {
            // easy just allocate
            short_array = new short[newCount];
            for (i = 0; i < newCount; i++) {
              short_array[i] = 0;
            }
            obj = short_array;
          } else {
            if (short_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                short_array = new short[newCount];
                for (j = 0; j < newCount; j++) {
                  short_array[j] = 0;
                }
                obj = short_array;
              } else {
                short[] tmp3;
                tmp3 = java.util.Arrays.copyOf(short_array, curCount);
                short_array = new short[newCount];
                for (k = 0; k < curCount; k++) {
                  short_array[k] = tmp3[k];
                }
                for (k = curCount; k < newCount; k++) {
                  short_array[k] = 0;
                }
                obj = short_array;
              }
            }
          }
          break;
        case INT:
          int[] int_array = (int[]) obj;
          if (int_array == null) {
            // easy just allocate
            int_array = new int[newCount];
            for (i = 0; i < newCount; i++) {
              int_array[i] = 0;
            }
            obj = int_array;
          } else {
            if (int_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                int_array = new int[newCount];
                for (j = 0; j < newCount; j++) {
                  int_array[j] = 0;
                }
                obj = int_array;
              } else {
                int[] tmp3;
                tmp3 = java.util.Arrays.copyOf(int_array, curCount);
                int_array = new int[newCount];
                for (k = 0; k < curCount; k++) {
                  int_array[k] = tmp3[k];
                }
                for (k = curCount; k < newCount; k++) {
                  int_array[k] = 0;
                }
                obj = int_array;
              }
            }
          }
          break;
        case LONG:
          obj = null;
          Log.e(TAG, "bad type: "+type+" in FT_RENEW_ARRAY (not yet implemented)");
          break;
        case CHARMAPS:
          FTCharMapRec[] charmaps_array = (FTCharMapRec[]) obj;
          if (charmaps_array == null) {
            // easy just allocate
            charmaps_array = new FTCharMapRec[newCount];
            for (i = 0; i < newCount; i++) {
              charmaps_array[i] = new FTCharMapRec();
            }
            obj = charmaps_array;
          } else {
            if (charmaps_array.length >= newCount) {
              // nothing to do
            } else {
              if (curCount == 0) {
                charmaps_array = new FTCharMapRec[newCount];
                for (j = 0; j < newCount; j++) {
                  charmaps_array[j] = new FTCharMapRec();
                }
                obj = charmaps_array;
              } else {
                FTCharMapRec[] tmp3;
                tmp3 = java.util.Arrays.copyOf(charmaps_array, curCount);
                charmaps_array = new FTCharMapRec[newCount];
                for (k = 0; k < curCount; k++) {
                  charmaps_array[k] = tmp3[k];
                }
                for (k = curCount; k < newCount; k++) {
                  charmaps_array[k] = new FTCharMapRec();
                }
                obj = charmaps_array;
              }
            }
          }
          break;
        default:
          Log.e(TAG, "bad type: "+type+" in FT_RENEW_ARRAY");
          return null;
      }
      return obj;
    }

    /* =====================================================================
     * FT_BYTE
     * =====================================================================
     */
    public static int FT_BYTE(byte[] p, int idx) {
      return p[idx] & 0xFF;
    }

    /* =====================================================================
     * FT_BYTE_U16
     * =====================================================================
     */
    public static int FT_BYTE_U16(byte[] p, int idx, int shift) {
      return (FT_BYTE(p, idx) << shift) & 0xFFFF;
    }

    /* =====================================================================
     * FT_BYTE_U32
     * =====================================================================
     */
    public static Long FT_BYTE_U32(byte[] p, int idx, int shift) {
      return (long)(FT_BYTE(p, idx) << shift) & 0xFFFFFFFF;
    }


    /* =====================================================================
     * FT_PEEK_SHORT
     * =====================================================================
     */
    public static int FT_PEEK_SHORT(byte[] p, int idx) {
      return FT_BYTE_U16(p, idx+0, 8) | FT_BYTE_U16(p, idx+1, 0);
    }

    /* =====================================================================
     * FT_PEEK_USHORT
     * =====================================================================
     */
    public static int FT_PEEK_USHORT(byte[] p, int idx) {
      return (FT_BYTE_U16(p, idx+0, 8) | FT_BYTE_U16(p, idx+1, 0)) & 0xFFFF;
    }

    /* =====================================================================
     * FT_NEXT_ULONG
     * =====================================================================
     */
    public static long FT_NEXT_ULONG(byte[] array, FTReference<Integer> offset_ref, int limit) {
      int offset = offset_ref.Get();
      if (offset + 4 > limit) {
        Log.e(TAG, "FT_NEXT_ULONG offset > limit!!");
        return 0L;
      } else {
        long val = ((array[offset] & 0xFF) << 24) | ((array[offset + 1] & 0xFF) << 16) | ((array[offset + 2] & 0xFF) << 8) | (array[offset + 3] & 0xFF);
        offset += 4;
        offset_ref.Set(offset);
        return val;
      }
    }

    /* =====================================================================
     * FT_NEXT_USHORT
     * =====================================================================
     */
    public static int FT_NEXT_USHORT(byte[] array, FTReference<Integer> offset_ref, int limit) {
      int offset = offset_ref.Get();
      if (offset + 2 > limit) {
        Log.e(TAG, "FT_NEXT_USHORT offset > limit!!");
        return 0;
      } else {
        int val = ((((array[offset] & 0xFF) << 8) + ((array[offset + 1] & 0xFF))) & 0xFFFF);
        offset += 2;
        offset_ref.Set(offset);
        return val;
      }
    }

    /* =====================================================================
     * FT_NEXT_SHORT
     * =====================================================================
     */
    public static short FT_NEXT_SHORT(byte[] array, FTReference<Integer> offset_ref, int limit) {
      int offset = offset_ref.Get();
      if (offset + 2 > limit) {
        Log.e(TAG, "FT_NEXT_USHORT offset > limit!!");
        return 0;
      } else {
        short val = (short)((array[offset] << 8) + ((array[offset + 1] & 0xFF)));
        offset += 2;
        offset_ref.Set(offset);
        return val;
      }
    }

}