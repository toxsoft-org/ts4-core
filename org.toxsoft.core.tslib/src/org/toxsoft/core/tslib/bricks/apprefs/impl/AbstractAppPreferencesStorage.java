package org.toxsoft.core.tslib.bricks.apprefs.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Basic implementation of the {@link IAppPreferences} storage.
 * <p>
 * Methods of implementing classes are caled solely from {@link AppPreferences} class.
 *
 * @author hazard157
 */
public abstract class AbstractAppPreferencesStorage {

  /**
   * Constructor for descendants.
   */
  protected AbstractAppPreferencesStorage() {
    // nop
  }

  /**
   * Lists IDs of bundles stored in this storage.
   *
   * @return {@link IStringList} - IDs of bundles (no particular order)
   */
  protected abstract IStringList listBundleIds();

  /**
   * Saves the bundle preferences values overwriteing the existing values.
   * <p>
   * Even an empty set must be stored permanently to indicate that the bundle with specified ID exists in storage.
   *
   * @param aBundleId String - the bundle ID, always an IDpath
   * @param aPrefs {@link IOptionSet} - preferences values may be an empty set but never is <code>null</code>
   */
  protected abstract void saveBundle( String aBundleId, IOptionSet aPrefs );

  /**
   * Loads values of preferences options from storage.
   * <p>
   * If There is no bundle with specified ID in the storage method must return <code>null</code>.
   *
   * @param aBundleId String - the bundle ID, always an IDpath
   * @return {@link IOptionSet} - the bundle prefences values or <code>null</code>
   */
  protected abstract IOptionSet loadBundle( String aBundleId );

  /**
   * Peremanently emoves bundle from storage.
   * <p>
   * Absent bundle ID must be silently ignored.
   *
   * @param aBundleId String - the bundle ID, always an IDpath
   */
  protected abstract void removeBundle( String aBundleId );

}
