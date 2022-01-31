package org.toxsoft.core.tslib.coll.primtypes.impl;

import org.toxsoft.core.tslib.coll.impl.ImmutableMap;
import org.toxsoft.core.tslib.coll.primtypes.ILongList;
import org.toxsoft.core.tslib.coll.primtypes.ILongMapEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link ILongMapEdit}.
 * <p>
 * This implementation is designed for {@link ILongList#EMPTY} implementation and to allows users realize own typed
 * empty collections. Unlike {@link ILongList#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - the type of mapped values
 */
public class ImmutableLongMap<E>
    extends ImmutableMap<Long, E>
    implements ILongMapEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableLongMap() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ILongMap
  //

  @Override
  public boolean hasKey( long aKey ) {
    return false;
  }

  @Override
  public E findByKey( long aKey ) {
    return null;
  }

  @Override
  public ILongList keys() {
    return ILongList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // ILongMapEdit
  //

  @Override
  public E put( long aKey, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E removeByKey( long aKey ) {
    throw new TsNullObjectErrorRtException();
  }

}
