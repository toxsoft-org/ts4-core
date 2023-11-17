package org.toxsoft.core.tslib.bricks.gencmd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mix-in interface of the entities capable to execute generic command.
 * <p>
 * Command execution may return the execution result of type {@link IOptionSet}. In the other words, command execution
 * is a kind of the method call where the command ID is the method name, command arguments are method arguments and the
 * {@link IOptionSet} is the method return value.
 *
 * @author hazard157
 */
public interface IGenericCommandExecutor {

  /**
   * Executes the command and optionally returns the execution result.
   *
   * @param aCommand {@link GenericCommand} - the command to execute
   * @return {@link IOptionSet} - execution result, may be {@link IOptionSet#NULL} but not <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException unknown command ID
   * @throws TsValidationFailedRtException any precondition check failed
   */
  IOptionSet execCommand( GenericCommand aCommand );

}
