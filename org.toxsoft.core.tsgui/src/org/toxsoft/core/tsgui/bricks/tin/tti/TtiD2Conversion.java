package org.toxsoft.core.tsgui.bricks.tin.tti;

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
    fieldInfos().add( new TinFieldInfo( FID_ANGLE, TTI_AT_FLOATING, //
        TSID_NAME, "Угол", //
        TSID_DESCRIPTION, "Угол поворота" //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ZOOM, TTI_AT_FLOATING, //
        TSID_NAME, "Масштаб", //
        TSID_DESCRIPTION, "Коеффициент уменьшения/увеличения" //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ORIGIN, TtiD2Point.INSTANCE, //
        TSID_NAME, "Точка опоры", //
        TSID_DESCRIPTION, "Точка вокруг которой осуществляется поврот и масштабирование" ) );
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
    double angle = extractChildAtomic( FID_ANGLE, aChildValues, avFloat( 0 ) ).asFloat();
    ID2Angle d2angle = D2Angle.ofDegrees( angle );
    double zoomFactor = extractChildAtomic( FID_ZOOM, aChildValues, avFloat( 0 ) ).asDouble();
    ID2Point origin = DT_D2POINT.defaultValue().asValobj();
    IAtomicValue originVal = aChildValues.getByKey( FID_ORIGIN ).atomicValue();
    if( originVal.isAssigned() ) {
      origin = originVal.asValobj();
    }
    return avValobj( new D2Conversion( d2angle, zoomFactor, origin ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    ID2Conversion d2conv = aValue.asValobj();
    double angle = d2conv.rotation().degrees();
    double zoomFactor = d2conv.zoomFactor();
    ID2Point origin = d2conv.origin();
    aChildValues.put( FID_ANGLE, TinValue.ofAtomic( avFloat( angle ) ) );
    aChildValues.put( FID_ZOOM, TinValue.ofAtomic( avFloat( zoomFactor ) ) );
    aChildValues.put( FID_ORIGIN, TtiD2Point.INSTANCE.makeValue( origin ) );
  }
}
