package org.toxsoft.tsgui.m5_1.impl.gui;

import static org.toxsoft.tsgui.m5_1.IM5Constants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.*;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanel;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanelWithValedsController;
import org.toxsoft.tsgui.panels.vecboard.IVecLadderLayout;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Панель {@link IM5EntityPanel} по умолчанию для редактирования сущности.
 * <p>
 * Панель создает редакторы (в режиме редактирования) всех полей без признака {@link IM5Constants#M5FF_HIDDEN} в
 * раскладке {@link IVecLadderLayout}.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public class M5DefaultEntityEditorPanel<T>
    extends M5EntityPanelWithValeds<T> {

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5DefaultEntityEditorPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager,
      IM5Model<T> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
    setLifecycleManager( aLifecycleManager );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @param aController {@link IM5EntityPanelWithValedsController} - контроллер панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5DefaultEntityEditorPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager,
      IM5Model<T> aModel, boolean aViewer, M5EntityPanelWithValedsController<T> aController ) {
    super( aContext, aModel, aViewer, aController );
    setLifecycleManager( aLifecycleManager );
  }

  @Override
  protected void doInitEditors() {
    // создает редакторы всех полей без признака M5FF_HIDDEN
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( !fDef.hasHint( M5FF_HIDDEN ) ) {
        addField( fDef.id() );
      }
    }
  }

}
