package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.gencmd.ITsResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * The package constants.
 *
 * @author hazard157
 */
public interface IGenericCommandConstants {

  /**
   * ID of the option {@link #OPDEF_COMMAND_RESULT}.
   */
  String OPID_COMMAND_RESULT = TS_ID + ".GenericCommand.Result"; //$NON-NLS-1$

  /**
   * ID of the option {@link #OPDEF_IS_UNKNOWN_ARGS_ALLOWED}.
   */
  String OPID_IS_UNKNOWN_ARGS_ALLOWED = ".GenericCommand.IsUnknownArgsAllowed"; //$NON-NLS-1$

  /**
   * <i>Result option:</i> contains the {@link ValidationResult} of the command execution. <br>
   * <i>Usage:</i> this is an option found in the {@link IOptionSet} returned by the
   * {@link IGenericCommandExecutor#execCommand(GenericCommand)} method.<br>
   * <i>Default value:</i> {@link ValidationResult#SUCCESS}
   */
  IDataDef OPDEF_COMMAND_RESULT = DataDef.create( OPID_COMMAND_RESULT, VALOBJ, //
      TSID_NAME, STR_COMMAND_RESULT, //
      TSID_DESCRIPTION, STR_COMMAND_RESULT_D, //
      TSID_KEEPER_ID, ValidationResult.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ValidationResult.SUCCESS ) //
  );

  /**
   * <i>Command option:</i> determines if unknown options are allowed and passed to the executor.<br>
   * <i>Usage: this is an option of the {@link IGenericCommandDef#params()}. When set to <code>true</code> it allows
   * command arguments, not listed in {@link IGenericCommandDef#listArgsDefs()} to pass validation by
   * {@link IGenericCommandDef#canExecCommand(IOptionSet)} and to be passed when creating command by the method
   * {@link IGenericCommandDef#createCommand(IOptionSet)}.</i><br>
   * <i>Default value: <code>false</code> (unknown options are prohibited</i>
   */
  IDataDef OPDEF_IS_UNKNOWN_ARGS_ALLOWED = DataDef.create( OPID_IS_UNKNOWN_ARGS_ALLOWED, BOOLEAN, //
      TSID_NAME, STR_IS_UNKNOWN_ARGS_ALLOWED, //
      TSID_DESCRIPTION, STR_IS_UNKNOWN_ARGS_ALLOWED_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

}
