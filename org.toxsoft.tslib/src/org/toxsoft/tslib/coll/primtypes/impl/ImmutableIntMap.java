package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ImmutableMap;
import org.toxsoft.tslib.coll.primtypes.IIntList;
import org.toxsoft.tslib.coll.primtypes.IIntMapEdit;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IIntMapEdit}.
 * <p>
 * This implementation is designed for {@link IIntList#EMPTY} implementation and to allows users realize own typed empty
 * collections. Unlike {@link IIntList#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class ImmutableIntMap<E>
    extends ImmutableMap<Integer, E>
    implements IIntMapEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableIntMap() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IIntMap
  //

  @Override
  public boolean hasKey( int aKey ) {
    return false;
  }

  @Override
  public E findByKey( int aKey ) {
    return null;
  }

  @Override
  public IIntList keys() {
    return IIntList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // IIntMapEdit
  //

  @Override
  public E put( int aKey, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E removeByKey( int aKey ) {
    throw new TsNullObjectErrorRtException();
  }

}
