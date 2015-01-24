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
  /*    TTGlyphLoaderFuncs                                                 */
  /*                                                                       */
  /* ===================================================================== */

import org.apwtcl.apwfreetypelib.aftbase.FTGlyphClassRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderFuncs;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphLoaderRec;
import org.apwtcl.apwfreetypelib.aftbase.FTGlyphSlotRec;
import org.apwtcl.apwfreetypelib.aftbase.FTOutlineRec;
import org.apwtcl.apwfreetypelib.aftbase.FTSubGlyphRec;
import org.apwtcl.apwfreetypelib.aftbase.FTTags;
import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTExecContextRec;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTInterpTags;
import org.apwtcl.apwfreetypelib.aftttinterpreter.TTOpCode;
import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;
import org.apwtcl.apwfreetypelib.aftutil.TTUtil;

public class TTGlyphLoaderFuncs extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "TTGlyphLoaderFuncs";

    public static TTLoaderRec loader = null;

    /* ==================== TTGlyphLoaderFuncs ================================== */
    public TTGlyphLoaderFuncs() {
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
    public static FTError.ErrorTag TTProcessSimpleGlyph(TTLoaderRec loader) {
      FTGlyphLoaderRec gloader = loader.getGloader();
      FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
      int n_points;

      n_points = gloader.getCurrent().getN_points();
      /* set phantom points */
      gloader.getCurrent().getPoints()[n_points] = loader.getPp1();
      gloader.getCurrent().getPoints()[n_points + 1] = loader.getPp2();
      gloader.getCurrent().getPoints()[n_points + 2] = loader.getPp3();
      gloader.getCurrent().getPoints()[n_points + 3] = loader.getPp4();
      gloader.getCurrent().getTags()[n_points] = Flags.Curve.CONIC;
      gloader.getCurrent().getTags()[n_points + 1] = Flags.Curve.CONIC;
      gloader.getCurrent().getTags()[n_points + 2] = Flags.Curve.CONIC;
      gloader.getCurrent().getTags()[n_points + 3] = Flags.Curve.CONIC;
      n_points += 4;
      if ((loader.getLoad_flags().getVal() & Flags.Load.NO_HINTING.getVal()) == 0) {
        loader.getZone().tt_prepare_zone(gloader.getCurrent(), 0, 0);
        for (int i = 0; i < (loader.getZone().getN_points() + 4); i++) {
          loader.getZone().getOrus()[loader.getZone().getOrus_idx() + i] = new FTVectorRec();
          loader.getZone().getOrus()[loader.getZone().getOrus_idx() + i].x = loader.getZone().getCurPoint_x(i);
          loader.getZone().getOrus()[loader.getZone().getOrus_idx() + i].y = loader.getZone().getCurPoint_y(i);
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
          if ((loader.getLoad_flags().getVal() & Flags.Load.NO_SCALE.getVal()) == 0) {
            x_scale = ((TTSizeRec)loader.getSize()).getMetrics().getX_scale();
            y_scale = ((TTSizeRec)loader.getSize()).getMetrics().getY_scale();
            do_scale = true;
          }
        }
        if (do_scale) {
          for (vecIdx = 0; vecIdx < limit; vecIdx++) {
            gloader.getCurrent().getPoints()[vecIdx].x = TTUtil.FTMulFix(gloader.getCurrent().getPoints()[vecIdx].x, x_scale);
            gloader.getCurrent().getPoints()[vecIdx].y = TTUtil.FTMulFix(gloader.getCurrent().getPoints()[vecIdx].y, y_scale);
          }
          loader.setPp1(gloader.getCurrent().getPoints()[n_points - 4]);
          loader.setPp2(gloader.getCurrent().getPoints()[n_points - 3]);
          loader.setPp3(gloader.getCurrent().getPoints()[n_points - 2]);
          loader.setPp4(gloader.getCurrent().getPoints()[n_points - 1]);
        }
      }
      if ((loader.getLoad_flags().getVal() & Flags.Load.NO_HINTING.getVal()) == 0) {
        loader.getZone().setN_points(loader.getZone().getN_points() + 4);
        error = loader.HintGlyph(false);
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
    public static FTError.ErrorTag TTProcessCompositeComponent(TTLoaderRec loader,
          FTSubGlyphRec subglyph, int start_point, int num_base_points) {
      FTGlyphLoaderRec gloader = loader.getGloader();
//      FT_Vector* base_vec = gloader.base.outline.points;
      int num_points = gloader.getBase().getN_points();
      boolean have_scale;
      int x;
      int y;

      have_scale = ((subglyph.flags.getVal() & (Flags.LoadType.WE_HAVE_A_SCALE.getVal() |
          Flags.LoadType.WE_HAVE_AN_XY_SCALE.getVal() | Flags.LoadType.WE_HAVE_A_2X2.getVal())) != 0);
      /* perform the transform required for this subglyph */
      if (have_scale) {
        int i;
        FTReference<FTMatrixRec> matrix_ref = new FTReference<FTMatrixRec>();
        FTReference<FTVectorRec> vec_ref = new FTReference<FTVectorRec>();

        for (i = num_base_points; i < num_points; i++) {
//FIXME!!!
//          FTOutlineRec.FTVectorTransform(gloader.getBase().getPoints()[i], subglyph.transform);
        }
      }
      /* get offset */
      if ((subglyph.flags.getVal() & Flags.LoadType.ARGS_ARE_XY_VALUES.getVal()) == 0) {
        int k = subglyph.arg1;
        int l = subglyph.arg2;

        /* match l-th point of the newly loaded component to the k-th point */
        /* of the previously loaded components.                             */
        /* change to the point numbers used by our outline */
        k += start_point;
        l += num_base_points;
        if (k >= num_base_points || l >= num_points) {
          return FTError.ErrorTag.GLYPH_INVALID_COMPOSITE;
        }
        x = gloader.getBase().getPoints()[k].x - gloader.getBase().getPoints()[l].x;
        y = gloader.getBase().getPoints()[k].y - gloader.getBase().getPoints()[l].y;
      } else {
        x = subglyph.arg1;
        y = subglyph.arg2;
        if (x == 0 && y == 0) {
          return FTError.ErrorTag.ERR_OK;
        }
        /* Use a default value dependent on                                     */
        /* TT_CONFIG_OPTION_COMPONENT_OFFSET_SCALED.  This is useful for old TT */
        /* fonts which don't set the xxx_COMPONENT_OFFSET bit.                  */
        if (have_scale && (subglyph.flags.getVal() & Flags.LoadType.SCALED_COMPONENT_OFFSET.getVal()) != 0) {
          /*************************************************************************/
          /*                                                                       */
          /* This algorithm is a guess and works much better than the above.       */
          /*                                                                       */
          int mac_xscale = FTCalc.FTHypot(subglyph.transform.xx, subglyph.transform.xy);
          int mac_yscale = FTCalc.FTHypot(subglyph.transform.yy, subglyph.transform.yx);
          x = TTUtil.FTMulFix(x, mac_xscale);
          y = TTUtil.FTMulFix(y, mac_yscale);
        }
        if ((loader.getLoad_flags().getVal() & Flags.Load.NO_SCALE.getVal()) == 0) {
          int x_scale = ((TTSizeRec)loader.getSize()).getMetrics().getX_scale();
          int y_scale = ((TTSizeRec)loader.getSize()).getMetrics().getY_scale();
          x = TTUtil.FTMulFix(x, x_scale);
          y = TTUtil.FTMulFix(y, y_scale);
          if ((subglyph.flags.getVal() & Flags.LoadType.ROUND_XY_TO_GRID.getVal()) != 0) {
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
    public static FTError.ErrorTag TTProcessCompositeGlyph(TTLoaderRec loader,
            int start_point, int start_contour) {
      FTError.ErrorTag error;
      int i;

      /* make room for phantom points */
      error = loader.getGloader().FT_GLYPHLOADER_CHECK_POINTS(loader.getGloader().getBase().getN_points() + 4, 0);
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
      loader.getGloader().getBase().getPoints()[loader.getGloader().getBase().getN_points()] = loader.getPp1();
      loader.getGloader().getBase().getPoints()[loader.getGloader().getBase().getN_points() + 1] = loader.getPp2();
      loader.getGloader().getBase().getPoints()[loader.getGloader().getBase().getN_points() + 2] = loader.getPp3();
      loader.getGloader().getBase().getPoints()[loader.getGloader().getBase().getN_points() + 3] = loader.getPp4();
      loader.getGloader().getBase().getTags()[loader.getGloader().getBase().getN_points()] = Flags.Curve.CONIC;
      loader.getGloader().getBase().getTags()[loader.getGloader().getBase().getN_points() + 1] = Flags.Curve.CONIC;
      loader.getGloader().getBase().getTags()[loader.getGloader().getBase().getN_points() + 2] = Flags.Curve.CONIC;
      loader.getGloader().getBase().getTags()[loader.getGloader().getBase().getN_points() + 3] = Flags.Curve.CONIC;
      {
        int n_ins;
        int max_ins;

        /* TT_Load_Composite_Glyph only gives us the offset of instructions */
        /* so we read them here                                             */
        long pos = loader.getStream().seek(loader.getIns_pos());
        n_ins = loader.getStream().readShort();
        if (pos < 0 /* || FT_READ_USHORT(n_ins) */ ) {
          return FTError.ErrorTag.UNEXPECTED_NULL_VALUE;
        }
        FTTrace.Trace(7, TAG, String.format("  Instructions size = %d", n_ins));
        /* check it */
        max_ins = ((TTFaceRec)loader.getFace()).getMax_profile().getMaxSizeOfInstructions();
        if (n_ins > max_ins) {
          /* acroread ignores this field, so we only do a rough safety check */
          if ((int)n_ins > loader.getByte_len()) {
            FTTrace.Trace(7, TAG, String.format("TT_Process_Composite_Glyph: "+
                "too many instructions (%d) for glyph with length %d",
                n_ins, loader.getByte_len()));
            error = FTError.ErrorTag.GLYPH_TOO_MANY_HINTS;
            return error;
          }
          /*
          tmp = loader.exec.glyphSize;
          error = Update_Max(loader.exec.memory, &tmp, sizeof ( FT_Byte ), (void*)&loader.exec.glyphIns, n_ins );
          loader.exec.glyphSize = (short)tmp;
          */
          loader.getExec().glyphSize = n_ins;
          loader.getExec().glyphIns = new TTOpCode.OpCode [n_ins];
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
        loader.getStream().readByteArray(my_array, n_ins);
        for(i = 0; i < n_ins; i++) {
          loader.getExec().glyphIns[i] = TTOpCode.OpCode.getTableTag(my_array[i]);
        }
//        if (FT_STREAM_READ(loader.exec.glyphIns, n_ins)) {
//          return error;
//        }
        loader.getGlyph().setControl_data(loader.getExec().glyphIns);
        loader.getGlyph().setControl_len(n_ins);
      }
      loader.getZone().tt_prepare_zone(loader.getGloader().getBase(), start_point, start_contour);
      /* Some points are likely touched during execution of  */
      /* instructions on components.  So let's untouch them. */
      for (i = start_point; i < loader.getZone().getN_points(); i++) {
        loader.getZone().getTags()[i] = Flags.Curve.getTableTag(loader.getZone().getTags()[i].getVal() & ~Flags.Curve.TOUCH_BOTH.getVal());
      }
      loader.getZone().setN_points(loader.getZone().getN_points() + 4);
      return loader.HintGlyph(true);
    }

}