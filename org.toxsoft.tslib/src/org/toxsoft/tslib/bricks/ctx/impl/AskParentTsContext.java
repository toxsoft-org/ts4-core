package org.toxsoft.tslib.bricks.ctx.impl;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IAskParent} implementation for {@link ITsContextRo} parent.
 *
 * @author hazard157
 */
class AskParentTsContext
    implements IAskParent {

  private final ITsContextRo parent;

  AskParentTsContext( ITsContextRo aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    parent = aParent;
  }

  @Override
  public IAtomicValue findOp( String aId ) {
    return parent.params().findByKey( aId );
  }

  @Override
  public Object findRef( String aName ) {
    return parent.find( aName );
  }

}
