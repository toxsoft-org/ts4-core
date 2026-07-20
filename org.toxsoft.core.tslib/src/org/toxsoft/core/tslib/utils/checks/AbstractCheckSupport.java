package org.toxsoft.core.tslib.utils.checks;

import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * {@link ITsCheckSupport} base helper base implementation.
 *
 * @author hazard157
 * @param <T> - type of elements in collection viewer
 */
public abstract class AbstractCheckSupport<T>
    implements ITsCheckSupport<T> {

  private final GenericChangeEventer genericChangeEventer;

  /**
   * Constructor.
   *
   * @param aEventSource Object - generic event source or <code>null</code> for <code>this</code>
   */
  public AbstractCheckSupport( Object aEventSource ) {
    if( aEventSource == null ) {
      genericChangeEventer = new GenericChangeEventer( this );
    }
    else {
      genericChangeEventer = new GenericChangeEventer( aEventSource );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCheckSupport
  //

  @Override
  final public boolean isChecksSupported() {
    return true;
  }

  @Override
  final public GenericChangeEventer checksChangeEventer() {
    return genericChangeEventer;
  }

}
