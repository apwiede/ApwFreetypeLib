/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann-pri (dot)de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTServiceTTCMapsRec                                                  */
  /*                                                                       */
  /*  This structure is used to store a cache for several frequently used  */
  /*  services.  It is the type of `face->services'.  You                  */
  /*  should only use FT_FACE_LOOKUP_SERVICE to access it.                 */
  /*                                                                       */
  /*  All fields should have the type FT_Pointer to relax compilation      */
  /*  dependencies.  We assume the developer isn't completely stupid.      */
  /*                                                                       */
  /*  Each field must be named `service_XXXX' where `XXX' corresponds to   */
  /*  the correct FT_SERVICE_ID_XXXX macro.  See the definition of         */
  /*  FT_FACE_LOOKUP_SERVICE below how this is implemented                 */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.afttruetype.TTCMapInfoRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTServiceTTCMapsRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTServiceTTCMapsRec";

    /* ==================== FTServiceTTCMapsRec ================================== */
  public FTServiceTTCMapsRec() {
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

  public FTError.ErrorTag getCmapInfo(FTCharMapRec charmap, FTReference<TTCMapInfoRec> cmap_info_ref) {
    Log.e(TAG, "getCmapInfo not yet implemented");
    return FTError.ErrorTag.ERR_OK;
  }

}