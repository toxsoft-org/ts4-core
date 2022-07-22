package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2RotationEdit} implementation.
 *
 * @author hazard157
 */
public class D2RotationEdit
    implements ID2RotationEdit {

  private final ID2PointEdit pivotPoint    = new D2PointEdit();
  private final ID2AngleEdit rotationAngle = new D2AngleEdit();

  /**
   * Constructor.
   */
  public D2RotationEdit() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aPivotPoint {@link ID2Point} - pivot point
   * @param aRotationAngle {@link ID2Angle} - rotation angle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2RotationEdit( ID2Point aPivotPoint, ID2Angle aRotationAngle ) {
    TsNullArgumentRtException.checkNulls( aPivotPoint, aRotationAngle );
    pivotPoint.setPoint( aPivotPoint );
    rotationAngle.setAngle( aRotationAngle );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Rotation} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2RotationEdit( ID2Rotation aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    pivotPoint.setPoint( aSource.pivotPoint() );
    rotationAngle.setAngle( aSource.rotationAngle() );
  }

  // ------------------------------------------------------------------------------------
  // ID2RotationEdit
  //

  @Override
  public ID2PointEdit pivotPoint() {
    return pivotPoint;
  }

  @Override
  public ID2AngleEdit rotationAngle() {
    return rotationAngle;
  }

  @Override
  public void setRotation( ID2Rotation aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    pivotPoint.setPoint( aSource.pivotPoint() );
    rotationAngle.setAngle( aSource.rotationAngle() );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return '{' + pivotPoint.toString() + ' ' + rotationAngle.toString() + '}';
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Rotation that) ) {
      return false;
    }
    return this.pivotPoint.equals( that.pivotPoint() ) && this.rotationAngle.equals( that.rotationAngle() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + pivotPoint.hashCode();
    result = PRIME * result + rotationAngle.hashCode();
    return result;
  }

}
