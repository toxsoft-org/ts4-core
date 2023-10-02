package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tsgui.ved.tintypes.InspFieldTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.helpers.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Информация о поле инспектора для {@link TsBorderInfo}.
 *
 * @author vs
 */
public class InspBorderTypeInfo
    extends AbstractTinTypeInfo<TsBorderInfo> {

  private static final String FID_BORDER_KIND  = "kind";            //$NON-NLS-1$
  private static final String FID_LEFT_RGBA    = "leftTopRgba";     //$NON-NLS-1$
  private static final String FID_RIGHT_RGBA   = "rightBottomRgba"; //$NON-NLS-1$
  private static final String FID_LINE_INFO    = "lineInfo";        //$NON-NLS-1$
  private static final String FID_PAINT_LEFT   = "paintLeft";       //$NON-NLS-1$
  private static final String FID_PAINT_TOP    = "paintTop";        //$NON-NLS-1$
  private static final String FID_PAINT_RIGHT  = "paintRight";      //$NON-NLS-1$
  private static final String FID_PAINT_BOTTOM = "paintBottom";     //$NON-NLS-1$

  /**
   * The type information singleton.
   */
  public static final ITinTypeInfo INSTANCE = new InspBorderTypeInfo();

  void addField( String aId, ITinTypeInfo aTinType, IStridablesList<IDataDef> aDefs ) {
    IDataDef dataDef = aDefs.getByKey( aId );
    IOptionSetEdit opSet = new OptionSet();
    if( dataDef.params().hasKey( TSID_NAME ) ) {
      opSet.setValue( TSID_NAME, dataDef.params().getValue( TSID_NAME ) );
    }
    if( dataDef.params().hasKey( TSID_DESCRIPTION ) ) {
      opSet.setValue( TSID_DESCRIPTION, dataDef.params().getValue( TSID_DESCRIPTION ) );
    }
    if( dataDef.params().hasKey( TSID_KEEPER_ID ) ) {
      opSet.setValue( TSID_KEEPER_ID, dataDef.params().getValue( TSID_KEEPER_ID ) );
    }
    if( dataDef.params().hasKey( OPID_EDITOR_FACTORY_NAME ) ) {
      opSet.setValue( OPID_EDITOR_FACTORY_NAME, dataDef.params().getValue( OPID_EDITOR_FACTORY_NAME ) );
    }
    if( dataDef.params().hasKey( TSID_DEFAULT_VALUE ) ) {
      opSet.setValue( TSID_DEFAULT_VALUE, dataDef.params().getValue( TSID_DEFAULT_VALUE ) );
    }

    fieldInfos().add( new TinFieldInfo( aId, aTinType, opSet ) );
  }

  private InspBorderTypeInfo() {
    super( ETinTypeKind.FULL, DT_BORDER_INFO, TsBorderInfo.class );

    IStridablesList<IDataDef> allDefs = TsBorderInfo.ALL_DEFS;

    addField( FID_BORDER_KIND, InspEnumTypeInfo.INSP_TYPE_INFO, allDefs );
    addField( FID_LEFT_RGBA, RGBATypeInfo.INSTANCE, allDefs );
    addField( FID_RIGHT_RGBA, RGBATypeInfo.INSTANCE, allDefs );
    addField( FID_LINE_INFO, InspLineTypeInfo.INSTANCE, allDefs );
    addField( FID_PAINT_LEFT, TTI_BOOLEAN, allDefs );
    addField( FID_PAINT_TOP, TTI_BOOLEAN, allDefs );
    addField( FID_PAINT_RIGHT, TTI_BOOLEAN, allDefs );
    addField( FID_PAINT_BOTTOM, TTI_BOOLEAN, allDefs );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    TsBorderInfo bordernfo = TsBorderInfo.ofSingle( new RGBA( 0, 0, 0, 255 ), new RGBA( 0, 0, 0, 255 ),
        TsLineInfo.ofWidth( 1 ), true, true, true, true );
    return doGetTinValue( bordernfo );
  }

  @Override
  protected ITinValue doGetTinValue( TsBorderInfo aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    return TinValue.ofFull( av, decompose( av ) );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    IAtomicValue kind = TinTools.getValue( FID_BORDER_KIND, aChildValues, avValobj( ETsBorderKind.SINGLE ) );
    IAtomicValue rgbaLeft = TinTools.getValue( FID_LEFT_RGBA, aChildValues, avValobj( new RGBA( 0, 0, 0, 255 ) ) );
    IAtomicValue rgbaRight = TinTools.getValue( FID_RIGHT_RGBA, aChildValues, avValobj( new RGBA( 0, 0, 0, 255 ) ) );
    IAtomicValue li = TinTools.getValue( FID_LINE_INFO, aChildValues, avValobj( TsLineInfo.ofWidth( 1 ) ) );
    IAtomicValue pl = TinTools.getValue( FID_PAINT_LEFT, aChildValues, avBool( true ) );
    IAtomicValue pt = TinTools.getValue( FID_PAINT_TOP, aChildValues, avBool( true ) );
    IAtomicValue pr = TinTools.getValue( FID_PAINT_RIGHT, aChildValues, avBool( true ) );
    IAtomicValue pb = TinTools.getValue( FID_PAINT_BOTTOM, aChildValues, avBool( true ) );

    IOptionSetEdit opSet = new OptionSet();
    opSet.setValue( "kind", kind ); //$NON-NLS-1$
    opSet.setValue( "leftTopRgba", rgbaLeft ); //$NON-NLS-1$
    opSet.setValue( "rightBottomRgba", rgbaRight ); //$NON-NLS-1$
    opSet.setValue( "lineInfo", li ); //$NON-NLS-1$
    opSet.setValue( "paintLeft", pl ); //$NON-NLS-1$
    opSet.setValue( "paintTop", pt ); //$NON-NLS-1$
    opSet.setValue( "paintRight", pr ); //$NON-NLS-1$
    opSet.setValue( "paintBottom", pb ); //$NON-NLS-1$
    TsBorderInfo bi = TsBorderInfo.ofOptions( opSet );

    return avValobj( bi );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    TsBorderInfo bi = TsBorderInfo.createSimpleBorder( 1, new RGBA( 0, 0, 0, 255 ) );
    if( aValue != null && aValue.isAssigned() ) {
      bi = aValue.asValobj();
    }
    aChildValues.put( FID_BORDER_KIND, TinValue.ofAtomic( avValobj( bi.kind() ) ) );
    aChildValues.put( FID_LEFT_RGBA, TinValue.ofAtomic( avValobj( bi.leftTopRGBA() ) ) );
    aChildValues.put( FID_RIGHT_RGBA, TinValue.ofAtomic( avValobj( bi.rightBottomRGBA() ) ) );
    aChildValues.put( FID_LINE_INFO, InspLineTypeInfo.INSTANCE.makeValue( bi.lineInfo() ) );
    aChildValues.put( FID_PAINT_LEFT, TinValue.ofAtomic( avBool( bi.shouldPaintLeft() ) ) );
    aChildValues.put( FID_PAINT_TOP, TinValue.ofAtomic( avBool( bi.shouldPaintTop() ) ) );
    aChildValues.put( FID_PAINT_RIGHT, TinValue.ofAtomic( avBool( bi.shouldPaintRight() ) ) );
    aChildValues.put( FID_PAINT_BOTTOM, TinValue.ofAtomic( avBool( bi.shouldPaintBottom() ) ) );
  }
}
