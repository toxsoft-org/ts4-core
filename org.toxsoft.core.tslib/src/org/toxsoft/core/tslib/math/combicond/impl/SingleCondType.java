package org.toxsoft.core.tslib.math.combicond.impl;

import static org.toxsoft.core.tslib.math.combicond.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISingleCondType} base implementation.
 *
 * @author hazard157
 */
public class SingleCondType
    extends StridableParameterized
    implements ISingleCondType {

  private final IStridablesListEdit<IDataDef> paramDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @param aParamDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - description of the condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public SingleCondType( String aId, IOptionSet aParams, IStridablesList<IDataDef> aParamDefs ) {
    super( aId, aParams );
    paramDefs.setAll( aParamDefs );
  }

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public SingleCondType( String aId, IOptionSet aParams ) {
    this( aId, aParams, IStridablesList.EMPTY );
  }

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public SingleCondType( String aId ) {
    this( aId, IOptionSet.NULL, IStridablesList.EMPTY );
  }

  /**
   * Initializes {@link #params()} in the chained construction string.
   *
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @return {@link SingleCondType} - <code><b>this</b></code> instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleCondType setParams( Object... aIdsAndValues ) {
    params().setAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    return this;
  }

  /**
   * Initializes {@link #paramDefs()} in the chained construction string.
   *
   * @param aParamDefs {@link IDataDef}[] - description of the condition parameters
   * @return {@link SingleCondType} - <code><b>this</b></code> instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SingleCondType setDefs( IDataDef... aParamDefs ) {
    paramDefs().setAll( aParamDefs );
    return this;
  }

  // ------------------------------------------------------------------------------------
  // ISingleCondType
  //

  @Override
  public IStridablesListEdit<IDataDef> paramDefs() {
    return paramDefs;
  }

  @Override
  public ValidationResult validateParams( IOptionSet aCondParams ) {
    ValidationResult vr = OptionSetUtils.validateOptionSet( aCondParams, paramDefs );
    if( !vr.isError() ) {
      vr = ValidationResult.firstNonOk( vr, doValidateParams( aCondParams ) );
    }
    return vr;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Checks condition creation arguments and throws an exception on error.
   * <p>
   * Performs following checks:
   * <ul>
   * <li>{@link ISingleCondParams#typeId()} is equal to this type ID {@link #id()};</li>
   * <li>invokes {@link #validateParams(IOptionSet)} on {@link ISingleCondParams#params()}.</li>
   * </ul>
   *
   * @param aParams {@link ISingleCondParams} - single condition checker creation parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException check failed
   */
  final public void checkCreationArg( ISingleCondParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    if( !aParams.typeId().equals( id() ) ) {
      ValidationResult vr = ValidationResult.error( FMT_ERR_INV_COND_TYPE_ID, aParams.typeId(), id() );
      throw new TsValidationFailedRtException( vr );
    }
    TsValidationFailedRtException.checkError( validateParams( aParams.params() ) );
  }

  @Override
  public String humanReadableDescription( IOptionSet aCondParams ) {
    TsNullArgumentRtException.checkNull( aCondParams );
    ValidationResult vr = validateParams( aCondParams );
    if( vr.isError() ) {
      return vr.message();
    }
    return doHumanReadableDescription( aCondParams );
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Subclass may perform additional validation of the condition parameters.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}. There is no need to call superclass method when overriding.
   *
   * @param aCondParams {@link IOptionSet} - values already checked with
   *          {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}
   * @return {@link ValidationResult} - validation result
   */
  protected ValidationResult doValidateParams( IOptionSet aCondParams ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Subclass may override creation of the condition description.
   * <p>
   * In base class returns {@link IOptionSet#toString()}. There is no need to call superclass method when overriding.
   *
   * @param aCondParams {@link IOptionSet} - values already checked with
   *          {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}
   * @return String - human-readable description, never is <code>null</code>
   */
  protected String doHumanReadableDescription( IOptionSet aCondParams ) {
    return aCondParams.toString();
  }

}
