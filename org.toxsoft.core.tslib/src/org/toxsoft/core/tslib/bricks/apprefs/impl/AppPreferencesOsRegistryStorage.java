package org.toxsoft.core.tslib.bricks.apprefs.impl;

import static org.toxsoft.core.tslib.bricks.apprefs.impl.ITsResources.*;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.wrappers.StringListArrayWrapper;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * App preferences storage to OS registry using {@link Preferences}.
 *
 * @author hazard157
 */
public class AppPreferencesOsRegistryStorage
    extends AbstractAppPreferencesStorage {

  private final String rootNodePath;

  /**
   * Constructor.
   * <p>
   * Argument is path to root as in {@link Preferences#absolutePath()}. More detailed discussion may be found at
   * {@link Preferences}. Typical value of the argument is <code>"/ru.toxsoft/appname"</code>.
   *
   * @param aRootPath String - absolute path to the application node in OS registry
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid path (empty or not starts with slash '/')
   */
  public AppPreferencesOsRegistryStorage( String aRootPath ) {
    TsErrorUtils.checkNonBlank( aRootPath );
    TsIllegalArgumentRtException.checkTrue( aRootPath.charAt( 0 ) != '/', FMT_ERR_NON_ROOT_PREFS_PATH, aRootPath );
    rootNodePath = aRootPath;
  }

  // ------------------------------------------------------------------------------------
  // AbstractAppPreferencesStorage
  //

  @Override
  protected IStringList listBundleIds() {
    try {
      // get app prefs node (or create if needed)
      Preferences prefsNode = Preferences.userRoot().node( rootNodePath );
      return new StringListArrayWrapper( prefsNode.keys() );
    }
    catch( BackingStoreException ex ) {
      LoggerUtils.errorLogger().error( ex );
      throw new TsIllegalStateRtException( ex );
    }
  }

  @Override
  protected void saveBundle( String aBundleId, IOptionSet aParams ) {
    Preferences prefsNode = Preferences.userRoot().node( rootNodePath );
    String s = OptionSetKeeper.KEEPER_INDENTED.ent2str( aParams );
    prefsNode.put( aBundleId, s );
  }

  @Override
  protected IOptionSet loadBundle( String aBundleId ) {
    Preferences prefsNode = Preferences.userRoot().node( rootNodePath );
    String s = prefsNode.get( aBundleId, OptionSetKeeper.STR_EMPTY_OPSET_REPRESENTATION );
    return OptionSetKeeper.KEEPER.str2ent( s );
  }

  @Override
  protected void removeBundle( String aBundleId ) {
    Preferences prefsNode = Preferences.userRoot().node( rootNodePath );
    prefsNode.remove( aBundleId );
  }

}
