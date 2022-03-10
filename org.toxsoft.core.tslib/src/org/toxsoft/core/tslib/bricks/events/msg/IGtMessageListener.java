package org.toxsoft.core.tslib.bricks.events.msg;

/**
 * Generic topic message listener.
 *
 * @author hazard157
 */
public interface IGtMessageListener {

  /**
   * Method is called when generic topic message is received.
   *
   * @param aMessage {@link GtMessage} - the message
   */
  void onGenericTopicMessage( GtMessage aMessage );

}
