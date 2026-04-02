package org.toxsoft.core.tslib.bricks.coopcomp;

import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.ctx.*;

/**
 * Mix-in interface of the cooperative multi-threading component.
 * <p>
 * FIXME usage
 *
 * @author hazard157
 */
public interface ITsCooperativeComponent
    extends ITsInitializable, IWorkerComponent, ICooperativeMultiTaskable {

  /**
   * Returns the current state of the component.
   *
   * @return {@link ETsCoopCompState} - component state
   */
  ETsCoopCompState compState();

  /**
   * Returns the arguments of the {@link #init(ITsContextRo)} call.
   * <p>
   * Calling this method before successful initialization returns <code>null</code>.
   *
   * @return {@link ITsContextRo} - the initialization arguments or <code>null</code> if not initialized yet
   */
  ITsContextRo compInitArgs();

}
