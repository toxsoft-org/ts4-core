package org.toxsoft.core.tslib.bricks.events.msg;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The messanger for {@link GtMessage} exchange.
 * <p>
 * Sends messages from sener to all subscribed listeners, including remote ones. Topics are identified by arbitrary
 * IDpath. There is no need to create the topic explicitly.
 * <p>
 * Depending on implementation, messages may or may not be stored, queued or delayed.
 *
 * @author hazard157
 */
public interface IGtMessenger {

  /**
   * Sends the message.
   *
   * @param aMessage {@link GtMessage} - the message
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void send( GtMessage aMessage );

  /**
   * Returns the eventer - message listening tool.
   *
   * @return {@link IGtMessageEventer} - the eventer
   */
  IGtMessageEventer eventer();

}
