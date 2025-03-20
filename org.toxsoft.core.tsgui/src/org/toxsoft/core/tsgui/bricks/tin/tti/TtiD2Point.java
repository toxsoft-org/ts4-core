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
 * {@link ITinTypeInfo} implementation for {@link ID2Point} entities.
 *
 * @author vs
 */
public class TtiD2Point
    extends AbstractTinTypeInfo<ID2Point> {

  private static final String FID_X = "d2x"; //$NON-NLS-1$
  private static final String FID_Y = "d2y"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new TtiD2Point();

  private TtiD2Point() {
    super( ETinTypeKind.FULL, DT_D2POINT, ID2Point.class );
    fieldInfos().add( new TinFieldInfo( FID_X, TTI_AT_FLOATING, //
        TSID_NAME, STR_D2POINT_X, //
        TSID_DESCRIPTION, STR_D2POINT_X_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_Y, TTI_AT_FLOATING, //
        TSID_NAME, STR_D2POINT_Y, //
        TSID_DESCRIPTION, STR_D2POINT_Y_D //
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
    ID2Point d2p = aValue != null ? aValue.asValobj() : dataType().params().getValobj( TSID_DEFAULT_VALUE );
    aChildValues.put( FID_X, TinValue.ofAtomic( avFloat( d2p.x() ) ) );
    aChildValues.put( FID_Y, TinValue.ofAtomic( avFloat( d2p.y() ) ) );
  }

}
