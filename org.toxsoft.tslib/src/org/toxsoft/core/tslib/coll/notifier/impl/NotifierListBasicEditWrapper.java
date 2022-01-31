package org.toxsoft.core.tslib.coll.notifier.impl;

import java.util.Collection;
import java.util.Iterator;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListBasicEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.notifier.INotifierListBasicEdit;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IListBasicEdit}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class NotifierListBasicEditWrapper<E>
    extends AbstractNotifierList<E>
    implements INotifierListBasicEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Wrapped list.
   */
  private IListBasicEdit<E> source;

  /**
   * Constructor.
   *
   * @param aSource {@link IListBasicEdit} - the list to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierListBasicEditWrapper( IListBasicEdit<E> aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ITsNotifierCollection
  //

  @Override
  public void fireItemByIndexChangeEvent( int aIndex ) {
    fireChangedEvent( ECrudOp.EDIT, get( aIndex ) );
  }

  @Override
  public void fireItemByRefChangeEvent( Object aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    if( this instanceof ITsFastIndexListTag ) {
      for( int i = size() - 1; i >= 0; i-- ) {
        if( get( i ) == aItem ) {
          fireChangedEvent( ECrudOp.EDIT, aItem );
          return;
        }
      }
    }
    else {
      for( E e : this ) {
        if( e == aItem ) {
          fireChangedEvent( ECrudOp.EDIT, aItem );
          return;
        }
      }
    }
    throw new TsItemNotFoundRtException();
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

  @Override
  public boolean hasElem( E aElem ) {
    return source.hasElem( aElem );
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // IListBasicEdit
  //

  @Override
  public int add( E aElem ) {
    checkAdd( aElem );
    int index = source.add( aElem );
    fireChangedEvent( ECrudOp.CREATE, aElem );
    return index;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void addAll( E... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length > 0 ) {
      for( int i = 0; i < aArray.length; i++ ) {
        checkRemove( aArray[i] );
      }
      source.addAll( aArray );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void addAll( ITsCollection<E> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );
    if( !aElemList.isEmpty() ) {
      if( aElemList instanceof ITsFastIndexListTag ) {
        ITsFastIndexListTag<E> src = (ITsFastIndexListTag<E>)aElemList;
        for( int i = 0, n = src.size(); i < n; i++ ) {
          checkAdd( src.get( i ) );
        }
      }
      else {
        for( E e : aElemList ) {
          checkAdd( e );
        }
      }
      source.addAll( aElemList );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void addAll( Collection<E> aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    if( !aElems.isEmpty() ) {
      for( E e : aElems ) {
        checkAdd( e );
      }
      source.addAll( aElems );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    checkRemove( aElem );
    int retval = source.remove( aElem );
    if( retval >= 0 ) {
      fireChangedEvent( ECrudOp.REMOVE, aElem );
    }
    return retval;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    checkRemove( source.get( aIndex ) );
    E e = source.removeByIndex( aIndex );
    fireChangedEvent( ECrudOp.REMOVE, e );
    return e;
  }

  @Override
  public void setAll( ITsCollection<E> aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    if( aElems.isEmpty() ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IList<E> oldContent = new ElemArrayList<>( source );
    source.clear();
    try {
      if( aElems instanceof ITsFastIndexListTag ) {
        ITsFastIndexListTag<E> coll = (ITsFastIndexListTag<E>)aElems;
        for( int i = 0, n = coll.size(); i < n; i++ ) {
          checkAdd( coll.get( i ) );
        }
      }
      else {
        for( E s : aElems ) {
          checkAdd( s );
        }
      }
    }
    catch( Exception e ) {
      source.setAll( oldContent );
      throw e;
    }
    source.addAll( aElems );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @Override
  public void setAll( Collection<E> aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    if( aElems.isEmpty() ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IList<E> oldContent = new ElemArrayList<>( source );
    source.clear();
    try {
      for( E s : aElems ) {
        checkAdd( s );
      }
    }
    catch( Exception e ) {
      source.setAll( oldContent );
      throw e;
    }
    source.addAll( aElems );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void setAll( E... aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    if( aElems.length == 0 ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IList<E> oldContent = new ElemArrayList<>( source );
    source.clear();
    try {
      for( E s : aElems ) {
        checkAdd( s );
      }
    }
    catch( Exception e ) {
      source.setAll( oldContent );
      throw e;
    }
    source.addAll( aElems );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( !isEmpty() ) {
      if( source instanceof ITsFastIndexListTag ) {
        for( int i = 0, n = source.size(); i < n; i++ ) {
          checkRemove( source.get( i ) );
        }
      }
      else {
        for( E e : source ) {
          checkRemove( e );
        }
      }
      source.clear();
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

}
