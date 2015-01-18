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
  /*    FTStrokerRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTStrokerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTStrokerRec";

  public final static int FT_STROKER_LINECAP_BUTT = 0;
  public final static int FT_STROKER_LINECAP_ROUND = 1;
  public final static int FT_STROKER_LINECAP_SQUARE = 2;
  public final static int FT_STROKER_LINEJOIN_ROUND = 0;
  public final static int FT_STROKER_LINEJOIN_BEVEL = 1;
  public final static int FT_STROKER_LINEJOIN_MITER_VARIABLE = 2;
  public final static int FT_STROKER_LINEJOIN_MITER = 2;
  public final static int FT_STROKER_LINEJOIN_MITER_FIXED = 3;

  private int angle_in = 0;             /* direction into curr join */
  private int angle_out = 0;            /* direction out of join  */
  private FTVectorRec center = null;               /* current position */
  private int line_length = 0;          /* length of last lineto */
  private boolean first_point = false;          /* is this the start? */
  private boolean subpath_open = false;         /* is the subpath open? */
  private int subpath_angle = 0;        /* subpath start direction */
  private FTVectorRec subpath_start = null;        /* subpath start position */
  private int subpath_line_length = 0;  /* subpath start lineto len */
  private boolean handle_wide_strokes = false;  /* use wide strokes logic? */
  private int line_cap = 0;
  private int line_join = 0;
  private int line_join_saved = 0;
  private int miter_limit = 0;
  private int radius = 0;
  private FTStrokeBorderRec[] borders = null;
  private FTLibraryRec library = null;

  /* ==================== FTStrokerRec ================================== */
  public FTStrokerRec(FTLibraryRec library) {
    oid++;
    id = oid;

    if (library == null) {
      Log.e(TAG, "constructor library == null!");
      return;
    }
    this.library = library;
    borders = new FTStrokeBorderRec[2];
    borders[0] = new FTStrokeBorderRec();
    borders[1] = new FTStrokeBorderRec();
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
      str.append("..angle_in: "+angle_in+'\n');
      str.append("..angle_out: "+angle_out+'\n');
      str.append("..center: "+center.x+" "+center.y+'\n');
      str.append("..line_length: "+line_length+'\n');
      str.append("..first_point: "+first_point+'\n');
      str.append("..subpath_open: "+subpath_open+'\n');
      str.append("..subpath_angle: "+subpath_angle+'\n');
      str.append("..subpath_start: "+subpath_start.x+" "+subpath_start.y+'\n');
      str.append("..subpath_line_length: "+subpath_line_length+'\n');
      str.append("..handle_wide_strokes: "+handle_wide_strokes+'\n');
      str.append("..line_cap: "+line_cap+'\n');
      str.append("..line_join: "+line_join+'\n');
      str.append("..line_join_saved: "+line_join_saved+'\n');
      str.append("..miter_limit: "+miter_limit+'\n');
      str.append("..radius: "+radius+'\n');
      return str.toString();
    }

}