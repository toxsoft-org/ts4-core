package org.toxsoft.core.tsgui.widgets.pdw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.graphics.ETsFulcrum;
import org.toxsoft.core.tsgui.graphics.image.EThumbSize;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.utils.rectfit.ERectFitMode;
import org.toxsoft.core.tsgui.utils.rectfit.RectFitInfo;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.core.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

class ImageWidget
    implements ILazyControl<Control> {

  class ImageCanvas
      extends TsComposite {

    public ImageCanvas( Composite aParent, int aStyle ) {
      super( aParent, aStyle );
      addPaintListener( new PaintListener() {

        @Override
        public void paintControl( PaintEvent aE ) {
          doPaint( aE );
        }

      } );
    }

    @Override
    public Point computeSize( int aWHint, int aHHint, boolean aChanged ) {
      if( isSizeFixed ) {
        return new Point( defaultSize.x(), defaultSize.y() );
      }
      // текущий размер изображения (он или реальный, или по умолчанию)
      int w = imageSize.x();
      int h = imageSize.y();
      // какие размеры нужно считать?
      boolean calcW = aHHint != SWT.DEFAULT;
      boolean calcH = aWHint != SWT.DEFAULT;
      // запрошены оба размера по умолчанию - возвращаем текущий размер изображения
      if( !calcW && !calcH ) {
        return new Point( w, h );
      }
      // заданы оба размера в аргументах - возвращаем заданные размеры,будем вписываться
      if( calcW && calcH ) {
        return new Point( aWHint, aHHint );
      }
      // задан один размер, вычислим другой исходя сохранения соотношения сторон в режимах адаптивного масштабирования
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
  RectFitInfo fitInfo      = RectFitInfo.NONE;
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
