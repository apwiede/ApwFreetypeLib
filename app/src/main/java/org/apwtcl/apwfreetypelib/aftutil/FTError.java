/* =====================================================================
 *  This Java implementation is derived from FreeType code
 *  Portions of this software are copyright (C) 2014 The FreeType
 *  Project (www.freetype.org).  All rights reserved.
 * 
 *  Copyright (C) of the Java implementation 2014
 *  Arnulf Wiedemann arnulf at wiedemann-pri.de
 *
 *  See the file "license.terms" for information on usage and
 *  redistribution of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 * =====================================================================
 */

package org.apwtcl.apwfreetypelib.aftutil;


  /* ===================================================================== */
  /*    FT_Error                                                             */
  /*                                                                       */
  /* ===================================================================== */

import android.util.SparseArray;

public class FTError extends FTDebug {
    private static int oid = 0;

    private int id;
    private static String TAG = "FT_Error";

    public enum ErrorTag {
      ERR_OK(0, "ERR_OK"),

      CMAP_INVALID_CHARMAP_HANDLE(-10, "CMAP_INVALID_CHARMAP_HANDLE"),

      FACE_BAD_FACE_OBJECT_TYPE(-11, "FACE_BAD_FACE_OBJECT_TYPE"),
      FACE_BAD_SIZE_OBJECT_TYPE(-12, "FACE_BAD_SIZE_OBJECT_TYPE"),
      FACE_NO_SIZE_REQUEST_FUNCTION(-13, "FACE_NO_SIZE_REQUEST_FUNCTION"),
      FACE_NO_SELECT_SIZE_FUNCTION(-14, "FACE_NO_SELECT_SIZE_FUNCTION"),

      GR_BAD_ARGUMENT(-15, "GR_BAD_ARGUMENT"),
      GR_BAD_TARGET_DEPTH(-16, "GR_BAD_TARGET_DEPTH"),

      GLYPH_ARRAY_TOO_LARGE(-20, "GLYPH_ARRAY_TOO_LARGE"),
      GLYPH_BAD_CACHE_CLASS_TYPE(-21, "GLYPH_BAD_CACHE_CLASS_TYPE"),
      GLYPH_COULD_NOT_FIND_CONTEXT(-22, "GLYPH_COULD_NOT_FIND_CONTEXT"),
      GLYPH_CANNOT_RENDER_GLYPH(-23, "GLYPH_CANNOT_RENDER_GLYPH"),
      GLYPH_INVALID_ARGUMENT(-24, "GLYPH_INVALID_ARGUMENT"),
      GLYPH_INVALID_CACHE_HANDLE(-25, "GLYPH_INVALID_CACHE_HANDLE"),
      GLYPH_INVALID_COMPOSITE(-26, "GLYPH_INVALID_COMPOSITE"),
      GLYPH_INVALID_FACE_HANDLE(-27, "GLYPH_INVALID_FACE_HANDLE"),
      GLYPH_INVALID_GLYPH_FORMAT(-28, "GLYPH_INVALID_GLYPH_FORMAT"),
      GLYPH_INVALID_GLYPH_INDEX(-29, "GLYPH_INVALID_GLYPH_INDEX"),
      GLYPH_INVALID_LIBRARY_HANDLE(-30, "GLYPH_INVALID_LIBRARY_HANDLE"),
      GLYPH_INVALID_OUTLINE(-31, "GLYPH_INVALID_OUTLINE"),
      GLYPH_INVALID_PIXEL_SIZE(-32, "GLYPH_INVALID_PIXEL_SIZE"),
      GLYPH_INVALID_PPEM(-33, "GLYPH_INVALID_PPEM"),
      GLYPH_INVALID_TABLE(-34, "GLYPH_INVALID_TABLE"),
      GLYPH_OUT_OF_MEMORY(-35, "GLYPH_OUT_OF_MEMORY"),
      GLYPH_ROTTEN_GLYPH(-36, "GLYPH_ROTTEN_GLYPH"),
      GLYPH_TABLE_MISSING(-37, "GLYPH_TABLE_MISSING"),
      GLYPH_TOO_MANY_CACHES(-38, "GLYPH_TOO_MANY_CACHES"),
      GLYPH_TOO_MANY_HINTS(-39, "GLYPH_TOO_MANY_HINTS"),
      GLYPH_TOO_MANY_POINTS(-40, "GLYPH_TOO_MANY_POINTS"),
      GLYPH_UNIMPLEMENTED_FEATURE(-41, "GLYPH_UNIMPLEMENTED_FEATURE"),

      INIT_INVALID_LIBRARY_HANDLE(-50, "INIT_INVALID_LIBRARY_HANDLE"),
      INIT_INVALID_ARGUMENT(-51, "INIT_INVALID_ARGUMENT"),
      INIT_LOWER_MODULE_VERSION(-52, "INIT_LOWER_MODULE_VERSION"),
      INIT_TOO_MANY_DRIVERS(-53, "INIT_TOO_MANY_DRIVERS"),
      INIT_BAD_ARGUMENT(-54, "INIT_BAD_ARGUMENT"),

      INTERP_ARRAY_BOUND_ERROR(-60, "INTERP_ARRAY_BOUND_ERROR"),
      INTERP_ARRAY_TOO_LARGE(-61, "INTERP_ARRAY_TOO_LARGE"),
      INTERP_BAD_ARGUMENT(-62, "INTERP_BAD_ARGUMENT"),
      INTERP_CANNOT_OPEN_STREAM(-63, "INTERP_CANNOT_OPEN_STREAM"),
      INTERP_CODE_OVERFLOW(-64, "INTERP_CODE_OVERFLOW"),
      INTERP_COULD_NOT_FIND_CONTEXT(-65, "INTERP_COULD_NOT_FIND_CONTEXT"),
      INTERP_DEBUG_OPCODE(-66, "INTERP_DEBUG_OPCODE"),
      INTERP_DIVIDE_BY_ZERO(-67, "INTERP_DIVIDE_BY_ZERO"),
      INTERP_ENDF_IN_EXEC_STREAM(-68, "INTERP_ENDF_IN_EXEC_STREAM"),
      INTERP_EXECUTION_TOO_LONG(-69, "INTERP_EXECUTION_TOO_LONG"),
      INTERP_INVALID_ARGUMENT(-70, "INTERP_INVALID_ARGUMENT"),
      INTERP_INVALID_CHARMAP_HANDLE(-71, "INTERP_INVALID_CHARMAP_HANDLE"),
      INTERP_INVALID_CODE_RANGE(-72, "INTERP_INVALID_CODE_RANGE"),
      INTERP_INVALID_DRIVER_HANDLE(-73, "INTERP_INVALID_DRIVER_HANDLE"),
      INTERP_INVALID_FACE_HANDLE(-74, "INTERP_INVALID_FACE_HANDLE"),
      INTERP_INVALID_HANDLE(-75, "INTERP_INVALID_HANDLE"),
      INTERP_INVALID_LIBRARY_HANDLE(-76, "INTERP_INVALID_LIBRARY_HANDLE"),
      INTERP_INVALID_OPCODE(-77, "INTERP_INVALID_OPCODE"),
      INTERP_INVALID_REFERENCE(-78, "INTERP_INVALID_REFERENCE"),
      INTERP_INVALID_SIZE_HANDLE(-79, "INTERP_INVALID_SIZE_HANDLE"),
      INTERP_INVALID_TABLE(-80, "INTERP_INVALID_TABLE"),
      INTERP_LOWER_MODULE_VERSION(-82, "INTERP_LOWER_MODULE_VERSION"),
      INTERP_MISSING_MODULE(-83, "INTERP_MISSING_MODULE"),
      INTERP_NESTED_DEFS(-84, "INTERP_NESTED_DEFS"),
      INTERP_NOT_YET_IMPLEMENTED(-85, "INTERP_NOT_YET_IMPLEMENTED"),
      INTERP_STACK_OVERFLOW(-86, "INTERP_STACK_OVERFLOW"),
      INTERP_TABLE_LOCATIONS_MISSING(-87, "INTERP_TABLE_LOCATIONS_MISSING"),
      INTERP_TOO_FEW_ARGUMENTS(-88, "INTERP_TOO_FEW_ARGUMENTS"),
      INTERP_TOO_MANY_DRIVERS(-89, "INTERP_TOO_MANY_DRIVERS"),
      INTERP_TOO_MANY_FUNCTION_DEFS(-90, "INTERP_TOO_MANY_FUNCTION_DEFS"),
      INTERP_TOO_MANY_INSTRUCTION_DEFS(-91, "INTERP_TOO_MANY_INSTRUCTION_DEFS"),
      INTERP_UNKNOWN_FILE_FORMAT(-92, "INTERP_UNKNOWN_FILE_FORMAT"),

      LOAD_ARRAY_TOO_LARGE(-110, "LOAD_ARRAY_TOO_LARGE"),
      LOAD_CANNOT_OPEN_STREAM(-111, "LOAD_CANNOT_OPEN_STREAM"),
      LOAD_HMTX_TABLE_MISSING(-112, "LOAD_HMTX_TABLE_MISSING"),
      LOAD_HORIZ_HEADER_MISSING(-113, "LOAD_HORIZ_HEADER_MISSING"),
      LOAD_INVALID_ARGUMENT(-114, "LOAD_INVALID_ARGUMENT"),
      LOAD_INVALID_CHARMAP_HANDLE(-115, "LOAD_INVALID_CHARMAP_HANDLE"),
      LOAD_INVALID_DATA(-116, "LOAD_INVALID_DATA"),
      LOAD_INVALID_FACE_HANDLE(-117, "LOAD_INVALID_FACE_HANDLE"),
      LOAD_INVALID_FILE_FORMAT(-118, "LOAD_INVALID_FILE_FORMAT"),
      LOAD_INVALID_GLYPH_ID(-119, "LOAD_INVALID_GLYPH_ID"),
      LOAD_INVALID_GLYPH_FORMAT(-130, "LOAD_INVALID_GLYPH_FORMAT"),
      LOAD_INVALID_HANDLE(-131,"LOAD_INVALID_HANDLE"),
      LOAD_INVALID_SLOT_HANDLE(-132, "LOAD_INVALID_SLOT_HANDLE"),
      LOAD_INVALID_SIZE_HANDLE(-133, "LOAD_INVALID_SIZE_HANDLE"),
      LOAD_INVALID_STREAM_OPERATION(-134, "LOAD_INVALID_STREAM_OPERATION"),
      LOAD_INVALID_TABLE(-135, "LOAD_INVALID_TABLE"),
      LOAD_MISSING_MODULE(-136,"LOAD_MISSING_MODULE"),
      LOAD_NAME_TABLE_MISSING(-137, "LOAD_NAME_TABLE_MISSING"),
      LOAD_STREAM_CANNOT_SEEK(-138, "LOAD_STREAM_CANNOT_SEEK"),
      LOAD_STREAM_READ_AFTER_SIZE(-139, "LOAD_STREAM_READ_AFTER_SIZE"),
      LOAD_STREAM_READ_ERROR(-140, "LOAD_STREAM_READ_ERROR"),
      LOAD_TABLE_MISSING(-141, "LOAD_TABLE_MISSING"),
      LOAD_UNKNOWN_FILE_FORMAT(-142, "LOAD_UNKNOWN_FILE_FORMAT"),

      RASTER_ERR_INVALID(-160, "RASTER_ERR_INVALID"),
      RASTER_ERR_NEG_HEIGHT(-161, "RASTER_ERR_NEG_HEIGHT"),
      RASTER_ERR_NOT_INI(-162, "RASTER_ERR_NOT_INI"),
      RASTER_ERR_OVERFLOW(-163, "RASTER_ERR_OVERFLOW"),
      RASTER_MEMORY_OVERFLOW(-164, "RASTER_MEMORY_OVERFLOW"),
      RASTER_ERR_UNSUPPORTED(-165, "RASTER_ERR_UNSUPPORTED"),

      RENDER_INVALID_ARGUMENT(-170, "RENDER_INVALID_ARGUMENT"),
      RENDER_INVALID_LIBRARY_HANDLE(-171, "RENDER_INVALID_LIBRARY_HANDLE"),
      RENDER_INVALID_MODE(-172, "RENDER_INVALID_MODE"),
      RENDER_INVALID_OUTLINE(-173, "RENDER_INVALID_OUTLINE"),
      RENDER_INVALID_PIXEL_SIZE(-174, "RENDER_INVALID_PIXEL_SIZE"),
      RENDER_RASTER_OVERFLOW(-175, "RENDER_RASTER_OVERFLOW"),

      UNEXPECTED_NULL_VALUE(-179, "UNEXPECTED_NULL_VALUE"),

      VALID_INVALID_TOO_SHORT(-180, "VALID_INVALID_TOO_SHORT"),
      VALID_INVALID_GLYPH_ID(-181, "VALID_INVALID_GLYPH_ID");

      private int val;
      private String str;
      private static SparseArray<ErrorTag> tagToErrorTagMapping;
      public static ErrorTag getTableTag(int i) {
        if (tagToErrorTagMapping == null) {
          initMapping();
        }
        return tagToErrorTagMapping.get(i);
      }
      private static void initMapping() {
        tagToErrorTagMapping = new SparseArray<ErrorTag>();
        for (ErrorTag t : values()) {
          tagToErrorTagMapping.put(t.val, t);
        }
      }
      private ErrorTag(int val, String str) {
        this.val = val;
        this.str = str;
      }
      public int getVal() {
        return val;
      }
      public String getDescription() {
        return str;
      }
    }

    private final static int MAX_ERRORS = 150;

    /* ==================== FTError ================================== */
    public FTError() {
      oid++;
      id = oid;      

    }
    
    /* ==================== mySelf ================================== */
    public String mySelf() {
      String str = TAG+"!"+id+"!";
      return str;
    } 
        
    /* ==================== toString ===================================== */
    public String toString() {
      StringBuffer str = new StringBuffer(mySelf()+"!");
      return str.toString();
    }

    /* ==================== toDebugString ===================================== */
    public String toDebugString() {
      StringBuffer str = new StringBuffer(mySelf()+"\n");
      return str.toString();
    }
 
    /* ==================== toDebugString ===================================== */
    public static String GetErrorString(ErrorTag errno) {
      StringBuffer str = new StringBuffer(String.format("Error: %d %s", errno, errno.getDescription()));
      return str.toString();
    }
 
}