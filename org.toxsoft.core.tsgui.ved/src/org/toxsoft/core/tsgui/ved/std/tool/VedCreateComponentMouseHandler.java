package org.toxsoft.core.tsgui.ved.std.tool;

import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public class VedCreateComponentMouseHandler
    extends VedAbstractToolMouseHandler {

  private final IVedDataModel         dataModel;
  private final IVedComponentProvider compProvider;

  double startX;
  double startY;

  IVedComponent component = null;

  IVedDragExecutor createCompExecutor;

  VedCreateComponentMouseHandler( IVedDataModel aDataModel, IVedComponentProvider aCompProvider, double aMinWidth,
      double aMinHeight ) {
    dataModel = aDataModel;
    compProvider = aCompProvider;
    createCompExecutor = new VedCreateCompDragExecutor();
  }

  @Override
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    return createCompExecutor;
  }

  @Override
  protected IStridablesList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    return IStridablesList.EMPTY;
  }

  @Override
  protected void doDispose() {
    // nop
  }

}
