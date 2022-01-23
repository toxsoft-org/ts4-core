package org.toxsoft.tslib.bricks.ctx.impl;

import org.toxsoft.tslib.av.IAtomicValue;

/**
 * {@link IAskParent} implementation for no parent.
 *
 * @author hazard157
 */
class AskParentNone
    implements IAskParent {

  /**
   * Singlton instance.
   */
  static final IAskParent NONE = new AskParentNone();

  private AskParentNone() {
    // nop
  }

  @Override
  public IAtomicValue findOp( String aId ) {
    return null;
  }

  @Override
  public Object findRef( String aName ) {
    return null;
  }

}
