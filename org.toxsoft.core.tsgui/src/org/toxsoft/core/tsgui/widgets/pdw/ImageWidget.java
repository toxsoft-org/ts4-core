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

class ImageWidget
    implements ILazyControl<Control> {

  /**
   * Canvas to draw an {@link ImageWidget#image} on.
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
      // current image size (either reeal image size or default size)
      int w = imageSize.x();
      int h = imageSize.y();
      // which dimension (X or Y or both) we need to cumpute?
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
      // one dimansion is asked while another is specified - copmute asked one based on image aspect ratio and fit mode
      double aspect = ((double)w) / ((double)h);
      ERectFitMode fitMode = fitInfo.fitMode();
      if( calcW ) {
        if( fitMode == ERectFitMode.FIT_BOTH || fitMode == ERectFitMode.FIT_HEIGHT ) {
          w = (int)(aHHint * aspect);
        }
        return new Point( w, aHHint );
      }
      if( fitMode == ERectFitMode.FIT_BOTH || fitMode == ERectFitMode.FIT_WIDTH ) {
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
      if( image == null || image.isDisposed() || imageSize == ITsPoint.ZERO ) {
        return;
      }
      Rectangle b = getBounds();
      ITsPoint canvasSize = new TsPoint( b.width, b.height );
      if( b.width <= 1 || b.height <= 1 ) {
        return;
      }
      ITsPoint sizeToPaint = fitInfo.calcSize( imageSize.x(), imageSize.y(), canvasSize.x(), canvasSize.y() );
      // вычислим координаты левого верхнего угла рисования
      int dbX = fulcrum.calcTopleftX( canvasSize.x(), sizeToPaint.x() );
      int dbY = fulcrum.calcTopleftY( canvasSize.y(), sizeToPaint.y() );
      aEvent.gc.drawImage( image, 0, 0, imageSize.x(), imageSize.y(), dbX, dbY, sizeToPaint.x(), sizeToPaint.y() );
    }

  }

  Image       image        = null;
  boolean     isSizeFixed  = false;
  ITsPoint    defaultSize  = new TsPoint( 256, 256 );
  ITsPoint    imageSize    = defaultSize;            // for image = null is equal to defaultSize
  ETsFulcrum  fulcrum      = ETsFulcrum.LEFT_TOP;
  RectFitInfo fitInfo      = RectFitInfo.BEST;
  ImageCanvas canvas       = null;
  boolean     needRelayout = false;

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

  public ETsFulcrum getFulcrum() {
    return fulcrum;
  }

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

  public void redraw() {
    if( canvas != null ) {
      canvas.redraw();
      if( needRelayout ) {
        canvas.getParent().layout();
      }
    }
  }

}
