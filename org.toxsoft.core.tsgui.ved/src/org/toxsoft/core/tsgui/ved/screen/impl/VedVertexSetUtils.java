package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные методы для работы с набором вершин {@link IVedVertexSet}
 *
 * @author vs
 */
public class VedVertexSetUtils {

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

  /**
   * Возвращает прямоугольник, являющийся объединением двух переданных.
   *
   * @param r1 ITsRectangle - первый прямоугольник
   * @param r2 ITsRectangle - второй прямоугольник
   * @return ITsRectangle - прямоугольник, являющийся объединением двух переданных
   */
  public static ITsRectangle union( ITsRectangle r1, ITsRectangle r2 ) {
    int minX = Math.min( r1.a().x(), r2.a().x() );
    int minY = Math.min( r1.a().y(), r2.a().y() );
    int maxX = Math.max( r1.b().x(), r2.b().x() );
    int maxY = Math.max( r1.b().y(), r2.b().y() );

    return new TsRectangle( minX, minY, maxX - minX + 1, maxY - minY + 1 );
  }

  /**
   * Возвращает прямоугольник, являющийся объединением двух переданных.
   *
   * @param r1 ID2Rectangle - первый прямоугольник
   * @param r2 ID2Rectangle - второй прямоугольник
   * @return ID2Rectangle - прямоугольник, являющийся объединением двух переданных
   */
  public static ID2Rectangle union( ID2Rectangle r1, ID2Rectangle r2 ) {
    double minX = Math.min( r1.a().x(), r2.a().x() );
    double minY = Math.min( r1.a().y(), r2.a().y() );
    double maxX = Math.max( r1.b().x(), r2.b().x() );
    double maxY = Math.max( r1.b().y(), r2.b().y() );

    return new D2Rectangle( minX, minY, maxX - minX + 1, maxY - minY + 1 );
  }

  private VedVertexSetUtils() {
    // запрет на создание экземпляров
  }
}
