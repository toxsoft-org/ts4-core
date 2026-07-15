package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.graphics.*;

class RoundedBorderPath {

  /**
   * Создаёт Path для границы прямоугольника со скруглёнными углами.
   *
   * @param device SWT Device (Display)
   * @param x X-координата левого верхнего угла
   * @param y Y-координата левого верхнего угла
   * @param width Ширина прямоугольника
   * @param height Высота прямоугольника
   * @param arcWidth Ширина дуги скругления угла
   * @param arcHeight Высота дуги скругления угла
   * @param borderWidth Толщина границы
   * @return Path, описывающий границу (нужно вызвать dispose() после использования)
   */
  public static Path createRoundedBorderPath( Device device, float x, float y, float width, float height,
      float arcWidth, float arcHeight, float borderWidth ) {

    Path path = new Path( device );

    // --- Внешний контур (по часовой стрелке) ---
    addRoundedRect( path, x, y, width, height, arcWidth, arcHeight );

    // --- Внутренний контур (против часовой стрелки — «вычитается» из заливки) ---
    float ix = x + borderWidth;
    float iy = y + borderWidth;
    float iw = width - 2 * borderWidth;
    float ih = height - 2 * borderWidth;

    // Радиус дуги уменьшается пропорционально толщине границы
    float iArcW = Math.max( 0, arcWidth - borderWidth );
    float iArcH = Math.max( 0, arcHeight - borderWidth );

    if( iw > 0 && ih > 0 ) {
      addRoundedRectReverse( path, ix, iy, iw, ih, iArcW, iArcH );
    }

    return path;
  }

  /**
   * Добавляет скруглённый прямоугольник в Path по часовой стрелке.
   */
  private static void addRoundedRect( Path path, float x, float y, float w, float h, float aw, float ah ) {

    float rx = aw / 2f;
    float ry = ah / 2f;

    path.moveTo( x + rx, y );
    path.lineTo( x + w - rx, y );
    path.quadTo( x + w, y, x + w, y + ry );
    path.lineTo( x + w, y + h - ry );
    path.quadTo( x + w, y + h, x + w - rx, y + h );
    path.lineTo( x + rx, y + h );
    path.quadTo( x, y + h, x, y + h - ry );
    path.lineTo( x, y + ry );
    path.quadTo( x, y, x + rx, y );
    path.close();
  }

  /**
   * Добавляет скруглённый прямоугольник в Path против часовой стрелки (для создания «дырки» при заливке
   * Path.WIND_EVEN_ODD или WIND_NON_ZERO).
   */
  private static void addRoundedRectReverse( Path path, float x, float y, float w, float h, float aw, float ah ) {

    float rx = aw / 2f;
    float ry = ah / 2f;

    path.moveTo( x + rx, y );
    path.quadTo( x, y, x, y + ry );
    path.lineTo( x, y + h - ry );
    path.quadTo( x, y + h, x + rx, y + h );
    path.lineTo( x + w - rx, y + h );
    path.quadTo( x + w, y + h, x + w, y + h - ry );
    path.lineTo( x + w, y + ry );
    path.quadTo( x + w, y, x + w - rx, y );
    path.close();
  }
}
