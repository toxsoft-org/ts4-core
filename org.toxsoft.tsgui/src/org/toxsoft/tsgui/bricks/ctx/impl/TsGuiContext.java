package org.toxsoft.tsgui.bricks.ctx.impl;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tslib.bricks.ctx.impl.TsContextBase;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link ITsGuiContext} imlrmrntation.
 *
 * @author hazard157
 */
public class TsGuiContext
    extends TsContextBase<ITsGuiContext>
    implements ITsGuiContext {

  private IEclipseContext eclipseContext; // non-null only for root ts-context in ts-context hierarchy

  /**
   * Creates the root context.
   *
   * @param aEclipseContext {@link IEclipseContext} - Eclipse E4 context
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public TsGuiContext( IEclipseContext aEclipseContext ) {
    super( new AskParentEclipseContext( aEclipseContext ) );
    eclipseContext = aEclipseContext;
  }

  /**
   * Creates the child context.
   *
   * @param aParentContext {@link ITsGuiContext} - the parent context
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public TsGuiContext( ITsGuiContext aParentContext ) {
    super( aParentContext );
    eclipseContext = null;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContext
  //

  @Override
  public IEclipseContext eclipseContext() {
    if( eclipseContext != null ) {
      return eclipseContext;
    }
    TsInternalErrorRtException.checkNull( parent() );
    // search for root context
    return parent().eclipseContext();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Changes the Eclipse E4 context assosiated to the root context.
   * <p>
   * Notes:
   * <ul>
   * <li>this method may be called only for root context;</li>
   * <li>{@link ITsGuiContext#eclipseContext()} will be changed for all child contexts.</li>
   * </ul>
   *
   * @param aEclipseContext {@link IEclipseContext} - Eclipse E4 context
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsUnsupportedFeatureRtException method is called for non-root context
   */
  public void setEclipseContext( IEclipseContext aEclipseContext ) {
    TsNullArgumentRtException.checkNull( aEclipseContext );
    TsUnsupportedFeatureRtException.checkNull( eclipseContext );
    eclipseContext = aEclipseContext;
    ((AskParentEclipseContext)getAskParent()).setEclipseContext( eclipseContext );
  }

}
