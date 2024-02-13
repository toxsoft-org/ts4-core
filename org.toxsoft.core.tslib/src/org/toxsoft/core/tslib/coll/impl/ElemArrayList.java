package org.toxsoft.core.tslib.coll.impl;

import static org.toxsoft.core.tslib.coll.ITsSharedResources.*;
import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IListEdit} implementation as single array of elements.
 * <p>
 * During this list lifecycle length of internal array only increases the only way to decrease used memory is method
 * {@link #truncate()}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class ElemArrayList<E>
    implements IListEdit<E>, ITsFastIndexListTag<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * <code>true</code> if duplicate items are allowed in list.
   */
  protected final boolean allowDuplicates;

  /**
   * Internal array to hold list elements, only first {@link #size} items are used as list elements.
   */
  private Object[] items = TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;

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
  public ElemArrayList( int aInitialCapacity, boolean aAllowDuplicates ) {
    if( aInitialCapacity < 0 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_INITIAL_CAPACITY, this.getClass().getSimpleName(),
          Integer.valueOf( aInitialCapacity ) );
    }
    if( aInitialCapacity > MIN_ARRAY_LIST_CAPACITY ) {
      ensureCapacity( aInitialCapacity );
    }
    else {
      ensureCapacity( MIN_ARRAY_LIST_CAPACITY );
    }
    allowDuplicates = aAllowDuplicates;
  }

  /**
   * Constructor creates list with default capacity {@link #DEFAULT_ARRAY_LIST_CAPACITY}.
   *
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   */
  public ElemArrayList( boolean aAllowDuplicates ) {
    this( DEFAULT_ARRAY_LIST_CAPACITY, aAllowDuplicates );
  }

  /**
   * Creates empty list with duplicates allowed.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public ElemArrayList( int aInitialCapacity ) {
    this( aInitialCapacity, true );
  }

  /**
   * Creates empty list with defaults: duplicates allowed and capacity of {@link #DEFAULT_ARRAY_LIST_CAPACITY}.
   */
  public ElemArrayList() {
    this( DEFAULT_ARRAY_LIST_CAPACITY, true );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems &lt;E&gt;[] - array of list elements
   * @throws TsNullArgumentRtException array reference on any of it's element = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  public ElemArrayList( E... aElems ) {
    this( TsErrorUtils.checkArrayArg( aElems ).length, false );
    System.arraycopy( aElems, 0, items, 0, aElems.length );
    size = aElems.length;
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;E&gt; - collection of lisyt elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public ElemArrayList( Collection<E> aSource ) {
    this();
    addAll( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;E&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public ElemArrayList( ITsCollection<E> aSource ) {
    this();
    addAll( aSource );
  }

  private ElemArrayList( @SuppressWarnings( "unused" ) long aFoo, E[] aSourceArray ) {
    TsErrorUtils.checkArrayArg( aSourceArray );
    allowDuplicates = false;
    items = aSourceArray;
    size = items.length;
  }

  /**
   * Creates {@link ElemArrayList} using specified array as internal holder.
   * <p>
   * No check for <code>null</code>-s are performed in this constructor! <b>Warning:</b> this is optimization for
   * library code writers! Use with caution! Modifing the source array after list creation will lead to program crash!
   *
   * @param <E> - the type of elements in collection
   * @param aSourceArray &le;E&gt; - the source array
   * @return {@link ElemArrayList}&le;E&gt; - new instanse
   */
  public static <E> ElemArrayList<E> createDirect( E[] aSourceArray ) {
    return new ElemArrayList<>( 0L, aSourceArray );
  }

  // ------------------------------------------------------------------------------------
  // Internal methods
  //

  protected ElemArrayList<E> src2list( E[] aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    Iterable<E> src = createDirect( aSource );
    int count = 0;
    if( !allowDuplicates ) {
      Set<E> set = new HashSet<>();
      for( E e : aSource ) {
        if( !hasElem( e ) ) {
          set.add( e );
          count++;
        }
      }
      src = set;
    }
    ElemArrayList<E> result = new ElemArrayList<>( count, true );
    for( E e : src ) {
      result.add( e );
    }
    return result;
  }

  protected ElemArrayList<E> src2list( ITsCollection<E> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    Iterable<E> src = aSource;
    int count = 0;
    if( !allowDuplicates ) {
      Set<E> set = new HashSet<>();
      for( E e : aSource ) {
        if( !hasElem( e ) ) {
          set.add( e );
          count++;
        }
      }
      src = set;
    }
    ElemArrayList<E> result = new ElemArrayList<>( count, true );
    for( E e : src ) {
      result.add( e );
    }
    return result;
  }

  protected ElemArrayList<E> src2list( Collection<E> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    ElemArrayList<E> result = new ElemArrayList<>( aSource.size(), true );
    Collection<E> src = allowDuplicates ? aSource : new HashSet<>( aSource );
    for( E e : src ) {
      if( !allowDuplicates ) {
        if( hasElem( e ) ) {
          continue;
        }
      }
      result.add( e );
    }
    return result;
  }

  protected void addList( ElemArrayList<E> aList ) {
    if( aList.size > 0 ) {
      ensureCapacity( size + aList.size );
      System.arraycopy( aList.items, 0, items, size, aList.size );
      size += aList.size;
      ++changeCount;
    }
  }

  protected void insertList( int aIndex, ElemArrayList<E> aList ) {
    if( aIndex < 0 || aIndex > size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, this.getClass().getSimpleName(),
          Integer.valueOf( aIndex ), Integer.valueOf( size ) );
    }
    int argSize = aList.size;
    if( argSize > 0 ) {
      Object[] src = items;
      Object[] dest = items;
      int newSize = size + argSize;
      if( newSize > items.length ) {
        dest = new Object[newSize + MIN_ARRAY_LIST_CAPACITY];
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
   * When adding an element to the full list this method calculates new recommended length of internal array.
   *
   * @return int - new length (2 times bigger in this implementation)
   */
  private int recommendNewLength() {
    if( items.length >= Integer.MAX_VALUE / 2 ) {
      return Integer.MAX_VALUE - 1;
    }
    int recmmendedLength = items.length + items.length;
    if( recmmendedLength < MIN_ARRAY_LIST_CAPACITY ) {
      recmmendedLength = MIN_ARRAY_LIST_CAPACITY;
    }
    return recmmendedLength;
  }

  // --------------------------------------------------------------------------
  // IList implementation
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
  public boolean hasElem( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    for( int i = 0; i < size; i++ ) {
      if( items[i].equals( aElem ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int indexOf( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    for( int i = 0; i < size; i++ ) {
      if( items[i].equals( aElem ) ) {
        return i;
      }
    }
    return -1;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E get( int aIndex ) {
    if( aIndex < 0 || aIndex >= size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, this.getClass().getSimpleName(),
          Integer.valueOf( aIndex ), Integer.valueOf( size - 1 ) );
    }
    return (E)items[aIndex];
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    if( aSrcArray.length < size ) {
      // Make a new array of a's runtime type, but my contents:
      return (E[])Arrays.copyOf( items, size, aSrcArray.getClass() );
    }
    System.arraycopy( items, 0, aSrcArray, 0, size );
    for( int i = size; i < aSrcArray.length; i++ ) {
      aSrcArray[i] = null;
    }
    return aSrcArray;
  }

  @Override
  public Object[] toArray() {
    if( size == 0 ) {
      return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    }
    return Arrays.copyOf( items, size );
  }

  // --------------------------------------------------------------------------
  // IListBasicEdit
  //

  @Override
  public int add( E aElem ) {
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

  @SuppressWarnings( "unchecked" )
  @Override
  public void addAll( E... aArray ) {
    addList( src2list( aArray ) );
  }

  @Override
  public void addAll( ITsCollection<E> aList ) {
    addList( src2list( aList ) );
  }

  @Override
  public void addAll( Collection<E> aElemColl ) {
    addList( src2list( aElemColl ) );
  }

  @Override
  public void clear() {
    size = 0;
    ++changeCount;
  }

  @Override
  public int remove( E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    int index = indexOf( aElem );
    if( index >= 0 ) {
      removeByIndex( index );
    }
    return index;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public E removeByIndex( int aIndex ) {
    if( aIndex < 0 || aIndex >= size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, Integer.valueOf( aIndex ),
          Integer.valueOf( size - 1 ) );
    }
    E result = (E)items[aIndex];
    System.arraycopy( items, aIndex + 1, items, aIndex, size - aIndex - 1 );
    --size;
    items[size] = null; // освободим ссылку для сборки мусора
    ++changeCount;
    return result;
  }

  @Override
  public void removeRangeByIndex( int aIndex, int aCount ) {
    if( aIndex < 0 || aIndex >= size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, this.getClass().getSimpleName(),
          Integer.valueOf( aIndex ), Integer.valueOf( size - 1 ) );
    }
    if( aCount < 0 || aCount > (size - aIndex) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_REMOVING_ITEMS_COUNT_OUT_OF_RANGE,
          this.getClass().getSimpleName(), Integer.valueOf( aCount ), Integer.valueOf( size - aIndex - 1 ) );
    }
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
  // IListEdit
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public E set( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( aIndex < 0 || aIndex >= size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, this.getClass().getSimpleName(),
          Integer.valueOf( aIndex ), Integer.valueOf( size - 1 ) );
    }
    if( !allowDuplicates ) {
      TsItemAlreadyExistsRtException.checkTrue( hasElem( aElem ) );
    }
    E result = (E)items[aIndex];
    items[aIndex] = aElem;
    ++changeCount;
    return result;
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    if( aIndex < 0 || aIndex > size ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INDEX_OUT_OF_RANGE, this.getClass().getSimpleName(),
          Integer.valueOf( aIndex ), Integer.valueOf( size ) );
    }
    if( allowDuplicates || !hasElem( aElem ) ) {
      Object[] src = items;
      Object[] dest = items;
      if( size == items.length ) {
        dest = new Object[recommendNewLength()];
        System.arraycopy( src, 0, dest, 0, aIndex );
      }
      System.arraycopy( src, aIndex, dest, aIndex + 1, size - aIndex );
      dest[aIndex] = aElem;
      items = dest;
      ++size;
      ++changeCount;
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    insertList( aIndex, src2list( aArray ) );
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aColl ) {
    insertList( aIndex, src2list( aColl ) );
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aElemColl ) {
    insertList( aIndex, src2list( aElemColl ) );
  }

  @Override
  public void setAll( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    clear();
    if( aColl instanceof ITsFastIndexListTag ) {
      ITsFastIndexListTag<E> coll = (ITsFastIndexListTag<E>)aColl;
      for( int i = 0, n = coll.size(); i < n; i++ ) {
        add( coll.get( i ) );
      }
    }
    else {
      for( E e : aColl ) {
        add( e );
      }
    }
  }

  @Override
  public void setAll( Collection<E> aColl ) {
    TsErrorUtils.checkCollectionArg( aColl );
    clear();
    for( E e : aColl ) {
      add( e );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void setAll( E... aElems ) {
    TsErrorUtils.checkArrayArg( aElems );
    clear();
    for( E e : aElems ) {
      add( e );
    }
  }

  // --------------------------------------------------------------------------
  // Implementation-specific API
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
    if( aCapacity < 0 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_CAPACITY, Integer.valueOf( aCapacity ) );
    }
    if( aCapacity > items.length ) {
      int newLength = recommendNewLength();
      if( newLength < aCapacity ) {
        newLength = aCapacity;
      }
      Object[] newItems = new Object[newLength];
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
    items = TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
    ++changeCount;
  }

  /**
   * Returns reference to internal array.
   * <p>
   * Warning: changing array elements will change list content! This method is made public only for library developers.
   * The single prupose of method is optimization of some internal library routines.
   *
   * @return &lt;E&gt;[] - internall array of elements
   */
  @SuppressWarnings( "unchecked" )
  public E[] getInternalArray() {
    return (E[])items;
  }

  // --------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {

      int index = 0;

      private final int expectedChangeCount = changeCount;

      private void checkForConcurrentModification() {
        if( expectedChangeCount != changeCount ) {
          throw new ConcurrentModificationException();
        }
      }

      @Override
      public boolean hasNext() {
        checkForConcurrentModification();
        return index < size();
      }

      @Override
      public E next() {
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
