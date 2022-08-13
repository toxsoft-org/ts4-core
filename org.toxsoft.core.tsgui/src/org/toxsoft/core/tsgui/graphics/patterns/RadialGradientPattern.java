package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * "Узор" радиального градиента.
 * <p>
 *
 * @author vs
 */
public class RadialGradientPattern
    extends AbstractFractionalGradientPattern {

  /**
   * Смещение центра по оси x в процентах от ширины
   */
  private final double centerDx;

  /**
   * Смещение центра по оси y в процентах
   */
  private final double centerDy;

  /**
   * Конструктор.<br>
   *
   * @param aCenterDx double - cмещение центра по оси x в процентах от ширины
   * @param aCenterDy double - Смещение центра по оси y в процентах от высоты
   * @param aFractions IList&lt;Pair&lt;Double, RGBA>> - список фракций
   * @param aContext ITsGuiContext - соотвествующий контекст
   */
  public RadialGradientPattern( double aCenterDx, double aCenterDy, IList<Pair<Double, RGBA>> aFractions,
      ITsGuiContext aContext ) {
    super( aFractions, aContext );
    centerDx = aCenterDx;
    centerDy = aCenterDy;
  }

  public RadialGradientPattern( RadialGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aInfo.fractions, aContext );
    centerDx = aInfo.centerX;
    centerDy = aInfo.centerY;
  }

  @Override
  Image createImage( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    // double dx = aWidth / 4.;
    // double dy = aHeight / 3.;
    double dx = aWidth - (centerDx * 2 * aWidth) / 100.;
    double dy = aHeight - (centerDy * 2 * aHeight) / 100.;

    double radius = Math.sqrt( (aWidth / 2. + dx) * (aWidth / 2. + dx) + (aHeight / 2. + dy) * (aHeight / 2. + dy) );

    for( int j = 0; j < aHeight; j++ ) {
      for( int i = 0; i < aWidth; i++ ) {
        double x = i - aWidth / 2. + dx;
        double y = j - aHeight / 2. + dy;
        double value = Math.sqrt( x * x + y * y ); // расстояние от центра

        value = Math.abs( value / radius );

        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( value ) ) {
            RGBA rgba = gf.calcRgb( value );
            int p = rgba.rgb.red << 8 | rgba.rgb.green << 16 | rgba.rgb.blue << 24;
            imd.setPixel( i, j, p );
            imd.setAlpha( i, j, rgba.alpha );
            break;
          }
        }
      }
    }

    img.dispose();
    // if( img.isDisposed() ) {
    img = new Image( aGc.getDevice(), imd );
    // }
    return img;
  }

  @Override
  IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd ) {
    return new RadialGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  @Override
  public ISwtPatternInfo patternInfo() {
    // TODO Auto-generated method stub
    return null;
  }

}
