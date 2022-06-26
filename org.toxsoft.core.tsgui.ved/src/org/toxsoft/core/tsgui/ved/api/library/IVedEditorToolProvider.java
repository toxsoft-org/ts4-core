package org.toxsoft.core.tsgui.ved.api.library;

import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Editor tool factory.
 *
 * @author hazard157
 */
public interface IVedEditorToolProvider
    extends IStridableParameterized {

  // FIXME API
  IVedEditorTool createToole( /* arguments??? */ );

}
