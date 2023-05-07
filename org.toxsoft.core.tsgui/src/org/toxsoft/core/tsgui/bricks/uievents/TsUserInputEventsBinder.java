package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

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
      // mouse press cancels the dragging and event is ignored
      if( isDragging() ) {
        cancelDragAndFireCancelEvent();
        return;
      }
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      lastMouseDownButton = b;
      lastMouseDownTime = aEvent.time;
      lastMouseDownCoors = pfe( aEvent );
      readyForDrag = true;
      fireMouseDownEvent( b, aEvent.stateMask, p );
    }

    @Override
    public void mouseUp( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      // if dragging then finish it and return
      if( isDragging() ) {
        finishDragAndFireEvent( aEvent.stateMask, p );
        return;
      }
      // fire mouse move event
      fireMouseUpEvent( b, aEvent.stateMask, p );
      // detect mouse click - first, check that same button was release soon after press
      if( b == lastMouseDownButton && (aEvent.time - lastMouseDownTime <= MOUSE_CLICK_DETECT_DELTA_MSECS) ) {
        // detect mouse click - second, check that mouse has not moved too far
        if( (Math.abs( p.x() - lastMouseDownCoors.x() ) <= MOUSE_CLICK_DETECT_DELTA_COORS)
            && (Math.abs( p.y() - lastMouseDownCoors.y() ) <= MOUSE_CLICK_DETECT_DELTA_COORS) ) {
          // fire click event
          fireClickEvent( b, aEvent.stateMask, p );
        }
      }
      resetDragSupport();
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      ITsPoint p = pfe( aEvent );
      ETsMouseButton b = bfe( aEvent );
      fireDoubleClickEvent( b, aEvent.stateMask, p );
    }
  };

  /**
   * Listens mouse move events from the control and delegates to the registred listeners.
   */
  private final MouseMoveListener delegatorMouseMoveListener = aEvent -> {
    ITsPoint p = pfe( aEvent );
    // start dragging if necessary
    if( this.readyForDrag ) {
      this.readyForDrag = false;
      // fire drag start event with mouse info as it was at last mouse down
      this.dragInfo = new DragOperationInfo( this.lastMouseDownButton, aEvent.stateMask, this.lastMouseDownCoors,
          this.boundControl );
      fireMouseDragStartEvent();
    }
    // handle dragging
    if( isDragging() ) {
      int buttsOldMask = this.dragInfo.startingState() & SWT.BUTTON_MASK;
      int buttsNewMask = aEvent.stateMask & SWT.BUTTON_MASK;
      // buttons state was change during drag - it means some events were lost, cancel dragging for safety
      if( buttsOldMask != buttsNewMask ) {
        cancelDragAndFireCancelEvent();
      }
      else {
        fireMouseDragMoveEvent( aEvent.stateMask, p );
      }
      return;
    }
    // fire move event
    fireMouseMoveEvent( aEvent.stateMask, p );
  };

  /**
   * Listens mouse wheel events from the control and delegates to the registred listeners.
   */
  private final ISingleSourcing_MouseWheelListener delegatorMouseWheelListener = aEvent -> {
    ITsPoint p = pfe( aEvent );
    fireMouseWheelEvent( aEvent.stateMask, p, aEvent.count );
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

  private int     bindingFlags = 0;
  private Control boundControl = null;

  // --- mouse click detection (also used for dragging support)
  private ETsMouseButton lastMouseDownButton = ETsMouseButton.OTHER; // button from mouse event - MouseEvent.buttons
  private int            lastMouseDownTime   = 0;                    // time from mouse event - MouseEvent.time
  private ITsPoint       lastMouseDownCoors  = ITsPoint.ZERO;
  // ---

  // --- mouse dragging support
  private boolean   readyForDrag = false;
  DragOperationInfo dragInfo     = null;
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
    return switch( aEvent.button ) {
      case 1 -> ETsMouseButton.LEFT;
      case 2 -> ETsMouseButton.MIDDLE;
      case 3 -> ETsMouseButton.RIGHT;
      default -> ETsMouseButton.OTHER;
    };
  }

  private boolean isDragging() {
    return dragInfo != null;
  }

  private void resetDragSupport() {
    readyForDrag = false;
    dragInfo = null;
  }

  private void fireMouseDownEvent( ETsMouseButton aButton, int aState, ITsPoint aPoint ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDown( source, aButton, aState, aPoint, boundControl ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireClickEvent( ETsMouseButton aButton, int aState, ITsPoint aPoint ) {
    // fire click event
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseClick( source, aButton, aState, aPoint, boundControl ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireDoubleClickEvent( ETsMouseButton aButton, int aState, ITsPoint aPoint ) {
    // fire click event
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDoubleClick( source, aButton, aState, aPoint, boundControl ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireMouseMoveEvent( int aState, ITsPoint aPoint ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseMove( source, aState, aPoint, boundControl ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireMouseUpEvent( ETsMouseButton aButton, int aState, ITsPoint aPoint ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseUp( source, aButton, aState, aPoint, boundControl ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireMouseWheelEvent( int aState, ITsPoint aPoint, int aScrollLines ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseWheel( source, aState, aPoint, boundControl, aScrollLines ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireMouseDragStartEvent() {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDragStart( source, dragInfo ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void fireMouseDragMoveEvent( int aState, ITsPoint aPoint ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDragMove( source, dragInfo, aState, aPoint ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  private void finishDragAndFireEvent( int aState, ITsPoint aPoint ) {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDragFinish( source, dragInfo, aState, aPoint ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    resetDragSupport();
  }

  private void cancelDragAndFireCancelEvent() {
    for( ITsMouseInputListener l : mouseListeners() ) {
      try {
        if( l.onMouseDragCancel( source, dragInfo ) ) {
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    resetDragSupport();
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
  public Control boundControl() {
    return boundControl;
  }

  /**
   * Returns binding flags as was specified in {@link #bindToControl(Control, int)}.
   * <p>
   * If there is no binding to control then returns 0.
   *
   * @return int - what is bound (<code>BIND_XXX</code> flags ORed)
   */
  public int bindingFlags() {
    return bindingFlags;
  }

  /**
   * Binds to the specified control to produce specified events.
   *
   * @param aBindControl {@link Control} - SWT-control generating user input events
   * @param aBindingFlags int - what to bind specified by <code>BIND_XXX</code> flags ORed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException control is disposed
   * @throws TsIllegalStateRtException this instance is already bind to control
   */
  public void bindToControl( Control aBindControl, int aBindingFlags ) {
    TsNullArgumentRtException.checkNull( aBindControl );
    TsIllegalStateRtException.checkNoNull( boundControl );
    TsIllegalArgumentRtException.checkTrue( aBindControl.isDisposed() );
    // setup
    bindingFlags = aBindingFlags;
    boundControl = aBindControl;
    boundControl.addDisposeListener( aEvent -> unbind() );
    // mouse
    if( (bindingFlags & BIND_MOUSE_BUTTONS) != 0 ) {
      boundControl.addMouseListener( delegatorMouseButtonsListener );
    }
    if( (bindingFlags & BIND_MOUSE_MOVE) != 0 ) {
      TsSinglesourcingUtils.Control_addMouseMoveListener( boundControl, delegatorMouseMoveListener );
    }
    if( (bindingFlags & BIND_MOUSE_WHEEL) != 0 ) {
      TsSinglesourcingUtils.Control_addMouseWheelListener( boundControl, delegatorMouseWheelListener );
    }
    // keyboard
    if( (bindingFlags & BIND_KEY_DOWN_UP) != 0 ) {
      boundControl.addKeyListener( delegatorKeyListener );
    }

  }

  /**
   * Removes all bindings established by {@link #bindToControl(Control, int)} method.
   * <p>
   * This method is called automatically before the {@link #boundControl()} is disposed.
   * <p>
   * Calling the method again is safe.
   */
  public void unbind() {
    if( boundControl != null ) {
      if( isDragging() ) {
        cancelDragAndFireCancelEvent();
      }
      if( (bindingFlags & BIND_MOUSE_BUTTONS) != 0 ) {
        boundControl.removeMouseListener( delegatorMouseButtonsListener );
      }
      if( (bindingFlags & BIND_MOUSE_MOVE) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseMoveListener( boundControl, delegatorMouseMoveListener );
      }
      if( (bindingFlags & BIND_MOUSE_WHEEL) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseWheelListener( boundControl, delegatorMouseWheelListener );
      }
      if( (bindingFlags & BIND_KEY_DOWN_UP) != 0 ) {
        boundControl.removeKeyListener( delegatorKeyListener );
      }
      boundControl = null;
      bindingFlags = 0;
    }
  }

}
