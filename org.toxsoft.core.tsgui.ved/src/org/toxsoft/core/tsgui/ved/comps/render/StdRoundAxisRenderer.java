package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
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
 * Стандартный отрисовщик круговой шкалы.
 *
 * @author vs
 */
public class StdRoundAxisRenderer
    extends AbstractViselRenderer {

  // Формула для каждой засечки со значением v:
  // angle = 225° + (v / 360) × 270°
  // x = cx + cos(angle_rad) × (R + offset)
  // y = cy + sin(angle_rad) × (R + offset)
  // Где R — радиус до конца засечки, offset — отступ подписи наружу от засечки.

  // Ключевое отличие SVG от обычной математики: ось Y направлена вниз, поэтому углы идут по часовой стрелке. Это
  // значит, что формула остаётся той же самой — никаких дополнительных преобразований не нужно:
  // angle_deg = 225 + (value / 360) × 270
  // angle_rad = angle_deg × π / 180
  //
  // x = cx + cos(angle_rad) × (R + offset)
  // y = cy + sin(angle_rad) × (R + offset)
  // В стандартной математике (ось Y вверх) нужно было бы писать cy - sin(...), но в SVG cy + sin(...) — и спидометрная
  // ориентация получается сама собой. Таблица показывает уже готовые SVG-координаты с вашим cx и cy.

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdRoundAxisRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "roundAxisRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_ROUND_AXIS_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          // TSID_NAME, "шкала", //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_ROUND_AXIS_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  static final String PROPID_ANNOTATIONS     = "annotations";  //$NON-NLS-1$
  static final String PROPID_AXIS_MARKING    = "axisMarking";  //$NON-NLS-1$
  static final String PROPID_DRAW_ARC        = "drawArc";      //$NON-NLS-1$
  static final String PROPID_OUTER_TICKS     = "outerTicks";   //$NON-NLS-1$
  static final String PROPID_BIG_TICK_LENGTH = "btLength";     //$NON-NLS-1$
  static final String PROPID_MID_TICK_LENGTH = "mtLength";     //$NON-NLS-1$
  static final String PROPID_LIT_TICK_LENGTH = "ltLength";     //$NON-NLS-1$
  static final String PROPID_RADIUS_SCALE    = "radiusScale";  //$NON-NLS-1$
  static final String PROPID_ROTATE_LABELS   = "rotateLabels"; //$NON-NLS-1$
  static final String PROPID_TEXT_INDENT     = "textIndent";   //$NON-NLS-1$

  static final ITinFieldInfo TFI_ANNOTATIONS = TtiUtils.strFieldInfo( PROPID_ANNOTATIONS, //
      "Надписи", "Надписи для больших засечек шкалы разделенные символом \';\'" );

  static final ITinFieldInfo TFI_AXIS_MARKING = TtiUtils.typedFieldInfo( PROPID_AXIS_MARKING, TtiAxisMarking.INSTANCE, //
      "Разметка шкалы", "Разметка шкалы - информация о засечках" );

  static final ITinFieldInfo TFI_OUTER_TICKS = TtiUtils.booleanFieldInfo( PROPID_OUTER_TICKS, //
      "Засечки наружу", "Засечки наружу", true );

  static final ITinFieldInfo TFI_BIG_TICK_LENGTH = TtiUtils.doubleFieldInfo( PROPID_BIG_TICK_LENGTH, //
      "Длина большой засечки", "Длина большой засечки в процентах от длины радиуса шкалы", 10 );

  static final ITinFieldInfo TFI_MID_TICK_LENGTH = TtiUtils.doubleFieldInfo( PROPID_MID_TICK_LENGTH, //
      "Длина средней засечки", "Длина средней засечки в процентах от длины большой засечки", 60 );

  static final ITinFieldInfo TFI_LIT_TICK_LENGTH = TtiUtils.doubleFieldInfo( PROPID_LIT_TICK_LENGTH, //
      "Длина малой засечки", "Длина малой засечки в процентах от длины большой засечки", 30 );

  static final ITinFieldInfo TFI_RADIUS_SCALE = TtiUtils.doubleFieldInfo( PROPID_RADIUS_SCALE, //
      "Масштаб", "Длина радиуса в процентах от радиуса визеля", 100 );

  static final ITinFieldInfo TFI_ROTATE_LABELS = TtiUtils.booleanFieldInfo( PROPID_ROTATE_LABELS, //
      "Поворачивать подписи", "Признак того, нужно ли поворачивать подписи", true );

  static final ITinFieldInfo TFI_TEXT_INDENT = TtiUtils.doubleFieldInfo( PROPID_TEXT_INDENT, //
      "Отступ текста", "Отступ подписи от засечки", 1.0 );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractCircularRendererFactory( FACTORY_ID, //
      TSID_NAME, "Renderer", //
      TSID_DESCRIPTION, "Standard round axis renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_RADIUS_SCALE );
      aFields.add( TFI_AXIS_MARKING );
      aFields.add( TFI_OUTER_TICKS );

      aFields.add( TFI_BIG_TICK_LENGTH );
      aFields.add( TFI_MID_TICK_LENGTH );
      aFields.add( TFI_LIT_TICK_LENGTH );

      aFields.add( TFI_LINE_INFO );
      aFields.add( TFI_FG_COLOR );

      aFields.add( TFI_FONT );
      aFields.add( TFI_TEXT_COLOR );
      aFields.add( TFI_ANNOTATIONS );
      aFields.add( TFI_TEXT_INDENT );
      aFields.add( TFI_ROTATE_LABELS );
      aFields.add( TtiUtils.booleanFieldInfo( PROPID_DRAW_ARC, "Рисовать дугу", "Рисовать дугу" ) );
    }

    // @Override
    // protected ITinTypeInfo doCreateTypeInfo() {
    // IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    // fields.add( TFI_RADIUS_SCALE );
    // fields.add( TFI_AXIS_MARKING );
    // fields.add( TFI_OUTER_TICKS );
    //
    // fields.add( TFI_BIG_TICK_LENGTH );
    // fields.add( TFI_MID_TICK_LENGTH );
    // fields.add( TFI_LIT_TICK_LENGTH );
    //
    // fields.add( TFI_LINE_INFO );
    // fields.add( TFI_FG_COLOR );
    //
    // fields.add( TFI_FONT );
    // fields.add( TFI_TEXT_COLOR );
    // fields.add( TFI_ANNOTATIONS );
    // fields.add( TFI_TEXT_INDENT );
    // fields.add( TFI_ROTATE_LABELS );
    // fields.add( TtiUtils.booleanFieldInfo( PROPID_DRAW_ARC, "Рисовать дугу", "Рисовать дугу" ) );
    //
    // // ----------------------------------------------------------------------------
    // // Скрытые поля, значения которых устанавливаются извне
    // //
    // fields.add( TtiUtils.createHidden( TFI_OWNER_RADIUS ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_X ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_Y ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_START_ANGLE ) );
    // fields.add( TtiUtils.createHidden( TFI_OWNER_DELTA_ANGLE ) );
    //
    // return new PropertableEntitiesTinTypeInfo<>( fields, AbstractViselRenderer.class );
    // }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setDouble( PROPID_RADIUS_SCALE, 0.85 );
      opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
      opSet.setValobj( PROPID_AXIS_MARKING, AxisMarking.DEFAULT );

      opSet.setDouble( PROPID_BIG_TICK_LENGTH, 10 );
      opSet.setDouble( PROPID_MID_TICK_LENGTH, 60 );
      opSet.setDouble( PROPID_LIT_TICK_LENGTH, 30 );

      opSet.setBool( PROPID_OUTER_TICKS, true );

      opSet.setValobj( TFI_FG_COLOR.id(), ETsColor.BLACK.rgba() );
      IOptionSetEdit opSetRgba = new OptionSet();
      opSetRgba.setValobj( TsColorSourceKindRgba.OPDEF_RGBA, new RGBA( 0, 0, 0, 255 ) );
      TsColorDescriptor tsd = TsColorSourceKindRgba.INSTANCE.createDescriptor( opSetRgba );
      opSet.setValobj( TFI_TEXT_COLOR.id(), tsd );
      opSet.setValobj( PROPID_FONT, FontInfo.DEFAULT );
      opSet.setStr( PROPID_ANNOTATIONS, "10;20;30;40;50" );
      opSet.setDouble( PROPID_TEXT_INDENT, 1.0 );
      opSet.setBool( PROPID_ROTATE_LABELS, false );
      opSet.setBool( PROPID_DRAW_ARC, false );
      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      if( aCfg == null || aCfg.id().equals( "default" ) || aCfg.viselId().equals( "none" ) ) {
        ViselRendererCfg rCfg = defaultCfg( aVisel.id() );
        return new StdRoundAxisRenderer( "stdRend", propDefs(), rCfg, aVisel, aVedScreen.tsContext() ); //$NON-NLS-1$
      }
      return new StdRoundAxisRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

    @Override
    public String getConfigTextRepresentation( ViselRendererCfg aCfg ) {
      AxisMarking am = aCfg.propValues().getValobj( PROPID_AXIS_MARKING );
      return "" + am.bigTickQtty() + " делений";
    }
  };

  public static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setDouble( PROPID_RADIUS_SCALE, 0.85 );
    opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
    opSet.setValobj( PROPID_AXIS_MARKING, AxisMarking.DEFAULT );

    opSet.setDouble( PROPID_BIG_TICK_LENGTH, 10 );
    opSet.setDouble( PROPID_MID_TICK_LENGTH, 60 );
    opSet.setDouble( PROPID_LIT_TICK_LENGTH, 30 );

    opSet.setBool( PROPID_OUTER_TICKS, true );

    opSet.setValobj( TFI_FG_COLOR.id(), ETsColor.BLACK.rgba() );
    IOptionSetEdit opSetRgba = new OptionSet();
    opSetRgba.setValobj( TsColorSourceKindRgba.OPDEF_RGBA, new RGBA( 0, 0, 0, 255 ) );
    TsColorDescriptor tsd = TsColorSourceKindRgba.INSTANCE.createDescriptor( opSetRgba );
    opSet.setValobj( TFI_TEXT_COLOR.id(), tsd );
    opSet.setValobj( PROPID_FONT, FontInfo.DEFAULT );
    opSet.setStr( PROPID_ANNOTATIONS, "10;20;30;40;50" );
    opSet.setDouble( PROPID_TEXT_INDENT, 1.0 );
    opSet.setBool( PROPID_ROTATE_LABELS, false );
    opSet.setBool( PROPID_DRAW_ARC, false );
    return new ViselRendererCfg( "rendCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsColorDescriptor textColorDescr;
  TsLineInfo        lineInfo       = TsLineInfo.DEFAULT;
  String            annotationsStr = "1;2;3;4;5";                                    //$NON-NLS-1$
  IStringListEdit   annotations    = new StringArrayList( "1", "2", "3", "4", "5" ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$

  AxisMarking axisMarking = AxisMarking.DEFAULT;

  FontInfo fontInfo = null;
  Font     font     = null;

  RGBA fgRgba = new RGBA( 0, 0, 0, 255 );

  double ownerRadius = 100;
  double radius      = 50;

  boolean drawArc = false;

  boolean rotateLabels = false;
  double  textIndent   = 1.0;
  // D2Point cx = new D2Point( 50., 50 );

  double cx = 50;
  double cy = 50;

  ViselRendererCfg rendererCfg = null;

  double startAngleGrad = 0;
  double deltaAngleGrad = 180;

  double bigTickLength = 10;
  double midTickLength = 60;
  double litTickLength = 30;

  boolean outerTicks = true;

  double tickIndent = 0; // отступ начала засечки от внешнего радиуса

  double radiusScale = 0.7;

  // class ViselListener
  // implements IPropertyChangeListener<IVedItem> {
  //
  // @Override
  // public void onPropsChanged( IVedItem aSource, IOptionSet aNewValues, IOptionSet aOldValues ) {
  // doOnViselPropsChanged( aSource, aNewValues, aOldValues );
  // }
  // }
  //
  // ViselListener viselPropsListener = new ViselListener();

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of data definitions
   * @param aCfg {@link ViselRendererCfg} - visel configeration
   * @param aVisel {@link IVedVisel} - the corresponding visel
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public StdRoundAxisRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
    // rendererCfg = new ViselRendererCfg( aCfg.id(), aCfg.kindId(), aCfg.factoryId(), aCfg.propValues(), aVisel.id() );
    // setPropValues( rendererCfg.propValues() );
    // aVisel.props().propsEventer().addListener( viselPropsListener );
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
    if( aChangedValues.hasKey( PROPID_OWNER_RADIUS ) ) {
      ownerRadius = aChangedValues.getDouble( PROPID_OWNER_RADIUS );
    }
    if( aChangedValues.hasKey( PROPID_OWNER_ANCHOR_X ) ) {
      cx = aChangedValues.getDouble( PROPID_OWNER_ANCHOR_X );
    }
    if( aChangedValues.hasKey( PROPID_OWNER_ANCHOR_Y ) ) {
      cy = aChangedValues.getDouble( PROPID_OWNER_ANCHOR_Y );
    }
    if( aChangedValues.hasKey( PROPID_START_ANGLE ) ) {
      startAngleGrad = aChangedValues.getDouble( PROPID_START_ANGLE );
    }
    if( aChangedValues.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngleGrad = aChangedValues.getDouble( PROPID_DELTA_ANGLE );
    }

    if( aChangedValues.hasKey( PROPID_RADIUS_SCALE ) ) {
      radiusScale = aChangedValues.getDouble( PROPID_RADIUS_SCALE );
    }
    if( aChangedValues.hasKey( PROPID_LINE_INFO ) ) {
      lineInfo = aChangedValues.getValobj( PROPID_LINE_INFO );
    }
    if( aChangedValues.hasKey( PROPID_AXIS_MARKING ) ) {
      axisMarking = aChangedValues.getValobj( PROPID_AXIS_MARKING );
    }
    if( aChangedValues.hasKey( PROPID_BIG_TICK_LENGTH ) ) {
      bigTickLength = aChangedValues.getDouble( PROPID_BIG_TICK_LENGTH );
    }
    if( aChangedValues.hasKey( PROPID_MID_TICK_LENGTH ) ) {
      midTickLength = aChangedValues.getDouble( PROPID_MID_TICK_LENGTH );
    }
    if( aChangedValues.hasKey( PROPID_LIT_TICK_LENGTH ) ) {
      litTickLength = aChangedValues.getDouble( PROPID_LIT_TICK_LENGTH );
    }
    if( aChangedValues.hasKey( PROPID_OUTER_TICKS ) ) {
      outerTicks = aChangedValues.getBool( PROPID_OUTER_TICKS );
      tickIndent = calcTickIndent();
    }
    if( aChangedValues.hasKey( TFI_FG_COLOR.id() ) ) {
      fgRgba = aChangedValues.getByKey( TFI_FG_COLOR.id() ).asValobj();
    }
    if( aChangedValues.hasKey( TFI_TEXT_COLOR.id() ) ) {
      IAtomicValue v = aChangedValues.getByKey( TFI_TEXT_COLOR.id() );
      textColorDescr = aChangedValues.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
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
    if( aChangedValues.hasKey( PROPID_TEXT_INDENT ) ) {
      textIndent = aChangedValues.getDouble( PROPID_TEXT_INDENT );
    }
    // updateCachedValues();
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
  // textColorDescr = aProps.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
  // }
  // if( aProps.hasKey( PROPID_WIDTH ) ) {
  // radius = aProps.getDouble( PROPID_WIDTH ) / 2.;
  // cx = new D2Point( radius, radius );
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
  // if( aProps.hasKey( ViselRoundAxis.PROPID_RENDERER_CFG ) ) {
  // rendererCfg = aProps.getValobj( ViselRoundAxis.PROPID_RENDERER_CFG );
  // updateCachedValues();
  // }
  // }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return FACTORY.doCreateTypeInfo();
  }

  @Override
  public void doPaint( ITsGraphicsContext aPaintContext ) {
    radius = ownerRadius * radiusScale;
    Display display = getDisplay();
    double btLength = (radius * bigTickLength) / 100.;
    double mtLength = (btLength * midTickLength) / 100.;
    double ltLength = (btLength * litTickLength) / 100.;
    aPaintContext.setForegroundRgba( fgRgba );
    if( drawArc ) {
      double arcR = radius;
      if( outerTicks ) {
        arcR = radius - btLength - tickIndent;
      }
      aPaintContext.drawArc( (int)(cx - arcR), (int)(cy - arcR), (int)(2 * arcR), (int)(2 * arcR), //
          (int)(-startAngleGrad - 180), (int)(-deltaAngleGrad) );
    }

    int ticksQtty = axisMarking.ticksQtty();
    double dAngleGrad = (deltaAngleGrad) / ticksQtty;
    for( int i = 0; i <= ticksQtty; i++ ) {
      double angleRad = Math.toRadians( startAngleGrad - 180 + i * dAngleGrad );
      double sin = Math.sin( angleRad );
      double cos = Math.cos( angleRad );
      if( i % axisMarking.bigTickNumber() == 0 ) {
        double x1 = cx + (radius - tickIndent) * cos;
        double y1 = cy + (radius - tickIndent) * sin;
        double x2 = cx + (radius - tickIndent - btLength) * cos;
        double y2 = cy + (radius - tickIndent - btLength) * sin;
        aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
      }
      else {
        if( axisMarking.midTickNumber() != 0 && i % axisMarking.midTickNumber() == 0 ) {
          double x1 = cx + (radius - tickIndent - (btLength - mtLength)) * cos;
          double y1 = cy + (radius - tickIndent - (btLength - mtLength)) * sin;
          double x2 = cx + (radius - tickIndent - btLength) * cos;
          double y2 = cy + (radius - tickIndent - btLength) * sin;
          if( !outerTicks ) {
            x1 = cx + (radius - tickIndent) * cos;
            y1 = cy + (radius - tickIndent) * sin;
            x2 = cx + (radius - tickIndent - mtLength) * cos;
            y2 = cy + (radius - tickIndent - mtLength) * sin;
          }
          aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
        }
        else {
          if( axisMarking.litTickNumber() != 0 ) {
            double x1 = cx + (radius - tickIndent - (btLength - ltLength)) * cos;
            double y1 = cy + (radius - tickIndent - (btLength - ltLength)) * sin;
            double x2 = cx + (radius - tickIndent - btLength) * cos;
            double y2 = cy + (radius - tickIndent - btLength) * sin;
            if( !outerTicks ) {
              x1 = cx + (radius - tickIndent) * cos;
              y1 = cy + (radius - tickIndent) * sin;
              x2 = cx + (radius - tickIndent - ltLength) * cos;
              y2 = cy + (radius - tickIndent - ltLength) * sin;
            }
            aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
          }
        }
      }
    }

    aPaintContext.gc().setFont( font );
    aPaintContext.gc().setForeground( textColorDescr.color( getDisplay() ) );
    int textIdx = 0;
    for( int i = 0; i <= ticksQtty; i++ ) {
      double angleGrad = startAngleGrad - 180 + i * dAngleGrad;
      double angleRad = Math.toRadians( angleGrad );
      double sin = Math.sin( angleRad );
      double cos = Math.cos( angleRad );
      if( i % axisMarking.bigTickNumber() == 0 ) {
        if( annotations.size() > textIdx ) {
          String text = annotations.get( textIdx );
          Point extent = aPaintContext.gc().textExtent( text );
          double textR = extent.y / 2.;
          if( !rotateLabels ) {
            textR = Math.max( extent.x, extent.y ) / 2. + textIndent;
          }
          double x = (radius - textR) * cos;
          double y = (radius - textR) * sin;
          if( !outerTicks ) {
            x = (radius - btLength - textR) * cos;
            y = (radius - btLength - textR) * sin;
          }
          if( rotateLabels ) {
            drawRotatedLabel( aPaintContext.gc(), display, text, //
                (int)(cx + x), (int)(cy + y), (float)angleGrad + 90, extent );
          }
          else {
            aPaintContext.gc().drawText( text, (int)(cx + x - extent.x / 2.), (int)(cy + y - extent.y / 2.), true );
          }
          textIdx++;
        }
      }
    }

  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // void updateCachedValues() {
  // if( rendererCfg != null ) {
  // drawArc = rendererCfg.propValues().getBool( PROPID_DRAW_ARC );
  // if( rendererCfg.propValues().hasKey( PROPID_ROTATE_LABELS ) ) {
  // rotateLabels = rendererCfg.propValues().getBool( PROPID_ROTATE_LABELS );
  // }
  // axisMarking = rendererCfg.propValues().getValobj( PROPID_AXIS_MARKING );
  // fgRgba = rendererCfg.propValues().getValobj( PROPID_FG_COLOR );
  // bigTickLength = rendererCfg.propValues().getDouble( PROPID_BIG_TICK_LENGTH );
  // midTickLength = rendererCfg.propValues().getDouble( PROPID_MID_TICK_LENGTH );
  // litTickLength = rendererCfg.propValues().getDouble( PROPID_LIT_TICK_LENGTH );
  // outerTicks = rendererCfg.propValues().getBool( PROPID_OUTER_TICKS );
  // tickIndent = calcTickIndent();
  // if( rendererCfg.propValues().hasKey( PROPID_RADIUS_SCALE ) ) {
  // radiusScale = rendererCfg.propValues().getDouble( PROPID_RADIUS_SCALE );
  // }
  // radius = (visel().props().getDouble( PROPID_WIDTH ) * radiusScale) / 200.;
  //
  // annotations.clear();
  // annotationsStr = rendererCfg.propValues().getByKey( TFI_ANNOTATIONS.id() ).asString();
  // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
  // while( st.hasMoreElements() ) {
  // annotations.add( st.nextToken().trim() );
  // }
  // }
  //
  // }

  // void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewValues, IOptionSet aOldValues ) {
  // if( aNewValues.hasKey( ViselRoundAxis.PROPID_START_ANGLE ) ) {
  // startAngleGrad = aNewValues.getDouble( ViselRoundAxis.PROPID_START_ANGLE );
  // }
  // if( aNewValues.hasKey( ViselRoundAxis.PROPID_DELTA_ANGLE ) ) {
  // deltaAngleGrad = aNewValues.getDouble( ViselRoundAxis.PROPID_DELTA_ANGLE );
  // }
  // if( aNewValues.hasKey( PROPID_LINE_INFO ) ) {
  // lineInfo = aNewValues.getValobj( PROPID_LINE_INFO );
  // }
  // if( aNewValues.hasKey( TFI_FG_COLOR.id() ) ) {
  // fgRgba = aNewValues.getByKey( TFI_FG_COLOR.id() ).asValobj();
  // }
  // if( aNewValues.hasKey( TFI_TEXT_COLOR.id() ) ) {
  // IAtomicValue v = aNewValues.getByKey( TFI_TEXT_COLOR.id() );
  // textColorDescr = aNewValues.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
  // }
  // if( aNewValues.hasKey( PROPID_WIDTH ) ) {
  // radius = aNewValues.getDouble( PROPID_WIDTH ) / 2.;
  // cx = new D2Point( radius, radius );
  // }
  // if( aNewValues.hasKey( ViselRoundAxis.PROPID_RENDERER_CFG ) ) {
  // updateCachedValues();
  // }
  // if( aNewValues.hasKey( PROPID_FONT ) ) {
  // fontInfo = aNewValues.getValobj( PROPID_FONT );
  // font = fontManager().getFont( fontInfo );
  // }
  // if( aNewValues.hasKey( TFI_ANNOTATIONS.id() ) ) {
  // annotations.clear();
  // annotationsStr = aNewValues.getByKey( TFI_ANNOTATIONS.id() ).asString();
  // StringTokenizer st = new StringTokenizer( annotationsStr, ";" ); //$NON-NLS-1$
  // while( st.hasMoreElements() ) {
  // annotations.add( st.nextToken().trim() );
  // }
  // }
  //
  // }

  private void drawRotatedLabel( GC gc, Display aDisplay, String aText, int aTcx, int aTcy, float aRotDeg,
      Point aExtent ) {
    // Display d = Display.getCurrent();
    Transform oldTransform = new Transform( aDisplay );
    gc.getTransform( oldTransform );

    // Размер текста для центрирования
    int tw = aExtent.x;
    int th = aExtent.y;

    Transform tr = new Transform( aDisplay );
    gc.getTransform( tr );
    // Переносим начало координат в точку подписи, поворачиваем, рисуем со смещением -tw/2, -th/2
    tr.translate( aTcx, aTcy );
    tr.rotate( aRotDeg );
    gc.setTransform( tr );

    // gc.setForeground( color );
    // drawText без фона (SWT.DRAW_TRANSPARENT)
    gc.setAdvanced( true );
    gc.setTextAntialias( SWT.ON );
    gc.drawText( aText, -tw / 2, -th / 2, true );

    // Восстанавливаем трансформацию
    gc.setTransform( oldTransform );
    oldTransform.dispose();
    tr.dispose();
  }

  private double calcTickIndent() {
    double dr = 0;
    if( outerTicks ) {
      dr += calcTextHeight() + textIndent;
    }
    return dr;
  }

  private int calcTextHeight() {
    if( !annotationsStr.isBlank() ) {
      GC gc = null;
      try {
        gc = new GC( getDisplay() );
        gc.setFont( font );
        return gc.textExtent( annotationsStr ).y;
      }
      finally {
        if( gc != null ) {
          gc.dispose();
        }
      }
    }
    return 0;
  }

}
