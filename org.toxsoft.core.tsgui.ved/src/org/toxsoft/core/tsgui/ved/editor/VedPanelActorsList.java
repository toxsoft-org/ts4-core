package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.m5.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GUI The pane contains list of VED model actors {@link IVedScreenModel#actors()}.
 *
 * @author hazard157
 */
public class VedPanelActorsList
    extends TsStdEventsProducerPanel<IVedActor> {

  private final IVedScreen vedScreen;

  private final MpcActors panel;

  /**
   * Многокомпонентная панель отображения списка акторов.
   *
   * @author vs
   */
  class MpcActors
      extends MultiPaneComponentModown<IVedActor> {

    enum EViewMode {
      ALL,
      LINKED,
      NONLINKED
    }

    class LinkedFilter
        implements ITsFilter<IVedActor> {

      IStringList actorIds = IStringList.EMPTY;

      @Override
      public boolean accept( IVedActor aActor ) {
        return actorIds.hasElem( aActor.id() );
      }

      public void setSelectedVisel( String aViselId ) {
        if( aViselId == null ) {
          actorIds = IStringList.EMPTY;
        }
        else {
          actorIds = VedScreenUtils.viselActorIds( aViselId, vedScreen );
        }
      }
    }

    class UnboundFilter
        implements ITsFilter<IVedActor> {

      IStringList actorIds = IStringList.EMPTY;

      @Override
      public boolean accept( IVedActor aActor ) {
        return actorIds.hasElem( aActor.id() );
      }

      public void update() {
        actorIds = VedScreenUtils.listUnboundActorIds( vedScreen );
      }
    }

    private static final String ACTID_VIEW_ALL       = "all";       //$NON-NLS-1$
    private static final String ACTID_VIEW_LINKED    = "linked";    //$NON-NLS-1$
    private static final String ACTID_VIEW_NONLINKED = "nonlinked"; //$NON-NLS-1$

    private static final ITsActionDef actViewAll = TsActionDef.ofRadio2( ACTID_VIEW_ALL, //
        STR_L_VIEW_ALL, STR_L_VIEW_ALL_D, ICONID_ALL_ITEMS );

    private static final ITsActionDef actViewLinked = TsActionDef.ofRadio2( ACTID_VIEW_LINKED, //
        STR_L_VIEW_LINKED, STR_L_VIEW_LINKED_D, ICONID_LINKED );

    private static final ITsActionDef actViewNonlinked = TsActionDef.ofRadio2( ACTID_VIEW_NONLINKED, //
        STR_L_VIEW_UNBOUND, STR_L_VIEW_UNBOUND_D, ICONID_NON_LINKED );

    EViewMode viewMode = EViewMode.ALL;

    private final IVedViselSelectionManager selectionManager;

    private final LinkedFilter linkedFilter = new LinkedFilter();

    private final UnboundFilter unboundFilter = new UnboundFilter();

    public MpcActors( ITsGuiContext aContext, IM5Model<IVedActor> aModel, IM5ItemsProvider<IVedActor> aItemsProvider,
        IM5LifecycleManager<IVedActor> aLifecycleManager, IVedViselSelectionManager aSelectionManager ) {
      super( aContext, aModel, aItemsProvider, aLifecycleManager );
      selectionManager = aSelectionManager;
      if( selectionManager != null ) {
        selectionManager.genericChangeEventer().addListener( aSource -> {
          switch( selectionManager.selectionKind() ) {
            case NONE:
            case MULTI:
              linkedFilter.setSelectedVisel( null );
              tree().refresh();
              break;
            case SINGLE:
              linkedFilter.setSelectedVisel( selectionManager.singleSelectedViselId() );
              tree().refresh();
              break;
            default:
              throw new IllegalArgumentException( "Unexpected value: " + selectionManager.selectionKind() ); //$NON-NLS-1$
          }

        } );
      }
    }

    @Override
    protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
        IListEdit<ITsActionDef> aActs ) {
      if( selectionManager != null ) {
        aActs.add( ACDEF_SEPARATOR );
        aActs.add( actViewAll );
        aActs.add( actViewLinked );
        aActs.add( actViewNonlinked );
      }
      return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
    }

    @Override
    protected void doProcessAction( String aActionId ) {
      switch( aActionId ) {
        case ACTID_VIEW_ALL: {
          viewMode = EViewMode.ALL;
          tree().filterManager().setFilter( ITsFilter.ALL );
        }
          break;
        case ACTID_VIEW_LINKED: {
          viewMode = EViewMode.LINKED;
          tree().filterManager().setFilter( linkedFilter );
        }
          break;
        case ACTID_VIEW_NONLINKED: {
          viewMode = EViewMode.NONLINKED;
          unboundFilter.update();
          tree().filterManager().setFilter( unboundFilter );
        }
          break;
        default:
          super.doProcessAction( aActionId );
      }
    }

    @Override
    protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, IVedActor aSel ) {
      switch( viewMode ) {
        case ALL:
          toolbar().findAction( ACTID_VIEW_ALL ).setChecked( true );
          toolbar().findAction( ACTID_VIEW_LINKED ).setChecked( false );
          toolbar().findAction( ACTID_VIEW_NONLINKED ).setChecked( false );
          break;
        case LINKED:
          toolbar().findAction( ACTID_VIEW_ALL ).setChecked( false );
          toolbar().findAction( ACTID_VIEW_LINKED ).setChecked( true );
          toolbar().findAction( ACTID_VIEW_NONLINKED ).setChecked( false );
          break;
        case NONLINKED:
          toolbar().findAction( ACTID_VIEW_ALL ).setChecked( false );
          toolbar().findAction( ACTID_VIEW_LINKED ).setChecked( false );
          toolbar().findAction( ACTID_VIEW_NONLINKED ).setChecked( true );
          break;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + viewMode ); //$NON-NLS-1$
      }
      super.doUpdateActionsState( aIsAlive, aIsSel, aSel );
    }

  }

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedPanelActorsList( Composite aParent, ITsGuiContext aContext, IVedScreen aVedScreen,
      IVedViselSelectionManager aSelectionManager ) {
    super( aParent, aContext );
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    this.setLayout( new BorderLayout() );
    //
    IM5Model<IVedActor> model = m5().getModel( IVedM5Constants.MID_VED_ACTOR, IVedActor.class );
    IM5LifecycleManager<IVedActor> lm = model.getLifecycleManager( vedScreen );

    OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    // OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_FALSE );
    OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_SUMMARY_PANE_HIDDEN.setValue( aContext.params(), AV_FALSE );

    panel = new MpcActors( aContext, model, lm.itemsProvider(), lm, aSelectionManager );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    vedScreen.model().actors().eventer().addListener( this::onVedActorsListChange );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void onVedActorsListChange( @SuppressWarnings( "unused" ) IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    // TODO perform minimal action for not to change selection
    switch( aOp ) {
      case CREATE: {
        panel.refresh();
        break;
      }
      case EDIT: {
        IVedActor actor = vedScreen.model().actors().list().findByKey( aId );
        if( actor != null ) {
          panel.unboundFilter.update();
          panel.tree().refresh();
          // FIXME just update item in tree
        }
        break;
      }
      case LIST: {
        panel.refresh();
        break;
      }
      case REMOVE: {
        panel.refresh();
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public IVedActor selectedItem() {
    return panel.selectedItem();
  }

  @Override
  public void setSelectedItem( IVedActor aItem ) {
    panel.setSelectedItem( aItem );
  }

}
