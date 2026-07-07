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
public class StdGaugeKnobRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdGaugeKnobRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "gaugeKnobRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_GAUGE_BKG_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Крепление стрелки", //
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

  static final String PROPID_OUTER_RADIUS_SCALE = "outerRScale"; //$NON-NLS-1$
  static final String PROPID_INNER_RADIUS_SCALE = "innerRScale"; //$NON-NLS-1$
  static final String PROPID_FRAME_FILL         = "frameFill";   //$NON-NLS-1$

  static final ITinFieldInfo TFI_OUTER_SCALE = TtiUtils.doubleFieldInfo( PROPID_OUTER_RADIUS_SCALE, //
      "Внешний радиус", "Доля от общего радиуса датчика", 0.1 );

  static final ITinFieldInfo TFI_INNER_SCALE = TtiUtils.doubleFieldInfo( PROPID_INNER_RADIUS_SCALE, //
      "Внутренний радиус", "Доля от общего радиуса датчика", 0.06 );

  static final ITinFieldInfo TFI_FRAME_FILL = TtiUtils.typedFieldInfo( PROPID_FRAME_FILL, //
      TFI_BK_FILL.typeInfo(), PROPID_FRAME_FILL, FACTORY_ID );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractCircularRendererFactory( FACTORY_ID, //
      TSID_NAME, "Knob renderer", //
      TSID_DESCRIPTION, "Standard gauge knob renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFfields ) {
      aFfields.add( TFI_OUTER_SCALE );
      aFfields.add( TFI_INNER_SCALE );
      aFfields.add( TFI_FRAME_FILL );
      aFfields.add( TFI_BK_FILL );
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();

      opSet.setDouble( PROPID_OUTER_RADIUS_SCALE, 0.4 );
      opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.3 );

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
      return new StdGaugeKnobRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();

    opSet.setDouble( PROPID_OUTER_RADIUS_SCALE, 0.1 );
    opSet.setDouble( PROPID_INNER_RADIUS_SCALE, 0.06 );

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

    return new ViselRendererCfg( "stdKnobCfg", KIND_ID, FACTORY_ID, opSet, aViselId ); //$NON-NLS-1$
  }

  ViselRendererCfg rendererCfg = null;

  double radius = 100;

  double initialLength = 100;

  double frameRadiusScale = 0.1;
  double innerRadiusScale = 0.06;

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
  public StdGaugeKnobRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
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
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    if( aChangedValues.hasKey( PROPID_OUTER_RADIUS_SCALE ) ) {
      frameRadiusScale = aChangedValues.getDouble( PROPID_OUTER_RADIUS_SCALE );
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
    // updateCachedValues();
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {

    double r = ownerRadius * frameRadiusScale;
    aPaintContext.setFillInfo( frameFillInfo );
    int x = Math.round( (int)(ownerRadius - r) );
    int y = Math.round( (int)(ownerRadius - r) );
    aPaintContext.fillOval( x, y, (int)(2 * r), (int)(2 * r) );

    r = ownerRadius * innerRadiusScale;
    aPaintContext.setFillInfo( innerFillInfo );
    x = Math.round( (int)(ownerRadius - r) );
    y = Math.round( (int)(ownerRadius - r) );
    aPaintContext.fillOval( x, y, (int)(2 * r), (int)(2 * r) );
  }

}
