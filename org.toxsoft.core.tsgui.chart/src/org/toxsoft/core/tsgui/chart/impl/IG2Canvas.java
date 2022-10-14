package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Интерфейс холста для отрисовки графиков.
 *
 * @author vs
 */
public interface IG2Canvas
    extends IStridable, IG2ChartArea {

  /**
   * Возвращает отступы по краю холста.
   *
   * @return Margins - отступы по краю холста
   */
  Margins margins();
}
