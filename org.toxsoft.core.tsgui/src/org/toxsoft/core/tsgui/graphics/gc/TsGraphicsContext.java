package org.toxsoft.core.tsgui.graphics.gc;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * {@link ITsGraphicsContext} implementation.
 *
 * @author hazard157
 */
public class TsGraphicsContext
    implements ITsGraphicsContext {

  // static class ErrorHandler
  // implements Consumer<Error> {
  //
  // @Override
  // public void accept( Error aError ) {
  // System.out.println( ">>>>> SWT Resource error: " + aError ); //$NON-NLS-1$
  // }
  // }
  //
  // static final ErrorHandler errorHandler = new ErrorHandler();

  private final GC gc;

  private final ITsGuiContext tsContext;

  private final ITsRectangle drawingArea;

  private TsLineInfo lineInfo = null;

  private TsFillInfo fillInfo = null;

  private TsBorderInfo borderInfo = TsBorderInfo.NONE;

  private TsImage bkImage = null;

  private TsImage unknownImage = null;

  private int unknownImageSize = 32;

  /**
   * Constructor for paint event drawing.
   *
   * @param aEvent {@link PaintEvent} - the paint event
   * @param aTsContext {@link ITsGuiContext} - the context
   */
  public TsGraphicsContext( PaintEvent aEvent, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    gc = aEvent.gc;
    drawingArea = new TsRectangle( aEvent.x, aEvent.y, aEvent.width, aEvent.height );
    // gc.setNonDisposeHandler( errorHandler );
  }

  /**
   * Constructor to draw on an existing SWT drawing context.
   *
   * @param aGc {@link GC} - the SWT drawing context
   * @param aTsContext {@link ITsGuiContext} - the context
   */
  public TsGraphicsContext( GC aGc, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    gc = aGc;
    drawingArea = new TsRectangle( 0, 0, 1, 1 );
    // gc.setNonDisposeHandler( errorHandler );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void fillImage( TsImage aImage, int aX, int aY, int aWidth, int aHeight, EImageFillKind aFillKind ) {
    switch( aFillKind ) {
      case CENTER:
        fillCenterImage( aImage, aX, aY, aWidth, aHeight );
        break;
      case FIT:
        fillFitImage( aImage, aWidth, aHeight );
        break;
      case TILE:
        fillTileImage( aImage, aX, aY, aWidth, aHeight );
        break;
      default:
        throw new IllegalArgumentException( "Unexpected value: " + aFillKind ); //$NON-NLS-1$
    }
  }

  private void fillTileImage( TsImage aImage, int aX, int aY, int aWidth, int aHeight ) {
    ImageData imd = aImage.image().getImageData();
    int width = imd.width;
    int height = imd.height;

    int x = aX;
    int y = aY;

    while( x < aX + aWidth ) {
      y = aY;
      while( y < aY + aHeight ) {
        gc.drawImage( aImage.image(), x, y );
        y += height;
      }
      x += width;
    }
  }

  private void fillCenterImage( TsImage aImage, int aX, int aY, int aWidth, int aHeight ) {
    ITsPoint imgSize = aImage.imageSize();
    int imgX = aX + (aWidth - imgSize.x()) / 2;
    int imgY = aY + (aHeight - imgSize.y()) / 2;

    gc.drawImage( aImage.image(), imgX, imgY );
  }

  private void fillFitImage( TsImage aImage, int aWidth, int aHeight ) {
    ITsPoint imgSize = aImage.imageSize();

    double kWidth = (double)aWidth / imgSize.x();
    double kHeight = (double)aHeight / imgSize.y();

    double scaleFactor = 1.0;

    if( kWidth < kHeight ) {
      scaleFactor = kWidth;
    }
    if( kHeight < kWidth ) {
      scaleFactor = kHeight;
    }

    ITsPoint newSize = new TsPoint( (int)(imgSize.x() * scaleFactor), (int)(imgSize.y() * scaleFactor) );

    int imgX = (aWidth - newSize.x()) / 2;
    int imgY = (aHeight - newSize.y()) / 2;

    gc.drawImage( aImage.image(), 0, 0, imgSize.x(), imgSize.y(), imgX, imgY, newSize.x(), newSize.y() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // TsGraphicsContext
  //

  @Override
  public GC gc() {
    return gc;
  }

  @Override
  public ITsRectangle drawArea() {
    return drawingArea;
  }

  @Override
  public void setLineInfo( TsLineInfo aLineInfo ) {
    lineInfo = aLineInfo;
  }

  @Override
  public void drawLine( int aX1, int aY1, int aX2, int aY2 ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    gc.drawLine( aX1, aY1, aX2, aY2 );
  }

  @Override
  public void drawRect( int aX, int aY, int aWidth, int aHeight ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    gc.drawRectangle( aX, aY, aWidth, aHeight );
  }

  @Override
  public void drawRoundRect( int aX, int aY, int aWidth, int aHeight, int aArcWdth, int aArcHeight ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    gc.drawRoundRectangle( aX, aY, aWidth, aHeight, aArcWdth, aArcHeight );
  }

  @Override
  public void drawPath( Path aPath, int aX, int aY ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    Transform oldTransform = new Transform( gc.getDevice() );
    gc.getTransform( oldTransform );
    Transform tr = new Transform( gc.getDevice() );
    gc.getTransform( tr );
    tr.translate( aX, aY );
    gc.setTransform( tr );
    tr.dispose();

    gc.drawPath( aPath );

    gc.setTransform( oldTransform );
    oldTransform.dispose();
  }

  @Override
  public void setFillInfo( TsFillInfo aFillInfo ) {
    fillInfo = aFillInfo;
  }

  @Override
  public void fillRect( int aX, int aY, int aWidth, int aHeight ) {
    Pattern pattern = null;
    if( fillInfo != null ) {
      switch( fillInfo.kind() ) {
        case NONE:
          return;
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          gc.setBackground( colorManager().getColor( rgba.rgb ) );
          gc.setAlpha( rgba.alpha );
          break;
        case GRADIENT:
          IGradient grad = fillInfo.gradientFillInfo().createGradient( tsContext );
          if( grad != null ) {
            pattern = grad.pattern( gc, aWidth, aHeight );
            gc.setBackgroundPattern( pattern );
          }
          break;
        case IMAGE:
          TsImageFillInfo imgInfo = fillInfo.imageFillInfo();
          if( imgInfo.imageDescriptor() == TsImageDescriptor.NONE ) {
            unknownImage = imageManager().createUnknownImage( unknownImageSize );
            bkImage = unknownImage;
          }
          else {
            bkImage = imageManager().getImage( imgInfo.imageDescriptor() );
          }
          break;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fillInfo.kind() ); //$NON-NLS-1$
      }
    }
    Transform oldTransform = new Transform( gc.getDevice() );
    gc.getTransform( oldTransform );
    Transform tr = new Transform( gc.getDevice() );
    gc.getTransform( tr );
    tr.translate( aX, aY );
    gc.setTransform( tr );
    tr.dispose();
    if( fillInfo.kind() == ETsFillKind.IMAGE ) {
      TsImageFillInfo imgInfo = fillInfo.imageFillInfo();
      fillImage( bkImage, 0, 0, aWidth, aHeight, imgInfo.kind() );
      if( unknownImage != null ) {
        unknownImage.dispose();
        unknownImage = null;
      }
    }
    else {
      gc.fillRectangle( 0, 0, aWidth, aHeight );
    }
    if( pattern != null ) {
      pattern.dispose();
    }
    gc.setTransform( oldTransform );
    oldTransform.dispose();
  }

  @Override
  public void fillRoundRect( int aX, int aY, int aWidth, int aHeight, int aArcWidth, int aArcHeight ) {
    Pattern pattern = null;
    if( fillInfo != null ) {
      switch( fillInfo.kind() ) {
        case NONE:
          return;
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          gc.setBackground( colorManager().getColor( rgba.rgb ) );
          gc.setAlpha( rgba.alpha );
          break;
        case GRADIENT:
          IGradient grad = fillInfo.gradientFillInfo().createGradient( tsContext );
          if( grad != null ) {
            pattern = grad.pattern( gc, aWidth, aHeight );
            // pattern.setNonDisposeHandler( errorHandler );
            gc.setBackgroundPattern( pattern );
          }
          break;
        case IMAGE:
          TsImageFillInfo imgInfo = fillInfo.imageFillInfo();
          if( imgInfo.imageDescriptor() == TsImageDescriptor.NONE ) {
            bkImage = unknownImage;
          }
          else {
            bkImage = imageManager().getImage( imgInfo.imageDescriptor() );
          }
          if( imgInfo.kind() == EImageFillKind.TILE ) {
            fillTileImage( bkImage, aX, aY, aWidth, aHeight );
          }
          return;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fillInfo.kind() ); //$NON-NLS-1$
      }
    }
    Transform oldTransform = new Transform( gc.getDevice() );
    // oldTransform.setNonDisposeHandler( errorHandler );
    gc.getTransform( oldTransform );
    Transform tr = new Transform( gc.getDevice() );
    // tr.setNonDisposeHandler( errorHandler );
    gc.getTransform( tr );
    tr.translate( aX, aY );
    gc.setTransform( tr );
    tr.dispose();
    gc.fillRoundRectangle( 0, 0, aWidth, aHeight, aArcWidth, aArcHeight );
    if( pattern != null ) {
      pattern.dispose();
    }
    gc.setTransform( oldTransform );
    oldTransform.dispose();
  }

  @Override
  public void fillOval( int aX, int aY, int aWidth, int aHeight ) {
    Pattern pattern = null;
    if( fillInfo != null ) {
      switch( fillInfo.kind() ) {
        case NONE:
          return;
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          gc.setBackground( colorManager().getColor( rgba.rgb ) );
          gc.setAlpha( rgba.alpha );
          break;
        case GRADIENT:
          IGradient grad = fillInfo.gradientFillInfo().createGradient( tsContext );
          if( grad != null ) {
            pattern = grad.pattern( gc, aWidth, aHeight );
            gc.setBackgroundPattern( pattern );
          }
          break;
        case IMAGE:
          TsImageFillInfo imgInfo = fillInfo.imageFillInfo();
          if( imgInfo.imageDescriptor() == TsImageDescriptor.NONE ) {
            unknownImage = imageManager().createUnknownImage( unknownImageSize );
            bkImage = unknownImage;
          }
          else {
            bkImage = imageManager().getImage( imgInfo.imageDescriptor() );
          }
          if( imgInfo.kind() == EImageFillKind.TILE ) {
            fillTileImage( bkImage, aX, aY, aWidth, aHeight );
          }
          if( unknownImage != null ) {
            unknownImage.dispose();
            unknownImage = null;
          }
          return;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fillInfo.kind() ); //$NON-NLS-1$
      }
    }
    Transform oldTransform = new Transform( gc.getDevice() );
    gc.getTransform( oldTransform );
    Transform tr = new Transform( gc.getDevice() );
    gc.getTransform( tr );
    tr.translate( aX, aY );
    gc.setTransform( tr );
    tr.dispose();
    gc.fillOval( 0, 0, aWidth, aHeight );
    if( pattern != null ) {
      pattern.dispose();
    }
    gc.setTransform( oldTransform );
    oldTransform.dispose();
  }

  @Override
  public void fillPath( Path aPath, int aX, int aY, int aWidth, int aHeight ) {
    Pattern pattern = null;

    if( fillInfo != null ) {
      switch( fillInfo.kind() ) {
        case NONE:
          return;
        case SOLID:
          RGBA rgba = fillInfo.fillColor();
          gc.setBackground( colorManager().getColor( rgba.rgb ) );
          gc.setAlpha( rgba.alpha );
          break;
        case GRADIENT:
          IGradient grad = fillInfo.gradientFillInfo().createGradient( tsContext );
          if( grad != null ) {
            pattern = grad.pattern( gc, aWidth, aHeight );
            gc.setBackgroundPattern( pattern );
          }
          break;
        case IMAGE:
          TsImageFillInfo imgInfo = fillInfo.imageFillInfo();
          if( imgInfo.imageDescriptor() == TsImageDescriptor.NONE ) {
            unknownImage = imageManager().createUnknownImage( unknownImageSize );
            bkImage = unknownImage;
          }
          else {
            bkImage = imageManager().getImage( imgInfo.imageDescriptor() );
          }
          if( imgInfo.kind() == EImageFillKind.TILE ) {
            fillTileImage( bkImage, aX, aY, aWidth, aHeight );
          }
          if( unknownImage != null ) {
            unknownImage.dispose();
            unknownImage = null;
          }
          return;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fillInfo.kind() ); //$NON-NLS-1$
      }
    }
    Transform oldTransform = new Transform( gc.getDevice() );
    gc.getTransform( oldTransform );
    Transform tr = new Transform( gc.getDevice() );
    gc.getTransform( tr );
    tr.translate( aX, aY );
    gc.setTransform( tr );
    tr.dispose();
    gc.fillPath( aPath );
    if( pattern != null ) {
      pattern.dispose();
    }
    gc.setTransform( oldTransform );
    oldTransform.dispose();
  }

  @Override
  public void setBorderInfo( TsBorderInfo aBorderInfo ) {
    borderInfo = aBorderInfo;
  }

  @Override
  public void drawRectBorder( int aX, int aY, int aWidth, int aHeight ) {
    ITsRectangle r = new TsRectangle( aX, aY, aWidth, aHeight );
    TsGraphicsUtils.drawBorder( gc, borderInfo, r, colorManager() );
  }

  @Override
  public void setBackgroundRgba( RGBA aRgba ) {
    gc.setAlpha( aRgba.alpha );
    gc.setBackground( colorManager().getColor( aRgba.rgb ) );
  }

  @Override
  public void setBackgroundRgb( RGB aRgb ) {
    gc.setAlpha( 255 );
    gc.setBackground( colorManager().getColor( aRgb ) );
  }

  @Override
  public void setForegroundRgba( RGBA aRgba ) {
    gc.setAlpha( aRgba.alpha );
    gc.setForeground( colorManager().getColor( aRgba.rgb ) );
  }

  @Override
  public void setForegroundRgb( RGB aRgb ) {
    gc.setAlpha( 255 );
    gc.setForeground( colorManager().getColor( aRgb ) );
  }

}
