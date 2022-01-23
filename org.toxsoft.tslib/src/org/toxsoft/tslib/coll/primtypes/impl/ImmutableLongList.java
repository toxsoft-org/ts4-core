package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ImmutableList;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.coll.primtypes.ILongListEdit;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link ILongListEdit}.
 * <p>
 * This implementation is designed for {@link ILongList#EMPTY} implementation and to allows users realize own typed
 * empty collections. Unlike {@link ILongList#EMPTY} user constants will be types with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 */
public class ImmutableLongList
    extends ImmutableList<Long>
    implements ILongListEdit {

  private static final long serialVersionUID = 157157L;

  // ------------------------------------------------------------------------------------
  // ILongList
  //

  @Override
  public boolean hasValue( long aValue ) {
    return false;
  }

  @Override
  public int indexOfValue( long aValue ) {
    return -1;
  }

  @Override
  public long getValue( int aIndex ) {
    throw new TsIllegalArgumentRtException();
  }

  @Override
  public long[] toValuesArray() {
    return TsLibUtils.EMPTY_ARRAY_OF_LONGS;
  }

  // ------------------------------------------------------------------------------------
  // ILongListEdit
  //

  @Override
  public int add( long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int removeValue( long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long removeValueByIndex( int aIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long set( int aIndex, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insert( int aIndex, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

}
