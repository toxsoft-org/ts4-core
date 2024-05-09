package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Description of the single condition type.
 *
 * @author hazard157
 */
public interface ITsSingleCheckerType
    extends ITsSingleCondType {

  /**
   * Creates the single checker instance.
   *
   * @param <E> - expected environment class
   * @param aEnviron &lt;E&gt; - the environment
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the checker description
   * @return {@link AbstractTsSingleChecker} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not of this type
   * @throws TsValidationFailedRtException failed {@link #validateParams(IOptionSet)}
   */
  <E> AbstractTsSingleChecker<E> create( E aEnviron, ITsSingleCondInfo aCombiCondInfo );

}
