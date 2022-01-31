package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Character output stream to {@link Appendable} receiver.
 * <p>
 * This class may be used to write to the receivers {@link StringBuffer}, {@link StringBuilder}, {@link PrintStream},
 * {@link Writer}, etc.
 *
 * @author hazard157
 */
public class CharOutputStreamAppendable
    implements ICharOutputStream {

  private final Appendable destination;

  /**
   * Creates output stream to the specified receiver.
   *
   * @param aDestination {@link Appendable} - the receiver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CharOutputStreamAppendable( Appendable aDestination ) {
    destination = TsNullArgumentRtException.checkNull( aDestination );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @Override
  public void writeChar( char aCh )
      throws IOException {
    destination.append( aCh );
  }

}
