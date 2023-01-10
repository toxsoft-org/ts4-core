package org.toxsoft.core.tslib.av.avtree;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Неизменяемая реализация {@link IAvTreePathElement}.
 * 
 * @author hazard157
 */
public class AvTreePathElement
    implements IAvTreePathElement, Serializable {

  /**
   * for serialization
   */
  private static final long serialVersionUID = 4327422671758852488L;
  private final boolean isNode;
  private final String id;
  private final boolean isArray;
  private final int arrayIndex;

  /**
   * Создает объект со всеми инвариантами.
   * 
   * @param aIsNode boolean - признак, что элемент пути ссылается на узел, а не на поле дерева
   * @param aId String - идентификатор поля, узла или семейства
   * @param aIsArray boolean - признак ссылки на элемент массива
   * @param aArrayIndex int - индекс элемента массива, 0 если это не ссылка на элемент массива
   */
  public AvTreePathElement( boolean aIsNode, String aId, boolean aIsArray, int aArrayIndex ) {
    isNode = aIsNode;
    id = StridUtils.checkValidIdPath( aId );
    isArray = aIsArray;
    arrayIndex = aArrayIndex;
  }

  // ------------------------------------------------------------------------------------
  // IAvTreePathElement
  //

  @Override
  public boolean isNode() {
    return isNode;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public boolean isArrayElement() {
    return isArray;
  }

  @Override
  public int arrayElementIndex() {
    return arrayIndex;
  }

}
