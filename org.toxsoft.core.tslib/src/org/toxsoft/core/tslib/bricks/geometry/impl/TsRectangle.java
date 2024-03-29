package org.toxsoft.core.tslib.bricks.geometry.impl;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Неизменяемая реализация {@link ITsRectangle}.
 *
 * @author hazard157
 */
public final class TsRectangle
    implements ITsRectangle {

  private final ITsPoint a;
  private final ITsPoint b;
  private final ITsPoint size;

  /**
   * Создает прямугольник двумя точками.
   *
   * @param aP1 {@link ITsPoint} - первая точка
   * @param aP2 {@link ITsPoint} - вторая точка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsRectangle( ITsPoint aP1, ITsPoint aP2 ) {
    if( aP1 == null || aP2 == null ) {
      throw new TsNullArgumentRtException();
    }
    int x1 = Math.min( aP1.x(), aP2.x() );
    int y1 = Math.min( aP1.y(), aP2.y() );
    int x2 = Math.max( aP1.x(), aP2.x() );
    int y2 = Math.max( aP1.y(), aP2.y() );
    a = new TsPoint( x1, y1 );
    b = new TsPoint( x2, y2 );
    size = new TsPoint( x2 - x1 + 1, y2 - y1 + 1 );
  }

  /**
   * Создает прямоугольник из координаты левого верхнего углая и размеров.
   *
   * @param aX int - X координата левого верхнего угла
   * @param aY int - Y координата левого верхнего угла
   * @param aWidth int - ширина прямогуольника
   * @param aHeight int - высота прямогуольника
   * @throws TsIllegalArgumentRtException aWidth < 1
   * @throws TsIllegalArgumentRtException aHeight < 1
   * @throws TsIllegalArgumentRtException правая нижняя точка выходат за {@link Integer#MAX_VALUE} значения
   */
  public TsRectangle( int aX, int aY, int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeight < 1 );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aWidth + aX );
    TsIllegalArgumentRtException.checkTrue( Integer.MAX_VALUE < aHeight + aY );
    a = new TsPoint( aX, aY );
    b = new TsPoint( aX + aWidth - 1, aY + aHeight - 1 );
    size = new TsPoint( aWidth, aHeight );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsRectangle} - the source
   */
  public TsRectangle( ITsRectangle aSource ) {
    this( aSource.a(), aSource.b() );
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

}
