package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.impl.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IMultiPaneComponent} base implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneComponent<T>
    implements IMultiPaneComponent<T> {

  /**
   * {@link IMultiPaneComponent#treeModeManager()} implementation.
   *
   * @author hazard157
   */
  class InternalTreeModeMan
      extends TreeModeManager<T> {

    public InternalTreeModeMan( boolean aHasTreeMode ) {
      super( aHasTreeMode );
    }

    @Override
    protected void onCurrentModeIdChanged() {
      internalSetTreeGroupingMode( currModeId() );
    }

    @Override
    protected void onModeInfoesChanged() {
      updateToolbarMenus();
    }

  }

  private final IGenericChangeListener itemsChangeListener = aSource -> refresh();

  @SuppressWarnings( "unchecked" )
  private final IGenericChangeListener filterPaneChangeListener = aSource -> {
    if( filterPane().isFilterOn() ) {
      tree().filterManager().setFilter( filterPane().getFilter() );
    }
    else {
      tree().filterManager().setFilter( ITsFilter.ALL );
    }
    updateSummaryPane();
  };

  private final TsSelectionChangeEventHelper<T> selectionChangeEventHelper;
  private final TsDoubleClickEventHelper<T>     doubleClickEventHelper;
  private final TsKeyDownEventHelper            keyDownEventHelper;

  private final ITsTreeMaker<T>     tableMaker; // default tree maker makes plain list (table mode) of items
  private final IM5TreeViewer<T>    tree;
  private final ITreeModeManager<T> tmm;

  private IM5ItemsProvider<T> itemsProvider = IM5ItemsProvider.EMPTY;

  private TsComposite        board       = null;
  private ITsToolBar         toolbar     = null;
  private IMpcDetailsPane<T> detailsPane = null;
  private IMpcSummaryPane<T> summaryPane = null;
  private IMpcFilterPane<T>  filterPane  = null;

  private boolean editable       = true;
  private boolean isInternalEdit = false;

  /**
   * Constructor.
   *
   * @param aViewer {@link IM5TreeViewer} - the tree viewer to be used
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException viewer has {@link ILazyControl#getControl()} already initialized
   */
  public MultiPaneComponent( IM5TreeViewer<T> aViewer ) {
    tree = TsNullArgumentRtException.checkNull( aViewer );
    TsIllegalArgumentRtException.checkTrue( aViewer.getControl() != null );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this ) {

      @Override
      protected void afterSelectionEventFired( T aItem ) {
        updateDetailsPane();
        updateSummaryPane();
        updateActionsState();
      }
    };
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this ) {

      @Override
      protected boolean beforeDoubleClickEventFired( T aItem ) {
        String actId = OPDEF_DBLCLICK_ACTION_ID.getValue( tsContext().params() ).asString();
        if( StridUtils.isValidIdPath( actId ) ) {
          processAction( actId );
          return true;
        }
        return false;
      }
    };
    // first run user handler and then code in this class
    keyDownEventHelper = new TsKeyDownEventHelper( this ) {

      @Override
      public boolean fireTsKeyDownEvent( Event aEvent ) {
        if( super.fireTsKeyDownEvent( aEvent ) ) {
          return true;
        }
        return handleKeyDownEvent( aEvent );
      }
    };
    tableMaker = new M5DefaultTreeMaker<>( tree().model().entityClass() );
    tmm = new InternalTreeModeMan( OPDEF_IS_SUPPORTS_TREE.getValue( tsContext().params() ).asBool() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates toolbar lazy control.
   * <p>
   * Must be called after {@link #detailsPane}, {@link #summaryPane} and {@link #filterPane} were initialized.
   *
   * @return {@link ITsToolBar} - cvreated toolbar, never is <code>null</code>
   */
  private ITsToolBar createToolBar() {
    // actions/buttons on toolbar
    IListEdit<ITsActionDef> actDefs = new ElemLinkedBundleList<>();
    boolean hasEditActions = OPDEF_IS_ACTIONS_CRUD.getValue( tsContext().params() ).asBool();
    boolean hasReordererActions = OPDEF_IS_ACTIONS_REORDER.getValue( tsContext().params() ).asBool();
    boolean hasRefreshActions = OPDEF_IS_ACTIONS_REFRESH.getValue( tsContext().params() ).asBool();
    boolean hasCheckSupport = OPDEF_IS_SUPPORTS_CHECKS.getValue( tsContext().params() ).asBool();
    boolean hasTreeSupport = OPDEF_IS_SUPPORTS_TREE.getValue( tsContext().params() ).asBool();
    // CRUD buttons
    if( hasEditActions ) {
      actDefs.add( ACDEF_ADD );
      actDefs.add( ACDEF_EDIT );
      actDefs.add( ACDEF_REMOVE );
    }
    // refresh buttin
    if( hasRefreshActions ) {
      actDefs.add( ACDEF_REFRESH );
    }
    // tree mode buttons
    if( hasTreeSupport ) {
      if( hasEditActions ) {
        actDefs.add( ACDEF_SEPARATOR );
      }
      actDefs.add( ACDEF_VIEW_AS_TREE_MENU );
      actDefs.add( ACDEF_VIEW_AS_LIST );
      actDefs.add( ACDEF_SEPARATOR );
      actDefs.add( ACDEF_COLLAPSE_ALL );
      actDefs.add( ACDEF_EXPAND_ALL );
    }
    // check/uncheck all buttons
    if( hasCheckSupport ) {
      if( hasEditActions ) {
        actDefs.add( ACDEF_SEPARATOR );
      }
      actDefs.add( ACDEF_CHECK_ALL );
      actDefs.add( ACDEF_UNCHECK_ALL );
    }
    // reorder buttons
    if( hasReordererActions ) {
      if( hasEditActions ) {
        actDefs.add( ACDEF_SEPARATOR );
      }
      actDefs.add( ACDEF_LIST_ELEM_MOVE_FIRST );
      actDefs.add( ACDEF_LIST_ELEM_MOVE_UP );
      actDefs.add( ACDEF_LIST_ELEM_MOVE_DOWN );
      actDefs.add( ACDEF_LIST_ELEM_MOVE_LAST );
    }
    // panes hide/show buuttons
    if( OPDEF_IS_ACTIONS_HIDE_PANES.getValue( tsContext().params() ).asBool() ) {
      boolean isBtnHideFilter = filterPane != null;
      boolean isBtnHideDetails = detailsPane != null;
      boolean isBtnHideSummary = summaryPane != null;
      if( isBtnHideFilter || isBtnHideDetails || isBtnHideSummary ) {
        actDefs.add( ACDEF_SEPARATOR );
        if( isBtnHideFilter ) {
          actDefs.add( ACDEF_HIDE_FILTERS );
        }
        if( isBtnHideDetails ) {
          actDefs.add( ACDEF_HIDE_DETAILS );
        }
        if( isBtnHideSummary ) {
          actDefs.add( ACDEF_HIDE_SUMMARY );
        }
      }
    }
    // toolbar name
    String name = TsLibUtils.EMPTY_STRING;
    if( !model().nmName().isEmpty() ) {
      name = model().nmName() + ": "; //$NON-NLS-1$
    }
    // icon size
    EIconSize iconSize = hdpiService().getToolbarIconsSize();
    // toolbar creation
    ITsToolBar tb = doCreateToolbar( tsContext(), name, iconSize, actDefs );
    TsInternalErrorRtException.checkNull( tb );
    return tb;
  }

  /**
   * Executes an action.
   * <p>
   * If action is present in toolbar and it is disabled than action will not be executed.
   *
   * @param aActionId String - the actonID
   */
  public void processAction( String aActionId ) {
    if( board == null || (toolbar.findAction( aActionId ) != null && !toolbar.isActionEnabled( aActionId )) ) {
      return;
    }
    T sel = tree.selectedItem();
    switch( aActionId ) {
      case ACTID_ADD: {
        if( isEditable() ) {
          T item = doAddItem();
          if( item != null ) {
            fillViewer( item );
          }
        }
        break;
      }
      case ACTID_EDIT: {
        if( isEditable() ) {
          TsInternalErrorRtException.checkNull( sel );
          T item = doEditItem( sel );
          if( item != null ) {
            fillViewer( item );
          }
        }
        break;
      }
      case ACTID_REMOVE: {
        if( isEditable() ) {
          TsInternalErrorRtException.checkNull( sel );
          T toSel = findItemToSelectAfterRemove( sel );
          if( doRemoveItem( sel ) ) {
            fillViewer( toSel );
          }
        }
        break;
      }
      case ACTID_REFRESH: {
        if( OPDEF_IS_ACTIONS_REFRESH.getValue( tsContext().params() ).asBool() ) {
          refresh();
        }
        break;
      }
      case ACTID_EXPAND_ALL: {
        tree.console().expandAll();
        break;
      }
      case ACTID_COLLAPSE_ALL: {
        tree.console().collapseAll();
        break;
      }
      case ACTID_VIEW_AS_TREE: {
        String nextModeId;
        if( tmm.isCurrentTreeMode() ) { // if in tree mode then select next grouping mode
          nextModeId = ETsCollMove.NEXT.findElemAtWni( tmm.currModeId(), tmm.treeModeInfoes().keys(), 10, true );
        }
        else { // if in table mode select last tree grouping mode
          nextModeId = tmm.lastModeId();
        }
        tmm.setCurrentMode( nextModeId );
        break;
      }
      case ACTID_VIEW_AS_LIST: {
        tmm.setCurrentMode( null );
        break;
      }
      case ACTID_LIST_ELEM_MOVE_FIRST: {
        if( sel != null && isEditable() ) {
          if( itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveFirst( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case ACTID_LIST_ELEM_MOVE_UP: {
        if( sel != null && isEditable() ) {
          if( itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().movePrev( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case ACTID_LIST_ELEM_MOVE_DOWN: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveNext( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case ACTID_LIST_ELEM_MOVE_LAST: {
        if( sel != null && isEditable() ) {
          if( itemsProvider != null && itemsProvider.reorderer() != null ) {
            itemsProvider.reorderer().moveLast( sel );
            fillViewer( sel );
          }
        }
        break;
      }
      case ACTID_CHECK_ALL:
        doCheckAll();
        break;
      case ACTID_UNCHECK_ALL:
        doUnCheckAll();
        break;
      case ACTID_HIDE_FILTER: {
        if( filterPane != null ) {
          setFilterPaneVisible( !filterPane.getControl().getVisible() );
        }
        break;
      }
      case ACTID_HIDE_DETAILS: {
        if( detailsPane != null ) {
          setDetailsPaneVisible( !detailsPane.getControl().getVisible() );
        }
        break;
      }
      case ACTID_HIDE_SUMMARY: {
        if( summaryPane != null ) {
          setSummaryPaneVisible( !summaryPane.getControl().getVisible() );
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

  /**
   * Refreshes the state of the action buttons on toolbar.
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
    toolbar.setActionEnabled( ACTID_ADD, editable && isCreationAllowed );
    toolbar.setActionEnabled( ACTID_EDIT, editable && isEditingAllowed );
    toolbar.setActionEnabled( ACTID_REMOVE, editable && isRemovalAllowed );
    toolbar.setActionEnabled( ACTID_REFRESH,
        isAlive && OPDEF_IS_ACTIONS_REFRESH.getValue( tsContext().params() ).asBool() );
    toolbar.setActionEnabled( ACTID_VIEW_AS_TREE, tmm.hasTreeMode() );
    toolbar.setActionEnabled( ACTID_VIEW_AS_LIST, tmm.hasTreeMode() );
    toolbar.setActionEnabled( ACTID_EXPAND_ALL, tmm.isCurrentTreeMode() );
    toolbar.setActionEnabled( ACTID_COLLAPSE_ALL, tmm.isCurrentTreeMode() );
    toolbar.setActionChecked( ACTID_VIEW_AS_TREE, tmm.isCurrentTreeMode() );
    toolbar.setActionChecked( ACTID_VIEW_AS_LIST, !tmm.isCurrentTreeMode() );
    boolean hasReordererActions = OPDEF_IS_ACTIONS_REORDER.getValue( tsContext().params() ).asBool();
    if( hasReordererActions ) {
      boolean isReorderAllowed = isAlive && itemsProvider != null && itemsProvider.reorderer() != null;
      if( isReorderAllowed ) {
        int index = -1;
        IList<T> list = itemsProvider.reorderer().list();
        if( sel != null ) {
          index = list.indexOf( sel );
        }
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_FIRST, index > 0 );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_UP, index > 0 );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_DOWN, index >= 0 && index < list.size() - 1 );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_LAST, index >= 0 && index < list.size() - 1 );
      }
      else {
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_FIRST, false );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_UP, false );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_DOWN, false );
        toolbar.setActionEnabled( ACTID_LIST_ELEM_MOVE_LAST, false );
      }
    }
    if( filterPane != null && toolbar.listButtonItems().hasElem( ACDEF_HIDE_FILTERS ) ) {
      toolbar.setActionChecked( ACTID_HIDE_FILTER, filterPane.getControl().getVisible() );
    }
    if( detailsPane != null && toolbar.listButtonItems().hasElem( ACDEF_HIDE_DETAILS ) ) {
      toolbar.setActionChecked( ACTID_HIDE_DETAILS, detailsPane.getControl().getVisible() );
    }
    if( summaryPane != null && toolbar.listButtonItems().hasElem( ACDEF_HIDE_SUMMARY ) ) {
      toolbar.setActionChecked( ACTID_HIDE_SUMMARY, summaryPane.getControl().getVisible() );
    }
    doUpdateActionsState( isAlive, isSel, sel );
  }

  /**
   * Changes {@link IM5TreeViewer#treeMaker()} depending on tree mode.
   *
   * @param aModeId String - the tree mode ID from the list of {@link ITreeModeManager#treeModeInfoes()}
   */
  void internalSetTreeGroupingMode( String aModeId ) {
    if( board != null ) {
      T sel = tree.selectedItem();
      if( aModeId != null ) {
        TreeModeInfo<T> modeInfo = tmm.treeModeInfoes().getByKey( aModeId );
        tree.setTreeMaker( modeInfo.treeMaker() );
      }
      else {
        tree.setTreeMaker( tableMaker );
      }
      tree.setSelectedItem( sel );
    }
  }

  /**
   * Updates drop down menu of tree modes in toolbar.
   */
  void updateToolbarMenus() {
    if( toolbar != null ) {
      toolbar.setActionMenu( ACTID_VIEW_AS_TREE, new TreeModeDropDownMenuCreator<>( tsContext(), treeModeManager() ) );
    }
  }

  /**
   * Builtin key press handler.
   *
   * @param aEvent {@link Event} - key down event
   * @return boolean - a flag that keyboard key was handled
   */
  boolean handleKeyDownEvent( Event aEvent ) {
    String actionId = TsLibUtils.EMPTY_STRING;
    switch( aEvent.keyCode ) {
      case SWT.DEL:
        actionId = ACTID_REMOVE;
        break;
      case SWT.INSERT:
        actionId = ACTID_ADD;
        break;
      case SWT.CR:
      case SWT.KEYPAD_CR:
        actionId = OPDEF_DBLCLICK_ACTION_ID.getValue( tsContext().params() ).asString();
        break;
      case SWT.F5:
        if( OPDEF_IS_ACTIONS_REFRESH.getValue( tsContext().params() ).asBool() ) {
          actionId = ACTID_REFRESH;
        }
        break;
      case SWT.F6:
        actionId = ACTID_HIDE_DETAILS;
        break;
      case SWT.F7:
        actionId = ACTID_HIDE_SUMMARY;
        break;
      default:
        return false;
    }
    if( StridUtils.isValidIdPath( actionId ) ) {
      processAction( actionId );
    }
    return true;
  }

  /**
   * Called when SWT widget is disposed.
   */
  private void internalDispose() {
    itemsProvider.genericChangeEventer().removeListener( itemsChangeListener );
    doDispose();
  }

  /**
   * Returns an item to be selected if the specified item will be removed.
   *
   * @param aSel &lt;T&gt; - item to be removed
   * @return &lt;T&gt; - item to select after removal or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
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

  void updateDetailsPane() {
    if( detailsPane != null ) {
      T selItem = tree.selectedItem();
      ITsNode selNode = (ITsNode)tree.console().selectedNode();
      IM5Bunch<T> values = selItem == null ? null : model().valuesOf( selItem );
      detailsPane.setValues( selNode, values );
    }
  }

  void updateSummaryPane() {
    if( summaryPane != null ) {
      ITsNode selNode = (ITsNode)tree.console().selectedNode();
      summaryPane.updateSummary( selNode, selectedItem(), tree.items(), tree.filterManager().items() );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tree.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public TsComposite createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( board );
    // create all lazy controls (tree is set in constructor)
    if( OPDEF_IS_DETAILS_PANE.getValue( tsContext().params() ).asBool() ) {
      detailsPane = doCreateDetailsPane();
    }
    if( OPDEF_IS_FILTER_PANE.getValue( tsContext().params() ).asBool() ) {
      filterPane = doCreateFilterPane();
    }
    if( OPDEF_IS_SUMMARY_PANE.getValue( tsContext().params() ).asBool() ) {
      summaryPane = doCreateSummaryPane();
    }
    toolbar = createToolBar(); // must call after all xxxPane were inited
    // create widgets
    board = new TsComposite( aParent, SWT.BORDER );
    board.setLayout( new BorderLayout() );
    // toolbar is always on boards top at whole width
    toolbar.createControl( board );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aActionId -> processAction( aActionId ) );
    // toolbar always exists but may be not visible
    toolbar.getControl().setVisible( OPDEF_IS_TOOLBAR.getValue( tsContext().params() ).asBool() );
    // board with remaining panes (filterPane, tree, detailsPane, summaryPane)
    TsComposite board1 = new TsComposite( board );
    board1.setLayout( new BorderLayout() );
    board1.setLayoutData( BorderLayout.CENTER );
    // filterPane
    if( filterPane != null ) {
      filterPane.createControl( board1 );
      filterPane.getControl().setLayoutData( BorderLayout.NORTH );
    }
    // board with remaining panes (tree, detailsPane, summaryPane)
    TsComposite board2 = new TsComposite( board1 );
    board2.setLayout( new BorderLayout() );
    board2.setLayoutData( BorderLayout.CENTER );
    // summaryPane
    if( summaryPane != null ) {
      summaryPane.createControl( board2 );
      summaryPane.getControl().setLayoutData( BorderLayout.SOUTH );
    }
    // board with remaining panes (tree, detailsPane)
    if( detailsPane != null ) { // есть detailsPane, расположим ее и tree на SashForm
      TsComposite board3 = new TsComposite( board2 );
      board3.setLayout( new BorderLayout() );
      board3.setLayoutData( BorderLayout.CENTER );
      tree.createControl( board3 );
      detailsPane.createControl( board3 );
      detailsPane.getControl().setLayoutData( OPDEF_DETAILS_PANE_PLACE.getValue( tsContext().params() ).asValobj() );
    }
    else { // no detailsPane
      tree.createControl( board2 );
    }
    tree.getControl().setLayoutData( BorderLayout.CENTER );
    tree.setIconSize( OPDEF_NODE_ICON_SIZE.getValue( tsContext().params() ).asValobj() );
    tree.addTsSelectionListener( selectionChangeEventHelper );
    tree.addTsDoubleClickListener( doubleClickEventHelper );
    // setup
    doCreateTreeColumns();
    updateToolbarMenus();
    tmm.setHasTreeMode( OPDEF_IS_SUPPORTS_TREE.getValue( tsContext().params() ).asBool() );
    tree.addTsKeyDownListener( keyDownEventHelper );
    tree.getControl().addDisposeListener( aEvent -> internalDispose() );
    internalSetTreeGroupingMode( treeModeManager().currModeId() );
    if( filterPane != null ) {
      filterPane.genericChangeEventer().addListener( filterPaneChangeListener );
    }
    // set panels hide/show initial state
    if( detailsPane != null ) {
      detailsPane.getControl().setVisible( !OPDEF_IS_DETAILS_PANE_HIDDEN.getValue( tsContext().params() ).asBool() );
    }
    if( filterPane != null ) {
      filterPane.getControl().setVisible( !OPDEF_IS_FILTER_PANE_HIDDEN.getValue( tsContext().params() ).asBool() );
    }
    if( summaryPane != null ) {
      summaryPane.getControl().setVisible( !OPDEF_IS_SUMMARY_PANE_HIDDEN.getValue( tsContext().params() ).asBool() );
    }
    // TODO MultiPaneComponent.createControl()
    doAfterCreateControls();
    board.getParent().layout( true, true );
    refresh();
    return board;
  }

  @Override
  public TsComposite getControl() {
    return board;
  }

  // ------------------------------------------------------------------------------------
  // IM5ModelRelated
  //

  @Override
  public IM5Model<T> model() {
    return tree.model();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
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
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyDownEventProducer
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
  // IMultiPaneComponent
  //

  @Override
  public IM5TreeViewer<T> tree() {
    return tree;
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
  public void refresh() {
    fillViewer( tree.selectedItem() );
  }

  @Override
  public ITreeModeManager<T> treeModeManager() {
    return tmm;
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
  public IM5ItemsProvider<T> itemsProvider() {
    return itemsProvider;
  }

  @Override
  public void setItemProvider( IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aItemsProvider );
    itemsProvider.genericChangeEventer().removeListener( itemsChangeListener );
    itemsProvider = aItemsProvider;
    itemsProvider.genericChangeEventer().addListener( itemsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the details pane.
   *
   * @return {@link IMpcDetailsPane} - the details pane or <code>null</code>
   */
  public IMpcDetailsPane<T> detailsPane() {
    return detailsPane;
  }

  /**
   * Show/hides details pane if it exists.
   *
   * @param aVisible boolean - visibility flag
   */
  public void setDetailsPaneVisible( boolean aVisible ) {
    if( detailsPane != null ) {
      detailsPane.getControl().setVisible( aVisible );
      board.layout( true, true );
    }
  }

  /**
   * Returns the summary pane.
   *
   * @return {@link IMpcSummaryPane} - the summary pane or <code>null</code>
   */
  public IMpcSummaryPane<T> summaryPane() {
    return summaryPane;
  }

  /**
   * Show/hides summary pane if it exists.
   *
   * @param aVisible boolean - visibility flag
   */
  public void setSummaryPaneVisible( boolean aVisible ) {
    if( summaryPane != null ) {
      summaryPane.getControl().setVisible( aVisible );
      board.layout( true, true );
    }
  }

  /**
   * Returns the filter pane.
   *
   * @return {@link IMpcDetailsPane} - the filter pane or <code>null</code>
   */
  public IMpcFilterPane<T> filterPane() {
    return filterPane;
  }

  /**
   * Show/hides filter pane if it exists.
   *
   * @param aVisible boolean - visibility flag
   */
  public void setFilterPaneVisible( boolean aVisible ) {
    if( filterPane != null ) {
      filterPane.getControl().setVisible( aVisible );
      board.layout( true, true );
    }
  }

  /**
   * Returns the toolbar.
   * <p>
   * Even if unvisible, toolbar always exists with actions on it.
   *
   * @return {@link ITsToolBar} - the toolbar
   */
  public ITsToolBar toolbar() {
    return toolbar;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may adjust created toolbar.
   * <p>
   * In base class simply creates toolbar, sets icons size, name labes and actions.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aName String - name of the toolbar
   * @param aIconSize {@link EIconSize} - toolbar icons size
   * @param aActs IList&lt;{@link ITsActionDef}&gt; - actions for buttons creation
   * @return {@link ITsToolBar} - created toolbar, must not be <code>null</code>
   */
  protected ITsToolBar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    ITsToolBar tb = new TsToolBar( aContext );
    tb.setIconSize( aIconSize );
    tb.setNameLabelText( aName );
    tb.addActionDefs( aActs );
    return tb;
  }

  /**
   * Subclass may create its own implemenation of details pane {@link IMpcDetailsPane}.
   * <p>
   * In the base class, if at least one M5-field has {@link IM5Constants#M5FF_DETAIL} details hint flag, creates
   * {@link MpcDetailsPaneWrapper} instance wrapped over default details pane created via
   * {@link IM5PanelCreator#createEntityDetailsPanel(ITsGuiContext)}. If no field has DETAILS hint then returns
   * <code>null</code>.
   * <p>
   * Method is called from {@link #createControl(Composite)}, and only if
   * {@link IMultiPaneComponentConstants#OPDEF_IS_DETAILS_PANE} option is set.
   *
   * @return {@link IMpcDetailsPane} - details pane or <code>null</code> if no details to show
   */
  protected IMpcDetailsPane<T> doCreateDetailsPane() {
    boolean needDetailsPane = false;
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_DETAIL) != 0 ) {
        needDetailsPane = true;
      }
    }
    if( needDetailsPane ) {
      IM5EntityPanel<T> panel = model().panelCreator().createEntityDetailsPanel( tsContext() );
      return new MpcDetailsPaneWrapper<>( this, panel );
    }
    return null;
  }

  /**
   * Subclass may create its own implemenation of filter pane {@link IMpcFilterPane}.
   * <p>
   * In the base class, if at least one M5-field has {@link IM5Constants#M5FF_COLUMN} details hint flag, creates
   * {@link MpcFilterPaneWrapper} instance wrapped over default filter pane created via
   * {@link IM5PanelCreator#createFilterPanel(ITsGuiContext)}. If no field has COLUMN hint then returns
   * <code>null</code>.
   * <p>
   * Method is called from {@link #createControl(Composite)}, and only if
   * {@link IMultiPaneComponentConstants#OPDEF_IS_FILTER_PANE} option is set.
   *
   * @return {@link IMpcFilterPane} - filter pane or <code>null</code> if no filter is provided
   */
  protected IMpcFilterPane<T> doCreateFilterPane() {
    boolean needFilterPane = false;
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_COLUMN) != 0 ) {
        needFilterPane = true;
      }
    }
    if( needFilterPane ) {
      IM5FilterPanel<T> m5fp = model().panelCreator().createFilterPanel( tsContext() );
      return new MpcFilterPaneWrapper<>( this, m5fp );
    }
    return null;
  }

  /**
   * Subclass may create its own implemenation of summary pane {@link IMpcSummaryPane}.
   * <p>
   * <p>
   * In the base class creates {@link MpcSummaryPaneMessage} instance with message provider
   * {@link MpcSummaryPaneMessage#DEFAULT_MESSAGE_RPOVIDER}.
   * <p>
   * Method is called from {@link #createControl(Composite)}, and only if
   * {@link IMultiPaneComponentConstants#OPDEF_IS_SUMMARY_PANE} option is set.
   *
   * @return {@link IMpcSummaryPane} - summary pane or <code>null</code> if no summary is needed
   */
  protected IMpcSummaryPane<T> doCreateSummaryPane() {
    return new MpcSummaryPaneMessage<T>( this, MpcSummaryPaneMessage.DEFAULT_MESSAGE_RPOVIDER );
  }

  /**
   * Subclass may create its own colemns in {@link #tree()}.
   * <p>
   * In the base class creates columns for fields flagged with {@link IM5Constants#M5FF_COLUMN} hint.
   */
  protected void doCreateTreeColumns() {
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_COLUMN) != 0 ) {
        IM5Column<T> col = tree().columnManager().add( fdef.id() );
        EHorAlignment ha = M5_OPDEF_COLUMN_ALIGN.getValue( fdef.params() ).asValobj();
        col.setAlignment( ha );
      }
    }
    // mvk WORKAROUND single column has narrow width in MS Windows
    if( tree.columnManager().columns().size() == 1 ) {
      tree.columnManager().columns().values().get( 0 ).setWidth( 900 );
    }
  }

  /**
   * Subclass may release resources allocated at control creation.
   * <p>
   * Warning: if widget wa snot cvreated then this method will not be called.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * Subclass may perform additional setup immediately afetr controls creation in {@link #createControl(Composite)}.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   */
  protected void doAfterCreateControls() {
    // nop
  }

  /**
   * Subclass must (if allowed) implement new item addition to the collection.
   * <p>
   * Common usage is to display some dialog for item creation and then add item to collection.
   * <p>
   * Throws exceptions in the base class; when overridden, it is not allowed to call the parent method.
   *
   * @return &lt;T&gt; - created and/or adde item or <code>null</code> if no item was added
   * @throws TsUnsupportedFeatureRtException in base class
   */
  protected T doAddItem() {
    throw new TsUnsupportedFeatureRtException( MSG_ERR_NO_ADD_ITEM_CODE );
  }

  // TODO TRANSLATE

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
   * Subclass may override "check all items" action.
   * <p>
   * In base class sets all items to checked state via method {@link ITsCheckSupport#setAllItemsCheckState(boolean)
   * tree().checks().setAllItemsCheckState( <b>true</b> )}.
   */
  protected void doCheckAll() {
    if( tree.checks().isChecksSupported() ) {
      tree.checks().setAllItemsCheckState( true );
    }
  }

  /**
   * Subclass may override "uncheck all items" action.
   * <p>
   * In base class sets all items to unchecked state via method {@link ITsCheckSupport#setAllItemsCheckState(boolean)
   * tree().checks().setAllItemsCheckState( <b>false</b> )}.
   */
  protected void doUnCheckAll() {
    if( tree.checks().isChecksSupported() ) {
      tree.checks().setAllItemsCheckState( false );
    }
  }

  /**
   * Here subclass must handle additional user actions added to the toolbar.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   *
   * @param aActionId String - the action ID
   */
  protected void doProcessAction( String aActionId ) {
    // nop
  }

  /**
   * Here subclass must set state of the additional user action buttons added to the toolbar.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   *
   * @param aIsAlive boolean - the flag that panel is alive
   * @param aIsSel boolean - the flag that there is non-<code>null</code> selected item
   * @param aSel &lt;T&gt; - selected item may be <code>null</code>
   */
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, T aSel ) {
    // nop
  }

  /**
   * Subclass may perform arbitrary code after any action was processed.
   * <p>
   * Method is called after both builtin and user actions processing.
   * <p>
   * Does nothing in base class, there is no need to call superclass method when overriding.
   *
   * @param aActionId String - the ID of the action just processed
   */
  protected void doAfterActionProcessed( String aActionId ) {
    // nop
  }

  /**
   * Subclass must return the flag that panel is alive.
   * <p>
   * "Живость" панели означает, что с содержимым можно работать. "Мертвая" же панель запрещает все известные базовому
   * классу редактирующие действия (и наследник в {@link #doUpdateActionsState(boolean, boolean, Object)} должен
   * запрещать все действия с данными). Например, при правке списка пользователей системы S5, панель "мертвая", если нет
   * соединения с сервером. При появлении же соединения, наследник должен вызвать {@link #fillViewer(Object)}, что
   * приведет к обновлению содержимого и состояния панели.
   * <p>
   * В базовом классе возвращает <code>true</code>, при переопределении вызывать родительский метод не нужно.
   *
   * @return boolean - the flag that panel is alive
   */
  protected boolean doGetAlive() {
    return true;
  }

  /**
   * Наследник должен определить, разрешено ли действие {@link ITsStdActionDefs#ACDEF_ADD}.
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
   * Наследник должен определить, разрешено ли действие {@link ITsStdActionDefs#ACDEF_EDIT}.
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
   * Наследник должен определить, разрешено ли действие {@link ITsStdActionDefs#ACDEF_REMOVE}.
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
    tree.items().setAll( itemsProvider.listItems() );
    for( int i = 0; i < tree.columnManager().columns().size() - 1; i++ ) {
      IM5Column<T> col = tree.columnManager().columns().values().get( i );
      col.pack();
    }
  }

}
