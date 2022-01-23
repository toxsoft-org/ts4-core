package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import static ru.toxsoft.tsgui.m5.gui.multipane.impl.ITsResources.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.gui.mpc.impl.TreeModeInfo;
import org.toxsoft.tsgui.m5.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.m5.gui.viewers.impl.M5TreeViewer;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMultiPaneModownComponent;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;

import ru.toxsoft.tsgui.dialogs.CommonDialogInfo;
import ru.toxsoft.tsgui.dialogs.ICommonDialogInfo;
import ru.toxsoft.tsgui.m5.gui.M5GuiUtils;
import ru.toxsoft.tsgui.m5.model.panels.IM5StdPanelConstants;

/**
 * Реализация {@link IMultiPaneModownComponent}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneModownComponent<T>
    extends BasicMultiPaneComponent<T>
    implements IMultiPaneModownComponent<T> {

  private IM5LifecycleManager<T> lifecycleManager = null;

  /**
   * Значения, которыми заполнается диалог создания нового элемента.
   */
  private IM5Bunch<T> initialValues = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   */
  public MultiPaneModownComponent( ITsGuiContext aContext, IM5Model<T> aModel ) {
    this( new M5TreeViewer<>( aContext, aModel ), null, null );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  public MultiPaneModownComponent( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    this( new M5TreeViewer<>( aContext, aModel ), aItemsProvider, aLifecycleManager );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   */
  public MultiPaneModownComponent( IM5TreeViewer<T> aTreeViewer ) {
    this( aTreeViewer, null, null );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public MultiPaneModownComponent( IM5TreeViewer<T> aTreeViewer, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    super( aTreeViewer, aItemsProvider );
    lifecycleManager = aLifecycleManager;
    IList<TreeModeInfo<?>> treeMakers =
        IM5StdPanelConstants.CTXREF_TREE_MODE_INFOES.getValue( aTreeViewer.tsContext() );
    if( treeMakers != null ) {
      for( TreeModeInfo tmi : treeMakers ) {
        treeModeManager().addTreeMode( tmi );
      }
    }
  }

  /**
   * Возвращает {@link #lifecycleManager()}, если он не <code>null</code>.
   * <p>
   * Если {@link #lifecycleManager()} возвращает <code>null</code>, метод выбрасывает исключение.
   *
   * @return {@link IM5LifecycleManager} - менедже ЖЦ, не бывает <code>null</code>
   */
  private IM5LifecycleManager<T> internalGetNonNullLifecycleManager() {
    if( lifecycleManager == null ) {
      throw new TsIllegalStateRtException( FMT_ERR_NULL_LIFECYCLE_MANAGER, model().id() );
    }
    return lifecycleManager;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMultiPaneComponent
  //

  @Override
  public IM5LifecycleManager<T> lifecycleManager() {
    return lifecycleManager;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    lifecycleManager = aLifecycleManager;
  }

  @Override
  public IM5Bunch<T> getItemCreationInitialValues() {
    return initialValues;
  }

  @Override
  public void setItemCreationInitialValues( IM5Bunch<T> aInitialValues ) {
    initialValues = aInitialValues;
  }

  // ------------------------------------------------------------------------------------
  // Шаблонные методы
  //

  @Override
  protected boolean doGetIsAddAllowed( T aSel ) {
    return lifecycleManager != null && lifecycleManager.isCreationAllowed();
  }

  @Override
  protected boolean doGetIsEditAllowed( T aSel ) {
    return lifecycleManager != null && lifecycleManager.isEditingAllowed();
  }

  @Override
  protected boolean doGetIsRemoveAllowed( T aSel ) {
    return lifecycleManager != null && lifecycleManager.isRemovalAllowed();
  }

  @Override
  protected T doAddItem() {
    ICommonDialogInfo cdi = CommonDialogInfo.cdiNewObjDialog( getShell() );
    return M5GuiUtils.askCreate( tsContext().appContext(), model(), getItemCreationInitialValues(), cdi,
        internalGetNonNullLifecycleManager() );
  }

  @Override
  protected T doEditItem( T aItem ) {
    ICommonDialogInfo cdi = CommonDialogInfo.cdiEditObjDialog( getShell() );
    return M5GuiUtils.askEdit( tsContext().appContext(), model(), aItem, cdi, internalGetNonNullLifecycleManager() );
  }

  @Override
  protected boolean doRemoveItem( T aItem ) {
    return M5GuiUtils.askRemove( tsContext().appContext(), model(), aItem, getShell(),
        internalGetNonNullLifecycleManager() );
  }

}
