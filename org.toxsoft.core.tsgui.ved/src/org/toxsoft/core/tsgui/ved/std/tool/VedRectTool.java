package org.toxsoft.core.tsgui.ved.std.tool;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.tool.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.std.comps.*;
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

  private final VedAbstractToolMouseHandler mouseHandler;

  /**
   * Конструктор.<br>
   *
   * @param aCompProvider IVedComponentProvider - поставщик компонент
   * @param aContext ITsGuiContext - контекст
   */
  public VedRectTool( IVedComponentProvider aCompProvider, ITsGuiContext aContext ) {
    super( "rectTool", STR_D_TOOL_RECT, STR_N_TOOL_RECT, ICONID_RECT, aCompProvider, aContext ); //$NON-NLS-1$
    mouseHandler = new VedRectToolMouseHandler( vedEnv(), aCompProvider );
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
    return mouseHandler;
  }

  @Override
  public boolean accept( IVedComponentView aView ) {
    return aView instanceof VedStdCompRectangle;
  }

}
