package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Point} immutable implementation.
 *
 * @author hazard157
 */
public final class D2Point
    implements ID2Point, Serializable {

  private static final long serialVersionUID = 157157L;

  private final double x;
  private final double y;

  /**
   * Constructor.
   *
   * @param aX double - X coordinate
   * @param aY double - Y coordinate
   * @throws TsIllegalArgumentRtException argument is NAN of INFINITY
   */
  public D2Point( double aX, double aY ) {
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
  public D2Point( ID2Point aSource ) {
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
    if( !(object instanceof ID2Point p) ) {
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

}
