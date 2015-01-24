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

package org.apwtcl.apwfreetypelib.afttruetype;

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

  /* ===================================================================== */
  /*    TTInterpBase                                                       */
  /*                                                                       */
  /* ===================================================================== */

public class TTInterpBase extends FTDebug {
    private static int oid = 0;



    private int id;
    private static String TAG = "TTInterpBase";
    public static TTExecContextRec cur = new TTExecContextRec();
    public int x = 0;
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
    
    /* ==================== setValueOfCurField ===================================== */
    public void setValueOfCurField(String name, int value) {
      try {
    	Class<?> cls = cur.getClass();
Debug(0, DebugTag.DBG_INTERP, TAG, "S1:"+cur.graphics_state.getRound_state());
    	name = "GS";
    	Field fld = cls.getDeclaredField(name);
Debug(0, DebugTag.DBG_INTERP, TAG, "fld:"+fld+"!"+cls);
//Class<?> cls4 = fld.getDeclaringClass();
//System.out.println("cls4:"+cls4+"!"+cls4.getName());
    	Class<?> cls2 = fld.getDeclaringClass();
Debug(0, DebugTag.DBG_INTERP, TAG, "cls2:"+cls2+"!"+cls2.getName());

        Class<?> cls3 = Class.forName("org.apwtcl.gles20.truetype.TTGraphicsState");
Debug(0, DebugTag.DBG_INTERP, TAG, "cls3:"+cls3+"!"+cls3.getName());
    	Field fld2 = cls3.getDeclaredField("round_state");
Debug(0, DebugTag.DBG_INTERP, TAG, "fld2:"+fld2);
    	fld2.setInt(cur.graphics_state, 13);
      } catch (NoSuchFieldException x) {
        x.printStackTrace();
      } catch (IllegalAccessException x) {
        x.printStackTrace();
      } catch (ClassNotFoundException x) {
          x.printStackTrace();
      }
Debug(0, DebugTag.DBG_INTERP, TAG, "var:"+name+"!"+cur.graphics_state.getRound_state()+"!");
    }
 
    /* ==================== callCurFunc ===================================== */
    public static int callCurFunc(String name, int value1, int value2) {
      int result;

Debug(0, DebugTag.DBG_INTERP, TAG, "call method:"+name+"!"+value1+"!"+value2+"!");
      try {
    	Class<?> cls = cur.getClass();
    	Method m = cls.getDeclaredMethod(name);
Debug(0, DebugTag.DBG_INTERP, TAG, "m:"+m+"!"+cls);
    	result = (int)m.invoke(cur, value1, value2);
    	return result;
      } catch (InvocationTargetException x) {
          x.printStackTrace();
      } catch (NoSuchMethodException x) {
        x.printStackTrace();
      } catch (IllegalAccessException x) {
        x.printStackTrace();
      }
      return -1;
    }
 
}