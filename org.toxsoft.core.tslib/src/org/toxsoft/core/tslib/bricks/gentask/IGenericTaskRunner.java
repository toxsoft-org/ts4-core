package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Generic task executer - runs (starts) the task.
 *
 * @author hazard157
 */
public sealed interface IGenericTaskRunner permits AbstractGenericTaskRunner {

  /**
   * Returns the meta-information about task to run.
   *
   * @return {@link IGenericTaskInfo} - the task information
   */
  IGenericTaskInfo taskInfo();

  /**
   * Runs (starts) the task.
   * <p>
   * It is implementation dependent whether the runner allows to run new instance of task before previous instance has
   * finished.
   * <p>
   * For a small and fast tasks returned task may be already finished.
   * <p>
   * It is strongly recommended to use a new instance of {@link ITsContextRo} each time calling this method.
   *
   * @param aInput {@link ITsContextRo} - the task input (options and references)
   * @return {@link IGenericTask} - created instance of the task
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canRun(ITsContextRo)}
   */
  IGenericTask run( ITsContextRo aInput );

  /**
   * Checks if runner can execute task with the specified input.
   * <p>
   * The input options and references are checked against {@link IGenericTaskInfo} definitions. Runner implementation
   * may add additional checks (eg if connection to the server is alive).
   * <p>
   * Success of this method does <b>not</b> guarantees that task will be started successfully. However, if this method
   * returns error, execution is guaranteed to fail.
   * <p>
   * Options and references not listed in {@link IGenericTaskInfo#inOps()} and {@link IGenericTaskInfo#inRefs()} are
   * ignored.
   *
   * @param aInput {@link ITsContextRo} - the task input (options and references)
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRun( ITsContextRo aInput );

}
