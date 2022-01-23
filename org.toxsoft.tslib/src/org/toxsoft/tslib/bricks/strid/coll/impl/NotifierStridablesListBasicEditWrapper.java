package org.toxsoft.tslib.bricks.strid.coll.impl;

import static org.toxsoft.tslib.bricks.strid.coll.impl.ITsResources.*;
import static org.toxsoft.tslib.bricks.validator.ValidationResult.*;

import java.io.Serializable;
import java.util.Iterator;

import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListBasicEdit;
import org.toxsoft.tslib.bricks.strid.coll.notifier.INotifierStridablesListBasicEdit;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.coll.notifier.basis.*;
import org.toxsoft.tslib.coll.notifier.impl.TsListValidatorsList;
import org.toxsoft.tslib.coll.notifier.impl.TsMapValidatorsList;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Оболочка над списком {@link IStridablesListBasicEdit} с извещением об изменениях при редактировании.
 *
 * @author hazard157
 * @param <E> - тип хранимых элементов
 */
public class NotifierStridablesListBasicEditWrapper<E extends IStridable>
    implements INotifierStridablesListBasicEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Список слушателей изменений.
   */
  private final IListEdit<ITsCollectionChangeListener> listeners = new ElemArrayList<>();

  /**
   * Валидатор изменении в ассоциативной коллекции.
   */
  private final TsMapValidatorsList<String, E> mapValidator = new TsMapValidatorsList<>();

  /**
   * Валидатор изменении в линейнео коллекции.
   */
  private final TsListValidatorsList<E> listValidator = new TsListValidatorsList<>();

  /**
   * Признак работы валидатора.
   */
  private boolean validationEnabled = true;

  /**
   * Признак изменения состава элементов после последнего вызова {@link #resetPendingEvents()}.
   */
  private boolean eventsArePending = false;

  /**
   * Признак, режима пакетных изменений.
   * <p>
   * Устанавливается в состояние <code>true</code> при вызове {@link #pauseFiring()} и сбрасывается при вызове
   * {@link #resumeFiring(boolean)}.
   */
  private boolean firingPaused = false;

  /**
   * Признак, что были изменения в период между {@link #pauseFiring()} и {@link #resumeFiring(boolean)}.
   */
  private boolean wasChangesWhilePaused = false;

  /**
   * Список, который "оборачивается" настоящим классом.
   */
  protected final IStridablesListBasicEdit<E> source;

  /**
   * Creates source wrapper instance.
   *
   * @param aSource {@link IStridablesListBasicEdit}&lt;E&gt; - the wrapped list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierStridablesListBasicEditWrapper( IStridablesListBasicEdit<E> aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  protected void fireChangedEvent( ECrudOp aOp, Object aItem ) {
    eventsArePending = true;
    if( firingPaused ) {
      wasChangesWhilePaused = true;
      return;
    }
    if( listeners.isEmpty() ) {
      return;
    }
    IList<ITsCollectionChangeListener> ll = new ElemArrayList<>( listeners );
    for( int i = 0; i < ll.size(); i++ ) {
      ITsCollectionChangeListener l = ll.get( i );
      l.onCollectionChanged( this, aOp, aItem );
    }
  }

  protected ValidationResult doCanPut( String aKey, E aExistingItem, E aNewItem ) {
    ValidationResult r1 = mapValidator.canPut( this, aKey, aExistingItem, aNewItem );
    if( r1.isError() ) {
      return r1;
    }
    ValidationResult r2;
    if( aExistingItem == null ) {
      r2 = listValidator.canAdd( this, aNewItem );
    }
    else {
      r2 = listValidator.canReplace( this, aExistingItem, aNewItem );
    }
    return lastNonOk( r1, r2 );
  }

  protected void checkPut( String aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      TsValidationFailedRtException.checkError( doCanPut( aKey, aExistingItem, aNewItem ) );
    }
  }

  protected ValidationResult doCanRemove( String aKey ) {
    ValidationResult r1 = mapValidator.canRemove( this, aKey );
    if( r1.isError() ) {
      return r1;
    }
    ValidationResult r2 = ValidationResult.SUCCESS;
    E oldItem = findByKey( aKey );
    if( oldItem != null ) {
      r2 = listValidator.canRemove( this, oldItem );
    }
    return lastNonOk( r1, r2 );
  }

  protected void checkRemove( String aKey ) {
    if( aKey == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      TsValidationFailedRtException.checkError( doCanRemove( aKey ) );
    }
  }

  protected ValidationResult doCanAdd( String aKey, E aOleItem, E aNewItem ) {
    ValidationResult r1 = mapValidator.canAdd( this, aKey, aOleItem, aNewItem );
    if( r1.isError() ) {
      return r1;
    }
    ValidationResult r2 = listValidator.canAdd( this, aNewItem );
    return lastNonOk( r1, r2 );
  }

  protected void checkAdd( String aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    if( validationEnabled ) {
      TsValidationFailedRtException.checkError( doCanAdd( aKey, aExistingItem, aNewItem ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return source.hasElem( null );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( E aElem ) {
    return source.indexOf( aElem );
  }

  @Override
  public E get( int aIndex ) {
    return source.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    return source.hasKey( aKey );
  }

  @Override
  public E findByKey( String aKey ) {
    return source.findByKey( aKey );
  }

  @Override
  public IStringList keys() {
    return source.keys();
  }

  @Override
  public IList<E> values() {
    return source.values();
  }

  // ------------------------------------------------------------------------------------
  // IStridablesList
  //

  @Override
  public IStringList ids() {
    return source.ids();
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public int add( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkAdd( aElem.id(), source.findByKey( aElem.id() ), aElem );
    int oldSize = source.size();
    int index = source.add( aElem );
    int newSize = source.size();
    if( oldSize != newSize ) {
      fireChangedEvent( ECrudOp.CREATE, aElem.id() );
    }
    else {
      fireChangedEvent( ECrudOp.EDIT, aElem.id() );
    }
    return index;
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkRemove( aElem.id() );
    int index = source.remove( aElem );
    if( index >= 0 ) {
      fireChangedEvent( ECrudOp.REMOVE, aElem.id() );
    }
    return index;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    checkRemove( source.keys().get( aIndex ) );
    E e = source.removeByIndex( aIndex );
    if( e != null ) {
      fireChangedEvent( ECrudOp.REMOVE, e.id() );
    }
    return e;
  }

  @Override
  public void clear() {
    if( !isEmpty() ) {
      for( int i = 0, n = source.size(); i < n; i++ ) {
        checkRemove( source.keys().get( i ) );
      }
      source.clear();
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStringMapEdit
  //

  @Override
  public E put( String aKey, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    E oldElem = source.findByKey( aElem.id() );
    checkPut( aElem.id(), oldElem, aElem );
    int oldSize = source.size();
    source.put( aElem );
    int newSize = source.size();
    if( newSize != oldSize ) {
      fireChangedEvent( ECrudOp.CREATE, aElem.id() );
    }
    else {
      fireChangedEvent( ECrudOp.EDIT, aElem.id() );
    }
    return oldElem;
  }

  @Override
  public E removeByKey( String aKey ) {
    checkRemove( aKey );
    E e = source.findByKey( aKey );
    if( e != null ) {
      source.removeByKey( aKey );
      fireChangedEvent( ECrudOp.REMOVE, e.id() );
    }
    return e;
  }

  // ------------------------------------------------------------------------------------
  // IStridablesListBasicEdit
  //

  @Override
  public int put( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkPut( aElem.id(), source.findByKey( aElem.id() ), aElem );
    int oldSize = source.size();
    int index = source.put( aElem );
    int newSize = source.size();
    if( newSize != oldSize ) {
      fireChangedEvent( ECrudOp.CREATE, aElem.id() );
    }
    else {
      fireChangedEvent( ECrudOp.EDIT, aElem.id() );
    }
    return index;
  }

  @Override
  public E replace( String aId, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    E oldItem = source.findByKey( aId );
    checkPut( aId, oldItem, aElem );
    source.replace( aId, aElem );
    if( oldItem == null ) {
      fireChangedEvent( ECrudOp.CREATE, aElem.id() );
    }
    else {
      if( aId.equals( aElem.id() ) ) {
        fireChangedEvent( ECrudOp.EDIT, aElem.id() );
      }
      else {
        fireChangedEvent( ECrudOp.REMOVE, aId );
        fireChangedEvent( ECrudOp.CREATE, aElem.id() );
      }
    }
    return oldItem;
  }

  @Override
  public E removeById( String aId ) {
    checkRemove( aId );
    E e = source.findByKey( aId );
    if( e != null ) {
      source.removeById( aId );
      fireChangedEvent( ECrudOp.REMOVE, e.id() );
    }
    return e;
  }

  // ------------------------------------------------------------------------------------
  // ITsNotifierCollection
  //

  @Override
  public void addCollectionChangeListener( ITsCollectionChangeListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeCollectionChangeListener( ITsCollectionChangeListener aListener ) {
    listeners.remove( aListener );
  }

  @Override
  public void pauseFiring() {
    firingPaused = true;
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    if( !firingPaused ) {
      return;
    }
    firingPaused = false;
    if( wasChangesWhilePaused ) {
      if( aFireDelayed ) {
        fireChangedEvent( ECrudOp.LIST, null );
      }
      wasChangesWhilePaused = false;
    }
  }

  @Override
  public boolean isFiringPaused() {
    return firingPaused;
  }

  @Override
  public boolean isPendingEvents() {
    return eventsArePending;
  }

  @Override
  public void resetPendingEvents() {
    eventsArePending = false;
  }

  @Override
  public void fireItemByIndexChangeEvent( int aIndex ) {
    fireChangedEvent( ECrudOp.EDIT, source.keys().get( aIndex ) );
  }

  @Override
  public void fireItemByRefChangeEvent( Object aItem ) {
    TsItemNotFoundRtException.checkNull( aItem );
    for( int i = source.size() - 1; i >= 0; i-- ) {
      E e = source.values().get( i );
      if( e == aItem ) {
        fireChangedEvent( ECrudOp.EDIT, e.id() );
        return;
      }
    }
  }

  @Override
  public void fireBatchChangeEvent() {
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @Override
  public boolean isValidationEnabled() {
    return validationEnabled;
  }

  @Override
  public void setValidationEnabled( boolean aEnabled ) {
    validationEnabled = aEnabled;
  }

  // ------------------------------------------------------------------------------------
  // INotifierList
  //

  @Override
  public ValidationResult canAdd( E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    return canAdd( aNewItem.id(), aNewItem );
  }

  @Override
  public ValidationResult canRemove( int aIndex ) {
    String key = keys().get( aIndex );
    return doCanRemove( key );
  }

  /**
   * Логику работы приближаем к "естественному" пониманию.<br>
   * <ul>
   * <li>Если на месте aIndex находится элемент с ключом aNewItem.id(), то все естественно и понятно, соответствует
   * doCanPut( aNewItem.id(), pointedItem, aNewItem );</li>
   * <li>Если существующий элемент имеет другой ключ, и в списке нет элемента с ключом aNewItem.id(), то эта ситуация
   * также понятна, и соответствует doCanPut( aNewItem.id(), null, aNewItem );</li>
   * <li>Проблема, если не на месте aIndex существует элемент с идентификатором aNewItem.id(). Какой из этих двух
   * элементов (в позиции aIndex или с ключом aNewItem.id()) следует заменить? Считаем, что пользователь хочет заменить
   * pointedItem, но существует элемент с таким идентификатором. Это не соответствует ни одному методу валидаторов
   * IXxxCollectionChangeValidator, поэтому ошибку веренм тут же.</li>
   * </ul>
   */
  @Override
  public ValidationResult canReplace( int aIndex, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    String key = aNewItem.id();
    E pointedItem = get( aIndex );
    boolean isKeyAtIndex = pointedItem.id().equals( key );
    if( isKeyAtIndex ) {
      return doCanPut( key, pointedItem, aNewItem );
    }
    E foundItem = findByKey( key );
    if( foundItem == null ) {
      return doCanPut( key, null, aNewItem );
    }
    return ValidationResult.error( FMT_ERR_CANT_REPLACE_BY_ID, Integer.valueOf( aIndex ), pointedItem.id(), key );
  }

  @Override
  public ITsListValidator<E> getListValidator() {
    return listValidator;
  }

  @Override
  public void addCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    listValidator.addCollectionChangeValidator( aValidator );
  }

  @Override
  public void removeCollectionChangeValidator( ITsListValidator<E> aValidator ) {
    listValidator.removeCollectionChangeValidator( aValidator );
  }

  // ------------------------------------------------------------------------------------
  // INotifierMap
  //

  @Override
  public ValidationResult canPut( String aKey, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    E oldItem = findByKey( aKey );
    return doCanPut( aKey, oldItem, aNewItem );
  }

  @Override
  public ValidationResult canRemove( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return doCanRemove( aKey );
  }

  @Override
  public ValidationResult canAdd( String aKey, E aNewItem ) {
    TsNullArgumentRtException.checkNull( aNewItem );
    E oldItem = findByKey( aKey );
    return doCanAdd( aKey, oldItem, aNewItem );
  }

  @Override
  public void fireItemByKeyChangeEvent( String aKey ) {
    if( source.keys().hasElem( aKey ) ) {
      fireChangedEvent( ECrudOp.EDIT, aKey );
    }
  }

  @Override
  public ITsMapValidator<String, E> getMapValidator() {
    return mapValidator;
  }

  @Override
  public void addCollectionChangeValidator( ITsMapValidator<String, E> aValidator ) {
    mapValidator.addCollectionChangeValidator( aValidator );
  }

  @Override
  public void removeCollectionChangeValidator( ITsMapValidator<String, E> aValidator ) {
    mapValidator.removeCollectionChangeValidator( aValidator );
  }

}
