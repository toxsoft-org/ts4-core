package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Object inspector helper constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITtiConstants {

  // ------------------------------------------------------------------------------------
  // Atomic types

  ITinTypeInfo TTI_AT_BOOLEAN = new TinAtomicTypeInfo<>( DDEF_BOOLEAN, Boolean.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Boolean aEntity ) {
      return avBool( aEntity.booleanValue() );
    }
  };

  ITinTypeInfo TTI_AT_INTEGER = new TinAtomicTypeInfo<>( DDEF_INTEGER, Integer.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Integer aEntity ) {
      return avInt( aEntity.intValue() );
    }
  };

  ITinTypeInfo TTI_AT_FLOATING = new TinAtomicTypeInfo<>( DDEF_FLOATING, Double.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Double aEntity ) {
      return avFloat( aEntity.doubleValue() );
    }
  };

  ITinTypeInfo TTI_AT_STRING = new TinAtomicTypeInfo<>( DDEF_STRING, String.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  ITinTypeInfo TTI_AT_TIMESTAMP = new TinAtomicTypeInfo<>( DDEF_TIMESTAMP, Long.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Long aEntity ) {
      return avTimestamp( aEntity.longValue() );
    }
  };

  // ------------------------------------------------------------------------------------
  // TsLIB entities

  ITinTypeInfo TTI_IDNAME = new TinAtomicTypeInfo<>( DDEF_IDNAME, String.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  ITinTypeInfo TTI_IDPATH = new TinAtomicTypeInfo<>( DDEF_IDPATH, String.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  // ------------------------------------------------------------------------------------
  // Colors

  int  DEFAULT_COLOR_COMPONENT_VALUE = 255;
  RGB  DEFAULT_RGB_VALUE             = ETsColor.GRAY.rgb();
  RGBA DEFAULT_RGBA_VALUE            = ETsColor.GRAY.rgba();

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

  ITinTypeInfo TTI_COLOR_COMPONENT = new TinAtomicTypeInfo.TtiInteger( DT_COLOR_COMPONENT );

  // ------------------------------------------------------------------------------------
  // Geometry

  /**
   * Data type: {@link ID2Conversion} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_D2CONVERSION = DataType.create( VALOBJ, //
      TSID_NAME, STR_COLOR_RGBA, //
      TSID_DESCRIPTION, STR_COLOR_RGBA_D, //
      TSID_KEEPER_ID, D2Conversion.KEEPER_ID, //
      // FIXME - указать редактор для D2Conversion
      // OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleFontInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( ID2Conversion.NONE ) //
  );

  ITinTypeInfo TTI_D2CONVERSION = new TinAtomicTypeInfo<>( DT_D2CONVERSION, ID2Conversion.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( ID2Conversion aEntity ) {
      return avValobj( aEntity );
    }
  };

}
