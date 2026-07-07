package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Стандартный отрисовщик {@link ViselRoundGauge}.
 *
 * @author vs
 */
public class StdRoundGaugeRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".stdRoundGougeRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "roundGaugeRenderer"; //$NON-NLS-1$

  static final IDataType DT_STD_ROUND_GAUGE_RENDERER = //
      DataType.create( VedAbstractRendererFactory.DT_TS_RENDERER_CFG, //
          TSID_DEFAULT_VALUE, avValobj( defaultCfg( "none" ) ) );

  public static final ITinTypeInfo tinTypeInfo() {
    IDataType dt = DataType.create( DT_STD_ROUND_GAUGE_RENDERER );
    return new TinAtomicTypeInfo.TtiValobj<>( dt, ViselRendererCfg.class );
  }

  static final String PROPID_VALUE_FILL       = "valueFill";      //$NON-NLS-1$
  static final String PROPID_VALUE_LINE_INFO  = "valueLineInfo";  //$NON-NLS-1$
  static final String PROPID_VALUE_LINE_COLOR = "valueLineColor"; //$NON-NLS-1$
  static final String PROPID_THICKNESS        = "thickness";      //$NON-NLS-1$

  static final ITinFieldInfo TFI_VALUE_FILL = TtiUtils.typedFieldInfo( PROPID_VALUE_FILL, TtiTsFillInfo.INSTANCE, //
      "Фон значения", "Параметры заливки фона дуги, отображающей значение" );

  static final ITinFieldInfo TFI_VALUE_LINE_INFO =
      TtiUtils.typedFieldInfo( PROPID_VALUE_LINE_INFO, TtiTsLineInfo.INSTANCE, //
          "Линия значения", "Параметры линии границы дуги, отображающей значение" );

  static final ITinFieldInfo TFI_VALUE_LINE_COLOR = TtiUtils.typedFieldInfo( PROPID_VALUE_LINE_COLOR, TtiRGBA.INSTANCE, //
      "Цвет линии значения", "Цвет линии границы дуги, отображающей значение" );

  static final ITinFieldInfo TFI_THICKNESS = TtiUtils.doubleFieldInfo( PROPID_THICKNESS, //
      "Толщина дуги", "Толщина дуги" );

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
      aFields.add( TFI_THICKNESS );

      aFields.add( TFI_VALUE_LINE_INFO );
      aFields.add( TFI_VALUE_LINE_COLOR );
      aFields.add( TFI_VALUE_FILL );

      aFields.add( TFI_LINE_INFO );
      aFields.add( TFI_FG_COLOR );
      aFields.add( TFI_BK_FILL );
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setDouble( PROPID_THICKNESS, 24. );
      opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
      opSet.setValobj( PROPID_VALUE_LINE_INFO, TsLineInfo.DEFAULT );

      opSet.setValobj( PROPID_VALUE_LINE_COLOR, new RGBA( 0, 0, 00, 255 ) );
      opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );

      opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 128, 128, 128, 255 ) ) );
      opSet.setValobj( PROPID_VALUE_FILL, new TsFillInfo( new RGBA( 0, 0, 200, 255 ) ) );

      return new ViselRendererCfg( aId, KIND_ID, FACTORY_ID, opSet, aViselId );
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      if( aCfg == null || aCfg.id().equals( "default" ) || aCfg.viselId().equals( "none" ) ) {
        ViselRendererCfg rCfg = defaultCfg( aVisel.id() );
        return new StdRoundGaugeRenderer( "stdRend", propDefs(), rCfg, aVisel, aVedScreen.tsContext() ); //$NON-NLS-1$
      }
      return new StdRoundGaugeRenderer( aCfg.id(), propDefs(), aCfg, aVisel, aVedScreen.tsContext() );
    }

  };

  private static final ViselRendererCfg defaultCfg( String aViselId ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setDouble( PROPID_THICKNESS, 24. );
    opSet.setValobj( PROPID_LINE_INFO, TsLineInfo.DEFAULT );
    opSet.setValobj( PROPID_VALUE_LINE_INFO, TsLineInfo.DEFAULT );

    opSet.setValobj( PROPID_VALUE_LINE_COLOR, new RGBA( 0, 0, 00, 255 ) );
    opSet.setValobj( PROPID_FG_COLOR, new RGBA( 0, 0, 0, 255 ) );

    opSet.setValobj( PROPID_BK_FILL, new TsFillInfo( new RGBA( 128, 128, 128, 255 ) ) );
    opSet.setValobj( PROPID_VALUE_FILL, new TsFillInfo( new RGBA( 0, 0, 200, 255 ) ) );
    return new ViselRendererCfg( "rendCfg", KIND_ID, FACTORY_ID, opSet, aViselId );
  }

  TsLineInfo lineInfo      = TsLineInfo.DEFAULT;
  TsLineInfo valueLineInfo = TsLineInfo.DEFAULT;

  TsFillInfo fillInfo;
  TsFillInfo valueFillInfo;

  double thickness = 24.;

  RGBA fgRgba    = new RGBA( 0, 0, 0, 255 );
  RGBA valueRgba = new RGBA( 0, 0, 0, 255 );

  double radius = 50;

  D2Point cp = new D2Point( 50., 50 ); // Center point

  ViselRendererCfg rendererCfg = null;

  double startAngleGrad      = 0;
  double deltaAngleGrad      = 180;
  double valueDeltaAngleGrad = 0;

  double minValue  = 0;
  double maxValue  = 100;
  double currValue = 50;

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
  public StdRoundGaugeRenderer( String aId, IStridablesList<IDataDef> aPropDefs, ViselRendererCfg aCfg,
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
    if( aChangedValues.hasKey( PROPID_WIDTH ) ) {
      radius = aChangedValues.getDouble( PROPID_WIDTH ) / 2.;
      cp = new D2Point( radius, radius );
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_START_ANGLE ) ) {
      startAngleGrad = aChangedValues.getDouble( ViselRoundGauge.PROPID_START_ANGLE );
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_DELTA_ANGLE ) ) {
      deltaAngleGrad = aChangedValues.getDouble( ViselRoundGauge.PROPID_DELTA_ANGLE );
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_RENDERER_CFG ) ) {
      rendererCfg = aChangedValues.getValobj( ViselRoundGauge.PROPID_RENDERER_CFG );
      updateCachedValues();
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_MIN_VALUE ) ) {
      minValue = aChangedValues.getDouble( ViselRoundGauge.PROPID_MIN_VALUE );
      if( currValue < minValue ) {
        currValue = minValue;
      }
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_MAX_VALUE ) ) {
      maxValue = aChangedValues.getDouble( ViselRoundGauge.PROPID_MAX_VALUE );
      if( currValue > maxValue ) {
        currValue = maxValue;
      }
    }
    if( aChangedValues.hasKey( ViselRoundGauge.PROPID_VALUE ) ) {
      currValue = aChangedValues.getDouble( ViselRoundGauge.PROPID_VALUE );
      if( currValue < minValue ) {
        currValue = minValue;
      }
      if( currValue > maxValue ) {
        currValue = maxValue;
      }
    }
    double dv = currValue - minValue;
    valueDeltaAngleGrad = dv * deltaAngleGrad / (maxValue - minValue);
  }

  // @Override
  // public void setPropValues( IOptionSet aProps ) {
  // if( aProps.hasKey( PROPID_WIDTH ) ) {
  // radius = aProps.getDouble( PROPID_WIDTH ) / 2.;
  // cp = new D2Point( radius, radius );
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_START_ANGLE ) ) {
  // startAngleGrad = aProps.getDouble( ViselRoundGauge.PROPID_START_ANGLE );
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_DELTA_ANGLE ) ) {
  // deltaAngleGrad = aProps.getDouble( ViselRoundGauge.PROPID_DELTA_ANGLE );
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_RENDERER_CFG ) ) {
  // rendererCfg = aProps.getValobj( ViselRoundGauge.PROPID_RENDERER_CFG );
  // updateCachedValues();
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_MIN_VALUE ) ) {
  // minValue = aProps.getDouble( ViselRoundGauge.PROPID_MIN_VALUE );
  // if( currValue < minValue ) {
  // currValue = minValue;
  // }
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_MAX_VALUE ) ) {
  // maxValue = aProps.getDouble( ViselRoundGauge.PROPID_MAX_VALUE );
  // if( currValue > maxValue ) {
  // currValue = maxValue;
  // }
  // }
  // if( aProps.hasKey( ViselRoundGauge.PROPID_VALUE ) ) {
  // currValue = aProps.getDouble( ViselRoundGauge.PROPID_VALUE );
  // if( currValue < minValue ) {
  // currValue = minValue;
  // }
  // if( currValue > maxValue ) {
  // currValue = maxValue;
  // }
  // }
  // double dv = currValue - minValue;
  // valueDeltaAngleGrad = dv * deltaAngleGrad / (maxValue - minValue);
  // }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return FACTORY.doCreateTypeInfo();
  }

  @Override
  public void doPaint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( fillInfo );
    Path p = createPath( (float)(-startAngleGrad - 180 - valueDeltaAngleGrad),
        (float)(-deltaAngleGrad + valueDeltaAngleGrad), lineInfo.capStyle() == ETsLineCapStyle.ROUND );

    aPaintContext.fillPath( p, 0, 0, (int)(2 * radius), 2 * (int)radius );
    aPaintContext.setLineInfo( lineInfo );
    aPaintContext.setForegroundRgba( fgRgba );
    aPaintContext.drawPath( p, 0, 0 );
    aPaintContext.setForegroundRgba( new RGBA( 0, 0, 0, 255 ) );
    p.dispose();

    p = createPath( (float)(-startAngleGrad - 180), -(float)valueDeltaAngleGrad,
        lineInfo.capStyle() == ETsLineCapStyle.ROUND );

    aPaintContext.setFillInfo( valueFillInfo );
    aPaintContext.fillPath( p, 0, 0, (int)(2 * radius), 2 * (int)radius );
    aPaintContext.setLineInfo( valueLineInfo );
    aPaintContext.setForegroundRgba( valueRgba );
    aPaintContext.drawPath( p, 0, 0 );
    aPaintContext.setForegroundRgba( new RGBA( 0, 0, 0, 255 ) );
    p.dispose();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateCachedValues() {
    if( rendererCfg != null ) {
      thickness = rendererCfg.propValues().getDouble( PROPID_THICKNESS );
      fgRgba = rendererCfg.propValues().getValobj( PROPID_FG_COLOR );
      valueRgba = rendererCfg.propValues().getValobj( PROPID_VALUE_LINE_COLOR );
      fillInfo = rendererCfg.propValues().getValobj( PROPID_BK_FILL );
      valueFillInfo = rendererCfg.propValues().getValobj( PROPID_VALUE_FILL );
      lineInfo = rendererCfg.propValues().getValobj( PROPID_LINE_INFO );
      valueLineInfo = rendererCfg.propValues().getValobj( PROPID_VALUE_LINE_INFO );
    }

  }

  // void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewValues, IOptionSet aOldValues ) {
  // if( aNewValues.hasKey( ViselRoundGauge.PROPID_START_ANGLE ) ) {
  // startAngleGrad = aNewValues.getDouble( ViselRoundAxis.PROPID_START_ANGLE );
  // }
  // if( aNewValues.hasKey( ViselRoundAxis.PROPID_DELTA_ANGLE ) ) {
  // deltaAngleGrad = aNewValues.getDouble( ViselRoundGauge.PROPID_DELTA_ANGLE );
  // }
  // if( aNewValues.hasKey( PROPID_LINE_INFO ) ) {
  // lineInfo = aNewValues.getValobj( PROPID_LINE_INFO );
  // }
  // if( aNewValues.hasKey( TFI_FG_COLOR.id() ) ) {
  // fgRgba = aNewValues.getByKey( TFI_FG_COLOR.id() ).asValobj();
  // }
  // if( aNewValues.hasKey( PROPID_WIDTH ) ) {
  // radius = aNewValues.getDouble( PROPID_WIDTH ) / 2.;
  // cp = new D2Point( radius, radius );
  // }
  // if( aNewValues.hasKey( ViselRoundAxis.PROPID_RENDERER_CFG ) ) {
  // updateCachedValues();
  // }
  // }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Рисует залитую дугу (кольцевой сегмент) через SWT Path. Система углов совпадает с SWT/математической: 0° = вправо,
   * положительный sweepDeg = против часовой стрелки.
   *
   * @param startDeg начальный угол в градусах
   * @param sweepDeg угол дуги в градусах (+ против часовой, - по часовой)
   * @param roundCaps true = закруглённые концы, false = плоские
   */
  Path createPath( float startDeg, float sweepDeg, boolean roundCaps ) {
    Display display = getDisplay();

    float cx = (float)cp.x();
    float cy = (float)cp.y();

    Path path = new Path( display );

    float half = (float)thickness / 2.0f;
    float outer = (float)radius;
    float inner = Math.max( 0.5f, (float)radius - 2 * half );

    float[] ox1 = pointOn( cx, cy, outer, startDeg );
    float[] ix1 = pointOn( cx, cy, inner, startDeg );
    float[] ox2 = pointOn( cx, cy, outer, startDeg + sweepDeg );
    float[] ix2 = pointOn( cx, cy, inner, startDeg + sweepDeg );

    if( roundCaps ) {
      path.moveTo( ox1[0], ox1[1] );
      // Внешняя дуга
      path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, startDeg, sweepDeg );
      // Полукруг на конечном торце: от ox2 к ix2
      addCapSemicircle( path, ox2[0], ox2[1], ix2[0], ix2[1], half, sweepDeg );
      // Внутренняя дуга обратно
      path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, startDeg + sweepDeg, -sweepDeg );
      // Полукруг на начальном торце: от ix1 к ox1
      addCapSemicircle( path, ix1[0], ix1[1], ox1[0], ox1[1], half, sweepDeg );
    }
    else {
      path.moveTo( ox1[0], ox1[1] );
      path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, startDeg, sweepDeg );
      path.lineTo( ix2[0], ix2[1] );
      path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, startDeg + sweepDeg, -sweepDeg );
      path.close();
    }
    return path;
  }

  /**
   * Точка на окружности радиуса R при математическом угле angleDeg. Экранная Y-ось смотрит вниз, поэтому y = cy -
   * R*sin(a).
   */
  private static float[] pointOn( float cx, float cy, float R, float angleDeg ) {
    double a = Math.toRadians( angleDeg );
    return new float[] { cx + (float)(R * Math.cos( a )), cy - (float)(R * Math.sin( a )) };
  }

  /**
   * Добавляет полукруг-торец между точками A=(fromX,fromY) и B=(toX,toY). A и B — концы диаметра полукруга (внешняя и
   * внутренняя точки торца). Полукруг выступает НАРУЖУ от кольца. Выбор направления обхода: Вектор A→B смотрит внутрь
   * (от внешней к внутренней точке). sweepDeg > 0 (CCW-контур) → полукруг идёт по часовой: arcAngle = -180 sweepDeg < 0
   * (CW-контур) → полукруг идёт против часовой: arcAngle = +180 Угол вектора A→B вычисляется в математической системе
   * (Y вверх): atan2 принимает экранный dy с инвертированным знаком.
   */
  private static void addCapSemicircle( Path path, float fromX, float fromY, float toX, float toY, float aRadius,
      float sweepDeg ) {
    float mx = (fromX + toX) / 2f;
    float my = (fromY + toY) / 2f;

    // Угол вектора A→B в математической системе координат (инвертируем Y)
    float phiMath = (float)Math.toDegrees( Math.atan2( -(toY - fromY), toX - fromX ) );

    float startAngle = phiMath + 180f;
    float arcAngle = sweepDeg > 0 ? 180f : -180f;

    path.addArc( mx - aRadius, my - aRadius, aRadius * 2, aRadius * 2, startAngle, arcAngle );
  }

}
