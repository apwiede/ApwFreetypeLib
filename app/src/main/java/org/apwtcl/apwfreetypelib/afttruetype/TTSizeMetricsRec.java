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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTSizeMetricsRec                                                   */
  /*                                                                       */
  /* A note regarding non-squared pixels:                                  */
  /*                                                                       */
  /* (This text will probably go into some docs at some time; for now, it  */
  /* is kept here to explain some definitions in the TT_Size_Metrics       */
  /* record).                                                              */
  /*                                                                       */
  /* The CVT is a one-dimensional array containing values that control     */
  /* certain important characteristics in a font, like the height of all   */
  /* capitals, all lowercase letter, default spacing or stem width/height. */
  /*                                                                       */
  /* These values are found in FUnits in the font file, and must be scaled */
  /* to pixel coordinates before being used by the CVT and glyph programs. */
  /* Unfortunately, when using distinct x and y resolutions (or distinct x */
  /* and y pointsizes), there are two possible scalings.                   */
  /*                                                                       */
  /* A first try was to implement a `lazy' scheme where all values were    */
  /* scaled when first used.  However, while some values are always used   */
  /* in the same direction, some others are used under many different      */
  /* circumstances and orientations.                                       */
  /*                                                                       */
  /* I have found a simpler way to do the same, and it even seems to work  */
  /* in most of the cases:                                                 */
  /*                                                                       */
  /* - All CVT values are scaled to the maximum ppem size.                 */
  /*                                                                       */
  /* - When performing a read or write in the CVT, a ratio factor is used  */
  /*   to perform adequate scaling.  Example:                              */
  /*                                                                       */
  /*     x_ppem = 14                                                       */
  /*     y_ppem = 10                                                       */
  /*                                                                       */
  /*   We choose ppem = x_ppem = 14 as the CVT scaling size.  All cvt      */
  /*   entries are scaled to it.                                           */
  /*                                                                       */
  /*     x_ratio = 1.0                                                     */
  /*     y_ratio = y_ppem/ppem (< 1.0)                                     */
  /*                                                                       */
  /*   We compute the current ratio like:                                  */
  /*                                                                       */
  /*   - If projVector is horizontal,                                      */
  /*       ratio = x_ratio = 1.0                                           */
  /*                                                                       */
  /*   - if projVector is vertical,                                        */
  /*       ratio = y_ratio                                                 */
  /*                                                                       */
  /*   - else,                                                             */
  /*       ratio = sqrt( (proj.x * x_ratio) ^ 2 + (proj.y * y_ratio) ^ 2 ) */
  /*                                                                       */
  /*   Reading a cvt value returns                                         */
  /*     ratio * cvt[index]                                                */
  /*                                                                       */
  /*   Writing a cvt value in pixels:                                      */
  /*     cvt[index] / ratio                                                */
  /*                                                                       */
  /*   The current ppem is simply                                          */
  /*     ratio * ppem                                                      */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TTSizeMetricsRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTSizeMetricsRec";

  /* for non-square pixels */
  private int x_ratio = 0;
  private int y_ratio = 0;
  private int ppem = 0;          /* maximum ppem size              */
  private int ratio = 0;          /* current ratio                  */
  private int scale = 0;
  private int[] compensations = null; /* device-specific compensations  */
  private boolean valid = false;
  private boolean rotated = false;     /* `is the glyph rotated?'-flag   */
  private boolean stretched = false;   /* `is the glyph stretched?'-flag */

  /* ==================== TTSizeMetricsRec ================================== */
  public TTSizeMetricsRec() {
    oid++;
    id = oid;

    compensations = new int[4];
    compensations[0] = 0;
    compensations[1] = 0;
    compensations[2] = 0;
    compensations[3] = 0;
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
    str.append("..x_ratio: "+x_ratio+'\n');
    str.append("..y_ratio: "+y_ratio+'\n');
    str.append("..ppem: "+ppem+'\n');
    str.append("..ratio: "+ratio+'\n');
    str.append("..scale: "+scale+'\n');
    str.append("..compensations: "+compensations[0]+" "+compensations[1]+" "+compensations[2]+" "+compensations[3]+" "+'\n');
    str.append("..valid: "+valid+'\n');
    str.append("..rotated: "+rotated+'\n');
    str.append("..stretched: "+stretched+'\n');
    return str.toString();
  }

  /* ==================== getX_ratio ================================== */
  public int getX_ratio() {
    return x_ratio;
  }

  /* ==================== setX_ratio ================================== */
  public void setX_ratio(int x_ratio) {
    this.x_ratio = x_ratio;
  }

  /* ==================== getY_ratio ================================== */
  public int getY_ratio() {
    return y_ratio;
  }

  /* ==================== setY_ratio ================================== */
  public void setY_ratio(int y_ratio) {
    this.y_ratio = y_ratio;
  }

  /* ==================== getPpem ================================== */
  public int getPpem() {
    return ppem;
  }

  /* ==================== setPpem ================================== */
  public void setPpem(int ppem) {
    this.ppem = ppem;
  }

  /* ==================== getRatio ================================== */
  public int getRatio() {
    return ratio;
  }

  /* ==================== setRatio ================================== */
  public void setRatio(int ratio) {
    this.ratio = ratio;
  }

  /* ==================== getScale ================================== */
  public int getScale() {
    return scale;
  }

  /* ==================== setScale ================================== */
  public void setScale(int scale) {
    this.scale = scale;
  }

  /* ==================== getCompensations ================================== */
  public int[] getCompensations() {
    return compensations;
  }

  /* ==================== setCompensations ================================== */
  public void setCompensations(int[] compensations) {
    this.compensations = compensations;
  }

  /* ==================== isValid ================================== */
  public boolean isValid() {
    return valid;
  }

  /* ==================== setValid ================================== */
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  /* ==================== isRotated ================================== */
  public boolean isRotated() {
    return rotated;
  }

  /* ==================== setRotated ================================== */
  public void setRotated(boolean rotated) {
    this.rotated = rotated;
  }

  /* ==================== isStretched ================================== */
  public boolean isStretched() {
    return stretched;
  }

  /* ==================== setStretched ================================== */
  public void setStretched(boolean stretched) {
    this.stretched = stretched;
  }

}