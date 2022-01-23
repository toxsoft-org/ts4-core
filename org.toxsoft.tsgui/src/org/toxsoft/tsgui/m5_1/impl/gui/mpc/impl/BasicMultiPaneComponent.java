package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import static ru.toxsoft.tsgui.components.stdacts.IStdActions.*;
import static ru.toxsoft.tsgui.m5.IM5Constants.*;
import static ru.toxsoft.tsgui.m5.gui.multipane.IMultiPaneComponentParams.*;
import static ru.toxsoft.tsgui.m5.gui.multipane.impl.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.bricks.stdevents.*;
import org.toxsoft.tsgui.bricks.stdevents.impl.TsKeyDownEventHelper;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.graphics.EHorAlignment;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.m5.gui.mpc.ITreeModeManager;
import org.toxsoft.tsgui.m5.gui.mpc.impl.TreeModeInfo;
import org.toxsoft.tsgui.m5.gui.viewers.*;
import org.toxsoft.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.tsgui.m5_1.*;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanel;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.*;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.helpers.IM5Column;
import org.toxsoft.tsgui.utils.layout.BorderLayout;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

import ru.toxsoft.tsgui.appcontext.addons.TsHdpiSupport;
import ru.toxsoft.tsgui.components.hdrtoolbar.HdrToolbar;
import ru.toxsoft.tsgui.components.hdrtoolbar.IHdrToolbarListener;
import ru.toxsoft.tsgui.components.stdacts.IStdActions;
import ru.toxsoft.tsgui.components.stdacts.ITsActionInfo;
import ru.toxsoft.tsgui.m5.gui.multipane.*;
import ru.toxsoft.tsgui.m5.model.*;
import ru.toxsoft.tsgui.utils.stdevents.*;
import ru.toxsoft.tsgui.widgets.toolbar.NamedToolBar;
import ru.toxsoft.tslib.datavalue.impl.DvUtils;
import ru.toxsoft.tslib.error.*;
import ru.toxsoft.tslib.file.EFileSystemType;
import ru.toxsoft.tslib.utils.collnavi.ETsMoveDirection;
import ru.toxsoft.tslib.utils.misc.*;

/**
 * Базовая реализация {@link IMultiPaneComponent}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class BasicMultiPaneComponent<T>
    implements IMultiPaneComponent<T> {

  class InternalTreeModeMan
      extends TreeModeManager<T> {

    public InternalTreeModeMan( boolean aHasTreeMode ) {
      super( aHasTreeMode );
    }

    @Override
    protected void onCurrentModeIdChanged() {
      internalSetTreeGroupingMode( getCurrentModeId() );
    }

    @Override
    protected void onModeInfoesChanged() {
      updateToolbarMenus();
    }

  }

  private final IHdrToolbarListener toolbarListener = new IHdrToolbarListener() {

    @Override
    public void onButtonPressed( int aActionId ) {
      processAction( aActionId );
    }
  };

  /**
   * Обработчик двоного щелчка на элементе.
   * <p>
   * Вызывает обратчник наследника {@link #doProcessTreeDoubleClick(Object)}, а если наследник не обрабатывает двойной
   * щелчок, вызывает действие {@link IStdActions#SAID_EDIT}.
   */
  private final ITsDoubleClickListener<T> treeDoubleClickListener = ( aSource, aSelectedItem ) -> {
    if( !doProcessTreeDoubleClick( aSelectedItem ) ) {
      int doubleClickActId = DOUBLE_CLICK_ACTION_ID.getValue( tsContext().params() ).asInt();
      if( (toolbar().findAction( doubleClickActId ) != null) && toolbar().isActionEnabled( doubleClickActId ) ) {
        processAction( doubleClickActId );
      }
    }
  };

  private final ITsSelectionChangeListener<T> treeSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    onSelectionChanged( aSelectedItem );
    updateDetailsPane();
    updateSummaryPane();
    updateActionsState();
  };

  private final IGenericChangeListener filterPaneChangeListener = aSource -> {
    tree().filterManager().setFilter( filterPane().getFilter() );
    updateSummaryPane();
  };

  final IGenericChangeListener itemsChangeListener = aSource -> refresh();

  /**
   * Отработка изменений параметров контекста "на лету".
   */
  final ITsCollectionChangeListener paramsChangeListener = ( aSource, aOp, aItem ) -> {
    if( aItem == null ) {
      treeModeManager().setHasTreeMode( HAS_TREE_MODE.getValue( tsContext().params() ).asBool() );
      tree().setIconSize( NODE_ICON_SIZE.getValue( tsContext().params() ) );
      tree().refresh();
      return;
    }
    if( aItem.equals( HAS_TREE_MODE.id() ) ) {
      treeModeManager().setHasTreeMode( HAS_TREE_MODE.getValue( tsContext().params() ).asBool() );
      return;
    }
    if( aItem.equals( NODE_ICON_SIZE.id() ) ) {
      tree().setIconSize( NODE_ICON_SIZE.getValue( tsContext().params() ) );
      tree().refresh();
      return;
    }
  };

  private final TsKeyDownEventHelper keyDownEventHelper;

  private final IM5TreeMaker<T>     tableMaker;
  private final IM5TreeViewer<T>    tree;
  private final ITreeModeManager<T> tmm;

  private TsComposite        board       = null;
  private HdrToolbar         toolbar     = null;
  private IMpsDetailsPane<T> detailsPane = null;
  private IMpcSummaryPane<T> summaryPane = null;
  private IMpcFilterPane<T>  filterPane  = null;

  private boolean editable       = true;
  private boolean isInternalEdit = false;

  IM5ItemsProvider<T> itemsProvider = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   */
  public BasicMultiPaneComponent( ITsGuiContext aContext, IM5Model<T> aModel ) {
    this( new M5TreeViewer<>( aContext, aModel ), null );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент (кроме aItemsProvider) = <code>null</code>
   */
  public BasicMultiPaneComponent( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider ) {
    this( new M5TreeViewer<>( aContext, aModel ), aItemsProvider );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   */
  public BasicMultiPaneComponent( IM5TreeViewer<T> aTreeViewer ) {
    this( aTreeViewer, null );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @throws TsNullArgumentRtException aTreeViewer = <code>null</code>
   */
  public BasicMultiPaneComponent( IM5TreeViewer<T> aTreeViewer, IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aTreeViewer );
    TsIllegalArgumentRtException.checkTrue( aTreeViewer.getControl() != null );
    // если существует aItemsProvider.reorderer() и явно не запрещено, зададим изменение порядка элементов
    if( !aTreeViewer.tsContext().params().hasKey( HAS_REORDER_ACTIONS.id() ) ) {
      if( aItemsProvider != null ) {
        boolean isReordrer = aItemsProvider.reorderer() != null;
        HAS_REORDER_ACTIONS.setValue( aTreeViewer.tsContext().params(), DvUtils.avBool( isReordrer ) );
      }
    }
    // обработку клавиш сделам так, чтобы сначала обрабатывались пользовательские
    keyDownEventHelper = new TsKeyDownEventHelper( this ) {

      @Override
      public boolean fireTsKeyDownEvent( Event aEvent ) {
        if( super.fireTsKeyDownEvent( aEvent ) ) {
          return true;
        }
        return handleKeyDownEvent( aEvent );
      }
    };
    tableMaker = new M5DefaultTreeMaker<>( aTreeViewer.objModel().entityClass() );
    itemsProvider = aItemsProvider;
    tree = aTreeViewer;
    tree.setTreeMaker( tableMaker );
    tree.addTsDoubleClickListener( treeDoubleClickListener );
    tree.addTsSelectionListener( treeSelectionChangeListener );
    tmm = new InternalTreeModeMan( HAS_TREE_MODE.getValue( tsContext().params() ).asBool() );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private final HdrToolbar createToolbar( Composite aParent ) {
    IListEdit<ITsActionInfo> acts = new ElemLinkedBundleList<>();
    boolean hasEditActions = HAS_EDIT_ACTIONS.getValue( tsContext().params() ).asBool();
    boolean hasReordererActions = HAS_REORDER_ACTIONS.getValue( tsContext().params() ).asBool();
    boolean hasCheckActions = HAS_CHECK_ACTIONS.getValue( tsContext().params() ).asBool();
    boolean hasTreeMode = HAS_TREE_MODE.getValue( tsContext().params() ).asBool();
    if( hasEditActions ) {
      acts.add( SA_ADD );
      acts.add( SA_EDIT );
      acts.add( SA_REMOVE );
    }
    // кнопки дерево/таблица, развернуть/свернуть все
    if( hasTreeMode ) {
      if( hasEditActions ) {
        acts.add( SA_SEPARATOR );
      }
      acts.add( SA_VIEW_AS_TREE_MENU );
      acts.add( SA_VIEW_AS_LIST );
      acts.add( SA_SEPARATOR );
      acts.add( SA_COLLAPSE_ALL );
      acts.add( SA_EXPAND_ALL );
    }
    // кнопки пометки/размекти всех эелементов
    if( hasCheckActions ) {
      if( hasEditActions ) {
        acts.add( SA_SEPARATOR );
      }
      acts.add( SA_CHECK_ALL );
      acts.add( SA_UNCHECK_ALL );
    }
    // кнопки дерево/таблица, развернуть/свернуть все
    if( hasReordererActions ) {
      if( hasEditActions ) {
        acts.add( SA_SEPARATOR );
      }
      acts.add( SA_LIST_ELEM_MOVE_FIRST );
      acts.add( SA_LIST_ELEM_MOVE_UP );
      acts.add( SA_LIST_ELEM_MOVE_DOWN );
      acts.add( SA_LIST_ELEM_MOVE_LAST );
    }
    // кнопки показа/сокрытия панелей: фильтра, деталей и итоговой
    if( USE_HIDE_BUTTONS.getValue( tsContext().params() ).asBool() ) {
      boolean isBtnHideFilter = USE_FILTER_PANE.getValue( tsContext().params() ).asBool();
      boolean isBtnHideDetails = USE_DETAILS_PANE.getValue( tsContext().params() ).asBool();
      boolean isBtnHideSummary = USE_SUMMARY_PANE.getValue( tsContext().params() ).asBool();
      if( isBtnHideFilter || isBtnHideDetails || isBtnHideSummary ) {
        acts.add( SA_SEPARATOR );
        if( isBtnHideFilter ) {
          acts.add( SA_HIDE_FILTERS );
        }
        if( isBtnHideDetails ) {
          acts.add( SA_HIDE_DETAILS );
        }
        if( isBtnHideSummary ) {
          acts.add( SA_HIDE_SUMMARY );
        }
      }
    }
    // подбор размера значков панели инструментов
    EIconSize iconSize = EIconSize.IS_16X16;
    TsHdpiSupport hdpiSupport = tsContext().get( TsHdpiSupport.class );
    if( hdpiSupport != null ) {
      iconSize = hdpiSupport.getToolbarIconSize();
    }
    // настройка текст в панели инструментов
    String name = StringUtils.EMPTY_STRING;
    if( !model().nmName().isEmpty() ) {
      name = model().nmName() + ": "; //$NON-NLS-1$
    }
    // создание панели инструментов
    HdrToolbar t = doCreateToolbar( aParent, name, iconSize, acts );
    TsInternalErrorRtException.checkNull( t );
    return t;
  }

  /**
   * Обновляет выпадающие меню панели инструментов.
   */
  void updateToolbarMenus() {
    if( toolbar() != null ) {
      toolbar().setActionMenu( SAID_VIEW_AS_TREE,
          new TreeModeDropDownMenuCreator<>( tsContext().appContext(), treeModeManager() ) );
    }
  }

  /**
   * Обновляет состояние действий на панели упралвения.
   */
  public final void updateActionsState() {
    if( board == null ) {
      return;
    }
    boolean isAlive = doGetAlive();
    T sel = tree.selectedItem();
    boolean isSel = sel != null;
    boolean isCreationAllowed = isAlive;
    boolean isEditingAllowed = isAlive && isSel;
    boolean isRemovalAllowed = isAlive && isSel;
    if( isAlive ) {
      isCreationAllowed = isCreationAllowed && doGetIsAddAllowed( sel );
      isEditingAllowed = isEditingAllowed && doGetIsEditAllowed( sel );
      isRemovalAllowed = isRemovalAllowed && doGetIsRemoveAllowed( sel );
    }
    else {
      isCreationAllowed = false;
      isEditingAllowed = false;
      isRemovalAllowed = false;
    }
    toolbar.setActionEnabled( SAID_ADD, editable && isCreationAllowed );
    toolbar.setActionEnabled( SAID_EDIT, editable && isEditingAllowed );
    toolbar.setActionEnabled( SAID_REMOVE, editable && isRemovalAllowed );
    toolbar.setActionEnabled( SAID_REFRESH, isAlive );
    toolbar.setActionEnabled( SAID_VIEW_AS_TREE, tmm.hasTreeMode() );
    toolbar.setActionEnabled( SAID_VIEW_AS_LIST, tmm.hasTreeMode() );
    toolbar.setActionEnabled( SAID_EXPAND_ALL, tmm.isCurrentTreeMode() );
    toolbar.setActionEnabled( SAID_COLLAPSE_ALL, tmm.isCurrentTreeMode() );
    toolbar.setActionChecked( SAID_VIEW_AS_TREE, tmm.isCurrentTreeMode() );
    toolbar.setActionChecked( SAID_VIEW_AS_LIST, !tmm.isCurrentTreeMode() );
    boolean hasReordererActions = HAS_REORDER_ACTIONS.getValue( tsContext().params() ).asBool();
    if( hasReordererActions ) {
      boolean isReorderAllowed = isAlive && itemsProvider != null && itemsProvider.reorderer() != null;
      if( isReorderAllowed ) {
        int index = -1;
        IList<T> list = itemsProvider.reorderer().list();
        if( sel != null ) {
          index = list.indexOf( sel );
        }
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_FIRST, index > 0 );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_UP, index > 0 );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_DOWN, index >= 0 && index < list.size() - 1 );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_LAST, index >= 0 && index < list.size() - 1 );
      }
      else {
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_FIRST, false );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_UP, false );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_DOWN, false );
        toolbar.setActionEnabled( SAID_LIST_ELEM_MOVE_LAST, false );
      }
    }
    if( filterPane != null && toolbar.actionItems().hasElem( SA_HIDE_FILTERS ) ) {
      toolbar.setActionChecked( SAID_HIDE_FILTER, filterPane.getControl().getVisible() );
    }
    if( detailsPane != null && toolbar.actionItems().hasElem( SA_HIDE_DETAILS ) ) {
      toolbar.setActionChecked( SAID_HIDE_DETAILS, detailsPane.getControl().getVisible() );
    }
    if( summaryPane != null && toolbar.actionItems().hasElem( SA_HIDE_SUMMARY ) ) {
      toolbar.setActionChecked( SAID_HIDE_SUMMARY, summaryPane.getControl().getVisible() );
    }
    doUpdateActionsState( isAlive, isSel, sel );
  }

  void internalSetTreeGroupingMode( String aModeId ) {
    if( board == null ) {
      return;
    }
    T sel = tree.selectedItem();
    if( aModeId != null ) {
      TreeModeInfo<T> modeInfo = tmm.treeModeInfoes().getItem( aModeId );
      tree.setTreeMaker( modeInfo.treeMaker() );
    }
    else {
      tree.setTreeMaker( tableMaker );
    }
    tree.setSelectedItem( sel );
  }

  void updateDetailsPane() {
    if( detailsPane == null ) {
      return;
    }
    T selItem = tree.selectedItem();
    ITsNode selNode = (ITsNode)tree.console().selectedNode();
    IM5Bunch<T> values = selItem == null ? null : model().valuesOf( selItem );
    detailsPane.setValues( selNode, values );
  }

  void updateSummaryPane() {
    if( summaryPane == null ) {
      return;
    }
    ITsNode selNode = (ITsNode)tree.console().selectedNode();
    summaryPane.updatePane( selNode, tree.items() );
  }

  private final T findItemToSelectAfterRemove( T aSel ) {
    int index = tree.items().indexOf( aSel );
    if( index < tree.items().size() - 2 ) {
      return tree.items().get( index + 1 );
    }
    if( index > 0 ) {
      return tree.items().get( index - 1 );
    }
    return null;
  }

  boolean handleKeyDownEvent( Event aEvent ) {
    int actionId = -1;
    switch( aEvent.keyCode ) {
      case SWT.DEL:
        actionId = SAID_REMOVE;
        break;
      case SWT.INSERT:
        actionId = SAID_ADD;
        break;
      case SWT.CR:
      case SWT.KEYPAD_CR:
        actionId = SAID_EDIT;
        break;
      case SWT.F5:
        actionId = SAID_REFRESH;
        break;
      case SWT.F6:
        actionId = SAID_HIDE_DETAILS;
        break;
      case SWT.F7:
        actionId = SAID_HIDE_SUMMARY;
        break;
      default:
        return false;
    }
    processAction( actionId );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    tree.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    tree.removeTsSelectionListener( aListener );
  }

  @Override
  public T selectedItem() {
    return tree.selectedItem();
  }

  @Override
  public void setSelectedItem( T aItem ) {
    tree.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    tree.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    tree.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsKeyDownEventProducer
  //

  @Override
  public void addTsKeyDownListener( ITsKeyEventListener aListener ) {
    keyDownEventHelper.addTsKeyDownListener( aListener );
  }

  @Override
  public void removeTsKeyDownListener( ITsKeyEventListener aListener ) {
    keyDownEventHelper.removeTsKeyDownListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @Override
  public TsComposite createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( board );
    // создаем служебные панели (пока не их виджеты!) если нужно
    if( USE_DETAILS_PANE.getValue( tsContext().params() ).asBool() ) {
      detailsPane = doCreateDetailsPane();
    }
    if( USE_SUMMARY_PANE.getValue( tsContext().params() ).asBool() ) {
      summaryPane = doCreateSummaryPane();
    }
    if( USE_FILTER_PANE.getValue( tsContext().params() ).asBool() ) {
      filterPane = doCreateFilterPane();
    }
    // создаем виджеты
    board = new TsComposite( aParent, SWT.BORDER );
    board.setLayout( new BorderLayout() );
    // board.setLayoutData( BorderLayout.CENTER );
    // toolbar
    toolbar = createToolbar( board );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addToolbarListener( toolbarListener );
    // панель иснтументов создаем всегда (для работы логики с действями), но не всегда показываем
    toolbar.getControl().setVisible( USE_TOOL_BAR.getValue( tsContext().params() ).asBool() );
    // панель с остальными компонентами (filterPane, tree, detailsPane, summaryPane )
    TsComposite board1 = new TsComposite( board );
    board1.setLayout( new BorderLayout() );
    board1.setLayoutData( BorderLayout.CENTER );
    // filterPane
    if( filterPane != null ) {
      filterPane.createControl( board1 );
      filterPane.getControl().setLayoutData( BorderLayout.NORTH );
    }
    // панель с остальными компонентами (tree, detailsPane, summaryPane )
    TsComposite board2 = new TsComposite( board1 );
    board2.setLayout( new BorderLayout() );
    board2.setLayoutData( BorderLayout.CENTER );
    // summaryPane
    if( summaryPane != null ) {
      summaryPane.createControl( board2 );
      summaryPane.getControl().setLayoutData( BorderLayout.SOUTH );
    }
    // панель с остальными компонентами (tree, detailsPane)
    if( detailsPane != null ) { // есть detailsPane, расположим ее и tree на SashForm
      TsComposite b2 = new TsComposite( board2 );
      b2.setLayout( new BorderLayout() );
      b2.setLayoutData( BorderLayout.CENTER );
      tree.createControl( b2 );
      detailsPane.createControl( b2 );
      detailsPane.getControl().setLayoutData( DETAILS_PANE_PLACEMENT.getValue( tsContext().params() ) );
    }
    else { // нет detailsPane, воткнет tree прямо в board2
      tree.createControl( board2 );
    }
    tree.getControl().setLayoutData( BorderLayout.CENTER );
    tree.setIconSize( NODE_ICON_SIZE.getValue( tsContext().params() ) );
    // setup
    doCreateTreeColumns( tree );
    // TODO: mvk WORKAROUND "схлопывания" единичного столбца под windows
    if( tree.columnManager().columns().size() == 1 ) {
      if( EFileSystemType.currentFsType() == EFileSystemType.WINDOWS ) {
        tree.columnManager().columns().values().get( 0 ).setWidth( 900 );
      }
    }
    updateToolbarMenus();
    refresh();
    tmm.setHasTreeMode( HAS_TREE_MODE.getValue( tsContext().params() ).asBool() );
    tsContext().params().addCollectionChangeListener( paramsChangeListener );
    tree.getControl().addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent aE ) {
        if( itemsProvider != null ) {
          itemsProvider.removeGenericChangeListener( itemsChangeListener );
        }
        tsContext().params().removeCollectionChangeListener( paramsChangeListener );
        doDispose();
      }
    } );
    internalSetTreeGroupingMode( treeModeManager().getCurrentModeId() );
    tree.addTsKeyDownListener( keyDownEventHelper );
    if( filterPane != null ) {
      filterPane.addGenericChangeListener( filterPaneChangeListener );
    }
    doAfterCreateControls();
    board.layout();
    board.getParent().layout();
    refresh();
    return board;
  }

  @Override
  public TsComposite getControl() {
    return board;
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Возвращает окно приложения.
   *
   * @return {@link Shell} - окно приложения
   * @throws TsIllegalStateRtException метод вызван до создания виджетов методом {@link #createControl(Composite)}
   */
  @Override
  public Shell getShell() {
    TsIllegalStateRtException.checkNull( board );
    return board.getShell();
  }

  /**
   * Возвращает панель инструментов.
   *
   * @return {@link HdrToolbar} - панель инструментов
   */
  public HdrToolbar toolbar() {
    return toolbar;
  }

  /**
   * Возвращает панель детальной информации.
   *
   * @return {@link IMpsDetailsPane} - панель детальной информации, или <code>null</code> если нет панели
   */
  public IMpsDetailsPane<T> detailsPane() {
    return detailsPane;
  }

  /**
   * Возвращает панель суммарной информации.
   *
   * @return {@link IMpcSummaryPane} - панель суммарной информации, или <code>null</code> если нет панели
   */
  public IMpcSummaryPane<T> summaryPane() {
    return summaryPane;
  }

  /**
   * Возвращает панель фильтра.
   *
   * @return {@link IMpcFilterPane} - панель фильтра, или <code>null</code> если нет панели
   */
  public IMpcFilterPane<T> filterPane() {
    return filterPane;
  }

  /**
   * Определяет, отображаются ли элементы в виде дерева (или в виде таблицы).
   *
   * @return boolean - признак отображения элементов в виде дерева (а не таблицы).
   */
  public boolean isTreeMode() {
    return toolbar.isActionChecked( SAID_VIEW_AS_TREE );
  }

  /**
   * Выполняет зщаданное действие.
   * <p>
   * Выолняет как встроенные действия, так и действия, определенные наследником в {@link #doProcessAction(int)}. Если
   * действие присутствует в панели инструментов и запрещено, оно не будет выполнено.
   *
   * @param aActionId int - идентификатор действия.
   */
  public void processAction( int aActionId ) {
    if( board == null || (toolbar.findAction( aActionId ) != null && !toolbar.isActionEnabled( aActionId )) ) {
      return;
    }
    T sel = tree.selectedItem();
    switch( aActionId ) {
      case SAID_ADD: {
        if( isEditable() ) {
          T item = doAddItem();
          if( item != null ) {
            fillViewer( item );
          }
        }
        break;
      }
      case SAID_EDIT: {
        if( isEditable() ) {
          TsInternalErrorRtException.checkNull( sel );
          T item = doEditItem( sel );
          if( item != null ) {
            fillViewer( item );
          }
        }
        break;
      }
      case SAID_REMOVE: {
        if( isEditable() ) {
          TsInternalErrorRtException.checkNull( sel );
          T toSel = findItemToSelectAfterRemove( sel );
          if( doRemoveItem( sel ) ) {
            fillViewer( toSel );
          }
        }
        break;
      }
      case SAID_REFRESH: {
        refresh();
        break;
      }
      case SAID_EXPAND_ALL: {
        tree.console().expandAll();
        break;
      }
      case SAID_COLLAPSE_ALL: {
        tree.console().collapseAll();
        break;
      }
      case SAID_VIEW_AS_TREE: {
        String nextModeId;
        if( tmm.isCurrentTreeMode() ) { // если сейчас в режиме дерева - выберем следующий режим дерева
          nextModeId = ETsMoveDirection.NEXT.findNewPos( tmm.getCurrentModeId(), tmm.treeModeInfoes().idList(), true );
        }
        else { // если сейчас в режиме таблицы - перейдем в последний режим дерева
          nextModeId = tmm.getLastModeId();
        }
        tmm.setCurrentMode( nextModeId );
        break;
      }
      case SAID_VIEW_AS_LIST: {
        tmm.setCurrentMode( null );
        break;
      }
      case SAID_LIST_ELEM_MOVE_FIRST: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveFirst( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case SAID_LIST_ELEM_MOVE_UP: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().movePrev( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case SAID_LIST_ELEM_MOVE_DOWN: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveNext( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case SAID_LIST_ELEM_MOVE_LAST: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveLast( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case SAID_CHECK_ALL:
        doCheckAll();
        break;
      case SAID_UNCHECK_ALL:
        doUnCheckAll();
        break;
      case SAID_HIDE_FILTER: {
        if( filterPane != null ) {
          filterPane.getControl().setVisible( !filterPane.getControl().getVisible() );
          board.layout( true, true );
        }
        break;
      }
      case SAID_HIDE_DETAILS: {
        if( detailsPane != null ) {
          detailsPane.getControl().setVisible( !detailsPane.getControl().getVisible() );
          board.layout( true, true );
        }
        break;
      }
      case SAID_HIDE_SUMMARY: {
        if( summaryPane != null ) {
          summaryPane.getControl().setVisible( !summaryPane.getControl().getVisible() );
          board.layout( true, true );
        }
        break;
      }
      default:
        try {
          doProcessAction( aActionId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( getShell(), ex );
        }
        break;
    }
    try {
      doAfterActionProcessed( aActionId );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMultiPaneComponent
  //

  @Override
  public IM5TreeViewer<T> tree() {
    return tree;
  }

  @Override
  public ITsGuiContext tsContext() {
    return tree.tsContext();
  }

  @Override
  public IM5Model<T> model() {
    return tree.objModel();
  }

  @Override
  public void fillViewer( T aToSelect ) {
    if( board == null || isInternalEdit ) {
      return;
    }
    doFillTree();
    tree.setSelectedItem( aToSelect );
    updateActionsState();
  }

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( editable != aEditable ) {
      editable = aEditable;
    }
    updateActionsState();
  }

  @Override
  public boolean isInternalEdit() {
    return isInternalEdit;
  }

  @Override
  public void refresh() {
    fillViewer( tree.selectedItem() );
  }

  @Override
  final public ITreeModeManager<T> treeModeManager() {
    return tmm;
  }

  @Override
  public IM5ItemsProvider<T> itemsProvider() {
    return itemsProvider;
  }

  @Override
  public void setItemProvider( IM5ItemsProvider<T> aItemsProvider ) {
    if( itemsProvider != null ) {
      itemsProvider.removeGenericChangeListener( itemsChangeListener );
    }
    itemsProvider = aItemsProvider;
    if( itemsProvider != null ) {
      itemsProvider.addGenericChangeListener( itemsChangeListener );
    }
  }

  // ------------------------------------------------------------------------------------
  // Шаблонные методы
  //

  /**
   * Наследник может настроить создаваемую панель инструментов по своему усмотрению.
   * <p>
   * В базовом классе просто вызывает конструктор {@link HdrToolbar#HdrToolbar(Composite, String, EIconSize, IList)}.
   * Вызывать родительский метод не нужно, если самому создавать панель инструментов.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aName String - название панели {@link NamedToolBar}
   * @param aIconSize {@link EIconSize} - размер значков на панели
   * @param aActs IList&lt;{@link ITsActionInfo}&gt; - список кнопок в порядке расположения на панели
   * @return {@link HdrToolbar} - созданная панель инструментов
   */
  protected HdrToolbar doCreateToolbar( Composite aParent, String aName, EIconSize aIconSize,
      IListEdit<ITsActionInfo> aActs ) {
    return new HdrToolbar( aParent, aName, aIconSize, aActs );
  }

  /**
   * Наследник может создать собственную панель просмотра детальной информации.
   * <p>
   * В базовом классе, если есть хотя бы одно поле с флагом {@link IM5Constants#M5FF_DETAIL}, создает экземпляр
   * {@link DefaultDetailsPane} с экземпляровм панели {@link IM5PanelCreator#createEntityDetailsPanel(ITsGuiContext)}.
   * Если такого поля нет, возвращает <code>null</code>.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}, только если задан параметр
   * {@link IMpcConfigOptions#USE_DETAILS_PANE}.
   * <p>
   * Если наследник решил, что панель не нужна, можно вернуть <code>null</code>.
   *
   * @return {@link IMpsDetailsPane} - созданная панель детальной информации или <code>null</code>
   */
  protected IMpsDetailsPane<T> doCreateDetailsPane() {
    boolean needDetailsPane = false;
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_DETAIL) != 0 ) {
        needDetailsPane = true;
      }
    }
    if( needDetailsPane ) {
      IM5EntityPanel<T> panel = model().panelCreator().createEntityDetailsPanel( tsContext() );
      return new DefaultDetailsPane<>( this, panel );
    }
    return null;
  }

  /**
   * Наследник может создать собственную панель просмотра суммарной информации.
   * <p>
   * В базовом классе создает экземпляр {@link DefaultSummaryPane}. При переопределении вызывать родительский метод не
   * нужно.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}, только если задан параметр
   * {@link IMpcConfigOptions#USE_SUMMARY_PANE}.
   * <p>
   * Если наследник решил, что панель не нужна, можно вернуть <code>null</code>.
   *
   * @return {@link IMpcSummaryPane} - созданная панель суммарной информации или <code>null</code>
   */
  protected IMpcSummaryPane<T> doCreateSummaryPane() {
    return new DefaultSummaryPane<>( this );
  }

  /**
   * Наследник может произвести дополнительные настройки сразу после создания виджетов в
   * {@link #createControl(Composite)}.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   */
  protected void doAfterCreateControls() {
    // nop
  }

  /**
   * Наследник может создать собственную панель фильтра.
   * <p>
   * В базовом классе создает экземпляр {@link DefaultFilterPane}. При переопределении вызывать родительский метод не
   * нужно.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}, только если задан параметр
   * {@link IMpcConfigOptions#USE_FILTER_PANE}.
   * <p>
   * Если наследник решил, что панель не нужна, можно вернуть <code>null</code>.
   *
   * @return {@link IMpcFilterPane} - созданная панель фильтра или<code>null</code>
   */
  protected IMpcFilterPane<T> doCreateFilterPane() {
    IMpcFilterPane<T> fp = new DefaultFilterPane<>( this );
    fp.setFilterOn( true );
    return fp;
  }

  /**
   * Наследник в этом методе должен вернуть признак "живости" панели.
   * <p>
   * "Живость" панели означает, что с содержимым можно работать. "Мертвая" же панель запрещает все известные базовому
   * классу редактирующие действия (и наследник в {@link #doUpdateActionsState(boolean, boolean, Object)} должен
   * запрещать все действия с данными). Например, при правке списка пользователей системы S5, панель "мертвая", если нет
   * соединения с сервером. При появлении же соединения, наследник должен вызвать {@link #fillViewer(Object)}, что
   * приведет к обновлению содержимого и состояния панели.
   * <p>
   * В базовом классе возвращает <code>true</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @return bolean - признак "живости" панели
   */
  protected boolean doGetAlive() {
    return true;
  }

  /**
   * Наследник должен установить состояние дополнительных действий на панели инструментов.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aIsAlive boolean - признак "живости" панели
   * @param aIsSel boolean - признак, что есть неnull выделенный элемент
   * @param aSel &lt;T&gt; - выделенный элемент или <code>null</code>
   */
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, T aSel ) {
    // nop
  }

  /**
   * Вызывается, когда изменяется текущий выделенный элемент.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aSelectedItem &lt;T&gt; - текущий выделенный элемент, может быть <code>null</code>
   */
  protected void onSelectionChanged( T aSelectedItem ) {
    // nop
  }

  /**
   * Наследник может сам обработать двойной щелчок левой кнопки мыши.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @param aSelectedItem &lt;T&gt; - элемент, на котором был щелчок или <code>null</code>
   * @return boolean - признак, что наследник обработал событие<br>
   *         <b>true</b> - наследник обработал событие, родитель большие ничего не будет делать;<br>
   *         <b>false</b> - родитель вызовет обработку действия {@link IStdActions#SA_EDIT}.
   */
  protected boolean doProcessTreeDoubleClick( T aSelectedItem ) {
    return false;
  }

  /**
   * Наследник должен обработать действия, которые он добавил в панель инструемнтов.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aActionId int - идентификатор действия
   */
  protected void doProcessAction( int aActionId ) {
    // nop
  }

  /**
   * Наследник может выполнить дополнительные действия после обработки действия.
   * <p>
   * Вызывается после и встроенных, и пользовательских действий.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   *
   * @param aActionId int - идентификатор действия
   */
  protected void doAfterActionProcessed( int aActionId ) {
    // nop
  }

  /**
   * Наследник в этом методе может создать столбцы дерева.
   * <p>
   * В базовом классе создает столбцы с полями, которые помечены флагом {@link IM5Constants#M5FF_COLUMN}. При
   * переопределении можно вызывать родительский метод и потом править столбцы, или не вызывать и самому создать все
   * столбцы..
   *
   * @param aTree {@link IM5TreeViewer} - настраиваемое дерево
   */
  protected void doCreateTreeColumns( IM5TreeViewer<T> aTree ) {
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_COLUMN) != 0 ) {
        IM5Column<T> col = aTree.columnManager().add( fdef );
        EHorAlignment ha = OPDEF_M5_COLUMN_ALIGN.getValue( fdef.params() ).asValobj();
        col.setAlignment( ha );
      }
    }
  }

  /**
   * Наследник должен переопределить процесс добавления элемента.
   * <p>
   * В базовм классе выбрасывает исключение, при переопределении нельзя вызывать родительский метод.
   *
   * @return &lt;T&gt; - созданный элемент или <code>null</code>, если элемент не был создан
   * @throws TsUnsupportedFeatureRtException всегда выбрасывается в базовом классе
   */
  protected T doAddItem() {
    throw new TsUnsupportedFeatureRtException( MSG_ERR_NO_ADD_ITEM_CODE );
  }

  /**
   * Наследник должен переопределить процесс редактирования элемента.
   * <p>
   * В базовм классе выбрасывает исключение, при переопределении нельзя вызывать родительский метод.
   *
   * @param aItem &lt;T&gt; - редактируемый элемент, не бывает <code>null</code>
   * @return &lt;T&gt; - отредактированный элемент или <code>null</code>, если элемент не был создан
   * @throws TsUnsupportedFeatureRtException всегда выбрасывается в базовом классе
   */
  protected T doEditItem( T aItem ) {
    throw new TsUnsupportedFeatureRtException( MSG_ERR_NO_EDIT_ITEM_CODE );
  }

  /**
   * Наследник должен переопределить процесс удаления элемента.
   * <p>
   * В базовм классе выбрасывает исключение, при переопределении нельзя вызывать родительский метод.
   *
   * @param aItem &lt;T&gt; - удаляемый элемент, не бывает <code>null</code>
   * @return boolean - признак, что элемент был удален
   * @throws TsUnsupportedFeatureRtException всегда выбрасывается в базовом классе
   */
  protected boolean doRemoveItem( T aItem ) {
    throw new TsUnsupportedFeatureRtException( MSG_ERR_NO_REMOVE_ITEM_CODE );
  }

  /**
   * Наследник может (если поддерживается) пометить все элементы в списке.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   */
  protected void doCheckAll() {
    // nop
  }

  /**
   * Наследник может (если поддерживается) снять пометки со всех элементов в списке.
   * <p>
   * В базовом классе ничего не делает, при переопределении вызывать родительский метод не нужно.
   */
  protected void doUnCheckAll() {
    // nop
  }

  /**
   * Наследник в этом методе может заполнить {@link IM5TreeViewer#items()} экземплярвами сущности.
   * <p>
   * Метод необходимо переопределить, если на задан поставщик {@link #itemsProvider}.
   * <p>
   * В базовом классе заполняет элементы дерева из списка {@link IM5ItemsProvider#listItems()}, а если
   * {@link #itemsProvider} = <code>null</code>, очищает список элементов. При заполнении дерева выставляет
   * автоматическую ширину столбцов.
   * <p>
   * При переопределении вызывать родительский метод не нужно.
   */
  protected void doFillTree() {
    if( itemsProvider != null ) {
      tree.items().setAll( itemsProvider.listItems() );
    }
    else {
      tree.items().clear();
    }
    for( int i = 0; i < tree.columnManager().columns().size() - 1; i++ ) {
      IM5Column<T> col = tree.columnManager().columns().values().get( i );
      col.pack();
    }
  }

  /**
   * Наследник может освободить ресурсы, удалить слушатели иделать другую подчистку по закрытии панели.
   * <p>
   * Вызывается при уничтоении виджета панели. <b>Внимание:</b> если виджет не был создан, метод не вызвается!
   * <p>
   * В базовом классе ничего не делает, при переопределении вызвать родительский метод не нужно. Однако, при втором
   * уровне наследования, надо вызывать родитеьский метод в конце собственной реализации.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * Наследник должен определить, разрешено ли действие {@link IStdActions#SA_ADD}.
   * <p>
   * Вызывается только тогда, когда {@link #doGetAlive()} = <code>true</code>.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @param aSel &lt;T&gt; - текущий выбранный в дереве элемент или <code>null</code>
   * @return boolean - состояние разрешенности
   */
  protected boolean doGetIsAddAllowed( T aSel ) {
    return false;
  }

  /**
   * Наследник должен определить, разрешено ли действие {@link IStdActions#SA_EDIT}.
   * <p>
   * Вызывается только тогда, когда {@link #doGetAlive()} = <code>true</code>.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @param aSel &lt;T&gt; - текущий выбранный в дереве элемент или <code>null</code>
   * @return boolean - состояние разрешенности
   */
  protected boolean doGetIsEditAllowed( T aSel ) {
    return false;
  }

  /**
   * Наследник должен определить, разрешено ли действие {@link IStdActions#SA_REMOVE}.
   * <p>
   * Вызывается только тогда, когда {@link #doGetAlive()} = <code>true</code>.
   * <p>
   * В базовом классе просто возвращает <code>false</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @param aSel &lt;T&gt; - текущий выбранный в дереве элемент или <code>null</code>
   * @return boolean - состояние разрешенности
   */
  protected boolean doGetIsRemoveAllowed( T aSel ) {
    return false;
  }

}
