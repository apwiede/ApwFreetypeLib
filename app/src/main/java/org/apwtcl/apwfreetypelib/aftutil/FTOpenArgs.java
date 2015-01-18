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

package org.apwtcl.apwfreetypelib.aftutil;

import org.apwtcl.apwfreetypelib.aftbase.FTModuleRec;

import java.io.*;

  /* ===================================================================== */
  /*    FTOpenArgs                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to indicate how to open a new font file or        */
  /*    stream.  A pointer to such a structure can be used as a parameter  */
  /*    for the functions @FT_Open_Face and @FT_Attach_Stream.             */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    flags       :: A set of bit flags indicating how to use the        */
  /*                   structure.                                          */
  /*                                                                       */
  /*    memory_base :: The first byte of the file in memory.               */
  /*                                                                       */
  /*    memory_size :: The size in bytes of the file in memory.            */
  /*                                                                       */
  /*    pathname    :: A pointer to an 8-bit file pathname.                */
  /*                                                                       */
  /*    stream      :: A handle to a source stream object.                 */
  /*                                                                       */
  /*    driver      :: This field is exclusively used by @FT_Open_Face;    */
  /*                   it simply specifies the font driver to use to open  */
  /*                   the face.  If set to~0, FreeType tries to load the  */
  /*                   face with each one of the drivers in its list.      */
  /*                                                                       */
 /*    num_params  :: The number of extra parameters.                     */
  /*                                                                       */
  /*    params      :: Extra parameters passed to the font driver when     */
  /*                   opening a new face.                                 */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The stream type is determined by the contents of `flags' which     */
  /*    are tested in the following order by @FT_Open_Face:                */
  /*                                                                       */
  /*    If the `FT_OPEN_MEMORY' bit is set, assume that this is a          */
  /*    memory file of `memory_size' bytes, located at `memory_address'.   */
  /*    The data are are not copied, and the client is responsible for     */
  /*    releasing and destroying them _after_ the corresponding call to    */
  /*    @FT_Done_Face.                                                     */
  /*                                                                       */
  /*    Otherwise, if the `FT_OPEN_STREAM' bit is set, assume that a       */
  /*    custom input stream `stream' is used.                              */
  /*                                                                       */
  /*    Otherwise, if the `FT_OPEN_PATHNAME' bit is set, assume that this  */
  /*    is a normal file and use `pathname' to open it.                    */
  /*                                                                       */
  /*    If the `FT_OPEN_DRIVER' bit is set, @FT_Open_Face only tries to    */
 /*    open the file with the driver whose handler is in `driver'.        */
  /*                                                                       */
  /*    If the `FT_OPEN_PARAMS' bit is set, the parameters given by        */
  /*    `num_params' and `params' is used.  They are ignored otherwise.    */
  /*                                                                       */
  /*    Ideally, both the `pathname' and `params' fields should be tagged  */
  /*    as `const'; this is missing for API backwards compatibility.  In   */
  /*    other words, applications should treat them as read-only.          */
  /*                                                                       */
  /* ===================================================================== */

public class FTOpenArgs extends FTDebug {
  private static int oid = 0;

  private int id;
  private static String TAG = "FTOpenArgs";

  private int flags = 0;
  private String pathname = null;
  private FTStreamRec stream = null;
  private FTModuleRec driver = null;
  private int num_params = 0;
  private FTParameter[] params = null;

  /* ==================== FTOpenArgs ================================== */
  public FTOpenArgs() {
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
    str.append("..flags: "+flags+'\n');
    str.append("..pathname: "+pathname+'\n');
    str.append("..num_params: "+num_params+'\n');
    return str.toString();
  }

  /* ==================== getFlags ================================== */
  public int getFlags() {
    return flags;
  }

  /* ==================== setFlags ================================== */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /* ==================== getPathname ================================== */
  public String getPathname() {
    return pathname;
  }

  /* ==================== setPathname ================================== */
  public void setPathname(String pathname) {
    this.pathname = pathname;
  }

  /* ==================== getStream ================================== */
  public FTStreamRec getStream() {
    return stream;
  }

  /* ==================== setStream ================================== */
  public void setStream(FTStreamRec stream) {
    this.stream = stream;
  }

  /* ==================== getDriver ================================== */
  public FTModuleRec getDriver() {
    return driver;
  }

  /* ==================== setDriver ================================== */
  public void setDriver(FTModuleRec driver) {
    this.driver = driver;
  }

  /* ==================== getNum_params ================================== */
  public int getNum_params() {
    return num_params;
  }

  /* ==================== setNum_params ================================== */
  public void setNum_params(int num_params) {
    this.num_params = num_params;
  }

  /* ==================== getParams ================================== */
  public FTParameter[] getParams() {
    return params;
  }

  /* ==================== setParams ================================== */
  public void setParams(FTParameter[] params) {
    this.params = params;
  }

}