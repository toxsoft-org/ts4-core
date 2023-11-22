package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;

/**
 * Undo/Redo item, that set full screen configuration.
 * <p>
 *
 * @author vs
 */
public class FullInfoUndoRedoItem
    extends AbstractUndoRedoItem {

  private final IVedScreen vedScreen;
  private final String     screenCfg4Undo;
  private final String     screenCfg4Redo;

  /**
   * Конструктор.
   *
   * @param aScreen {@link IVedScreen} - экран редактора
   * @param aScreenCfg4Undo String - конфигурация экрана до изменений
   * @param aScreedCfg4Redo String - конфигурация экрана после изменений
   */
  public FullInfoUndoRedoItem( IVedScreen aScreen, String aScreenCfg4Undo, String aScreedCfg4Redo ) {
    vedScreen = aScreen;
    screenCfg4Undo = aScreenCfg4Undo;
    screenCfg4Redo = aScreedCfg4Redo;
  }

  // ------------------------------------------------------------------------------------
  // AbstractUndoRedoItem
  //

  @Override
  public void undo() {
    IVedScreenCfg cfg = VedScreenCfg.KEEPER.str2ent( screenCfg4Undo );
    VedEditorUtils.setVedScreenConfig( vedScreen, cfg );
  }

  @Override
  public void redo() {
    IVedScreenCfg cfg = VedScreenCfg.KEEPER.str2ent( screenCfg4Redo );
    VedEditorUtils.setVedScreenConfig( vedScreen, cfg );
  }

}
