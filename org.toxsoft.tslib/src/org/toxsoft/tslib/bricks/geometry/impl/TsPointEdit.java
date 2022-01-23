package org.toxsoft.tslib.bricks.geometry.impl;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Редактируемая реализация {@link ITsPoint}.
 *
 * @author hazard157
 */
public final class TsPointEdit
    implements ITsPoint, Serializable {

  private static final long serialVersionUID = 157157L;

  private int x = 0;
  private int y = 0;

  /**
   * Создает точку со всеми координатами.
   *
   * @param aX int - x координата
   * @param aY int - н координата
   */
  public TsPointEdit( int aX, int aY ) {
    x = aX;
    y = aY;
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ITsPoint} - исходня точка
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsPointEdit( ITsPoint aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    x = aSource.x();
    y = aSource.y();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ITsPoint) ) {
      return false;
    }
    ITsPoint p = (ITsPoint)object;
    return (p.x() == this.x) && (p.y() == this.y);
  }

  @Override
  public int hashCode() {
    // внимание: у неизменяемой точки должен быть такой же алгоритм подсчета!
    return x ^ y;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + '(' + x + ',' + y + ')';
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsPoint
  //

  @Override
  public int x() {
    return x;
  }

  @Override
  public int y() {
    return y;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Задает x координату.
   *
   * @param aX int - x координата
   */
  public void setX( int aX ) {
    x = aX;
  }

  /**
   * Задает y координату.
   *
   * @param aY int - y координата
   */
  public void setY( int aY ) {
    y = aY;
  }

  /**
   * Задает координаты точки.
   *
   * @param aX int - x координата
   * @param aY int - y координата
   */
  public void setPoint( int aX, int aY ) {
    x = aX;
    y = aY;
  }

  /**
   * Задает координаты точки.
   *
   * @param aSource {@link ITsPoint} - исходня точка
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setPoint( ITsPoint aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    x = aSource.x();
    y = aSource.y();
  }

}
