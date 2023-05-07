package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Point} immutable implementation.
 *
 * @author hazard157
 */
public final class D2Point
    implements ID2Point, Serializable {

  private static final long serialVersionUID = 4151059724175075255L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Point"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link D2PointEdit} (but not to {@link D2Point}).
   */
  public static final IEntityKeeper<ID2Point> KEEPER =
      new AbstractEntityKeeper<>( ID2Point.class, EEncloseMode.ENCLOSES_BASE_CLASS, ID2Point.ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Point aEntity ) {
          aSw.writeDouble( aEntity.x() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.y() );
        }

        @Override
        protected ID2Point doRead( IStrioReader aSr ) {
          double x = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double y = aSr.readDouble();
          return new D2PointEdit( x, y );
        }
      };

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
    x = duck( checkCoor( aX ) );
    y = duck( checkCoor( aY ) );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Point} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Point( ID2Point aSource ) {
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
    if( object instanceof ID2Point p ) {
      return (p.x() == this.x) && (p.y() == this.y);
    }
    return false;
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
