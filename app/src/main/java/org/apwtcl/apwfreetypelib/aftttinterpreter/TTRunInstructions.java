package org.apwtcl.apwfreetypelib.aftttinterpreter;

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

  /* ===================================================================== */
  /*    TTRunInstructions                                                  */
  /*                                                                       */
  /* <Description>                                                         */
  /*    Executes one or more instruction in the execution context.  This   */
  /*    is the main function of the TrueType opcode interpreter.           */
  /*                                                                       */
  /* <Input>                                                               */
  /*    exec :: A handle to the target execution context.                  */
  /*                                                                       */
  /* <Return>                                                              */
  /*    FreeType error code.  0 means success.                             */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Only the object manager and debugger should call this function.    */
  /*                                                                       */
  /*    This function is publicly exported because it is directly          */
  /*    invoked by the TrueType debugger.                                  */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTCallRec;
import org.apwtcl.apwfreetypelib.afttruetype.TTDefRec;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTRunInstructions extends TTExecContextRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTRunInstructions";

  private int numInstructions = 0;
  private boolean doInterpDebug = false;

  private final static int MAX_RUNNABLE_OPCODES = 1000000;
  public final static int TT_INTERPRETER_VERSION_35 = 35;

  private TTInstructionFuncGrp0 func_grp0 = null;
  private TTInstructionFuncGrp1 func_grp1 = null;
  private TTInstructionFuncGrp2 func_grp2 = null;
  private TTInstructionFuncGrp3 func_grp3 = null;
  private TTInstructionFuncGrp4 func_grp4 = null;
  private TTInstructionFuncGrp5 func_grp5 = null;
  private TTInstructionFuncGrp6 func_grp6 = null;
  private TTInstructionFuncGrp7 func_grp7 = null;
  private TTInstructionFuncGrp8 func_grp8 = null;
  private TTInstructionFuncGrp9 func_grp9 = null;

  /* ==================== TTRunInstructions ================================== */
  public TTRunInstructions()
  {
    oid++;
    id = oid;

    func_grp0 = new TTInstructionFuncGrp0(this);
    func_grp1 = new TTInstructionFuncGrp1(this);
    func_grp2 = new TTInstructionFuncGrp2(this);
    func_grp3 = new TTInstructionFuncGrp3(this);
    func_grp4 = new TTInstructionFuncGrp4(this);
    func_grp5 = new TTInstructionFuncGrp5(this);
    func_grp6 = new TTInstructionFuncGrp6(this);
    func_grp7 = new TTInstructionFuncGrp7(this);
    func_grp8 = new TTInstructionFuncGrp8(this);
    func_grp9 = new TTInstructionFuncGrp9(this);
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

    /* ==================== RunInstructions =====================================
     *
     * RunInstructions
     *
     *  This function executes a run of opcodes.  It will exit in the
     *  following cases:
     *
     *  - Errors (in which case it returns FALSE).
     *
     *  - Reaching the end of the main code range (returns TRUE).
     *    Reaching the end of a code range within a function call is an
     *    error.
     *
     *  - After executing one single opcode, if the flag `Instruction_Trap'
     *    is set to TRUE (returns TRUE).
     *
     *  On exit with TRUE, test IP < CodeSize to know whether it comes from
     *  an instruction trap or a normal termination.
     *
     *
     *  Note: The documented DEBUG opcode pops a value from the stack.  This
     *        behaviour is unsupported; here a DEBUG opcode is always an
     *        error.
     *
     *
     * THIS IS THE INTERPRETER'S MAIN LOOP.
     *
     *  Instructions appear in the specification's order.
     *
     * =====================================================================
     */

  @Override
  public FTError.ErrorTag RunInstructions() {
    TTOpCode.OpCode[] insts = code;
    int ins_counter = 0;  /* executed instructions counter */
    short i;

    Debug(0, DebugTag.DBG_INTERP, TAG, "RunInstructions");

    render_funcs = new TTRenderFunc();
    render_funcs.SetCvtFuncs(this);
    render_funcs.ComputeFuncs(this);
    render_funcs.ComputeRound(graphics_state.round_state);
//Debug(0, DebugTag.DBG_INTERP, TAG,  "cur.IP: "+cur.IP+"!"+cur.length);
    do {
      opcode = code[IP];
      numInstructions++;
      if (numInstructions > 500000) {
        doInterpDebug = true;
      }

      FTTrace.Trace(3, TAG, String.format("opcode: 0x%02x", opcode.getVal())+" cur.IP: "+IP+" opcode: "+opcode+" numInstructions: "+numInstructions);
//FTGlyphLoaderRec._showLoaderZone2("interp_opcode");
Debug(0, DebugTag.DBG_INTERP, TAG, "OP: "+opcode+" "+opcode.getOpCodeLength());
      length = opcode.getOpCodeLength();
      if (length < 0) {
        if (IP + 1 >= codeSize) {
          return LErrorCodeOverflow();
        }
        length = 2 - length * (code[IP + 1].getVal());
      }
      if (IP + length > codeSize) {
        return LErrorCodeOverflow();
      }
        /* First, let's check for empty stack and overflow */
      numArgs = top - (opcode.getPushCount() >> 4);
Debug(0, DebugTag.DBG_INTERP, TAG, String.format("numArgs: %d top: %d stack[numArgs]: %d", numArgs, top, stack[numArgs]));
        /* `args' is the top of the stack once arguments have been popped. */
        /* One can also interpret it as the index of the last argument.    */
      if (numArgs < 0) {
        if (pedantic_hinting) {
          error = FTError.ErrorTag.INTERP_TOO_FEW_ARGUMENTS;
          return LError();
        }
          /* push zeroes onto the stack */
        for (i = 0; i < opcode.getPushCount() >> 4; i++) {
          stack[i] = 0;
        }
        numArgs = 0;
      }
      new_top = numArgs + (opcode.getPushCount() & 15);
        /* `new_top' is the new top of the stack, after the instruction's */
        /* execution.  `top' will be set to `new_top' after the `switch'  */
        /* statement.                                                     */
Debug(0, DebugTag.DBG_INTERP, TAG, "Stack: "+new_top+"!"+stackSize+"!");
      if (new_top > stackSize) {
        error = FTError.ErrorTag.INTERP_STACK_OVERFLOW;
        return LError();
      }
      step_ins = true;
      error = FTError.ErrorTag.ERR_OK;
      {
        switch (opcode) {
          /* TTInstructionGroup0 */
          case SVTCA_y:  /* SVTCA y  */
          case SVTCA_x:  /* SVTCA x  */
            func_grp1.SVTCA();
            break;
          case SPvTCA_y:  /* SPvTCA y */
          case SPvTCA_x:  /* SPvTCA x */
            func_grp1.SPVTCA();
            break;
          case SFvTCA_y:  /* SFvTCA y */
          case SFvTCA_x:  /* SFvTCA x */
            func_grp1.SFVTCA();
            break;
/*
          {
            int AA;
            int BB;

            AA = ((opcode.getVal() & 1) << 14);
            BB = (AA ^ 0x4000);
            if (opcode.getVal() < 4) {
              graphics_state.projVector.x = AA;
              graphics_state.projVector.y = BB;
              graphics_state.dualVector.x = AA;
              graphics_state.dualVector.y = BB;
            }
            if ((opcode.getVal() & 2) == 0) {
              graphics_state.freeVector.x = AA;
              graphics_state.freeVector.y = BB;
            } else {
//              GUESS_VECTOR( freeVector );
            }
            render_funcs.ComputeFuncs(this);
            break;
          }
*/
          case SPvTL_OR_IF: /* SPvTL // */
          case SPvTL_PLUS:  /* SPvTL +  */
            func_grp0.SPvTL();
            break;
          case SFvTL_OR_IF: /* SFvTL // */
          case SFvTL_PLUS:  /* SFvTL +  */
            func_grp0.SFVTL();
            break;
          case SPvFS:  /* SPvFS */
            func_grp0.SPVFS();
            break;
          case SFvFS:  /* SFvFS */
            func_grp0.SFVFS();
            break;
          case GPV:  /* GPV */
            func_grp0.GPV();
            break;
          case GFV:  /* GFV */
            func_grp0.GFV();
            break;
          case SFvTPv:  /* SFvTPv */
            func_grp0.SFVTPV();
            break;
          case ISECT:  /* ISECT  */
            func_grp0.ISECT();
            break;

          /* TTInstructionGroup1 */
          case SRP0:  /* SRP0 */
            func_grp1.SRP0();
            break;
          case SRP1:  /* SRP1 */
            func_grp1.SRP1();
            break;
          case SRP2:  /* SRP2 */
            func_grp1.SRP2();
            break;
          case SZP0:  /* SZP0 */
            func_grp1.SZP0();
            break;
          case SZP1:  /* SZP1 */
            func_grp1.SZP1();
            break;
          case SZP2:  /* SZP2 */
            func_grp1.SZP2();
            break;
          case SZPS:  /* SZPS */
            func_grp1.SZPS();
            break;
          case SLOOP:  /* SLOOP */
            func_grp1.SLOOP();
            break;
          case RTG:  /* RTG */
            func_grp1.RTG();
            break;
          case RTHG:  /* RTHG */
            func_grp1.RTHG();
            break;
          case SMD:  /* SMD */
            func_grp1.SMD();
            break;
          case ELSE:  /* ELSE */
            func_grp1.ELSE();
            break;
          case JMPR:  /* JMPR */
            func_grp1.JMPR();
            break;
          case SCvTCi:  /* SCVTCI */
            func_grp1.SCVTCI();
            break;
          case SSwCi:  /* SSWCI */
            func_grp1.SSWCI();
            break;
          case SSW:  /* SSW */
            func_grp1.SSW();
            break;

          /* TTInstructionGroup2 */
          case DUP:  /* DUP */
            func_grp2.DUP();
            break;
          case POP:  /* POP */
            /* nothing :-) */
            break;
          case CLEAR:  /* CLEAR */
            func_grp2.CLEAR();
            break;
          case SWAP:  /* SWAP */
            func_grp2.SWAP();
            break;
          case DEPTH:  /* DEPTH */
            func_grp2.DEPTH();
            break;
          case CINDEX:  /* CINDEX */
            func_grp2.CINDEX();
            break;
          case MINDEX:  /* MINDEX */
            func_grp2.MINDEX();
            break;
          case AlignPTS:  /* ALIGNPTS */
            func_grp2.ALIGNPTS();
            break;
          case INS_0x28:  /* ???? */
            func_grp2.UNKNOWN();
            break;
          case UTP:  /* UTP */
            func_grp2.UTP();
            break;
          case LOOPCALL:  /* LOOPCALL */
            func_grp2.LOOPCALL();
            break;
          case CALL:  /* CALL */
            func_grp2.CALL();
            break;
          case FDEF:  /* FDEF */
            func_grp2.FDEF();
            break;
          case ENDF:  /* ENDF */
            func_grp2.ENDF();
            break;
          case MDAP_0:  /* MDAP */
          case MDAP_1:  /* MDAP */
            func_grp2.MDAP();
            break;

          /* TTInstructionGroup3 */
          case IUP_0:  /* IUP */
          case IUP_1:  /* IUP */
            func_grp3.IUP();
            break;
          case SHP_0:  /* SHP */
          case SHP_1:  /* SHP */
            func_grp3.SHP();
            break;
          case SHC_0:  /* SHC */
          case SHC_1:  /* SHC */
            func_grp3.SHC();
            break;
          case SHZ_0:  /* SHZ */
          case SHZ_1:  /* SHZ */
            func_grp3.SHZ();
            break;
          case SHPIX:  /* SHPIX */
            func_grp3.SHPIX();
            break;
          case IP:  /* IP    */
            func_grp3.IP();
            break;
          case MSIRP_0:  /* MSIRP */
          case MSIRP_1:  /* MSIRP */
            func_grp3.MSIRP();
            break;
          case AlignRP:  /* AlignRP */
            func_grp3.ALIGNRP();
            break;
          case RTDG:  /* RTDG */
            func_grp3.RTDG();
            break;
          case MIAP_0:  /* MIAP */
          case MIAP_1:  /* MIAP */
            func_grp3.MIAP();
            break;

          /* TTInstructionGroup4 */
          case NPushB:  /* NPUSHB */
            func_grp4.NPUSHB();
            break;
          case NPushW:  /* NPUSHW */
            func_grp4.NPUSHW();
            break;
          case WS:  /* WS */
            func_grp4.WS();
            break;
          //  Set_Invalid_Ref:
          //            error = FT_Error.ErrorTag.INTERP_INVALID_REFERENCE;
          //          break;
          case RS:  /* RS */
            func_grp4.RS();
            break;
          case WCvtP:  /* WCVTP */
            func_grp4.WCVTP();
            break;
          case RCvt:  /* RCVT */
            func_grp4.RCVT();
            break;
          case GC_0:  /* GC */
          case GC_1:  /* GC */
            func_grp4.GC();
            break;
          case SCFS:  /* SCFS */
            func_grp4.SCFS();
            break;
          case MD_0:  /* MD */
          case MD_1:  /* MD */
            func_grp4.MD();
            break;
          case MPPEM:  /* MPPEM */
            func_grp4.MPPEM();
            break;
          case MPS:  /* MPS */
            func_grp4.MPS();
            break;
          case FlipON:  /* FLIPON */
            func_grp4.FLIPON();
            break;
          case FlipOFF:  /* FLIPOFF */
            func_grp4.FLIPOFF();
            break;
          case DEBUG:  /* DEBUG */
            func_grp4.DEBUG();
            break;

          /* TTInstructionGroup5 */
          case LT:  /* LT */
            func_grp5.LT();
            break;
          case LTEQ:  /* LTEQ */
            func_grp5.LTEQ();
            break;
          case GT:  /* GT */
            func_grp5.GT();
            break;
          case GTEQ:  /* GTEQ */
            func_grp5.GTEQ();
            break;
          case EQ:  /* EQ */
            func_grp5.EQ();
            break;
          case NEQ:  /* NEQ */
            func_grp5.NEQ();
            break;
          case ODD:  /* ODD */
            func_grp5.ODD();
          case EVEN:  /* EVEN */
            func_grp5.EVEN();
            break;
          case IF:  /* IF */
            func_grp5.IF();
            break;
          case EIF:  /* EIF */
            /* do nothing */
            break;
          case AND:  /* AND */
            func_grp5.AND();
            break;
          case OR:  /* OR */
            func_grp5.OR();
            break;
          case NOT:  /* NOT */
            func_grp5.NOT();
            break;
          case DeltaP1:  /* DELTAP1 */
            func_grp5.DELTAP();
            break;
          case SDB:  /* SDB */
            func_grp5.SDB();
            break;
          case SDS:  /* SDS */
            func_grp5.SDS();
            break;

          /* TTInstructionGroup6 */
          case ADD:  /* ADD */
            func_grp6.ADD();
            break;
          case SUB:  /* SUB */
            func_grp6.SUB();
            break;
          case DIV:  /* DIV */
            func_grp6.DIV();
            break;
          case MUL:  /* MUL */
            func_grp6.MUL();
            break;
          case ABS:  /* ABS */
            func_grp6.ABS();
            break;
          case NEG:  /* NEG */
            func_grp6.NEG();
            break;
          case FLOOR:  /* FLOOR */
            func_grp6.FLOOR();
            break;
          case CEILING:  /* CEILING */
            func_grp6.CEILING();
            break;
          case ROUND_0:  /* ROUND */
          case ROUND_1:  /* ROUND */
          case ROUND_2:  /* ROUND */
          case ROUND_3:  /* ROUND */
            func_grp6.ROUND();
            break;
          case NROUND_0:  /* NROUND */
          case NROUND_1:  /* NROUND */
          case NROUND_2:  /* NRRUND */
          case NROUND_3:  /* NROUND */
            func_grp6.NROUND();
            break;

          /* TTInstructionGroup7 */
          case WCvtF:  /* WCVTF */
            func_grp7.WCVTF();
            break;
          case DeltaP2:  /* DELTAP2 */
          case DeltaP3:  /* DELTAP3 */
            func_grp5.DELTAP();
            break;
          case DeltaCn_0:  /* DELTAC0 */
          case DeltaCn_1:  /* DELTAC1 */
          case DeltaCn_2:  /* DELTAC2 */
            func_grp7.DELTAC();
            break;
          case SROUND:  /* SROUND */
            func_grp7.SROUND();
            break;
          case S45Round:  /* S45Round */
            func_grp7.S45ROUND();
            break;
          case JROT:  /* JROT */
            func_grp7.JROT();
            break;
          case JROF:  /* JROF */
            func_grp7.JROF();
            break;
          case ROFF:  /* ROFF */
            func_grp7.ROFF();
            break;
          case INS_0x7B:  /* ???? */
            func_grp2.UNKNOWN();
            break;
          case RUTG:  /* RUTG */
            func_grp7.RUTG();
            break;
          case RDTG:  /* RDTG */
            func_grp7.RDTG();
            break;
          case SANGW:  /* SANGW */
          case AA:  /* AA    */
            /* nothing - obsolete */
            break;

          /* TTInstructionGroup8 */
          case FlipPT:  /* FLIPPT */
            func_grp8.FLIPPT();
            break;
          case FlipRgON:  /* FLIPRGON */
            func_grp8.FLIPRGON();
            break;
          case FlipRgOFF:  /* FLIPRGOFF */
            func_grp8.FLIPRGOFF();
            break;
          case INS_0x83:  /* UNKNOWN */
          case INS_0x84:  /* UNKNOWN */
            func_grp2.UNKNOWN();
            break;
          case ScanCTRL:  /* SCANCTRL */
            func_grp8.SCANCTRL();
            break;
          case SDVPTL_0:  /* SDPVTL */
          case SDVPTL_1:  /* SDPVTL */
            func_grp8.SDPVTL();
            break;
          case GetINFO:  /* GETINFO */
            func_grp8.GETINFO();
            break;
          case IDEF:  /* IDEF */
            func_grp8.IDEF();
            break;
          case ROLL:  /* ROLL */
            func_grp8.ROLL();
            break;
          case MAX:  /* MAX */
            func_grp8.MAX();
            break;
          case MIN:  /* MIN */
            func_grp8.MIN();
            break;
          case ScanTYPE:  /* SCANTYPE */
            func_grp8.SCANTYPE();
            break;
          case InstCTRL:  /* INSTCTRL */
            func_grp8.INSTCTRL();
            break;
          case INS_0x8F:      /* 0x8F 143 */
            func_grp2.UNKNOWN();
            break;

          /* TTInstructionGroup9 */
          default:
            if (opcode.getVal() >= TTOpCode.OpCode.MIRP_00.getVal()) { /* 0xE0 224 */
              func_grp9.MIRP();
            } else {
              if (opcode.getVal() >= TTOpCode.OpCode.MDRP_00.getVal()) {    /* 0xC0 192 */
                func_grp9.MDRP();
              } else {
                if ( opcode.getVal() >= TTOpCode.OpCode.PushW_0.getVal()) {    /* 0xB8 184 */
                  func_grp9.PUSHW();
                } else {
                  if (opcode.getVal() >= TTOpCode.OpCode.PushB_0.getVal()) {    /* 0xB0 176 */
                    func_grp9.PUSHB();
                  } else {
                    func_grp2.UNKNOWN();
                  }
                }
              }
            }
        }
        boolean useSuiteLabel = false;
        if (error != FTError.ErrorTag.ERR_OK) {
          switch (error) {
              /* looking for redefined instructions */
            case INTERP_INVALID_OPCODE:
            {
              int defIdx = 0;
              TTDefRec def = IDefs[defIdx];
              int limit = defIdx + numIDefs;

              for (defIdx = 0; defIdx < limit; defIdx++) {
                def = IDefs[defIdx];
                if (def.active && opcode.getVal() == def.opc) {
                  TTCallRec callrec;

                  if (callTop >= callSize) {
                    error = FTError.ErrorTag.INTERP_INVALID_REFERENCE;
                    return LError();
                  }
                  callrec = callStack[callTop];
                  callrec.CallerRange = curRange.getVal();
                  callrec.CallerIP = IP + 1;
                  callrec.CurCount = 1;
                  callrec.CurRestart = def.start;
                  callrec.CurEnd = def.end;
                  if (this.TTGotoCodeRange(TTInterpTags.CodeRange.getTableTag(def.range), def.start) != FTError.ErrorTag.ERR_OK) {
                    return LError();
                  }
                  useSuiteLabel = true;
                  break;
                }
              }
            }
            if (!useSuiteLabel) {
              error = FTError.ErrorTag.INTERP_INVALID_OPCODE;
              return LError();
            }
            default:
              return LError();
          }
        }
        if (!useSuiteLabel) {
          top = new_top;
          if (step_ins) {
            IP += length;
          }
            /* increment instruction counter and check if we didn't */
            /* run this program for too long (e.g. infinite loops). */
          if (++ins_counter > MAX_RUNNABLE_OPCODES) {
            return FTError.ErrorTag.INTERP_EXECUTION_TOO_LONG;
          }
        }
//Debug(0, DBG_INTERP, TAG, String.format("ll4: %d %d", cur.IP, cur.codeSize));
//        showLoaderZone();
        if (IP >= codeSize) {
          if (callTop > 0) {
            error = FTError.ErrorTag.INTERP_CODE_OVERFLOW;
            return LError();
          } else {
            return FTError.ErrorTag.ERR_OK;
          }
        }
      }
    } while (!instruction_trap);
    return FTError.ErrorTag.ERR_OK;
  }

  private FTError.ErrorTag LErrorCodeOverflow() {
    error = FTError.ErrorTag.INTERP_CODE_OVERFLOW;
    return LError();
  }

  private FTError.ErrorTag LError() {
      /* If any errors have occurred, function tables may be broken. */
      /* Force a re-execution of `prep' and `fpgm' tables if no      */
      /* bytecode debugger is run.                                   */
    if (error != FTError.ErrorTag.ERR_OK && !instruction_trap) {
//        FT_TRACE1(( "  The interpreter returned error 0x%x\n", cur.error ));
//        exc.size.cvt_ready = false;
    }
    return error;
  }

}