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

  /* ===================================================================== */
  /*    FTCSBitRec                                                         */
  /* <Description>                                                         */
  /*    A very compact structure used to describe a small glyph bitmap.    */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    width     :: The bitmap width in pixels.                           */
  /*                                                                       */
  /*    height    :: The bitmap height in pixels.                          */
  /*                                                                       */
  /*    left      :: The horizontal distance from the pen position to the  */
  /*                 left bitmap border (a.k.a. `left side bearing', or    */
  /*                 `lsb').                                               */
  /*                                                                       */
  /*    top       :: The vertical distance from the pen position (on the   */
  /*                 baseline) to the upper bitmap border (a.k.a. `top     */
  /*                 side bearing').  The distance is positive for upwards */
  /*                 y~coordinates.                                        */
  /*                                                                       */
  /*    format    :: The format of the glyph bitmap (monochrome or gray).  */
  /*                                                                       */
  /*    max_grays :: Maximum gray level value (in the range 1 to~255).     */
  /*                                                                       */
  /*    pitch     :: The number of bytes per bitmap line.  May be positive */
  /*                 or negative.                                          */
  /*                                                                       */
  /*    xadvance  :: The horizontal advance width in pixels.               */
  /*                                                                       */
  /*    yadvance  :: The vertical advance height in pixels.                */
  /*                                                                       */
  /*    buffer    :: A pointer to the bitmap pixels.                       */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class FTCSBitRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTCSBitRec";

  private int width = 0;
  private int height = 0;
  private int left = 0;
  private int top = 0;
  private byte format = 0;
  private int max_grays = 0;
  private int pitch = 0;
  private int xadvance = 0;
  private int yadvance = 0;
  private byte[] buffer = null;

  /* ==================== FTCSBitRec ================================== */
  public FTCSBitRec() {
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
    str.append("..width: "+width+'\n');
    str.append("..height: "+height+'\n');
    str.append("..left: "+left+'\n');
    str.append("..top: "+top+'\n');
    str.append("..format: "+format+'\n');
    str.append("..max_grays: "+max_grays+'\n');
    str.append("..pitch: "+pitch+'\n');
    str.append("..xadvance: "+xadvance+'\n');
    str.append("..yadvance: "+yadvance+'\n');
    return str.toString();
  }
 
  /* =====================================================================
   * FTCSBitCacheLookupScaler
   * =====================================================================
   */
  public FTError.ErrorTag FTCSBitCacheLookupScaler(Object cache,
             FTCScalerRec scaler, Long load_flags, int glyphIndex,
             FTReference<FTCSBitRec> sbit_ref, FTReference<FTCNodeRec> node_ref) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTCSBitRec sbit;
    FTCBasicQueryRec query = new FTCBasicQueryRec();
    FTCNodeRec node = null; /* make compiler happy */
    Object hash;

    if (node_ref != null) {
        node_ref.Set(null);
    }
    /* other argument checks delayed to FTC_Cache_Lookup */
    if (sbit_ref == null || scaler == null) {
        return FTError.ErrorTag.GLYPH_INVALID_ARGUMENT;
    }
    sbit_ref.Set(null);
    /* FT_Load_Glyph(), FT_Load_Char() take FT_UInt flags */
    query.getAttrs().setScaler(scaler);
    query.getAttrs().setLoad_flags(load_flags.intValue());
    /* beware, the hash must be the same for all glyph ranges! */
    hash = FTCHashFuncs.FTC_BASIC_ATTR_HASH(query.getAttrs()) + glyphIndex / FTCSNodeRec.FTC_SBIT_ITEMS_PER_NODE;
    FTReference<FTCBasicQueryRec> query_ref = new FTReference<FTCBasicQueryRec>();
    query_ref.Set(query);
System.out.println("FTCSBitCacheLookupScaler: cache: "+cache+"!"+((FTCGCacheClassRec)cache).families+"!");
    error = ((FTCGCacheClassRec)cache).FTCGCacheLookup((int)hash, glyphIndex, query, node_ref);
//      FTC_GCACHE_LOOKUP_CMP(cache, ftc_basic_family_compare, FTC_SNode_Compare,
//              hash, glyphIndex, query_ref, node, error );
//      query = query_ref.Get();
    node = node_ref.Get();
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    sbit = ((FTCSNodeRec)(node)).getSbits()[(glyphIndex - ((FTCGNodeRec)node).getGindex())];
    sbit_ref.Set(sbit);
    if (node_ref != null) {
      node_ref.Set(node);
      node.ref_count++;
    }
    return error;
  }


  /* ==================== getFormat ================================== */
  public byte getFormat() {
    return format;
  }

  /* ==================== setFormat ================================== */
  public void setFormat(byte format) {
    this.format = format;
  }

  /* ==================== getTop ================================== */
  public int getTop() {
    return top;
  }

  /* ==================== setTop ================================== */
  public void setTop(int top) {
    this.top = top;
  }

  /* ==================== getLeft ================================== */
  public int getLeft() {
    return left;
  }

  /* ==================== setLeft ================================== */
  public void setLeft(int left) {
    this.left = left;
  }

  /* ==================== getHeight ================================== */
  public int getHeight() {
    return height;
  }

  /* ==================== setHeight ================================== */
  public void setHeight(int height) {
    this.height = height;
  }

  /* ==================== getWidth ================================== */
  public int getWidth() {
    return width;
  }

  /* ==================== setWidth ================================== */
  public void setWidth(int width) {
    this.width = width;
  }

  /* ==================== getMax_grays ================================== */
  public int getMax_grays() {
    return max_grays;
  }

  /* ==================== setMax_grays ================================== */
  public void setMax_grays(int max_grays) {
    this.max_grays = max_grays;
  }

  /* ==================== getPitch ================================== */
  public int getPitch() {
    return pitch;
  }

  /* ==================== setPitch ================================== */
  public void setPitch(int pitch) {
    this.pitch = pitch;
  }

  /* ==================== getXadvance ================================== */
  public int getXadvance() {
    return xadvance;
  }

  /* ==================== setXadvance ================================== */
  public void setXadvance(int xadvance) {
    this.xadvance = xadvance;
  }


  /* ==================== getYadvance ================================== */
  public int getYadvance() {
    return yadvance;
  }

  /* ==================== setYadvance ================================== */
  public void setYadvance(int yadvance) {
    this.yadvance = yadvance;
  }

  /* ==================== getBuffer ================================== */
  public byte[] getBuffer() {
    return buffer;
  }

  /* ==================== setBuffer ================================== */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

}