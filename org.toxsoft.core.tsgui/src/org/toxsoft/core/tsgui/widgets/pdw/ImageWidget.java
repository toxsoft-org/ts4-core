package org.toxsoft.core.tsgui.widgets.pdw;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Package-internal SWT control displaying the {@link Image}.
 * <p>
 * Notes of implementation:
 * <ul>
 * <li>this control is a {@link ILazyControl} so actual implementing control is {@link ImageCanvas};</li>
 * <li>the size {@link Control#getBounds()} of the control is computed by
 * {@link ImageCanvas#computeSize(int, int, boolean)} dynamically, depending on image fit strategy {@link #getFitInfo()}
 * and if size is fixed {@link #isPreferredSizeFixed()};</li>
 * <li>all operations on the widget does not rapaints the widget. To repaint call {@link #redraw()} explicitly.</li>
 * </ul>
 *
 * @author hazard157
 */
class ImageWidget
    implements ILazyControl<Control> {

  /**
   * Actual SWT implementation of a {@link ImageWidget}, the canvas to draw an {@link ImageWidget#image} on.
   *
   * @author hazard157
   */
  class ImageCanvas
      extends TsComposite {

    public ImageCanvas( Composite aParent, int aStyle ) {
      super( aParent, aStyle );
      addPaintListener( this::doPaint );
    }

    @Override
    public Point computeSize( int aWHint, int aHHint, boolean aChanged ) {
      if( isSizeFixed || image == null ) {
        return new Point( defaultSize.x(), defaultSize.y() );
      }
      // current image size (either real image size or default size)
      int w = imageSize.x();
      int h = imageSize.y();
      // which dimension (X or Y or both) we need to compute?
      boolean calcW = aHHint != SWT.DEFAULT;
      boolean calcH = aWHint != SWT.DEFAULT;
      // asked both dimension's default size - return current image size
      if( !calcW && !calcH ) {
        return new Point( w, h );
      }
      // both dimensions are fixed in arguments - return them, we'll fit in specified bounds
      if( calcW && calcH ) {
        return new Point( aWHint, aHHint );
      }
      // one dimension is asked while another is specified - compute asked one based on image aspect ratio and fit mode
      double aspect = ((double)w) / ((double)h);
      ERectFitMode fm = fitInfo.fitMode();
      if( calcW ) { // height is fixed, find width
        if( fm == ERectFitMode.FIT_BOTH || fm == ERectFitMode.FIT_FILL || fm == ERectFitMode.FIT_HEIGHT ) {
          w = (int)(aHHint * aspect);
        }
        return new Point( w, aHHint );
      }
      // width is fixed, find height
      if( fm == ERectFitMode.FIT_BOTH || fm == ERectFitMode.FIT_BOTH || fm == ERectFitMode.FIT_WIDTH ) {
        h = (int)(aWHint / aspect);
      }
      return new Point( aWHint, h );
    }

    /**
     * Scaled draws {@link ImageWidget#image} in current {@link #getBounds()}.
     * <p>
     * Following parameters are respected when drawing image:
     * <ul>
     * <li>{@link ImageWidget#image} - the image to draw (may be <code>null</code>);</li>
     * <li>{@link ImageWidget#imageSize} - either real image or defult dimensions if image is <code>null</code>;</li>
     * <li>{@link ImageWidget#fitInfo} - determines how to fit image in canvas bounds;</li>
     * <li>{@link ImageWidget#fulcrum} - determines how to place image in canvas.</li>
     * </ul>
     *
     * @param aEvent {@link PaintEvent} - drawing surface in event
     */
    void doPaint( PaintEvent aEvent ) {
      currentZoomFactor = RectFitInfo.DEFAULT_ZOOM;
      if( image == null || image.isDisposed() || imageSize == ITsPoint.ZERO ) {
        return;
      }
      Rectangle b = getBounds();
      ITsPoint canvasSize = new TsPoint( b.width, b.height );
      if( b.width <= 1 || b.height <= 1 ) {
        return;
      }
      ITsPoint sizeToPaint = fitInfo.calcSize( imageSize.x(), imageSize.y(), canvasSize.x(), canvasSize.y() );
      // calculate the coordinates of the upper left corner of the drawing
      int dbX = fulcrum.calcTopleftX( canvasSize.x(), sizeToPaint.x() );
      int dbY = fulcrum.calcTopleftY( canvasSize.y(), sizeToPaint.y() );
      aEvent.gc.drawImage( image, 0, 0, imageSize.x(), imageSize.y(), dbX, dbY, sizeToPaint.x(), sizeToPaint.y() );
      currentZoomFactor = ((double)sizeToPaint.x()) / ((double)imageSize.x());
    }

  }

  Image       image             = null;
  boolean     isSizeFixed       = false;
  ITsPoint    defaultSize       = new TsPoint( 256, 256 );
  ITsPoint    imageSize         = defaultSize;             // for image = null is equal to defaultSize
  ETsFulcrum  fulcrum           = ETsFulcrum.LEFT_TOP;
  RectFitInfo fitInfo           = RectFitInfo.BEST;
  ImageCanvas canvas            = null;
  boolean     needRelayout      = false;
  double      currentZoomFactor = RectFitInfo.DEFAULT_ZOOM;

  /**
   * Constructor.
   */
  public ImageWidget() {
    // nop
  }

  private void internalUpdateTsCompositeMinSize() {
    if( canvas != null ) {
      if( isSizeFixed ) {
        canvas.setMinimumSize( new TsPoint( defaultSize.x(), defaultSize.y() ) );
      }
      else {
        canvas.setMinimumSize( EThumbSize.minSize().pointSize() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    canvas = new ImageCanvas( aParent, SWT.NONE );
    return canvas;
  }

  @Override
  public Control getControl() {
    return canvas;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the image to display.
   * <p>
   * Note: does not repaints the widget.
   *
   * @param aImage {@link Image} - the image or <code>null</code>
   * @return boolean - <code>true</code> image size was changed hence the control need to re-layout
   */
  public boolean setImage( Image aImage ) {
    image = aImage;
    ITsPoint oldImageSize = imageSize;
    if( image != null ) {
      ImageData imageData = image.getImageData();
      if( imageData.width >= 1 && imageData.height >= 1 ) {
        imageSize = new TsPoint( imageData.width, imageData.height );
      }
    }
    else {
      imageSize = ITsPoint.ZERO;
    }
    needRelayout = !oldImageSize.equals( imageSize );
    return needRelayout;
  }

  /**
   * Returns the image anchoring strategy.
   *
   * @return {@link ETsFulcrum} - the fulcrum
   */
  public ETsFulcrum getFulcrum() {
    return fulcrum;
  }

  /**
   * Sets the image anchoring strategy.
   *
   * @param aFulcrum {@link ETsFulcrum} - the fulcrum
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setFulcrum( ETsFulcrum aFulcrum ) {
    TsNullArgumentRtException.checkNull( aFulcrum );
    if( fulcrum != aFulcrum ) {
      fulcrum = aFulcrum;
      needRelayout = true;
    }
  }

  public boolean isPreferredSizeFixed() {
    return isSizeFixed;
  }

  public void setPreferredSizeFixed( boolean aFixed ) {
    if( isSizeFixed != aFixed ) {
      isSizeFixed = aFixed;
      internalUpdateTsCompositeMinSize();
    }
  }

  public ITsPoint getImageSize() {
    return imageSize;
  }

  public ITsPoint getDefaultSize() {
    return defaultSize;
  }

  public void setDefaultSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    TsIllegalArgumentRtException.checkTrue( aSize.x() < 1 || aSize.y() < 1 );
    if( !defaultSize.equals( aSize ) ) {
      defaultSize = aSize;
      internalUpdateTsCompositeMinSize();
    }
  }

  public RectFitInfo getFitInfo() {
    return fitInfo;
  }

  public void setFitInfo( RectFitInfo aFitInfo ) {
    TsNullArgumentRtException.checkNull( aFitInfo );
    if( !fitInfo.equals( aFitInfo ) ) {
      fitInfo = aFitInfo;
      needRelayout = true;
    }
  }

  public double getRealZoomFactor() {
    return currentZoomFactor;
  }

  public void redraw() {
    if( canvas != null ) {
      canvas.redraw();
      if( needRelayout ) {
        canvas.getParent().layout();
      }
    }
  }

}
