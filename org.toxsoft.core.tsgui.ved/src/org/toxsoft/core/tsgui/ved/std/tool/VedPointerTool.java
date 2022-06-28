package org.toxsoft.core.tsgui.ved.std.tool;

import static org.toxsoft.core.tsgui.ved.std.tool.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.impl.*;

/**
 * Инструмент "Указатель".
 * <p>
 * Является базовым инструментом, который позволяет выделять компонеты, перемещать их и изменять их размеры.
 *
 * @author vs
 */
public class VedPointerTool
    extends AbstractVedTool {

  protected VedPointerTool( ITsGuiContext aContext ) {
    super( "pointerTool", STR_N_TOOL_POINTER, STR_D_TOOL_POINTER, "", aContext ); //$NON-NLS-1$
    // TODO Auto-generated constructor stub
  }

  @Override
  public IMouseHandler mouseHandler() {
    // TODO Auto-generated method stub
    return null;
  }

}
