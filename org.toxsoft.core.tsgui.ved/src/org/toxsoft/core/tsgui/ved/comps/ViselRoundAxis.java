package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
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

public class ViselRoundAxis
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".roundAxisViselFactory"; //$NON-NLS-1$

  public static final String PROPID_START_ANGLE = "startAngle"; //$NON-NLS-1$
  public static final String PROPID_DELTA_ANGLE = "deltaAngle"; //$NON-NLS-1$

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

  static final ITinFieldInfo TFI_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_RENDERER_CFG, StdRoundAxisRenderer.tinTypeInfo(), //
          "Rendering properties", "Properties of visual representation of the axis" );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_ROUND_AXIS, //
      TSID_DESCRIPTION, STR_VISEL_ROUND_AXIS_D, //
      TSID_ICON_ID, ICONID_VISEL_ROUND_AXIS ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselRoundAxis( aCfg, propDefs(), aVedScreen );
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

      // fields.addAll( ViselRoundAxisRenderer.fields );

      fields.add( new TinFieldInfo( PROPID_START_ANGLE, TTI_AT_FLOATING, PROP_START_ANGLE.params() ) );
      fields.add( new TinFieldInfo( PROPID_DELTA_ANGLE, TTI_AT_FLOATING, PROP_DELTA_ANGLE.params() ) );

      fields.add( TFI_RENDERER_CFG );

      // fields.add( TFI_RADIUS );
      // fields.add( TFI_BK_FILL );
      // fields.add( TFI_FG_COLOR );
      // fields.add( TFI_LINE_INFO );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_ZOOM_HIDDEN );
      fields.add( TFI_ANGLE_HIDDEN );
      fields.add( TFI_TRANSFORM_HIDDEN );
      fields.add( TFI_IS_ACTIVE_HIDDEN );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRoundAxis.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      // IOptionSetEdit options = new OptionSet();
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setBool( PROPID_IS_ASPECT_FIXED, true );
      cfg.propValues().setDouble( PROPID_ASPECT_RATIO, 1. );
      // cfg.propValues().setDouble( PROPID_RADIUS, 200. );
      // cfg.propValues().setInt( PROPID_BIG_TICK_QTTY, 6 );
      // cfg.propValues().setStr( PROPID_ANNOTATIONS, "10;20;30;40;50;60" );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    };

  };

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRoundAxis( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // renderer = new StdRoundAxisRenderer( "stdRend", StdRoundAxisRenderer.FACTORY.propDefs(), //$NON-NLS-1$
    // aConfig.propValues().getValobj( PROPID_RENDERER_CFG ), this, aVedScreen.tsContext() );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  double radius;
  double startAngle = 0;
  double deltaAngle = 90;
  RGBA   fgRgba;

  AbstractViselRenderer renderer;

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
      // if( cfg.viselId().equals( "none" ) ) {
      // cfg = new ViselRendererCfg( cfg.id(), cfg.kindId(), cfg.factoryId(), cfg.propValues(), id() );
      // }
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        renderer = factory.create( cfg, this, vedScreen() );
      }
    }

    // if( aChangedValue.hasKey( PROPID_FG_COLOR ) ) {
    // fgRgba = aChangedValue.getByKey( PROPID_FG_COLOR ).asValobj();
    // }
    // if( aChangedValue.hasKey( PROPID_RADIUS ) ) {
    // radius = aChangedValue.getByKey( PROPID_RADIUS ).asDouble();
    // }
    if( aChangedValue.hasKey( PROPID_START_ANGLE ) ) {
      startAngle = aChangedValue.getByKey( PROPID_START_ANGLE ).asDouble();
    }
    if( aChangedValue.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngle = aChangedValue.getByKey( PROPID_DELTA_ANGLE ).asDouble();
    }
    // renderer.setPropValues( aChangedValue );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    renderer.paint( aPaintContext );
    // TsLineInfo li = props().getValobj( TFI_LINE_INFO.id() );
    // aPaintContext.setLineInfo( li );
    // aPaintContext.setForegroundRgba( fgRgba );
    //
    // // aPaintContext.drawArc( 0, 0, (int)radius * 2, (int)radius * 2, (int)startAngle, (int)(startAngle + deltaAngle)
    // );
    // for( int i = 0; i < 61; i++ ) {
    // if( i % 12 == 0 ) {
    // drawTick( Math.toRadians( -i * 3. ), 16., "" + 10 * i, aPaintContext );
    // }
    // else {
    // drawTick( Math.toRadians( -i * 3. ), 16., TsLibUtils.EMPTY_STRING, aPaintContext );
    // }
    // }
    //
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void drawTick( double aAngle, double aLength, String aText, ITsGraphicsContext aPaintContext ) {
    double sin = Math.sin( aAngle );
    double cos = Math.cos( aAngle );

    double x1 = radius + radius * cos;
    double y1 = radius + radius * sin;

    double x2 = radius + (radius + aLength) * cos;
    double y2 = radius + (radius + aLength) * sin;

    aPaintContext.drawLine( (int)Math.round( x1 ), (int)Math.round( y1 ), (int)Math.round( x2 ),
        (int)Math.round( y2 ) );

    if( aText != null && !aText.isBlank() ) {
      Point textExt = aPaintContext.gc().textExtent( aText );

      // double tx = radius + (radius + textExt.x + 16) * cos;
      // double ty = radius + (radius + textExt.y + 16) * sin;
      double tx = radius + (radius + 16 + 16) * cos;
      double ty = radius + (radius + 16 + 16) * sin;

      // double dr = Math.abs( textExt.x * cos ) + Math.abs( textExt.y * sin ) + 8;
      // double tx = radius + (radius + dr) * cos;
      // double ty = radius + (radius + dr) * sin;

      aPaintContext.gc().drawText( aText, (int)(tx - textExt.x / 2.), (int)(ty - textExt.y / 2.), true );
      // aPaintContext.gc().drawText( aText, (int)(tx), (int)(ty), true );
      // aPaintContext.gc().drawText( aText, (int)(tx + textExt.x / 2. * cos), (int)(ty + textExt.y * sin / 2.), true );

      // aPaintContext.gc().setBackground( new Color( 255, 0, 0 ) );
      // aPaintContext.gc().fillOval( (int)tx - 2, (int)ty - 2, 4, 4 );
    }

  }
}
