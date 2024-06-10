package org.toxsoft.core.tslib.bricks.ctx.impl;

import static org.toxsoft.core.tslib.bricks.ctx.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsContextRefDef} implementation.
 *
 * @author hazard157
 * @param <T> - the reference type
 */
public class TsContextRefDef<T>
    implements ITsContextRefDef<T> {

  private final String         refKey;
  private final Class<T>       refClass;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   *
   * @param aRefKey String - the key
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aParams {@link IOptionSet} - initial values for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the key is an blank string
   */
  public TsContextRefDef( String aRefKey, Class<T> aClass, IOptionSet aParams ) {
    refKey = TsErrorUtils.checkNonBlank( aRefKey );
    refClass = TsNullArgumentRtException.checkNull( aClass );
    params.addAll( aParams );
  }

  /**
   * Constructs with class name as the key.
   *
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aParams {@link IOptionSet} - initial values for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsContextRefDef( Class<T> aClass, IOptionSet aParams ) {
    refClass = TsNullArgumentRtException.checkNull( aClass );
    params.addAll( aParams );
    refKey = aClass.getName();
  }

  /**
   * Static constructor.
   *
   * @param <T> - reference type
   * @param aRefKey String - the key
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aIdsAndValues Object[] - initial {@link #params()} values as {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsContextRefDef}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the key is an blank string
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static <T> TsContextRefDef<T> create( String aRefKey, Class<T> aClass, Object... aIdsAndValues ) {
    return new TsContextRefDef<>( aRefKey, aClass, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  /**
   * Static constructor with class name as the key.
   *
   * @param <T> - referece type
   * @param aClass {@link Class}&lt;T&gt; - the reference class
   * @param aIdsAndValues Object[] - initial {@link #params()} values as {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsContextRefDef}&lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static <T> TsContextRefDef<T> create( Class<T> aClass, Object... aIdsAndValues ) {
    return new TsContextRefDef<>( aClass, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  @SuppressWarnings( "unchecked" )
  protected T internalToRef( Object aRef ) {
    if( aRef == null ) {
      if( isMandatory() ) {
        throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_REF, refKey );
      }
      return null;
    }
    if( !refClass.isInstance( aRef ) ) {
      String msg = String.format( FMT_ERR_INV_CLASS_REF, refKey, aRef.getClass().getName(), refClass.getName() );
      throw new ClassCastException( msg );
    }
    return (T)aRef;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextRefDef
  //

  @Override
  public String refKey() {
    return refKey;
  }

  @Override
  public Class<T> refClass() {
    return refClass;
  }

  @Override
  public T getRef( ITsContextRo aContext ) {
    Object ref = aContext.find( refKey );
    return internalToRef( ref );
  }

  @Override
  public T getRef( ITsContextRo aContext, T aDefaultRef ) {
    Object ref = aContext.find( refKey );
    if( ref == null ) {
      return aDefaultRef;
    }
    return internalToRef( ref );
  }

  @Override
  public void setRef( ITsContext aContext, T aRef ) {
    TsNullArgumentRtException.checkNulls( aContext, aRef );
    aContext.put( refKey, refClass.cast( aRef ) );
  }

}
