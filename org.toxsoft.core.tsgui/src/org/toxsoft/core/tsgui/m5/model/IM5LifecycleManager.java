package org.toxsoft.core.tsgui.m5.model;

import java.io.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5-modeled entities lifecycle manager.
 * <p>
 * In general lifecycle manager (LM) has following functionality:
 * <ul>
 * <li><b>list</b> available items - {@link #itemsProvider()} provides available entities of modeled type, if allowed by
 * {@link #isCrudOpAllowed(ECrudOp) isAllowed(<b>LIST</b>)} = <code>true</code>. For example, Java <code>enum</code>
 * constants provider lists all constants of <code>enum</code>;</li>
 * <li><b>create</b> instances of entities - if allowed by {@link #isCrudOpAllowed(ECrudOp) isAllowed(<b>CREATE</b>)} =
 * <code>true</code> it is possible to create entity instance from {@link IM5Bunch} modeled values using the method
 * {@link #create(IM5Bunch)};</li>
 * <li><b>edit</b> existing instance - if allowed by {@link #isCrudOpAllowed(ECrudOp) isAllowed(<b>EDIT</b>)} =
 * <code>true</code> it is possible to change entity field values to {@link IM5Bunch} modeled values using the method
 * {@link #edit(IM5Bunch)}. Editing is supported also for immutable types like {@link File}. In such case the new
 * instance is created and returned by the method {@link #edit(IM5Bunch)};</li>
 * <li><b>remove</b> (delete) existing instance - if allowed by {@link #isCrudOpAllowed(ECrudOp)
 * isAllowed(<b>REMOVE</b>)} = <code>true</code> it is possible to remove (delete) entity instance using method
 * {@link #remove(Object)}.</li>
 * </ul>
 * The CRUD operations (create/edit/remove) have corresponding methods <code>canXxx()</code> to check if opereation will
 * be successful.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5LifecycleManager<T>
    extends IM5ModelRelated<T> {

  /**
   * Determines if specified operation is allowed by lifecycle manager.
   *
   * @param aOp {@link ECrudOp} - the requested operation
   * @return boolean - operation allowance flag
   */
  boolean isCrudOpAllowed( ECrudOp aOp );

  /**
   * Returns the master object who is responsible for lifecycle management
   *
   * @param <M> - expected mater object type
   * @return &lt;M&gt; - the master object, may be <code>null</code>
   */
  <M> M master();

  /**
   * Returns the items lister.
   *
   * @return {@link IM5ItemsProvider} - items lister
   * @throws TsUnsupportedFeatureRtException {@link #isCrudOpAllowed(ECrudOp) isAllowed(<b>LIST</b>)} ==
   *           <code>false</code>
   */
  IM5ItemsProvider<T> itemsProvider();

  /**
   * Returns field values set for new item creation.
   * <p>
   * Sometimes new item creation requires special handling like unique invariant ID. Note that creation bunch not always
   * need to the item creation. For example, returned value may be used in used dialog and user may cancel new item
   * creation.
   *
   * @return {@link IM5BunchEdit}&lt;T&gt; - values for new item creation
   */
  IM5BunchEdit<T> createNewItemValues();

  /**
   * Validates the ability to create the entity with specified field values.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - the field values of instance to be created
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreate( IM5Bunch<T> aValues );

  /**
   * Creates the new instance of the entity.
   * <p>
   * The method may throw other exceptions if creation does not succeed even if validation {@link #canCreate(IM5Bunch)}
   * returns {@link ValidationResult#SUCCESS}. For example when database connection is lost between validation and
   * actual invokation of this method.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - the field values of instance to be created
   * @return &lt;T&gt - the created entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation {@link #canCreate(IM5Bunch)} failed
   */
  T create( IM5Bunch<T> aValues );

  /**
   * Validates the ability to change entity field values.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - the new values
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canEdit( IM5Bunch<T> aValues );

  /**
   * Changes the entity field values.
   * <p>
   * The method may throw other exceptions if editing does not succeed even if validation {@link #canEdit(IM5Bunch)}
   * returns {@link ValidationResult#SUCCESS}. For example when database connection is lost between validation and
   * actual invocation of this method.
   * <p>
   * The method may return new instance and must return new instance for immutable entities.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - the new values
   * @return &lt;T&gt; - entity with changed fields, either new instance or existing one
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation {@link #canEdit(IM5Bunch)} failed
   */
  T edit( IM5Bunch<T> aValues );

  /**
   * Validates the ability to remove the entity.
   *
   * @param aEntity &lt;T&gt; - the entity to be removed
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemove( T aEntity );

  /**
   * Removes (deletes) the entity.
   *
   * @param aEntity &lt;T&gt; - the entity to be removed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation {@link #canRemove(Object)} failed
   */
  void remove( T aEntity );

}
