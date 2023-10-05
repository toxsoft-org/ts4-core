package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of the properties set {@link IPropertiesSetRo}.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertiesSet<S>
    extends IPropertiesSetRo<S>, IOpsSetter {

  /**
   * Set several properties values at once.
   * <p>
   * Unknown property ID in argument are silently ignored.
   *
   * @param aNewValues {@link IStringMap}&lt;{@link IAtomicValue}&gt; - map "property ID" - "property value"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException incompatible value type for any property
   */
  void setProps( IStringMap<IAtomicValue> aNewValues );

  /**
   * Sets all properties values to defaults.
   */
  void resetToDefaults();

  /**
   * Sets the value change interceptor.
   * <p>
   * <code>null</code> argument turns off interception.
   *
   * @param aInterceptor {@link IPropertyChangeInterceptor} - the interceptor or <code>null</code>
   */
  void setInterceptor( IPropertyChangeInterceptor<S> aInterceptor );

  // ------------------------------------------------------------------------------------
  // Inline method for convenience
  //

  @SuppressWarnings( "javadoc" )
  default void setPropPairs( Object... aIdsAndValues ) {
    setProps( OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

}
