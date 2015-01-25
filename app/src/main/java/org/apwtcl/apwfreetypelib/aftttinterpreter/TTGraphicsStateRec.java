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

package org.apwtcl.apwfreetypelib.aftttinterpreter;

  /* ===================================================================== */
  /*    TTGraphicsStateRec                                                    */
  /*                                                                       */
  /* <Struct>                                                              */
  /*    TTGraphicsStateRec                                                    */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The TrueType graphics state used during bytecode interpretation.   */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class TTGraphicsStateRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGraphicsStateRec";

  protected int rp0;
  protected int rp1;
  protected int rp2;
  protected FTVectorRec dualVector;
  protected FTVectorRec projVector;
  protected FTVectorRec freeVector;
  protected int loop;
  protected int minimum_distance;
  protected TTInterpTags.Round round_state;
  protected boolean auto_flip;
  protected int control_value_cutin;
  protected int single_width_cutin;
  protected int single_width_value;
  protected int delta_base;
  protected int delta_shift;
  protected byte instruct_control;
    /* According to Greg Hitchcock from Microsoft, the `scan_control'     */
    /* variable as documented in the TrueType specification is a 32-bit   */
    /* integer; the high-word part holds the SCANTYPE value, the low-word */
    /* part the SCANCTRL value.  We separate it into two fields.          */
  protected boolean scan_control;
  protected int scan_type;
  protected int gep0;
  protected int gep1;
  protected int gep2;

  /* ==================== TTGraphicsStateRec ================================== */
  public TTGraphicsStateRec() {
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
    str.append("...rp0: "+rp0+'\n');
    str.append("...rp1: "+rp1+'\n');
    str.append("...rp2: "+rp2+'\n');
    str.append("...dualVector: x: "+dualVector.x+" y: "+dualVector.y+'\n');
    str.append("...projVector: x: "+projVector.x+" y: "+projVector.y+'\n');
    str.append("...freeVector: x: "+freeVector.x+" y: "+freeVector.y+'\n');
    str.append("...loop: "+loop+'\n');
    str.append("...minimum_distance: "+minimum_distance+'\n');
    str.append("...round_state: "+round_state+'\n');
    str.append("...auto_flip: "+auto_flip+'\n');
    str.append("...control_value_cutin: "+control_value_cutin+'\n');
    str.append("...single_width_cutin: "+single_width_cutin+'\n');
    str.append("...single_width_value: "+single_width_value+'\n');
    str.append("...delta_base: "+delta_base+'\n');
    str.append("...delta_shift: "+delta_shift+'\n');
    str.append("...instruct_control: "+instruct_control+'\n');
    str.append("...scan_control: "+scan_control+'\n');
    str.append("...scan_type: "+scan_type+'\n');
    str.append("...gep0: "+gep0+'\n');
    str.append("...gep1: "+gep1+'\n');
    str.append("...gep2: "+gep2+'\n');
    return str.toString();
  }

  /* ==================== getRp0 ================================== */
  public int getRp0() {
    return rp0;
  }

  /* ==================== setRp0 ================================== */
  public void setRp0(int rp0) {
    this.rp0 = rp0;
  }

  /* ==================== getRp1 ================================== */
  public int getRp1() {
    return rp1;
  }

  /* ==================== setRp1 ================================== */
  public void setRp1(int rp1) {
    this.rp1 = rp1;
  }

  /* ==================== getRp2 ================================== */
  public int getRp2() {
    return rp2;
  }

  /* ==================== setRp2 ================================== */
  public void setRp2(int rp2) {
    this.rp2 = rp2;
  }

  /* ==================== getDualVector ================================== */
  public FTVectorRec getDualVector() {
    return dualVector;
  }

  /* ==================== setDualVector ================================== */
  public void setDualVector(FTVectorRec dualVector) {
    this.dualVector = dualVector;
  }

  /* ==================== getProjVector ================================== */
  public FTVectorRec getProjVector() {
    return projVector;
  }

  /* ==================== setProjVector ================================== */
  public void setProjVector(FTVectorRec projVector) {
    this.projVector = projVector;
  }

  /* ==================== getFreeVector ================================== */
  public FTVectorRec getFreeVector() {
    return freeVector;
  }

  /* ==================== setFreeVector ================================== */
  public void setFreeVector(FTVectorRec freeVector) {
    this.freeVector = freeVector;
  }

  /* ==================== getLoop ================================== */
  public int getLoop() {
    return loop;
  }

  /* ==================== setLoop ================================== */
  public void setLoop(int loop) {
    this.loop = loop;
  }

  /* ==================== getMinimum_distance ================================== */
  public int getMinimum_distance() {
    return minimum_distance;
  }

  /* ==================== setMinimum_distance ================================== */
  public void setMinimum_distance(int minimum_distance) {
    this.minimum_distance = minimum_distance;
  }

  /* ==================== getRound_state ================================== */
  public TTInterpTags.Round getRound_state() {
    return round_state;
  }

  /* ==================== setRound_state ================================== */
  public void setRound_state(TTInterpTags.Round round_state) {
    this.round_state = round_state;
  }

  /* ==================== isAuto_flip ================================== */
  public boolean isAuto_flip() {
    return auto_flip;
  }

  /* ==================== setAuto_flip ================================== */
  public void setAuto_flip(boolean auto_flip) {
    this.auto_flip = auto_flip;
  }

  /* ==================== getControl_value_cutin ================================== */
  public int getControl_value_cutin() {
    return control_value_cutin;
  }

  /* ==================== setControl_value_cutin ================================== */
  public void setControl_value_cutin(int control_value_cutin) {
    this.control_value_cutin = control_value_cutin;
  }

  /* ==================== getSingle_width_cutin ================================== */
  public int getSingle_width_cutin() {
    return single_width_cutin;
  }

  /* ==================== setSingle_width_cutin ================================== */
  public void setSingle_width_cutin(int single_width_cutin) {
    this.single_width_cutin = single_width_cutin;
  }

  /* ==================== getSingle_width_value ================================== */
  public int getSingle_width_value() {
    return single_width_value;
  }

  /* ==================== setSingle_width_value ================================== */
  public void setSingle_width_value(int single_width_value) {
    this.single_width_value = single_width_value;
  }

  /* ==================== getDelta_base ================================== */
  public int getDelta_base() {
    return delta_base;
  }

  /* ==================== setDelta_base ================================== */
  public void setDelta_base(int delta_base) {
    this.delta_base = delta_base;
  }

  /* ==================== getDelta_shift ================================== */
  public int getDelta_shift() {
    return delta_shift;
  }

  /* ==================== setDelta_shift ================================== */
  public void setDelta_shift(int delta_shift) {
    this.delta_shift = delta_shift;
  }

  /* ==================== getInstruct_control ================================== */
  public byte getInstruct_control() {
    return instruct_control;
  }

  /* ==================== setInstruct_control ================================== */
  public void setInstruct_control(byte instruct_control) {
    this.instruct_control = instruct_control;
  }

  /* ==================== isScan_control ================================== */
  public boolean isScan_control() {
    return scan_control;
  }

  /* ==================== setScan_control ================================== */
  public void setScan_control(boolean scan_control) {
    this.scan_control = scan_control;
  }

  /* ==================== getScan_type ================================== */
  public int getScan_type() {
    return scan_type;
  }

  /* ==================== setScan_type ================================== */
  public void setScan_type(int scan_type) {
    this.scan_type = scan_type;
  }

  /* ==================== getGep0 ================================== */
  public int getGep0() {
    return gep0;
  }

  /* ==================== setGep0 ================================== */
  public void setGep0(int gep0) {
    this.gep0 = gep0;
  }

  /* ==================== getGep1 ================================== */
  public int getGep1() {
    return gep1;
  }

  /* ==================== setGep1 ================================== */
  public void setGep1(int gep1) {
    this.gep1 = gep1;
  }

  /* ==================== getGep2 ================================== */
  public int getGep2() {
    return gep2;
  }

  /* ==================== setGeps ================================== */
  public void setGep2(int gep2) {
    this.gep2 = gep2;
  }

}