package org.toxsoft.tslib.utils.logs.impl;

import static org.toxsoft.tslib.utils.logs.impl.ITsResources.*;

import java.util.Formatter;

import org.toxsoft.tslib.coll.primtypes.*;
import org.toxsoft.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.ELogSeverity;
import org.toxsoft.tslib.utils.logs.ILogger;

// TRANSLATE

/**
 * Message text formatter for loggers.
 * <p>
 * Форматировщик сам не производит вывод текста в назначение (диалоговое окно, лог-файл, консоль и т.п.). Он просто
 * формирует многстрочный текст для дальнейшго вывода средствами одного из классов, реализующих интерфейс логера
 * {@link ILogger}.
 * <p>
 * Каждый логер может может создать экземпляр и настроить параметры форматирования для своих нужд.
 * <p>
 * This is thread-safe class.
 *
 * @author hazard157
 */
public class LogMessageFormatter {

  /**
   * Форматная строка для {@link Formatter}, формирующая метку времени вида "YYYY-MM-DD HH:MM:SS ".<br>
   * Обратите внимание на пробел в конце!
   */
  public static final String TIMESTAMP_FORMAT_STR = "%tF %tT "; //$NON-NLS-1$

  /**
   * Пустой заполнитель, когда не установлен признак {@link #isTimeOnEveryLine}.
   * <p>
   * Занимает столько же места, как и отфорорматированный форматом {@link #TIMESTAMP_FORMAT_STR} метика времени, то есть
   * как строка "YYYY-MM-DD HH:MM:SS ".<br>
   * Обратите внимание на пробел в конце!
   */
  private static final String TIMSTAMP_FILLER_STR = "                    "; //$NON-NLS-1$

  /**
   * Ширина фиксированного поля для вывода уровня важности сообщения.
   * <p>
   * Вычисляется как наибольшая ширина идентификаторов вжности {@link ELogSeverity#id()} в конструкторе.
   */
  private final int severityFieldWidth;

  private final boolean isTimeOnEveryLine;
  private final boolean isIndentedAsTime;
  private final int     indentWidth;

  /**
   * Creates logger with specified options.
   *
   * @param aTimestampOnEveryLine boolean - start every line with timestamp (othervise fill with spaces)
   * @param aIndentAsTimestamp boolean - start with trimestamp or spaces (othervise start line with text)
   * @param aExStackIndentWidth int - indent spaces on every nexted exceptionstack trace output
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LogMessageFormatter( boolean aTimestampOnEveryLine, boolean aIndentAsTimestamp, int aExStackIndentWidth ) {
    isTimeOnEveryLine = aTimestampOnEveryLine;
    isIndentedAsTime = aIndentAsTimestamp;
    int iw = aExStackIndentWidth;
    if( iw < 0 ) {
      indentWidth = 0;
    }
    else {
      if( iw > 40 ) {
        indentWidth = 40;
      }
      else {
        indentWidth = iw;
      }
    }
    int width = 0;
    for( ELogSeverity s : ELogSeverity.values() ) {
      if( s.id().length() > width ) {
        width = s.id().length();
      }
    }
    severityFieldWidth = width;
  }

  /**
   * Creates logger with options default values.
   */
  public LogMessageFormatter() {
    this( false, true, 4 );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Форматирует метку времени в соответствии с {@link #TIMESTAMP_FORMAT_STR}.
   *
   * @param aTimestamp Long - метка времени
   * @return String - строка вида "YYYY-MM-DD HH:MM:SS ".
   */
  private static final String formatTimestamp( Long aTimestamp ) {
    return String.format( TIMESTAMP_FORMAT_STR, aTimestamp, aTimestamp );
  }

  /**
   * Форматирует метку времени в соответствии с {@link #TIMESTAMP_FORMAT_STR}.
   *
   * @param aTimestamp long - метка времени
   * @return String - строка вида "YYYY-MM-DD HH:MM:SS ".
   */
  private static final String formatTimestamp( long aTimestamp ) {
    return formatTimestamp( Long.valueOf( aTimestamp ) );
  }

  /**
   * Внутренняя реализация {@link #format(long, ELogSeverity, String, Object...)}, возвращающая редактируемый список.
   *
   * @param aTimestamp long - метка времени, когда случилось событие
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aMessage String - форматирующий текст сообщения, может быть пустым
   * @param aArgs Object[] - аргументы
   * @return IStringList - список строк многострочного сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( "nls" )
  public IStringListEdit doLogMessageSl( long aTimestamp, ELogSeverity aLogSeverity, String aMessage,
      Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aLogSeverity, aMessage, aArgs );

    // Форматируем строку
    String tmStr = formatTimestamp( aTimestamp );
    String sever = String.format( "[%-" + severityFieldWidth + "s]", aLogSeverity.id() );
    String fmt = String.format( aMessage, aArgs );
    String msg = tmStr + sever + " " + fmt;
    IStringListEdit result = new StringArrayList();

    // если сообщение оказалось многострочным, разобъем его
    splitLines( msg, result );
    String prefix = TsLibUtils.EMPTY_STRING;
    if( isTimeOnEveryLine ) {
      prefix = tmStr;
    }
    else {
      if( isIndentedAsTime ) {
        prefix = TIMSTAMP_FILLER_STR;
      }
    }
    for( int i = 1, n = result.size(); i < n; i++ ) {
      result.set( i, prefix + result.get( i ) );
    }
    return result;
  }

  /**
   * Добавляет в список строки об иключении и рекурсивно о порождающих исключениях.
   *
   * @param aPrefix String - префикс, добавляемый к каждой строке
   * @param aSle IStringListBasicEdit - список строк, куда добавляется ообщение
   * @param aError Throwable - логируемое исключение
   * @param aLevel int - уровень отступа при рекурсивных вызовах
   */
  @SuppressWarnings( "nls" )
  private final void addErrorStack( String aPrefix, IStringListBasicEdit aSle, Throwable aError, int aLevel ) {
    // информация об исключении
    String indent;
    if( aLevel > 0 ) {
      String fmtStr = "%-" + aLevel * indentWidth + "s";
      indent = String.format( fmtStr, "" );
    }
    else {
      indent = TsLibUtils.EMPTY_STRING;
    }
    indent = aPrefix + indent;

    if( aError instanceof TsException ) {
      TsException e = (TsException)aError;
      String cName = aError.getClass().getSimpleName();
      Long timeStamp = Long.valueOf( e.timestamp() );
      aSle.add( indent + String.format( "%s (%tF %tT.%tL)", cName, timeStamp, timeStamp, timeStamp ) );
      aSle.add( indent + aError.getMessage() );
    }
    else {
      String cName = aError.getClass().getCanonicalName();
      if( cName == null ) {
        cName = aError.getClass().getName();
      }
      aSle.add( indent + cName );
      aSle.add( indent + STR_EXCEPTION_MESSAGE_LABEL + aError.getMessage() );
    }

    // выведем информацию о стеке
    StackTraceElement[] trace = aError.getStackTrace();
    for( int i = 0; i < trace.length; i++ ) {
      aSle.add( indent + trace[i].toString() );
    }

    // если есть еще вложенные исключения - выводим и их
    if( aError.getCause() != null ) {
      aSle.add( indent + STR_CAUSED_BY );
      addErrorStack( aPrefix, aSle, aError.getCause(), aLevel + 1 );
    }
  }

  /**
   * Разбивает многострочный текст на набор строк и добавляет из в список строк.
   * <p>
   * Находит символы перевода строки в aMultiLineText и выделяет строки между ними в отдельные элементы и добавляет их в
   * список aSle. Естественно, в результат не включаются сами символы перевода строки. Если аргумент - пустая строка,
   * или не содержит символов перевода строки, то добавляет в список единственный эелемент - строку aMultiLineText.
   * <p>
   * В качетсве сиволов перевода строки рассматриваются одиночные символы '\n' (Unix) и '\r' (MAcOS), а также пара
   * символов '\n\r' (DOS/Windows).
   *
   * @param aMultiLineText String - потенциально многострочный текст
   * @param aSle IStringListBasicEdit- список строк, куда добавляется результат разбиения
   * @throws TsNullArgumentRtException аргумент = null
   */
  private static void splitLines( String aMultiLineText, IStringListBasicEdit aSle ) {
    TsNullArgumentRtException.checkNulls( aMultiLineText, aSle );
    int argLen = aMultiLineText.length();
    if( argLen == 0 ) {
      aSle.add( aMultiLineText );
      return;
    }
    int i1 = 0, i = 0;
    while( i < argLen ) {
      char ch = aMultiLineText.charAt( i );
      if( ch == '\r' ) { // MacOS '\r'
        aSle.add( aMultiLineText.substring( i1, i ) );
        i1 = ++i;
        continue;
      }
      if( ch == '\n' ) { // Unix '\n'
        if( i < argLen - 1 && aMultiLineText.charAt( i + 1 ) == '\r' ) { // DOS/Win '\n\r'
          aSle.add( aMultiLineText.substring( i1, i ) );
          ++i;
          i1 = ++i;
          continue;
        }
        aSle.add( aMultiLineText.substring( i1, i ) );
        i1 = ++i;
        continue;
      }
      ++i;
    }
    aSle.add( aMultiLineText.substring( i1, i ) );
  }

  // ------------------------------------------------------------------------------------
  // Методы форматированнания сообщений
  //

  /**
   * Форматирует сообщение в соответствии с важностью, текущим моментом времени и текстом сообщения с аргументами.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   * <p>
   * Каждый элемент списка-результата хранит ровно одну строку сообщения.
   *
   * @param aTimestamp long - метка времени, когда случилось событие
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aMessage String - форматирующий текст сообщения, может быть пустым
   * @param aArgs Object[] - аргументы
   * @return IStringList - список строк многострочного сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public IStringList format( long aTimestamp, ELogSeverity aLogSeverity, String aMessage, Object... aArgs ) {
    return doLogMessageSl( aTimestamp, aLogSeverity, aMessage, aArgs );
  }

  /**
   * Форматирует сообщение в соответствии с важностью, текущим моментом времени, вызвавшим исключением и текстом
   * сообщения с аргументами.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   * <p>
   * Каждый элемент списка-результата хранит ровно одну строку сообщения.
   *
   * @param aTimestamp long - метка времени, когда случилось событие
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @param aMessage String - форматирующий текст сообщения, может быть пустым
   * @param aArgs Object[] - аргументы
   * @return IStringList - список строк многострочного сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public IStringList format( long aTimestamp, ELogSeverity aLogSeverity, Throwable aException, String aMessage,
      Object... aArgs ) {
    TsNullArgumentRtException.checkNull( aException );
    IStringListEdit sl = doLogMessageSl( aTimestamp, aLogSeverity, aMessage, aArgs );

    String prefix = TsLibUtils.EMPTY_STRING;
    if( isTimeOnEveryLine ) {
      prefix = formatTimestamp( aTimestamp );
    }
    else {
      if( isIndentedAsTime ) {
        prefix = TIMSTAMP_FILLER_STR;
      }
    }
    addErrorStack( prefix, sl, aException, 0 );
    return sl;
  }

}
