package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IM5LifecycleManager} base implementation.
 * <p>
 * Notes on implementation:
 * <ul>
 * <li>CRUD opertions implemenating methods <code>doXxx()</code> are called only when corresponding operation is
 * enabled. That is {@link #isCrudOpAllowed(ECrudOp)} returns <code>true</code>;</li>
 * <li>default (built-in) items provider calls {@link #doListEntities()} and {@link #doGetItemsReorderer()} for items
 * listing and reordering. If user specifies non-<code>null</code> argument for
 * {@link #setItemsProvider(IM5ItemsProvider)}, these methods will not be called.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - modelled entity type
 * @param <M> - master object type
 */
public class M5LifecycleManager<T, M>
    implements IM5LifecycleManager<T>, ITsContextable {

  /**
   * Default items provider.
   *
   * @author hazard157
   */
  protected class InternalItemsProvider
      implements IM5ItemsProvider<T> {

    @Override
    public IGenericChangeEventer genericChangeEventer() {
      return NoneGenericChangeEventer.INSTANCE;
    }

    @Override
    public IList<T> listItems() {
      return doListEntities();
    }

    @Override
    public IListReorderer<T> reorderer() {
      return doGetItemsReorderer();
    }

  }

  private final IM5Model<T> model;
  private final boolean     isCreationSupported;
  private final boolean     isEditingSupported;
  private final boolean     isRemoveSupported;
  private final boolean     isListingSupported;
  private final M           master;

  private IM5ItemsProvider<T> itemsProvider;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aCanCreate boolean - entity creation support flags
   * @param aCanEdit boolean - entity editing support flags
   * @param aCanRemove boolean - entity removingsupport flags
   * @param aEnumeratable boolean - entity listing support flags
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public M5LifecycleManager( IM5Model<T> aModel, boolean aCanCreate, boolean aCanEdit, boolean aCanRemove,
      boolean aEnumeratable, M aMaster ) {
    TsNullArgumentRtException.checkNull( aModel );
    model = aModel;
    isCreationSupported = aCanCreate;
    isEditingSupported = aCanEdit;
    isRemoveSupported = aCanRemove;
    isListingSupported = aEnumeratable;
    master = aMaster;
    itemsProvider = new InternalItemsProvider();
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsContext tsContext() {
    return model.domain().tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IM5LifecycleManager
  //

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  public boolean isCrudOpAllowed( ECrudOp aOp ) {
    return switch( aOp ) {
      case CREATE -> isCreationSupported;
      case EDIT -> isEditingSupported;
      case REMOVE -> isRemoveSupported;
      case LIST -> isListingSupported;
    };
  }

  @SuppressWarnings( "unchecked" )
  @Override
  final public M master() {
    return master;
  }

  @Override
  public IM5ItemsProvider<T> itemsProvider() {
    if( !isListingSupported ) {
      throw new TsUnsupportedFeatureRtException( FMT_ERR_MODEL_CANT_LIST_OBJS, model.id() );
    }
    return itemsProvider;
  }

  @Override
  public ValidationResult canCreate( IM5Bunch<T> aValues ) {
    TsNullArgumentRtException.checkNull( aValues );
    if( !isCreationSupported ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_CREATE_OBJS, model.id() );
    }
    if( !aValues.model().equals( model ) ) {
      return ValidationResult.error( FMT_ERR_LC_VALUES_NOT_OF_MODEL, model.id(), aValues.model().id() );
    }
    try {
      return doBeforeCreate( aValues );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  @Override
  public T create( IM5Bunch<T> aValues ) {
    TsValidationFailedRtException.checkError( canCreate( aValues ) );
    return doCreate( aValues );
  }

  @Override
  public ValidationResult canEdit( IM5Bunch<T> aValues ) {
    TsNullArgumentRtException.checkNull( aValues );
    if( !isEditingSupported ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_EDIT_OBJS, model.id() );
    }
    if( !aValues.model().equals( model ) ) {
      return ValidationResult.error( FMT_ERR_LC_VALUES_NOT_OF_MODEL, model.id() );
    }
    return doBeforeEdit( aValues );
  }

  @Override
  public T edit( IM5Bunch<T> aValues ) {
    try {
      TsValidationFailedRtException.checkError( canEdit( aValues ) );
      return doEdit( aValues );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  @Override
  public ValidationResult canRemove( T aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    if( !isRemoveSupported ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_REMOVE_OBJS, model.id() );
    }
    if( !model.isModelledObject( aEntity ) ) {
      return ValidationResult.error( FMT_ERR_LC_OBJECT_NOT_OF_MODEL, model.id() );
    }
    return doBeforeRemove( aEntity );
  }

  @Override
  public void remove( T aEntity ) {
    TsValidationFailedRtException.checkError( canRemove( aEntity ) );
    try {
      doRemove( aEntity );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw ex;
    }
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Sets the {@link #itemsProvider()}.
   *
   * @param aItemsProvider {@link IM5ItemsProvider}&lt;T&gt; - items provider or <code>null</code> for default provider
   */
  public void setItemsProvider( IM5ItemsProvider<T> aItemsProvider ) {
    if( aItemsProvider != null ) {
      itemsProvider = aItemsProvider;
    }
    else {
      itemsProvider = new InternalItemsProvider();
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform validation before instance creation.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doBeforeCreate( IM5Bunch<T> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If creation is supported subclass must create the entity instance.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return &lt;T&gt; - created instance
   */
  protected T doCreate( IM5Bunch<T> aValues ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_CREATION_CODE, model.id() );
  }

  /**
   * Subclass may perform validation before existing editing.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doBeforeEdit( IM5Bunch<T> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If editing is supported subclass must edit the existing entity.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   * <p>
   * The old values may be found in the {@link IM5Bunch#originalEntity()} which obviously is not <code>null</code>.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return &lt;T&gt; - created instance
   */
  protected T doEdit( IM5Bunch<T> aValues ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_EDIT_CODE, model.id() );
  }

  /**
   * Subclass may perform validation before remove existing entity.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity to be removed, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doBeforeRemove( T aEntity ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If removing is supported subclass must remove the existing entity.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aEntity &lt;T&gt; - the entity to be removed, never is <code>null</code>
   */
  protected void doRemove( T aEntity ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_REMOVE_CODE, model.id() );
  }

  /**
   * If enumeration is supported subclass must return list of entities.
   * <p>
   * In base class returns {@link IList#EMPTY}, there is no need to call superclass method when overriding.
   *
   * @return {@link IList}&lt;T&gt; - list of entities in the scope of maetr object
   */
  protected IList<T> doListEntities() {
    return IList.EMPTY;
  }

  /**
   * If enumeration is supported subclass may allow items reordering.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   * <p>
   * This method is called every time when user asks for {@link IM5ItemsProvider#reorderer()}.
   *
   * @return {@link IListReorderer}&lt;T&gt; - optional {@link IM5ItemsProvider#listItems()} reordering means
   */
  protected IListReorderer<T> doGetItemsReorderer() {
    return null;
  }

}
