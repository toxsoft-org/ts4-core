package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link ID2Rectangle}.
 *
 * @author hazard157
 */
public final class D2Rectangle
    implements ID2Rectangle {

  private final D2Point a;
  private final D2Point b;
  private final D2Point size;

  /**
   * Создает прямугольник двумя точками.
   *
   * @param aP1 {@link ID2Point} - первая точка
   * @param aP2 {@link ID2Point} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public D2Rectangle( ID2Point aP1, ID2Point aP2 ) {
    if( aP1 == null || aP2 == null ) {
      throw new TsNullArgumentRtException();
    }
    a = new D2Point( Math.min( aP1.x(), aP2.x() ), Math.min( aP1.y(), aP2.y() ) );
    b = new D2Point( Math.max( aP1.x(), aP2.x() ), Math.max( aP1.y(), aP2.y() ) );
    size = new D2Point( b.x() - a.x(), b.y() - a.y() );
  }

  /**
   * Создает прямоугольник из координаты левого верхнего углая и размеров.
   * <p>
   * Точки могут иметь любые координаты - метод разберется и правильно определит коорднаты точка {@link #a()} и
   * {@link #b()}.
   *
   * @param aX double - X координата левого верхнего угла
   * @param aY double - Y координата левого верхнего угла
   * @param aWidth double - ширина прямогуольника
   * @param aHeight double - высота прямогуольника
   * @throws TsIllegalArgumentRtException aWidth < 1
   * @throws TsIllegalArgumentRtException aHeight < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public D2Rectangle( double aX, double aY, double aWidth, double aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    a = new D2Point( aX, aY );
    b = new D2Point( aX + aWidth - 1, aY + aHeight - 1 );
    size = new D2Point( aWidth, aHeight );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ID2Rectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public D2Rectangle( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    a = new D2Point( aSource.a() );
    b = new D2Point( aSource.b() );
    size = new D2Point( b.x() - a.x(), b.y() - a.y() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ID2Rectangle
  //

  @Override
  public ID2Point a() {
    return a;
  }

  @Override
  public ID2Point b() {
    return b;
  }

  @Override
  public double x1() {
    return a.x();
  }

  @Override
  public double y1() {
    return a.y();
  }

  @Override
  public double x2() {
    return b.x();
  }

  @Override
  public double y2() {
    return b.y();
  }

  @Override
  public double width() {
    return size.x();
  }

  @Override
  public double height() {
    return size.y();
  }

  @Override
  public ID2Point size() {
    return size;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "[" + a().toString() + "," + size.x() + "," + size.y() + "]";
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof ID2Rectangle that ) {
      return this.x1() == that.x1() && this.x2() == that.x2() && this.y1() == that.y1() && this.y2() == that.y2();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + a.hashCode();
    result = TsLibUtils.PRIME * result + b.hashCode();
    return result;
  }

}
