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
  /*    TTGlyphSimpleRec                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model a TrueType glyf table.  All fields       */
  /*    comply to the TrueType specification.                              */
  /*                                                                       */
  /* ===================================================================== */

import android.util.Log;

import org.apwtcl.apwfreetypelib.aftbase.Flags;
import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;
import org.apwtcl.apwfreetypelib.aftutil.FTVectorRec;

import java.util.Set;

public class TTGlyphSimpleRec extends TTGlyphRec {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTGlyphSimple";

  private int[] org_contours = null; /* end points of contours */
  private int instructions_length = 0;
  private byte[] instructions = null;
  private Set<TTTags.GlyphFlags>[] flags = null;
  private int[] x_coordinates = null;
  private int[] y_coordinates = null;

  /* next fields not in the file */
  private int[] contours = null; /* end points of contours */
  private int num_points = 0;
  private FTVectorRec[] points = null;
  private Set<TTTags.GlyphFlags>[] tags = null;

  /* ==================== TTGlyphSimpleRec ================================== */
  public TTGlyphSimpleRec() {
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
    String super_str = super.toDebugString();
    StringBuffer str = new StringBuffer(mySelf()+"\n");
    str.append("..instructions_length: "+instructions_length+'\n');
    str.append("..num_points: "+num_points+'\n');
    return super_str+str.toString();
  }

  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface, int limit) {
    Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load: glyf offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    int prev_contour = 0;
    int contour_idx;
    int contour_limit;
    int flag_idx;
    int tag_idx;
    int tag_limit;
    int vec_idx;
    int vec_limit;
    int x;
    int y;
    Set<TTTags.GlyphFlags> tag;
    int delta;

    super.Load(stream, ttface, limit);
    /* the call to super.Load() has already positioned us to the correct file offset! */

Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "after glyph_header: glyf_offset: "+glyf_offset+" "+stream.pos());
   limit += glyf_offset;
    /* reading the contours' end points & number of points */
    contour_idx = 0;
    contour_limit = num_contours;
    if(num_contours >= 0xFFF || glyf_offset + (num_contours + 1) * 2 > limit) {
      return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
    }
    org_contours = new int[num_contours];
    for(contour_idx = 0; contour_idx < num_contours; contour_idx++) {
      org_contours[contour_idx] = stream.readShort();
    }
    contours = new int[num_contours];
    prev_contour = org_contours[0];
    if(num_contours > 0) {
      contours[0] = prev_contour;
    }
    if(prev_contour < 0) {
      return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
    }
    for(contour_idx = 1; contour_idx < contour_limit; contour_idx++) {
      contours[contour_idx] = org_contours[contour_idx];
      if(contours[contour_idx] <= prev_contour) {
        /* unordered contours: this is invalid */
        return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
      }
      prev_contour = contours[contour_idx];
    }
    num_points = 0;
    if(num_contours > 0) {
      num_points = contours[contour_idx - 1] + 1;
      if(num_points < 0) {
        return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
      }
    }

    /* note that we will add four phantom points later */
    points = new FTVectorRec[num_points + 4];

    /* reading the bytecode instructions */
    if(stream.pos() + 2 > limit) {
      return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
    }
    instructions_length = stream.readShort();
    if(instructions_length > ttface.getMax_profile().getMaxSizeOfInstructions()) {
      return FTError.ErrorTag.GLYPH_TOO_MANY_HINTS;
    }
    if(stream.pos() + instructions_length > limit) {
      return FTError.ErrorTag.GLYPH_TOO_MANY_HINTS;
    }
//    if(!load_flags.contains(NO_AUTOHINT) {
    // we always read the instructions!
      instructions = new byte[instructions_length];
      stream.readByteArray(instructions, instructions_length);
//    }

    /* reading the point tags */
    flag_idx = 0;
    tag_idx = 0;
    tag_limit = num_points;
    flags = new Set[num_points];
    tags = new Set[num_points];
    while(tag_idx < tag_limit) {
      byte ch;
      int count;

      if(stream.pos() + 1 > limit) {
        return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
      }
      ch = (byte)(stream.readByte() & 0xFF);
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("tag: %d 0x%02x", tag_idx, ch)+TTTags.GlyphFlags.GlyphFlagsSetToString(TTTags.GlyphFlags.makeTableTagSet(ch)));
      flags[flag_idx] = TTTags.GlyphFlags.makeTableTagSet(ch);
      tags[tag_idx++] = TTTags.GlyphFlags.makeTableTagSet(ch);
      if((ch & TTTags.GlyphFlags.REPEAT.getVal()) != 0) {
        if(stream.pos() + 1 > limit) {
          return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
        }
        count = stream.readByte();
        if(flag_idx + count > tag_limit) {
          return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
        }
        while(count > 0) {
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("tag2: %d 0x%02x", tag_idx, ch)+TTTags.GlyphFlags.GlyphFlagsSetToString(TTTags.GlyphFlags.makeTableTagSet(ch)));
          tags[tag_idx++] = TTTags.GlyphFlags.makeTableTagSet(ch);
          count--;
        }

      }
      flag_idx++;
    }

    points = new FTVectorRec[num_points];
    for(vec_idx = 0; vec_idx < num_points; vec_idx++) {
      points[vec_idx] = new FTVectorRec();
    }
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "pos before reading x: "+stream.pos());
    /* reading the X coordinates */
    x_coordinates = new int[num_points];
    tag_idx = 0;
    x = 0;
    if(stream.pos() > limit) {
      return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
    }
    for(vec_idx = 0; vec_idx < num_points; vec_idx++) {
      delta = 0;
      tag = tags[vec_idx];
      if(tag.contains(TTTags.GlyphFlags.X_SHORT)) {
        if(stream.pos() + 1 > limit) {
          return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
        }
        delta = stream.readByte() & 0xFF;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  x delta_byte: %d", delta));
        if(!tag.contains(TTTags.GlyphFlags.X_SAME)) {
          delta = -delta;
        }
      } else {
        if(!tag.contains(TTTags.GlyphFlags.X_SAME)) {
          if(stream.pos() + 2 > limit) {
            return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
          }
          delta = (short)stream.readShort();
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  x delta_short: %d", delta));
        }
      }
      x += delta;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  x: %d, delta: %d", x, delta));
      x_coordinates[vec_idx] = delta;
      points[vec_idx].setX(x);
      tags[tag_idx].remove(TTTags.GlyphFlags.X_SHORT);
      tags[tag_idx].remove(TTTags.GlyphFlags.X_SAME);
      tag_idx++;
    }

    /* reading the Y coordinates */
    y_coordinates = new int[num_points];
    tag_idx = 0;
    y = 0;
    if(stream.pos() > limit) {
      return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
    }
    for(vec_idx = 0; vec_idx < num_points; vec_idx++) {
      delta = 0;
      tag = tags[vec_idx];
      if(tag.contains(TTTags.GlyphFlags.Y_SHORT)) {
        if(stream.pos() + 1 > limit) {
          return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
        }
        delta = stream.readByte() & 0xFF;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  y delta_byte: %d", delta));
        if(!tag.contains(TTTags.GlyphFlags.Y_SAME)) {
          delta = -delta;
        }
      } else {
        if(!tag.contains(TTTags.GlyphFlags.Y_SAME)) {
          if(stream.pos() + 2 > limit) {
            return FTError.ErrorTag.GLYPH_INVALID_OUTLINE;
          }
          delta = (short)stream.readShort();
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  y delta_short: %d", delta));
        }
      }
      y += delta;
Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("  y: %d, delta: %d", y, delta));
      y_coordinates[vec_idx] = delta;
      points[vec_idx].setY(y);
      tags[tag_idx].remove(TTTags.GlyphFlags.Y_SHORT);
      tags[tag_idx].remove(TTTags.GlyphFlags.Y_SAME);
      tags[tag_idx].remove(TTTags.GlyphFlags.REPEAT);
      tags[tag_idx].remove(TTTags.GlyphFlags.RESERVED1);
      tags[tag_idx].remove(TTTags.GlyphFlags.RESERVED2);
      tag_idx++;
    }

int j;
for( j = 0; j < num_points; j++) {
  Debug(0, DebugTag.DBG_LOAD_GLYPH, TAG, String.format("tag4: %d 0x%02x", j, TTTags.GlyphFlags.GlyphFlagsSetToInt(tags[j])) + TTTags.GlyphFlags.GlyphFlagsSetToString(tags[j]));
}
    FTTrace.Trace(7, TAG, "loaded");
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, toDebugString());
    return error;
  }

  /* ==================== getContours ================================== */
  public int[] getContours() {
    return contours;
  }

  /* ==================== setContours ================================== */
  public void setContours(int[] contours) {
    this.contours = contours;
  }

  /* ==================== getInstructions_length ================================== */
  public int getInstructions_length() {
    return instructions_length;
  }

  /* ==================== setInstructions_length ================================== */
  public void setInstructions_length(int instructions_length) {
    this.instructions_length = instructions_length;
  }

  /* ==================== getInstructions ================================== */
  public byte[] getInstructions() {
    return instructions;
  }

  /* ==================== setInstructions ================================== */
  public void setInstructions(byte[] instructions) {
    this.instructions = instructions;
  }

  /* ==================== getFlags ================================== */
  public Set<TTTags.GlyphFlags>[] getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(Set<TTTags.GlyphFlags>[] flags) {
    this.flags = flags;
  }

  /* ==================== getX_coordinates ================================== */
  public int[] getX_coordinates() {
    return x_coordinates;
  }

  /* ==================== setX_coordinates ================================== */
  public void setX_coordinates(int[] x_coordinates) {
    this.x_coordinates = x_coordinates;
  }

  /* ==================== getY_coordinates ================================== */
  public int[] getY_coordinates() {
    return y_coordinates;
  }

  /* ==================== setY_coordinates ================================== */
  public void setY_coordinates(int[] y_coordinates) {
    this.y_coordinates = y_coordinates;
  }

  /* ==================== getTags ================================== */
  public Set<TTTags.GlyphFlags>[] getTags() {
    return tags;
  }

  /* ==================== getTags ================================== */
  public  Set<TTTags.GlyphFlags>getTag(int tag_idx) {
    return tags[tag_idx];
  }

  /* ==================== setTags ================================== */
  public void setTags(Set<TTTags.GlyphFlags>[] tags) {
    this.tags = tags;
  }

  /* ==================== getPoints ================================== */
  public FTVectorRec[] getPoints() {
    return points;
  }

  /* ==================== getPoint ================================== */
  public FTVectorRec getPoint(int point_idx) {
    return points[point_idx];
  }

  /* ==================== getPoint_x ================================== */
  public int getPoint_x(int point_idx) {
    return points[point_idx].getX();
  }

  /* ==================== getPoint_y ================================== */
  public int getPoint_y(int point_idx) {
    return points[point_idx].getY();
  }

  /* ==================== setPoints ================================== */
  public void setPoints(FTVectorRec[] points) {
    this.points = points;
  }

  /* ==================== getNum_points ================================== */
  public int getNum_points() {
    return num_points;
  }

  /* ==================== setNum_points ================================== */
  public void setNum_points(int num_points) {
    this.num_points = num_points;
  }

  /* ==================== getOrg_contours ================================== */
  public int[] getOrg_contours() {
    return org_contours;
  }

  /* ==================== setOrg_contours ================================== */
  public void setOrg_contours(int[] org_contours) {
    this.org_contours = org_contours;
  }

}