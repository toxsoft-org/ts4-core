package org.toxsoft.core.tsgui.graphics;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Miscallenous graphics utility methods.
 *
 * @author hazard157
 */
public class TsGraphicsUtils {

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
    if( aD2Conv.rotation() != ID2Rotation.NONE ) {
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
   * Рисует рамку (границу).
   *
   * @param aGc GC - графичский контекст
   * @param aBorderInfo TsBorderInfo - парамтры рамки
   * @param aRect ITsRectangle - прямоугольник описывающий рамку
   * @param aColorManager ITsColorManager - менеджер цветов
   */
  public static void drawBorder( GC aGc, TsBorderInfo aBorderInfo, ITsRectangle aRect, ITsColorManager aColorManager ) {
    aBorderInfo.lineInfo().setToGc( aGc );
    int thick = aBorderInfo.lineInfo().width();
    RGBA rgbaLeft = aBorderInfo.leftTopRGBA();
    RGBA rgbaRight = aBorderInfo.rightBottomRGBA();

    int x1 = aRect.x1();
    int x2 = aRect.x1() + aRect.width();
    int y1 = aRect.y1();
    int y2 = aRect.y1() + aRect.height();

    if( aBorderInfo.isSingle() ) { // одинарная граница
      if( aBorderInfo.shouldPaintAll() ) { // нужно рисовать все стороны
        if( aBorderInfo.leftTopRGBA().equals( aBorderInfo.rightBottomRGBA() ) ) { // одноцветный прямоугольник
          aGc.drawRectangle( x1 + thick / 2, y1 + thick / 2, aRect.width() - thick, aRect.height() - thick );
        }
        else { // двухцветная граница
          aGc.setLineWidth( 1 );
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y1 + i, x2 - i, y1 + i );
          }
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y1 + i, x1 + i, y2 - i );
          }

          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y2 - i, x2 - i, y2 - i );
          }
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x2 - i, y1 + i, x2 - i, y2 - i );
          }
        }
      }
      else { // рисуем только отмеченные стороны
        aGc.setLineWidth( 1 );
        if( aBorderInfo.shouldPaintLeft() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y1 + i, x1 + i, y2 - i );
          }
        }
        if( aBorderInfo.shouldPaintTop() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y1 + i, x2 - i, y1 + i );
          }
        }
        if( aBorderInfo.shouldPaintRight() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x2 - i, y1 + i, x2 - i, y2 - i );
          }
        }
        if( aBorderInfo.shouldPaintBottom() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          for( int i = 0; i < thick; i++ ) {
            aGc.drawLine( x1 + i, y2 - i, x2 - i, y2 - i );
          }
        }
      }
    }
    else { // двойная граница
      aGc.setLineWidth( thick );
      if( aBorderInfo.shouldPaintAll() ) { // нужно рисовать все стороны
        aGc.setAlpha( rgbaLeft.alpha );
        aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
        aGc.drawRectangle( x1 + thick / 2, y1 + thick / 2, aRect.width() - 2 * thick, aRect.height() - 2 * thick );

        aGc.setAlpha( rgbaRight.alpha );
        aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
        aGc.drawRectangle( x1 + thick + thick / 2, y1 + thick + thick / 2, aRect.width() - 2 * thick,
            aRect.height() - 2 * thick );
      }
      else { // рисуем только отмеченные стороны
        if( aBorderInfo.shouldPaintLeft() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          aGc.drawLine( x1 + thick / 2, y1, x1 + thick / 2, y2 - thick );
        }
        if( aBorderInfo.shouldPaintTop() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          aGc.drawLine( x1 + thick / 2, y1 + thick / 2, x2 - thick, y1 + thick / 2 );
        }
        if( aBorderInfo.shouldPaintRight() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          aGc.drawLine( x2 - thick - thick / 2, y1, x2 - thick - thick / 2, y2 - thick );
        }
        if( aBorderInfo.shouldPaintBottom() ) {
          aGc.setAlpha( rgbaLeft.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaLeft.rgb ) );
          aGc.drawLine( x1 + thick / 2, y2 - thick - thick / 2, x2 - thick, y2 - thick - thick / 2 );
        }

        if( aBorderInfo.shouldPaintLeft() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          aGc.drawLine( x1 + thick / 2 + thick, y1 + thick, x1 + thick / 2 + thick, y2 );
        }
        if( aBorderInfo.shouldPaintTop() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          aGc.drawLine( x1 + thick / 2 + thick, y1 + thick + thick / 2, x2, y1 + thick + thick / 2 );
        }
        if( aBorderInfo.shouldPaintRight() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          aGc.drawLine( x2 - thick / 2, y1, x2 - thick / 2, y2 - thick );
        }
        if( aBorderInfo.shouldPaintBottom() ) {
          aGc.setAlpha( rgbaRight.alpha );
          aGc.setForeground( aColorManager.getColor( rgbaRight.rgb ) );
          aGc.drawLine( x1 + thick / 2 + thick, y2 - thick / 2, x2, y2 - thick / 2 );
        }
      }
    }
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsGraphicsUtils() {
    // nop
  }

}