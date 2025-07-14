package org.toxsoft.core.tslib.bricks.ctx.impl;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsContext( ITsContextRo aParent ) {
    super( aParent );
  }

  /**
   * Creates an empty context linked to the source to the parent references and options.
   *
   * @param aAskParent {@link IAskParent} - parent ops and refs retreival
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsContext( IAskParent aAskParent ) {
    super( aAskParent );
  }

}
