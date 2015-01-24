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
  /*    TTOpcode                                                           */
  /*    all opcodes for the interpreter                                    */
  /* ===================================================================== */

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTOpCode extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTOpcode";

  /* =====================================================================
   *
   * Rounding mode constants.
   *
   * =====================================================================
   */
  public enum OpCode {
    SVTCA_y(0, TTUtil.Pack(0, 0), 1, "SVTCA y", "Set (F and P) Vectors to Coordinate Axis y"),
    SVTCA_x(1, TTUtil.Pack(0, 0), 1, "SVTCA x", "Set (F and P) Vectors to Coordinate Axis x"),
    SPvTCA_y(2, TTUtil.Pack(0, 0), 1, "SPvTCA y", "Set PVector to Coordinate Axis y"),
    SPvTCA_x(3, TTUtil.Pack(0, 0), 1, "SPvTCA x", "Set PVector to Coordinate Axis x"),
    SFvTCA_y(4, TTUtil.Pack(0, 0), 1, "SFvTCA y", "Set FVector to Coordinate Axis y"),
    SFvTCA_x(5, TTUtil.Pack(0, 0), 1, "SFvTCA x", "Set FVector to Coordinate Axis x"),
    SPvTL_OR_IF(6, TTUtil.Pack(2, 0), 1, "SPvTL ||", "Set PVector To Line ||"),
    SPvTL_PLUS(7, TTUtil.Pack(2, 0), 1, "SPvTL +", "Set PVector To Line +"),
    SFvTL_OR_IF(8, TTUtil.Pack(2, 0), 1, "SFvTL ||", "Set FVector To Line ||"),
    SFvTL_PLUS(9, TTUtil.Pack(2, 0), 1, "SFvTL +", "Set FVector To Line +"),
    SPvFS(10, TTUtil.Pack(2, 0), 1, "SPvFS", "Set PVector From Stack"),
    SFvFS(11, TTUtil.Pack(2, 0), 1, "SFvFS", "Set FVector From Stack"),
    GPV(12, TTUtil.Pack(0, 2), 1, "GPV", "Get Projection Vector"),
    GFV(13, TTUtil.Pack(0, 2), 1, "GFV", "Get Freedom Vector"),
    SFvTPv(14, TTUtil.Pack(0, 0), 1, "SFvTPv","Set FVector To PVector"),
    ISECT(15, TTUtil.Pack(5, 0), 1, "ISECT", "moves point to InterSECTion"),

    SRP0(16, TTUtil.Pack(1, 0), 1, "SRP0", "Set Reference Point 0"),
    SRP1(17, TTUtil.Pack(1, 0), 1, "SRP1", "Set Reference Point 1"),
    SRP2(18, TTUtil.Pack(1, 0), 1, "SRP2", "Set Reference Point 2"),
    SZP0(19, TTUtil.Pack(1, 0), 1, "SZP0", "Set Zone Pointer 0"),
    SZP1(20, TTUtil.Pack(1, 0), 1, "SZP1", "Set Zone Pointer 1"),
    SZP2(21, TTUtil.Pack(1, 0), 1, "SZP2", "Set Zone Pointer 2"),
    SZPS(22, TTUtil.Pack(1, 0), 1, "SZPS", "Set Zone PointerS"),
    SLOOP(23, TTUtil.Pack(2, 0), 1, "SLOOP", "Set LOOP variable "),
    RTG(24, TTUtil.Pack(0, 0), 1, "RTG", "Round To Grid"),
    RTHG(25, TTUtil.Pack(0, 0), 1, "RTHG", "Round To Half Grid"),
    SMD(26, TTUtil.Pack(1, 0), 1, "SMD", "Set Minimum Distance"),
    ELSE(27, TTUtil.Pack(0, 0), 1, "ELSE", "ELSE"),
    JMPR(28, TTUtil.Pack(1, 0), 1, "JMPR", "JuMP Relative"),
    SCvTCi(29, TTUtil.Pack(1, 0), 1, "SCvTCi", "Set Control Value Table Cut In"),
    SSwCi(30, TTUtil.Pack(1, 0), 1, "SSwCi", "Set Single Width Cut In"),
    SSW(31, TTUtil.Pack(1, 0), 1, "SSW", "Set Single Width"),

    DUP(32, TTUtil.Pack(1, 2), 1, "DUP", "DUPlicate the top stack's element"),
    POP(33, TTUtil.Pack(1, 0), 1, "POP", "POP the stack's top element"),
    CLEAR(34, TTUtil.Pack(0, 0), 1, "CLEAR", "CLEAR the entire stack"),
    SWAP(35, TTUtil.Pack(2, 2), 1, "SWAP", "SWAP the stack's top two elements"),
    DEPTH(36, TTUtil.Pack(0, 1), 1, "DEPTH", "return the stack DEPTH"),
    CINDEX(37, TTUtil.Pack(1, 1), 1, "CINDEX", "Copy INDEXed element"),
    MINDEX(38, TTUtil.Pack(1, 0), 1, "MINDEX", "Move INDEXed element"),
    AlignPTS(39, TTUtil.Pack(2, 0), 1, "AlignPTS", "ALIGN PoinTS"),
    INS_0x28(40, TTUtil.Pack(0, 0), 1, "INS_$28", "UNKNOWN instruction 0x28"),
    UTP(41, TTUtil.Pack(1, 0), 1, "UTP", "UnTouch Point"),
    LOOPCALL(42, TTUtil.Pack(2, 0), 1, "LOOPCALL", "LOOP and CALL function"),
    CALL(43, TTUtil.Pack(1, 0), 1, "CALL", "CALL function"),
    FDEF(44, TTUtil.Pack(1, 0), 1, "FDEF", "Function DEFinition"),
    ENDF(45, TTUtil.Pack(0, 0), 1, "ENDF", "END Function definition"),
    MDAP_0(46, TTUtil.Pack(1, 0), 1, "MDAP[0]", "Move Direct Absolute Point 0"),
    MDAP_1(47, TTUtil.Pack(1, 0), 1, "MDAP[1]", "Move Direct Absolute Point 1"),

    IUP_0(48, TTUtil.Pack(0, 0), 1, "IUP[0]", "Interpolate Untouched Points 0"),
    IUP_1(49, TTUtil.Pack(0, 0), 1, "IUP[1]", "Interpolate Untouched Points 1"),
    SHP_0(50, TTUtil.Pack(0, 0), 1, "SHP[0]", "SHift Point by the last point 0"),
    SHP_1(51, TTUtil.Pack(0, 0), 1, "SHP[1]", "SHift Point by the last point 1"),
    SHC_0(52, TTUtil.Pack(1, 0), 1, "SHC[0]", "SHift Contour 0"),
    SHC_1(53, TTUtil.Pack(1, 0), 1, "SHC[1]", "SHift Contour 1"),
    SHZ_0(54, TTUtil.Pack(1, 0), 1, "SHZ[0]", "SHift Zone 0"),
    SHZ_1(55, TTUtil.Pack(1, 0), 1, "SHZ[1]", "SHift Zone 1"),
    SHPIX(56, TTUtil.Pack(1, 0), 1, "SHPIX", "SHift points by a PIXel amount"),
    IP(57, TTUtil.Pack(0, 0), 1, "IP", "Interpolate Point"),
    MSIRP_0(58, TTUtil.Pack(2, 0), 1, "MSIRP[0]", "Move Stack Indirect Relative Position 0"),
    MSIRP_1(59, TTUtil.Pack(2, 0), 1, "MSIRP[1]", "Move Stack Indirect Relative Position 1"),
    AlignRP(60, TTUtil.Pack(0, 0), 1, "AlignRP", "ALIGN Relative Point"),
    RTDG(61, TTUtil.Pack(0, 0), 1, "RTDG", "Round To Double Grid"),
    MIAP_0(62, TTUtil.Pack(2, 0), 1, "MIAP[0]", "Move Indirect Absolute Point 0"),
    MIAP_1(63, TTUtil.Pack(2, 0), 1, "MIAP[1]", "Move Indirect Absolute Point 1"),

    NPushB(64, TTUtil.Pack(0, 0), -1, "NPushB", "PUSH N Bytes"),
    NPushW(65, TTUtil.Pack(0, 0), -2, "NPushW", "PUSH N Words"),
    WS(66, TTUtil.Pack(2, 0), 1, "WS", "Write Store"),
    RS(67, TTUtil.Pack(1, 1), 1, "RS", "Read Store"),
    WCvtP(68, TTUtil.Pack(2, 0), 1, "WCvtP", "Write CVT in Pixel units"),
    RCvt(69, TTUtil.Pack(1, 1), 1, "RCvt", "Read CVT"),
    GC_0(70, TTUtil.Pack(1, 1), 1, "GC[0]", "Get Coordinate projected onto 0"),
    GC_1(71, TTUtil.Pack(1, 1), 1, "GC[1]", "Get Coordinate projected onto 1"),
    SCFS(72, TTUtil.Pack(2, 0), 1, "SCFS", "Set Coordinate From Stack"),
    MD_0(73, TTUtil.Pack(2, 1), 1, "MD[0]", "Measure Distance 0"),
    MD_1(74, TTUtil.Pack(2, 1), 1, "MD[1]", "Measure Distance 1"),
    MPPEM(75, TTUtil.Pack(0, 1), 1, "MPPEM", "Measure Pixel Per EM"),
    MPS(76, TTUtil.Pack(0, 1), 1, "MPS", "Measure Point Size"),
    FlipON(77, TTUtil.Pack(0, 0), 1, "FlipON", "Set auto-FLIP to ON"),
    FlipOFF(78, TTUtil.Pack(0, 0), 1, "FlipOFF", "Set auto-FLIP to OFF"),
    DEBUG(79, TTUtil.Pack(1, 0), 1, "DEBUG", "DEBUG.  Unsupported."),

    LT(80, TTUtil.Pack(2, 1), 1, "LT", "Less Than"),
    LTEQ(81, TTUtil.Pack(2, 1), 1, "LTEQ", "Less Than or EQual"),
    GT(82, TTUtil.Pack(2, 1), 1, "GT", "Greater Than"),
    GTEQ(83, TTUtil.Pack(2, 1), 1, "GTEQ", "Greater Than or EQual"),
    EQ(84, TTUtil.Pack(2, 1), 1, "EQ", "EQual"),
    NEQ(85, TTUtil.Pack(2, 1), 1, "NEQ", "Not EQual"),
    ODD(86, TTUtil.Pack(1, 1), 1, "ODD", "Is ODD"),
    EVEN(87, TTUtil.Pack(1, 1), 1, "EVEN", "Is EVEN"),
    IF(88, TTUtil.Pack(1, 0), 1, "IF", "IF test"),
    EIF(89, TTUtil.Pack(0, 0), 1, "EIF", "EIF do nothing"),
    AND(90, TTUtil.Pack(2, 1), 1, "AND", "logical AND"),
    OR(91, TTUtil.Pack(2, 1), 1, "OR", "logical OR"),
    NOT(92, TTUtil.Pack(1, 1), 1, "NOT", "logical NOT"),
    DeltaP1(93, TTUtil.Pack(1, 0), 1, "DeltaP1", "DELTA exceptions P1"),
    SDB(94, TTUtil.Pack(1, 0), 1, "SDB", "Set Delta Base"),
    SDS(95, TTUtil.Pack(1, 0), 1, "SDS", "Set Delta Shift"),

    ADD(96, TTUtil.Pack(2, 1), 1, "ADD", "ADD"),
    SUB(97, TTUtil.Pack(2, 1), 1, "SUB", "SUBtract"),
    DIV(98, TTUtil.Pack(2, 2), 1, "DIV", "DIVide"),
    MUL(99, TTUtil.Pack(2, 1), 1, "MUL", "MULtiply"),
    ABS(100, TTUtil.Pack(1, 1), 1, "ABS", "ABSolute value"),
    NEG(101, TTUtil.Pack(1, 1), 1, "NEG", "NEGate"),
    FLOOR(102, TTUtil.Pack(1, 1), 1, "FLOOR", "FLOOR"),
    CEILING(103, TTUtil.Pack(1, 1), 1, "CEILING", "CEILING"),
    ROUND_0(104, TTUtil.Pack(1, 1), 1, "ROUND[0]", "ROUND value 0"),
    ROUND_1(105, TTUtil.Pack(1, 1), 1, "ROUND[1]", "ROUND value 1"),
    ROUND_2(106, TTUtil.Pack(1, 1), 1, "ROUND[2]", "ROUND value 2"),
    ROUND_3(107, TTUtil.Pack(1, 1), 1, "ROUND[3]", "ROUND value 3"),
    NROUND_0(108, TTUtil.Pack(1, 1), 1, "NROUND[0]", "No ROUNDing of value 0"),
    NROUND_1(109, TTUtil.Pack(1, 1), 1, "NROUND[1]", "No ROUNDing of value 1"),
    NROUND_2(110, TTUtil.Pack(1, 1), 1, "NROUND[2]", "No ROUNDing of value 2"),
    NROUND_3(111, TTUtil.Pack(1, 1), 1, "NROUND[3]", "No ROUNDing of value 3"),

    WCvtF(112, TTUtil.Pack(2, 0), 1, "WCvtF", "Write CVT in Funits"),
    DeltaP2(113, TTUtil.Pack(1, 0), 1, "DeltaP2", "DELTA exceptions P2"),
    DeltaP3(114, TTUtil.Pack(1, 0), 1, "DeltaP3", "DELTA exceptions P3"),
    DeltaCn_0(115, TTUtil.Pack(1, 0), 1, "DeltaCn[0]", "DELTA exceptions C1"),
    DeltaCn_1(116, TTUtil.Pack(1, 0), 1, "DeltaCn[1]", "DELTA exceptions C2"),
    DeltaCn_2(117, TTUtil.Pack(1, 0), 1, "DeltaCn[2]", "DELTA exceptions C3"),
    SROUND(118, TTUtil.Pack(1, 0), 1, "SROUND", "Super ROUND"),
    S45Round(119, TTUtil.Pack(1, 0), 1, "S45Round", "Super ROUND 45 degrees"),
    JROT(120, TTUtil.Pack(2, 0), 1, "JROT", "Jump Relative On True"),
    JROF(121, TTUtil.Pack(2, 0), 1, "JROF", "Jump Relative On False"),
    ROFF(122, TTUtil.Pack(0, 0), 1, "ROFF", "Round OFF"),
    INS_0x7B(123, TTUtil.Pack(0, 0), 1, "INS_$7B", "UNKNOWN instruction 0x7B"),
    RUTG(124, TTUtil.Pack(0, 0), 1, "RUTG", "Round Up To Grid"),
    RDTG(125, TTUtil.Pack(0, 0), 1, "RDTG", "Round Down To Grid"),
    SANGW(126, TTUtil.Pack(1, 0), 1, "SANGW", "SANGW nothing to do"),
    AA(127, TTUtil.Pack(1, 0), 1, "AA", "AA nothing to do"),

    FlipPT(128, TTUtil.Pack(0, 0), 1, "FlipPT", "FLIP PoinT"),
    FlipRgON(129, TTUtil.Pack(2, 0), 1, "FlipRgON", "FLIP RanGe ON"),
    FlipRgOFF(130, TTUtil.Pack(2, 0), 1, "FlipRgOFF", "FLIP RanGe OFF"),
    INS_0x83(131, TTUtil.Pack(0, 0), 1, "INS_$83", "UNKNOWN instruction 0x83"),
    INS_0x84(132, TTUtil.Pack(0, 0), 1, "INS_$84", "UNKNOWN instruction 0x84"),
    ScanCTRL(133, TTUtil.Pack(1, 0), 1, "ScanCTRL", "SCAN ConTRoL"),
    SDVPTL_0(134, TTUtil.Pack(2, 0), 1, "SDVPTL[0]", "Set Dual PVector to Line 0"),
    SDVPTL_1(135, TTUtil.Pack(2, 0), 1, "SDVPTL[1]", "Set Dual PVector to Line 1"),
    GetINFO(136, TTUtil.Pack(1, 1), 1, "GetINFO", "GET INFOrmation"),
    IDEF(137, TTUtil.Pack(1, 0), 1, "IDEF", "Instruction DEFinition"),
    ROLL(138, TTUtil.Pack(3, 3), 1, "ROLL", "ROLL top three elements"),
    MAX(139, TTUtil.Pack(2, 1), 1, "MAX", "MAXimum"),
    MIN(140, TTUtil.Pack(2, 1), 1, "MIN", "MINimum"),
    ScanTYPE(141, TTUtil.Pack(1, 0), 1, "ScanTYPE", "SCAN TYPE"),
    InstCTRL(142, TTUtil.Pack(2, 0), 1, "InstCTRL", "INSTruction ConTRoL"),
    INS_0x8F(143, TTUtil.Pack(0, 0), 1, "INS_$8F", "UNKNOWN instruction 0x83"),

    INS_$90(144, TTUtil.Pack(0, 0), 1, "INS_$90", "UNKNOWN instruction 0x90"),
    INS_$91(145, TTUtil.Pack(0, 0), 1, "INS_$91", "UNKNOWN instruction 0x91"),
    INS_$92(146, TTUtil.Pack(0, 0), 1, "INS_$92", "UNKNOWN instruction 0x92"),
    INS_$93(147, TTUtil.Pack(0, 0), 1, "INS_$93", "UNKNOWN instruction 0x93"),
    INS_$94(148, TTUtil.Pack(0, 0), 1, "INS_$94", "UNKNOWN instruction 0x94"),
    INS_$95(149, TTUtil.Pack(0, 0), 1, "INS_$95", "UNKNOWN instruction 0x95"),
    INS_$96(150, TTUtil.Pack(0, 0), 1, "INS_$96", "UNKNOWN instruction 0x96"),
    INS_$97(151, TTUtil.Pack(0, 0), 1, "INS_$97", "UNKNOWN instruction 0x97"),
    INS_$98(152, TTUtil.Pack(0, 0), 1, "INS_$98", "UNKNOWN instruction 0x98"),
    INS_$99(153, TTUtil.Pack(0, 0), 1, "INS_$99", "UNKNOWN instruction 0x99"),
    INS_$9A(154, TTUtil.Pack(0, 0), 1, "INS_$9A", "UNKNOWN instruction 0x9A"),
    INS_$9B(155, TTUtil.Pack(0, 0), 1, "INS_$9B", "UNKNOWN instruction 0x9B"),
    INS_$9C(156, TTUtil.Pack(0, 0), 1, "INS_$9C", "UNKNOWN instruction 0x9C"),
    INS_$9D(157, TTUtil.Pack(0, 0), 1, "INS_$9D", "UNKNOWN instruction 0x9D"),
    INS_$9E(158, TTUtil.Pack(0, 0), 1, "INS_$9E", "UNKNOWN instruction 0x9E"),
    INS_$9F(159, TTUtil.Pack(0, 0), 1, "INS_$9F", "UNKNOWN instruction 0x9F"),

    INS_$A0(160, TTUtil.Pack(0, 0), 1, "INS_$A0", "UNKNOWN instruction 0xA0"),
    INS_$A1(161, TTUtil.Pack(0, 0), 1, "INS_$A1", "UNKNOWN instruction 0xA1"),
    INS_$A2(162, TTUtil.Pack(0, 0), 1, "INS_$A2", "UNKNOWN instruction 0xA2"),
    INS_$A3(163, TTUtil.Pack(0, 0), 1, "INS_$A3", "UNKNOWN instruction 0xA3"),
    INS_$A4(164, TTUtil.Pack(0, 0), 1, "INS_$A4", "UNKNOWN instruction 0xA4"),
    INS_$A5(165, TTUtil.Pack(0, 0), 1, "INS_$A5", "UNKNOWN instruction 0xA5"),
    INS_$A6(166, TTUtil.Pack(0, 0), 1, "INS_$A6", "UNKNOWN instruction 0xA6"),
    INS_$A7(167, TTUtil.Pack(0, 0), 1, "INS_$A7", "UNKNOWN instruction 0xA7"),
    INS_$A8(168, TTUtil.Pack(0, 0), 1, "INS_$A8", "UNKNOWN instruction 0xA8"),
    INS_$A9(169, TTUtil.Pack(0, 0), 1, "INS_$A9", "UNKNOWN instruction 0xA9"),
    INS_$AA(170, TTUtil.Pack(0, 0), 1, "INS_$AA", "UNKNOWN instruction 0xAA"),
    INS_$AB(171, TTUtil.Pack(0, 0), 1, "INS_$AB", "UNKNOWN instruction 0xAB"),
    INS_$AC(172, TTUtil.Pack(0, 0), 1, "INS_$AC", "UNKNOWN instruction 0xAC"),
    INS_$AD(173, TTUtil.Pack(0, 0), 1, "INS_$AD", "UNKNOWN instruction 0xAD"),
    INS_$AE(174, TTUtil.Pack(0, 0), 1, "INS_$AE", "UNKNOWN instruction 0xAE"),
    INS_$AF(175, TTUtil.Pack(0, 0), 1, "INS_$AF", "UNKNOWN instruction 0xAF"),

    PushB_0(176, TTUtil.Pack(0, 1), 2, "PushB[0]", "PUSH Bytes 0"),
    PushB_1(177, TTUtil.Pack(0, 2), 3, "PushB[1]", "PUSH Bytes 1"),
    PushB_2(178, TTUtil.Pack(0, 3), 4, "PushB[2]", "PUSH Bytes 2"),
    PushB_3(179, TTUtil.Pack(0, 4), 5, "PushB[3]", "PUSH Bytes 3"),
    PushB_4(180, TTUtil.Pack(0, 5), 6, "PushB[4]", "PUSH Bytes 4"),
    PushB_5(181, TTUtil.Pack(0, 6), 7, "PushB[5]", "PUSH Bytes 5"),
    PushB_6(182, TTUtil.Pack(0, 7), 8, "PushB[6]", "PUSH Bytes 6"),
    PushB_7(183, TTUtil.Pack(0, 8), 9, "PushB[7]", "PUSH Bytes 7"),
    PushW_0(184, TTUtil.Pack(0, 1), 3, "PushW[0]", "PUSH Words 0"),
    PushW_1(185, TTUtil.Pack(0, 2), 5, "PushW[1]", "PUSH Words 1"),
    PushW_2(186, TTUtil.Pack(0, 3), 7, "PushW[2]", "PUSH Words 2"),
    PushW_3(187, TTUtil.Pack(0, 4), 9, "PushW[3]", "PUSH Words 3"),
    PushW_4(188, TTUtil.Pack(0, 5), 11, "PushW[4]", "PUSH Words 4"),
    PushW_5(189, TTUtil.Pack(0, 6), 13, "PushW[5]", "PUSH Words 5"),
    PushW_6(190, TTUtil.Pack(0, 7), 15, "PushW[6]", "PUSH Words 6"),
    PushW_7(191, TTUtil.Pack(0, 8), 17, "PushW[7]", "PUSH Words 7"),

    MDRP_00(192, TTUtil.Pack(1, 0), 1, "MDRP[00]", "Move Direct Relative Point 00"),
    MDRP_01(193, TTUtil.Pack(1, 0), 1, "MDRP[01]", "Move Direct Relative Point 01"),
    MDRP_02(194, TTUtil.Pack(1, 0), 1, "MDRP[02]", "Move Direct Relative Point 02"),
    MDRP_03(195, TTUtil.Pack(1, 0), 1, "MDRP[03]", "Move Direct Relative Point 03"),
    MDRP_04(196, TTUtil.Pack(1, 0), 1, "MDRP[04]", "Move Direct Relative Point 04"),
    MDRP_05(197, TTUtil.Pack(1, 0), 1, "MDRP[05]", "Move Direct Relative Point 05"),
    MDRP_06(198, TTUtil.Pack(1, 0), 1, "MDRP[06]", "Move Direct Relative Point 06"),
    MDRP_07(199, TTUtil.Pack(1, 0), 1, "MDRP[07]", "Move Direct Relative Point 07"),
    MDRP_08(200, TTUtil.Pack(1, 0), 1, "MDRP[08]", "Move Direct Relative Point 08"),
    MDRP_09(201, TTUtil.Pack(1, 0), 1, "MDRP[09]", "Move Direct Relative Point 09"),
    MDRP_10(202, TTUtil.Pack(1, 0), 1, "MDRP[10]", "Move Direct Relative Point 10"),
    MDRP_11(203, TTUtil.Pack(1, 0), 1, "MDRP[11]", "Move Direct Relative Point 11"),
    MDRP_12(204, TTUtil.Pack(1, 0), 1, "MDRP[12]", "Move Direct Relative Point 12"),
    MDRP_13(205, TTUtil.Pack(1, 0), 1, "MDRP[13]", "Move Direct Relative Point 13"),
    MDRP_14(206, TTUtil.Pack(1, 0), 1, "MDRP[14]", "Move Direct Relative Point 14"),
    MDRP_15(207, TTUtil.Pack(1, 0), 1, "MDRP[15]", "Move Direct Relative Point 15"),

    MDRP_16(208, TTUtil.Pack(1, 0), 1, "MDRP[16]", "Move Direct Relative Point 16"),
    MDRP_17(209, TTUtil.Pack(1, 0), 1, "MDRP[17]", "Move Direct Relative Point 17"),
    MDRP_18(210, TTUtil.Pack(1, 0), 1, "MDRP[18]", "Move Direct Relative Point 18"),
    MDRP_19(211, TTUtil.Pack(1, 0), 1, "MDRP[19]", "Move Direct Relative Point 19"),
    MDRP_20(212, TTUtil.Pack(1, 0), 1, "MDRP[20]", "Move Direct Relative Point 20"),
    MDRP_21(213, TTUtil.Pack(1, 0), 1, "MDRP[21]", "Move Direct Relative Point 21"),
    MDRP_22(214, TTUtil.Pack(1, 0), 1, "MDRP[22]", "Move Direct Relative Point 22"),
    MDRP_23(125, TTUtil.Pack(1, 0), 1, "MDRP[23]", "Move Direct Relative Point 23"),
    MDRP_24(216, TTUtil.Pack(1, 0), 1, "MDRP[24]", "Move Direct Relative Point 24"),
    MDRP_25(217, TTUtil.Pack(1, 0), 1, "MDRP[25]", "Move Direct Relative Point 25"),
    MDRP_26(218, TTUtil.Pack(1, 0), 1, "MDRP[26]", "Move Direct Relative Point 26"),
    MDRP_27(219, TTUtil.Pack(1, 0), 1, "MDRP[27]", "Move Direct Relative Point 27"),
    MDRP_28(220, TTUtil.Pack(1, 0), 1, "MDRP[28]", "Move Direct Relative Point 28"),
    MDRP_29(221, TTUtil.Pack(1, 0), 1, "MDRP[29]", "Move Direct Relative Point 29"),
    MDRP_30(222, TTUtil.Pack(1, 0), 1, "MDRP[30]", "Move Direct Relative Point 30"),
    MDRP_31(223, TTUtil.Pack(1, 0), 1, "MDRP[31]", "Move Direct Relative Point 31"),

    MIRP_00(224, TTUtil.Pack(2, 0), 1, "MIRP[00]", "Move Indirect Relative Point 00"),
    MIRP_01(225, TTUtil.Pack(2, 0), 1, "MIRP[01]", "Move Indirect Relative Point 01"),
    MIRP_02(226, TTUtil.Pack(2, 0), 1, "MIRP[02]", "Move Indirect Relative Point 02"),
    MIRP_03(227, TTUtil.Pack(2, 0), 1, "MIRP[03]", "Move Indirect Relative Point 03"),
    MIRP_04(228, TTUtil.Pack(2, 0), 1, "MIRP[04]", "Move Indirect Relative Point 04"),
    MIRP_05(229, TTUtil.Pack(2, 0), 1, "MIRP[05]", "Move Indirect Relative Point 05"),
    MIRP_06(230, TTUtil.Pack(2, 0), 1, "MIRP[06]", "Move Indirect Relative Point 06"),
    MIRP_07(231, TTUtil.Pack(2, 0), 1, "MIRP[07]", "Move Indirect Relative Point 07"),
    MIRP_08(232, TTUtil.Pack(2, 0), 1, "MIRP[08]", "Move Indirect Relative Point 08"),
    MIRP_09(233, TTUtil.Pack(2, 0), 1, "MIRP[09]", "Move Indirect Relative Point 09"),
    MIRP_10(234, TTUtil.Pack(2, 0), 1, "MIRP[10]", "Move Indirect Relative Point 10"),
    MIRP_11(235, TTUtil.Pack(2, 0), 1, "MIRP[11]", "Move Indirect Relative Point 11"),
    MIRP_12(236, TTUtil.Pack(2, 0), 1, "MIRP[12]", "Move Indirect Relative Point 12"),
    MIRP_13(237, TTUtil.Pack(2, 0), 1, "MIRP[13]", "Move Indirect Relative Point 13"),
    MIRP_14(238, TTUtil.Pack(2, 0), 1, "MIRP[14]", "Move Indirect Relative Point 14"),
    MIRP_15(239, TTUtil.Pack(2, 0), 1, "MIRP[15]", "Move Indirect Relative Point 15"),

    MIRP_16(240, TTUtil.Pack(2, 0), 1, "MIRP[16]", "Move Indirect Relative Point 16"),
    MIRP_17(241, TTUtil.Pack(2, 0), 1, "MIRP[17]", "Move Indirect Relative Point 17"),
    MIRP_18(242, TTUtil.Pack(2, 0), 1, "MIRP[18]", "Move Indirect Relative Point 18"),
    MIRP_19(243, TTUtil.Pack(2, 0), 1, "MIRP[19]", "Move Indirect Relative Point 19"),
    MIRP_20(244, TTUtil.Pack(2, 0), 1, "MIRP[20]", "Move Indirect Relative Point 20"),
    MIRP_21(245, TTUtil.Pack(2, 0), 1, "MIRP[21]", "Move Indirect Relative Point 21"),
    MIRP_22(246, TTUtil.Pack(2, 0), 1, "MIRP[22]", "Move Indirect Relative Point 22"),
    MIRP_23(247, TTUtil.Pack(2, 0), 1, "MIRP[23]", "Move Indirect Relative Point 23"),
    MIRP_24(248, TTUtil.Pack(2, 0), 1, "MIRP[24]", "Move Indirect Relative Point 24"),
    MIRP_25(249, TTUtil.Pack(2, 0), 1, "MIRP[25]", "Move Indirect Relative Point 25"),
    MIRP_26(250, TTUtil.Pack(2, 0), 1, "MIRP[26]", "Move Indirect Relative Point 26"),
    MIRP_27(251, TTUtil.Pack(2, 0), 1, "MIRP[27]", "Move Indirect Relative Point 27"),
    MIRP_28(252, TTUtil.Pack(2, 0), 1, "MIRP[28]", "Move Indirect Relative Point 28"),
    MIRP_29(253, TTUtil.Pack(2, 0), 1, "MIRP[29]", "Move Indirect Relative Point 29"),
    MIRP_30(254, TTUtil.Pack(2, 0), 1, "MIRP[30]", "Move Indirect Relative Point 30"),
    MIRP_31(255, TTUtil.Pack(2, 0), 1, "MIRP[31]", "Move Indirect Relative Point 31");

    private int val;
    private byte push_count;
    private int opcode_length;
    private String str;
    private String description;
    private static SparseArray<OpCode> tagToOpCodeMapping;
    public static OpCode getTableTag(int i) {
      if (tagToOpCodeMapping == null) {
        initMapping();
      }
      return tagToOpCodeMapping.get(i);
    }
    private static void initMapping() {
      tagToOpCodeMapping = new SparseArray<OpCode>();
      for (OpCode t : values()) {
        tagToOpCodeMapping.put(t.val, t);
      }
    }
    private OpCode(int val, byte push_count, int opcode_length, String str, String description) {
      if (val == 64) {
        Debug(0, DebugTag.DBG_INTERP, TAG, "Opcode64! "+val+" "+push_count+" "+opcode_length+" "+str);
      }
      this.val = val;
      this.push_count = push_count;
      this.opcode_length = opcode_length;
      this.str = str;
      this.description = description;
    }
    public int getOpCodeLength() {
      if (val == 64) {
        Debug(0, DebugTag.DBG_INTERP, TAG, "Opcode64 len! "+val+" "+push_count+" "+opcode_length+" "+str);
      }
      return opcode_length;
    }
    public int getVal() {
      return val;
    }
    public int getPushCount() {
      return push_count;
    }
    public String toString() {
      return str + ": " + description;
    }
  }

  /* ==================== TTOpcode ================================== */
  public TTOpCode()
  {
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
    return str.toString();
  }

}
