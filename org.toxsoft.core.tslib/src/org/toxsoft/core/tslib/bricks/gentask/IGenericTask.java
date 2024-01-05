package org.toxsoft.core.tslib.bricks.gentask;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The generic task is a an asynchronously executed job with input and output specified as {@link ITsContextRo}.
 * <p>
 * Task input and output parameters (options, references) and some other meta information is described by
 * {@link IGenericTaskInfo}. Generally the parameters are defined by the application. However there are few parameters
 * common for any task as defined in {@link IGenericTaskConstants}. Some pre-defined parameters are mandatory while
 * others are optional.
 * <p>
 * It is implementation-specific if task may be started while previous task is still executing. Anyway, recursive calls
 * of {@link #runSync(ITsContextRo)} is not allowed throwing an exception.
 *
 * @author hazard157
 */
public sealed interface IGenericTask
    extends IGenericTaskAsyncRunner, IGenericTaskSyncRunner
    permits AbstractGenericTask {

  /**
   * Returns the meta-information about task to run.
   *
   * @return {@link IGenericTaskInfo} - the task information
   */
  IGenericTaskInfo taskInfo();

  /**
   * Asynchronously starts a task for execution.
   *
   * @param aIn {@link ITsContextRo} - input parameters
   * @return {@link Future}&lt;{@link ITsContextRo}&gt; - result of execution to be checked when it is done
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException instance allows only single task and it is already running
   * @throws TsValidationFailedRtException failed {@link #canRun(ITsContextRo)}
   */
  @Override
  Future<ITsContextRo> runAsync( ITsContextRo aIn );

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
   * @throws TsValidationFailedRtException failed {@link #canRun(ITsContextRo)}
   */
  @Override
  ITsContextRo runSync( ITsContextRo aIn );

  /**
   * Checks if task can be run with the specified input.
   * <p>
   * The input options and references are checked against {@link #taskInfo()} definitions. Task implementation may add
   * additional checks (eg if connection to the server is alive).
   * <p>
   * Success of this method does <b>not</b> guarantees that task will be run successfully. However, if this method
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
