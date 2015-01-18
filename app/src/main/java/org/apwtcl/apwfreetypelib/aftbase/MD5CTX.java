/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf at wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    MD5CTX                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class MD5CTX extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "MD5CTX";

    public long lo = 0;
    public long hi = 0;
    public long a = 0;
    public long b = 0;
    public long c = 0;
    public long d = 0;
    public byte[] buffer = new byte[64];
    public long[] block = new long[16];

    /* ==================== MD5CTX ================================== */
    public MD5CTX() {
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
     * F
     * =====================================================================
     */
    private static long F(long x, long y, long z) {
//System.out.println(String.format("F fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (z ^ (x & (y ^ z)))));
      return (z ^ (x & (y ^ z)));
    }

    /* =====================================================================
     * G
     * =====================================================================
     */
    private static long G(long x, long y, long z) {
//System.out.println(String.format("G fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (y ^ (z & (x ^ y)))));
      return (y ^ (z & (x ^ y)));
    }

    /* =====================================================================
     * H
     * =====================================================================
     */
    private static long H(long x, long y, long z) {
//System.out.println(String.format("H fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (x ^ y ^ z)));
      return (x ^ y ^ z);
    }

    /* =====================================================================
     * I
     * =====================================================================
     */
    private static long I(long x, long y, long z) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("I fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (y ^ (x | ~z))));
      return (y ^ (x | ~z));
    }

    /* =====================================================================
     * SET
     * =====================================================================
     */
    private static long SET(MD5CTX ctx, byte[] ptr, int ptr_idx, int n) {
//System.out.println(String.format("SET: 0x%08x 0x%08x 0x%08x 0x%08x", ptr[(ptr_idx + n) * 4],
//              ((ptr[(ptr_idx + n) * 4 + 1] & 0xFF) << 8),
//              ((ptr[(ptr_idx + n) * 4 + 2] & 0xFF) << 16),
//              ((ptr[(ptr_idx + n) * 4 + 3] & 0xFF) << 24)));
      ctx.block[n] = (ptr[(ptr_idx + n) * 4] & 0xFF) |
	            ((ptr[(ptr_idx + n) * 4 + 1] & 0xFF) << 8) |
	            ((ptr[(ptr_idx + n) * 4 + 2] & 0xFF) << 16) |
	            ((ptr[(ptr_idx + n) * 4 + 3] & 0xFF) << 24);
      ctx.block[n] &= 0xFFFFFFFFL;
      return ctx.block[n];
    }

    /* =====================================================================
     * GET
     * =====================================================================
     */
    private static long GET(MD5CTX ctx, int n) {
      return ctx.block[n];
    }

    /* =====================================================================
     * STEP
     * =====================================================================
     */
    private static long STEP(String func, long a, long b, long c, long d, 
            long x, long t, long s) {
      
      a = a & 0xFFFFFFFFL;
      b = b & 0xFFFFFFFFL;
      c = c & 0xFFFFFFFFL;
      d = d & 0xFFFFFFFFL;
      x = x & 0xFFFFFFFFL;
      t = t & 0xFFFFFFFFL;
      s = s & 0xFFFFFFFFL;
//System.out.println(String.format("STEP F01: x: 0x%08x, t: 0x%08x, a: 0x%08x", x, t, a));        
      switch(func) {
      case "F":
        a += F(b, c, d) + x + t;
//System.out.println(String.format("STEP F1: x: 0x%08x, t: 0x%08x, s: 0x%08x, a: 0x%08x", x, t, s, a));        
//System.out.println(String.format("a1: 0x%08x, a2: 0x%08x", (a << s) & 0xFFFFFFFFL, ((((long)a & 0xFFFFFFFFL) >> (32 - s))) & 0xFFFFFFFFL));
        a = ((a << s) & 0xFFFFFFFFL) | ((((long)a & 0xFFFFFFFFL) >> (32 - s) & 0xFFFFFFFFL));
//System.out.println(String.format("STEP F2: a: 0x%08x", a));        
        a += b;
        a &= 0xFFFFFFFFL;
//System.out.println(String.format("STEP F3: a: 0x%08x", a));        
        break;
      case "G":
//System.out.println(String.format("STEP F02: a: 0x%08x 0x%08x", a, G(b, c, d) + x + t));        
        a += G(b, c, d) + x + t;
//System.out.println(String.format("STEP G1: x: 0x%08x, t: 0x%08x, s: 0x%08x, a: 0x%08x", x, t, s, a & 0xFFFFFFFFL));        
//System.out.println(String.format("a1: 0x%08x, a2: 0x%08x", (a << s) & 0xFFFFFFFFL, ((((long)a & 0xFFFFFFFFL) >> (32 - s))) & 0xFFFFFFFFL));
        a = ((a << s) & 0xFFFFFFFFL) | ((((long)a & 0xFFFFFFFFL) >> (32 - s) & 0xFFFFFFFFL));
//System.out.println(String.format("STEP G2: a: 0x%08x", a));        
        a += b;
        a &= 0xFFFFFFFFL;
//System.out.println(String.format("STEP G3: a: 0x%08x", a));        
        break;
      case "H":
        a += H(b, c, d) + x + t;
//System.out.println(String.format("STEP H1: x: 0x%08x, t: 0x%08x, s: 0x%08x, a: 0x%08x", x, t, s, a));        
//System.out.println(String.format("a1: 0x%08x, a2: 0x%08x", (a << s) & 0xFFFFFFFFL, ((((long)a & 0xFFFFFFFFL) >> (32 - s))) & 0xFFFFFFFFL));
        a = ((a << s) & 0xFFFFFFFFL) | ((((long)a & 0xFFFFFFFFL) >> (32 - s) & 0xFFFFFFFFL));
//System.out.println(String.format("STEP H2: a: 0x%08x", a));        
        a += b;
        a &= 0xFFFFFFFFL;
//System.out.println(String.format("STEP H3: a: 0x%08x", a));        
        break;
      case "I":
        a += I(b, c, d) + x + t;
//System.out.println(String.format("STEP I1: x: 0x%08x, t: 0x%08x, s: 0x%08x, a: 0x%08x", x, t, s, a));        
//System.out.println(String.format("a1: 0x%08x, a2: 0x%08x", (a << s) & 0xFFFFFFFFL, ((((long)a & 0xFFFFFFFFL) >> (32 - s))) & 0xFFFFFFFFL));
        a = ((a << s) & 0xFFFFFFFFL) | ((((long)a & 0xFFFFFFFFL) >> (32 - s) & 0xFFFFFFFFL));
//System.out.println(String.format("STEP I2: a: 0x%08x", a));        
        a += b;
        a &= 0xFFFFFFFFL;
//System.out.println(String.format("STEP I3: a: 0x%08x", a));        
        break;
      }
      return a;
    }

    /* =====================================================================
     * body
     *
     * This processes one or more 64-byte data blocks, but does NOT update
     * the bit counters.  There are no alignment requirements.
     * =====================================================================
     */
    public static int body(FTReference<MD5CTX> ctx_ref, FTReference<byte[]> data_ref, int ptr_idx, long size) {
      byte[] ptr;
      MD5CTX ctx = ctx_ref.Get();
      long a;
      long b;
      long c;
      long d;
      long saved_a;
      long saved_b;
      long saved_c;
      long saved_d;

Debug(0, DebugTag.DBG_RENDER, TAG, "BODY:");
      ptr = data_ref.Get();
      ctx.a = ctx.a & 0xFFFFFFFFL;
      ctx.b = ctx.b & 0xFFFFFFFFL;
      ctx.c = ctx.c & 0xFFFFFFFFL;
      ctx.d = ctx.d & 0xFFFFFFFFL;
      a = ctx.a;
      b = ctx.b;
      c = ctx.c;
      d = ctx.d;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ctx.a: 0x%08x", a));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ctx.b: 0x%08x", b));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ctx.c: 0x%08x", c));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ctx.d: 0x%08x", d));
      do {
        saved_a = a;
        saved_b = b;
        saved_c = c;
        saved_d = d;
/* Round 1 */
//System.out.println("ROUND1");        
        a = STEP("F", a, b, c, d, SET(ctx, ptr, ptr_idx, 0), 0xd76aa478, 7);
//System.out.println(String.format("F: a: 0x%08x", a));
        d = STEP("F", d, a, b, c, SET(ctx, ptr, ptr_idx, 1), 0xe8c7b756, 12);
//System.out.println(String.format("F: d: 0x%08x", d));
        c = STEP("F", c, d, a, b, SET(ctx, ptr, ptr_idx, 2), 0x242070db, 17);
//System.out.println(String.format("F: c: 0x%08x", c));
        b = STEP("F", b, c, d, a, SET(ctx, ptr, ptr_idx, 3), 0xc1bdceee, 22);
//System.out.println(String.format("F: b: 0x%08x", b));

        a = STEP("F", a, b, c, d, SET(ctx, ptr, ptr_idx, 4), 0xf57c0faf, 7);
        d = STEP("F", d, a, b, c, SET(ctx, ptr, ptr_idx, 5), 0x4787c62a, 12);
        c = STEP("F", c, d, a, b, SET(ctx, ptr, ptr_idx, 6), 0xa8304613, 17);
        b = STEP("F", b, c, d, a, SET(ctx, ptr, ptr_idx, 7), 0xfd469501, 22);
        
        a = STEP("F", a, b, c, d, SET(ctx, ptr, ptr_idx, 8), 0x698098d8, 7);
        d = STEP("F", d, a, b, c, SET(ctx, ptr, ptr_idx, 9), 0x8b44f7af, 12);
        c = STEP("F", c, d, a, b, SET(ctx, ptr, ptr_idx, 10), 0xffff5bb1, 17);
        b = STEP("F", b, c, d, a, SET(ctx, ptr, ptr_idx, 11), 0x895cd7be, 22);
        
        a = STEP("F", a, b, c, d, SET(ctx, ptr, ptr_idx, 12), 0x6b901122, 7);
        d = STEP("F", d, a, b, c, SET(ctx, ptr, ptr_idx, 13), 0xfd987193, 12);
        c = STEP("F", c, d, a, b, SET(ctx, ptr, ptr_idx, 14), 0xa679438e, 17);
        b = STEP("F", b, c, d, a, SET(ctx, ptr, ptr_idx, 15), 0x49b40821, 22);

/* Round 2 */
//System.out.println(String.format("ROUND2 a: 0x%08x, b: 0x%08x, c: 0x%08x, d: 0x%08x", a, b, c, d));        
      	a = STEP("G", a, b, c, d, GET(ctx, 1), 0xf61e2562, 5);
      	d = STEP("G", d, a, b, c, GET(ctx, 6), 0xc040b340, 9);
      	c = STEP("G", c, d, a, b, GET(ctx, 11), 0x265e5a51, 14);
      	b = STEP("G", b, c, d, a, GET(ctx, 0), 0xe9b6c7aa, 20);
      	
      	a = STEP("G", a, b, c, d, GET(ctx, 5), 0xd62f105d, 5);
      	d = STEP("G", d, a, b, c, GET(ctx, 10), 0x02441453, 9);
      	c = STEP("G", c, d, a, b, GET(ctx, 15), 0xd8a1e681, 14);
      	b = STEP("G", b, c, d, a, GET(ctx, 4), 0xe7d3fbc8, 20);
      	
      	a = STEP("G", a, b, c, d, GET(ctx, 9), 0x21e1cde6, 5);
      	d = STEP("G", d, a, b, c, GET(ctx, 14), 0xc33707d6, 9);
      	c = STEP("G", c, d, a, b, GET(ctx, 3), 0xf4d50d87, 14);
      	b = STEP("G", b, c, d, a, GET(ctx, 8), 0x455a14ed, 20);
      	
      	a = STEP("G", a, b, c, d, GET(ctx, 13), 0xa9e3e905, 5);
      	d = STEP("G", d, a, b, c, GET(ctx, 2), 0xfcefa3f8, 9);
      	c = STEP("G", c, d, a, b, GET(ctx, 7), 0x676f02d9, 14);
      	b = STEP("G", b, c, d, a, GET(ctx, 12), 0x8d2a4c8a, 20);

/* Round 3 */
//System.out.println("ROUND3");        
      	a = STEP("H", a, b, c, d, GET(ctx, 5), 0xfffa3942, 4);
      	d = STEP("H", d, a, b, c, GET(ctx, 8), 0x8771f681, 11);
      	c = STEP("H", c, d, a, b, GET(ctx, 11), 0x6d9d6122, 16);
      	b = STEP("H", b, c, d, a, GET(ctx, 14), 0xfde5380c, 23);
      	
      	a = STEP("H", a, b, c, d, GET(ctx, 1), 0xa4beea44, 4);
      	d = STEP("H", d, a, b, c, GET(ctx, 4), 0x4bdecfa9, 11);
      	c = STEP("H", c, d, a, b, GET(ctx, 7), 0xf6bb4b60, 16);
      	b = STEP("H", b, c, d, a, GET(ctx, 10), 0xbebfbc70, 23);
      	
      	a = STEP("H", a, b, c, d, GET(ctx, 13), 0x289b7ec6, 4);
      	d = STEP("H", d, a, b, c, GET(ctx, 0), 0xeaa127fa, 11);
      	c = STEP("H", c, d, a, b, GET(ctx, 3), 0xd4ef3085, 16);
      	b = STEP("H", b, c, d, a, GET(ctx, 6), 0x04881d05, 23);
      	
      	a = STEP("H", a, b, c, d, GET(ctx, 9), 0xd9d4d039, 4);
      	d = STEP("H", d, a, b, c, GET(ctx, 12), 0xe6db99e5, 11);
      	c = STEP("H", c, d, a, b, GET(ctx, 15), 0x1fa27cf8, 16);
      	b = STEP("H", b, c, d, a, GET(ctx, 2), 0xc4ac5665, 23);

/* Round 4 */
//System.out.println("ROUND4");        
      	a = STEP("I", a, b, c, d, GET(ctx, 0), 0xf4292244, 6);
      	d = STEP("I", d, a, b, c, GET(ctx, 7), 0x432aff97, 10);
      	c = STEP("I", c, d, a, b, GET(ctx, 14), 0xab9423a7, 15);
      	b = STEP("I", b, c, d, a, GET(ctx, 5), 0xfc93a039, 21);
      	
      	a = STEP("I", a, b, c, d, GET(ctx, 12), 0x655b59c3, 6);
      	d = STEP("I", d, a, b, c, GET(ctx, 3), 0x8f0ccc92, 10);
      	c = STEP("I", c, d, a, b, GET(ctx, 10), 0xffeff47d, 15);
      	b = STEP("I", b, c, d, a, GET(ctx, 1), 0x85845dd1, 21);
      	
      	a = STEP("I", a, b, c, d, GET(ctx, 8), 0x6fa87e4f, 6);
      	d = STEP("I", d, a, b, c, GET(ctx, 15), 0xfe2ce6e0, 10);
      	c = STEP("I", c, d, a, b, GET(ctx, 6), 0xa3014314, 15);
      	b = STEP("I", b, c, d, a, GET(ctx, 13), 0x4e0811a1, 21);
      	
      	a = STEP("I", a, b, c, d, GET(ctx, 4), 0xf7537e82, 6);
      	d = STEP("I", d, a, b, c, GET(ctx, 11), 0xbd3af235, 10);
      	c = STEP("I", c, d, a, b, GET(ctx, 2), 0x2ad7d2bb, 15);
      	b = STEP("I", b, c, d, a, GET(ctx, 9), 0xeb86d391, 21);

      	a += saved_a;
      	a &= 0xFFFFFFFFL;
      	b += saved_b;
        b &= 0xFFFFFFFFL;
      	c += saved_c;
        c &= 0xFFFFFFFFL;
      	d += saved_d;
        d &= 0xFFFFFFFFL;

      	ptr_idx += 64;
//System.out.println(String.format("SIZE: %d", size));              	
      } while ((size -= 64) != 0);

      ctx.a = a;
      ctx.b = b;
      ctx.c = c;
      ctx.d = d;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("end ctx.a: 0x%08x", a));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("end ctx.b: 0x%08x", b));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("end ctx.c: 0x%08x", c));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("end ctx.d: 0x%08x", d));
      ctx_ref.Set(ctx);
      return ptr_idx;
    }


    /* =====================================================================
     * MD5Init
     * =====================================================================
     */
    public static void MD5Init(FTReference<MD5CTX> ctx_ref) {
      MD5CTX ctx = ctx_ref.Get();
      ctx.a = 0x67452301;
      ctx.b = 0xefcdab89;
      ctx.c = 0x98badcfe;
      ctx.d = 0x10325476;
      ctx.lo = 0;
      ctx.hi = 0;
    }

    /* =====================================================================
     * MD5Update
     * =====================================================================
     */
    public static void MD5Update(FTReference<MD5CTX> ctx_ref, FTReference<Object> data_ref, long size) {
      long saved_lo;
      int used;
      int free;
      MD5CTX ctx = ctx_ref.Get();
      byte[] data = (byte[])data_ref.Get();
      int i;
      int ctx_buffer_idx = 0;
      int data_idx = 0;
      FTReference<byte[]> buffer_ref = new FTReference<byte[]>();
      long orig_size = size;

      saved_lo = ctx.lo;
      if ((ctx.lo = (saved_lo + size) & 0x1FFFFFFFL) < saved_lo) {
      	ctx.hi++;
      }
      ctx.hi += size >> 29;
      used = (int)(saved_lo & 0x3FL);
//System.out.println(String.format("USED: %d", used));      
      if (used != 0) {
      	free = 64 - used;
      	if (size < free) {
      	  i = 0;
          for (ctx_buffer_idx = used; ctx_buffer_idx < used + size; ctx_buffer_idx++) {
            ctx.buffer[ctx_buffer_idx] = data[i++];
          }
      	  return;
      	}
      	i = 0;
        for (ctx_buffer_idx = used; ctx_buffer_idx < used + free; ctx_buffer_idx++) {
      	  ctx.buffer[ctx_buffer_idx] = data[i++];
        }
        data_idx = free;
      	size -= free;
      	buffer_ref.Set(ctx.buffer);
      	ctx_ref.Set(ctx);
      	ctx_buffer_idx = body(ctx_ref, buffer_ref, 0, 64);
      	ctx = ctx_ref.Get();
      	ctx.buffer = buffer_ref.Get();
      }
      if (size >= 64) {
        buffer_ref.Set(data);
        ctx_ref.Set(ctx);
      	data_idx = body(ctx_ref, buffer_ref, data_idx, (size & ~0x3FL));
      	ctx = ctx_ref.Get();
      	ctx.buffer = buffer_ref.Get();
      	size &= 0x3f;
      }
      for (i = 0; i < size; i++) {
        Debug(0, DebugTag.DBG_RENDER, TAG, String.format("ctx.buffer[i]: %d 0x%02x", i, ctx.buffer[i]));
      }
      for (i = 0; i < size; i++) {
        ctx.buffer[i] = data[data_idx++];
      }
      ctx_ref.Set(ctx);
      data_ref.Set(data);
    }

    /* =====================================================================
     * MD5Final
     * =====================================================================
     */
    public static void MD5Final(FTReference<byte[]> result_ref, FTReference<MD5CTX> ctx_ref) {
      int used;
      int free;
      byte[] result = result_ref.Get();
      int i;
      MD5CTX ctx = ctx_ref.Get();
      FTReference<byte[]> buffer_ref = new FTReference<byte[]>();

      used = (int)(ctx.lo & 0x3FL);
      ctx.buffer[used++] = (byte)0x80;
      free = 64 - used;
      if (free < 8) {
        for (i = used; i < used + free; i++) {
      	  ctx.buffer[i] = 0;
        }
        buffer_ref.Set(ctx.buffer);
        ctx_ref.Set(ctx);
      	body(ctx_ref, buffer_ref, 0, 64);
      	ctx = ctx_ref.Get();
      	ctx.buffer = buffer_ref.Get();
      	used = 0;
      	free = 64;
      }
      for (i = used; i < used + free - 8; i++) {
        ctx.buffer[i] = 0;
      }
      ctx.lo <<= 3;
      ctx.buffer[56] = (byte)ctx.lo;
      ctx.buffer[57] = (byte)(ctx.lo >> 8);
      ctx.buffer[58] = (byte)(ctx.lo >> 16);
      ctx.buffer[59] = (byte)(ctx.lo >> 24);
      ctx.buffer[60] = (byte)ctx.hi;
      ctx.buffer[61] = (byte)(ctx.hi >> 8);
      ctx.buffer[62] = (byte)(ctx.hi >> 16);
      ctx.buffer[63] = (byte)(ctx.hi >> 24);
      buffer_ref.Set(ctx.buffer);
      ctx_ref.Set(ctx);
      body(ctx_ref, buffer_ref, 0, 64);
      ctx = ctx_ref.Get();
      ctx.buffer = buffer_ref.Get();
      result[0] = (byte)ctx.a;
      result[1] = (byte)(ctx.a >> 8);
      result[2] = (byte)(ctx.a >> 16);
      result[3] = (byte)(ctx.a >> 24);
      result[4] = (byte)ctx.b;
      result[5] = (byte)(ctx.b >> 8);
      result[6] = (byte)(ctx.b >> 16);
      result[7] = (byte)(ctx.b >> 24);
      result[8] = (byte)ctx.c;
      result[9] = (byte)(ctx.c >> 8);
      result[10] = (byte)(ctx.c >> 16);
      result[11] = (byte)(ctx.c >> 24);
      result[12] = (byte)ctx.d;
      result[13] = (byte)(ctx.d >> 8);
      result[14] = (byte)(ctx.d >> 16);
      result[15] = (byte)(ctx.d >> 24);
for(int j = 0; j < 16; j++) {
  Debug(0, DebugTag.DBG_RENDER, TAG, String.format("MD5 j: %d 0x%02x", j, result[j] & 0xFF));
}
      ctx = new MD5CTX();
      ctx_ref.Set(ctx);
      result_ref.Set(result);
    }

}