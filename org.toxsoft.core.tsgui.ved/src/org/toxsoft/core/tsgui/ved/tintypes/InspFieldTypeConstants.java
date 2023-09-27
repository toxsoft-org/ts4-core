package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.helpers.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Константы типов полей для инспектора.
 * <p>
 *
 * @author vs
 */
public interface InspFieldTypeConstants {

  /**
   * Информация о поле инспектора для значений типа {@link Integer}.
   */
  ITinTypeInfo TTI_INTEGER = new TinAtomicTypeInfo<>( DDEF_INTEGER, Integer.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Integer aEntity ) {
      return avInt( aEntity.intValue() );
    }
  };

  /**
   * Информация о поле инспектора для значений типа {@link Double}.
   */
  ITinTypeInfo TTI_FLOATING = new TinAtomicTypeInfo<>( DDEF_FLOATING, Double.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Double aEntity ) {
      return avFloat( aEntity.doubleValue() );
    }
  };

  /**
   * Информация о поле инспектора для значений типа {@link String}.
   */
  ITinTypeInfo TTI_STRING = new TinAtomicTypeInfo<>( DDEF_STRING, String.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  /**
   * Информация о поле инспектора для значений типа {@link Boolean}.
   */
  ITinTypeInfo TTI_BOOLEAN = new TinAtomicTypeInfo<>( DDEF_BOOLEAN, Boolean.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Boolean aEntity ) {
      return avBool( aEntity.booleanValue() );
    }
  };

  /**
   * Информация о поле инспектора для значений типа {@link Enum}.
   */
  ITinTypeInfo TTI_ENUM_INFO = new TinAtomicTypeInfo<>( DT_ENUM, Enum.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Enum aEntity ) {
      return avValobj( aEntity );
    }
  };

  /**
   * Информация о поле инспектора для значений типа "компонента цвета".
   */
  ITinTypeInfo TTI_COLOR_COMPONENT = new TinAtomicTypeInfo<>( DT_COLOR_COMPONENT, Integer.class ) {

    @Override
    protected IAtomicValue doGetAtomicValue( Integer aEntity ) {
      return avInt( aEntity.intValue() );
    }

  };

  /**
   * Информация о поле инспектора для значений типа {@link RGB}.
   */
  ITinTypeInfo TTI_RGB_COLOR = InspRgbTypeInfo.INSP_TYPE_INFO;

  // /**
  // * Информация о поле инспектора для значений типа {@link RGBA}.
  // */
  // ITinTypeInfo TTI_RGBA_COLOR = InspRgbaTypeInfo.INSP_TYPE_INFO;

  /**
   * Информация о поле инспектора для значений типа {@link TsFillInfo}.
   */
  ITinTypeInfo TTI_FILL_INFO = InspFillTypeInfo.INSTANCE;

  /**
   * Информация о поле инспектора для значений типа {@link TsLineInfo}.
   */
  ITinTypeInfo TTI_LINE_INFO = InspLineTypeInfo.INSTANCE;

}
