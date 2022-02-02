package org.toxsoft.core.tslib.bricks.apprefs.impl;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;

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
