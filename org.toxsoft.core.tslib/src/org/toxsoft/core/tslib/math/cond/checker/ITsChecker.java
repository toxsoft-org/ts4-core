package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.utils.*;

/**
 * Interface of checks if some condition is met.
 * <p>
 * The checker is assumed to operate in some environment {@link AbstractTsSingleChecker#env()}. Once instance is
 * created, client may call {@link #checkCondition()} many times.
 * <p>
 * Checker extends {@link ICloseable} interface. Not all checker may need to be closed, however general rule is to
 * dispose checker when it is not needed any more.
 *
 * @author hazard157
 */
public interface ITsChecker
    extends ICloseable {

  /**
   * Returns the result of the check if the condition is met.
   *
   * @return boolean - <code>true</code> if condition is met
   */
  boolean checkCondition();

}
