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

  private static final long serialVersionUID = 1010579325250088274L;

  private final ID2AngleEdit rotation   = new D2AngleEdit();
  private double             zoomFactor = 1.0;
  private final ID2PointEdit origin     = new D2PointEdit();

  /**
   * Constructor.
   * <p>
   * Creates an editable copy of {@link ID2Conversion#NONE}.
   */
  public D2ConversionEdit() {
    this( ID2Conversion.NONE );
  }

  /**
   * Constructor.
   *
   * @param aRotation {@link ID2Angle} - the rotation angle
   * @param aZoomFactor double - the zoom factor (1.0 = no zoom)
   * @param aOrigin {@link ID2Point} - source origin coordinates on target
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid value
   */
  public D2ConversionEdit( ID2Angle aRotation, double aZoomFactor, ID2Point aOrigin ) {
    rotation.setAngle( aRotation );
    zoomFactor = checkZoom( aZoomFactor );
    origin.setPoint( aOrigin );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Conversion} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2ConversionEdit( ID2Conversion aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    rotation.setAngle( aSource.rotation() );
    zoomFactor = aSource.zoomFactor();
    origin.setPoint( aSource.origin() );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversion
  //

  @Override
  public ID2AngleEdit rotation() {
    return rotation;
  }

  @Override
  public double zoomFactor() {
    return zoomFactor;
  }

  @Override
  public ID2PointEdit origin() {
    return origin;
  }

  @Override
  public boolean isConversion() {
    return (zoomFactor != 1.0) || !rotation.equals( ID2Angle.ZERO ) || !origin.equals( ID2Point.ZERO );
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
    rotation.setAngle( aSource.rotation() );
    zoomFactor = aSource.zoomFactor();
    origin.setPoint( aSource.origin() );
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
