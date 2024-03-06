package org.toxsoft.core.tslib.utils;

import java.lang.ref.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * List of listeners wrapped in {@link WeakReference}.
 * <p>
 * Intended to simplify implementation of Observer programming pattern.
 *
 * @author hazard157
 * @param <L> - type of the listeners
 */
public final class WeakRefListenersList<L>
    implements ITsClearable {

  private final IListEdit<WeakReference<L>> listeners = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public WeakRefListenersList() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private int getListenerIndexInListenersList( L aListener ) {
    TsNullArgumentRtException.checkNull( aListener );
    for( int i = 0; i < listeners.size(); i++ ) {
      WeakReference<L> wr = listeners.get( i );
      L l = wr.get();
      if( l == aListener ) {
        return i;
      }
    }
    return -1;
  }

  private void removeDisposedListeners() {
    int i = 0;
    while( i < listeners.size() ) {
      WeakReference<L> wr = listeners.get( i );
      L l = wr.get();
      if( l == null ) {
        listeners.removeByIndex( i );
      }
      else {
        ++i;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    while( !listeners.isEmpty() ) {
      WeakReference<L> ref = listeners.removeByIndex( 0 );
      ref.clear();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if there are no listeners registered.
   * <p>
   * The method {@link #list()} may return an empty list even if this method returns <code>true</code>. This may happen
   * because this method returns number of weak references in internal list and does <b>not</b> removes cleared
   * references while {@link #list()} removes them from internal list.
   *
   * @return boolean - <code>true</code> if there are no registered listeners in internal list
   */
  public boolean isEmpty() {
    return listeners.isEmpty();
  }

  /**
   * Returns the listeners list.
   * <p>
   * By the way removes references to disposed listeners from the internal weak references list.
   *
   * @return {@link IList}&lt;L&gt; - the listeners list
   */
  public IList<L> list() {
    if( listeners.isEmpty() ) {
      return IList.EMPTY;
    }
    removeDisposedListeners();
    IListEdit<L> ll = new ElemArrayList<>();
    for( int i = 0, n = listeners.size(); i < n; i++ ) {
      WeakReference<L> wr = listeners.get( i );
      L l = wr.get();
      if( l != null ) {
        ll.add( l );
      }
    }
    return ll;
  }

  /**
   * Adds the listener.
   * <p>
   * Does nothing if listener was already registered.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void addListener( L aListener ) {
    if( getListenerIndexInListenersList( aListener ) < 0 ) {
      listeners.add( new WeakReference<>( aListener ) );
    }
  }

  /**
   * Removes the listener.
   * <p>
   * Does nothing if listener was not registered.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void removeListener( L aListener ) {
    int index = getListenerIndexInListenersList( aListener );
    if( index >= 0 ) {
      WeakReference<L> wr = listeners.removeByIndex( index );
      wr.clear();
    }
  }

}
