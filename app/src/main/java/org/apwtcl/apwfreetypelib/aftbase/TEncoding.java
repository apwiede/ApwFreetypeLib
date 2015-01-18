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
  /*    TEncoding                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.afttruetype.TTNameTableRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;

public class TEncoding extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TEncoding";

    public TTTags.Platform platform_id;
    public int encoding_id = -1;
    public FTTags.Encoding encoding;

    /* ==================== TEncoding ================================== */
    public TEncoding(TTTags.Platform platform_id, int encoding_id, FTTags.Encoding encoding) {
      oid++;
      id = oid;
      this.platform_id = platform_id;
      this.encoding_id = encoding_id;
      this.encoding = encoding;
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