package org.toxsoft.core.tslib.bricks.strid.more;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IStridablesRegisrty} registration validator.
 *
 * @author hazard157
 * @param <T> - type of the items in registry
 */
public interface IStridablesRegisrtyValidator<T extends IStridable> {

  /**
   * Checks if item may be unregistered (removed from the registry).
   *
   * @param aItem &lt;T&gt; - the item to be registered
   * @return {@link ValidationResult} - the result of check
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRegister( T aItem );

  /**
   * Checks if item may be unregistered (removed from the registry).
   *
   * @param aId String - identifier of the item to be unregistered
   * @return {@link ValidationResult} - the result of check
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default ValidationResult canUnregister( String aId ) {
    return ValidationResult.SUCCESS;
  }

}
