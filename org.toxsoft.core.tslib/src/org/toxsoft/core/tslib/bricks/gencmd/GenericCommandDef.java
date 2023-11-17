package org.toxsoft.core.tslib.bricks.gencmd;

import static org.toxsoft.core.tslib.bricks.gencmd.IGenericCommandConstants.*;
import static org.toxsoft.core.tslib.bricks.gencmd.ITsResources.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCommandDef} implementation base.
 *
 * @author hazard157
 */
public non-sealed class GenericCommandDef
    extends StridableParameterized
    implements IGenericCommandDef {

  private final IStridablesListEdit<IDataDef> argDefs = new StridablesList<>();

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
  public GenericCommandDef( String aCmdId, IOptionSet aParams ) {
    super( aCmdId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IGenericCommandDef
  //

  @Override
  final public boolean isUnknownArgsAllowed() {
    return OPDEF_IS_UNKNOWN_ARGS_ALLOWED.getValue( params() ).asBool();
  }

  @Override
  final public IStridablesListEdit<IDataDef> listArgsDefs() {
    return argDefs;
  }

  @Override
  final public ValidationResult canExecCommand( IOptionSet aArgs ) {
    ValidationResult vr = OptionSetUtils.validateOptionSet( aArgs, argDefs );
    if( vr.isError() ) {
      return vr;
    }
    if( !isUnknownArgsAllowed() ) {
      for( String argId : aArgs.keys() ) {
        if( !argDefs.hasKey( argId ) ) {
          return ValidationResult.error( FMT_ERR_UNKNOWN_ARG, id(), argId );
        }
      }
    }
    return doCanExecCommand( aArgs );
  }

  @Override
  final public GenericCommand createCommand( IOptionSet aArgs ) {
    TsValidationFailedRtException.checkError( canExecCommand( aArgs ) );
    return GenericCommand.of( id(), aArgs );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional arguments check.
   * <p>
   * <code>aArgs</code> are already checked by {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}
   * against definitions {@link #listArgsDefs()}. Also, if {@link #isUnknownArgsAllowed()} is <code>true</code>
   * arguments are guaranteed to not contain options not listed in {@link #listArgsDefs()}.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aArgs {@link IOptionSet} - the command arguments to check
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanExecCommand( IOptionSet aArgs ) {
    return ValidationResult.SUCCESS;
  }

}
