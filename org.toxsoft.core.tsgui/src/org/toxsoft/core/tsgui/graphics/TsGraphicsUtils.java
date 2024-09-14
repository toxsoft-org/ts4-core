package org.toxsoft.core.tsgui.graphics;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Miscallenous graphics utility methods.
 *
 * @author hazard157
 */
public class TsGraphicsUtils {

  /**
   * Creates SWT {@link Rectangle} from tslib {@link ITsRectangle}.
   *
   * @param aRect {@link ITsRectangle} - tslib rectangle
   * @return {@link Rectangle} - created instance of SWT rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static Rectangle rectFromTs( ITsRectangle aRect ) {
    TsNullArgumentRtException.checkNull( aRect );
    return new Rectangle( aRect.a().x(), aRect.a().y(), aRect.width(), aRect.height() );
  }

  /**
   * Creates TS rectangle from the SWT {@link Rectangle}.
   *
   * @param aRect {@link Rectangle} - SWT rectangle
   * @return {@link TsRectangleEdit} - editable TS rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsRectangleEdit tsFromRect( Rectangle aRect ) {
    TsNullArgumentRtException.checkNull( aRect );
    return new TsRectangleEdit( aRect.x, aRect.y, aRect.width, aRect.height );
  }

  /**
   * Sets the rectangle coordinates from TS to SWT.
   *
   * @param aSource {@link ITsRectangle} - TS rectangle
   * @param aDest {@link Rectangle} - SWT rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void setRect( ITsRectangle aSource, Rectangle aDest ) {
    TsNullArgumentRtException.checkNulls( aSource, aDest );
    aDest.x = aSource.x1();
    aDest.y = aSource.y1();
    aDest.width = aSource.width();
    aDest.height = aSource.height();
  }

  /**
   * Sets the rectangle coordinates from SWT to TS.
   *
   * @param aSource {@link ITsRectangle} - SWT rectangle
   * @param aDest {@link TsRectangleEdit} - TS rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void setRect( Rectangle aSource, TsRectangleEdit aDest ) {
    TsNullArgumentRtException.checkNulls( aSource, aDest );
    aDest.setRect( aSource.x, aSource.y, aSource.width, aSource.height );
  }

  /**
   * Converts {@link ID2Conversion} to SWT {@link Transform}.
   *
   * @param aD2Conv {@link ID2Conversion} - conversion
   * @param aGc {@link GC} - graphics context
   * @return {@link Transform} - transformation matrix for graphics context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static Transform conv2transform( ID2Conversion aD2Conv, GC aGc ) {
    TsNullArgumentRtException.checkNulls( aD2Conv, aGc );
    Transform t = new Transform( aGc.getDevice() );
    float zf = (float)aD2Conv.zoomFactor();
    t.translate( (float)aD2Conv.origin().x(), (float)aD2Conv.origin().y() );
    t.scale( zf, zf );

    if( aD2Conv.rotation() != ID2Angle.ZERO ) {
      t.rotate( (float)aD2Conv.rotation().degrees() );
    }
    return t;
  }

  /**
   * Draws the line according to its attributes.
   *
   * @param aGc GC - the canvas
   * @param aColor {@link Color} - line color
   * @param aLineInfo {@link TsLineInfo} - the line attributes
   * @param aP1 {@link ITsPoint} - line starting point
   * @param aP2 {@link ITsPoint} - lene ending point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void drawLine( GC aGc, Color aColor, TsLineInfo aLineInfo, ITsPoint aP1, ITsPoint aP2 ) {
    TsNullArgumentRtException.checkNulls( aGc, aColor, aLineInfo, aP1, aP2 );
    aGc.setForeground( aColor );
    aLineInfo.setToGc( aGc );
    aGc.drawLine( aP1.x(), aP1.y(), aP2.x(), aP2.y() );
  }

  /**
   * Draws the border (rectangle surrounding of something).
   *
   * @param aGc {@link GC} - the graphics context to draw on
   * @param aBorderInfo {@link TsBorderInfo} - the border (frame) drawing info
   * @param aRect ITsRectangle - frame coordinates
   * @param aColorManager ITsColorManager - the color manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void drawBorder( GC aGc, TsBorderInfo aBorderInfo, ITsRectangle aRect, ITsColorManager aColorManager ) {
    TsNullArgumentRtException.checkNulls( aGc, aBorderInfo, aRect, aColorManager );
    switch( aBorderInfo.kind() ) {
      case NONE: {
        // nop
        break;
      }
      case SINGLE: {
        drawSingleBoreder( aGc, aBorderInfo, aRect, aColorManager );
        break;
      }
      case DOUBLE: {
        drawDoubleBoreder( aGc, aBorderInfo, aRect, aColorManager );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aBorderInfo.kind().id() );
    }
  }

  private static void drawSingleBoreder( GC aGc, TsBorderInfo aBinf, ITsRectangle aRect, ITsColorManager aColorMan ) {
    // prepare
    aBinf.lineInfo().setToGc( aGc );
    int thick = aBinf.lineInfo().width();
    RGBA rgbaLeft = aBinf.leftTopRGBA();
    RGBA rgbaRight = aBinf.rightBottomRGBA();
    int x1 = aRect.x1();
    int x2 = aRect.x1() + aRect.width();
    int y1 = aRect.y1();
    int y2 = aRect.y1() + aRect.height();
    aGc.setLineWidth( thick );
    // draw
    if( aBinf.shouldPaintAll() ) { // нужно рисовать все стороны
      if( aBinf.leftTopRGBA().equals( aBinf.rightBottomRGBA() ) ) { // одноцветный прямоугольник
        aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
        aGc.drawRectangle( x1 + thick / 2, y1 + thick / 2, aRect.width() - thick, aRect.height() - thick );
      }
      else { // двухцветная граница
        aGc.setLineWidth( 1 );
        aGc.setAlpha( rgbaLeft.alpha );
        aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
        for( int i = 0; i < thick; i++ ) {
          aGc.drawLine( x1 + i, y1 + i, x2 - i, y1 + i );
        }
        for( int i = 0; i < thick; i++ ) {
          aGc.drawLine( x1 + i, y1 + i, x1 + i, y2 - i );
        }

        aGc.setAlpha( rgbaRight.alpha );
        aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
        for( int i = 0; i < thick; i++ ) {
          aGc.drawLine( x1 + i, y2 - i, x2 - i, y2 - i );
        }
        for( int i = 0; i < thick; i++ ) {
          aGc.drawLine( x2 - i, y1 + i, x2 - i, y2 - i );
        }
      }
      return;
    }
    aGc.setLineWidth( 1 );
    if( aBinf.shouldPaintLeft() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      for( int i = 0; i < thick; i++ ) {
        aGc.drawLine( x1 + i, y1 + i, x1 + i, y2 - i );
      }
    }
    if( aBinf.shouldPaintTop() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      for( int i = 0; i < thick; i++ ) {
        aGc.drawLine( x1 + i, y1 + i, x2 - i, y1 + i );
      }
    }
    if( aBinf.shouldPaintRight() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      for( int i = 0; i < thick; i++ ) {
        aGc.drawLine( x2 - i, y1 + i, x2 - i, y2 - i );
      }
    }
    if( aBinf.shouldPaintBottom() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      for( int i = 0; i < thick; i++ ) {
        aGc.drawLine( x1 + i, y2 - i, x2 - i, y2 - i );
      }
    }
  }

  private static void drawDoubleBoreder( GC aGc, TsBorderInfo aBinf, ITsRectangle aRect, ITsColorManager aColorMan ) {
    // prepare
    aBinf.lineInfo().setToGc( aGc );
    int thick = aBinf.lineInfo().width();
    RGBA rgbaLeft = aBinf.leftTopRGBA();
    RGBA rgbaRight = aBinf.rightBottomRGBA();
    int x1 = aRect.x1();
    int x2 = aRect.x1() + aRect.width();
    int y1 = aRect.y1();
    int y2 = aRect.y1() + aRect.height();
    aGc.setLineWidth( thick );
    // draw
    if( aBinf.shouldPaintAll() ) { // нужно рисовать все стороны
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      aGc.drawRectangle( x1 + thick / 2, y1 + thick / 2, aRect.width() - 2 * thick, aRect.height() - 2 * thick );

      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      aGc.drawRectangle( x1 + thick + thick / 2, y1 + thick + thick / 2, aRect.width() - 2 * thick,
          aRect.height() - 2 * thick );
      return;
    }
    if( aBinf.shouldPaintLeft() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      aGc.drawLine( x1 + thick / 2, y1, x1 + thick / 2, y2 - thick );
    }
    if( aBinf.shouldPaintTop() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      aGc.drawLine( x1 + thick / 2, y1 + thick / 2, x2 - thick, y1 + thick / 2 );
    }
    if( aBinf.shouldPaintRight() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      aGc.drawLine( x2 - thick - thick / 2, y1, x2 - thick - thick / 2, y2 - thick );
    }
    if( aBinf.shouldPaintBottom() ) {
      aGc.setAlpha( rgbaLeft.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaLeft.rgb ) );
      aGc.drawLine( x1 + thick / 2, y2 - thick - thick / 2, x2 - thick, y2 - thick - thick / 2 );
    }

    if( aBinf.shouldPaintLeft() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      aGc.drawLine( x1 + thick / 2 + thick, y1 + thick, x1 + thick / 2 + thick, y2 );
    }
    if( aBinf.shouldPaintTop() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      aGc.drawLine( x1 + thick / 2 + thick, y1 + thick + thick / 2, x2, y1 + thick + thick / 2 );
    }
    if( aBinf.shouldPaintRight() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      aGc.drawLine( x2 - thick / 2, y1, x2 - thick / 2, y2 - thick );
    }
    if( aBinf.shouldPaintBottom() ) {
      aGc.setAlpha( rgbaRight.alpha );
      aGc.setForeground( aColorMan.getColor( rgbaRight.rgb ) );
      aGc.drawLine( x1 + thick / 2 + thick, y2 - thick / 2, x2, y2 - thick / 2 );
    }
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsGraphicsUtils() {
    // nop
  }

}
