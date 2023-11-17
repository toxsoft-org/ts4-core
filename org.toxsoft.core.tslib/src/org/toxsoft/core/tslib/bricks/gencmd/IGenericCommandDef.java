package org.toxsoft.core.tslib.bricks.gencmd;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The command definition includes command ID and the command argument description.
 *
 * @author hazard157
 */
public sealed interface IGenericCommandDef
    extends IStridableParameterized permits GenericCommandDef {

  /**
   * Determines if arguments not listed in {@link #listArgsDefs()} are allowed to be placed in the command.
   * <p>
   * Simply returns the value of the option {@link IGenericCommandConstants#OPDEF_IS_UNKNOWN_ARGS_ALLOWED} from the
   * {@link #params()}.
   *
   * @return boolean - <code>true</code> unlisted options are allowed as the arguments
   */
  boolean isUnknownArgsAllowed();

  /**
   * Returns the command argument definitions.
   *
   * @return {@link IStridablesList}&gt;{@link IDataDef}&gt; - list of all possible commands
   */
  IStridablesList<IDataDef> listArgsDefs();

  /**
   * Checks command argument values validity against definitions {@link #listArgsDefs()}.
   * <p>
   * Check includes:
   * <ul>
   * <li>atomic type of the value is compatible with the argument definition;</li>
   * <li>mandatory options are present;</li>
   * <li>if {@link #isUnknownArgsAllowed()}=<code>true</code> also checks if any option, other than listed in
   * {@link #listArgsDefs()} exists in <code>aArgs</code>;</li>
   * <li>any additional checks performed by the implementation.</li>
   * </ul>
   *
   * @param aArgs {@link IOptionSet} - the command arguments
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canExecCommand( IOptionSet aArgs );

  /**
   * Creates the instance of the command with the arguments check.
   * <p>
   * Command will have {@link GenericCommand#cmdId()} equal to {@link #id()} of this definition.
   *
   * @param aArgs {@link IOptionSet} - the command arguments
   * @return {@link GenericCommand} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed call to {@link #canExecCommand(IOptionSet)}
   */
  GenericCommand createCommand( IOptionSet aArgs );

}
