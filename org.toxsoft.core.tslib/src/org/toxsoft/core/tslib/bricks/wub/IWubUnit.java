package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The worker unit is an identifiable piece of code managed by the container (the box).
 * <p>
 * Method {@link #params()} returns the unit creation parameters.
 * <p>
 * Note: WUB unit does <b>not</b> allows {@link #start()} to be called after stop is requested. Such call will cause
 * {@link #start()} to throw {@link TsIllegalStateRtException}.
 *
 * @author hazard157
 */
public sealed interface IWubUnit
    extends IStridableParameterized, ICooperativeMultiTaskable, IWorkerComponent
    permits IWubBox, AbstractWubUnit {

  /**
   * Initializes the unit to work in the environment specified as an argument.
   * <p>
   * Once successfully initialized (that is, put in execution environment) the unit can not initialized again. It may be
   * started/stopped but not initialized.
   *
   * @param aEnviron {@link ITsContextRo} - the execution environment
   * @return {@link ValidationResult} - initialization success result
   */
  ValidationResult init( ITsContextRo aEnviron );

  /**
   * Returns the current state of the unit.
   *
   * @return boolean - the unit state
   */
  EWubUnitState state();

  /**
   * Returns the arguments of the {@link #init(ITsContextRo)} call.
   * <p>
   * Calling this method before successful initialization return <code>null</code>.
   *
   * @return {@link ITsContextRo} - the execution environment or <code>null</code> if not initialized yet
   */
  ITsContextRo environ();

  /**
   * Returns the diagnostics information about the unit.
   * <p>
   * Calling this method before successful initialization is harmless however return values may be senseless.
   *
   * @return {@link IWubUnitDiagnostics} - the diagnostics information
   */
  IWubUnitDiagnostics getUnitDiagnostics();

}
