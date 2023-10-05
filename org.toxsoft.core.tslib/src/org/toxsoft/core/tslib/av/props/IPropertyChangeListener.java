package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Listens to the properties values change event.
 * <p>
 * Properties characterize the entity of interest to the client. Such entity is declared to be source of the event.
 * Usually the entity implements {@link IPropertable} interface, so the &lt;S&gt; type is the type of the reference to
 * the {@link IPropertable} Java object.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertyChangeListener<S> {

  /**
   * Called when properties values changes.
   * <p>
   * Argument <code>aNewValues</code> contain only changed properties while <code>aOldValues</code> contains all values
   * as listed by {@link IPropertiesSetRo#propDefs()}.
   *
   * @param aSource &lt;S&gt; - the event source
   * @param aNewValues {@link IOptionSet} - changed properties values after change
   * @param aOldValues {@link IOptionSet} - all properties values before change
   */
  void onPropsChanged( S aSource, IOptionSet aNewValues, IOptionSet aOldValues );

}
