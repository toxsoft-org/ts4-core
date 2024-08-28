package org.toxsoft.core.tslib.bricks.geometry.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
  // Object
  //

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( object instanceof ITsPoint p ) {
      return (p.x() == this.x) && (p.y() == this.y);
    }
    return false;
  }

  @Override
  public int hashCode() {
    // Note: all impoementaions of ITsPoint must have the same algorithm
    return x ^ y;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + '(' + x + ',' + y + ')';
  }

  // ------------------------------------------------------------------------------------
  // ITsPoint
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
