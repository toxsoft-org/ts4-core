package org.toxsoft.core.tsgui.ved.core.library;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The library of VED components.
 *
 * @author hazard157
 */
public interface IVedLibrary
    extends IStridableParameterized, IIconIdable {

  /**
   * Returns the compnents providers conatained in this library.
   *
   * @return {@link IStridablesList}&lt;{@link IVedComponentProvider}&gt; - contained component providers
   */
  IStridablesList<IVedComponentProvider> componentProviders();

}
