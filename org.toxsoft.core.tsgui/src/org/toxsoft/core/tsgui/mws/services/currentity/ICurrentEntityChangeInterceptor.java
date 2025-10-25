package org.toxsoft.core.tsgui.mws.services.currentity;

/**
 * Intercepts and may affect setting current entity.
 *
 * @author hazard157
 * @param <E> - type of "something"
 */
public interface ICurrentEntityChangeInterceptor<E> {

  /**
   * Subclass may handle current entity change or even determine which value to set.
   * <p>
   * This method is called after current entity changed but before listeners are informed.
   * <p>
   * If method returns <code>aOld</code> reference (checked by <code><b>==</b></code> operator) then call to
   * {@link ICurrentEntityService#setCurrent(Object)} will have no effect.
   * <p>
   * If method returns <code>aNew</code> reference (checked by <code><b>==</b></code> operator) then call to
   * {@link ICurrentEntityService#setCurrent(Object)} will proceed as if interceptor was not set.
   * <p>
   * Returning all other value continues as if {@link ICurrentEntityService#setCurrent(Object)} was called with argument
   * rertruned by this method.
   *
   * @param aOld &lt;E&gt; - old value of {@link ICurrentEntityService#current()} may be <code>null</code>
   * @param aNew &lt;E&gt; - new value of {@link ICurrentEntityService#current()} may be <code>null</code>
   * @return &lt;E&gt; - the value to be actually set as a new value
   */
  E beforeListenersInformed( E aOld, E aNew );
}
