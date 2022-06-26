package org.toxsoft.core.tsgui.ved.api.library;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The library of VED components and tools.
 *
 * @author hazard157
 */
public interface IVedLibrary
    extends IStridableParameterized {

  /**
   * Returns the compnents providers conatained in this library.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentProvider}&gt; - contained component providers
   */
  IStridablesList<IVedComponentProvider> componentProviders();

  /**
   * Returns the editor tools providers conatained in this library.
   *
   * @return {@link IStridablesList}&lt;{@link IVedEditorToolProvider}&gt; - contained component providers
   */
  IStridablesList<IVedEditorToolProvider> toolProviders();

}
