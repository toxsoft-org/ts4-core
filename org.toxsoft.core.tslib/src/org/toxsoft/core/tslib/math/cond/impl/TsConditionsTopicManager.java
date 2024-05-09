package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.math.cond.impl.ITsCombiConSharedResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsConditionsTopicManager} implementation.
 *
 * @author hazard157
 * @param <T> - class of the managed types, subclass of {@link ITsSingleCondType}
 */
public class TsConditionsTopicManager<T extends ITsSingleCondType>
    implements ITsConditionsTopicManager, ITsClearable {

  private final IStridablesListEdit<T> typesList = new StridablesList<>();

  // ------------------------------------------------------------------------------------
  // ITsConditionsTopicManager
  //

  @Override
  final public ITsCombiCondInfoBuilder newBuilder() {
    return new TsCombiCondInfoBuilder( this );
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  final public IStridablesList<ITsSingleCondType> listSingleCondTypes() {
    return (IStridablesList)typesList;
  }

  @Override
  final public ValidationResult checkSingleCondInfo( ITsSingleCondInfo aSingleCondInfo ) {
    TsNullArgumentRtException.checkNull( aSingleCondInfo );
    T scType = typesList.findByKey( aSingleCondInfo.typeId() );
    if( scType == null ) {
      return ValidationResult.error( FMT_ERR_UNKNOWN_COND_TYPE_ID, aSingleCondInfo.typeId() );
    }
    ValidationResult vr = scType.validateParams( aSingleCondInfo.params() );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, doCheckSingleCondInfo( aSingleCondInfo, scType ) );
  }

  @Override
  final public ValidationResult checkCombiCondInfo( ITsCombiCondInfo aCombiCondInfo ) {
    TsNullArgumentRtException.checkNull( aCombiCondInfo );
    ValidationResult vr = ValidationResult.SUCCESS;
    // check each single filters
    for( ITsSingleCondInfo scInf : aCombiCondInfo.singleInfos() ) {
      vr = ValidationResult.firstNonOk( vr, checkSingleCondInfo( scInf ) );
      if( vr.isError() ) {
        return vr;
      }
    }
    return ValidationResult.firstNonOk( vr, doCheckSingleCondInfo( aCombiCondInfo ) );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  final public void clear() {
    typesList.clear();
    doAfterClear();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  protected IStridablesListEdit<T> typesList() {
    return typesList;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Subclass may perform additional check.
   * <p>
   * Argument is checked to be of known <code>aType</code> and parameters has already passed (without error) the check
   * by the method {@link ITsSingleCondType#validateParams(IOptionSet)}.
   * <p>
   * In the base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when
   * overriding.
   *
   * @param aSingleCondInfo {@link ITsSingleCondInfo} - the description to check
   * @param aType &lt;T&gt; - type of the <code>aSingleCondInfo</code>
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCheckSingleCondInfo( ITsSingleCondInfo aSingleCondInfo, T aType ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Subclass may perform additional check.
   * <p>
   * All elements of {@link ITsCombiCondInfo#singleInfos()} are checked by the method
   * {@link #checkSingleCondInfo(ITsSingleCondInfo)}.
   * <p>
   * In the base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when
   * overriding.
   *
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the combined condition description
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doCheckSingleCondInfo( ITsCombiCondInfo aCombiCondInfo ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Subclass may perform additional actions after {@link #typesList()} is cleared in {@link #clear()}.
   * <p>
   * For example, subclass may add ALWAYS/NEVER or other "builtin" types to ensure they are always registered.
   */
  protected void doAfterClear() {
    // nop
  }

}
