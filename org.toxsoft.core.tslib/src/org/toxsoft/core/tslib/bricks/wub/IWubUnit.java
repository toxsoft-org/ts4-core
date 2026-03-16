package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The worker unit is an identifiable piece of code managed by the container (the box).
 * <p>
 * Method {@link #params()} returns the unit <i>creation parameters</i> as specified in constructor
 * {@link AbstractWubUnit#AbstractWubUnit(String, IOptionSet)}. The <i>initialization parameters</i> specified when
 * calling {@link #init(ITsContextRo)} are different one. Creation parameters are specified by the unit implementation
 * subclass by the unit programmer, they are fixed and used to describe the unit. Initialization parameters are
 * specified by the container and may have different values on each invocation.
 * <p>
 * Note:
 * <ul>
 * <li>WUB unit does <b>not</b> allows {@link #start()} to be called after stop is requested. Such call will cause
 * {@link #start()} to throw {@link TsIllegalStateRtException};</li>
 * <li>All implementation of this interface must be based on {@link AbstractWubUnit}.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IWubUnit
    extends IStridableParameterized, ICooperativeMultiTaskable, IWorkerComponent {

  /**
   * Initializes the unit to work in the environment specified as an argument.
   * <p>
   * Once successfully initialized (that is, put in execution environment) the unit can not be initialized again. It may
   * be started/stopped but not initialized.
   * <p>
   * Method does not throws an exception rather returns the error in {@link ValidationResult}.
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
