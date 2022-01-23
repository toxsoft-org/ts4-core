package org.toxsoft.tslib.bricks.events.change;

/**
 * General purpose any changes event listener.
 *
 * @author hazard157
 */
public interface IGenericChangeListener {

  /**
   * Method is call when specified change event happens.
   *
   * @param aSource Object - the event source
   */
  void onGenericChangeEvent( Object aSource );

}
