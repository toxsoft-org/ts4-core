package org.toxsoft.core.tslib.bricks.ctx.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
    return parent.params().findValue( aId );
  }

  @Override
  public Object findRef( String aKey ) {
    return parent.find( aKey );
  }

}
