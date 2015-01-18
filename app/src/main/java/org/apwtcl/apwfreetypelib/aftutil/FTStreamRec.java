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

package org.apwtcl.apwfreetypelib.aftutil;

import java.io.*;

  /* =====================================================================
   *    FTStreamRec
   *
   * @description:
   *   A structure used to describe an input stream.
   *
   * @input:
   *   size ::
   *     The stream size in bytes.
   *
   *   pos ::
   *     The current position within the stream.
   *
   *   pathname ::
   *     This field is completely ignored by FreeType.  However, it is often
   *     useful during debugging to use it to store the stream's filename
   *     (where available).
   *
   *   cursor ::
   *     This field is set and used internally by FreeType when parsing
   *     frames.
   *
   *   limit ::
   *     This field is set and used internally by FreeType when parsing
   *     frames.
   * =====================================================================
   */

public class FTStreamRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTStreamRec";
    
  private RandomAccessFile raf = null;
  private long size = 0L;
  private long pos = 0L;
  private String pathname = null;
  private long cursor = 0L;
  private long limit = 0L;

  /* ==================== FTStreamRec ================================== */
  public FTStreamRec() {
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
    str.append("..size: "+size+'\n');
    str.append("..pos: "+pos+'\n');
    str.append("..pathname: "+pathname+'\n');
    str.append("..cursor: "+cursor+'\n');
    str.append("..limit: "+limit+'\n');
    return str.toString();
  }
 
  /* =====================================================================
   * create a new input stream from an FTOpenArgs structure
   * =====================================================================
   */
  public FTError.ErrorTag Open(FTOpenArgs args) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    if (args == null) {
      Debug(0, DebugTag.DBG_INIT, TAG, "Open invalid argument");
      return FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
    }
    if (args.getPathname() == null) {
Debug(0, DebugTag.DBG_INIT, TAG, "Open invalid argument");
      return FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
    }
    this.pathname = args.getPathname();
    /* create a normal system stream */
    try {
Debug(0, DebugTag.DBG_INIT, TAG, "pathname: "+pathname+"!");
      raf = new RandomAccessFile(pathname, "r");
      size = raf.length();
      limit = size;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return FTError.ErrorTag.LOAD_INVALID_STREAM_OPERATION;
    } catch (IOException e) {
       e.printStackTrace();
       return FTError.ErrorTag.LOAD_INVALID_STREAM_OPERATION;
    }
    return error;
  }

  /* =====================================================================
   * getSize
   * =====================================================================
   */
  public long getSize() {
    return this.size;
  }

  /* =====================================================================
     * pos
     * =====================================================================
     */
    public long pos() {
      try {
        return raf.getFilePointer();
      } catch (IOException e) {
    	  e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * seek
     * =====================================================================
     */
    public long seek(long offset) {
      try {
        raf.seek(offset);
        cursor = offset;
        return pos();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * readByteArray
     * =====================================================================
     */
    public int readByteArray(byte[] buffer, int lgth) {
      try {
    	for (int i = 0; i < lgth; i++) {
          buffer[i] = raf.readByte();
    	}
        cursor += lgth;
    	return lgth;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * readLong
     * =====================================================================
     */
    public long readLong() {
      try {
        cursor += 8;
        return raf.readLong();
      } catch (IOException e) {
      	e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * readInt
     * =====================================================================
     */
    public int readInt() {
      try {
        cursor += 4;
        return raf.readInt();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * readShort
     * =====================================================================
     */
    public int readShort() {
      try {
        cursor += 2;
        return raf.readShort() & 0xFFFF;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * readByte
     * =====================================================================
     */
    public byte readByte() {
      try {
        cursor += 1;
        return raf.readByte();
      } catch (IOException e) {
       e.printStackTrace();
      }
      return -1;
    }

    /* =====================================================================
     * FTStreamEnterFrame
     * =====================================================================
     */
    public FTError.ErrorTag FTStreamEnterFrame(long count) {
      if (count > size) {
        return FTError.ErrorTag.LOAD_STREAM_READ_AFTER_SIZE;
      }
      return FTError.ErrorTag.ERR_OK;
    }
     
}