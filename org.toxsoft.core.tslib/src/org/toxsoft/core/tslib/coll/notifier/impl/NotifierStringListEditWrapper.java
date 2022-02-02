package org.toxsoft.core.tslib.coll.notifier.impl;

import java.util.Collection;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.INotifierStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IStringListEdit}.
 *
 * @author hazard157
 */
public class NotifierStringListEditWrapper
    extends NotifierStringListBasicEditWrapper
    implements INotifierStringListEdit {

  private static final long serialVersionUID = 157157L;

  /**
   * Wrapped list.
   */
  private final IStringListEdit source;

  /**
   * Constructor.
   *
   * @param aSource {@link IStringListEdit} - the list to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierStringListEditWrapper( IStringListEdit aSource ) {
    super( aSource );
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStringListeEdit
  //

  @Override
  public String set( int aIndex, String aValue ) {
    String old = source.get( aIndex );
    checkReplace( old, aValue );
    String oldValue = source.set( aIndex, aValue );
    fireChangedEvent( ECrudOp.EDIT, aValue );
    return oldValue;
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    if( aCount > 0 ) {
      for( int i = aIndex, n = aIndex + aCount; i < n; i++ ) {
        checkRemove( source.get( i ) );
      }
      source.removeRangeByIndex( aIndex, aCount );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insert( int aIndex, String aValue ) {
    checkAdd( aValue );
    source.insert( aIndex, aValue );
    fireChangedEvent( ECrudOp.CREATE, aValue );
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    if( !aColl.isEmpty() ) {
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
      source.insertAll( aIndex, aColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insertAll( int aIndex, String... aArray ) {
    TsNullArgumentRtException.checkNull( aArray );
    if( aArray.length != 0 ) {
      for( String e : aArray ) {
        checkAdd( e );
      }
      source.insertAll( aIndex, aArray );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public void insertAll( int aIndex, Collection<String> aStrColl ) {
    TsNullArgumentRtException.checkNull( aStrColl );
    if( !aStrColl.isEmpty() ) {
      for( String s : aStrColl ) {
        checkAdd( s );
      }
      source.insertAll( aIndex, aStrColl );
      fireChangedEvent( ECrudOp.LIST, null );
    }
  }

}
