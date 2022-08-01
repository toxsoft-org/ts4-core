package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2PointEdit} implementation.
 *
 * @author hazard157
 */
public final class D2PointEdit
    implements ID2PointEdit, Serializable {

  private static final long serialVersionUID = 5419533956300417542L;

  private double x = 0.0;
  private double y = 0.0;

  /**
   * Constructor of point (0.0, 0.0).
   */
  public D2PointEdit() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aX double - X coordinate
   * @param aY double - Y coordinate
   * @throws TsIllegalArgumentRtException argument is NAN of INFINITY
   */
  public D2PointEdit( double aX, double aY ) {
    x = duck( checkCoor( aX ) );
    y = duck( checkCoor( aY ) );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Point} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2PointEdit( ID2Point aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    x = duck( aSource.x() );
    y = duck( aSource.y() );
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
  // ID2PointEdit
  //

  @Override
  public void setX( double aX ) {
    x = duck( checkCoor( aX ) );
  }

  @Override
  public void setY( double aY ) {
    y = duck( checkCoor( aY ) );
  }

  @Override
  public void setPoint( double aX, double aY ) {
    x = duck( checkCoor( aX ) );
    y = duck( checkCoor( aY ) );
  }

  @Override
  public void setPoint( ID2Point aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    x = duck( aSource.x() );
    y = duck( aSource.y() );
  }

  @Override
  public void shiftOn( double aDeltaX, double aDeltaY ) {
    checkCoor( aDeltaX );
    checkCoor( aDeltaY );
    x = duck( x + aDeltaX );
    y = duck( y + aDeltaY );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "(" + x + ',' + y + ')'; //$NON-NLS-1$
  }

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

}
