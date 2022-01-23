package org.toxsoft.tslib.coll.impl;

import java.io.Serializable;
import java.util.Collection;

import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.basis.ITsCollection;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IListEdit}.
 * <p>
 * This implementation is designed for {@link IList#EMPTY} implementation and to allows users realize own typed empty
 * collections. Unlike {@link IList#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - the type of elements in this collection
 */
public class ImmutableList<E>
    extends ImmutableCollectionBase<E>
    implements IListEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableList() {
    // nop
  }

  @Override
  public void clear() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insert( int aIndex, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void insertAll( int aIndex, E... aArray ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insertAll( int aIndex, ITsCollection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insertAll( int aIndex, Collection<E> aColl ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E set( int aIndex, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E get( int aIndex ) {
    throw new TsIllegalArgumentRtException();
  }

  @Override
  public int indexOf( E aElem ) {
    return -1;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

}
