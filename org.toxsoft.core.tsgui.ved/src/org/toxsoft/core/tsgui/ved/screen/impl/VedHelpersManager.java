package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedSnippetManager} implementation.
 *
 * @author hazard157
 * @param <T> - the snippet type
 */
class VedSnippetsManager<T extends VedAbstractSnippet>
    implements IVedSnippetManager<T>, IGenericChangeEventCapable, ICloseable {

  private final GenericChangeEventer genericEventer;

  private final IListEdit<T>         activeList = new ElemArrayList<>();
  private final INotifierListEdit<T> allList    = new NotifierListEditWrapper<>( new ElemArrayList<>() );
  private final IListReorderer<T>    reorderer;

  public VedSnippetsManager() {
    reorderer = new ListReorderer<>( allList );
    genericEventer = new GenericChangeEventer( this );
    allList.addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      refreshActiveSnippetsList( null );
      genericEventer.fireChangeEvent();

    } );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void refreshActiveSnippetsList( T aAddedSnippet ) {
    IListEdit<T> ll = new ElemArrayList<>( allList.size() );
    for( T e : allList ) {
      if( e == aAddedSnippet || activeList.hasElem( e ) ) {
        ll.add( e );
      }
    }
    activeList.addAll( ll );
  }

  // ------------------------------------------------------------------------------------
  // IVedSnippetsManager
  //

  @Override
  public IList<T> list() {
    return activeList;
  }

  @Override
  public IList<T> listAll() {
    return allList;
  }

  @Override
  public boolean isActive( T aSnippet ) {
    return activeList.hasElem( aSnippet );
  }

  @Override
  public void setActive( T aSnippet, boolean aActive ) {
    if( allList.hasElem( aSnippet ) ) {
      if( isActive( aSnippet ) == aActive ) {
        return;
      }
      if( aActive ) {
        refreshActiveSnippetsList( aSnippet );
      }
      else {
        activeList.remove( aSnippet );
      }
      genericEventer.fireChangeEvent();
    }
  }

  @Override
  public IListReorderer<T> reorderer() {
    return reorderer;
  }

  @Override
  public void insert( int aIndex, T aSnippet ) {
    TsNullArgumentRtException.checkNull( aSnippet );
    TsErrorUtils.checkCollIndex( allList.size(), aIndex );
    TsItemAlreadyExistsRtException.checkTrue( allList.hasElem( aSnippet ) );
    try {
      allList.add( aSnippet );
      refreshActiveSnippetsList( aSnippet );
    }
    finally {
      allList.resumeFiring( true );
    }
  }

  @Override
  public void remove( T aSnippet ) {
    if( allList.remove( aSnippet ) >= 0 ) {
      aSnippet.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericEventer;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    allList.pauseFiring();
    activeList.clear();
    while( !allList.isEmpty() ) {
      T e = allList.removeByIndex( allList.size() - 1 );
      e.dispose();
    }
    allList.resumeFiring( true );
  }

}
