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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTBitmapSize                                                          */
  /* <Description>                                                         */
  /*    This structure models the metrics of a bitmap strike (i.e., a set  */
  /*    of glyphs for a given point size and resolution) in a bitmap font. */
  /*    It is used for the `available_sizes' field of @FT_Face.            */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    height :: The vertical distance, in pixels, between two            */
  /*              consecutive baselines.  It is always positive.           */
  /*                                                                       */
  /*    width  :: The average width, in pixels, of all glyphs in the       */
  /*              strike.                                                  */
  /*                                                                       */
  /*    size   :: The nominal size of the strike in 26.6 fractional        */
  /*              points.  This field is not very useful.                  */
  /*                                                                       */
  /*    x_ppem :: The horizontal ppem (nominal width) in 26.6 fractional   */
  /*              pixels.                                                  */
  /*                                                                       */
  /*    y_ppem :: The vertical ppem (nominal height) in 26.6 fractional    */
  /*              pixels.                                                  */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Windows FNT:                                                       */
  /*      The nominal size given in a FNT font is not reliable.  Thus when */
  /*      the driver finds it incorrect, it sets `size' to some calculated */
  /*      values and sets `x_ppem' and `y_ppem' to the pixel width and     */
  /*      height given in the font, respectively.                          */
  /*                                                                       */
  /*    TrueType embedded bitmaps:                                         */
  /*      `size', `width', and `height' values are not contained in the    */
  /*      bitmap strike itself.  They are computed from the global font    */
  /*      parameters.                                                      */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTBitmapSize extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTBitmapSize";

    public int height = 0;
    public int width = 0;
    public int size = 0;
    public int x_ppem = 0;
    public int y_ppem = 0;

    /* ==================== FTBitmapSize ================================== */
    public FTBitmapSize() {
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
 
}