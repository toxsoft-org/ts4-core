package org.toxsoft.core.tslib.bricks.strio.chario;

import java.io.IOException;

import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Ouput stream of characters.
 *
 * @author hazard157
 */
public interface ICharOutputStream {

  /**
   * None special case output stream, any attempt to write throws {@link TsNullObjectErrorRtException}.
   */
  ICharOutputStream NONE = new InternalNoneCharOutputStream();

  /**
   * Writes one character to output stream.
   *
   * @param aCh char - character to write
   * @throws IOException - input/output error
   */
  void writeChar( char aCh )
      throws IOException;

}

class InternalNoneCharOutputStream
    implements ICharOutputStream {

  @Override
  public void writeChar( char aCh )
      throws IOException {
    throw new TsNullObjectErrorRtException();
  }

}
