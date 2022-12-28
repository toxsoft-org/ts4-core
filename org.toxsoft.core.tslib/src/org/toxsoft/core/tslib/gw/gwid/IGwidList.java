package org.toxsoft.core.tslib.gw.gwid;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * List of the {@link Gwid}s with som helper API added.
 * <p>
 * This is "read-only" interface with editable {@link GwidList} implementation.
 *
 * @author hazard157
 */
public interface IGwidList
    extends IList<Gwid> {

  /**
   * Singleton of the always empty list.
   * <p>
   * All reading methods returns as expected for an empty list, all mutator method throw an
   * {@link TsNullObjectErrorRtException}.
   */
  IGwidList EMPTY = new InternalEmptyGwidList();

  /**
   * Creates and returns list of class IDs of GWIDs in this list.
   * <p>
   * Returned list contains no duplicate elements.
   *
   * @return {@link IStringList} - list of class IDs of the SKIDs in thie list
   */
  IStringList listClassIds();

  /**
   * Returns the SKIDs of all objects in all concrete GWIDs in list.
   * <p>
   * Returned list does not contains duplicated values.
   *
   * @return {@link ISkidList} - SKIDs list
   */
  ISkidList objIds();

}

final class InternalEmptyGwidList
    extends ImmutableList<Gwid>
    implements IGwidList {

  private static final long serialVersionUID = -7078291937608633812L;

  /**
   * Method correctly deserializes {@link IGwidList#EMPTY} value.
   *
   * @return {@link ObjectStreamException} - {@link IGwidList#EMPTY}
   * @throws ObjectStreamException is declared but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IGwidList.EMPTY;
  }

  @Override
  public IStringList listClassIds() {
    return IStringList.EMPTY;
  }

  @Override
  public ISkidList objIds() {
    return ISkidList.EMPTY;
  }

}
