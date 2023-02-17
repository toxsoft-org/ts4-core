package org.toxsoft.core.tslib.utils.logs;

import java.util.*;

import org.toxsoft.core.tslib.utils.errors.*;

// TRANSLATE

/**
 * Интерфейс логирования (журналирования) событий.
 * <p>
 * Этот интерфейс включает минимальный набор методов логирования. Реализации логеров на в разных системах могут (и
 * будут) существенно отличатся, однако этот интерфейс позволяет использовать один код в общих подсистемах, в том числе,
 * в подсистемах TsLib.
 * <p>
 * В общем случае, логер может иметь несколько мест размещения логов, но этот логер подразумевает работу только с одным
 * местом логирования.
 * <p>
 * Некоторые логеры могут иметь понятие "закрывания" (например, лог-файл). Если происходит вызов методов "закрытого"
 * логера, то любой из нижеприведенных методов выбрасывает ислючение {@link TsIllegalStateRtException}. "Закрываемый"
 * логер описывается интерфейсом {@link ICloseableLogger}.
 * <p>
 * Реализация логера должна обеспечивать потоко-безопасность, если иное специально не оговорено.
 *
 * @author hazard157
 */
public interface ILogger {

  /**
   * Нулевой логер, пишет в "никуда", игнорирует логи.
   */
  ILogger NULL = new InternalNullLogger();

  /**
   * Определяет, физически логируются ли сообщаения запрошенной важности.
   * <p>
   * Несмотря на то, что любой метод вывода сообщений (как то log(), warn() error() и т.п.) можно вызвать в любой
   * момент, физически не все сообщения могут выводится в лог. Например, когда {@link ILogger} является оболочкой над
   * Log4J, то в текстовый файл лога попадают те сообщения, которые разрешены в xml-файле конфигурации.
   * <p>
   * Данный метод позволяет заранее узнать, будет ли логированое сообщение запрашиваемой важности, и если нет, то не
   * формировать сообщение. Ведь зачастую подготовка удобочитаемого сообщения само может оказаться достаточно
   * ресурсоемким действием.
   *
   * @param aSeverity ELogSeverity - важностть сообщения
   * @return boolean - признак, что сообщен я указанной важности физически выводятся в лог
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean isSeverityOn( ELogSeverity aSeverity );

  /**
   * Выводит форматированное текстовое сообщение в лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void log( ELogSeverity aLogSeverity, String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение в лог вместе с информацией об исключении.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void log( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object... aArgs );

  /**
   * Выводит исключение в лог с заданной важностью.
   *
   * @param aLogSeverity ELogSeverity - важностть сообщения
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void log( ELogSeverity aLogSeverity, Throwable aException );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#INFO} в лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void info( String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#WARNING} в лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void warning( String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#WARNING} и информацией об исключении в
   * лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void warning( Throwable aException, String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#ERROR} в лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void error( String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#ERROR} и информацией об исключении в
   * лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void error( Throwable aException, String aMessage, Object... aArgs );

  /**
   * Выводит искючение с важностью {@link ELogSeverity#ERROR}.
   *
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void error( Throwable aException );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#DEBUG} в лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void debug( String aMessage, Object... aArgs );

  /**
   * Выводит форматированное текстовое сообщение с важностью {@link ELogSeverity#DEBUG} и информацией об исключении в
   * лог.
   * <p>
   * Форматирование происходит согласно правилам класса {@link Formatter}.
   *
   * @param aException Throwable - исключение, вызвавшее сообщение в лог
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException попутка записи в уже закрытый логер
   */
  void debug( Throwable aException, String aMessage, Object... aArgs );

}

/**
 * Реализация ничего-не-делающего логера для константы {@link ILogger#NULL}.
 *
 * @author hazard157
 */
class InternalNullLogger
    implements ICloseableLogger {

  @Override
  public boolean isSeverityOn( ELogSeverity aSeverity ) {
    TsNullArgumentRtException.checkNull( aSeverity );
    return true;
  }

  @Override
  public void error( String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void error( Throwable aException, String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void error( Throwable aException ) {
    // nop
  }

  @Override
  public void info( String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void log( ELogSeverity aLogSeverity, String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException ) {
    // nop
  }

  @Override
  public void warning( String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void warning( Throwable exception, String message, Object... args ) {
    // nop
  }

  @Override
  public void close() {
    // nop
  }

  @Override
  public void debug( String aMessage, Object... aArgs ) {
    // nop
  }

  @Override
  public void debug( Throwable aException, String aMessage, Object... aArgs ) {
    // nop
  }

}
