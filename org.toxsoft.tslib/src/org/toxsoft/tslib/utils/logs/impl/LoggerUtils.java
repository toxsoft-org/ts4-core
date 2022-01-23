package org.toxsoft.tslib.utils.logs.impl;

import java.io.File;

import org.toxsoft.tslib.utils.errors.TsIoRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.*;

// TRANSLATE

/**
 * Вспомогатеьлные методы логирования и получения ссылок на известные логеры.
 * <p>
 * Лог - это журнал, в котором заносятся сообщения системы в текстовом виде. Логер (наследник {@link ILogger}) - класс,
 * который заносит (выводит) сообщения в лог. Лог может выводится в стандартный поток вывода, в файла, базу данных и
 * т.п. Это зависит от реализации логера.
 * <p>
 * С каждим сообщением в лог связывается:
 * <ul>
 * <li>метка времени - назначается логером по системным часам {@link System#currentTimeMillis()};</li>
 * <li>важность {@link ELogSeverity} - задается пользователем, в соответствии с сутью лог-сообщения;</li>
 * <li>текстовое сообщение - удобочитаемое, краткое описание, тело сообщения. ISkLogger предоставляет возможность
 * форматированного вывода, аналогично {@link String#format(String, Object...)}. Текст сообщения может быть
 * многострочным.;</li>
 * <li>необязательноя исключнение - зачастю, необходимость вывода сообщения в лог определяется программным исключением (
 * {@link Throwable}). С сообщением можно связать одно исключение, которое может в свою очередь включать цепочку
 * исключений методом {@link Throwable#getCause()}.</li>
 * </ul>
 * <p>
 * Данная подсистема логирования предназначена для создания минимальной базовой инфраструктуры, общей для кода больших
 * систем. Основой подсистемы являестя интерфейс логера {@link ILogger}. Каждый, кому нужна функциональность
 * логирования, должен создать (или получить разделяемый) экземпляр {@link ILogger}.
 * <p>
 * В подсистеме реализованы следующие логеры:
 * <ul>
 * <li>{@link ILogger#NULL} - заглушка, логер, который ничего никуда не пишет;</li>
 * <li>{@link #stdoutLogger()} - ссылка на разделяемый экземпляр логера, пишущего в стандартный вывод {@link System#out}
 * ;</li>
 * <li>{@link #stderrLogger()} - ссылка на разделяемый экземпляр логера, пишущего в стандартный вывод ошибок
 * {@link System#err};</li>
 * <li>Файловые логеры - создаются методом {@link #createFileLogger(File, boolean, boolean)}, и после использования
 * требует завершения работы методом {@link ICloseableLogger#close()}.</li>
 * </ul>
 * <p>
 * Кроме того, поддерживается понятия:
 * <ul>
 * <li>логер по умолчанию - методы {@link #defaultLogger()} и {@link #setDefaultLogger(ILogger)};</li>
 * <li>логер ошибок по умолчанию - методы {@link #errorLogger()} и {@link #setErrorLogger(ILogger)}.</li>
 * <li></li>
 * </ul>
 * Эти логеры предназанчены для использования общими подсистемами системы.
 *
 * @author hazard157
 */
public final class LoggerUtils {

  /**
   * Единственный экземпляр логера, пишущего в {@link System#out}.
   */
  private static final ILogger stdoutLogger = new PrintStreamLogger( System.out );

  /**
   * Единственный экземпляр логера, пишущего в {@link System#err}.
   */
  private static final ILogger stderrLogger = new PrintStreamLogger( System.err );

  /**
   * Логер по умолчанию.
   */
  private static ILogger defaultLogger = stdoutLogger;

  /**
   * Логер ошибок по умолчанию.
   */
  private static ILogger errorLogger = stderrLogger;

  // ------------------------------------------------------------------------------------
  // Методы получения разных логеров
  //

  /**
   * Возвращает ссылку на единственный, экземпляр логера, пишущего в стандартный выход {@link System#out}.
   * <p>
   * Вовзращаемый логер является потоко-безопасным.
   *
   * @return {@link ILogger} - логер, пишущий в {@link System#out}.
   */
  public static ILogger stdoutLogger() {
    return stdoutLogger;
  }

  /**
   * Возвращает ссылку на единственный, экземпляр логера, пишущего в стандартный вывод ошибок {@link System#err}.
   * <p>
   * Вовзращаемый логер является потоко-безопасным.
   *
   * @return {@link ILogger} - логер, пишущий в {@link System#err}.
   */
  public static ILogger stderrLogger() {
    return stderrLogger;
  }

  /**
   * Возвращает вновь созданный экземпляр логера, лишущего в файл,
   * <p>
   * Внимание: возвращается интерфейс {@link ICloseableLogger}, после завершения использования его следует закрыть
   * методом {@link ICloseableLogger#close()}.
   *
   * @param aLogFile File - текстовый файл, в который будет производится запись
   * @param aAppend <b>true</b> - добавлять сообщения в конец существующего файла;<br>
   *          <b>false</b> - очистить файл, ин начать запись сообщении в пустой файл.
   * @param aAutoCreate <b>true</b> - если не существут, создать лог-файл aLogFile;<br>
   *          <b>false</b> - если не существует файл aLogFile, выбросить исключение.
   * @param aFormatter {@link LogMessageFormatter} - log messages formatter
   * @return {@link ICloseableLogger} - логер, с необходимостью "закрытия" после завершения использования
   * @throws TsNullArgumentRtException aLogFile = null
   * @throws TsIoRtException при ошибках работы с файлами
   */
  public static ICloseableLogger createFileLogger( File aLogFile, boolean aAppend, boolean aAutoCreate,
      LogMessageFormatter aFormatter ) {
    return new FileLogger( aLogFile, aAppend, aAutoCreate, aFormatter );
  }

  /**
   * Возвращает вновь созданный экземпляр логера, лишущего в файл,
   * <p>
   * Внимание: возвращается интерфейс {@link ICloseableLogger}, после завершения использования его следует закрыть
   * методом {@link ICloseableLogger#close()}.
   *
   * @param aLogFile File - текстовый файл, в который будет производится запись
   * @param aAppend <b>true</b> - добавлять сообщения в конец существующего файла;<br>
   *          <b>false</b> - очистить файл, ин начать запись сообщении в пустой файл.
   * @param aAutoCreate <b>true</b> - если не существут, создать лог-файл aLogFile;<br>
   *          <b>false</b> - если не существует файл aLogFile, выбросить исключение.
   * @return {@link ICloseableLogger} - логер, с необходимостью "закрытия" после завершения использования
   * @throws TsNullArgumentRtException aLogFile = null
   * @throws TsIoRtException при ошибках работы с файлами
   */
  public static ICloseableLogger createFileLogger( File aLogFile, boolean aAppend, boolean aAutoCreate ) {
    return new FileLogger( aLogFile, aAppend, aAutoCreate, new LogMessageFormatter() );
  }

  /**
   * Создает логер, дописывающий сообщения в лог-файл.
   * <p>
   * Если файл не существует, то он будет создан. Данный метод эквивалентен вызову
   * {@link #createFileLogger(File, boolean, boolean) createFileLogger(aLogFile,true,true)}.
   *
   * @param aLogFile File - текстовый файл, в который будет производится запись
   * @return {@link ICloseableLogger} - логер, с необходимостью "закрытия" после завершения использования
   * @throws TsNullArgumentRtException aLogFile = null
   * @throws TsIoRtException при ошибках работы с файлами
   */
  public static ICloseableLogger createFileLogger( File aLogFile ) {
    return createFileLogger( aLogFile, true, true );
  }

  // ------------------------------------------------------------------------------------
  // Работа с логерами по умолчанию
  //

  /**
   * Возвращает логер по умолчанию.
   * <p>
   * Начальное значение (до изменения методом {@link #setDefaultLogger(ILogger)}) совпадает с {@link #stdoutLogger()}.
   *
   * @return ISkLogger - логер по умолчанию
   */
  public static ILogger defaultLogger() {
    return defaultLogger;
  }

  /**
   * Возвращает логер ошибок по умолчанию.
   * <p>
   * Начальное значение (до изменения методом {@link #setErrorLogger(ILogger)}) совпадает с {@link #stderrLogger()}.
   *
   * @return ISkLogger - логер ошибок по умолчанию
   */
  public static ILogger errorLogger() {
    return errorLogger;
  }

  /**
   * Устанавливает логер по умолчанию.
   *
   * @param aDefaultLogger ISkLogger - новый логер
   * @return ISkLogger - предыдущий логер по умолчанию
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ILogger setDefaultLogger( ILogger aDefaultLogger ) {
    TsNullArgumentRtException.checkNull( aDefaultLogger );
    ILogger tmp = defaultLogger;
    defaultLogger = aDefaultLogger;
    return tmp;
  }

  /**
   * Устанавливает логер ошибок по умолчанию.
   *
   * @param aErrorLogger ISkLogger - новый логер ошибок
   * @return ISkLogger - предыдущий логер ошибок по умолчанию
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ILogger setErrorLogger( ILogger aErrorLogger ) {
    TsNullArgumentRtException.checkNull( aErrorLogger );
    ILogger tmp = errorLogger;
    errorLogger = aErrorLogger;
    return tmp;
  }

  /**
   * Восстанавливает начальные значения для {@link #defaultLogger()} и {@link #errorLogger()}.
   */
  public static void restoreDefaultLoggers() {
    defaultLogger = stdoutLogger;
    errorLogger = stderrLogger;
  }

}
