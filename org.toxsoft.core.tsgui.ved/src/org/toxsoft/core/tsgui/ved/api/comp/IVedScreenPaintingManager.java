package org.toxsoft.core.tsgui.ved.api.comp;

import org.eclipse.swt.*;
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
   * Forces all outstanding paint requests for the widget to be processed before this method returns. If there are no
   * outstanding paint request, this method does nothing.
   * <p>
   * Note:
   * </p>
   * <ul>
   * <li>This method does not cause a redraw.</li>
   * <li>Some OS versions forcefully perform automatic deferred painting. This method does nothing in that case.</li>
   * </ul>
   *
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  void update();

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
