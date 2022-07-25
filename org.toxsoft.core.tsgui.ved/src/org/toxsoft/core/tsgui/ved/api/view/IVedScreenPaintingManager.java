package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages painters used to paint everything except component views on the screen.
 *
 * @author hazard157
 */
public interface IVedScreenPaintingManager {

  /**
   * Adds views decorator painter to the end of decorators list.
   * <p>
   * If decorator is already added to the list then does nothing.
   * <p>
   * Method call does not causes screen to redraw, use {@link #redraw()} after.
   *
   * @param aDecorator {@link IVedViewDecorator} - the decorator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addViewsDecorator( IVedViewDecorator aDecorator );

  /**
   * Removes views decorator painter from the decorators list.
   * <p>
   * If decorator is not added to the list then does nothing.
   * <p>
   * Method call does not causes screen to redraw, use {@link #redraw()} after.
   *
   * @param aDecorator {@link IVedViewDecorator} - the decorator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeViewsDecorator( IVedViewDecorator aDecorator );

  /**
   * Adds screen decorator painter to the end of decorators list.
   * <p>
   * If decorator is already added to the list then does nothing.
   * <p>
   * Method call does not causes screen to redraw, use {@link #redraw()} after.
   *
   * @param aDecorator {@link IVedScreenDecorator} - the decorator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addScreensDecorator( IVedScreenDecorator aDecorator );

  /**
   * Removes screen decorator painter from the decorators list.
   * <p>
   * If decorator is not added to the list then does nothing.
   * <p>
   * Method call does not causes screen to redraw, use {@link #redraw()} after.
   *
   * @param aDecorator {@link IVedScreenDecorator} - the decorator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeScreensDecorator( IVedScreenDecorator aDecorator );

  /**
   * Tries to redraw whole screen as soon as possible.
   */
  void redraw();

  /**
   * Redraws the specified view in its bounds.
   *
   * @param aViewId String - the view ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such view
   */
  void redrawView( String aViewId );

  /**
   * Redraws the region of the screen.
   *
   * @param aScreenRect {@link ITsRectangle} - screen region to redraw in pixels
   */
  void redrawRect( ITsRectangle aScreenRect );

  /**
   * Sets mouse cursor shape on screen.
   *
   * @param aCursor {@link Cursor} - mouse cursor or <code>null</code> for default
   */
  void setCursor( Cursor aCursor );

}
