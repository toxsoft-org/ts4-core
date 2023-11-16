package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * The interface to access executing and finished generic task.
 * <p>
 * Instance of the task is created and returned by {@link IGenericTaskRunner#run(ITsContextRo)}. Returned task may
 * already been finished. For a long running tasks {@link #isFinished()} may be polled to determine when task is done.
 * After task is finished it must be explicitly closed by {@link #close()} method. Closed task may release some
 * resources, however {@link #in()} and {@link #out()} are still accessible.
 * <p>
 * Closing the running task (when {@link #isFinished()} = <code>false</code>) will request task cancellation. Depending
 * on task nature and runner implementation, finishing task after {@link #close()} may take a while. Calling
 * {@link #close()} on finished task is ignored.
 * <p>
 * Implements {@link IGenericChangeEventCapable}. Generates events when task state changes including when is becomes
 * finished (if returned by {@link IGenericTaskRunner#run(ITsContextRo)} instance was not finished) and when something
 * application-specific happened during task execution (like a new portion of data has arrived).
 *
 * @author hazard157
 */
public sealed interface IGenericTask
    extends IGenericChangeEventCapable, ICloseable permits GenericTask {

  /**
   * Determines if task is finished regardless of the reason, whether it was cancelled or fully executed.
   *
   * @return boolean - <code>true</code> task is finished, <code>false</code> - task is runnung
   */
  boolean isFinished();

  /**
   * Returns the input parameters specified at the task start.
   * <p>
   * This is the same reference as was passed to {@link IGenericTaskRunner#run(ITsContextRo)}.
   *
   * @return {@link ITsContextRo} - the task input (options and references)
   */
  ITsContextRo in();

  /**
   * Returns the task output options and parameters.
   * <p>
   * Although the returned {@link ITsContextRo} is a read-only interface, the contents of the task's output can change
   * while the task is running. Only when {@link #isFinished()} becomes <code>true</code> does the output content stop
   * changing.
   *
   * @return {@link ITsContextRo} - task output options and references
   */
  ITsContextRo out();

}
