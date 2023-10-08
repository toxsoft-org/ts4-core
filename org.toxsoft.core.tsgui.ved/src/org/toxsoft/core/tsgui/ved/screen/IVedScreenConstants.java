package org.toxsoft.core.tsgui.ved.screen;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IVedScreenConstants {

  String PROPID_IS_ACTIVE     = VED_ID + ".isActive";    //$NON-NLS-1$
  String PROPID_X             = VED_ID + ".x";           //$NON-NLS-1$
  String PROPID_Y             = VED_ID + ".y";           //$NON-NLS-1$
  String PROPID_WIDTH         = VED_ID + ".width";       //$NON-NLS-1$
  String PROPID_HEIGHT        = VED_ID + ".height";      //$NON-NLS-1$
  String PROPID_TRANSFORM     = VED_ID + ".transform";   //$NON-NLS-1$
  String PROPID_BK_COLOR      = VED_ID + ".bkColor";     //$NON-NLS-1$
  String PROPID_FG_COLOR      = VED_ID + ".fgColor";     //$NON-NLS-1$
  String PROPID_VISEL_ID      = VED_ID + ".viselId";     //$NON-NLS-1$
  String PROPID_VISEL_PROP_ID = VED_ID + ".viselPropId"; //$NON-NLS-1$

  IDataDef PROP_IS_ACTIVE = DataDef.create( PROPID_IS_ACTIVE, BOOLEAN, TSID_NAME, STR_IS_ACTIVE, //
      TSID_DESCRIPTION, STR_IS_ACTIVE_D, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  IDataDef PROP_X = DataDef.create( PROPID_X, FLOATING, //
      TSID_NAME, STR_X, //
      TSID_DESCRIPTION, STR_X_D, //
      TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
  );

  IDataDef PROP_Y = DataDef.create( PROPID_Y, FLOATING, //
      TSID_NAME, STR_Y, //
      TSID_DESCRIPTION, STR_Y_D, //
      TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
  );

  IDataDef PROP_WIDTH = DataDef.create( PROPID_WIDTH, FLOATING, //
      TSID_NAME, STR_WIDTH, //
      TSID_DESCRIPTION, STR_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 100 ) //
  );

  IDataDef PROP_HEIGHT = DataDef.create( PROPID_HEIGHT, FLOATING, //
      TSID_NAME, STR_HEIGHT, //
      TSID_DESCRIPTION, STR_HEIGHT_D, //
      TSID_DEFAULT_VALUE, avFloat( 100 ) //
  );

  IDataDef PROP_TRANSFORM = DataDef.create3( PROPID_TRANSFORM, DT_D2CONVERSION, //
      TSID_NAME, STR_TRANSFORM, //
      TSID_DESCRIPTION, STR_TRANSFORM_D, //
      TSID_DEFAULT_VALUE, avValobj( ID2Conversion.NONE ) //
  );

  IDataDef PROP_BK_COLOR = DataDef.create3( PROPID_BK_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, STR_BK_COLOR, //
      TSID_DESCRIPTION, STR_BK_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.WHITE.rgba() ) //
  );

  IDataDef PROP_FG_COLOR = DataDef.create3( PROPID_FG_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, STR_FG_COLOR, //
      TSID_DESCRIPTION, STR_FG_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgba() ) //
  );

  IDataDef PROP_VISEL_ID = DataDef.create3( PROPID_VISEL_ID, DDEF_IDPATH, //
      TSID_NAME, STR_VISEL_ID, //
      TSID_DESCRIPTION, STR_VISEL_ID_D, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  IDataDef PROP_VISEL_PROP_ID = DataDef.create3( PROPID_VISEL_PROP_ID, DDEF_IDPATH, //
      TSID_NAME, STR_VISEL_PROP_ID, //
      TSID_DESCRIPTION, STR_VISEL_PROP_ID_D, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  ITinFieldInfo TFI_IS_ACTIVE     = new TinFieldInfo( PROP_IS_ACTIVE, TTI_AT_BOOLEAN );
  ITinFieldInfo TFI_X             = new TinFieldInfo( PROP_X, TTI_AT_FLOATING );
  ITinFieldInfo TFI_Y             = new TinFieldInfo( PROP_Y, TTI_AT_FLOATING );
  ITinFieldInfo TFI_WIDTH         = new TinFieldInfo( PROP_WIDTH, TTI_AT_FLOATING );
  ITinFieldInfo TFI_HEIGHT        = new TinFieldInfo( PROP_HEIGHT, TTI_AT_FLOATING );
  ITinFieldInfo TFI_TRANSFORM     = new TinFieldInfo( PROP_TRANSFORM, TTI_D2CONVERSION );
  ITinFieldInfo TFI_BK_COLOR      = new TinFieldInfo( PROP_BK_COLOR, RGBATypeInfo.INSTANCE );
  ITinFieldInfo TFI_FG_COLOR      = new TinFieldInfo( PROP_FG_COLOR, RGBATypeInfo.INSTANCE );
  ITinFieldInfo TFI_VISEL_ID      = new TinFieldInfo( PROP_VISEL_ID, TTI_IDPATH );
  ITinFieldInfo TFI_VISEL_PROP_ID = new TinFieldInfo( PROP_VISEL_PROP_ID, TTI_IDPATH );

  // ------------------------------------------------------------------------------------
  // Yet undone
  //

  // IDataDef PROP_FONT = DataDef.create( VED_ID + ".font", VALOBJ, //$NON-NLS-1$
  // TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
  // OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY_NAME, //
  // TSID_NAME, STR_FONT, //
  // TSID_DESCRIPTION, STR_FONT_D, //
  // TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 10, false, false ) ) //$NON-NLS-1$
  // );
  //
  // IDataDef PARAM_IS_HOVERED = DataDef.create( VED_ID + ".isHovered", BOOLEAN, //$NON-NLS-1$
  // TSID_NAME, STR_IS_HOVERED, //
  // TSID_DESCRIPTION, STR_IS_HOVERED_D, //
  // TSID_DEFAULT_VALUE, AV_FALSE //
  // );
  //
  // /**
  // * Sign that the primitive is selected (current).<br>
  // * Type: {@link EAtomicType#BOOLEAN}
  // */
  // IDataDef PARAM_IS_SELECTED = DataDef.create( VED_ID + ".isSelected", BOOLEAN, //$NON-NLS-1$
  // TSID_NAME, STR_IS_SELECTED, //
  // TSID_DESCRIPTION, STR_IS_SELECTED_D, //
  // TSID_DEFAULT_VALUE, AV_FALSE //
  // );
  //
  // /**
  // * Sign of hiding (temporary invisibility) of a primitive.<br>
  // * Type: {@link EAtomicType#BOOLEAN}
  // */
  // IDataDef PROP_IS_HIDDEN = DataDef.create( VED_ID + ".visible", BOOLEAN, //$NON-NLS-1$
  // TSID_NAME, STR_HIDDEN, //
  // TSID_DESCRIPTION, STR_HIDDEN_D, //
  // TSID_FORMAT_STRING, FMT_BOOL_CHECK, //
  // TSID_DEFAULT_VALUE, AV_FALSE //
  // );
  //
  // /**
  // * Method for snapping a bounding rectangle to coordinates.<br>
  // * Type: {@link ETsFulcrum}
  // */
  // IDataDef PROP_FULCRUM = DataDef.create( VED_ID + ".fulcrum", VALOBJ, //$NON-NLS-1$
  // TSID_NAME, STR_FULCRUM, //
  // TSID_DESCRIPTION, STR_FULCRUM_D, //
  // TSID_DEFAULT_VALUE, avValobj( ETsFulcrum.LEFT_TOP ), //
  // TSID_KEEPER_ID, ETsFulcrum.KEEPER_ID //
  // );

}
