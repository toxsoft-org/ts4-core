package org.toxsoft.core.tslib.bricks.geometry.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsPoint} editable implementation.
 *
 * @author hazard157
 */
public final class TsPointEdit
    implements ITsPoint, Serializable {

  private static final long serialVersionUID = 157157L;

  private int x = 0;
  private int y = 0;

  /**
   * Constructor.
   *
   * @param aX int - X coordinate
   * @param aY int - X coordinate
   */
  public TsPointEdit( int aX, int aY ) {
    x = aX;
    y = aY;
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsPoint} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsPointEdit( ITsPoint aSource ) {
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
    if( !(object instanceof ITsPoint p) ) {
      return false;
    }
    return (p.x() == this.x) && (p.y() == this.y);
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
   * Changes location on specified amount.
   *
   * @param aDeltaX int - X coordinate shifting amount
   * @param aDeltaY int - Y coordinate shifting amount
   */
  public void shiftOn( int aDeltaX, int aDeltaY ) {
    x += aDeltaX;
    y += aDeltaY;
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
