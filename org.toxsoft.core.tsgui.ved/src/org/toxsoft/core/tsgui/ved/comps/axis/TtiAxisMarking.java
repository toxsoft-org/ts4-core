package org.toxsoft.core.tsgui.ved.comps.axis;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link ITinTypeInfo} implementation for {@link AxisMarking} entities.
 *
 * @author vs
 */
public class TtiAxisMarking
    extends AbstractTinTypeInfo<AxisMarking> {

  private static final String FID_BIG_TICK_QTTY   = "btQtty";   //$NON-NLS-1$
  private static final String FID_BIG_TICK_NUMBER = "btNumber"; //$NON-NLS-1$
  private static final String FID_LIT_TICK_NUMBER = "ltNumber"; //$NON-NLS-1$
  private static final String FID_MID_TICK_NUMBER = "mtNumber"; //$NON-NLS-1$

  public static final IDataType DT_AXIS_MARKING = DataType.create( VALOBJ, //
      TSID_NAME, "Разметка шкалы", //
      TSID_DESCRIPTION, "Разметка шкалы - информация о засечках", //
      TSID_KEEPER_ID, AxisMarking.KEEPER_ID, //
      // OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgb.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( AxisMarking.DEFAULT ) //
  );

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new TtiAxisMarking();

  private TtiAxisMarking() {
    super( ETinTypeKind.FULL, DT_AXIS_MARKING, AxisMarking.class );
    fieldInfos().add( TtiUtils.intFieldInfo( FID_BIG_TICK_QTTY, "Big tick count", "Big tick count" ) );
    fieldInfos().add( TtiUtils.intFieldInfo( FID_BIG_TICK_NUMBER, "Big tick number", "Big tick number" ) );
    fieldInfos().add( TtiUtils.intFieldInfo( FID_LIT_TICK_NUMBER, "Little tick number", "Little tick number" ) );
    fieldInfos().add( TtiUtils.intFieldInfo( FID_MID_TICK_NUMBER, "Middle tick number", "Middle tick number" ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    int btQtty = extractChildInt( FID_BIG_TICK_QTTY, aChildValues, 5 );
    int btNumber = extractChildInt( FID_BIG_TICK_NUMBER, aChildValues, 10 );
    int ltNumber = extractChildInt( FID_LIT_TICK_NUMBER, aChildValues, 1 );
    int mdNumber = extractChildInt( FID_MID_TICK_NUMBER, aChildValues, 5 );
    AxisMarking am = new AxisMarking( btQtty, btNumber, ltNumber, mdNumber );
    return avValobj( am );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    AxisMarking am = aValue != null ? aValue.asValobj() : AxisMarking.DEFAULT;
    aChildValues.put( FID_BIG_TICK_QTTY, TinValue.ofAtomic( avInt( am.bigTickQtty ) ) );
    aChildValues.put( FID_BIG_TICK_NUMBER, TinValue.ofAtomic( avInt( am.bigTickNumber ) ) );
    aChildValues.put( FID_LIT_TICK_NUMBER, TinValue.ofAtomic( avInt( am.litTickNumber ) ) );
    aChildValues.put( FID_MID_TICK_NUMBER, TinValue.ofAtomic( avInt( am.midTickNumber ) ) );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( AxisMarking.DEFAULT );
  }

  @Override
  protected ITinValue doGetTinValue( AxisMarking aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    IStringMap<ITinValue> cv = INSTANCE.decompose( av );
    return TinValue.ofFull( av, cv );
  }

  @Override
  protected AxisMarking doCreateEntity( ITinValue aValue ) {
    return aValue.atomicValue().asValobj();
  }

}
