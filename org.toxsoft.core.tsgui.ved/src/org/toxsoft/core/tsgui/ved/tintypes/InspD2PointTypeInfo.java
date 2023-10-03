package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
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
 * Информация о поле типа - {@link InspD2PointTypeInfo}
 *
 * @author vs
 */
public class InspD2PointTypeInfo
    extends AbstractTinTypeInfo<ID2Point> {

  private static final String FID_X = "d2x"; //$NON-NLS-1$
  private static final String FID_Y = "d2y"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSP_TYPE_INFO = new InspD2PointTypeInfo();

  private InspD2PointTypeInfo() {
    super( ETinTypeKind.FULL, DT_D2POINT, ID2Point.class );
    fieldInfos().add( new TinFieldInfo( FID_X, TTI_FLOATING, //
        TSID_NAME, STR_N_POINT_X, //
        TSID_DESCRIPTION, STR_D_POINT_X //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_Y, TTI_FLOATING, //
        TSID_NAME, STR_N_POINT_Y, //
        TSID_DESCRIPTION, STR_D_POINT_Y //
    ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( dataType().params().getValobj( TSID_DEFAULT_VALUE ) );
  }

  @Override
  protected ITinValue doGetTinValue( ID2Point aEntity ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    values.put( FID_X, TinValue.ofAtomic( avFloat( aEntity.x() ) ) );
    values.put( FID_Y, TinValue.ofAtomic( avFloat( aEntity.y() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    double x = extractChildAtomic( FID_X, aChildValues, avFloat( 0 ) ).asDouble();
    double y = extractChildAtomic( FID_Y, aChildValues, avFloat( 0 ) ).asDouble();
    return avValobj( new D2Point( x, y ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    D2Point d2p = aValue != null ? aValue.asValobj() : dataType().params().getValobj( TSID_DEFAULT_VALUE );
    aChildValues.put( FID_X, TinValue.ofAtomic( avFloat( d2p.x() ) ) );
    aChildValues.put( FID_Y, TinValue.ofAtomic( avFloat( d2p.y() ) ) );
  }

}
