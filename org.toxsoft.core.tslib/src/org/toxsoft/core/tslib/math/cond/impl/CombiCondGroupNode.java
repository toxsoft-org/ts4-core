package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondNode} immutable implementation when {@link ITsCombiCondNode#isSingle()} = <code>false</code>.
 *
 * @author hazard157
 */
class CombiCondGroupNode
    extends TsCombiCondNode {

  private static final long serialVersionUID = 157157L;

  private final ITsCombiCondNode left;
  private final ELogicalOp       op;
  private final ITsCombiCondNode right;

  protected CombiCondGroupNode( ITsCombiCondNode aLeft, ELogicalOp aOp, ITsCombiCondNode aRight, boolean aIsInverted ) {
    super( aIsInverted );
    left = aLeft;
    op = aOp;
    right = aRight;
  }

  @Override
  public boolean isSingle() {
    return false;
  }

  @Override
  public String singleCondId() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ITsCombiCondNode left() {
    return left;
  }

  @Override
  public ELogicalOp op() {
    return op;
  }

  @Override
  public ITsCombiCondNode right() {
    return right;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    String s = EMPTY_STRING;
    if( isInverted() ) {
      s += "!"; //$NON-NLS-1$
    }
    s += '(';
    s += left.toString();
    s += ' ';
    s += op.opChar();
    s += ' ';
    s += right.toString();
    s += ')';
    s += ' ';
    return s;
  }

}
