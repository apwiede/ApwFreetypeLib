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

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.FTBBoxRec;
import org.apwtcl.apwfreetypelib.aftbase.FTFaceRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphSlotRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSizeRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSubGlyphRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftsfnt.FTSfntInterfaceClass;
import org.apwtcl.apwfreetypelib.aftsfnt.TTLoad;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTInterpTags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

import java.util.HashSet;
import java.util.Set;

  /* ===================================================================== */
  /*    TTLoaderRec                                                          */
  /*                                                                       */
  /* ===================================================================== */

public class TTLoaderRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTLoaderRec";

  private FTFaceRec face = null;
  private FTSizeRec size = null;
  private FTGlyphSlotRec glyph = null;
  private TTGlyphLoaderRec gloader = null;
  private Set<Flags.Load> load_flags = new HashSet<>();
  private int glyph_index = 0;
  private FTStreamRec stream = null;
  private int byte_len = 0;
  private int n_contours = 0;
  private FTBBoxRec bbox = null;
  private int left_bearing = 0;
  private int advance = 0;
  private int linear = 0;
  private boolean linear_def = false;
  private boolean preserve_pps = false;
  private FTVectorRec pp1 = null;
  private FTVectorRec pp2 = null;
  private long glyf_offset = 0;
  /* the zone where we load our glyphs */
  private TTGlyphZoneRec base = null;
  private TTGlyphZoneRec zone = null;
  private TTExecContextRec exec;
  private TTOpCode.OpCode[] instructions;
  private int ins_pos = 0;
  /* for possible extensibility in other formats */
  private Object other;
  /* since version 2.1.8 */
  private int top_bearing = 0;
  private int vadvance = 0;
  private FTVectorRec pp3 = null;
  private FTVectorRec pp4 = null;
  /* since version 2.2.1 */
  private int cursor = 0;
  private int limit = 0;

  /* ==================== TTLoaderRec ================================== */
  public TTLoaderRec() {
    oid++;
    id = oid;

    bbox = new FTBBoxRec();
    pp1 = new FTVectorRec();
    pp2 = new FTVectorRec();
    pp3 = new FTVectorRec();
    pp4 = new FTVectorRec();
    base = new TTGlyphZoneRec();
    zone = new TTGlyphZoneRec();
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
    str.append("..load_flags: "+"load_flags"+'\n');
    str.append("..glyph_index: "+"glyph_index"+'\n');
    str.append("..byte_len: "+"byte_len"+'\n');
    str.append("..n_contours: "+"n_contours"+'\n');
    str.append("..left_bearing: "+"left_bearing"+'\n');
    str.append("..advance: "+"advance"+'\n');
    str.append("..linear: "+"linear"+'\n');
    str.append("..linear_def: "+"linear_def"+'\n');
    str.append("..preserve_pps: "+"preserve_pps"+'\n');
    str.append("..ins_pos: "+"ins_pos"+'\n');
    str.append("..top_bearing: "+"top_bearing"+'\n');
    str.append("..vadvance: "+"vadvance"+'\n');
    str.append("..cursor: "+"cursor"+'\n');
    str.append("..limit: "+"limit"+'\n');
    return str.toString();
  }

  /* =====================================================================
   * tt_loader_init
   * =====================================================================
   */
  public FTError.ErrorTag tt_loader_init(TTSizeRec size, FTGlyphSlotRec glyph, Set<Flags.Load> load_flags, boolean glyf_table_only) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_loader_init");
    TTFaceRec ttface;
    FTError.ErrorTag error;
    boolean pedantic = load_flags.contains(Flags.Load.PEDANTIC);

    ttface = (TTFaceRec)glyph.getFace();
    /* load execution context */
    if (!load_flags.contains(Flags.Load.NO_HINTING) && !glyf_table_only) {
      TTExecContextRec exec;
      boolean grayscale;
      boolean reexecute = false;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "fill TTSizeRec\n");
      if (!size.isCvt_ready()) {
        error = size.tt_size_ready_bytecode(pedantic);
        if (error != FTError.ErrorTag.ERR_OK) {
          return error;
        }
      }
      /* query new execution context */

      exec = size.isDebug() ? size.getContext()
                         : ((TTDriverRec)ttface.getDriver()).context;
      if (exec == null) {
        return FTError.ErrorTag.GLYPH_COULD_NOT_FIND_CONTEXT;
      }
//      grayscale = (((load_flags.getVal() >> 16) & 15) != FTTags.RenderMode.MONO.getVal());
      grayscale = !load_flags.contains(Flags.Load.COLOR);
      exec.TTLoadContext(ttface, size);
      {
        /* a change from mono to grayscale rendering (and vice versa) */
        /* requires a re-execution of the CVT program                 */
        if (grayscale != exec.grayscale) {
          FTTrace.Trace(7, TAG, "tt_loader_init: grayscale change, re-executing `prep' table");
          exec.grayscale = grayscale;
          reexecute = true;
        }
      }
      if (reexecute) {
        int i;

        for (i = 0; i < size.getCvt_size(); i++) {
          size.getCvt()[i] = TTUtil.FTMulFix(ttface.getCvt_table().getCvtValue(i), size.getTtmetrics().getScale());
        }
        size.tt_size_run_prep(pedantic);
      }
      /* see whether the cvt program has disabled hinting */
      if ((exec.graphics_state.getInstruct_control() & 1) != 0) {
        load_flags.add(Flags.Load.NO_HINTING);
      }
      /* load default graphics state -- if needed */
      if ((exec.graphics_state.getInstruct_control() & 2) != 0) {
        exec.graphics_state = new TTDefaultGraphicsStateClass();
      }
      exec.pedantic_hinting = load_flags.contains(Flags.Load.PEDANTIC);
      this.exec = exec;
      this.base.setExec(exec);
      this.zone.setExec(exec);
      this.instructions = exec.glyphIns;
    }
    /* seek to the beginning of the glyph table -- for Type 42 fonts     */
    /* the table might be accessed from a Postscript stream or something */
    /* else...                                                           */
    {
      FTReference<Integer> length_ref = new FTReference<Integer>();
      length_ref.Set(new Integer(0));
      error = ttface.gotoTable(TTTags.Table.glyf, ttface.getStream(), length_ref);

      if (error == FTError.ErrorTag.GLYPH_TABLE_MISSING) {
        this.glyf_offset = 0;
      } else {
        if (error != FTError.ErrorTag.ERR_OK) {
          Log.e(TAG, "tt_loader_init: could not access glyph table");
          return error;
        } else {
          this.glyf_offset = ttface.getStream().pos();
        }
      }
    }
    /* get face's glyph loader */
    if (!glyf_table_only) {
      glyph.getInternal().getLoader().GlyphLoaderRewind();
      this.gloader = (TTGlyphLoaderRec)glyph.getInternal().getLoader();
    }
    this.load_flags = load_flags;
    this.face = ttface;
    this.size = size;
    this.glyph = glyph;
    this.stream = ttface.getStream();
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   * tt_get_metrics
   *
   * Return the vertical metrics in font units for a given glyph.
   * Greg Hitchcock from Microsoft told us that if there were no `vmtx'
   * table, typoAscender/Descender from the `OS/2' table would be used
   * instead, and if there were no `OS/2' table, use ascender/descender
   * from the `hhea' table.  But that is not what Microsoft's rasterizer
   * apparently does: It uses the ppem value as the advance height, and
   * sets the top side bearing to be zero.
   *
   * =====================================================================
   */
  public void tt_get_metrics() {
    TTFaceRec face = (TTFaceRec)getFace();
    int advance_width = 0;
    int advance_height = 0;
    FTReference<Integer> left_bearing_ref = new FTReference<>();
    FTReference<Integer> top_bearing_ref = new FTReference<>();
    FTReference<Integer> advance_width_ref = new FTReference<>();
    FTReference<Integer> advance_height_ref = new FTReference<>();

    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "tt_get_metrics");
    advance_width_ref.Set(advance_width);
    ((FTSfntInterfaceClass)face.getSfnt()).getMetrics(face, false, glyph_index, left_bearing_ref, advance_width_ref);
    FTTrace.Trace(7, TAG, String.format("  advance width (font units): %d", advance_width_ref.Get()));
    FTTrace.Trace(7, TAG, String.format("  left side bearing (font units): %d", left_bearing_ref.Get()));

    left_bearing = left_bearing_ref.Get();
    advance_width = advance_width_ref.Get();
    top_bearing_ref.Set(top_bearing);
    advance_height_ref.Set(advance_height);

    if (face.isVertical_info()) {
      ((FTSfntInterfaceClass)face.getSfnt()).getMetrics(face, true, glyph_index, top_bearing_ref, advance_height_ref);
    } else {
      top_bearing_ref.Set(0);
      advance_height_ref.Set(face.getUnits_per_EM());
    }

    FTTrace.Trace(7, TAG, String.format("  advance height (font units): %d", advance_height_ref.Get()));
    FTTrace.Trace(7, TAG, String.format("  top side bearing (font units): %d", top_bearing_ref.Get()));
    top_bearing = top_bearing_ref.Get();
    advance_height = advance_height_ref.Get();
    advance = advance_width;
    vadvance = advance_height;
    if (!linear_def) {
      linear_def = true;
      linear = advance_width;
    }
  }

  /* =====================================================================
   * load_truetype_glyph
   *
   * <Description>
   *    Loads a given truetype glyph.  Handles composites and uses a
   *    TT_Loader object.
   *
   * =====================================================================
   */
  public FTError.ErrorTag load_truetype_glyph(int gindex, int recurse_count, boolean header_only) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "load_truetype_glyph index: "+gindex+"!");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int x_scale;
    int y_scale;
    long offset;
    TTFaceRec ttface = (TTFaceRec)this.face;
    boolean opened_frame = false;

    /* some fonts have an incorrect value of `maxComponentDepth', */
    /* thus we allow depth 1 to catch the majority of them        */
    if (recurse_count > 1 && recurse_count > ttface.getMax_profile().getMaxComponentDepth()) {
      error = FTError.ErrorTag.GLYPH_INVALID_COMPOSITE;
      if (opened_frame) {
        ttface.forgetGlyphFrame(this);
      }
      return error;
    }
    /* check glyph index */
    if (gindex >= face.getNum_glyphs()) {
      error = FTError.ErrorTag.GLYPH_INVALID_GLYPH_INDEX;
      if (opened_frame) {
        ttface.forgetGlyphFrame(this);
      }
      return error;
    }
    glyph_index = gindex;
    if (!load_flags.contains(Flags.Load.NO_SCALE)) {
      x_scale = size.getMetrics().getX_scale();
      y_scale = size.getMetrics().getY_scale();
    } else {
      x_scale = 0x10000;
      y_scale = 0x10000;
    }
    tt_get_metrics();
    error = ttface.getGlyf_table().LoadGlyph(ttface.getStream(), ttface, glyph_index);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    TTGlyphRec glyph = ttface.getGlyf_table().getGlyphs()[glyph_index];
    setN_contours(glyph.getNum_contours());
    bbox.setxMin(glyph.getX_min());
    bbox.setyMin(glyph.getY_min());
    bbox.setxMax(glyph.getX_max());
    bbox.setyMax(glyph.getY_max());
    FTTrace.Trace(7, TAG, String.format("  # of contours: %d", n_contours));
    FTTrace.Trace(7, TAG, String.format("  xMin: %4d  xMax: %4d", bbox.getxMin(), bbox.getxMax()));
    FTTrace.Trace(7, TAG, String.format("  yMin: %4d  yMax: %4d", bbox.getyMin(), bbox.getyMax()));


Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "loader.n_contours: "+n_contours+" header_only: "+header_only);
    if (n_contours == 0) {
      bbox.setxMin(0);
      bbox.setxMax(0);
      bbox.setyMin(0);
      bbox.setyMax(0);
      if (header_only) {
        if (opened_frame) {
          ttface.forgetGlyphFrame(this);
        }
        return error;
      }
      /* must initialize points before (possibly) overriding */
      /* glyph metrics from the incremental interface        */

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader_set_pp1: loader.bbox.xMin: %d, loader.bbox.yMax: %d, loader.left_bearing: %d, loader.top_bearing: %d, loader.vadvance: %d\n", bbox.getxMin(), bbox.getyMax(), left_bearing, top_bearing, vadvance));
      /* Calculate the four phantom points.                     */
      /* The first two stand for horizontal origin and advance. */
      /* The last two stand for vertical origin and advance.    */
      pp1.setX(bbox.getxMin() - left_bearing);
      pp1.setY(0);
      pp2.setX(pp1.getX() + advance);
      pp2.setY(0);
      pp3.setX(0);
      pp3.setY(top_bearing + bbox.getyMax());
      pp4.setX(0);
      pp4.setY(pp3.getY() - vadvance);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader.pp1.x: %d %d %d %d", pp1.getX(), pp2.getX(), pp3.getY(), pp4.getY())+load_flags.contains(Flags.Load.NO_SCALE));
      if (!load_flags.contains(Flags.Load.NO_SCALE)) {
        pp1.setX(TTUtil.FTMulFix(pp1.getX(), x_scale));
        pp2.setX(TTUtil.FTMulFix(pp2.getX(), x_scale));
        pp3.setY(TTUtil.FTMulFix(pp3.getY(), y_scale));
        pp4.setY(TTUtil.FTMulFix(pp4.getY(), y_scale));
      }
      error = FTError.ErrorTag.ERR_OK;
      if (opened_frame) {
        ttface.forgetGlyphFrame(this);
      }
      return error;
    }
    /* must initialize points before (possibly) overriding */
    /* glyph metrics from the incremental interface        */

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader_set_pp2: loader.bbox.xMin: %d, loader.bbox.yMax: %d, loader.left_bearing: %d, loader.top_bearing: %d, loader.vadvance: %d\n", bbox.getxMin(), bbox.getyMax(), left_bearing, top_bearing, vadvance));
    /* Calculate the four phantom points.                     */
    /* The first two stand for horizontal origin and advance. */
    /* The last two stand for vertical origin and advance.    */
    pp1.setX(bbox.getxMin() - left_bearing);
    pp1.setY(0);
    pp2.setX(pp1.getX() + advance);
    pp2.setY(0);
    pp3.setX(0);
    pp3.setY(top_bearing + bbox.getyMax());
    pp4.setX(0);
    pp4.setY(pp3.getY() - vadvance);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader_set_pp3: loader.pp1.x: %d, loader.pp1.y: %d, loader.pp2.x: %d, loader.pp2.y: %d, loader.pp3.x: %d, loader.pp3.y: %d, loader.pp4.x: %d, loader.pp4.y: %d\n", pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), pp3.getX(), pp3.getY(), pp4.getX(), pp4.getY()));
    /***********************************************************************/
    /***********************************************************************/
    /***********************************************************************/
    /* if it is a simple glyph, load it */
    if (n_contours > 0) {
      error = gloader.tt_load_simple_glyph(this);
      if (error != FTError.ErrorTag.ERR_OK) {
        if (opened_frame) {
          ttface.forgetGlyphFrame(this);
        }
        return error;
      }
      /* all data have been read */
      ttface.forgetGlyphFrame(this);
      opened_frame = false;
      error = TTProcessSimpleGlyph();
      if (error != FTError.ErrorTag.ERR_OK) {
        if (opened_frame) {
          ttface.forgetGlyphFrame(this);
        }
        return error;
      }
      gloader.GlyphLoaderAdd();
    } else { 
    /* otherwise, load a composite! */
      if (n_contours == -1) {
        int start_point;
        int start_contour;
        int ins_pos;  /* position of composite instructions, if any */

        start_point = gloader.getBase().getN_points();
        start_contour = gloader.getBase().getN_contours();
        /* for each subglyph, read composite header */
        error = ttface.readCompositeGlyph(this);
        if (error != FTError.ErrorTag.ERR_OK) {
          if (opened_frame) {
            ttface.forgetGlyphFrame(this);
          }
          return error;
        }
        /* store the offset of instructions */
        ins_pos = this.ins_pos;
        /* all data we need are read */
        ttface.forgetGlyphFrame(this);
        opened_frame = false;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader_set_pp4: loader.pp1.x: %d, loader.pp1.y: %d, loader.pp2.x: %d, loader.pp2.y: %d, loader.pp3.x: %d, loader.pp3.y: %d, loader.pp4.x: %d, loader.pp4.y: %d\n", pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), pp3.getX(), pp3.getY(), pp4.getX(), pp4.getY()));
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "LOAD_NO_SCALE: "+load_flags.contains(Flags.Load.NO_SCALE));
        if (!load_flags.contains(Flags.Load.NO_SCALE)) {
          pp1.setX(TTUtil.FTMulFix(pp1.getX(), x_scale));
          pp2.setX(TTUtil.FTMulFix(pp2.getX(), x_scale));
          pp3.setY(TTUtil.FTMulFix(pp3.getY(), y_scale));
          pp4.setY(TTUtil.FTMulFix(pp4.getY(), y_scale));
        }
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("loader_set_pp5: loader.pp1.x: %d, loader.pp1.y: %d, loader.pp2.x: %d, loader.pp2.y: %d, loader.pp3.x: %d, loader.pp3.y: %d, loader.pp4.x: %d, loader.pp4.y: %d\n", pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), pp3.getX(), pp3.getY(), pp4.getX(), pp4.getY()));
        /* if the flag FT_LOAD_NO_RECURSE is set, we return the subglyph */
        /* `as is' in the glyph slot (the client application will be     */
        /* responsible for interpreting these data)...                   */
        if (load_flags.contains(Flags.Load.NO_RECURSE)) {
          gloader.GlyphLoaderAdd();
          glyph.setFormat(FTTags.GlyphFormat.COMPOSITE);
          if (opened_frame) {
            ttface.forgetGlyphFrame(this);
          }
          return error;
        }
        /*********************************************************************/
        /*********************************************************************/
        /*********************************************************************/
        {
          int n;
          int num_base_points;
          FTSubGlyphRec subglyph = null;
          int num_points = start_point;
          int num_subglyphs = gloader.getCurrent().getNum_subglyphs();
          int num_base_subgs = gloader.getBase().getNum_subglyphs();
          FTStreamRec old_stream = stream;
          int old_byte_len = byte_len;
  
          gloader.GlyphLoaderAdd();
          /* read each subglyph independently */
          for (n = 0; n < num_subglyphs; n++) {
            FTVectorRec[] pp = new FTVectorRec[4];

            /* Each time we call load_truetype_glyph in this loop, the   */
            /* value of `gloader.base.subglyphs' can change due to table */
            /* reallocations.  We thus need to recompute the subglyph    */
            /* pointer on each iteration.                                */
            subglyph = gloader.getBase().getSubglyphs()[num_base_subgs + n];
            pp[0] = pp1;
            pp[1] = pp2;
            pp[2] = pp3;
            pp[3] = pp4;
            num_base_points = gloader.getBase().getN_points();
            error = load_truetype_glyph(subglyph.getIndex(), recurse_count + 1, false);
            if (error != FTError.ErrorTag.ERR_OK) {
              if (opened_frame) {
                ttface.forgetGlyphFrame(this);
              }
              return error;
            }
            /* restore subglyph pointer */
            subglyph = gloader.getBase().getSubglyphs()[num_base_subgs + n];
            if ((subglyph.getFlags().getVal() & Flags.SubGlyph.USE_MY_METRICS.getVal()) != 0) {
              pp1 = pp[0];
              pp2 = pp[1];
              pp3 = pp[2];
              pp4 = pp[3];
            }
            num_points = gloader.getBase().getN_points();
            if (num_points == num_base_points) {
              continue;
            } 
            /* gloader.base.outline consists of three parts:               */
            /* 0 -(1). start_point -(2). num_base_points -(3). n_points. */
            /*                                                              */
            /* (1): exists from the beginning                               */
            /* (2): components that have been loaded so far                 */
            /* (3): the newly loaded component                              */
            TTProcessCompositeComponent(subglyph, start_point, num_base_points);
          }
          stream = old_stream;
          byte_len = old_byte_len;
          /* process the glyph */
          this.ins_pos = ins_pos;
          if (load_flags.contains(Flags.Load.NO_HINTING) &&
                  ((subglyph.getFlags().getVal() & Flags.LoadType.WE_HAVE_INSTR.getVal()) != 0) && (num_points > start_point)) {
            TTProcessCompositeGlyph(start_point, start_contour);
          } 
        }
      } else {
      /* invalid composite count (negative but not -1) */
      error = FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
      }
    }
    if (opened_frame) {
      ttface.forgetGlyphFrame(this);
    }
    return error;
  }

  /* =====================================================================
   * compute_glyph_metrics
   * =====================================================================
   */
  public FTError.ErrorTag compute_glyph_metrics(int glyph_index) {
    Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "compute_glyph_metrics");
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    TTFaceRec ttface = (TTFaceRec)face;
    FTBBoxRec bbox;
    int y_scale;
    FTGlyphSlotRec glyph = getGlyph();
    TTSizeRec ttsize = (TTSizeRec)this.size;

    int i;
    for (i = 0; i < gloader.getCurrent().getN_points() + 4; i++) {
//Debug(0, DBG_LOAD_GLYPH, TAG, String.format("PP2: i: %d x: %d y: %d\n", i, loader.gloader.current.outline.points[loader.gloader.current.outline.points_idx + i].x,
//loader.gloader.current.outline.points[loader.gloader.current.outline.points_idx + i].y));
    }
    y_scale = 0x10000;
    if (!load_flags.contains(Flags.Load.NO_SCALE)) {
      y_scale = ttsize.getMetrics().getY_scale();
    }
    if (glyph.getFormat().getVal() != FTTags.GlyphFormat.COMPOSITE.getVal()) {
      FTReference<FTBBoxRec> bbox_ref = new FTReference<FTBBoxRec>();
      bbox_ref.Set(new FTBBoxRec());
      glyph.getOutline().FTOutlineGetCBox(bbox_ref);
      bbox = bbox_ref.Get();
    } else {
      bbox = this.bbox;
    }
      /* get the device-independent horizontal advance; it is scaled later */
      /* by the base layer.                                                */
    glyph.setLinearHoriAdvance(linear);
    glyph.getMetrics().setHoriBearingX(bbox.getxMin());
    glyph.getMetrics().setHoriBearingY(bbox.getyMax());
    glyph.getMetrics().setHoriAdvance(pp2.getX() - pp1.getX());
      /* adjust advance width to the value contained in the hdmx table */
    if (ttface.getPostscript().getIsFixedPitch() != 0 &&
        (!load_flags.contains(Flags.Load.NO_HINTING))) {
      int widthpIdx;

      widthpIdx = TTLoad.tt_face_get_device_metrics(ttface, ttsize.getMetrics().getX_ppem(), glyph_index);
      if (widthpIdx != 0) {
        glyph.getMetrics().setHoriAdvance(ttface.getHdmx_table().getRecords()[widthpIdx] << 6);
      }
    }
      /* set glyph dimensions */
    glyph.getMetrics().setWidth(bbox.getxMax() - bbox.getxMin());
    glyph.getMetrics().setHeight(bbox.getyMax() - bbox.getyMin());
      /* Now take care of vertical metrics.  In the case where there is */
      /* no vertical information within the font (relatively common),   */
      /* create some metrics manually                                   */
    {
      int top;      /* scaled vertical top side bearing  */
      int advance;  /* scaled vertical advance height    */

        /* Get the unscaled top bearing and advance height. */
      if (ttface.isVertical_info() && ttface.getVertical().getNumberOfVMetrics() > 0) {
        top = FTCalc.FTDivFix(pp3.getY() - bbox.getyMax(), y_scale);
        if (pp3.getY() <= pp4.getY()) {
          advance = 0;
        } else {
          advance = FTCalc.FTDivFix(pp3.getY() - pp4.getY(), y_scale);
        }
      } else {
        int height;

          /* XXX Compute top side bearing and advance height in  */
          /*     Get_VMetrics instead of here.                   */

          /* NOTE: The OS/2 values are the only `portable' ones, */
          /*       which is why we use them, if there is an OS/2 */
          /*       table in the font.  Otherwise, we use the     */
          /*       values defined in the horizontal header.      */
        height = FTCalc.FTDivFix(bbox.getyMax() - bbox.getyMin(), y_scale);
        if (ttface.getOs2().getVersion() != 0xFFFF) {
          advance = ttface.getOs2().getSTypoAscender() - ttface.getOs2().getSTypoDescender();
        } else {
          advance = ttface.getHorizontal().getAscender() - ttface.getHorizontal().getDescender();
        }
        top = (advance - height) / 2;
      }
      glyph.setLinearVertAdvance(advance);
        /* scale the metrics */
      if (!load_flags.contains(Flags.Load.NO_SCALE)) {
        top = FTCalc.FTMulFix(top, y_scale);
        advance = FTCalc.FTMulFix(advance, y_scale);
      }
        /* XXX: for now, we have no better algorithm for the lsb, but it */
        /*      should work fine.                                        */
        /*                                                               */
      glyph.getMetrics().setVertBearingX(glyph.getMetrics().getHoriBearingX() -
          glyph.getMetrics().getHoriAdvance() / 2);
      glyph.getMetrics().setVertBearingY(top);
      glyph.getMetrics().setVertAdvance(advance);
    }
    return error;
  }

  /* =====================================================================
   * HintGlyph
   *
   * <Description>
   *    Hint the glyph using the zone prepared by the caller.  Note that
   *    the zone is supposed to include four phantom points.
   *
   * =====================================================================
   */
  public FTError.ErrorTag HintGlyph(boolean is_composite) {
    FTError.ErrorTag error;
    int origin;
    int n_ins;

Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "TTHintGlyph: "+glyph.getControl_len());
    for(int i = 0; i < gloader.getCurrent().getN_points() + 4; i++) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("PP: %d %d %d\n", i, gloader.getCurrent().getPoints()[i].getX(),
          gloader.getCurrent().getPoints()[i].getY()));
    }
zone.showLoaderZone("TTHintGlyph1", exec);
gloader.getCurrent().showGloaderGlyph("TTHintGlyph1");
    if (glyph.getControl_len() > 0xFFFFL) {
      FTTrace.Trace(7, TAG, "TT_Hint_Glyph: too long instructions ");
      FTTrace.Trace(7, TAG, String.format("(0x%lx byte) is truncated",
          glyph.getControl_len()));
    }
    n_ins = glyph.getControl_len();
    origin = zone.getCurPoint(zone.getN_points() - 4).getX();
    origin = FTCalc.FT_PIX_ROUND(origin) - origin;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("origin: %d", origin));
    if (origin != 0) {
      FTGlyphLoaderRec.translate_array(zone.getN_points(), zone.getCur(), 0, origin, 0);
    }
    for (int i = 0; i < 5; i++) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A: i: %d cur.x: %d, cur.y: %d", i, zone.getCurPoint_x(i), zone.getCurPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A: i: %d orus.x: %d, orus.y: %d", i, zone.getOrusPoint_x(i), zone.getOrusPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A: i: %d org.x: %d, org.y: %d", i, zone.getOrgPoint_x(i), zone.getOrgPoint_y(i)));
    }
    for (int i = zone.getN_points() - 4; i < zone.getN_points(); i++) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A1: i: %d cur.x: %d, cur.y: %d", i, zone.getCurPoint_x(i), zone.getCurPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A1: i: %d orus.x: %d, orus.y: %d", i, zone.getOrusPoint_x(i), zone.getOrusPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30A1: i: %d org.x: %d, org.y: %d", i, zone.getOrgPoint_x(i), zone.getOrgPoint_y(i)));
    }
      /* save original point position in org */
    if (n_ins > 0) {
      for (int i = 0; i < (zone.getN_points()); i++) {
        zone.setOrgPoint(i, zone.getCurPoint(i));
      }
    }
    for (int i = 0; i < 5; i++) {
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30B: i: %d cur.x: %d, cur.y: %d", i, zone.getCurPoint_x(i), zone.getCurPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30B: i: %d orus.x: %d, orus.y: %d", i, zone.getOrusPoint_x(i), zone.getOrusPoint_y(i)));
      Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("TTHintGlyph 30B: i: %d org.x: %d, org.y: %d", i, zone.getOrgPoint_x(i), zone.getOrgPoint_y(i)));
    }
      /* Reset graphics state. */
    exec.graphics_state = ((TTSizeRec)size).getGraphics_state();
      /* XXX: UNDOCUMENTED! Hinting instructions of a composite glyph */
      /*      completely refer to the (already) hinted subglyphs.     */
    if (is_composite) {
      exec.metrics.setX_scale(1 << 16);
      exec.metrics.setY_scale(1 << 16);
      // FIXME !! eventually problem with Arrays.copyOf!!
      zone.xsetOrus(java.util.Arrays.copyOf(zone.getCur(), zone.getN_points()));
    } else {
      exec.metrics.setX_scale((size).getMetrics().getX_scale());
      exec.metrics.setY_scale((size).getMetrics().getY_scale());
    }
      /* round pp2 and pp4 */
    zone.setCurPoint_x(zone.getN_points() - 3, FTCalc.FT_PIX_ROUND(zone.getCurPoint_x(zone.getN_points() - 3)));
    zone.setCurPoint_y(zone.getN_points() - 1, FTCalc.FT_PIX_ROUND(zone.getCurPoint_y(zone.getN_points() - 1)));
    if (n_ins > 0) {
      boolean debug;
      FTGlyphLoaderRec gloader = this.gloader;
      FTOutlineRec current_outline = gloader.getCurrent();

      error = exec.TTSetCodeRange(TTInterpTags.CodeRange.GLYPH, exec.glyphIns, null, n_ins);
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      exec.is_composite = is_composite;
      exec.pts.copy(zone);
      debug = (!load_flags.contains(Flags.Load.NO_SCALE) && ((TTSizeRec)size).isDebug());
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "call TTRunContext");
if (gloader.getBase() != null) {
  gloader.getBase().showGloaderGlyph("base before call TTRunContext");
}
if (gloader.getCurrent() != null) {
  gloader.getCurrent().showGloaderGlyph("current before call TTRunContext");
}
zone.showLoaderZone("before call TTRunContext", exec);
      error = exec.TTRunContext(debug);
      if (error != FTError.ErrorTag.ERR_OK && exec.pedantic_hinting) {
        return error;
      }
        /* store drop-out mode in bits 5-7; set bit 2 also as a marker */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, "scantype: 0x"+Integer.toHexString(exec.graphics_state.getScan_type()));
      current_outline.addTag(0, Flags.Curve.HAS_SCANMODE);
      if ((exec.graphics_state.getScan_type() & 1) != 0) {
        current_outline.addTag(0, Flags.Curve.TOUCH_X);
      }
      if ((exec.graphics_state.getScan_type() & 2) != 0) {
        current_outline.addTag(0, Flags.Curve.TOUCH_Y);
      }
    }
      /* save glyph phantom points */
    if (!preserve_pps) {
      pp1 = zone.getCurPoint(zone.getN_points() - 4);
      pp2 = zone.getCurPoint(zone.getN_points() - 3);
      pp3 = zone.getCurPoint(zone.getN_points() - 2);
      pp4 = zone.getCurPoint(zone.getN_points() - 1);
    }
    return FTError.ErrorTag.ERR_OK;
  }


  /* =====================================================================
   *    TTProcessSimpleGlyph
   *
   * <Description>
   *    Once a simple glyph has been loaded, it needs to be processed.
   *    Usually, this means scaling and hinting through bytecode
   *    interpretation.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTProcessSimpleGlyph() {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int n_points;

    n_points = gloader.getCurrent().getN_points();
      /* set phantom points */
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("pp1: %d %d, pp2: %d %d, pp3: %d %d, pp4: %d %d", pp1.getX(), pp1.getY(), pp2.getX(), pp2.getY(), pp3.getX(), pp3.getY(), pp4.getX(), pp4.getY()));
    gloader.getCurrent().setPoint(n_points, pp1);
    gloader.getCurrent().setPoint(n_points + 1, pp2);
    gloader.getCurrent().setPoint(n_points + 2, pp3);
    gloader.getCurrent().setPoint(n_points + 3, pp4);
    gloader.getCurrent().setTag(n_points, Flags.Curve.CONIC);
    gloader.getCurrent().setTag(n_points + 1, Flags.Curve.CONIC);
    gloader.getCurrent().setTag(n_points + 2, Flags.Curve.CONIC);
    gloader.getCurrent().setTag(n_points + 3, Flags.Curve.CONIC);
    n_points += 4;
    if (!load_flags.contains(Flags.Load.NO_HINTING)) {
      zone.tt_prepare_zone(gloader.getCurrent(), 0, 0);
      for (int i = 0; i < (zone.getN_points() + 4); i++) {
        zone.setOrusPoint(i, new FTVectorRec());
        zone.setOrusPoint_x(i, zone.getCurPoint_x(i));
        zone.setOrusPoint_y(i, zone.getCurPoint_y(i));
      }
    }
    {
      int vecIdx = 0;
      int limit = n_points;
      int x_scale = 0; /* pacify compiler */
      int y_scale = 0;
      boolean do_scale = false;
      {
          /* scale the glyph */
        if (!load_flags.contains(Flags.Load.NO_SCALE)) {
          x_scale = size.getMetrics().getX_scale();
          y_scale = size.getMetrics().getY_scale();
          do_scale = true;
        }
      }
      if (do_scale) {
        for (vecIdx = 0; vecIdx < limit; vecIdx++) {
          gloader.getCurrent().setPoint_x(vecIdx, TTUtil.FTMulFix(gloader.getCurrent().getPoint_x(vecIdx), x_scale));
          gloader.getCurrent().setPoint_y(vecIdx, TTUtil.FTMulFix(gloader.getCurrent().getPoint_y(vecIdx), y_scale));
        }
        pp1 = gloader.getCurrent().getPoint(n_points - 4);
        pp2 = gloader.getCurrent().getPoint(n_points - 3);
        pp3 = gloader.getCurrent().getPoint(n_points - 2);
        pp4 = gloader.getCurrent().getPoint(n_points - 1);
      }
    }
    if (!load_flags.contains(Flags.Load.NO_HINTING)) {
      zone.setN_points(zone.getN_points() + 4);
      error = HintGlyph(false);
    }
    return error;
  }

  /* =====================================================================
   *    TTProcessCompositeComponent
   *
   * <Description>
   *    Once a composite component has been loaded, it needs to be
   *    processed.  Usually, this means transforming and translating.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTProcessCompositeComponent(FTSubGlyphRec subglyph, int start_point, int num_base_points) {
    int num_points = gloader.getBase().getN_points();
    boolean have_scale;
    int x;
    int y;

    have_scale = ((subglyph.getFlags().getVal() & (Flags.LoadType.WE_HAVE_A_SCALE.getVal() |
        Flags.LoadType.WE_HAVE_AN_XY_SCALE.getVal() | Flags.LoadType.WE_HAVE_A_2X2.getVal())) != 0);
      /* perform the transform required for this subglyph */
    if (have_scale) {
      int i;

      for (i = num_base_points; i < num_points; i++) {
//FIXME!!!
        Log.e(TAG, "SubGlyph FTVectorTransform not yet implemented!");
//          FTOutlineRec.FTVectorTransform(gloader.getBase().getPoints()[i], subglyph.transform);
      }
    }
      /* get offset */
    if ((subglyph.getFlags().getVal() & Flags.LoadType.ARGS_ARE_XY_VALUES.getVal()) == 0) {
      int k = subglyph.getArg1();
      int l = subglyph.getArg2();

        /* match l-th point of the newly loaded component to the k-th point */
        /* of the previously loaded components.                             */
        /* change to the point numbers used by our outline */
      k += start_point;
      l += num_base_points;
      if (k >= num_base_points || l >= num_points) {
        return FTError.ErrorTag.GLYPH_INVALID_COMPOSITE;
      }
      x = gloader.getBase().getPoint_x(k) - gloader.getBase().getPoint_x(l);
      y = gloader.getBase().getPoint_y(k) - gloader.getBase().getPoint_y(l);
    } else {
      x = subglyph.getArg1();
      y = subglyph.getArg2();
      if (x == 0 && y == 0) {
        return FTError.ErrorTag.ERR_OK;
      }
        /* Use a default value dependent on                                     */
        /* TT_CONFIG_OPTION_COMPONENT_OFFSET_SCALED.  This is useful for old TT */
        /* fonts which don't set the xxx_COMPONENT_OFFSET bit.                  */
      if (have_scale && (subglyph.getFlags().getVal() & Flags.LoadType.SCALED_COMPONENT_OFFSET.getVal()) != 0) {
        /*************************************************************************/
          /*                                                                       */
          /* This algorithm is a guess and works much better than the above.       */
          /*                                                                       */
        int mac_xscale = FTCalc.FTHypot(subglyph.getTransform().getXx(), subglyph.getTransform().getXy());
        int mac_yscale = FTCalc.FTHypot(subglyph.getTransform().getYy(), subglyph.getTransform().getYx());
        x = TTUtil.FTMulFix(x, mac_xscale);
        y = TTUtil.FTMulFix(y, mac_yscale);
      }
      if (!load_flags.contains(Flags.Load.NO_SCALE)) {
        int x_scale = size.getMetrics().getX_scale();
        int y_scale = size.getMetrics().getY_scale();
        x = TTUtil.FTMulFix(x, x_scale);
        y = TTUtil.FTMulFix(y, y_scale);
        if ((subglyph.getFlags().getVal() & Flags.LoadType.ROUND_XY_TO_GRID.getVal()) != 0) {
          x = FTCalc.FT_PIX_ROUND(x);
          y = FTCalc.FT_PIX_ROUND(y);
        }
      }
    }
    if (x != 0 || y != 0) {
      gloader.translate_array(num_points - num_base_points, gloader.getBase().getPoints(), num_base_points, x, y);
    }
    return FTError.ErrorTag.ERR_OK;
  }

  /* =====================================================================
   *    TTProcessCompositeGlyph
   *
   * <Description>
   *    This is slightly different from TTProcessSimpleGlyph, in that
   *    its sole purpose is to hint the glyph.  Thus this function is
   *    only available when bytecode interpreter is enabled.
   *
   * =====================================================================
   */
  public FTError.ErrorTag TTProcessCompositeGlyph(int start_point, int start_contour) {
    FTError.ErrorTag error;
    int i;

      /* make room for phantom points */
    error = gloader.FT_GLYPHLOADER_CHECK_POINTS(gloader.getBase().getN_points() + 4, 0);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    gloader.getBase().setPoint(gloader.getBase().getN_points(),  pp1);
    gloader.getBase().setPoint(gloader.getBase().getN_points() + 1, pp2);
    gloader.getBase().setPoint(gloader.getBase().getN_points() + 2, pp3);
    gloader.getBase().setPoint(gloader.getBase().getN_points() + 3, pp4);
    gloader.getBase().setTag(gloader.getBase().getN_points(), Flags.Curve.CONIC);
    gloader.getBase().setTag(gloader.getBase().getN_points() + 1, Flags.Curve.CONIC);
    gloader.getBase().setTag(gloader.getBase().getN_points() + 2, Flags.Curve.CONIC);
    gloader.getBase().setTag(gloader.getBase().getN_points() + 3, Flags.Curve.CONIC);
    {
      int n_ins;
      int max_ins;

        /* TT_Load_Composite_Glyph only gives us the offset of instructions */
        /* so we read them here                                             */
      long pos = stream.seek(getIns_pos());
      n_ins = stream.readShort();
      if (pos < 0 /* || FT_READ_USHORT(n_ins) */ ) {
        return FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
      }
      FTTrace.Trace(7, TAG, String.format("  Instructions size = %d", n_ins));
        /* check it */
      max_ins = ((TTFaceRec)getFace()).getMax_profile().getMaxSizeOfInstructions();
      if (n_ins > max_ins) {
          /* acroread ignores this field, so we only do a rough safety check */
        if (n_ins > byte_len) {
          FTTrace.Trace(7, TAG, String.format("TT_Process_Composite_Glyph: "+
                  "too many instructions (%d) for glyph with length %d",
              n_ins, byte_len));
          error = FTError.ErrorTag.GLYPH_TOO_MANY_HINTS;
          return error;
        }
          /*
          tmp = loader.exec.glyphSize;
          error = Update_Max(loader.exec.memory, &tmp, sizeof ( FT_Byte ), (void*)&loader.exec.glyphIns, n_ins );
          loader.exec.glyphSize = (short)tmp;
          */
        exec.glyphSize = n_ins;
        exec.glyphIns = new TTOpCode.OpCode [n_ins];
        if (error != FTError.ErrorTag.ERR_OK) {
          return error;
        }
      } else {
        if (n_ins == 0) {
          return FTError.ErrorTag.ERR_OK;
        }
      }
// FIXME!!
      byte[] my_array = new byte[n_ins];
      stream.readByteArray(my_array, n_ins);
      for(i = 0; i < n_ins; i++) {
        exec.glyphIns[i] = TTOpCode.OpCode.getTableTag(my_array[i]);
      }
//        if (FT_STREAM_READ(loader.exec.glyphIns, n_ins)) {
//          return error;
//        }
      glyph.setControl_data(exec.glyphIns);
      glyph.setControl_len(n_ins);
    }
    zone.tt_prepare_zone(gloader.getBase(), start_point, start_contour);
      /* Some points are likely touched during execution of  */
      /* instructions on components.  So let's untouch them. */
    for (i = start_point; i < zone.getN_points(); i++) {
      zone.removeTag(i, Flags.Curve.TOUCH_X);
      zone.removeTag(i, Flags.Curve.TOUCH_Y);
    }
    zone.setN_points(zone.getN_points() + 4);
    return HintGlyph(true);
  }

  /* ==================== getFace ================================== */
  public FTFaceRec getFace() {
    return face;
  }

  /* ==================== setFace ================================== */
  public void setFace(FTFaceRec face) {
    this.face = face;
  }

  /* ==================== getSize ================================== */
  public FTSizeRec getSize() {
    return size;
  }

  /* ==================== setSize ================================== */
  public void setSize(FTSizeRec size) {
    this.size = size;
  }

  /* ==================== getGlyph ================================== */
  public FTGlyphSlotRec getGlyph() {
    return glyph;
  }

  /* ==================== setGlyph ================================== */
  public void setGlyph(FTGlyphSlotRec glyph) {
    this.glyph = glyph;
  }

  /* ==================== getGloader ================================== */
  public TTGlyphLoaderRec getGloader() {
    return gloader;
  }

  /* ==================== setGloader ================================== */
  public void setGloader(TTGlyphLoaderRec gloader) {
    this.gloader = gloader;
  }

  /* ==================== getLoad_flags ================================== */
  public Set<Flags.Load> getLoad_flags() {
    return load_flags;
  }

  /* ==================== addLoad_flag ================================== */
  public void setLoad_flag(Flags.Load load_flag) {
    this.load_flags.clear();
    this.load_flags.add(load_flag);
  }

  /* ==================== addLoad_flag ================================== */
  public void addLoad_flag(Flags.Load load_flag) {
    this.load_flags.add(load_flag);
  }

  /* ==================== removeLoad_flag ================================== */
  public void removeLoad_flag(Flags.Load load_flag) {
    this.load_flags.remove(load_flag);
  }

  /* ==================== setLoad_flags ================================== */
  public void setLoad_flags(Set<Flags.Load> load_flags) {
    this.load_flags = load_flags;
  }

  /* ==================== getGlyph_index ================================== */
  public int getGlyph_index() {
    return glyph_index;
  }

  /* ==================== setGlyph_index ================================== */
  public void setGlyph_index(int glyph_index) {
    this.glyph_index = glyph_index;
  }

  /* ==================== getStream ================================== */
  public FTStreamRec getStream() {
    return stream;
  }

  /* ==================== setStream ================================== */
  public void setStream(FTStreamRec stream) {
    this.stream = stream;
  }

  /* ==================== getByte_len ================================== */
  public int getByte_len() {
    return byte_len;
  }

  /* ==================== setByte_len ================================== */
  public void setByte_len(int byte_len) {
    this.byte_len = byte_len;
  }

  /* ==================== getN_contours ================================== */
  public int getN_contours() {
    return n_contours;
  }

  /* ==================== setN_contours ================================== */
  public void setN_contours(int n_contours) {
    this.n_contours = n_contours;
  }

  /* ==================== getBbox ================================== */
  public FTBBoxRec getBbox() {
    return bbox;
  }

  /* ==================== setBbox ================================== */
  public void setBbox(FTBBoxRec bbox) {
    this.bbox = bbox;
  }

  /* ==================== getLeft_bearing ================================== */
  public int getLeft_bearing() {
    return left_bearing;
  }

  /* ==================== setLeft_bearing ================================== */
  public void setLeft_bearing(int left_bearing) {
    this.left_bearing = left_bearing;
  }

  /* ==================== getAdvance ================================== */
  public int getAdvance() {
    return advance;
  }

  /* ==================== setAdvance ================================== */
  public void setAdvance(int advance) {
    this.advance = advance;
  }

  /* ==================== getLinear ================================== */
  public int getLinear() {
    return linear;
  }

  /* ==================== setLinear ================================== */
  public void setLinear(int linear) {
    this.linear = linear;
  }

  /* ==================== isLinear_def ================================== */
  public boolean isLinear_def() {
    return linear_def;
  }

  /* ==================== setLinear_def ================================== */
  public void setLinear_def(boolean linear_def) {
    this.linear_def = linear_def;
  }

  /* ==================== isPreserve_pps ================================== */
  public boolean isPreserve_pps() {
    return preserve_pps;
  }

  /* ==================== setPreserve_pps ================================== */
  public void setPreserve_pps(boolean preserve_pps) {
    this.preserve_pps = preserve_pps;
  }

  /* ==================== getPp1 ================================== */
  public FTVectorRec getPp1() {
    return pp1;
  }

  /* ==================== setPp1 ================================== */
  public void setPp1(FTVectorRec pp1) {
    this.pp1 = pp1;
  }

  /* ==================== getPp2 ================================== */
  public FTVectorRec getPp2() {
    return pp2;
  }

  /* ==================== setPp2 ================================== */
  public void setPp2(FTVectorRec pp2) {
    this.pp2 = pp2;
  }

  /* ==================== getGlyf_offset ================================== */
  public long getGlyf_offset() {
    return glyf_offset;
  }

  /* ==================== setGlyf_offset ================================== */
  public void setGlyf_offset(long glyf_offset) {
    this.glyf_offset = glyf_offset;
  }

  /* ==================== getBase ================================== */
  public TTGlyphZoneRec getBase() {
    return base;
  }

  /* ==================== setBase ================================== */
  public void setBase(TTGlyphZoneRec base) {
    this.base = base;
  }

  /* ==================== getZone ================================== */
  public TTGlyphZoneRec getZone() {
    return zone;
  }

  /* ==================== setZone ================================== */
  public void setZone(TTGlyphZoneRec zone) {
    this.zone = zone;
  }

  /* ==================== getExec ================================== */
  public TTExecContextRec getExec() {
    return exec;
  }

  /* ==================== setExec ================================== */
  public void setExec(TTExecContextRec exec) {
    this.exec = exec;
  }

  /* ==================== getInstructions ================================== */
  public TTOpCode.OpCode[] getInstructions() {
    return instructions;
  }

  /* ==================== setInstructions ================================== */
  public void setInstructions(TTOpCode.OpCode[] instructions) {
    this.instructions = instructions;
  }

  /* ==================== getIns_pos ================================== */
  public int getIns_pos() {
    return ins_pos;
  }

  /* ==================== setIns_pos ================================== */
  public void setIns_pos(int ins_pos) {
    this.ins_pos = ins_pos;
  }

  /* ==================== getOther ================================== */
  public Object getOther() {
    return other;
  }

  /* ==================== setOther ================================== */
  public void setOther(Object other) {
    this.other = other;
  }

  /* ==================== getTop_bearing ================================== */
  public int getTop_bearing() {
    return top_bearing;
  }

  /* ==================== setTop_bearing ================================== */
  public void setTop_bearing(int top_bearing) {
    this.top_bearing = top_bearing;
  }

  /* ==================== getVadvance ================================== */
  public int getVadvance() {
    return vadvance;
  }

  /* ==================== setVadvance ================================== */
  public void setVadvance(int vadvance) {
    this.vadvance = vadvance;
  }

  /* ==================== getPp3 ================================== */
  public FTVectorRec getPp3() {
    return pp3;
  }

  /* ==================== setPp3 ================================== */
  public void setPp3(FTVectorRec pp3) {
    this.pp3 = pp3;
  }

  /* ==================== getPp4 ================================== */
  public FTVectorRec getPp4() {
    return pp4;
  }

  /* ==================== setPp4 ================================== */
  public void setPp4(FTVectorRec pp4) {
    this.pp4 = pp4;
  }

  /* ==================== getCursor ================================== */
  public int getCursor() {
    return cursor;
  }

  /* ==================== setCursor ================================== */
  public void setCursor(int cursor) {
    this.cursor = cursor;
  }

  /* ==================== getLimit ================================== */
  public int getLimit() {
    return limit;
  }

  /* ==================== setLimit ================================== */
  public void setLimit(int limit) {
    this.limit = limit;
  }

}