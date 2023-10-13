package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
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

  public static VedFulcrumVertexSet createWithFulcrums( VedAbstractVisel aVisel, VedScreen aVedScreen,
      ETsFulcrum... aFulcrums ) {
    return new VedFulcrumVertexSet( aVisel, listVertexesWithFulcrums( aFulcrums ), aVedScreen );
  }

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

  public static VedFulcrumVertexSet createExceptFulcrums( VedAbstractVisel aVisel, VedScreen aVedScreen,
      ETsFulcrum... aFulcrums ) {
    return new VedFulcrumVertexSet( aVisel, listVertexesWithoutFulcrums( aFulcrums ), aVedScreen );
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

  private static final Color colorBlue = new Color( new RGB( 0, 0, 128 ) );

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  protected VedFulcrumVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVisel, aVertexes, aVedScreen );
    if( aVertexes.size() > 0 ) {
      update( 0.0, 0.0, aVertexes.get( 0 ).id() );
    }
    updateVertexes();
    updateSwtRect();
  }

  @Override
  protected boolean doOnVertexDrag( IVedVertex aVertex, double aDx, double aDy, EVedDragState aDragState ) {
    update( aDx, aDy, aVertex.id() );
    updateVertexes();
    updateSwtRect();
    return true;
  }

  @Override
  protected void doPaint( GC aGc ) {
    TsGraphicsContext tgc = new TsGraphicsContext( aGc, tsContext() );
    int oldStyle = aGc.getLineStyle();
    Color oldColor = aGc.getForeground();
    aGc.setLineWidth( 1 );
    aGc.setLineStyle( SWT.LINE_DASH );
    aGc.setForeground( colorBlue );

    aGc.drawRectangle( swtRect );

    aGc.setLineStyle( oldStyle );
    aGc.setForeground( oldColor );

    for( IVedVertex v : vertexes() ) {
      if( !isVertexHidden( v.id() ) ) {
        v.paint( tgc );
      }
    }
  }

  @Override
  protected void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewVals, IOptionSet aOldVals ) {
    updateVertexes();
    updateSwtRect();
  }

  // ------------------------------------------------------------------------------------
  // Implementaton
  //

  protected void update( double aDx, double aDy, String aVertexId ) {
    org.toxsoft.core.tsgui.ved.screen.helpers.ID2Portable d2p = visel();
    if( d2p == null ) {
      return;
    }
    org.toxsoft.core.tsgui.ved.screen.helpers.ID2Resizable d2r = visel();

    ID2Rectangle rect = visel().bounds();

    if( ETsFulcrum.asList().hasKey( aVertexId ) ) {
      ETsFulcrum fulcrum = ETsFulcrum.getById( aVertexId );
      switch( fulcrum ) {
        case TOP_CENTER: {
          d2p.setLocation( rect.x1(), rect.y1() + aDy );
          if( d2r != null ) {
            d2r.setSize( rect.width(), rect.height() - aDy );
          }
          break;
        }
        case BOTTOM_CENTER: {
          if( d2r != null ) {
            d2r.setSize( rect.width(), rect.height() + aDy );
          }
          break;
        }
        case LEFT_CENTER: {
          d2p.setLocation( rect.x1() + aDx, rect.y1() );
          if( d2r != null ) {
            d2r.setSize( rect.width() - aDx, rect.height() );
          }
          break;
        }
        case RIGHT_CENTER: {
          if( d2r != null ) {
            d2r.setSize( rect.width() + aDx, rect.height() );
          }
          break;
        }
        case LEFT_TOP: {
          d2p.setLocation( rect.x1() + aDx, rect.y1() + aDy );
          if( d2r != null ) {
            d2r.setSize( rect.width() - aDx, rect.height() - aDy );
          }
          break;
        }
        case RIGHT_BOTTOM: {
          if( d2r != null ) {
            d2r.setSize( rect.width() + aDx, rect.height() + aDy );
          }
          break;
        }
        case RIGHT_TOP: {
          d2p.setLocation( rect.x1(), rect.y1() + aDy );
          if( d2r != null ) {
            d2r.setSize( rect.width() + aDx, rect.height() - aDy );
          }
          break;
        }
        case LEFT_BOTTOM: {
          d2p.setLocation( rect.x1() + aDx, rect.y1() );
          if( d2r != null ) {
            d2r.setSize( rect.width() - aDx, rect.height() + aDy );
          }
          break;
        }
        case CENTER: {
          d2p.setLocation( rect.x1() + aDx, rect.y1() + aDy );
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  protected void updateVertexes() {
    ID2Rectangle r = visel().bounds();
    ITsRectangle rect = new TsRectangle( (int)r.x1() + 1, (int)r.y1() + 1, (int)r.width() - 2, (int)r.height() - 2 );
    for( IVedVertex v : vertexes() ) {
      if( ETsFulcrum.asList().hasKey( v.id() ) ) {
        ETsFulcrum fulcrum = ETsFulcrum.getById( v.id() );
        int x = rect.x1();
        int y = rect.y1();
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
    swtRect.x = (int)Math.round( tsr.x1() + 2 );
    swtRect.y = (int)Math.round( tsr.y1() + 2 );
    swtRect.width = (int)Math.round( tsr.width() - 4 );
    swtRect.height = (int)Math.round( tsr.height() - 4 );
  }

}
