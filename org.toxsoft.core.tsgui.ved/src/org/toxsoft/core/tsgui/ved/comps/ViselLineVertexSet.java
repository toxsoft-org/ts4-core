package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link IVedVertexSet} implementation for {@link ViselRoundRect} editing.
 *
 * @author vs
 */
class ViselLineVertexSet
    extends VedAbstractVertexSet {

  private static final Color SURROUNDING_RECT_DRAW_COLOR = new Color( new RGB( 0, 0, 128 ) );

  private static final String VID_ONE_ADGE   = "oneAdge";   //$NON-NLS-1$
  private static final String VID_OTHER_ADGE = "otherAdge"; //$NON-NLS-1$

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
   * @return {@link ViselLineVertexSet} - created instance of {@link IVedVertexSet}
   */
  static ViselLineVertexSet create( VedAbstractVisel aVisel, VedScreen aVedScreen ) {
    ITsColorManager cm = aVedScreen.tsContext().get( ITsColorManager.class );
    IStridablesListEdit<IVedVertex> vertexes = new StridablesList<>();
    VedRectVertex v;
    v = new VedRectVertex( VID_ONE_ADGE, 8., 8., ECursorType.SIZSTR_N_ALL );
    v.setBackground( cm.getColor( ETsColor.RED ) );
    vertexes.add( v );
    v = new VedRectVertex( VID_OTHER_ADGE, 8., 8., ECursorType.SIZSTR_N_ALL );
    v.setBackground( cm.getColor( ETsColor.RED ) );
    vertexes.add( v );
    return new ViselLineVertexSet( aVisel, vertexes, aVedScreen );
  }

  protected ViselLineVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
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

    aGc.gc().drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
    aGc.gc().setLineStyle( oldStyle );
    aGc.gc().setForeground( oldColor );

    for( IVedVertex v : vertexes() ) {
      if( !isVertexHidden( v.id() ) ) {
        v.paint( aGc );
      }
    }
    // ID2Rectangle r = bounds();
    // aGc.drawRect( (int)r.x1(), (int)r.x2(), (int)r.width(), (int)r.height() );
  }

  // @Override
  protected void update( int aSwtDx, int aSwtDy, String aVertexId ) {
    // super.update( aSwtDx, aSwtDy, aVertexId );
    ID2Point d2p = deltaSwt2Visel( aSwtDx, aSwtDy );
    ViselLine line = (ViselLine)visel();
    if( aVertexId.equals( VID_ONE_ADGE ) ) {
      double x = line.props().getDouble( PROPID_X );
      double y = line.props().getDouble( PROPID_Y );
      x += d2p.x();
      y += d2p.y();
      IStringMapEdit<IAtomicValue> newValues = new StringMap<>();
      newValues.put( PROPID_X, AvUtils.avFloat( x ) );
      newValues.put( PROPID_Y, AvUtils.avFloat( y ) );
      line.props().setProps( newValues );
    }
    if( aVertexId.equals( VID_OTHER_ADGE ) ) {
      double x = line.props().getDouble( ViselLine.PROPID_X2 );
      double y = line.props().getDouble( ViselLine.PROPID_Y2 );
      x += d2p.x();
      y += d2p.y();
      IStringMapEdit<IAtomicValue> newValues = new StringMap<>();
      newValues.put( ViselLine.PROPID_X2, AvUtils.avFloat( x ) );
      newValues.put( ViselLine.PROPID_Y2, AvUtils.avFloat( y ) );
      line.props().setProps( newValues );
    }
  }

  // @Override
  protected void updateVertexes() {
    ViselLine line = (ViselLine)visel();
    x1 = line.props().getDouble( PROPID_X );
    y1 = line.props().getDouble( PROPID_Y );
    x2 = line.props().getDouble( ViselLine.PROPID_X2 );
    y2 = line.props().getDouble( ViselLine.PROPID_Y2 );

    double minX = Math.min( x1, x2 );
    double minY = Math.min( y1, y2 );

    x1 = x1 - minX;
    y1 = y1 - minY;
    x2 = x2 - minX;
    y2 = y2 - minY;

    IVedVertex v1 = vertexes().get( 0 );
    v1.setLocation( x1 - v1.bounds().width() / 2., y1 - v1.bounds().height() / 2. );
    IVedVertex v2 = vertexes().get( 1 );
    v2.setLocation( x2 - v2.bounds().width() / 2., y2 - v2.bounds().height() / 2. );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateSwtRect() {
    if( thisSwtRect == null ) {
      thisSwtRect = new Rectangle( 0, 0, 1, 1 );
    }
    ID2Rectangle tsr = bounds();
    thisSwtRect.x = (int)Math.round( tsr.x1() + 2 );
    thisSwtRect.y = (int)Math.round( tsr.y1() + 2 );
    thisSwtRect.width = (int)Math.round( tsr.width() - 4 );
    thisSwtRect.height = (int)Math.round( tsr.height() - 4 );
  }

}
