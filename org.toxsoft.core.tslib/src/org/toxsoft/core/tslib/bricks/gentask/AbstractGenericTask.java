package org.toxsoft.core.tslib.bricks.gentask;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericTask} implementation base.
 *
 * @author hazard157
 */
public abstract class AbstractGenericTask
    implements IGenericTask {

  private final IGenericTaskInfo taskInfo;

  private boolean   isSyncRun    = false; // true means that sync task is running
  private Future<?> asyncRunning = null;  // NOT null means that async task is running

  /**
   * Constructor for subclasses.
   *
   * @param aInfo {@link IGenericTaskInfo} - task info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractGenericTask( IGenericTaskInfo aInfo ) {
    taskInfo = TsNullArgumentRtException.checkNull( aInfo );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ITsContext prepareOutput( ITsContextRo aIn ) {
    ITsContext out = new TsContext();
    REFDEF_OUT_INPUT.setRef( out, aIn );
    REFDEF_OUT_TASK_INFO.setRef( out, taskInfo );
    return out;
  }

  /**
   * Checks if task is running either asynchronously or synchronously.
   * <p>
   * Sets {@link #asyncRunning} to <code>null</code> if asynchronous task has been finished.
   *
   * @return boolean - <code>true</code> if task is running
   */
  private boolean isRunning() {
    if( isSyncRun ) {
      return true;
    }
    if( asyncRunning != null && asyncRunning.isDone() ) {
      asyncRunning = null;
    }
    return asyncRunning != null;
  }

  // ------------------------------------------------------------------------------------
  // IGenericTask
  //

  @Override
  final public IGenericTaskInfo taskInfo() {
    return taskInfo;
  }

  @Override
  final public Future<ITsContextRo> runAsync( ITsContextRo aIn ) {
    TsValidationFailedRtException.checkError( canRun( aIn ) );
    TsIllegalArgumentRtException.checkTrue( isRunning() );
    ITsContext out = prepareOutput( aIn );
    Future<ITsContextRo> future = doRunAsync( aIn, out );
    asyncRunning = future;
    return future;
  }

  @Override
  final public ITsContextRo runSync( ITsContextRo aIn ) {
    TsValidationFailedRtException.checkError( canRun( aIn ) );
    TsIllegalStateRtException.checkTrue( isSyncRun ); // no recursive calls allowed!
    TsIllegalArgumentRtException.checkTrue( isRunning() );
    ITsContext out = prepareOutput( aIn );
    isSyncRun = true;
    try {
      doRunSync( aIn, out );
      return out;
    }
    finally {
      isSyncRun = false;
    }
  }

  @Override
  final public ValidationResult canRun( ITsContextRo aInput ) {
    ValidationResult vr = GenericTaskUtils.validateInput( taskInfo, aInput );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, doCanRun( aInput ) );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must asynchronously run the task.
   * <p>
   * The prepared <code>aOutput</code> instance has available parameters prepared.
   *
   * @param aInput {@link ITsContextRo} - the valid input of the task
   * @param aOutput {@link ITsContextRo} - the task output
   */
  protected abstract void doRunSync( ITsContextRo aInput, ITsContext aOutput );

  /**
   * Implementation must asynchronously run the task.
   * <p>
   * The prepared <code>aOutput</code> instance has available parameters prepared. Note: the result {@link Future} must
   * return the argument <code>aOutput</code> as a result of {@link Future#get()}.
   *
   * @param aInput {@link ITsContextRo} - the valid input of the task
   * @param aOutput {@link ITsContextRo} - the task output to be returned by the {@link Future} instance
   * @return {@link Future}&lt;{@link ITsContextRo}&gt; - result of execution to be checked when it is done
   */
  protected abstract Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput );

  /**
   * Subclass may perform additional check if task can be executed.
   * <p>
   * It is guaranteed that content of <code>aInput</code> successfully passed option and references against
   * {@link #taskInfo()} definitions.
   * <p>
   * In the base class method returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when
   * overriding.
   *
   * @param aInput {@link ITsContextRo} - the task input
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    return ValidationResult.SUCCESS;
  }

}
