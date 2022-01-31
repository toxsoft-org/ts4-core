package org.toxsoft.core.tslib.utils.logs.impl;

import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ELogSeverity;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * {@link ILogger} implementation without formatted text output.
 * <p>
 * This class implements all methods of the {@link ILogger}. Descendants must only:
 * <ul>
 * <li>setup log formatting options;</li>
 * <li>implement {@link #printLine(String)} to write messages to the desired output stream.</li>
 * </ul>
 *
 * @author hazard157
 */
public abstract class AbstractLogger
    extends AbstractBasicLogger {

  private final LogMessageFormatter formatter;

  /**
   * Constructor.
   *
   * @param aFormatter {@link LogMessageFormatter} - log messages formatter
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  protected AbstractLogger( LogMessageFormatter aFormatter ) {
    formatter = TsNullArgumentRtException.checkNull( aFormatter );
  }

  // ------------------------------------------------------------------------------------
  // For descrndants
  //

  protected LogMessageFormatter msgFormatter() {
    return formatter;
  }

  // ------------------------------------------------------------------------------------
  // ILogger
  //

  @Override
  public void log( ELogSeverity aLogSeverity, String aMessage, Object... aArgs ) {
    if( beforeLog( aLogSeverity, null, aMessage, aArgs ) ) {
      IStringList sl = formatter.format( System.currentTimeMillis(), aLogSeverity, aMessage, aArgs );
      for( int i = 0, n = sl.size(); i < n; i++ ) {
        printLine( sl.get( i ) );
      }
      afterLog();
    }
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object... aArgs ) {
    if( beforeLog( aLogSeverity, aException, aMessage, aArgs ) ) {
      IStringList sl = formatter.format( System.currentTimeMillis(), aLogSeverity, aException, aMessage, aArgs );
      for( int i = 0, n = sl.size(); i < n; i++ ) {
        printLine( sl.get( i ) );
      }
      afterLog();
    }
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException ) {
    if( beforeLog( aLogSeverity, aException, null, null ) ) {
      IStringList sl = formatter.format( System.currentTimeMillis(), aLogSeverity, aException, TsLibUtils.EMPTY_STRING,
          TsLibUtils.EMPTY_ARRAY_OF_OBJECTS );
      for( int i = 0, n = sl.size(); i < n; i++ ) {
        printLine( sl.get( i ) );
      }
      afterLog();
    }
  }

  // ------------------------------------------------------------------------------------
  // Methods for descendants
  //

  // TRANSLATE

  /**
   * Выполнение предусловий и опеределение, надо ли заносить сообщение в лог.
   * <p>
   * Вызывается перед началом логирования, то есть перед вызовами {@link #printLine(String)} одного сообщения.
   * <p>
   * Внимание: неиспользуемые аргументы имеют значение null!
   * <p>
   * В классе {@link AbstractLogger} ничего не делает, просто возвращает true.
   *
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aException Throwable - исключение, вызвавшее сообщение в лог или null
   * @param aMessage String - текст форматировнного сообщения или null
   * @param aArgs Object[] - аргументы форматирования или null
   * @return <b>true</b> - сообщение будет занесено в лог;<br>
   *         <b>false</b> - сообщение будет игнорировано.
   */
  protected boolean beforeLog( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object[] aArgs ) {
    // здесь ничего не делает
    return true;
  }

  /**
   * Вызывается после окончания логирования, то есть перед вызовами {@link #printLine(String)} одного сообщения.
   * <p>
   * Наследники могут переопределить, если надо предпринять действия после занесения лог-сообщения, например, сбрость
   * буферы выходного потока.
   * <p>
   * В классе {@link AbstractLogger} ничего не делает.
   */
  protected void afterLog() {
    // здесь ничего не делает
  }

  /**
   * Выводит в назначение лога одну строку текста.
   * <p>
   * {@link AbstractLogger} гарантирует, что aLine не содержит сиволы перевода строки '\r' или новой строки '\n'.
   *
   * @param aLine String - одна строка текста, может быть пустой строкой но не null
   */
  abstract protected void printLine( String aLine );

}
