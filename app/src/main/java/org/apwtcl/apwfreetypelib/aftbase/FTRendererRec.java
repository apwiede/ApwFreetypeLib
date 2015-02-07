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

import org.apwtcl.apwfreetypelib.aftraster.FTRasterParamsRec;
import org.apwtcl.apwfreetypelib.aftraster.FTRasterRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTParameterRec;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTRendererRec extends FTModuleRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTRendererRec";


  protected FTTags.GlyphFormat glyph_format = FTTags.GlyphFormat.NONE;
  private FTGlyphClassRec glyph_class = null;
  protected FTRasterRec raster = null;
  protected FTRendererClassRec clazz = null;

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
  public FTError.ErrorTag rasterRender(FTRasterRec raster, FTRasterParamsRec params) {
    return clazz.rasterRender(raster, params);
  }

  /* ==================== render ===================================== */
  public FTError.ErrorTag render(FTRendererRec render, FTGlyphSlotRec slot, int render_mode, Object obj) {
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
    library.setCur_renderer(renderer);
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
    node.FTListAdd(library.getRenderers());
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
    cur = library.getRenderers().head;
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
      if (renderer.glyph_format == format) {
        if (list_node_ref != null) {
          node = cur;
        }
        result = renderer;
        break;
      }
      cur = cur.next;
    }
    if (list_node_ref != null) {
      list_node_ref.Set(node);
    }
    return result;
  }

  /* =====================================================================
   * FTSetRenderer
   * =====================================================================
   */
  public static FTError.ErrorTag FTSetRenderer(FTLibraryRec library, FTRendererRec renderer, int num_params, FTParameterRec[] parameters) {
FTDebug.Debug(0, FTDebug.DebugTag.DBG_RENDER, TAG, "FTSetRenderer");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTListNodeRec node;
    FTParameterRec parameter;
    int parameterIdx = 0;

    if (library == null) {
      return FTError.ErrorTag.RENDER_INVALID_LIBRARY_HANDLE;
    }
    if (renderer == null) {
      return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
    }
    node = FTListRec.FTListFind(library.getRenderers(), renderer);
    if (node == null) {
      error = FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
      return error;
    }
    FTListRec.FTListUp(library.getRenderers(), node);
    if (renderer.glyph_format == FTTags.GlyphFormat.OUTLINE) {
      library.setCur_renderer(renderer);
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

  /* ==================== getGlyph_format ================================== */
  public FTTags.GlyphFormat getGlyph_format() {
    return glyph_format;
  }

  /* ==================== setGlyph_format ================================== */
  public void setGlyph_format(FTTags.GlyphFormat glyph_format) {
    this.glyph_format = glyph_format;
  }

  /* ==================== getGlyph_class ================================== */
  public FTGlyphClassRec getGlyph_class() {
    return glyph_class;
  }

  /* ==================== setGlyph_class ================================== */
  public void setGlyph_class(FTGlyphClassRec glyph_class) {
    this.glyph_class = glyph_class;
  }

  /* ==================== getRaster ================================== */
  public FTRasterRec getRaster() {
    return raster;
  }

  /* ==================== setRaster ================================== */
  public void setRaster(FTRasterRec raster) {
    this.raster = raster;
  }

  /* ==================== getClazz ================================== */
  public FTRendererClassRec getClazz() {
    return clazz;
  }

  /* ==================== setClazz ================================== */
  public void setClazz(FTRendererClassRec clazz) {
    this.clazz = clazz;
  }

}