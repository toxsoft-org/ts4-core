package org.toxsoft.unit.txtproj.core.sinent;

import static org.toxsoft.unit.txtproj.core.sinent.ITsResources.*;

import org.toxsoft.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strid.coll.impl.NotifierStridablesListBasicEditWrapper;
import org.toxsoft.tslib.bricks.strid.coll.impl.SortedStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.notifier.INotifierStridablesListBasicEdit;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.unit.txtproj.core.impl.AbstractProjDataUnit;

/**
 * Абстрактная базовая реализация {@link ISinentManager}.
 *
 * @author hazard157
 * @param <E> - тип (класс) сущности
 * @param <F> - тип (класс) информации о сущности
 */
public abstract class AbstractSinentManager<E extends ISinentity<F>, F>
    extends AbstractProjDataUnit
    implements ISinentManager<E, F> {

  /**
   * Слушатель изменений в дочерных элементах.
   * <p>
   * Предназначена для использования наследниками.
   * <p>
   * Просто делегирует вызов {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   */
  protected final IGenericChangeListener genericChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      items.fireItemByRefChangeEvent( aSource );
      helper().fireChangeEvent();
    }
  };

  private final ITsCollectionChangeListener itemsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      helper().fireChangeEvent();
    }
  };

  private final String                      keyword;
  private IEntityKeeper<E>                  sinentKeeper = null;
  final INotifierStridablesListBasicEdit<E> items        =
      new NotifierStridablesListBasicEditWrapper<>( new SortedStridablesList<E>() );

  /**
   * Конструктор со всеми инвариантами.
   * <p>
   * Если хранитель равен <code>null</code>, то в конструкторе наследника должен быть вызван
   * {@link #setSinentKeeper(IEntityKeeper)}.
   *
   * @param aKeyword String - ключевое слово (ИД-путь или пустая строка) для записи коллекции методом
   *          {@link StrioUtils#writeCollection(IStrioWriter, String, ITsCollection, IEntityKeeper)}
   * @param aKeeper {@link IEntityKeeper}&lt;E&gt; - хранитель сущностей, может быть <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   */
  public AbstractSinentManager( String aKeyword, IEntityKeeper<E> aKeeper ) {
    super();
    TsNullArgumentRtException.checkNull( aKeyword );
    if( !aKeyword.isEmpty() ) {
      StridUtils.checkValidIdPath( aKeyword );
    }
    keyword = aKeyword;
    sinentKeeper = aKeeper;
    items.addCollectionChangeListener( itemsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISinentManager
  //

  @Override
  public INotifierStridablesListBasicEdit<E> items() {
    return items;
  }

  @Override
  public E createItem( String aId, F aInfo ) {
    TsValidationFailedRtException.checkError( canCreateItem( aId, aInfo ) );
    E elem = doCreateItem( aId, aInfo );
    items.add( elem );
    elem.eventer().addListener( genericChangeListener );
    return elem;
  }

  @Override
  public ValidationResult canCreateItem( String aId, F aInfo ) {
    TsNullArgumentRtException.checkNulls( aId, aInfo );
    if( !StridUtils.isValidIdPath( aId ) ) {
      return ValidationResult.error( FMT_ERR_ID_NOT_AN_ID_PATH, keyword, aId );
    }
    if( items.hasKey( aId ) ) {
      return ValidationResult.error( FMT_ERR_ID_ALREADY_EXISTS, keyword, aId );
    }
    return doCanCreateItem( aId, aInfo );
  }

  @Override
  public E editItem( String aOldId, String aId, F aInfo ) {
    TsValidationFailedRtException.checkError( canEditItem( aOldId, aId, aInfo ) );
    E e = items.getByKey( aOldId );
    if( aId.equals( aOldId ) ) {
      e.setInfo( aInfo );
      return e;
    }
    e = doCreateItem( aId, aInfo );
    e.eventer().addListener( genericChangeListener );
    items.replace( aOldId, e );
    return e;
  }

  @Override
  public ValidationResult canEditItem( String aOldId, String aId, F aInfo ) {
    TsNullArgumentRtException.checkNulls( aOldId, aId, aInfo );
    if( !StridUtils.isValidIdPath( aId ) ) {
      return ValidationResult.error( FMT_ERR_ID_NOT_AN_ID_PATH, keyword, aId );
    }
    if( !items.hasKey( aOldId ) ) {
      return ValidationResult.error( FMT_ERR_ID_NOT_EXISTS, keyword, aOldId );
    }
    // если меняется идентификатор, надо проверить уникальность нового
    if( !aId.equals( aOldId ) ) {
      if( items.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_ID_ALREADY_EXISTS, keyword, aId );
      }
    }
    return doCanEditItem( aOldId, aId, aInfo );
  }

  @Override
  public void removeItem( String aId ) {
    TsValidationFailedRtException.checkError( canRemoveItem( aId ) );
    E elem = items.removeById( aId );
    if( elem != null ) {
      elem.eventer().removeListener( genericChangeListener );
    }
  }

  @Override
  public ValidationResult canRemoveItem( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !items.hasKey( aId ) ) {
      vr = ValidationResult.warn( FMT_WARN_ID_NOT_EXISTS, keyword, aId );
    }
    return ValidationResult.lastNonOk( vr, doCanRemoveItem( aId ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractProjDataUnit
  //

  @Override
  protected void doWrite( IStrioWriter aSw ) {
    TsIllegalStateRtException.checkNull( sinentKeeper );
    StrioUtils.writeCollection( aSw, keyword, items, sinentKeeper, true );
  }

  @Override
  protected void doRead( IStrioReader aSr ) {
    TsIllegalStateRtException.checkNull( sinentKeeper );
    genericChangeEventer.pauseFiring();
    try {
      items.clear();
      items.addAll( StrioUtils.readCollection( aSr, keyword, sinentKeeper ) );
      for( E e : items ) {
        e.eventer().addListener( genericChangeListener );
      }
    }
    finally {
      genericChangeEventer.resumeFiring( true );
    }
  }

  @Override
  protected void doClear() {
    items.clear();
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Возвращает внутренный помощник работы с извещениями.
   *
   * @return {@link GenericChangeEventer} - внутренный помощник работы с извещениями
   */
  public GenericChangeEventer helper() {
    return genericChangeEventer;
  }

  /**
   * Задает хранитель элементов.
   *
   * @param aSinentKeeper {@link IEntityKeeper}&lt;E&gt; - хранитель элементов
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void setSinentKeeper( IEntityKeeper<E> aSinentKeeper ) {
    TsNullArgumentRtException.checkNull( aSinentKeeper );
    sinentKeeper = aSinentKeeper;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  abstract protected E doCreateItem( String aId, F aInfo );

  /**
   * Наследник может расширить правила проверки {@link #canCreateItem(String, Object)}.
   * <p>
   * В базовом классе просто возвращает {@link ValidationResult#SUCCESS}, при переопределении вызывать метод бзового
   * класса не нужно.
   *
   * @param aId String - идентификатор (гарантированно уникальный ИД-путь) элемента
   * @param aInfo F - описание элемента, не бывает null
   * @return {@link ValidationResult} - результат проверки
   */
  protected ValidationResult doCanCreateItem( String aId, F aInfo ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Наследник может расширить правила проверки {@link #canEditItem(String, String, Object)}.
   * <p>
   * В базовом классе просто возвращает {@link ValidationResult#SUCCESS}, при переопределении вызывать метод бзового
   * класса не нужно.
   *
   * @param aOldId String - идентификатор проверено, что существующего элемента
   * @param aId String - новый идентификатор (принесовпадении со старым, гарантированно уникальный ИД-путь) элемента
   * @param aInfo F - описание элемента, не бывает null
   * @return {@link ValidationResult} - результат проверки
   */
  protected ValidationResult doCanEditItem( String aOldId, String aId, F aInfo ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Наследник может расширить правила проверки {@link #canRemoveItem(String)}.
   * <p>
   * В базовом классе просто возвращает {@link ValidationResult#SUCCESS}, при переопределении вызывать метод бзового
   * класса не нужно.
   *
   * @param aId String - идентификатор удаляемого элемента, не бывает null
   * @return {@link ValidationResult} - результат проверки
   */
  protected ValidationResult doCanRemoveItem( String aId ) {
    return ValidationResult.SUCCESS;
  }

}
