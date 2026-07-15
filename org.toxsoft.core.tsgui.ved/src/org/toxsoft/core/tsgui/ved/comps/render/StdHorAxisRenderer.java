package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.comps.axis.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Стандартный отрисовщик горизонтальной шкалы.
 *
 * @author vs
 */
public class StdHorAxisRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdHorAxisRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "horAxisRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_HOR_AXIS_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_HOR_AXIS_RENDERER, TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  static final String PROPID_ANNOTATIONS  = "annotations"; //$NON-NLS-1$
  static final String PROPID_AXIS_MARKING = "axisMarking"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_ANNOTATIONS = TtiUtils.strFieldInfo( PROPID_ANNOTATIONS, //
      "Надписи", "Надписи для больших засечек шкалы разделенные символом \';\'" );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new VedAbstractRendererFactory( FACTORY_ID, //
      TSID_NAME, "Renderer", //
      TSID_DESCRIPTION, "Default horizontal axis renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
      opSet.setValobj( PROPID_AXIS_MARKING, AxisMarking.DEFAULT );

      opSet.setValobj( TFI_FG_COLOR.id(), ETsColor.BLACK.rgba() );
      IOptionSetEdit opSetRgba = new OptionSet();
      opSetRgba.setValobj( TsColorSourceKindRgba.OPDEF_RGBA, new RGBA( 0, 0, 0, 255 ) );
      TsColorDescriptor tsd = TsColorSourceKindRgba.INSTANCE.createDescriptor( opSetRgba );
      opSet.setValobj( TFI_TEXT_COLOR.id(), tsd );
      opSet.setValobj( PROPID_FONT, FontInfo.DEFAULT );
      opSet.setStr( PROPID_ANNOTATIONS, "10;20;30;40;50" );
      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields ) {
      String name = "Разметка шкалы";
      String descr = "Разметка шкалы - информация о засечках";
      fields.add( TtiUtils.typedFieldInfo( PROPID_AXIS_MARKING, TtiAxisMarking.INSTANCE, name, descr ) );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_FG_COLOR );

      fields.add( TFI_FONT );
      fields.add( TFI_TEXT_COLOR );
      fields.add( TFI_ANNOTATIONS );
    }

    // @Override
    // protected ITinTypeInfo doCreateTypeInfo() {
    // IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    // String name = "Разметка шкалы";
    // String descr = "Разметка шкалы - информация о засечках";
    // fields.add( TtiUtils.typedFieldInfo( PROPID_AXIS_MARKING, TtiAxisMarking.INSTANCE, name, descr ) );
    // fields.add( TFI_LINE_INFO );
    // fields.add( TFI_FG_COLOR );
    //
    // fields.add( TFI_FONT );
    // fields.add( TFI_TEXT_COLOR );
    // fields.add( TFI_ANNOTATIONS );
    // return new PropertableEntitiesTinTypeInfo<>( fields, AbstractViselRenderer.class );
    // }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      if( aCfg == null || aCfg.id().equals( "default" ) ) {
        return new StdHorAxisRenderer( "harDefault", propDefs(), defaultCfg( aVisel.id() ), aVisel, //$NON-NLS-1$
            aVedScreen.tsContext() );
      }
      return new StdHorAxisRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  public static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
    opSet.setValobj( PROPID_AXIS_MARKING, AxisMarking.DEFAULT );

    opSet.setValobj( TFI_FG_COLOR.id(), ETsColor.BLACK.rgba() );
    IOptionSetEdit opSetRgba = new OptionSet();
    opSetRgba.setValobj( TsColorSourceKindRgba.OPDEF_RGBA, new RGBA( 0, 0, 0, 255 ) );
    TsColorDescriptor tsd = TsColorSourceKindRgba.INSTANCE.createDescriptor( opSetRgba );
    opSet.setValobj( TFI_TEXT_COLOR.id(), tsd );
    opSet.setValobj( PROPID_FONT, FontInfo.DEFAULT );
    opSet.setStr( PROPID_ANNOTATIONS, "10;20;30;40;50" );
    return new ViselRendererCfg( "rendCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsColorDescriptor textColor;
  TsLineInfo        lineInfo       = TsLineInfo.DEFAULT;
  String            annotationsStr = "1;2;3;4;5";                                    //$NON-NLS-1$
  IStringListEdit   annotations    = new StringArrayList( "1", "2", "3", "4", "5" ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$

  AxisMarking axisMarking = AxisMarking.DEFAULT;

  FontInfo fontInfo = null;
  Font     font     = null;

  RGBA fgRgba = new RGBA( 0, 0, 0, 255 );

  double height = 32;
  double width  = 100;

  ViselRendererCfg rendererCfg = null;

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of data definitions
   * @param aCfg {@link ViselRendererCfg} - visel configeration
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public StdHorAxisRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
    // rendererCfg = new ViselRendererCfg( aCfg.id(), aCfg.kindId(), aCfg.factoryId(), aCfg.propValues(), aVisel.id() );
    // setPropValues( rendererCfg.propValues() );
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
    if( aChangedValues.hasKey( PROPID_LINE_INFO ) ) {
      lineInfo = aChangedValues.getValobj( PROPID_LINE_INFO );
    }
    if( aChangedValues.hasKey( PROPID_AXIS_MARKING ) ) {
      axisMarking = aChangedValues.getValobj( PROPID_AXIS_MARKING );
    }
    if( aChangedValues.hasKey( TFI_FG_COLOR.id() ) ) {
      fgRgba = aChangedValues.getByKey( TFI_FG_COLOR.id() ).asValobj();
    }
    if( aChangedValues.hasKey( TFI_TEXT_COLOR.id() ) ) {
      IAtomicValue v = aChangedValues.getByKey( TFI_TEXT_COLOR.id() );
      textColor = aChangedValues.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
    }
    if( aChangedValues.hasKey( PROPID_WIDTH ) ) {
      width = aChangedValues.getDouble( PROPID_WIDTH );
    }
    if( aChangedValues.hasKey( PROPID_HEIGHT ) ) {
      height = aChangedValues.getDouble( PROPID_HEIGHT );
    }
    if( aChangedValues.hasKey( PROPID_FONT ) ) {
      fontInfo = aChangedValues.getValobj( PROPID_FONT );
      font = fontManager().getFont( fontInfo );
    }
    if( aChangedValues.hasKey( TFI_ANNOTATIONS.id() ) ) {
      annotations.clear();
      annotationsStr = aChangedValues.getByKey( TFI_ANNOTATIONS.id() ).asString();
      StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
      while( st.hasMoreElements() ) {
        annotations.add( st.nextToken().trim() );
      }
    }
    if( aChangedValues.hasKey( ViselHorLinearAxis.PROPID_RENDERER_CFG ) ) {
      rendererCfg = aChangedValues.getValobj( ViselHorLinearAxis.PROPID_RENDERER_CFG );
    }
    updateCfg();
  }

  // @Override
  // public void setPropValues( IOptionSet aProps ) {
  // if( aProps.hasKey( PROPID_LINE_INFO ) ) {
  // lineInfo = aProps.getValobj( PROPID_LINE_INFO );
  // }
  // if( aProps.hasKey( PROPID_AXIS_MARKING ) ) {
  // axisMarking = aProps.getValobj( PROPID_AXIS_MARKING );
  // }
  // if( aProps.hasKey( TFI_FG_COLOR.id() ) ) {
  // fgRgba = aProps.getByKey( TFI_FG_COLOR.id() ).asValobj();
  // }
  // if( aProps.hasKey( TFI_TEXT_COLOR.id() ) ) {
  // IAtomicValue v = aProps.getByKey( TFI_TEXT_COLOR.id() );
  // textColor = aProps.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
  // }
  // if( aProps.hasKey( PROPID_WIDTH ) ) {
  // width = aProps.getDouble( PROPID_WIDTH );
  // }
  // if( aProps.hasKey( PROPID_HEIGHT ) ) {
  // height = aProps.getDouble( PROPID_HEIGHT );
  // }
  // if( aProps.hasKey( PROPID_FONT ) ) {
  // fontInfo = aProps.getValobj( PROPID_FONT );
  // font = fontManager().getFont( fontInfo );
  // }
  // if( aProps.hasKey( TFI_ANNOTATIONS.id() ) ) {
  // annotations.clear();
  // annotationsStr = aProps.getByKey( TFI_ANNOTATIONS.id() ).asString();
  // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
  // while( st.hasMoreElements() ) {
  // annotations.add( st.nextToken().trim() );
  // }
  // }
  // if( aProps.hasKey( ViselHorLinearAxis.PROPID_RENDERER_CFG ) ) {
  // rendererCfg = aProps.getValobj( ViselHorLinearAxis.PROPID_RENDERER_CFG );
  // }
  // updateCfg();
  // }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return FACTORY.doCreateTypeInfo();
  }

  @Override
  protected void doPaint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setLineInfo( lineInfo );
    aPaintContext.setForegroundRgba( fgRgba );

    Font oldFont = aPaintContext.gc().getFont();
    aPaintContext.gc().setFont( font );

    // double width = props().getDouble( PROPID_WIDTH );
    // double ltDelta = width / (axisMarking.bigTickQtty() - 1.);

    int dy = 0;
    int textH = 0;
    if( annotations.size() > 0 ) {
      textH = aPaintContext.gc().textExtent( annotations.first() ).y;
      dy += 1 + textH;
    }

    int btLentgh = (int)(height - dy);
    int mtLentgh = (int)(btLentgh * 0.75);
    int ltLentgh = (int)(btLentgh * 0.5);

    double x = 0;
    int annoIdx = 0;
    int ticksQtty = axisMarking.bigTickNumber() * (axisMarking.bigTickQtty() - 1) + 1;
    double ltDelta = width / (ticksQtty - 1);
    for( int i = 0; i < ticksQtty; i++ ) {
      double tx = x + ltDelta * i;
      aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - ltLentgh );
      if( (i % axisMarking.bigTickNumber()) == 0 ) {
        if( annotations.size() > annoIdx ) {
          aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - btLentgh );
          String a = annotations.get( annoIdx );
          annoIdx++;
          int dx = aPaintContext.gc().textExtent( a ).x / 2;
          aPaintContext.gc().drawText( a, (int)(tx - dx), 0, true );
        }
      }
      if( axisMarking.midTickNumber() != 0 && (i % axisMarking.midTickNumber()) == 0 ) {
        aPaintContext.drawLine( (int)tx, (int)height, (int)tx, (int)height - mtLentgh );
      }
    }
    aPaintContext.gc().setFont( oldFont );
  }

  void updateCfg() {
    if( rendererCfg != null ) {
      rendererCfg.propValues().setValobj( PROPID_LINE_INFO, lineInfo );
      rendererCfg.propValues().setValobj( PROPID_AXIS_MARKING, axisMarking );
      rendererCfg.propValues().setValobj( TFI_FG_COLOR.id(), fgRgba );
      rendererCfg.propValues().setValobj( TFI_TEXT_COLOR.id(), textColor );
      // rendererCfg.propValues().setDouble( PROPID_WIDTH, width );
      // rendererCfg.propValues().setDouble( PROPID_HEIGHT, height );
      rendererCfg.propValues().setValobj( PROPID_FONT, fontInfo );
      rendererCfg.propValues().setStr( PROPID_ANNOTATIONS, annotationsStr );
      annotations.clear();
      StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
      while( st.hasMoreElements() ) {
        annotations.add( st.nextToken().trim() );
      }
    }
    // if( aProps.hasKey( TFI_ANNOTATIONS.id() ) ) {
    // annotations.clear();
    // String annotationsStr = aProps.getByKey( TFI_ANNOTATIONS.id() ).asString();
    // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
    // while( st.hasMoreElements() ) {
    // annotations.add( st.nextToken().trim() );
    // }
    // }
  }
}
