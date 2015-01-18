/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 * 
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann: arnulf (at) wiedemann (dot) pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftbase;

import android.util.Log;
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftraster.FTRaster1RendererClass;
import org.apwtcl.apwfreetypelib.aftraster.FTSmoothRendererClass;
import org.apwtcl.apwfreetypelib.aftsfnt.SfntModuleClass;
import org.apwtcl.apwfreetypelib.afttruetype.TTDriverClass;
import org.apwtcl.apwfreetypelib.afttruetype.TTDriverRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

import java.util.*;

  /* ===================================================================== */
  /*    FTModules                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class FTModules extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTModules";

  public final static int FT_MAX_MODULES = 32;

  private boolean[] used_modules = null;
  private FTModuleClassRec[] default_modules = null;
  private static int num_modules = 0;

  /* ==================== FTModules ================================== */
  public FTModules() {
    int i = 0;
    oid++;
    id = oid;
    used_modules = new boolean[FT_MAX_MODULES];
    for (i = 0; i < FT_MAX_MODULES; i++) {
      used_modules[i] = false;
    }
    used_modules[FTTags.ModuleTag.TT_DRIVER.getVal()] = true;
    used_modules[FTTags.ModuleTag.FT_RASTER1_RENDERER.getVal()] = true;
    used_modules[FTTags.ModuleTag.SFNT_MODULE.getVal()] = true;
    used_modules[FTTags.ModuleTag.FT_SMOOTH_RENDERER.getVal()] = true;
    default_modules = new FTModuleClassRec[FT_MAX_MODULES];
    for (i = 0; i < FT_MAX_MODULES; i++) {
      default_modules[i] = null;
    }
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
 
  /* =====================================================================
   *   FTAddDefaultModules
   * =====================================================================
   */
  public FTError.ErrorTag FTAddDefaultModules(FTLibraryRec library) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int i;

    TTDriverClass tt_driver_class = new TTDriverClass();
    default_modules[FTTags.ModuleTag.TT_DRIVER.getVal()] = tt_driver_class;
Debug(0, DebugTag.DBG_INIT, TAG, "FTAddDefaultModules: "+num_modules+" "+FTTags.ModuleTag.TT_DRIVER+": "+tt_driver_class);
    num_modules++;

    FTRaster1RendererClass ft_raster1_renderer_class = new FTRaster1RendererClass();
    default_modules[FTTags.ModuleTag.FT_RASTER1_RENDERER.getVal()] = ft_raster1_renderer_class;
Debug(0, DebugTag.DBG_INIT, TAG, "FTAddDefaultModules: "+num_modules+" "+FTTags.ModuleTag.FT_RASTER1_RENDERER+": "+ft_raster1_renderer_class);
    num_modules++;

    SfntModuleClass sfnt_module_class = new SfntModuleClass();
    default_modules[FTTags.ModuleTag.SFNT_MODULE.getVal()] = sfnt_module_class;
Debug(0, DebugTag.DBG_INIT, TAG, "FTAddDefaultModules: "+num_modules+" "+FTTags.ModuleTag.SFNT_MODULE+": "+sfnt_module_class);
    num_modules++;

    FTSmoothRendererClass ft_smooth_renderer_class = new FTSmoothRendererClass();
    default_modules[FTTags.ModuleTag.FT_SMOOTH_RENDERER.getVal()] = ft_smooth_renderer_class;
Debug(0, DebugTag.DBG_INIT, TAG, "FTAddDefaultModules: "+num_modules+" "+FTTags.ModuleTag.FT_SMOOTH_RENDERER+": "+ft_smooth_renderer_class);
    num_modules++;

    for (i = 0; i < FT_MAX_MODULES; i++) {
      if (used_modules[i]) {
FTDebug.Debug(0,  FTDebug.DebugTag.DBG_INIT, TAG, String.format("add module: %d %s %s", i,
        default_modules[i].module_name,
        default_modules[i].module_type.getDescription()));
        error = FTAddModule(library, default_modules[i]);
        if (error != FTError.ErrorTag.ERR_OK) {
          Log.e(TAG, "FTAddModule: cannot install: " + default_modules[i].module_name);
        }
      }
    }
    return error;
  }

  /* =====================================================================
   *   FTAddModule 
   * =====================================================================
   */
  private FTError.ErrorTag FTAddModule(FTLibraryRec library, FTModuleClassRec module_clazz) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int nn;
    FTModuleRec module = null;

    if (library == null) {
      return FTError.ErrorTag.INIT_INVALID_LIBRARY_HANDLE;
    }
    if (module_clazz == null) {
      return FTError.ErrorTag.INIT_INVALID_ARGUMENT;
    }

    for (nn = 0; nn < library.num_modules; nn++) {
      module = library.modules[nn];
FTDebug.Debug(0,  FTDebug.DebugTag.DBG_INIT,  TAG,  "nn: "+nn+"!"+module.module_clazz);
      if (module.module_clazz.module_name.equals(module_clazz.module_name)) {
        /* this installed module has the same name, compare their versions */
        if (module_clazz.module_version <= module.module_clazz.module_version) {
          return FTError.ErrorTag.INIT_LOWER_MODULE_VERSION;
        }
        /* remove the module from our list, then exit the loop to replace */
        /* it by our new version..                                        */
//        FTRemoveModule(library, module);
        break;
      }
    }
    error = FTError.ErrorTag.ERR_OK;
    if (library.num_modules >= FT_MAX_MODULES) {
      error = FTError.ErrorTag.INIT_TOO_MANY_DRIVERS;
      return error;
    }
    FTRendererRec renderer = null;
    TTDriverRec driver = null;
    /* allocate module object */
    switch (module_clazz.module_type) {
      case UNKNOWN:
        Log.e(TAG, "module type unknown in FTAddModule: "+module_clazz.module_type.getDescription());
        return FTError.ErrorTag.INIT_BAD_ARGUMENT;
      case FT_MODULE:
        module = new FTModuleRec();
        break;
      case FT_RENDERER:
        module = new FTRendererRec();
        renderer = (FTRendererRec)module;
        renderer.clazz = (FTRendererClassRec)module_clazz;
        break;
      case TT_DRIVER:
        module = new TTDriverRec();
        driver = (TTDriverRec)module;
        break;
      default:
        Log.e(TAG, String.format("unknown module type in FTAddModule: %d", module_clazz.module_type.getVal()));
        return FTError.ErrorTag.INIT_BAD_ARGUMENT;
    }
    // base initialization
    module.library = library;
    module.module_clazz = module_clazz;
    /* check whether the module is a renderer - this must be performed */
    /* before the normal module initialization                         */
    int flags = module.module_clazz.module_flags;
    if (Flags.Module.isRenderer(flags)) {
FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT,  TAG, "FTAddModule module is renderer: "+module.module_clazz.module_name+" "+Flags.Module.RENDERER);
        // add to the renderers list
FTDebug.Debug(0,  FTDebug.DebugTag.DBG_INIT,  TAG, "REND1: "+module.module_clazz.module_name);
      error = renderer.addRenderer();
      if (error != FTError.ErrorTag.ERR_OK) {
        if (Flags.Module.isFontDriver(flags)) {
          if (Flags.Module.isDriverNoOutlines(flags)) {
            driver.getGlyph_loader().FTGlyphLoaderDone();
          }
        }
        if (Flags.Module.isRenderer(flags)) {
          if (renderer.clazz != null && renderer.glyph_format == FTTags.GlyphFormat.OUTLINE &&
               renderer.raster != null) {
            renderer.clazz.rasterDone(renderer.raster);
          }
        }
//          FT_FREE(module);
        return error;
      }
    }
    // is the module a auto-hinter?
    if (Flags.Module.isHinter(flags)) {
      library.auto_hinter = module;
    }
    // if the module is a font driver
    if (Flags.Module.isFontDriver(flags)) {
FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FT_Add_Module module is font driver: "+module.module_clazz.module_name+" "+Flags.Module.FONT_DRIVER);
      // allocate glyph loader if needed
      driver.setDriver_clazz((FTDriverClassRec)module.module_clazz);
      int driver_flags = driver.module_clazz.module_flags;
      if (! Flags.Module.isDriverNoOutlines(driver_flags)) {
FTDebug.Debug(0,  FTDebug.DebugTag.DBG_INIT,  TAG, "FT_Add_Module module uses outlines: "+module.module_clazz.module_name+" "+Flags.Module.FONT_DRIVER);
        driver.setGlyph_loader(new FTGlyphLoaderRec());
      }
    }
FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "FT_Add_Module call module_init for module: "+module.module_clazz.module_name);
    error = module_clazz.moduleInit(module);
    if (error == FTError.ErrorTag.ERR_OK) {
      /* add module to the library's table */
      library.modules[library.num_modules++] = module;
      return error;
    }
    if (Flags.Module.isFontDriver(flags)) {
      driver.module_clazz = module.module_clazz;
      if (!Flags.Module.isDriverNoOutlines(flags)) {
        driver.getGlyph_loader().FTGlyphLoaderDone();
      }
    }
    if (Flags.Module.isRenderer(flags)) {
      renderer.module_clazz = module.module_clazz;
      if (renderer.clazz != null && renderer.glyph_format == FTTags.GlyphFormat.OUTLINE &&
           renderer.raster != null) {
        renderer.clazz.rasterDone(renderer.raster);
      }
    }
    return error;
  }

}
