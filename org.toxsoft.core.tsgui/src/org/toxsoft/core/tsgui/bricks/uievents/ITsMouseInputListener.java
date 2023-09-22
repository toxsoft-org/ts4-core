package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Mouse input event listener.
 * <p>
 * Note: implementation in TsGUI is designed to bind listener only to the SWT {@link Control}.
 * <p>
 * Each method returns boolean flag. <code>true</code> means that event was handled by listener so there is no need to
 * call other listeners of the same event. All default implementations return <code>false</code>.
 *
 * @author hazard157
 */
public interface ITsMouseInputListener {

  /**
   * Called when mouse button was pressed.
   *
   * @param aSource Object - the event source
   * @param aButton {@link ETsMouseButton} - the pressed button
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  default boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    return false;
  }

  /**
   * Called when mouse button was released.
   *
   * @param aSource Object - the event source
   * @param aButton {@link ETsMouseButton} - the released button
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  default boolean onMouseUp( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    return false;
  }

  /**
   * Called when there was mouse button single click.
   *
   * @param aSource Object - the event source
   * @param aButton {@link ETsMouseButton} - the clicked button
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  default boolean onMouseClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    return false;
  }

  /**
   * Called when there was mouse button double click.
   *
   * @param aSource Object - the event source
   * @param aButton {@link ETsMouseButton} - the clicked button
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  default boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    return false;
  }

  /**
   * Called when mouse moves.
   *
   * @param aSource Object - the event source
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  default boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    return false;
  }

  /**
   * Called when mouse wheel rolling event (scroll) was detected.
   *
   * @param aSource Object - the event source
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @param aScrollLines int - number of scrolled "lines" (>0 - scroll up, <0 scroll down)
   * @return boolean - event processing flag
   */
  default boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
    return false;
  }

  /**
   * Called when dragging is started - that is mouse drag gesture was detected.
   * <p>
   * Mouse drag gesture is detected when mouse button is pressed and mouse was moved with button hold.
   * <p>
   * On drag start {@link DragOperationInfo} instance is created and passed to all subsequent move, finish and cancel
   * events. Next drag will create next instance of {@link DragOperationInfo} class. User may set arbitrary reference as
   * "cargo" of {@link DragOperationInfo} instance. For example, {@link DragOperationInfo#cargo()} may be an item user
   * may drag from one place to another.
   *
   * @param aSource Object - the event source
   * @param aDragInfo {@link DragOperationInfo} - dragging information as introduced at start
   * @return boolean - event processing flag
   */
  default boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    return false;
  }

  /**
   * Called when dragging is continuing - that is mouse is moving with button pressed.
   *
   * @param aSource Object - the event source
   * @param aDragInfo {@link DragOperationInfo} - dragging information as introduced at start
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to {@link DragOperationInfo#starterControl()}
   * @return boolean - event processing flag
   */
  default boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    return false;
  }

  /**
   * Called when dragging is finished - that is pressed button was released.
   *
   * @param aSource Object - the event source
   * @param aDragInfo {@link DragOperationInfo} - dragging information as introduced at start
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to {@link DragOperationInfo#starterControl()}
   * @return boolean - event processing flag
   */
  default boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    return false;
  }

  /**
   * Called when dragging is cancelled by some action.
   * <p>
   * In fact mouse may continue moving with button pressed however dragging logical operation is cancelled. The\reason
   * may be both programmatically and user action like pressing ESC key on keyboard.
   *
   * @param aSource Object - the event source
   * @param aDragInfo {@link DragOperationInfo} - dragging information as introduced at start
   * @return boolean - event processing flag
   */
  default boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    return false;
  }

}
