package org.toxsoft.core.tslib.coll.primtypes;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Linear collection of <code>int</code> values.
 *
 * @author hazard157
 */
public interface IIntList
    extends IList<Integer> {

  /**
   * Singleton of always empty immutable list of <code>int</code><i>s</i>.
   */
  IIntListEdit EMPTY = new InternalNullIntList();

  /**
   * Determines if list contains specified vlaue.
   *
   * @param aValue int - element to search for
   * @return <b>true</b> - yes, this list contains specified element at least once;<br>
   *         <b>false</b> - no, this list does not contain the element.
   */
  boolean hasValue( int aValue );

  /**
   * Returns the index of the first occurrence of the specified element in this list.
   *
   * @param aValue int - element to search for
   * @return int - the index of the first occurrence of, or -1 if this list does not contain the element
   */
  int indexOfValue( int aValue );

  /**
   * Returns the element at the specified position in this list.
   *
   * @param aIndex int - index of the element to return in range 0..{@link #size()}-1
   * @return int - the element at the specified position in this list
   * @throws TsIllegalArgumentRtException index is out of range
   */
  int getValue( int aIndex );

  /**
   * Returns an array containing all of the elements in this list in proper sequence (from first to last element).
   *
   * @return int[] - an array containing all of the elements in this list in proper sequence
   */
  int[] toValuesArray();

  // ------------------------------------------------------------------------------------
  // Reimplement IList<Integer> methods
  //

  @Override
  default boolean hasElem( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return hasValue( aElem.intValue() );
  }

  @Override
  default int indexOf( Integer aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return indexOfValue( aElem.intValue() );
  }

  @Override
  default Integer get( int aIndex ) {
    return Integer.valueOf( getValue( aIndex ) );
  }

}

/**
 * Internal class for {@link IIntList#EMPTY} singleton implementation.
 *
 * @author hazard157
 */
class InternalNullIntList
    extends ImmutableIntList {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IIntList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IIntList.EMPTY;
  }

}
