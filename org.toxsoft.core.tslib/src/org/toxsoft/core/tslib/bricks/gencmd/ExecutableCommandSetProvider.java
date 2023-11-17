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
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCommandSetProvider} implementation base.
 *
 * @author hazard157
 */
public non-sealed abstract class ExecutableCommandSetProvider
    implements IGenericCommandSetProvider {

  private final IStridablesListEdit<AbstractExcutableCommandDef> cmdDefs = new StridablesList<>();

  /**
   * Constructor.
   */
  public ExecutableCommandSetProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IGenericCommandSetProvider
  //

  @Override
  final public IOptionSet execCommand( GenericCommand aCommand ) {
    ValidationResult vr = canExecCommand( aCommand );
    TsValidationFailedRtException.checkError( vr );
    IOptionSetEdit result = new OptionSet();
    OPDEF_COMMAND_RESULT.setValue( result, avValobj( vr ) );
    // invoke executor
    AbstractExcutableCommandDef exCd = cmdDefs.getByKey( aCommand.cmdId() );
    exCd.doExecCommand( aCommand.args(), result );
    return result;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStridablesList<IGenericCommandDef> listCommandDefs() {
    return (IStridablesList)cmdDefs;
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

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Add the command definition to the list {@link #listCommandDefs()}.
   *
   * @param aDef {@link IGenericCommandDef} - the executable command definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException definition with the same ID already was added
   */
  public void addCommandDefinition( AbstractExcutableCommandDef aDef ) {
    TsNullArgumentRtException.checkNull( aDef );
    TsItemAlreadyExistsRtException.checkTrue( cmdDefs.hasKey( aDef.id() ) );
    cmdDefs.add( aDef );
  }

}
