package org.toxsoft.core.tsgui.ved.impl;

import java.util.*;

import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
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
    implements IVedScreenToolsManager, IVedDisposable {

  private final IStridablesListEdit<VedAbstractEditorTool> toolsList = new StridablesList<>();
  private final GenericChangeEventer                       activeToolChangeEventer;

  private final VedScreen      screen;
  private final VedEnvironment vedEnv;

  private String  activeToolId = null;
  private boolean disposed     = false;

  public VedScreenToolsManager( VedScreen aScreen, VedEnvironment aVedEnv ) {
    screen = aScreen;
    vedEnv = aVedEnv;
    activeToolChangeEventer = new GenericChangeEventer( this );
    initializeTools();
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
    return activeToolId;
  }

  @Override
  public void setActiveTool( String aToolId ) {
    if( !Objects.equals( activeToolId, aToolId ) ) {
      if( aToolId != null ) {
        TsItemNotFoundRtException.checkFalse( toolsList.hasKey( aToolId ) );
      }
      activeToolId = aToolId;
      activeToolChangeEventer.fireChangeEvent();
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
  // IVedDisposable
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
