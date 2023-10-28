package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Implementation of the VED canvas user input handling.
 * <p>
 * Note: source for all events is a {@link IVedScreen}.
 *
 * @author hazard157
 */
class VedCanvasHandler
    implements ITsUserInputListener {

  private final VedScreen vedScreen;

  private final IVedItemsManager<VedAbstractActor>              actors;
  private final IVedSnippetManager<VedAbstractUserInputHandler> handlersBefore;
  private final IVedSnippetManager<VedAbstractUserInputHandler> handlersAfter;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the VED screen
   */
  public VedCanvasHandler( VedScreen aScreen ) {
    vedScreen = aScreen;
    actors = vedScreen.model().actors();
    handlersBefore = vedScreen.model().screenHandlersBefore();
    handlersAfter = vedScreen.model().screenHandlersAfter();
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onKeyDown( aSource, aCode, aChar, aState ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onKeyDown( aSource, aCode, aChar, aState ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onKeyDown( aSource, aCode, aChar, aState ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onKeyUp( Object aSource, int aCode, char aChar, int aState ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onKeyUp( aSource, aCode, aChar, aState ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onKeyUp( aSource, aCode, aChar, aState ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onKeyUp( aSource, aCode, aChar, aState ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDown( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDown( aSource, aButton, aState, aCoors, aWidget ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDown( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseUp( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseUp( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseUp( aSource, aButton, aState, aCoors, aWidget ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseUp( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseClick( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseClick( aSource, aButton, aState, aCoors, aWidget ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseClick( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDoubleClick( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDoubleClick( aSource, aButton, aState, aCoors, aWidget ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDoubleClick( aSource, aButton, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseMove( aSource, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseMove( aSource, aState, aCoors, aWidget ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseMove( aSource, aState, aCoors, aWidget ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseWheel( aSource, aState, aCoors, aWidget, aScrollLines ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseWheel( aSource, aState, aCoors, aWidget, aScrollLines ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseWheel( aSource, aState, aCoors, aWidget, aScrollLines ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragStart( aSource, aDragInfo ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDragStart( aSource, aDragInfo ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragStart( aSource, aDragInfo ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragMove( aSource, aDragInfo, aState, aCoors ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDragMove( aSource, aDragInfo, aState, aCoors ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragMove( aSource, aDragInfo, aState, aCoors ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragFinish( aSource, aDragInfo, aState, aCoors ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDragFinish( aSource, aDragInfo, aState, aCoors ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragFinish( aSource, aDragInfo, aState, aCoors ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersBefore.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragCancel( aSource, aDragInfo ) ) {
        return true;
      }
    }
    if( vedScreen.isActorsEnabled() ) {
      for( VedAbstractActor a : new ElemArrayList<>( actors.list() ) ) {
        if( a.isActive() && a.userInputListener().onMouseDragCancel( aSource, aDragInfo ) ) {
          return true;
        }
      }
    }
    for( VedAbstractUserInputHandler h : new ElemArrayList<>( handlersAfter.list() ) ) {
      if( h.isActive() && h.userInputListener().onMouseDragCancel( aSource, aDragInfo ) ) {
        return true;
      }
    }
    return false;
  }

}
