package org.toxsoft.tslib.bricks.strio.chario.impl;

import java.io.*;

import org.toxsoft.tslib.bricks.strio.chario.ICharInputStreamCloseable;
import org.toxsoft.tslib.utils.errors.TsIoRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.files.TsFileUtils;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Text file reader charachter input stream.
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
      reader = new FileReader( aInFile );
    }
    catch( FileNotFoundException ex ) {
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
