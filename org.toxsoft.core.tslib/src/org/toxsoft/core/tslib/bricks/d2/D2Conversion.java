package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Conversion} implementation.
 *
 * @author hazard157
 */
public final class D2Conversion
    implements ID2Conversion, Serializable {

  private static final long serialVersionUID = 1501632436869409292L;

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

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "%s %.4f %s", rotation.toString(), zoomFactor, origin.toString() ); //$NON-NLS-1$
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
