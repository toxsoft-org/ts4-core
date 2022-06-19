package org.toxsoft.core.tsgui.mws.services.e4helper;

/**
 * Listens to the events when perspective changes.
 *
 * @author hazard157
 */
public interface ITsE4PerspectiveSwitchListener {

  /**
   * Called when perspective is changed.
   *
   * @param aSource {@link ITsE4Helper} - the event source
   * @param aPerspectiveId String - activated perspective ID or <code>null</code>
   */
  void onPerspectiveChanged( ITsE4Helper aSource, String aPerspectiveId );

}
