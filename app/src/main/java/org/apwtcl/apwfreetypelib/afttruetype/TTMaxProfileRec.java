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
  /*    TTMaxProfile                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The maximum profile is a table containing many max values which    */
  /*    can be used to pre-allocate arrays.  This ensures that no memory   */
  /*    allocation occurs during a glyph load.                             */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    version               :: The version number.                       */
  /*                                                                       */
  /*    numGlyphs             :: The number of glyphs in this TrueType     */
  /*                             font.                                     */
  /*                                                                       */
  /*    maxPoints             :: The maximum number of points in a         */
  /*                             non-composite TrueType glyph.  See also   */
  /*                             the structure element                     */
  /*                             `maxCompositePoints'.                     */
  /*                                                                       */
  /*    maxContours           :: The maximum number of contours in a       */
  /*                             non-composite TrueType glyph.  See also   */
  /*                             the structure element                     */
  /*                             `maxCompositeContours'.                   */
  /*                                                                       */
  /*    maxCompositePoints    :: The maximum number of points in a         */
  /*                             composite TrueType glyph.  See also the   */
  /*                             structure element `maxPoints'.            */
  /*                                                                       */
  /*    maxCompositeContours  :: The maximum number of contours in a       */
  /*                             composite TrueType glyph.  See also the   */
  /*                             structure element `maxContours'.          */
  /*                                                                       */
  /*    maxZones              :: The maximum number of zones used for      */
  /*                             glyph hinting.                            */
  /*                                                                       */
  /*    maxTwilightPoints     :: The maximum number of points in the       */
  /*                             twilight zone used for glyph hinting.     */
  /*                                                                       */
  /*    maxStorage            :: The maximum number of elements in the     */
  /*                             storage area used for glyph hinting.      */
  /*                                                                       */
  /*    maxFunctionDefs       :: The maximum number of function            */
  /*                             definitions in the TrueType bytecode for  */
  /*                             this font.                                */
  /*                                                                       */
  /*    maxInstructionDefs    :: The maximum number of instruction         */
  /*                             definitions in the TrueType bytecode for  */
  /*                             this font.                                */
  /*                                                                       */
  /*    maxStackElements      :: The maximum number of stack elements used */
  /*                             during bytecode interpretation.           */
  /*                                                                       */
  /*    maxSizeOfInstructions :: The maximum number of TrueType opcodes    */
  /*                             used for glyph hinting.                   */
  /*                                                                       */
  /*    maxComponentElements  :: The maximum number of simple (i.e., non-  */
  /*                             composite) glyphs in a composite glyph.   */
  /*                                                                       */
  /*    maxComponentDepth     :: The maximum nesting depth of composite    */
  /*                             glyphs.                                   */
  /*                                                                       */
  /* <Note>                                                                */
  /*    This structure is only used during font loading.                   */
  /*                                                                       */
  /* ===================================================================== */


import org.apwtcl.apwfreetypelib.aftutil.FTDebug;
import org.apwtcl.apwfreetypelib.aftutil.FTError;
import org.apwtcl.apwfreetypelib.aftutil.FTReference;
import org.apwtcl.apwfreetypelib.aftutil.FTStreamRec;
import org.apwtcl.apwfreetypelib.aftutil.FTTrace;

public class TTMaxProfileRec extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "TTMaxProfile";

  private int version = 0;
  private int num_glyphs = 0;

  private int max_points = 0;
  private int max_contours = 0;
  private int max_composite_points = 0;
  private int max_composite_contours = 0;
  private int max_zones = 0;
  private int max_twilight_points = 0;
  private int max_storage = 0;
  private int max_function_defs = 0;
  private int max_instruction_defs = 0;
  private int max_stack_elements = 0;
  private int max_size_of_instructions = 0;
  private int max_component_elements = 0;
  private int max_component_depth = 0;

  /* ==================== TTMaxProfile ================================== */
  public TTMaxProfileRec() {
    oid++;
    id = oid;
  }
    
  /* ==================== Load ================================== */
  public FTError.ErrorTag Load(FTStreamRec stream, TTFaceRec ttface) {
Debug(0, DebugTag.DBG_LOAD_FACE, TAG, "Load offset: "+String.format("0x%08x", stream.pos()));
    FTError.ErrorTag error = FTError.ErrorTag.ERR_OK;
    FTReference<Integer> length_ref = new FTReference<Integer>();

    error = ttface.gotoTable(TTTags.Table.maxp, stream, length_ref);
    if (error != FTError.ErrorTag.ERR_OK) {
      return error;
    }
    version = stream.readInt();
    num_glyphs = stream.readShort();
    if (version >= 0x10000L) {
      max_points = stream.readShort();
      max_contours = stream.readShort();
      max_composite_points = stream.readShort();
      max_composite_contours = stream.readShort();
      max_zones = stream.readShort();
      max_twilight_points = stream.readShort();
      max_storage = stream.readShort();
      max_function_defs = stream.readShort();
      max_instruction_defs = stream.readShort();
      max_stack_elements = stream.readShort();
      max_size_of_instructions = stream.readShort();
      max_component_elements = stream.readShort();
      max_component_depth = stream.readShort();
      if (error != FTError.ErrorTag.ERR_OK) {
        return error;
      }
        /* XXX: an adjustment that is necessary to load certain */
        /* broken fonts like `Keystrokes MT' :-( */
        /*                            */
        /* We allocate 64 function entries by default when */
        /* the maxFunctionDefs value is smaller. */
      if (max_function_defs < 64) {
        max_function_defs = 64;
      }
        /* we add 4 phantom points later */
      if (max_twilight_points > (0xFFFF - 4)) {
        FTTrace.Trace(7, TAG, "tt_face_load_maxp:"
            + " too much twilight points in `maxp' table;\n"
            + "             "
            + " some glyphs might be rendered incorrectly");
        max_twilight_points = (short) (0xFFFF - 4);
      }
        /* we arbitrarily limit recursion to avoid stack exhaustion */
      if (max_component_depth > 100) {
        FTTrace.Trace(7, TAG, "tt_face_load_maxp:" +
            String.format( " abnormally large component depth (%d) set to 100",
                max_component_depth));
        max_component_depth = 100;
      }
    }
    FTTrace.Trace(7, TAG, String.format("numGlyphs: %d\n", num_glyphs));
    return error;
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

  /* ==================== getVersion ===================================== */
  public int getVersion() {
    return version;
  }

  /* ==================== getNumGlyphs ===================================== */
  public int getNumGlyphs(){
    return num_glyphs;
  }

  /* ==================== getMaxPoints ===================================== */
  public int getMaxPoints() {
    return max_points;
  }

  /* ==================== getMaxContours ===================================== */
  public int getMaxContours() {
    return max_contours;
  }

  /* ==================== getMaxCompositePoints ===================================== */
  public int getMaxCompositePoints() {
    return max_composite_points;
  }

  /* ==================== getMaxCompositeContours ===================================== */
  public int getMaxCompositeContours() {
    return max_composite_contours;
  }

  /* ==================== getMaxZones ===================================== */
  public int getMaxZones() {
    return max_zones;
  }

  /* ==================== getMaxTwilightPoints ===================================== */
  public int getMaxTwilightPoints() {
    return max_twilight_points;
  }

  /* ==================== getMaxStorage ===================================== */
  public int getMaxStorage() {
    return max_storage;
  }

  /* ==================== getMaxFunctionDefs ===================================== */
  public int getMaxFunctionDefs() {
    return max_function_defs;
  }

  /* ==================== getMaxInstructionDefs ===================================== */
  public int getMaxInstructionDefs() {
    return max_instruction_defs;
  }

  /* ==================== getStackElements ===================================== */
  public int getMaxStackElements() {
    return max_stack_elements;
  }

  /* ==================== getMaxSizeOfInstructions ===================================== */
  public int getMaxSizeOfInstructions() {
    return max_size_of_instructions;
  }

  /* ==================== getComponentElements ===================================== */
  public int getMaxComponentElements() {
    return max_component_elements;
  }

  /* ==================== getMaxComponentDepth ===================================== */
  public int getMaxComponentDepth() {
    return max_component_depth;
  }

}