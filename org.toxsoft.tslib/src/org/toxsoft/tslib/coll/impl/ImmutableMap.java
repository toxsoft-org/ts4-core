package org.toxsoft.tslib.coll.impl;

import java.io.Serializable;

import org.toxsoft.tslib.coll.*;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IMapEdit}.
 * <p>
 * This implementation is designed for {@link IMap#EMPTY} implementation and to allows users realize own typed empty
 * collections. Unlike {@link IMap#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class ImmutableMap<K, E>
    extends ImmutableCollectionBase<E>
    implements IMapEdit<K, E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableMap() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( K aKey ) {
    return false;
  }

  @Override
  public E findByKey( K aKey ) {
    return null;
  }

  @Override
  public IList<K> keys() {
    return IList.EMPTY;
  }

  @Override
  public IList<E> values() {
    return IList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( K aKey, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E removeByKey( K aKey ) {
    throw new TsNullObjectErrorRtException();
  }

}
