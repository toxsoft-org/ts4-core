package org.toxsoft.core.tsgui.bricks.ctx.impl;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextRefDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRefDef;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContextRefDef;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link ITsContextRefDef}.
 *
 * @author hazard157
 * @param <T> - класс ссылки
 */
public class TsGuiContextRefDef<T>
    extends TsContextRefDef<T>
    implements ITsGuiContextRefDef<T> {

  /**
   * Constructor.
   *
   * @param aRefKey String - the key
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aParams {@link IOptionSet} - initial values for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the key is an blank string
   */
  public TsGuiContextRefDef( String aRefKey, Class<T> aClass, IOptionSet aParams ) {
    super( aRefKey, aClass, aParams );
  }

  /**
   * Constructs with class name as the key.
   *
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aParams {@link IOptionSet} - initial values for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsGuiContextRefDef( Class<T> aClass, IOptionSet aParams ) {
    super( aClass, aParams );
  }

  /**
   * Static constructor.
   *
   * @param <T> - referece type
   * @param aRefKey String - the key
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aIdsAndValues Object[] - initial {@link #params()} values as {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsGuiContextRefDef}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the key is an blank string
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static <T> TsGuiContextRefDef<T> create( String aRefKey, Class<T> aClass, Object... aIdsAndValues ) {
    return new TsGuiContextRefDef<>( aRefKey, aClass, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  /**
   * Static constructor with class name as the key.
   *
   * @param <T> - referece type
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aIdsAndValues Object[] - initial {@link #params()} values as {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsGuiContextRefDef}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static <T> TsGuiContextRefDef<T> create( Class<T> aClass, Object... aIdsAndValues ) {
    return new TsGuiContextRefDef<>( aClass, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextRefDef
  //

  @Override
  public T getRef( IEclipseContext aContext ) {
    Object ref = aContext.get( refKey() );
    return internalToRef( ref );
  }

  @Override
  public T getRef( IEclipseContext aContext, T aDefaultRef ) {
    Object ref = aContext.get( refKey() );
    if( ref == null ) {
      return aDefaultRef;
    }
    return internalToRef( ref );
  }

  @Override
  public void setRef( IEclipseContext aContext, T aRef ) {
    TsNullArgumentRtException.checkNulls( aContext, aRef );
    aContext.set( refKey(), refClass().cast( aRef ) );
  }

}
