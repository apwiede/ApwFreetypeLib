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
  /*    TTCMapRec                                                          */
  /*                                                                       */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftbase.FTCMapClassRec;
import org.apwtcl.apwfreetypelib.aftbase.FTCMapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTCharMapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class TTCMapRec extends FTCMapRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTCMapRec";

  protected int flags = 0;

  /* ==================== TTCMapRec ================================== */
  public TTCMapRec() {
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

  /* ==================== getFlags ===================================== */
  public int getFlags(int flags) {
    return flags;
  }

  /* ==================== setFlags ===================================== */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /* =====================================================================
   * tt_face_build_cmaps
   * =====================================================================
   */
  public static FTError.ErrorTag buildCMaps(TTFaceRec ttface) {
Debug(0, DebugTag.DBG_CMAP, TAG, "buildCMaps");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int num_tables = ttface.getCmap_table().getNumSubtables();
    for (int n = 0; n < num_tables; n++) {
      FTCharMapRec charmap = new FTCharMapRec();
      TTCMapClassRec my_clazz = null;

      TTCmapEncodingRec entry = ttface.getCmap_table().getSubtableEncoding(n);
      charmap.setPlatformId(entry.getPlatformId());
      charmap.setEncodingId(entry.getEncodingId());
      charmap.setEncoding(FTTags.Encoding.NONE);
      charmap.setFace(ttface);
      int format = ttface.getCmap_table().getFormatEntry(n);
      TTTags.CMapFormat cmap_format;
      switch (TTTags.CMapFormat.getTableTag(format)) {
        case CMap0:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap0);
          break;
        case CMap2:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap2);
          break;
        case CMap4:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap4);
          break;
        case CMap6:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap6);
          break;
        case CMap8:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap8);
          break;
        case CMap10:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap10);
          break;
        case CMap12:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap12);
          break;
        case CMap13:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap13);
          break;
        case CMap14:
          my_clazz = ttface.getCmap_table().getCMapClassEntry(TTTags.CMapFormat.CMap14);
          break;
      }
      if (my_clazz == null) {
        continue;
      }
      TTValidatorRec valid = new TTValidatorRec();
      error = FTError.ErrorTag.ERR_OK;

System.out.println("validate format: "+my_clazz.getFormat());
      valid.init(FTTags.Validate.DEFAULT);
      valid.setNumGlyphs(ttface.getMax_profile().getNumGlyphs());
      error = ((TTCMapClassRec) my_clazz).validate(valid);
      if (valid.getError() == FTError.ErrorTag.ERR_OK) {
        TTCMapRec ttcmap = null;

                /* It might make sense to store the single variation         */
                /* selector cmap somewhere special.  But it would have to be */
                /* in the public FTFaceRec, and we can't change that.       */
        FTReference<FTCMapRec> ttcmap_ref = new FTReference<FTCMapRec>();
        ttcmap_ref.Set(ttcmap);
        if (FTCMapRec.FTCMapNew((FTCMapClassRec) my_clazz, charmap, ttcmap_ref) != FTError.ErrorTag.ERR_OK) {
                  /* it is simpler to directly set `flags' than adding */
                  /* a parameter to FTCMapNew                        */
          ttcmap = (TTCMapRec) ttcmap_ref.Get();
          ttcmap.setFlags(error.getVal());
        }
      } else {
        FTTrace.Trace(7, TAG, "buildCMaps: broken cmap sub-table ignored");
      }
    }
    return error;
  }

}