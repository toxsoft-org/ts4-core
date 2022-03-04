package org.toxsoft.core.tslib.bricks.events.msg;

/**
 * Generic topic message listener.
 *
 * @author hazard157
 */
public interface IGenericTopicMessageListener {

  /**
   * Method is called when generic topic message is received.
   *
   * @param aMessage {@link GenericTopicMessage} - the message
   */
  void onGenericTopicMessage( GenericTopicMessage aMessage );

}
