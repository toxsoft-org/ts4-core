package org.toxsoft.tslib.coll.primtypes.impl;

import java.io.Serializable;
import java.util.Iterator;

import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.primtypes.*;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Basic {@link IIntMap} implementation for sorted and unsorted maps.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
class IntMapBase<E>
    implements IIntMapEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IIntListBasicEdit keyList;
  private final IListEdit<E>      elemList;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Constructor for subclasses.
   *
   * @param aKeyList {@link IIntListBasicEdit} - keys list, either sorted or unsorted
   * @param aElemList {@link IListEdit}&lt;E&gt; - elements list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected IntMapBase( IIntListBasicEdit aKeyList, IListEdit<E> aElemList ) {
    TsNullArgumentRtException.checkNulls( aKeyList, aElemList );
    keyList = aKeyList;
    elemList = aElemList;
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return keyList.size();
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return elemList.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return elemList.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return elemList.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return elemList.toArray();
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( Integer aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return hasKey( aKey.intValue() );
  }

  @Override
  public E findByKey( Integer aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return findByKey( aKey.intValue() );
  }

  @Override
  public IIntList keys() {
    return keyList;
  }

  @Override
  public IList<E> values() {
    return elemList;
  }

  // ------------------------------------------------------------------------------------
  // IIntMap
  //

  @Override
  public boolean hasKey( int aKey ) {
    return keyList.hasValue( aKey );
  }

  @Override
  public E findByKey( int aKey ) {
    int index = keyList.indexOfValue( aKey );
    if( index >= 0 ) {
      return elemList.get( index );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    keyList.clear();
    elemList.clear();
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( Integer aKey, E aElem ) {
    TsNullArgumentRtException.checkNull( aKey );
    return put( aKey.intValue(), aElem );
  }

  @Override
  public E removeByKey( Integer aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    return removeByKey( aKey.intValue() );
  }

  // ------------------------------------------------------------------------------------
  // IIntMapEdit
  //

  @Override
  public E put( int aKey, E aElem ) {
    if( aElem == null ) {
      throw new TsNullArgumentRtException();
    }
    int index = keyList.indexOfValue( aKey );
    if( index < 0 ) { // adding an element
      // for sorted map new element will be inserted in it's place, for unsorted maps - at an end
      index = keyList.add( aKey );
      elemList.insert( index, aElem );
    }
    else { // updateing element
      return elemList.set( index, aElem );
    }

    return null;
  }

  @Override
  public E removeByKey( int aKey ) {
    int index = keyList.removeValue( aKey );
    if( index >= 0 ) {
      return elemList.removeByIndex( index );
    }
    return null;
  }

}
