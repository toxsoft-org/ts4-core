package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
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

/**
 * Стандартный отрисовщик фона прибора.
 * <p>
 *
 * @author vs
 */
public class StdGaugeBkgRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdGaugeBkgRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "gaugeBackgroundRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_GAUGE_BKG_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Фон", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  /**
   * Возвращает тип данных для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - тип данных для инспектора свойств.
   */
  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_GAUGE_BKG_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  public static final String PROPID_FRAME_RADIUS_SCALE = "frameRScale";
  public static final String PROPID_INNER_RADIUS_SCALE = "innerRScale";
  public static final String PROPID_FRAME_FILL         = "frameFill";

  static final ITinFieldInfo TFI_R1_SCALE = TtiUtils.doubleFieldInfo( PROPID_FRAME_RADIUS_SCALE, //
      "R1", "Доля от общего радиуса датчика", 0.95 );

  static final ITinFieldInfo TFI_R2_SCALE = TtiUtils.doubleFieldInfo( PROPID_INNER_RADIUS_SCALE, //
      "R2", "Доля от общего радиуса датчика", 0.85 );

  static final ITinFieldInfo TFI_FRAME_FILL = TtiUtils.typedFieldInfo( PROPID_FRAME_FILL, //
      TFI_BK_FILL.typeInfo(), PROPID_FRAME_FILL, FACTORY_ID );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractCircularRendererFactory( FACTORY_ID, //
      TSID_NAME, "Renderer", //
      TSID_DESCRIPTION, "Standard gauge arrow renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields ) {
      fields.add( TFI_R1_SCALE );
      fields.add( TFI_R2_SCALE );
      fields.add( TFI_FRAME_FILL );
      fields.add( TFI_BK_FILL );
    }

    // @Override
    // protected ITinTypeInfo doCreateTypeInfo() {
    // IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    //
    // fields.add( TFI_R1_SCALE );
    // fields.add( TFI_R2_SCALE );
    // fields.add( TFI_FRAME_FILL );
    // fields.add( TFI_BK_FILL );
    //
    // // ----------------------------------------------------------------------------
    // // Скрытые поля, значения которых устанавливаются извне
    // //
    // fields.add( TtiUtils.createHidden( TFI_OWNER_RADIUS ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_X ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_Y ) );
    //
    // return new PropertableEntitiesTinTypeInfo<>( fields, AbstractViselRenderer.class );
    // }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();

      opSet.setDouble( PROPID_FRAME_RADIUS_SCALE, 0.95 );
      opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.90 );

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
      return new StdGaugeBkgRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();

    opSet.setDouble( PROPID_FRAME_RADIUS_SCALE, 0.95 );
    opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.90 );

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

    return new ViselRendererCfg( "stdBkgCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  ViselRendererCfg rendererCfg = null;

  double radius = 100;

  double initialLength = 100;

  double frameRadiusScale = 0.95;
  double innerRadiusScale = 0.90;

  double ownerRadius = 100;

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
  public StdGaugeBkgRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
    aVisel.props().setBool( PROPID_IS_ASPECT_FIXED, true );
    aVisel.props().setDouble( PROPID_ASPECT_RATIO, 1.0 );
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
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    if( aChangedValues.hasKey( PROPID_FRAME_RADIUS_SCALE ) ) {
      frameRadiusScale = aChangedValues.getDouble( PROPID_FRAME_RADIUS_SCALE );
    }
    if( aChangedValues.hasKey( PROPID_INNER_RADIUS_SCALE ) ) {
      innerRadiusScale = aChangedValues.getDouble( PROPID_INNER_RADIUS_SCALE );
    }
    if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
      innerFillInfo = aChangedValues.getValobj( PROPID_BK_FILL );
    }
    if( aChangedValues.hasKey( PROPID_FRAME_FILL ) ) {
      frameFillInfo = aChangedValues.getValobj( PROPID_FRAME_FILL );
    }
    if( aChangedValues.hasKey( PROPID_OWNER_RADIUS ) ) {
      ownerRadius = aChangedValues.getDouble( PROPID_OWNER_RADIUS );
    }
    updateCachedValues();
  }

  // @Override
  // public void setPropValues( IOptionSet aProps ) {
  // // if( aProps.hasKey( ViselCircularGauge.PROPID_ARROW_RENDERER_CFG ) ) {
  // // rendererCfg = aProps.getValobj( ViselCircularGauge.PROPID_ARROW_RENDERER_CFG );
  // // updateCachedValues();
  // // }
  // // if( aProps.hasKey( PROPID_WIDTH ) ) {
  // // radius = aProps.getDouble( PROPID_WIDTH ) / 2.;
  // // }
  // }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {

    double viselX = props().getDouble( PROPID_OWNER_ANCHOR_X );
    double viselY = props().getDouble( PROPID_OWNER_ANCHOR_Y );

    double r = ownerRadius * frameRadiusScale;
    aPaintContext.setFillInfo( frameFillInfo );
    int x = (int)Math.round( viselX - r );
    int y = (int)Math.round( viselY - r );
    aPaintContext.fillOval( x, y, (int)(2 * r), (int)(2 * r) );

    r = ownerRadius * innerRadiusScale;
    aPaintContext.setFillInfo( innerFillInfo );
    x = (int)Math.round( viselX - r );
    y = (int)Math.round( viselY - r );
    aPaintContext.fillOval( x, y, (int)(2 * r), (int)(2 * r) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateCachedValues() {
    if( rendererCfg != null ) {
      // fillInfo = rendererCfg.propValues().getValobj( PROPID_BK_FILL );
      // lineInfo = rendererCfg.propValues().getValobj( PROPID_LINE_INFO );
    }
  }

}
