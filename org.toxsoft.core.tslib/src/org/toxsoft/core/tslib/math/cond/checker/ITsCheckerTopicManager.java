package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages checker types of the one topic.
 *
 * @author hazard157
 * @param <E> - the checker environment class
 */
public interface ITsCheckerTopicManager<E>
    extends ITsConditionsTopicManager {

  /**
   * Returns the class of the checkers operating environment,
   *
   * @return {@link Class}&lt;T&gt; - checker environment class
   */
  Class<E> checkEnvironmentClass();

  /**
   * Creates the combined checker instance.
   *
   * @param aCombiCondInfo {@link ITsCombiCondInfo} - the combined condition description
   * @param aEnviron &lt;E&gt; - the environment
   * @return {@link ITsChecker}&lt;T&gt; - created combined checker
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #checkCombiCondInfo(ITsCombiCondInfo)}
   */
  ITsChecker createCombiChecker( ITsCombiCondInfo aCombiCondInfo, E aEnviron );

  /**
   * Registers the checker type.
   *
   * @param aType {@link ITsSingleCheckerType} - the type to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException type with the same ID is already registered
   */
  void registerType( ITsSingleCheckerType<E> aType );

  /**
   * Returns all single checker types of this topic.
   *
   * @return {@link IStridablesList}&lt;{@link ITsSingleCheckerType}&gt; - list of types
   */
  IStridablesList<ITsSingleCheckerType<E>> singleTypes();

}
