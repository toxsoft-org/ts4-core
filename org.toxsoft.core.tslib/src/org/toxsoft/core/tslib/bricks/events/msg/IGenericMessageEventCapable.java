package org.toxsoft.core.tslib.bricks.events.msg;

/**
 * Mixin interface for entities having {@link IGenericMessageEventer} eventer.
 *
 * @author hazard157
 */
public interface IGenericMessageEventCapable {

  /**
   * Returns the generic message eventer.
   *
   * @return {@link IGenericMessageEventer} - the generic message eventer
   */
  IGenericMessageEventer genericMessageEventer();

}
