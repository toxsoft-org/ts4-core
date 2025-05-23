package org.toxsoft.core.tslib.gw.skid;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * List of the {@link Skid}s with some helper API added.
 * <p>
 * This is "read-only" interface with editable {@link SkidList} implementation.
 *
 * @author hazard157
 */
public interface ISkidList
    extends IList<Skid> {

  /**
   * Singleton of the always empty list.
   * <p>
   * All reading methods returns as expected for an empty list, all mutator method throw an
   * {@link TsNullObjectErrorRtException}.
   */
  ISkidList EMPTY = new InternalEmptySkidList();

  /**
   * Creates and returns list of class IDs of SKIDs in this list.
   * <p>
   * Returned list contains no duplicate elements.
   * <p>
   * Each time the result is calculated so for big collection it may take a while,
   *
   * @return {@link IStringList} - sorted list of class IDs of the SKIDs in this list
   */
  IStringList classIds();

  /**
   * Creates and returns object SKIDs of the specified class.
   * <p>
   * If no SKID in the list is of the specified class, then returns and empty list.
   * <p>
   * Each time the result is calculated so for big collection it may take a while,
   *
   * @param aClassId String - the class ID
   * @return {@link IList}&lt;{@link Skid}&gt; - SKIDs of the specified class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<Skid> listSkidsOfClass( String aClassId );

  /**
   * Creates and returns object STRIDs of the specified class.
   * <p>
   * If no SKID in the list is of the specified class, then returns and empty list.
   * <p>
   * Each time the result is calculated so for big collection it may take a while,
   *
   * @param aClassId String - the class ID
   * @return {@link IStringList} - STRIDs of SKIDs of the specified class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IStringList listStridsOfClass( String aClassId );

  /**
   * Finds first duplicate SKID in the list.
   * <p>
   * If there is multiple duplicate SKIDs in this list method returns first duplicate SKID.
   * <p>
   * Each time the result is calculated so for big collection it may take a while,
   *
   * @return {@link Skid} - first of duplicate SKIDs or <code>null</code> if there is no duplicates
   */
  Skid findDuplicateSkid();

}

final class InternalEmptySkidList
    extends ImmutableList<Skid>
    implements ISkidList {

  private static final long serialVersionUID = 6959327702105284490L;

  /**
   * Method correctly deserializes {@link ISkidList#EMPTY} value.
   *
   * @return {@link ObjectStreamException} - {@link ISkidList#EMPTY}
   * @throws ObjectStreamException is declared but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ISkidList.EMPTY;
  }

  @Override
  public IStringList classIds() {
    return IStringList.EMPTY;
  }

  @Override
  public IList<Skid> listSkidsOfClass( String aClassId ) {
    TsNullArgumentRtException.checkNull( aClassId );
    return IList.EMPTY;
  }

  @Override
  public IStringList listStridsOfClass( String aClassId ) {
    TsNullArgumentRtException.checkNull( aClassId );
    return IStringList.EMPTY;
  }

  @Override
  public Skid findDuplicateSkid() {
    return null;
  }

}
