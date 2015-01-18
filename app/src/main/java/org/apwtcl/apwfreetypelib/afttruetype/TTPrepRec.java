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
  /*    TTPrepRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType prep table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTPrepRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTPrep";

  private int prep_program_size = 0;
  private TTOpCode.OpCode[] prep_program = null;

  /* ==================== TTPrepRec ================================== */
  public TTPrepRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: prep offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    Integer length = new Integer(0);
    FTReference<Integer> length_ref = new FTReference<Integer>();

    length_ref.Set(length);
    error = ttface.gotoTable(TTTags.Table.prep, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      prep_program = null;
      prep_program_size = 0;
      error = FTError.ErrorTag.ERR_OK;
      FTTrace.Trace(7, TAG, "is missing");
    } else {
      length = length_ref.Get();
      prep_program_size = length;
      if (prep_program == null) {
        prep_program = new TTOpCode.OpCode[length];
      }
      byte[] my_array = new byte[length];
      if (stream.readByteArray(my_array, length.intValue()) < 0) {
        error = FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
        return error;
      }
// FIXME !! need better code!
      for(int i = 0; i < length; i++) {
        prep_program[i] = TTOpCode.OpCode.getTableTag(my_array[i] & 0xFF);
      }
      FTTrace.Trace(7, TAG, String.format("loaded, %12d bytes", prep_program_size));
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
    str.append("..prep_program_size: "+prep_program_size+'\n');
    return str.toString();
  }

  /* ==================== getPrepProgramSize ================================== */
  public int getPrepProgramSize() {
    return prep_program_size;
  }

  /* ==================== getPrepProgram ================================== */
  public TTOpCode.OpCode[] getPrepProgram() {
    return prep_program;
  }

}
