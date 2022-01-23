package org.toxsoft.tslib.bricks.events.msg;

/**
 * Mixin interface for entities having {@link IGenericMessageEventer} eventer.
 *
 * @author goga
 */
public interface IGenericMessageEventCapable {

  /**
   * Returns the generic message eventer.
   *
   * @return {@link IGenericMessageEventer} - the generic message eventer
   */
  IGenericMessageEventer genericChangeEventer();

}
