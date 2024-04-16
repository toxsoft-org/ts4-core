package org.toxsoft.core.tslib.math.combicond.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICombiCondParams} implementation when {@link ICombiCondParams#isSingle()} = <code>true</code>.
 *
 * @author hazard157
 */
class InternalCondParamsSingle
    extends CombiCondParams {

  private static final long serialVersionUID = 157157L;

  private final ISingleCondParams params;

  InternalCondParamsSingle( ISingleCondParams aParams, boolean aIsInverted ) {
    super( aIsInverted );
    params = TsNullArgumentRtException.checkNull( aParams );
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public ISingleCondParams single() {
    return params;
  }

  @Override
  public ICombiCondParams left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ICombiCondParams right() {
    throw new TsUnsupportedFeatureRtException();
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
    return s + single().toString();
  }

}
