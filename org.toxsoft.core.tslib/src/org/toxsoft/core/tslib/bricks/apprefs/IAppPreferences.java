package org.toxsoft.core.tslib.bricks.apprefs;

import java.util.prefs.Preferences;

import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Application preferences.
 * <p>
 * Application preferences are grouped in identifyed bundles {@link IPrefBundle}.
 * <p>
 * Preferences may be stored using different backends: system registry ({@link Preferences}), text file
 * (<code>CONFIG.INI</code>), etc.
 *
 * @author hazard157
 */
public interface IAppPreferences {

  /**
   * Lists IDs of all existing bundles.
   *
   * @return {@link IStringList} - existing bundle IDs list
   */
  IStringList listPrefBundleIds();

  /**
   * Creates unexisting or edits existing bundle.
   *
   * @param aBundleId String - bundle ID
   * @param aName String - bundle name {@link IPrefBundle#nmName()}
   * @param aDescription String - bundle description {@link IPrefBundle#description()}
   * @return {@link IPrefBundle} - new or existing bundle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException bundle ID is not a valid IDpath
   */
  IPrefBundle defineBundle( String aBundleId, String aName, String aDescription );

  /**
   * Returns an existing bundle or <code>null</code> if none exists.
   *
   * @param aBundleId String - bundle ID
   * @return {@link IPrefBundle} - an existing bundle or <code>null</code> if none exists
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  IPrefBundle findBundle( String aBundleId );

  /**
   * Returns existing bundle or creates if it does not exists.
   *
   * @param aBundleId String - bundle ID (IDpath)
   * @return {@link IPrefBundle} - existing or newly created bundle
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  IPrefBundle getBundle( String aBundleId );

  /**
   * Removes bundle.
   * <p>
   * If bundle does not exists then method does nothing.
   *
   * @param aBundleId String - bundle ID
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void removeBundle( String aBundleId );

}
