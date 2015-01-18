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

public class RasterUtil extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "RasterUtil";

  public static int PIXEL_BITS = 8;

  /* ==================== RasterUtil ================================== */
  public RasterUtil() {
    oid++;
    id = oid;
  }

  /* =====================================================================
   * ONE_PIXEL
   * =====================================================================
   */
  public static int ONE_PIXEL() {
    return (1 << PIXEL_BITS);
  }

  /* =====================================================================
   * PIXEL_MASK
   * =====================================================================
   */
  public static int PIXEL_MASK() {
    return (-1 << PIXEL_BITS);
  }

  /* =====================================================================
   * TRUNC
   * =====================================================================
   */
  public static int TRUNC(int x) {
    return ((int)(x >> PIXEL_BITS));
  }

  /* =====================================================================
   * SUBPIXELS
   * =====================================================================
   */
  public static int SUBPIXELS(int x) {
    return ((int)x << PIXEL_BITS);
  }

  /* =====================================================================
   * FLOOR
   * =====================================================================
   */
  public static int FLOOR(int x) {
    return (x & -ONE_PIXEL());
  }

  /* =====================================================================
   * CEILING
   * =====================================================================
   */
  public static int CEILING(int x) {
    return ((x + ONE_PIXEL() - 1) & -ONE_PIXEL());
  }

  /* =====================================================================
   * ROUND
   * =====================================================================
   */
  public static int ROUND(int x) {
    return ((x + ONE_PIXEL() / 2) & -ONE_PIXEL());
  }

  /* =====================================================================
   * UPSCALE
   * =====================================================================
   */
  public static int UPSCALE(int x) {
    return (x << (PIXEL_BITS - 6));
  }

  /* =====================================================================
   * DOWNSCALE
   * =====================================================================
   */
  public static int DOWNSCALE(int x) {
    return (x >> (PIXEL_BITS - 6));
  }
}
