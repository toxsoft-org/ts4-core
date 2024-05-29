package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsChecker} implementation base.
 *
 * @author hazard157
 * @param <E> - the checker environment class
 */
public abstract class AbstractTsSingleChecker<E>
    implements ITsChecker {

  private final E environ;

  /**
   * Constructor.
   *
   * @param aEnv &lt;E&gt; - the environment of the condition to be checked
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractTsSingleChecker( E aEnv ) {
    TsNullArgumentRtException.checkNull( aEnv );
    environ = aEnv;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the environment the checker is operating in.
   *
   * @return &lt;E&gt; - the environment of the condition to be checked
   */
  final public E env() {
    return environ;
  }

  // ------------------------------------------------------------------------------------
  // ITsChecker
  //

  @Override
  public abstract boolean checkCondition();

  @Override
  public abstract void close();

}
