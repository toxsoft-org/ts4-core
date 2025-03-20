package org.toxsoft.core.tsgui.ved.comps;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * {@link IVedVertexSet} implementation for {@link ViselRoundRect} editing.
 *
 * @author vs
 */
class ViselBezierVertexSet
    extends VedAbstractVertexSet {

  private static final Color SURROUNDING_RECT_DRAW_COLOR = new Color( new RGB( 0, 0, 128 ) );

  private static final String VID_START = "startVertex"; //$NON-NLS-1$
  private static final String VID_END   = "endVertex";   //$NON-NLS-1$
  private static final String VID_CTRL  = "ctrlVertex";  //$NON-NLS-1$

  private Rectangle thisSwtRect = new Rectangle( 0, 0, 1, 1 );

  double x1 = 0.;
  double y1 = 0.;
  double x2 = 100.;
  double y2 = 100.;

  /**
   * Creates the vertex set.
   *
   * @param aVisel {@link VedAbstractVisel} - owner VISEL
   * @param aVedScreen {@link VedScreen} - the VED screen
   * @return {@link ViselBezierVertexSet} - created instance of {@link IVedVertexSet}
   */
  static ViselBezierVertexSet create( VedAbstractVisel aVisel, VedScreen aVedScreen ) {
    ITsColorManager cm = aVedScreen.tsContext().get( ITsColorManager.class );
    IStridablesListEdit<IVedVertex> vertexes = new StridablesList<>();
    VedRectVertex v;
    v = new VedRectVertex( VID_START, 8., 8., ECursorType.SIZSTR_N_ALL );
    v.setBackground( cm.getColor( ETsColor.RED ) );
    vertexes.add( v );
    v = new VedRectVertex( VID_END, 8., 8., ECursorType.SIZSTR_N_ALL );
    v.setBackground( cm.getColor( ETsColor.RED ) );
    vertexes.add( v );
    VedCircleVertex cv = new VedCircleVertex( VID_CTRL, 6, ECursorType.SIZSTR_N_ALL );
    cv.setBackground( cm.getColor( ETsColor.YELLOW.rgb() ) );
    vertexes.add( cv );

    return new ViselBezierVertexSet( aVisel, vertexes, aVedScreen );
  }

  protected ViselBezierVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVisel, aVertexes, aVedScreen );
    updateVertexes();
    // updateSwtRect();
  }

  @Override
  protected void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewVals, IOptionSet aOldVals ) {
    updateVertexes();
    // updateSwtRect();
  }

  @Override
  protected boolean doOnVertexDrag( IVedVertex aVertex, int aSwtDx, int aSwtDy, EVedDragState aDragState ) {
    update( aSwtDx, aSwtDy, aVertex.id() );
    updateVertexes();
    return false;
  }

  @Override
  protected void doPaint( ITsGraphicsContext aGc ) {
    int oldStyle = aGc.gc().getLineStyle();
    Color oldColor = aGc.gc().getForeground();
    aGc.gc().setLineWidth( 1 );
    aGc.gc().setLineStyle( SWT.LINE_DASH );
    aGc.gc().setForeground( SURROUNDING_RECT_DRAW_COLOR );

    IVedVertex sv = vertexes().getByKey( VID_START );
    IVedVertex ev = vertexes().getByKey( VID_END );
    IVedVertex cv = vertexes().getByKey( VID_CTRL );

    double dx = sv.bounds().width() / 2.;
    double dy = sv.bounds().height() / 2.;
    double cx = cv.originX() + cv.bounds().width() / 2.;
    double cy = cv.originY() + cv.bounds().height() / 2.;

    aGc.gc().drawLine( (int)(sv.originX() + dx), (int)(sv.originY() + dy), (int)cx, (int)cy );
    aGc.gc().drawLine( (int)cx, (int)cy, (int)(ev.originX() + dx), (int)(ev.originY() + dy) );

    aGc.gc().setLineStyle( oldStyle );
    aGc.gc().setForeground( oldColor );

    for( IVedVertex v : vertexes() ) {
      if( !isVertexHidden( v.id() ) ) {
        v.paint( aGc );
      }
    }
  }

  // @Override
  protected void update( int aSwtDx, int aSwtDy, String aVertexId ) {
    ID2Point d2p = deltaSwt2Visel( aSwtDx, aSwtDy );
    ViselBezier curve = (ViselBezier)visel();
    if( aVertexId.equals( VID_START ) ) {
      D2Point sp = curve.props().getValobj( ViselBezier.FID_START );
      D2Point newPoint = new D2Point( sp.x() + d2p.x(), sp.y() + d2p.y() );
      curve.props().setValobj( ViselBezier.FID_START, newPoint );
    }
    if( aVertexId.equals( VID_END ) ) {
      D2Point ep = curve.props().getValobj( ViselBezier.FID_END );
      D2Point newPoint = new D2Point( ep.x() + d2p.x(), ep.y() + d2p.y() );
      curve.props().setValobj( ViselBezier.FID_END, newPoint );
    }
    if( aVertexId.equals( VID_CTRL ) ) {
      D2Point cp = curve.props().getValobj( ViselBezier.FID_CTRL );
      D2Point newPoint = new D2Point( cp.x() + d2p.x(), cp.y() + d2p.y() );
      curve.props().setValobj( ViselBezier.FID_CTRL, newPoint );
    }

  }

  // @Override
  protected void updateVertexes() {
    ViselBezier curve = (ViselBezier)visel();

    double x = curve.originX();
    double y = curve.originY();

    ID2Point sp = curve.props().getValobj( ViselBezier.FID_START );
    ID2Point ep = curve.props().getValobj( ViselBezier.FID_END );
    ID2Point cp = curve.props().getValobj( ViselBezier.FID_CTRL );

    IVedVertex v = vertexes().getByKey( VID_START );
    v.setLocation( sp.x() - x - v.bounds().width() / 2., sp.y() - y - v.bounds().height() / 2. );
    v = vertexes().getByKey( VID_END );
    v.setLocation( ep.x() - x - v.bounds().width() / 2., ep.y() - y - v.bounds().height() / 2. );
    v = vertexes().getByKey( VID_CTRL );
    v.setLocation( cp.x() - x - v.bounds().width() / 2., cp.y() - y - v.bounds().height() / 2. );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

}
