package org.toxsoft.core.tslib.bricks.strid.coll.impl;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ImmutableList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Empty immutable implementation of {@link IStridablesListEdit}.
 * <p>
 * This implementation is designed for {@link IStridablesList#EMPTY} implementation and to allows users realize own
 * typed empty collections. Unlike {@link IStridablesList#EMPTY} user constants will be types with respective Java
 * types.
 * <p>
 * All reading methods calls are allowed while all editing methods throw {@link TsNullObjectErrorRtException}.
 *
 * @author hazard157
 * @param <E> - concrete type of {@link IStridable} elements
 */
public class ImmutableStridablesList<E extends IStridable>
    extends ImmutableList<E>
    implements IStridablesListEdit<E> {

  private static final long serialVersionUID = 157157L;

  /**
   * Constructor.
   */
  public ImmutableStridablesList() {
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
    return ids();
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

  // ------------------------------------------------------------------------------------
  // IStridablesList
  //

  @Override
  public IStringList ids() {
    return IStringList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // IStridablesListBasicEdit
  //

  @Override
  public int put( E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E replace( String aId, E aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public E removeById( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setAll( IStridablesList<E> aSrc ) {
    throw new TsNullObjectErrorRtException();
  }

}
