package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tsgui.m5_1.impl.ITsResources.*;

import org.eclipse.swt.widgets.Shell;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.helpers.IListReorderer;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Базовая реалиазция {@link IM5LifecycleManager}.
 *
 * @author goga
 * @param <T> - класс моделированной сущности
 * @param <M> - класс мастер-объекта
 */
public class M5LifecycleManager<T, M>
    implements IM5LifecycleManager<T> {

  protected class InternalItemsProvider
      implements IM5ItemsProvider<T> {

    private GenericChangeEventer eventer = null;

    InternalItemsProvider() {
      // nop
    }

    public void fireChangeEvent() {
      genericChangeEventer().fireChangeEvent();
    }

    @Override
    public GenericChangeEventer genericChangeEventer() {
      if( eventer == null ) {
        eventer = new GenericChangeEventer( this );
      }
      return eventer;
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

  private final InternalItemsProvider itemsProvider;

  private final IM5Model<T> model;
  private final boolean     canCreate;
  private final boolean     canEdit;
  private final boolean     canRemove;
  private final boolean     enumeratable;
  private M                 master;

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель
   * @param aCanCreate boolean - признак, что сущности можно создавать
   * @param aCanEdit boolean - признак, что сущности можно редактировать
   * @param aCanRemove boolean - признак, что сущности можно удалять
   * @param aEnumeratable boolean - признак, что сущности можне перечислть в {@link #itemsProvider}
   * @param aMaster &lt;M&gt; - мастер объект, может быть null
   * @throws TsNullArgumentRtException aModel = null
   */
  public M5LifecycleManager( IM5Model<T> aModel, boolean aCanCreate, boolean aCanEdit, boolean aCanRemove,
      boolean aEnumeratable, M aMaster ) {
    TsNullArgumentRtException.checkNull( aModel );
    itemsProvider = new InternalItemsProvider();
    model = aModel;
    canCreate = aCanCreate;
    canEdit = aCanEdit;
    canRemove = aCanRemove;
    enumeratable = aEnumeratable;
    master = aMaster;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return model.domain().tsContext();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5LifecycleManager
  //

  @Override
  final public boolean isCreationAllowed() {
    return canCreate;
  }

  @Override
  final public boolean isEditingAllowed() {
    return canEdit;
  }

  @Override
  final public boolean isRemovalAllowed() {
    return canRemove;
  }

  @Override
  final public boolean isItemsProvided() {
    return enumeratable;
  }

  @Override
  public IM5Model<T> model() {
    return model;
  }

  @Override
  public InternalItemsProvider itemsProvider() {
    if( !enumeratable ) {
      throw new TsUnsupportedFeatureRtException();
    }
    return itemsProvider;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public M master() {
    return master;
  }

  @Override
  final public ValidationResult canCreate( IM5Bunch<T> aValues ) {
    TsNullArgumentRtException.checkNull( aValues );
    if( !canCreate ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_CREATE_OBJS, model.id() );
    }
    if( !aValues.model().equals( model ) ) {
      return ValidationResult.error( FMT_ERR_LC_VALUES_NOT_OF_MODEL, model.id() );
    }
    try {
      return doBeforeCreate( aValues );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( tsContext().get( Shell.class ), ex );
      throw ex;
    }
  }

  @Override
  final public T create( IM5Bunch<T> aValues ) {
    TsValidationFailedRtException.checkError( canCreate( aValues ) );
    return doCreate( aValues );
  }

  @Override
  final public ValidationResult canEdit( IM5Bunch<T> aValues ) {
    TsNullArgumentRtException.checkNull( aValues );
    if( !canEdit ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_EDIT_OBJS, model.id() );
    }
    if( !aValues.model().equals( model ) ) {
      return ValidationResult.error( FMT_ERR_LC_VALUES_NOT_OF_MODEL, model.id() );
    }
    return doBeforeEdit( aValues );
  }

  @Override
  final public T edit( IM5Bunch<T> aValues ) {
    try {
      TsValidationFailedRtException.checkError( canEdit( aValues ) );
      return doEdit( aValues );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( tsContext().get( Shell.class ), ex );
      throw ex;
    }
  }

  @Override
  final public ValidationResult canRemove( T aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    if( !canRemove ) {
      return ValidationResult.error( FMT_ERR_MODEL_CANT_REMOVE_OBJS, model.id() );
    }
    if( !model.isModelledObject( aEntity ) ) {
      return ValidationResult.error( FMT_ERR_LC_OBJECT_NOT_OF_MODEL, model.id() );
    }
    return doBeforeRemove( aEntity );
  }

  @Override
  final public void remove( T aEntity ) {
    TsValidationFailedRtException.checkError( canRemove( aEntity ) );
    try {
      doRemove( aEntity );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( tsContext().get( Shell.class ), ex );
      throw ex;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает мастер-объект.
   * <p>
   * Этот метод может понадобиться только в конструкторе наследника, для удобства написания кода.
   *
   * @param aMaster &lt;M&gt; - мастер объект, может быть null
   */
  public final void setMaster( M aMaster ) {
    if( master != aMaster ) {
      Object old_master = master;
      master = aMaster;
      doMasterObjectChanged( master, old_master );
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может реализовать свою проверку и подготовку к созданию экземпляра сущности.
   * <p>
   * В базовом классе возвращает {@link ValidationResult#SUCCESS}. при переопределении вызывать родительский метод не
   * нужно.
   *
   * @param aValues {@link IM5Bunch} - значения полей создаваемого объекта, не бывает null
   * @return {@link ValidationResult} - результат проверки любых предусловий
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected ValidationResult doBeforeCreate( IM5Bunch<T> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Наследник может осуществить реальное создание объекта.
   * <p>
   * В базовом классе выбрасывает исключение {@link TsInternalErrorRtException}. При переопределении нельзя вызывать
   * метод базового класса.
   * <p>
   * Внимание: этот метод объязательно должен быть переопределен для контроллера, поддерживающего создание объектов (то
   * есть, {@link #isCreationAllowed()} = <code>true</code>).
   *
   * @param aValues {@link IM5Bunch} - значения полей создаваемого объекта, не бывает null
   * @return &lt;T&gt; - созданный объект
   */
  protected T doCreate( IM5Bunch<T> aValues ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_CREATION_CODE, model.id() );
  }

  /**
   * Наследник может реализовать свою проверку и подготовку к редактированию экземпляра сущности.
   * <p>
   * В базовом классе возвращает {@link ValidationResult#SUCCESS}. при переопределении вызывать родительский метод не
   * нужно.
   *
   * @param aValues {@link IM5Bunch} - новые значения полей редактируемого объекта, не бывает null
   * @return {@link ValidationResult} - результат проверки любых предусловий
   */
  protected ValidationResult doBeforeEdit( IM5Bunch<T> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Наследник может осуществить реальное редактирование объекта.
   * <p>
   * В базовом классе выбрасывает исключение {@link TsInternalErrorRtException}. При переопределении нельзя вызывать
   * метод базового класса.
   * <p>
   * Внимание: этот метод объязательно должен быть переопределен для контроллера, поддерживающего редактирование
   * объектов (то есть, {@link #isEditingAllowed()} = <code>true</code>).
   *
   * @param aValues {@link IM5Bunch} - новые значения полей редактируемого объекта, не бывает null
   * @return &lt;T&gt; - созданный объект
   */
  protected T doEdit( IM5Bunch<T> aValues ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_EDIT_CODE, model.id() );
  }

  /**
   * Наследник может реализовать свою проверку и подготовку к удалению экземпляра сущности.
   * <p>
   * В базовом классе возвращает {@link ValidationResult#SUCCESS}. при переопределении вызывать родительский метод не
   * нужно.
   *
   * @param aEntity &lt;T&gt; - удаляемая сущность
   * @return {@link ValidationResult} - результат проверки любых предусловий
   */
  protected ValidationResult doBeforeRemove( T aEntity ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Наследник может осуществить реальное удаление объекта.
   * <p>
   * В базовом классе выбрасывает исключение {@link TsInternalErrorRtException}. При переопределении нельзя вызывать
   * метод базового класса.
   * <p>
   * Внимание: этот метод объязательно должен быть переопределен для контроллера, поддерживающего удаление объектов (то
   * есть, {@link #isRemovalAllowed()} = <code>true</code>).
   *
   * @param aEntity &lt;T&gt; - удаляемая сущность
   */
  protected void doRemove( T aEntity ) {
    throw new TsInternalErrorRtException( FMT_ERR_NO_REMOVE_CODE, model.id() );
  }

  /**
   * Наследник может вернуть список всех сущностей от указанного мастера.
   * <p>
   * В базовом классе возвращает {@link IList#EMPTY}. При переопределении родительский метод вызывать не нужно.
   * <p>
   * Метод вызывается только для перечисляемых сушностей (то есть, {@link #isItemsProvided()} = <code>true</code>) из
   * метода {@link #itemsProvider}.
   *
   * @return {@link IList}&lt;T&gt; - список всех экземпляров сущности
   */
  protected IList<T> doListEntities() {
    return IList.EMPTY;
  }

  /**
   * Наследник может обработать изменения мастер-объекта после создания менеджера.
   * <p>
   * Вызывается при смене мастер-объекта из метода {@link #setMaster(Object)}.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aNewMaster {@link Object} - новый мастер-объект, может быть <code>null</code>
   * @param aOldMaster {@link Object} - старый мастер-объект, может быть <code>null</code>
   */
  protected void doMasterObjectChanged( Object aNewMaster, Object aOldMaster ) {
    // nop
  }

  /**
   * Возвращает упорядочиватель списка {@link IM5ItemsProvider#listItems()}.
   * <p>
   * По умолчанию поставщик {@link #itemsProvider()} возвращает значение, возвращаемое этим метоодом.
   * <p>
   * В базовом классе просто возвращает <code>null</code>, упорядочиватель списка {@link IM5ItemsProvider}.
   *
   * @return {@link IListReorderer} - упорядочиватель списка {@link IM5ItemsProvider}
   */
  protected IListReorderer<T> doGetItemsReorderer() {
    return null;
  }

}
