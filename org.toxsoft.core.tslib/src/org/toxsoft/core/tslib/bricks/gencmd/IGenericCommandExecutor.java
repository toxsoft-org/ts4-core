package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.gencmd.IGenericCommandConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
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
   * Simple return value of command execution specifying the success of the execution.
   */
  IOptionSet OK_RESULT = OptionSetUtils.createOpSet( OPID_COMMAND_RESULT, avValobj( ValidationResult.SUCCESS ) );

  /**
   * Simple return value of command execution specifying the failure of the execution.
   */
  IOptionSet FAIL_RESULT =
      OptionSetUtils.createOpSet( OPID_COMMAND_RESULT, avValobj( ValidationResult.error( EMPTY_STRING ) ) );

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

  /**
   * Determines if the specified command can be executed before the actual execution.
   * <p>
   * Implementation of this method is optional.
   *
   * @param aCommand {@link GenericCommand} - the command
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default ValidationResult canExecCommand( GenericCommand aCommand ) {
    return ValidationResult.SUCCESS;
  }

}
