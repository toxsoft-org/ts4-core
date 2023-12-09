package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Вершина - чувствительная для мыши отображаемая область экрана.
 * <p>
 * Предназначена для формирования "активной" границы инструментами редактора. Например для прямоугольной границы захват
 * и перетаскивание одной из вершин прямоугольника может приводить к изменению размеров подчиненной компоненты.<br>
 *
 * @author vs
 */
public interface IVedVertex
    extends IPointsHost, ITsPaintable, ID2Portable, IStridable {

  /**
   * Тип курсора мыши, когда он находится над вершиной.
   *
   * @return ECursorType - тип курсора мыши, когда он находится над вершиной
   */
  ECursorType cursorType();
}
