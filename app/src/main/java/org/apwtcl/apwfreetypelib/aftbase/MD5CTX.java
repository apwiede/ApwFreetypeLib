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

  private long lo = 0;
  private long hi = 0;
  private long a = 0;
  private long b = 0;
  private long c = 0;
  private long d = 0;
  private byte[] buffer = new byte[64];
  private long[] block = new long[16];

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
  private long F(long x, long y, long z) {
//System.out.println(String.format("F fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (z ^ (x & (y ^ z)))));
    return (z ^ (x & (y ^ z)));
  }

  /* =====================================================================
   * G
   * =====================================================================
   */
  private long G(long x, long y, long z) {
//System.out.println(String.format("G fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (y ^ (z & (x ^ y)))));
    return (y ^ (z & (x ^ y)));
  }

  /* =====================================================================
   * H
   * =====================================================================
   */
  private long H(long x, long y, long z) {
//System.out.println(String.format("H fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (x ^ y ^ z)));
    return (x ^ y ^ z);
  }

  /* =====================================================================
   * I
   * =====================================================================
   */
  private long I(long x, long y, long z) {
Debug(0, DebugTag.DBG_MD5, TAG, String.format("I fcn: x: 0x%08x, y: 0x%08x, z: 0x%08x, val: 0x%08x", x, y, z, (y ^ (x | ~z))));
    return (y ^ (x | ~z));
  }

  /* =====================================================================
   * SET
   * =====================================================================
   */
  private long SET(byte[] ptr, int ptr_idx, int n) {
//System.out.println(String.format("SET: 0x%08x 0x%08x 0x%08x 0x%08x", ptr[(ptr_idx + n) * 4],
//              ((ptr[(ptr_idx + n) * 4 + 1] & 0xFF) << 8),
//              ((ptr[(ptr_idx + n) * 4 + 2] & 0xFF) << 16),
//              ((ptr[(ptr_idx + n) * 4 + 3] & 0xFF) << 24)));
    this.block[n] = (ptr[(ptr_idx + n) * 4] & 0xFF) |
           ((ptr[(ptr_idx + n) * 4 + 1] & 0xFF) << 8) |
           ((ptr[(ptr_idx + n) * 4 + 2] & 0xFF) << 16) |
           ((ptr[(ptr_idx + n) * 4 + 3] & 0xFF) << 24);
    this.block[n] &= 0xFFFFFFFFL;
    return this.block[n];
  }

  /* =====================================================================
   * GET
   * =====================================================================
   */
  private long GET(int n) {
      return this.block[n];
    }

  /* =====================================================================
   * STEP
   * =====================================================================
   */
  private long STEP(String func, long a, long b, long c, long d,
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
  public int body(int ptr_idx, long size) {
    byte[] ptr;
    long a;
    long b;
    long c;
    long d;
    long saved_a;
    long saved_b;
    long saved_c;
    long saved_d;

Debug(0, DebugTag.DBG_MD5, TAG, "BODY:");
    ptr = this.buffer;
    this.a = this.a & 0xFFFFFFFFL;
    this.b = this.b & 0xFFFFFFFFL;
    this.c = this.c & 0xFFFFFFFFL;
    this.d = this.d & 0xFFFFFFFFL;
    a = this.a;
    b = this.b;
    c = this.c;
    d = this.d;
Debug(0, DebugTag.DBG_MD5, TAG, String.format("ctx.a: 0x%08x", a));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("ctx.b: 0x%08x", b));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("ctx.c: 0x%08x", c));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("ctx.d: 0x%08x", d));
    do {
      saved_a = a;
      saved_b = b;
      saved_c = c;
      saved_d = d;
/* Round 1 */
//System.out.println("ROUND1");        
      a = STEP("F", a, b, c, d, SET(ptr, ptr_idx, 0), 0xd76aa478, 7);
//System.out.println(String.format("F: a: 0x%08x", a));
      d = STEP("F", d, a, b, c, SET(ptr, ptr_idx, 1), 0xe8c7b756, 12);
//System.out.println(String.format("F: d: 0x%08x", d));
      c = STEP("F", c, d, a, b, SET(ptr, ptr_idx, 2), 0x242070db, 17);
//System.out.println(String.format("F: c: 0x%08x", c));
      b = STEP("F", b, c, d, a, SET(ptr, ptr_idx, 3), 0xc1bdceee, 22);
//System.out.println(String.format("F: b: 0x%08x", b));

      a = STEP("F", a, b, c, d, SET(ptr, ptr_idx, 4), 0xf57c0faf, 7);
      d = STEP("F", d, a, b, c, SET(ptr, ptr_idx, 5), 0x4787c62a, 12);
      c = STEP("F", c, d, a, b, SET(ptr, ptr_idx, 6), 0xa8304613, 17);
      b = STEP("F", b, c, d, a, SET(ptr, ptr_idx, 7), 0xfd469501, 22);
        
      a = STEP("F", a, b, c, d, SET(ptr, ptr_idx, 8), 0x698098d8, 7);
      d = STEP("F", d, a, b, c, SET(ptr, ptr_idx, 9), 0x8b44f7af, 12);
      c = STEP("F", c, d, a, b, SET(ptr, ptr_idx, 10), 0xffff5bb1, 17);
      b = STEP("F", b, c, d, a, SET(ptr, ptr_idx, 11), 0x895cd7be, 22);
        
      a = STEP("F", a, b, c, d, SET(ptr, ptr_idx, 12), 0x6b901122, 7);
      d = STEP("F", d, a, b, c, SET(ptr, ptr_idx, 13), 0xfd987193, 12);
      c = STEP("F", c, d, a, b, SET(ptr, ptr_idx, 14), 0xa679438e, 17);
      b = STEP("F", b, c, d, a, SET(ptr, ptr_idx, 15), 0x49b40821, 22);

/* Round 2 */
//System.out.println(String.format("ROUND2 a: 0x%08x, b: 0x%08x, c: 0x%08x, d: 0x%08x", a, b, c, d));        
    	a = STEP("G", a, b, c, d, GET(1), 0xf61e2562, 5);
    	d = STEP("G", d, a, b, c, GET(6), 0xc040b340, 9);
    	c = STEP("G", c, d, a, b, GET(11), 0x265e5a51, 14);
    	b = STEP("G", b, c, d, a, GET(0), 0xe9b6c7aa, 20);
      	
    	a = STEP("G", a, b, c, d, GET(5), 0xd62f105d, 5);
    	d = STEP("G", d, a, b, c, GET(10), 0x02441453, 9);
    	c = STEP("G", c, d, a, b, GET(15), 0xd8a1e681, 14);
    	b = STEP("G", b, c, d, a, GET(4), 0xe7d3fbc8, 20);
      	
    	a = STEP("G", a, b, c, d, GET(9), 0x21e1cde6, 5);
    	d = STEP("G", d, a, b, c, GET(14), 0xc33707d6, 9);
    	c = STEP("G", c, d, a, b, GET(3), 0xf4d50d87, 14);
    	b = STEP("G", b, c, d, a, GET(8), 0x455a14ed, 20);
      	
    	a = STEP("G", a, b, c, d, GET(13), 0xa9e3e905, 5);
    	d = STEP("G", d, a, b, c, GET(2), 0xfcefa3f8, 9);
    	c = STEP("G", c, d, a, b, GET(7), 0x676f02d9, 14);
    	b = STEP("G", b, c, d, a, GET(12), 0x8d2a4c8a, 20);

/* Round 3 */
//System.out.println("ROUND3");        
    	a = STEP("H", a, b, c, d, GET(5), 0xfffa3942, 4);
    	d = STEP("H", d, a, b, c, GET(8), 0x8771f681, 11);
    	c = STEP("H", c, d, a, b, GET(11), 0x6d9d6122, 16);
    	b = STEP("H", b, c, d, a, GET(14), 0xfde5380c, 23);
      	
    	a = STEP("H", a, b, c, d, GET(1), 0xa4beea44, 4);
    	d = STEP("H", d, a, b, c, GET(4), 0x4bdecfa9, 11);
    	c = STEP("H", c, d, a, b, GET(7), 0xf6bb4b60, 16);
    	b = STEP("H", b, c, d, a, GET(10), 0xbebfbc70, 23);
      	
    	a = STEP("H", a, b, c, d, GET(13), 0x289b7ec6, 4);
    	d = STEP("H", d, a, b, c, GET(0), 0xeaa127fa, 11);
    	c = STEP("H", c, d, a, b, GET(3), 0xd4ef3085, 16);
    	b = STEP("H", b, c, d, a, GET(6), 0x04881d05, 23);
      	
    	a = STEP("H", a, b, c, d, GET(9), 0xd9d4d039, 4);
    	d = STEP("H", d, a, b, c, GET(12), 0xe6db99e5, 11);
    	c = STEP("H", c, d, a, b, GET(15), 0x1fa27cf8, 16);
    	b = STEP("H", b, c, d, a, GET(2), 0xc4ac5665, 23);

/* Round 4 */
//System.out.println("ROUND4");        
    	a = STEP("I", a, b, c, d, GET(0), 0xf4292244, 6);
      d = STEP("I", d, a, b, c, GET(7), 0x432aff97, 10);
      c = STEP("I", c, d, a, b, GET(14), 0xab9423a7, 15);
      b = STEP("I", b, c, d, a, GET(5), 0xfc93a039, 21);
      	
      a = STEP("I", a, b, c, d, GET(12), 0x655b59c3, 6);
      d = STEP("I", d, a, b, c, GET(3), 0x8f0ccc92, 10);
      c = STEP("I", c, d, a, b, GET(10), 0xffeff47d, 15);
      b = STEP("I", b, c, d, a, GET(1), 0x85845dd1, 21);
      	
      a = STEP("I", a, b, c, d, GET(8), 0x6fa87e4f, 6);
      d = STEP("I", d, a, b, c, GET(15), 0xfe2ce6e0, 10);
      c = STEP("I", c, d, a, b, GET(6), 0xa3014314, 15);
      b = STEP("I", b, c, d, a, GET(13), 0x4e0811a1, 21);
      	
      a = STEP("I", a, b, c, d, GET(4), 0xf7537e82, 6);
      d = STEP("I", d, a, b, c, GET(11), 0xbd3af235, 10);
      c = STEP("I", c, d, a, b, GET(2), 0x2ad7d2bb, 15);
      b = STEP("I", b, c, d, a, GET(9), 0xeb86d391, 21);

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

    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
Debug(0, DebugTag.DBG_MD5, TAG, String.format("end ctx.a: 0x%08x", a));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("end ctx.b: 0x%08x", b));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("end ctx.c: 0x%08x", c));
Debug(0, DebugTag.DBG_MD5, TAG, String.format("end ctx.d: 0x%08x", d));
    return ptr_idx;
  }


  /* =====================================================================
   * MD5Init
   * =====================================================================
   */
  public void MD5Init() {
    this.a = 0x67452301;
    this.b = 0xefcdab89;
    this.c = 0x98badcfe;
    this.d = 0x10325476;
    this.lo = 0;
    this.hi = 0;
  }

  /* =====================================================================
   * MD5Update
   * =====================================================================
   */
  public void MD5Update(FTReference<Object> data_ref, long size) {
    long saved_lo;
    int used;
    int free;
    byte[] data = (byte[])data_ref.Get();
    int i;
    int ctx_buffer_idx = 0;
    int data_idx = 0;
    FTReference<byte[]> buffer_ref = new FTReference<byte[]>();
    long orig_size = size;

    saved_lo = this.lo;
    if ((this.lo = (saved_lo + size) & 0x1FFFFFFFL) < saved_lo) {
    	this.hi++;
    }
    this.hi += size >> 29;
    used = (int)(saved_lo & 0x3FL);
//System.out.println(String.format("USED: %d", used));      
    if (used != 0) {
    	free = 64 - used;
    	if (size < free) {
    	  i = 0;
        for (ctx_buffer_idx = used; ctx_buffer_idx < used + size; ctx_buffer_idx++) {
          this.buffer[ctx_buffer_idx] = data[i++];
        }
    	  return;
    	}
    	i = 0;
      for (ctx_buffer_idx = used; ctx_buffer_idx < used + free; ctx_buffer_idx++) {
    	  this.buffer[ctx_buffer_idx] = data[i++];
      }
      data_idx = free;
    	size -= free;
    	ctx_buffer_idx = body(0, 64);
    }
    if (size >= 64) {
    	data_idx = body(data_idx, (size & ~0x3FL));
    	size &= 0x3f;
    }
    for (i = 0; i < size; i++) {
      Debug(0, DebugTag.DBG_MD5, TAG, String.format("ctx.buffer[i]: %d 0x%02x", i, this.buffer[i]));
    }
    for (i = 0; i < size; i++) {
      this.buffer[i] = data[data_idx++];
    }
    data_ref.Set(data);
  }

  /* =====================================================================
   * MD5Final
   * =====================================================================
   */
  public void MD5Final(byte[] result) {
    int used;
    int free;
    int i;

    used = (int)(this.lo & 0x3F);
    this.buffer[used++] = (byte)0x80;
    free = 64 - used;
    if (free < 8) {
      for (i = used; i < used + free; i++) {
        this.buffer[i] = 0;
      }
    	body(0, 64);
    	used = 0;
    	free = 64;
    }
    for (i = used; i < used + free - 8; i++) {
      this.buffer[i] = 0;
    }
    this.lo <<= 3;
    this.buffer[56] = (byte)this.lo;
    this.buffer[57] = (byte)(this.lo >> 8);
    this.buffer[58] = (byte)(this.lo >> 16);
    this.buffer[59] = (byte)(this.lo >> 24);
    this.buffer[60] = (byte)this.hi;
    this.buffer[61] = (byte)(this.hi >> 8);
    this.buffer[62] = (byte)(this.hi >> 16);
    this.buffer[63] = (byte)(this.hi >> 24);
    body(0, 64);
    result[0] = (byte)this.a;
    result[1] = (byte)(this.a >> 8);
    result[2] = (byte)(this.a >> 16);
    result[3] = (byte)(this.a >> 24);
    result[4] = (byte)this.b;
    result[5] = (byte)(this.b >> 8);
    result[6] = (byte)(this.b >> 16);
    result[7] = (byte)(this.b >> 24);
    result[8] = (byte)this.c;
    result[9] = (byte)(this.c >> 8);
    result[10] = (byte)(this.c >> 16);
    result[11] = (byte)(this.c >> 24);
    result[12] = (byte)this.d;
    result[13] = (byte)(this.d >> 8);
    result[14] = (byte)(this.d >> 16);
    result[15] = (byte)(this.d >> 24);
for(int j = 0; j < 16; j++) {
  Debug(2, DebugTag.DBG_MD5, TAG, String.format("MD5 j: %d 0x%02x", j, result[j] & 0xFF));
}
  }

}