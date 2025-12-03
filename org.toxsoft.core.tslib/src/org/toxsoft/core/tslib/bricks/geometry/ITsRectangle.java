package org.toxsoft.core.tslib.bricks.geometry;

import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Прямоугольник, реализация абстракции "прямоугольник паралелльный осям на координатной плоскости".
 * <p>
 * Прямоугольник создается так, что всегда точка {@link #a()} будет слева и вверху точки {@link #b()}.
 * <p>
 * Прямоугльник, у которого {@link #a()} = {@link #b()} имеет размеры 1x1.
 * <p>
 * Имеет две реализации - неизменяемую {@link TsRectangle} и редактируемую {@link TsRectangleEdit}. Всегда надо
 * стараться использовать неизменяемый класс. Редактируемый класс бывает нужен при проведении расчетов.
 *
 * @author hazard157
 */
public interface ITsRectangle {

  /**
   * "Никакой" прямоугольник, не имеет координат, все методы выбрасывают {@link TsNullObjectErrorRtException}.
   */
  ITsRectangle NONE = new InternalNoneRctangle();

  /**
   * Минимальный прямоугольник (0,0,1,1).
   */
  ITsRectangle MINRECT = new TsRectangle( 0, 0, 1, 1 );

  /**
   * Возвращает левую верхнюю точку прямоугольника.
   *
   * @return {@link ITsPoint} - левая верхняя точка прямоугольника
   */
  ITsPoint a();

  /**
   * Возвращает правую нижнюю точку прямоугольника.
   *
   * @return {@link ITsPoint} - правая нижняя точка прямоугольника
   */
  ITsPoint b();

  /**
   * Возвращает X координату левой верхней точки.
   *
   * @return int - X координата левой верхней точки
   */
  int x1();

  /**
   * Возвращает Y координату левой верхней точки.
   *
   * @return int - Y координата левой верхней точки
   */
  int y1();

  /**
   * Возвращает X координату правой нижней точки.
   *
   * @return int - X координата правой нижней точки
   */
  int x2();

  /**
   * Возвращает Y координату правой нижней точки.
   *
   * @return int - Y координата правой нижней точки
   */
  int y2();

  /**
   * Возвращает ширину прямоугольника.
   *
   * @return int - ширина прямоугольника
   */
  int width();

  /**
   * Возвращает высоту прямоугольника.
   *
   * @return int - высота прямоугольника
   */
  int height();

  /**
   * Retruns the dimansions of the rectangle as {@link ITsPoint}.
   * <p>
   * For an editable instance returns reference to the editable field.
   *
   * @return {@link ITsPoint} - the dimansions as {@link ITsPoint}
   */
  ITsPoint size();

  /**
   * Retruns the dimansions of the rectangle.
   * <p>
   * For an editable instance returns reference to the editable field.
   *
   * @return {@link ITsDims} - the dimansions
   */
  ITsDims dims();

  /**
   * Определяет, находится ли точка внутри прямоугольника.
   *
   * @param aX int - X координата точки
   * @param aY int - Y координата точки
   * @return boolean - признак, что точка внутри прямоугольника
   */
  default boolean contains( int aX, int aY ) {
    return aX >= x1() && aX <= x2() && aY >= y1() && aY <= y2();
  }

  /**
   * Определяет, находится ли точка внутри прямоугольника.
   *
   * @param aPoint {@link ITsPoint} - координаты точки
   * @return boolean - признак, что точка внутри прямоугольника
   * @throws TsNullArgumentRtException аргумент = null
   */
  default boolean contains( ITsPoint aPoint ) {
    if( aPoint == null ) {
      throw new TsNullArgumentRtException();
    }
    return aPoint.x() >= x1() && aPoint.x() <= x2() && aPoint.y() >= y1() && aPoint.y() <= y2();
  }

  /**
   * Determines if the argument rectangle is is inside this rectangle.
   *
   * @param aX int - X coordinate of the rectangle top right corner
   * @param aY int - W coordinate of the rectangle top right corner
   * @param aWidth int - the width of the rectangle
   * @param aHeight int - the height of the rectangle
   * @return boolean - <code>true</code> if the argument is inside this rectangle
   * @throws TsValidationFailedRtException failed {@link TsRectangle#validateArgs(int, int, int, int)}
   */
  default boolean contains( int aX, int aY, int aWidth, int aHeight ) {
    TsRectangle.checkArgs( aX, aY, aWidth, aHeight );
    return contains( aX, aY ) && contains( aX + aWidth - 1, aY + aHeight - 1 );
  }

  /**
   * Determines if the argument rectangle is is inside this rectangle.
   *
   * @param aRect {@link ITsRectangle} - the argument rectangle
   * @return boolean - <code>true</code> if the argument is inside this rectangle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default boolean contains( ITsRectangle aRect ) {
    TsNullArgumentRtException.checkNulls( aRect );
    return contains( aRect.a() ) && contains( aRect.b() );
  }

  /**
   * Determines if this rectangle intersects with the argument rectangle
   *
   * @param aRect {@link ITsRectangle} - the rectangle
   * @return boolean - <code>true</code> rectangles has at least one common point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default boolean intersects( ITsRectangle aRect ) {
    if( this.x1() > aRect.x2() || this.x2() < aRect.x1() ) {
      return false;
    }
    if( this.y1() > aRect.y2() || this.y2() < aRect.y1() ) {
      return false;
    }
    return true;
  }

}

class InternalNoneRctangle
    implements ITsRectangle {

  @Override
  public ITsPoint a() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public ITsPoint b() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int x1() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int y1() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int x2() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int y2() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int width() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int height() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public ITsPoint size() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public ITsDims dims() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean contains( int aX, int aY ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean contains( ITsPoint aPoint ) {
    throw new TsNullObjectErrorRtException();
  }

}
