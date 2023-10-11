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
class VedSnippetManager<T extends VedAbstractSnippet>
    implements IVedSnippetManager<T>, IGenericChangeEventCapable, ICloseable {

  private final GenericChangeEventer genericEventer;

  private final INotifierListEdit<T> snippetsList = new NotifierListEditWrapper<>( new ElemArrayList<>() );
  private final IListReorderer<T>    reorderer;

  public VedSnippetManager() {
    reorderer = new ListReorderer<>( snippetsList );
    genericEventer = new GenericChangeEventer( this );
    snippetsList.addCollectionChangeListener( ( src, op, item ) -> genericEventer.fireChangeEvent() );
  }

  // ------------------------------------------------------------------------------------
  // IVedSnippetsManager
  //

  @Override
  public IList<T> list() {
    return snippetsList;
  }

  @Override
  public IListReorderer<T> reorderer() {
    return reorderer;
  }

  @Override
  public void insert( int aIndex, T aSnippet ) {
    TsNullArgumentRtException.checkNull( aSnippet );
    TsErrorUtils.checkCollIndex( snippetsList.size(), aIndex );
    TsItemAlreadyExistsRtException.checkTrue( snippetsList.hasElem( aSnippet ) );
    snippetsList.insert( aIndex, aSnippet );
  }

  @Override
  public void remove( T aSnippet ) {
    if( snippetsList.remove( aSnippet ) >= 0 ) {
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
    snippetsList.pauseFiring();
    while( !snippetsList.isEmpty() ) {
      T e = snippetsList.removeByIndex( snippetsList.size() - 1 );
      e.dispose();
    }
    snippetsList.resumeFiring( true );
  }

}
