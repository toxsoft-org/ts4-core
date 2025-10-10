package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Information about dragging used by {@link ITsMouseInputListener}.
 * <p>
 * Instance of this class is created before event producer calls
 * {@link ITsMouseInputListener#onMouseDragStart(Object, DragOperationInfo)}.
 *
 * @author hazard157
 */
public final class DragOperationInfo {

  private final ETsMouseButton button;
  private final int            state;
  private final ITsPoint       point;
  private final Control        starterControl;

  private Object cargo = null;

  DragOperationInfo( ETsMouseButton aButton, int aState, ITsPoint aPoint, Control aStarter ) {
    button = TsNullArgumentRtException.checkNull( aButton );
    state = aState;
    point = aPoint;
    starterControl = aStarter;
  }

  /**
   * Determines which mouse button has started the dragging.
   *
   * @return {@link ITsPoint} - button that started the dragging
   */
  public ETsMouseButton button() {
    return button;
  }

  /**
   * Returns keyboard and mouse buttons state when the dragging has started.
   * <p>
   * This is the value as specified for {@link MouseEvent#stateMask}.
   *
   * @return {@link ITsPoint} - state flags when the dragging has started.
   */
  public int startingState() {
    return state;
  }

  /**
   * Returns coordinates where the dragging has started.
   * <p>
   * The coordinates are specified to widget as declared in {@link MouseEvent#x} and {@link MouseEvent#y}.
   *
   * @return {@link ITsPoint} - dragging starting point
   */
  public ITsPoint startingPoint() {
    return point;
  }

  /**
   * Returns the control that started dragging.
   *
   * @return {@link Control} - drag strater control
   */
  public Control starterControl() {
    return starterControl;
  }

  /**
   * Returns user specified data.
   * <p>
   * Event producer {@link TsUserInputEventsBinder} guarantees that the same instance of the {@link DragOperationInfo}
   * will be used during one drag operation.
   *
   * @param <T> - expected type of the user data
   * @return &lt;T&gt; - the user data
   */
  @SuppressWarnings( "unchecked" )
  public <T> T cargo() {
    return (T)cargo;
  }

  /**
   * Returns user specified data if it is of expected type.
   * <p>
   * Returns <code>null</code> if user data is <code>null</code> or user data is not of type <code>aClass</code>.
   * <p>
   * May be used to check if drag event is for the specific handler.
   *
   * @param <T> - expected type of the user data
   * @param aCargoCalss {@link Class}&lt;T&gt; - expected type of the user data
   * @return &lt;T&gt; - the user data
   */
  @SuppressWarnings( "unchecked" )
  public <T> T cargo( Class<T> aCargoCalss ) {
    if( aCargoCalss.isInstance( cargo ) ) {
      return (T)cargo;
    }
    return null;
  }

  /**
   * Sets {@link #cargo()}.
   *
   * @param <T> - type of the used data
   * @param aData &lt;T&gt; - user data , may be <code>null</code>
   */
  public <T> void setCargo( T aData ) {
    cargo = aData;
  }

}
