package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Vector} implementation.
 *
 * @author hazard157
 */
public final class D2Vector
    implements ID2Vector, Serializable {

  private static final long serialVersionUID = -6227376902258600650L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Vector"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Read {@link ID2Vector} may safely be casted to {@link D2PointEdit}.
   */
  public static final IEntityKeeper<ID2Vector> KEEPER =
      new AbstractEntityKeeper<>( ID2Vector.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Vector aEntity ) {
          D2Point.KEEPER.write( aSw, aEntity.a() );
          aSw.writeSeparatorChar();
          D2Point.KEEPER.write( aSw, aEntity.b() );
        }

        @Override
        protected ID2Vector doRead( IStrioReader aSr ) {
          ID2Point a = D2Point.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ID2Point b = D2Point.KEEPER.read( aSr );
          return new D2VectorEdit( a, b );
        }

      };

  private final ID2Point a;
  private final ID2Point b;
  private final ID2Angle angle;
  private final double   length;

  /**
   * Constructor.
   *
   * @param aInitPoint {@link ID2Point} - initial point
   * @param aTermPoint {@link ID2Point} - terminal point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Vector( ID2Point aInitPoint, ID2Point aTermPoint ) {
    a = new D2Point( aInitPoint );
    b = new D2Point( aTermPoint );
    double dx = b.x() - a.x();
    double dy = b.y() - a.y();
    length = duck( Math.sqrt( dx * dx + dy * dy ) );
    angle = D2Angle.ofRadians( Math.acos( dx / length ) );
  }

  /**
   * Creates vector basted on initial coordinates, length and angle.
   *
   * @param aX double - X coordintae of initial point
   * @param aY double - L coordintae of initial point
   * @param aLength souble - the length of the vector
   * @param aDegrees double angle in degrees
   * @return {@link D2Vector} - created instence
   * @throws TsIllegalArgumentRtException any value is invalid as defined by {@link D2Utils}<code>.checkXxx()</code>
   */
  public static D2Vector ofXyLenDeg( double aX, double aY, double aLength, double aDegrees ) {
    ID2Point initPoint = new D2Point( aX, aY );
    checkLength( aLength );
    checkAngleDegrees( aDegrees );
    double radians = deg2rad( aDegrees );
    double dx = Math.cos( radians );
    double dy = Math.sin( radians );
    ID2Point termPoint = new D2Point( aX + dx, aY + dy );
    return new D2Vector( initPoint, termPoint );
  }

  // ------------------------------------------------------------------------------------
  // ID2Vector
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
  public double length() {
    return length;
  }

  @Override
  public ID2Angle angle() {
    return angle;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "{%s,%s,l=%.4f,%s}", //$NON-NLS-1$
        a.toString(), b.toString(), Double.valueOf( length ), angle.toString() );
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Vector that) ) {
      return false;
    }
    return this.a.equals( that.a() ) && this.b.equals( that.b() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + a.hashCode();
    result = PRIME * result + b.hashCode();
    return result;
  }

}
