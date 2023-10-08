package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Набор вершин типа {@link IVedVertex}.
 *
 * @author vs
 */
public interface IVedVertexSet
    extends IVedDecorator, ITsUserInputListener {

  /**
   * Возвращает все вершины набора.
   *
   * @return IList&lt;& extends IVedVertex> - список всех вершин в наборе
   */
  IList<? extends IVedVertex> vertexes();

}
