package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

public interface IG2GraphicRenderer {

  void drawGraphic( GC aGc, IList<IList<Pair<Integer, Integer>>> aPolyLines, ITsRectangle aBounds );

  /**
   * Рисует представление графика - то как он выглядит (без учета реальных данных) и что визуально может
   * идентифицировать его.
   * <p>
   * <b>Мотивация:</b><br>
   * Данное представление может быть использовано для формирования легенды и визуального отображения значений визира.
   * <br>
   * <b>На заметку:</b><br>
   * Не выход за переданные границы является областью отвественности реализации. Фон области отрисовывать не надо.
   *
   * @param aGc GC - графический контекст (не null)
   * @param aBounds TsRectangle - границы области, в которой должно уместиться представление (не null)
   */
  void drawRepresentation( GC aGc, ITsRectangle aBounds );

  /**
   * @return цвет графика
   */
  Color graphicColor();

  /**
   * @return атрибуты линии
   */
  TsLineInfo lineInfo();

}
