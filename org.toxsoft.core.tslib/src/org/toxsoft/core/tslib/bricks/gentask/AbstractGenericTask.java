package org.toxsoft.core.tslib.bricks.gentask;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IGenericTask} implementation base.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractGenericTask
    implements IGenericTask {

  private final IGenericTaskInfo taskInfo;
  private boolean                isSyncRun = false;

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
    TsIllegalArgumentRtException.checkFalse( canLaunchTaskNow() );
    ITsContext out = prepareOutput( aIn );
    Future<ITsContextRo> future = doRunAsync( aIn, out );
    TsInternalErrorRtException.checkNull( future );
    return future;
  }

  @Override
  final public ITsContextRo runSync( ITsContextRo aIn ) {
    TsValidationFailedRtException.checkError( canRun( aIn ) );
    TsIllegalStateRtException.checkTrue( isSyncRun ); // no recursive calls allowed!
    TsIllegalArgumentRtException.checkFalse( canLaunchTaskNow() );
    isSyncRun = true;
    try {
      return doRunSync( aIn );
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
   * Subclass may override to synchronously run the task.
   * <p>
   * Implementation in the base class calls {@link #runAsync(ITsContextRo)} and waits until {@link Future#isDone()}
   * becomes <code>true</code>. No need to call parent method when overriding.
   *
   * @param aInput {@link ITsContextRo} - the valid input of the task
   * @return {@link ITsContextRo} - the task output
   */
  protected ITsContextRo doRunSync( ITsContextRo aInput ) {
    Future<ITsContextRo> future = runAsync( aInput );
    // wait until task is done
    while( !future.isDone() ) {
      // FIXME what if implementation is performing task not in the other thread but eg. in GUI thread slices???
      try {
        Thread.sleep( 10 );
      }
      catch( InterruptedException ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }

    try {
      return future.get();
    }
    catch( InterruptedException | ExecutionException ex ) {
      // as we checked isDone() this exception must not happen
      LoggerUtils.errorLogger().error( ex );
      throw new TsInternalErrorRtException( ex );
    }
  }

  /**
   * Implementation must asynchronously run the task.
   * <p>
   * The prepared <code>aOutput</code> instance has available parameters prepared.
   *
   * @param aInput {@link ITsContextRo} - the valid input of the task
   * @param aOutput {@link ITsContextRo} - the task output to be returned by the {@link Future} instance
   * @return {@link Future}&lt;{@link ITsContextRo}&gt; - result of execution to be checked when it is done
   */
  protected abstract Future<ITsContextRo> doRunAsync( ITsContextRo aInput, ITsContext aOutput );

  /**
   * Determines if new task may be started.
   * <p>
   * If subclass not allows multiple simultaneous tasks it may override this method to return false when task is aleady
   * running and not finished yet.
   * <p>
   * In the base class returns <code>true</code> assuming that any number of asynchronous tasks may be run
   * simultaneously.
   *
   * @return boolean - <code>true</code> no task is executing now or instance supports simultaneous launch of tasks<br>
   *         <code>false</code> previous task is still running, can't start new one
   */
  protected boolean canLaunchTaskNow() {
    return true;
  }

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
