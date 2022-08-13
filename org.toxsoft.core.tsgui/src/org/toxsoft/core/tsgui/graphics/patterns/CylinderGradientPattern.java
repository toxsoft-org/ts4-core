package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Узор для заливки имитирующей цилиндр.
 * <p>
 *
 * @author vs
 */
public class CylinderGradientPattern
    extends AbstractFractionalGradientPattern {

  private final CylinderGradientInfo info;

  /**
   * Конструктор.<br>
   *
   * @param aInfo CylinderGradientInfo - параметры цилиндрической заливки
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public CylinderGradientPattern( CylinderGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aInfo.fractions(), aContext );
    info = aInfo;
  }

  @Override
  Image createImage( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    for( int j = 0; j < aHeight; j++ ) {
      double prevAngle = 0;
      for( int i = 0; i < aWidth; i++ ) {
        double dx = i - aWidth / 2;
        double dy = j;
        double distance = Math.sqrt( dx * dx + dy * dy );
        if( distance == 0 ) {
          distance = 1;
        }
        double angle = -Math.abs( Math.toDegrees( Math.acos( dx / distance ) ) );

        double radius = aWidth / 2.;
        angle = -Math.abs( Math.toDegrees( Math.acos( i / radius - 1 ) ) );

        // if( j == 0 ) {
        // System.out.println( "angle = " + angle + "; cos = " + (i / radius - 1) );
        // }

        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( angle ) ) {
            // if( ((int)angle) % 5 == 0 ) {
            prevAngle = angle;
            // }
            RGBA rgba = gf.calcRgb( prevAngle );
            // RGBA rgba = gf.calcRgb( (int)angle );
            // RGBA rgba = gf.calcRgb( (i / radius - 1) * 100 );
            int p = rgba.rgb.red << 8 | rgba.rgb.green << 16 | rgba.rgb.blue << 24;
            imd.setPixel( i, j, p );
            imd.setAlpha( i, j, rgba.alpha );
            break;
          }
        }
      }
    }

    img.dispose();
    img = new Image( aGc.getDevice(), imd );
    return img;
  }

  @Override
  IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd ) {
    return new CylinderGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  // ------------------------------------------------------------------------------------
  // ISwtPattern
  //

  @Override
  public ISwtPatternInfo patternInfo() {
    return info;
  }

}
