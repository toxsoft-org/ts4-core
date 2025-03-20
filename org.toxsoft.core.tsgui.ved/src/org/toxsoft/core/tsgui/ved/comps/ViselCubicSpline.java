package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
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
 * The VISEL: cubic bezier curve.
 *
 * @author vs
 */
public class ViselCubicSpline
    extends VedAbstractVisel {

  static final String FID_START = "startPoint"; //$NON-NLS-1$
  static final String FID_END   = "endPoint";   //$NON-NLS-1$
  static final String FID_CTRL1 = "ctrlPoint1"; //$NON-NLS-1$
  static final String FID_CTRL2 = "ctrlPoint2"; //$NON-NLS-1$

  private final static ITinFieldInfo TFI_START = new TinFieldInfo( FID_START, TtiD2Point.INSTANCE, //
      TSID_NAME, "Начальная точка", //
      TSID_DESCRIPTION, "Начальная точка" //
  );

  private final static ITinFieldInfo TFI_END = new TinFieldInfo( FID_END, TtiD2Point.INSTANCE, //
      TSID_NAME, "Конечная точка", //
      TSID_DESCRIPTION, "Конечная точка" //
  );

  private final static ITinFieldInfo TFI_CTRL1 = new TinFieldInfo( FID_CTRL1, TtiD2Point.INSTANCE, //
      TSID_NAME, "Контрольная точка 1", //
      TSID_DESCRIPTION, "Контрольная точка 1" //
  );

  private final static ITinFieldInfo TFI_CTRL2 = new TinFieldInfo( FID_CTRL2, TtiD2Point.INSTANCE, //
      TSID_NAME, "Контрольная точка 2", //
      TSID_DESCRIPTION, "Контрольная точка 2" //
  );

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.cubicSpline"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_LINE, //
      TSID_DESCRIPTION, STR_VISEL_LINE_D, //
      TSID_ICON_ID, ICONID_VISEL_BEZIER //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );

      fields.add( TFI_START );
      fields.add( TFI_END );
      fields.add( TFI_CTRL1 );
      fields.add( TFI_CTRL2 );

      fields.add( TinFieldInfo.makeCopy( TFI_X, TSID_NAME, "x1", ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_Y, TSID_NAME, "y1", ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) ); //$NON-NLS-1$

      fields.add( TinFieldInfo.makeCopy( TFI_WIDTH, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TinFieldInfo.makeCopy( TFI_HEIGHT, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TinFieldInfo.makeCopy( TFI_FULCRUM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_COLOR_DESCRIPTOR );
      fields.add( TFI_ASPECT_RATIO );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselCubicSpline.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselCubicSpline( aCfg, propDefs(), aVedScreen );
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
      cfg.propValues().setValobj( FID_CTRL1, new D2Point( 100, 0 ) );
      cfg.propValues().setValobj( FID_CTRL2, new D2Point( 0, 100 ) );

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
  public ViselCubicSpline( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // ------------------------------------------------------------------------------------
  // ID2Portable
  //

  @Override
  public double originX() {
    double min = Math.min( startPoint.x(), endPoint.x() );
    return Math.min( min, ctrlPoint1.x() );
  }

  @Override
  public double originY() {
    double min = Math.min( startPoint.y(), endPoint.y() );
    return Math.min( min, ctrlPoint1.y() );
  }

  @Override
  public void setLocation( double aX, double aY ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    double deltaX = aX - originX();
    double deltaY = aY - originY();
    startPoint = new D2Point( startPoint.x() + deltaX, startPoint.y() + deltaY );
    endPoint = new D2Point( endPoint.x() + deltaX, endPoint.y() + deltaY );
    ctrlPoint1 = new D2Point( ctrlPoint1.x() + deltaX, ctrlPoint1.y() + deltaY );
    ctrlPoint2 = new D2Point( ctrlPoint2.x() + deltaX, ctrlPoint2.y() + deltaY );

    values.put( FID_START, avValobj( startPoint ) );
    values.put( FID_END, avValobj( endPoint ) );
    values.put( FID_CTRL1, avValobj( ctrlPoint1 ) );
    values.put( FID_CTRL2, avValobj( ctrlPoint2 ) );

    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // ID2Resizable
  //

  @Override
  public double width() {
    return Math.abs( maxX() - originX() );
  }

  @Override
  public double height() {
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

  TsColorDescriptor colorDescr;

  Color color = new Color( ETsColor.BLACK.rgba() );

  ID2Point startPoint;
  ID2Point endPoint;
  ID2Point ctrlPoint1;
  ID2Point ctrlPoint2;

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {

    if( aValuesToSet.hasKey( FID_START ) ) {
      startPoint = aValuesToSet.getValobj( FID_START );
    }
    if( aValuesToSet.hasKey( FID_END ) ) {
      endPoint = aValuesToSet.getValobj( FID_END );
    }
    if( aValuesToSet.hasKey( FID_CTRL1 ) ) {
      ctrlPoint1 = aValuesToSet.getValobj( FID_CTRL1 );
    }
    if( aValuesToSet.hasKey( FID_CTRL2 ) ) {
      ctrlPoint2 = aValuesToSet.getValobj( FID_CTRL2 );
    }

    if( aValuesToSet.hasKey( TFI_LINE_INFO.id() ) ) {
      lineInfo = aValuesToSet.getValobj( TFI_LINE_INFO.id() );
    }

    if( aValuesToSet.hasKey( TFI_COLOR_DESCRIPTOR.id() ) ) {
      TsColorDescriptor cd = aValuesToSet.getValobj( TFI_COLOR_DESCRIPTOR.id() );
      color = cd.color( getDisplay() );
    }
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselCubicSplineVertexSet.create( this, vedScreen() );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    Path path = null;
    try {
      float x = (float)originX();
      float y = (float)originY();
      path = new Path( getDisplay() );
      path.moveTo( (float)startPoint.x() - x, (float)startPoint.y() - y );
      path.cubicTo( (float)ctrlPoint1.x() - x, (float)ctrlPoint1.y() - y, //
          (float)ctrlPoint2.x() - x, (float)ctrlPoint2.y() - y, //
          (float)endPoint.x() - x, (float)endPoint.y() - y );

      aPaintContext.setForegroundRgb( color.getRGB() );
      aPaintContext.gc().setAlpha( color.getAlpha() );
      aPaintContext.setLineInfo( lineInfo );

      aPaintContext.drawPath( path, 0, 0 );
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
      path.cubicTo( (float)ctrlPoint1.x() - x, (float)ctrlPoint1.y() - y, //
          (float)ctrlPoint2.x() - x, (float)ctrlPoint2.y() - y, //
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
    max = Math.max( max, ctrlPoint1.x() );
    return Math.max( max, ctrlPoint2.x() );
  }

  private double maxY() {
    double max = Math.max( startPoint.y(), endPoint.y() );
    max = Math.max( max, ctrlPoint1.y() );
    return Math.max( max, ctrlPoint2.y() );
  }

}
