package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The conincal gradient pattern.
 *
 * @author vs
 */
public class ConicalGradient
    extends AbstractFractionalGradient {

  /**
   * Constructor.
   *
   * @param aFractions IList&lt;Pair&lt;Double, RGBA&gt;&gt; - list of fractions
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public ConicalGradient( IList<Pair<Double, RGBA>> aFractions, ITsGuiContext aContext ) {
    super( aFractions, aContext );
  }

  /**
   * Constructor.
   *
   * @param aInfo {@link ConicalGradientInfo} - gradient parameters
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public ConicalGradient( ConicalGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aInfo.fractions, aContext );
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

    double cx = aWidth / 2.;
    double cy = aHeight / 2.;

    for( int j = 0; j < aHeight; j++ ) {
      for( int i = 0; i < aWidth; i++ ) {
        double x = i - cx;
        double y = j - cy;
        // double alpha = calcAlpha( x, y );
        double alpha = calcAlpha( x, -y );

        double value = (alpha * 100.) / (2 * Math.PI);
        // if( j == aHeight - 2 && i == aWidth - 2 ) {
        // System.out.println( "alpha = " + alpha + "; value = " + value );
        // }
        boolean fractionFound = false;
        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( value ) ) {
            RGBA rgba = gf.calcRgb( value );
            int p = rgba.rgb.red << redShift | rgba.rgb.green << greenShift | rgba.rgb.blue << blueShift;
            imd.setPixel( i, j, p );
            imd.setAlpha( i, j, rgba.alpha );
            fractionFound = true;
            // if( alpha == Math.PI / 2. ) {
            // System.out.println( "value = " + value + "; rgba = " + rgba );
            // alpha = calcAlpha( x, y );
            // }
            if( i == 290 ) {
              // System.out.println( "value = " + value + "; x = " + i + "; alpha = " + alpha );
              alpha = calcAlpha( x, y );
            }
            // if( alpha == Math.PI / 2. && value == 25 && rgba.rgb.red == 64 ) {
            // if( alpha == Math.PI / 2. && rgba.rgb.red == 64 ) {
            // rgba = gf.calcRgb( value );
            // }
            break;
          }
        }
        if( !fractionFound ) {
          // System.out.println( "No fraction for value = " + value + "; alpha = " + alpha );
        }
      }
    }

    img.dispose();
    img = new Image( aGc.getDevice(), imd );
    return img;
  }

  @Override
  IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd ) {
    return new ConicalGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //
  double calcAlpha( double x, double y ) {
    double dx = Math.abs( x );
    double dy = Math.abs( y );
    double length = Math.sqrt( dx * dx + dy * dy );

    double alpha = Math.acos( x / length );
    if( length == 0 ) {
      return 0;
    }
    if( x >= 0 ) {
      if( y <= 0 ) { // 4 квадрант
        alpha = 4 * Math.PI / 2 - alpha;
      }
      else { // 1 квадрант do nothing
        // alpha = 0;
      }
    }
    else {
      if( y <= 0 ) { // 3 квадрант
        alpha = 2 * Math.PI - alpha;
      }
      else { // 2 квадрант do nothing
        // alpha = alpha + Math.PI / 2;
      }
    }
    return alpha;
  }

}
