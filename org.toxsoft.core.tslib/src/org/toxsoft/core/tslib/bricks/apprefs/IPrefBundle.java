package org.toxsoft.core.tslib.bricks.apprefs;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * The bundle of related parameters used as application preferences.
 * <p>
 * Preference values {@link #prefs()} are saved in persistent storage.
 * <p>
 * Parameters are defined at creation {@link IAppPreferences#defineBundle(String, IOptionSet)} and are <b>not</b>
 * stored. Parameters are used by GUI editing tools and does not affect on the preferences.
 *
 * @author hazard157
 */
public interface IPrefBundle
    extends IStridable, IIconIdable {

  /**
   * Returns the editable preferences stored in this bundle.
   *
   * @return {@link INotifierOptionSetEdit} - preference options values
   */
  INotifierOptionSetEdit prefs();

  /**
   * Return all known definition of the preference options.
   * <p>
   * Note that parameter definitions is <b>not</b> stored by the storage backend.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of known definitions
   */
  IStridablesList<IDataDef> listKnownOptions();

  /**
   * Defines the preference option as a known one.
   *
   * @param aOptionInfo {@link IDataDef} - option definition
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException definition with the same {@link IDataDef#id()} already exists
   */
  void defineOption( IDataDef aOptionInfo );

  /**
   * Removes option definition.
   * <p>
   * If there is no option with specified ID, then method does nothing.
   *
   * @param aOptionId String - the option ID
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void undefineOption( String aOptionId );

}
