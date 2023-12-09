package org.toxsoft.core.tslib.bricks.gentask;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link AbstractGenericTaskRunner} implementation base.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractGenericTaskRunner
    implements IGenericTaskRunner {

  private final IGenericTaskInfo taskInfo;

  /**
   * Constructor for subclass.
   *
   * @param aTaskInfo {@link IGenericTaskInfo} - the task information
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractGenericTaskRunner( IGenericTaskInfo aTaskInfo ) {
    TsNullArgumentRtException.checkNull( aTaskInfo );
    taskInfo = aTaskInfo;
  }

  // ------------------------------------------------------------------------------------
  // IGenericTaskRunner
  //

  @Override
  final public IGenericTaskInfo taskInfo() {
    return taskInfo;
  }

  @Override
  final public IGenericTask run( ITsContextRo aInput ) {
    TsValidationFailedRtException.checkError( canRun( aInput ) );
    return doRun( aInput );
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
  // To override
  //

  /**
   * Implementation must run the task.
   * <p>
   * Returned task may already be closed.
   *
   * @param aInput {@link ITsContextRo} - validated input options and references
   * @return {@link GenericTask} - created instance of the task
   */
  protected abstract GenericTask doRun( ITsContextRo aInput );

  /**
   * Subclass may perform additional check if task can be executed.
   * <p>
   * It is guaranteed that content of <code>aInput</code> successfully passed option and references against
   * {@link #taskInfo()} definitions.
   * <p>
   * In the base class returns {@link ValidationResult#SUCCESS} there is no need to call superclass method when
   * overriding.
   *
   * @param aInput {@link ITsContextRo} - the task input (options and references), not <code>null</code>
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanRun( ITsContextRo aInput ) {
    return ValidationResult.SUCCESS;
  }

}
