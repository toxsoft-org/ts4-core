package org.toxsoft.core.tsgui.ved.screen.snippets;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
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
   * Returns the managed snippets.
   *
   * @return {@link IStridablesList}&lt;T&gt; - the ordered list of snippets
   */
  IList<T> list();

  /**
   * Returns the managed snippets order change means.
   *
   * @return {@link IListReorderer}&ltT&gt; - the {@link #list()} re-orderer
   */
  IListReorderer<T> reorderer();

  /**
   * Adds snippet to the end of the list {@link #list()}.
   *
   * @param aSnippet &lt;T&gt; - the snippet to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException the snippet is already added
   */
  default void add( T aSnippet ) {
    insert( list().size(), aSnippet );
  }

  /**
   * Inserts the snippet at the specified position in the list {@link #list()}.
   *
   * @param aIndex int - index of inserted item
   * @param aSnippet &lt;T&gt; - the snippet to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid index
   * @throws TsItemAlreadyExistsRtException the snippet is already added
   */
  void insert( int aIndex, T aSnippet );

  /**
   * Removes the snippet.
   * <p>
   * IF snippet does not exist then method does nothing.
   *
   * @param aSnippet &lt;T&gt; - the snippet to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( T aSnippet );

}
