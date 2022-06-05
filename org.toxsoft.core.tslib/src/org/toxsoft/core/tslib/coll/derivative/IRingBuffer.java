package org.toxsoft.core.tslib.coll.derivative;

import org.toxsoft.core.tslib.coll.basis.*;

/**
 * The ring (circular) buffer.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public interface IRingBuffer<E>
    extends ITsCountableCollection, ITsSizeRestrictableCollection, ITsClearable {

  void put( E aElem );

  E get();

  E getOrNull();

  boolean isFull();

}
