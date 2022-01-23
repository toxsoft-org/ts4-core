package org.toxsoft.tslib.bricks.ctx.impl;

import org.toxsoft.tslib.bricks.ctx.ITsContext;
import org.toxsoft.tslib.bricks.ctx.ITsContextRo;

/**
 * Реализация {@link ITsContext}.
 *
 * @author hazard157
 */
public class TsContext
    extends TsContextBase<ITsContextRo> {

  /**
   * Creates an empty context with no parent.
   */
  public TsContext() {
    // nop
  }

  /**
   * Creates an empty context linked to the parent.
   *
   * @param aParent {@link ITsContextRo} - the parent context
   */
  public TsContext( ITsContextRo aParent ) {
    super( aParent );
  }

}
