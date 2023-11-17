package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.gencmd.IGenericCommandConstants.*;
import static org.toxsoft.core.tslib.bricks.gencmd.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCommandSetProvider} implementation base.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractGenericCommandSetProvider
    implements IGenericCommandSetProvider {

  private final IStridablesListEdit<IGenericCommandDef> cmdDefs      = new StridablesList<>();
  private final IStringMapEdit<IGenericCommandExecutor> executorsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public AbstractGenericCommandSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IGenericCommandSetProvider
  //

  @Override
  final public IOptionSet execCommand( GenericCommand aCommand ) {
    ValidationResult vr = canExecCommand( aCommand );
    TsValidationFailedRtException.checkError();
    IOptionSetEdit result = new OptionSet();
    OPDEF_COMMAND_RESULT.setValue( result, avValobj( vr ) );
    doExecCommand( aCommand, result );
    return result;
  }

  @Override
  public IStridablesList<IGenericCommandDef> listCommandDefs() {
    return cmdDefs;
  }

  @Override
  final public ValidationResult canExecCommand( GenericCommand aCommand ) {
    TsNullArgumentRtException.checkNull( aCommand );
    IGenericCommandDef cmdDef = listCommandDefs().findByKey( aCommand.cmdId() );
    if( cmdDef == null ) {
      return ValidationResult.error( FMT_ERR_UNKNOWN_CMD_ID, aCommand.cmdId() );
    }
    ValidationResult vr = cmdDef.canExecCommand( aCommand.args() );
    if( vr.isError() ) {
      return vr;
    }
    return doCanExecCommand( aCommand );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional check if command can be executed.
   * <p>
   * It is guaranteed that <code>aCommand</code> is known command and already successfully passed
   * {@link IGenericCommandDef#canExecCommand(IOptionSet)} of the corresponding definition.
   * <p>
   * In the base class returns {@link ValidationResult#SUCCESS} there is no need to call superclass method when
   * overriding.
   *
   * @param aCommand {@link GenericCommand} - the command to execute, not <code>null</code>
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanExecCommand( GenericCommand aCommand ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Implementation must execute command with the specified arguments.
   * <p>
   * Arguments is checked by {@link #canExecCommand(GenericCommand)}.
   * <p>
   * It is strongly recommended to put execution result if command execution fails.
   *
   * @param aCommmand {@link GenericCommand} - the command to execute
   * @param aResult {@link IOptionSetEdit} - the results to be filled optionally
   */
  protected abstract void doExecCommand( GenericCommand aCommmand, IOptionSetEdit aResult );

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Add the command definition to the list {@link #listCommandDefs()}.
   *
   * @param aDef {@link IGenericCommandDef} - the command definition
   * @param aExecutor {@link IGenericCommandExecutor} - the command executor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException definition with the same ID already was added
   */
  public void addCommandDefinition( IGenericCommandDef aDef, IGenericCommandExecutor aExecutor ) {
    TsNullArgumentRtException.checkNulls( aDef, aExecutor );
    TsItemAlreadyExistsRtException.checkTrue( cmdDefs.hasKey( aDef.id() ) );
    cmdDefs.add( aDef );
    executorsMap.put( aDef.id(), aExecutor );
  }

}
