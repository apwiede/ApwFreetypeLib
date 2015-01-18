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
  /*    FTRendererRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftraster.FTRasterParams;
import org.apwtcl.apwfreetypelib.aftraster.FTRasterRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTParameter;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTRendererRec extends FTModuleRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTRendererRec";


  public FTTags.GlyphFormat glyph_format = FTTags.GlyphFormat.NONE;
  private FTGlyphClassRec glyph_class = null;
  public FTRasterRec raster = null;

  public FTRendererClassRec clazz = null;

  /* ==================== FTRendererRec ================================== */
  public FTRendererRec() {
    super();
    oid++;
    id = oid;
  }

  /* ==================== mySelf ================================== */
  public String mySelf() {
    return TAG + "!" + id + "!";
  }

  /* ==================== toString ===================================== */
  public String toString() {
    return mySelf() + "!";
  }

  /* ==================== toDebugString ===================================== */
  public String toDebugString() {
    StringBuffer str = new StringBuffer(mySelf() + "\n");
    return str.toString();
  }

  /* ==================== rasterRender ===================================== */
  public FTError.ErrorTag rasterRender(FTRasterRec raster, FTRasterParams params) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterRest not yet fully implemented");
    return clazz.rasterRender(raster, params);
  }

  /* ==================== render ===================================== */
  public FTError.ErrorTag render(FTRendererRec render, FTGlyphSlotRec slot, int render_mode, Object obj) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    Log.e(TAG, "rasterRest not yet fully implemented");
    return clazz.renderGlyph(render, slot, render_mode, (FTVectorRec)obj);
  }

  /* ==================== initModule ===================================== */
  public FTError.ErrorTag initModule(FTModuleClassRec module) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "initModule\n");

    this.module_clazz = module;
    return error;
  }

  /* =====================================================================
   * ft_set_current_renderer
   * =====================================================================
   */
  private FTError.ErrorTag ft_set_current_renderer(FTLibraryRec library) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTRendererRec renderer;

FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "ft_set_current_renderer\n");
    renderer = this.FTLookupRenderer(library, FTTags.GlyphFormat.OUTLINE, null);
    library.cur_renderer = renderer;
    return error;
  }

  /* =====================================================================
   * addRenderer
   * =====================================================================
   */
  public FTError.ErrorTag addRenderer() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTListNodeRec node;

    FTRendererClassRec clazz = (FTRendererClassRec) module_clazz;
FTDebug.Debug(0, FTDebug.DebugTag.DBG_INIT, TAG, "addRenderer: " + module_clazz.module_name + "!" +
        clazz.getGlyphFormat() + "!");
    glyph_format = clazz.getGlyphFormat();
    /* allocate raster object if needed */
    if (clazz.getGlyphFormat() == FTTags.GlyphFormat.OUTLINE) {
      FTReference<FTRasterRec> raster_ref = new FTReference<FTRasterRec>();

      raster_ref.Set(raster);
      error = clazz.rasterNew(raster_ref);
      raster = raster_ref.Get();
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
    }
    /* add to list */
    node = new FTListNodeRec();
    node.data = this;
    node.FTListAdd(library.renderers);
    ft_set_current_renderer(library);
    return error;
  }

  /* =====================================================================
   * FTLookupRenderer
   * =====================================================================
   */
  public FTRendererRec FTLookupRenderer(FTLibraryRec library, FTTags.GlyphFormat format, FTReference<FTListNodeRec> list_node_ref) {
    FTListNodeRec cur;
    FTListNodeRec node = null;
    FTRendererRec result = null;

    if (library == null) {
      return result;
    }
    cur = library.renderers.head;
    if (list_node_ref != null) {
      node = list_node_ref.Get();
      if (node != null) {
        cur = node.next;
      }
      node = null;
    }
    while (cur != null) {
      FTRendererRec renderer;

      renderer = (FTRendererRec) (cur.data);
      if (renderer.getGlyphFormat() == format) {
        if (list_node_ref != null) {
          node = cur;
        }
        result = renderer;
        break;
      }
      cur = cur.next;
    }
    return result;
  }

  /* =====================================================================
   * FTSetRenderer
   * =====================================================================
   */
  public static FTError.ErrorTag FTSetRenderer(FTLibraryRec library, FTRendererRec renderer, int num_params, FTParameter[] parameters) {
FTDebug.Debug(0, FTDebug.DebugTag.DBG_RENDER, TAG, "FTSetRenderer");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTListNodeRec node;
    FTParameter parameter;
    int parameterIdx = 0;

    if (library == null) {
      return FTError.ErrorTag.RENDER_INVALID_LIBRARY_HANDLE;
    }
    if (renderer == null) {
      return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
    }
    node = FTListRec.FTListFind(library.renderers, renderer);
    if (node == null) {
      error = FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
      return error;
    }
    FTListRec.FTListUp(library.renderers, node);
    if (renderer.glyph_format == FTTags.GlyphFormat.OUTLINE) {
      library.cur_renderer = renderer;
    }
    if (num_params > 0) {
      parameter = parameters[parameterIdx];
      for (; num_params > 0; num_params--) {
        error = renderer.clazz.setMode(renderer, parameter.getTag(), parameter.getData());
        if (error != FTError.ErrorTag.ERR_OK) {
          break;
        }
        parameterIdx++;
      }
    }
    return error;
  }

  /* =====================================================================
   * getGlyphFormat
   * =====================================================================
   */
  public FTTags.GlyphFormat getGlyphFormat() {
    return glyph_format;
  }

  /* =====================================================================
   * getGlyphFormat
   * =====================================================================
   */
  public FTGlyphClassRec getGlyphClass() {
    return glyph_class;
  }

}