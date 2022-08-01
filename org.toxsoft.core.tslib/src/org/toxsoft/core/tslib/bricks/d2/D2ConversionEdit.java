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
public final class D2ConversionEdit
    implements ID2ConversionEdit, Serializable {

  private static final long serialVersionUID = 4995515663514559187L;

  private final ID2PointEdit    origin   = new D2PointEdit();
  private final ID2RotationEdit rotation = new D2RotationEdit();

  private double zoomFactor = 1.0;

  /**
   * Constructor.
   * <p>
   * Creates copy of {@link ID2Conversion#NONE}.
   */
  public D2ConversionEdit() {
    this( ID2Conversion.NONE );
  }

  /**
   * Constructor.
   *
   * @param aZoomFactor double - the zoom factor (1.0 = no zoom)
   * @param aOrigin {@link ID2Point} - source origin coordinates on target
   * @param aRotation {@link ID2Rotation} - the rotation parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid value
   */
  public D2ConversionEdit( double aZoomFactor, ID2Point aOrigin, ID2Rotation aRotation ) {
    zoomFactor = checkZoom( aZoomFactor );
    origin.setPoint( aOrigin );
    rotation.setRotation( aRotation );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Conversion} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2ConversionEdit( ID2Conversion aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    zoomFactor = aSource.zoomFactor();
    origin.setPoint( aSource.origin() );
    rotation.setRotation( aSource.rotation() );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversion
  //

  @Override
  public double zoomFactor() {
    return zoomFactor;
  }

  @Override
  public ID2PointEdit origin() {
    return origin;
  }

  @Override
  public ID2RotationEdit rotation() {
    return rotation;
  }

  // ------------------------------------------------------------------------------------
  // ID2ConversionEdit
  //

  @Override
  public void setZoomFactor( double aZoomFactor ) {
    zoomFactor = duck( checkZoom( aZoomFactor ) );
  }

  @Override
  public void setConversion( ID2Conversion aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    zoomFactor = aSource.zoomFactor();
    origin.setPoint( aSource.origin() );
    rotation.setRotation( aSource.rotation() );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "%.4f %s %s", zoomFactor, origin.toString(), rotation.toString() ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Conversion that) ) {
      return false;
    }
    return this.zoomFactor == that.zoomFactor() && this.origin.equals( that.origin() )
        && this.rotation.equals( that.rotation() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( zoomFactor );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    result = PRIME * result + origin.hashCode();
    result = PRIME * result + rotation.hashCode();
    return result;
  }

}
