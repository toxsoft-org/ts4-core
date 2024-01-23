package org.toxsoft.core.tslib.bricks.gentask;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;

import java.util.concurrent.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericTask} implementation base.
 * <p>
 * Notes on configuration options implementation:
 * <ul>
 * <li>use {@link #addConfigOptionDefs(IDataDef...)} to defint configuration options;</li>
 * <li>changing configuration via {@link #setCfgOptionValues(IOptionSet)} call {@link #afterOptionValuesUpdated()} for
 * subclass to process configuration change (like to save values to permanent storage);</li>
 * <li>option values {@link #opVals} are declared <code>protected</code> for subclass to allow options change without
 * post-processing (eg in {@link #afterOptionValuesUpdated()} to correct invalid values).</li>
 * </ul>
 *
 * @author hazard157
 */
public abstract class AbstractGenericTask
    implements IGenericTask {

  private final IGenericTaskInfo              taskInfo;
  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();
  protected final IOptionSetEdit              opVals = new OptionSet();

  private boolean isSyncRun = false;

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
  // API for subclasses
  //

  /**
   * Add the configuration option definition to {@link #cfgOptionDefs()}.
   * <p>
   * Also add default value to the set {@link #cfgOptionValues()}.
   *
   * @param aDefs {@link IDataDef}[] - the definitions to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException the option with the same ID is already defined
   * @throws TsIllegalArgumentRtException definition does not allows but default value is {@link IAtomicValue#NULL}
   */
  protected void addConfigOptionDefs( IDataDef... aDefs ) {
    TsErrorUtils.checkArrayArg( aDefs );
    for( IDataDef dd : aDefs ) {
      TsItemAlreadyExistsRtException.checkTrue( opDefs.hasKey( dd.id() ) );
      TsIllegalArgumentRtException.checkTrue( !dd.isNullAllowed() && dd.defaultValue() == IAtomicValue.NULL );
      opDefs.add( dd );
      opVals.setValue( dd, dd.defaultValue() );
    }
  }

  protected void addConfigOptionDefs( IStridablesList<IDataDef> aDefs ) {
    TsNullArgumentRtException.checkNull( aDefs );
    addConfigOptionDefs( aDefs.toArray( new IDataDef[0] ) );
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

  @Override
  final public IStridablesList<IDataDef> cfgOptionDefs() {
    return opDefs;
  }

  @Override
  final public IOptionSet cfgOptionValues() {
    return opVals;
  }

  @Override
  final public void setCfgOptionValues( IOptionSet aValues ) {
    OptionSetUtils.checkOptionSet( aValues, opDefs );
    opVals.refreshSet( aValues );
    afterOptionValuesUpdated();
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

  protected void afterOptionValuesUpdated() {
    // nop
  }

}
