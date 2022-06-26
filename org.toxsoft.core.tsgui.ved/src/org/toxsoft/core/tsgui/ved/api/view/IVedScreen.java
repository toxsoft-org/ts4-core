package org.toxsoft.core.tsgui.ved.api.view;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * VED screen is interactive visualization of {@link IVedDataModel}.
 * <p>
 * The may be several screens displaying the same data model with different zoom factor and/or vieweport.
 *
 * @author hazard157
 */
public interface IVedScreen {

  // TODO API ???

  IVedEditorTool activeTool();

  void setActiveTool( IVedEditorTool aTool );

  IStridablesList<IVedEditorTool> tools();

  IVedSelectedComponentManager selectionManager();

}
