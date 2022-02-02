package org.toxsoft.core.tslib.utils;

/**
 * The component that supports the concept of shutdoen.
 * <p>
 * This interface is compatible but different from {@link AutoCloseable}. Differences are:
 * <ul>
 * <li>In this interface {@link #close()} does not throws any exception, while in {@link AutoCloseable} it throws
 * checkable exception {@link Exception};</li>
 * <li>this interface can not be used in <code>try</code>-with-resources block;</li>
 * </ul>
 * Any type may extend both {@link ICloseable} and {@link AutoCloseable}.
 *
 * @author hazard157
 */
public interface ICloseable {

  /**
   * Shuts down the pre-initialized component.
   * <p>
   * Never throws an exception. If the component has not been initialized or has already been shut down by this method,
   * it does nothing
   */
  void close();

}
