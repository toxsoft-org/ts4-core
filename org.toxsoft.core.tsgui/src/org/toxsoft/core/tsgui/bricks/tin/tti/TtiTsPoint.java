package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link ITinTypeInfo} implementation for {@link ITsPoint} entities.
 *
 * @author vs
 */
public class TtiTsPoint
    extends AbstractTinTypeInfo<ITsPoint> {

  private static final String FID_X = "tspX"; //$NON-NLS-1$
  private static final String FID_Y = "tspY"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new TtiTsPoint();

  private TtiTsPoint() {
    super( ETinTypeKind.FULL, DT_TSPOINT, ITsPoint.class );
    fieldInfos().add( new TinFieldInfo( FID_X, TTI_AT_INTEGER, //
        TSID_NAME, STR_TSPOINT_X, //
        TSID_DESCRIPTION, STR_TSPOINT_X_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_Y, TTI_AT_INTEGER, //
        TSID_NAME, STR_TSPOINT_Y, //
        TSID_DESCRIPTION, STR_TSPOINT_Y_D //
    ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( dataType().params().getValobj( TSID_DEFAULT_VALUE ) );
  }

  @Override
  protected ITinValue doGetTinValue( ITsPoint aEntity ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    values.put( FID_X, TinValue.ofAtomic( avInt( aEntity.x() ) ) );
    values.put( FID_Y, TinValue.ofAtomic( avInt( aEntity.y() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), values );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    int x = extractChildAtomic( FID_X, aChildValues, avInt( 0 ) ).asInt();
    int y = extractChildAtomic( FID_Y, aChildValues, avInt( 0 ) ).asInt();
    return avValobj( new TsPoint( x, y ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    ITsPoint p = aValue != null ? aValue.asValobj() : dataType().params().getValobj( TSID_DEFAULT_VALUE );
    aChildValues.put( FID_X, TinValue.ofAtomic( avInt( p.x() ) ) );
    aChildValues.put( FID_Y, TinValue.ofAtomic( avInt( p.y() ) ) );
  }

}
