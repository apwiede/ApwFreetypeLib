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

  private int width = 0;
  private int height = 0;
  private int horiBearingX = 0;
  private int horiBearingY = 0;
  private int horiAdvance = 0;
  private int vertBearingX = 0;
  private int vertBearingY = 0;
  private int vertAdvance = 0;

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
    str.append("...width: " + width + '\n');
    str.append("...height: "+height+'\n');
    str.append("...horiBearingX: "+horiBearingX+'\n');
    str.append("...horiBearingY: "+horiBearingY+'\n');
    str.append("...horiAdvance: "+horiAdvance+'\n');
    str.append("...vertBearingX: "+vertBearingX+'\n');
    str.append("...vertBearingY: "+vertBearingY+'\n');
    str.append("...vertAdvance: "+vertAdvance+'\n');
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

  /* ==================== getWidth ================================== */
  public int getWidth() {
    return width;
  }

  /* ==================== setWidth ================================== */
  public void setWidth(int width) {
    this.width = width;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getHoriBearingX ================================== */
  public int getHoriBearingX() {
    return horiBearingX;
  }

  /* ==================== setHoriBearingX ================================== */
  public void setHoriBearingX(int horiBearingX) {
    this.horiBearingX = horiBearingX;
  }

  /* ==================== getHoriBearingY ================================== */
  public int getHoriBearingY() {
    return horiBearingY;
  }

  /* ==================== setHoriBearingY ================================== */
  public void setHoriBearingY(int horiBearingY) {
    this.horiBearingY = horiBearingY;
  }

  /* ==================== getHoriAdvance ================================== */
  public int getHoriAdvance() {
    return horiAdvance;
  }

  /* ==================== setHoriAdvance ================================== */
  public void setHoriAdvance(int horiAdvance) {
    this.horiAdvance = horiAdvance;
  }

  /* ==================== getVertBearingX ================================== */
  public int getVertBearingX() {
    return vertBearingX;
  }

  /* ==================== setVertBearingX ================================== */
  public void setVertBearingX(int vertBearingX) {
    this.vertBearingX = vertBearingX;
  }

  /* ==================== getVertBearingY ================================== */
  public int getVertBearingY() {
    return vertBearingY;
  }

  /* ==================== setVertBearingY ================================== */
  public void setVertBearingY(int vertBearingY) {
    this.vertBearingY = vertBearingY;
  }

  /* ==================== getVertAdvance ================================== */
  public int getVertAdvance() {
    return vertAdvance;
  }

  /* ==================== setVertAdvance ================================== */
  public void setVertAdvance(int vertAdvance) {
    this.vertAdvance = vertAdvance;
  }

}