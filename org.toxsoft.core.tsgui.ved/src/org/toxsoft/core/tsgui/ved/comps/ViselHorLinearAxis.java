package org.toxsoft.core.tsgui.ved.comps;

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
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Линейная горизонтальная шкала.
 *
 * @author vs
 */
public class ViselHorLinearAxis
    extends VedAbstractVisel {

  static class InternalInterceptor
      implements IVedItemPropertyChangeInterceptor<ViselHorLinearAxis> {

    @Override
    public void interceptPropsChange( ViselHorLinearAxis aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
      // if( aNewValues.hasKey( PROPID_BIG_TICK_QTTY ) ) {
      // int btQtty = aNewValues.getInt( PROPID_BIG_TICK_QTTY );
      // if( btQtty < 2 ) {
      // aValuesToSet.setInt( PROPID_BIG_TICK_QTTY, 2 );
      // }
      // }
    }

  }

  static final InternalInterceptor interceptor = new InternalInterceptor();

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".horLinearAxisViselFactory"; //$NON-NLS-1$

  // static final String PROPID_ANNOTATIONS = "annotations"; //$NON-NLS-1$
  // static final String PROPID_BIG_TICK_QTTY = "bigTickQtty"; //$NON-NLS-1$
  // static final String PROPID_MID_TICK_QTTY = "midTickQtty"; //$NON-NLS-1$
  // static final String PROPID_LIT_TICK_QTTY = "litTickQtty"; //$NON-NLS-1$
  //
  // static final ITinFieldInfo TFI_TICKS_COLOR = TtiUtils.fieldInfo( TFI_FG_COLOR, //
  // "Цвет засечек", "Цвет засечек шкалы" );
  //
  // static final ITinFieldInfo TFI_ANNOTATIONS = TtiUtils.strFieldInfo( PROPID_ANNOTATIONS, //
  // "Надписи", "Надписи для больших засечек шкалы разделенные символом \';\'" );
  //
  // static final ITinFieldInfo TFI_BIG_TICK_QTTY = TtiUtils.intFieldInfo( PROPID_BIG_TICK_QTTY, //
  // "Больших засечек", "Количество больших засечек" );
  //
  // static final ITinFieldInfo TFI_MID_TICK_QTTY = TtiUtils.intFieldInfo( PROPID_MID_TICK_QTTY, //
  // "Средних засечек", "Количество средних засечек" );
  //
  // static final ITinFieldInfo TFI_LIT_TICK_QTTY = TtiUtils.intFieldInfo( PROPID_LIT_TICK_QTTY, //
  // "Малых засечек", "Количество малых засечек" );

  public static final String PROPID_RENDERER_CFG = "rendererCfg"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_RENDERER_CFG, StdHorAxisRenderer.tinTypeInfo(), //
          "Rendering properties", "Properties of visual representation of the axis" );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_HOR_LINEAR_AXIS, //
      TSID_DESCRIPTION, STR_VISEL_HOR_LINEAR_AXIS_D, //
      TSID_ICON_ID, ICONID_VISEL_HOR_LINEAR_AXIS ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselHorLinearAxis( aCfg, propDefs(), aVedScreen );
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

      fields.add( TFI_RENDERER_CFG );

      // fields.add( TFI_TICKS_COLOR );
      // fields.add( TFI_FG_COLOR );
      // fields.add( TFI_LINE_INFO );

      // fields.add( TFI_FONT );
      // fields.add( TFI_TEXT_COLOR );
      // fields.add( TFI_BIG_TICK_QTTY );
      // fields.add( TFI_ANNOTATIONS );

      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselHorLinearAxis.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setDouble( PROPID_WIDTH, 150 );
      cfg.propValues().setDouble( PROPID_HEIGHT, 42 );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

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
  public ViselHorLinearAxis( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( interceptor );
    renderer = new StdHorAxisRenderer( "defRend", StdHorAxisRenderer.FACTORY.propDefs(),
        aConfig.propValues().getValobj( PROPID_RENDERER_CFG ), this, aVedScreen.tsContext() );
  }

  // double radius;
  // double startAngle = 0;
  // double deltaAngle = 90;
  // RGBA fgRgba;

  // TsColorDescriptor textColor;
  // IStringListEdit annotations = new StringArrayList();

  // int bigTickQtty = 2;
  // int midTickQtty = 1;
  // int litTickQtty = 10;
  //
  // int ticksQtty = 2;
  //
  // int btLentgh = 12;
  // int mtLentgh = 9;
  // int ltLentgh = 6;
  //
  // int bigTickNumber = 1;
  // int midTickNumber = 1;
  //
  // FontInfo fontInfo = null;

  AbstractViselRenderer renderer;

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_RENDERER_CFG ) ) {
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        renderer = factory.create( cfg, this, vedScreen() );
        double width = aChangedValue.getDouble( PROPID_WIDTH );
        double height = aChangedValue.getDouble( PROPID_HEIGHT );
        renderer.props().setDouble( PROPID_WIDTH, width );
        renderer.props().setDouble( PROPID_HEIGHT, height );
      }
    }

    if( aChangedValue.keys().hasElem( PROPID_WIDTH ) ) {
      double width = aChangedValue.getDouble( PROPID_WIDTH );
      renderer.props().setDouble( PROPID_WIDTH, width );
    }

    if( aChangedValue.keys().hasElem( PROPID_HEIGHT ) ) {
      double height = aChangedValue.getDouble( PROPID_HEIGHT );
      renderer.props().setDouble( PROPID_WIDTH, height );
    }

    // if( aChangedValue.hasKey( TFI_TICKS_COLOR.id() ) ) {
    // fgRgba = aChangedValue.getByKey( TFI_TICKS_COLOR.id() ).asValobj();
    // }
    // if( aChangedValue.hasKey( TFI_TEXT_COLOR.id() ) ) {
    // textColor = aChangedValue.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
    // }
    // if( aChangedValue.hasKey( PROPID_HEIGHT ) ) {
    // height = aChangedValue.getDouble( PROPID_HEIGHT );
    // }
    // if( aChangedValue.hasKey( PROPID_FONT ) ) {
    // fontInfo = aChangedValue.getValobj( PROPID_FONT );
    // }
    // if( aChangedValue.hasKey( TFI_ANNOTATIONS.id() ) ) {
    // annotations.clear();
    // String annotationsStr = aChangedValue.getByKey( TFI_ANNOTATIONS.id() ).asString();
    // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
    // while( st.hasMoreElements() ) {
    // annotations.add( st.nextToken().trim() );
    // }
    // }
    // if( aChangedValue.hasKey( TFI_BIG_TICK_QTTY.id() ) ) {
    // bigTickQtty = aChangedValue.getByKey( TFI_BIG_TICK_QTTY.id() ).asInt();
    // }
    //
    // if( litTickQtty > 0 ) {
    // ticksQtty = (bigTickQtty - 1) * (litTickQtty) + 1;
    // bigTickNumber = litTickQtty;
    // if( midTickQtty > 0 ) {
    // midTickNumber = litTickQtty / (midTickQtty + 1);
    // }
    // }
    // else {
    // if( midTickQtty > 0 ) {
    // ticksQtty = (bigTickQtty - 1) * (midTickQtty + 1) + 1;
    // }
    // else {
    // ticksQtty = bigTickQtty;
    // }
    // }
    // renderer.setPropValues( aChangedValue );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    renderer.paint( aPaintContext );
    // TsLineInfo li = props().getValobj( TFI_LINE_INFO.id() );
    // aPaintContext.setLineInfo( li );
    // aPaintContext.setForegroundRgba( fgRgba );
    //
    // double width = props().getDouble( PROPID_WIDTH );
    // double ltDelta = width / (ticksQtty - 1.);
    //
    // int dy = 0;
    // int textH = 0;
    // if( annotations.size() > 0 ) {
    // textH = aPaintContext.gc().textExtent( annotations.first() ).y;
    // dy += 1 + textH;
    // }
    //
    // btLentgh = (int)(height - dy);
    // mtLentgh = (int)(btLentgh * 0.75);
    // ltLentgh = (int)(btLentgh * 0.5);
    //
    // double x = 0;
    // int annoIdx = 0;
    // for( int i = 0; i < ticksQtty; i++ ) {
    // double tx = x + ltDelta * i;
    // aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - ltLentgh );
    // if( (i % bigTickNumber) == 0 ) {
    // if( annotations.size() > annoIdx ) {
    // aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - btLentgh );
    // String a = annotations.get( annoIdx );
    // annoIdx++;
    // int dx = aPaintContext.gc().textExtent( a ).x / 2;
    // aPaintContext.gc().drawText( a, (int)(tx - dx), 0, true );
    // }
    // }
    // if( midTickNumber != 0 && (i % midTickNumber) == 0 ) {
    // aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - mtLentgh );
    // }
    // }
    //
  }

}
