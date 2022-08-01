package org.toxsoft.test.ved.exe.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.test.ved.exe.ITestVedExeConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.glib.comps.*;
import org.toxsoft.core.tsgui.ved.glib.comps.tools.*;
import org.toxsoft.core.tsgui.ved.olds.std.tool.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mned view: editor area.
 *
 * @author hazard157
 */
public class UipartMnedEditorArea_old
    extends MwsAbstractPart
    implements IVedContextable {

  IVedComponentProvider rectCompProvider;
  IVedComponentProvider roundRectCompProvider;

  Canvas     canvas;
  IVedScreen screen;

  @Override
  protected void doInit( Composite aParent ) {

    ITsGuiContext ctx = new TsGuiContext( partContext() );

    aParent.setLayout( new BorderLayout() );

    canvas = new Canvas( aParent, SWT.NONE );
    canvas.setLayoutData( BorderLayout.CENTER );
    screen = vedEnv().screenManager().createScreen( canvas );

    IVedToolsPanel toolsPanel = createToolsPanel( aParent );
    ((TsPanel)toolsPanel).setLayoutData( BorderLayout.WEST );
    toolsPanel.addToolSelectionListener( new IVedToolSelectionListener() {

      @Override
      public void onToolDeactivated( IVedEditorTool aTool ) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onToolActivated( IVedEditorTool aTool ) {
        screen.setActiveTool( aTool );
      }

    } );

    IOptionSet props = OptionSetUtils.createOpSet( //
        PDEF_X, Double.valueOf( 100.0 ), //
        PDEF_Y, Double.valueOf( 100.0 ), //
        PDEF_WIDTH, Double.valueOf( 100.0 ), //
        PDEF_HEIGHT, Double.valueOf( 200.0 ), //
        PDEF_FG_COLOR, ETsColor.RED.rgb(), //
        PDEF_FG_COLOR, ETsColor.CYAN.rgb() //
    );

    IVedComponent comp = rectCompProvider.createComponent( "rect1", env, new OptionSet(), new OptionSet() );
    IVedPorter porter = comp.createView( screen ).porter();
    porter.setBounds( 100, 100, 200, 150 );

    env.dataModel().addComponent( comp );

    comp = roundRectCompProvider.createComponent( "rect2", env, props, new OptionSet() );
    porter = comp.createView( screen ).porter();
    porter.setBounds( 10, 300, 200, 150 );

    env.dataModel().addComponent( comp );

    VedToolBar tb = new VedToolBar( aParent, EIconSize.IS_32X32, ctx );
    // поднастроим внешний вид ToolBar'a
    tb.setBackgroundImage( iconManager().loadFreeIcon( ICONID_GRADIENT ) );

    TsAction undoAction = tb.findAction( ACTID_UNDO );
    TsAction redoAction = tb.findAction( ACTID_REDO );
    undoAction.setDisabledImageDescriptor( iconManager().loadFreeDescriptor( ICONID_UNDO_GRAYED ) );
    redoAction.setDisabledImageDescriptor( iconManager().loadFreeDescriptor( ICONID_REDO_GRAYED ) );

    tb.setLayoutData( BorderLayout.NORTH );

    tb.addListener( aActionId -> {
      switch( aActionId ) {
        case ACTID_ZOOM_ORIGINAL:
          screen.setZoomFactor( 1.0 );
          break;
        case ACTID_ZOOM_IN:
          screen.setZoomFactor( screen.zoomFactor() * 1.1 );
          break;
        case ACTID_ZOOM_OUT:
          screen.setZoomFactor( screen.zoomFactor() * 0.9 );
          break;
        case ACTID_UNDO:
          // undoManager.undo();
          break;
        case ACTID_REDO:
          // undoManager.redo();
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    } );
  }

  private IVedToolsPanel createToolsPanel( Composite aParent ) {
    IVedToolsPanel tp = new VedToolsPanel( aParent, tsContext() );

    VedPointerTool pointerTool = new VedPointerTool( tsContext() );
    tp.addTool( pointerTool, TsLibUtils.EMPTY_STRING );

    String rectGroupId = "rectGroup"; //$NON-NLS-1$
    IVedToolsGroup rectGroup = tp.createGroup( rectGroupId );
    VedRectTool rectTool = new VedRectTool( rectCompProvider, tsContext() );
    ((VedToolsGroup)rectGroup).addTool( rectTool );
    VedRoundRectTool roundrectTool = new VedRoundRectTool( roundRectCompProvider, tsContext() );
    ((VedToolsGroup)rectGroup).addTool( roundrectTool );

    tp.createContent();

    return tp;
  }

}
