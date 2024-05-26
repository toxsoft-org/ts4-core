package org.toxsoft.core.tslib.bricks.wub;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IWubUnit} implementation base.
 * <p>
 * Notes for subclass implementers:
 * <ul>
 * <li>in constructor subclass may set up <code>final</code> fields according to parameter values, performs other action
 * but it is strongly prohibited to allocate/open external resources. Note that common stop sequence is not allied to
 * the created but not initialized units;</li>
 * <li>initialization {@link #doInit(ITsContextRo)} is the main preparation method where unit receives the environment.
 * Usually after initializaton the unit is ready for work;</li>
 * <li>{@link #doStart()} is called before first call to {@link #doJob()} after initialization. STarting sequence
 * implementation is optional and may be needed if unit depends on other units to be initialized;</li>
 * <li>normal workload is performed in method {@link #doDoJob()}. The {@link #doDoJob()} method is called periodically
 * by the unit container. There should be no assumptions about the frequency or interval between method calls. For
 * example, container may pause single unit and not call {@link #doJob()} method for any period of time;</li>
 * <li>when unit work is to be finished the {@link #doQueryStop()} method is called. If possible unit should finish work
 * immediately and return <code>true</code>. If stopping process need a time, then {@link #doQueryStop()} should return
 * <code>false</code> and {@link #doStopping()} is called instead of {@link #doDoJob()} periodically. When work is done
 * {@link #doStopping()} must return <code>false</code>;</li>
 * <li>if normal stopping sequence takes too long, or when requested container may wish to stop unit immediately. In
 * this case {@link #doDestroy()} will be called.</li>
 * </ul>
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractWubUnit
    implements IWubUnit {

  private final WubUnitDiagnostics diagnosticsInfo = new WubUnitDiagnostics();

  private final String     id;
  private final IOptionSet params;

  private ITsContextRo environ = null;

  private EWubUnitState wuState = EWubUnitState.CREATED;

  /**
   * Constructor.
   *
   * @param aId String - the unit ID (an IDpath)
   * @param aParame {@link IOptionSet} - unit creation parameters including name and description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  protected AbstractWubUnit( String aId, IOptionSet aParame ) {
    id = StridUtils.checkValidIdPath( aId );
    params = new OptionSet( aParame );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return id;
  }

  @Override
  final public String nmName() {
    return DDEF_NAME.getValue( params ).asString();
  }

  @Override
  final public String description() {
    return DDEF_DESCRIPTION.getValue( params ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  final public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // ICooperativeMultiTaskable
  //

  @Override
  final public void doJob() {
    switch( wuState ) {
      case CREATED:
      case INITED: { // such calls are not allowed
        throw new TsIllegalStateRtException();
      }
      case STARTED: { // normal #doJob()
        doDoJob();
        break;
      }
      case STOP_QUERIED: { // periodically call #doStopping() to perform stop
        if( doStopping() ) {
          wuState = EWubUnitState.STOPPED;
        }
        break;
      }
      case STOPPED: { // unit is stopped, do nothing
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( wuState.id() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IWorkerComponent
  //

  @Override
  final public void start() {
    TsIllegalStateRtException.checkTrue( wuState != EWubUnitState.INITED );
    doStart();
    wuState = EWubUnitState.STARTED;
  }

  @Override
  final public boolean queryStop() {
    switch( wuState ) {
      case CREATED: { // not initialized unit may be stopped immediately
        wuState = EWubUnitState.STOPPED;
        return true;
      }
      case INITED:
      case STARTED: { // begin stopping process
        if( doQueryStop() ) {
          wuState = EWubUnitState.STOPPED;
          return true;
        }
        wuState = EWubUnitState.STOP_QUERIED;
        return false;
      }
      case STOP_QUERIED: { // already queried, do nothing, return false
        return false;
      }
      case STOPPED: { // already stopped, do nothing, return true
        return true;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( wuState.id() );
    }
  }

  @Override
  final public boolean isStopped() {
    return wuState == EWubUnitState.STOPPED;
  }

  @Override
  final public void destroy() {
    try {
      doDestroy();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    wuState = EWubUnitState.STOPPED;
  }

  // ------------------------------------------------------------------------------------
  // IWorkerUnit
  //

  @Override
  final public ValidationResult init( ITsContextRo aEnviron ) {
    TsNullArgumentRtException.checkNull( aEnviron );
    TsIllegalStateRtException.checkTrue( wuState != EWubUnitState.CREATED );
    environ = aEnviron; // temporary set environment
    ValidationResult vr = doInit( aEnviron );
    TsInternalErrorRtException.checkNull( vr );
    if( !vr.isError() ) {
      environ = aEnviron;
      wuState = EWubUnitState.INITED;
    }
    else {
      environ = null; // if initialization fails the environment becomes unaccessible
    }
    return vr;
  }

  @Override
  final public EWubUnitState state() {
    return wuState;
  }

  @Override
  final public ITsContextRo environ() {
    return environ;
  }

  @Override
  final public IWubUnitDiagnostics getUnitDiagnostics() {
    return diagnosticsInfo.createCopy();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the editable internal instance of the diagnostics info to be update by subclasses.
   *
   * @return {@link WubUnitDiagnostics} - editable diagnostics info
   */
  public WubUnitDiagnostics diagnosticsEdit() {
    return diagnosticsInfo;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must perform any resource allocation and preparation tasks here.
   *
   * @param aEnviron {@link ITsContextRo} - the execution environment, never is <code>null</code>
   * @return {@link ValidationResult} - initialization success result
   */
  protected abstract ValidationResult doInit( ITsContextRo aEnviron );

  /**
   * Subclass may perform additional actions after initializing but before for call of {@link #doJob()}.
   * <p>
   * Does nothing in base class, there is no need to call parent method when overriding.
   */
  protected void doStart() {
    // nop
  }

  /**
   * Implementation must perform ir's work in this method.
   * <p>
   * It is very important the method to return as soon as possible. The method is called periodically, so the
   * implementation must divide the work into as small and fast parts as possible.
   */
  protected abstract void doDoJob();

  /**
   * Implementation must immediately release all allocated resources and stop working if possible.
   * <p>
   * If immediate stopping is not possible method must return <code>false</code> and unit should continue stopping
   * process in {@link #doStopping()}.
   *
   * @return boolean - <code>true</code> if unit has stopped
   */
  protected abstract boolean doQueryStop();

  /**
   * Subclass must override if {@link #doQueryStop()} may return <code>false</code>.
   * <p>
   * When {@link #queryStop()} does not stops unit immediately the stopping process begins. During the
   * {@link EWubUnitState#STOP_QUERIED} state this method is called instead of {@link #doDoJob()} from the
   * {@link #doJob()}.
   * <p>
   * Implementation must check or perform stopping process and return <code>true</code> when unit stops successfully.
   * <p>
   * Returns <code>false</code> in base class, there is no need to call parent method when overriding.
   *
   * @return boolean - <code>true</code> if unit has stopped
   */
  protected boolean doStopping() {
    // nop
    return false;
  }

  /**
   * Subclass may perform special actions when normal stop process is not completed.
   * <p>
   * Method is called from {@link #destroy()} only if unit is not stopped. Note that when this method is called the unit
   * may be in any state except {@link EWubUnitState#CREATED} and {@link EWubUnitState#STOPPED}.
   * <p>
   * Does nothing in base class, there is no need to call parent method when overriding.
   */
  protected void doDestroy() {
    // nop
  }

}
