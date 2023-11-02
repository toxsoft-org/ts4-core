package org.toxsoft.core.tsgui.ved.screen;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IVedScreenConstants {

  /**
   * TODO make properties read-only<br>
   * TODO make properties invisible (like Width.Height for circle<br>
   */

  // ------------------------------------------------------------------------------------
  // Mandatory properties
  //

  String PROPID_IS_ACTIVE   = "isActive";      //$NON-NLS-1$
  String PROPID_NAME        = TSID_NAME;
  String PROPID_DESCRIPTION = TSID_DESCRIPTION;
  String PROPID_X           = "x";             //$NON-NLS-1$
  String PROPID_Y           = "y";             //$NON-NLS-1$
  String PROPID_WIDTH       = "width";         //$NON-NLS-1$
  String PROPID_HEIGHT      = "height";        //$NON-NLS-1$
  String PROPID_TRANSFORM   = "transform";     //$NON-NLS-1$

  IDataDef PROP_IS_ACTIVE = DataDef.create( PROPID_IS_ACTIVE, BOOLEAN, //
      TSID_NAME, STR_IS_ACTIVE, //
      TSID_DESCRIPTION, STR_IS_ACTIVE_D, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  IDataDef PROP_NAME = DataDef.create3( PROPID_NAME, DDEF_NAME, //
      TSID_NAME, STR_ITEM_NAME, //
      TSID_DESCRIPTION, STR_ITEM_NAME_D //
  );

  IDataDef PROP_DESCRIPTION = DataDef.create3( PROPID_DESCRIPTION, DDEF_DESCRIPTION, //
      TSID_DESCRIPTION, STR_ITEM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_ITEM_DESCRIPTION_D //
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

  /**
   * List of mandatory properties IDs for the VISELS.
   */
  IStringList VISEL_MANDATORY_PROP_IDS = new StringArrayList( //
      PROPID_IS_ACTIVE, //
      PROPID_NAME, //
      PROPID_DESCRIPTION, //
      PROPID_X, //
      PROPID_Y, //
      PROPID_WIDTH, //
      PROPID_HEIGHT, //
      PROPID_TRANSFORM //
  );

  /**
   * List of mandatory properties IDs for the actors.
   * <p>
   * These option must be listed by {@link IVedItemFactoryBase#propDefs()} in any order. Moreover the returned property
   * definition must match elements of this list with <code>==</code> operator, not by {@link Object#equals(Object)}
   * method.
   */
  IStringList ACTOR_MANDATORY_PROP_IDS = new StringArrayList( //
      PROPID_IS_ACTIVE //
  );

  // ------------------------------------------------------------------------------------
  // Optional VISEL properties
  //

  String PROPID_TEXT             = "text";            //$NON-NLS-1$
  String PROPID_FONT             = "font";            //$NON-NLS-1$
  String PROPID_HOR_ALIGNMENT    = "horAlign";        //$NON-NLS-1$
  String PROPID_VER_ALIGNMENT    = "verAlign";        //$NON-NLS-1$
  String PROPID_BK_COLOR         = "bkColor";         //$NON-NLS-1$
  String PROPID_FG_COLOR         = "fgColor";         //$NON-NLS-1$
  String PROPID_BK_FILL          = "bkFill";          //$NON-NLS-1$
  String PROPID_LINE_INFO        = "lineInfo";        //$NON-NLS-1$
  String PROPID_BORDER_INFO      = "borderInfo";      //$NON-NLS-1$
  String PROPID_IS_ASPECT_FIXED  = "isAspectFixed";   //$NON-NLS-1$
  String PROPID_ASPECT_RATIO     = "aspectRatio";     //$NON-NLS-1$
  String PROPID_RADIUS           = "radius";          //$NON-NLS-1$
  String PROPID_ON_OFF_STATE     = "onOffState";      //$NON-NLS-1$
  String PROPID_IMAGE_DESCRIPTOR = "imageDescriptor"; //$NON-NLS-1$

  IDataDef PROP_TEXT = DataDef.create3( PROPID_TEXT, DDEF_STRING, //
      TSID_NAME, STR_TEXT, //
      TSID_DESCRIPTION, STR_TEXT_D, //
      TSID_DEFAULT_VALUE, avStr( "Abc123" ) // //$NON-NLS-1$
  );

  IDataDef PROP_FONT = DataDef.create3( PROPID_FONT, DDEF_STRING, //
      TSID_NAME, STR_FONT, //
      TSID_DESCRIPTION, STR_FONT_D, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IFontInfo.DEFAULT ) //
  );

  IDataDef PROP_HOR_ALIGNMENT = DataDef.create3( PROPID_HOR_ALIGNMENT, DT_AV_ENUM, //
      TSID_NAME, STR_HOR_ALIGN, //
      TSID_DESCRIPTION, STR_HOR_ALIGN_D, //
      TSID_KEEPER_ID, EHorAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EHorAlignment.LEFT ) //
  );

  IDataDef PROP_VER_ALIGNMENT = DataDef.create3( PROPID_VER_ALIGNMENT, DT_AV_ENUM, //
      TSID_NAME, STR_VER_ALIGN, //
      TSID_DESCRIPTION, STR_VER_ALIGN_D, //
      TSID_KEEPER_ID, EVerAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EVerAlignment.CENTER ) //
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

  IDataDef PROP_BK_FILL = DataDef.create3( PROPID_BK_FILL, DT_TS_FILL_INFO, //
      TSID_NAME, STR_BK_FILL, //
      TSID_DESCRIPTION, STR_BK_FILL_D //
  );

  IDataDef PROP_BORDER_INFO = DataDef.create3( PROPID_BORDER_INFO, DT_TS_BORDER_INFO, //
      TSID_NAME, STR_BORDER_INFO, //
      TSID_DESCRIPTION, STR_BORDER_INFO_D //
  );

  IDataDef PROP_LINE_INFO = DataDef.create3( PROPID_LINE_INFO, DT_TS_LINE_INFO, //
      TSID_NAME, STR_LINE_INFO, //
      TSID_DESCRIPTION, STR_LINE_INFO_D //
  );

  IDataDef PROP_IS_ASPECT_FIXED = DataDef.create3( PROPID_IS_ASPECT_FIXED, DDEF_TS_BOOL, //
      TSID_NAME, STR_IS_ASPECT_FIXED, //
      TSID_DESCRIPTION, STR_IS_ASPECT_FIXED_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IDataDef PROP_ASPECT_RATIO = DataDef.create3( PROPID_ASPECT_RATIO, DDEF_FLOATING, //
      TSID_NAME, STR_ASPECT_RATIO, //
      TSID_DESCRIPTION, STR_ASPECT_RATIO_D, //
      TSID_FORMAT_STRING, "%.3f", //$NON-NLS-1$
      ValedDoubleSpinner.OPID_FLOATING_DIGITS, avInt( 3 ), //
      TSID_DEFAULT_VALUE, avFloat( 1.0 ) //
  );

  IDataDef PROP_RADIUS = DataDef.create3( PROPID_RADIUS, DDEF_FLOATING, //
      TSID_NAME, STR_RADIUS, //
      TSID_DESCRIPTION, STR_RADIUS_D, //
      TSID_MIN_INCLUSIVE, AV_F_1, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  IDataDef PROP_ON_OF_STATE = create3( PROPID_ON_OFF_STATE, DDEF_BOOLEAN, //
      TSID_NAME, STR_ON_OFF_STATE, //
      TSID_DESCRIPTION, STR_ON_OFF_STATE_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IDataDef PROP_IMAGE_DESCRIPTOR = DataDef.create3( PROPID_IMAGE_DESCRIPTOR, DT_TS_IMAGE_DESCRIPTOR, //
      TSID_NAME, STR_IMAGE_DESCRIPTOR, //
      TSID_DESCRIPTION, STR_IMAGE_DESCRIPTOR_D //
  );

  // ------------------------------------------------------------------------------------
  // Optional actor properties
  //

  String PROPID_VISEL_ID      = "viselId";     //$NON-NLS-1$
  String PROPID_VISEL_PROP_ID = "viselPropId"; //$NON-NLS-1$

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

  // ------------------------------------------------------------------------------------
  // Known parameters
  //

  // TODO do we need this constants here ?
  // String PRMID_IS_HIDDEN = ITinWidgetConstants.PRMID_IS_HIDDEN;
  // IDataDef PRMDEF_IS_HIDDEN = ITinWidgetConstants.PRMDEF_IS_HIDDEN;

  // ------------------------------------------------------------------------------------
  // TIN field info corresponding to the properties
  //

  ITinFieldInfo TFI_IS_ACTIVE        = new TinFieldInfo( PROP_IS_ACTIVE, TTI_AT_BOOLEAN );
  ITinFieldInfo TFI_NAME             = new TinFieldInfo( PROP_NAME, TTI_AT_STRING );
  ITinFieldInfo TFI_DESCRIPTION      = new TinFieldInfo( PROP_DESCRIPTION, TTI_AT_STRING );
  ITinFieldInfo TFI_X                = new TinFieldInfo( PROP_X, TTI_AT_FLOATING );
  ITinFieldInfo TFI_Y                = new TinFieldInfo( PROP_Y, TTI_AT_FLOATING );
  ITinFieldInfo TFI_WIDTH            = new TinFieldInfo( PROP_WIDTH, TTI_POSITIVE_FLOATING );
  ITinFieldInfo TFI_HEIGHT           = new TinFieldInfo( PROP_HEIGHT, TTI_POSITIVE_FLOATING );
  ITinFieldInfo TFI_TRANSFORM        = new TinFieldInfo( PROP_TRANSFORM, TtiD2Conversion.INSTANCE );
  ITinFieldInfo TFI_TEXT             = new TinFieldInfo( PROP_TEXT, TTI_AT_STRING );
  ITinFieldInfo TFI_FONT             = new TinFieldInfo( PROP_FONT, TtiTsFontInfo.INSTANCE );
  ITinFieldInfo TFI_HOR_ALIGNMENT    = new TinFieldInfo( PROP_HOR_ALIGNMENT, TtiAvEnum.INSTANCE );
  ITinFieldInfo TFI_VER_ALIGNMENT    = new TinFieldInfo( PROP_VER_ALIGNMENT, TtiAvEnum.INSTANCE );
  ITinFieldInfo TFI_BK_COLOR         = new TinFieldInfo( PROP_BK_COLOR, TtiRGBA.INSTANCE );
  ITinFieldInfo TFI_FG_COLOR         = new TinFieldInfo( PROP_FG_COLOR, TtiRGBA.INSTANCE );
  ITinFieldInfo TFI_BK_FILL          = new TinFieldInfo( PROP_BK_FILL, TtiTsFillInfo.INSTANCE );
  ITinFieldInfo TFI_LINE_INFO        = new TinFieldInfo( PROP_LINE_INFO, TtiTsLineInfo.INSTANCE );
  ITinFieldInfo TFI_BORDER_INFO      = new TinFieldInfo( PROP_BORDER_INFO, TTI_TS_BORDER_INFO );
  ITinFieldInfo TFI_IS_ASPECT_FIXED  = new TinFieldInfo( PROP_IS_ASPECT_FIXED, TTI_AT_BOOLEAN );
  ITinFieldInfo TFI_ASPECT_RATIO     = new TinFieldInfo( PROP_ASPECT_RATIO, TTI_AT_FLOATING );
  ITinFieldInfo TFI_VISEL_ID         = new TinFieldInfo( PROP_VISEL_ID, TTI_IDPATH );
  ITinFieldInfo TFI_VISEL_PROP_ID    = new TinFieldInfo( PROP_VISEL_PROP_ID, TTI_IDPATH );
  ITinFieldInfo TFI_RADIUS           = new TinFieldInfo( PROP_RADIUS, TTI_POSITIVE_FLOATING );
  ITinFieldInfo TFI_IMAGE_DESCRIPTOR = new TinFieldInfo( PROP_IMAGE_DESCRIPTOR, TTI_TS_IMAGE_DECRIPTOR );

}
