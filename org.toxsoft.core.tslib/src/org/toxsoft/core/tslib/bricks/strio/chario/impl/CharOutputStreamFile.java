package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.*;
import java.nio.charset.*;

import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Character output stream (encodesd as {@link StandardCharsets#UTF_8}) to the {@link File} receiver.
 *
 * @author hazard157
 */
public class CharOutputStreamFile
    implements ICharOutputStreamCloseable {

  private final Writer writer;
  private final char[] cbuf   = new char[1];
  private boolean      closed = false;

  /**
   * Creates output stream to the specified receiver.
   *
   * @param aDestination {@link Writer} - the receiver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CharOutputStreamFile( File aDestination ) {
    TsFileUtils.checkFileAppendable( aDestination );
    try {
      writer = new FileWriter( aDestination, StandardCharsets.UTF_8 );
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
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

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICharOutputStreamCloseable
  //

  @Override
  public boolean isClosed() {
    return closed;
  }

  // ------------------------------------------------------------------------------------
  // Interface ICloseable
  //

  @Override
  public void close() {
    if( !isClosed() ) {
      try {
        writer.close();
        closed = true;
      }
      catch( IOException ex ) {
        // don't throw silly exception
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

}
