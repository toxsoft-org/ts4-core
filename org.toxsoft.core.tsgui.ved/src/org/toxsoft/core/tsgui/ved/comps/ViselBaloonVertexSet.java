package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.ViselBaloon.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * {@link IVedVertexSet} implementation for {@link ViselBaloon} editing.
 *
 * @author vs
 */
public class ViselBaloonVertexSet
    extends VedFulcrumVertexSet {

  private static final String VID_ARC_WIDTH  = "arcWidth";   //$NON-NLS-1$
  private static final String VID_ARC_HEIGHT = "arcHeight";  //$NON-NLS-1$
  private static final String VID_NOSE_WIDTH = "noseWidth";  //$NON-NLS-1$
  private static final String VID_NOSE_TOP   = "noseLength"; //$NON-NLS-1$

  private Rectangle thisSwtRect = new Rectangle( 0, 0, 1, 1 );

  /**
   * Creates the vertex set.
   *
   * @param aVisel {@link VedAbstractVisel} - owner VISEL
   * @param aVedScreen {@link VedScreen} - the VED screen
   * @return {@link ViselRoundRectVertexSet} - created instance of {@link IVedVertexSet}
   */
  static ViselBaloonVertexSet create( VedAbstractVisel aVisel, VedScreen aVedScreen ) {
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
    v = new VedCircleVertex( VID_NOSE_TOP, 4., ECursorType.HAND );
    v.setBackground( cm.getColor( ETsColor.YELLOW ) );
    vertexes.add( v );
    v = new VedCircleVertex( VID_NOSE_WIDTH, 4., ECursorType.HAND );
    v.setBackground( cm.getColor( ETsColor.YELLOW ) );
    vertexes.add( v );
    return new ViselBaloonVertexSet( aVisel, vertexes, aVedScreen );
  }

  protected ViselBaloonVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
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
    ViselBaloon baloon = (ViselBaloon)visel();
    if( aVertexId.equals( VID_ARC_WIDTH ) ) {
      double arcW = baloon.props().getDouble( PROPID_ARC_WIDTH ) - 2 * aDx;
      if( arcW < 0 ) {
        arcW = 0;
      }
      if( arcW > baloon.bounds().width() ) {
        arcW = baloon.bounds().width();
      }
      baloon.props().setDouble( PROPID_ARC_WIDTH, arcW );
    }
    if( aVertexId.equals( VID_ARC_HEIGHT ) ) {
      double arcH = baloon.props().getDouble( PROPID_ARC_HEIGHT ) + 2 * aDy;
      if( arcH < 0 ) {
        arcH = 0;
      }
      if( arcH > baloon.bounds().height() ) {
        arcH = baloon.bounds().height();
      }
      baloon.props().setDouble( PROPID_ARC_HEIGHT, arcH );
    }
    if( aVertexId.equals( VID_NOSE_WIDTH ) ) {
      ETsFulcrum fl = baloon.props().getValobj( PROP_NOSE_FULCRUM );
      double arcW = baloon.props().getDouble( PROPID_ARC_WIDTH );
      double arcH = baloon.props().getDouble( PROPID_ARC_HEIGHT );
      double nw = baloon.props().getDouble( PROP_NOSE_WIDTH );
      switch( fl ) {
        case BOTTOM_CENTER:
        case TOP_CENTER: {
          nw = nw + 2 * aDx;
          if( nw > baloon.bounds().width() - 2 * arcW ) {
            nw = baloon.bounds().width() - 2 * arcW;
          }
        }
          break;
        case RIGHT_CENTER:
        case LEFT_CENTER: {
          nw = nw + 2 * aDy;
          if( nw > baloon.bounds().height() - 2 * arcH ) {
            nw = baloon.bounds().height() - 2 * arcH;
          }
        }
          break;
        case CENTER:
        case LEFT_BOTTOM:
        case LEFT_TOP:
        case RIGHT_BOTTOM:
        case RIGHT_TOP:
        default:
          throw new IllegalArgumentException( "Unexpected value: " + fl ); //$NON-NLS-1$
      }
      if( nw < 2 ) {
        nw = 2;
      }
      baloon.props().setDouble( PROP_NOSE_WIDTH, nw );
    }
    if( aVertexId.equals( VID_NOSE_TOP ) ) {
      double delta = aDy;
      if( baloon.noseFulcrum().isLeft() || baloon.noseFulcrum().isRight() ) {
        delta = aDx;
      }
      double oldL = baloon.props().getDouble( PROPID_NOSE_LENGTH );
      double nl = oldL + delta;
      if( baloon.noseFulcrum().isLeft() || baloon.noseFulcrum().isTop() ) {
        nl = oldL - delta;
      }
      if( nl < 0 ) {
        nl = 0;
      }
      if( baloon.noseFulcrum().isLeft() || baloon.noseFulcrum().isRight() ) {
        baloon.props().setDouble( PROPID_WIDTH, baloon.props().getDouble( PROPID_WIDTH ) + nl - oldL );
      }
      else {
        if( baloon.noseFulcrum().isTop() ) {
          baloon.props().setDouble( PROPID_Y, baloon.props().getDouble( PROPID_Y ) - (nl - oldL) );
        }
        baloon.props().setDouble( PROPID_HEIGHT, baloon.props().getDouble( PROPID_HEIGHT ) + nl - oldL );
      }
      baloon.props().setDouble( PROPID_NOSE_LENGTH, nl );
    }
  }

  @Override
  protected void updateVertexes() {
    super.updateVertexes();
    clearHiddenIds();

    ViselBaloon baloon = (ViselBaloon)visel();

    double nw = baloon.props().getDouble( PROP_NOSE_WIDTH );
    double nl = baloon.props().getDouble( PROP_NOSE_LENGTH );
    ETsFulcrum f = baloon.props().getValobj( PROPID_NOSE_FULCRUM );
    setVertexVisible( f.id(), false );

    ID2Rectangle br = visel().bounds();
    for( IVedVertex v : vertexes() ) {
      double dVert = v.bounds().width() / 2.;
      if( v.id().equals( VID_ARC_WIDTH ) ) {
        int x = (int)(br.x1() + br.width() - baloon.props().getDouble( PROPID_ARC_WIDTH ) / 2.);
        v.setLocation( x - v.bounds().width() / 2., br.y1() - v.bounds().height() / 2. );
      }
      if( v.id().equals( VID_ARC_HEIGHT ) ) {
        double arcH = baloon.props().getDouble( PROPID_ARC_HEIGHT );
        int x = (int)(br.x1() + br.width());
        v.setLocation( x - v.bounds().width() / 2., (int)(br.y1() + arcH / 2. - v.bounds().height() / 2.) );
      }
      if( v.id().equals( VID_NOSE_TOP ) ) {
        ITsPoint p = baloon.noseTop();
        v.setLocation( p.x() - dVert + 1, p.y() - dVert );
      }
      if( v.id().equals( VID_NOSE_WIDTH ) ) {
        switch( f ) {
          case BOTTOM_CENTER: {
            double x = br.x1() + br.width() / 2. + nw / 2. - dVert;
            double y = br.y1() + br.height() - nl - dVert;
            v.setLocation( x, y );
          }
            break;
          case LEFT_CENTER: {
            double x = br.x1() + nl - dVert;
            double y = br.y1() + br.height() / 2. + nw / 2. - dVert;
            v.setLocation( x, y );
          }
            break;
          case RIGHT_CENTER: {
            double x = br.x1() + br.width() - nl + dVert;
            double y = br.y1() + br.height() / 2. + nw / 2. - dVert;
            v.setLocation( x, y );
          }
            break;
          case TOP_CENTER: {
            double x = br.x1() + br.width() / 2. + nw / 2. - dVert;
            double y = br.y1() + nl - dVert;
            v.setLocation( x, y );
          }
            break;
          case CENTER:
          case LEFT_BOTTOM:
          case LEFT_TOP:
          case RIGHT_BOTTOM:
          case RIGHT_TOP:
          default:
            throw new IllegalArgumentException( "Unexpected value: " + f ); //$NON-NLS-1$
        }

      }
    }
    updateSwtRect();
  }

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
