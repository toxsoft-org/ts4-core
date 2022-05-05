package org.toxsoft.core.tslib.coll.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Balanced implementation of list as linked list of arrays (bundles).
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class ElemLinkedBundleList<E>
    implements IListEdit<E>, Serializable {

  // FIXME serialize as array

  private static final long serialVersionUID = 157157L;

  /**
   * Bundleed array of elements, chained as linked list.
   *
   * @author hazard157
   */
  private class Bundle {

    final Object elems[];
    int          count = 0;
    Bundle       next  = null;

    Bundle( int aCapacity ) {
      elems = new Object[aCapacity];
    }

    @SuppressWarnings( "unchecked" )
    E lastValue() {
      return (E)elems[count - 1];
    }

    @SuppressWarnings( "unchecked" )
    E elem( int aIndex ) {
      return (E)elems[aIndex];
    }

    boolean isEmpty() {
      return count == 0;
    }

    boolean isFull() {
      return count == elems.length;
    }

    public void insert( int aIndex, E aElem ) {
      int ceiling = count;
      if( count == elems.length ) {
        --ceiling;
      }
      for( int i = ceiling; i > aIndex; i-- ) {
        elems[i] = elems[i - 1];
      }
      elems[aIndex] = aElem;
      if( count < elems.length ) {
        ++count;
      }
    }

  }

  transient int     bundleCapacity;
  transient boolean allowDuplicates;
  transient Bundle  firstBundle;
  transient Bundle  lastBundle;
  transient int     size;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  // --------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates list with all invariant properties.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public ElemLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    init( aBundleCapacity, aAllowDuplicates );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public ElemLinkedBundleList() {
    init( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems long[] - array of list elements
   * @throws TsNullArgumentRtException aElem = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  public ElemLinkedBundleList( E... aElems ) {
    this();
    addAll( aElems );
  }

  /**
   * Copy constructor.
   *
   * @param aSrc {@link ITsCollection} - source list
   * @throws TsNullArgumentRtException aSrc = null
   */
  public ElemLinkedBundleList( ITsCollection<E> aSrc ) {
    this();
    addAll( aSrc );
  }

  private void init( int aBundleCapacity, boolean aAllowDuplicates ) {
    TsIllegalArgumentRtException
        .checkTrue( aBundleCapacity < MIN_BUNDLE_CAPACITY || aBundleCapacity > MAX_BUNDLE_CAPACITY );
    bundleCapacity = aBundleCapacity;
    firstBundle = new Bundle( bundleCapacity );
    lastBundle = firstBundle;
    size = 0;
    allowDuplicates = aAllowDuplicates;
  }

  // ------------------------------------------------------------------------------------
  // Serialization
  // List is written in following form:
  // - int bundleCapacity
  // - boolean allowDuplicates
  // - int size
  // - long[] elements
  //

  private void writeObject( ObjectOutputStream aOut )
      throws IOException {
    aOut.defaultWriteObject();
    aOut.writeInt( bundleCapacity );
    aOut.writeBoolean( allowDuplicates );
    aOut.writeInt( size );
    for( Bundle b = firstBundle; b != null; b = b.next ) {
      for( int i = 0; i < b.count; i++ ) {
        aOut.writeObject( b.elems[i] );
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  private void readObject( ObjectInputStream aIn )
      throws IOException {
    int rBundleCapacity = aIn.readInt();
    boolean rallowDups = aIn.readBoolean();
    init( rBundleCapacity, rallowDups );
    int rSize = aIn.readInt();
    for( int i = 0; i < rSize; i++ ) {
      Object val;
      try {
        val = aIn.readObject();
      }
      catch( ClassNotFoundException ex ) {
        throw new TsIoRtException( ex );
      }
      add( (E)val );
    }
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {

      Bundle currBundle = firstBundle;
      int    currIndex  = 0;

      private final int expectedChangeCount = changeCount;

      private void checkForConcurrentModification() {
        if( expectedChangeCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return currBundle.next != null || currIndex < currBundle.count;
      }

      @Override
      public E next() {
        checkForConcurrentModification();
        if( hasNext() ) {
          E result = currBundle.elem( currIndex++ );
          if( currIndex >= currBundle.count && currBundle.next != null ) {
            currBundle = currBundle.next;
            currIndex = 0;
          }
          return result;
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return size;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( E aElem ) {
    return indexOf( aElem ) >= 0;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    E[] result = aSrcArray;
    if( aSrcArray.length < size ) {
      result = (E[])Array.newInstance( aSrcArray.getClass().getComponentType(), size );
    }
    for( int i = 0; i < size; i++ ) {
      result[i] = get( i );
    }
    for( int i = size; i < aSrcArray.length; i++ ) {
      result[i] = null;
    }
    return result;
  }

  @Override
  public Object[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    }
    Object[] result = new Object[size];
    for( int i = 0; i < size; i++ ) {
      result[i] = get( i );
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // IList
  //

  @Override
  public int indexOf( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    if( b.isEmpty() ) {
      return -1;
    }
    int index = 0;
    do {
      for( int i = 0, n = b.count; i < n; i++ ) {
        if( b.elem( i ).equals( aElem ) ) {
          return index + i;
        }
      }
      index += b.count;
      b = b.next;
    } while( b != null );
    return -1;
  }

  @Override
  public E get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        return b.elem( index );
      }
      index -= b.count;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    if( size != 0 ) {
      firstBundle.next = null;
      lastBundle = firstBundle;
      firstBundle.count = 0;
      Arrays.fill( firstBundle.elems, null );
      size = 0;
      ++changeCount;
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  @Override
  public int add( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( !allowDuplicates ) {
      TsItemAlreadyExistsRtException.checkTrue( indexOf( aElem ) >= 0 );
    }
    Bundle b = lastBundle;
    if( b.isFull() ) {
      b = new Bundle( bundleCapacity );
      lastBundle.next = b;
      lastBundle = b;
    }
    b.elems[b.count++] = aElem;
    ++changeCount;
    return size++;
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int indexBase = 0;
    do {
      for( int i = 0; i < b.count; i++ ) {
        if( b.elem( i ).equals( aElem ) ) { // b.elems[i] == aElem
          --b.count;
          --size;
          if( b.count == 0 ) { // buncde is empty
            if( bPrev == null ) { // the same as check b == firstBundle
              if( b.next != null ) { // is there any more bundles?
                firstBundle = b.next;
              }
            }
            else {
              if( b == lastBundle ) {
                lastBundle = bPrev.next; // here bPrev != null
                lastBundle.next = null;
              }
              else {
                bPrev.next = b.next;
              }
            }
          }
          else { // shift down upper part of the array
            for( int j = i; j < b.count; j++ ) {
              b.elems[j] = b.elems[j + 1];
            }
            b.elems[b.count] = null; // clear unued reference
          }
          ++changeCount;
          return indexBase + i;
        }
      }
      indexBase += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    return -1;
  }

  @Override
  public E removeByIndex( int aIndex ) {
    TsIllegalStateRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int index = aIndex;
    do {
      if( index < b.count ) {
        E result = b.elem( index );
        --b.count;
        --size;
        if( b.count == 0 ) { // bundle is empty
          if( bPrev == null ) {
            if( b.next != null ) {
              firstBundle = b.next;
            }
          }
          else {
            if( b == lastBundle ) {
              lastBundle = bPrev.next; // here bPrev != null
              lastBundle.next = null;
            }
            else {
              bPrev.next = b.next;
            }
          }
        }
        else { // сдвинем вниз верхнюю часть массива
          for( int j = index; j < b.count; j++ ) {
            b.elems[j] = b.elems[j + 1];
          }
          b.elems[b.count] = null; // clear unused reference
        }
        ++changeCount;
        return result;
      }
      index -= b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // IListBasicEdit
  //

  // nop

  // ------------------------------------------------------------------------------------
  // IListEdit
  //

  @Override
  public E set( int aIndex, E aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    TsNullArgumentRtException.checkNull( aElem );

    // FIXME check for duplicates

    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        E e = b.elem( index );
        b.elems[index] = aElem;
        ++changeCount;
        return e;
      }
      index -= b.count;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    TsNullArgumentRtException.checkNull( aElem );
    if( aIndex == size ) {
      add( aElem );
      return;
    }

    // FIXME check for duplicates

    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        if( b.isFull() ) {
          Bundle bNext = b.next;
          if( bNext == null || bNext.isFull() ) { // need to add new bundle
            Bundle newBundle = new Bundle( bundleCapacity );
            newBundle.next = b.next;
            b.next = newBundle;
            if( b == lastBundle ) {
              lastBundle = newBundle;
            }
            bNext = newBundle;
          }
          bNext.insert( 0, b.lastValue() );
        }
        b.insert( index, aElem );
        ++changeCount;
        ++size;
        return;
      }
      index -= b.count;
      b = b.next;
    } while( b != null );
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( !(obj instanceof IList) ) {
      return false;
    }
    return TsCollectionsUtils.isListsEqual( this, (IList)obj );
  }

  @Override
  public int hashCode() {
    return TsCollectionsUtils.calcListHashCode( this );
  }

}
