package org.toxsoft.tslib.utils.logs.impl;

import java.io.PrintStream;

import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Legger with output to the {@link PrintStream}.
 *
 * @author hazard157
 */
public class PrintStreamLogger
    extends AbstractLogger {

  private final PrintStream out;

  /**
   * Создает логер с настройками форматирования, удобными для просмотра на экране.
   *
   * @param aOut PrintStream - выходной поток, куда пишет логер
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PrintStreamLogger( PrintStream aOut ) {
    this( aOut, new LogMessageFormatter() );
  }

  /**
   * Создает логер с настройками форматирования, удобными для просмотра на экране.
   *
   * @param aOut PrintStream - выходной поток, куда пишет логер
   * @param aFormatter {@link LogMessageFormatter} - log messages formatter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PrintStreamLogger( PrintStream aOut, LogMessageFormatter aFormatter ) {
    super( aFormatter );
    if( aOut == null ) {
      throw new TsNullArgumentRtException();
    }
    out = aOut;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkLogger
  //

  @Override
  protected void printLine( String aLine ) {
    synchronized (out) {
      out.println( aLine );
    }
  }

}
