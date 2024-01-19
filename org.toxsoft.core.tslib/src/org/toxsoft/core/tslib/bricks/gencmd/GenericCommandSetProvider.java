package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.bricks.gencmd.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Default implementation of {@link IGenericCommandSetProvider}.
 * <p>
 * Use {@link #addCommandDefinition(IGenericCommandDef, IGenericCommandExecutor)} to add command to the set.
 *
 * @author hazard157
 */
public final class GenericCommandSetProvider
    implements IGenericCommandSetProvider {

  private final IStridablesListEdit<IGenericCommandDef> cmdDefs      = new StridablesList<>();
  private final IStringMapEdit<IGenericCommandExecutor> executorsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public GenericCommandSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IGenericCommandSetProvider
  //

  @Override
  public IOptionSet execCommand( GenericCommand aCommand ) {
    TsValidationFailedRtException.checkError( canExecCommand( aCommand ) );
    IGenericCommandExecutor cmdExec = executorsMap.getByKey( aCommand.cmdId() );
    IOptionSet result = cmdExec.execCommand( aCommand );
    TsInternalErrorRtException.checkNoNull( result );
    return result;
  }

  @Override
  public IStridablesList<IGenericCommandDef> listCommandDefs() {
    return cmdDefs;
  }

  @Override
  public ValidationResult canExecCommand( GenericCommand aCommand ) {
    TsNullArgumentRtException.checkNull( aCommand );
    IGenericCommandDef cmdDef = cmdDefs.findByKey( aCommand.cmdId() );
    if( cmdDef == null ) {
      return ValidationResult.error( FMT_ERR_UNKNOWN_CMD_ID, aCommand.cmdId() );
    }
    ValidationResult vr = cmdDef.canExecCommand( aCommand.args() );
    if( vr.isError() ) {
      return vr;
    }
    IGenericCommandExecutor cmdExec = executorsMap.getByKey( aCommand.cmdId() );
    return cmdExec.canExecCommand( aCommand.args() );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

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
