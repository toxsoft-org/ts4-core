package org.toxsoft.core.tslib.coll.primtypes;

import java.io.ObjectStreamException;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsCollectionEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.ImmutableLongList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Linear collection of <code>long</code> values.
 * <p>
 * While this list of <code>long</code>s extends {@link IList} with &lt;{@link Long}&gt; generic type, editable
 * extensions {@link ILongListBasicEdit} and {@link ILongListEdit} do <b>not</b> extend
 * {@link ITsCollectionEdit}&lt{@link Long}&gt;.
 *
 * @author hazard157
 */
public interface ILongList
    extends IList<Long> {

  /**
   * Singleton of always empty immutable list of <code>long</code><i>s</i>.
   */
  ILongListEdit EMPTY = new InternalNullLongList();

  /**
   * Determines if list contains specified vlaue.
   *
   * @param aValue long - element to search for
   * @return <b>true</b> - yes, this list contains specified element at least once;<br>
   *         <b>false</b> - no, this list does not contain the element.
   */
  boolean hasValue( long aValue );

  /**
   * Returns the index of the first occurrence of the specified element in this list.
   *
   * @param aValue long - element to search for
   * @return int - the index of the first occurrence of, or -1 if this list does not contain the element
   */
  int indexOfValue( long aValue );

  /**
   * Returns the element at the specified position in this list.
   *
   * @param aIndex int - index of the element to return in range 0..{@link #size()}-1
   * @return long - the element at the specified position in this list
   * @throws TsIllegalArgumentRtException index is out of range
   */
  long getValue( int aIndex );

  /**
   * Returns an array containing all of the elements in this list in proper sequence (from first to last element).
   *
   * @return long[] - an array containing all of the elements in this list in proper sequence
   */
  long[] toValuesArray();

  // ------------------------------------------------------------------------------------
  // Reimplement IList<Long> methods
  //

  @Override
  default boolean hasElem( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.longValue() );
  }

  @Override
  default int indexOf( Long aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.longValue() );
  }

  @Override
  default Long get( int aIndex ) {
    return Long.valueOf( getValue( aIndex ) );
  }

}

/**
 * Internal class for {@link ILongList#EMPTY} singleton implementation.
 *
 * @author hazard157
 */
class InternalNullLongList
    extends ImmutableLongList {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ILongList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ILongList.EMPTY;
  }

}
