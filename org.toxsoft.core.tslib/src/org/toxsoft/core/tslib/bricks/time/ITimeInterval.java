package org.toxsoft.core.tslib.bricks.time;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

import org.toxsoft.core.tslib.bricks.time.impl.TimeInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Поянтие "интервал времени" (реализован неизменным классом-значением {@link TimeInterval}).
 * <p>
 * В этой модели мы считаем, что все время имеет протяженность от {@link TimeUtils#MIN_TIMESTAMP} миллисекунд с 00:00:00
 * 01.01.1970 до {@link TimeUtils#MAX_TIMESTAMP} миллисекунд (обе границы включены). Поскольку это в точности
 * соответствует интервале чисел {@link Long}, то любая long метка времени всегда находится внутри "нашего времени".
 * <p>
 * Настоящее понятие интервала времени включает в себя метку начала {@link #startTime()} и окончания {@link #endTime()}.
 * Окончание интервала не может быть раньше начала. Метки начала и окончания входят в интервал. Таким образом,
 * минимальная длина интервала равна 1 миллисекунде (когда {@link #startTime()} = {@link #endTime()}).
 * <p>
 * Реализует интерфейс {@link Comparable}&lt;{@link ITimeInterval}&gt; сортируя по возрастанию сначала
 * {@link #startTime()}, а потом {@link #endTime()}.
 * <p>
 * <b>Внимание:</b> интерфейс не предназначен для раелизации пользователями. Единственная применимая реализация - final
 * класс {@link TimeInterval}. Тем не менее, интерфейс объявлен как для самодокументируемости понятия "интервал
 * времени", так и для использования константы {@link #NULL}, реализованной специальным внутренным классом.
 *
 * @author hazard157
 */
public interface ITimeInterval
    extends Comparable<ITimeInterval> {

  /**
   * Все время - от начала времен и до его окончания.
   * <p>
   * Все остальные интервал времени находятся внутри {@link #WHOLE}.
   */
  ITimeInterval WHOLE = new TimeInterval( TimeUtils.MIN_TIMESTAMP, TimeUtils.MAX_TIMESTAMP );

  /**
   * "Нулевой" (остутствующий, не определенный, не заданный) интервал времени.
   * <p>
   * Фактически, эта константа может, и должна использоваться вместо null. Все методы интерфейса {@link ITimeInterval}
   * этого экземпляра выбрасывают исключение {@link TsNullObjectErrorRtException}. Если есть подозрение, что полученная
   * сыылка на {@link ITimeInterval} может быть не опеределен, то сначала надо проверить ссылку простым сравнением
   * <code>interval != {@link ITimeInterval#NULL}</code>.
   * <p>
   * Безопсано можно использовать Object-методы этого класса {@link #equals(Object)}, {@link #hashCode()},
   * {@link #toString()}, а также {@link Comparable#compareTo(Object)}.
   */
  ITimeInterval NULL = new InternalNullTimeInterval();

  /**
   * Возвращает метку времени начала интервала.
   *
   * @return long - время начала интеравала (в миллисекундах с начала эпохи)
   */
  long startTime();

  /**
   * Возвращает метку времени окончания интервала.
   *
   * @return long - время окончания интеравала (в миллисекундах с начала эпохи)
   */
  long endTime();

  /**
   * Returns the interval duration as milliseconds.
   *
   * @return long - milliseconds duration, alwasy >= 1
   */
  long duration();

  /**
   * Returns staring time (inclusive) as {@link LocalDateTime}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to UTC {@link #startTime()}.
   *
   * @return {@link LocalDateTime} - start time of interval
   */
  LocalDateTime getStartDatetime();

  /**
   * Returns ending time (inclusive) as {@link LocalDateTime}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to UTC {@link #endTime()}.
   *
   * @return {@link LocalDateTime} - end time of interval
   */
  LocalDateTime getEndDatetime();

  /**
   * Returns the interval duration as {@link Duration}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to {@link #duration()}.
   *
   * @return {@link Duration} - the duration
   */
  Duration getDuration();

}

final class InternalNullTimeInterval
    implements ITimeInterval, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly deserializes {@link ITimeInterval#NULL} value.
   *
   * @return {@link ObjectStreamException} - {@link ITimeInterval#NULL}
   * @throws ObjectStreamException is declared by convention but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITimeInterval.NULL;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return ITimeInterval.class.getSimpleName() + ".NULL"; //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) { // отсекаем также и NULL
      return true;
    }
    if( aObj instanceof ITimeInterval that ) {
      return that.startTime() == this.startTime() && that.endTime() == this.endTime();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( ITimeInterval aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    // this class singleton NULL is equal to itself and is less than any other interval
    return aThat == this ? 0 : -1;
  }

  // ------------------------------------------------------------------------------------
  // ITimeInterval
  //

  @Override
  public long startTime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public long endTime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public long duration() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public LocalDateTime getStartDatetime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public LocalDateTime getEndDatetime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public Duration getDuration() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

}
