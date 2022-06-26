package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tslib.bricks.strid.coll.*;

public interface IVedScreen {

  IVedTool activeTool();

  void setActiveTool( IVedTool aTool );

  IStridablesList<IVedTool> tools();

  IVedScreenSelectionManager selectionManager();

}
