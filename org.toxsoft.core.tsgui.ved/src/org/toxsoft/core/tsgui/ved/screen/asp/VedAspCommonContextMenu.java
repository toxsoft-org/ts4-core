package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.asp.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор общих действий для контекстного меню.
 * <p>
 *
 * @author vs
 */
public class VedAspCommonContextMenu
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  static final String ACTID_SCREEN_CONFIG = "screen.config"; //$NON-NLS-1$

  static final ITsActionDef ACDEF_SCREEN_CONFIG = ofPush2( ACTID_SCREEN_CONFIG, //
      STR_SCREEN_CONFIG, STR_SCREEN_CONFIG_D, ICONID_SETTINGS );

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private VedAbstractVisel activeVisel = null;

  private final IGenericChangeListener canvasChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      if( aSource instanceof PanelCanvasConfig ) {
        IVedCanvasCfg cfg = ((PanelCanvasConfig)aSource).getDataRecord();
        vedScreen.view().setCanvasConfig( cfg );
        vedScreen.view().redraw();
        vedScreen.view().update();
      }

    }
  };

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @param aSelectionManager IVedViselSelectionManager - manager of the selected visels
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspCommonContextMenu( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    selectionManager = aSelectionManager;
    selectionManager.genericChangeEventer().addListener( this::onSelectionChanged );
    defineAction( ACDEF_SCREEN_CONFIG, this::doConfigurateCanvas );
    // defineAction( ACDEF_REMOVE, this::doRemove );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiConextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // MethodPerActionTsActionSetProvider
  //

  @Override
  protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
    switch( aActionDef.id() ) {
      case ACTID_REMOVE: {
        if( activeVisel == null ) {
          return selectionManager.selectionKind() != ESelectionKind.NONE;
        }
        break;
      }
      case ACTID_SCREEN_CONFIG: {
        return activeVisel == null;
      }
      default:
        break;
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает активный визуальный элемент.<br>
   * От этого зависит доступность некоторых действий набора.
   *
   * @param aVisel {@link VedAbstractVisel} - визуальный элемент, м.б. <b>null</b>
   */
  public void setActiveVisel( VedAbstractVisel aVisel ) {
    activeVisel = aVisel;
    actionsStateEventer().fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doRemove() {
    if( (activeVisel == null) || selectionManager.isSelected( activeVisel.id() ) ) {
      deleteSelectedVisels();
    }
    else {
      vedScreen.model().visels().remove( activeVisel.id() );
      for( String actorId : VedScreenUtils.viselActorIds( activeVisel.id(), vedScreen ) ) {
        vedScreen.model().actors().remove( actorId );
      }
    }
    activeVisel = null;
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    actionsStateEventer().fireChangeEvent();
  }

  void doConfigurateCanvas() {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.put( IGenericChangeListener.class, canvasChangeListener );
    IVedCanvasCfg canvasConfig = PanelCanvasConfig.editCanvasConfig( vedScreen.view().canvasConfig(), ctx );
    if( canvasConfig != null ) {
      vedScreen.view().setCanvasConfig( canvasConfig );
      vedScreen.view().redraw();
      vedScreen.view().update();
    }
  }

  void deleteSelectedVisels() {
    IStringList ids = new StringArrayList( selectionManager.selectedViselIds() );
    selectionManager.deselectAll();
    for( String id : ids ) {
      vedScreen.model().visels().remove( id );
      for( String actorId : VedScreenUtils.viselActorIds( id, vedScreen ) ) {
        vedScreen.model().actors().remove( actorId );
      }
    }
  }

}
