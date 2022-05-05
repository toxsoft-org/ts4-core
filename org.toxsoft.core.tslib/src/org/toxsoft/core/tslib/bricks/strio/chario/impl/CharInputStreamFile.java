package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.*;
import java.nio.charset.*;

import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Text file reader charachter input stream in {@link StandardCharsets#UTF_8}.
 * <p>
 * This interface extends {@link Closeable}, so it is required to {@link #close()} instance after creation.
 * Try-with-resources construction may be used for this.
 *
 * @author hazard157
 */
public final class CharInputStreamFile
    extends AbstractCharInputStream
    implements ICharInputStreamCloseable {

  private final char[] cbuf = new char[1];
  private FileReader   reader;

  private boolean closed = false;

  /**
   * Creates input stream from file.
   *
   * @param aInFile {@link File} - inpit file
   * @throws TsNullArgumentRtException argument = null
   * @throws TsIoRtException error accessing file
   */
  public CharInputStreamFile( File aInFile ) {
    super( TsFileUtils.checkFileReadable( aInFile ).getPath() );
    try {
      reader = new FileReader( aInFile, StandardCharsets.UTF_8 );
    }
    catch( IOException ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @Override
  public int doReadChar()
      throws IOException {
    if( isClosed() ) {
      return -1;
    }
    // is there any data to read?
    if( !reader.ready() ) {
      return -1;
    }
    // read next char or EOF?
    if( reader.read( cbuf ) == -1 ) {
      return -1;
    }
    return cbuf[0];
  }

  @Override
  public void close() {
    if( reader != null ) {
      try {
        reader.close();
        closed = true;
      }
      catch( IOException ex ) {
        // ignore silly exception-on-close
        LoggerUtils.errorLogger().error( ex );
      }
      reader = null; // done
    }
  }

  // ------------------------------------------------------------------------------------
  // Interface ICharInputStreamCloseable
  //

  @Override
  public boolean isClosed() {
    return closed;
  }

}
