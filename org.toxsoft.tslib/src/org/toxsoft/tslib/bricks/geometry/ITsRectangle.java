package org.toxsoft.tslib.bricks.geometry;

import org.toxsoft.tslib.bricks.geometry.impl.TsRectangle;
import org.toxsoft.tslib.bricks.geometry.impl.TsRectangleEdit;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

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
   * Возвращает размеры прямоугольника.
   *
   * @return {@link ITsPoint} - размеры прямоугольника
   */
  ITsPoint size();

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
  public boolean contains( int aX, int aY ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean contains( ITsPoint aPoint ) {
    throw new TsNullObjectErrorRtException();
  }

}
