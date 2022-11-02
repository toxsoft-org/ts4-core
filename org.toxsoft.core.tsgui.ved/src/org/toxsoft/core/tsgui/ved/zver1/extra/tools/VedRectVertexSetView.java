package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
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

  /**
   * Конструктор.<br>
   *
   * @param aVedScreen IVedScreen - экран редактора
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

    for( IVedVertex v : listVertexes() ) {
      v.paint( aGc );
    }
  }

  // ------------------------------------------------------------------------------------
  // {@link IScreenObject}
  //

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
