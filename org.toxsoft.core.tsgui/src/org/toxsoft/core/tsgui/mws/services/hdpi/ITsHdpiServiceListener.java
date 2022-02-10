package org.toxsoft.core.tsgui.mws.services.hdpi;

/**
 * Listener of {@link ITsHdpiService}.
 *
 * @author hazard157
 */
public interface ITsHdpiServiceListener {

  /**
   * Called when any icons size changed.
   *
   * @param aSource {@link ITsHdpiService} - event source
   * @param aEvent {@link TsHdpiIconSizeEvent} - the icon size change event
   */
  default void onAppGuiIconSizeChanged( ITsHdpiService aSource, TsHdpiIconSizeEvent aEvent ) {
    // nop
  }

}
