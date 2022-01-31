package org.toxsoft.core.tsgui.widgets.pdw;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.graphics.ETsFulcrum;
import org.toxsoft.core.tsgui.graphics.image.TsImage;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.utils.anim.IPausableAnimation;
import org.toxsoft.core.tsgui.utils.rectfit.RectFitInfo;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextable;
import org.toxsoft.core.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link TsImage} drawing widget.
 * <p>
 * {@link IPausableAnimation} methods has no effect if no image is set or image is {@link TsImage#isSingleFrame()}.
 *
 * @author hazard157
 */
public interface IPdwWidget
    extends ILazyControl<Control>, ITsContextable, IPausableAnimation {

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
   * Refreshes and redraws.
   */
  void redraw();

}
