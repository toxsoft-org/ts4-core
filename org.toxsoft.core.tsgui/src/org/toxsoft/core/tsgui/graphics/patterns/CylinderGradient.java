package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Узор для заливки имитирующей цилиндр.
 * <p>
 *
 * @author vs
 */
public class CylinderGradient
    extends AbstractFractionalGradient {

  // private final CylinderGradientInfo info;

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
  public CylinderGradient( IList<Pair<Double, RGBA>> aFractions, double aDegrees, ITsGuiContext aContext ) {
    super( aFractions, aContext );
    angleDegrees = aDegrees;
    angleRadians = Math.toRadians( angleDegrees );
  }

  @Override
  Image createImage( GC aGc, int aWidth, int aHeight ) {
    if( angleDegrees == 0 ) {
      return createNonRotatedImage( aGc, aWidth, aHeight );
    }
    if( angleDegrees == 90 ) {
      return createRotated90Image( aGc, aWidth, aHeight );
    }
    ID2Rectangle d2r = new D2Rectangle( 0, 0, aWidth, aHeight );

    d2r = TsGeometryUtils.bounds( TsGeometryUtils.rotateRect( d2r, angleRadians ) );

    double dx = aWidth / 2.;
    double dy = aHeight / 2.;

    int width = (int)Math.floor( d2r.width() ) + 4;
    int height = (int)Math.floor( d2r.height() ) + 4;

    Image img = createNonRotatedImage( aGc, width, height );

    Image newImg = new Image( aGc.getDevice(), aWidth, aHeight );
    GC gc = new GC( newImg );

    Transform tr = new Transform( getDisplay() );
    tr.translate( (float)dx, (float)dy );
    tr.rotate( (float)angleDegrees );
    tr.translate( -(float)dx, -(float)dy );
    gc.setTransform( tr );

    gc.drawImage( img, (int)(d2r.x1()), (int)d2r.y1() );

    img.dispose();
    tr.dispose();
    gc.dispose();
    return newImg;
  }

  @Override
  IGradientFraction createFraction( Pair<Double, RGBA> aStart, Pair<Double, RGBA> aEnd ) {
    return new CylinderGradientFraction( aStart.left().doubleValue(), aStart.right(), aEnd.left().doubleValue(),
        aEnd.right() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private Image createNonRotatedImage( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

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

  private Image createRotated90Image( GC aGc, int aWidth, int aHeight ) {
    Image img = new Image( aGc.getDevice(), aWidth, aHeight );
    ImageData imd = img.getImageData();

    int redShift = Math.abs( imd.palette.redShift );
    int greenShift = Math.abs( imd.palette.greenShift );
    int blueShift = Math.abs( imd.palette.blueShift );

    double radius = aHeight / 2.;

    for( int j = 0; j < aHeight; j++ ) {
      double prevAngle = 0;
      for( int i = 0; i < aWidth; i++ ) {
        double dx = j - aHeight / 2;
        double dy = i;
        double distance = Math.sqrt( dx * dx + dy * dy );
        if( distance == 0 ) {
          distance = 1;
        }
        // double angle = -Math.abs( Math.toDegrees( Math.acos( dx / distance ) ) );

        // double radius = aWidth / 2.;
        double angle = -Math.abs( Math.toDegrees( Math.acos( j / radius - 1 ) ) );

        for( IGradientFraction gf : fractions() ) {
          if( gf.isMine( angle ) ) {
            prevAngle = angle;
            RGBA rgba = gf.calcRgb( prevAngle );
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
