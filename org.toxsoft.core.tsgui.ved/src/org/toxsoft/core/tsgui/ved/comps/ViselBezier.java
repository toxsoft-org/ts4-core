package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: quadratic bezier curve.
 *
 * @author vs
 */
public class ViselBezier
    extends VedAbstractVisel {

  static final String PROPID_X2 = "x2"; //$NON-NLS-1$
  static final String PROPID_Y2 = "y2"; //$NON-NLS-1$

  private final static IDataDef PROP_X2 = DataDef.create( PROPID_X2, FLOATING, //
      TSID_NAME, STR_X, //
      TSID_DESCRIPTION, STR_X_D, //
      TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
  );

  private final static IDataDef PROP_Y2 = DataDef.create( PROPID_Y2, FLOATING, //
      TSID_NAME, STR_Y, //
      TSID_DESCRIPTION, STR_Y_D, //
      TSID_DEFAULT_VALUE, avFloat( 0.0 ) //
  );

  private final static ITinFieldInfo TFI_X2 = new TinFieldInfo( PROP_X2, TTI_AT_FLOATING );
  private final static ITinFieldInfo TFI_Y2 = new TinFieldInfo( PROP_Y2, TTI_AT_FLOATING );

  static final String FID_START = "startPoint"; //$NON-NLS-1$
  static final String FID_END   = "endPoint";   //$NON-NLS-1$
  static final String FID_CTRL  = "ctrlPoint";  //$NON-NLS-1$

  private final static ITinFieldInfo TFI_START = new TinFieldInfo( FID_START, TtiD2Point.INSTANCE, //
      TSID_NAME, "Начальная точка", //
      TSID_DESCRIPTION, "Начальная точка" //
  );

  private final static ITinFieldInfo TFI_END = new TinFieldInfo( FID_END, TtiD2Point.INSTANCE, //
      TSID_NAME, "Конечная точка", //
      TSID_DESCRIPTION, "Конечная точка" //
  );

  private final static ITinFieldInfo TFI_CTRL = new TinFieldInfo( FID_CTRL, TtiD2Point.INSTANCE, //
      TSID_NAME, "Контрольная точка", //
      TSID_DESCRIPTION, "Контрольная точка" //
  );

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.bezier2"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_LINE, //
      TSID_DESCRIPTION, STR_VISEL_LINE_D, //
      TSID_ICON_ID, ICONID_VISEL_SPLINE //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );

      fields.add( TFI_START );
      fields.add( TFI_END );
      fields.add( TFI_CTRL );

      // fields.add( TFI_X );
      // fields.add( TFI_Y );
      fields.add( TinFieldInfo.makeCopy( TFI_X, TSID_NAME, "x1", ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_Y, TSID_NAME, "y1", ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_X2, TSID_NAME, "x2" ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_Y2, TSID_NAME, "y2" ) ); //$NON-NLS-1$

      fields.add( TinFieldInfo.makeCopy( TFI_WIDTH, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TinFieldInfo.makeCopy( TFI_HEIGHT, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      // fields.add( TFI_WIDTH );
      // fields.add( TFI_HEIGHT );
      // fields.add( TinFieldInfo.makeCopy( TFI_X, TSID_NAME, "x2" ) ); //$NON-NLS-1$
      // fields.add( TinFieldInfo.makeCopy( TFI_Y, TSID_NAME, "y2" ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_FULCRUM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      // fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_COLOR_DESCRIPTOR );
      // fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselBezier.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselBezier( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setDouble( PROPID_WIDTH, 100.0 );
      cfg.propValues().setDouble( PROPID_HEIGHT, 100.0 );
      cfg.propValues().setValobj( PROPID_TS_FULCRUM, TsFulcrum.of( ETsFulcrum.LEFT_TOP ) );
      cfg.propValues().setValobj( FID_START, new D2Point( 0, 0 ) );
      cfg.propValues().setValobj( FID_END, new D2Point( 100, 100 ) );
      cfg.propValues().setValobj( FID_CTRL, new D2Point( 100, 0 ) );

      TsColorDescriptor cd = new TsColorDescriptor( TsColorSourceKindRgba.KIND_ID, //
          OptionSetUtils.createOpSet( TsColorSourceKindRgba.OPDEF_RGBA, avValobj( new RGBA( 0, 0, 0, 255 ) ) ) );
      cfg.propValues().setValobj( PROP_COLOR_DESCRIPTOR, cd );

      IVedItemsPaletteEntry pent1 = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent1 );
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
  public ViselBezier( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // ------------------------------------------------------------------------------------
  // ID2Portable
  //

  @Override
  public double originX() {
    double min = Math.min( startPoint.x(), endPoint.x() );
    return Math.min( min, ctrlPoint.x() );
  }

  @Override
  public double originY() {
    double min = Math.min( startPoint.y(), endPoint.y() );
    return Math.min( min, ctrlPoint.y() );
  }

  @Override
  public void setLocation( double aX, double aY ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    double deltaX = aX - originX();
    double deltaY = aY - originY();
    startPoint = new D2Point( startPoint.x() + deltaX, startPoint.y() + deltaY );
    endPoint = new D2Point( endPoint.x() + deltaX, endPoint.y() + deltaY );
    ctrlPoint = new D2Point( ctrlPoint.x() + deltaX, ctrlPoint.y() + deltaY );

    values.put( FID_START, avValobj( startPoint ) );
    values.put( FID_END, avValobj( endPoint ) );
    values.put( FID_CTRL, avValobj( ctrlPoint ) );

    props().setProps( values );
    // updatePoints( props() );
  }

  // ------------------------------------------------------------------------------------
  // ID2Resizable
  //

  @Override
  public double width() {
    // updatePoints( props() );
    return Math.abs( maxX() - originX() );
  }

  @Override
  public double height() {
    // updatePoints( props() );
    return Math.abs( maxY() - originY() );
  }

  @Override
  public void setSize( double aWidth, double aHeight ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( PROPID_WIDTH, avFloat( aWidth ) );
    values.put( PROPID_HEIGHT, avFloat( aHeight ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  TsLineInfo lineInfo = TsLineInfo.ofWidth( 1 );

  double x1 = 0.0;
  double y1 = 0.0;
  double x2 = 100.0;
  double y2 = 100.0;

  // double width = 1.0;
  // double height = 1.0;

  TsColorDescriptor colorDescr;

  Color color = new Color( ETsColor.BLACK.rgba() );

  LineSegment lineSegment = new LineSegment( x1, y1, x2, y2 );

  // D2Rectangle boundsRect;

  ID2Point startPoint;
  ID2Point endPoint;
  ID2Point ctrlPoint;

  void updatePoints( IOptionSet aOpSet ) {
    if( aOpSet.hasKey( PROPID_X ) ) {
      x1 = aOpSet.getDouble( PROPID_X );
    }
    if( aOpSet.hasKey( PROPID_Y ) ) {
      y1 = aOpSet.getDouble( PROPID_Y );
    }
    if( aOpSet.hasKey( PROPID_X2 ) ) {
      x2 = aOpSet.getDouble( PROPID_X2 );
    }
    if( aOpSet.hasKey( PROPID_Y2 ) ) {
      y2 = aOpSet.getDouble( PROPID_Y2 );
    }
  }

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {

    if( aValuesToSet.hasKey( FID_START ) ) {
      startPoint = aValuesToSet.getValobj( FID_START );
    }
    if( aValuesToSet.hasKey( FID_END ) ) {
      endPoint = aValuesToSet.getValobj( FID_END );
    }
    if( aValuesToSet.hasKey( FID_CTRL ) ) {
      ctrlPoint = aValuesToSet.getValobj( FID_CTRL );
    }

    // updatePoints( aValuesToSet );

    if( aValuesToSet.hasKey( TFI_LINE_INFO.id() ) ) {
      lineInfo = aValuesToSet.getValobj( TFI_LINE_INFO.id() );
    }

    if( aValuesToSet.hasKey( TFI_COLOR_DESCRIPTOR.id() ) ) {
      TsColorDescriptor cd = aValuesToSet.getValobj( TFI_COLOR_DESCRIPTOR.id() );
      color = cd.color( getDisplay() );
    }

    double minX = Math.min( x1, x2 );
    double minY = Math.min( y1, y2 );
    lineSegment = new LineSegment( x1 - minX, y1 - minY, x2 - minX, y2 - minY );
    // Rectangle2D r2d = lineSegment.bounds();
    // boundsRect = new D2Rectangle( r2d.getX(), r2d.getY(), r2d.getWidth(), r2d.getHeight() );

    aValuesToSet.setDouble( PROPID_WIDTH, Math.abs( x1 - x2 ) );
    aValuesToSet.setDouble( PROPID_HEIGHT, Math.abs( y1 - y2 ) );
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselBezierVertexSet.create( this, vedScreen() );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    Path path = null;
    try {
      float x = (float)originX();
      float y = (float)originY();
      path = new Path( getDisplay() );
      path.moveTo( (float)startPoint.x() - x, (float)startPoint.y() - y );
      path.quadTo( (float)ctrlPoint.x() - x, (float)ctrlPoint.y() - y, //
          (float)endPoint.x() - x, (float)endPoint.y() - y );

      aPaintContext.setForegroundRgb( color.getRGB() );
      aPaintContext.gc().setAlpha( color.getAlpha() );
      aPaintContext.setLineInfo( lineInfo );

      aPaintContext.gc().drawPath( path );
    }
    finally {
      if( path != null && !path.isDisposed() ) {
        path.dispose();
      }
    }
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    if( !super.isYours( aX, aY ) ) {
      return false;
    }
    Path path = null;
    GC gc = null;
    try {
      gc = new GC( getDisplay() );
      float x = (float)originX();
      float y = (float)originY();
      path = new Path( getDisplay() );
      path.moveTo( (float)startPoint.x() - x, (float)startPoint.y() - y );
      path.quadTo( (float)ctrlPoint.x() - x, (float)ctrlPoint.y() - y, //
          (float)endPoint.x() - x, (float)endPoint.y() - y );

      for( int i = 0; i < 8; i++ ) {
        for( int j = 0; j < 8; j++ ) {
          if( path.contains( (float)aX - 2 + i, (float)aY - 2 + j, gc, true ) ) {
            return true;
          }
        }
      }
      return false;
    }
    finally {
      if( path != null && !path.isDisposed() ) {
        path.dispose();
      }
      if( gc != null && !gc.isDisposed() ) {
        gc.dispose();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private double maxX() {
    double max = Math.max( startPoint.x(), endPoint.x() );
    return Math.max( max, ctrlPoint.x() );
  }

  private double maxY() {
    double max = Math.max( startPoint.y(), endPoint.y() );
    return Math.max( max, ctrlPoint.y() );
  }

  String pointIdWithMinX() {
    double min;
    String id;
    if( startPoint.x() < endPoint.x() ) {
      min = startPoint.x();
      id = FID_START;
    }
    else {
      min = endPoint.x();
      id = FID_END;
    }
    if( ctrlPoint.x() < min ) {
      return FID_CTRL;
    }
    return id;
  }

  String pointIdWithMinY() {
    double min;
    String id;
    if( startPoint.y() < endPoint.y() ) {
      min = startPoint.y();
      id = FID_START;
    }
    else {
      min = endPoint.y();
      id = FID_END;
    }
    if( ctrlPoint.y() < min ) {
      return FID_CTRL;
    }
    return id;
  }

}
