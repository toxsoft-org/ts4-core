package org.toxsoft.core.tsgui.valed.controls.enums;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.enums.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

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
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  static <E extends Enum<E>> Class<E> getEnumClassFromContext( ITsGuiContext aContext ) {
    // search by reference in context
    Class<?> rawClass = REFDEF_ENUM_CLASS.getRef( aContext );
    if( rawClass == null ) {
      // search by option in context
      String enumClassName = OPDEF_ENUM_CLASS_NAME.getValue( aContext.params() ).asString();
      if( !enumClassName.isBlank() ) {
        try {
          rawClass = Class.forName( enumClassName );
        }
        catch( ClassNotFoundException ex ) {
          throw new TsIllegalArgumentRtException( ex, FMT_ERR_ENUM_CLASS_NOT_FOUND, enumClassName );
        }
      }
      // search by keeper ID if any
      String keeperId = aContext.params().getStr( TSID_KEEPER_ID, null );
      if( keeperId != null ) {
        IEntityKeeper<?> keeper = TsValobjUtils.findKeeperById( keeperId );
        if( keeper != null ) {
          rawClass = keeper.entityClass();
        }
        else {
          throw new TsIllegalArgumentRtException( FMT_ERR_KEEPER_CLASS_NOT_FOUND, keeperId );
        }
      }
      else {
        throw new TsIllegalArgumentRtException( MSG_ERR_ENUM_CLASS_NOT_SPECIFIED );
      }
    }
    if( !Enum.class.isAssignableFrom( rawClass ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_CLASS_NOT_ENUM, rawClass.getName() );
    }
    return (Class)rawClass;
  }

}
