package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The radial gradient pattern.
 *
 * @author vs
 */
public class RadialGradient
    extends AbstractFractionalGradient {

  /**
   * Center offset on the x-axis as a percentage of the width.
   */
  private final double centerDx;

  /**
   * Center offset on the y-axis as a percentage of the height.
   */
  private final double centerDy;

  /**
   * Constructor.
   *
   * @param aCenterDx double - center offset on the x-axis as a percentage of the width.
   * @param aCenterDy double - center offset on the y-axis as a percentage of the height
   * @param aFractions IList&lt;Pair&lt;Double, RGBA&gt;&gt; - list of fractions
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public RadialGradient( double aCenterDx, double aCenterDy, IList<Pair<Double, RGBA>> aFractions,
      ITsGuiContext aContext ) {
    super( aFractions, aContext );
    centerDx = aCenterDx;
    centerDy = aCenterDy;
  }

  /**
   * Constructor.
   *
   * @param aInfo {@link RadialGradientInfo} - gradient parameters
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public RadialGradient( RadialGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aInfo.fractions, aContext );
    centerDx = aInfo.centerX;
    centerDy = aInfo.centerY;
  }

  // ------------------------------------------------------------------------------------
  // AbstractFractionalGradient
  //

  @Override
  Image createImage( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

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
            // int p = rgba.rgb.red << 8 | rgba.rgb.green << 16 | rgba.rgb.blue << 24;
            int p = rgba.rgb.red << redShift | rgba.rgb.green << greenShift | rgba.rgb.blue << blueShift;
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
    return new RadialGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  @Override
  public IGradientInfo patternInfo() {
    // TODO реализовать RadialGradient.patternInfo()
    throw new TsUnderDevelopmentRtException( "RadialGradient.patternInfo()" );
  }

}
