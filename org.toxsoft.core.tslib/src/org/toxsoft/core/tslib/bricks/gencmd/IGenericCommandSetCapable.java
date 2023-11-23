package org.toxsoft.core.tslib.bricks.gencmd;

/**
 * Mix-in interface of the entities having commander - the command set provider,
 *
 * @author hazard157
 */
public interface IGenericCommandSetCapable {

  /**
   * returns the commander - the means to send commands to the this entity.
   *
   * @return {@link IGenericCommandSetProvider} - the commander
   */
  IGenericCommandSetProvider commander();

}
