package org.toxsoft.core.tsgui.ved.api;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.api.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface IVedFrameworkConstants {

  /**
   * Framework specific identifiers prefix.
   */
  String VED_ID = "ts.gef"; //$NON-NLS-1$

  /**
   * Entity operation is allowed.<br>
   * Type: {@link EAtomicType#BOOLEAN}
   */
  IDataDef PROP_IS_ENABLED = DataDef.create( VED_ID + ".isEnabled", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_IS_ENABLED, //
      TSID_DESCRIPTION, STR_IS_ENABLED_D, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Sign that the primitive is under the mouse cursor.<br>
   * Type: {@link EAtomicType#BOOLEAN}
   */
  IDataDef PARAM_IS_HOVERED = DataDef.create( VED_ID + ".isHovered", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_IS_HOVERED, //
      TSID_DESCRIPTION, STR_IS_HOVERED_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Sign that the primitive is selected (current).<br>
   * Type: {@link EAtomicType#BOOLEAN}
   */
  IDataDef PARAM_IS_SELECTED = DataDef.create( VED_ID + ".isSelected", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_IS_SELECTED, //
      TSID_DESCRIPTION, STR_IS_SELECTED_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Sign of hiding (temporary invisibility) of a primitive.<br>
   * Type: {@link EAtomicType#BOOLEAN}
   */
  IDataDef PROP_IS_HIDDEN = DataDef.create( VED_ID + ".visible", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_HIDDEN, //
      TSID_DESCRIPTION, STR_HIDDEN_D, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * X coordinate of the primitive in pixels.<br>
   * Type: {@link EAtomicType#INTEGER}
   */
  IDataDef PROP_X = DataDef.create( VED_ID + ".x", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_X, //
      TSID_DESCRIPTION, STR_X_D, //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * Y coordinate of the primitive in pixels.<br>
   * Type: {@link EAtomicType#INTEGER}
   */
  IDataDef PROP_Y = DataDef.create( VED_ID + ".y", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_Y, //
      TSID_DESCRIPTION, STR_Y_D, //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * Width of the bounding rectangle in pixels.<br>
   * Type: {@link EAtomicType#INTEGER}
   */
  IDataDef PROP_WIDTH = DataDef.create( VED_ID + ".width", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_WIDTH, //
      TSID_DESCRIPTION, STR_WIDTH_D, //
      TSID_DEFAULT_VALUE, avInt( 100 ) //
  );

  /**
   * Height of the bounding rectangle in pixels.<br>
   * Type: {@link EAtomicType#INTEGER}
   */
  IDataDef PROP_HEIGHT = DataDef.create( VED_ID + ".height", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_HEIGHT, //
      TSID_DESCRIPTION, STR_HEIGHT_D, //
      TSID_DEFAULT_VALUE, avInt( 100 ) //
  );

  /**
   * Method for snapping a bounding rectangle to coordinates.<br>
   * Type: {@link ETsFulcrum}
   */
  IDataDef PROP_FULCRUM = DataDef.create( VED_ID + ".fulcrum", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_FULCRUM, //
      TSID_DESCRIPTION, STR_FULCRUM_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsFulcrum.LEFT_TOP ), //
      TSID_KEEPER_ID, ETsFulcrum.KEEPER_ID //
  );

  /**
   * Background color (solid fill color).<br>
   * Type: {@link RGB}
   */
  IDataDef PROP_BK_COLOR = DataDef.create( VED_ID + ".bkColor", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_NAME, STR_BK_COLOR, //
      TSID_DESCRIPTION, STR_BK_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.WHITE.rgb() ) //
  );

  /**
   * Primary color for drawing graphic primitives.<br>
   * Type: {@link RGB}
   */
  IDataDef PROP_FG_COLOR = DataDef.create( VED_ID + ".fgColor", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_NAME, STR_FG_COLOR, //
      TSID_DESCRIPTION, STR_FG_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgb() ) //
  );

  /**
   * Font used to display text.<br>
   * Type: {@link IFontInfo}
   */
  IDataDef PROP_FONT = DataDef.create( VED_ID + ".font", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY_NAME, //
      TSID_NAME, STR_FONT, //
      TSID_DESCRIPTION, STR_FONT_D, //
      TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 10, false, false ) ) //$NON-NLS-1$
  );

}
