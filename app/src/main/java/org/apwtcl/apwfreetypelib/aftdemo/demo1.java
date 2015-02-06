package org.apwtcl.apwfreetypelib.aftdemo;

/**
 * Created by arnulf on 31.12.14.
 */

import android.os.Environment;
import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTApwFreeType;
import org.apwtcl.apwfreetypelib.aftbase.FTBitmapRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRequester;
import org.apwtcl.apwfreetypelib.aftbase.FTLibraryRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftbase.FTStrokerRec;
import org.apwtcl.apwfreetypelib.aftcache.FTCBasicICacheClass;
import org.apwtcl.apwfreetypelib.aftcache.FTCBasicSCacheClass;
import org.apwtcl.apwfreetypelib.aftcache.FTCCMapCacheClass;
import org.apwtcl.apwfreetypelib.aftcache.FTCGCacheClassRec;
import org.apwtcl.apwfreetypelib.aftcache.FTCManagerRec;
import org.apwtcl.apwfreetypelib.aftcache.FTCScalerRec;
import org.apwtcl.apwfreetypelib.aftutil.*;

public class demo1 extends FTDebug {

  public static String TAG = "cl1";

  public demo1() {
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    System.out.println("path:" + path + "!");
    String font_file_name = path + "/Vera.ttf";
    int face_index = 0;
    int max_faces = 0;
    int max_sizes = 0;

    try {
      FTOpenArgsRec open_args = new FTOpenArgsRec();
      open_args.setPathname(font_file_name);

      TFont font_args = new TFont();
      font_args.setFilepathname(font_file_name);
      font_args.setFace_index(face_index);

      FTApwFreeType freetype = new FTApwFreeType();
      FTLibraryRec library = new FTLibraryRec();
      FTError.ErrorTag error = freetype.InitFreeType(library);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not init freetype");
        return;
      }

      FTReference<FTFaceRec> face_ref = new FTReference<>();
      error = FTFaceRec.FTNewFace(library, font_file_name, 0, face_ref);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not open face");
        return;
      }
      FTFaceRec face = face_ref.Get();
      Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "FACE: " + face.toDebugString());
      int width = 12 * 64;
      int height = 12 * 64;
      int h_res = 72;
      int v_res = 72;
      error = face.getSize().SetCharSize(face, width, height, h_res, v_res);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not open face");
        return;
      }
      Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "SIZE: " + face.getSize().toDebugString());

      int load_flags = 0;
      int glyph_index = 72;
      error = face.getGlyph().LoadGlyph(glyph_index, load_flags);

      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not load glyph 72");
        return;
      }


      FTFaceRequester requester = new FTDemoFaceRequester();
      FTCManagerRec manager = new FTCManagerRec(library, max_faces, max_sizes, requester, font_args);

      FTCGCacheClassRec sbits_cache = new FTCBasicSCacheClass();    /* the glyph small bitmaps cache */
      error = manager.RegisterCache(sbits_cache);
Debug(0, DebugTag.DBG_INIT, TAG, "sbits_cache: " + sbits_cache.toDebugString());
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not initialize small bitmaps cache");
        return;
      }

      FTCGCacheClassRec image_cache = new FTCBasicICacheClass();    /* the glyph image cache         */
Debug(0, DebugTag.DBG_INIT, TAG, "image_cache: " + image_cache.toDebugString());
      error = manager.RegisterCache(image_cache);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not initialize glyph image cache");
        return;
      }

      FTCGCacheClassRec cmap_cache = new FTCCMapCacheClass();     /* the charmap cache             */
      Debug(0, DebugTag.DBG_INIT, TAG, "cmap_cache: " + cmap_cache.toDebugString());
      error = manager.RegisterCache(cmap_cache);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not initialize charmap cache");
        return;
      }

      FTBitmapRec bitmap = new FTBitmapRec();
      FTStrokerRec stroker = new FTStrokerRec(library);

      FTReference<FTSizeRec> size_ref = new FTReference<>();
      FTSizeRec size = null;
      FTCScalerRec scaler = new FTCScalerRec();
      scaler.setFace_id(font_args);
      error = manager.LookupSize(scaler, size_ref);
      if (error != FTError.ErrorTag.ERR_OK) {
        Log.e(TAG, "could not initialize size rec");
        return;
      }
      size = size_ref.Get();

//FIXME!!
      font_args.setCmap_index(0);
      int charcode = 'H';
      int gindex;
      gindex = ((FTCCMapCacheClass) cmap_cache).FTCCMapCacheLookup(font_args, font_args.getCmap_index(), charcode);
      System.out.println(String.format("gindex for charcode: %c 0x%02x %d", charcode, charcode, gindex));

      charcode = 'e';
      gindex = ((FTCCMapCacheClass) cmap_cache).FTCCMapCacheLookup(font_args, font_args.getCmap_index(), charcode);
      System.out.println(String.format("gindex for charcode: %c 0x%02x %d", charcode, charcode, gindex));

      charcode = 'l';
      gindex = ((FTCCMapCacheClass) cmap_cache).FTCCMapCacheLookup(font_args, font_args.getCmap_index(), charcode);
      System.out.println(String.format("gindex for charcode: %c 0x%02x %d", charcode, charcode, gindex));

      charcode = 'o';
      gindex = ((FTCCMapCacheClass) cmap_cache).FTCCMapCacheLookup(font_args, font_args.getCmap_index(), charcode);
      System.out.println(String.format("gindex for charcode: %c 0x%02x %d", charcode, charcode, gindex));


      System.out.println("cache init done");

      int x = 0;
      FTReference<FTBitmapRec> bitmap_ref = new FTReference<FTBitmapRec>();
/*
    FTFaceRec face;
    FTReference<FTFaceRec> face_ref = new FTReference<>();
    error = FTFaceRec.FTNewFace(library, path+"/Vera.ttf", face_index, face_ref);
    System.out.println("FTNewFace done");
    System.out.println("result: "+error);
    if(error == FTError.ErrorTag.ERR_OK) {
      face = face_ref.Get();
      int cmap_index = face.charmap != null ? FTCharMapRec.FTGetCharmapIndex(face.charmap) : 0;
      int num_indices = face.num_glyphs;
    }
*/




/*
    FTStreamRec stream = new FTStreamRec();
    result = stream.Open(path+"/Vera.ttf");
    System.out.println("result: "+result);


    TTFaceRec face = new TTFaceRec();
    result = TTSfntLoad.sfnt_open_font(stream, face);

    TTCHeaderRec ttc_head = new TTCHeaderRec();
    result = ttc_head.Load(stream);
    System.out.println("result2: "+result);
    if (result != FTError.ErrorTag.ERR_OK) {
      return;
    }
    System.out.println("ttc_head: "+ttc_head.toDebugString());

    int face_index = 0;
    ttc_head.GotoFaceIndex(stream, face_index);
    TTTableOffsetRec table_offsets = new TTTableOffsetRec();
    result = table_offsets.Load(stream);
    System.out.println("result3: "+result);
    if (result != FTError.ErrorTag.ERR_OK) {
      return;
    }
    System.out.println("table_offsets: "+table_offsets.toDebugString());
    FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, "cl1", "table_offsets: "+table_offsets.toDebugString());

    TTTableDirectoryRec table_dir;
    int num_tabs = table_offsets.GetNumTables();
    for (int i = 0; i < num_tabs; i++) {
      table_dir = new TTTableDirectoryRec();
      result = table_dir.Load(stream);
      System.out.println(String.format("%d result: ", i)  + result);
      if (result != FTError.ErrorTag.ERR_OK) {
        return;
      }
      System.out.println("table_dirs: " + table_dir.toDebugString());
    }

    System.out.println("TableId Description: "+ TTTags.Table.getTableTag(0x45424454).getDescription());
*/

    } catch(Exception e) {
      e.printStackTrace();
      return;
    }
  }

}
