/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 *
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf at wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTGlyphRenderFuncs                                                 */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class FTGlyphRenderFuncs extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FTGlyphRenderFuncs";

    /* ==================== FTGlyphRenderFuncs ================================== */
    public FTGlyphRenderFuncs() {
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

    /* =====================================================================
     * FTRenderGlyph
     * =====================================================================
     */
    public static FTError.ErrorTag FTRenderGlyph(FTGlyphSlotRec slot, int render_mode) {
Debug(0, DebugTag.DBG_RENDER, TAG, "FTRenderGlyph");
      FTLibraryRec library;

      if (slot == null || slot.getFace() == null) {
        return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
      }
      library = slot.getFace().getDriver().library;
      return FTRenderGlyphInternal(library, slot, render_mode);
    }
    
    /* =====================================================================
     * FTRenderGlyphInternal
     * =====================================================================
     */
    public static FTError.ErrorTag FTRenderGlyphInternal(FTLibraryRec library, FTGlyphSlotRec slot, int render_mode) {
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
      FTRendererRec renderer;
      FTReference<FTListNodeRec> node_ref = new FTReference<FTListNodeRec>();

Debug(0, DebugTag.DBG_RENDER, TAG, "FTRenderGlyphInternal");
      /* if it is already a bitmap, no need to do anything */
      if (slot.getFormat() == FTTags.GlyphFormat.BITMAP) {
        /* already a bitmap, don't do anything */
      } else {
        FTListNodeRec node = null;
        boolean update = false;

        /* small shortcut for the very common case */
        if (slot.getFormat() == FTTags.GlyphFormat.OUTLINE) {
          renderer = library.cur_renderer;
          node = library.renderers.head;
        } else {
          node_ref.Set(node);
          renderer = library.cur_renderer.FTLookupRenderer(library, slot.getFormat(), node_ref);
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer name: "+renderer.clazz.module_name);
          node = node_ref.Get();
        }
        error = FTError.ErrorTag.GLYPH_UNIMPLEMENTED_FEATURE;
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer 0: "+renderer+"!");
        while (renderer != null) {
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer 1: "+renderer+"!"+slot.getBitmap()+"!");
          error = renderer.render(renderer, slot, render_mode, null);
          if (error == FTError.ErrorTag.ERR_OK || error != FTError.ErrorTag.GLYPH_CANNOT_RENDER_GLYPH) {
            break;
          }
          /* FT_Err_Cannot_Render_Glyph is returned if the render mode   */
          /* is unsupported by the current renderer for this glyph image */
          /* format.                                                     */
          /* now, look for another renderer that supports the same */
          /* format.                                               */
          node_ref.Set(node);
          renderer = library.cur_renderer.FTLookupRenderer(library, slot.getFormat(), node_ref);
if (renderer != null && renderer.clazz != null) {
Debug(0, DebugTag.DBG_RENDER, TAG, "renderer name: "+renderer.clazz.module_name);
}
          node = node_ref.Get();
          update = true;
        }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert0d: rows %d width %d pitch %d num_grays %d", slot.getBitmap().getRows(), slot.getBitmap().getWidth(), slot.getBitmap().getPitch(), slot.getBitmap().getNum_grays()));
        /* if we changed the current renderer for the glyph image format */
        /* we need to select it as the next current one                  */
        if (error == FTError.ErrorTag.ERR_OK && update && renderer != null) {
          FTRendererRec.FTSetRenderer(library, renderer, 0, null);
        }
      }
      /* we convert to a single bitmap format for computing the checksum */
      {
        FTBitmapRec bitmap = new FTBitmapRec();
        FTError.ErrorTag err;
        FTReference<FTBitmapRec> bitmap_ref = new FTReference<FTBitmapRec>();
        
        bitmap_ref.Set(bitmap);
StringBuffer str = new StringBuffer("");
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert: rows %d width %d pitch %d num_grays %d", bitmap.getRows(), bitmap.getWidth(), bitmap.getPitch(), bitmap.getNum_grays()));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("before BITMAP convert slot: rows %d width %d pitch %d num_grays %d", slot.getBitmap().getRows(), slot.getBitmap().getWidth(), slot.getBitmap().getPitch(), slot.getBitmap().getNum_grays()));
        err = slot.getBitmap().Convert(library, bitmap, 1);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("after BITMAP convert: rows %d width %d pitch %d num_grays %d", bitmap.getRows(), bitmap.getWidth(), bitmap.getPitch(), bitmap.getNum_grays()));
        bitmap = bitmap_ref.Get();
int r;
int c;
//Debug(0, DebugTag.DBG_RENDER, TAG, String.format("++ Bitmapconv char: %c %d rows: %d width: %d",
//    FTDemoHandle.currCharacter, FTDemoHandle.currGIndex, bitmap.rows, bitmap.width));
str = new StringBuffer("");
for(r = 0; r < bitmap.getRows(); r++) {
  str.delete(0,  str.length());
  str.append(String.format("%02d: ", r));
  for(c = 0; c < bitmap.getWidth(); c++) {
    str.append(String.format("%02x", bitmap.getBuffer()[r+c]));
  }
  Debug(0, DebugTag.DBG_RENDER, TAG, str.toString());
}
        if (err == FTError.ErrorTag.ERR_OK) {
          MD5CTX ctx = new MD5CTX();
          byte[] md5 = new byte[16];
          int i;

          FTReference<MD5CTX> ctx_ref = new FTReference<MD5CTX>();
          ctx_ref.Set(ctx);
          MD5CTX.MD5Init(ctx_ref);
          ctx = ctx_ref.Get();
          FTReference<Object> buffer_ref = new FTReference<Object>();
          buffer_ref.Set(bitmap.getBuffer());
          MD5CTX.MD5Update(ctx_ref, buffer_ref, (long)(bitmap.getRows() * bitmap.getPitch()));
          ctx = ctx_ref.Get();
          bitmap.setBuffer((byte[])buffer_ref.Get());
          FTReference<byte[]> md5_ref = new FTReference<byte[]>();
          md5_ref.Set(md5);
          MD5CTX.MD5Final(md5_ref, ctx_ref);
          ctx = ctx_ref.Get();
          md5 = md5_ref.Get();
          str = new StringBuffer(
                String.format("MD5 checksum for %dx%d bitmap:  ", bitmap.getRows(), bitmap.getPitch()));
          for (i = 0; i < 16; i++) {
            str.append(String.format("%02X", md5[i]));
          }
          FTTrace.Trace(7, TAG, str.toString());
        }
        bitmap.Done(library);
      }
      return error;
    }

}