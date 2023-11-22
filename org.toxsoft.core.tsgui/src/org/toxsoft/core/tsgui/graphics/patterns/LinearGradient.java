package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The radial gradient pattern.
 *
 * @author vs
 */
public class LinearGradient
    extends AbstractFractionalGradient {

  private final double angleDegrees;

  private final double angleRadians;

  /**
   * Constructor.
   *
   * @param aFractions IList&lt;Pair&lt;Double, RGBA&gt;&gt; - list of fractions
   * @param aDegrees double - rotation angle in degrees
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public LinearGradient( IList<Pair<Double, RGBA>> aFractions, double aDegrees, ITsGuiContext aContext ) {
    super( aFractions, aContext );
    angleDegrees = aDegrees;
    angleRadians = Math.toRadians( angleDegrees );
  }

  /**
   * Constructor.
   *
   * @param aInfo {@link RadialGradientInfo} - gradient parameters
   * @param aDegrees double - rotation angle in degrees
   * @param aContext ITsGuiContext - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException - number of fractions is less than 2 2
   */
  public LinearGradient( LinearGradientInfo aInfo, double aDegrees, ITsGuiContext aContext ) {
    super( aInfo.fractions, aContext );
    angleDegrees = aDegrees;
    angleRadians = Math.toRadians( angleDegrees );
  }

  // ------------------------------------------------------------------------------------
  // AbstractFractionalGradient
  //

  @Override
  public Image createImage( GC aGc, int aWidth, int aHeight ) {
    if( angleRadians == 0 ) {
      return createNonRotatedImage( aGc, aWidth, aHeight );
    }
    if( angleDegrees == 90 ) {
      return createRotated90Image( aGc, aWidth, aHeight );
    }

    ID2Rectangle d2r = new D2Rectangle( 0, 0, aWidth, aHeight );

    d2r = TsGeometryUtils.bounds( TsGeometryUtils.rotateRect( d2r, angleRadians ) );

    double dx = aWidth / 2.;
    double dy = aHeight / 2.;

    int width = (int)Math.floor( d2r.width() );
    int height = (int)Math.floor( d2r.height() );

    Image img = createNonRotatedImage( aGc, width, height );

    Image newImg = new Image( aGc.getDevice(), aWidth, aHeight );
    GC gc = new GC( newImg );

    Transform tr = new Transform( getDisplay() );
    tr.translate( (float)dx, (float)dy );
    tr.rotate( (float)angleDegrees );
    tr.translate( -(float)dx, -(float)dy );
    gc.setTransform( tr );

    gc.drawImage( img, (int)Math.floor( d2r.x1() + 1 ), (int)Math.floor( d2r.y1() + 2 ) );

    img.dispose();
    tr.dispose();
    gc.dispose();
    return newImg;
  }

  @Override
  IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd ) {
    return new LinearGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Image createNonRotatedImage( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

    for( int j = 0; j < aHeight; j++ ) {
      for( int i = 0; i < aWidth; i++ ) {
        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( i / (double)aWidth ) ) {
            RGBA rgba = gf.calcRgb( i / (double)aWidth );
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

  Image createRotated90Image( GC aGc, int aWidth, int aHeight ) {
    int width = aWidth;
    int height = aHeight;
    Image img = new Image( aGc.getDevice(), width, height );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

    for( int i = 0; i < width; i++ ) {
      for( int j = 0; j < height; j++ ) {
        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( j / (double)aHeight ) ) {
            RGBA rgba = gf.calcRgb( j / (double)aHeight );
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

}
