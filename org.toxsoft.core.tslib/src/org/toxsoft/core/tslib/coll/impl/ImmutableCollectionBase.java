package org.toxsoft.core.tslib.coll.impl;

import java.util.Collection;
import java.util.Iterator;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsCollectionEdit;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Basic implementation of immutable collection.
 * <p>
 * Overides {@link Object} methods.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class ImmutableCollectionBase<E>
    implements ITsCollectionEdit<E> {

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  final public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  final public boolean equals( Object obj ) {
    return obj == this;
  }

  @Override
  final public int hashCode() {
    return getClass().getSimpleName().hashCode();
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  final public Iterator<E> iterator() {
    return new EmptyIterator<>();
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  final public int size() {
    return 0;
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  final public boolean hasElem( E aElem ) {
    return false;
  }

  @Override
  final public E[] toArray( E[] aSrcArray ) {
    TsNullArgumentRtException.checkNull( aSrcArray );
    return aSrcArray;
  }

  @Override
  public Object[] toArray() {
    return TsLibUtils.EMPTY_ARRAY_OF_OBJECTS;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    throw new TsNullObjectErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollectionEdit
  //

  @Override
  final public int add( E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  final public void addAll( E... aArray ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public void addAll( ITsCollection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public void addAll( Collection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public int remove( E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public E removeByIndex( int aIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public void removeRangeByIndex( int aIndex, int aCount ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public void setAll( ITsCollection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  final public void setAll( Collection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  final public void setAll( E... aElems ) {
    throw new TsNullObjectErrorRtException();
  }

}
