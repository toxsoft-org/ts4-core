package org.toxsoft.core.tslib.coll.primtypes.impl;

import java.io.Serializable;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ImmutableCollectionBase;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IStringMapEdit}.
 * <p>
 * This implementation is designed for {@link IStringMap#EMPTY} implementation and to allows users realize own typed
 * empty collections. Unlike {@link IStringMap#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class ImmutableStringMap<E>
    extends ImmutableCollectionBase<E>
    implements IStringMapEdit<E>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableStringMap() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    return false;
  }

  @Override
  public E findByKey( String aKey ) {
    return null;
  }

  @Override
  public IStringList keys() {
    return IStringList.EMPTY;
  }

  @Override
  public IList<E> values() {
    return IList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // IMapEdit
  //

  @Override
  public E put( String aKey, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E removeByKey( String aKey ) {
    throw new TsNullObjectErrorRtException();
  }

}
