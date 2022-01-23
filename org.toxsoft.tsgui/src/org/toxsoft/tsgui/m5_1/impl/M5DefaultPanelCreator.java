package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tslib.av.impl.AvUtils.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.*;
import org.toxsoft.tsgui.m5_1.impl.gui.M5DefaultEntityEditorPanel;
import org.toxsoft.tsgui.m5_1.impl.gui.M5DefaultEntityViewerPanel;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMultiPaneModownComponent;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl.MultiPaneModownComponent;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IM5PanelCreator}, создающая панели по умолчанию.
 * <p>
 * Предназначен также для переопределения для создания собственных реализации панелей.
 *
 * @author goga
 * @param <T> - тип (класс) моедлированной сущности
 */
public class M5DefaultPanelCreator<T>
    implements IM5PanelCreator<T> {

  private IM5Model<T> model = null;

  /**
   * Конструктор.
   */
  public M5DefaultPanelCreator() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  void setOwnerModel( IM5Model<T> aModel ) {
    model = aModel;
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  /**
   * Возвращает модель сущностей.
   *
   * @return {@link IM5Model} - модель сущностей
   */
  public IM5Model<T> model() {
    return model;
  }

  // ------------------------------------------------------------------------------------
  // IM5PanelCreator
  //

  @Override
  public IM5EntityPanel<T> createEntityViewerPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5EntityPanel<T> p = doCreateEntityViewerPanel( aContext );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5EntityEditPanel<T> createEntityEditorPanel( ITsGuiContext aContext,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5EntityEditPanel<T> p = doCreateEntityEditorPanel( aContext, aLifecycleManager );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5CollViewerPanel<T> createCollViewerPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollViewerPanel<T> p = doCreateCollViewerPanel( aContext, aItemsProvider );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  @Override
  public IM5CollEditPanel<T> createCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNull( aContext );
    IM5CollEditPanel<T> p = doCreateCollEditPanel( aContext, aItemsProvider, aLifecycleManager );
    TsInternalErrorRtException.checkNull( p );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Наследник может создать свою реализацию панели просмотра сущности.
   * <p>
   * В базовом классе создает экземпляр {@link M5DefaultEntityViewerPanel}, при переопределении вызывать родительский
   * метод не нужно.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели, не бывает <code>null</code>
   * @return {@link IM5EntityPanel} - созданная панель, не должна быть <code>null</code>
   */
  protected IM5EntityPanel<T> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
    return new M5DefaultEntityViewerPanel<>( aContext, model );
  }

  /**
   * Наследник может создать свою реализацию панели редактирования сущности.
   * <p>
   * В базовом классе создает экземпляр {@link M5DefaultEntityEditorPanel}, при переопределении вызывать родительский
   * метод не нужно.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели, не бывает <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   * @return {@link IM5EntityEditPanel} - созданная панель, не должна быть <code>null</code>
   */
  protected IM5EntityEditPanel<T> doCreateEntityEditorPanel( ITsGuiContext aContext,
      IM5LifecycleManager<T> aLifecycleManager ) {
    return new M5DefaultEntityEditorPanel<>( aContext, aLifecycleManager, model, false );
  }

  /**
   * Наследник может создать свою реализацию панели просмотра коллекции сущностей.
   * <p>
   * В базовом классе создает экземпляр {@link M5CollPanelModownMultiPaneImpl}, при переопределении вызывать
   * родительский метод не нужно.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели, не бывает <code>null</code>
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик просматриваемой коллекции, может быть <code>null</code>
   * @return {@link IM5EntityPanel} - созданная панель, не должна быть <code>null</code>
   */
  protected IM5CollViewerPanel<T> doCreateCollViewerPanel( ITsGuiContext aContext,
      IM5ItemsProvider<T> aItemsProvider ) {
    HAS_EDIT_ACTIONS.setValue( aContext.params(), AV_FALSE );
    IMultiPaneModownComponent<T> mpc = new MultiPaneModownComponent<>( aContext, model, aItemsProvider, null );
    return new M5CollPanelModownMultiPaneImpl<>( mpc, true );
  }

  /**
   * Наследник может создать свою реализацию панели просмотра коллекции сущностей.
   * <p>
   * В базовом классе создает экземпляр {@link M5CollPanelModownMultiPaneImpl}, при переопределении вызывать
   * родительский метод не нужно.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели, не бывает <code>null</code>
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик просматриваемой коллекции, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - мнеджер управления ЖЦ, может быть <code>null</code>
   * @return {@link IM5CollEditPanel} - созданная панель, не должна быть <code>null</code>
   */
  protected IM5CollEditPanel<T> doCreateCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    HAS_EDIT_ACTIONS.setValue( aContext.params(), AV_TRUE );
    IMultiPaneModownComponent<T> mpc =
        new MultiPaneModownComponent<>( aContext, model, aItemsProvider, aLifecycleManager );
    return new M5CollPanelModownMultiPaneImpl<>( mpc, false );
  }

}
