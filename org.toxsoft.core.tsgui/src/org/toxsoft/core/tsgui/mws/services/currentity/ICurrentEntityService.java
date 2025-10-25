package org.toxsoft.core.tsgui.mws.services.currentity;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Introduces concept of some entity currently selected in application.
 * <p>
 * <p>
 * Usage:
 * <ul>
 * <li>Create descendant interface <code>ICurrentXxxService</code>;</li>
 * <li>Subclass from {@link CurrentEntityService} and new interface <code>ICurrentXxxService</code>;</li>
 * <li>Create instance and put it to the application context with the key <code>ICurrentXxxService.class</code>, usually
 * in <code>Quant.initApp()</code> or <code>Addon.initApp()</code>.</li>
 * </ul>
 *
 * @author hazard157
 * @param <E> - type of "something"
 */
public interface ICurrentEntityService<E> {

  /**
   * Returns the current entity.
   *
   * @return <b>E</b> - the current entity or <code>null</code>
   */
  E current();

  /**
   * Sets the current entity.
   *
   * @param aCurrent &lt;E&gt; - the current entity or <code>null</code>
   * @return boolean - determines if {@link #current()} was actually changed<br>
   *         <b>true</b> - {@link #current()} was changed;<br>
   *         <b>false</b> - {@link #current()} was not changed, old and new are equal with <b>==</b> operator check.
   */
  boolean setCurrent( E aCurrent );

  /**
   * Generates an event {@link ICurrentEntityChangeListener#onCurrentContentChanged(Object)}.
   * <p>
   * Useful when reference to the current entity does nt changes but entity properties changes.
   */
  void informOnContentChange();

  /**
   * Add the current entity change listener if not added before.
   *
   * @param aListener {@link ICurrentEntityChangeListener} - he listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener );

  /**
   * Removes the listener if any found
   *
   * @param aListener {@link ICurrentEntityChangeListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeCurrentEntityChangeListener( ICurrentEntityChangeListener<E> aListener );

  /**
   * Sets interceptor of the {@link #setCurrent(Object)} processing.
   * <p>
   * Specifing <code>null</code> as an argument will remove interceptor.
   *
   * @param aInterceptor {@link ICurrentEntityChangeInterceptor} - the interceptor or <code>null</code>
   */
  void setCurrentEntityChangeInterceptor( ICurrentEntityChangeInterceptor<E> aInterceptor );

}
