package org.toxsoft.core.tslib.bricks.events.msg;

/**
 * Mixin interface for entities having {@link IGenericTopicMessageEventer} eventer.
 *
 * @author goga
 */
public interface IGenericTopicMessageEventCapable {

  /**
   * Returns the generic topic message eventer.
   *
   * @return {@link IGenericTopicMessageEventer} - the generic topic message eventer
   */
  IGenericTopicMessageEventer genericTopicMessageEventer();

}
