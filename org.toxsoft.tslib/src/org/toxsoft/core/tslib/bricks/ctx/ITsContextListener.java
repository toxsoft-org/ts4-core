package org.toxsoft.core.tslib.bricks.ctx;

import org.toxsoft.core.tslib.av.IAtomicValue;

/**
 * Listener to the changes in TS context.
 * <p>
 * Note on listener methods invocation: the change event initially is fired by the context for which mutator method was
 * called, like {@link ITsContext#put(Class, Object)}. The same about options editing. This is called source context and
 * reference to the source context is send as <code>aSource</code> argument of the <code>onXxx()</code> methods of this
 * interface.
 *
 * @author hazard157
 */
public interface ITsContextListener {

  /**
   * Informs about context reference change.
   * <p>
   * Listener is called every time when client sets the value of the reference, even if new value is equal to the old
   * one.
   *
   * @param <C> - context type
   * @param aSource {@link ITsContextRo} - the source
   * @param aName String - changed reference name or <code>null</code> on batch changes
   * @param aRef {@link IAtomicValue} - the changed reference or <code>null</code> on batch changes
   */
  <C extends ITsContextRo> void onContextRefChanged( C aSource, String aName, Object aRef );

  /**
   * Informs about context option value change.
   * <p>
   * Listener is called every time when client sets option value, even if new value is equal to the old one.
   *
   * @param <C> - context type
   * @param aSource {@link ITsContextRo} - the source
   * @param aId String - changed option ID or <code>null</code> on batch changes
   * @param aValue {@link IAtomicValue} - the value of the changed option or <code>null</code> on batch changes
   */
  <C extends ITsContextRo> void onContextOpChanged( C aSource, String aId, IAtomicValue aValue );

}
