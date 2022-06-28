package org.toxsoft.core.tsgui.ved.std;

import static org.toxsoft.core.tsgui.ved.std.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Standard properties for all components.
 * <p>
 * Not all components have all of these properties. Hoveve, if component declared standard property then property must
 * behave exactly as described here.
 *
 * @author hazard157
 */
public interface IVedStdProperties {

  // ------------------------------------------------------------------------------------
  // Identification
  //

  /**
   * ID of property {@link #PDEF_NAME}.
   */
  String PID_NAME = "name"; //$NON-NLS-1$

  /**
   * Property: human-readable short name.
   */
  IDataDef PDEF_NAME = DataDef.createOverride2( PID_NAME, DDEF_NAME, //
      TSID_NAME, STR_N_NAME, //
      TSID_DESCRIPTION, STR_D_NAME //
  );

  /**
   * ID of property {@link #PDEF_DESCRIPTION}.
   */
  String PID_DESCRIPTION = "description"; //$NON-NLS-1$

  /**
   * Property: optional description.
   */
  IDataDef PDEF_DESCRIPTION = DataDef.createOverride2( PID_DESCRIPTION, DDEF_DESCRIPTION, //
      TSID_NAME, STR_N_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_DESCRIPTION //
  );

  // ------------------------------------------------------------------------------------
  // Component bounds
  //

  /**
   * ID of property {@link #PDEF_X}.
   */
  String PID_X = "x"; //$NON-NLS-1$

  /**
   * Property: components X coordinate.
   */
  IDataDef PDEF_X = DataDef.create( PID_X, FLOATING, //
      TSID_NAME, STR_N_X, //
      TSID_DESCRIPTION, STR_D_X, //
      TSID_DEFAULT_VALUE, AV_F_0 //
  );

  /**
   * ID of property {@link #PDEF_Y}.
   */
  String PID_Y = "x"; //$NON-NLS-1$

  /**
   * Property: components Y coordinate.
   */
  IDataDef PDEF_Y = DataDef.create( PID_Y, FLOATING, //
      TSID_NAME, STR_N_Y, //
      TSID_DESCRIPTION, STR_D_Y, //
      TSID_DEFAULT_VALUE, AV_F_0 //
  );

  /**
   * ID of property {@link #PDEF_WIDTH}.
   */
  String PID_WIDTH = "width"; //$NON-NLS-1$

  /**
   * Property: component Y width.
   */
  IDataDef PDEF_WIDTH = DataDef.create( PID_WIDTH, FLOATING, //
      TSID_NAME, STR_N_WIDTH, //
      TSID_DESCRIPTION, STR_D_WIDTH, //
      TSID_DEFAULT_VALUE, avFloat( 10.0 ) //
  );

  /**
   * ID of property {@link #PDEF_HEIGHT}.
   */
  String PID_HEIGHT = "height"; //$NON-NLS-1$

  /**
   * Property: component Y width.
   */
  IDataDef PDEF_HEIGHT = DataDef.create( PID_HEIGHT, FLOATING, //
      TSID_NAME, STR_N_HEIGHT, //
      TSID_DESCRIPTION, STR_D_HEIGHT, //
      TSID_DEFAULT_VALUE, avFloat( 10.0 ) //
  );

  // ------------------------------------------------------------------------------------
  // Component colors
  //

  /**
   * ID of property {@link #PDEF_FG_COLOR}.
   */
  String PID_FG_COLOR = "fgColor"; //$NON-NLS-1$

  /**
   * Property: components foreground color.
   */
  IDataDef PDEF_FG_COLOR = DataDef.create( PID_FG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_FG_COLOR, //
      TSID_DESCRIPTION, STR_D_FG_COLOR, //
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgb() ) //
  );

  /**
   * ID of property {@link #PDEF_BG_COLOR}.
   */
  String PID_BG_COLOR = "bgColor"; //$NON-NLS-1$

  /**
   * Property: components background color.
   */
  IDataDef PDEF_BG_COLOR = DataDef.create( PID_BG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_BG_COLOR, //
      TSID_DESCRIPTION, STR_D_BG_COLOR, //
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.WHITE.rgb() ) //
  );

  // ------------------------------------------------------------------------------------
  // Rotate
  //

  /**
   * ID of property {@link #PDEF_ROTATION_ANGLE}.
   */
  String PID_ROTATION_ANGLE = "rotationAngle"; //$NON-NLS-1$

  /**
   * Property: component rotation angle.
   */
  IDataDef PDEF_ROTATION_ANGLE = DataDef.create( PID_ROTATION_ANGLE, FLOATING, //
      TSID_NAME, STR_N_ROTATION_ANGLE, //
      TSID_DESCRIPTION, STR_D_ROTATION_ANGLE, //
      TSID_DEFAULT_VALUE, avFloat( 10.0 ) //
  );

  IDataDef PDEF_ROTATION_CENTER = null;

  // ------------------------------------------------------------------------------------
  // Flip
  //

  IDataDef PDEF_HOR_FLIP = null;

  IDataDef PDEF_VER_FLIP = null;

  // ------------------------------------------------------------------------------------
  // Zoom
  //

  IDataDef PDEF_ZOOM_X = null;

  IDataDef PDEF_ZOOM_Y = null;

}
