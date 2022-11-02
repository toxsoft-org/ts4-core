package org.toxsoft.core.tsgui.ved.zver0.tools_old.palette2;

import org.toxsoft.core.tsgui.ved.zver0.tools_old.tools2.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;

/**
 * Слушатель изменения "активного" инструмента.
 * <p>
 *
 * @author vs
 */
public interface IVedToolSelectionListener {

  /**
   * Для активного инструмента вызывается в момент, когда активируется другой инструмент.
   *
   * @param aTool IVedEditorTool - деактивируемый инструмент
   */
  void onToolDeactivated( IVedEditorTool aTool );

  /**
   * Вызывается в момент активации инструмента.
   *
   * @param aTool IVedEditorTool - новый активный инструмент
   */
  void onToolActivated( IVedEditorTool aTool );
}
