package org.toxsoft.tsgui.valed.controls.enums;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tsgui.valed.controls.enums.ITsResources.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextRefDef;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContextRefDef;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;

/**
 * Constants of Valeds working with {@link Enum} values.
 *
 * @author hazard157
 */
public interface IValedEnumConstants {

  /**
   * ID of the {@link #REFDEF_ENUM_CLASS}.
   */
  String REFID_ENUM_CLASS = VALED_OPID_PREFIX + ".ValedEnum.EnumClassRef"; //$NON-NLS-1$

  /**
   * Context reference to the {@link Class} of the value {@link Enum} type.
   */
  @SuppressWarnings( "rawtypes" )
  ITsGuiContextRefDef<Class> REFDEF_ENUM_CLASS = TsGuiContextRefDef.create( REFID_ENUM_CLASS, Class.class );

  /**
   * ID of the {@link #OPDEF_ENUM_CLASS_NAME}.
   */
  String OPID_ENUM_CLASS_NAME = VALED_OPID_PREFIX + ".ValedEnum.EnumClassName"; //$NON-NLS-1$

  /**
   * Context reference to the {@link Class#getName()} of the value {@link Enum} type.
   */
  IDataDef OPDEF_ENUM_CLASS_NAME = DataDef.create( OPID_ENUM_CLASS_NAME, STRING, //
      TSID_DEFAULT_VALUE, TsLibUtils.EMPTY_STRING //
  );

  /**
   * Extracts <code>enum</code> class from context using {@link #REFDEF_ENUM_CLASS} or {@link #OPDEF_ENUM_CLASS_NAME}.
   *
   * @param <E> - the nuem tyoe
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link Class} - the found enum class
   * @throws TsIllegalArgumentRtException no enum class info in context
   * @throws TsIllegalArgumentRtException java {@link Class} not found
   * @throws TsIllegalArgumentRtException java {@link Class} does not corresponds to <code>enum</code>
   */
  @SuppressWarnings( "unchecked" )
  static <E extends Enum<E>> Class<E> getEnumClassFromContext( ITsGuiContext aContext ) {
    // ищем класс перечисления по ссылке
    Class<E> enumClass = REFDEF_ENUM_CLASS.getRef( aContext );
    if( enumClass == null ) {
      // ищем класс перечисления из параметра
      String enumClassName = OPDEF_ENUM_CLASS_NAME.getValue( aContext.params() ).asString();
      if( enumClassName.isBlank() ) {
        throw new TsIllegalArgumentRtException( MSG_ERR_ENUM_CLASS_NOT_SPECIFIED );
      }
      try {
        enumClass = (Class<E>)Class.forName( enumClassName );
      }
      catch( ClassNotFoundException ex ) {
        throw new TsIllegalArgumentRtException( ex, FMT_ERR_ENUM_CLASS_NOT_FOUND, enumClassName );
      }
    }
    if( !Enum.class.isAssignableFrom( enumClass ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_CLASS_NOT_ENUM, enumClass.getName() );
    }
    return enumClass;
  }

}
