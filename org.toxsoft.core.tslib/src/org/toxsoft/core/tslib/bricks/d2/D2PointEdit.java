package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Point} editable implementation.
 *
 * @author hazard157
 */
public final class D2PointEdit
    implements ID2Point, Serializable {

  private static final long serialVersionUID = 157157L;

  private double x = 0.0;
  private double y = 0.0;

  /**
   * Constructor.
   *
   * @param aX double - X coordinate
   * @param aY double - Y coordinate
   * @throws TsIllegalArgumentRtException argument is NAN of INFINITY
   */
  public D2PointEdit( double aX, double aY ) {
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aX ) );
    TsIllegalArgumentRtException.checkFalse( Double.isFinite( aY ) );
    x = aX;
    y = aY;
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Point} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2PointEdit( ID2Point aSource ) {
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
    if( !(object instanceof D2PointEdit p) ) {
      return false;
    }
    return (p.x() == this.x) && (p.y() == this.y);
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( x );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( y );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "(" + x + ',' + y + ')'; //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // ID2Point
  //

  @Override
  public double x() {
    return x;
  }

  @Override
  public double y() {
    return y;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets X coordinate.
   *
   * @param aX double - X coordinate
   */
  public void setX( double aX ) {
    x = aX;
  }

  /**
   * Задает y координату.
   *
   * @param aY double - y координата
   */
  public void setY( double aY ) {
    y = aY;
  }

  /**
   * Задает координаты точки.
   *
   * @param aX double - x координата
   * @param aY double - y координата
   */
  public void setPoint( double aX, double aY ) {
    x = aX;
    y = aY;
  }

  /**
   * Задает координаты точки.
   *
   * @param aSource {@link ID2Point} - исходня точка
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setPoint( ID2Point aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    x = aSource.x();
    y = aSource.y();
  }

}
