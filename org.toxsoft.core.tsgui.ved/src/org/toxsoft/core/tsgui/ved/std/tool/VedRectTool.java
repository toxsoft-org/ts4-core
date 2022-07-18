package org.toxsoft.core.tsgui.ved.std.tool;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.tool.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;

/**
 * Инструмент "Прямоугольник".
 * <p>
 * Инструмент, который позволяет создавать и редактировать прямоугольники.
 *
 * @author vs
 */
public class VedRectTool
    extends VedAbstractEditorTool {

  /**
   * Конструктор.<br>
   *
   * @param aContext ITsGuiContext - контекст
   */
  public VedRectTool( ITsGuiContext aContext ) {
    super( "rectTool", STR_D_TOOL_RECT, STR_N_TOOL_RECT, ICONID_RECT, aContext ); //$NON-NLS-1$
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // {@link VedAbstractEditorTool}
  //

  @Override
  public void onZoomFactorChanged( double aZoomFactor ) {
    // TODO Auto-generated method stub

  }

  @Override
  public IVedMouseHandler mouseHandler() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean accept( IVedComponentView aView ) {
    // TODO Auto-generated method stub
    return false;
  }

}
