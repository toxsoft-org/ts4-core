package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Balanced implementation of list as linked list of arrays (bundles).
 * <p>
 * {@link IList} is implemented as linked list of element arrays (bundles). When bundle becames empty, it is removed
 * from the chain of bundles. When inserting new element not more than two adjucent bundles are affected - new bundle
 * will be inserted in chain, if necessary.
 * <p>
 * Bundle capaciny (array length) can be specified in constructor. Increasing bundle capacity bring get() speed closer
 * to array access speed, while element insertion/removal speed decreases.
 *
 * @author hazard157
 */
public class StringLinkedBundleList
    implements IStringListEdit, Serializable {

  // FIXME serialize as array

  private static final long serialVersionUID = 157157L;

  private static class Bundle {

    final String elems[];

    int count = 0;

    Bundle next = null;

    Bundle( int aCapacity ) {
      elems = new String[aCapacity];
    }

    String lastValue() {
      return elems[count - 1];
    }

    String elem( int aIndex ) {
      return elems[aIndex];
    }

    boolean isEmpty() {
      return count == 0;
    }

    boolean isFull() {
      return count == elems.length;
    }

    public void insert( int aIndex, String aElem ) {
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

  private transient int     bundleCapacity;
  private transient boolean allowDuplicates;
  private transient int     size;
  private transient Bundle  lastBundle;
  transient Bundle          firstBundle;

  transient int changeCount = 0; // Counter of list editing operations used for concurrent access detection

  // --------------------------------------------------------------------------
  // Creation
  //

  /**
   * Creates list with all invariant properties.
   *
   * @param aBundleCapacity int - number of elements in bundle
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aBundleCapacity is out of range
   */
  public StringLinkedBundleList( int aBundleCapacity, boolean aAllowDuplicates ) {
    init( aBundleCapacity, aAllowDuplicates );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and bundle size of {@link #DEFAULT_BUNDLE_CAPACITY}.
   */
  public StringLinkedBundleList() {
    this( DEFAULT_BUNDLE_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems String[] - array of list elements
   * @throws TsNullArgumentRtException aElems or any element = <code>null</code>
   */
  public StringLinkedBundleList( String... aElems ) {
    this();
    TsErrorUtils.checkArrayArg( aElems );
    addAll( aElems );
  }

  /**
   * Copy constructor.
   *
   * @param aSrc {@link IStringList} - source list
   * @throws TsNullArgumentRtException aSrc = null
   */
  public StringLinkedBundleList( IStringList aSrc ) {
    this();
    TsNullArgumentRtException.checkNull( aSrc );
    addAll( aSrc );
  }

  private void init( int aBundleCapacity, boolean aAllowDuplicates ) {
    bundleCapacity = BUNDLE_CAPACITY_RANGE.inRange( aBundleCapacity );
    bundleCapacity = aBundleCapacity;
    firstBundle = new Bundle( bundleCapacity );
    lastBundle = firstBundle;
    size = 0;
    allowDuplicates = aAllowDuplicates;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Check that argument or any of it's element os not <code>null</code>.
   * <p>
   * Is used to check collection arguments.
   *
   * @param aColl Collection&lt;String&gt; - specified collection
   * @throws TsNullArgumentRtException aColl or any of it's element = null
   */
  private static void checkCollection( Collection<String> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    for( String s : aColl ) {
      TsNullArgumentRtException.checkNull( s );
    }
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
        aOut.writeUTF( b.elems[i] );
      }
    }
  }

  private void readObject( ObjectInputStream aIn )
      throws IOException {
    int rBundleCapacity = aIn.readInt();
    boolean rallowDups = aIn.readBoolean();
    init( rBundleCapacity, rallowDups );
    int rSize = aIn.readInt();
    for( int i = 0; i < rSize; i++ ) {
      String val = aIn.readUTF();
      add( val );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStringList
  //

  @Override
  public boolean isEmpty() {
    return firstBundle.isEmpty();
  }

  @Override
  public String get( int aIndex ) {
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

  @Override
  public boolean hasElem( String aElem ) {
    return indexOf( aElem ) >= 0;
  }

  @Override
  public int indexOf( String aElem ) {
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
  public int size() {
    return size;
  }

  @Override
  public String[] toArray() {
    String[] result = new String[size];
    for( int i = 0; i < size; i++ ) {
      // OPTIMIZE without get(), directly walk through bundles!
      result[i] = get( i );
    }
    return result;
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    String[] destArray = aSrcArray;
    if( destArray.length < size ) {
      destArray = new String[size];
    }
    int i;
    for( i = 0; i < size; i++ ) {
      destArray[i] = get( i );
    }
    for( ; i < destArray.length; i++ ) {
      destArray[i] = null;
    }
    return destArray;
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<>() {

      private final int expectedChangeCount = changeCount;

      Bundle currBundle = firstBundle;
      int    currIndex  = 0;

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
      public String next() {
        checkForConcurrentModification();
        if( hasNext() ) {
          String result = currBundle.elem( currIndex++ );
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
  // IStringListEdit
  //

  @Override
  public int add( String aElem ) {
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
  public void addAll( String... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    for( int i = 0; i < aArray.length; i++ ) {
      add( aArray[i] );
    }
  }

  @Override
  public void addAll( ITsCollection<String> aElemList ) {
    TsNullArgumentRtException.checkNull( aElemList );
    for( String e : aElemList ) {
      add( e );
    }
  }

  @Override
  public void addAll( Collection<String> aColl ) {
    checkCollection( aColl );
    for( String e : aColl ) {
      TsNullArgumentRtException.checkNull( e );
      add( e );
    }
  }

  @Override
  public void setAll( String... aStrings ) {
    TsNullArgumentRtException.checkNull( aStrings );
    clear();
    addAll( aStrings );
  }

  @Override
  public void setAll( ITsCollection<String> aStringList ) {
    TsNullArgumentRtException.checkNull( aStringList );
    clear();
    addAll( aStringList );
  }

  @Override
  public void setAll( Collection<String> aStrColl ) {
    TsNullArgumentRtException.checkNull( aStrColl );
    clear();
    addAll( aStrColl );
  }

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

  @Override
  public String removeByIndex( int aIndex ) {
    TsIllegalStateRtException.checkTrue( aIndex < 0 || aIndex >= size );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int index = aIndex;
    do {
      if( index < b.count ) {
        String result = b.elem( index );
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
        else { // shift down upper part of the array
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

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aCount < 0 );
    TsIllegalArgumentRtException.checkFalse( aIndex >= size() );
    if( aCount == 0 ) {
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aIndex + aCount >= size() );
    // OPTIMIZE create optimized implementation
    for( int i = 0; i < aCount; i++ ) {
      removeByIndex( aIndex );
    }
  }

  @Override
  public int remove( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    Bundle bPrev = null;
    int indexOfBundle0 = 0;
    do {
      for( int i = 0; i < b.count; i++ ) {
        if( b.elem( i ).equals( aElem ) ) { // b.elems[i] == aElem
          --b.count;
          --size;
          if( b.count == 0 ) { // bundle is empty
            if( bPrev == null ) { // the same as b == firstBundle check
              if( b.next != null ) { // is any more bundle?
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
          else { // shift down upper part of array
            for( int j = i; j < b.count; j++ ) {
              b.elems[j] = b.elems[j + 1];
            }
            b.elems[b.count] = null; // очистим неиспользуемую ссылку
          }
          ++changeCount;
          return i + indexOfBundle0;
        }
      }
      indexOfBundle0 += b.count;
      bPrev = b;
      b = b.next;
    } while( b != null );
    return -1;
  }

  @Override
  public String set( int aIndex, String aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    TsNullArgumentRtException.checkNull( aElem );
    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        String e = b.elem( index );
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
  public void insert( int aIndex, String aElem ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    TsNullArgumentRtException.checkNull( aElem );
    if( aIndex == size ) {
      add( aElem );
      return;
    }
    Bundle b = firstBundle;
    int index = aIndex;
    do {
      if( index < b.count ) {
        if( b.isFull() ) {
          Bundle bNext = b.next;
          if( bNext == null || bNext.isFull() ) { // нужно добавить связку
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

  @Override
  public void insertAll( int aIndex, String... aArray ) {
    TsErrorUtils.checkArrayArg( aArray );
    // OPTIMIZE create optimized implementation
    for( int i = aArray.length - 1; i >= 0; i-- ) {
      insert( aIndex, aArray[i] );
    }
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<String> aElemList ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size() );
    TsNullArgumentRtException.checkNull( aElemList );
    if( aElemList.isEmpty() ) {
      return;
    }
    // OPTIMIZE create optimized implementation
    if( aElemList instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<String> coll = (ITsFastIndexListTag<String>)aElemList;
      for( int i = coll.size() - 1; i >= 0; i-- ) {
        insert( aIndex, coll.get( i ) );
      }
    }
    else {
      IListEdit<String> tmpList = new ElemArrayList<>( aElemList.size() );
      for( String e : aElemList ) {
        tmpList.insert( 0, e );
      }
      insertAll( aIndex, tmpList );
    }
  }

  @Override
  public void insertAll( int aIndex, Collection<String> aColl ) {
    checkCollection( aColl );
    // OPTIMIZE create optimized implementation
    IStringList list = new StringArrayList( aColl );
    insertAll( aIndex, list );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IStringList ) {
      return TsCollectionsUtils.isListsEqual( this, (IStringList)aObj );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    for( int i = 0, n = size(); i < n; i++ ) {
      result = TsLibUtils.PRIME * result + get( i ).hashCode();
    }
    return result;
  }

}
