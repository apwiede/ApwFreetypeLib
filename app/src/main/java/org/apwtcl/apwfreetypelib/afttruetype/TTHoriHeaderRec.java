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
  /*    TTHoriHeader                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType horizontal header, the `hhea' */
  /*    table, as well as the corresponding horizontal metrics table,      */
  /*    i.e., the `hmtx' table.                                            */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    Version                :: The table version.                       */
  /*                                                                       */
  /*    Ascender               :: The font's ascender, i.e., the distance  */
  /*                              from the baseline to the top-most of all */
  /*                              glyph points found in the font.          */
  /*                                                                       */
  /*                              This value is invalid in many fonts, as  */
  /*                              it is usually set by the font designer,  */
  /*                              and often reflects only a portion of the */
  /*                              glyphs found in the font (maybe ASCII).  */
  /*                                                                       */
  /*                              You should use the `sTypoAscender' field */
  /*                              of the OS/2 table instead if you want    */
  /*                              the correct one.                         */
  /*                                                                       */
  /*    Descender              :: The font's descender, i.e., the distance */
  /*                              from the baseline to the bottom-most of  */
  /*                              all glyph points found in the font.  It  */
  /*                              is negative.                             */
  /*                                                                       */
  /*                              This value is invalid in many fonts, as  */
  /*                              it is usually set by the font designer,  */
  /*                              and often reflects only a portion of the */
  /*                              glyphs found in the font (maybe ASCII).  */
  /*                                                                       */
  /*                              You should use the `sTypoDescender'      */
  /*                              field of the OS/2 table instead if you   */
  /*                              want the correct one.                    */
  /*                                                                       */
  /*    Line_Gap               :: The font's line gap, i.e., the distance  */
  /*                              to add to the ascender and descender to  */
  /*                              get the BTB, i.e., the                   */
  /*                              baseline-to-baseline distance for the    */
  /*                              font.                                    */
  /*                                                                       */
  /*    advance_Width_Max      :: This field is the maximum of all advance */
  /*                              widths found in the font.  It can be     */
  /*                              used to compute the maximum width of an  */
  /*                              arbitrary string of text.                */
  /*                                                                       */
  /*    min_Left_Side_Bearing  :: The minimum left side bearing of all     */
  /*                              glyphs within the font.                  */
  /*                                                                       */
  /*    min_Right_Side_Bearing :: The minimum right side bearing of all    */
  /*                              glyphs within the font.                  */
  /*                                                                       */
  /*    xMax_Extent            :: The maximum horizontal extent (i.e., the */
  /*                              `width' of a glyph's bounding box) for   */
  /*                              all glyphs in the font.                  */
  /*                                                                       */
  /*    caret_Slope_Rise       :: The rise coefficient of the cursor's     */
  /*                              slope of the cursor (slope=rise/run).    */
  /*                                                                       */
  /*    caret_Slope_Run        :: The run coefficient of the cursor's      */
  /*                              slope.                                   */
  /*                                                                       */
  /*    Reserved               :: 8~reserved bytes.                        */
  /*                                                                       */
  /*    metric_Data_Format     :: Always~0.                                */
  /*                                                                       */
  /*    number_Of_HMetrics     :: Number of HMetrics entries in the `hmtx' */
  /*                              table -- this value can be smaller than  */
 /*                              the total number of glyphs in the font.  */
  /*                                                                       */
  /*    long_metrics           :: A pointer into the `hmtx' table.         */
  /*                                                                       */
  /*    short_metrics          :: A pointer into the `hmtx' table.         */
  /*                                                                       */
  /* <Note>                                                                */
  /*    IMPORTANT: The TT_HoriHeader and TT_VertHeader structures should   */
  /*               be identical except for the names of their fields which */
  /*               are different.                                          */
  /*                                                                       */
  /*               This ensures that a single function in the `ttload'     */
  /*               module is able to read both the horizontal and vertical */
  /*               headers.                                                */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTHoriHeaderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTHoriHeader";

  private int version = 0;
  private int ascender = 0;
  private int descender = 0;
  private int line_gap = 0;
  private int advance_width_max = 0;      /* advance width maximum */
  private int min_left_side_bearing = 0;  /* minimum left-sb       */
  private int min_right_side_bearing = 0; /* minimum right-sb      */
  private int x_max_extent = 0;            /* xmax extents          */
  private int caret_slope_rise = 0;
  private int caret_slope_run = 0;
  private int caret_offset = 0;
  private int[] reserved = new int[4];
  private int metric_data_format = 0;
  private int number_of_hmetrics = 0;
  /* The following fields are not defined by the TrueType specification */
  /* but they are used to connect the metrics header to the relevant    */
  /* `HMTX' table.                                                      */
  private Object long_metrics;
  private Object short_metrics;

  /* ==================== TTHoriHeader ================================== */
  public TTHoriHeaderRec() {
    oid++;
    id = oid;
  }
    
    /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: hhea offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.hhea, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    version = stream.readInt();
    ascender = (short)stream.readShort();
    descender = (short)stream.readShort();
    line_gap = stream.readShort();
    advance_width_max = (short)stream.readShort();
    min_left_side_bearing = (short)stream.readShort();
    min_right_side_bearing = (short)stream.readShort();
    x_max_extent = stream.readShort();
    caret_slope_rise = stream.readShort();
    caret_slope_run = stream.readShort();
    caret_offset = stream.readShort();
    for (int i = 0; i < 4; i++) {
      reserved[i] = stream.readShort();
    }
    metric_data_format = stream.readShort();
    number_of_hmetrics = stream.readShort();
    long_metrics  = null;
    short_metrics = null;
    FTTrace.Trace(7, TAG, String.format("ascender:           %5d", ascender));
    FTTrace.Trace(7, TAG, String.format("descender:          %5d", descender));
    FTTrace.Trace(7, TAG, String.format("number_of_hmetrics: %5d", number_of_hmetrics));
    return FTError.ErrorTag.ERR_OK;
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

  /* ==================== getVersion ===================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getAscender ===================================== */
  public int getAscender() {
    return ascender;
  }

  /* ==================== getDescender ===================================== */
  public int getDescender() {
    return descender;
  }

  /* ==================== getLineGap ===================================== */
  public int getLineGap() {
    return line_gap;
  }

  /* ==================== getAdvanceWidthMax ===================================== */
  public int getAdvanceWidthMax() {      /* advance width maximum */
    return advance_width_max;      /* advance width maximum */
  }

  /* ==================== getMinLeftSideBearing ===================================== */
  public int getMinLeftSideBearing() {  /* minimum left-sb       */
    return min_left_side_bearing;  /* minimum left-sb       */
  }

  /* ==================== getMinRightSideBearing ===================================== */
  public int getMinRightSideBearing() {
    ; /* minimum right-sb      */
    return min_right_side_bearing; /* minimum right-sb      */
  }

  /* ==================== getXMaxExtent ===================================== */
  public int getXMaxExtent() {            /* xmax extents          */
    return x_max_extent;            /* xmax extents          */
  }

  /* ==================== getCaretSlopeRise ===================================== */
  public int getCaretSlopeRise() {
    return caret_slope_rise;
  }

  /* ==================== getCaretSlopeRun ===================================== */
  public int getCaretSlopeRun() {
    return caret_slope_run;
  }

  /* ==================== getCaretOffset ===================================== */
  public int getCaretOffset() {
    return caret_offset;
  }

  /* ==================== getReserved ===================================== */
  public int[] getReserved() {
    return reserved;
  }

  /* ==================== getMetricDataFormat ===================================== */
  public int getMetricDataFormat() {
    return metric_data_format;
  }

  /* ==================== getNumberOfHMetrics ===================================== */
  public int getNumberOfHMetrics() {
    return number_of_hmetrics;
  }

  /* ==================== getLongMetrics ===================================== */
  public Object getLongMetrics() {
    return long_metrics;
  }

  /* ==================== getShortMetrics ===================================== */
  public Object getShortMetrics() {
    return short_metrics;
  }

}