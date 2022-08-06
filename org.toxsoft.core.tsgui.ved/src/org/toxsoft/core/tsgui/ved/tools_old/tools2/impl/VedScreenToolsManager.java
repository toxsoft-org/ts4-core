package org.toxsoft.core.tsgui.ved.tools_old.tools2.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.*;

import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.tools_old.tools2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenToolsManager} implementation.
 *
 * @author hazard157
 */
class VedScreenToolsManager
    implements IVedScreenToolsManager, IDisposable {

  private final IStridablesListEdit<VedAbstractEditorTool> toolsList = new StridablesList<>();
  private final GenericChangeEventer                       activeToolChangeEventer;

  private final VedScreen      screen;
  private final VedEnvironment vedEnv;

  private VedAbstractEditorTool activeTool = null;
  private boolean               disposed   = false;

  public VedScreenToolsManager( VedScreen aScreen, VedEnvironment aVedEnv ) {
    screen = aScreen;
    vedEnv = aVedEnv;
    activeToolChangeEventer = new GenericChangeEventer( this );
    initializeTools();
    // activeTool = toolsList.first();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final void initializeTools() {
    for( IVedLibrary lib : vedEnv.libraryManager().listLibs() ) {
      for( IVedEditorToolProvider p : lib.toolProviders() ) {
        IVedEditorTool rawTool = p.createTool( vedEnv, screen );
        if( rawTool instanceof VedAbstractEditorTool tool ) {
          toolsList.add( tool );
        }
        else {
          throw new TsInternalErrorRtException();
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenToolsManager
  //

  @Override
  public String activeToolId() {
    return activeTool != null ? activeTool.id() : EMPTY_STRING;
  }

  @Override
  public void setActiveTool( String aToolId ) {
    if( !Objects.equals( activeToolId(), aToolId ) ) {
      if( aToolId != null && !aToolId.isEmpty() ) {
        TsItemNotFoundRtException.checkFalse( toolsList.hasKey( aToolId ) );
      }
      // deactivate current tool
      if( activeTool != null ) {
        if( activeTool.keyListener() != null ) {
          screen.removeSwtKeyListener( activeTool.keyListener() );
        }
        if( activeTool.mouseListener() != null ) {
          screen.removeSwtMouseListener( activeTool.mouseListener() );
        }
        if( activeTool.screenDecorator() != null ) {
          screen.paintingManager().removeScreensDecorator( activeTool.screenDecorator() );
        }
        if( activeTool.viewDecorator() != null ) {
          screen.paintingManager().removeViewsDecorator( activeTool.viewDecorator() );
        }
        activeTool.papiToolDeactivated();
      }
      activeTool = toolsList.getByKey( aToolId );
      // activate tool
      if( activeTool != null ) {
        if( activeTool.keyListener() != null ) {
          screen.addSwtKeyListener( activeTool.keyListener() );
        }
        if( activeTool.mouseListener() != null ) {
          screen.addSwtMouseListener( activeTool.mouseListener() );
        }
        if( activeTool.screenDecorator() != null ) {
          screen.paintingManager().addScreensDecorator( activeTool.screenDecorator() );
        }
        if( activeTool.viewDecorator() != null ) {
          screen.paintingManager().addViewsDecorator( activeTool.viewDecorator() );
        }
        activeTool.papiToolActivated();
      }
      activeToolChangeEventer.fireChangeEvent();
      screen.redraw();
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStridablesList<IVedEditorTool> listTools() {
    return (IStridablesList)toolsList;
  }

  @Override
  public IGenericChangeEventer activeToolChangeEventer() {
    return activeToolChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      while( !toolsList.isEmpty() ) {
        toolsList.removeByIndex( 0 ).dispose();
      }
    }
  }

}
