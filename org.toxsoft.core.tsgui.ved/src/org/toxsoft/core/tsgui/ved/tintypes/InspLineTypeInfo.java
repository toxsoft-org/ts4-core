package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Информация о поле инспектора для {@link TsLineInfo}.
 *
 * @author vs
 */
public class InspLineTypeInfo
    extends AbstractTinTypeInfo<TsLineInfo> {

  private final static String FID_WIDTH      = "lWidth";     //$NON-NLS-1$
  private final static String FID_LINE_STYLE = "lLineStyle"; //$NON-NLS-1$
  private final static String FID_CAP_STYLE  = "lCapStyle";  //$NON-NLS-1$
  private final static String FID_JOIN_STYLE = "lJoinStyle"; //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static InspLineTypeInfo INSTANCE = new InspLineTypeInfo();

  private InspLineTypeInfo() {
    super( ETinTypeKind.FULL, DT_LINE_INFO, TsLineInfo.class );

    fieldInfos().add( new TinFieldInfo( FID_WIDTH, TTI_INTEGER, //
        TSID_NAME, STR_N_LINE_THICK, //
        TSID_DESCRIPTION, STR_D_LINE_THICK //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_LINE_STYLE, InspEnumTypeInfo.INSP_TYPE_INFO, //
        TSID_NAME, STR_N_LINE_STYLE, //
        TSID_DESCRIPTION, STR_D_LINE_STYLE, //
        TSID_KEEPER_ID, ETsLineType.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_CAP_STYLE, InspEnumTypeInfo.INSP_TYPE_INFO, //
        TSID_NAME, STR_N_CAP_STYLE, //
        TSID_DESCRIPTION, STR_D_CAP_STYLE, //
        TSID_KEEPER_ID, ETsLineCapStyle.KEEPER_ID //
    ) );

    fieldInfos().add( new TinFieldInfo( FID_JOIN_STYLE, InspEnumTypeInfo.INSP_TYPE_INFO, //
        TSID_NAME, STR_N_JOIN_STYLE, //
        TSID_DESCRIPTION, STR_D_JOIN_STYLE, //
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
    int width = TinTools.getValue( FID_WIDTH, aChildValues, avInt( 1 ) ).asInt();

    ETsLineType lineType = TinTools.getValue( FID_LINE_STYLE, aChildValues, avValobj( ETsLineType.SOLID ) ).asValobj();
    ETsLineCapStyle capStyle;
    capStyle = TinTools.getValue( FID_CAP_STYLE, aChildValues, avValobj( ETsLineCapStyle.ROUND ) ).asValobj();
    ETsLineJoinStyle joinStyle;
    joinStyle = TinTools.getValue( FID_JOIN_STYLE, aChildValues, avValobj( ETsLineJoinStyle.ROUND ) ).asValobj();

    return avValobj( new TsLineInfo( width, lineType, capStyle, joinStyle, IIntList.EMPTY ) );
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
