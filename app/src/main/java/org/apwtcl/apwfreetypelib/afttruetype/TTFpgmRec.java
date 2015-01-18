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

package org.apwtcl.apwfreetypelib.afttruetype;

  /* ===================================================================== */
  /*    TTFpgmRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType fpgm table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTFpgmRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTFpgm";

  private int font_program_size = 0;
  private TTOpCode.OpCode[] font_program = null;

  /* ==================== TTFpgmRec ================================== */
  public TTFpgmRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: fpgm offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.fpgm, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      font_program = null;
      font_program_size = 0;
      error = FTError.ErrorTag.ERR_OK;
      FTTrace.Trace(7, TAG, "is missing");
    } else {
      length = length_ref.Get();
      font_program_size = length;
      if (font_program == null) {
        font_program = new TTOpCode.OpCode[length];
      }
// FIXME!! need better code!
      byte[] my_array = new byte[length];
      if (stream.readByteArray(my_array, length) < 0) {
        error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
        return error;
      }

      for(int i = 0; i < length; i++) {
        font_program[i] = TTOpCode.OpCode.getTableTag(my_array[i] & 0xFF);
      }
      FTTrace.Trace(7, TAG, String.format("loaded, %12d bytes", font_program_size));
    }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, toDebugString());
    return error;
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
    str.append("..font_program_size: "+font_program_size+'\n');
    return str.toString();
  }

  /* ==================== getFontProgramSize ================================== */
  public int getFontProgramSize() {
    return font_program_size;
  }

  /* ==================== getFontProgram ================================== */
  public TTOpCode.OpCode[] getFontProgram() {
    return font_program;
  }

}
