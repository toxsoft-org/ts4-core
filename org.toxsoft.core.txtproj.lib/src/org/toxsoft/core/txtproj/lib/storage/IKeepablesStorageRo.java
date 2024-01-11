package org.toxsoft.core.txtproj.lib.storage;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * Read-only methods of the {@link IKeepablesStorage} interface.
 *
 * @author hazard157
 */
public interface IKeepablesStorageRo
    extends Iterable<TdfSection> {

  /**
   * Determines if the storage contains the section with specified ID.
   *
   * @param aId String - the section ID
   * @return boolean - <code>true</code> if the section with specified ID exists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean hasSection( String aId );

  // TODO TRANSLATE

  /**
   * Read the entity from the section.
   * <p>
   * If no such section was found then returns <code>aDefault</code> value.
   * <p>
   * This method reads section written by the method {@link IKeepablesStorage#writeItem(String, Object, IEntityKeeper)}.
   *
   * @param <T> - expected type of the entities
   * @param aId String - the ID of the section to read
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @param aDefault &lt;T&gt - value, returned when the section not found, may be <code>null</code>
   * @return &lt;T&gt; - read entity of <code>aDefault</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIoRtException read I/O error
   * @throws StrioRtException invalid storage format
   */
  <T> T readItem( String aId, IEntityKeeper<T> aKeeper, T aDefault );

  /**
   * Read the linear collection of an entities as a list.
   * <p>
   * If no such section was found then returns an empty list.
   * <p>
   * This method reads section written by the method
   * {@link IKeepablesStorage#writeColl(String, ITsCollection, IEntityKeeper)}.
   *
   * @param <T> - expected type of the entities
   * @param aId String - the ID of the section to read
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @return {@link IList}&lt;T&gt - collection read as a list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIoRtException read I/O error
   * @throws StrioRtException invalid storage format
   */
  <T> IList<T> readColl( String aId, IEntityKeeper<T> aKeeper );

  /**
   * Reads the map of the entities where keys are an IDpaths.
   * <p>
   * If no such section was found then returns an empty map.
   * <p>
   * This method reads section written by the method
   * {@link IKeepablesStorage#writeStridMap(String, IStringMap, IEntityKeeper)}.
   *
   * @param <T> - expected type of the entities
   * @param aId String - the ID of the section to read
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @return {@link IStringMap}&lt;T&gt; - the read map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIoRtException read I/O error
   * @throws StrioRtException invalid storage format
   */
  <T> IStringMap<T> readStridMap( String aId, IEntityKeeper<T> aKeeper );

}
