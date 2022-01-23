package org.toxsoft.tslib.coll.primtypes.impl;

import org.toxsoft.tslib.coll.impl.ImmutableList;
import org.toxsoft.tslib.coll.primtypes.IIntList;
import org.toxsoft.tslib.coll.primtypes.IIntListEdit;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IIntListEdit}.
 * <p>
 * This implementation is designed for {@link IIntList#EMPTY} implementation and to allows user realize own typed empty
 * collections. Unlike {@link IIntList#EMPTY} user constants will be typed with respective Java types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 */
public class ImmutableIntList
    extends ImmutableList<Integer>
    implements IIntListEdit {

  private static final long serialVersionUID = 157157L;

  // ------------------------------------------------------------------------------------
  // IIntList
  //

  /**
   *
   */

  @Override
  public boolean hasValue( int aValue ) {
    return false;
  }

  @Override
  public int indexOfValue( int aValue ) {
    return -1;
  }

  @Override
  public int getValue( int aIndex ) {
    throw new TsIllegalArgumentRtException();
  }

  @Override
  public int[] toValuesArray() {
    return TsLibUtils.EMPTY_ARRAY_OF_INTS;
  }

  // ------------------------------------------------------------------------------------
  // IIntListEdit
  //

  @Override
  public int add( int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int removeValue( int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int removeValueByIndex( int aIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int set( int aIndex, int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insert( int aIndex, int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

}
