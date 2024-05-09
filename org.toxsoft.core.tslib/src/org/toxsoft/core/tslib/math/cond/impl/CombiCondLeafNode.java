package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsCombiCondNode} immutable implementation when {@link ITsCombiCondNode#isSingle()} = <code>true</code>.
 *
 * @author hazard157
 */
final class CombiCondLeafNode
    extends TsCombiCondNode {

  private static final long serialVersionUID = 157157L;

  private final String singleId;

  CombiCondLeafNode( String aSingleCondId, boolean aIsInverted ) {
    super( aIsInverted );
    singleId = StridUtils.checkValidIdPath( aSingleCondId );
  }

  @Override
  public boolean isSingle() {
    return true;
  }

  @Override
  public String singleCondId() {
    return singleId;
  }

  @Override
  public ITsCombiCondNode left() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ELogicalOp op() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ITsCombiCondNode right() {
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
    return s + singleCondId();
  }

}
