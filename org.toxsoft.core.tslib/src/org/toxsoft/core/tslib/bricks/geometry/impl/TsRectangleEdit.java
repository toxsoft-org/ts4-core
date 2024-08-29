package org.toxsoft.core.tslib.bricks.geometry.impl;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link ITsRectangle}.
 *
 * @author hazard157
 */
public final class TsRectangleEdit
    implements ITsRectangle {

  private final TsPointEdit a    = new TsPointEdit( 0, 0 );
  private final TsPointEdit b    = new TsPointEdit( 0, 0 );
  private final TsPointEdit size = new TsPointEdit( 1, 1 );
  private final TsDimsEdit  dims = new TsDimsEdit( 1, 1 );

  /**
   * Создает прямоугольник с левой верхней точкой в (0,0) и размерами (1,1).
   */
  public TsRectangleEdit() {
    // nop
  }

  /**
   * Создает прямугольник двумя точками.
   *
   * @param aP1 {@link ITsPoint} - первая точка
   * @param aP2 {@link ITsPoint} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsRectangleEdit( ITsPoint aP1, ITsPoint aP2 ) {
    setRect( aP1, aP2 );
  }

  /**
   * Создает прямоугольник из координаты левого верхнего углая и размеров.
   * <p>
   * Точки могут иметь любые координаты - метод разберется и правильно определит коорднаты точка {@link #a()} и
   * {@link #b()}.
   *
   * @param aX int - X координата левого верхнего угла
   * @param aY int - Y координата левого верхнего угла
   * @param aWidth int - ширина прямогуольника
   * @param aHeight int - высота прямогуольника
   * @throws TsIllegalArgumentRtException aWidth < 1
   * @throws TsIllegalArgumentRtException aHeight < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public TsRectangleEdit( int aX, int aY, int aWidth, int aHeight ) {
    setRect( aX, aY, aWidth, aHeight );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ITsRectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsRectangleEdit( ITsRectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ITsRectangle
  //

  @Override
  public ITsPoint a() {
    return a;
  }

  @Override
  public ITsPoint b() {
    return b;
  }

  @Override
  public int x1() {
    return a.x();
  }

  @Override
  public int y1() {
    return a.y();
  }

  @Override
  public int x2() {
    return b.x();
  }

  @Override
  public int y2() {
    return b.y();
  }

  @Override
  public int width() {
    return size.x();
  }

  @Override
  public int height() {
    return size.y();
  }

  @Override
  public ITsPoint size() {
    return size;
  }

  @Override
  public ITsDims dims() {
    return dims;
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
    if( aObj instanceof ITsRectangle that ) {
      return this.x1() == that.x1() && this.x2() == that.x2() && this.y1() == that.y1() && this.y2() == that.y2();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    // внимание: у редактируемого прямоугольника должен быть такой же алгоритм подсчета!
    result = TsLibUtils.PRIME * result + x1();
    result = TsLibUtils.PRIME * result + x2();
    result = TsLibUtils.PRIME * result + y1();
    result = TsLibUtils.PRIME * result + y2();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Перемещает прямоугольник в заданное место.
   *
   * @param aNewX int - новая X координата левого верхнего угла.
   * @param aNewY int - новая Y координата левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void moveTo( int aNewX, int aNewY ) {
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < size.x() + aNewX );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < size.y() + aNewY );
    a.setPoint( aNewX, aNewY );
    b.setX( a.x() + size.x() - 1 );
    b.setY( a.y() + size.y() - 1 );
  }

  /**
   * Перемещает прямоугольник в заданное место.
   *
   * @param aNewA {@link ITsPoint} - новые координаты левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void moveTo( ITsPoint aNewA ) {
    TsNullArgumentRtException.checkNull( aNewA );
    moveTo( aNewA.x(), aNewA.y() );
  }

  /**
   * Смещает прямоугольник на заданное расстояние.
   *
   * @param aDeltaX int - изменение X координаты левого верхнего угла.
   * @param aDeltaY int - изменение Y координаты левого верхнего угла.
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void shiftOn( int aDeltaX, int aDeltaY ) {
    int newX = a.x() + aDeltaX;
    int newY = a.y() + aDeltaY;
    moveTo( newX, newY );
  }

  /**
   * Изменяет размеры прямугольника.
   *
   * @param aWidth int - ширина прямогуольника
   * @param aHeight int - высота прямогуольника
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException ширина < 1
   * @throws TsIllegalArgumentRtException высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setSize( int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aWidth + a.x() );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aHeight + a.y() );
    b.setX( a.x() + aWidth - 1 );
    b.setY( a.y() + aHeight - 1 );
    size.setPoint( aWidth, aHeight );
    dims.setDims( size.x(), size.y() );
  }

  /**
   * Изменяет размеры прямугольника.
   *
   * @param aSize {@link ITsPoint} - новые размеры
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException ширина < 1
   * @throws TsIllegalArgumentRtException высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    setSize( aSize.x(), aSize.y() );
  }

  /**
   * Изменяет размер прямоугольника на указанную величину.
   *
   * @param aDeltaW int - изменение ширины
   * @param aDeltaH int - изменение высоты
   * @throws TsIllegalArgumentRtException новая ширина < 1
   * @throws TsIllegalArgumentRtException новая высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void changeSize( int aDeltaW, int aDeltaH ) {
    int newWidth = size.x() + aDeltaW;
    int mewHeight = size.y() + aDeltaH;
    setSize( newWidth, mewHeight );
  }

  /**
   * Задает размеры прямоугольника из координаты левого верхнего углая и размеров.
   *
   * @param aX int - X координата левого верхнего угла
   * @param aY int - Y координата левого верхнего угла
   * @param aWidth int - ширина прямогуольника
   * @param aHeight int - высота прямогуольника
   * @throws TsIllegalArgumentRtException width or height< 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setRect( int aX, int aY, int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aWidth + aX );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aHeight + aY );
    a.setPoint( aX, aY );
    b.setPoint( aX + aWidth - 1, aY + aHeight - 1 );
    size.setPoint( aWidth, aHeight );
    dims.setDims( size.x(), size.y() );
  }

  /**
   * Создает прямугольник двумя точками.
   * <p>
   * Точки могут иметь любые координаты - метод разберется и правильно определит коорднаты точка {@link #a()} и
   * {@link #b()}.
   *
   * @param aP1 {@link ITsPoint} - первая точка
   * @param aP2 {@link ITsPoint} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void setRect( ITsPoint aP1, ITsPoint aP2 ) {
    if( aP1 == null || aP2 == null ) {
      throw new TsNullArgumentRtException();
    }
    a.setX( Math.min( aP1.x(), aP2.x() ) );
    a.setY( Math.min( aP1.y(), aP2.y() ) );
    b.setX( Math.max( aP1.x(), aP2.x() ) );
    b.setY( Math.max( aP1.y(), aP2.y() ) );
    size.setX( b.x() - a.x() + 1 );
    size.setY( b.y() - a.y() + 1 );
    dims.setDims( size.x(), size.y() );
  }

  /**
   * Sets this rectangle to be union with the specified rectangle.
   *
   * @param aRect {@link ITsRectangle} - the rectangle
   * @return boolean - <code>true</code> if rectangle was actually changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean union( ITsRectangle aRect ) {
    TsNullArgumentRtException.checkNull( aRect );
    return union( aRect.x1(), aRect.y1(), aRect.width(), aRect.height() );
  }

  /**
   * Sets this rectangle to be union with the specified rectangle.
   *
   * @param aX int - specified rectangle X coordinate
   * @param aY int - specified rectangle T coordinate
   * @param aWidth int - specified rectangle width
   * @param aHeight int - specified rectangle height
   * @return boolean - <code>true</code> if rectangle was actually changed
   * @throws TsIllegalArgumentRtException aWidth < 1 or aHeight < 1
   */
  public boolean union( int aX, int aY, int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeight < 1 );
    int x1 = Math.min( a.x(), aX );
    int y1 = Math.min( a.y(), aY );
    int x2 = Math.max( b.x(), aX + aWidth );
    int y2 = Math.max( b.y(), aY + aHeight );
    if( x1 == a.x() && y1 == a.y() && x2 == b.x() && y2 == b.y() ) {
      return false;
    }
    a.setPoint( x1, y1 );
    b.setPoint( x2, y2 );
    size.setPoint( b.x() - a.x() + 1, b.y() - a.y() + 1 );
    dims.setDims( size.x(), size.y() );
    return true;
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ITsRectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setRect( ITsRectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setRect( aSource.a(), aSource.b() );
  }

}
