package org.toxsoft.core.tslib.bricks.apprefs.impl;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

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
  public IPrefBundle defineBundle( String aBundleId, String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    PrefBundle bundle = findBundle( aBundleId );
    boolean needToSaveBundle = false;
    if( bundle == null ) {
      bundle = new PrefBundle( aBundleId, IOptionSet.NULL, storage );
      bundlesMap.put( aBundleId, bundle );
      needToSaveBundle = true;
    }
    bundle.setNameAndDescription( aName, aDescription ); // обновляем даже у существующей связки
    if( needToSaveBundle ) {
      storage.saveBundle( aBundleId, bundle.params );
    }
    return bundle;
  }

  @Override
  public PrefBundle findBundle( String aBundleId ) {
    PrefBundle bundle = bundlesMap.findByKey( aBundleId );
    if( bundle != null ) {
      return bundle;
    }
    IOptionSet params = storage.loadBundle( aBundleId );
    if( params != null ) {
      bundle = new PrefBundle( aBundleId, params, storage );
      bundlesMap.put( aBundleId, bundle );
      storage.saveBundle( aBundleId, bundle.params );
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
    bundle = new PrefBundle( aBundleId, IOptionSet.NULL, storage );
    bundlesMap.put( aBundleId, bundle );
    storage.saveBundle( aBundleId, bundle.params );
    return bundle;
  }

  @Override
  public void removeBundle( String aBundleId ) {
    bundlesMap.removeByKey( aBundleId );
    storage.removeBundle( aBundleId );
  }

}
