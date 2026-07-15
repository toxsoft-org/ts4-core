package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.render.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Отрисовщик дуги.
 *
 * @author vs
 */
public class ViselArcRenderer
    extends AbstractViselRenderer {

  static final String PROPID_THICKNESS = "thickness"; //$NON-NLS-1$

  /**
   * Thickness
   */
  public static final IDataDef PROP_THICKNESS = DataDef.create3( PROPID_THICKNESS, DT_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_THICKNESS, //
      TSID_DESCRIPTION, STR_VISEL_ARC_THICKNESS_D, //
      TSID_DEFAULT_VALUE, avFloat( 0 ) );

  static final IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();

  static {
    fields.add( TFI_OWNER_START_ANGLE );
    fields.add( TFI_OWNER_DELTA_ANGLE );
    fields.add( new TinFieldInfo( PROPID_THICKNESS, TTI_AT_FLOATING, PROP_THICKNESS.params() ) );
    fields.add( TFI_BK_FILL );
    fields.add( TFI_FG_COLOR );
    fields.add( TFI_LINE_INFO );
  }

  /**
   * Arc renderer kind id
   */
  public static final String KIND_ID = "arcRenderer"; //$NON-NLS-1$

  double     startAngle = 0;
  double     deltaAngle = 90;
  double     thickness  = 10;
  double     radius     = 100;
  RGBA       fgRgba;
  TsFillInfo fillInfo;
  TsLineInfo lineInfo;

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aPropDefs IStridablesList&lt;IDataDef> - list of datat definitions
   * @param aVisel {@link IVedVisel} - the corresponding VISEL
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public ViselArcRenderer( String aId, IStridablesList<IDataDef> aPropDefs, IVedVisel aVisel,
      ITsGuiContext aTsContext ) {
    super( aId, aPropDefs, aVisel, aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // IViselRenderer
  //

  @Override
  public String kindId() {
    return KIND_ID;
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return new PropertableEntitiesTinTypeInfo<>( fields, ViselArcRenderer.class );
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFieldInfoes() {
    return fields;
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = aChangedValues.getByKey( PROPID_FG_COLOR ).asValobj();
    }
    if( aChangedValues.hasKey( PROPID_START_ANGLE ) ) {
      startAngle = aChangedValues.getByKey( PROPID_START_ANGLE ).asDouble();
    }
    if( aChangedValues.hasKey( PROPID_DELTA_ANGLE ) ) {
      deltaAngle = aChangedValues.getByKey( PROPID_DELTA_ANGLE ).asDouble();
    }
    if( aChangedValues.hasKey( PROPID_WIDTH ) ) {
      radius = aChangedValues.getByKey( PROPID_WIDTH ).asDouble() / 2.;
    }
    if( aChangedValues.hasKey( PROPID_THICKNESS ) ) {
      thickness = aChangedValues.getByKey( PROPID_THICKNESS ).asDouble();
    }
    if( aChangedValues.hasKey( PROPID_BK_FILL ) ) {
      fillInfo = aChangedValues.getByKey( PROPID_BK_FILL ).asValobj();
    }
    if( aChangedValues.hasKey( PROPID_LINE_INFO ) ) {
      lineInfo = aChangedValues.getByKey( PROPID_LINE_INFO ).asValobj();
    }
  }

  @Override
  public void doPaint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( fillInfo );
    Path p = createPath();

    aPaintContext.fillPath( p, 0, 0, (int)(2 * radius), 2 * (int)radius );
    aPaintContext.setLineInfo( lineInfo );
    aPaintContext.setForegroundRgba( fgRgba );
    aPaintContext.drawPath( p, 0, 0 );
    aPaintContext.setForegroundRgba( new RGBA( 0, 0, 0, 255 ) );
    p.dispose();
  }

  // // ------------------------------------------------------------------------------------
  // // API
  // //
  //
  // public double startAngle() {
  // return startAngle;
  // }
  //
  // public double deltaAngle() {
  // return deltaAngle;
  // }
  //
  // public double radius() {
  // return radius;
  // }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Path createPath() {
    double r1 = radius - thickness;
    double r2 = radius;
    Path p = new Path( getDisplay() );

    double x;
    double y;

    double cx = r2;
    double cy = r2;

    double sAngle = Math.toRadians( startAngle );
    double dAngle = Math.toRadians( deltaAngle );
    double eAngle = sAngle + dAngle;
    x = r2 * Math.cos( -sAngle );
    y = r2 * Math.sin( -sAngle );
    D2Point p1 = new D2Point( cx + x, cy + y );

    x = r2 * Math.cos( -eAngle );
    y = r2 * Math.sin( -eAngle );
    D2Point p2 = new D2Point( cx + x, cy + y );

    x = r1 * Math.cos( -eAngle );
    y = r1 * Math.sin( -eAngle );
    D2Point p3 = new D2Point( cx + x, cy + y );

    x = r1 * Math.cos( -sAngle );
    y = r1 * Math.sin( -sAngle );
    D2Point p4 = new D2Point( cx + x, cy + y );

    if( lineInfo.capStyle() != ETsLineCapStyle.ROUND ) {
      p.moveTo( (float)p1.x(), (float)p1.y() );
      p.lineTo( (float)p4.x(), (float)p4.y() );
      p.addArc( 0, 0, (float)r2 * 2.f, (float)r2 * 2.f, (float)startAngle, (float)deltaAngle );

      float dx = (float)(thickness);
      float dy = (float)(thickness);
      p.addArc( dx, dy, (float)r1 * 2.f, (float)r1 * 2.f, (float)(startAngle + deltaAngle), -(float)deltaAngle );
      p.moveTo( (float)p2.x(), (float)p2.y() );
      p.lineTo( (float)p3.x(), (float)p3.y() );

      return p;
    }

    double dx = p1.x() - p4.x();
    double dy = p1.y() - p4.y();
    double length = Math.sqrt( dx * dx + dy * dy );
    double r = length / 2.;
    double cxx = Math.min( p1.x(), p4.x() ) + Math.abs( dx ) / 2;
    double cyy = Math.min( p1.y(), p4.y() ) + Math.abs( dy ) / 2;
    p.moveTo( (float)p1.x(), (float)p1.y() );
    p.addArc( (float)(cxx - r), (float)(cyy - r), (float)(2 * r), (float)(2 * r), 180 + (float)startAngle, 180 );
    p.addArc( 0, 0, (float)r2 * 2.f, (float)r2 * 2.f, (float)startAngle, (float)deltaAngle );

    cxx = Math.min( p2.x(), p3.x() ) + Math.abs( dx ) / 2;
    cyy = Math.min( p2.y(), p3.y() ) + Math.abs( dy ) / 2;
    p.addArc( (float)(cxx - r), (float)(cyy - r), (float)(2 * r), (float)(2 * r), (float)(startAngle + deltaAngle),
        180 );

    float dxx = (float)(thickness);
    float dyy = (float)(thickness);
    p.addArc( dxx, dyy, (float)r1 * 2.f, (float)r1 * 2.f, (float)(startAngle + deltaAngle), -(float)deltaAngle );

    // p.addString( "58", (float)(width() / 3.), (float)(height() / 3.), fontManager().getFont( "Arial", 48, SWT.BOLD )
    // );

    return p;
  }

  static double calcAngle1( double x, double y ) {
    double dx = Math.abs( x );
    double dy = Math.abs( y );
    double length = Math.sqrt( dx * dx + dy * dy );

    double alpha = Math.acos( x / length );
    if( length == 0 ) {
      return 0;
    }
    if( x >= 0 ) {
      if( y <= 0 ) { // 4 квадрант
        alpha = 4 * Math.PI / 2 - alpha;
      }
      else { // 1 квадрант do nothing
        // alpha = 0;
      }
    }
    else {
      if( y <= 0 ) { // 3 квадрант
        alpha = 2 * Math.PI - alpha;
      }
      else { // 2 квадрант do nothing
        // alpha = alpha + Math.PI / 2;
      }
    }
    return alpha;
  }

}
