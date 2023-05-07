package org.toxsoft.core.tslib.bricks.d2;

import java.io.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Rectangle} editable implementation.
 *
 * @author hazard157
 */
public final class D2RectangleEdit
    implements ID2Rectangle, Serializable {

  private static final long serialVersionUID = -2688868471678739599L;

  private final D2PointEdit a    = new D2PointEdit( 0.0, 0.0 );
  private final D2PointEdit b    = new D2PointEdit( 100.0, 100.0 );
  private final D2SizeEdit  size = new D2SizeEdit( 0.0, 0.0 );

  /**
   * Создает прямоугольник с левой верхней точкой в (0,0) и размерами (1,1).
   */
  public D2RectangleEdit() {
    // nop
  }

  /**
   * Создает прямугольник двумя точками.
   *
   * @param aP1 {@link ID2Point} - первая точка
   * @param aP2 {@link ID2Point} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public D2RectangleEdit( ID2Point aP1, ID2Point aP2 ) {
    setRect( aP1, aP2 );
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
  public D2RectangleEdit( double aX, double aY, double aWidth, double aHeight ) {
    setRect( aX, aY, aWidth, aHeight );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ID2Rectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public D2RectangleEdit( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ID2Rectangle
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
    return size.width();
  }

  @Override
  public double height() {
    return size.height();
  }

  @Override
  public ID2Size size() {
    return size;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "[" + a().toString() + "," + size.toString() + "]";
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Перемещает прямоугольник в заданное место.
   *
   * @param aNewX double - новая X координата левого верхнего угла.
   * @param aNewY double - новая Y координата левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void moveTo( double aNewX, double aNewY ) {
    a.setPoint( aNewX, aNewY );
    b.setX( a.x() + size.width() );
    b.setY( a.y() + size.height() );
  }

  /**
   * Перемещает прямоугольник в заданное место.
   *
   * @param aNewA {@link ID2Point} - новые координаты левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void moveTo( ID2Point aNewA ) {
    TsNullArgumentRtException.checkNull( aNewA );
    moveTo( aNewA.x(), aNewA.y() );
  }

  /**
   * Смещает прямоугольник на заданное расстояние.
   *
   * @param aDeltaX double - изменение X координаты левого верхнего угла.
   * @param aDeltaY double - изменение Y координаты левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void shiftOn( double aDeltaX, double aDeltaY ) {
    double newX = a.x() + aDeltaX;
    double newY = a.y() + aDeltaY;
    moveTo( newX, newY );
  }

  /**
   * Изменяет размеры прямугольника.
   *
   * @param aWidth double - ширина прямогуольника
   * @param aHeight double - высота прямогуольника
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException ширина < 0.0
   * @throws TsIllegalArgumentRtException высота < 0.0
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setSize( double aWidth, double aHeight ) {
    size.setSize( aWidth, aHeight );
    b.setX( a.x() + size.width() );
    b.setY( a.y() + size.height() );
  }

  /**
   * Изменяет размеры прямугольника.
   *
   * @param aSize {@link ID2Point} - новые размеры
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException ширина < 1
   * @throws TsIllegalArgumentRtException высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setSize( ID2Point aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    setSize( aSize.x(), aSize.y() );
  }

  /**
   * Изменяет размер прямоугольника на указанную величину.
   *
   * @param aDeltaW double - изменение ширины
   * @param aDeltaH double - изменение высоты
   * @throws TsIllegalArgumentRtException новая ширина < 1
   * @throws TsIllegalArgumentRtException новая высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void changeSize( double aDeltaW, double aDeltaH ) {
    double newWidth = size.width() + aDeltaW;
    double mewHeight = size.height() + aDeltaH;
    setSize( newWidth, mewHeight );
  }

  /**
   * Задает размеры прямоугольника из координаты левого верхнего углая и размеров.
   *
   * @param aX double - X координата левого верхнего угла
   * @param aY double - Y координата левого верхнего угла
   * @param aWidth double - ширина прямогуольника
   * @param aHeight double - высота прямогуольника
   * @throws TsIllegalArgumentRtException ширина < 0.0
   * @throws TsIllegalArgumentRtException высота < 0.0
   */
  public void setRect( double aX, double aY, double aWidth, double aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0.0 || aHeight < 0.0 );
    setSize( aWidth, aHeight );
    a.setPoint( aX, aY );
  }

  /**
   * Создает прямугольник двумя точками.
   * <p>
   * Точки могут иметь любые координаты - метод разберется и правильно определит коорднаты точка {@link #a()} и
   * {@link #b()}.
   *
   * @param aP1 {@link ID2Point} - первая точка
   * @param aP2 {@link ID2Point} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void setRect( ID2Point aP1, ID2Point aP2 ) {
    if( aP1 == null || aP2 == null ) {
      throw new TsNullArgumentRtException();
    }
    a.setX( Math.min( aP1.x(), aP2.x() ) );
    a.setY( Math.min( aP1.y(), aP2.y() ) );
    b.setX( Math.max( aP1.x(), aP2.x() ) );
    b.setY( Math.max( aP1.y(), aP2.y() ) );
    size.setWidth( b.x() - a.x() );
    size.setHeight( b.y() - a.y() );
  }

  /**
   * Copy rectangle from the source.
   *
   * @param aSource {@link ID2Rectangle} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setRect( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setRect( aSource.a(), aSource.b() );
  }

}
