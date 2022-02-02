package org.toxsoft.core.tslib.bricks.ctx.impl;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

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
