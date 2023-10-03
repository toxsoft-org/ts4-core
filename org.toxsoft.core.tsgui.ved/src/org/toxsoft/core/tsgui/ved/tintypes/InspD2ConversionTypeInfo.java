package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Информация о поле типа - {@link InspD2ConversionTypeInfo}
 *
 * @author vs
 */
public class InspD2ConversionTypeInfo
    extends AbstractTinTypeInfo<ID2Conversion> {

  private final String FID_ANGLE  = "convAngle";  //$NON-NLS-1$
  private final String FID_ZOOM   = "convZoom";   //$NON-NLS-1$
  private final String FID_ORIGIN = "convOrigin"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static InspD2ConversionTypeInfo INSTANCE = new InspD2ConversionTypeInfo();

  private InspD2ConversionTypeInfo() {
    super( ETinTypeKind.FULL, DT_D2CONVERSION, ID2Conversion.class );
    fieldInfos().add( new TinFieldInfo( FID_ANGLE, TTI_FLOATING, //
        TSID_NAME, "Угол", //
        TSID_DESCRIPTION, "Угол поворота" //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ZOOM, TTI_FLOATING, //
        TSID_NAME, "Масштаб", //
        TSID_DESCRIPTION, "Коеффициент уменьшения/увеличения" //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_ORIGIN, InspD2PointTypeInfo.INSP_TYPE_INFO, //
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
    values.put( FID_ORIGIN, InspD2PointTypeInfo.INSP_TYPE_INFO.makeValue( aEntity.origin() ) );
    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    double angle = extractChildAtomic( FID_ANGLE, aChildValues, avFloat( 0 ) ).asFloat();
    ID2Angle d2angle = D2Angle.ofDegrees( angle );
    double zoomFactor = extractChildAtomic( FID_ZOOM, aChildValues, avFloat( 0 ) ).asDouble();

    ID2Point origin = DT_D2POINT.defaultValue().asValobj();
    IAtomicValue originVal = TinTools.getValue( FID_ORIGIN, aChildValues );
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
    aChildValues.put( FID_ORIGIN, InspD2PointTypeInfo.INSP_TYPE_INFO.makeValue( origin ) );
  }
}
