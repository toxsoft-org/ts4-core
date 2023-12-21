package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор вершин, представляющих собой подмножество опорных точек прямоугольника.
 *
 * @author vs
 */
public class VedFulcrumVertexSet
    extends VedAbstractVertexSet {

  private static final Color SURROUNDING_RECT_DRAW_COLOR = new Color( new RGB( 0, 0, 128 ) );

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  protected VedFulcrumVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVisel, aVertexes, aVedScreen );
    // FIXME зачем здесь update(), который вызывает изменение свойств визеля?????
    // if( aVertexes.size() > 0 ) {
    // update( 0.0, 0.0, aVertexes.get( 0 ).id() );
    // }
    updateVertexes();
    updateSwtRect();
  }

  /**
   * Creates instance with the vertexes of all fulcrums except the specified ones.
   *
   * @param aVisel {@link VedAbstractVertex} - the ownerVISEL
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @param aFulcrums {@link ETsFulcrum}[] - fulcrums to exclude
   * @return {@link VedFulcrumVertexSet} - created instance
   */
  public static VedFulcrumVertexSet createExceptFulcrums( VedAbstractVisel aVisel, VedScreen aVedScreen,
      ETsFulcrum... aFulcrums ) {
    return new VedFulcrumVertexSet( aVisel, listVertexesWithoutFulcrums( aFulcrums ), aVedScreen );
  }

  /**
   * Creates instance with the vertexes at the specified locations.
   *
   * @param aVisel {@link VedAbstractVertex} - the ownerVISEL
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @param aFulcrums {@link ETsFulcrum}[] - fulcrums to include
   * @return {@link VedFulcrumVertexSet} - created instance
   */
  public static VedFulcrumVertexSet createWithFulcrums( VedAbstractVisel aVisel, VedScreen aVedScreen,
      ETsFulcrum... aFulcrums ) {
    return new VedFulcrumVertexSet( aVisel, listVertexesWithFulcrums( aFulcrums ), aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // Implementaton
  //

  protected static IStridablesListEdit<IVedVertex> listVertexesWithFulcrums( ETsFulcrum... aFulcrums ) {
    ETsFulcrum[] fulcrums;
    if( aFulcrums.length <= 0 ) {
      fulcrums = ETsFulcrum.values();
    }
    else {
      fulcrums = new ETsFulcrum[aFulcrums.length];
    }

    IStridablesListEdit<IVedVertex> verts = new StridablesList<>();
    for( ETsFulcrum f : fulcrums ) {
      verts.add( new VedFulcrumVertex( f ) );
    }
    return verts;
  }

  protected static IStridablesListEdit<IVedVertex> listVertexesWithoutFulcrums( ETsFulcrum... aFulcrums ) {
    ETsFulcrum[] fulcrums;
    fulcrums = ETsFulcrum.values();

    IStridablesListEdit<IVedVertex> verts = new StridablesList<>();
    for( ETsFulcrum f : fulcrums ) {
      verts.add( new VedFulcrumVertex( f ) );
    }
    for( ETsFulcrum f : aFulcrums ) {
      verts.removeById( f.id() );
    }
    return verts;
  }

  protected void update( int aSwtDx, int aSwtDy, String aVertexId ) {
    VedAbstractVisel visel = visel();
    if( visel == null ) {
      return;
    }

    // ID2Rectangle rect = visel().bounds();
    double width = visel.props().getDouble( PROPID_WIDTH );
    double height = visel.props().getDouble( PROPID_HEIGHT );

    ID2Point d2pScr = deltaSwt2Screen( aSwtDx, aSwtDy ); // delta in screen coordiantes
    ID2Point d2p = deltaSwt2Visel( aSwtDx, aSwtDy ); // delta in visel ccordinates

    if( ETsFulcrum.asList().hasKey( aVertexId ) ) {
      ETsFulcrum fulcrum = ETsFulcrum.getById( aVertexId );
      TsFulcrum tsf = visel.props().getValobj( PROPID_TS_FULCRUM );
      ETsFulcrum viselFulcrum = null;
      if( tsf.fulcrum() != null ) {
        // d2p = deltaSwt2Screen( aSwtDx, aSwtDy );
        viselFulcrum = tsf.fulcrum();
      }
      double originDx = (width * tsf.xPerc()) / 100.;
      double originDy = (height * tsf.yPerc()) / 100.;
      switch( fulcrum ) {
        case TOP_CENTER: {
          if( ETsFulcrum.TOP_CENTER != viselFulcrum ) {
            if( viselFulcrum != null && viselFulcrum.isVerticalCenter() ) {
              visel.props().setPropPairs( PROP_HEIGHT, avFloat( height - 2 * d2p.y() ) );
            }
            else {
              visel.props().setPropPairs( PROP_HEIGHT, avFloat( height - d2pScr.y() ) );
            }
          }
          else {
            visel.props().setPropPairs( //
                PROP_X, avFloat( visel.originX() - deltaSwt2Visel( 0, aSwtDy ).x() ), //
                // PROP_Y, avFloat( visel.originY() + d2pScr.y() - deltaSwt2Screen( aSwtDx, 0 ).y() ), //
                PROP_Y, avFloat( visel.originY() + d2pScr.y() ), //
                PROP_HEIGHT, avFloat( height - d2pScr.y() + deltaSwt2Visel( aSwtDx, 0 ).y() ) );
          }
          break;
        }
        case BOTTOM_CENTER: {
          visel.props().setPropPairs( PROP_HEIGHT, avFloat( height + d2p.y() ) );
          break;
        }
        case LEFT_CENTER: {
          visel.props().setPropPairs( PROP_X, avFloat( visel.originX() + d2p.x() ) );
          visel.props().setPropPairs( PROP_WIDTH, avFloat( width - d2p.x() ) );
          break;
        }
        case RIGHT_CENTER: {
          visel.props().setPropPairs( PROP_WIDTH, avFloat( width + d2p.x() ) );
          break;
        }
        case LEFT_TOP: {
          visel.setLocation( visel.originX() + d2p.x(), visel.originY() + d2p.y() );
          visel.setSize( width - d2p.x(), height - d2p.y() );
          break;
        }
        case RIGHT_BOTTOM: {
          // visel.props().setPropPairs( PROP_WIDTH, avFloat( rect.width() + aDx ) );
          // visel.props().setPropPairs( PROP_HEIGHT, avFloat( rect.height() + aDy ) );
          visel.setSize( width + d2p.x(), height + d2p.y() );
          break;
        }
        case RIGHT_TOP: {
          visel.setLocation( visel.originX(), visel.originY() + d2p.y() );
          visel.setSize( width + d2p.x(), height - d2p.y() );
          break;
        }
        case LEFT_BOTTOM: {
          visel.setLocation( visel.originX() + d2p.x(), visel.originY() );
          visel.setSize( width - d2p.x(), height + d2p.y() );
          break;
        }
        case CENTER: {
          visel.setLocation( visel.originX() + d2pScr.x(), visel.originY() + d2pScr.y() );
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  protected void updateVertexes() {
    ID2Rectangle r = visel().bounds();
    int w = (int)r.width() - 2;
    if( w < 1 ) {
      w = 1;
    }
    int h = (int)r.height() - 2;
    if( h < 1 ) {
      h = 1;
    }

    // ITsRectangle rect = new TsRectangle( (int)r.x1() + 1, (int)r.y1() + 1, w, h );
    ITsRectangle rect = new TsRectangle( 1, 1, w, h );
    for( IVedVertex v : vertexes() ) {
      if( ETsFulcrum.asList().hasKey( v.id() ) ) {
        ETsFulcrum fulcrum = ETsFulcrum.getById( v.id() );
        // int x = rect.x1();
        // int y = rect.y1();
        int x = 0;
        int y = 0;
        switch( fulcrum ) {
          case LEFT_TOP:
            break;
          case TOP_CENTER:
            x += rect.width() / 2;
            break;
          case BOTTOM_CENTER:
            x += rect.width() / 2;
            y += rect.height();
            break;
          case CENTER:
            x += rect.width() / 2;
            y += rect.height() / 2;
            break;
          case LEFT_CENTER:
            y += rect.height() / 2;
            break;
          case RIGHT_CENTER:
            x += rect.width();
            y += rect.height() / 2;
            break;
          case RIGHT_BOTTOM:
            x += rect.width();
            y += rect.height();
            break;
          case RIGHT_TOP:
            x += rect.width();
            break;
          case LEFT_BOTTOM:
            y += rect.height();
            break;
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
        v.setLocation( x - 4, y - 4 );
      }
    }
  }

  private void updateSwtRect() {
    ID2Rectangle tsr = bounds();
    // swtRect.x = (int)Math.round( tsr.x1() + 2 );
    // swtRect.y = (int)Math.round( tsr.y1() + 2 );
    swtRect.x = -1;
    swtRect.y = -1;
    swtRect.width = (int)Math.round( tsr.width() - 4 );
    swtRect.height = (int)Math.round( tsr.height() - 4 );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVertexSet
  //

  @Override
  protected boolean doOnVertexDrag( IVedVertex aVertex, int aSwtDx, int aSwtDy, EVedDragState aDragState ) {
    update( aSwtDx, aSwtDy, aVertex.id() );
    updateVertexes();
    updateSwtRect();
    return true;
  }

  @Override
  protected void doPaint( ITsGraphicsContext aGc ) {
    int oldStyle = aGc.gc().getLineStyle();
    Color oldColor = aGc.gc().getForeground();
    aGc.gc().setLineWidth( 1 );
    aGc.gc().setLineStyle( SWT.LINE_DASH );
    aGc.gc().setForeground( SURROUNDING_RECT_DRAW_COLOR );

    aGc.gc().drawRectangle( swtRect );
    aGc.gc().setLineStyle( oldStyle );
    aGc.gc().setForeground( oldColor );

    for( IVedVertex v : vertexes() ) {
      if( !isVertexHidden( v.id() ) ) {
        v.paint( aGc );
      }
    }
  }

  @Override
  protected void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewVals, IOptionSet aOldVals ) {
    updateVertexes();
    updateSwtRect();
  }

}
