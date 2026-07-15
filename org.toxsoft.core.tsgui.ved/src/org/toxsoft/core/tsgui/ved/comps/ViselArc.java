package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.path.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: filled and outlined arc.
 *
 * @author vs
 */
public class ViselArc
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".arcViselFactory"; //$NON-NLS-1$

  static final String PROPID_START_ANGLE = "startAngle"; //$NON-NLS-1$
  static final String PROPID_DELTA_ANGLE = "deltaAngle"; //$NON-NLS-1$
  static final String PROPID_THICKNESS   = "thickness";  //$NON-NLS-1$

  /**
   * Thickness
   */
  public static final IDataDef PROP_THICKNESS = DataDef.create3( PROPID_THICKNESS, DT_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_THICKNESS, //
      TSID_DESCRIPTION, STR_VISEL_ARC_THICKNESS_D, //
      TSID_DEFAULT_VALUE, avFloat( 0 ) );

  static final ITinFieldInfo TFI_START_ANGLE = TtiUtils.doubleFieldInfo( PROPID_START_ANGLE, //
      "Начальный угол", "Начальный угол в градусах", 0 );

  static final ITinFieldInfo TFI_DELTA_ANGLE = TtiUtils.doubleFieldInfo( PROPID_DELTA_ANGLE, //
      "Приращение угла", "Приращение угла в градусах (+ против часовой стрелке)", 180 );

  static final ITinFieldInfo TFI_THICKNESS = TtiUtils.doubleFieldInfo( PROPID_THICKNESS, //
      "Толщина", "Толщина дуги в пикселях", 24 );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_ARC, //
      TSID_DESCRIPTION, STR_VISEL_ARC_D, //
      TSID_ICON_ID, ICONID_VISEL_ARC ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselArc( aCfg, propDefs(), aVedScreen );
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

      fields.add( TFI_START_ANGLE );
      fields.add( TFI_DELTA_ANGLE );
      fields.add( TFI_THICKNESS );

      fields.add( TFI_BK_FILL );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_ZOOM_HIDDEN );
      fields.add( TFI_ANGLE_HIDDEN );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE_HIDDEN );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselArc.class );
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
  public ViselArc( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  double     startAngle = 0;
  double     deltaAngle = 180;
  double     thickness  = 10;
  double     radius     = 100;
  RGBA       fgRgba;
  TsFillInfo fillInfo;
  TsLineInfo lineInfo;

  Path arcPath = null;

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = aChangedValue.getValobj( PROPID_FG_COLOR );
    }
    if( aChangedValue.hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = aChangedValue.getByKey( PROPID_FG_COLOR ).asValobj();
    }
    if( aChangedValue.hasKey( PROPID_START_ANGLE ) ) {
      startAngle = aChangedValue.getByKey( PROPID_START_ANGLE ).asDouble();
    }
    if( aChangedValue.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngle = aChangedValue.getByKey( PROPID_DELTA_ANGLE ).asDouble();
    }
    if( aChangedValue.hasKey( PROPID_WIDTH ) ) {
      radius = aChangedValue.getByKey( PROPID_WIDTH ).asDouble() / 2.;
    }
    if( aChangedValue.hasKey( PROPID_THICKNESS ) ) {
      thickness = aChangedValue.getByKey( PROPID_THICKNESS ).asDouble();
    }
    if( aChangedValue.hasKey( PROPID_BK_FILL ) ) {
      fillInfo = aChangedValue.getByKey( PROPID_BK_FILL ).asValobj();
    }
    if( aChangedValue.hasKey( PROPID_LINE_INFO ) ) {
      lineInfo = aChangedValue.getByKey( PROPID_LINE_INFO ).asValobj();
    }

    // renderer.setPropValues( aChangedValue );
    // updateSwtRect();
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    // renderer.paint( aPaintContext );
    TsLineInfo li = props().getValobj( TFI_LINE_INFO.id() );
    aPaintContext.setFillInfo( fillInfo );
    boolean roundEnd = li.capStyle() == ETsLineCapStyle.ROUND;
    // Path p = createPath( radius, thickness, startAngle, deltaAngle, roundEnd, getDisplay() );
    Path p;

    if( arcPath != null ) {
      arcPath.dispose();
    }
    arcPath =
        PathDataUtils.createArcPath( radius, thickness, (float)startAngle, (float)deltaAngle, roundEnd, getDisplay() );

    aPaintContext.fillPath( arcPath, 0, 0, (int)(2 * radius), 2 * (int)radius );
    aPaintContext.setLineInfo( li );
    aPaintContext.setForegroundRgba( fgRgba );
    aPaintContext.drawPath( arcPath, 0, 0 );
    aPaintContext.setForegroundRgba( new RGBA( 0, 0, 0, 255 ) );
    // p.dispose();
  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselArcVertexSet.create( this, vedScreen() );
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    // TODO вычислять исходя из геометрии не использовать GC
    GC gc = new GC( getDisplay() );
    boolean result = arcPath.contains( (float)aX, (float)aY, gc, false );
    gc.dispose();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает координаты начальной точки дуги.
   *
   * @return {@link ID2Point} - координаты начальной точки дуги
   */
  public ID2Point startPoint() {
    double sAngle = Math.toRadians( startAngle );
    double x = radius + radius * Math.cos( -sAngle );
    double y = radius + radius * Math.sin( -sAngle );
    return new D2Point( x, y );
  }

  /**
   * Возвращает координаты конечной точки дуги.
   *
   * @return {@link ID2Point} - координаты конечной точки дуги
   */
  public ID2Point endPoint() {
    double eAngle = Math.toRadians( startAngle + deltaAngle );
    double x = radius + radius * Math.cos( -eAngle );
    double y = radius + radius * Math.sin( -eAngle );
    return new D2Point( x, y );
  }

  /**
   * Возвращает координаты центральной точки дуги.
   *
   * @return {@link ID2Point} - координаты центральной точки дуги
   */
  public ID2Point centerPoint() {
    return new D2Point( radius, radius );
  }

}
