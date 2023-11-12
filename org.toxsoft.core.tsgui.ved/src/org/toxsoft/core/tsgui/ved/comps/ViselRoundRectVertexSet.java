package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ViselRoundRect.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * {@link IVedVertexSet} implementation for {@link ViselRoundRect} editing.
 *
 * @author vs
 */
class ViselRoundRectVertexSet
    extends VedFulcrumVertexSet {

  private static final String VID_ARC_WIDTH  = "arcWidth";  //$NON-NLS-1$
  private static final String VID_ARC_HEIGHT = "arcHeight"; //$NON-NLS-1$

  private Rectangle thisSwtRect = new Rectangle( 0, 0, 1, 1 );

  /**
   * Creates the vertex set.
   *
   * @param aVisel {@link VedAbstractVisel} - owner VISEL
   * @param aVedScreen {@link VedScreen} - the VED screen
   * @return {@link ViselRoundRectVertexSet} - created instance of {@link IVedVertexSet}
   */
  static ViselRoundRectVertexSet create( VedAbstractVisel aVisel, VedScreen aVedScreen ) {
    ITsColorManager cm = aVedScreen.tsContext().get( ITsColorManager.class );
    IStridablesListEdit<IVedVertex> vertexes;
    vertexes = VedFulcrumVertexSet.listVertexesWithoutFulcrums( ETsFulcrum.RIGHT_TOP, ETsFulcrum.RIGHT_CENTER,
        ETsFulcrum.TOP_CENTER );
    VedCircleVertex v;
    v = new VedCircleVertex( VID_ARC_WIDTH, 4., ECursorType.SIZSTR_N_WEST_EAST );
    v.setBackground( cm.getColor( ETsColor.YELLOW ) );
    vertexes.add( v );
    v = new VedCircleVertex( VID_ARC_HEIGHT, 4., ECursorType.SIZSTR_N_NORTH_SOUTH );
    v.setBackground( cm.getColor( ETsColor.YELLOW ) );
    vertexes.add( v );
    return new ViselRoundRectVertexSet( aVisel, vertexes, aVedScreen );
  }

  protected ViselRoundRectVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVisel, aVertexes, aVedScreen );
  }

  @Override
  protected void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewVals, IOptionSet aOldVals ) {
    updateVertexes();
    updateSwtRect();
  }

  @Override
  protected void update( double aDx, double aDy, String aVertexId ) {
    super.update( aDx, aDy, aVertexId );
    ViselRoundRect roundRect = (ViselRoundRect)visel();
    if( aVertexId.equals( VID_ARC_WIDTH ) ) {
      double arcW = roundRect.props().getDouble( PROPID_ARC_WIDTH ) - 2 * aDx;
      if( arcW < 0 ) {
        arcW = 0;
      }
      if( arcW > roundRect.bounds().width() ) {
        arcW = roundRect.bounds().width();
      }
      roundRect.props().setDouble( PROPID_ARC_WIDTH, arcW );
    }
    if( aVertexId.equals( VID_ARC_HEIGHT ) ) {
      double arcH = roundRect.props().getDouble( PROPID_ARC_HEIGHT ) + 2 * aDy;
      if( arcH < 0 ) {
        arcH = 0;
      }
      if( arcH > roundRect.bounds().height() ) {
        arcH = roundRect.bounds().height();
      }
      roundRect.props().setDouble( PROPID_ARC_HEIGHT, arcH );
    }
  }

  @Override
  protected void updateVertexes() {
    super.updateVertexes();
    clearHiddenIds();
    ViselRoundRect baloon = (ViselRoundRect)visel();
    ID2Rectangle br = visel().bounds();
    for( IVedVertex v : vertexes() ) {
      if( v.id().equals( VID_ARC_WIDTH ) ) {
        int x = (int)(br.x1() + br.width() - baloon.props().getDouble( PROPID_ARC_WIDTH ) / 2.);
        v.setLocation( x - v.bounds().width() / 2., br.y1() - v.bounds().height() / 2. );
      }
      if( v.id().equals( VID_ARC_HEIGHT ) ) {
        double arcH = baloon.props().getDouble( PROPID_ARC_HEIGHT );
        int x = (int)(br.x1() + br.width());
        v.setLocation( x - v.bounds().width() / 2., (int)(br.y1() + arcH / 2. - v.bounds().height() / 2.) );
      }
    }
    updateSwtRect();
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
