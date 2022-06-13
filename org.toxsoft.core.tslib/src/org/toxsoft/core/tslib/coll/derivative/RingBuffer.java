package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IRingBuffer} implementation.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class RingBuffer<E>
    implements IRingBuffer<E> {

  /**
   * Minimal allowed capacity of the buffer.
   */
  public static final int MIN_CAPACITY = 4;

  /**
   * Maximal allowed capacity of the buffer.
   */
  public static final int MAX_CAPACITY = 16 * 1024 * 14;

  private final Object[] items;

  private int size        = 0;
  private int oldestIndex = 0;

  /**
   * Constructor.
   * <p>
   * Capccity will be fitted in range {@link #MIN_CAPACITY} .. {@link #MAX_CAPACITY}.
   *
   * @param aCapacity int - buffer capacity
   */
  public RingBuffer( int aCapacity ) {
    int capacity = TsMiscUtils.inRange( aCapacity, MIN_CAPACITY, MIN_CAPACITY );
    items = new Object[capacity];
  }

  // ------------------------------------------------------------------------------------
  // inmplementation
  //

  private int capacity() {
    return items.length;
  }

  private int pos( int aCurr, int aDelta ) {
    TsIllegalArgumentRtException.checkTrue( aCurr < 0 || aCurr > capacity() );
    int delta;
    if( aDelta >= 0 ) {
      delta = aDelta % capacity();
    }
    else {
      delta = -((-aDelta) % capacity());
    }
    int pos = aCurr + delta;
    if( pos >= capacity() ) {
      pos -= capacity();
    }
    else {
      pos += capacity();
    }
    return pos;
  }

  // ------------------------------------------------------------------------------------
  // IRingBuffer
  //

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isSizeRestricted() {
    return true;
  }

  @Override
  public int maxSize() {
    return capacity();
  }

  @Override
  public void clear() {
    size = oldestIndex = 0;
  }

  @Override
  public void put( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    // if buffer is full just remove oldestItem
    if( size == capacity() ) {
      oldestIndex = pos( oldestIndex, 1 );
      --size;
    }
    int inIndex = pos( oldestIndex, size );
    items[inIndex] = aElem;
    ++size;
  }

  @Override
  public E get() {
    E e = getOrNull();
    if( e == null ) {
      throw new TsIllegalStateRtException();
    }
    return e;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E getOrNull() {
    if( size == 0 ) {
      return null;
    }
    E e = (E)items[oldestIndex];
    oldestIndex = pos( oldestIndex, 1 );
    --size;
    return e;
  }

  @Override
  public boolean isFull() {
    return size == capacity();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public IList<E> getItems() {
    IListEdit<E> ll = new ElemArrayList<>( size, true );
    for( int i = 0; i < size; i++ ) {
      ll.add( (E)items[pos( oldestIndex, i )] );
    }
    return ll;
  }

}
