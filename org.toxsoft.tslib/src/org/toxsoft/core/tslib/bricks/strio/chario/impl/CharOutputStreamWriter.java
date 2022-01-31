package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.IOException;
import java.io.Writer;

import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Character output stream to {@link Writer} receiver.
 *
 * @author hazard157
 */
public class CharOutputStreamWriter
    implements ICharOutputStream {

  private final Writer writer;
  private final char[] cbuf = new char[1];

  /**
   * Creates output stream to the specified receiver.
   *
   * @param aDestination {@link Writer} - the receiver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "resource" )
  public CharOutputStreamWriter( Writer aDestination ) {
    TsNullArgumentRtException.checkNull( aDestination );
    writer = aDestination;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICharOutputStream
  //

  @Override
  public void writeChar( char aCh )
      throws IOException {
    cbuf[0] = aCh;
    writer.write( cbuf );
  }

}
