package org.toxsoft.core.tslib.bricks.coopcomp;

import static org.toxsoft.core.tslib.bricks.coopcomp.ETsCoopCompState.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * The only base implementation of {@link ITsCooperativeComponent}.
 *
 * @author hazard157
 */
abstract class AbstractTsCooperativeComponent
    implements ITsCooperativeComponent {

  private ITsContextRo initArgs = null;

  /**
   * Current state of the component.
   * <p>
   * Package-private field ensures that changes may be done only this class and permitted children.
   */
  ETsCoopCompState compState = CREATED;

  /**
   * Constructor.
   */
  protected AbstractTsCooperativeComponent() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void ensureComponentState( ETsCoopCompState aState ) {
    if( compState != aState ) {
      throw new TsIllegalStateRtException();
    }
  }

  private void ensureComponentState( ETsCoopCompState aState1, ETsCoopCompState aState2 ) {
    if( compState != aState1 && compState != aState2 ) {
      throw new TsIllegalStateRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsInitializable
  //

  @Override
  final public ValidationResult init( ITsContextRo aArgs ) {
    ensureComponentState( CREATED, INITIALIZED );
    TsNullArgumentRtException.checkNull( aArgs );
    initArgs = aArgs; // temporary set arguments
    ValidationResult vr;
    try {
      vr = doInit( aArgs );
      TsInternalErrorRtException.checkNull( vr );
    }
    catch( Exception ex ) {
      vr = ValidationResult.error( ex );
    }
    if( !vr.isError() ) {
      initArgs = aArgs;
      compState = INITIALIZED;
    }
    else {
      initArgs = null; // if initialization fails the arguments becomes unaccessible
    }
    return vr;
  }

  // ------------------------------------------------------------------------------------
  // IWorkerComponent
  //

  @Override
  final public void start() {
    ensureComponentState( INITIALIZED );
    doStart();
    compState = WORKING;
  }

  @Override
  final public boolean queryStop() {
    ensureComponentState( INITIALIZED, WORKING );
    compState = STOPPING;
    if( doQueryStop() ) {
      internalSetToFinalState();
      return true;
    }
    return false;
  }

  @Override
  final public boolean isStopped() {
    return switch( compState ) {
      case CREATED, WORKING -> throw new TsIllegalStateRtException();
      case DESTROYED, INITIALIZED -> true;
      case STOPPING -> false;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
  }

  @Override
  final public void destroy() {
    if( compState == DESTROYED ) {
      return;
    }
    try {
      doDestroy();
    }
    catch( Exception ex ) {
      LoggerUtils.error( ex );
    }
    compState = DESTROYED;
  }

  // ------------------------------------------------------------------------------------
  // ICooperativeMultiTaskable
  //

  @Override
  final public void doJob() {
    ensureComponentState( WORKING, STOPPING );
    if( compState == WORKING ) {
      doDoJob();
    }
    else {
      if( doStopping() ) {
        internalSetToFinalState();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCooperativeComponent
  //

  @Override
  final public ETsCoopCompState compState() {
    return compState;
  }

  @Override
  final public ITsContextRo compInitArgs() {
    return initArgs;
  }

  // ------------------------------------------------------------------------------------
  // Methods for permitted child classes
  //

  /**
   * Sets component to final state either {@link ETsCoopCompState#INITIALIZED} or {@link ETsCoopCompState#DESTROYED}.
   */
  abstract void internalSetToFinalState();

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must perform any resource allocation and preparation tasks here.
   *
   * @param aArgs {@link ITsContextRo} - the execution environment, never is <code>null</code>
   * @return {@link ValidationResult} - initialization success result
   */
  protected abstract ValidationResult doInit( ITsContextRo aArgs );

  /**
   * Subclass may perform additional actions after initializing but before first call of {@link #doJob()}.
   * <p>
   * Called from {@link #start()}.
   * <p>
   * Does nothing in base class, there is no need to call parent method when overriding.
   */
  protected void doStart() {
    // nop
  }

  /**
   * Implementation must perform ir's work in this method.
   * <p>
   * Called from {@link #doJob()} in {@link ETsCoopCompState#WORKING} state.
   * <p>
   * Note: this method is <b>not</b> called during stopping process, {@link #doStopping()} is called instead.
   * <p>
   * It is very important the method to return as soon as possible. The method is called periodically, so the
   * implementation must divide the work into as small and fast parts as possible.
   */
  protected abstract void doDoJob();

  /**
   * Implementation must immediately release all allocated resources and stop working if possible.
   * <p>
   * If immediate stopping is not possible method must return <code>false</code> and component should continue stopping
   * process in {@link #doStopping()}.
   * <p>
   * Implementation must <b>not</b> throw any exception.
   *
   * @return boolean - <code>true</code> if component has stopped
   */
  protected abstract boolean doQueryStop();

  /**
   * Implementation must perform stopping job after stop was queried if needed.
   * <p>
   * Called from {@link #doJob()} in {@link ETsCoopCompState#STOPPING} state.
   * <p>
   * Subclass must override if {@link #doQueryStop()} may return <code>false</code>.
   * <p>
   * Implementation must check or perform stopping process and return <code>true</code> when component stops
   * successfully. Implementation must <b>not</b> throw any exception.
   * <p>
   * Returns <code>false</code> in base class, there is no need to call parent method when overriding.
   *
   * @return boolean - <code>true</code> if component has stopped
   */
  protected boolean doStopping() {
    // nop
    return false;
  }

  /**
   * Subclass may perform special actions when normal stop process is not completed.
   * <p>
   * Called from {@link #destroy()}.
   * <p>
   * Method is called from {@link #destroy()} only if component is not destroyed yet.
   * <p>
   * Does nothing in base class, there is no need to call parent method when overriding.
   */
  protected void doDestroy() {
    // nop
  }

}
