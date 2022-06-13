package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;
import java.util.concurrent.locks.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;

/**
 * Singleton for {@link IStrioWriter} output to the {@link System#out}.
 * <p>
 * Output is flashed on each {@link IStrioHardConstants#CHAR_EOL}.
 * <p>
 * The class is thread-safe.
 *
 * @author hazard157
 */
public class CharOutputStreamSysOut
    implements ICharOutputStream {

  private static CharOutputStreamSysOut instance = null;
  private final ReentrantLock            lock;

  /**
   * Закрытый конструктор синглтона.
   */
  private CharOutputStreamSysOut() {
    lock = new ReentrantLock();
  }

  /**
   * Returns the singleton instance.
   *
   * @return {@link CharOutputStreamSysOut} - the singleton instance
   */
  public static CharOutputStreamSysOut getInstance() {
    if( instance == null ) {
      instance = new CharOutputStreamSysOut();
    }
    return instance;
  }

  @Override
  public void writeChar( char aCh )
      throws IOException {
    synchronized (lock) {
      System.out.print( aCh );
      if( aCh == CHAR_EOL || aCh == CHAR_EOF ) {
        System.out.flush();
      }
    }
  }

}
