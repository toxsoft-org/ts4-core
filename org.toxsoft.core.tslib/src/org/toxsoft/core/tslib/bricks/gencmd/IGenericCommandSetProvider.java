package org.toxsoft.core.tslib.bricks.gencmd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Provides the command definitions together with their execution means.
 * <p>
 * There is to implementations of this interface (both can be subclassed):
 * <ul>
 * <li>{@link ExecutableCommandSetProvider} - has built-in dispatcher to find and invoke command executor
 * {@link AbstractExcutableCommandDef} instances. Mostly can be used without subclassing;</li>
 * <li>{@link AbstractGenericCommandSetProvider} - must be subclassed to dispatch command execution, as an abstract
 * class need to be subclassed.</li>
 * </ul>
 *
 * @author hazard157
 */
public sealed interface IGenericCommandSetProvider
    extends IGenericCommandExecutor permits AbstractGenericCommandSetProvider,ExecutableCommandSetProvider {

  /**
   * returns all available command definitions.
   *
   * @return {@link IStridablesList}&lt;{@link IGenericCommandDef}&gt; - the list of supported command definitions
   */
  IStridablesList<IGenericCommandDef> listCommandDefs();

  /**
   * Determines if the specified command can be executed before the actual execution.
   * <p>
   * Success of this method does <b>not</b> guarantees that command will be executed successfully. However, if this
   * method returns error, execution is guaranteed to fail.
   * <p>
   * Method checks for:
   * <ul>
   * <li>the command ID is known;</li>
   * <li>success on {@link IGenericCommandDef#canExecCommand(IOptionSet)} call;</li>
   * <li>any additional check may be performed by concrete implementation of the provider.</li>
   * </ul>
   *
   * @param aCommand {@link GenericCommand} - the command to be executed
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canExecCommand( GenericCommand aCommand );

  /**
   * Dispatches the command execution to the corresponding element of the list {@link #listCommandDefs()}.
   *
   * @param aCommand {@link GenericCommand} - the command to execute
   * @return {@link IOptionSet} - execution result, may be {@link IOptionSet#NULL} but not <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed call to {@link #canExecCommand(GenericCommand)}
   */
  @Override
  IOptionSet execCommand( GenericCommand aCommand );

}
