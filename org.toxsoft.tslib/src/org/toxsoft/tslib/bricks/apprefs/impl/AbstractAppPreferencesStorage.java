package org.toxsoft.tslib.bricks.apprefs.impl;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.tslib.coll.primtypes.IStringList;

/**
 * Basic implementation of the {@link IAppPreferences} storage.
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

  protected abstract IStringList listBundleIds();

  protected abstract void saveBundle( String aBundleId, IOptionSet aParams );

  protected abstract IOptionSet loadBundle( String aBundleId );

  protected abstract void removeBundle( String aBundleId );
}
