package org.toxsoft.core.tslib.math.cond.checker;

/**
 * Interface of checks if some condition is met.
 * <p>
 * The checker is assumed to operate in some environment {@link AbstractTsSingleChecker#env()}. Once instance is created,
 * client may call {@link #checkCondition()} many times.
 *
 * @author hazard157
 */
public interface ITsChecker {

  /**
   * Returns the result of the check if the condition is met.
   *
   * @return boolean - <code>true</code> if condition is met
   */
  boolean checkCondition();

}
