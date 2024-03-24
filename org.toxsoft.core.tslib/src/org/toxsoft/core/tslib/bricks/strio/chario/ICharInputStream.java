package org.toxsoft.core.tslib.bricks.strio.chario;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Charachter reader (input) stream.
 *
 * @author hazard157
 */
public interface ICharInputStream {

  /**
   * Position in stream.
   *
   * @author hazard157
   */
  interface IPosition {

    /**
     * Returns human readable name of input stream.
     * <p>
     * For file inputs it may be absolute file path, for network data - URL of source.
     *
     * @return String - human readable name of input stream
     */
    String inputName();

    /**
     * Returns the number of line since start of reading.
     * <p>
     * Line number changes after reader encounters end of line symbol. Return 0 before reading starts.
     *
     * @return int - number of current line (first line has number 1)
     */
    int lineNo();

    /**
     * Returns just returned charachter position on current line.
     * <p>
     * Return 0 before reading starts.
     *
     * @return int - just returned charachter position on line (first char has number 1)
     */
    int charNo();

  }

  /**
   * Returns current position in stream.
   *
   * @return {@link IPosition} - current position ins stream
   */
  IPosition currentPosition();

  /**
   * Returns current end-of-file callback.
   *
   * @return ICharInputEofCallback - end-of-file callback
   */
  ICharInputEofCallback eofCallback();

  /**
   * Sets end-of-file callback.
   *
   * @param aEofCallback CharInputEofCallback - end-of-file callback
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void setEofCallback( ICharInputEofCallback aEofCallback );

  /**
   * Reads next character from inpuit stream.
   * <p>
   * At the end of file returns -1.
   *
   * @return int - character from inpuit stream or -1 on EOF
   * @throws IOException any input/output error occured
   */
  int readChar()
      throws IOException;

}
