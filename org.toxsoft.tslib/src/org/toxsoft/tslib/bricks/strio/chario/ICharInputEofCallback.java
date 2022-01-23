package org.toxsoft.tslib.bricks.strio.chario;

import org.toxsoft.tslib.utils.errors.TsIoRtException;

/**
 * Callback methods when {@link ICharInputStream} encounters end of file.
 *
 * @author hazard157
 */
public interface ICharInputEofCallback {

  /**
   * "None" special case impementation, alwayes returns -1.
   */
  ICharInputEofCallback NONE = new InternalNullCharInputEofCallback();

  /**
   * Called before {@link ICharInputStream#readChar()} returns -1.
   * <p>
   * This method can load data from disk or wait for data from network. If method delivers more data to be read it must
   * return next character, not -1.
   *
   * @return int - return value for caller {@link ICharInputStream#readChar()} method
   * @throws TsIoRtException I/O error
   */
  int onEof()
      throws TsIoRtException;

}

class InternalNullCharInputEofCallback
    implements ICharInputEofCallback {

  @Override
  public int onEof()
      throws TsIoRtException {
    return -1;
  }

}
