package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.gencmd.IGenericCommandConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCommandDef} implementation united with the command executor.
 *
 * @author hazard157
 */
public abstract class AbstractExcutableCommandDef
    extends GenericCommandDef
    implements IGenericCommandExecutor {

  /**
   * Constructor.
   *
   * @param aCmdId String - the command ID
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public AbstractExcutableCommandDef( String aCmdId, IOptionSet aParams ) {
    super( aCmdId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IGenericCommandExecutor
  //

  /**
   * {@inheritDoc}
   *
   * @throws TsIllegalArgumentRtException command ID is not {@link #id()}
   * @throws TsValidationFailedRtException failed {@link #canExecCommand(IOptionSet)}
   */
  @Override
  final public IOptionSet execCommand( GenericCommand aCommand ) {
    TsNullArgumentRtException.checkNull( aCommand );
    TsIllegalArgumentRtException.checkFalse( aCommand.cmdId().equals( id() ) );
    TsValidationFailedRtException.checkError( canExecCommand( aCommand.args() ) );
    IOptionSetEdit result = new OptionSet();
    ValidationResult vr = canExecCommand( aCommand.args() );
    if( !vr.isError() ) {
      OPDEF_COMMAND_RESULT.setValue( result, avValobj( vr ) );
    }
    doExecCommand( aCommand.args(), result );
    return result;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must execute command with the specified arguments.
   * <p>
   * Arguments are checked by {@link #canExecCommand(IOptionSet)}.
   * <p>
   * It is strongly recommended to return execution result if command execution fails.
   * <p>
   * Note: this method is call both by {@link #execCommand(GenericCommand)} and
   * {@link ExecutableCommandSetProvider#execCommand(GenericCommand)}.
   *
   * @param aArgs {@link IOptionSet} - command arguments
   * @param aResult {@link IOptionSetEdit} - the results to be filled optionally
   */
  protected abstract void doExecCommand( IOptionSet aArgs, IOptionSetEdit aResult );

}
