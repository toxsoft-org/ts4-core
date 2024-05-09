package org.toxsoft.core.tslib.math.cond;

import java.io.*;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.cond.impl.*;

/**
 * Information set to create combined condition checker or filter.
 * <p>
 * Implementation must guarantee that created instance is valid.
 *
 * @author hazard157
 */
public interface ITsCombiCondInfo {

  /**
   * Parameters to create a combined condition that is always met.
   */
  ITsCombiCondInfo ALWAYS = new InternalAlwaysCombiCondParams();

  /**
   * Options for creating a combined condition that is never met.
   */
  ITsCombiCondInfo NEVER = new InternalNeverCombiCondParams();

  /**
   * Returns the root node of the combined condition binary tree.
   *
   * @return {@link ITsCombiCondNode} - condition tree root node
   */
  ITsCombiCondNode rootNode();

  /**
   * Returns the single conditions corresponding to the leaf nodes of the {@link #rootNode()} tree.
   * <p>
   * Leaf node {@link ITsCombiCondNode#singleCondId()} is used as a key of the map.
   *
   * @return {@link IStringMap}&lt;{@link ITsSingleCondInfo}&gt; - map @single condition ID" - "condition info"
   */
  IStringMap<ITsSingleCondInfo> singleInfos();

}

/**
 * Internal class for {@link ITsCombiCondInfo#ALWAYS} singleton implementation.
 *
 * @author hazard157
 */
class InternalAlwaysCombiCondParams
    implements ITsCombiCondInfo, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiCondInfo#ALWAYS} will be read correctly.
   *
   * @return Object - always {@link ITsCombiCondInfo#ALWAYS}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsCombiCondInfo.ALWAYS;
  }

  @Override
  public ITsCombiCondNode rootNode() {
    return TsCombiCondNode.ALWAYS;
  }

  @Override
  public IStringMap<ITsSingleCondInfo> singleInfos() {
    return IStringMap.EMPTY;
  }

}

/**
 * Internal class for {@link ITsCombiCondInfo#NEVER} singleton implementation.
 *
 * @author hazard157
 */
class InternalNeverCombiCondParams
    implements ITsCombiCondInfo, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsCombiCondInfo#NEVER} will be read correctly.
   *
   * @return Object - always {@link ITsCombiCondInfo#NEVER}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITsCombiCondInfo.NEVER;
  }

  @Override
  public ITsCombiCondNode rootNode() {
    return TsCombiCondNode.NEVER;
  }

  @Override
  public IStringMap<ITsSingleCondInfo> singleInfos() {
    return IStringMap.EMPTY;
  }

}
