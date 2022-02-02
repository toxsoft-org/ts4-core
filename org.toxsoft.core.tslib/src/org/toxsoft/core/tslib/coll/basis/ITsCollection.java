package org.toxsoft.core.tslib.coll.basis;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Basic read-only interface of all <code>tslib</code> collections.
 * <p>
 * All tslib collections are forbidden to hold <code>null</code> as elements.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface ITsCollection<E>
    extends Iterable<E>, ITsCountableCollection {

  /**
   * Determines if this collection contains specified element.
   * <p>
   * More formally, returns <code>true</code> if and only if this collection contains at least one element
   * <code>el</code> such that <code>el.equals(aElem)</code>.
   *
   * @param aElem &lt;E&gt; - element whose presence in this collection is to be tested
   * @return <b>true</b> - this collection contains the specified element;<br>
   *         <b>false</b> - this collection does not conatains the specified element.
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  boolean hasElem( E aElem );

  /**
   * Returns an array containing all of the elements in this collection.
   * <p>
   * If specified array is big enough it is returned, otherwise, a new array of the same runtime type is allocated for
   * this purpose.
   *
   * @param aSrcArray E[] - array to store elements of this collection
   * @return E[] - an array containing all of the elements in this collection
   * @throws TsNullArgumentRtException aSrcArray = null
   */
  E[] toArray( E[] aSrcArray );

  /**
   * Returns an array containing all of the elements in this collection.
   * <p>
   * Method allocates new array excpet when collection is empty.
   *
   * @return Object[] - an array containing all of the elements in this collection
   */
  Object[] toArray();

}
