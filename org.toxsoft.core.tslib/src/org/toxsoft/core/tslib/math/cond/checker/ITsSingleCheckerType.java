package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Description of the single condition type.
 *
 * @author hazard157
 * @param <E> - the checker environment class
 */
public interface ITsSingleCheckerType<E>
    extends ITsSingleCondType {

  /**
   * Creates the single checker instance.
   *
   * @param aEnviron Object - the environment
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the checker description
   * @return {@link ITsChecker} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not of this type
   * @throws TsValidationFailedRtException failed {@link #validateParams(IOptionSet)}
   */
  ITsChecker create( E aEnviron, ITsSingleCondInfo aCombiCondInfo );

}
