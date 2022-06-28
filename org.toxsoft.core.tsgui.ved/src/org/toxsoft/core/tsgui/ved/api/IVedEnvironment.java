package org.toxsoft.core.tsgui.ved.api;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;

/**
 * VED environment is the entry point to the VED framework.
 *
 * @author hazard157
 */
public interface IVedEnvironment
    extends ITsGuiContextable {

  /**
   * Returns VED data model to be edited.
   *
   * @return {@link IVedDataModel} - the dtaa model
   */
  IVedDataModel dataModel();

  /**
   * Returns the screen manager - the view of the VED.
   *
   * @return {@link IVedScreenManager} - screens manager
   */
  IVedScreenManager screenManager();

  /**
   * Returns the library manager.
   *
   * @return {@link IVedLibraryManager} - the library manager
   */
  IVedLibraryManager libraryManager();

}
