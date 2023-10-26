package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Параметры линейного градиента.
 * <p>
 *
 * @author vs
 */
public class LinearGradient
    extends AbstractGradient {

  private final LinearGradientInfo info;

  private final ITsGuiContext context;

  /**
   * Конструкторы.<br>
   *
   * @param aInfo LinearGradientInfo - параметры линейного градиента
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public LinearGradient( LinearGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aContext );
    info = aInfo;
    context = aContext;
  }

  @Override
  public ITsGuiContext tsContext() {
    return context;
  }

  // ------------------------------------------------------------------------------------
  // ISwtPattern
  //

  @Override
  public IGradientInfo patternInfo() {
    return info;
  }

  @Override
  public Pattern pattern( GC aGc, int aWidth, int aHeight ) {
    if( isVertical() ) {
      return createVerticalPattern( aGc, aWidth, aHeight );
    }
    float x1 = (float)(aWidth * info.startPoint().x() / 100.);
    float y1 = (float)(aHeight * info.startPoint().y() / 100.);
    float x2 = (float)(aWidth * info.endPoint().x() / 100.);
    float y2 = (float)(aHeight * info.endPoint().y() / 100.);

    Color c1 = colorManager().getColor( info.startRGBA().rgb );
    Color c2 = colorManager().getColor( info.endRGBA().rgb );
    int alpha1 = info.startRGBA().alpha;
    int alpha2 = info.endRGBA().alpha;

    return new Pattern( aGc.getDevice(), x1, y1, x2, y2, c1, alpha1, c2, alpha2 );
    // Pattern pat = new Pattern( aGc.getDevice(), x1, y1, x2, y2, c1, alpha1, c2, alpha2 );
    //
    // Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    // GC gc = new GC( img.getDevice() );
    // gc.setBackgroundPattern( pat );
    // gc.fillRectangle( 0, 0, aWidth, aHeight );
    //
    // pat.dispose();
    // gc.dispose();
    //
    // pat = new Pattern( aGc.getDevice(), img );
    // img.dispose();
    // return pat;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean isVertical() {
    return info.startPoint().x() == info.endPoint().x();
  }

  boolean isHorizontal() {
    return info.startPoint().y() == info.endPoint().y();
  }

  Pattern createVerticalPattern( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

    double dr = (double)(info.endRGBA().rgb.red - info.startRGBA().rgb.red) / aHeight;
    double dg = (double)(info.endRGBA().rgb.green - info.startRGBA().rgb.green) / aHeight;
    double db = (double)(info.endRGBA().rgb.blue - info.startRGBA().rgb.blue) / aHeight;
    double da = (double)(info.endRGBA().alpha - info.startRGBA().alpha) / aHeight;

    for( int i = 0; i < aWidth; i++ ) {
      int r = info.startRGBA().rgb.red;
      int g = info.startRGBA().rgb.green;
      int b = info.startRGBA().rgb.blue;
      int a = info.startRGBA().alpha;
      for( int j = 0; j < aHeight; j++ ) {
        int p = (int)(r + j * dr) << redShift | (int)(g + j * dg) << greenShift | (int)(b + j * db) << blueShift;
        imd.setPixel( i, j, p );
        imd.setAlpha( i, j, (int)(a + j * da) );
      }
    }

    img.dispose();
    img = new Image( aGc.getDevice(), imd );
    Pattern pat = new Pattern( aGc.getDevice(), img );
    img.dispose();
    return pat;
  }

}
