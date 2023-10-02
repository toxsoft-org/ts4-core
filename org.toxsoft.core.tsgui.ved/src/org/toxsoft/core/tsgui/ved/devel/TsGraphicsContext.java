package org.toxsoft.core.tsgui.ved.devel;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

public class TsGraphicsContext
    implements ITsGraphicsContext {

  private final GC gc;

  private final ITsGuiContext tsContext;

  private final ITsRectangle drawingArea;

  private TsLineInfo lineInfo = null;

  private TsFillInfo fillInfo = null;

  private TsBorderInfo borderInfo = TsBorderInfo.NONE;

  private TsImage bkImage = null;

  private TsImage unknownImage = null;

  private int unknownImageSize = 32;

  public TsGraphicsContext( PaintEvent aEvent, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    gc = aEvent.gc;
    drawingArea = new TsRectangle( aEvent.x, aEvent.y, aEvent.width, aEvent.height );
    unknownImage = imageManager().createUnknownImage( unknownImageSize );
  }

  public TsGraphicsContext( GC aGc, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    gc = aGc;
    drawingArea = new TsRectangle( 0, 0, 1, 1 );
    unknownImage = imageManager().createUnknownImage( unknownImageSize );
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
  public void drawRect( int aX, int aY, int aWidth, int aHeight ) {
    if( lineInfo != null ) {
      lineInfo.setToGc( gc );
    }
    gc.drawRectangle( aX, aY, aWidth, aHeight );
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
            bkImage = unknownImage;
          }
          else {
            bkImage = imageManager().getImage( imgInfo.imageDescriptor() );
          }
          if( imgInfo.kind() == EImageFillKind.TILE ) {
            fillTileImage( bkImage, aX, aY, aWidth, aHeight );
          }
          return;
        // throw new TsUnderDevelopmentRtException();
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
    // gc.fillRectangle( aX, aY, aWidth, aHeight );
    gc.fillRectangle( 0, 0, aWidth, aHeight );
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
  public void setForegroundRgba( RGBA aRgba ) {
    gc.setAlpha( aRgba.alpha );
    gc.setForeground( colorManager().getColor( aRgba.rgb ) );
  }

  @Override
  public void setForegroundRgb( RGB aRgb ) {
    gc.setAlpha( 255 );
    gc.setForeground( colorManager().getColor( aRgb ) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void fillTileImage( TsImage aImage, int aX, int aY, int aWidth, int aHeight ) {
    gc.setClipping( new Rectangle( aX, aY, aWidth, aHeight ) );

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

    gc.setClipping( (Rectangle)null );
  }

}
