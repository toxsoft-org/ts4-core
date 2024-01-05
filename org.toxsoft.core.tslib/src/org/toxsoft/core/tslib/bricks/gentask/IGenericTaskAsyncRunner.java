package org.toxsoft.core.tslib.bricks.gentask;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Run the task asynchronously.
 *
 * @author hazard157
 */
public interface IGenericTaskAsyncRunner {

  /**
   * Asynchronously starts a task for execution.
   *
   * @param aIn {@link ITsContextRo} - input parameters
   * @return {@link Future}&lt;{@link ITsContextRo}&gt; - result of execution to be checked when it is done
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException instance allows only single task and it is already running
   * @throws TsValidationFailedRtException preconditions check failed
   */
  Future<ITsContextRo> runAsync( ITsContextRo aIn );

}
