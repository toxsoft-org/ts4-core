package org.toxsoft.tslib.bricks.events.msg;

/**
 * Generic message listener.
 *
 * @author hazard157
 */
public interface IGenericMessageListener {

  /**
   * Method is called when generic message is received.
   *
   * @param aMessage {@link GenericMessage} - the message
   */
  void onGenericMessage( GenericMessage aMessage );

}
