package org.toxsoft.core.tslib.math.cond.filter;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Description of the single condition type.
 *
 * @author hazard157
 */
public interface ITsSingleFilterType
    extends ITsSingleCondType {

  /**
   * Creates the filter.
   *
   * @param <T> - expected type of filtered objects
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the filter description
   * @return {@link ITsFilter} - created filter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not of this type
   * @throws TsValidationFailedRtException failed {@link #validateParams(IOptionSet)}
   */
  <T> ITsFilter<T> create( ITsSingleCondInfo aCombiCondInfo );

}
