package org.toxsoft.core.tsgui.bricks.tin.helpers;

import static org.toxsoft.core.tsgui.bricks.tin.helpers.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Object inspector helper constants.
 *
 * @author hazard157
 */
public interface ITinConstants {

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
   * TIN type info of {@link #DT_COLOR_COMPONENT}.
   */
  ITinTypeInfo TTI_COLOR_COMPONENT = new TinAtomicTypeInfo<>( DT_COLOR_COMPONENT, Integer.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Integer aEntity ) {
      return avInt( aEntity.intValue() );
    }

  };

  /**
   * Default value for {@link #DT_COLOR_RGB}.
   */
  RGB DEFAULT_RGB_VALUE = ETsColor.GRAY.rgb();

  /**
   * Data type: color as {@link RGB} {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_COLOR_RGB = DataType.create( VALOBJ, //
      TSID_NAME, STR_COLOR_RGB, //
      TSID_DESCRIPTION, STR_COLOR_RGB_D, //
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
      TSID_NAME, STR_COLOR_RGBA, //
      TSID_DESCRIPTION, STR_COLOR_RGBA_D, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_RGBA_VALUE ) //
  );

}
