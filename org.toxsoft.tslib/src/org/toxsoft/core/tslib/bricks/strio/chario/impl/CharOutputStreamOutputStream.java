package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.IOException;
import java.io.OutputStream;

import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Character output stream to {@link OutputStream} receiver.
 *
 * @author hazard157
 */
public class CharOutputStreamOutputStream
    implements ICharOutputStream {

  private final OutputStream outstream;
  private final byte[]       b = new byte[2];

  /**
   * Creates output stream to the specified receiver.
   *
   * @param aDestination {@link OutputStream} - the receiver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "resource" )
  public CharOutputStreamOutputStream( OutputStream aDestination ) {
    TsNullArgumentRtException.checkNull( aDestination );
    outstream = aDestination;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICharOutputStream
  //

  @Override
  public void writeChar( char aCh )
      throws IOException {
    b[1] = (byte)(aCh);
    b[0] = (byte)(aCh >>> 8);
    outstream.write( b );
  }

}
