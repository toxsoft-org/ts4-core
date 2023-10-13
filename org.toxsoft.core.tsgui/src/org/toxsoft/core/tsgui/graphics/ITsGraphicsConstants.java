package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Helper constants for package entities.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsGraphicsConstants {

  int  DEFAULT_COLOR_COMPONENT_VALUE = 255;
  RGB  DEFAULT_RGB_VALUE             = ETsColor.GRAY.rgb();
  RGBA DEFAULT_RGBA_VALUE            = ETsColor.GRAY.rgba();
  RGBA DEFAULT_SOLID_FILL_COLOR      = ETsColor.WHITE.rgba();
  RGBA DEFAULT_SIMPLE_BORDER_COLOR   = ETsColor.BLACK.rgba();

  IDataType DT_COLOR_COMPONENT = DataType.create( EAtomicType.INTEGER, //
      TSID_NAME, STR_COLOR_COMPONENT, //
      TSID_DEFAULT_VALUE, STR_COLOR_COMPONENT_D, //
      TSID_MIN_INCLUSIVE, AV_0, //
      TSID_MAX_INCLUSIVE, avInt( DEFAULT_COLOR_COMPONENT_VALUE ), //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  IDataType DT_COLOR_RGB = DataType.create( VALOBJ, //
      TSID_NAME, STR_COLOR_RGB, //
      TSID_DESCRIPTION, STR_COLOR_RGB_D, //
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_RGB_VALUE ) //
  );

  IDataType DT_COLOR_RGBA = DataType.create( VALOBJ, //
      TSID_NAME, STR_COLOR_RGBA, //
      TSID_DESCRIPTION, STR_COLOR_RGBA_D, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_RGBA_VALUE ) //
  );

  /**
   * Data type: {@link ID2Conversion} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_D2CONVERSION = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_D2CONVERSION, //
      TSID_DESCRIPTION, STR_DT_D2CONVERSION_D, //
      TSID_KEEPER_ID, D2Conversion.KEEPER_ID, //
      // FIXME - указать редактор для D2Conversion
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( ID2Conversion.NONE ) //
  );

  /**
   * Data type: {@link TsFillInfo} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_TS_FILL_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_TS_FILL_INFO, //
      TSID_DESCRIPTION, STR_TS_FILL_INFO_D, //
      TSID_KEEPER_ID, TsFillInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsFillInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( new TsFillInfo( DEFAULT_SOLID_FILL_COLOR ) ) //
  );

  /**
   * Data type: {@link TsBorderInfo} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_TS_BORDER_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_TS_BORDER_INFO, //
      TSID_DESCRIPTION, STR_TS_BORDER_INFO_D, //
      TSID_KEEPER_ID, TsBorderInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsBorderInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( TsBorderInfo.createSimpleBorder( 1, DEFAULT_SIMPLE_BORDER_COLOR ) ) //
  );

}
