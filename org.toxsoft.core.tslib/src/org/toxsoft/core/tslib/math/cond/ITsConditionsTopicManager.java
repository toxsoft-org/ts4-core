package org.toxsoft.core.tslib.math.cond;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages single condition types associated with one topic.
 * <p>
 * This is a base interface.
 * <p>
 * TODO more about different condition check implementations: filters, checkers, etc
 *
 * @author hazard157
 */
public interface ITsConditionsTopicManager {

  /**
   * Always empty (containing no types) topic manager.
   */
  ITsConditionsTopicManager NONE = new InternalNoneConditionsTopicManager();

  /**
   * Returns the new instance of the combined conditions builder of this topic.
   *
   * @return {@link ITsCombiCondInfoBuilder} - new instance of the builder
   */
  ITsCombiCondInfoBuilder newBuilder();

  /**
   * Returns all single condition types of this topic.
   *
   * @return {@link IStridablesList}&lt;{@link ITsSingleCondType}&gt; - list of types
   */
  IStridablesList<ITsSingleCondType> listSingleCondTypes();

  /**
   * Checks if single condition description is valid for this topic.
   *
   * @param aSingleCondInfo {@link ITsSingleCondInfo} - the description to check
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult checkSingleCondInfo( ITsSingleCondInfo aSingleCondInfo );

  /**
   * Checks if combined condition can be created from the specified description.
   *
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the combined condition description
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult checkCombiCondInfo( ITsCombiCondInfo aCombiCondInfo );

}

class InternalNoneConditionsTopicManager
    implements ITsConditionsTopicManager {

  static final ValidationResult VR_ERR = ValidationResult.error( "NONE topic manager" ); //$NON-NLS-1$

  @Override
  public ITsCombiCondInfoBuilder newBuilder() {
    return ITsCombiCondInfoBuilder.NONE;
  }

  @Override
  public IStridablesList<ITsSingleCondType> listSingleCondTypes() {
    return IStridablesList.EMPTY;
  }

  @Override
  public ValidationResult checkSingleCondInfo( ITsSingleCondInfo aSingleCondInfo ) {
    TsNullArgumentRtException.checkNull( aSingleCondInfo );
    return VR_ERR;
  }

  @Override
  public ValidationResult checkCombiCondInfo( ITsCombiCondInfo aCombiCondInfo ) {
    TsNullArgumentRtException.checkNull( aCombiCondInfo );
    return VR_ERR;
  }

}
