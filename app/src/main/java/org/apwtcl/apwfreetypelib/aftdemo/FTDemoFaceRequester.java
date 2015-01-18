package org.apwtcl.apwfreetypelib.aftdemo;

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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRequester;
import org.apwtcl.apwfreetypelib.aftbase.FTLibraryRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTDemoFaceRequester extends FTFaceRequester {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTFaceRequester";

  /* ==================== FTCSBitRec ================================== */
  public FTDemoFaceRequester() {
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

  /* ==================== requestFace ================================== */
  @Override
  public FTError.ErrorTag requestFace(Object face_id, FTLibraryRec library, Object request_data, FTReference<FTFaceRec> face_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTFaceRec face = null;
    TFont font = (TFont)face_id;

    face_ref.Set(null);
Debug(0, DebugTag.DBG_INIT, TAG, "my_face_requester");
    if (font.getFile_address() != null) {
      Log.w(TAG, "file_address not yet implemented");
//        error = FTNewMemoryFace(lib,
//               (byte[]*)font.file_address, font.file_size, font.face_index, aface);
    } else {
      error = FTFaceRec.FTNewFace(library, font.getFilepathname(), font.getFace_index(), face_ref);
      face = face_ref.Get();
Debug(0, DebugTag.DBG_INIT, TAG, "FACE: "+face+"!"+face.getDriver()+"!");
    }
    if (error == FTError.ErrorTag.ERR_OK) {
/*
         format = FT_Get_X11_Font_Format( *aface );

        if ( !strcmp( format, "Type 1" ) ) {
          char   orig[5];
          char*  suffix        = (char*)strrchr( font.filepathname, '.' );
          int    has_extension = suffix                                &&
                                 ( strcasecmp( suffix, ".pfa" ) == 0 ||
                                   strcasecmp( suffix, ".pfb" ) == 0 );

          if ( has_extension )
            memcpy( orig, suffix, 5 );
          else
            /* we have already allocate four more bytes */
/*
            suffix = (char*)font.filepathname + strlen( font.filepathname );

          memcpy( suffix, ".afm", 5 );
          if ( FT_Attach_File( *aface, font.filepathname ) )
          {
            memcpy( suffix, ".pfm", 5 );
            FT_Attach_File( *aface, font.filepathname );
          }

          if ( has_extension )
            memcpy( suffix, orig, 5 );
          else
            suffix = '\0';
        }

        if ( (*aface).charmaps )
          (*aface).charmap = (*aface).charmaps[font.cmap_index];
*/
    }
    face_ref.Set(face);
    return error;
  }

    /* =====================================================================
     * GetSize
     * =====================================================================
     */
/*
    public static int GetSize(FTDemoHandle handle, FTReference<FTSizeRec> size_erf) {
      Log.w(TAG, "WARNING: GetSize not yet implemented");
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "GetSize not yet implemented!!");
      return 1;
    }
*/
}