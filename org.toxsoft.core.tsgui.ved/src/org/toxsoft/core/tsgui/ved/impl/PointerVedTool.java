package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.impl.IVedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Инструмент "Указатель".
 * <p>
 * Является базовым инструментом, который позволяет выделять компонеты, перемещать их и изменять их размеры.
 *
 * @author vs
 */
public class PointerVedTool
    extends AbstractVedTool {

  protected PointerVedTool( ITsGuiContext aContext ) {
    super( "pointerTool", STRN_TOOL_POINTER, STRD_TOOL_POINTER, "", aContext ); //$NON-NLS-1$
    // TODO Auto-generated constructor stub
  }

  @Override
  public IMouseHandler mouseHandler() {
    // TODO Auto-generated method stub
    return null;
  }

}
