package org.toxsoft.core.tsgui.ved.std;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Standard properties for all components.
 * <p>
 * Not all components have all of these properties. Hovever, if component declared standard property then property must
 * behave exactly as described here.
 *
 * @author hazard157
 */
public interface IVedStdProperties {

  // ------------------------------------------------------------------------------------
  // ID prefixes for VED builtin entities providers
  //

  /**
   * ID prefix of the component providers {@link IVedEntityProvider}.
   */
  String VED_COMP_ID = VED_ID + ".comp"; //$NON-NLS-1$

  /**
   * ID prefix of the tailor providers {@link IVedEntityProvider}.
   */
  String VED_TAILOR_ID = VED_ID + ".tailor"; //$NON-NLS-1$

  /**
   * ID prefix of the actor providers {@link IVedEntityProvider}.
   */
  String VED_ACTOR_ID = VED_ID + ".actor"; //$NON-NLS-1$

  /**
   * ID prefix of the binding providers {@link IVedBindingProvider}.
   */
  String VED_BIND_ID = VED_ID + ".bind"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Identification
  //

  /**
   * ID of the property {@link #PDEF_NAME}.
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
   * ID of the property {@link #PDEF_DESCRIPTION}.
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
   * ID of the property {@link #PDEF_X}.
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
   * ID of the property {@link #PDEF_Y}.
   */
  String PID_Y = "y"; //$NON-NLS-1$

  /**
   * Property: components Y coordinate.
   */
  IDataDef PDEF_Y = DataDef.create( PID_Y, FLOATING, //
      TSID_NAME, STR_N_Y, //
      TSID_DESCRIPTION, STR_D_Y, //
      TSID_DEFAULT_VALUE, AV_F_0 //
  );

  /**
   * ID of the property {@link #PDEF_WIDTH}.
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
   * ID of the property {@link #PDEF_HEIGHT}.
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
  // Component drawing properties
  //

  /**
   * ID of the property {@link #PDEF_FG_COLOR}.
   */
  String PID_FG_COLOR = "fgColor"; //$NON-NLS-1$

  /**
   * Property: components foreground color.
   */
  IDataDef PDEF_FG_COLOR = DataDef.create( PID_FG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_FG_COLOR, //
      TSID_DESCRIPTION, STR_D_FG_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, TSID_DEFAULT_VALUE,
      avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * ID of the property {@link #PDEF_BG_COLOR}.
   */
  String PID_BG_COLOR = "bgColor"; //$NON-NLS-1$

  /**
   * Property: components background color.
   */
  IDataDef PDEF_BG_COLOR = DataDef.create( PID_BG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_BG_COLOR, //
      TSID_DESCRIPTION, STR_D_BG_COLOR, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 255, 255, 255, 255 ) ) //
  );

  /**
   * ID of the property {@link #PDEF_FILL_INFO}.
   */
  String PID_FILL_INFO = "fillInfo"; //$NON-NLS-1$

  /**
   * Property: information how to draw components background.
   */
  IDataDef PDEF_FILL_INFO = DataDef.create( PID_FILL_INFO, VALOBJ, //
      TSID_NAME, STR_N_FILL_INFO, //
      TSID_DESCRIPTION, STR_D_FILL_INFO, //
      TSID_KEEPER_ID, TsFillInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsFillInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsFillInfo.NONE ) //
  );

  /**
   * ID of the property {@link #PDEF_BORDER_INFO}.
   */
  String PID_BORDER_INFO = "borderInfo"; //$NON-NLS-1$

  /**
   * Property: rectanglar outline (border, frame) drawing info.
   */
  IDataDef PDEF_BORDER_INFO = DataDef.create( PID_BORDER_INFO, VALOBJ, //
      TSID_NAME, STR_N_BORDER_INFO, //
      TSID_DESCRIPTION, STR_D_BORDER_INFO, //
      TSID_KEEPER_ID, TsBorderInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsBorderInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsBorderInfo.createSimpleBorder( 1, new RGBA( 0, 0, 0, 0 ) ) ) //
  );

  /**
   * ID of the property {@link #PDEF_OUTLINE_INFO}.
   */
  String PID_OUTLINE_INFO = "outlineInfo"; //$NON-NLS-1$

  /**
   * Property: outline drawing info.
   */
  IDataDef PDEF_OUTLINE_INFO = DataDef.create( PID_OUTLINE_INFO, VALOBJ, //
      TSID_NAME, STR_N_OUTLINE_INFO, //
      TSID_DESCRIPTION, STR_D_OUTLINE_INFO, //
      TSID_KEEPER_ID, TsOutlineInfo.KEEPER_ID, //
      // FIXME ValedAvValobjTsOutlineInfo OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsOutlineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsOutlineInfo.ofSimple( TsLineInfo.ofWidth( 1 ), new RGBA( 0, 0, 0, 0 ) ) ) //
  );

  // ------------------------------------------------------------------------------------
  // Rotate
  //

  /**
   * ID of the property {@link #PDEF_ROTATION}.
   */
  String PID_ROTATION = "rotation"; //$NON-NLS-1$

  /**
   * Property: component rotation angle.
   */
  IDataDef PDEF_ROTATION = DataDef.create( PID_ROTATION, VALOBJ, //
      TSID_NAME, STR_N_ROTATION, //
      TSID_DESCRIPTION, STR_D_ROTATION, //
      TSID_KEEPER_ID, D2Rotation.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ID2Rotation.NONE ) //
  );

  // ------------------------------------------------------------------------------------
  // Flip
  //

  /**
   * ID of the property {@link #PDEF_FLIP_HOR}.
   */
  String PID_FLIP_HOR = "flipHor"; //$NON-NLS-1$

  /**
   * Property: the sign to flip component horizontally.
   */
  IDataDef PDEF_FLIP_HOR = DataDef.create( PID_ROTATION, BOOLEAN, //
      TSID_NAME, STR_N_FLIP_HOR, //
      TSID_DESCRIPTION, STR_D_FLIP_HOR, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * ID of the property {@link #PDEF_FLIP_VER}.
   */
  String PID_FLIP_VER = "flipVer"; //$NON-NLS-1$

  /**
   * Property: the sign to flip component vertically.
   */
  IDataDef PDEF_FLIP_VER = DataDef.create( PID_ROTATION, BOOLEAN, //
      TSID_NAME, STR_N_FLIP_VER, //
      TSID_DESCRIPTION, STR_D_FLIP_VER, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  // ------------------------------------------------------------------------------------
  // Fulcrum
  //

  /**
   * ID of the property {@link #PDEF_FULCRUM}.
   */
  String PID_FULCRUM = "fulcrum"; //$NON-NLS-1$

  /**
   * Property: (x,y) coordinates anchoring fulcrum.
   */
  IDataDef PDEF_FULCRUM = DataDef.create( PID_FULCRUM, VALOBJ, //
      TSID_NAME, STR_N_FULCRUM, //
      TSID_DESCRIPTION, STR_D_FULCRUM, //
      TSID_KEEPER_ID, ETsFulcrum.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsFulcrum.LEFT_TOP ) //
  );

  // ------------------------------------------------------------------------------------
  // Zoom
  //

  /**
   * ID of the property {@link #PDEF_ZOOM_X}.
   */
  String PID_ZOOM_X = "zoomX"; //$NON-NLS-1$

  /**
   * Property: zoom factor along X axis.
   */
  IDataDef PDEF_ZOOM_X = DataDef.create( PID_ROTATION, FLOATING, //
      TSID_NAME, STR_N_ZOOM_X, //
      TSID_DESCRIPTION, STR_D_ZOOM_X, //
      TSID_DEFAULT_VALUE, avFloat( 1.0 ) //
  );

  /**
   * ID of the property {@link #PDEF_ZOOM_Y}.
   */
  String PID_ZOOM_Y = "zoomY"; //$NON-NLS-1$

  /**
   * Property: zoom factor along Y axis.
   */
  IDataDef PDEF_ZOOM_Y = DataDef.create( PID_ROTATION, FLOATING, //
      TSID_NAME, STR_N_ZOOM_Y, //
      TSID_DESCRIPTION, STR_D_ZOOM_Y, //
      TSID_DEFAULT_VALUE, avFloat( 1.0 ) //
  );

}
