package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.path.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.graphics.shadow.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Стандартный отрисовщик стрелки прибора.
 * <p>
 *
 * @author vs
 */
public class StdGaugeArrowRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdGaugeArrowRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "gaugeArrowRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_GAUGE_ARROW_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_NAME, "Стрелка", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  /**
   * Возвращает тип данных для инспектора свойств.
   *
   * @return {@link ITinTypeInfo} - тип данных для инспектора свойств.
   */
  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_GAUGE_ARROW_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  static final String PROPID_ARROW_LENGTH = "arrowLength"; //$NON-NLS-1$
  static final String PROPID_ARROW_THICK  = "arrowThick";  //$NON-NLS-1$
  static final String PROPID_ANCHOR_X     = "anchorX";     //$NON-NLS-1$
  static final String PROPID_ANCHOR_Y     = "anchorY";     //$NON-NLS-1$

  static final ITinFieldInfo TFI_ARROW_LENGTH = TtiUtils.doubleFieldInfo( PROPID_ARROW_LENGTH, //
      "Длина стрелки", "Длина стрелки в процентах от длины радиуса датчика", 80 );

  static final ITinFieldInfo TFI_ARROW_THICK = TtiUtils.doubleFieldInfo( PROPID_ARROW_THICK, //
      "Толщина стрелки", "Толщина стрелки в процентах от длины радиуса датчика", 4 );

  static final ITinFieldInfo TFI_ANCHOR_X = TtiUtils.doubleFieldInfo( PROPID_ANCHOR_X, //
      "X", "X координата точки закрепления в процентах от ширины стрелки", 50. );

  static final ITinFieldInfo TFI_ANCHOR_Y = TtiUtils.doubleFieldInfo( PROPID_ANCHOR_Y, //
      "Y", "Y координата точки закрепления в процентах от длины стрелки", 100. );

  static final TsPathData defaultArrowPathData = new TsPathData( new byte[] { 1, 2, 2, 5 }, //
      new float[] { //
          4.0f, 0.0f, //
          0.0f, 100.0f, //
          8.0f, 100.0f //
      } );

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
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_PATH_DATA );
      aFields.add( TFI_ARROW_LENGTH );
      aFields.add( TFI_ARROW_THICK );
      aFields.add( TFI_ANCHOR_X );
      aFields.add( TFI_ANCHOR_Y );

      aFields.add( TFI_LINE_INFO );
      aFields.add( TFI_FG_COLOR );
      aFields.add( TFI_BK_FILL );

      aFields.add( TFI_SHADOW_INFO );
    }

    // @Override
    // protected ITinTypeInfo doCreateTypeInfo() {
    // IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    //
    // fields.add( TFI_PATH_DATA );
    // fields.add( TFI_ARROW_LENGTH );
    // fields.add( TFI_ARROW_THICK );
    // fields.add( TFI_ANCHOR_X );
    // fields.add( TFI_ANCHOR_Y );
    //
    // fields.add( TFI_LINE_INFO );
    // fields.add( TFI_FG_COLOR );
    // fields.add( TFI_BK_FILL );
    //
    // fields.add( TFI_SHADOW_INFO );
    //
    // // ----------------------------------------------------------------------------
    // // Скрытые поля, значения которых устанавливаются извне
    // //
    // fields.add( TtiUtils.createHidden( TFI_OWNER_RADIUS ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_X ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_Y ) );
    // fields.add( TtiUtils.createHidden( TFI_ARROW_ANGLE ) );
    //
    // return new PropertableEntitiesTinTypeInfo<>( fields, AbstractViselRenderer.class );
    // }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setDouble( PROPID_ARROW_LENGTH, 80. );
      opSet.setDouble( PROPID_ARROW_THICK, 4. );

      opSet.setDouble( PROPID_ANCHOR_X, 50. );
      opSet.setDouble( PROPID_ANCHOR_Y, 100. );

      opSet.setValobj( PROPID_PATH_DATA, defaultArrowPathData );
      opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
      opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );
      opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 128, 128, 128, 255 ) ) );

      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      return new StdGaugeArrowRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setDouble( PROPID_ARROW_LENGTH, 80. );
    opSet.setDouble( PROPID_ARROW_THICK, 4. );

    opSet.setDouble( PROPID_ANCHOR_X, 50. );
    opSet.setDouble( PROPID_ANCHOR_Y, 100. );

    opSet.setValobj( PROPID_PATH_DATA, defaultArrowPathData );
    opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
    opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );
    opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 128, 128, 128, 255 ) ) );

    return new ViselRendererCfg( "stdArrowCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsPathData initialPathData = new TsPathData( defaultArrowPathData.types(), defaultArrowPathData.points() );
  PathData   pathData        = new PathData();

  ViselRendererCfg rendererCfg = null;

  // double radius = 100;
  double angleRad = 0.0; // угол стрелки

  double cx = 2;   // кордината x точки закрепления
  double cy = 100; // кордината y точки закрепления

  TsLineInfo   lineInfo   = TsLineInfo.DEFAULT;
  TsFillInfo   fillInfo;
  TsShadowInfo shadowInfo = TsShadowInfo.NONE;

  RGBA lineColor = new RGBA( 0, 0, 0, 255 );

  double initialLength = 100;

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of data definitions
   * @param aCfg {@link ViselRendererCfg} - visel configeration
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public StdGaugeArrowRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg,
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
    if( aChangedValues.hasKey( PROPID_PATH_DATA ) ) {
      initialPathData = aChangedValues.getValobj( PROPID_PATH_DATA );
      if( initialPathData == null ) {
        initialPathData = new TsPathData( defaultArrowPathData.types(), defaultArrowPathData.points() );
      }
      Path p = new Path( getDisplay(), initialPathData.PathData() );
      float[] bounds = new float[4];
      p.getBounds( bounds );
      initialLength = bounds[3];
      p.dispose();
    }
    if( aChangedValues.hasKey( PROPID_FG_COLOR ) ) {
      lineColor = aChangedValues.getValobj( PROPID_FG_COLOR );
    }
    if( aChangedValues.hasKey( PROPID_SHADOW_INFO ) ) {
      shadowInfo = aChangedValues.getValobj( PROPID_SHADOW_INFO );
    }
    if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
      fillInfo = aChangedValues.getValobj( PROPID_BK_FILL );
    }
    if( aChangedValues.hasKey( PROPID_ARROW_ANGLE ) ) {
      angleRad = aChangedValues.getDouble( PROPID_ARROW_ANGLE );
    }
    updateCachedValues();
    updateGeomery();
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
  // // updateGeomery();
  // }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {
    Path path = new Path( aPaintContext.gc().getDevice(), pathData );
    float[] bounds = new float[4];
    path.getBounds( bounds );
    aPaintContext.setFillInfo( fillInfo );
    aPaintContext.setLineInfo( lineInfo );
    aPaintContext.setForegroundRgba( lineColor );

    double viselX = props().getDouble( PROPID_OWNER_ANCHOR_X );
    double viselY = props().getDouble( PROPID_OWNER_ANCHOR_Y );

    int x = (int)(viselX - cx);
    int y = (int)(viselY - cy);
    aPaintContext.gc().setAdvanced( true );
    aPaintContext.gc().setAntialias( SWT.ON );

    if( shadowInfo != TsShadowInfo.NONE ) {
      ImageData imd = TsShadowUtils.buildDropShadowData( path, shadowInfo, aPaintContext.gc().getDevice(), false );
      Image img = new Image( aPaintContext.gc().getDevice(), imd );
      int shadowX = Math.round( bounds[0] + x - shadowInfo.blur() + 4 );
      int shadowY = Math.round( bounds[1] + y - shadowInfo.blur() + 4 );
      aPaintContext.gc().drawImage( img, shadowX, shadowY );
      // aPaintContext.gc().drawImage( img, x, y );
      img.dispose();
    }

    aPaintContext.fillPath( path, x, y, (int)bounds[2], (int)bounds[3] );
    aPaintContext.drawPath( path, x, y );
    path.dispose();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateCachedValues() {
    if( rendererCfg != null ) {
      fillInfo = rendererCfg.propValues().getValobj( PROPID_BK_FILL );
      lineInfo = rendererCfg.propValues().getValobj( PROPID_LINE_INFO );
    }
    updateGeomery();
  }

  void updateGeomery() {
    PathData pd = new PathData();
    pd.types = initialPathData.types();
    pd.points = initialPathData.points();
    Path path = new Path( getDisplay(), pd );
    float[] bounds = new float[4];
    path.getBounds( bounds );
    path.dispose();

    double ownerR = props().getDouble( PROPID_OWNER_RADIUS );

    double neededLength = (props().getDouble( PROPID_ARROW_LENGTH ) * ownerR) / 100.;
    float zoomFactor = (float)(neededLength / initialLength);

    // radius = cy;

    PathData scaledPd = PathDataUtils.scale( initialPathData.PathData(), 0, 0, zoomFactor, zoomFactor );
    pathData.types = scaledPd.types;
    pathData.points = scaledPd.points;

    pd = new PathData();
    pd.types = scaledPd.types;
    pd.points = scaledPd.points;
    path = new Path( getDisplay(), pd );
    path.getBounds( bounds );
    path.dispose();

    pathData = PathDataUtils.shift( pathData, -bounds[0], -bounds[1] );

    cx = bounds[2] / 2.;
    cy = (neededLength * props().getDouble( PROPID_ANCHOR_Y )) / 100.;

    pathData = PathDataUtils.rotate( pathData, (float)cx, (float)cy, (float)(angleRad - 90) );
  }

}
