package org.toxsoft.core.tsgui.bricks.uievents;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsKeyInputProducer} delegating implementation.
 * <p>
 * Delegates (resends) key input events from existing producer further to the listeners registered in this instance with
 * changes event source.
 *
 * @author hazard157
 */
public class TsKeyInputDelegator
    implements ITsKeyInputProducer, ITsKeyInputListener {

  private final IListEdit<ITsKeyInputListener> listeners = new ElemArrayList<>();

  private Object source;

  /**
   * Constructor.
   *
   * @param aSource Object - new source of the events
   * @param aProducer {@link ITsKeyInputProducer} - the delegated producersssssssss
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsKeyInputDelegator( Object aSource, ITsKeyInputProducer aProducer ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  public void addTsKeyInputListener( ITsKeyInputListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeTsKeyInputListener( ITsKeyInputListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( doBeforeDelegateKeyDown( aCode, aChar, aState ) ) {
      return true;
    }
    // send key down event to all listeners until one returns true
    if( !listeners.isEmpty() ) {
      IList<ITsKeyInputListener> ll = new ElemArrayList<>( listeners );
      for( ITsKeyInputListener l : ll ) {
        if( l.onKeyDown( source, aCode, aChar, aState ) ) {
          return true;
        }
      }
    }
    return doAfterDelegateKeyDown( aCode, aChar, aState );
  }

  @Override
  public boolean onKeyUp( Object aSource, int aCode, char aChar, int aState ) {
    if( doBeforeDelegateKeyUp( aCode, aChar, aState ) ) {
      return true;
    }
    // send key up event to all listeners until one returns true
    if( !listeners.isEmpty() ) {
      IList<ITsKeyInputListener> ll = new ElemArrayList<>( listeners );
      for( ITsKeyInputListener l : ll ) {
        if( l.onKeyUp( source, aCode, aChar, aState ) ) {
          return true;
        }
      }
    }
    return doAfterDelegateKeyUp( aCode, aChar, aState );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may process key press event before resending to the listeners.
   * <p>
   * If method returns <code>true</code> no listeners or {@link #doAfterDelegateKeyDown(int, char, int)} will be called.
   * <p>
   * In base class returns <code>false</code>, there is no need to call superclass method when overriding.
   *
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return default boolean - event processing flag
   */
  protected boolean doBeforeDelegateKeyDown( int aCode, char aChar, int aState ) {
    return false;
  }

  /**
   * Subclass may process key press event after listeners.
   * <p>
   * Method is <b>not</b> called if any listeners returns <code>true</code>.
   * <p>
   * In base class returns <code>false</code>, there is no need to call superclass method when overriding.
   *
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return default boolean - event processing flag
   */
  protected boolean doAfterDelegateKeyDown( int aCode, char aChar, int aState ) {
    return false;
  }

  /**
   * Subclass may process key release event before resending to the listeners.
   * <p>
   * If method returns <code>true</code> no listeners or {@link #doAfterDelegateKeyUp(int, char, int)} will be called.
   * <p>
   * In base class returns <code>false</code>, there is no need to call superclass method when overriding.
   *
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return default boolean - event processing flag
   */
  protected boolean doBeforeDelegateKeyUp( int aCode, char aChar, int aState ) {
    return false;
  }

  /**
   * Subclass may process key release event after listeners.
   * <p>
   * Method is <b>not</b> called if any listeners returns <code>true</code>.
   * <p>
   * In base class returns <code>false</code>, there is no need to call superclass method when overriding.
   *
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @return default boolean - event processing flag
   */
  protected boolean doAfterDelegateKeyUp( int aCode, char aChar, int aState ) {
    return false;
  }

}
