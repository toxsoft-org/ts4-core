package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

public class RoundButtonRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".roundButtonRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "buttonRenderer"; //$NON-NLS-1$

  public static final String PROPID_FRAME_FILL = "frameFill";

  static final ITinFieldInfo TFI_FRAME_FILL = TtiUtils.typedFieldInfo( PROPID_FRAME_FILL, //
      TFI_BK_FILL.typeInfo(), PROPID_FRAME_FILL, FACTORY_ID );

  static final IDataType DT_STD_ROUND_BUTTON_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Отображение", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  /**
   * Возвращает тип данных для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - тип данных для инспектора свойств.
   */
  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_ROUND_BUTTON_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new VedAbstractRendererFactory( FACTORY_ID, //
      TSID_NAME, "Renderer", //
      TSID_DESCRIPTION, "Round button renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_TEXT );
      aFields.add( TFI_FONT );
      aFields.add( TFI_FRAME_FILL );
      aFields.add( TFI_BK_FILL );
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();

      // opSet.setDouble( PROPID_FRAME_RADIUS_SCALE, 0.95 );
      // opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.90 );

      IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
      fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 229, 229, 229, 255 ) ) );
      fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 242, 242, 242, 255 ) ) );
      LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
      TsFillInfo fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
      opSet.setValobj( PROPID_BK_FILL, fi );

      fractions = new ElemArrayList<>();
      fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 204, 204, 204, 255 ) ) );
      fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 229, 229, 229, 255 ) ) );
      lgi = new LinearGradientInfo( fractions, 90 );
      fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
      opSet.setValobj( PROPID_FRAME_FILL, fi );

      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      return new RoundButtonRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();

    // opSet.setDouble( PROPID_FRAME_RADIUS_SCALE, 0.95 );
    // opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.90 );

    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 229, 229, 229, 255 ) ) );
    fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 242, 242, 242, 255 ) ) );
    LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    TsFillInfo fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    opSet.setValobj( PROPID_BK_FILL, fi );

    fractions = new ElemArrayList<>();
    fractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 204, 204, 204, 255 ) ) );
    fractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 229, 229, 229, 255 ) ) );
    lgi = new LinearGradientInfo( fractions, 90 );
    fi = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    opSet.setValobj( PROPID_FRAME_FILL, fi );

    return new ViselRendererCfg( "rbrCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsFillInfo frameFillInfo = TsFillInfo.NONE;
  TsFillInfo innerFillInfo = TsFillInfo.NONE;

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of data definitions
   * @param aCfg {@link ViselRendererCfg} - visel configeration
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public RoundButtonRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
  }

  @Override
  public String kindId() {
    return KIND_ID;
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFieldInfoes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
      innerFillInfo = aChangedValues.getValobj( PROPID_BK_FILL );
    }
    if( aChangedValues.hasKey( PROPID_FRAME_FILL ) ) {
      frameFillInfo = aChangedValues.getValobj( PROPID_FRAME_FILL );
    }
  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {
    double w = props().getDouble( PROPID_WIDTH );
    double h = props().getDouble( PROPID_HEIGHT );
    double doubleR = Math.min( w, h );

    aPaintContext.setFillInfo( frameFillInfo );
    aPaintContext.fillOval( 0, 0, (int)doubleR, (int)doubleR );

    aPaintContext.setFillInfo( innerFillInfo );
    aPaintContext.fillOval( 4, 4, (int)doubleR - 8, (int)doubleR - 8 );

  }

}
