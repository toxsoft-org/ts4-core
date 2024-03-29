package org.toxsoft.core.tsgui.ved.screen;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * On the VED screen viewer canvas the content of the model {@link IVedScreenModel} is painted.
 * <p>
 * Also screen view managers user input handlers.
 *
 * @author hazard157
 */
public interface IVedScreenView
    extends ID2Conversionable {

  /**
   * Determines if canvas background is drawn.
   * <p>
   * Canvas background filling parameters are defined by configuration parameter {@link IVedCanvasCfg#fillInfo()}.
   *
   * @return boolean - <code>true</code> if background fill parameters is applied
   */
  boolean isBackgroundFill();

  /**
   * Changes background filling {@link #isBackgroundFill()}.
   *
   * @param aFill boolean - <code>true</code> if background fill parameters is applied
   */
  void setBackgroundFill( boolean aFill );

  /**
   * Returns the canvas configuration set by {@link #setCanvasConfig(IVedCanvasCfg)}.
   *
   * @return {@link IVedCanvasCfg} - VED canvas configuration
   */
  IVedCanvasCfg canvasConfig();

  /**
   * Sets the canvas configuration.
   * <p>
   * Note: setting canvas config resets current conversion value to the {@link IVedCanvasCfg#conversion()}.
   *
   * @param aCanvasConfig {@link IVedCanvasCfg} - VED canvas configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setCanvasConfig( IVedCanvasCfg aCanvasConfig );

  /**
   * Tries to redraw whole screen as soon as possible.
   * <p>
   * Simply calls {@link Canvas#update()}.
   */
  void redraw();

  /**
   * Forces all outstanding paint requests for the widget to be processed before this method returns.
   * <p>
   * Simply calls {@link Canvas#update()}.
   */
  void update();

  /**
   * Returns the coordinates converter between VED coordinates spaces.
   *
   * @return {@link IVedCoorsConverter} - the converter
   */
  IVedCoorsConverter coorsConverter();

  /**
   * Returns VISEL IDs under the point specified in the SWT coordinates space.
   * <p>
   * Returns the ordered list: topmost VISEL is the first element, Z-order lowest VISEL is last in the list.
   *
   * @param aSwtCoors {@link ITsPoint} - the SWT coordinates on VED screen
   * @return {@link IStringList} - the VISEL IDs
   */
  IStringList listViselIdsAtPoint( ITsPoint aSwtCoors );

  /**
   * Redraws the specified VISEL.
   * <p>
   * More precisely redraws the region the VISEL is responsible for. Default behavior is to redraw rectangular SWT area
   * containing VISEL. But for optimization VISEL may request redraw of the complex shaped area.
   *
   * @param aViselId String - the view ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such view
   */
  void redrawVisel( String aViselId );

  /**
   * Redraws the region of the screen.
   *
   * @param aScreenRect {@link ITsRectangle} - SWT rectangular area to redraw, specified in pixels
   */
  void redrawSwtRect( ITsRectangle aScreenRect );

  /**
   * Sets mouse cursor shape on screen.
   *
   * @param aCursor {@link Cursor} - mouse cursor or <code>null</code> for default
   */
  void setCursor( Cursor aCursor );

  /**
   * Returns the SWT control implementing the screen.
   *
   * @return {@link Control} - the screen SWT representation
   */
  Control getControl();

  /**
   * Returns canvas configuration change (via {@link #setCanvasConfig(IVedCanvasCfg)}) eventer.
   *
   * @return {@link IGenericChangeEventer} - canvas config change eventer
   */
  IGenericChangeEventer configChangeEventer();

}
