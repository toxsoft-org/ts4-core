package org.toxsoft.core.tsgui.ved.screen.snippets;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages the VED screen snippets in the {@link IVedScreenModel}.
 *
 * @author hazard157
 * @param <T> - the snippet type
 */
public interface IVedSnippetManager<T extends VedAbstractSnippet> {

  /**
   * Returns the active snippets.
   *
   * @return {@link IList}&lt;T&gt; - active snippets list
   */
  IList<T> list();

  IListReorderer<T> reorderer();

  void insert( int aIndex, T aSnippet );

  void remove( T aSnippet );

  /**
   * Adds snippet to the end of the list.
   *
   * @param aSnippet &lt;T&gt; - snippet to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default void add( T aSnippet ) {
    insert( list().size(), aSnippet );
  }

}
