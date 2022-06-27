package org.toxsoft.core.tsgui.ved.incub.geom;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link ID2Rectangle}.
 *
 * @author hazard157
 */
public final class D2Rectangle
    implements ID2Rectangle {

  private final D2Point a    = new D2Point( 0.0, 0.0 );
  private final D2Point b    = new D2Point( 100.0, 100.0 );
  private final D2Point size = new D2Point( 0.0, 0.0 );

  /**
   * Создает прямоугольник с левой верхней точкой в (0,0) и размерами (1,1).
   */
  public D2Rectangle() {
    // nop
  }

  /**
   * Создает прямугольник двумя точками.
   *
   * @param aP1 {@link ID2Point} - первая точка
   * @param aP2 {@link ID2Point} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public D2Rectangle( ID2Point aP1, ID2Point aP2 ) {
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
  public D2Rectangle( int aX, int aY, int aWidth, int aHeight ) {
    setRect( aX, aY, aWidth, aHeight );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ID2Rectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public D2Rectangle( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
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
    size.setPoint( a );
    b.setX( a.x() + aWidth - 1 );
    b.setY( a.y() + aHeight - 1 );
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
   * @throws TsIllegalArgumentRtException ширина < 1
   * @throws TsIllegalArgumentRtException высота < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public void setRect( int aX, int aY, int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aWidth + aX );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aHeight + aY );
    a.setPoint( aX, aY );
    b.setPoint( aX + aWidth - 1, aY + aHeight - 1 );
    size.setPoint( aWidth, aHeight );
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
    size.setX( b.x() - a.x() + 1 );
    size.setY( b.y() - a.y() + 1 );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ID2Rectangle} - исходный прямоугольник
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setRect( ID2Rectangle aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setRect( aSource.a(), aSource.b() );
  }

}
