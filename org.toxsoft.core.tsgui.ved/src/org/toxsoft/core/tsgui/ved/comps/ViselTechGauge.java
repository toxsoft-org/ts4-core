package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
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
 * Промышленный стрелочный датчик с круговой шкалой (например, манометр) .
 * <p>
 *
 * @author vs
 */
public class ViselTechGauge
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".circularGaugeViselFactory"; //$NON-NLS-1$

  static final String PROPID_START_ANGLE = "startAngle"; //$NON-NLS-1$
  static final String PROPID_DELTA_ANGLE = "deltaAngle"; //$NON-NLS-1$

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

  public static final String PROPID_BKG_RENDERER_CFG   = "bkgRendererCfg";   //$NON-NLS-1$
  public static final String PROPID_FRAME_RENDERER_CFG = "frameRendererCfg"; //$NON-NLS-1$
  public static final String PROPID_KNOB_RENDERER_CFG  = "knobRendererCfg";  //$NON-NLS-1$
  public static final String PROPID_AXIS_RENDERER_CFG  = "axisRendererCfg";  //$NON-NLS-1$
  public static final String PROPID_ARROW_RENDERER_CFG = "arrowRendererCfg"; //$NON-NLS-1$

  static final String PROPID_MIN_VALUE = "minValue"; //$NON-NLS-1$
  static final String PROPID_MAX_VALUE = "maxValue"; //$NON-NLS-1$
  static final String PROPID_VALUE     = "value";    //$NON-NLS-1$

  static final ITinFieldInfo TFI_BKG_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_BKG_RENDERER_CFG, StdGaugeBkgRenderer.tinTypeInfo(), //
          "Background", "Properties of visual representation of the background" );

  static final ITinFieldInfo TFI_AXIS_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_AXIS_RENDERER_CFG, StdRoundAxisRenderer.tinTypeInfo(), //
          "Axis", "Properties of visual representation of the axis" );

  static final ITinFieldInfo TFI_ARROW_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_ARROW_RENDERER_CFG, StdGaugeArrowRenderer.tinTypeInfo(), //
          "Arrow", "Properties of visual representation of the arrow" );

  static final ITinFieldInfo TFI_KNOB_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_KNOB_RENDERER_CFG, StdGaugeKnobRenderer.tinTypeInfo(), //
          "Knob", "Properties of visual representation of the knob" );

  static final ITinFieldInfo TFI_FRAME_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_FRAME_RENDERER_CFG, StdGaugeFrameRenderer.tinTypeInfo(), //
          "Frame", "Properties of visual representation of the frame" );

  static final ITinFieldInfo TFI_MIN_VALUE =
      TtiUtils.doubleFieldInfo( PROPID_MIN_VALUE, "Минимум", "Минимальное измеряемое значение", 0.0 );

  static final ITinFieldInfo TFI_MAX_VALUE =
      TtiUtils.doubleFieldInfo( PROPID_MAX_VALUE, "Максимум", "Максимальное измеряемое значение", 100.0 );

  static final ITinFieldInfo TFI_VALUE = //
      TtiUtils.doubleFieldInfo( PROPID_VALUE, "Значение", "Теущее значение", 50.0 );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "Датчик", //
      TSID_DESCRIPTION, "Датчик с круговой шкалой", //
      TSID_ICON_ID, ICONID_VISEL_CIRCULAR_GAUGE ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselTechGauge( aCfg, propDefs(), aVedScreen );
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

      fields.add( TFI_BKG_RENDERER_CFG );
      fields.add( TFI_AXIS_RENDERER_CFG );
      fields.add( TFI_ARROW_RENDERER_CFG );
      fields.add( TFI_KNOB_RENDERER_CFG );
      fields.add( TFI_FRAME_RENDERER_CFG );

      fields.add( TFI_IS_ASPECT_FIXED_HIDDEN );
      fields.add( TFI_ASPECT_RATIO_HIDDEN );
      fields.add( TFI_ZOOM_HIDDEN );
      fields.add( TFI_ANGLE_HIDDEN );
      fields.add( TFI_TRANSFORM_HIDDEN );
      fields.add( TFI_IS_ACTIVE_HIDDEN );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselTechGauge.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      // IOptionSetEdit options = new OptionSet();
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setBool( PROPID_IS_ASPECT_FIXED, true );
      cfg.propValues().setDouble( PROPID_ASPECT_RATIO, 1. );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  double minValue = 0.0;
  double maxValue = 100.0;
  double value    = 50.0;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselTechGauge( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // renderer = new StdRoundAxisRenderer( "stdRend", StdRoundAxisRenderer.FACTORY.propDefs(), //$NON-NLS-1$
    // aConfig.propValues().getValobj( PROPID_RENDERER_CFG ), this, aVedScreen.tsContext() );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // private double radius;

  double startAngle = 0;
  double deltaAngle = 90;
  RGBA   fgRgba;

  AbstractViselRenderer bkgRenderer;
  AbstractViselRenderer axisRenderer;
  AbstractViselRenderer arrowRenderer;
  AbstractViselRenderer knobRenderer;
  AbstractViselRenderer frameRenderer;

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aNewValues.hasKey( PROPID_AXIS_RENDERER_CFG ) ) {
      ViselRendererCfg cfg = aNewValues.getValobj( PROPID_AXIS_RENDERER_CFG );
      if( cfg.viselId() == null || cfg.viselId().isBlank() || cfg.viselId().equals( "none" ) ) {
        cfg = new ViselRendererCfg( cfg.id(), cfg.kindId(), cfg.factoryId(), cfg.propValues(), id() );
        aValuesToSet.setValobj( PROPID_AXIS_RENDERER_CFG, cfg );
      }
    }
    if( aNewValues.hasKey( PROPID_ARROW_RENDERER_CFG ) ) {
      ViselRendererCfg cfg = aNewValues.getValobj( PROPID_ARROW_RENDERER_CFG );
      if( cfg.viselId() == null || cfg.viselId().isBlank() || cfg.viselId().equals( "none" ) ) {
        cfg = new ViselRendererCfg( cfg.id(), cfg.kindId(), cfg.factoryId(), cfg.propValues(), id() );
        aValuesToSet.setValobj( PROPID_ARROW_RENDERER_CFG, cfg );
      }
    }
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );

    if( aChangedValue.hasKey( PROPID_MIN_VALUE ) ) {
      minValue = aChangedValue.getDouble( PROPID_MIN_VALUE );
      if( arrowRenderer != null ) {
        arrowRenderer.props().setDouble( PROPID_ARROW_ANGLE, calcArrowAngle() );
      }
    }
    if( aChangedValue.hasKey( PROPID_MAX_VALUE ) ) {
      maxValue = aChangedValue.getDouble( PROPID_MAX_VALUE );
      if( arrowRenderer != null ) {
        arrowRenderer.props().setDouble( PROPID_ARROW_ANGLE, calcArrowAngle() );
      }
    }
    if( aChangedValue.hasKey( PROPID_VALUE ) ) {
      value = aChangedValue.getDouble( PROPID_VALUE );
      if( arrowRenderer != null ) {
        arrowRenderer.props().setDouble( PROPID_ARROW_ANGLE, calcArrowAngle() );
      }
    }

    if( aChangedValue.hasKey( PROPID_BKG_RENDERER_CFG ) ) { // обновим отрисовщик
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_BKG_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        bkgRenderer = factory.create( cfg, this, vedScreen() );
        updateRenderer( bkgRenderer );
      }
    }

    if( aChangedValue.hasKey( PROPID_AXIS_RENDERER_CFG ) ) { // обновим отрисовщик
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_AXIS_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        axisRenderer = factory.create( cfg, this, vedScreen() );
        updateRenderer( axisRenderer );
        axisRenderer.props().setDouble( PROP_START_ANGLE, startAngle );
        axisRenderer.props().setDouble( PROP_DELTA_ANGLE, deltaAngle );
      }
    }

    if( aChangedValue.hasKey( PROPID_ARROW_RENDERER_CFG ) ) { // обновим отрисовщик стрелки
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_ARROW_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        arrowRenderer = factory.create( cfg, this, vedScreen() );
        updateRenderer( arrowRenderer );
      }
    }

    if( aChangedValue.hasKey( PROPID_KNOB_RENDERER_CFG ) ) { // обновим отрисовщик крепления стрелки
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_KNOB_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        knobRenderer = factory.create( cfg, this, vedScreen() );
        updateRenderer( knobRenderer );
      }
    }

    if( aChangedValue.hasKey( PROPID_FRAME_RENDERER_CFG ) ) { // обновим отрисовщик ободка
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_FRAME_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        frameRenderer = factory.create( cfg, this, vedScreen() );
        updateRenderer( frameRenderer );
      }
    }

    if( aChangedValue.hasKey( PROPID_START_ANGLE ) ) {
      startAngle = aChangedValue.getByKey( PROPID_START_ANGLE ).asDouble();
      axisRenderer.props().setDouble( PROP_START_ANGLE, startAngle );
    }
    if( aChangedValue.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngle = aChangedValue.getByKey( PROPID_DELTA_ANGLE ).asDouble();
      axisRenderer.props().setDouble( PROP_DELTA_ANGLE, deltaAngle );
    }

    if( aChangedValue.hasKey( PROPID_WIDTH ) ) {
      // double width = aChangedValue.getDouble( PROPID_WIDTH );
      if( bkgRenderer != null ) {
        updateRenderer( bkgRenderer );
      }
      if( axisRenderer != null ) {
        updateRenderer( axisRenderer );
        axisRenderer.props().setDouble( PROP_START_ANGLE, startAngle );
        axisRenderer.props().setDouble( PROP_DELTA_ANGLE, deltaAngle );
      }
      if( arrowRenderer != null ) {
        updateRenderer( arrowRenderer );
        arrowRenderer.props().setDouble( PROPID_ARROW_ANGLE, calcArrowAngle() );
      }
      if( knobRenderer != null ) {
        updateRenderer( knobRenderer );
      }
      if( frameRenderer != null ) {
        updateRenderer( frameRenderer );
      }
    }
    vedScreen().view().redrawVisel( id() );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    if( bkgRenderer != null ) {
      bkgRenderer.paint( aPaintContext );
    }
    axisRenderer.paint( aPaintContext );
    arrowRenderer.paint( aPaintContext );
    if( knobRenderer != null ) {
      knobRenderer.paint( aPaintContext );
    }
    if( frameRenderer != null ) {
      frameRenderer.paint( aPaintContext );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateRenderer( AbstractViselRenderer aRenderer ) {
    double width = props().getDouble( PROPID_WIDTH );
    aRenderer.props().setDouble( PROPID_OWNER_RADIUS, width / 2. );
    aRenderer.props().setDouble( PROPID_OWNER_ANCHOR_X, width / 2. );
    aRenderer.props().setDouble( PROPID_OWNER_ANCHOR_Y, width / 2. );
  }

  double calcArrowAngle() {
    double valueRange = Math.abs( maxValue - minValue );
    double k = Math.abs( value - minValue ) / valueRange;
    double angleRange = Math.abs( deltaAngle );
    return startAngle + k * angleRange;
  }

}
