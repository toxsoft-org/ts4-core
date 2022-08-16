package org.toxsoft.core.tslib.bricks.qnodes;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * {@link IQNode} kind meta-information.
 * <p>
 * Implementation of this interface must be effectively immutable.
 *
 * @author hazard157
 * @param <T> - node entity type
 */
public interface IQNodeKind<T>
    extends IStridableParameterized {

  /**
   * Returns class of entity hold by nodes of this kind.
   *
   * @return {@link Class} - class of {@link IQNode#entity()}
   */
  Class<T> entityClass();

  /**
   * Determines the ability that node can have child.
   *
   * @return boolean - <code>true</code> if node can hhave child
   */
  boolean canHaveChilds();

}
