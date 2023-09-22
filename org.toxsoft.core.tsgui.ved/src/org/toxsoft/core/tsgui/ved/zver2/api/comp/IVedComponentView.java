package org.toxsoft.core.tsgui.ved.zver2.api.comp;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * VED component view to be displayed on {@link IVedScreen}.
 *
 * @author hazard157
 */
public interface IVedComponentView
    extends IStridable, ID2Conversionable {

  /**
   * Returns the means to draw the view on the SWT {@link Canvas}.
   *
   * @return {@link IVedViewPainter} - component painter
   */
  IVedViewPainter painter();

  /**
   * returns the porter responsible for changing coordinates, size, etc. of the component.
   *
   * @return {@link IVedViewPorter} - component geometrical representation changer
   */
  IVedViewPorter porter();

  /**
   * Returns the full information the component outline.
   *
   * @return {@link IVedOutline} - the component view's outline information
   */
  IVedOutline outline();

  /**
   * Returns the values of the external data.
   *
   * @return {@link IOptionSetEdit} - the external data
   */
  IOptionSetEdit extdata();

  /**
   * Returns the component which is represented by this view.
   *
   * @return {@link IVedComponent} - the owner component
   */
  IVedComponent component();

  /**
   * Returns the screen where this view of the component is painted.
   *
   * @return {@link IVedScreen} - owner screen
   */
  IVedScreen ownerScreen();

  /**
   * Redraws the single view as soon as possible.
   */
  void redraw();

}
