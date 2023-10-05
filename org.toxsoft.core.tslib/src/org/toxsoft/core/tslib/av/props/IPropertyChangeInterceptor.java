package org.toxsoft.core.tslib.av.props;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Handles properties values change attempt.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public interface IPropertyChangeInterceptor<S> {

  /**
   * Called when properties values changes.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to properties. It initially contains
   * the same vales as <code>aNewValues</code>. Interceptor may remove values from <code>aValuesToSet</code> edit
   * existing, add any other properties values or event clear to cancel changes.
   * <p>
   * Cuurrent values of the properties may be accessed via <code>aSource</code> argument.
   *
   * @param aSource &lt;S&gt; - the event source
   * @param aNewValues {@link IOptionSetEdit} - changed properties values after change
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   */
  void interceptPropsChange( S aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet );

}
