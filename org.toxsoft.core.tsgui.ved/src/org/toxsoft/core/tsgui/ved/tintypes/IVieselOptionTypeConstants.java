package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Константы типов данных для опций визуального элемента {@link IVedVisel}.
 * <p>
 *
 * @author vs
 */
public interface IVieselOptionTypeConstants {

  /**
   * Data type: {@link EAtomicType#INTEGER INTEGER} R/G/B/A color component in range 0..255.
   */
  IDataType DT_COLOR_COMPONENT = DataType.create( EAtomicType.INTEGER, //
      TSID_NAME, STR_COLOR_COMPONENT, //
      TSID_DEFAULT_VALUE, STR_COLOR_COMPONENT_D, //
      TSID_MIN_INCLUSIVE, AV_0, //
      TSID_MAX_INCLUSIVE, avInt( 255 ), //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * Default value for {@link #DT_COLOR_RGB}.
   */
  RGB DEFAULT_RGB_VALUE = ETsColor.GRAY.rgb();

  /**
   * Data type: color as {@link RGB} {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_COLOR_RGB = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_COLOR_RGB, //
      TSID_DESCRIPTION, STR_D_COLOR_RGB, //
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_RGB_VALUE ) //
  );

  /**
   * Default value for {@link #DT_COLOR_RGBA}.
   */
  RGBA DEFAULT_RGBA_VALUE = ETsColor.GRAY.rgba();

  /**
   * Data type: color as {@link RGBA} {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_COLOR_RGBA = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_COLOR_RGBA, //
      TSID_DESCRIPTION, STR_D_COLOR_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_RGBA_VALUE ) //
  );

  /**
   * Описание типа данных {@link Enum}
   */
  IDataType DT_ENUM = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_ENUM, //
      TSID_DESCRIPTION, STR_D_ENUM, //
      TSID_ENUMERATION, avBool( true ), //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjEnumCombo.FACTORY_NAME );

  /**
   * Описание типа данных {@link ID2Point}
   */
  IDataType DT_D2POINT = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_D2POINT, //
      TSID_DESCRIPTION, STR_D_D2POINT, //
      TSID_KEEPER_ID, D2Point.KEEPER_ID, //
      // FIXME - указать редактор для D2Point
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( ID2Point.ZERO ) //
  );

  /**
   * Описание типа данных {@link ID2Conversion}
   */
  IDataType DT_D2CONVERSION = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_D2CONVERSION, //
      TSID_DESCRIPTION, STR_D_D2CONVERSION, //
      TSID_KEEPER_ID, D2Conversion.KEEPER_ID, //
      // FIXME - указать редактор для D2Conversion
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( ID2Conversion.NONE ) //
  );

  /**
   * Описание типа данных {@link TsLineInfo}
   */
  IDataType DT_LINE_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.DEFAULT ) //
  );

  /**
   * Описание типа данных {@link TsBorderInfo}
   */
  IDataType DT_BORDER_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_BORDER_INFO, //
      TSID_DESCRIPTION, STR_D_BORDER_INFO, //
      TSID_KEEPER_ID, TsBorderInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsBorderInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsBorderInfo.createSimpleBorder( 1, new RGBA( 0, 0, 0, 255 ) ) ) //
  );

  /**
   * Описание типа данных {@link IFontInfo}
   */
  IDataType DT_FONT_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_FONT, //
      TSID_DESCRIPTION, STR_D_FONT, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 14, 0 ) ) // //$NON-NLS-1$
  );

  /**
   * Описание типа данных {@link TsImageDescriptor}
   */
  IDataType DT_IMAGE_DESCRIPTOR = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_IMAGE_DESCRIPTOR, //
      TSID_DESCRIPTION, STR_D_IMAGE_DESCRIPTOR, //
      TSID_KEEPER_ID, TsImageDescriptor.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsImageDescriptor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsImageDescriptor.NONE ) //
  );

  /**
   * Описание типа данных {@link TsImageFillInfo}
   */
  IDataType DT_IMAGE_FILL_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_IMAGE_FILL_INFO, //
      TSID_DESCRIPTION, STR_D_IMAGE_FILL_INFO, //
      TSID_KEEPER_ID, TsImageFillInfo.KEEPER_ID, //
      // FIXME указать редактор для TsImageFillInfo
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new TsImageFillInfo( TsImageDescriptor.NONE, EImageFillKind.FIT ) ) //
  );

  /**
   * Описание типа данных {@link TsFillInfo}
   */
  IDataType DT_FILL_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_FILL_INFO, //
      TSID_DESCRIPTION, STR_D_FILL_INFO, //
      TSID_KEEPER_ID, TsFillInfo.KEEPER_ID, //
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new TsFillInfo( new RGBA( 255, 255, 255, 255 ) ) ) //
  );

}
