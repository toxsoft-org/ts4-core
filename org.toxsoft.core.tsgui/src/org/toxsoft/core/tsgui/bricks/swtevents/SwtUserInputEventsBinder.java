package org.toxsoft.core.tsgui.bricks.swtevents;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Binds user input listeners <code>ISwtXxxListener</code> to the specified control.
 * <p>
 * This is default helper implementation of producers {@link ISwtKeyEventProducer} and {@link ISwtMouseInputProducer}.
 * <p>
 * This helper correctly compiles and runs both in RCP and RAP.
 *
 * @author hazard157
 */
public class SwtUserInputEventsBinder
    implements ISwtMouseInputProducer, ISwtKeyEventProducer {

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
   * Flags to bind mouse tracking {@link MouseTrackListener} events to the SWT control.
   */
  public static final int BIND_MOUSE_TRACK = 0x0000_0004;

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

  /**
   * Listens mouse button events from the control and delegates to the registred listeners.
   */
  private final MouseListener delegatorMouseButtonsListener = new MouseListener() {

    @Override
    public void mouseUp( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseUp( aEvent );
      }
    }

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseDown( aEvent );
      }
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseDoubleClick( aEvent );
      }
    }
  };

  /**
   * Listens mouse move events from the control and delegates to the registred listeners.
   */
  private final MouseMoveListener delegatorMouseMoveListener = aEvent -> {
    for( ISwtMouseListener l : this.mouseListeners ) {
      l.mouseMove( aEvent );
    }
  };

  /**
   * Listens mouse wheel events from the control and delegates to the registred listeners.
   */
  private final ISingleSourcing_MouseWheelListener delegatorMouseWheelListener = aEvent -> {
    for( ISwtMouseListener l : this.mouseListeners ) {
      l.mouseScrolled( aEvent );
    }
  };

  /**
   * Listens mouse traversal events from the control and delegates to the registred listeners.
   */
  private final MouseTrackListener delegatorMouseTrackListener = new MouseTrackListener() {

    @Override
    public void mouseHover( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseHover( aEvent );
      }
    }

    @Override
    public void mouseExit( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseExit( aEvent );
      }
    }

    @Override
    public void mouseEnter( MouseEvent aEvent ) {
      for( ISwtMouseListener l : mouseListeners ) {
        l.mouseEnter( aEvent );
      }
    }
  };

  /**
   * Listens keyboard keys press and release events from the control and delegates to the registred listeners.
   */
  private final KeyListener delegatorKeyListener = new KeyListener() {

    @Override
    public void keyPressed( KeyEvent aEvent ) {
      for( ISwtKeyListener l : keyListeners ) {
        l.keyPressed( aEvent );
      }
    }

    @Override
    public void keyReleased( KeyEvent aEvent ) {
      for( ISwtKeyListener l : keyListeners ) {
        l.keyReleased( aEvent );
      }
    }

  };

  private final IListEdit<ISwtMouseListener> mouseListeners = new ElemArrayList<>();
  private final IListEdit<ISwtKeyListener>   keyListeners   = new ElemArrayList<>();

  private int     bindFlags   = 0;
  private Control bindControl = null;

  /**
   * Constructor.
   */
  public SwtUserInputEventsBinder() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns bound SWT-control as was specified in {@link #bindToControl(Control, int)}.
   * <p>
   * If there is no binding to control then returns <code>null</code>.
   *
   * @return {@link Control} - SWT-control generating user input events or <code>null</code> if there is no binding
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
    if( (bindFlags & BIND_MOUSE_TRACK) != 0 ) {
      bindControl.addMouseTrackListener( delegatorMouseTrackListener );
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
      if( (bindFlags & BIND_MOUSE_BUTTONS) != 0 ) {
        bindControl.removeMouseListener( delegatorMouseButtonsListener );
      }
      if( (bindFlags & BIND_MOUSE_MOVE) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseMoveListener( bindControl, delegatorMouseMoveListener );
      }
      if( (bindFlags & BIND_MOUSE_WHEEL) != 0 ) {
        TsSinglesourcingUtils.Control_removeMouseWheelListener( bindControl, delegatorMouseWheelListener );
      }
      if( (bindFlags & BIND_MOUSE_TRACK) != 0 ) {
        bindControl.removeMouseTrackListener( delegatorMouseTrackListener );
      }
      if( (bindFlags & BIND_KEY_DOWN_UP) != 0 ) {
        bindControl.removeKeyListener( delegatorKeyListener );
      }
      bindControl = null;
      bindFlags = 0;
    }
  }

  // ------------------------------------------------------------------------------------
  // ISwtMouseEventProducer
  //

  @Override
  public void addSwtMouseListener( ISwtMouseListener aListener ) {
    if( !mouseListeners.hasElem( aListener ) ) {
      mouseListeners.add( aListener );
    }
  }

  @Override
  public void removeSwtMouseListener( ISwtMouseListener aListener ) {
    mouseListeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ISwtKeyEventProducer
  //

  @Override
  public void addSwtKeyListener( ISwtKeyListener aListener ) {
    if( !keyListeners.hasElem( aListener ) ) {
      keyListeners.add( aListener );
    }
  }

  @Override
  public void removeSwtKeyListener( ISwtKeyListener aListener ) {
    keyListeners.remove( aListener );
  }

}
