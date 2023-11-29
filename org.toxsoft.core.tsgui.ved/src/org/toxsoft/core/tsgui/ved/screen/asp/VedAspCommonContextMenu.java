package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.asp.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
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

  /**
   * Action: align group of selected visels to left edge.
   */
  public static final ITsActionDef ACDEF_BK_COLOR = ofPush2( ACTID_SCREEN_CONFIG, //
      STR_SCREEN_CONFIG, STR_SCREEN_CONFIG_D, ICONID_SETTINGS );

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private VedAbstractVisel activeVisel = null;

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
    defineAction( ACDEF_BK_COLOR, this::doConfigurateCanvas );
    defineAction( ACDEF_REMOVE, this::doRemove );
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
  public boolean isActionEnabled( String aActionId ) {
    if( aActionId.equals( ACDEF_REMOVE.id() ) ) {
      if( activeVisel == null ) {
        return selectionManager.selectionKind() != ESelectionKind.NONE;
      }
    }
    if( aActionId.equals( ACDEF_BK_COLOR.id() ) ) {
      if( activeVisel != null ) {
        return false;
      }
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
    }
    activeVisel = null;
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    actionsStateEventer().fireChangeEvent();
  }

  void doConfigurateCanvas() {
    IVedCanvasCfg canvasConfig = PanelCanvasConfig.editCanvasConfig( vedScreen.view().canvasConfig(), tsContext() );
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
    }
  }

}
