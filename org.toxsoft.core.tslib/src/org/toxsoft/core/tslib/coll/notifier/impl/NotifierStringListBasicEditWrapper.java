package org.toxsoft.core.tslib.coll.notifier.impl;

import java.util.Collection;
import java.util.Iterator;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.INotifierStringListBasicEdit;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListBasicEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IStringListBasicEdit}.
 *
 * @author hazard157
 */
public class NotifierStringListBasicEditWrapper
    extends AbstractNotifierList<String>
    implements INotifierStringListBasicEdit {

  private static final long serialVersionUID = 157157L;

  /**
   * Wrapped list.
   */
  private final IStringListBasicEdit source;

  /**
   * Constructor.
   *
   * @param aSource {@link IStringListBasicEdit} - the list to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierStringListBasicEditWrapper( IStringListBasicEdit aSource ) {
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
      for( String e : this ) {
        if( e == aItem ) {
          fireChangedEvent( ECrudOp.EDIT, aItem );
          return;
        }
      }
    }
    throw new TsItemNotFoundRtException();
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
  public Iterator<String> iterator() {
    return source.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( String aValue ) {
    return source.hasElem( aValue );
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  @Override
  public String[] toArray() {
    return source.toArray();
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( String aElem ) {
    return source.indexOf( aElem );
  }

  @Override
  public String get( int aIndex ) {
    return source.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( !isEmpty() ) {
      for( int i = 0, n = source.size(); i < n; i++ ) {
        checkRemove( source.get( i ) );
      }
      source.clear();
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  @Override
  public int add( String aElem ) {
    checkAdd( aElem );
    int index = source.add( aElem );
    fireChangedEvent( ECrudOp.CREATE, aElem );
    return index;
  }

  @Override
  public int remove( String aElem ) {
    if( source.hasElem( aElem ) ) {
      checkRemove( aElem );
    }
    int index = source.remove( aElem );
    if( index >= 0 ) {
      fireChangedEvent( ECrudOp.REMOVE, aElem );
    }
    return index;
  }

  @Override
  public String removeByIndex( int aIndex ) {
    checkRemove( source.get( aIndex ) );
    String value = source.removeByIndex( aIndex );
    fireChangedEvent( ECrudOp.REMOVE, value );
    return value;
  }

  @Override
  public void addAll( String... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length != 0 ) {
      for( int i = 0; i < aArray.length; i++ ) {
        checkRemove( aArray[i] );
      }
      source.addAll( aArray );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void addAll( ITsCollection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( !aColl.isEmpty() ) {
      for( String s : aColl ) {
        checkAdd( s );
      }
      source.addAll( aColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void addAll( Collection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( !aColl.isEmpty() ) {
      for( String s : aColl ) {
        checkAdd( s );
      }
      source.addAll( aColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void setAll( String... aElems ) {
    TsNullArgumentRtException.checkNull( aElems );
    if( aElems.length == 0 ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IStringList oldContent = new StringArrayList( source );
    source.clear();
    try {
      for( String s : aElems ) {
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

  @Override
  public void setAll( ITsCollection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( aColl.isEmpty() ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IStringList oldContent = new StringArrayList( source );
    source.clear();
    try {
      if( aColl instanceof ITsFastIndexListTag ) {
        ITsFastIndexListTag<String> coll = (ITsFastIndexListTag<String>)aColl;
        for( int i = 0, n = coll.size(); i < n; i++ ) {
          checkAdd( coll.get( i ) );
        }
      }
      else {
        for( String s : aColl ) {
          checkAdd( s );
        }
      }
    }
    catch( Exception e ) {
      source.setAll( oldContent );
      throw e;
    }
    source.addAll( aColl );
    fireChangedEvent( ECrudOp.LIST, null );
  }

  @Override
  public void setAll( Collection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( int i = 0, count = size(); i < count; i++ ) {
      checkRemove( get( i ) );
    }
    if( aColl.isEmpty() ) {
      if( !isEmpty() ) {
        source.clear();
        fireChangedEvent( ECrudOp.LIST, null );
      }
      return;
    }
    // сохраним содержимое, чтобы проверки шли с пустым списком
    IStringList oldContent = new StringArrayList( source );
    source.clear();
    try {
      for( String s : aColl ) {
        checkAdd( s );
      }
    }
    catch( Exception e ) {
      source.setAll( oldContent );
      throw e;
    }
    source.setAll( aColl );
    fireChangedEvent( ECrudOp.LIST, null );
  }

}
