package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinTypeInfo} implementation for {@link ID2Angle} entities.
 *
 * @author vs
 */
public class TtiD2Angle
    extends AbstractTinTypeInfo<ID2Angle> {

  private static final String FID_RADIANS = "radians"; //$NON-NLS-1$
  private static final String FID_DEGREES = "degrees"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new TtiD2Angle();

  private TtiD2Angle() {
    super( ETinTypeKind.FULL, DT_D2ANGLE, ID2Angle.class );
    fieldInfos().add( new TinFieldInfo( FID_RADIANS, TTI_AT_FLOATING, //
        TSID_NAME, STR_D2ANGLE_RADIANS, //
        TSID_DESCRIPTION, STR_D2ANGLE_RADIANS_D, //
        TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_DEGREES, TTI_AT_FLOATING, //
        TSID_NAME, STR_D2ANGLE_DEGREES, //
        TSID_DESCRIPTION, STR_D2ANGLE_DEGREES_D, //
        TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
    ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( dataType().params().getValobj( TSID_DEFAULT_VALUE ) );
  }

  @Override
  protected ITinValue doGetTinValue( ID2Angle aEntity ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();

    values.put( FID_DEGREES, TinValue.ofAtomic( avFloat( aEntity.degrees() ) ) );
    values.put( FID_RADIANS, TinValue.ofAtomic( avFloat( aEntity.radians() ) ) );

    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  protected ITinValue doApplyFieldChange( IStringMapEdit<ITinValue> aCurrValues, String aFieldId,
      ITinValue aChildFieldNewValue ) {
    D2Angle angle;
    switch( aFieldId ) {
      case FID_DEGREES: {
        double degrees = aChildFieldNewValue.atomicValue().asDouble();
        angle = D2Angle.ofDegrees( degrees );
        break;
      }
      case FID_RADIANS: {
        double radians = aChildFieldNewValue.atomicValue().asDouble();
        angle = D2Angle.ofRadians( radians );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aFieldId );
    }
    aCurrValues.put( aFieldId, aChildFieldNewValue );
    return TinValue.ofFull( avValobj( angle ), aCurrValues );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    IAtomicValue defVal = dataType().params().getValue( TSID_DEFAULT_VALUE );
    double degrees = extractChildAtomic( FID_DEGREES, aChildValues, defVal ).asDouble();
    double radians = extractChildAtomic( FID_RADIANS, aChildValues, defVal ).asDouble();
    /**
     * FIXME - here is the problem - which one to choose?
     */

    // DEBUG ---
    TsTestUtils.pl( "\n\n===deg = %.4f, rad = %.4f\n", degrees, radians );
    // ---

    return avValobj( D2Angle.ofDegrees( degrees ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    IAtomicValue defVal = dataType().params().getValue( TSID_DEFAULT_VALUE );
    D2Angle d2a = aValue != null ? aValue.asValobj() : defVal.asValobj();
    aChildValues.put( FID_DEGREES, TinValue.ofAtomic( avFloat( d2a.degrees() ) ) );
    aChildValues.put( FID_RADIANS, TinValue.ofAtomic( avFloat( d2a.radians() ) ) );
  }

}
