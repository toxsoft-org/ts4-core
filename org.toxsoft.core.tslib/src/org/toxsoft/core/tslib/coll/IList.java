package org.toxsoft.core.tslib.coll;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An ordered collection (sequence) of elements.
 * <p>
 * This is basic interface of all linear (non-associative) ordered collections.
 * <p>
 * This is readonly interface, ie interface without modificaion methods (mutators).
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IList<E>
    extends ITsCollection<E> {

  /**
   * Singleton of always empty uneditable (immutable) list.
   */
  @SuppressWarnings( "rawtypes" )
  IListEdit EMPTY = new InternalNullList();

  /**
   * Returns the index of the first occurrence of the specified element in this collection.
   * <p>
   * Equality check between specified element and collection elements is performed with {@link Object#equals(Object)}.
   *
   * @param aElem &lt;E&gt; -IList element to search for
   * @return int - the index of the first occurrence of the specified element in this collection
   * @throws TsNullArgumentRtException aElem = null
   */
  int indexOf( E aElem );

  /**
   * Returns the element at the specified position in this collection.
   *
   * @param aIndex int - index of the element to return (in range 0..{@link #size()}-1)
   * @return &lt;E&gt; - the element at the specified position in this list
   * @throws TsIllegalArgumentRtException the index is out of range
   */
  E get( int aIndex );

  // ------------------------------------------------------------------------------------
  // Convenience default methods
  //

  /**
   * Returns first element of collection.
   *
   * @return &lt;E&gt; - first element of collection or <code>null</code> for empty collection
   */
  default E first() {
    if( isEmpty() ) {
      return null;
    }
    return get( 0 );
  }

  /**
   * Returns last element of collection.
   *
   * @return &lt;E&gt; - last element of collection or <code>null</code> for empty collection
   */
  default E last() {
    int count = size();
    if( count > 0 ) {
      return get( count - 1 );
    }
    return null;
  }

  /**
   * Finds the element if list contains one element or returns <code>null</code> if none.
   *
   * @return &lt;E&gt; - the only element of collection or <code>null</code> if collection is empty or many elements
   */
  default E findOnly() {
    if( size() <= 1 ) {
      return first();
    }
    return null;
  }

  /**
   * Returns the element if list contains exactly one element.
   *
   * @return &lt;E&gt; - the only element of collection
   * @throws TsIllegalStateRtException collection contains no elements or 2 or more elements
   */
  default E getOnly() {
    if( size() == 1 ) {
      return first();
    }
    throw new TsIllegalStateRtException();
  }

  /**
   * Returns next element of specified element.
   * <p>
   * If no such element exists in this collection or specified element is the last one, returns <code>null</code>.
   *
   * @param aItem &lt;E&gt; - specified element
   * @return &lt;E&gt; - next element or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default E next( E aItem ) {
    int index = indexOf( aItem );
    if( index >= 0 && index < size() - 1 ) {
      return get( index + 1 );
    }
    return null;
  }

  /**
   * Returns previous element of specified element.
   * <p>
   * If no such element exists in this collection or specified element is the first one, returns <code>null</code>.
   *
   * @param aItem &lt;E&gt; - specified element
   * @return &lt;E&gt; - previous element or <code>null</code>
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  default E prev( E aItem ) {
    int index = indexOf( aItem );
    if( index > 0 ) {
      return get( index - 1 );
    }
    return null;
  }

  /**
   * Returns the new list with selected elements from this collection.
   *
   * @param aIndexes {@link IIntList} - indices of elements to be copied in new list
   * @return IListEdit&lt;E&gt; - list with selected elements
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any index is out of range
   */
  default IListEdit<E> fetch( IIntList aIndexes ) {
    TsNullArgumentRtException.checkNull( aIndexes );
    int count = aIndexes.size();
    if( count == 0 ) {
      return IList.EMPTY;
    }
    IListEdit<E> ll = new ElemArrayList<>( count );
    if( this instanceof ITsFastIndexListTag ) {
      for( int i = 0; i < count; i++ ) {
        ll.add( get( aIndexes.getValue( i ) ) );
      }
    }
    else {
      IIntList idxes = new SortedIntLinkedBundleList( aIndexes );
      // если запрошенные индексы недопустимы - выросим исключение
      if( idxes.getValue( count - 1 ) >= size() ) {
        throw new TsIllegalArgumentRtException();
      }
      int i = 0, j = 0;
      done:
      for( E e : this ) {
        while( i == idxes.getValue( j ) ) { // while вместо if на случай, если несколько одинаковых индексов подряд идут
          ll.add( e );
          if( ++j >= count ) {
            break done; // выход из внешнего цикла
          }
        }
        ++i;
      }
    }
    return ll;
  }

  /**
   * Returns the new list with selected elements from this collection.
   * <p>
   * Elements with indexes from aFromIndex to aToIndex-1 are copied in resulting list. If aFromIndex = aToIndex, than
   * resulting list is empty.
   *
   * @param aFromIndex int - index of first copiend element
   * @param aToIndex int - index of element after last copied element
   * @return IList&lt;E&gt; - list with selected elements
   * @throws TsIllegalArgumentRtException any index is out of range
   * @throws TsIllegalArgumentRtException aFromIndex &gt; aToIndex
   */
  default IList<E> fetch( int aFromIndex, int aToIndex ) {
    TsIllegalArgumentRtException.checkTrue( aFromIndex < 0 || aToIndex > size() || aFromIndex > aToIndex );
    int count = aToIndex - aFromIndex;
    if( count == 0 ) {
      return IList.EMPTY;
    }
    IListEdit<E> ll = new ElemArrayList<>( count );
    if( this instanceof ITsFastIndexListTag ) {
      for( int i = aFromIndex; i <= aToIndex; i++ ) {
        ll.add( get( i ) );
      }
    }
    else {
      int i = 0;
      for( E e : this ) {
        if( i++ >= aFromIndex ) {
          ll.add( e );
        }
        else {
          if( i >= aToIndex ) {
            break;
          }
        }
      }
    }
    return ll;
  }

  /**
   * Determines if specified element is first in ordered collection.
   * <p>
   * <code>null</code> is considered as first element only in empty collection.
   *
   * @param aItem &lt;E&gt; - specified element, may be <code>null</code>
   * @return boolean - <code>true</code> if element is first in collection
   */
  default boolean isFirst( E aItem ) {
    return Objects.equals( aItem, first() );
  }

  /**
   * Determines if specified element is last in ordered collection.
   * <p>
   * <code>null</code> is considered as last element only in empty collection.
   *
   * @param aItem &lt;E&gt; - specified element, may be <code>null</code>
   * @return boolean - <code>true</code> if element is last in collection
   */
  default boolean isLast( E aItem ) {
    return Objects.equals( aItem, last() );
  }

  /**
   * Determines if specified index is in allowed range to access this list's elements.
   * <p>
   * For empty list returns <code>false</code>.
   *
   * @param aIndex int - specified index
   * @return boolean - <code>true</code> if index is in range 0 .. {@link #size()}-1
   */
  default boolean isInRange( int aIndex ) {
    return aIndex >= 0 && aIndex < size();
  }

  /**
   * Copies content to the destination list or creates new one if needed.
   * <p>
   * If argument is <code>null</code> then new {@link ElemArrayList} instance is created,
   *
   * @param aDest {@link IListBasicEdit}&ltE&gt; - destination list or <code>null</code>
   * @return {@link IListBasicEdit} - the argument or new list if argument was <code>null</code>
   */
  default IListBasicEdit<E> copyTo( IListBasicEdit<E> aDest ) {
    IListBasicEdit<E> dest = aDest;
    if( dest == null ) {
      dest = new ElemArrayList<>( size() );
    }
    dest.addAll( this );
    return dest;
  }

}

/**
 * Internal class for {@link IList#EMPTY} singleton implementation.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
class InternalNullList<E>
    extends ImmutableList<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IList.EMPTY;
  }

  // Object methods are implemented in parent class

}
