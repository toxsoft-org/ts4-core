package org.toxsoft.core.tslib.math.logican;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILogFoNode} leaf implementation for {@link ILogFoNode#isLeaf()} = <code>true</code>.
 *
 * @author hazard157
 */
class LfnKeyword
    implements ILogFoNode {

  private final String  keyword;
  private final boolean inverted;

  LfnKeyword( String aKeywrod, boolean aInverted ) {
    keyword = StridUtils.checkValidIdPath( aKeywrod );
    inverted = aInverted;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public String keyword() {
    return keyword;
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
    return inverted;
  }

  @Override
  public String toString() {
    if( inverted ) {
      return "NOT " + keyword; //$NON-NLS-1$
    }
    return keyword;
  }

}
