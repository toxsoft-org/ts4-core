package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.bricks.validator.vrl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * List of multiple validation results.
 * <p>
 * This is part of the "read-only-interface/mutable class" pattern part. Class {@link ValResList} has mutation methods.
 *
 * @author hazard157
 * @deprecated use {@link IVrList} instead
 */
@Deprecated
public interface IValResList {

  /**
   * Determines if list is empty or contains only {@link EValidationResultType#OK} results.
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

  /**
   * Returns the first worst result from the {@link #results()} list.
   * <p>
   * Returns first ERROR from list, if no error, returns first WARNING. Otherwise returns first item in
   * {@link #results()}. If results is an empty list returns {@link ValidationResult#SUCCESS}.
   *
   * @return {@link ValidationResult} - the first occurrence of the worst result
   */
  ValidationResult getFirstWorst();

}
