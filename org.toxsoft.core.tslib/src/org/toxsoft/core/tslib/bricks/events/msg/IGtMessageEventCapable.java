package org.toxsoft.core.tslib.bricks.events.msg;

/**
 * Mixin interface for entities having {@link IGtMessageEventer} eventer.
 *
 * @author goga
 */
public interface IGtMessageEventCapable {

  /**
   * Returns the generic topic message eventer.
   *
   * @return {@link IGtMessageEventer} - the generic topic message eventer
   */
  IGtMessageEventer gtMessageEventer();

}
