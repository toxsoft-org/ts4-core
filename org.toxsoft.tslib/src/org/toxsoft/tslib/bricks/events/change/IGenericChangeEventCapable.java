package org.toxsoft.tslib.bricks.events.change;

/**
 * Mixin interface for entities having {@link IGenericChangeEventer} eventer.
 *
 * @author goga
 */
public interface IGenericChangeEventCapable {

  /**
   * Returns the generic change eventer.
   *
   * @return {@link IGenericChangeEventer} - the generic change eventer
   */
  IGenericChangeEventer genericChangeEventer();

}
