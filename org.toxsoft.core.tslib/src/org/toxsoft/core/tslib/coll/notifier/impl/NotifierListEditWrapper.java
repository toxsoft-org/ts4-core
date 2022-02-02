package org.toxsoft.core.tslib.coll.notifier.impl;

import java.util.Collection;

import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.INotifierListEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IListEdit}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class NotifierListEditWrapper<E>
    extends NotifierListBasicEditWrapper<E>
    implements INotifierListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Список, который "оборачивается" настоящим классом.
   */
  private final IListEdit<E> source;

  /**
   * Constructor.
   *
   * @param aSource {@link IListEdit} - the list to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierListEditWrapper( IListEdit<E> aSource ) {
    super( aSource );
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    E old = source.get( aIndex );
    checkReplace( old, aElem );
    E e = source.set( aIndex, aElem );
    fireChangedEvent( ECrudOp.LIST, null );
    return e;
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkAdd( aElem );
    source.insert( aIndex, aElem );
    fireChangedEvent( ECrudOp.CREATE, aElem );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length != 0 ) {
      for( E e : aArray ) {
        checkAdd( e );
      }
      source.insertAll( aIndex, aArray );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );
    if( !aElemList.isEmpty() ) {
      if( aElemList instanceof ITsFastIndexListTag ) {
        ITsFastIndexListTag<E> coll = (ITsFastIndexListTag<E>)aElemList;
        for( int i = 0, n = coll.size(); i < n; i++ ) {
          checkAdd( coll.get( i ) );
        }
      }
      else {
        for( E e : aElemList ) {
          checkAdd( e );
        }
      }
      source.insertAll( aIndex, aElemList );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aElemColl ) {
    TsNullArgumentRtException.checkNull( aElemColl );
    if( !aElemColl.isEmpty() ) {
      for( E e : aElemColl ) {
        checkAdd( e );
      }
      source.insertAll( aIndex, aElemColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

}
