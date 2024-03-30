package org.toxsoft.core.tslib.math.combicond.impl;

import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICombiCondParams} implementation when {@link ICombiCondParams#isSingle()} = <code>false</code>.
 *
 * @author hazard157
 */
class InternalCondParamsCombi
    extends CombiCondParams {

  private static final long serialVersionUID = 157157L;

  private final ICombiCondParams left;
  private final ELogicalOp       op;
  private final ICombiCondParams right;

  protected InternalCondParamsCombi( ICombiCondParams aLeft, ELogicalOp aOp, ICombiCondParams aRight,
      boolean aIsInverted ) {
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
  public ISingleCondParams single() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ICombiCondParams left() {
    return left;
  }

  @Override
  public ELogicalOp op() {
    return op;
  }

  @Override
  public ICombiCondParams right() {
    return right;
  }
}
