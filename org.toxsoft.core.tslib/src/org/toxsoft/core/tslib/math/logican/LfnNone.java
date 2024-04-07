package org.toxsoft.core.tslib.math.logican;

import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILogFoNode#NONE} implementation, all methods throw {@link TsUnsupportedFeatureRtException}.
 *
 * @author hazard157
 */
class LfnNone
    implements ILogFoNode {

  LfnNone() {
    // nop
  }

  @Override
  public boolean isLeaf() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public String keyword() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ILogFoNode left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ILogFoNode right() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isInverted() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public String toString() {
    return "<<NONE>>"; //$NON-NLS-1$
  }

}
