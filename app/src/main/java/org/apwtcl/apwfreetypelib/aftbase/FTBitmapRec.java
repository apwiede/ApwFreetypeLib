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
import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

  /* ===================================================================== */
  /*    FTBitmapRec                                                           */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to describe a bitmap or pixmap to the raster.     */
  /*    Note that we now manage pixmaps of various depths through the      */
  /*    `pixel_mode' field.                                                */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    rows         :: The number of bitmap rows.                         */
  /*                                                                       */
  /*    width        :: The number of pixels in bitmap row.                */
  /*                                                                       */
  /*    pitch        :: The pitch's absolute value is the number of bytes  */
  /*                    taken by one bitmap row, including padding.        */
  /*                    However, the pitch is positive when the bitmap has */
  /*                    a `down' flow, and negative when it has an `up'    */
  /*                    flow.  In all cases, the pitch is an offset to add */
  /*                    to a bitmap pointer in order to go down one row.   */
  /*                                                                       */
  /*                    Note that `padding' means the alignment of a       */
  /*                    bitmap to a byte border, and FreeType functions    */
  /*                    normally align to the smallest possible integer    */
  /*                    value.                                             */
  /*                                                                       */
  /*                    For the B/W rasterizer, `pitch' is always an even  */
  /*                    number.                                            */
  /*                                                                       */
  /*                    To change the pitch of a bitmap (say, to make it a */
  /*                    multiple of 4), use @FT_Bitmap_Convert.            */
  /*                    Alternatively, you might use callback functions to */
  /*                    directly render to the application's surface; see  */
  /*                    the file `example2.cpp' in the tutorial for a      */
  /*                    demonstration.                                     */
  /*                                                                       */
  /*    buffer       :: A typeless pointer to the bitmap buffer.  This     */
  /*                    value should be aligned on 32-bit boundaries in    */
  /*                    most cases.                                        */
  /*                                                                       */
  /*    num_grays    :: This field is only used with                       */
  /*                    @FT_PIXEL_MODE_GRAY; it gives the number of gray   */
  /*                    levels used in the bitmap.                         */
  /*                                                                       */
  /*    pixel_mode   :: The pixel mode, i.e., how pixel bits are stored.   */
  /*                    See @FT_Pixel_Mode for possible values.            */
  /*                                                                       */
  /*    palette_mode :: This field is intended for paletted pixel modes;   */
  /*                    it indicates how the palette is stored.  Not       */
  /*                    used currently.                                    */
  /*                                                                       */
  /*    palette      :: A typeless pointer to the bitmap palette; this     */
  /*                    field is intended for paletted pixel modes.  Not   */
  /*                    used currently.                                    */
  /*                                                                       */
  /* <Note>                                                                */
  /*   For now, the only pixel modes supported by FreeType are mono and    */
  /*   grays.  However, drivers might be added in the future to support    */
  /*   more `colorful' options.                                            */
  /*                                                                       */
  /* ===================================================================== */

public class FTBitmapRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTBitmapRec";

  private final static Long FT_ULONG_MAX = 0x7fffffffffffffffL;

  private int rows = 0;
  private int width = 0;
  private int pitch = 0;
  private byte[] buffer = null;
  private int num_grays = 0;
  private FTTags.PixelMode pixel_mode = FTTags.PixelMode.GRAY;
  private byte palette_mode = 0;
  private Object palette = null;

  /* ==================== FTBitmapRec ================================== */
  public FTBitmapRec() {
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
    str.append("..rows: "+rows+'\n');
    str.append("..width: "+width+'\n');
    str.append("..pitch: "+pitch+'\n');
    str.append("..num_grays: "+num_grays+'\n');
    str.append("..pixel_mode: "+pixel_mode+'\n');
    str.append("..palette_mode: "+palette_mode+'\n');
    return str.toString();
  }


  /* =====================================================================
   * Convert
   * =====================================================================
   */
  public FTError.ErrorTag Convert(FTLibraryRec library, FTBitmapRec target, int alignment) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int row = 0;
    int col = 0;
    int idx;
    StringBuffer str = new StringBuffer();

    if (library == null) {
      return FTError.ErrorTag.GLYPH_INVALID_LIBRARY_HANDLE;
    }
Debug(0, DebugTag.DBG_RENDER, TAG, "Bitmap slot buffer:");
idx = 0;
for (row = 0; row < rows; row++) {
  str.delete(0, str.length());
  str.append(String.format("row: %02d ", row));
  for (col = 0; col < width; col++) {
    str.append(String.format(" 0x%02x", buffer[idx]));
    idx++;
  }
  Debug(0, DebugTag.DBG_RENDER, TAG, str.toString());
}
    switch (pixel_mode) {
      case MONO:
      case GRAY:
      case GRAY2:
      case GRAY4:
      case LCD:
      case LCD_V:
      case BGRA:
      {
        int pad;
        int old_size;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("CONVERT 1: rows: %d, width: %d", rows, width));
        old_size = target.rows * target.pitch;
        if (old_size < 0) {
          old_size = -old_size;
        }
        target.pixel_mode = FTTags.PixelMode.GRAY;
        target.rows = rows;
        target.width = width;
        pad = 0;
        if (alignment > 0) {
          pad = width % alignment;
          if (pad != 0) {
            pad = alignment - pad;
          }
        }
        target.pitch = width + pad;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("pitch: %d rows: %d %d div: %d", target.pitch, target.rows, FT_ULONG_MAX, (target.pitch > 0 ? (FT_ULONG_MAX / target.pitch) : 0)));
        if (target.pitch > 0 && target.rows > (FT_ULONG_MAX / target.pitch)) {
          return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
        }
        if (target.rows * target.pitch > old_size) {
          target.buffer = new byte[target.rows * target.pitch];
        }
        if (target.rows * target.pitch > old_size /* &&
             FT_QREALLOC(target.buffer, old_size, target.rows * target.pitch) */  && target.buffer == null) {
          return error;
        }
        }
        break;
      default:
        error = FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    switch (pixel_mode) {
      case MONO: {
        byte[] s = buffer;
        byte[] t = target.buffer;
        int i;
        int s_idx = 0;
        int t_idx = 0;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "CONVERT 2");
        target.num_grays = 2;
        for (i = rows; i > 0; i--) {
          byte[] ss = s;
          byte[] tt = t;
          int j;
          int ss_idx = s_idx;
          int tt_idx = t_idx;

          /* get the full bytes */
          for (j = width >> 3; j > 0; j--) {
            int val = ss[ss_idx + 0]; /* avoid a byte.int cast on each line */

            tt[tt_idx + 0] = (byte)((val & 0x80) >> 7);
            tt[tt_idx + 1] = (byte)((val & 0x40) >> 6);
            tt[tt_idx + 2] = (byte)((val & 0x20) >> 5);
            tt[tt_idx + 3] = (byte)((val & 0x10) >> 4);
            tt[tt_idx + 4] = (byte)((val & 0x08) >> 3);
            tt[tt_idx + 5] = (byte)((val & 0x04) >> 2);
            tt[tt_idx + 6] = (byte)((val & 0x02) >> 1);
            tt[tt_idx + 7] = (byte)( val & 0x01);
            tt_idx += 8;
            ss_idx += 1;
          }
          /* get remaining pixels (if any) */
          j = width & 7;
          if (j > 0) {
            int val = ss[ss_idx + 0];

            for ( ; j > 0; j--) {
              tt[tt_idx + 0] = (byte)((val & 0x80) >> 7);
              val <<= 1;
              tt_idx += 1;
            }
          }
          s_idx += pitch;
          t_idx += target.pitch;
        }
      }
      break;
      case GRAY:
      case LCD:
      case LCD_V: {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "CONVERT 3");
        int width = this.width;
        int i;
        int s_idx = 0;
        int t_idx = 0;

        target.num_grays = 256;
        for (i = rows; i > 0; i--) {
          for (int j = 0; j < width; j++) {
            target.buffer[t_idx + j] = buffer[s_idx + j];
          }
          s_idx += pitch;
          t_idx += target.pitch;
        }
      }
      break;
      case GRAY2: {
        int i;
        int s_idx = 0;
        int t_idx = 0;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "CONVERT 4");
        target.num_grays = 4;
        for (i = rows; i > 0; i--) {
          int j;
          int ss_idx = s_idx;
          int tt_idx = t_idx;

          /* get the full bytes */
          for (j = width >> 2; j > 0; j--) {
            int val = buffer[ss_idx + 0];

            target.buffer[tt_idx + 0] = (byte)((val & 0xC0) >> 6);
            target.buffer[tt_idx + 1] = (byte)((val & 0x30) >> 4);
            target.buffer[tt_idx + 2] = (byte)((val & 0x0C) >> 2);
            target.buffer[tt_idx + 3] = (byte)((val & 0x03));
            ss_idx += 1;
            tt_idx += 4;
          }
          j = width & 3;
          if (j > 0) {
            int val = buffer[ss_idx + 0];

            for (; j > 0; j--) {
              target.buffer[tt_idx + 0] = (byte)((val & 0xC0) >> 6);
              val <<= 2;
              tt_idx += 1;
            }
          }
          s_idx += pitch;
          t_idx += target.pitch;
        }
      }
      break;
      case GRAY4: {
        int i;
        int s_idx = 0;
        int t_idx = 0;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "CONVERT 5");
        target.num_grays = 16;
        for (i = rows; i > 0; i--) {
          int j;
          int ss_idx = s_idx;
          int tt_idx = t_idx;

          /* get the full bytes */
          for (j = width >> 1; j > 0; j--) {
            int val = buffer[ss_idx + 0];

            target.buffer[tt_idx + 0] = (byte)((val & 0xF0) >> 4);
            target.buffer[tt_idx + 1] = (byte)((val & 0x0F));
            ss_idx += 1;
            tt_idx += 2;
          }
          if ((width & 1) != 0) {
            target.buffer[0] = (byte)((buffer[ss_idx + 0] & 0xF0) >> 4);
          }
          s_idx += pitch;
          t_idx += target.pitch;
        }
      }
      break;
      case BGRA: {
        int s_pitch = pitch;
        int t_pitch = target.pitch;
        int i;
        int s_idx = 0;
        int t_idx = 0;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "CONVERT 6");
        target.num_grays = 256;
        for (i = rows; i > 0; i--) {
          int j;
          int ss_idx = s_idx;
          int tt_idx = t_idx;

          for (j = width; j > 0; j--) {
            FTReference<byte[]> ss_ref = new FTReference<byte[]>();
            ss_ref.Set(buffer);
            target.buffer[tt_idx + 0] = ft_gray_for_premultiplied_srgb_bgra(ss_ref, ss_idx);
            buffer = ss_ref.Get();
            ss_idx += 4;
            tt_idx += 1;
          }
          s_idx += s_pitch;
          t_idx += t_pitch;
        }
      }
      break;
      default:
        ;
    }
    return error;
  }

  /* =====================================================================
   * Done
   * =====================================================================
   */
  public FTError.ErrorTag Done(FTLibraryRec library) {
    if (library == null) {
      return FTError.ErrorTag.RENDER_INVALID_LIBRARY_HANDLE;
    }
    rows = 0;
    width = 0;
    pitch = 0;
    buffer = null;
    num_grays = 0;
    pixel_mode = FTTags.PixelMode.GRAY;
    palette_mode = 0;
    palette = null;
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * ft_gray_for_premultiplied_srgb_bgra
   * =====================================================================
   */
  public Byte ft_gray_for_premultiplied_srgb_bgra(FTReference<byte[]> ss_ref, int ss_idx) {
    Log.w(TAG, "ft_gray_for_premultiplied_srgb_bgra: not yet implemented!!\n");
    byte[] ss = ss_ref.Get();
    return ss[ss_idx];
  }

  /* ==================== getRows ================================== */
  public int getRows() {
    return rows;
  }

  /* ==================== setRows ================================== */
  public void setRows(int rows) {
    this.rows = rows;
  }

  /* ==================== getWidth ================================== */
  public int getWidth() {
    return width;
  }

  /* ==================== setWidth ================================== */
  public void setWidth(int width) {
    this.width = width;
  }

  /* ==================== getPitch ================================== */
  public int getPitch() {
    return pitch;
  }

  /* ==================== setPitch ================================== */
  public void setPitch(int pitch) {
    this.pitch = pitch;
  }

  /* ==================== getBuffer ================================== */
  public byte[] getBuffer() {
    return buffer;
  }

  /* ==================== setBuffer ================================== */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  /* ==================== getNum_grays ================================== */
  public int getNum_grays() {
    return num_grays;
  }

  /* ==================== setNum_grays ================================== */
  public void setNum_grays(int num_grays) {
    this.num_grays = num_grays;
  }

  /* ==================== getPixel_mode ================================== */
  public FTTags.PixelMode getPixel_mode() {
    return pixel_mode;
  }

  /* ==================== setPixel_mode ================================== */
  public void setPixel_mode(FTTags.PixelMode pixel_mode) {
    this.pixel_mode = pixel_mode;
  }

  /* ==================== getPalette_mode ================================== */
  public byte getPalette_mode() {
    return palette_mode;
  }

  /* ==================== setPalette_mode ================================== */
  public void setPalette_mode(byte palette_mode) {
    this.palette_mode = palette_mode;
  }

  /* ==================== getPalette ================================== */
  public Object getPalette() {
    return palette;
  }

  /* ==================== setPalette ================================== */
  public void setPalette(Object palette) {
    this.palette = palette;
  }

}