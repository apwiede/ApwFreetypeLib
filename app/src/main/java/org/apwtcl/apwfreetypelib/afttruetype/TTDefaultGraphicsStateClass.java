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
  /*    TTDefaultGraphicsStateClass                                                    */
  /*                                                                       */
  /* <Struct>                                                              */
  /*    TTDefaultGraphicsStateClass                                                    */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The TrueType graphics state used during bytecode interpretation.   */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftttinterpreter.TTGraphicsStateRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTInterpTags;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTDefaultGraphicsStateClass extends TTGraphicsStateRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTDefaultGraphicsStateClass";

  /* ================ TTDefaultGraphicsStateClass ========================== */
  public TTDefaultGraphicsStateClass() {
    oid++;
    id = oid;

    rp0 = 0;
    rp1 = 0;
    rp2 = 0;
    dualVector = new FTVectorRec();
    dualVector.x = 0x4000;
    dualVector.y = 0;
    projVector = new FTVectorRec();
    projVector.x = 0x4000;
    projVector.y = 0;
    freeVector = new FTVectorRec();
    freeVector.x = 0x4000;
    freeVector.y = 0;

    loop = 1;
    minimum_distance = 64;
    round_state = TTInterpTags.RoundState.To_Grid;

    auto_flip = true;
    control_value_cutin = 68;
    single_width_cutin = 0;
    single_width_value = 0;
    delta_base = 9;
    delta_shift = 3;

    instruct_control = 0;
    scan_control = false;
    scan_type = 0;
    gep0 = 1;
    gep1 = 1;
    gep2 = 1;
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
    String super_str = super.toDebugString();
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    return super_str + str;
  }

}