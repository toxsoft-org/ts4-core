package org.toxsoft.core.tslib.math.logican;

import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;

import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parsed logical formula node.
 *
 * @author hazard157
 */
public interface ILogFoNode {

  /**
   * Not a node singleton, returned when formula has an error.
   * <p>
   * All methods of this instance throw {@link TsUnsupportedFeatureRtException} exception.
   */
  ILogFoNode NONE = new LfnNone();

  /**
   * Node corresponds to the keyword TRUE.
   */
  ILogFoNode TRUE = new LfnKeyword( KW_TRUE, false );

  /**
   * Node corresponds to the keyword FALSE.
   */
  ILogFoNode FALSE = new LfnKeyword( KW_FALSE, false );

  /**
   * Determines if node is a leaf containing the {@link #keyword()} only or a binary operation.
   * <p>
   * Binary operation has two operan nodes {@link #left()} and {@link #right()} and operation {@link #op()} between.
   *
   * @return boolean - the leaf node flag
   */
  boolean isLeaf();

  /**
   * Determines if this node value must be inverted (logical NOT applied).
   *
   * @return boolean - the logical inversion flag
   */
  boolean isInverted();

  /**
   * Returns the keywrod of the leaf node.
   *
   * @return String - the keyword
   * @throws TsUnsupportedFeatureRtException node is a binary node, {@link #isLeaf()} = <code>false</code>
   */
  String keyword();

  /**
   * Returns the left operand of the binary logical operation.
   *
   * @return {@link ILogFoNode} - the left operan, the left node
   * @throws TsUnsupportedFeatureRtException node is a leaf, {@link #isLeaf()} = <code>true</code>
   */
  ILogFoNode left();

  /**
   * Returns the logical operation between left and right nodes.
   *
   * @return {@link ELogicalOp} - the logical operation
   * @throws TsUnsupportedFeatureRtException node is a leaf, {@link #isLeaf()} = <code>true</code>
   */
  ELogicalOp op();

  /**
   * Returns the right operand of the binary logical operation.
   *
   * @return {@link ILogFoNode} - the right operan, the right node
   * @throws TsUnsupportedFeatureRtException node is a leaf, {@link #isLeaf()} = <code>true</code>
   */
  ILogFoNode right();

}
