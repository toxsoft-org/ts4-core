package org.toxsoft.core.tsgui.ved.olds;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Стандартная реализация прямоугольного контура.
 * <p>
 * <b>Мотивация:</b><br>
 * Помощь в реализации компонент редактора, для которых в качестве контура (абриса) достаточно описывающего
 * прямоугольника.
 *
 * @author vs
 */
public class D2RectOutline
    implements IVedOutline {

  private final ID2Rectangle bounds;

  /**
   * Конструктор.
   *
   * @param aRect ID2Rectangle - прямоугольник
   */
  public D2RectOutline( ID2Rectangle aRect ) {
    bounds = new D2Rectangle( aRect );
  }

  /**
   * Конструктор.
   *
   * @param aX double - x координата левого верхнего угла
   * @param aY double - y координата левого верхнего угла
   * @param aWidth double - ширина прямоугольника
   * @param aHeight double - высота прямоугольника
   */
  public D2RectOutline( double aX, double aY, double aWidth, double aHeight ) {
    bounds = new D2Rectangle( aX, aY, aWidth, aHeight );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedOutline}
  //

  @Override
  public ID2Rectangle bounds() {
    return bounds;
  }

  @Override
  public ID2Point boundsCenter() {
    return new D2Point( bounds.x1() + bounds.width() / 2., bounds.y1() + bounds.height() / 2. );
  }

  @Override
  public boolean contains( double aX, double aY ) {
    if( aX < bounds.x1() || aY < bounds.y1() ) {
      return false;
    }
    if( aX > bounds.x2() || aY > bounds.y2() ) {
      return false;
    }
    return true;
  }

  @Override
  public ID2Point nearestPoint( double aX, double aY ) {
    double dLeft = Math.abs( bounds.x1() - aX );
    double dRight = Math.abs( bounds.x2() - aX );
    double nx = bounds.x1();
    if( dRight < dLeft ) {
      nx = bounds.x2();
    }

    double dTop = Math.abs( bounds.y1() - aY );
    double dBottom = Math.abs( bounds.y2() - aY );
    double ny = bounds.y1();
    if( dBottom < dTop ) {
      nx = bounds.y2();
    }

    double dHor = Math.min( dLeft, dRight );
    double dVer = Math.min( dTop, dBottom );
    if( aY >= bounds.y1() && aY <= bounds.y2() && aX >= bounds.x1() && aX <= bounds.x2() ) {
      if( dHor <= dVer ) {
        return new D2Point( nx, aY );
      }
      return new D2Point( aX, ny );
    }

    if( aY >= bounds.y1() && aY <= bounds.y2() ) {
      return new D2Point( nx, aY );
    }

    if( aX >= bounds.x1() && aX <= bounds.x2() ) {
      return new D2Point( aX, ny );
    }

    double minVertexD = distance( bounds.x1(), bounds.y1(), aX, aY );
    nx = bounds.x1();
    ny = bounds.y1();
    double d = distance( bounds.x2(), bounds.y1(), aX, aY );
    if( d < minVertexD ) {
      minVertexD = d;
      nx = bounds.x2();
      ny = bounds.y1();
    }
    d = distance( bounds.x2(), bounds.y2(), aX, aY );
    if( d < minVertexD ) {
      minVertexD = d;
      nx = bounds.x2();
      ny = bounds.y2();
    }
    d = distance( bounds.x1(), bounds.y2(), aX, aY );
    if( d < minVertexD ) {
      minVertexD = d;
      nx = bounds.x1();
      ny = bounds.y2();
    }
    return new D2Point( nx, ny );
  }

  @Override
  public double distanceTo( double aX, double aY ) {
    ID2Point p = nearestPoint( aX, aY );
    return distance( aX, aY, p.x(), p.y() );
  }

  @Override
  public ID2Point outlinePoint( double aPercent ) {
    double length = (aPercent * perimetr()) / 100.;
    double currLength = 0.0;
    if( length <= currLength + bounds.width() ) {
      return new D2Point( bounds.x1() + (length - currLength), bounds.y1() );
    }
    currLength = bounds.width();
    if( length <= currLength + bounds.height() ) {
      return new D2Point( bounds.x2(), bounds.y1() + (length - currLength) );
    }
    currLength = bounds.width() + bounds.height();
    if( length <= currLength + bounds.width() ) {
      return new D2Point( bounds.x2() - (length - currLength), bounds.y2() );
    }
    currLength = 2.0 * bounds.width() + bounds.height();
    return new D2Point( bounds.x1(), bounds.y2() - (length - currLength) );
  }

  @Override
  public boolean hasPath() {
    return false;
  }

  @Override
  public Path outlinePath() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает x координату левого верхнего угла
   *
   * @return double - x координата левого верхнего угла
   */
  public double x() {
    return bounds.x1();
  }

  /**
   * Возвращает y координату левого верхнего угла
   *
   * @return double - y координата левого верхнего угла
   */
  public double y() {
    return bounds.y1();
  }

  /**
   * Возвращает ширину прямоугольника
   *
   * @return double - ширина прямоугольника
   */
  public double width() {
    return bounds.width();
  }

  /**
   * Возвращает высоту прямоугольника
   *
   * @return double - высота прямоугольника
   */
  public double height() {
    return bounds.height();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private double perimetr() {
    return 2.0 * bounds.width() + 2.0 * bounds.height();
  }

  private static double distance( double aX1, double aY1, double aX2, double aY2 ) {
    double dx = aX1 - aX2;
    double dy = aY1 - aY2;
    return Math.sqrt( dx * dx + dy * dy );
  }

}
