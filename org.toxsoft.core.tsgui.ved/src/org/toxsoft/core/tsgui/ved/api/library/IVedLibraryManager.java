package org.toxsoft.core.tsgui.ved.api.library;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED library manager.
 * <p>
 * VED libraries {@link IVedLibrary} must register themselfs with {@link #registerLibrary(IVedLibrary)}.
 *
 * @author hazard157
 */
public interface IVedLibraryManager {

  /**
   * Returns the registered libraries.
   *
   * @return {@link IStridablesList}&lt;{@link IVedLibrary}&gt; - the libraries list
   */
  IStridablesList<IVedLibrary> listLibs();

  /**
   * Registers the library in this manager and adda to {@link #listLibs()}.
   *
   * @param aLibrary {@link IVedLibrary} - library to be registered
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException librarye with the same ID is already registered
   */
  void registerLibrary( IVedLibrary aLibrary );

  /**
   * Finds the component provider.
   *
   * @param aLibraryId String - the ID of library to search the component in
   * @param aComponentKindId STring - compoenent kind ID
   * @return {@link IVedComponentProvider} - the component provider or <code>null</code> if none found
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IVedComponentProvider findProvider( String aLibraryId, String aComponentKindId );

}
