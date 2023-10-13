package org.toxsoft.core.tsgui.bricks.tin;

/**
 * @author hazard157
 */
public interface ITinWidgetPropertyChangeListener {

  /**
   * Called when property (one of the root nodes) in the inspector changes it's value.
   *
   * @param aSource {@link ITinWidget} - the event source
   * @param aChangedPropId String - changed property ID, never is <code>null</code>
   */
  void onPropertyChange( ITinWidget aSource, String aChangedPropId );

}
