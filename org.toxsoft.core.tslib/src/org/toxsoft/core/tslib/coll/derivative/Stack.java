package org.toxsoft.core.tslib.coll.derivative;

import static org.toxsoft.core.tslib.coll.derivative.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStack} implementation as the wrapper over an editable list.
 * <p>
 * Deepest stack element has the index 0, stack top has index {@link #size()}-1.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class Stack<E>
    implements IStack<E>, Serializable {

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
   * <p>
   * Source list will be cleared.
   *
   * @param aSourceList {@link IListBasicEdit}&lt;E&gt; - the source list
   * @param aMaxSize int - max number of elements or <0 for no size restriction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Stack( IListBasicEdit<E> aSourceList, int aMaxSize ) {
    source = TsNullArgumentRtException.checkNull( aSourceList );
    if( aMaxSize < 0 ) {
      maxSize = -1;
    }
    else {
      maxSize = aMaxSize;
    }
    source.clear();
  }

  /**
   * Creates unrestricted stack.
   *
   * @param aSourceList {@link IListBasicEdit}&lt;E&gt; - the source list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Stack( IListBasicEdit<E> aSourceList ) {
    this( aSourceList, -1 );
  }

  /**
   * Creates restricted stack based on linked list.
   *
   * @param aMaxSize int - maximum number of elements in stack
   */
  public Stack( int aMaxSize ) {
    this( new ElemLinkedList<>(), aMaxSize );
  }

  /**
   * Creates unrestricted stack based on linked list.
   */
  public Stack() {
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
  // ITsClearable
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
  // IStack
  //

  @Override
  public void push( E aElem ) {
    if( isSizeRestricted() && size() >= maxSize ) {
      throw new TsIllegalStateRtException( MSG_ERR_STACK_FULL, Integer.valueOf( source.size() ) );
    }
    source.add( aElem );
  }

  @Override
  public E peek() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
    return source.get( source.size() - 1 );
  }

  @Override
  public E peekOrNull() {
    if( isEmpty() ) {
      return null;
    }
    return source.get( source.size() - 1 );
  }

  @Override
  public E pop() {
    TsIllegalStateRtException.checkTrue( isEmpty(), MSG_ERR_STACK_EMPTY );
    return source.removeByIndex( source.size() - 1 );
  }

  @Override
  public E popOrNull() {
    if( isEmpty() ) {
      return null;
    }
    return source.removeByIndex( source.size() - 1 );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IStack) ) {
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
