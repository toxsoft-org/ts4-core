package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.comps.render.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * /** Visel отображающий значение в заданном диапазоне в виде "дуги".
 *
 * @author vs
 */
public class ViselRoundGauge
    extends VedAbstractVisel {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.RoundGauge"; //$NON-NLS-1$

  public static final String PROPID_START_ANGLE = "startAngle"; //$NON-NLS-1$
  public static final String PROPID_DELTA_ANGLE = "deltaAngle"; //$NON-NLS-1$
  public static final String PROPID_VALUE       = "value";      //$NON-NLS-1$
  public static final String PROPID_MIN_VALUE   = "minValue";   //$NON-NLS-1$
  public static final String PROPID_MAX_VALUE   = "maxValue";   //$NON-NLS-1$

  /**
   * Start angle
   */
  public static final IDataDef PROP_START_ANGLE = DataDef.create3( PROPID_START_ANGLE, DT_FLOATING, //
      TSID_NAME, "Начальный угол", //
      TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 0 ) );

  /**
   * End angle
   */
  public static final IDataDef PROP_DELTA_ANGLE = DataDef.create3( PROPID_DELTA_ANGLE, DT_FLOATING, //
      TSID_NAME, "Приращение угла", //
      TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 90 ) );

  public static final String PROPID_RENDERER_CFG = "rendererCfg"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_VALUE = TtiUtils.doubleFieldInfo( PROPID_VALUE, //
      "Value", "Current value", 50. );

  static final ITinFieldInfo TFI_MIN_VALUE = TtiUtils.doubleFieldInfo( PROPID_MIN_VALUE, //
      "Minimum", "Minimal value", 0. );

  static final ITinFieldInfo TFI_MAX_VALUE = TtiUtils.doubleFieldInfo( PROPID_MAX_VALUE, //
      "Maximum", "Maximal value", 100. );

  static final ITinFieldInfo TFI_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_RENDERER_CFG, StdRoundGaugeRenderer.tinTypeInfo(), //
          "Rendering properties", "Properties of visual representation of the round indicator" );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "Круговой индикатор", //
      TSID_DESCRIPTION, "Круговой индикатор", //
      TSID_ICON_ID, ICONID_VISEL_ROUND_AXIS ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselRoundGauge( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );

      fields.add( new TinFieldInfo( PROPID_START_ANGLE, TTI_AT_FLOATING, PROP_START_ANGLE.params() ) );
      fields.add( new TinFieldInfo( PROPID_DELTA_ANGLE, TTI_AT_FLOATING, PROP_DELTA_ANGLE.params() ) );

      fields.add( TFI_MIN_VALUE );
      fields.add( TFI_MAX_VALUE );
      fields.add( TFI_VALUE );

      fields.add( TFI_RENDERER_CFG );

      fields.add( TFI_IS_ASPECT_FIXED_HIDDEN );
      fields.add( TFI_ASPECT_RATIO_HIDDEN );
      fields.add( TFI_ZOOM_HIDDEN );
      fields.add( TFI_ANGLE_HIDDEN );
      fields.add( TFI_TRANSFORM_HIDDEN );
      fields.add( TFI_IS_ACTIVE_HIDDEN );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRoundGauge.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setBool( PROPID_IS_ASPECT_FIXED, true );
      cfg.propValues().setDouble( PROPID_ASPECT_RATIO, 1. );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  double startAngle = 0;
  double deltaAngle = 90;

  double minValue = 0.0;
  double maxValue = 100.0;
  double value    = 50.0;

  AbstractViselRenderer renderer;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRoundGauge( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aNewValues.hasKey( PROPID_RENDERER_CFG ) ) {
      ViselRendererCfg cfg = aNewValues.getValobj( PROPID_RENDERER_CFG );
      if( cfg.viselId() == null || cfg.viselId().isBlank() || cfg.viselId().equals( "none" ) ) {
        cfg = new ViselRendererCfg( cfg.id(), cfg.kindId(), cfg.factoryId(), cfg.propValues(), id() );
        aValuesToSet.setValobj( PROPID_RENDERER_CFG, cfg );
      }
    }
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_RENDERER_CFG ) ) { // обновим отрисовщик
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        renderer = factory.create( cfg, this, vedScreen() );
      }
    }

    if( aChangedValue.hasKey( PROPID_START_ANGLE ) ) {
      startAngle = aChangedValue.getDouble( PROPID_START_ANGLE );
    }
    if( aChangedValue.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngle = aChangedValue.getDouble( PROPID_DELTA_ANGLE );
    }
    if( aChangedValue.hasKey( PROPID_MIN_VALUE ) ) {
      minValue = aChangedValue.getDouble( PROPID_MIN_VALUE );
    }
    if( aChangedValue.hasKey( PROPID_MAX_VALUE ) ) {
      maxValue = aChangedValue.getDouble( PROPID_MAX_VALUE );
    }
    if( aChangedValue.hasKey( PROPID_VALUE ) ) {
      value = aChangedValue.getDouble( PROPID_VALUE );
    }
    updateRenderer();
    // renderer.setPropValues( aChangedValue );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    renderer.paint( aPaintContext );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateRenderer() {
    renderer.props().setDouble( PROPID_MIN_VALUE, minValue );
    renderer.props().setDouble( PROPID_MAX_VALUE, maxValue );
    renderer.props().setDouble( PROPID_VALUE, value );
    renderer.props().setDouble( PROPID_START_ANGLE, startAngle );
    renderer.props().setDouble( PROPID_DELTA_ANGLE, deltaAngle );
  }
}
