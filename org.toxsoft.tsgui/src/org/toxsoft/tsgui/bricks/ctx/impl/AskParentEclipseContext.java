package org.toxsoft.tsgui.bricks.ctx.impl;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.ctx.impl.IAskParent;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
  public Object findRef( String aName ) {
    return ecContext.get( aName );
  }

}
