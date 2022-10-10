package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вершина, представляющая собой прямоугольник.
 * <p>
 *
 * @author vs
 */
public class RectVertex
    extends VedAbstractVertex {

  private final Rectangle rect = new Rectangle( 0, 0, 0, 0 );

  private final ETsFulcrum fulcrum;

  protected RectVertex( int aWidth, int aHeight, Color fgColor, Color bgColor, ETsFulcrum aFulcrum ) {
    super( aFulcrum.id(), aFulcrum.nmName(), aFulcrum.description() );
    fulcrum = aFulcrum;
    rect.width = aWidth;
    rect.height = aHeight;
    setForeground( fgColor );
    setBackground( bgColor );
  }

  @Override
  public Rectangle bounds() {
    return rect;
  }

  // @Override
  // public boolean containsNormPoint( double aX, double aY ) {
  // double zf = getConversion().zoomFactor();
  // return rect.contains( (int)Math.round( aX * zf ), (int)Math.round( aY * zf ) );
  // }

  @Override
  public boolean containsScreenPoint( int aX, int aY ) {
    return rect.contains( aX, aY );
  }

  @Override
  public void paint( GC aGc ) {
    aGc.setForeground( foregroundColor() );
    aGc.setBackground( backgroundColor() );

    aGc.fillRectangle( rect );
    aGc.drawRectangle( rect );
  }

  @Override
  public ECursorType cursorType() {
    return cursorType( fulcrum );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает точку привязки вершины.
   * <p>
   *
   * @return ETsFulcrum - точка привязки вершины
   */
  ETsFulcrum fulcrum() {
    return fulcrum;
  }

  /**
   * Задает новый граничный прямоугольник для вершины.<br>
   *
   * @param aRect Rectangle - новый граничный прямоугольник для вершины
   */
  public void setRect( Rectangle aRect ) {
    rect.x = aRect.x;
    rect.y = aRect.y;
    rect.width = aRect.width;
    rect.height = aRect.height;
  }

  /**
   * Возвращает тип курсора для точки опоры прямоугольника.<br>
   *
   * @param aFulcrum ETsFulcrum - точка опоры прямоугольника
   * @return ECursorType - тип курсора мыши
   */
  public static ECursorType cursorType( ETsFulcrum aFulcrum ) {
    switch( aFulcrum ) {
      case TOP_CENTER:
      case BOTTOM_CENTER:
        return ECursorType.SIZSTR_N_NORTH_SOUTH;
      case CENTER:
        return ECursorType.SIZSTR_N_ALL;
      case LEFT_CENTER:
      case RIGHT_CENTER:
        return ECursorType.SIZSTR_N_WEST_EAST;
      case LEFT_TOP:
        return ECursorType.SIZSTR_N_NORTH_WEST;
      case RIGHT_BOTTOM:
        return ECursorType.SIZSTR_N_SOUTH_EAST;
      case RIGHT_TOP:
        return ECursorType.SIZSTR_N_NORTH_EAST;
      case LEFT_BOTTOM:
        return ECursorType.SIZSTR_N_SOUTH_WEST;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
