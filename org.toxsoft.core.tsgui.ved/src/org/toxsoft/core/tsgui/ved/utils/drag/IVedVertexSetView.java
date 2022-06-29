package org.toxsoft.core.tsgui.ved.utils.drag;

import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальное представление набора вершин.
 * <p>
 *
 * @author vs
 */
public interface IVedVertexSetView
    extends IScreenObject, IVedPainter {

  /**
   * Возвращает список вершин.<br>
   *
   * @return IStridablesList&lt;? extends IVedVertex> - список вершин
   */
  IStridablesList<? extends IVedVertex> listVertexes();

  /**
   * Добаляет вершину к набору.<br>
   *
   * @param aVertex IVedVertex - добавляемая вершина
   * @throws TsItemAlreadyExistsRtException - если вершина с таким идентификатором уже существует
   */
  void addVertex( IVedVertex aVertex );
}
