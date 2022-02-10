package org.toxsoft.core.tsgui.m5;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.m5.model.impl.M5Model;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5 modelling domain.
 *
 * @author hazard157
 */
public interface IM5Domain
    extends IStridable, ITsGuiContextable {

  /**
   * Lists all models in this domain.
   *
   * @return {@link IStridablesList}&lt;{@link IM5Model}&gt; - list of all models
   */
  IStridablesList<IM5Model<?>> models();

  /**
   * Lists models owned by this domain without models from parent domains.
   * <p>
   * This methios returns the subset of {@link #models()}.
   *
   * @return {@link IStridablesList}&lt;{@link IM5Model}&gt; - list of models owned by this domain
   */
  IStridablesList<IM5Model<?>> selfModels();

  /**
   * Finds model by ID.
   *
   * @param <T> - expected type of modelled entity
   * @param aModelId String - the model ID
   * @return {@link IM5Model} - the model or <code>null</code> if no such model exsists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> IM5Model<T> findModel( String aModelId );

  /**
   * Returns model by ID.
   *
   * @param <T> - expected type of modelled entity
   * @param aModelId String - the model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - expected type of modelled entity
   * @return {@link IM5Model} - the model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no model with specified ID found
   */
  <T> IM5Model<T> getModel( String aModelId, Class<T> aEntityClass );

  /**
   * Adds model to the models owned by this domain.
   * <p>
   * If model with the same ID is already owned by this domain then the excpetion will be thrown.
   * <p>
   * If model with the same ID is in on of the acestor domains, model will be added to this domain and further the new
   * model will be found and returned by ID either by {@link #findModel(String)}, {@link #getModel(String, Class)} or
   * found in {@link #models()} and {@link #selfModels()} lists.
   *
   * @param <T> - type of modelled entity
   * @param aModel {@link M5Model}&lt;T&gt; - the model to add
   * @return {@link IM5Model}&lt;T&gt; - always returns the argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException model with the same ID already exists in {@link #selfModels()}
   */
  <T> IM5Model<T> addModel( M5Model<T> aModel );

  /**
   * Adds new or replaces existing model.
   * <p>
   * Еhis method, in contrast to the method {@link #addModel(M5Model)} does not throws exception but replaces existing
   * model with the same ID.
   *
   * @param <T> - type of modelled entity
   * @param aModel {@link M5Model}&lt;T&gt; - the model to add
   * @return {@link IM5Model}&lt;T&gt; - always returns the argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> IM5Model<T> replaceModel( M5Model<T> aModel );

  /**
   * Removes the model from {@link #selfModels()}.
   * <p>
   * If this domain contain no such model method does nothing.
   *
   * @param aModelId String - ID of model to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeModel( String aModelId );

  /**
   * Return the parent domain,if any.
   *
   * @return {@link IM5Domain} - the parent domain or <code>null</code> for root domain
   */
  IM5Domain parent();

  /**
   * Creates the child domain.
   *
   * @param aId String - the domain ID (IDpath)
   * @param aContext {@link ITsGuiContext} - new odmain context
   * @return {@link IM5Domain} - the child domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException child domain with specified ID already exists
   */
  IM5Domain createChildDomain( String aId, ITsGuiContext aContext );

}
