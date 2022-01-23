package org.toxsoft.tslib.bricks.strio.chario;

import java.io.Closeable;

import org.toxsoft.tslib.utils.ICloseable;

/**
 * Ouput stream of characters which requires to be closed.
 *
 * @author hazard157
 */
public interface ICharOutputStreamCloseable
    extends ICharOutputStream, ICloseable, Closeable {

  /**
   * Determines if stream is close.
   *
   * @return boolean - <code>true</code> if stream is already closed
   */
  boolean isClosed();

}
