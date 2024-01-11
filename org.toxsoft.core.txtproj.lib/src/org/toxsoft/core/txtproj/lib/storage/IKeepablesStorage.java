package org.toxsoft.core.txtproj.lib.storage;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * Storage of entities serializable using {@link IEntityKeeper}.
 * <p>
 * Different implementations are using different storages like single file, files in directory, database, etc.
 *
 * @author hazard157
 */
public interface IKeepablesStorage
    extends IKeepablesStorageRo, ITsClearable {

  /**
   * Writes the entity to the dedicated section of the storage.
   *
   * @param <T> - expected type of the entities
   * @param aId String - unique identifier (an IDpath) of the section within the storage
   * @param aItem &lt;T&gt; - the entity to write
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIoRtException writing I/O error
   */
  <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper );

  /**
   * Writes a collection of entities.
   *
   * @param <T> - expected type of the entities
   * @param aId String - unique identifier (an IDpath) of the section within the storage
   * @param aColl {@link ITsCollection}&lt;T&gt; - collection to write
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIoRtException writing I/O error
   */
  <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper );

  /**
   * Writes string map ofenetities where keys are an IDnames.
   *
   * @param <T> - expected type of the entities
   * @param aId String - unique identifier (an IDpath) of the section within the storage
   * @param aMap {@link IStringMap}&lt;T&gt; - the map of entities
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException writing I/O error
   */
  <T> void writeStridMap( String aId, IStringMap<T> aMap, IEntityKeeper<T> aKeeper );

  /**
   * Writes (creates new or overwrites existing) section.
   *
   * @param aSection {@link TdfSection} - the section to write
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException writing I/O error
   */
  void writeSection( TdfSection aSection );

  /**
   * Permanently removes the section from the storage.
   * <p>
   * Does nothing if the section is not found in storage.
   *
   * @param aId String - ID of the section to be removed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException writing I/O error
   */
  void removeSection( String aId );

  /**
   * Copies content from source storage to this storage.
   *
   * @param aSource {@link IKeepablesStorageRo} - the source storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException writing I/O error
   */
  void copyFrom( IKeepablesStorageRo aSource );

}
