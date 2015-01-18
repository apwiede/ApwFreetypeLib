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
    SVTCA_y(0, TTUtil.Pack(0, 0), 1, "SVTCA y"),
    SVTCA_x(1, TTUtil.Pack(0, 0), 1, "SVTCA x"),
    SPvTCA_y(2, TTUtil.Pack(0, 0), 1, "SPvTCA y"),
    SPvTCA_x(3, TTUtil.Pack(0, 0), 1, "SPvTCA x"),
    SFvTCA_y(4, TTUtil.Pack(0, 0), 1, "SFvTCA y"),
    SFvTCA_x(5, TTUtil.Pack(0, 0), 1, "SFvTCA x"),
    SPvTL_OR_IF(6, TTUtil.Pack(2, 0), 1, "SPvTL ||"),
    SPvTL_PLUS(7, TTUtil.Pack(2, 0), 1, "SPvTL +"),
    SFvTL_OR_IF(8, TTUtil.Pack(2, 0), 1, "SFvTL ||"),
    SFvTL_PLUS(9, TTUtil.Pack(2, 0), 1, "SFvTL +"),
    SPvFS(10, TTUtil.Pack(2, 0), 1, "SPvFS"),
    SFvFS(11, TTUtil.Pack(2, 0), 1, "SFvFS"),
    GPV(12, TTUtil.Pack(0, 2), 1, "GPV"),
    GFV(13, TTUtil.Pack(0, 2), 1, "GFV"),
    SFvTPv(14, TTUtil.Pack(0, 0), 1, "SFvTPv"),
    ISECT(15, TTUtil.Pack(5, 0), 1, "ISECT"),

    SRP0(16, TTUtil.Pack(1, 0), 1, "SRP0"),
    SRP1(17, TTUtil.Pack(1, 0), 1, "SRP1"),
    SRP2(18, TTUtil.Pack(1, 0), 1, "SRP2"),
    SZP0(19, TTUtil.Pack(1, 0), 1, "SZP0"),
    SZP1(20, TTUtil.Pack(1, 0), 1, "SZP1"),
    SZP2(21, TTUtil.Pack(1, 0), 1, "SZP2"),
    SZPS(22, TTUtil.Pack(1, 0), 1, "SZPS"),
    SLOOP(23, TTUtil.Pack(2, 0), 1, "SLOOP"),
    RTG(24, TTUtil.Pack(0, 0), 1, "RTG"),
    RTHG(25, TTUtil.Pack(0, 0), 1, "RTHG"),
    SMD(26, TTUtil.Pack(1, 0), 1, "SMD"),
    ELSE(27, TTUtil.Pack(0, 0), 1, "ELSE"),
    JMPR(28, TTUtil.Pack(1, 0), 1, "JMPR"),
    SCvTCi(29, TTUtil.Pack(1, 0), 1, "SCvTCi"),
    SSwCi(30, TTUtil.Pack(1, 0), 1, "SSwCi"),
    SSW(31, TTUtil.Pack(1, 0), 1, "SSW"),

    DUP(32, TTUtil.Pack(1, 2), 1, "DUP"),
    POP(33, TTUtil.Pack(1, 0), 1, "POP"),
    CLEAR(34, TTUtil.Pack(0, 0), 1, "CLEAR"),
    SWAP(35, TTUtil.Pack(2, 2), 1, "SWAP"),
    DEPTH(36, TTUtil.Pack(0, 1), 1, "DEPTH"),
    CINDEX(37, TTUtil.Pack(1, 1), 1, "CINDEX"),
    MINDEX(38, TTUtil.Pack(1, 0), 1, "MINDEX"),
    AlignPTS(39, TTUtil.Pack(2, 0), 1, "AlignPTS"),
    INS_0x28(40, TTUtil.Pack(0, 0), 1, "INS_$28"),
    UTP(41, TTUtil.Pack(1, 0), 1, "UTP"),
    LOOPCALL(42, TTUtil.Pack(2, 0), 1, "LOOPCALL"),
    CALL(43, TTUtil.Pack(1, 0), 1, "CALL"),
    FDEF(44, TTUtil.Pack(1, 0), 1, "FDEF"),
    ENDF(45, TTUtil.Pack(0, 0), 1, "ENDF"),
    MDAP_0(46, TTUtil.Pack(1, 0), 1, "MDAP[0]"),
    MDAP_1(47, TTUtil.Pack(1, 0), 1, "MDAP[1]"),

    IUP_0(48, TTUtil.Pack(0, 0), 1, "IUP[0]"),
    IUP_1(49, TTUtil.Pack(0, 0), 1, "IUP[1]"),
    SHP_0(50, TTUtil.Pack(0, 0), 1, "SHP[0]"),
    SHP_1(51, TTUtil.Pack(0, 0), 1, "SHP[1]"),
    SHC_0(52, TTUtil.Pack(1, 0), 1, "SHC[0]"),
    SHC_1(53, TTUtil.Pack(1, 0), 1, "SHC[1]"),
    SHZ_0(54, TTUtil.Pack(1, 0), 1, "SHZ[0]"),
    SHZ_1(55, TTUtil.Pack(1, 0), 1, "SHZ[1]"),
    SHPIX(56, TTUtil.Pack(1, 0), 1, "SHPIX"),
    IP(57, TTUtil.Pack(0, 0), 1, "IP"),
    MSIRP_0(58, TTUtil.Pack(2, 0), 1, "MSIRP[0]"),
    MSIRP_1(59, TTUtil.Pack(2, 0), 1, "MSIRP[1]"),
    AlignRP(60, TTUtil.Pack(0, 0), 1, "AlignRP"),
    RTDG(61, TTUtil.Pack(0, 0), 1, "RTDG"),
    MIAP_0(62, TTUtil.Pack(2, 0), 1, "MIAP[0]"),
    MIAP_1(63, TTUtil.Pack(2, 0), 1, "MIAP[1]"),

    NPushB(64, TTUtil.Pack(0, 0), -1, "NPushB"),
    NPushW(65, TTUtil.Pack(0, 0), -2, "NPushW"),
    WS(66, TTUtil.Pack(2, 0), 1, "WS"),
    RS(67, TTUtil.Pack(1, 1), 1, "RS"),
    WCvtP(68, TTUtil.Pack(2, 0), 1, "WCvtP"),
    RCvt(69, TTUtil.Pack(1, 1), 1, "RCvt"),
    GC_0(70, TTUtil.Pack(1, 1), 1, "GC[0]"),
    GC_1(71, TTUtil.Pack(1, 1), 1, "GC[1]"),
    SCFS(72, TTUtil.Pack(2, 0), 1, "SCFS"),
    MD_0(73, TTUtil.Pack(2, 1), 1, "MD[0]"),
    MD_1(74, TTUtil.Pack(2, 1), 1, "MD[1]"),
    MPPEM(75, TTUtil.Pack(0, 1), 1, "MPPEM"),
    MPS(76, TTUtil.Pack(0, 1), 1, "MPS"),
    FlipON(77, TTUtil.Pack(0, 0), 1, "FlipON"),
    FlipOFF(78, TTUtil.Pack(0, 0), 1, "FlipOFF"),
    DEBUG(79, TTUtil.Pack(1, 0), 1, "DEBUG"),

    LT(80, TTUtil.Pack(2, 1), 1, "LT"),
    LTEQ(81, TTUtil.Pack(2, 1), 1, "LTEQ"),
    GT(82, TTUtil.Pack(2, 1), 1, "GT"),
    GTEQ(83, TTUtil.Pack(2, 1), 1, "GTEQ"),
    EQ(84, TTUtil.Pack(2, 1), 1, "EQ"),
    NEQ(85, TTUtil.Pack(2, 1), 1, "NEQ"),
    ODD(86, TTUtil.Pack(1, 1), 1, "ODD"),
    EVEN(87, TTUtil.Pack(1, 1), 1, "EVEN"),
    IF(88, TTUtil.Pack(1, 0), 1, "IF"),
    EIF(89, TTUtil.Pack(0, 0), 1, "EIF"),
    AND(90, TTUtil.Pack(2, 1), 1, "AND"),
    OR(91, TTUtil.Pack(2, 1), 1, "OR"),
    NOT(92, TTUtil.Pack(1, 1), 1, "NOT"),
    DeltaP1(93, TTUtil.Pack(1, 0), 1, "DeltaP1"),
    SDB(94, TTUtil.Pack(1, 0), 1, "SDB"),
    SDS(95, TTUtil.Pack(1, 0), 1, "SDS"),

    ADD(96, TTUtil.Pack(2, 1), 1, "ADD"),
    SUB(97, TTUtil.Pack(2, 1), 1, "SUB"),
    DIV(98, TTUtil.Pack(2, 2), 1, "DIV"),
    MUL(99, TTUtil.Pack(2, 1), 1, "MUL"),
    ABS(100, TTUtil.Pack(1, 1), 1, "ABS"),
    NEG(101, TTUtil.Pack(1, 1), 1, "NEG"),
    FLOOR(102, TTUtil.Pack(1, 1), 1, "FLOOR"),
    CEILING(103, TTUtil.Pack(1, 1), 1, "CEILING"),
    ROUND_0(104, TTUtil.Pack(1, 1), 1, "ROUND[0]"),
    ROUND_1(105, TTUtil.Pack(1, 1), 1, "ROUND[1]"),
    ROUND_2(106, TTUtil.Pack(1, 1), 1, "ROUND[2]"),
    ROUND_3(107, TTUtil.Pack(1, 1), 1, "ROUND[3]"),
    NROUND_0(108, TTUtil.Pack(1, 1), 1, "NROUND[0]"),
    NROUND_1(109, TTUtil.Pack(1, 1), 1, "NROUND[1]"),
    NROUND_2(110, TTUtil.Pack(1, 1), 1, "NROUND[2]"),
    NROUND_3(111, TTUtil.Pack(1, 1), 1, "NROUND[3]"),

    WCvtF(112, TTUtil.Pack(2, 0), 1, "WCvtF"),
    DeltaP2(113, TTUtil.Pack(1, 0), 1, "DeltaP2"),
    DeltaP3(114, TTUtil.Pack(1, 0), 1, "DeltaP3"),
    DeltaCn_0(115, TTUtil.Pack(1, 0), 1, "DeltaCn[0]"),
    DeltaCn_1(116, TTUtil.Pack(1, 0), 1, "DeltaCn[1]"),
    DeltaCn_2(117, TTUtil.Pack(1, 0), 1, "DeltaCn[2]"),
    SROUND(118, TTUtil.Pack(1, 0), 1, "SROUND"),
    S45Round(119, TTUtil.Pack(1, 0), 1, "S45Round"),
    JROT(120, TTUtil.Pack(2, 0), 1, "JROT"),
    JROF(121, TTUtil.Pack(2, 0), 1, "JROF"),
    ROFF(122, TTUtil.Pack(0, 0), 1, "ROFF"),
    INS_0x7B(123, TTUtil.Pack(0, 0), 1, "INS_$7B"),
    RUTG(124, TTUtil.Pack(0, 0), 1, "RUTG"),
    RDTG(125, TTUtil.Pack(0, 0), 1, "RDTG"),
    SANGW(126, TTUtil.Pack(1, 0), 1, "SANGW"),
    AA(127, TTUtil.Pack(1, 0), 1, "AA"),

    FlipPT(128, TTUtil.Pack(0, 0), 1, "FlipPT"),
    FlipRgON(129, TTUtil.Pack(2, 0), 1, "FlipRgON"),
    FlipRgOFF(130, TTUtil.Pack(2, 0), 1, "FlipRgOFF"),
    INS_0x83(131, TTUtil.Pack(0, 0), 1, "INS_$83"),
    INS_0x84(132, TTUtil.Pack(0, 0), 1, "INS_$84"),
    ScanCTRL(133, TTUtil.Pack(1, 0), 1, "ScanCTRL"),
    SDVPTL_0(134, TTUtil.Pack(2, 0), 1, "SDVPTL[0]"),
    SDVPTL_1(135, TTUtil.Pack(2, 0), 1, "SDVPTL[1]"),
    GetINFO(136, TTUtil.Pack(1, 1), 1, "GetINFO"),
    IDEF(137, TTUtil.Pack(1, 0), 1, "IDEF"),
    ROLL(138, TTUtil.Pack(3, 3), 1, "ROLL"),
    MAX(139, TTUtil.Pack(2, 1), 1, "MAX"),
    MIN(140, TTUtil.Pack(2, 1), 1, "MIN"),
    ScanTYPE(141, TTUtil.Pack(1, 0), 1, "ScanTYPE"),
    InstCTRL(142, TTUtil.Pack(2, 0), 1, "InstCTRL"),
    INS_0x8F(143, TTUtil.Pack(0, 0), 1, "INS_$8F"),

    INS_$90(144, TTUtil.Pack(0, 0), 1, "INS_$90"),
    INS_$91(145, TTUtil.Pack(0, 0), 1, "INS_$91"),
    INS_$92(146, TTUtil.Pack(0, 0), 1, "INS_$92"),
    INS_$93(147, TTUtil.Pack(0, 0), 1, "INS_$93"),
    INS_$94(148, TTUtil.Pack(0, 0), 1, "INS_$94"),
    INS_$95(149, TTUtil.Pack(0, 0), 1, "INS_$95"),
    INS_$96(150, TTUtil.Pack(0, 0), 1, "INS_$96"),
    INS_$97(151, TTUtil.Pack(0, 0), 1, "INS_$97"),
    INS_$98(152, TTUtil.Pack(0, 0), 1, "INS_$98"),
    INS_$99(153, TTUtil.Pack(0, 0), 1, "INS_$99"),
    INS_$9A(154, TTUtil.Pack(0, 0), 1, "INS_$9A"),
    INS_$9B(155, TTUtil.Pack(0, 0), 1, "INS_$9B"),
    INS_$9C(156, TTUtil.Pack(0, 0), 1, "INS_$9C"),
    INS_$9D(157, TTUtil.Pack(0, 0), 1, "INS_$9D"),
    INS_$9E(158, TTUtil.Pack(0, 0), 1, "INS_$9E"),
    INS_$9F(159, TTUtil.Pack(0, 0), 1, "INS_$9F"),

    INS_$A0(160, TTUtil.Pack(0, 0), 1, "INS_$A0"),
    INS_$A1(161, TTUtil.Pack(0, 0), 1, "INS_$A1"),
    INS_$A2(162, TTUtil.Pack(0, 0), 1, "INS_$A2"),
    INS_$A3(163, TTUtil.Pack(0, 0), 1, "INS_$A3"),
    INS_$A4(164, TTUtil.Pack(0, 0), 1, "INS_$A4"),
    INS_$A5(165, TTUtil.Pack(0, 0), 1, "INS_$A5"),
    INS_$A6(166, TTUtil.Pack(0, 0), 1, "INS_$A6"),
    INS_$A7(167, TTUtil.Pack(0, 0), 1, "INS_$A7"),
    INS_$A8(168, TTUtil.Pack(0, 0), 1, "INS_$A8"),
    INS_$A9(169, TTUtil.Pack(0, 0), 1, "INS_$A9"),
    INS_$AA(170, TTUtil.Pack(0, 0), 1, "INS_$AA"),
    INS_$AB(171, TTUtil.Pack(0, 0), 1, "INS_$AB"),
    INS_$AC(172, TTUtil.Pack(0, 0), 1, "INS_$AC"),
    INS_$AD(173, TTUtil.Pack(0, 0), 1, "INS_$AD"),
    INS_$AE(174, TTUtil.Pack(0, 0), 1, "INS_$AE"),
    INS_$AF(175, TTUtil.Pack(0, 0), 1, "INS_$AF"),

    PushB_0(176, TTUtil.Pack(0, 1), 2, "PushB[0]"),
    PushB_1(177, TTUtil.Pack(0, 2), 3, "PushB[1]"),
    PushB_2(178, TTUtil.Pack(0, 3), 4, "PushB[2]"),
    PushB_3(179, TTUtil.Pack(0, 4), 5, "PushB[3]"),
    PushB_4(180, TTUtil.Pack(0, 5), 6, "PushB[4]"),
    PushB_5(181, TTUtil.Pack(0, 6), 7, "PushB[5]"),
    PushB_6(182, TTUtil.Pack(0, 7), 8, "PushB[6]"),
    PushB_7(183, TTUtil.Pack(0, 8), 9, "PushB[7]"),
    PushW_0(184, TTUtil.Pack(0, 1), 3, "PushW[0]"),
    PushW_1(185, TTUtil.Pack(0, 2), 5, "PushW[1]"),
    PushW_2(186, TTUtil.Pack(0, 3), 7, "PushW[2]"),
    PushW_3(187, TTUtil.Pack(0, 4), 9, "PushW[3]"),
    PushW_4(188, TTUtil.Pack(0, 5), 11, "PushW[4]"),
    PushW_5(189, TTUtil.Pack(0, 6), 13, "PushW[5]"),
    PushW_6(190, TTUtil.Pack(0, 7), 15, "PushW[6]"),
    PushW_7(191, TTUtil.Pack(0, 8), 17, "PushW[7]"),

    MDRP_00(192, TTUtil.Pack(1, 0), 1, "MDRP[00]"),
    MDRP_01(193, TTUtil.Pack(1, 0), 1, "MDRP[01]"),
    MDRP_02(194, TTUtil.Pack(1, 0), 1, "MDRP[02]"),
    MDRP_03(195, TTUtil.Pack(1, 0), 1, "MDRP[03]"),
    MDRP_04(196, TTUtil.Pack(1, 0), 1, "MDRP[04]"),
    MDRP_05(197, TTUtil.Pack(1, 0), 1, "MDRP[05]"),
    MDRP_06(198, TTUtil.Pack(1, 0), 1, "MDRP[06]"),
    MDRP_07(199, TTUtil.Pack(1, 0), 1, "MDRP[07]"),
    MDRP_08(200, TTUtil.Pack(1, 0), 1, "MDRP[08]"),
    MDRP_09(201, TTUtil.Pack(1, 0), 1, "MDRP[09]"),
    MDRP_10(202, TTUtil.Pack(1, 0), 1, "MDRP[10]"),
    MDRP_11(203, TTUtil.Pack(1, 0), 1, "MDRP[11]"),
    MDRP_12(204, TTUtil.Pack(1, 0), 1, "MDRP[12]"),
    MDRP_13(205, TTUtil.Pack(1, 0), 1, "MDRP[13]"),
    MDRP_14(206, TTUtil.Pack(1, 0), 1, "MDRP[14]"),
    MDRP_15(207, TTUtil.Pack(1, 0), 1, "MDRP[15]"),

    MDRP_16(208, TTUtil.Pack(1, 0), 1, "MDRP[16]"),
    MDRP_17(209, TTUtil.Pack(1, 0), 1, "MDRP[17]"),
    MDRP_18(210, TTUtil.Pack(1, 0), 1, "MDRP[18]"),
    MDRP_19(211, TTUtil.Pack(1, 0), 1, "MDRP[19]"),
    MDRP_20(212, TTUtil.Pack(1, 0), 1, "MDRP[20]"),
    MDRP_21(213, TTUtil.Pack(1, 0), 1, "MDRP[21]"),
    MDRP_22(214, TTUtil.Pack(1, 0), 1, "MDRP[22]"),
    MDRP_23(125, TTUtil.Pack(1, 0), 1, "MDRP[23]"),
    MDRP_24(216, TTUtil.Pack(1, 0), 1, "MDRP[24]"),
    MDRP_25(217, TTUtil.Pack(1, 0), 1, "MDRP[25]"),
    MDRP_26(218, TTUtil.Pack(1, 0), 1, "MDRP[26]"),
    MDRP_27(219, TTUtil.Pack(1, 0), 1, "MDRP[27]"),
    MDRP_28(220, TTUtil.Pack(1, 0), 1, "MDRP[28]"),
    MDRP_29(221, TTUtil.Pack(1, 0), 1, "MDRP[29]"),
    MDRP_30(222, TTUtil.Pack(1, 0), 1, "MDRP[30]"),
    MDRP_31(223, TTUtil.Pack(1, 0), 1, "MDRP[31]"),

    MIRP_00(224, TTUtil.Pack(2, 0), 1, "MIRP[00]"),
    MIRP_01(225, TTUtil.Pack(2, 0), 1, "MIRP[01]"),
    MIRP_02(226, TTUtil.Pack(2, 0), 1, "MIRP[02]"),
    MIRP_03(227, TTUtil.Pack(2, 0), 1, "MIRP[03]"),
    MIRP_04(228, TTUtil.Pack(2, 0), 1, "MIRP[04]"),
    MIRP_05(229, TTUtil.Pack(2, 0), 1, "MIRP[05]"),
    MIRP_06(230, TTUtil.Pack(2, 0), 1, "MIRP[06]"),
    MIRP_07(231, TTUtil.Pack(2, 0), 1, "MIRP[07]"),
    MIRP_08(232, TTUtil.Pack(2, 0), 1, "MIRP[08]"),
    MIRP_09(233, TTUtil.Pack(2, 0), 1, "MIRP[09]"),
    MIRP_10(234, TTUtil.Pack(2, 0), 1, "MIRP[10]"),
    MIRP_11(235, TTUtil.Pack(2, 0), 1, "MIRP[11]"),
    MIRP_12(236, TTUtil.Pack(2, 0), 1, "MIRP[12]"),
    MIRP_13(237, TTUtil.Pack(2, 0), 1, "MIRP[13]"),
    MIRP_14(238, TTUtil.Pack(2, 0), 1, "MIRP[14]"),
    MIRP_15(239, TTUtil.Pack(2, 0), 1, "MIRP[15]"),

    MIRP_16(240, TTUtil.Pack(2, 0), 1, "MIRP[16]"),
    MIRP_17(241, TTUtil.Pack(2, 0), 1, "MIRP[17]"),
    MIRP_18(242, TTUtil.Pack(2, 0), 1, "MIRP[18]"),
    MIRP_19(243, TTUtil.Pack(2, 0), 1, "MIRP[19]"),
    MIRP_20(244, TTUtil.Pack(2, 0), 1, "MIRP[20]"),
    MIRP_21(245, TTUtil.Pack(2, 0), 1, "MIRP[21]"),
    MIRP_22(246, TTUtil.Pack(2, 0), 1, "MIRP[22]"),
    MIRP_23(247, TTUtil.Pack(2, 0), 1, "MIRP[23]"),
    MIRP_24(248, TTUtil.Pack(2, 0), 1, "MIRP[24]"),
    MIRP_25(249, TTUtil.Pack(2, 0), 1, "MIRP[25]"),
    MIRP_26(250, TTUtil.Pack(2, 0), 1, "MIRP[26]"),
    MIRP_27(251, TTUtil.Pack(2, 0), 1, "MIRP[27]"),
    MIRP_28(252, TTUtil.Pack(2, 0), 1, "MIRP[28]"),
    MIRP_29(253, TTUtil.Pack(2, 0), 1, "MIRP[29]"),
    MIRP_30(254, TTUtil.Pack(2, 0), 1, "MIRP[30]"),
    MIRP_31(255, TTUtil.Pack(2, 0), 1, "MIRP[31]");

    private int val;
    private byte push_count;
    private int opcode_length;
    private String str;
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
    private OpCode(int val, byte push_count, int opcode_length, String str) {
      if (val == 64) {
        Debug(0, DebugTag.DBG_INTERP, TAG, "Opcode64! "+val+" "+push_count+" "+opcode_length+" "+str);
      }
      this.val = val;
      this.push_count = push_count;
      this.opcode_length = opcode_length;
      this.str = str;
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
