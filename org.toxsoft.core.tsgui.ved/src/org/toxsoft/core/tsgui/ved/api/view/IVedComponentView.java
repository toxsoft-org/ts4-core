package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.api.*;

/**
 * VED component view to be displayed on {@link IVedScreen}.
 * <p>
 * Note: each view may allocate resources that must be `released by calling {@link #dispose()}.
 *
 * @author hazard157
 */
public interface IVedComponentView
    extends IVedDisposable {

  /**
   * Returns the means to draw the view on the SWT {@link Canvas}.
   *
   * @return {@link IVedPainter} - component painter
   */
  IVedPainter painter();

  /**
   * returns the porter responsible for changing coordinates, size, etc. of the component.
   *
   * @return {@link IVedPorter} - component geometrical representation changer
   */
  IVedPorter porter();

  /**
   * Returns the full information the component outline.
   *
   * @return {@link IVedOutline} - the component view's outline information
   */
  IVedOutline outline();

  /**
   * Returns the owner component which is represented by this view.
   *
   * @return {@link IVedComponent} - the owner component
   */
  IVedComponent owner();

}
