package org.toxsoft.tslib.bricks.geometry.impl;

import java.io.Serializable;

import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация {@link ITsPoint}.
 *
 * @author hazard157
 */
public final class TsPoint
    implements ITsPoint, Serializable {

  private static final long serialVersionUID = 157157L;

  private final int x;
  private final int y;

  /**
   * Создает точку со всеми координатами.
   *
   * @param aX int - x координата
   * @param aY int - н координата
   */
  public TsPoint( int aX, int aY ) {
    x = aX;
    y = aY;
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link ITsPoint} - исходня точка
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsPoint( ITsPoint aSource ) {
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
    // внимание: у редактируемой точки должен быть такой же алгоритм подсчета!
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

}
