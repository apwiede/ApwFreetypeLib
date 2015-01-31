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

package org.apwtcl.apwfreetypelib.aftbase;

  /* ===================================================================== */
  /*    FTOutlineRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure is used to describe an outline to the scan-line     */
  /*    converter.                                                         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    n_contours :: The number of contours in the outline.               */
  /*                                                                       */
  /*    n_points   :: The number of points in the outline.                 */
  /*                                                                       */
  /*    points     :: A pointer to an array of `n_points' @FTVectorRec     */
  /*                  elements, giving the outline's point coordinates.    */
  /*                                                                       */
  /*    tags       :: A pointer to an array of `n_points' chars, giving    */
  /*                  each outline point's type.                           */
  /*                                                                       */
  /*                  If bit~0 is unset, the point is `off' the curve,     */
  /*                  i.e., a Bézier control point, while it is `on' if    */
  /*                  set.                                                 */
  /*                                                                       */
  /*                  Bit~1 is meaningful for `off' points only.  If set,  */
  /*                  it indicates a third-order Bézier arc control point; */
  /*                  and a second-order control point if unset.           */
  /*                                                                       */
  /*                  If bit~2 is set, bits 5-7 contain the drop-out mode  */
  /*                  (as defined in the OpenType specification; the value */
  /*                  is the same as the argument to the SCANMODE          */
  /*                  instruction).                                        */
  /*                                                                       */
  /*                  Bits 3 and~4 are reserved for internal purposes.     */
  /*                                                                       */
  /*    contours   :: An array of `n_contours' shorts, giving the end      */
  /*                  point of each contour within the outline.  For       */
  /*                  example, the first contour is defined by the points  */
  /*                  `0' to `contours[0]', the second one is defined by   */
  /*                  the points `contours[0]+1' to `contours[1]', etc.    */
  /*                                                                       */
  /*    flags      :: A set of bit flags used to characterize the outline  */
  /*                  and give hints to the scan-converter and hinter on   */
  /*                  how to convert/grid-fit it.  See @FT_OUTLINE_FLAGS.  */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The B/W rasterizer only checks bit~2 in the `tags' array for the   */
  /*    first point of each contour.  The drop-out mode as given with      */
  /*    @FT_OUTLINE_IGNORE_DROPOUTS, @FT_OUTLINE_SMART_DROPOUTS, and       */
  /*    @FT_OUTLINE_INCLUDE_STUBS in `flags' is then overridden.           */
  /*                                                                       */
  /* ===================================================================== */

import android.util.SparseArray;

import org.apwtcl.apwfreetypelib.aftutil.FTCalc;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTMatrixRec;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

public class FTOutlineRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTOutlineRec";

  protected int n_contours = 0;          /* number of contours in glyph    */
  protected int n_points = 0;            /* number of points in the glyph  */
  protected FTVectorRec[] points = null; /* the outline's points           */
  protected int points_idx = 0;
  protected Flags.Curve[] tags = null;   /* the points flags               */
  protected int tags_idx = 0;
  protected int[] contours = null;       /* the contour end points         */
  protected int contours_idx = 0;
  protected int flags = 0;               /* outline masks                  */

  /* ==================== FTOutlineRec ================================== */
  public FTOutlineRec() {
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
    str.append("..n_contours: "+n_contours+'\n');
    str.append("..n_points: "+n_points+'\n');
    str.append("..flags: "+flags+'\n');
    str.append("..points_idx: "+points_idx+'\n');
    str.append("..tags_idx: "+tags_idx+'\n');
    str.append("..contours_idx: "+contours_idx+'\n');
    return str.toString();
  }
 
  /* ==================== toDebugString ===================================== */
  public void copy(FTOutlineRec from) {
    n_contours = from.n_contours;
    n_points = from.n_points;
    points = from.points;
    tags = from.tags;
    contours = from.contours;
    flags = from.flags;
    points_idx = from.points_idx;
    tags_idx = from.tags_idx;
    contours_idx = from.contours_idx;
  }
    
  /* =====================================================================
   *    FTOutlineCheck
   *
   * <Description>
   *    Check the contents of an outline descriptor.
   *
   * <Input>
   *    outline :: A handle to a source outline.
   *
   * <Return>
   *    FreeType error code.  0~means success.
   *
   * =====================================================================
   */
  public static FTError.ErrorTag FTOutlineCheck(FTOutlineRec outline) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;

    if (outline != null) {
      int n_points = outline.n_points;
      int n_contours = outline.n_contours;
      int end0, end;
      int n;

      /* empty glyph? */
      if (n_points == 0 && n_contours == 0) {
        return error;
      }
      /* check point and contour counts */
      if (n_points <= 0 || n_contours <= 0) {
        error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
        return error;
      }
      end0 = end = -1;
      for (n = 0; n < n_contours; n++) {
        end = outline.contours[n];
        /* note that we don't accept empty contours */
        if (end <= end0 || end >= n_points) {
          error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
          return error;
        }
        end0 = end;
      }
      if (end != n_points - 1) {
        error = FTError.ErrorTag.LOAD_INVALID_ARGUMENT;
        return error;
      }
      /* XXX: check the tags array */
      return error;
    }
    return error;
  }

  /* =====================================================================
   * OutlineTranslate
   * =====================================================================
   */
  public void OutlineTranslate(int xOffset, int yOffset) {
    int n;

Debug(0, DebugTag.DBG_RENDER, TAG, "FTOutlineTranslate");
    for (n = 0; n < n_points; n++) {
      points[n].setX(points[n].getX() + xOffset);
      points[n].setY(points[n].getY() + yOffset);
    }
  }

  /* =====================================================================
   * OutlineTransform
   * =====================================================================
   */
  public void OutlineTransform(FTMatrixRec matrix) {
    int limit;
    int vec_idx = 0;

    if (matrix == null) {
      return;
    }
    limit = n_points;
    for ( ; vec_idx < limit; vec_idx++) {
      VectorTransform(points[vec_idx], matrix);
    }
  }
  
  /* =====================================================================
   * VectorTransform
   * =====================================================================
   */
  public void VectorTransform(FTVectorRec vec, FTMatrixRec matrix) {
    int xz;
    int yz;

    if (vec == null || matrix == null) {
      return;
    }
    xz = FTCalc.FTMulFix(vec.getX(), matrix.getXx()) + FTCalc.FTMulFix(vec.getY(), matrix.getXy());
    yz = FTCalc.FTMulFix(vec.getY(), matrix.getYx()) + FTCalc.FTMulFix(vec.getY(), matrix.getYy());
    vec.setX(xz);
    vec.setY(yz);
  }
    
  /* =====================================================================
   * FTOutlineGetCBox
   * =====================================================================
   */
  public void FTOutlineGetCBox(FTReference<FTBBoxRec> cbox_ref) {
    FTBBoxRec cbox = cbox_ref.Get();
    int xMin;
    int yMin;
    int xMax;
    int yMax;

System.out.println(String.format("FTOutlineGetCBox: n_points: %d", n_points));
    if (cbox != null) {
      if (n_points == 0) {
        xMin = 0;
        yMin = 0;
        xMax = 0;
        yMax = 0;
      } else {
        int vecIdx = 0;
        int limit = n_points;
        FTVectorRec vec;

int i;
for (i = 0; i < n_points; i++) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("FTOutLineGetCBox: i: %d x: %d y: %d\n", i, points[i].getX(), points[i].getY()));
}
        vec = points[vecIdx];
        xMin = xMax = vec.getX();
        yMin = yMax = vec.getY();
        vecIdx++;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("xMin: xMin: %d xMax: %d yMin: %d yMax: %d vecIdx: %d", xMin, xMax, yMin, yMax, vecIdx));

        for ( ; vecIdx < limit; vecIdx++) {
          int x;
          int y;

          vec = points[vecIdx];
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("vec: %d %d %d", vecIdx, vec.getX(), vec.getY()));
          x = vec.getX();
          if (x < xMin) {
            xMin = x;
          }
          if (x > xMax) {
            xMax = x;
          }
          y = vec.getY();
          if (y < yMin) {
            yMin = y;
          }
          if (y > yMax) {
            yMax = y;
          }
        }
      }
      cbox.setxMin(xMin);
      cbox.setxMax(xMax);
      cbox.setyMin(yMin);
      cbox.setyMax(yMax);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("FTOutlineGetCBox end:: xMin: %d xMax: %d yMin: %d yMax: %d", xMin, xMax, yMin, yMax));
    }
    cbox_ref.Set(cbox);
  }

  /* =====================================================================
   * clear
   * =====================================================================
   */
  public void clear() {
    n_contours = 0;
    n_points = 0;
//      points = null;
//      tags = null;
//      contours = null;
    flags = 0;
    points_idx = 0;
    tags_idx = 0;
    contours_idx = 0;
  }
 
  /* =====================================================================
   *    FTOutlineDecompose
   *
   * =====================================================================
   */
  public FTError.ErrorTag FTOutlineDecompose(FTOutlineFuncs func_interface, Object user) {
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTVectorRec v_last;
    FTVectorRec v_control = new FTVectorRec();
    FTVectorRec v_start;
    int pointIdx;
    int limit;
    int tagsIdx;
    int n;        /* index of contour in outline     */
    int first = 0; /* index of first point in contour */
    Flags.Curve tag;       /* current point's state           */
    int shift;
    int delta;
    boolean doConic = true;
    boolean doContinue = false;
    boolean isClosed = false;

Debug(0, DebugTag.DBG_RENDER, TAG, "ftoutline FTOutlineDecompose");
    if (func_interface == null) {
      return FTError.ErrorTag.RENDER_INVALID_ARGUMENT;
    }
    shift = func_interface.getShift();
    delta = func_interface.getDelta();
    first = 0;
    for (n = 0; n < n_contours; n++) {
      int last;  /* index of last point in contour */

      FTTrace.Trace(7, TAG, String.format("ftoutline FT_Outline_Decompose: Outline %d of %d", n, n_contours));
      last = contours[n];
      if (last < 0) {
        return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
      }
      limit = last;
      v_start = points[first];
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("first: %d,  last: %d, v_start.x: %d, v_start.y: %d", first, last, v_start.getX(), v_start.getY()));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("delta: %d, shift: %d", delta, shift));
      v_start.setX(((v_start.getX()) << shift) - delta);
      v_start.setY(((v_start.getY()) << shift) - delta);
      v_last = points[last];
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("v_last.x: %d, v_last.y: %d", v_last.getX(), v_last.getY()));
      v_last.setX(((v_last.getX()) << shift) - delta);
      v_last.setY(((v_last.getY()) << shift) - delta);
      v_control.setX(v_start.getX());
      v_control.setY(v_start.getY());
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("v_start: %d %d v_last: %d %d", v_start.getX(), v_start.getY(), v_last.getX(), v_last.getY()));
      pointIdx = first;
      tagsIdx = first;
      tag = Flags.Curve.getTableTag(tags[tagsIdx].getVal() & 3);
      /* A contour cannot start with a cubic control point! */
      if (tag == Flags.Curve.CUBIC) {
        return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
      }
      /* check first point to determine origin */
      if (tag == Flags.Curve.CONIC) {
        /* first point is conic control.  Yes, this happens. */
        if ((tags[last].getVal() & 3) == Flags.Curve.ON.getVal()) {
          /* start at last point if it is on the curve */
          v_start.setX(v_last.getX());
          v_start.setY(v_last.getY());
          limit--;
        } else {
          /* if both first and last points are conic,         */
          /* start at their middle and record its position    */
          /* for closure                                      */
          v_start.setX((v_start.getX() + v_last.getX()) / 2);
          v_start.setY((v_start.getY() + v_last.getY()) / 2);
          v_last.setX(v_start.getX());
          v_last.setY(v_start.getY());
        }
        pointIdx--;
        tagsIdx--;
      }
Debug(0, DebugTag.DBG_RENDER, TAG, "Decompose:");
      FTTrace.Trace(7, TAG, String.format("  move to (%.2f, %.2f)",
                  v_start.getX() / 64.0, v_start.getY() / 64.0));
      error = func_interface.moveTo(v_start, user);
      FTTrace.Trace(7, TAG, String.format("  move to after (%.2f, %.2f)",
          v_start.getX() / 64.0, v_start.getY() / 64.0));
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
        return error;
      }
      while (pointIdx < limit) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("decompose1: pointIdx: %d, limit: %d, n: %d, outline.n_contours: %d", pointIdx, limit, n, n_contours));
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==2 v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
        pointIdx++;
        tagsIdx++;
        tag = Flags.Curve.getTableTag(tags[tagsIdx].getVal() & 3);
        switch (tag)
        {
        case ON:  /* emit a single line_to */
          {
            FTVectorRec vec = new FTVectorRec();

Debug(0, DebugTag.DBG_RENDER, TAG, "FT_CURVE_TAG_ON");
            vec.setX((((points[pointIdx].getX())) << shift) - delta);
            vec.setY(((points[pointIdx].getY()) << shift) - delta);
            FTTrace.Trace(7, TAG, String.format("  line to (%.2f, %.2f)\n",
                        vec.getX() / 64.0, vec.getY() / 64.0));
            error = func_interface.lineTo(vec, user);
            if (error != FTError.ErrorTag.ERR_OK) {
              FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
              return error;
            }
            continue;
          }
        case CONIC:  /* consume conic arcs */
Debug(0, DebugTag.DBG_RENDER, TAG, "FT_CURVE_TAG_CONIC");
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==3a v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
          v_control.setX(((points[pointIdx].getX()) << shift) - delta);
          v_control.setY(((points[pointIdx].getY()) << shift) - delta);
          doConic = true;
          while (doConic) {
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("decompose doConic: pointIdx: %d,  limit: %d", pointIdx, limit));
            doConic = false;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==3b v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
            doContinue = false;
            if (pointIdx < limit) {
              FTVectorRec vec = new FTVectorRec();
              FTVectorRec v_middle = new FTVectorRec();

              pointIdx++;
              tagsIdx++;
              tag = Flags.Curve.getTableTag(tags[tagsIdx].getVal() & 3);
              vec.setX(((points[pointIdx].getX()) << shift) - delta);
              vec.setY(((points[pointIdx].getY()) << shift) - delta);
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("tag: %d, tagIdx: %d, pointIdx: %d, limit: %d, vec.x: %d, vec.y: %d", tag, tagsIdx, pointIdx, limit, vec.getX(), vec.getY()));
              if (tag == Flags.Curve.ON) {
                FTTrace.Trace(0, TAG, String.format("  1 conic to (%.2f, %.2f)"+
                          " with control (%.2f, %.2f)",
                          vec.getX() / 64.0, vec.getY() / 64.0,
                          v_control.getX() / 64.0, v_control.getY() / 64.0));
                error = func_interface.conicTo(v_control, vec, user);
Debug(0, DebugTag.DBG_RENDER, TAG, "Decompose2: after call conic_to");
                if (error != FTError.ErrorTag.ERR_OK) {
                  FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
                  return error;
                }
                doContinue = true;
                break;
              }
              if (tag != Flags.Curve.CONIC) {
                return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
              }
              v_middle.setX((v_control.getX() + vec.getX()) / 2);
              v_middle.setY((v_control.getY() + vec.getY()) / 2);
              FTTrace.Trace(7, TAG, String.format("  2 conic to (%.2f, %.2f)"+
                        " with control (%.2f, %.2f)",
                        v_middle.getX() / 64.0, v_middle.getY() / 64.0,
                        v_control.getX() / 64.0, v_control.getY() / 64.0));
              error = func_interface.conicTo(v_control, v_middle, user);
 Debug(0, DebugTag.DBG_RENDER, TAG, "Decompose3: after call conic_to");
              if (error != FTError.ErrorTag.ERR_OK) {
                FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
                return error;
              }
              v_control.setX(vec.getX());
              v_control.setY(vec.getY());
              doConic = true;
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==4a v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
            }
          }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==4b v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
          if (doContinue) {
            continue;
          }
          FTTrace.Trace(8, TAG, String.format(" 3 conic to (%.2f, %.2f)"+
                      " with control (%.2f, %.2f)",
                      v_start.getX() / 64.0, v_start.getY() / 64.0,
                      v_control.getX() / 64.0, v_control.getY() / 64.0));
          error = func_interface.conicTo(v_control, v_start, user);
          if (error != FTError.ErrorTag.ERR_OK) {
            FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
            return error;
          }
          isClosed = true;
          break;
        default:  /* FT_CURVE_TAG_CUBIC */
          {
            FTVectorRec vec1 = new FTVectorRec();
            FTVectorRec vec2 = new FTVectorRec();

Debug(0, DebugTag.DBG_RENDER, TAG, "FT_CURVE_TAG_CUBIC");
            if (pointIdx + 1 > limit ||
                 (tags[tagsIdx + 1].getVal() & 3) != Flags.Curve.CUBIC.getVal()) {
              return FTError.ErrorTag.RENDER_INVALID_OUTLINE;
            }
            pointIdx += 2;
            tagsIdx += 2;
            vec1.setX((((points[pointIdx - 2].getX()) << shift) - delta));
            vec1.setY((((points[pointIdx - 2].getY()) << shift) - delta));
            vec2.setX((((points[pointIdx - 1].getX()) << shift) - delta));
            vec2.setY((((points[pointIdx - 1].getY()) << shift) - delta));
            if (pointIdx <= limit) {
              FTVectorRec vec = new FTVectorRec();

              vec.setX((((points[pointIdx].getX()) << shift) - delta));
              vec.setY((((points[pointIdx].getY()) << shift) - delta));
              FTTrace.Trace(7, TAG, String.format(" 1 cubic to (%.2f, %.2f)"+
                          " with controls (%.2f, %.2f) and (%.2f, %.2f)\n",
                          vec.getX() / 64.0, vec.getY() / 64.0,
                          vec1.getX() / 64.0, vec1.getY() / 64.0,
                          vec2.getX() / 64.0, vec2.getY() / 64.0));
              error = func_interface.cubicTo(vec1, vec2, vec, user);
              if (error != FTError.ErrorTag.ERR_OK) {
                FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
                return error;
              }
              continue;
            }
            FTTrace.Trace(7, TAG, String.format(" 2 cubic to (%.2f, %.2f)"+
                        " with controls (%.2f, %.2f) and (%.2f, %.2f)",
                        v_start.getX() / 64.0, v_start.getY() / 64.0,
                        vec1.getX() / 64.0, vec1.getY() / 64.0,
                        vec2.getX() / 64.0, vec2.getY() / 64.0));
            error = func_interface.cubicTo(vec1, vec2, v_start, user);
            if (error != FTError.ErrorTag.ERR_OK) {
              FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
              return error;
            }
            isClosed = true;
            break;
          }
        }
      }
Debug(0, DebugTag.DBG_RENDER, TAG, String.format("==5 v_start.x: %d, v_start.y: %d", v_start.getX(), v_start.getY()));
      if (! isClosed) {
        /* close the contour with a line segment */
        FTTrace.Trace(7, TAG, String.format(" 2 line to (%.2f, %.2f)",
                  v_start.getX() / 64.0, v_start.getY() / 64.0));
        error = func_interface.lineTo(v_start, user);
      }
      if (error != FTError.ErrorTag.ERR_OK) {
        FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Error %d", error));
        return error;
      }
      first = last + 1;
    }

    FTTrace.Trace(7, TAG, String.format("FT_Outline_Decompose: Done %d", n));
    return FTError.ErrorTag.ERR_OK;
  }

  /* ==================== getN_contours ================================== */
  public int getN_contours() {
    return n_contours;
  }

  /* ==================== setN_contours ================================== */
  public void setN_contours(int n_contours) {
    this.n_contours = n_contours;
  }

  /* ==================== getN_points ================================== */
  public int getN_points() {
    return n_points;
  }

  /* ==================== setN_points ================================== */
  public void setN_points(int n_points) {
    this.n_points = n_points;
  }

  /* ==================== getPoint ================================== */
  public FTVectorRec getPoint(int idx) {
    return points[points_idx+idx];
  }

  /* ==================== getPoint_x ================================== */
  public int getPoint_x(int idx) {
    return points[points_idx+idx].getX();
  }

  /* ==================== getPoint_y ================================== */
  public int getPoint_y(int idx) {
    return points[points_idx+idx].getY();
  }

  /* ==================== setPoint_x ================================== */
  public void setPoint_x(int idx, int val) {
    points[points_idx+idx].setX(val);
  }

  /* ==================== setPoint_y ================================== */
  public void setPoint_y(int idx, int val) {
    points[points_idx+idx].setY(val);
  }

  /* ==================== setPoint ================================== */
  public void setPoint(int idx, FTVectorRec point) {
    points[points_idx+idx] = point;
  }

  /* ==================== getPoints ================================== */
  public FTVectorRec[] getPoints() {
    return points;
  }

  /* ==================== setPoints ================================== */
  public void setPoints(FTVectorRec[] points) {
    this.points = points;
  }

  /* ==================== getTag ================================== */
  public Flags.Curve getTag(int idx) {
    return tags[tags_idx+idx];
  }

  /* ==================== setTag ================================== */
  public void setTag(int idx, Flags.Curve tag) {
    tags[tags_idx+idx] = tag;
  }

  /* ==================== getTags ================================== */
  public Flags.Curve[] getTags() {
    return tags;
  }

  /* ==================== setTags ================================== */
  public void setTags(Flags.Curve[] tags) {
    this.tags = tags;
  }

  /* ==================== getContour ================================== */
  public int getContour(int idx) {
    return contours[contours_idx+idx];
  }

  /* ==================== setContours ================================== */
  public void setContour(int idx, int contour) {
    contours[contours_idx+idx] = contour;
  }

  /* ==================== getContours ================================== */
  public int[] getContours() {
    return contours;
  }

  /* ==================== setContours ================================== */
  public void setContours(int[] contours) {
    this.contours = contours;
  }

  /* ==================== getFlags ================================== */
  public int getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /* ==================== getPoints_idx ================================== */
  public int getPoints_idx() {
    return points_idx;
  }

  /* ==================== setPoints_idx ================================== */
  public void setPoints_idx(int points_idx) {
    this.points_idx = points_idx;
  }

  /* ==================== getTags_idx ================================== */
  public int getTags_idx() {
    return tags_idx;
  }

  /* ==================== setTags_idx ================================== */
  public void setTags_idx(int tags_idx) {
    this.tags_idx = tags_idx;
  }

  /* ==================== getContours_idx ================================== */
  public int getContours_idx() {
    return contours_idx;
  }

  /* ==================== setContours_idx ================================== */
  public void setContours_idx(int contours_idx) {
    this.contours_idx = contours_idx;
  }

}