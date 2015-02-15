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

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTCMap12Rec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCMap13Rec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCMap14Rec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCMap4Rec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCMapRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTUtil;
import org.apwtcl.apwfreetypelib.aftutil.FTUtilFlags;

  /* ===================================================================== */
  /*    FTCMapRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTCMapRec extends FTCharMapRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCMapRec";

  private FTCMapClassRec clazz = null;

  /* ==================== FTCMapRec ================================== */
  public FTCMapRec() {
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

  /* ==================== FTCMapNew ===================================== */
  public static FTError.ErrorTag FTCMapNew(FTCMapClassRec clazz, FTCharMapRec charmap, FTReference<FTCMapRec> cmap_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTFaceRec face;
    FTCMapRec cmap = null;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "FTCMapNew: format"+clazz.format);
    if (clazz == null || charmap == null || charmap.getFace() == null) {
      return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    face = charmap.getFace();
    switch (clazz.format) {
    case CMap0:
    case CMap2:
    case CMap6:
    case CMap8:
    case CMap10:
      cmap = new TTCMapRec();
      break;
    case CMap4:
      cmap = new TTCMap4Rec();
      break;
    case CMap12:
      cmap = new TTCMap12Rec();
      break;
    case CMap13:
      cmap = new TTCMap13Rec();
      break;
    case CMap14:
      cmap = new TTCMap14Rec();
      break;
    default:
      Log.e(TAG, String.format("funny TTCMapClassType: %d", clazz.format));
    }
    cmap.setCharmap(charmap);
    cmap.clazz = clazz;

    error = clazz.initCMap(cmap);
    if (error != FTError.ErrorTag.ERR_OK) {
//FIXME!!      ft_cmap_done_internal(cmap);
      cmap = null;
      if (cmap_ref != null) {
        cmap_ref.Set(cmap);
      }
      return error;
    }
    /* add it to our list of charmaps */
    face.charmaps = (FTCharMapRec[])FTUtil.FT_RENEW_ARRAY(face.charmaps, FTUtilFlags.ArrayType.CHARMAPS, face.getNum_charmaps(), face.getNum_charmaps() + 1);
    face.charmaps[face.getNum_charmaps()] = cmap;
    face.setNum_charmaps((face.getNum_charmaps() + 1));
    if (cmap_ref != null) {
      cmap_ref.Set(cmap);
    }
    return error;
  }

  /* ==================== setCharmap ================================== */
  public void setCharmap(FTCharMapRec charmap) {
    this.face = charmap.getFace();
    this.encoding = charmap.getEncoding();
    this.platform_id = charmap.getPlatformId();
    this.encoding_id = charmap.getEncodingId();
  }

  /* ==================== setClazz ================================== */
  public void setClazz(FTCMapClassRec clazz) {
    this.clazz = clazz;
  }

  /* ==================== getClazz ================================== */
  public FTCMapClassRec getClazz() {
    return clazz;
  }

}