package org.toxsoft.core.tslib.math.cond.filter;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages filter types of the one topic.
 *
 * @author hazard157
 * @param <T> - class of the input objects to be filtered by {@link ITsFilter#accept(Object)}
 */
public interface ITsFilterTopicManager<T>
    extends ITsConditionsTopicManager {

  /**
   * Returns the class of the input objects to be filtered by {@link ITsFilter#accept(Object)}.
   *
   * @return {@link Class}&lt;T&gt; - filter input objects class
   */
  Class<T> filterObjectClass();

  /**
   * Creates the combined filter instance.
   *
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the combined condition description
   * @return {@link ITsFilter}&lt;T&gt; - created combined filter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #checkCombiCondInfo(ITsCombiCondInfo)}
   */
  ITsFilter<T> createCombiFilter( ITsCombiCondInfo aCombiCondInfo );

  /**
   * Registers the filter type.
   *
   * @param aType {@link ITsSingleFilterType} - the type to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException type with the same ID is already registered
   */
  void registerType( ITsSingleFilterType aType );

  /**
   * Returns all single filter types of this topic.
   *
   * @return {@link IStridablesList}&lt;{@link ITsSingleFilterType}&gt; - list of types
   */
  IStridablesList<ITsSingleFilterType> singleTypes();

}
