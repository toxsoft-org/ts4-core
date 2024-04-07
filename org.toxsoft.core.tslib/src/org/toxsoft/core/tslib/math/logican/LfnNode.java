package org.toxsoft.core.tslib.math.logican;

import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILogFoNode} node implementation for {@link ILogFoNode#isLeaf()} = <code>false</code>.
 *
 * @author hazard157
 */
class LfnNode
    implements ILogFoNode {

  private final ILogFoNode left;
  private final ELogicalOp op;
  private final ILogFoNode right;
  private final boolean    inverted;

  LfnNode( ILogFoNode aLeft, ELogicalOp aOp, ILogFoNode aRight, boolean aInverted ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    left = aLeft;
    op = aOp;
    right = aRight;
    inverted = aInverted;
  }

  static ILogFoNode invert( ILogFoNode aLfn ) {
    if( aLfn.isLeaf() ) {
      return new LfnKeyword( aLfn.keyword(), !aLfn.isInverted() );
    }
    return new LfnNode( aLfn.left(), aLfn.op(), aLfn.right(), !aLfn.isInverted() );
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public String keyword() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ILogFoNode left() {
    return left;
  }

  @Override
  public ELogicalOp op() {
    return op;
  }

  @Override
  public ILogFoNode right() {
    return right;
  }

  @Override
  public boolean isInverted() {
    return inverted;
  }

  @Override
  public String toString() {
    return String.format( "(%s %s %s)", left.toString(), op.id(), right.toString() ); //$NON-NLS-1$
  }

}
