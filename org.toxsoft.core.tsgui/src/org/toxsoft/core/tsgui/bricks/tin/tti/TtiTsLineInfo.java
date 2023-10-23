package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Информация о поле инспектора для {@link TtiTsLineInfo}.
 *
 * @author vs
 */
public class TtiTsLineInfo
    extends AbstractTinTypeInfo<TsLineInfo> {

  private final static String FID_WIDTH      = "width";     //$NON-NLS-1$
  private final static String FID_LINE_STYLE = "style";     //$NON-NLS-1$
  private final static String FID_CAP_STYLE  = "capStyle";  //$NON-NLS-1$
  private final static String FID_JOIN_STYLE = "joinStyle"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final TtiTsLineInfo INSTANCE = new TtiTsLineInfo();

  private TtiTsLineInfo() {
    super( ETinTypeKind.FULL, DT_TS_LINE_INFO, TsLineInfo.class );

    fieldInfos().add( new TinFieldInfo( FID_WIDTH, TTI_AT_INTEGER, //
        TSID_NAME, STR_LINE_THICK, //
        TSID_DESCRIPTION, STR_LINE_THICK_D //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_LINE_STYLE, TtiAvEnum.INSTANCE, //
        TSID_NAME, STR_LINE_STYLE, //
        TSID_DESCRIPTION, STR_LINE_STYLE_D, //
        TSID_KEEPER_ID, ETsLineType.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_CAP_STYLE, TtiAvEnum.INSTANCE, //
        TSID_NAME, STR_LINE_CAP_STYLE, //
        TSID_DESCRIPTION, STR_LINE_CAP_STYLE_D, //
        TSID_KEEPER_ID, ETsLineCapStyle.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_JOIN_STYLE, TtiAvEnum.INSTANCE, //
        TSID_NAME, STR_LINE_JOIN_STYLE, //
        TSID_DESCRIPTION, STR_LINE_JOIN_STYLE_D, //
        TSID_KEEPER_ID, ETsLineJoinStyle.KEEPER_ID //
    ) );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( TsLineInfo.ofWidth( 1 ) );
  }

  @Override
  protected ITinValue doGetTinValue( TsLineInfo aEntity ) {
    IStringMapEdit<ITinValue> childValues = new StringMap<>();
    childValues.put( FID_WIDTH, TinValue.ofAtomic( avInt( aEntity.width() ) ) );
    childValues.put( FID_LINE_STYLE, TinValue.ofAtomic( avValobj( aEntity.type() ) ) );
    childValues.put( FID_CAP_STYLE, TinValue.ofAtomic( avValobj( aEntity.capStyle() ) ) );
    childValues.put( FID_JOIN_STYLE, TinValue.ofAtomic( avValobj( aEntity.joinStyle() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), childValues );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    int width = extractChildAtomic( FID_WIDTH, aChildValues, avInt( 1 ) ).asInt();
    ETsLineType lineType = extractChildValobj( FID_LINE_STYLE, aChildValues, avValobj( ETsLineType.SOLID ) ).asValobj();
    ETsLineCapStyle cap =
        extractChildValobj( FID_CAP_STYLE, aChildValues, avValobj( ETsLineCapStyle.ROUND ) ).asValobj();
    ETsLineJoinStyle join =
        extractChildValobj( FID_JOIN_STYLE, aChildValues, avValobj( ETsLineJoinStyle.ROUND ) ).asValobj();
    return avValobj( new TsLineInfo( width, lineType, cap, join, IIntList.EMPTY ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    TsLineInfo li = aValue.asValobj();
    aChildValues.put( FID_WIDTH, TinValue.ofAtomic( avInt( li.width() ) ) );
    aChildValues.put( FID_LINE_STYLE, TinValue.ofAtomic( avValobj( li.type() ) ) );
    aChildValues.put( FID_CAP_STYLE, TinValue.ofAtomic( avValobj( li.capStyle() ) ) );
    aChildValues.put( FID_JOIN_STYLE, TinValue.ofAtomic( avValobj( li.joinStyle() ) ) );
  }

}
