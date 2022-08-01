package org.toxsoft.core.tsgui.ved.olds.std.tool;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.olds.std.tool.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.olds.drag.*;
import org.toxsoft.core.tsgui.ved.olds.std.comps.*;

/**
 * Инструмент "Указатель".
 * <p>
 * Является базовым инструментом, который позволяет выделять компонеты, перемещать их и изменять их размеры.
 *
 * @author vs
 */
public class VedPointerTool
    extends VedAbstractEditorTool {

  VedPointerToolMouseHandler mouseHandler;

  /**
   * Конструктор.<br>
   *
   * @param aContext ITsGuiContext - контекст
   */
  public VedPointerTool( ITsGuiContext aContext ) {
    super( "pointerTool", STR_N_TOOL_POINTER, STR_D_TOOL_POINTER, ICONID_POINTER, null, aContext ); //$NON-NLS-1$
    mouseHandler = new VedPointerToolMouseHandler( this, aContext.eclipseContext() );
  }

  @Override
  public IVedMouseHandler mouseHandler() {
    return mouseHandler;
  }

  @Override
  public boolean accept( IVedComponentView aView ) {
    return aView.component() instanceof VedStdCompRectangle;
  }

  @Override
  public void onZoomFactorChanged( double aZoomFactor ) {
    mouseHandler.onZoomFactorChanged( aZoomFactor );
  }
}
