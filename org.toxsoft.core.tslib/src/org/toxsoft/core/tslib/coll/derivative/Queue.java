package org.toxsoft.core.tslib.coll.derivative;

import static org.toxsoft.core.tslib.coll.derivative.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQueue} implementation as the wrapper over an editable list.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class Queue<E>
    implements IQueue<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * The wrapped source list.
   */
  private final IListBasicEdit<E> source;

  /**
   * Maximal size, maximal number of elements in collection or -1 if size is not restricted.
   */
  private final int maxSize;

  /**
   * Constructor with all invariants.
   *
   * @param aSourceList {@link IListBasicEdit}&lt;E&gt; - the source list
   * @param aMaxSize int - max number of elements or <0 for no size restriction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Queue( IListBasicEdit<E> aSourceList, int aMaxSize ) {
    source = TsNullArgumentRtException.checkNull( aSourceList );
    if( aMaxSize < 0 ) {
      maxSize = -1;
    }
    else {
      maxSize = aMaxSize;
    }
  }

  /**
   * Creates unrestricted queue.
   *
   * @param aSourceList {@link IListBasicEdit}&lt;E&gt; - the source list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Queue( IListBasicEdit<E> aSourceList ) {
    this( aSourceList, -1 );
  }

  /**
   * Creates restricted queue based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in queue
   */
  public Queue( int aMaxSize ) {
    this( new ElemLinkedList<>(), aMaxSize );
  }

  /**
   * Creates unrestricted queue based on linked list.
   */
  public Queue() {
    this( new ElemLinkedList<>(), -1 );
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public E get( int aIndex ) {
    return source.get( aIndex );
  }

  @Override
  public boolean hasElem( E aElem ) {
    return source.hasElem( aElem );
  }

  @Override
  public int indexOf( E aElem ) {
    return source.indexOf( aElem );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public E[] toArray( E[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    source.clear();
  }

  // ------------------------------------------------------------------------------------
  // ITsSizeRestrictableCollection
  //

  @Override
  public boolean isSizeRestricted() {
    return maxSize != -1;
  }

  @Override
  public int maxSize() {
    return maxSize;
  }

  // ------------------------------------------------------------------------------------
  // IQueue
  //

  @Override
  public boolean putTail( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      throw new TsIllegalStateRtException( MSG_ERR_QUEUE_FULL, Integer.valueOf( source.size() ) );
    }
    source.add( aElem );
    return true;
  }

  @Override
  public boolean offerTail( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      return false;
    }
    source.add( aElem );
    return true;
  }

  @Override
  public E getHead() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
    return source.removeByIndex( 0 );
  }

  @Override
  public E getHeadOrNull() {
    if( source.isEmpty() ) {
      return null;
    }
    return source.removeByIndex( 0 );
  }

  @Override
  public E peekHead() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_QUEUE_EMPTY );
    return source.get( 0 );
  }

  @Override
  public E peekHeadOrNull() {
    if( source.isEmpty() ) {
      return null;
    }
    return source.get( 0 );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IQueue) ) {
      return false;
    }
    return source.equals( obj );
  }

  @Override
  public int hashCode() {
    return source.hashCode();
  }

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

}
