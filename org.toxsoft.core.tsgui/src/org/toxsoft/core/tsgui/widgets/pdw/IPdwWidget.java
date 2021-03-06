package org.toxsoft.core.tsgui.widgets.pdw;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link TsImage} drawing widget.
 * <p>
 * {@link IPausableAnimation} methods has no effect if no image is set or image is {@link TsImage#isSingleFrame()}.
 * <p>
 * TODO describe how widget size is managed depending on settings and image existance and size
 *
 * @author hazard157
 */
public interface IPdwWidget
    extends ILazyControl<Control>, ITsContextable, IPausableAnimation, ITsMouseEventProducer {

  /**
   * Returns displayed image.
   *
   * @return {@link TsImage} - shown image or <code>null</code>
   */
  TsImage getTsImage();

  /**
   * Sets the image to display.
   * <p>
   * Has no visual effect until {@link #redraw()}.
   *
   * @param aImage {@link TsImage} - the image to show or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setTsImage( TsImage aImage );

  /**
   * Returns the size of {@link #getTsImage()}.
   *
   * @return {@link ITsPoint} - the image size or {@link ITsPoint#ZERO} if image is <code>null</code>
   */
  ITsPoint getImageSize();

  /**
   * Returns the image placement.
   * <p>
   * Determines how image is located insize display area.
   *
   * @return {@link ETsFulcrum} - image placement
   */
  ETsFulcrum getFulcrum();

  /**
   * Sets the image placement settings.
   * <p>
   * Has no visual effect until {@link #redraw()}.
   *
   * @param aFulcrum {@link ETsFulcrum} - image placement settings
   */
  void setFulcrum( ETsFulcrum aFulcrum );

  /**
   * Returns image display settings.
   *
   * @return {@link RectFitInfo} - image display settings
   */
  RectFitInfo getFitInfo();

  /**
   * Sets image display settings.
   * <p>
   * Has no visual effect until {@link #redraw()}.
   *
   * @param aInfo {@link RectFitInfo} - image display settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setFitInfo( RectFitInfo aInfo );

  /**
   * Returns preffered size of the image display area.
   *
   * @return {@link ITsPoint} - the area size in pixels
   */
  ITsPoint getAreaPreferredSize();

  /**
   * Sets preffered size of the image display area.
   * <p>
   * Has no visual effect until {@link #redraw()}.
   *
   * @param aSize {@link ITsPoint} - the area size in pixels
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException width or height is 0 or negative
   */
  void setAreaPreferredSize( ITsPoint aSize );

  /**
   * Determines if preffered size of the widget is fixed.
   *
   * @return boolean - the flag that preffered size of the widget is fixed
   */
  boolean isPreferredSizeFixed();

  /**
   * Sets value of the {@link #isPreferredSizeFixed()}.
   *
   * @param aFixed boolean - the flag that preffered size of the widget is fixed
   */
  void setPreferredSizeFixed( boolean aFixed );

  /**
   * Refreshes and redraws.
   */
  void redraw();

}
