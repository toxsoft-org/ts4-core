package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VedRectVertexSetView
    extends VedAbstractVertexSetView {

  Rectangle rect = new Rectangle( 0, 0, 0, 0 );

  Color colorBlue;

  private final IEclipseContext appContext;

  private final ITsCursorManager cursorManager;

  private boolean visible = true;

  /**
   * Конструктор.<br>
   *
   * @param aInitialRect Rectangle - начальный прямоугольник
   * @param aAppContext IEclipseContext - контекст окна
   */
  public VedRectVertexSetView( Rectangle aInitialRect, IEclipseContext aAppContext ) {
    super( "rectVertexSet", "Вершины прямоугольника", "Набор вершин прямоугольника" ); //$NON-NLS-1$
    appContext = aAppContext;
    cursorManager = appContext.get( ITsCursorManager.class );

    updateRect( rect, aInitialRect, 2, 2 );

    ITsColorManager cm = aAppContext.get( ITsColorManager.class );

    colorBlue = cm.getColor( ETsColor.BLUE );
    Color fgColor = cm.getColor( ETsColor.BLACK );
    Color bgColor = cm.getColor( ETsColor.RED );

    for( ETsFulcrum fulcrum : ETsFulcrum.values() ) {
      RectVertex vertex = new RectVertex( 8, 8, fgColor, bgColor, fulcrum );
      addVertex( vertex );
    }
    updateVertexes();
  }

  @Override
  public void paint( GC aGc ) {
    if( !visible ) {
      return;
    }
    int oldStyle = aGc.getLineStyle();
    Color oldColor = aGc.getForeground();
    aGc.setLineStyle( SWT.LINE_DASH );
    aGc.setForeground( colorBlue );

    aGc.drawRectangle( rect );

    aGc.setLineStyle( oldStyle );
    aGc.setForeground( oldColor );
  }

  // ------------------------------------------------------------------------------------
  // {@link IScreenObject}
  //
  @Override
  public boolean visible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @Override
  public ECursorType cursorType() {
    return ECursorType.HAND;
  }

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

  @Override
  public boolean containsScreenPoint( int aX, int aY ) {
    return false;
  }

  @Override
  public boolean containsNormPoint( double aX, double aY ) {
    return false;
  }

  @Override
  public <T> T entity() {
    return null;
  }

  @Override
  protected void onZoomFactorChanged() {
    // updateVisRect();
    // updateVertexes();
    // System.out.println( "rect = " + rect.toString() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setRect( Rectangle aShapeBounds ) {
    updateRect( rect, aShapeBounds, 2, 2 );
    updateVertexes();
  }

  /**
   * Обновляет положения всех вершин и внутренний прямоугольник, при изменении одной из вершин.<br>
   *
   * @param aDx - смещение по оси x
   * @param aDy - смещение по оси y
   * @param aVertexId String - идентификатор вершины, один из ИДов элементов {@link ETsFulcrum}
   */
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

  @SuppressWarnings( "unchecked" )
  @Override
  public IStridablesList<? extends RectVertex> listVertexes() {
    return (IStridablesList<RectVertex>)super.listVertexes();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

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

}
