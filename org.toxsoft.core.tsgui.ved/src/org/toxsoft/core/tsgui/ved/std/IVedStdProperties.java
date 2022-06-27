package org.toxsoft.core.tsgui.ved.std;

import static org.toxsoft.core.tsgui.ved.std.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
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
  // Component bounds
  //

  /**
   * ID of property {@link #PDEF_X}.
   */
  String PID_X = "x"; //$NON-NLS-1$

  /**
   * Property: components X coordinate.
   */
  IDataDef PDEF_X = DataDef.create( PID_X, EAtomicType.FLOATING, //
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
  IDataDef PDEF_Y = DataDef.create( PID_Y, EAtomicType.FLOATING, //
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
  IDataDef PDEF_WIDTH = DataDef.create( PID_WIDTH, EAtomicType.FLOATING, //
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
  IDataDef PDEF_HEIGHT = DataDef.create( PID_HEIGHT, EAtomicType.FLOATING, //
      TSID_NAME, STR_N_HEIGHT, //
      TSID_DESCRIPTION, STR_D_HEIGHT, //
      TSID_DEFAULT_VALUE, avFloat( 10.0 ) //
  );

  // ------------------------------------------------------------------------------------
  // Rotate
  //

  /**
   * ID of property {@link #PDEF_ROTATION_ANGLE}.
   */
  String PID_ROTATION_ANGLE = "height"; //$NON-NLS-1$

  /**
   * Property: component rotation angle.
   */
  IDataDef PDEF_ROTATION_ANGLE = DataDef.create( PID_ROTATION_ANGLE, EAtomicType.FLOATING, //
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
