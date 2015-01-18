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

package org.apwtcl.apwfreetypelib.aftcache;

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

  /* ===================================================================== */
  /*    FTCScalerRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to describe a given character size in either      */
  /*    pixels or points to the cache manager.  See                        */
  /*    @FTC_Manager_LookupSize.                                           */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    face_id :: The source face ID.                                     */
  /*                                                                       */
  /*    width   :: The character width.                                    */
  /*                                                                       */
  /*    height  :: The character height.                                   */
  /*                                                                       */
  /*    pixel   :: A Boolean.  If 1, the `width' and `height' fields are   */
  /*               interpreted as integer pixel character sizes.           */
  /*               Otherwise, they are expressed as 1/64th of points.      */
  /*                                                                       */
  /*    x_res   :: Only used when `pixel' is value~0 to indicate the       */
  /*               horizontal resolution in dpi.                           */
  /*                                                                       */
  /*    y_res   :: Only used when `pixel' is value~0 to indicate the       */
  /*               vertical resolution in dpi.                             */
  /*                                                                       */
  /* <Note>                                                                */
  /*    This type is mainly used to retrieve @FT_Size objects through the  */
  /*    cache manager.                                                     */
  /*                                                                       */
  /* ===================================================================== */

public class FTCScalerRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCScalerRec";

  private Object face_id = null;
  private int width = 0;
  private int height = 0;
  private int pixel = 0;
  private int x_res = 0;
  private int y_res = 0;

  /* ==================== FTCScalerRec ================================== */
  public FTCScalerRec() {
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
    str.append("..width:"+width+'\n');
    str.append("..height: "+height+'\n');
    str.append("..pixel: "+pixel+'\n');
    str.append("..x_res: "+x_res+'\n');
    str.append("..y_res: "+y_res+'\n');
    return str.toString();
  }
 
  /* =====================================================================
   * ftc_scaler_lookup_size
   * =====================================================================
   */
  public FTError.ErrorTag ftc_scaler_lookup_size(FTCManagerRec manager, FTCScalerRec scaler, FTReference<FTSizeRec> size_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTFaceRec face;
    FTSizeRec size = null;

Debug(0, DebugTag.DBG_CACHE, TAG, "ftc_scaler_lookup_size");
    FTReference<FTFaceRec> face_ref = new FTReference<FTFaceRec>();
    error = manager.LookupFace(scaler.face_id, face_ref);
    face = face_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      size_ref.Set(size);
      return error;
    }
    
    error = FTSizeRec.FTNewSize(face, size_ref);
    size = size_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      size_ref.Set(size);
      return error;
    }
    size.ActivateSize();
    if (scaler.pixel != 0) {
      Log.w(TAG, "FTSetPixelSizes not yet implemented!!");
//        error = FTSetPixelSizes(face, scaler.width, scaler.height);
    } else {
      error = size.SetCharSize(face, scaler.width, scaler.height, scaler.x_res, scaler.y_res);
    }
    if (error != FTError.ErrorTag.ERR_OK) {
//        FTSizeRec.FTDoneSize(size);
      size = null;
    }
    size_ref.Set(face.getSize());
    return error;
  }

  /* ==================== getFace_id ================================== */
  public Object getFace_id() {
    return face_id;
  }

  /* ==================== setFace_id ================================== */
  public void setFace_id(Object face_id) {
    this.face_id = face_id;
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

  /* ==================== getPixel ================================== */
  public int getPixel() {
    return pixel;
  }

  /* ==================== setPixel ================================== */
  public void setPixel(int pixel) {
    this.pixel = pixel;
  }

  /* ==================== getX_res ================================== */
  public int getX_res() {
    return x_res;
  }

  /* ==================== setX_res ================================== */
  public void setX_res(int x_res) {
    this.x_res = x_res;
  }

  /* ==================== getY_res ================================== */
  public int getY_res() {
    return y_res;
  }

  /* ==================== setY_res ================================== */
  public void setY_res(int y_res) {
    this.y_res = y_res;
  }

}