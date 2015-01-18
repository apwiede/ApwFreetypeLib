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
  /*    FTGlyphMetricsRec                                                  */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model the metrics of a single glyph.  The      */
  /*    values are expressed in 26.6 fractional pixel format; if the flag  */
  /*    @FT_LOAD_NO_SCALE has been used while loading the glyph, values    */
  /*    are expressed in font units instead.                               */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    width ::                                                           */
  /*      The glyph's width.                                               */
  /*                                                                       */
  /*    height ::                                                          */
  /*      The glyph's height.                                              */
  /*                                                                       */
  /*    horiBearingX ::                                                    */
  /*      Left side bearing for horizontal layout.                         */
  /*                                                                       */
  /*    horiBearingY ::                                                    */
  /*      Top side bearing for horizontal layout.                          */
  /*                                                                       */
  /*    horiAdvance ::                                                     */
  /*      Advance width for horizontal layout.                             */
  /*                                                                       */
  /*    vertBearingX ::                                                    */
  /*      Left side bearing for vertical layout.                           */
  /*                                                                       */
  /*    vertBearingY ::                                                    */
  /*      Top side bearing for vertical layout.  Larger positive values    */
  /*      mean further below the vertical glyph origin.                    */
  /*                                                                       */
  /*    vertAdvance ::                                                     */
  /*      Advance height for vertical layout.  Positive values mean the    */
  /*      glyph has a positive advance downward.                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    If not disabled with @FT_LOAD_NO_HINTING, the values represent     */
  /*    dimensions of the hinted glyph (in case hinting is applicable).    */
  /*                                                                       */
  /*    Stroking a glyph with an outside border does not increase          */
  /*    `horiAdvance' or `vertAdvance'; you have to manually adjust these  */
  /*    values to account for the added width and height.                  */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class FTGlyphMetricsRec extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTGlyphMetricsRec";

    public int width = 0;
    public int height = 0;
    public int horiBearingX = 0;
    public int horiBearingY = 0;
    public int horiAdvance = 0;
    public int vertBearingX = 0;
    public int vertBearingY = 0;
    public int vertAdvance = 0;

    /* ==================== FTGlyphMetricsRec ================================== */
    public FTGlyphMetricsRec() {
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
     * clear
     * =====================================================================
     */
    public void clear() {
      width = 0;
      height = 0;
      horiBearingX = 0;
      horiBearingY = 0;
      horiAdvance = 0;
      vertBearingX = 0;
      vertBearingY = 0;
      vertAdvance = 0;
    }

}