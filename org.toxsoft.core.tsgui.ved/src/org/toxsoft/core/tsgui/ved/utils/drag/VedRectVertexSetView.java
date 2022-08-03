package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор вершин прямоугольника.
 *
 * @author vs
 */
public class VedRectVertexSetView
    extends VedAbstractVertexSetView {

  Rectangle rect = new Rectangle( 0, 0, 0, 0 );

  Color colorBlue;

  ID2Conversion d2Conv = ID2Conversion.NONE;

  /**
   * Конструктор.<br>
   *
   * @param aContext ITsGuiContext - контекст окна
   */
  public VedRectVertexSetView( IVedScreen aVedScreen, ITsGuiContext aContext ) {
    super( aVedScreen );

    ITsColorManager cm = aContext.get( ITsColorManager.class );

    colorBlue = cm.getColor( ETsColor.BLUE );
    Color fgColor = cm.getColor( ETsColor.BLACK );
    Color bgColor = cm.getColor( ETsColor.RED );

    for( ETsFulcrum fulcrum : ETsFulcrum.values() ) {
      RectVertex vertex = new RectVertex( 8, 8, fgColor, bgColor, fulcrum );
      addVertex( vertex );
    }
  }

  // /**
  // * Конструктор.<br>
  // *
  // * @param aInitialRect Rectangle - начальный прямоугольник
  // * @param aContext ITsGuiContext - контекст окна
  // */
  // public VedRectVertexSetView( IVedScreen aScreen, ITsGuiContext aContext ) {
  // super( aScreen, aContext );
  //
  // // public VedRectVertexSetView( Rectangle aInitialRect, ITsGuiContext aContext ) {
  //
  // // updateRect( rect, aInitialRect, 2, 2 );
  //
  // ITsColorManager cm = aContext.get( ITsColorManager.class );
  //
  // colorBlue = cm.getColor( ETsColor.BLUE );
  // Color fgColor = cm.getColor( ETsColor.BLACK );
  // Color bgColor = cm.getColor( ETsColor.RED );
  //
  // for( ETsFulcrum fulcrum : ETsFulcrum.values() ) {
  // RectVertex vertex = new RectVertex( 8, 8, fgColor, bgColor, fulcrum );
  // addVertex( vertex );
  // }
  // updateVertexes();
  // }

  @Override
  public void paint( GC aGc ) {
    if( !visible() ) {
      return;
    }
    int oldStyle = aGc.getLineStyle();
    Color oldColor = aGc.getForeground();
    aGc.setLineWidth( 1 );
    aGc.setLineStyle( SWT.LINE_DASH );
    aGc.setForeground( colorBlue );

    aGc.drawRectangle( rect );

    aGc.setLineStyle( oldStyle );
    aGc.setForeground( oldColor );
  }

  // ------------------------------------------------------------------------------------
  // {@link IScreenObject}
  //

  // @Override
  // public ECursorType cursorType() {
  // return ECursorType.HAND;
  // }

  @Override
  public Rectangle bounds() {
    Rectangle bounds = null;
    for( RectVertex vertex : listVertexes() ) {
      if( bounds == null ) {
        bounds = vertex.bounds();
      }
      else {
        bounds = vertex.bounds().union( bounds );
      }
    }
    return bounds;
  }

  // @Override
  // public boolean containsScreenPoint( int aX, int aY ) {
  // return false;
  // }
  //
  // @Override
  // public boolean containsNormPoint( double aX, double aY ) {
  // return false;
  // }
  //
  // @Override
  // public <T> T entity() {
  // return null;
  // }

  // ------------------------------------------------------------------------------------
  // IVedVertexSetView
  //

  @Override
  protected void doInit() {
    ITsRectangle r = calcRect( componentViews() );
    if( r != null ) {
      rect.x = r.a().x() - 2;
      rect.y = r.a().y() - 2;
      rect.width = r.width() + 4;
      rect.height = r.height() + 4;
    }
    updateVertexes();
  }

  // @Override
  // public void init( ITsRectangle aRect ) {
  // Rectangle r = new Rectangle( aRect.a().x(), aRect.a().y(), aRect.width(), aRect.height() );
  // setRect( r );
  // }

  @SuppressWarnings( "unchecked" )
  @Override
  public IStridablesList<? extends RectVertex> listVertexes() {
    return (IStridablesList<RectVertex>)super.listVertexes();
  }

  @Override
  protected void onConversionChanged() {
    doInit();
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void doPaintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    paint( aGc );
    for( IVedVertex v : listVertexes() ) {
      v.paint( aGc );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Обновляет положения всех вершин и внутренний прямоугольник, при изменении одной из вершин.<br>
   *
   * @param aDx - смещение по оси x
   * @param aDy - смещение по оси y
   * @param aVertexId String - идентификатор вершины, один из ИДов элементов {@link ETsFulcrum}
   */
  @Override
  public void update( double aDx, double aDy, String aVertexId ) {
    ETsFulcrum fulcrum = ETsFulcrum.findById( aVertexId );
    switch( fulcrum ) {
      case TOP_CENTER: {
        rect.y += aDy;
        rect.height -= aDy;
        break;
      }
      case BOTTOM_CENTER: {
        rect.height += aDy;
        break;
      }
      case LEFT_CENTER: {
        rect.x += aDx;
        rect.width -= aDx;
        break;
      }
      case RIGHT_CENTER: {
        rect.width += aDx;
        break;
      }
      case LEFT_TOP: {
        rect.x += aDx;
        rect.y += aDy;
        rect.width -= aDx;
        rect.height -= aDy;
        break;
      }
      case RIGHT_BOTTOM: {
        rect.width += aDx;
        rect.height += aDy;
        break;
      }
      case RIGHT_TOP: {
        rect.y += aDy;
        rect.width += aDx;
        rect.height -= aDy;
        break;
      }
      case LEFT_BOTTOM: {
        rect.x += aDx;
        rect.width -= aDx;
        rect.height += aDy;
        break;
      }
      case CENTER: {
        rect.x += aDx;
        rect.y += aDy;
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateVertexes();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void setRect( Rectangle aShapeBounds ) {
    updateRect( rect, aShapeBounds, 2, 2 );
    updateVertexes();
  }

  private static void updateRect( Rectangle aRectDest, Rectangle aRectSource, int aDx, int aDy ) {
    aRectDest.x = aRectSource.x - aDx;
    aRectDest.y = aRectSource.y - aDy;
    aRectDest.width = aRectSource.width + 2 * aDx;
    aRectDest.height = aRectSource.height + 2 * aDy;
  }

  private void updateVertexes() {
    for( RectVertex vertex : listVertexes() ) {
      int x = rect.x;
      int y = rect.y;
      switch( vertex.fulcrum() ) {
        case LEFT_TOP:
          break;
        case TOP_CENTER:
          x += rect.width / 2;
          break;
        case BOTTOM_CENTER:
          x += rect.width / 2;
          y += rect.height;
          break;
        case CENTER:
          x += rect.width / 2;
          y += rect.height / 2;
          break;
        case LEFT_CENTER:
          y += rect.height / 2;
          break;
        case RIGHT_CENTER:
          x += rect.width;
          y += rect.height / 2;
          break;
        case RIGHT_BOTTOM:
          x += rect.width;
          y += rect.height;
          break;
        case RIGHT_TOP:
          x += rect.width;
          break;
        case LEFT_BOTTOM:
          y += rect.height;
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }

      vertex.setRect( new Rectangle( x - 4, y - 4, 8, 8 ) );
    }
  }

  private ITsRectangle calcRect( IStridablesList<IVedComponentView> aCompViews ) {
    ID2Convertor convertor = vedScreen().coorsConvertor();
    ITsRectangle r = null;
    for( IVedComponentView view : aCompViews ) {
      ITsRectangle tsRect = convertor.rectBounds( view.outline().bounds() );
      if( r == null ) {
        r = tsRect;
      }
      else {
        r = union( r, tsRect );
      }
    }
    return r;
  }

  private static ITsRectangle union( ITsRectangle r1, ITsRectangle r2 ) {
    int minX = Math.min( r1.a().x(), r2.a().x() );
    int minY = Math.min( r1.a().y(), r2.a().y() );
    int maxX = Math.max( r1.b().x(), r2.b().x() );
    int maxY = Math.max( r1.b().y(), r2.b().y() );

    return new TsRectangle( minX, minY, maxX - minX + 1, maxY - minY + 1 );
  }
}
