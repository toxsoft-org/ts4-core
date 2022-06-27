package org.toxsoft.core.tsgui.ved.incub.geom;

import java.awt.geom.*;

/**
 * Геометрическое понятие - "Отрезок линии".
 * <p>
 *
 * @author vs
 */
public class D2LineSegment {

  private double x1;
  private double y1;
  private double x2;
  private double y2;

  private double a;
  private double b;
  private double c;

  private double angle = 0;

  /**
   * Конструктор.<br>
   * Создает отрезок линии по двум точкам.
   *
   * @param aX1 double - x координата первой точки
   * @param aY1 double - y координата первой точки
   * @param aX2 double - x координата второй точки
   * @param aY2 double - y координата второй точки
   */
  public D2LineSegment( double aX1, double aY1, double aX2, double aY2 ) {
    x1 = aX1;
    y1 = aY1;
    x2 = aX2;
    y2 = aY2;

    a = y1 - y2;
    b = x2 - x1;
    c = x1 * y2 - x2 * y1;

    angle = Math.atan( -a / b );

    /**
     * ax + by + c = 0; -уравнение прямой<br>
     * k = -a/b<br>
     * x = (-by-c)/a y = kx+b by = -ax-c y = -ax/b - c/b k = -a/b
     */

  }

  /**
   * Конструктор копирования.
   *
   * @param aSource D2LineSegment - копируемый отрезок линии
   */
  public D2LineSegment( D2LineSegment aSource ) {
    x1 = aSource.x1;
    y1 = aSource.y1;
    x2 = aSource.x2;
    y2 = aSource.y2;

    a = y1 - y2;
    b = x2 - x1;
    c = x1 * y2 - x2 * y1;

    angle = Math.atan( -a / b );
  }

  /**
   * Конструктор по двум точкам.
   * <p>
   *
   * @param p1 ID2Point - первая точка отрезка
   * @param p2 ID2Point - вторая точка отрезка
   */
  public D2LineSegment( ID2Point p1, ID2Point p2 ) {
    x1 = p1.x();
    y1 = p1.y();
    x2 = p2.x();
    y2 = p2.y();

    a = y1 - y2;
    b = x2 - x1;
    c = x1 * y2 - x2 * y1;

    angle = Math.atan( -a / b );
  }

  /**
   * Длина отрезка.
   *
   * @return double - длина отрезка
   */
  public double length() {
    double dx = Math.abs( x1 - x2 );
    double dy = Math.abs( y1 - y2 );
    return Math.sqrt( dx * dx + dy * dy );
  }

  /**
   * Угол наклона прямой.
   *
   * @return double - угол наклона прямой в радианах
   */
  public double angle() {
    return angle;
  }

  /**
   * Определяет расстояние от прямой до точки
   *
   * @param x0 double - x координата точки
   * @param y0 double - y координата точки
   * @return double - расстояние до прямой
   */
  public double distanceTo( double x0, double y0 ) {
    return Math.abs( (a * x0 + b * y0 + c) / Math.sqrt( a * a + b * b ) );
  }

  /**
   * Возвращает координаты точки, являющейся проекцией заданной точки на прямую.
   *
   * @param x0 double - x координата точки
   * @param y0 double - y координата точки
   * @return ID2Point - точка проекции
   */
  public ID2Point projectionOf( double x0, double y0 ) {
    double x = (b * (b * x0 - a * y0) - a * c) / (a * a + b * b);
    double y = (a * (-b * x0 + a * y0) - b * c) / (a * a + b * b);
    return new D2Point( x, y );
  }

  /**
   * Вычисляет координату x для указанной координаты y
   *
   * @param y double - координата y
   * @return double - координата x
   */
  public double calcX( double y ) {
    return (-b * y - c) / a;
  }

  /**
   * Вычисляет координату y для указанной координаты x
   *
   * @param x double - координата x
   * @return double - координата y
   */
  public double calcY( double x ) {
    return -(a * x + c) / b;
  }

  /**
   * Вычисляет приращение координаты x для указанного приращения координаты y
   *
   * @param aDeltaY double - приращение координаты y
   * @return double - приращение координаты x
   */
  public double calcDeltaX( double aDeltaY ) {
    double x = calcX( 0 );
    return calcX( aDeltaY ) - x;
  }

  /**
   * Вычисляет приращение координаты y для указанного приращения координаты x
   *
   * @param aDeltaX double - приращение координаты x
   * @return double - приращение координаты y
   */
  public double calcDeltaY( double aDeltaX ) {
    double y = calcY( 0 );
    return calcY( aDeltaX ) - y;
  }

  /**
   * Возвращает левую X координату, описывающего прямоугольника.
   *
   * @return double - левая X координата, описывающего прямоугольника
   */
  public double x1() {
    return Math.min( x1, x2 );
  }

  /**
   * Возвращает верхнюю Y координату, описывающего прямоугольника.
   *
   * @return double - верхняя Y координата, описывающего прямоугольника
   */
  public double y1() {
    return Math.min( y1, y2 );
  }

  /**
   * Возвращает правую X координату, описывающего прямоугольника.
   *
   * @return double - правая X координата, описывающего прямоугольника
   */
  public double x2() {
    return Math.max( x1, x2 );
  }

  /**
   * Возвращает нижнюю Y координату, описывающего прямоугольника.
   *
   * @return double - нижняя Y координата, описывающего прямоугольника
   */
  public double y2() {
    return Math.max( y1, y2 );
  }

  /**
   * Возвращает левую X координату, описывающего прямоугольника.
   *
   * @return double - левую X координату, описывающего прямоугольника
   */
  public double boundsLeft() {
    return Math.min( x1, x2 );
  }

  /**
   * Возвращает верхнюю Y координату, описывающего прямоугольника.
   *
   * @return double - верхнюю Y координату, описывающего прямоугольника
   */
  public double boundsTop() {
    return Math.min( y1, y2 );
  }

  /**
   * Возвращает правую X координату, описывающего прямоугольника.
   *
   * @return double - правую X координату, описывающего прямоугольника
   */
  public double boundsRight() {
    return Math.max( x1, x2 );
  }

  /**
   * Возвращает нижнюю Y координату, описывающего прямоугольника.
   *
   * @return double - нижнюю Y координату, описывающего прямоугольника
   */
  public double boundsBottom() {
    return Math.max( y1, y2 );
  }

  /**
   * Возвращает прямоугольник, описывающий данный отрезок.
   *
   * @return Rectangle2D - прямоугольник, описывающий данный отрезок
   */
  Rectangle2D bounds() {
    double xx1 = Math.min( x1, x2 );
    double yy1 = Math.min( y1, y2 );
    return new Rectangle2D.Double( xx1, yy1, Math.abs( x2 - x1 ), Math.abs( y2 - y1 ) );
  }

  /**
   * Определяет принадлежит ли точка отрезку прямой.<br>
   *
   * @param aX double - x координата точки
   * @param aY double - y координата точки
   * @return <b>true</b> - точка принадлежит отрезку<br>
   *         <b>false</b> - точка находится вне отрезка прямой
   */
  public boolean contains( double aX, double aY ) {
    double xx1 = Math.min( x1, x2 );
    double yy1 = Math.min( y1, y2 );
    Rectangle2D r = new Rectangle2D.Double( xx1, yy1, Math.abs( x2 - x1 ), Math.abs( y2 - y1 ) );
    return r.contains( aX, aY );
  }

  /**
   * Возвращает точку пересечения двух отрезков или null если пересечение отсутствует.
   * <p>
   *
   * @param aSeg D2LineSegment - отрезок линии, пересечение с которым необходимо найти
   * @return ID2Point - точку пересечения двух отрезков или null если пересечение отсутствует
   */
  public ID2Point lineIntersection( D2LineSegment aSeg ) {
    double a1 = y2 - y1;
    double b1 = x1 - x2;
    double c1 = a1 * x1 + b1 * y1;

    double a2 = aSeg.y2 - aSeg.y1;
    double b2 = aSeg.x1 - aSeg.x2;
    double c2 = a2 * aSeg.x1 + b2 * aSeg.y1;

    double determinant = a1 * b2 - a2 * b1;

    if( determinant == 0 ) {
      // The lines are parallel. This is simplified
      return null;
    }
    double x = (b2 * c1 - b1 * c2) / determinant;
    double y = (a1 * c2 - a2 * c1) / determinant;
    return new D2Point( x, y );
  }

  /**
   * Возвращает точку, соответствующую центру отрезка
   *
   * @return ID2Point - точку, соответствующую центру отрезка
   */
  public ID2Point center() {
    return new D2Point( x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2 );
  }

  /**
   * Вычисляет координаты точки на прямой, находящейся на заданном расстоянии от заданной точки.
   *
   * @param aStartPoint ID2Point -точка на отрезке прямой, от которой отсчитывается расстояние
   * @param aDistance double - расстояние от заданной точки
   * @return ID2Point - точка на прямой, находящаяся на заданном расстоянии от заданной точки
   */
  public ID2Point calcPoint( ID2Point aStartPoint, double aDistance ) {
    double dx = aDistance / Math.sqrt( 1 + Math.atan( angle ) );
    double dy = dx * Math.atan( angle );
    return new D2Point( aStartPoint.x() + dx, aStartPoint.y() + dy );
  }

  /**
   * Возвращает новый отрезок, полученный путем параллельного переноса на aDx и aDy
   *
   * @param aDx double - смещение по оси х
   * @param aDy double - смещение по оси y
   * @return D2LineSegment - новый перемещенный отрезок линии
   */
  public D2LineSegment moveOn( double aDx, double aDy ) {
    return new D2LineSegment( x1 + aDx, y1 + aDy, x2 + aDx, y2 + aDy );
  }

  /**
   * Возвращает новый отрезок, повёрнутый на указанный угол, относительно центральной точки исходного отрезка.
   *
   * @param aAngle double - угол в радианах
   * @return GeomSegment - отрезок, повёрнутый на указанный угол, относительно центральной точки
   */
  public D2LineSegment rotate( double aAngle ) {

    ID2Point center = center();
    double r = length() / 2;

    double newX1 = r * Math.cos( aAngle + angle );
    double newX2 = r * Math.cos( aAngle + angle + Math.PI );
    double newY1 = r * Math.sin( aAngle + angle );
    double newY2 = r * Math.sin( aAngle + angle + Math.PI );

    D2LineSegment s = new D2LineSegment( newX1, newY1, newX2, newY2 );
    return s.moveOn( center.x(), center.y() );
  }

  /**
   * Возвращает приращение по X и Y, которые соответствуют перемещению вдоль отрезка на указанное расстояние.
   *
   * @param aLength double - расстояние на которое необходимо переместиться вдоль отрезка
   * @return ID2Point - где X - соответсвует приращению X координаты, а Y - приращению Y координаты
   */
  public ID2Point lengthToDxDy( double aLength ) {
    double dx = aLength * Math.cos( angle );
    double dy = aLength * Math.sin( angle );
    return new D2Point( dx, dy );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов {@link Object}
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj instanceof D2LineSegment seg ) {
      if( Double.doubleToLongBits( x1 ) != Double.doubleToLongBits( seg.x1 ) ) {
        return false;
      }
      if( Double.doubleToLongBits( x2 ) != Double.doubleToLongBits( seg.x2 ) ) {
        return false;
      }
      if( Double.doubleToLongBits( y1 ) != Double.doubleToLongBits( seg.y1 ) ) {
        return false;
      }
      if( Double.doubleToLongBits( y2 ) != Double.doubleToLongBits( seg.y2 ) ) {
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int i = 17;
    i = 37 * i + Double.hashCode( x1 );
    i = 37 * i + Double.hashCode( y1 );
    i = 37 * i + Double.hashCode( x2 );
    i = 37 * i + Double.hashCode( y2 );
    return i;
  }

}
