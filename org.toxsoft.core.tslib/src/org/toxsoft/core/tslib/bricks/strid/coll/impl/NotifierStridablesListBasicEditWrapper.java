package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import static org.toxsoft.core.tslib.bricks.strid.coll.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.validator.ValidationResult.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Wraps {@link IStridablesListBasicEdit} with notification and validation added.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class NotifierStridablesListBasicEditWrapper<E extends IStridable>
    implements INotifierStridablesListBasicEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * The listeners list.
   */
  private final IListEdit<ITsCollectionChangeListener> listeners = new ElemArrayList<>();

  /**
   * The map validation support.
   */
  private final TsMapValidatorsList<String, E> mapValidator = new TsMapValidatorsList<>();

  /**
   * The list validation support.
   */
  private final TsListValidatorsList<E> listValidator = new TsListValidatorsList<>();

  /**
   * The flag that validation is enabled.
   */
  private boolean validationEnabled = true;

  /**
   * Flags that elements list has been changed since last call to {@link #resetPendingEvents()}.
   */
  private boolean eventsArePending = false;

  /**
   * Counts {@link #pauseFiring()} calls.
   */
  private int pauseCounter = 0;

  /**
   * Flags that there were changes between calls to the methods {@link #pauseFiring()} and
   * {@link #resumeFiring(boolean)}.
   */
  private boolean wasChangesWhilePaused = false;

  /**
   * The wrapped list.
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
  // implementation
  //

  protected void fireChangedEvent( ECrudOp aOp, Object aItem ) {
    eventsArePending = true;
    if( isFiringPaused() ) {
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
  // ITsCollectionEdit
  //

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aCount < 0 );
    TsIllegalArgumentRtException.checkFalse( aIndex >= size() );
    if( aCount == 0 ) {
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aIndex + aCount >= size() );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      for( int i = 0; i < aCount; i++ ) {
        removeByIndex( aIndex );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void addAll( @SuppressWarnings( "unchecked" ) E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      for( int i = 0; i < aArray.length; i++ ) {
        add( aArray[i] );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void addAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      if( aColl instanceof ITsFastIndexListTag ) {
        @SuppressWarnings( { "rawtypes", "unchecked" } )
        ITsFastIndexListTag<E> ll = (ITsFastIndexListTag)aColl;
        for( int i = 0, count = ll.size(); i < count; i++ ) {
          add( ll.get( i ) );
        }
      }
      else {
        for( E e : aColl ) {
          add( e );
        }
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void addAll( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) { // for atomic error recovery check for nulls before really make changes in collection
      TsNullArgumentRtException.checkNull( e );
    }
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      for( E e : aColl ) {
        add( e );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void setAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      clear();
      addAll( aColl );
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void setAll( Collection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( E e : aColl ) { // for atomic error recovery check for nulls before really make changes in collection
      TsNullArgumentRtException.checkNull( e );
    }
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      clear();
      for( E e : aColl ) {
        add( e );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public void setAll( E... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      clear();
      for( int i = 0; i < aArray.length; i++ ) {
        add( aArray[i] );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public void putAll( IMap<String, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    IList<String> srcIds = aSrc.keys();
    IList<? extends E> srcValues = aSrc.values();
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      for( int i = 0, n = srcIds.size(); i < n; i++ ) {
        put( srcIds.get( i ), srcValues.get( i ) );
      }
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
    }
  }

  @Override
  public void setAll( IMap<String, ? extends E> aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    boolean alreadyPaused = isFiringPaused();
    if( !alreadyPaused ) {
      pauseFiring();
    }
    try {
      clear();
      putAll( aSrc );
    }
    finally {
      if( !alreadyPaused ) {
        resumeFiring( true );
      }
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

  @Override
  public void putAll( IStringMap<? extends E> aSrc ) {
    putAll( (IMap<String, ? extends E>)aSrc );
  }

  @Override
  public void setAll( IStringMap<? extends E> aSrc ) {
    setAll( (IMap<String, ? extends E>)aSrc );
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
    TsInternalErrorRtException.checkTrue( pauseCounter == Integer.MAX_VALUE );
    ++pauseCounter;
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    if( pauseCounter == 0 ) { // already fired or not even paused yet
      return;
    }
    --pauseCounter;
    if( pauseCounter == 0 ) {
      if( wasChangesWhilePaused ) {
        if( aFireDelayed ) {
          fireChangedEvent( ECrudOp.LIST, null );
        }
        wasChangesWhilePaused = false;
      }
    }
  }

  @Override
  public void resumeFiringWithCounterReset( boolean aFireDelayed ) {
    if( pauseCounter == 0 ) { // already fired or not even paused yet
      return;
    }
    pauseCounter = 0;
    if( wasChangesWhilePaused ) {
      if( aFireDelayed ) {
        fireChangedEvent( ECrudOp.LIST, null );
      }
      wasChangesWhilePaused = false;
    }
  }

  @Override
  public boolean isFiringPaused() {
    return pauseCounter > 0;
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

  @Override
  public void clearCollectionChangeListeners() {
    listeners.clear();
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
   * We bring the logic closer to the "natural" understanding.<br>
   * <ul>
   * <li>If in place of <code>aIndex</code> there is an element with the key <code>aNewItem.id()</code>, then everything
   * is natural and understandable, corresponds to <code>doCanPut( aNewItem.id(), pointedItem, aNewItem )</code>;</li>
   * <li>If an existing item has a different key, and there is no item in the list with the key
   * <code>aNewItem.id()</code>, then this situation is also understandable, and corresponds to
   * <code>doCanPut( aNewItem.id(), null, aNewItem )</code>;</li>
   * <li>Problem if there is an element with an ID <code>aNewItem.id()</code> not in place of aIndex. Which of these two
   * elements (at position aIndex or with key <code>aNewItem.id()</code>) should be replaced? We assume that the user
   * wants to replace pointedItem, but there is an element with that ID. This doesn't match any validators method
   * IXxxCollectionChangeValidator, so we return the error.</li>
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
