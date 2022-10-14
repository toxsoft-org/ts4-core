package org.toxsoft.core.tsgui.chart.renderers;

import org.eclipse.swt.graphics.*;

import ru.toxsoft.tsgui.chart.legaсy.*;

/**
 * Отрисовщик границы прямоугольной области.
 *
 * @author vs
 */
public interface IBorderRenderer {

  /**
   * Отрисовывает границу прямоугольной области.
   *
   * @param aGc GC - графический контекст
   */
  void drawBorder( GC aGc );

  /**
   * Возвращаает поля прямоугольной области, которые представляют собой толщину границы по каждой стороне.
   *
   * @return Margins - толщина границы по каждой стороне
   */
  Margins margins();

}
