package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Run the task synchronously.
 *
 * @author hazard157
 */
public interface IGenericTaskSyncRunner {

  /**
   * Synchronously executes a task.
   * <p>
   * Any task may be executes synchronously, however it has no sense for a time consuming tasks.
   *
   * @param aIn {@link ITsContextRo} - input parameters
   * @return {@link ITsContextRo} - task execution result (an output)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException recursive call was detected
   * @throws TsIllegalStateRtException instance allows only single task and it is already running
   * @throws TsValidationFailedRtException preconditions check failed
   */
  ITsContextRo runSync( ITsContextRo aIn );

}
