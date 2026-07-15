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

/**
 * Стандартный отрисовщик фона прибора.
 * <p>
 *
 * @author vs
 */
public class StdGaugeFrameRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdGaugeFrameRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "gaugeFrameRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_GAUGE_FRAME_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Ободок", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  /**
   * Возвращает тип данных для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - тип данных для инспектора свойств.
   */
  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_GAUGE_FRAME_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  public static final String PROPID_FRAME_THICKNESS = "frameThickness";

  static final ITinFieldInfo TFI_FRAME_THICKNESS = TtiUtils.doubleFieldInfo( PROPID_FRAME_THICKNESS, //
      "Толщина", "Толщина впроцентах от общего радиуса датчика", 3 );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractCircularRendererFactory( FACTORY_ID, //
      TSID_NAME, "Frame renderer", //
      TSID_DESCRIPTION, "Standard gauge frame renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields ) {
      fields.add( TFI_FRAME_THICKNESS );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_BK_FILL );
    }

    // @Override
    // protected ITinTypeInfo doCreateTypeInfo() {
    // IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    //
    // fields.add( TFI_FRAME_THICKNESS );
    // fields.add( TFI_FG_COLOR );
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

      opSet.setDouble( PROPID_FRAME_THICKNESS, 3. );
      opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );
      opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 0, 0, 0, 255 ) ) );

      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      return new StdGaugeFrameRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();

    opSet.setDouble( PROPID_FRAME_THICKNESS, 3. );
    opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );
    opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 0, 0, 0, 255 ) ) );

    return new ViselRendererCfg( "stdFrameCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  ViselRendererCfg rendererCfg = null;

  double thickness   = 3;
  double ownerRadius = 100;

  RGBA       fgRgba        = new RGBA( 0, 0, 0, 255 );
  TsFillInfo fillInfo      = TsFillInfo.NONE;
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
  public StdGaugeFrameRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg,
      IVedVisel aVisel, ITsGuiContext aTsContext ) {
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
    if( aChangedValues.hasKey( PROPID_FRAME_THICKNESS ) ) {
      thickness = aChangedValues.getDouble( PROPID_FRAME_THICKNESS );
    }
    if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
      fillInfo = aChangedValues.getValobj( PROPID_BK_FILL );
    }
    if( aChangedValues.hasKey( PROPID_OWNER_RADIUS ) ) {
      ownerRadius = aChangedValues.getDouble( PROPID_OWNER_RADIUS );
    }
    if( aChangedValues.hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = aChangedValues.getValobj( PROPID_FG_COLOR );
    }
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
    Path path = null;
    try {
      double viselX = props().getDouble( PROPID_OWNER_ANCHOR_X );
      double viselY = props().getDouble( PROPID_OWNER_ANCHOR_Y );

      path = new Path( getDisplay() );
      float x = (float)(viselX - ownerRadius);
      float y = (float)(viselY - ownerRadius);
      path.addArc( x, y, (float)(2 * ownerRadius), (float)(2 * ownerRadius), 0, 360 );
      // float dr = (float)(ownerRadius - (ownerRadius * thickness) / 100.);
      float dr = (float)((ownerRadius * thickness) / 100.);
      path.addArc( x + dr, y + dr, (float)(2 * ownerRadius - 2 * dr), (float)(2 * ownerRadius - 2 * dr), 0, 360 );
      aPaintContext.setFillInfo( fillInfo );
      aPaintContext.fillPath( path, 0, 0, (int)(2 * ownerRadius), (int)(2 * ownerRadius) );
      aPaintContext.setForegroundRgba( fgRgba );
      aPaintContext.drawPath( path, 0, 0 );
    }
    finally {
      if( path != null ) {
        path.dispose();
      }
      aPaintContext.gc().setAlpha( 255 );
    }
  }

}
