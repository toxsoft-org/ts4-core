package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Angle} implementation.
 *
 * @author hazard157
 */
public final class D2Angle
    implements ID2Angle, Serializable {

  private static final long serialVersionUID = 2666622496509094742L;

  private static final char CHAR_DEGREE_SIGN = '°';

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Angle"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link D2AngleEdit} (but not to {@link D2Angle}).
   */
  public static final IEntityKeeper<ID2Angle> KEEPER =
      new AbstractEntityKeeper<>( ID2Angle.class, EEncloseMode.NOT_IN_PARENTHESES, ID2Angle.ZERO ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Angle aEntity ) {
          if( isDuck( aEntity.radians() ) ) {
            aSw.writeDouble( aEntity.radians() );
          }
          else {
            aSw.writeDouble( aEntity.degrees() );
            aSw.writeChar( CHAR_DEGREE_SIGN );
          }
        }

        @Override
        protected ID2Angle doRead( IStrioReader aSr ) {
          double angle = aSr.readDouble();
          if( aSr.peekChar( EStrioSkipMode.SKIP_NONE ) == CHAR_DEGREE_SIGN ) {
            aSr.nextChar();
            return D2AngleEdit.ofDegrees( angle );
          }
          return D2AngleEdit.ofRadians( angle );
        }
      };

  private final double radians;
  private final double degrees;

  /**
   * Copy constructor.
   *
   * @param aAngle {@link ID2Angle} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Angle( ID2Angle aAngle ) {
    TsNullArgumentRtException.checkNull( aAngle );
    radians = aAngle.radians();
    degrees = aAngle.degrees();
  }

  public D2Angle( double aAngle, boolean aIsRadians ) {
    if( aIsRadians ) {
      radians = checkAngleRadians( aAngle );
      degrees = duck( rad2deg( radians ) );
    }
    else {
      degrees = duck( aAngle );
      radians = deg2rad( degrees );
    }
  }

  /**
   * Creates angle of specified degrees.
   *
   * @param aDegrees double - angle value in degrees
   * @return {@link D2Angle} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2Angle ofDegrees( double aDegrees ) {
    return new D2Angle( aDegrees, false );
  }

  /**
   * Creates angle of specified radians.
   *
   * @param aRadians double - angle value in radians
   * @return {@link D2Angle} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2Angle ofRadians( double aRadians ) {
    return new D2Angle( aRadians, true );
  }

  // ------------------------------------------------------------------------------------
  // ID2Angle
  //

  @Override
  public double radians() {
    return radians;
  }

  @Override
  public double degrees() {
    return degrees;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "∠%.2f (%.1f" + CHAR_DEGREE_SIGN + ")", radians, degrees ); //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2AngleEdit that) ) {
      return false;
    }
    return this.radians == that.radians() && this.degrees == that.degrees();
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( radians );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( degrees );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

}
