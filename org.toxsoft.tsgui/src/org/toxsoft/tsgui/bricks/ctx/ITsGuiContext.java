package org.toxsoft.tsgui.bricks.ctx;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tslib.bricks.ctx.ITsContext;

/**
 * GUI components editable context.
 *
 * @author hazard157
 */
public interface ITsGuiContext
    extends ITsContext {

  /**
   * Return the Eclipse E4 context assosiated with the root {@link ITsGuiContext}.
   *
   * @return {@link IEclipseContext} - Eclipse E4 context, never is <code>null</code>
   */
  IEclipseContext eclipseContext();

  /**
   * Returns the parent TS context.
   *
   * @return {@link ITsGuiContext} - the parent context or <code>null</code> for the root context
   */
  @Override
  ITsGuiContext parent();

}
