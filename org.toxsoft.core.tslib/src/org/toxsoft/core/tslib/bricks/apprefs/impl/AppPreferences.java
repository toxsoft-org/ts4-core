package org.toxsoft.core.tslib.bricks.apprefs.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of the {@link IAppPreferences}.
 *
 * @author hazard157
 */
public class AppPreferences
    implements IAppPreferences {

  private final AbstractAppPreferencesStorage storage;
  private final IStringMapEdit<PrefBundle>    bundlesMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aStorage {@link AbstractAppPreferencesStorage} - storage backend
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public AppPreferences( AbstractAppPreferencesStorage aStorage ) {
    TsNullArgumentRtException.checkNull( aStorage );
    storage = aStorage;
  }

  // ------------------------------------------------------------------------------------
  // IAppPreferences
  //

  @Override
  public IStringList listPrefBundleIds() {
    return storage.listBundleIds();
  }

  @Override
  public IPrefBundle defineBundle( String aBundleId, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    PrefBundle bundle = findBundle( aBundleId );
    boolean needToSaveBundle = false;
    if( bundle == null ) {
      bundle = new PrefBundle( aBundleId, aParams, IOptionSet.NULL, storage );
      bundlesMap.put( aBundleId, bundle );
      needToSaveBundle = true;
    }
    else {
      bundle.params().setAll( aParams ); // update params of existing bundle
    }
    if( needToSaveBundle ) {
      storage.saveBundle( aBundleId, bundle.prefsValues );
    }
    return bundle;
  }

  @Override
  public PrefBundle findBundle( String aBundleId ) {
    PrefBundle bundle = bundlesMap.findByKey( aBundleId );
    if( bundle != null ) {
      return bundle;
    }
    IOptionSet prefs = storage.loadBundle( aBundleId );
    if( prefs != null ) {
      bundle = new PrefBundle( aBundleId, IOptionSet.NULL, prefs, storage );
      bundlesMap.put( aBundleId, bundle );
      storage.saveBundle( aBundleId, bundle.prefsValues );
      return bundle;
    }
    return null;
  }

  @Override
  public PrefBundle getBundle( String aBundleId ) {
    PrefBundle bundle = findBundle( aBundleId );
    if( bundle != null ) {
      return bundle;
    }
    StridUtils.checkValidIdPath( aBundleId );
    bundle = new PrefBundle( aBundleId, IOptionSet.NULL, IOptionSet.NULL, storage );
    bundlesMap.put( aBundleId, bundle );
    storage.saveBundle( aBundleId, bundle.prefsValues );
    return bundle;
  }

  @Override
  public void removeBundle( String aBundleId ) {
    bundlesMap.removeByKey( aBundleId );
    storage.removeBundle( aBundleId );
  }

}
