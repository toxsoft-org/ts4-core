package org.toxsoft.core.tslib.utils.logs;

import org.toxsoft.core.tslib.utils.ICloseable;

/**
 * Logger with mandatory {@link #close()} method.
 * <p>
 * Note: this interface also expands {@link AutoCloseable}, allowing to use it in {@code try}-with-resources block. Also
 * note that {@link #close()} of this interface does declares <code>throws {@link Exception}</code>.
 *
 * @author hazard157
 */
public interface ICloseableLogger
    extends ILogger, ICloseable, AutoCloseable {

  /**
   * Overrides both {@link ICloseable#close()} and {@link AutoCloseable#close()}.
   */
  @Override
  void close();

}
