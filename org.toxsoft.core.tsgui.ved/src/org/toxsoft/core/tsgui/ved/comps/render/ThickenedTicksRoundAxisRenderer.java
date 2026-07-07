package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.util.*;

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
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Стандартный отрисовщик круговой шкалы.
 *
 * @author vs
 */
public class ThickenedTicksRoundAxisRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".thickenedTicksRoundAxisRenderer"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "roundAxisRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_ROUND_AXIS_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_ROUND_AXIS_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  static final String PROPID_ANNOTATIONS     = "annotations";   //$NON-NLS-1$
  static final String PROPID_AXIS_MARKING    = "axisMarking";   //$NON-NLS-1$
  static final String PROPID_DRAW_ARC        = "drawArc";       //$NON-NLS-1$
  static final String PROPID_OUTER_TICKS     = "outerTicks";    //$NON-NLS-1$
  static final String PROPID_BIG_TICK_LENGTH = "btLength";      //$NON-NLS-1$
  static final String PROPID_MID_TICK_LENGTH = "mtLength";      //$NON-NLS-1$
  static final String PROPID_LIT_TICK_LENGTH = "ltLength";      //$NON-NLS-1$
  static final String PROPID_THICK_LINE_INFO = "thickLineInfo"; //$NON-NLS-1$

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

  static final ITinFieldInfo TFI_THICK_LINE_INFO = TtiUtils.typedFieldInfo( PROPID_THICK_LINE_INFO,
      TtiTsLineInfo.INSTANCE, "Утолщение", "Свойства утолщенной линии большой засечки" );

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new AbstractCircularRendererFactory( FACTORY_ID, //
      TSID_NAME, "Renderer", //
      TSID_DESCRIPTION, "Thicked tick round axis renderer", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    protected void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields ) {
      fields.add( TFI_AXIS_MARKING );
      fields.add( TFI_OUTER_TICKS );

      fields.add( TFI_BIG_TICK_LENGTH );
      fields.add( TFI_MID_TICK_LENGTH );
      fields.add( TFI_LIT_TICK_LENGTH );

      fields.add( TFI_LINE_INFO );
      fields.add( TFI_THICK_LINE_INFO );
      fields.add( TFI_FG_COLOR );

      fields.add( TFI_FONT );
      fields.add( TFI_TEXT_COLOR );
      fields.add( TFI_ANNOTATIONS );
      fields.add( TtiUtils.booleanFieldInfo( PROPID_DRAW_ARC, "Рисовать дугу", "Рисовать дугу" ) );
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
      opSet.setValobj( PROPID_THICK_LINE_INFO, TsLineInfo.ofWidth( 3 ) );
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
      opSet.setBool( PROPID_DRAW_ARC, false );
      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      if( aCfg == null || aCfg.id().equals( "default" ) || aCfg.viselId().equals( "none" ) ) {
        ViselRendererCfg rCfg = defaultCfg( "none" );
        return new ThickenedTicksRoundAxisRenderer( "stdRend", propDefs(), rCfg, aVisel, aVedScreen.tsContext() ); //$NON-NLS-1$
      }
      return new ThickenedTicksRoundAxisRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  public static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
    opSet.setValobj( PROPID_THICK_LINE_INFO, TsLineInfo.ofWidth( 3 ) );
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
    opSet.setBool( PROPID_DRAW_ARC, false );
    return new ViselRendererCfg( "rendCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsColorDescriptor textColorDescr;
  TsLineInfo        lineInfo       = TsLineInfo.DEFAULT;
  TsLineInfo        thickLineInfo  = TsLineInfo.ofWidth( 3 );
  String            annotationsStr = "1;2;3;4;5";                                    //$NON-NLS-1$
  IStringListEdit   annotations    = new StringArrayList( "1", "2", "3", "4", "5" ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$

  AxisMarking axisMarking = AxisMarking.DEFAULT;

  FontInfo fontInfo = null;
  Font     font     = null;

  RGBA fgRgba = new RGBA( 0, 0, 0, 255 );

  double  radius  = 50;
  boolean drawArc = false;

  D2Point cx = new D2Point( 50., 50 );

  ViselRendererCfg rendererCfg = null;

  double startAngleGrad = 0;
  double deltaAngleGrad = 180;

  double bigTickLength = 10;
  double midTickLength = 60;
  double litTickLength = 30;

  boolean outerTicks = true;

  double tickIndent = 0; // отступ начала засечки от внешнего радиуса

  // class ViselListener
  // implements IPropertyChangeListener<IVedItem> {
  //
  // @Override
  // public void onPropsChanged( IVedItem aSource, IOptionSet aNewValues, IOptionSet aOldValues ) {
  // doOnViselPropsChanged( aSource, aNewValues, aOldValues );
  // }
  // }

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
  public ThickenedTicksRoundAxisRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg,
      IVedVisel aVisel, ITsGuiContext aTsContext ) {
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
      textColorDescr = aChangedValues.getByKey( TFI_TEXT_COLOR.id() ).asValobj();
    }
    if( aChangedValues.hasKey( PROPID_WIDTH ) ) {
      radius = aChangedValues.getDouble( PROPID_WIDTH ) / 2.;
      cx = new D2Point( radius, radius );
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
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return FACTORY.doCreateTypeInfo();
  }

  @Override
  public void doPaint( ITsGraphicsContext aPaintContext ) {
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
      aPaintContext.drawArc( (int)(cx.x() - arcR), (int)(cx.y() - arcR), (int)(2 * arcR), (int)(2 * arcR), //
          (int)(-startAngleGrad - 180), (int)(-deltaAngleGrad) );
    }

    int ticksQtty = axisMarking.ticksQtty();
    double dAngleGrad = (deltaAngleGrad) / ticksQtty;
    for( int i = 0; i <= ticksQtty; i++ ) {
      double angleRad = Math.toRadians( startAngleGrad - 180 + i * dAngleGrad );
      double sin = Math.sin( angleRad );
      double cos = Math.cos( angleRad );
      aPaintContext.setLineInfo( lineInfo );
      if( i % axisMarking.bigTickNumber() == 0 ) {
        double x1 = cx.x() + (radius - tickIndent) * cos;
        double y1 = cx.y() + (radius - tickIndent) * sin;
        double x2 = cx.x() + (radius - tickIndent - btLength) * cos;
        double y2 = cx.y() + (radius - tickIndent - btLength) * sin;
        aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
        aPaintContext.setLineInfo( thickLineInfo );
        if( outerTicks ) {
          x2 = cx.x() + (radius - tickIndent - mtLength) * cos;
          y2 = cx.y() + (radius - tickIndent - mtLength) * sin;
          aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
        }
      }
      else {
        if( axisMarking.midTickNumber() != 0 && i % axisMarking.midTickNumber() == 0 ) {
          double x1 = cx.x() + (radius - tickIndent - (btLength - mtLength)) * cos;
          double y1 = cx.y() + (radius - tickIndent - (btLength - mtLength)) * sin;
          double x2 = cx.x() + (radius - tickIndent - btLength) * cos;
          double y2 = cx.y() + (radius - tickIndent - btLength) * sin;
          if( !outerTicks ) {
            x1 = cx.x() + (radius - tickIndent) * cos;
            y1 = cx.y() + (radius - tickIndent) * sin;
            x2 = cx.x() + (radius - tickIndent - mtLength) * cos;
            y2 = cx.y() + (radius - tickIndent - mtLength) * sin;
          }
          aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
        }
        else {
          if( axisMarking.litTickNumber() != 0 ) {
            double x1 = cx.x() + (radius - tickIndent - (btLength - ltLength)) * cos;
            double y1 = cx.y() + (radius - tickIndent - (btLength - ltLength)) * sin;
            double x2 = cx.x() + (radius - tickIndent - btLength) * cos;
            double y2 = cx.y() + (radius - tickIndent - btLength) * sin;
            if( !outerTicks ) {
              x1 = cx.x() + (radius - tickIndent) * cos;
              y1 = cx.y() + (radius - tickIndent) * sin;
              x2 = cx.x() + (radius - tickIndent - ltLength) * cos;
              y2 = cx.y() + (radius - tickIndent - ltLength) * sin;
            }
            aPaintContext.drawLine( (int)(x1), (int)(y1), (int)(x2), (int)(y2) );
          }
        }
      }
    }

    aPaintContext.gc().setFont( font );
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
          double x = (radius - extent.y / 2) * cos;
          double y = (radius - extent.y / 2) * sin;
          if( !outerTicks ) {
            x = (radius - btLength - extent.y / 2) * cos;
            y = (radius - btLength - extent.y / 2) * sin;
          }
          drawRotatedLabel( aPaintContext.gc(), display, text, (int)(cx.x() + x), (int)(cx.y() + y),
              (float)angleGrad + 90, extent );
          textIdx++;
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateCachedValues() {
    if( rendererCfg != null ) {
      drawArc = rendererCfg.propValues().getBool( PROPID_DRAW_ARC );
      lineInfo = rendererCfg.propValues().getValobj( PROPID_LINE_INFO );
      thickLineInfo = rendererCfg.propValues().getValobj( PROPID_THICK_LINE_INFO );
      axisMarking = rendererCfg.propValues().getValobj( PROPID_AXIS_MARKING );
      fgRgba = rendererCfg.propValues().getValobj( PROPID_FG_COLOR );
      bigTickLength = rendererCfg.propValues().getDouble( PROPID_BIG_TICK_LENGTH );
      midTickLength = rendererCfg.propValues().getDouble( PROPID_MID_TICK_LENGTH );
      litTickLength = rendererCfg.propValues().getDouble( PROPID_LIT_TICK_LENGTH );
      outerTicks = rendererCfg.propValues().getBool( PROPID_OUTER_TICKS );
      tickIndent = calcTickIndent();
    }

  }

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
    gc.setForeground( textColorDescr.color( aDisplay ) );
    gc.drawText( aText, -tw / 2, -th / 2, true );

    // Восстанавливаем трансформацию
    gc.setTransform( oldTransform );
    oldTransform.dispose();
    tr.dispose();
  }

  private double calcTickIndent() {
    double dr = 0;
    if( outerTicks ) {
      dr += calcTextHeight();
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
