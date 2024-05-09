package org.toxsoft.core.tslib.math.cond;

import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The node of the combined condition binary tree is a part of the {@link ITsCombiCondInfo}.
 * <p>
 * Tree leafs are nodes with flag {@link #isSingle()} = <code>true</code>. Leaf contains single condition ID as a key
 * for the map {@link ITsCombiCondInfo#singleInfos()}. Non-leaf, group node contains binary operation {@link #op()}
 * between {@link #left()} and {@link #right()} operand nodes.
 * <p>
 * To any kind of nodes the NOT (logical inversion) operation must be applied when {@link #isInverted()} =
 * <code>true</code>.
 *
 * @author hazard157
 */
public interface ITsCombiCondNode {

  /**
   * Determines if this is the single condition.
   *
   * @return boolean - the single condition flag<br>
   *         <b>true</b> - this is single condition, {@link #singleCondId()} and {@link #isInverted()} are used;<br>
   *         <b>false</b> - this is combined condition, {@link #left()}, {@link #op()}, {@link #right()} and
   *         {@link #isInverted()} are used.
   */
  boolean isSingle();

  /**
   * Determines if condition operation must be inverted (NOT applied).
   *
   * @return boolean - the flag of the NOT unary operation applied to this condition check result
   */
  boolean isInverted();

  /**
   * Returns the single condition variable name.
   *
   * @return String - the single condition ID is a key in {@link ITsCombiCondInfo#singleInfos()}
   * @throws TsUnsupportedFeatureRtException this is a combined condition, {@link #isSingle()} = <code>false</code>
   */
  String singleCondId();

  /**
   * Returns the left condition parameter values.
   *
   * @return {@link ITsCombiCondNode} - the left condition parameter values
   * @throws TsUnsupportedFeatureRtException this is a single condition, {@link #isSingle()} = <code>true</code>
   */
  ITsCombiCondNode left();

  /**
   * Returns the logical operation between left and right conditions.
   *
   * @return {@link ELogicalOp} - the logical operation between left and right condition
   * @throws TsUnsupportedFeatureRtException this is a single condition, {@link #isSingle()} = <code>true</code>
   */
  ELogicalOp op();

  /**
   * Returns the right condition parameter values.
   *
   * @return {@link ITsCombiCondNode} - the right condition parameter values
   * @throws TsUnsupportedFeatureRtException this is a single condition, {@link #isSingle()} = <code>true</code>
   */
  ITsCombiCondNode right();

}
