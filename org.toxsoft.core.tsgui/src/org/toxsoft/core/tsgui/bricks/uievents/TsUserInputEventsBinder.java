package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.rcp.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Binds user input listeners <code>ITsXxxInputListener</code> to the specified control.
 * <p>
 * This is default helper implementation of producer {@link ITsMouseInputProducer} and {@link ITsKeyInputProducer}.
 * <p>
 * This helper correctly compiles and runs both in RCP and RAP.
 *
 * @author hazard157
 */
public class TsUserInputEventsBinder
    implements ITsMouseInputProducer, ITsKeyInputProducer, ITsUserInputProducer {

  /**
   * Flags to bind mouse button press/release/click {@link MouseListener} events to the SWT control.
   */
  public static final int BIND_MOUSE_BUTTONS = 0x0000_0001;

  /**
   * Flags to bind mouse cursor moving {@link MouseMoveListener} events to the SWT control.
   */
  public static final int BIND_MOUSE_MOVE = 0x0000_0002;

  /**
   * Flags to bind mouse wheel rolling {@link MouseWheelListener} events to the SWT control.
   */
  public static final int BIND_MOUSE_WHEEL = 0x0000_0001;

  /**
   * Flags to bind all possible mouse events to the SWT control.
   */
  public static final int BIND_ALL_MOUSE_EVENTS = 0x0000_00FF;

  /**
   * Flags to bind keyboard key press and release (down&up) events to the SWT control.
   */
  public static final int BIND_KEY_DOWN_UP = 0x0000_0100;

  /**
   * Flags to bind all possible keyboard events to the SWT control.
   */
  public static final int BIND_ALL_KEY_EVENTS = 0x0000_0F00;

  /**
   * Flags to bind all possible user input to the SWT control.
   */
  public static final int BIND_ALL_INPUT_EVENTS = 0xFFFF_FFFF;

  private static int MOUSE_CLICK_DETECT_DELTA_MSECS = 100;
  private static int MOUSE_CLICK_DETECT_DELTA_COORS = 3;

  /**
   * Listens mouse button events from the control and delegates to the registred listeners.
   */
  private final MouseListener delegatorMouseButtonsListener = new MouseListener() {

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      lastMouseDownButton = b;
      lastMouseDownState = aEvent.stateMask;
      lastMouseDownTime = aEvent.time;
      lastMouseDownCoors = pfe( aEvent );
      readyForDrag = true;
      for( ITsMouseInputListener l : mouseListeners() ) {
        if( l.onMouseDown( source, b, aEvent.stateMask, p, bindControl ) ) {
          break;
        }
      }
    }

    @Override
    public void mouseUp( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      // if dragging then finish it and return
      if( isDragging() ) {
        for( ITsMouseInputListener l : mouseListeners() ) {
          if( l.onMouseDragFinish( source, dragInfo, aEvent.stateMask, p ) ) {
            break;
          }
        }
        resetDragSupport();
        return;
      }
      // fire mouse move event
      for( ITsMouseInputListener l : mouseListeners() ) {
        if( l.onMouseUp( source, b, aEvent.stateMask, p, bindControl ) ) {
          break;
        }
      }
      // detect mouse click - first, check that same button was release soon after press
      if( b == lastMouseDownButton && (aEvent.time - lastMouseDownTime <= MOUSE_CLICK_DETECT_DELTA_MSECS) ) {
        // detect mouse click - second, check that mouse has not moved too far
        if( (Math.abs( p.x() - lastMouseDownCoors.x() ) <= MOUSE_CLICK_DETECT_DELTA_COORS)
            && (Math.abs( p.y() - lastMouseDownCoors.y() ) <= MOUSE_CLICK_DETECT_DELTA_COORS) ) {
          // fire click event
          for( ITsMouseInputListener l : mouseListeners() ) {
            if( l.onMouseClick( source, b, aEvent.stateMask, p, bindControl ) ) {
              break;
            }
          }
        }
      }
      resetDragSupport();
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      for( ITsMouseInputListener l : mouseListeners() ) {
        if( l.onMouseDoubleClick( source, b, aEvent.stateMask, p, bindControl ) ) {
          break;
        }
      }
    }
  };

  /**
   * Listens mouse move events from the control and delegates to the registred listeners.
   */
  private final MouseMoveListener delegatorMouseMoveListener = new MouseMoveListener() {

    @Override
    public void mouseMove( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      // start dragging if necessary
      if( readyForDrag ) {
        readyForDrag = false;
        // fire drag start event with mouse info as it was at last mouse down
        dragInfo = new DragOperationInfo( lastMouseDownButton, lastMouseDownState, lastMouseDownCoors, bindControl );
        for( ITsMouseInputListener l : mouseListeners() ) {
          if( l.onMouseDragStart( source, dragInfo ) ) {
            break;
          }
        }
      }
      // if dragging then fire event and return
      if( isDragging() ) {
        for( ITsMouseInputListener l : mouseListeners() ) {
          if( l.onMouseDragMove( source, dragInfo, aEvent.stateMask, p ) ) {
            break;
          }
        }
        return;
      }
      // fire move event
      for( ITsMouseInputListener l : mouseListeners() ) {
        if( l.onMouseMove( source, aEvent.stateMask, p, bindControl ) ) {
          break;
        }
      }
    }
  };

  /**
   * Listens mouse wheel events from the control and delegates to the registred listeners.
   */
  private final ISingleSourcing_MouseWheelListener delegatorMouseWheelListener =
      new ISingleSourcing_MouseWheelListener() {

        @Override
        public void mouseScrolled( MouseEvent aEvent ) {
          ITsPoint p = pfe( aEvent );
          for( ITsMouseInputListener l : mouseListeners() ) {
            if( l.onMouseWheel( source, aEvent.stateMask, p, bindControl, aEvent.count ) ) {
              break;
            }
          }
        }
      };

  /**
   * Listens keyboard keys press and release events from the control and delegates to the registred listeners.
   */
  private final KeyListener delegatorKeyListener = new KeyListener() {

    @Override
    public void keyPressed( KeyEvent aEvent ) {
      for( ITsKeyInputListener l : keyListeners() ) {
        if( l.onKeyDown( source, aEvent.keyCode, aEvent.character, aEvent.stateMask ) ) {
          break;
        }
      }
    }

    @Override
    public void keyReleased( KeyEvent aEvent ) {
      for( ITsKeyInputListener l : keyListeners() ) {
        if( l.onKeyUp( source, aEvent.keyCode, aEvent.character, aEvent.stateMask ) ) {
          break;
        }
      }
    }

  };

  private final Object                           source;
  private final IListEdit<ITsMouseInputListener> mouseListenersList = new ElemArrayList<>();
  private final IListEdit<ITsKeyInputListener>   keyListenersList   = new ElemArrayList<>();

  private int     bindFlags   = 0;
  private Control bindControl = null;

  // --- mouse click detection (also used for dragging support)
  private ETsMouseButton lastMouseDownButton = ETsMouseButton.OTHER; // button from mouse event - MouseEvent.buttons
  private int            lastMouseDownTime   = 0;                    // time from mouse event - MouseEvent.time
  private ITsPoint       lastMouseDownCoors  = ITsPoint.ZERO;
  // ---

  // --- mouse dragging support
  private boolean   readyForDrag       = false;
  private int       lastMouseDownState = 0;    // state mask frmom mouse event - MouseEvent.stateMask
  DragOperationInfo dragInfo           = null;
  // ---

  /**
   * Constructor.
   *
   * @param aSource Object - source of the events
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsUserInputEventsBinder( Object aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    source = aSource;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IList<ITsMouseInputListener> mouseListeners() {
    if( mouseListenersList.isEmpty() ) {
      return IList.EMPTY;
    }
    return new ElemArrayList<>( mouseListenersList );
  }

  private IList<ITsKeyInputListener> keyListeners() {
    if( keyListenersList.isEmpty() ) {
      return IList.EMPTY;
    }
    return new ElemArrayList<>( keyListenersList );
  }

  private static ITsPoint pfe( MouseEvent aEvent ) {
    // Display display = bindControl.getDisplay();
    // Point p = display.map( bindControl, null, aEvent.x, aEvent.y );
    // return new TsPoint( p.x, p.y );
    return new TsPoint( aEvent.x, aEvent.y );
  }

  private static ETsMouseButton bfe( MouseEvent aEvent ) {
    switch( aEvent.button ) {
      case 1:
        return ETsMouseButton.LEFT;
      case 2:
        return ETsMouseButton.MIDDLE;
      case 3:
        return ETsMouseButton.RIGHT;
      default:
        return ETsMouseButton.OTHER;
    }
  }

  private boolean isDragging() {
    return dragInfo != null;
  }

  private void resetDragSupport() {
    readyForDrag = false;
    dragInfo = null;
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputProducer
  //

  @Override
  public void addTsMouseInputListener( ITsMouseInputListener aListener ) {
    if( !mouseListenersList.hasElem( aListener ) ) {
      mouseListenersList.add( aListener );
    }
  }

  @Override
  public void removeTsMouseInputListener( ITsMouseInputListener aListener ) {
    mouseListenersList.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputProducer
  //

  @Override
  public void addTsKeyInputListener( ITsKeyInputListener aListener ) {
    if( !keyListenersList.hasElem( aListener ) ) {
      keyListenersList.add( aListener );
    }
  }

  @Override
  public void removeTsKeyInputListener( ITsKeyInputListener aListener ) {
    keyListenersList.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputProducer
  //

  @Override
  public void addTsUserInputListener( ITsUserInputListener aListener ) {
    addTsKeyInputListener( aListener );
    addTsMouseInputListener( aListener );
  }

  @Override
  public void removeTsUserInputListener( ITsUserInputListener aListener ) {
    removeTsKeyInputListener( aListener );
    removeTsMouseInputListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the control the mouse input is bind to.
   * <p>
   * Mouse event coordinates in {@link ITsMouseInputListener} are specified relative to this control.
   * <p>
   * If there is no binding to control then returns <code>null</code>.
   *
   * @return {@link Control} - the bind control
   */
  public Control bindControl() {
    return bindControl;
  }

  /**
   * Returns binding flags as was specified in {@link #bindToControl(Control, int)}.
   * <p>
   * If there is no binding to control then returns 0.
   *
   * @return int - what is bound (<code>BIND_XXX</code> flags ORed)
   */
  public int bindFlags() {
    return bindFlags;
  }

  /**
   * Binds to the specified control to produce specified events.
   *
   * @param aBindControl {@link Control} - SWT-control generating user input events
   * @param aBindFlags int - what to bind specified by <code>BIND_XXX</code> flags ORed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException control is disposed
   * @throws TsIllegalStateRtException this instance is already bind to control
   */
  public void bindToControl( Control aBindControl, int aBindFlags ) {
    TsNullArgumentRtException.checkNull( aBindControl );
    TsIllegalStateRtException.checkNoNull( bindControl );
    TsIllegalArgumentRtException.checkTrue( aBindControl.isDisposed() );
    // setup
    bindFlags = aBindFlags;
    bindControl = aBindControl;
    bindControl.addDisposeListener( aEvent -> unbind() );
    // mouse
    if( (bindFlags & BIND_MOUSE_BUTTONS) != 0 ) {
      bindControl.addMouseListener( delegatorMouseButtonsListener );
    }
    if( (bindFlags & BIND_MOUSE_MOVE) != 0 ) {
      TsSinglesourcingUtils.Control_addMouseMoveListener( bindControl, delegatorMouseMoveListener );
    }
    if( (bindFlags & BIND_MOUSE_WHEEL) != 0 ) {
      TsSinglesourcingUtils.Control_addMouseWheelListener( bindControl, delegatorMouseWheelListener );
    }
    // keyboard
    if( (bindFlags & BIND_KEY_DOWN_UP) != 0 ) {
      bindControl.addKeyListener( delegatorKeyListener );
    }

  }

  /**
   * Removes all bindings established by {@link #bindToControl(Control, int)} method.
   * <p>
   * This method is called automatically before the {@link #bindControl()} is disposed.
   * <p>
   * Calling the method again is safe.
   */
  public void unbind() {
    if( bindControl != null ) {
      if( isDragging() ) {
        for( ITsMouseInputListener l : mouseListeners() ) {
          if( l.onMouseDragCancel( source, dragInfo ) ) {
            break;
          }
        }
        resetDragSupport();
      }
      if( (bindFlags & BIND_MOUSE_BUTTONS) != 0 ) {
        bindControl.removeMouseListener( delegatorMouseButtonsListener );
      }
      if( (bindFlags & BIND_MOUSE_MOVE) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseMoveListener( bindControl, delegatorMouseMoveListener );
      }
      if( (bindFlags & BIND_MOUSE_WHEEL) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseWheelListener( bindControl, delegatorMouseWheelListener );
      }
      if( (bindFlags & BIND_KEY_DOWN_UP) != 0 ) {
        bindControl.removeKeyListener( delegatorKeyListener );
      }
      bindControl = null;
      bindFlags = 0;
    }
  }

}
