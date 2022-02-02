package org.toxsoft.core.tslib.coll.primtypes.impl;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.Serializable;
import java.util.*;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsFastIndexListTag;
import org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Strings list, implemented as array of strings
 * <p>
 * <p>
 * During this list lifecycle length of internal array only increases the only way to decrease used memory is method
 * {@link #truncate()}.
 *
 * @author hazard157
 */
public class StringArrayList
    implements IStringListEdit, ITsFastIndexListTag<String>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * <code>true</code> if duplicate items are allowed in list.
   */
  protected final boolean allowDuplicates;

  /**
   * Internal array to hold list elements, only first {@link #size} items are used as list elements.
   */
  private String[] items = TsLibUtils.EMPTY_ARRAY_OF_STRINGS;

  /**
   * Number of elements in list - number of used items in array {@link #items}.
   */
  protected int size = 0;

  /**
   * Counter of list editing operations used for concurrent access detection.
   */
  protected int changeCount = 0;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates list with all invariant properties.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public StringArrayList( int aInitialCapacity, boolean aAllowDuplicates ) {
    TsIllegalArgumentRtException.checkTrue( aInitialCapacity < 0 );
    if( aInitialCapacity != 0 ) {
      ensureCapacity( aInitialCapacity );
    }
    allowDuplicates = aAllowDuplicates;
  }

  /**
   * Constructor creates list with default capacity {@link #DEFAULT_ARRAY_LIST_CAPACITY}.
   *
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   */
  public StringArrayList( boolean aAllowDuplicates ) {
    this( DEFAULT_ARRAY_LIST_CAPACITY, aAllowDuplicates );
  }

  /**
   * Creates empty list with duplicates allowed.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public StringArrayList( int aInitialCapacity ) {
    this( aInitialCapacity, true );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and capacity of {@link #DEFAULT_ARRAY_LIST_CAPACITY}.
   */
  public StringArrayList() {
    this( DEFAULT_ARRAY_LIST_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems String[] - array of list elements
   * @throws TsNullArgumentRtException array reference on any of it's element = <code>null</code>
   */
  public StringArrayList( String... aElems ) {
    TsErrorUtils.checkArrayArg( aElems );
    allowDuplicates = true;
    size = aElems.length;
    if( size > 0 ) {
      items = new String[size];
      for( int i = 0; i < size; i++ ) {
        items[i] = aElems[i];
      }
    }
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;E&gt; - collection of lisyt elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public StringArrayList( Collection<String> aSource ) {
    this();
    addAll( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;String&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public StringArrayList( ITsCollection<String> aSource ) {
    this();
    addAll( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  protected StringArrayList src2list( String[] aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    StringArrayList result = new StringArrayList( aSource.length, allowDuplicates );
    for( int i = 0, n = aSource.length; i < n; i++ ) {
      result.add( aSource[i] );
    }
    return result;
  }

  protected StringArrayList src2list( IStringList aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    StringArrayList result = new StringArrayList( aSource.size(), allowDuplicates );
    for( int i = 0, n = aSource.size(); i < n; i++ ) {
      result.add( aSource.get( i ) );
    }
    return result;
  }

  protected StringArrayList src2list( ITsCollection<String> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    StringArrayList result = new StringArrayList( aSource.size(), allowDuplicates );
    if( aSource instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<String> src = (ITsFastIndexListTag<String>)aSource;
      for( int i = 0, n = src.size(); i < n; i++ ) {
        result.add( src.get( i ) );
      }
    }
    else {
      for( String s : aSource ) {
        result.add( s );
      }
    }
    return result;
  }

  protected StringArrayList src2list( Collection<String> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    StringArrayList result = new StringArrayList( aSource.size(), allowDuplicates );
    for( String s : aSource ) {
      result.add( s );
    }
    return result;
  }

  protected void addList( StringArrayList aList ) {
    if( aList.size > 0 ) {
      ensureCapacity( size + aList.size );
      System.arraycopy( aList.items, 0, items, size, aList.size );
      size += aList.size;
      ++changeCount;
    }
  }

  protected void insertList( int aIndex, StringArrayList aList ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    int argSize = aList.size;
    if( argSize > 0 ) {
      String[] src = items;
      String[] dest = items;
      int newSize = size + argSize;
      if( newSize > items.length ) {
        dest = new String[newSize + MIN_ARRAY_LIST_CAPACITY];
        System.arraycopy( src, 0, dest, 0, aIndex );
      }
      System.arraycopy( src, aIndex, dest, aIndex + argSize, size - aIndex );
      System.arraycopy( aList.items, 0, dest, aIndex, argSize );
      items = dest;
      size = newSize;
      ++changeCount;
    }
  }

  /**
   * Calculates recommended new capcaity of the array when adding new elements.
   *
   * @return int - new reommended length - 2 time larger than current array
   */
  private int recommendNewLength() {
    int recmmendedLength = items.length + items.length;
    if( recmmendedLength < MIN_ARRAY_LIST_CAPACITY ) {
      recmmendedLength = MIN_ARRAY_LIST_CAPACITY;
    }
    return recmmendedLength;
  }

  // --------------------------------------------------------------------------
  // IStringList
  //

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean hasElem( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    for( int i = 0; i < size; i++ ) {
      if( items[i].equals( aElem ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    for( int i = 0; i < size; i++ ) {
      if( items[i].equals( aElem ) ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public String get( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    return items[aIndex];
  }

  @Override
  public String[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    }
    String[] result = new String[size];
    System.arraycopy( items, 0, result, 0, size );
    return result;
  }

  @Override
  public String[] toArray( String[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length < size ) {
      // Make a new array of a's runtime type, but my contents:
      return Arrays.copyOf( items, size, aSrcArray.getClass() );
    }
    System.arraycopy( items, 0, aSrcArray, 0, size );
    for( int i = size; i < aSrcArray.length; i++ ) {
      aSrcArray[i] = null;
    }
    return aSrcArray;
  }

  // --------------------------------------------------------------------------
  // IStringListBasicEdit
  //

  @Override
  public int add( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( !allowDuplicates ) {
      int index = indexOf( aElem );
      if( index >= 0 ) {
        return index;
      }
    }
    ensureCapacity( size + 1 );
    ++changeCount;
    items[size] = aElem;
    return size++;
  }

  @Override
  public void addAll( String... aArray ) {
    addList( src2list( aArray ) );
  }

  @Override
  public void addAll( ITsCollection<String> aElemList ) {
    addList( src2list( aElemList ) );
  }

  @Override
  public void addAll( Collection<String> aStrColl ) {
    addList( src2list( aStrColl ) );
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
    size = 0;
    ++changeCount;
  }

  @Override
  public int remove( String aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int index = indexOf( aElem );
    if( index >= 0 ) {
      removeByIndex( index );
    }
    return index;
  }

  @Override
  public String removeByIndex( int aIndex ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    String result = items[aIndex];
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
    items[size] = null; // освободим ссылку для сборки мусора
    ++changeCount;
    return result;
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    TsIllegalArgumentRtException.checkTrue( aCount < 0 || aCount > (size - aIndex) );
    if( aCount > 0 ) {
      System.arraycopy( items, aIndex + aCount, items, aIndex, size - aIndex - aCount );
      for( int i = size - aCount; i < size; i-- ) {
        items[i] = null; // освободим ссылку для сборки мусора
      }
      size -= aCount;
      ++changeCount;
    }
  }

  // --------------------------------------------------------------------------
  // IStringListEdit
  //

  @Override
  public String set( int aIndex, String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= size );
    if( !allowDuplicates ) {
      TsItemAlreadyExistsRtException.checkTrue( hasElem( aString ) );
    }
    String result = items[aIndex];
    items[aIndex] = aString;
    ++changeCount;
    return result;
  }

  @Override
  public void insert( int aIndex, String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex > size );
    if( allowDuplicates || !hasElem( aString ) ) {
      String[] src = items;
      String[] dest = items;
      if( size == items.length ) {
        dest = new String[recommendNewLength()];
        System.arraycopy( src, 0, dest, 0, aIndex );
      }
      System.arraycopy( src, aIndex, dest, aIndex + 1, size - aIndex );
      dest[aIndex] = aString;
      items = dest;
      ++size;
      ++changeCount;
    }
  }

  @Override
  public void insertAll( int aIndex, String... aArray ) {
    insertList( aIndex, src2list( aArray ) );
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<String> aStrList ) {
    insertList( aIndex, src2list( aStrList ) );
  }

  @Override
  public void insertAll( int aIndex, Collection<String> aStrColl ) {
    insertList( aIndex, src2list( aStrColl ) );
  }

  // --------------------------------------------------------------------------
  // Implementation specific additional API
  //

  /**
   * Increases (if required) intrenal array length to not less the specified capoacity.
   * <p>
   * Ths ize and contentof list remains the same.
   *
   * @param aCapacity int - requested minmal length of internal array
   * @throws TsIllegalArgumentRtException aCapacity < 0
   */
  public void ensureCapacity( int aCapacity ) {
    TsIllegalArgumentRtException.checkTrue( aCapacity < 0 );
    if( aCapacity > items.length ) {
      int newLength = recommendNewLength();
      if( newLength < aCapacity ) {
        newLength = aCapacity;
      }
      String[] newItems = new String[newLength];
      System.arraycopy( items, 0, newItems, 0, items.length );
      items = newItems;
    }
  }

  /**
   * Removes all elements from list and truncates internall array.
   * <p>
   * This is the only way to <b>decrease</b> ths size of internal array.
   */
  public void truncate() {
    size = 0;
    items = TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
    ++changeCount;
  }

  /**
   * Returns reference to internal array.
   * <p>
   * Warning: changing array elements will change list content! This method is made public only for library developers.
   * The single prupose of method is optimization of some internal library routines.
   *
   * @return &lt;String&gt;[] - internall array of elements
   */
  public String[] getInternalArray() {
    return items;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<String> iterator() {
    return new Iterator<>() {

      int               index               = 0;
      private final int expectedChnageCount = changeCount;

      private void checkForConcurrentModification() {
        if( expectedChnageCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return index < size();
      }

      @Override
      public String next() {
        if( hasNext() ) {
          return get( index++ );
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
