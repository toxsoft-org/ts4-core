package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * List of multiple validation results.
 * <p>
 * This is part of the "read-only-interface/mutable class" pattern part. Class {@link ValResList} has mutation methods.
 *
 * @author hazard157
 */
public interface IValResList {

  /**
   * Determines if list is empty of contains only {@link EValidationResultType#OK} results.
   *
   * @return boolean - <code>true</code> if {@link #results()} does not co0ntains any warning or error
   */
  boolean isOk();

  /**
   * Determines if list does not contains any error but at least one warning.
   *
   * @return boolean - <code>true</code> if list contains only OK and WARNING messages
   */
  boolean isWarning();

  /**
   * Determines if list has at least one {@link EValidationResultType#ERROR} result.
   *
   * @return boolean - <code>true</code> if any error is in {@link #results()} list
   */
  boolean isError();

  /**
   * Determines if {@link #results()} list is empty.
   *
   * @return boolean - if {@link #results()} list is empty
   */
  default boolean isEmpty() {
    return results().isEmpty();
  }

  /**
   * Return the validation results.
   *
   * @return {@link IList}&lt;{@link ValidationResult}&gt; - the validation results list
   */
  IList<ValidationResult> results();

}
