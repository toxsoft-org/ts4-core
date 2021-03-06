package org.toxsoft.core.tsgui.bricks.ctx.impl;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IAskParent} implementation for {@link IEclipseContext} parent.
 *
 * @author hazard157
 */
class AskParentEclipseContext
    implements IAskParent {

  private IEclipseContext ecContext;

  AskParentEclipseContext( IEclipseContext aEclipseContext ) {
    TsNullArgumentRtException.checkNull( aEclipseContext );
    ecContext = aEclipseContext;
  }

  void setEclipseContext( IEclipseContext aEclipseContext ) {
    TsNullArgumentRtException.checkNull( aEclipseContext );
    ecContext = aEclipseContext;
  }

  @Override
  public IAtomicValue findOp( String aId ) {
    return null;
  }

  @Override
  public Object findRef( String aKey ) {
    return ecContext.get( aKey );
  }

}
