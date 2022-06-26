package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public interface IVedLibrary
    extends IStridableParameterized {

  IStridablesList<IVedComponentProvider> componentProviders();

  IStridablesList<IVedToolProvider> toolProviders();

}
