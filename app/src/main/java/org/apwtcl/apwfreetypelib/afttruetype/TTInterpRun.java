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
  /*    TTInterpRun                                                          */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTInterpTags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;

public class TTInterpRun extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTInterpRun";

    public static int numInstructions = 0;
    public static boolean doInterpDebug = false;

    /* ==================== TTInterpRun ================================== */
    public TTInterpRun() {
      super();
      oid++;
      id = oid;
      String class_name = "org.apwtcl.gles20.truetype.TTInterpFuncs3";
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
     * TTRunContext
     * =====================================================================
     */

    public static FTError.ErrorTag TTRunContext(TTExecContextRec exec, boolean debug) {
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
Debug(0, FTDebug.DebugTag.DBG_INTERP, TAG, String.format("TT_Run_Context %d", TTInterpTags.CodeRange.GLYPH)+" zp0.cur: "+(Object)exec.zp0.xgetCur()+"\n zp1.cur: "+(Object)exec.zp1.xgetCur());

      if ((error = exec.TTGotoCodeRange(TTInterpTags.CodeRange.GLYPH, 0)) != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      exec.zp0 = exec.pts;
      exec.zp1 = exec.pts;
      exec.zp2 = exec.pts;
Debug(0, DebugTag.DBG_INTERP, TAG, "TT_Run_Context2 zp0.cur: "+exec.zp0.xgetCur()+" \nzp1.cur: "+(Object)exec.zp1.xgetCur());
for( int i = 0; i < 6; i++) {
//  Debug(0, DebugTag.DBG_INTERP, TAG, String.format("zp1.org: %d x: %d, y: %d\n", i, cur.zp1.org[i].x, cur.zp1.org[i].y));
//  Debug(0, DebugTag.DBG_INTERP, TAG, String.format("zp0.org: %d x: %d, y: %d\n", i, cur.zp0.org[i].x, cur.zp0.org[i].y));
}
      exec.graphics_state.gep0 = 1;
      exec.graphics_state.gep1 = 1;
      exec.graphics_state.gep2 = 1;
      exec.graphics_state.projVector.x = 0x4000;
      exec.graphics_state.projVector.y = 0x0000;
      exec.graphics_state.freeVector.x = exec.graphics_state.projVector.x;
      exec.graphics_state.freeVector.y = exec.graphics_state.projVector.y;
      exec.graphics_state.dualVector.x = exec.graphics_state.projVector.x;
      exec.graphics_state.dualVector.y = exec.graphics_state.projVector.y;
      exec.graphics_state.round_state = TTInterpTags.RoundState.To_Grid;
      exec.graphics_state.loop = 1;
      /* some glyphs leave something on the stack. so we clean it */
      /* before a new execution.                                  */
      exec.top = 0;
      exec.callTop = 0;
/* NEEDED !!!
      return exec.face.getInterpreter().run(exec);
 NEEDED !!! */
      return FTError.ErrorTag.ERR_OK;
    }
}
