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

/**
 * {@link ITinTypeInfo} implementation for {@link ID2Conversion} entities.
 *
 * @author vs
 */
public class TtiD2Conversion
    extends AbstractTinTypeInfo<ID2Conversion> {

  private final String FID_ANGLE  = "convAngle";  //$NON-NLS-1$
  private final String FID_ZOOM   = "convZoom";   //$NON-NLS-1$
  private final String FID_ORIGIN = "convOrigin"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final TtiD2Conversion INSTANCE = new TtiD2Conversion();

  private TtiD2Conversion() {
    super( ETinTypeKind.FULL, DT_D2CONVERSION, ID2Conversion.class );
    fieldInfos().add( new TinFieldInfo( FID_ANGLE, TtiD2Angle.INSTANCE, //
        TSID_NAME, STR_D2CONVERSION_ANGLE, //
        TSID_DESCRIPTION, STR_D2CONVERSION_ANGLE_D//
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ZOOM, TTI_AT_FLOATING, //
        TSID_NAME, STR_D2CONVERSION_ZOOM, //
        TSID_DESCRIPTION, STR_D2CONVERSION_ZOOM_D, //
        TSID_MIN_INCLUSIVE, avFloat( D2Utils.ZOOM_RANGE.minValue() ), //
        TSID_MAX_INCLUSIVE, avFloat( D2Utils.ZOOM_RANGE.maxValue() ), //
        TSID_DEFAULT_VALUE, AV_F_1 //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ORIGIN, TtiD2Point.INSTANCE, //
        TSID_NAME, STR_D2CONVERSION_ORIGIN, //
        TSID_DESCRIPTION, STR_D2CONVERSION_ORIGIN_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( DT_D2CONVERSION.defaultValue().asValobj() );
  }

  @Override
  protected ITinValue doGetTinValue( ID2Conversion aEntity ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    values.put( FID_ANGLE, TinValue.ofAtomic( avFloat( aEntity.rotation().degrees() ) ) );
    values.put( FID_ZOOM, TinValue.ofAtomic( avFloat( aEntity.zoomFactor() ) ) );
    values.put( FID_ORIGIN, TtiD2Point.INSTANCE.makeValue( aEntity.origin() ) );
    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    ID2Angle angle = extractChildValobj( FID_ANGLE, aChildValues, ID2Angle.ZERO );
    double zoomFactor = extractChildAtomic( FID_ZOOM, aChildValues, avFloat( 1.0 ) ).asDouble();
    ID2Point origin = DT_D2POINT.defaultValue().asValobj();
    IAtomicValue originVal = aChildValues.getByKey( FID_ORIGIN ).atomicValue();
    if( originVal.isAssigned() ) {
      origin = originVal.asValobj();
    }
    return avValobj( new D2Conversion( angle, zoomFactor, origin ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    ID2Conversion d2conv = aValue.asValobj();
    aChildValues.put( FID_ANGLE, TtiD2Angle.INSTANCE.makeValue( d2conv.rotation() ) );
    aChildValues.put( FID_ZOOM, TinValue.ofAtomic( avFloat( d2conv.zoomFactor() ) ) );
    aChildValues.put( FID_ORIGIN, TtiD2Point.INSTANCE.makeValue( d2conv.origin() ) );
  }
}
