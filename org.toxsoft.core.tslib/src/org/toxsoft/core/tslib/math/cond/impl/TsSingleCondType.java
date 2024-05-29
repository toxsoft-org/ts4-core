package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.math.cond.impl.ITsCombiConSharedResources.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsSingleCondType} implementation as a base class.
 *
 * @author hazard157
 */
public class TsSingleCondType
    extends StridableParameterized
    implements ITsSingleCondType {

  /**
   * Values returned by {@link #doHumanReadableDescription(IOptionSet)} for {@link ITsSingleCondInfo#TYPE_ID_ALWAYS}.
   */
  public static final String HUMAN_READABLE_DESCR_ALWAYS = "ALWAYS"; //$NON-NLS-1$

  /**
   * Values returned by {@link #doHumanReadableDescription(IOptionSet)} for {@link ITsSingleCondInfo#TYPE_ID_NEVER}.
   */
  public static final String HUMAN_READABLE_DESCR_NEVER = "never"; //$NON-NLS-1$

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
  public TsSingleCondType( String aId, IOptionSet aParams, IStridablesList<IDataDef> aParamDefs ) {
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
  public TsSingleCondType( String aId, IOptionSet aParams ) {
    this( aId, aParams, IStridablesList.EMPTY );
  }

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public TsSingleCondType( String aId ) {
    this( aId, IOptionSet.NULL, IStridablesList.EMPTY );
  }

  /**
   * Initializes {@link #params()} in the chained construction string.
   *
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @return {@link TsSingleCondType} - <code><b>this</b></code> instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsSingleCondType setParams( Object... aIdsAndValues ) {
    params().setAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    return this;
  }

  /**
   * Initializes {@link #paramDefs()} in the chained construction string.
   *
   * @param aParamDefs {@link IDataDef}[] - description of the condition parameters
   * @return {@link TsSingleCondType} - <code><b>this</b></code> instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsSingleCondType setDefs( IDataDef... aParamDefs ) {
    paramDefs().setAll( aParamDefs );
    return this;
  }

  // ------------------------------------------------------------------------------------
  // ISingleCondType
  //

  @Override
  final public IStridablesListEdit<IDataDef> paramDefs() {
    return paramDefs;
  }

  @Override
  final public ValidationResult validateParams( IOptionSet aCondParams ) {
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
   * <li>{@link ITsSingleCondInfo#typeId()} is equal to this type ID {@link #id()};</li>
   * <li>invokes {@link #validateParams(IOptionSet)} on {@link ITsSingleCondInfo#params()}.</li>
   * </ul>
   *
   * @param aParams {@link ITsSingleCondInfo} - single condition checker creation parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException check failed
   */
  final public void checkCreationArg( ITsSingleCondInfo aParams ) {
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
   * Returned value depends on {@link #id()}:
   * <ul>
   * <li>for {@link ITsSingleCondInfo#TYPE_ID_ALWAYS TYPE_ID_ALWAYS} returns {@link #HUMAN_READABLE_DESCR_ALWAYS};</li>
   * <li>for {@link ITsSingleCondInfo#TYPE_ID_NEVER TYPE_ID_NEVER} returns {@link #HUMAN_READABLE_DESCR_NEVER};</li>
   * <li>for all other type IDs returns {@link IOptionSet#toString() ITsSingleCondInfo.params().toString()}.</li>
   * </ul>
   * There is no need to call superclass method when overriding.
   *
   * @param aCondParams {@link IOptionSet} - values already checked with
   *          {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}
   * @return String - human-readable description, never is <code>null</code>
   */
  protected String doHumanReadableDescription( IOptionSet aCondParams ) {
    return switch( id() ) {
      case ITsSingleCondInfo.TYPE_ID_ALWAYS -> HUMAN_READABLE_DESCR_ALWAYS;
      case ITsSingleCondInfo.TYPE_ID_NEVER -> HUMAN_READABLE_DESCR_NEVER;
      default -> aCondParams.toString();
    };
  }

}
