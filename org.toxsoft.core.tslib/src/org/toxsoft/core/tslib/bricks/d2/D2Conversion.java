package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Conversion} implementation.
 *
 * @author hazard157
 */
public final class D2Conversion
    implements ID2Conversion, Serializable {

  private static final long serialVersionUID = 1501632436869409292L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Conversion"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link D2ConversionEdit} (but not to {@link D2Conversion}).
   */
  public static final IEntityKeeper<ID2Conversion> KEEPER =
      new AbstractEntityKeeper<>( ID2Conversion.class, EEncloseMode.ENCLOSES_BASE_CLASS, ID2Conversion.NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Conversion aEntity ) {
          D2Angle.KEEPER.write( aSw, aEntity.rotation() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.zoomFactor() );
          aSw.writeSeparatorChar();
          D2Point.KEEPER.write( aSw, aEntity.origin() );
        }

        @Override
        protected ID2Conversion doRead( IStrioReader aSr ) {
          ID2Angle angle = D2Angle.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          double zoomFactor = aSr.readDouble();
          aSr.ensureSeparatorChar();
          ID2Point origin = D2Point.KEEPER.read( aSr );
          return new D2Conversion( angle, zoomFactor, origin );
        }
      };

  private final ID2Angle rotation;
  private final double   zoomFactor;
  private final ID2Point origin;

  /**
   * Constructor.
   *
   * @param aRotation {@link ID2Angle} - the rotation angle
   * @param aZoomFactor double - the zoom factor (1.0 = no zoom)
   * @param aOrigin {@link ID2Point} - source origin coordinates on target
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid value
   */
  public D2Conversion( ID2Angle aRotation, double aZoomFactor, ID2Point aOrigin ) {
    zoomFactor = checkZoom( aZoomFactor );
    origin = new D2Point( aOrigin );
    rotation = new D2Angle( aRotation );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Conversion} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Conversion( ID2Conversion aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    rotation = new D2Angle( aSource.rotation() );
    zoomFactor = aSource.zoomFactor();
    origin = new D2Point( aSource.origin() );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversion
  //

  @Override
  public double zoomFactor() {
    return zoomFactor;
  }

  @Override
  public ID2Point origin() {
    return origin;
  }

  @Override
  public ID2Angle rotation() {
    return rotation;
  }

  @Override
  public boolean isConversion() {
    return (zoomFactor != 1.0) || !rotation.equals( ID2Angle.ZERO ) || !origin.equals( ID2Point.ZERO );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "%s %.3f %s", rotation.toString(), zoomFactor, origin.toString() ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Conversion that) ) {
      return false;
    }
    return this.rotation.equals( that.rotation() ) && this.zoomFactor == that.zoomFactor()
        && this.origin.equals( that.origin() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( zoomFactor );
    result = PRIME * result + rotation.hashCode();
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    result = PRIME * result + origin.hashCode();
    return result;
  }

}
