package org.toxsoft.core.tsgui.bricks.tstree;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Kind definition for the node {@link ITsNode}.
 *
 * @author hazard157
 * @param <T> - node entity type
 */
public interface ITsNodeKind<T>
    extends IStridable, IIconIdable {

  // TODO TRANSLATE

  /**
   * Возвращает класс сущности, содержащейся в узле.
   *
   * @return {@link Class} - класс сущности {@link ITsNode#entity()}
   */
  Class<T> entityClass();

  /**
   * Возвращает признак, что узел может иметь дочерние узлы.
   * <p>
   * Возможность иметь узлы еще не значит, что {@link ITsNode#childs()} вернет не пустой список. А вот если у узла
   * принципиально не может быть дочек, то {@link ITsNode#childs()} всегда возвращает пустой список.
   *
   * @return boolean - признак, что узел может иметь дочерние узлы
   */
  boolean canHaveChilds();

  /**
   * Возвращает имя сущности (которая содержится в узле {@link ITsNode#entity()}.
   *
   * @param aEntity &lt;T&gt; - сущность в узле дерева. может быть null
   * @return String - название узла с сущностью или <code>null</code>, если нет общего правила именования
   */
  String getEntityName( T aEntity );

}
