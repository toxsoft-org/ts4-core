package org.toxsoft.core.tslib.bricks.ctx.impl;

import org.toxsoft.core.tslib.bricks.ctx.*;

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

  /**
   * Creates an empty context linked to the source to the parent references and options.
   *
   * @param aAskParent {@link IAskParent} - parent ops and refs retreival
   */
  public TsContext( IAskParent aAskParent ) {
    super( aAskParent );
  }

}
