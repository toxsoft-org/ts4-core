package org.toxsoft.core.tsgui.bricks.cond.impl;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.cond.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.cond.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.dialogs.misc.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to edit {@link ITsCombiCondInfoBuilder#singlesMap()}.
 *
 * @author hazard157
 */
class PanelCcBuilderSinglesList
    extends AbstractLazyPanel<Control>
    implements IGenericChangeEventCapable {

  private static final ITsActionDef ACDEF_ADD_ALL_ABSENT = TsActionDef.ofTemplate( ACDEF_ADD_ALL, //
      TSID_NAME, STR_ADD_ALL_ABSENT, //
      TSID_DESCRIPTION, STR_ADD_ALL_ABSENT_D //
  );

  private static final ITsActionDef ACDEF_REMOVE_ALL_UNUSED = TsActionDef.ofTemplate( ACDEF_CLEAR, //
      TSID_NAME, STR_REMOVE_ALL_UNUSED, //
      TSID_DESCRIPTION, STR_REMOVE_ALL_UNUSED_D //
  );

  /**
   * Local actions to work with {@link ITsCombiCondInfoBuilder#singlesMap()}.
   *
   * @author hazard157
   */
  class AspLocal
      extends MethodPerActionTsActionSetProvider {

    public AspLocal() {
      defineAction( ACDEF_ADD, this::runAddSingle, PanelCcBuilderSinglesList.this::isEditable );
      defineAction( ACDEF_REMOVE, this::runRemoveSingle, this::isSelectedSingle );
      defineSeparator();
      defineAction( ACDEF_ADD_ALL_ABSENT, this::runAddAbsentSingles, this::isAbsentSingles );
      defineAction( ACDEF_REMOVE_ALL_UNUSED, this::runRemoveUnusedSingles, this::isUnusedSingles );
    }

    void runAddSingle() {
      ITsDialogInfo di = new TsDialogInfo( tsContext(), DLG_CONDITION_ID, DLG_CONDITION_ID_D );
      String scId = DialogAskValue.askIdPath( di, EMPTY_STRING, EMPTY_STRING );
      if( scId == null ) {
        return;
      }
      ccBuilder.putSingle( scId, ITsSingleCondInfo.ALWAYS );
      refresh();
      scListPanel.setSelectedItem( scId );
      eventer.fireChangeEvent();
    }

    boolean isSelectedSingle() {
      return editable && (scListPanel.selectedItem() != null);
    }

    void runRemoveSingle() {
      String selId = scListPanel.selectedItem();
      TsInternalErrorRtException.checkNull( selId );
      String toSelId = scListPanel.items().next( selId );
      if( toSelId == null ) {
        toSelId = scListPanel.items().last();
      }
      if( TsDialogUtils.askYesNoCancel( getShell(), STR_ASK_REMOVE_KEYWORD, toSelId ) != ETsDialogCode.YES ) {
        return;
      }
      ccBuilder.removeSingle( toSelId );
      refresh();
      scListPanel.setSelectedItem( toSelId );
      eventer.fireChangeEvent();
    }

    void runAddAbsentSingles() {
      if( ccBuilder.listAbsentSingleIds().isEmpty() ) {
        return;
      }
      for( String s : ccBuilder.listAbsentSingleIds() ) {
        ccBuilder.putSingle( s, ITsSingleCondInfo.NEVER );
      }
      refresh();
      eventer.fireChangeEvent();
    }

    boolean isAbsentSingles() {
      return editable && !ccBuilder.listAbsentSingleIds().isEmpty();
    }

    void runRemoveUnusedSingles() {
      int n = ccBuilder.listUnusedSingleIds().size();
      if( n == 0 ) {
        return;
      }
      if( TsDialogUtils.askYesNoCancel( getShell(), FMT_ASK_REMOVE_ALL_UNUSED,
          Integer.valueOf( n ) ) != ETsDialogCode.YES ) {
        return;
      }
      for( String s : ccBuilder.listUnusedSingleIds() ) {
        ccBuilder.removeSingle( s );
      }
      refresh();
      eventer.fireChangeEvent();
    }

    boolean isUnusedSingles() {
      return editable && !ccBuilder.listUnusedSingleIds().isEmpty();
    }

  }

  private final GenericChangeEventer eventer;
  private final ITsActionSetProvider asp = new AspLocal();

  private ITsCombiCondInfoBuilder ccBuilder; // never is null

  private TsToolbar                      toolbar     = null;
  private ProvidedItemsCollPanel<String> scListPanel = null;
  private IPanelSingleCondInfo           scInfoPanel = null;

  private boolean editable = true;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aBuilder {@link ITsCombiCondInfoBuilder} - builder to be used initially
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelCcBuilderSinglesList( ITsGuiContext aContext, ITsCombiCondInfoBuilder aBuilder ) {
    super( aContext );
    TsNullArgumentRtException.checkNull( aBuilder );
    eventer = new GenericChangeEventer( this );
    ccBuilder = aBuilder;
    asp.actionsStateEventer().addListener( s -> updateActionsState() );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite board = new Composite( aParent, SWT.NONE );
    board.setLayout( new BorderLayout() );
    // NORTH: toolbar
    toolbar = TsToolbar.create( board, tsContext(), asp.listAllActionDefs() );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // CENTER: sash form
    SashForm sfMain = new SashForm( board, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    // SashForm left pane: single filter IDs list
    scListPanel = new ProvidedItemsCollPanel<>( tsContext(), true );
    scListPanel.createControl( sfMain );
    // SashForm right pane: selected single condition info editor
    scInfoPanel = new PanelSingleCondInfo( tsContext(), !editable );
    scInfoPanel.createControl( sfMain );
    // setup
    sfMain.setWeights( 2500, 7500 );
    toolbar.addListener( asp );
    scInfoPanel.genericChangeEventer().addListener( s -> whenSingleInfoChanged() );
    scInfoPanel.setEditable( false );
    scListPanel.setItemsProvider( () -> ccBuilder.singlesMap().keys() );
    scListPanel.setVisualsProvider( ITsVisualsProvider.DEFAULT );
    scListPanel.addTsSelectionListener( ( src, sel ) -> whenScListPanelSelectionChanges() );
    refresh();
    updateActionsState();
    return board;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenSingleInfoChanged() {
    if( !scInfoPanel.canGetEntity().isError() ) {
      ITsSingleCondInfo scInf = scInfoPanel.getEntity();
      String scId = scListPanel.selectedItem();
      TsInternalErrorRtException.checkNull( scId );
      ccBuilder.putSingle( scId, scInf );
    }
    eventer.fireChangeEvent();
  }

  private void updateActionsState() {
    for( String actId : asp.listHandledActionIds() ) {
      toolbar.setActionEnabled( actId, asp.isActionEnabled( actId ) );
      toolbar.setActionChecked( actId, asp.isActionChecked( actId ) );
    }
  }

  private void updateScInfoPanel() {
    String scId = scListPanel.selectedItem();
    ITsSingleCondInfo sci = null;
    if( scId != null ) {
      sci = ccBuilder.singlesMap().findByKey( scId );
    }
    scInfoPanel.setEntity( sci );
    scInfoPanel.setEditable( sci != null );
  }

  private void whenScListPanelSelectionChanges() {
    updateScInfoPanel();
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Refresh panel content when {@link #getCombiCondInfoBuilder()} content changes from outside.
   */
  void refresh() {
    scListPanel.refresh();
    updateScInfoPanel();
    updateActionsState();
  }

  ITsCombiCondInfoBuilder getCombiCondInfoBuilder() {
    return ccBuilder;
  }

  void setCombiCondInfoBuilder( ITsCombiCondInfoBuilder aBuilder ) {
    TsNullArgumentRtException.checkNull( aBuilder );
    ccBuilder = aBuilder;
    scInfoPanel.setTopicManager( ccBuilder.topicManager() );
    refresh();
  }

  boolean isEditable() {
    return editable;
  }

  void setEditable( boolean aEditable ) {
    if( editable != aEditable ) {
      editable = aEditable;
      // scListPanel is a viewer, editing list is done via AspLocal
      scInfoPanel.setEditable( editable );
      updateActionsState();
    }
  }

}
